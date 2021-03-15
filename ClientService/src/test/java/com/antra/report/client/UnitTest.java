package com.antra.report.client;

import com.antra.report.client.entity.ReportRequestEntity;
import com.antra.report.client.pojo.reponse.GeneralResponse;
import com.antra.report.client.pojo.reponse.ReportVO;
import com.antra.report.client.pojo.request.ReportRequest;
import com.antra.report.client.service.ReportService;
import com.antra.report.client.controller.ReportController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.*;

public class UnitTest {

    @Mock
    private ReportService reportService;

    @Before
    public void configMock() {
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new ReportController(reportService));
//        Mockito.when(messages.getMessage(anyObject())).thenReturn("Mocked Message");
    }

    @Test
    public void createUser(){
        ReportRequest request = new ReportRequest();
        request.setHeaders(Collections.singletonList("header1"));
        request.setDescription("unit test");
        request.setSubmitter("tester");
        request.setData(Arrays.asList(Arrays.asList("data2","data3","data4"), Arrays.asList("data1","data8","data9")));
//        ReportRequestEntity entity = new ReportRequestEntity();
//        entity.setDescription("unit test");
//        entity.setSubmitter("tester");
//        Mockito.when(reportService.generateReportsSync(anyObject())).thenReturn(new ReportVO(entity));

        given().accept("application/json").contentType("application/json").body(request).post("/report/sync").peek().
                then().assertThat()
                .statusCode(201);
    }

    @Test
    public void testGetUserFromDB() {
        ReportRequestEntity entity = new ReportRequestEntity();
        entity.setReqId("1");
        entity.setSubmitter("Tester");
        entity.setDescription("Unit test");
        Mockito.when(reportService.getReportById(anyString())).thenReturn(new ReportVO(entity));
        given().accept("application/json").get("/report/1").peek().
                then().assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetUserFromEmptyDB(){
        Mockito.when(reportService.getReportById(anyString())).thenReturn(new ReportVO());
        given().accept("application/json").get("/report/1").peek().
                then().assertThat()
                .statusCode(404);
    }

    @Test
    public void createUserButExceptionRaised(){
        ReportRequest request = new ReportRequest();
        request.setHeaders(Collections.singletonList("header1"));
        request.setDescription("unit test");
        request.setSubmitter("tester");
        request.setData(Arrays.asList(Arrays.asList("data2","data3","data4"), Arrays.asList("data1","data8","data9")));
        Mockito.when(reportService.generateReportsSync(anyObject())).thenThrow(new RuntimeException("dummy error"));
        given().accept("application/json").contentType("application/json").body(request).post("/report/sync").peek().
                then().assertThat()
                .statusCode(400);
    }
}
