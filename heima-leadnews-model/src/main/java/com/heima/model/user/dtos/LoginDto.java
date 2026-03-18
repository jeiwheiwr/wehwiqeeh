package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Data
public class LoginDto {
    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号",required=true)
    public String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value ="密码", required = true)
    private String password;
}
