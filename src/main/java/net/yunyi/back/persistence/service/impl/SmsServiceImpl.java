package net.yunyi.back.persistence.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import java.util.HashMap;
import java.util.Map;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.auth.SendSmsUtil;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.common.util.GenerationSequenceUtil;
import net.yunyi.back.persistence.service.IRedisService;
import net.yunyi.back.persistence.service.ISmsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements ISmsService {

    Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Autowired
    IRedisService redisService;

    @Override
    public String sendCaptcha(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BizException(YunyiCommonEnum.INVALID_REQUEST.getResultCode(),
                "phone number should not be empty");
        }
        String randNum = GenerationSequenceUtil.getRandNum(4);
        logger.info("phone number : {} captcha : {}", phone, randNum);
        try {
            SendSmsResponse response = SendSmsUtil.sendSms(phone, randNum);
            if (response.getSendStatusSet().length != 1) {
                logger.error("unexpected response set length: expect 1 get {}",
                    response.getSendStatusSet().length);
                throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),
                    "error when sending sms by tencent cloud");
            }
            SendStatus status = response.getSendStatusSet()[0];
            if (!status.getCode().equalsIgnoreCase("Ok")) {
                throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),
                    "fail sending sms by tencent cloud");
            }
            Map<String, Object> value = new HashMap<>();
            value.put("phone", phone);
            value.put("code", randNum);
            String valueString = JSONObject.toJSONString(value);
            redisService.set(response.getRequestId(), valueString);
            redisService.expire(response.getRequestId(), 60);
            return response.getRequestId();
        } catch (Exception e) {
            throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),
                e.getMessage());
        }
    }

    @Override
    public String checkCaptcha(String requestId, String captcha) {
        logger.info("request id: {} captcha: {} ", requestId, captcha);
        if (StringUtils.isBlank(requestId) || StringUtils.isBlank(captcha)) {
            logger.error("param is blank");
            throw new BizException(YunyiCommonEnum.AUTH.getResultCode(), "param is blank");
        }
        try {
            String resultMap = redisService.get(requestId);
            if (resultMap == null) {
                return null;
            }
            Map<String, Object> map = JSON.parseObject(resultMap);
            String code = (String) map.get("code");
            if (!code.equalsIgnoreCase(captcha)) {
                return null;
            }
            String phone = (String) map.get("phone");
            if (StringUtils.isBlank(phone)) {
                throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),
                    "error parse json, phone is null");
            }
            return phone;
        } catch (Exception e) {
            throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),
                e.getMessage());
        }
    }
}
