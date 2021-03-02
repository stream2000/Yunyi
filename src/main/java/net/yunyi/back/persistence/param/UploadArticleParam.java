package net.yunyi.back.persistence.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UploadArticleParam {
	@NotBlank(message = "标题不能为空")
	private String title;
	@NotBlank(message = "类别不能为空")
	private String genre;
	@NotEmpty(message = "必须上传原文")
	private String originalText;

	private int articleId;
	private String transTitle;
}
