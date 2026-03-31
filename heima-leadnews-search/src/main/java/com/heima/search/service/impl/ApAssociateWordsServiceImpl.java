package com.heima.search.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.pojos.ApAssociateWords;
import com.heima.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关键词联想实现类
 * @author: xiaocai
 */
@Service
@Slf4j
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 关键词联想
     * @param userSearchDto
     * @return
     */
    @Override
    public ResponseResult findAssociate(UserSearchDto userSearchDto) {
        //1.参数检查
        if(userSearchDto == null || StringUtils.isBlank(userSearchDto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页检查
        if(userSearchDto.getPageSize()>20){
            userSearchDto.setPageSize(20);
        }

        //3.执行查询，模糊查询
        Query query = Query.query(Criteria.where("associateWords").regex(".*?\\" + userSearchDto.getSearchWords() + ".*"));
        query.limit(userSearchDto.getPageSize());
        List<ApAssociateWords> list = mongoTemplate.find(query, ApAssociateWords.class);
        return ResponseResult.okResult(list);
    }
}