package net.yunyi.back.common.response;

public enum YunyiCommonEnum implements BaseErrorInfoInterface {
	// 数据操作错误定义
	SUCCESS(200, "success"), INVALID_REQUEST(400, "bad request"), BODY_NOT_MATCH(400, "invalid request format"), SIGNATURE_NOT_MATCH(401, "signature not match"), NOT_FOUND(404, "resource not found"), INTERNAL_SERVER_ERROR(500, "internal server error!"), SERVER_BUSY(503, "server is under strong traffic, please retry later"), NO_TOKEN(401, "No jwt token presents"), AUTH(401, "JWT AUTH ERROR"), AUTH_USER_NOT_FOUND(401, "JWT AUTH ERROR"),

	/* Article */
	ARTICLE_NOT_FOUND(10000, "the article not exists"), Unknown(400, "unknown");

	/**
	 * 错误码
	 */
	private int resultCode;

	/**
	 * 错误描述
	 */
	private String resultMsg;

	YunyiCommonEnum(int resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	@Override
	public int getResultCode() {
		return resultCode;
	}

	@Override
	public String getResultMsg() {
		return resultMsg;
	}

}