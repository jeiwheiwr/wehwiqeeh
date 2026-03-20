package com.heima.wemedia.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;

    /**
     * 自媒体文章审核
     *
     * @param id
     */
    @Override
    public void autoScan(Integer id) {
        //1.查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            throw new RuntimeException("WmNewsAutoScanServiceImpl文章不存在");
        }
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
            //从内容中提取纯文本内容和图片
            Map<String, Object> map = handleTextAndImages(wmNews);

            //2.审核文本内容，阿里云接口
            boolean isTextScan = handleTextScan((String) map.get("content"), wmNews);
            if (!isTextScan)
                return;

            //3.审核图片，阿里云接口
            boolean isImageScan = handleImageScan((List<String>) map.get("images"), wmNews);
            if (!isImageScan)
                return;

            //4.审核成功，保存app端相关的文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);
            if (responseResult.getCode() != 200) {
                throw new RuntimeException("WmNewsAutoScanServiceImpl保存app端文章文章失败");
            }
            //回填article_id
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews, (short) 9, "审核成功");

        }
    }

    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;


    /**
     * 保存app端文章数据
     *
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews, dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if (wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }
        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            dto.setAuthorName(wmUser.getName());

        }
        //设置文章id
        if (wmNews.getArticleId() != null) {
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());
        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;

    }

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GreenImageScan greenImageScan;


    /**
     * 审核图片
     *
     * @param images 图片列表
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = true;
        if (images == null || images.isEmpty()) {
            return flag;
        }
        //下载图片minIO
        //图片去重
        images = images.stream().distinct().collect(Collectors.toList());

        List<byte[]> imageList = new ArrayList<>();
        for (String image : images) {
            byte[] bytes = fileStorageService.downLoadFile(image);
            imageList.add(bytes);
        }

        //审核图片
        try {
            Map map = greenImageScan.imageScan(imageList);
            if (map != null) {
                //审核失败
                if (map.get("suggestion").equals("block")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "当前文章中存在违规内容");

                }

                //不确定西 需要人工审核
                if (map.get("suggestion").equals("review")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;

    }

    @Autowired
    private GreenTextScan greenTextScan;


    /**
     * 审核纯文本内容
     *
     * @param content 纯文本内容
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) {
        boolean flag = true;

        if ((wmNews.getTitle() + "-" + content).length() == 0) {
            return flag;
        }

        try {
            Map map = greenTextScan.greeTextScan((wmNews.getTitle() + "-" + content));
            if (map != null) {
                //审核失败
                if (map.get("suggestion").equals("block")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "当前文章中存在违规内容");
                }

                //不确定信息  需要人工审核
                if (map.get("suggestion").equals("review")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 修改文章内容
     * @param wmNews
     * @param reason
     * @param status
     */
    private void updateWmNews(WmNews wmNews, Short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 1.从自媒体文章的内容中提取文本和图片
     * 2.提取文章的封面图片
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        //存储纯文本内容
        StringBuilder sb = new StringBuilder();
        List<String> images = new ArrayList<>();

        //1.从自媒体文章的内容中提取文本和图片
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps= JSON.parseArray(wmNews.getContent(),Map.class);
            for(Map map:maps){
                if(map.get("type").equals("text")){
                    sb.append(map.get("value").toString());
                }
                if(map.get("type").equals("image")){
                    images.add(map.get("value").toString());
                }
            }
        }

        //2.提取文章的封面图片
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }
        Map<String, Object> resultmap = new HashMap<>();
        resultmap.put("images", images);
        resultmap.put("content", sb.toString());
        return resultmap;
    }
}
