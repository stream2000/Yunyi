package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TransSegCommentPageVo {
	List<TransSegCommentVo> comments;
	int pageCount;
}
