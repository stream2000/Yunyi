package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListItemVo {

	private int id;
	private String name;
	private int uploaderId;
	private String title;
	private String transTitle;
	private String originalText;
	private String genre;
	private boolean hasTrans;
	private int createTime;
	private int updateTime;
	private int likeNum;
	private int viewNum;
	private int commentNum;
	private int transRequestNum;
	private SimpleTranslationVo bestTranslation;
	private List<SimpleTranslationVo> translations;
}
