package com.antra.report.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSendEmail {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;


    @Test
    public void testSendingEmail() {
        Map<String, String> message = new HashMap<>();
        message.put("to", "yuhuizhang1995@gmail.com");
//        message.put("from", "do_not_reply@antra.com");
        message.put("from", "yuhui");
        message.put("subject", "Test Email");
        message.put("body", "I did it from Antra1");
        message.put("token", "12345");
        queueMessagingTemplate.convertAndSend("email_queue", message);
    }
}
