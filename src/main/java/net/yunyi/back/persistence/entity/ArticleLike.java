package net.yunyi.back.persistence.entity;

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
public class ArticleLike implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer articleId;

    private Integer userId;


}
