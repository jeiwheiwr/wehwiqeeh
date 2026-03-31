package com.heima.model.behavior.dtos;


import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UnLikesBehaviorDto {
    //文章ID
    @IdEncrypt
    Long articleId;

    /**
     * 不喜欢操作方式
     * 0不喜欢
     * 1取消不喜欢
     */
    Short type;
}
