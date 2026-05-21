package lib.time;

/*******************************************************************************
 * Copyright (c) 1988-2020 Douglas M. Pase                                     *
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

import java.io.PrintStream;
import java.util.Calendar;
import java.util.TimeZone;

public class Date implements Comparable<Date>{
	public final int  year;
	public final byte month;
	public final byte day_of_month;
	
	public  static final Date      start_of_epoch  = new Date(1800, 1, 1);
	public  static final Date      end_of_epoch    = new Date(2100, 1, 1);
	private static final int[]     days_from_epoch = calc_epoc();	// days from the start of epoch to Jan 1st of each year
	private static final boolean[] is_leap_year    = calc_leap();	// is this a leap year

	public static final double days_per_year = 365.2421918;

	public Date(int y, int m, int d) 
	{
		year  = y;
		month = (byte) m;
		day_of_month   = (byte) d;
	}

	public Date(int day_of_epoc) 
	{
		int y = 0;
		int m = 0;
		int d = 0;
		
		if (0 <= day_of_epoc && day_of_epoc < days_from_epoch[days_from_epoch.length-1]) {
			for (int i=0; i < days_from_epoch.length; i++) {
				if (day_of_epoc < days_from_epoch[i]) {
					y = start_of_epoch.year + i - 1;
					
					// start day of each month (doe starts at zero)
					final int jan = 0;
					final int feb = jan + 31;
					final int mar = feb + 28 + (is_leap_year(y) ? 1 : 0);
					final int apr = mar + 31;
					final int may = apr + 30;
					final int jun = may + 31;
					final int jul = jun + 30;
					final int aug = jul + 31;
					final int sep = aug + 31;
					final int oct = sep + 30;
					final int nov = oct + 31;
					final int dec = nov + 30;

					int day_of_year = day_of_epoc - days_from_epoch[i-1];
					if (jan <= day_of_year && day_of_year < feb) {
						m = 1;
						d = day_of_year + 1 - jan;
					} else if (feb <= day_of_year && day_of_year < mar) {
						m = 2;
						d = day_of_year + 1 - feb;
					} else if (mar <= day_of_year && day_of_year < apr) {
						m = 3;
						d = day_of_year + 1 - mar;
					} else if (apr <= day_of_year && day_of_year < may) {
						m = 4;
						d = day_of_year + 1 - apr;
					} else if (may <= day_of_year && day_of_year < jun) {
						m = 5;
						d = day_of_year + 1 - may;
					} else if (jun <= day_of_year && day_of_year < jul) {
						m = 6;
						d = day_of_year + 1 - jun;
					} else if (jul <= day_of_year && day_of_year < aug) {
						m = 7;
						d = day_of_year + 1 - jul;
					} else if (aug <= day_of_year && day_of_year < sep) {
						m = 8;
						d = day_of_year + 1 - aug;
					} else if (sep <= day_of_year && day_of_year < oct) {
						m = 9;
						d = day_of_year + 1 - sep;
					} else if (oct <= day_of_year && day_of_year < nov) {
						m = 10;
						d = day_of_year + 1 - oct;
					} else if (nov <= day_of_year && day_of_year < dec) {
						m = 11;
						d = day_of_year + 1 - nov;
					} else if (dec <= day_of_year) {
						m = 12;
						d = day_of_year + 1 - dec;
					}
					break;
				}
			}
		} else if (days_from_epoch[days_from_epoch.length-1] <= day_of_epoc) {
		} else {
		}
		
		year  = y;
		month = (byte) m;
		day_of_month = (byte) d;
	}
	
	public static Date today()
	{
		TimeZone ltz = TimeZone.getDefault();

		Calendar zone = Calendar.getInstance(ltz);

		int year   = zone.get(Calendar.YEAR);
		int month  = zone.get(Calendar.MONTH);
		int day    = zone.get(Calendar.DAY_OF_MONTH);

		return new Date(year, month+1, day);
	}

	public static Date parseDate(String date) 
	{
		String[] fields = date.split("/");
		int month = Integer.parseInt(fields[0]);
		int day   = Integer.parseInt(fields[1]);
		int year  = Integer.parseInt(fields[2]);
		
		return new Date(year, month, day);
	}
	
	public boolean is_leap_year()
	{
		boolean leap = false;

		int year_of_epoc = year_of_epoc();
		if (0 <= year_of_epoc && year_of_epoc < is_leap_year.length) {
			leap = is_leap_year[year_of_epoc];
		} else {
			leap = is_leap_year(year);
		}
		
		return leap;
	}
	
	public int year_of_epoc()
	{
		return year - start_of_epoch.year;
	}
	
	public static boolean is_leap_year(int year)
	{
		boolean leap = false;

		if (year % 400 == 0) {
			leap = true;
		} else if (year % 100 == 0) {
			leap = false;
		} else if (year % 4 == 0) {
			leap = true;
		} else {
			leap = false;
		}
		
		return leap;
	}
	
	public static int day_of_year(Date d)
	{
		return (d == null) ? 0 : d.day_of_year();
	}
	
	public int day_of_year()
	{
		int doy = 0;
		if (month == 1) {
			doy = day_of_month - 1;
		} else if (month == 2) {
			doy = 31 + day_of_month - 1;
		} else if (month == 3) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + day_of_month - 1;
		} else if (month == 4) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + day_of_month - 1;
		} else if (month == 5) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + day_of_month - 1;
		} else if (month == 6) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + day_of_month - 1;
		} else if (month == 7) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + day_of_month - 1;
		} else if (month == 8) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + 31 + day_of_month - 1;
		} else if (month == 9) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + 31 + 31 + day_of_month - 1;
		} else if (month == 10) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + 31 + 31 + 30 + day_of_month - 1;
		} else if (month == 11) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + day_of_month - 1;
		} else if (month == 12) {
			doy = 31 + 28 + (is_leap_year() ? 1 : 0) + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + day_of_month - 1;
		}
		
		return doy;
	}
	
	public int day_of_epoch()
	{
		int day_of_epoc = 0;
		int day_of_year = day_of_year();
		int year_of_epoc = year_of_epoc();
		
		if (0 <= year_of_epoc && year_of_epoc < days_from_epoch.length) {
			day_of_epoc = days_from_epoch[year_of_epoc] + day_of_year;
		} else if (year_of_epoc < 0) {
			day_of_epoc = day_of_year - (is_leap_year(year) ? 366 : 365);
			for (int i=year_of_epoc+1; i < 0; i++) {
				day_of_epoc -= (is_leap_year(year+i) ? 366 : 365);
			}
		} else {
			day_of_epoc = day_of_year + days_from_epoch[days_from_epoch.length - 1];
			for (int i=days_from_epoch.length; i < year_of_epoc; i++) {
				day_of_epoc += (is_leap_year(start_of_epoch.year + i) ? 366 : 365);
			}
		}
		
		return day_of_epoc;
	}

	@Override
	public int compareTo(Date date) 
	{
		int rv = year - date.year;
		if (rv == 0) {
			rv = month - date.month;
			if (rv == 0) {
				rv = day_of_month - date.day_of_month;
			}
		}
		
		return rv;
	}
		
	public void print(PrintStream out)
	{
		out.print(month);
		out.print("/");
		out.print(day_of_month);
		out.print("/");
		out.print(year);
	}
	
	public void println(PrintStream out)
	{
		print(out);
		out.println();
	}
	
	public String toString()
	{
		return month + "/" + day_of_month + "/" + year;
	}
	
	public int days(Date y)
	{
		return days(this, y);
	}
	
	public double years(Date y)
	{
		return years(this, y);
	}
	
	public static int days(Date x, Date y)
	{
		int days = (y.day_of_epoch() - x.day_of_epoch());

		return (0 < days) ? days : -days ;
	}
	
	public static double years(Date x, Date y)
	{
		return days(x,y)/days_per_year;
	}
	
	public boolean lt(Date y)
	{
		return lt(this, y);
	}
	
	public boolean le(Date y)
	{
		return le(this, y);
	}
	
	public boolean eq(Date y)
	{
		return eq(this, y);
	}
	
	public boolean ne(Date y)
	{
		return ne(this, y);
	}
	
	public boolean gt(Date y)
	{
		return gt(this, y);
	}
	
	public boolean ge(Date y)
	{
		return ge(this, y);
	}
	
	// x < y
	public static boolean lt(Date x, Date y)
	{
		if (x == null || y == null) {
			return false;
		} else if (x.year < y.year) {
			return true;
		} else if (x.year == y.year && x.month < y.month) {
			return true;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month < y.day_of_month) {
			return true;
		} else {
			return false;
		}
	}
	
	// x <= y
	public static boolean le(Date x, Date y)
	{
		if (x == null && y == null) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else if (x.year < y.year) {
			return true;
		} else if (x.year == y.year && x.month < y.month) {
			return true;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month <= y.day_of_month) {
			return true;
		} else {
			return false;
		}
	}
	
	// x == y
	public static boolean eq(Date x, Date y)
	{
		if (x == null && y == null) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month == y.day_of_month) {
			return true;
		} else {
			return false;
		}
	}
	
	// x != y
	public static boolean ne(Date x, Date y)
	{
		if (x == null && y == null) {
			return false;
		} else if (x == null || y == null) {
			return true;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month == y.day_of_month) {
			return false;
		} else {
			return true;
		}
	}
	
	// x > y
	public static boolean gt(Date x, Date y)
	{
		if (x == null || y == null) {
			return false;
		} else if (x.year > y.year) {
			return true;
		} else if (x.year == y.year && x.month > y.month) {
			return true;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month > y.day_of_month) {
			return true;
		} else {
			return false;
		}
	}
	
	// x >= y
	public static boolean ge(Date x, Date y)
	{
		if (x == null && y == null) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else if (x.year > y.year) {
			return true;
		} else if (x.year == y.year && x.month > y.month) {
			return true;
		} else if (x.year == y.year && x.month == y.month && x.day_of_month >= y.day_of_month) {
			return true;
		} else {
			return false;
		}
	}
	
	// calculate whether each year in the epoch is a leap year
	private static boolean[] calc_leap()
	{
		boolean[] leap = new boolean[1 + end_of_epoch.year - start_of_epoch.year];
		
		for (int i=1; i < leap.length; i++) {
			int year = start_of_epoch.year + i;
			leap[i] = is_leap_year(year);
		}
		
		return leap;
	}
	
	// calculate the number of days since the start of the epoch for each year
	private static int[] calc_epoc()
	{
		int[] days = new int[1 + end_of_epoch.year - start_of_epoch.year];
		
		days[0] = 0;
		for (int i=1; i < days.length; i++) {
			int year = start_of_epoch.year + i;
			days[i] = days[i-1] + (is_leap_year(year-1) ? 366 : 365);
		}
		
		return days;
	}
}
