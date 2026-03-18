package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 自媒体图文内容信息表
 * </p>
 */
@Data
@TableName("wm_news")
public class WmNews implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 文章标题
     */
     @TableField(value = "title")
    private String title;

    /**
     * 图文内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 文章布局
     * 0无图文章
     * 1单图文章
     * 2多图文章
     */
     @TableField(value = "type")
    private Short type;

    /**
     * 图文频道ID
     */
     @TableField(value = "channel_id")
    private Integer channelId;

    /**
     * 文章标签
     */
     @TableField(value = "labels")
     private String labels;

    /**
     * 创建时间
     */
     @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 提交时间
     */
     @TableField(value = "submit_time")
    private Date submitTime;

    /**
     * 当前状态
     * 0草稿
     * 1提交（待审核）
     * 2审核失败
     * 3人工审核
     * 4人工审核通过
     * 8审核通过（待发布）
     * 9
     */
     @TableField(value = "status")
    private Short status;

    /**
     *定时发布时间，不定时则为空
     */
     @TableField(value = "publish_time")
    private Date publishTime;

    /**
     * 拒绝理由
     */
     @TableField(value = "reason")
    private String Reason;

    /**
     * 发布库文章ID
     */
     @TableField(value = "article_id")
    private Long articleId;

    /**
     * 图片用逗号隔开
     */
     @TableField(value = "images")
    private String images;

    @TableField("enable")
    private Short enable;

    /**
     * 状态枚举类
     */
    @Alias("WmNewsStatus")
    public enum Status {
        NORMAL((short) 0), SUBMIT((short) 1), FAIL((short) 2), ADMIN_AUTH((short) 3), ADMIN_SUCCESS((short) 4), SUCCESS((short) 8), PUBLISHED((short) 9);
        short code;

        Status(short code) {
            this.code = code;
        }

        public short getCode() {
            return this.code;
        }
    }
}
