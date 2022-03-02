package com.volvo.congestiontax.service;

import java.text.ParseException
;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.volvo.congestiontax.utils.DateUtils;
import com.volvo.congestiontax.utils.WeekendEnum;

@Service
public class CongestionTaxCalculator {

    private static List<String> tollFreeVehicles = new ArrayList<String>();

    static {
        tollFreeVehicles.add("MOTORCYCLES");
        tollFreeVehicles.add("BUSSES"); //changed to BUSSES from tractor (Fixed Bug)
        tollFreeVehicles.add("EMERGENCY");
        tollFreeVehicles.add("DIPLOMAT");
        tollFreeVehicles.add("FOREIGN");
        tollFreeVehicles.add("MILITARY");

    }
    
    private boolean IsTollFreeVehicle(String vehicle) {
        if (vehicle == null) return false;
        return tollFreeVehicles.contains(vehicle);
    }

    public int GetTollFee(LocalDateTime date, String vehicle)
    {
        if (IsTollFreeDate(date) || IsTollFreeVehicle(vehicle)) return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14 && minute >= 0 && minute <= 59) return 8; // (Fixed Bug)
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

    private Boolean IsTollFreeDate(LocalDateTime date)
    {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfWeek().getValue();
        int dayOfMonth = date.getDayOfMonth();

        if (day == WeekendEnum.SATURDAY.getCode() || day == WeekendEnum.SUNDAY.getCode()) return true;

        if (year == 2013)  // (Fixed Bug of Date API, year was returning 113)
        {
            if ((month == 1 && dayOfMonth == 1) ||
                    (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                    (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                    (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                    (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                    (month == 7) ||
                    (month == 11 && dayOfMonth == 1) ||
                    (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31)))
            {
                return true;
            }
        }
        return false;
    }
    /** Converting dates to Localdatetime for correct date and time and sort it so that 
     * mapping can be done easily.
     * @param vehicle
     * @param dates
     * @return
     * @throws ParseException
     */
    public int calculateTax(String vehicle, String[] dates) throws ParseException {
    	
    	//will convert Date to localDate for correct time management and better tracking.
    	List<LocalDateTime> localDateTimeList=Stream.of(dates).map(d->DateUtils.convertToLocalDateTime(d)).collect(Collectors.toList());
    	
    	//Sort the dates in ascending order
		List<LocalDateTime> dateTimeSortedList = localDateTimeList.stream().sorted((o1, o2) -> {
			if (o1.isBefore(o2)) {
				return -1;
			}
			return 1;
		}).collect(Collectors.toList());
    	
		//Map to store dates of toll passed in a single day corresponding to that particular day
		Map<String, List<LocalDateTime>> map = getDayWiseMapping(dateTimeSortedList);
    	
		// Interval Start date for the first time
    	return getTaxUpdated(vehicle, map, localDateTimeList.get(0) );
    	
    }

    /**Run the loop per day wise and get all the toll dates of that particular day. check the condition
     * and add to the total fee. Will add the total fee of previous day as well if day changed.
     * @param vehicle
     * @param dateMap
     * @param intervalStartDate
     * @return
     */
    public int getTaxUpdated(String vehicle, Map<String, List<LocalDateTime>> dateMap, LocalDateTime intervalStartDate)
    {
    	int totalFee = 0;
    	
		for (String key : dateMap.keySet()) { //day wise loop for per day fee tracking
			int totalDayFee = 0;
			int maxPreviousFee = 0;
			List<LocalDateTime> dateList = dateMap.get(key);

			for (LocalDateTime day : dateList) { // No. of toll per day tracking loop for checking the actual logic
				//Date date = Date.from(day.atZone(ZoneId.systemDefault()).toInstant()); 
				int nextFee = GetTollFee(day, vehicle);
				int tempFee = GetTollFee(intervalStartDate, vehicle);
				//maxPreviousFee is to track last Tax fee
				tempFee = Math.max(tempFee, maxPreviousFee);

				long minutes= Math.abs(ChronoUnit.MINUTES.between(day, intervalStartDate));
				if (minutes <= 60) {
					if (totalDayFee > 0) {
						totalDayFee -= tempFee;
					}
					if (nextFee >= tempFee) {
						totalDayFee += nextFee;
						maxPreviousFee = nextFee;
					} else {
						totalDayFee += tempFee;
						maxPreviousFee = nextFee;
					}
				} else {
					intervalStartDate = day;
					totalDayFee += nextFee;
					tempFee = nextFee;
					maxPreviousFee = nextFee;
				}
			}
			if (totalDayFee >= 60) {
				totalDayFee = 60;
			}
			
			totalFee += totalDayFee; //Adding in total Tax fee
		}
		//Fixed multiple Bugs
        return totalFee;
    }
    
	/** Get the full map of distinct Date with its toll date passed list
	 * @param dateTimeSortedList
	 * @return
	 */
	private Map<String, List<LocalDateTime>> getDayWiseMapping(List<LocalDateTime> dateTimeSortedList) {
		Map<String, List<LocalDateTime>> map = new LinkedHashMap<>();
		for (LocalDateTime i : dateTimeSortedList) {
			// custom key for the particular day
			String day = i.getDayOfMonth() + "-" + i.getMonthValue() + "-" + i.getYear();

			if (!map.containsKey(day)) {
				List<LocalDateTime> list = new ArrayList<LocalDateTime>();
				list.add(i);
				map.put(day, list);
			} else {
				List<LocalDateTime> tempList = map.get(day);
				tempList.add(i);
				map.put(day, tempList);
			}
		}
		return map;
	}
    
}


