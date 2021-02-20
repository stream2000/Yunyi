package net.yunyi.back.common.auth;

import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class SendSmsUtilTest {

    @Test
    void sendSms() {
        String phone = "+8617721260791";
        try {
            SendSmsResponse response = SendSmsUtil.sendSms(phone, "1234");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}