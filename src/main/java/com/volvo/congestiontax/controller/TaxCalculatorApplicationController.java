package com.volvo.congestiontax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.congestiontax.model.CongestionTaxCalculateRequest;
import com.volvo.congestiontax.model.CongestionTaxCalculateResponse;
import com.volvo.congestiontax.service.CongestionTaxCalculator;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/congestion/tax/v1")
public class TaxCalculatorApplicationController {

    @Autowired
    CongestionTaxCalculator congestionTaxCalculator;

    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    public ResponseEntity<CongestionTaxCalculateResponse> calculateTax(
            @ApiParam(value = "Date format is : yyyy-MM-dd HH:mm:ss", required = true) @RequestBody CongestionTaxCalculateRequest request) {
    	CongestionTaxCalculateResponse response;
        try {
            int tax = congestionTaxCalculator.calculateTax(request.getVehicle_type().toUpperCase(), request.getDates());

            response = new CongestionTaxCalculateResponse("NA", tax,
                    "Tax for the Vehicle :" + request.getVehicle_type() + " Amount : " + tax);
            return new ResponseEntity<CongestionTaxCalculateResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response = new CongestionTaxCalculateResponse(e.getLocalizedMessage(), 0,
                    "Tax calculation failed.");
            return new ResponseEntity<CongestionTaxCalculateResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
