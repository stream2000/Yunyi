package net.yunyi.back.persistence.vo;

import lombok.Data;

@Data
public class TransSegCommentVo {
	private int id;
	private int senderId;
	private String senderName;
	private String content;
	private int sendTime;
	private int floor;
}
