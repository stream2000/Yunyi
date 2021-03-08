package net.yunyi.back.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 翻译统计表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TransStats implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer transId;

	private Integer likeNum;

	private Integer commentNum;


}
