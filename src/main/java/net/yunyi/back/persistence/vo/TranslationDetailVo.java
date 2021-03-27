package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yunyi.back.persistence.entity.ArticleSegTrans;
import net.yunyi.back.persistence.entity.ArticleTextSeg;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDetailVo {
	private SimpleTranslationVo trans;
	private ArticleListItemVo article;
	private List<TransSegment> translations;

	@Data
	public static class TransSegment {
		private List<ArticleTextSeg> segments;
		private ArticleSegTrans trans;
	}

}
