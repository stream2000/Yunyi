package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author stream2000
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleComment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 评论唯一自增id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 楼层数
	 */
	private Integer floor;

	/**
	 * 发送者id
	 */
	private Integer senderId;

	/**
	 * 评论文章id
	 */
	private Integer articleId;

	/**
	 * 是否引用评论
	 */
	private Boolean hasRefComment;

	/**
	 * 评论引用的评论id
	 */
	private Integer refCommentId;

	/**
	 * 评论内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT) //新增有效
	private Date createTime;

}
