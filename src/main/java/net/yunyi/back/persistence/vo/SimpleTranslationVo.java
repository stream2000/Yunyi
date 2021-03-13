package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTranslationVo {
	private String content;
	private String uploaderName;
	private String uploaderId;
	private int likeNum;
	private int transId;
}
