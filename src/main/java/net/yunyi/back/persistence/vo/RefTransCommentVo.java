package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefTransCommentVo {
	private int id;
	private int senderId;
	private String senderName;
	private String content;
	private int sendTime;
	private int floor;
	private boolean hasRefComment;
	private int refCommentId;
}
