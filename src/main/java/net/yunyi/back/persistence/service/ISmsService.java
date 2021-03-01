package net.yunyi.back.persistence.service;

public interface ISmsService {

	String sendCaptcha(String phone);

	String checkCaptcha(String requestId, String captcha);
}
