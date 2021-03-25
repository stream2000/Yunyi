package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleTranslationVo {
	private SimpleTranslationVo bestTranslation;
	private List<SimpleTranslationVo> translations;
	private int transCount;
}
