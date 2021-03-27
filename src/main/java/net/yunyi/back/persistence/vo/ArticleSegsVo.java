package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.yunyi.back.persistence.entity.ArticleTextSeg;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleSegsVo {
	String title;
	List<ArticleTextSeg> segments;
}
