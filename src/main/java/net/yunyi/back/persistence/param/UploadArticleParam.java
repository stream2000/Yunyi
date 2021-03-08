package net.yunyi.back.persistence.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class UploadArticleParam {
	@Valid
	@NotEmpty(message = "翻译切分不能为空")
	@Size(min = 1, message = "至少要有一个分段")
	private List<String> segments;

	@NotBlank(message = "标题不能为空")
	private String title;

	@NotBlank(message = "类别不能为空")
	private String genre;

	@NotEmpty(message = "必须上传原文")
	private String originalText;

	private int articleId;

	private String transTitle;
}
