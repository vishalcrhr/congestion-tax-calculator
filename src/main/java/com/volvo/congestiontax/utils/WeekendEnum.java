package com.volvo.congestiontax.utils;

public enum WeekendEnum {

	SUNDAY(7), SATURDAY(6);
	
	private final Integer value;
	
	WeekendEnum(Integer value) {
		this.value = value;
	}
	
	public int getCode() {
		return this.value;	
	}
}
