package net.yunyi.back.persistence.vo;

import lombok.Data;

@Data
public class TransCommentVo implements Comparable<TransCommentVo> {
	private int id;
	private int senderId;
	private String senderName;
	private String content;
	private int sendTime;
	private int floor;
	private boolean hasRefComment;
	private int refCommentId;
	private RefTransCommentVo refComment;

	@Override
	public int compareTo(final TransCommentVo o) {
		return this.getFloor() - o.getFloor();
	}
}
