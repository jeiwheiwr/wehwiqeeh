package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmSensitive;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;


    /**
     * 计算热点文章
     */
    @Override
    public void computeHotArticle() {
        //1.查询前5天的文章数据
        Date dataParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> articleList = apArticleMapper.findArticleListByLast5days(dataParam);

        //2.计算文章分值
        List<HotArticleVo> hotArticleList = computeHotArticle(articleList);

        //3.为每个频道缓存30条分支较高的文章
        cacheTagToRedis(hotArticleList);


    }


    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private CacheService cacheService;

    /**
     * 为每个频道缓存30条分值较高的文章
     * @param hotArticleList
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleList) {
        //为每个频道缓存30条分值较高的文章
        ResponseResult result = wemediaClient.getChannels();
        if(result.getCode().equals(200)){
            String channelJson= JSON.toJSONString(result.getData());
            List<WmChannel> channelList = JSON.parseArray(channelJson, WmChannel.class);
            //检索出每个频道的文章
            if (channelList != null && channelList.size() > 0){
                for (WmChannel wmChannel : channelList){
                    List<HotArticleVo> articles = hotArticleList.stream().filter(apArticle -> apArticle.getChannelId().equals(wmChannel.getId())).collect(Collectors.toList());
                    sortAndCache(articles, ArticleConstants.HOT_ARTICLE_FIRST_PAGE+wmChannel.getId());
                }
            }
        }
        //设置推荐数据
        //给文章进行排序，取30条分值较高的文章存入redis、key、频道id、value：30条分值较高的文章
        sortAndCache(hotArticleList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 给文章进行排序，取30条分值较高的文章存入redis、key、频道id、value：30条分值较高的文章
     * @param key
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVos, String key) {
        hotArticleVos = hotArticleVos.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
        if (hotArticleVos.size() > 30) {
            hotArticleVos = hotArticleVos.subList(0, 30);
        }
        cacheService.set(key, JSON.toJSONString(hotArticleVos));
    }


    /**
     * 计算文章分值
     * @param apArticleList
     * @return
     */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList) {
        List<HotArticleVo> hotArticleList = new ArrayList<>();
        if (apArticleList != null && apArticleList.size() > 0){
            for (ApArticle apArticle : apArticleList){
                HotArticleVo hotArticleVo = new HotArticleVo();
                BeanUtils.copyProperties(apArticle,hotArticleVo);
                Integer score = computeScore(apArticle);
                hotArticleVo.setScore(score);
                hotArticleList.add(hotArticleVo);
            }
        }
        return hotArticleList;
    }


    /**
     * 计算文章的具体分值
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer score = 0;
        if (apArticle.getLikes() != null){
            score += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if (apArticle.getCollection() != null){
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        if (apArticle.getComment() != null){
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if (apArticle.getViews() != null){
            score += apArticle.getViews();
        }
        return score;
    }


}