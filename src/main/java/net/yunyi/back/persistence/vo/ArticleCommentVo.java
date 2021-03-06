package net.yunyi.back.persistence.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArticleCommentVo {
	private int id;
	private int sender_id;
	private String senderName;
	private String content;
	private Timestamp sendTime;
	private int floor;
	private boolean hasRefComment;
	private int refCommentId;
	private RefArticleComment refComment;
}
