package com.volvo.congestiontax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.volvo.congestiontax.model.CongestionTaxCalculateResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaxCalculatorApplicationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    

    //Testing the free time entry - 2013-01-14 21:00:00
    @Test
    public void testFreeTime() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-14 21:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the Public holiday time entry - 2013-01-01
    @Test
    public void testHoliday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-01 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
  //Testing the Sunday time entry - 2013-01-06 SUNDAY
    @Test
    public void testSunday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-06 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the Saturday time entry - 2013-01-05 Saturday
    @Test
    public void testSaturday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-05 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the workingday time entry - 2013-01-07 10:00:00 Saturday
    @Test
    public void testWorkingday10AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(8, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the workingday time entry - 2013-01-07 08:30:00 Saturday
    @Test
    public void testWorkingday0830AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 08:30:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(8, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the workingday time entry - 2013-01-07 16:59:00 Saturday
    @Test
    public void testWorkingday1659AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:59:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the workingday time entry - 2013-01-07 16:59:59 Saturday
    @Test
    public void testWorkingday165959AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the two entries in 60min 2013-01-07 16:30:59, 2013-01-07 16:59:59
    @Test
    public void testTwoEntriesIn60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:30:59\",\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the two entries in 60min 2013-01-07 15:30:59, 2013-01-07 16:59:59
    @Test
    public void testTwoEntriesNotIn60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 15:30:59\",\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(36, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing The maximum amount 60SEK
    // 2013-01-07 15:30:59 - 18 SEK
    // 2013-01-07 16:59:59 - 18 SEK  36
    // 2013-01-08 15:30:59 - 18 SEK
    // 2013-01-08 16:59:59 - 18 SEK36
    // 2013-01-09 15:30:59 - 18 SEK
    // 2013-01-09 16:59:59 - 18 SEK36
    // Total is : 108
    
    @Test
    public void testTotalFeeMultipleDays() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": ["
                + "\"2013-01-07 15:30:59\",\"2013-01-07 16:59:59\","
                + "\"2013-01-08 15:30:59\",\"2013-01-08 16:59:59\","
                + "\"2013-01-09 15:30:59\",\"2013-01-09 16:59:59\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(108, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    //2013-01-07 07:00:59   =18
    //2013-01-07 08:11:59   =13
	//2013-01-07 15:30:59  	=18
    //2013-01-07 16:59:59 	=18
    //Day1 fee= Min(60, 67)
    //Day1 Fee=60
    
    //2013-02-08 06:30:27	=13
    //2013-02-08 07:26:29   =18
    //Day2 Fee=Min(60, 18)
    //Day2 Fee=18
    
    //Total Fee would be Day1 +Day2=60+18======>78
    
    @Test
    public void testTaxFeePerDayMaxLimit() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": ["
                + "\"2013-01-07 07:00:59\",\"2013-01-07 08:11:59\","
                + "\"2013-01-07 15:30:59\",\"2013-01-07 16:59:59\","
                + "\"2013-02-08 06:30:27\",\"2013-02-08 07:26:29\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(78, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    //Testing the Tax Exempt vehicles - 2013-01-07 10:00:00
    @Test
    public void testTaxExemptvehicles() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Emergency\",\"dates\": [\"2013-01-07 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    //Testing the Month of July - 2013-07-01 10:00:00
    @Test
    public void testMonthOfJuly() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-07-01 10:00:00\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    

    //Testing Bad Date Format Expected 2013-07-01 10:00:00 but provided the 2013-07-01
    @Test
    public void testBadDateFormat() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicle_type\": \"Car\",\"dates\": [\"2013-07-01\"]}", headers);
        ResponseEntity<CongestionTaxCalculateResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/congestion/tax/v1/calculator").toString(), request, CongestionTaxCalculateResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
