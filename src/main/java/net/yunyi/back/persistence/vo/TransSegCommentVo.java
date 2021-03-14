package net.yunyi.back.persistence.vo;

import lombok.Data;

@Data
public class TransSegCommentVo implements Comparable<TransSegCommentVo> {
	private int id;
	private int senderId;
	private String senderName;
	private String content;
	private int sendTime;
	private int floor;

	@Override
	public int compareTo(final TransSegCommentVo o) {
		return floor - o.floor;
	}
}
