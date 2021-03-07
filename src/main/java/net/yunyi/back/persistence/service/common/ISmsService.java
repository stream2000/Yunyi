package net.yunyi.back.persistence.service.common;

public interface ISmsService {

	String sendCaptcha(String phone);

	String checkCaptcha(String requestId, String captcha);
}
