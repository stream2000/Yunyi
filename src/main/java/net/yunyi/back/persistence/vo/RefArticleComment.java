package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class RefArticleComment {
	private int id;
	private int senderId;
	private String senderName;
	private String content;
	private Timestamp sendTime;
	private int floor;
	private boolean hasRefComment;
	private int refCommentId;
}
