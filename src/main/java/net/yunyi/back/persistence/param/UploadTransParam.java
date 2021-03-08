package net.yunyi.back.persistence.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UploadTransParam {

	private int articleId;
	private List<TransSegment> segmentList;

	@Data
	public static class TransSegment {
		private List<Integer> refSegIds;
		@NotEmpty
		private String translationContent;
	}
}
