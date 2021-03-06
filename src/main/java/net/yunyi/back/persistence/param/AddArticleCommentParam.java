package net.yunyi.back.persistence.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleCommentParam {
	@Min(value = 1, message = "id必须大于0")
	private int articleId;

	@NotBlank(message = "评论内容不能为空")
	private String content;

	private int refCommentId;

	private boolean hasRefComment;
}
