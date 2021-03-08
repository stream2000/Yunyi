package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 单句评论表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TransItemComment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 翻译评论唯一自增id
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
	private Integer transSegId;

	/**
	 * 评论内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	private LocalDateTime createTime;


}
