package com.volvo.congestiontax.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	public static LocalDateTime convertToLocalDateTime(String dateToConvert) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(dateToConvert, format);
	}

}
