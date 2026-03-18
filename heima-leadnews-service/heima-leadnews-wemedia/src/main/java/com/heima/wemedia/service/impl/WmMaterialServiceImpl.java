package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    private  FileStorageService fileStorageService;
    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("",fileName+postfix,multipartFile.getInputStream());
            log.info("上传图片到MinIo成功,fileId:{}",fileId);

        }
        catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl.uploadPicture 上传图片到MinIo失败,fileId:{}",fileId);
        }

        //3.保存到数据库中
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(fileId);
        wmMaterial.setType(Short.valueOf("0"));
        wmMaterial.setIsCollect(Short.valueOf("0"));
        wmMaterial.setCreatedTime(LocalDateTime.now());
        save(wmMaterial);

        //4.返回结果
        return ResponseResult.okResult(wmMaterial);
    }


    /**
     * 素材列表查询
     * @param wmMaterialDto
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDto wmMaterialDto) {
        //1.检查参数
        wmMaterialDto.checkParam();

        //2.分页查询
        IPage page = new Page<>(wmMaterialDto.getPage(),wmMaterialDto.getSize());
        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper<>();
        if (wmMaterialDto.getIsCollection() != null && wmMaterialDto.getIsCollection() == 1) {
            queryWrapper.eq(WmMaterial::getIsCollect,wmMaterialDto.getIsCollection());
        }

        //按照用户查询
        queryWrapper.eq(WmMaterial::getId, WmThreadLocalUtil.getUser().getId());


        //按照时间倒序
        queryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        page = page(page, queryWrapper);

        //3.结果返回
        ResponseResult responseResult = new ResponseResult(wmMaterialDto.getPage(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;


    }
}