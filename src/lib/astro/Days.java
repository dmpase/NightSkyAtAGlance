package lib.astro;

/*******************************************************************************
 * Copyright (c) 2021-2023 Douglas M. Pase                                     *
 * All rights reserved.                                                        *
 * Redistribution and use in source and binary forms, with or without          *
 * modification, are permitted provided that the following conditions          *
 * are met:                                                                    *
 * o       Redistributions of source code must retain the above copyright      *
 *         notice, this list of conditions and the following disclaimer.       *
 * o       Redistributions in binary form must reproduce the above copyright   *
 *         notice, this list of conditions and the following disclaimer in     *
 *         the documentation and/or other materials provided with the          *
 *         distribution.                                                       *
 * o       Neither the name of the copyright holder nor the names of its       *
 *         contributors may be used to endorse or promote products derived     *
 *         from this software without specific prior written permission.       *
 *                                                                             *
 * The copyright holders provide no reassurances that the source code provided *
 * does not infringe any patent, copyright, or any other intellectual property *
 * rights of third parties. The copyright holders disclaim any liability to    *
 * any recipient for claims brought against recipient by any third party for   *
 * infringement of that party's intellectual property rights.                  *
 *                                                                             *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" *
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE   *
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE  *
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE   *
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR         *
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF        *
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS    *
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN     *
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)     *
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF      *
 * THE POSSIBILITY OF SUCH DAMAGE.                                             *
 *******************************************************************************/


public class Days {
	
	public static final int UNKNOWN   = 0;
	public static final int January   = UNKNOWN   + 1;
	public static final int February  = January   + 1;
	public static final int March     = February  + 1;
	public static final int April     = March     + 1;
	public static final int May       = April     + 1;
	public static final int June      = May       + 1;
	public static final int July      = June      + 1;
	public static final int August    = July      + 1;
	public static final int September = August    + 1;
	public static final int October   = September + 1;
	public static final int November  = October   + 1;
	public static final int December  = November  + 1;

	public static final int UNK = UNKNOWN;
	public static final int JAN = January;
	public static final int FEB = February;
	public static final int MAR = March;
	public static final int APR = April;
	public static final int MAY = May;
	public static final int JUN = June;
	public static final int JUL = July;
	public static final int AUG = August;
	public static final int SEP = September;
	public static final int OCT = October;
	public static final int NOV = November;
	public static final int DEC = December;


	public enum Month { 
		JAN, January, FEB, February, MAR, March,
		APR, April,   MAY, May,      JUN, June,
		JUL, July,    AUG, August,   SEP, September,
		OCT, October, NOV, November, DEC, December,
		UNKNOWN
	}

	public static final String[] month_to_long_name = {
			"UNKNOWN",
			"January",	"February",	"March",
			"April",	"May",		"June",
			"July",		"August",	"September",
			"October",	"November",	"December"
	};

	public static final String[] month_to_short_name = {
			"UNK",
			"JAN",	"FEB",	"MAR",
			"APR",	"MAY",	"JUN",
			"JUL",	"AUG",	"SEP",
			"OCT",	"NOV",	"DEC"
	};


	public static final boolean AD  = true;		// Anno Domini
	public static final boolean BC  = ! AD;		// Before Christ

	public static final boolean CE  = AD;		// Common Era
	public static final boolean BCE = BC;		// Before Common Era
	
	
	public static final int Sunday    = 0;
	public static final int Monday    = Sunday    + 1;
	public static final int Tuesday   = Monday    + 1;
	public static final int Wednesday = Tuesday   + 1;
	public static final int Thursday  = Wednesday + 1;
	public static final int Friday    = Thursday  + 1;
	public static final int Saturday  = Friday    + 1;
	
	public static final int SUN = Sunday;
	public static final int MON = Monday;
	public static final int TUE = Tuesday;
	public static final int WED = Wednesday;
	public static final int THU = Thursday;
	public static final int FRI = Friday;
	public static final int SAT = Saturday;

	public enum Week { 
		SUN, Sunday,    MON, Monday,   TUE, Tuesday,
		WED, Wednesday, THU, Thursday, FRI, Friday,
		SAT, Saturday,
	}

	public static final String[] week_to_long_name = {
			"Sunday",		"Monday",	"Tuesday",
			"Wednesday",	"Thursday",	"Friday",
			"Saturday"
	};

