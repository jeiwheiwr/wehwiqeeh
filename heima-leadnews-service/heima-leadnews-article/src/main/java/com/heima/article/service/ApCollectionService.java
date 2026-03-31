package com.heima.article.service;

import com.heima.model.article.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApCollectionService {
    /**
     * 保存收藏信息
     * @param dto
     * @return
     */
    public ResponseResult collection(CollectionBehaviorDto dto);
}
