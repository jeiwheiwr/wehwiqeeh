package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApUserService {
    /**
     * app登录功能
     * @param dto
     * @return
     */

    public ResponseResult login(LoginDto dto);
}
