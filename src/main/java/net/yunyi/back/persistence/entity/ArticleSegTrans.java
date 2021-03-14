package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 原文切分的翻译表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleSegTrans implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 翻译id
	 */
	private Integer transId;

	/**
	 * 切分id
	 */

	private String refIds;

	/**
	 * 翻译序号
	 */
	private Integer transSeq;

	/**
	 * 翻译内容
	 */
	private String content;


}
