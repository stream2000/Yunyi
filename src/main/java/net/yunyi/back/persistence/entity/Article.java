package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文章主键，唯一自增id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 上传者
	 */
	private Integer uploaderId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 翻译后的标题
	 */
	private String transTitle;

	/**
	 * 原文
	 */
	private String originalText;

	/**
	 * 类别
	 */
	private String genre;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 最后一次修改时间
	 */
	private LocalDateTime updateTime;


}