	public static final String[] week_to_short_name = {
			"SUN",	"MON",	"TUE",
			"WED",	"THU",	"FRI",
			"SAT"
	};


	public static final boolean East = true;	// East Latitude
	public static final boolean West = ! East;	// West Latitude

	public static final boolean E    = East;	// East Latitude
	public static final boolean W    = West;	// West Latitude

	
	public static final boolean AM  = true;		// Anti Meridian
	public static final boolean PM  = ! AM;		// Post Meridian

	
	// convert month from an integer to a Month enum 
	//  1 == January
	//  2 == February
	// ...
	// 12 == December
	public static Month int_to_month(int i)
	{
		Month month = Month.UNKNOWN;
		
		switch (((i%12)+12)%12+1) {
		case JAN:
			month = Month.JAN;
			break;
		case FEB:
			month = Month.FEB;
			break;
		case MAR:
			month = Month.MAR;
			break;
		case APR:
			month = Month.APR;
			break;
		case MAY:
			month = Month.MAY;
			break;
		case JUN:
			month = Month.JUN;
			break;
		case JUL:
			month = Month.JUL;
			break;
		case AUG:
			month = Month.AUG;
			break;
		case SEP:
			month = Month.SEP;
			break;
		case OCT:
			month = Month.OCT;
			break;
		case NOV:
			month = Month.NOV;
			break;
		case DEC:
			month = Month.DEC;
			break;
		}

		return month;
	}
	
	// convert a Month enum to an integer
	//  1 == January
	//  2 == February
	// ...
	// 12 == December
	public static int month_to_int(Month month)
	{
		int integer = UNKNOWN;
		switch (month) {
		case JAN: case January:
			integer = JAN;
			break;
		case FEB: case February:
			integer = FEB;
			break;
		case MAR: case March:
			integer = MAR;
			break;
		case APR: case April:
			integer = APR;
			break;
		case MAY: case May:
			integer = MAY;
			break;
		case JUN: case June:
			integer = JUN;
			break;
		case JUL: case July:
			integer = JUL;
			break;
		case AUG: case August:
			integer = AUG;
			break;
		case SEP: case September:
			integer = SEP;
			break;
		case OCT: case October:
			integer = OCT;
			break;
		case NOV: case November:
			integer = NOV;
			break;
		case DEC: case December:
			integer = DEC;
			break;
		case UNKNOWN:
		default:
			break;
		}
		
		return integer;
	}
	
	public static int parse_date(String str)
	{
		if (str != null && ! str.equals("")) {
			String[] fields = str.split("[/]");
			int month = Integer.parseInt(fields[0]);
			int day   = Integer.parseInt(fields[1]);
			int year  = Integer.parseInt(fields[2]);
			
			return (int) days_since_epoch(year, month, day);
		}

		return 0;
	}

	// compare Month enums for eq, lt, le, gt, ge, ne
	public static boolean lt(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a < b;
	}
	
	public static boolean le(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a <= b;
	}
	
	public static boolean eq(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a == b;
	}
	
	public static boolean gt(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a > b;
	}

	public static boolean ge(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a >= b;
	}

	public static boolean ne(Month x, Month y)
	{
		int a = month_to_int(x);
		int b = month_to_int(y);
		
		return a != b;
	}


	// number of days since the start of the epoch (Sunday, December 31, 1989)
	public static double days_since_epoch(int year, Month month, double day)
	{
		return days_since_epoch(year) + day_number(year, month_to_int(month)-1, day);
	}

	// number of days since the start of the epoch (Sunday, December 31, 1989)
	public static double days_since_epoch(int year, int month, double day)
	{
		int yr = year;
		int mo = month;
		if (month < 1) {
			mo += 12;
			yr -= 1;
		}
		return days_since_epoch(yr) + day_number(yr, mo, day);
	}
	
	// day of the week (Sunday == 0)
	public static int epoch_to_day_of_week(int day_of_epoch)
	{
		return (day_of_epoch+1) % 7;
	}


	// number of days since January 1st of the year (January 1st == 1)
	public static double day_number(int year, Month month, double day) 
	{
		double value = 0;

		if (is_leap_year(year)) {
			value = leap_year_days[month_to_int(month)-1];
		} else {
			value = normal_year_days[month_to_int(month)-1];
		}
		
		return value + day;
	}

