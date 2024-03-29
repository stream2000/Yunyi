package net.yunyi.back.persistence.vo;

import lombok.Data;

@Data
public class ArticleCommentVo implements Comparable<ArticleCommentVo> {
	private int id;
	private int sender_id;
	private String senderName;
	private String content;
	private int sendTime;
	private int floor;
	private boolean hasRefComment;
	private int refCommentId;
	private RefArticleComment refComment;

	@Override
	public int compareTo(final ArticleCommentVo o) {
		return this.getFloor() - o.getFloor();
	}
}
