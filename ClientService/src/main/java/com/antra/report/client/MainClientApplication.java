package com.antra.report.client;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableEurekaClient
public class MainClientApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainClientApplication.class);

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(
            AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        LOGGER.info(globalProp);
//        return new RestTemplate();
//    }
//
//    @Value("${com.antra.report.globalProp}")
//    private String globalProp;

    public static void main(String[] args) {
        SpringApplication.run(MainClientApplication.class, args);
    }

}
