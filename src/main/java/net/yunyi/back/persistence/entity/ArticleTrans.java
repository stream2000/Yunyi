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
 * @since 2021-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleTrans implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 上传者id
	 */
	private Integer uploaderId;

	/**
	 * 对应原文id
	 */
	private Integer articleId;

	/**
	 * 对应原文id
	 */
	private String transTitle;

}
