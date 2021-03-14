package net.yunyi.back.persistence.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AddTranslationSegCommentParam {
	@Min(value = 1, message = "id必须大于0")
	private int transSegId;

	@NotBlank(message = "评论内容不能为空")
	private String content;
}
