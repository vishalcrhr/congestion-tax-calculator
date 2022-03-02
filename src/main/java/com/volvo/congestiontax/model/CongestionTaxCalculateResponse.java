package com.volvo.congestiontax.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class CongestionTaxCalculateResponse {

    private String error;
    private int tax;
    private String message;

    
    public CongestionTaxCalculateResponse(String error, int tax, String message) {
        super();
        this.error = error;
        this.tax = tax;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public int getTax() {
        return tax;
    }

    public String getMessage() {
        return message;
    }

    /*
     * returns the current timestamp
     */
    public Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

}
