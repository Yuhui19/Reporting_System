package com.antra.report.client;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static io.restassured.RestAssured.when;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestApi {
    @Value("http://localhost:${local.server.port}")
    private String REST_SERVICE_URI ;

    @Test
    public void getNoneExistingReportByID(){
        when().
                get(REST_SERVICE_URI + "/report/1").peek().
                then().assertThat()
                .statusCode(404)
                .body("errorCode", Matchers.equalTo(404));
    }

    @Test
    public void tryToSendPostRequestToFindReportById(){
        when().
                post(REST_SERVICE_URI + "/report/1").peek().
                then().assertThat()
                .statusCode(405);
    }

//    @Test
//    public void listAllUsers(){
//       when().get(REST_SERVICE_URI + "/report").peek().then()
//                .statusCode(200)
//                .body("body.size()", Matchers.greaterThan(0));
//    }

}
