package net.yunyi.back.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 翻译点赞表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TransSegLike implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer transSegId;

    private Integer userId;


}
