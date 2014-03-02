package com.goeuro.ab.utilities;

public class DateUtils {

	public static boolean isDateValid(int day, int month, int year) {
		if (year < Constants.MIN_CALENDAR || year > Constants.MAX_CALENDAR) {
			return false;
		}
		
		if (month > 12) {
			return false;
		}
		
		if (day > 31) {
			return false;
		}
		
		if ((month == 2 && (day > 0 && day <= 28)) || 
				((month == 11 || month == 4 || month == 6 || month == 9) && (day > 0 && day <= 30)) ||
				((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day > 0 && day <= 31))) {
			return true;
		}
		
		return false;
	}
}
