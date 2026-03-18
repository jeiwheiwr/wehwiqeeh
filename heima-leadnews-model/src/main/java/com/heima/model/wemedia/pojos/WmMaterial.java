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
 * 自媒体素材表
 * </p>
 */
@Data
@TableName("wm_material")
public class WmMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 图片地址
     */
    @TableField("url")
    private String url;

    /**
     * 素材类型
     * 0图片
     * 1视频
     */
     @TableField("type")
     private Short type;

    /**
     * 是否收藏
     */
     @TableField("is_collect")
     private Short isCollect;

    /**
     * 创建时间
     */
     @TableField("created_time")
     private LocalDateTime createdTime;

}