	// number of days since January 1st of the year (January 1st == 1)
	public static double day_number(int year, int month, double day) 
	{
		double value = 0;

		if (is_leap_year(year)) {
			value = leap_year_days[month-1];
		} else {
			value = normal_year_days[month-1];
		}
		
		return value + day;
	}

	
	// number of days since the start of the epoch (December 31st, 1989)
	public static int days_since_epoch(int year)
	{
		int value = 0;
		
		if (year < 1990) {
			for (int i=year; i < 1990; i++) {
				value -= is_leap_year(i) ? 366 : 365;
			}
		} else {
			for (int i=1990; i < year; i++) {
				value += is_leap_year(i) ? 366 : 365;
			}
		}
		
		return value;
	}


	public static int epoch_to_year(double days_since_epoch)
	{
		int year;
		if (days_since_epoch < 0) {
			days_since_epoch -= 1;
			year = 1990;
			for (int dse=(int)days_since_epoch; dse < 0; dse += is_leap_year(year) ? 366 : 365) {
				year -= 1;
			}
		} else {
			year = 1989;
			for (int dse=(int)days_since_epoch ; 0 < dse; dse -= is_leap_year(year) ? 366 : 365) {
				year += 1;
			}
		}

		return year;
	}


	public static int epoch_to_month(double days_since_epoch)
	{
		int year = epoch_to_year(days_since_epoch);
		int start_of_year = days_since_epoch(year);
		int days_from_start_of_year = (int)days_since_epoch - start_of_year - 1;
		int month = is_leap_year(year) ? month_leap_year[days_from_start_of_year] : month_normal_year[days_from_start_of_year];
		
		return month;
	}


	public static int epoch_to_day_of_month(double days_since_epoch)
	{
		int year = epoch_to_year(days_since_epoch);
		int start_of_year = days_since_epoch(year);
		int days_from_start_of_year = (int)days_since_epoch - start_of_year - 1;
		int day_of_month = is_leap_year(year) ? day_of_month_leap_year[days_from_start_of_year] : day_of_month_normal_year[days_from_start_of_year];
		
		return day_of_month;
	}


