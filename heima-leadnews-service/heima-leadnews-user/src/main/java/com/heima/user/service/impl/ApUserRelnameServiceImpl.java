package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.common.constants.UserConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserRelnameService;
import io.swagger.models.auth.In;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jnlp.IntegrationService;

public class ApUserRelnameServiceImpl implements ApUserRelnameService {
    /**
     * 按照状态分页查询用户列表
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        //1.检查参数
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //分页检查
        dto.checkParam();

        //2.分页根据状态精确查询
        IPage page = new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dto.getStatus()!=null){
            lambdaQueryWrapper.eq(ApUserRealname::getStatus,dto.getStatus());
        }
        page=page(page,lambdaQueryWrapper);

        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;

    }




    /**
     * 2 审核失败  9审核成功
     * @param dto
     * @param Status
     * @return
     */
    @Override
    public ResponseResult updateStatus(AuthDto dto, Short Status) {
        //1.检查参数
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.修改认证状态
        ApUserRealname apUserRealname = new ApUserRealname();
        apUserRealname.setId(dto.getId());
        apUserRealname.setStatus(Status);
        if (StringUtils.isNotBlank(dto.getMsg())){
            apUserRealname.setReason(dto.getMsg());
        }
        updateById(apUserRealname);
        //3.如果审核状态是9，就是成功，需要创建自媒体账户
        if (Status.equals(UserConstants.PASS_AUTH)){
            ResponseResult responseResult = createWmUserAndAuthor(dto);
            if (responseResult!=null)
                return responseResult;
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
  }

  @Autowired
  private IWemediaClient wemediaClient;

   @Autowired
   private ApUserMapper apUserMapper;

    /**
     * 创建自媒体账户
     * @param dto
     * @return
     */

 public ResponseResult createWmUserAndAuthor(AuthDto dto){
     Integer userRelnameId = dto.getId();
     //查询用户认证信息
     ApUserRealname apUserRealname = getById(userRelnameId);
     if (apUserRealname==null){
         return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
     }
     //查询app端用户信息
     Integer userId = apUserRealname.getUserId();
     ApUser apUser = apUserMapper.selectById(userId);
     if (apUser==null){
         return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
     }
     //创建自媒体账户
     WmUser wmUser = wemediaClient.findWmUserByName(apUser.getName());
     if (wmUser==null){
         wmUser = new WmUser();
         wmUser.setApUserId(apUser.getId());
         wmUser.setName(apUser.getName());
         wmUser.setPassword(DigestUtils.md5Hex(apUser.getPassword()));
         wmUser.setPhone(apUser.getPhone());
         wmUser.setCreatedTime(apUser.getCreatedTime());
         wmUser.setStatus(9);
         wmUser.setSalt( apUser.getSalt());
         wemediaClient.saveWmUser(wmUser);
     }
     apUser.setFlag((short)1);
     apUserMapper.updateById(apUser);
     return null;
  }
}