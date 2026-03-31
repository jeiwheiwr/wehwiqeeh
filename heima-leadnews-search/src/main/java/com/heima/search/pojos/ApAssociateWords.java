package com.heima.search.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     关联搜索
 * </p>
 * @author: xiaocai
 */
@Data
@Document(collection = "ap_associate_words")
public class ApAssociateWords implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 关联词
     */
    private String associateWords;

    /**
     * 创建时间
     */
    private Date createdTime;
}
