package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 自媒体图文引用素材信息表
 * </p>
 */
@Data
@TableName("wm_news_material")
public class WmNewsMaterial implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
     private Integer id;

    /**
     * 素材ID
     */
    @TableField("material_id")
    private Integer materialId;

    /**
     * 图文ID
     */
    @TableField("news_id")
    private Integer newsId;

    /**
     * 引用类型
     * 0 内容引用
     * 1主图引用
     */
     @TableField("type")
     private Short type;


    /**
     * 引用数字
     */
     @TableField("ord")
     private Short ord;

}
