package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author stream2000
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "article_id", type = IdType.INPUT)
    private Integer articleId;

    private Integer viewNum;

    private Integer likeNum;

    private Integer commentNum;

    private Integer transRequestNum;


}
