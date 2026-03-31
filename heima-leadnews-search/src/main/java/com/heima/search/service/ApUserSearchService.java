package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {
    /**
     *保存用户搜索记录
     */
    public void insert(String keyword,Integer userId);

    /**
     * 查询用户搜索记录
     */
    public ResponseResult findUserSearch();

    /**
     * 删除用户搜索记录
     */
    public ResponseResult delUserSearch(HistorySearchDto dto);
}
