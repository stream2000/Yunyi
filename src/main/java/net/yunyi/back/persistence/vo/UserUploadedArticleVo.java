package net.yunyi.back.persistence.vo;

import lombok.Data;

@Data
public class UserUploadedArticleVo {
	private int id;
	private String genre;
	private boolean hasTrans;
	private String title;
	private int commentNum;
}
