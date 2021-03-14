package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleCommentPageVo {
	List<ArticleCommentVo> comments;
	int pageCount;
}
