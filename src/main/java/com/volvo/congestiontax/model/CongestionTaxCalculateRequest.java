package com.volvo.congestiontax.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;

@ApiModel
public class CongestionTaxCalculateRequest {

    private String vehicle_type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String[] dates;

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }

}
