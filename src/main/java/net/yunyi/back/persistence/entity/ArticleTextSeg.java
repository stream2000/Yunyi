package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 原文切分表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleTextSeg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 原文id
     */
    private Integer articleId;

    /**
     * 切分序号
     */
    private Integer sequenceNumber;

    /**
     * 切分内容
     */
    private String content;


}
