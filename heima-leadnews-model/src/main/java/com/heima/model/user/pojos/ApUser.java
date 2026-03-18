package com.heima.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ap_user")
public class ApUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    *主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
    * 密码、通信等加密盐
    */
    @TableField("salt")
    private String salt;

    /**
    * 用户名
    */
    @TableField("name")
    private String name;

    /**
     *密码，md5加密
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     *头像
     */
    @TableField("image")
    private String image;

    /**
     * 0男
     * 1女
     * 2未知
     */
    @TableField("sex")
    private Boolean sex;

    /**
     *是否获得证书
     */
    @TableField("is_certification")
    private Boolean certification;

    /**
     *是否授权
     */
    @TableField("is_identity_authentication")
    private Boolean identityAuthentication;

    /**
     *状态
     */
    @TableField("status")
    private Boolean status;

    /**
     *
     */
    @TableField("flag")
    private Short flag;

    /**
     *创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
