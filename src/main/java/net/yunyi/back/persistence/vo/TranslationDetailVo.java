package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleSegTrans;
import net.yunyi.back.persistence.entity.ArticleTextSeg;
import net.yunyi.back.persistence.entity.TransStats;
import net.yunyi.back.persistence.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDetailVo {
	private Article article;
	private TransStats stats;
	private User uploader;
	private List<TransSegment> translations;

	@Data
	public static class TransSegment {
		private List<ArticleTextSeg> segments;
		private ArticleSegTrans trans;
	}

}
