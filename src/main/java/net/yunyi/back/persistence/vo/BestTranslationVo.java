package net.yunyi.back.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestTranslationVo {
	private String content;
	private String uploaderName;
	private String uploaderId;
	private int likeNumber;
	private int transId;
}
