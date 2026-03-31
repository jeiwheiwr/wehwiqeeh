package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;
import com.heima.model.wemedia.pojos.WmNews;

public interface ArticleFreemarkerService {
    /**
     * 生成静态文章上传到minIO中
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinIO(ApArticle apArticle, String content);
}
