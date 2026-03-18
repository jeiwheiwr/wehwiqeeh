package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *频道信息表
 * </p>
 * @author itheima
 */
@Data
@TableName("wm_channel")
public class WmChannel implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    @TableField("name")
    private String name;

    /**
     * 频道描述
     */
     @TableField("description")
    private String description;

    /**
     * 频道状态 0-禁用，1-启用
     */
     @TableField("status")
    private Boolean status;

    /**
     * 默认排序
     */
     @TableField("ord")
    private Integer ord;

    /**
     * 创建时间
     */
     @TableField("created_time")
    private LocalDateTime createdTime;
}
