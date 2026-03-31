package com.heima.article.listener;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.HotArticleConstants;
import com.heima.model.article.mess.ArticleVisitStreamMess;
import com.heima.model.article.pojos.ApArticle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleIncrHandleListener {
    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String message){
        if (StringUtils.isNotBlank(message)){
            ArticleVisitStreamMess mess = JSON.parseObject(message, ArticleVisitStreamMess.class);
            apArticleService.updateScore(mess);
        }
    }
}