	// is this a leap year?
	public static boolean is_leap_year(int year)
	{
		return (year % 4 == 0) && (year % 400 != 0);
	}
	
	
	// convert the day-of-epoch to "m/d/yyyy" (north american format string)
	public static String day_of_epoch_to_mdy(int doe)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);
		
		String result = String.format("%d/%d/%d", month, day_of_month, year);
		
		return result;
	}
	
	
	// convert the day-of-epoch to "mm/dd/yyyy" (north american format string)
	public static String day_of_epoch_to_mmddyyyy(int doe)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);
		
		String result = String.format("%02d/%02d/%d", month, day_of_month, year);
		
		return result;
	}
	
	
	// convert the day-of-epoch to "mm/dd/yyyy" (north american format string)
	public static String day_of_epoch_to_mmddyyyy(int doe, String sep)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);

		String result = String.format("%02d%s%02d%s%d", month, sep, day_of_month, sep, year);

		return result;
	}
	
	
	// convert the day-of-epoch to "mm/dd/yyyy" (north american format string)
	public static String day_of_epoch_to_mdy(int doe, String sep)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);

		String result = String.format("%2d%s%2d%s%d", month, sep, day_of_month, sep, year);

		return result;
	}
	
	
	// convert the day-of-epoch to "mm/dd/yyyy" (north american format string)
	public static String day_of_epoch_to_yyyymmdd(int doe, String sep)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);

		String result = String.format("%d%s%02d%s%02d", year, sep, month, sep, day_of_month);

		return result;
	}
	
	
	// convert the day-of-epoch to "mm/dd/yyyy" (north american format string)
	public static String day_of_epoch_to_ymd(int doe, String sep)
	{
		int day_of_month = epoch_to_day_of_month(doe);
		int month = epoch_to_month(doe);
		int year = epoch_to_year(doe);

		String result = String.format("%d%s%d%s%d", year, sep, month, sep, day_of_month);

		return result;
	}


	// convert a standard (Gregorian) date to an astronomical (Julian) date
	public static double julian_date(int year, int month, double day)
	{
		if (month == JAN || month == FEB) {
			month += 12;
			year  -= 1;
		}

		int A = year/100;
		int B = 0;
		int C = (int)(365.25*year);
		int D = (int)(30.6001*(month+1));
		if (1582 < year || (year == 1582 && OCT <= month)) {
			B = 2 - A + A/4;
		}
		
		if (year < 0) {
			C = (int)((365.25*year)-0.75);
		}

		return B + C + D + day + 1720994.5;
	}

	// convert a standard (Gregorian) date to an astronomical (Julian) date
	public static double julian_date(int year, boolean era, int month, double day)
	{
		if (era == CE) {
			return julian_date(year, month, day);
		} else {
			return julian_date(-year, month, day);
		}
	}
	
	
	// convert Julian date to standard (Gregorian) year
	public static int julian_date_to_year(double date)
	{
		date += 0.5;
		int I = (int) date;
		double F = date - I;
		int A = 0;
		int B = I;
		if (2299160 < I) {
			A = (int)((I - 1867216.25)/36524.25);
			B = I + 1 + A -(int)(A/4);
		}
		int C = B + 1524;
		int D = (int)((C - 122.1)/365.25);
		int E = (int)(365.25*D);
		int G = (int)((C - E)/30.6001);
		
		@SuppressWarnings("unused")
		double day_of_month = C - E + F - (int)(30.6001*G);
		int month = (G < 13.5) ? G - 1 : G - 13;
		int year = (2.5 < month) ? D - 4716 : D - 4715;
		
		return year;
	}
	
	// convert Julian date to standard (Gregorian) month
	public static int julian_date_to_month(double date)
	{
		date += 0.5;
		int I = (int) date;
		double F = date - I;
		int A = 0;
		int B = I;
		if (2299160 < I) {
			A = (int)((I - 1867216.25)/36524.25);
			B = I + 1 + A -(int)(A/4);
		}
		int C = B + 1524;
		int D = (int)((C - 122.1)/365.25);
		int E = (int)(365.25*D);
		int G = (int)((C - E)/30.6001);
		
		@SuppressWarnings("unused")
		double day_of_month = C - E + F - (int)(30.6001*G);
		int month = (G < 13.5) ? G - 1 : G - 13;
		@SuppressWarnings("unused")
		int year = (2.5 < month) ? D - 4716 : D - 4715;
		
		return month;
	}
	
	// convert Julian date to standard (Gregorian) day of the month
	public static double julian_date_to_day(double date)
	{
		date += 0.5;
		int I = (int) date;
		double F = date - I;
		int A = 0;
		int B = I;
		if (2299160 < I) {
			A = (int)((I - 1867216.25)/36524.25);
			B = I + 1 + A -(int)(A/4);
		}
		int C = B + 1524;
		int D = (int)((C - 122.1)/365.25);
		int E = (int)(365.25*D);
		int G = (int)((C - E)/30.6001);
		
		double day_of_month = C - E + F - (int)(30.6001*G);
		int month = (G < 13.5) ? G - 1 : G - 13;
		@SuppressWarnings("unused")
		int year = (2.5 < month) ? D - 4716 : D - 4715;
		
		return day_of_month;
	}
	
	
	// compute the day of the week (0 == Sunday, 1 == Monday, etc.)
	public static int day_of_week(int year, int month, double day)
	{
		return (int)(julian_date(year, month, day) + 1.5) % 7;
	}


	// convert a Week enum to an integer
	// 0 == Sunday
	// 1 == Monday
	// ...
	// 6 == Saturday
	public static int week_to_int(Week week)
	{
		int integer = SUN;
		switch (week) {
		case SUN: case Sunday:
			integer = SUN;
			break;
		case MON: case Monday:
			integer = MON;
			break;
		case TUE: case Tuesday:
			integer = TUE;
			break;
		case WED: case Wednesday:
			integer = WED;
			break;
		case THU: case Thursday:
			integer = THU;
			break;
		case FRI: case Friday:
			integer = FRI;
			break;
		case SAT: case Saturday:
			integer = SAT;
			break;
		default:
			break;
		}
		
		return integer;
	}

	
	// convert week from an integer to a Week enum 
	// 0 == Sunday
	// 1 == Monday
	// ...
	// 6 == Saturday
	public static Week int_to_week(int i)
	{
		Week week = Week.SAT;
		
		switch (((i%7)+7)%7) {
		case SUN:
			week = Week.SUN;
			break;
		case MON:
			week = Week.MON;
			break;
		case TUE:
			week = Week.TUE;
			break;
		case WED:
			week = Week.WED;
			break;
		case THU:
			week = Week.THU;
			break;
		case FRI:
			week = Week.FRI;
			break;
		case SAT:
			week = Week.SAT;
			break;
		}

		return week;
	}

	// compare Week enums for eq, lt, le, gt, ge, ne
	public static boolean lt(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a < b;
	}
	
	public static boolean le(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a <= b;
	}
	
	public static boolean eq(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a == b;
	}
	
	public static boolean gt(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a > b;
	}
	
	public static boolean ge(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a >= b;
	}
	
	public static boolean ne(Week x, Week y)
	{
		int a = week_to_int(x);
		int b = week_to_int(y);
		
		return a != b;
	}
	
	// convert hours (24h format), minutes, seconds to decimal hours
	public static double hms_to_hours(int hours, int minutes, double seconds)
	{
		return hours + (minutes + seconds/60.0)/60.0;
	}
	
	
	// convert hours (12h format), minutes, seconds to decimal hours
	public static double hms_to_hours(int hours, boolean meridian, int minutes, double seconds)
	{
		hours = (meridian == AM) ? hours : hours + 12;
		return hours + (minutes + seconds/60.0)/60.0;
	}
	

	// extract the hours (0 <= x < 24, 24h format) from decimal hours
	public static int hours_to_h(double hours)
	{
		double hour = hours - 24*Math.floor(hours/24);
		return (int) Math.floor(hour);
	}


	// extract the minutes (0 <= x < 60) from decimal hours
	public static int hours_to_m(double hours)
	{
		double hour   = hours - 24*Math.floor(hours/24);
		double minute = 60*(hour - Math.floor(hour));
		return (int) Math.floor(minute);
	}


	// extract the seconds (0 <= x < 60, fractions) from decimal hours
	public static double hours_to_s(double hours)
	{
		double hour   = hours - 24 * Math.floor(hours/24);
		double minute = 60*(hour   - Math.floor(hour));
		double second = 60*(minute - Math.floor(minute));
		return second;
	}


	// convert Universal Time to Greenwich Sidereal Time (yyyy-mm-dd hh:mm:ss in UT)
	public static double ut_to_gst(int year, int month, int day, int hour, int minutes, double seconds)
	{
		double julian_date = julian_date(year, month, day);
		double S = julian_date - 2451545;
		double T = S/36525;
		double T0 = 6.697374558 + 2400.051336 * T + 0.000025862 * T * T;
		T0 -= 24*Math.floor(T0/24);
		double UT = hms_to_hours(hour, minutes, seconds);
		T0 += UT * 1.002737909;
		T0 -= 24*Math.floor(T0/24);

		return T0;
	}
	

	// convert Greenwich Sidereal Time (yyyy-mm-dd in standard time, hh:mm:ss in GST)
	public static double gst_to_ut(int year, int month, int day, int hour, int minutes, double seconds)
	{
		double julian_date = julian_date(year, month, day);
		double s = julian_date - 2451545;
		double t = s/36525;
		double t0 = 6.697374558 + 2400.051336 * t + 0.000025862 * t * t;
		t0 -= 24*Math.floor(t0/24);
		double gst = hms_to_hours(hour, minutes, seconds);
		gst -= t0;
		gst -= 24*Math.floor(gst/24);
		double ut = gst * 0.9972695663;

		return ut;
	}


	// convert longitude (in degrees, minutes, and/or seconds) to degrees + fraction
	public static double longitude(int degrees, int minutes, double seconds)
	{
		boolean positive = (0 < degrees) || (degrees == 0 && 0 < minutes) || (degrees == 0 && minutes == 0 && 0 <= seconds);
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		seconds = Math.abs(seconds);
		return (positive?1:-1)*(degrees + (minutes + seconds/60)/60);
	}

	public static double longitude(int degrees, double minutes)
	{
		boolean positive = (0 < degrees) || (degrees == 0 && 0 <= minutes);
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		return (positive?1:-1)*(degrees + minutes/60);
	}

	public static double longitude(int degrees, boolean east_west, int minutes, double seconds)
	{
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		seconds = Math.abs(seconds);
		return (east_west == East ? 1 : -1 )*(degrees + (minutes + seconds/60)/60);
	}

	public static double longitude(int degrees, boolean east_west, double minutes)
	{
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		return (east_west == East ? 1 : -1 )*(degrees + minutes/60);
	}

	public static boolean longitude_to_east_west(double longitude)
	{
		return (longitude <= 0) ? West : East;
	}

	public static int longitude_to_degrees(double longitude)
	{
		boolean positive = 0 <= longitude;
		return (int) ((positive?1:-1)*Math.floor(Math.abs(longitude)));
	}

	public static int longitude_to_minutes(double longitude)
	{
		double degrees = Math.abs(longitude);
		return (int)(60*(degrees - Math.floor(degrees)));
	}

	public static double longitude_to_seconds(double longitude)
	{
		double minutes = 60*Math.abs(longitude);
		return 60*(minutes - Math.floor(minutes));
	}

	public static double longitude_to_radians(double longitude)
	{
		return Math.PI * longitude / 180;
	}

	public static double radians_to_longitude(double radians)
	{
		return 180 * radians / Math.PI;
	}


	// convert latitude (in degrees, minutes, and/or seconds) to degrees + fraction
	public static double latitude(int degrees, int minutes, double seconds)
	{
		boolean positive = (0 < degrees) || (degrees == 0 && 0 < minutes) || (degrees == 0 && minutes == 0 && 0 <= seconds);
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		seconds = Math.abs(seconds);
		return (positive?1:-1)*(degrees + (minutes + seconds/60)/60);
	}

	// convert latitude (in degrees and minutes) to degrees + fraction
	public static double latitude(int degrees, double minutes)
	{
		boolean positive = (0 < degrees) || (degrees == 0 && 0 <= minutes);
		degrees = Math.abs(degrees);
		minutes = Math.abs(minutes);
		return (positive?1:-1)*(degrees + minutes/60);
	}

	public static int latitude_to_degrees(double latitude)
	{
		boolean positive = 0 <= latitude;
		return (int) ((positive?1:-1)*Math.floor(Math.abs(latitude)));
	}

	public static int latitude_to_minutes(double latitude)
	{
		double degrees = Math.abs(latitude);
		return (int)(60*(degrees - Math.floor(degrees)));
	}

	public static double latitude_to_seconds(double latitude)
	{
		double minutes = 60*Math.abs(latitude);
		return 60*(minutes - Math.floor(minutes));
	}

	public static double latitude_to_radians(double latitude)
	{
		return Math.PI * latitude / 180;
	}

	public static double radians_to_latitude(double radians)
	{
		return 180 * radians / Math.PI;
	}


	// convert Greenwich Sidereal Time to Local Sidereal Time
	public static double gst_to_lst(double gst, double longitude)
	{
		double lst = gst + longitude / 15;
		lst = (lst <   0) ? lst + 24 : lst;
		lst = (24  < lst) ? lst - 24 : lst;

		return lst;
	}


	// convert Local Sidereal Time to Greenwich Sidereal Time
	public static double lst_to_gst(double lst, double longitude)
	{
		double gst = lst - longitude / 15;
		gst = (gst <   0) ? gst + 24 : gst;
		gst = (24  < gst) ? gst - 24 : gst;

		return gst;
	}
	
	@SuppressWarnings("unused")
	private static final int[] days_in_month_normal_year = {
			31, 28, 31,		// winter 
			30, 31, 30,		// spring
			31, 31, 30,		// summer
			31, 30, 31,		// fall
	};

	@SuppressWarnings("unused")
	private static final int[] days_in_month_leap_year = {
			31, 29, 31,		// winter 
			30, 31, 30,		// spring
			31, 31, 30,		// summer
			31, 30, 31,		// fall
	};

	private static final int[] normal_year_days = {
			  0,  31,  59,	// winter 
			 90, 120, 151,	// spring
			181, 212, 243,	// summer
			273, 304, 334,	// fall
	};
	
	private static final int[] leap_year_days = {
			  0,  31,  60,	// winter 
			 91, 121, 152,	// spring
			182, 213, 244,	// summer
			274, 305, 335,	// fall
	};

	private static final int[] month_normal_year = {
		//	01  02  03  04  05  06  07  08  09  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31
			 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 	// january
			 2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,				 	// february
			 3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3, 	// march
			 4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,			// april
			 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 	// may
			 6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,			// june
			 7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7, 	// july
			 8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8, 	// august
			 9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,			// september
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 	// october
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,			// november
			12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 	// december
	};

	private static final int[] month_leap_year = {
		//	01  02  03  04  05  06  07  08  09  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31
			 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 	// january
			 2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,			 	// february
			 3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3, 	// march
			 4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,			// april
			 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 	// may
			 6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,			// june
			 7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7, 	// july
			 8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8, 	// august
			 9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9,			// september
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 	// october
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,			// november
			12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 	// december
	};

	private static final int[] day_of_month_normal_year = {
		//	01  02  03  04  05  06  07  08  09  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// january
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,					// february
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// march
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// april
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// may
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// june
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// july
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// august
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// september
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// october
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// november
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// december
	};

	private static final int[] day_of_month_leap_year = {
			//	01  02  03  04  05  06  07  08  09  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// january
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,	29,				// february
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// march
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// april
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// may
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// june
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// july
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// august
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// september
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// october
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,			// november
			 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 	// december
	};
	
	public static void main(String[] args)
	{
		// TODO
		/*
		Date today = Date.today();
		System.out.println(today.month+"/"+today.day_of_month+"/"+today.year);
		int date = (int) days_since_epoch(today.year, today.month, today.day_of_month);
		System.out.println(epoch_to_day_of_week(date));
		for (int year=1800; year <= 2100; year++) {
			for (int day_of_year=0; day_of_year < (is_leap_year(year)?366:365); day_of_year++) {
				int month = is_leap_year(year) ? month_leap_year[day_of_year] : month_normal_year[day_of_year];
				int day_of_month = is_leap_year(year) ? day_of_month_leap_year[day_of_year] : day_of_month_normal_year[day_of_year];

				double days_since_epoch = days_since_epoch(year, month, day_of_month);

				int epoch_to_year  = epoch_to_year (days_since_epoch);
				int epoch_to_month = epoch_to_month(days_since_epoch);
				int epoch_to_day_of_month = epoch_to_day_of_month(days_since_epoch);

				if (epoch_to_year != year || epoch_to_month != month || epoch_to_day_of_month != day_of_month) {
					System.out.println("********** "+month+"/"+day_of_month+"/"+year+" != "+epoch_to_month+"/"+epoch_to_day_of_month+"/"+epoch_to_year);
					System.exit(0);
				} else {
					System.out.println(month+"/"+day_of_month+"/"+year+" == "+epoch_to_month+"/"+epoch_to_day_of_month+"/"+epoch_to_year);
				}
			}
		}
		*/
		

		/*
		boolean test = true;
		if (test) {
			double longitude = -106.535754;
			System.out.println(longitude_to_degrees(longitude)+" "+longitude_to_minutes(longitude)+":"+longitude_to_seconds(longitude));
			System.out.println(longitude(106, West, 32, 8.714399999989837));
			double latitude = 35.1645649;
			System.out.println(latitude_to_degrees(latitude)+":"+latitude_to_minutes(latitude)+":"+latitude_to_seconds(latitude));
			System.out.println(latitude(35, 9, 52.433640000017476));
			System.exit(0);
		} else {
			System.out.println(days_since_epoch(1985, FEB, 17.25));
			System.out.println(julian_date(1985, FEB, 17.25));
			System.out.printf("%04d-%02d-%.2f\n", 
					julian_date_to_year(2446113.75), 
					julian_date_to_month(2446113.75), 
					julian_date_to_day(2446113.75));
			System.out.println(day_of_week(2021, MAY, 22));
			System.out.println(ut_to_gst(1980, APR, 22, 14, 36, 51.67));
			double UT = gst_to_ut(1980, APR, 22, 4, 40, 5.23);
			System.out.printf("%02d:%02d:%.2f\n", 
					hours_to_h(UT), 
					hours_to_m(UT), 
					hours_to_s(UT));
			double longitude = longitude(64, W, 0, 0.0);
			double hours = hms_to_hours(4, 40, 5.23);
			double lst = gst_to_lst(hours, longitude);
			System.out.printf("%02d:%02d:%.2f\n", 
					hours_to_h(lst), 
					hours_to_m(lst), 
					hours_to_s(lst));
			double gst = lst_to_gst(lst, longitude);
			System.out.printf("%02d:%02d:%.2f\n", 
					hours_to_h(gst), 
					hours_to_m(gst), 
					hours_to_s(gst));
		}
		*/
	}
}
