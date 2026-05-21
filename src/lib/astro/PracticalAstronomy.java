package lib.astro;

/*******************************************************************************
 * Copyright (c) 2025 Douglas M. Pase                                          *
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


import java.util.Calendar;
import java.util.TimeZone;

import lib.astro.CelestialCalculations.DMS;
import lib.astro.CelestialCalculations.HMS;
import lib.matrix.Matrix;
import nightskyataglance.NightSkyAtAGlance;

public class PracticalAstronomy {

	public static final double hours_per_day      =   24;
	public static final double minutes_per_hour   =   60;
	public static final double minutes_per_day    =   hours_per_day * minutes_per_hour;
	public static final double seconds_per_minute =   60;
	public static final double seconds_per_hour   =   seconds_per_minute * minutes_per_hour;
	public static final double seconds_per_day    =   seconds_per_hour * hours_per_day;
	public static final double millis_per_second  = 1000;
	public static final double millis_per_minute  =   millis_per_second * seconds_per_minute;
	public static final double millis_per_hour    =   millis_per_second * seconds_per_hour;
	public static final double millis_per_day     =   millis_per_second * seconds_per_day;

	public static final double minutes_per_degree =   60;
	public static final double seconds_per_degree =   seconds_per_minute * minutes_per_degree;
	public static final double millis_per_degree  =   millis_per_minute  * minutes_per_degree;

	public static final double degrees_per_radian =  180 / Math.PI;

	public static final double degrees_per_hour   =  360 / 24;

	public static final String degree_symbol = new String(Character.toChars(0x00B0));
	
	/* https://archive.stsci.edu/dss/dss_help.html#coordinates

	Decimal Degrees
        185.63325 29.8959861111111

    Hours, minutes and Seconds
        12 22 31.98      29 53 45.55
        12h22m31.98s     29d53m45.55s
        12:22:31.98     +29:53:45.55
        12h22'31.98"     29d53'45.55"
        12h 22m 31.98s   29d 53m 45.55s
        12h 22' 31.98"   29d 53' 45.55"
        12h 22' 31.98"  -29d 53' 45.55"
        12h22'31".98    -29d53'45".55
        12h22m31s.98    -29o53m45s.55
        12h 22' 31".98  -29d 53' 45".55
    
    Hours/Degrees and Minutes (no seconds)
        12 22     29 53
        12h22m   +29d53m
        12h22m    29d53m
        12:22m    29:53m
        12h22'    29d53'
        12h 22m   29d 53m
        12h 22'   29d 53'
        12h 22'  -29d 53'

    The RA may be given in decimal degrees by indicating
    a D or d after the degrees:
        12d 22m   29d 53m

	Spacing is not important, as long as the value is unambiguous, and that you can 
	delimit the hours/degrees, minutes, and (optional) seconds with letters, colons, 
	spaces, or any character that's not a digit or a decimal point.

	Note also that seconds of the form 31".98 or 31s.98 are accepted. This should 
	make it easy to cut and paste values into these fields from electronic publications. 
	 */

	// returns the right ascension in decimal hours
	public static final double parse_right_ascension(String str)
	{
		str = str.trim();
		if (str == null || str.equals("")) {
			return 0;
		}

		if (str.matches("[0-9][0-9]*([.][0-9]*)?")) {																			// 185.63325
			// Decimal Degrees
			double ddeg = Double.parseDouble(str);
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

			// Hours, Minutes, and Seconds
		} else if (str.matches("[0-9][0-9]*[ ][ ]*[0-9][0-9]*[ ][ ]*[0-9][0-9]*")) {											// 12 22 32.85
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Hh][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9]([.][0-9]*)?[Ss\"]")) {						// 12h 22m 31.98s or 12h22m31.98s or 12h22'31.98"
			str = str.replaceAll("[ ][ ]*", " ").replaceAll("[HhMm'Ss\"]", "");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Hh][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9][Ss\"]([.][0-9]*)?")) {						// 12h 22m 31s.98 or 12h22m31s.98 or 12h22'31".98
			str = str.replaceAll("[ ][ ]*", " ").replaceAll("[HhMm'Ss\"]", "");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[:][0-9][0-9]*[:][0-9][0-9]([.][0-9]*)?")) {											// 12:22:31.98
			str = str.replaceAll("[ ]*", "");
			String[] fields = str.split("[:]");
			int hrs = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			return dhrs;

			// Hours and Minutes
		} else if (str.matches("[0-9][0-9]*[ ][ ]*[0-9][0-9]*")) {																// 12 22.98
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Hh][ ]*[0-9][0-9]([.][0-9]*)?[Mm']")) {												// 12h 2.982m or 12h22.98m or 12h22.98'
			str = str.replaceAll("[HhMm']", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Hh][ ]*[0-9][0-9][Mm']([.][0-9]*)?")) {												// 12h 2m.982 or 12h22m.98 or 12h22'.98
			str = str.replaceAll("[Mm']", "").replaceAll("[Hh]", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[:][0-9][0-9]([.][0-9]*)?")) {														// 12:22.98
			str = str.replaceAll("[ ]*", "");
			String[] fields = str.split("[:]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Hh]([.][0-9]*)?")) {																// 12h.98
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Hh]", "");
			double dhrs = Double.parseDouble(str.trim());
			return dhrs;

		} else if (str.matches("[0-9][0-9]*([.][0-9]*)?[Hh]")) {																// 12.98h
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Hh]", "");
			double dhrs = Double.parseDouble(str.trim());
			return dhrs;

			// Degrees, Minutes, and Seconds
		} else if (str.matches("[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9]*([.][0-9]*)?[Ss\"]")) {	// 12d 22m 31.98s or 12d22m31.98s or 12d22'31.98"
			str = str.replaceAll("[Dd" + degree_symbol + "Mm'Ss\"]", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int deg = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9][Ss\"]([.][0-9]*)?")) {	// 12d 22m 31s.98 or 12d22m31s.98 or 12d22'31".98
			str = str.replaceAll("[Ss\"]", "").replaceAll("[Dd" + degree_symbol + "Mm']", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int deg = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

			// Degrees and Minutes
		} else if (str.matches("[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*([.][0-9]*)?[Mm']")) {						// 12d 22.98m or 12d22.98m or 12d22.98'
			str = str.replaceAll("[Mm']", "").replaceAll("[Dd" + degree_symbol + "]", " ").replaceAll("[ ][ ]*", " ").trim();
			String[] fields = str.split("[ ]");
			int deg = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9][Mm']([.][0-9]*)?")) {						// 12d 22m.98 or 12d22m.98 or 12d22'.98
			str = str.replaceAll("[Mm']", "").replaceAll("[Dd" + degree_symbol + "]", " ").replaceAll("[ ][ ]*", " ").trim();
			String[] fields = str.split("[ ]");
			int deg = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*([.][0-9]*)?[Dd" + degree_symbol + "]")) {											// 185.98d
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Hh]", "");
			double ddeg = Double.parseDouble(str.trim());
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;

		} else if (str.matches("[0-9][0-9]*[Dd" + degree_symbol + "]([.][0-9]*)?")) {											// 185d.98
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Dd]", "");
			double ddeg = Double.parseDouble(str.trim());
			double dhrs = degrees_to_hours(ddeg);
			return dhrs;
		}

		return Double.NaN;
	}

	// returns the declination in decimal degrees
	public static final double parse_declination(String str)
	{
		str = str.trim();
		if (str == null || str.equals("")) {
			return 0;
		}
		

		if (str.matches("[+-]?[0-9][0-9]*([.][0-9]*)?")) {																			// 29.8959861111111
			// Decimal Degrees
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			double ddeg = Double.parseDouble(str);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*([.][0-9]*)?[Dd" + degree_symbol + "]")) {											// 29.8959861111111d
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Dd" + degree_symbol + "]", "");
			double ddeg = Double.parseDouble(str);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "]([.][0-9]*)?")) {											// 29d.8959861111111
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Dd" + degree_symbol + "]", "");
			double ddeg = Double.parseDouble(str);
			return sign * ddeg;

			// Degrees, Minutes, and Seconds
		} else if (str.matches("[+-]?[0-9][0-9]*[ ][ ]*[0-9][0-9]*[ ][ ]*[0-9][0-9]*([.][0-9]*)?")) {								// 12 22 32.85
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9]([.][0-9]*)?[Ss\"]")) {	// 12d 22m 31.98s or 12d22m31.98s or 12d22'31.98"
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Dd" + degree_symbol + "Mm'Ss\"]", " ").trim();
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9][Ss\"]([.][0-9]*)?")) {	// 12d 22m 31.98s or 12d22m31.98s or 12d22'31.98"
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Dd" + degree_symbol + "Mm']", " ").trim();
			str = str.replaceAll("[Ss\"]", "").trim();
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int deg = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[:][0-9][0-9]*[:][0-9][0-9]([.][0-9]*)?")) {										// 12:22:31.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ]*", "");
			String[] fields = str.split("[:]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

			// Degrees and Minutes
		} else if (str.matches("[+-]?[0-9][0-9]*[ ][ ]*[0-9][0-9]*")) {																// 12 22.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double ddeg = dms_to_decimal_degrees(deg, min);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[:][0-9][0-9]([.][0-9]*)?")) {														// 12:22.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ]*", "");
			String[] fields = str.split("[:]");
			int    deg = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double ddeg = dms_to_decimal_degrees(deg, min);
			return sign * ddeg;

			// Degrees, Minutes, and Seconds
		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9]*([.][0-9]*)?[Ss\"]")) {	// 12d 22m 31.98s or 12d22m31.98s or 12d22'31.98"
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Dd" + degree_symbol + "Mm'Ss\"]", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9][Ss\"]([.][0-9]*)?")) {	// 12d 22m 31s.98 or 12d22m31s.98 or 12d22'31".98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Ss\"]", "").replaceAll("[Dd" + degree_symbol + "Mm']", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min, sec);
			return sign * ddeg;

			// Degrees and Minutes
		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9]*([.][0-9]*)?[Mm']")) {						// 12d 22.98m or 12d22.98m or 12d22.98'
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Mm']", "").replaceAll("[Dd" + degree_symbol + "]", " ").replaceAll("[ ][ ]*", " ").trim();
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double ddeg = dms_to_decimal_degrees(deg, min);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Dd" + degree_symbol + "][ ]*[0-9][0-9][Mm']([.][0-9]*)?")) {						// 12d 22m.98 or 12d22m.98 or 12d22'.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Mm']", "").replaceAll("[Dd" + degree_symbol + "]", " ").replaceAll("[ ][ ]*", " ").trim();
			String[] fields = str.split("[ ]");
			int    deg = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double fra = Double.parseDouble(fields[2].trim());
			double ddeg = dms_to_decimal_degrees(deg, min+fra);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Hh][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9]([.][0-9]*)?[Ss\"]")) {						// 12h 22m 31.98s or 12h22m31.98s or 12h22'31.98"
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[HhMm'Ss\"]", " ").trim();
			str = str.replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int    hrs = Integer.parseInt(fields[0].trim());
			int    min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Hh][ ]*[0-9][0-9]*[Mm'][ ]*[0-9][0-9][Ss\"]([.][0-9]*)?")) {						// 12h 22m 31s.98 or 12h22m31s.98 or 12h22'31".98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ][ ]*", " ").replaceAll("[HhMm'Ss\"]", "");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			int min = Integer.parseInt(fields[1].trim());
			double sec = Double.parseDouble(fields[2].trim());
			double dhrs = hms_to_decimal_hours(hrs, min, sec);
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Hh][ ]*[0-9][0-9]([.][0-9]*)?[Mm']")) {											// 12h 2.982m or 12h22.98m or 12h22.98'
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[HhMm']", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Hh][ ]*[0-9][0-9][Mm']([.][0-9]*)?")) {											// 12h 2m.982 or 12h22m.98 or 12h22'.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[Mm']", "").replaceAll("[Hh]", " ").replaceAll("[ ][ ]*", " ");
			String[] fields = str.split("[ ]");
			int hrs = Integer.parseInt(fields[0].trim());
			double min = Double.parseDouble(fields[1].trim());
			double dhrs = hms_to_decimal_hours(hrs, min);
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*[Hh]([.][0-9]*)?")) {																// 12h.98
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Hh]", "");
			double dhrs = Double.parseDouble(str.trim());
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;

		} else if (str.matches("[+-]?[0-9][0-9]*([.][0-9]*)?[Hh]")) {																// 12.98h
			int sign = (str.charAt(0) == '-') ? -1 : 1;
			str = str.replaceAll("[+-]", "");
			str = str.replaceAll("[ ][ ]*", "").replaceAll("[Hh]", "");
			double dhrs = Double.parseDouble(str.trim());
			double ddeg = hours_to_degrees(dhrs);
			return sign * ddeg;
		}

		return Double.NaN;
	}

	public static final double hms_to_decimal_hours(double hrs, double min, double sec, double ms)
	{
		return hrs + min / minutes_per_hour + sec / seconds_per_hour + ms / millis_per_hour;
	}

	public static final double hms_to_decimal_hours(double hrs, double min, double sec)
	{
		return hrs + min / minutes_per_hour + sec / seconds_per_hour;
	}

	public static final double hms_to_decimal_hours(double hrs, double min)
	{
		return hrs + min / minutes_per_hour;
	}

	public static final double dms_to_decimal_degrees(double deg, double min, double sec)
	{
		boolean negative = (deg < 0) || (deg == 0 && min < 0) || (deg == 0 && min == 0 && sec < 0);
		return (negative?-1:1) * (Math.abs(deg) + Math.abs(min / minutes_per_degree) + Math.abs(sec / seconds_per_degree));
	}

	public static final double dms_to_decimal_degrees(double deg, double min)
	{
		boolean negative = (deg < 0) || (deg == 0 && min < 0);
		return (negative?-1:1) * (Math.abs(deg) + Math.abs(min / minutes_per_degree));
	}

	public static final double hours_to_degrees(double hrs, double min, double sec)
	{
		return hours_to_degrees( hms_to_decimal_hours(hrs, min, sec) );
	}

	public static final double hours_to_degrees(double hrs, double min)
	{
		return hours_to_degrees( hms_to_decimal_hours(hrs, min) );
	}

	public static final double hours_to_degrees(double hrs)
	{
		return hrs * degrees_per_hour;
	}

	public static final double hours_to_radians(double hrs, double min, double sec)
	{
		return hours_to_radians( hms_to_decimal_hours(hrs, min, sec) );
	}

	public static final double hours_to_radians(double hrs, double min)
	{
		return hours_to_radians( hms_to_decimal_hours(hrs, min) );
	}

	public static final double hours_to_radians(double hrs)
	{
		return Math.toRadians(hours_to_degrees(hrs));
	}

	public static final double degrees_to_hours(double deg)
	{
		return deg / degrees_per_hour;
	}

	public static final double degrees_to_radians(double deg)
	{
		return Math.toRadians(deg);
	}

	public static final double radians_to_hours(double rad)
	{
		return degrees_to_hours( Math.toDegrees(rad) );
	}

	public static final double radians_to_degrees(double rad)
	{
		return Math.toDegrees(rad);
	}

	public static final double adjust360(double v)
	{
		return mod(v, 360);
	}

	public static final double adjust90(double v)
	{
		v += 90;
		v  = mod(v, 180);
		v -= 90;
		
		return v;
	}

	public static final double adjust24(double v)
	{
		return mod(v, 24);
	}

	public static final double mod(double v, double b)
	{
		v -= b * ((int) (v / b));
		v += (v < 0) ? b : 0;
		
		return v;
	}

	public static final int degree_of_decimal_degrees(double decimal_degrees)
	{
		return (int) decimal_degrees;
	}

	public static final int minute_of_decimal_degrees(double decimal_degrees)
	{
		return (int) ((int)(minutes_per_degree * decimal_degrees) % minutes_per_degree);
	}

	public static final double second_of_decimal_degrees(double decimal_degrees)
	{
		return seconds_per_minute * Round.FRAC(minutes_per_hour * decimal_degrees);
	}


	public static final int hour_of_decimal_hours(double decimal_hours)
	{
		return (int) decimal_hours;
	}

	public static final int minute_of_decimal_hours(double decimal_hours)
	{
		return (int) (minutes_per_hour * Round.FRAC(decimal_hours));
	}

	public static final double second_of_decimal_hours(double decimal_hours)
	{
		return seconds_per_minute * Round.FRAC(minutes_per_hour * decimal_hours);
	}

	public static final String decimal_hours_to_str_hms(double decimal_hours)
	{
		if (0 <= decimal_hours) {
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millis_per_hour) / millis_per_hour;
			int h = (int) adjust24(hour_of_decimal_hours(decimal_hours));
			int m = minute_of_decimal_degrees(decimal_hours);
			double s = second_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%05.2f%s", s, "s");

			return hs + " " + ms + " " + ss;
		} else {
			decimal_hours += 24;
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millis_per_hour) / millis_per_hour;
			int h = (int) adjust24(hour_of_decimal_hours(decimal_hours));
			int m = minute_of_decimal_degrees(decimal_hours);
			double s = second_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%05.2f%s", s, "s");

			return hs + " " + ms + " " + ss;
		}
	}

	public static final String decimal_hours_to_str_hm(double decimal_hours)
	{
		if (0 <= decimal_hours) {
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millis_per_hour) / millis_per_hour;
			int h = (int) adjust24(hour_of_decimal_hours(decimal_hours));
			int m = minute_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");

			return hs + " " + ms;
		} else {
			decimal_hours += 24;
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millis_per_hour) / millis_per_hour;
			int h = (int) adjust24(hour_of_decimal_hours(decimal_hours));
			int m = minute_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");

			return hs + " " + ms;
		}
	}

	public static final String decimal_degrees_to_str_dms(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			int s = (int) second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%02d%s", s, "\"");
			return String.format("+%s %s %s", ds, ms, ss);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			int s = (int) second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%02d%s", s, "\"");
			return String.format("-%s %s %s", ds, ms, ss);
		}
	}

	public static final String decimal_degrees_to_str_dms3(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%06.3f%s", s, "\"");
			return String.format("+%s %s %s", ds, ms, ss);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%06.3f%s", s, "\"");
			return String.format("-%s %s %s", ds, ms, ss);
		}
	}

	public static final String decimal_degrees_to_sstr_dms(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, "d");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%02.0f%s", s, "s");
			return String.format("+%s %s %s", ds, ms, ss);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, "d");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%02.0f%s", s, "s");
			return String.format("-%s %s %s", ds, ms, ss);
		}
	}

	public static final String decimal_degrees_to_sstr_dms3(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, "d");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%06.3f%s", s, "s");
			return String.format("+%s %s %s", ds, ms, ss);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			double s = second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, "d");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%06.3f%s", s, "s");
			return String.format("-%s %s %s", ds, ms, ss);
		}
	}

	public static final String decimal_degrees_to_str_dm(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			return String.format("+%s %s", ds, ms);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millis_per_degree) / millis_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%02d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			return String.format("-%s %s", ds, ms);
		}
	}

	
	// Practical Astronomy with Your Calculator, by Peter Duffett-Smith, 3rd Ed., 1988
	
	// Time, page 1

	// 4. Julian day numbers, page 6
	// this is the julian day in the specified time zone
	public static double julian_day_number(long millis, TimeZone timezone)
	{
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.setTimeInMillis(millis);

		int year            = c.get(Calendar.YEAR);
		int month           = c.get(Calendar.MONTH) + 1;
		int day_of_month    = c.get(Calendar.DAY_OF_MONTH);
		int hour            = c.get(Calendar.HOUR_OF_DAY);
		int minute          = c.get(Calendar.MINUTE);
		int second          = c.get(Calendar.SECOND);
		int ms              = c.get(Calendar.MILLISECOND);
		double decimal_hour = hms_to_decimal_hours(hour, minute, second, ms);
		double decimal_day  = day_of_month + decimal_hour / hours_per_day;
		if (0 < month && month <= 2 ) {
			year  -= 1;
			month += 12;
		}

		final int B;
		if (1582 < year || (year == 1582 && 10 < month) || (year == 1582 && month == 10 && 15 <= day_of_month)) {
			int A = (int) (year / 100);
			B = 2 - A + (int) (A/4);
		} else {
			B = 0;
		}

		final int C;
		if (year < 0) {
			C = (int) ((365.25 * year) - 0.75);
		} else {
			C = (int) (365.25 * year);
		}

		final int D = (int) (30.6001 * (month + 1));
		final double JD = B + C + D + decimal_day + 1720994.5;

		return JD;
	}

	// this is the julian day in the local time zone
	public static double julian_day_number(long millis)
	{
		TimeZone timezone = TimeZone.getDefault();
		return julian_day_number(millis, timezone);
	}
	
	// 9. Converting the local time to UT
	public static final long local_civil_time_to_universal_time(long ms, TimeZone tz)
	{
		Calendar c = Calendar.getInstance(tz);
		c.setTimeInMillis(ms);
		TimeZone zulu = TimeZone.getTimeZone("GMT");
		c.setTimeZone(zulu);

		return 0;
	}

	// 10. Converting UT to local civil time

	// 12. Conversion of UT to GST
	public static final double local_civil_to_greenwich_sidereal_time(long ms, TimeZone tz)
	{
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		Calendar c = Calendar.getInstance(gmt);
		c.setTimeInMillis(ms);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		final double JD = julian_day_number(c.getTimeInMillis(), gmt);
		final double S = JD - 2451545.0;
		final double T = S/36525;
		final double T0 = adjust24(6.697374558 + 2400.051336 * T + 0.000025862 * T * T);

		c.setTimeInMillis(ms);
		final double UT = c.get(Calendar.HOUR_OF_DAY) + (c.get(Calendar.MINUTE) + (c.get(Calendar.SECOND) + c.get(Calendar.MILLISECOND) / millis_per_second) / seconds_per_minute) / minutes_per_hour;
		final double GST = adjust24(1.002737909 * UT + T0);

		return GST;
	}

	// 13. Conversion of GST to UT
	public static final long greenwich_sidereal_to_civil_time(double greenwich_sidereal_time, long ms)
	{
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		Calendar c = Calendar.getInstance(gmt);
		c.setTimeInMillis(ms);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		final double JD = julian_day_number(c.getTimeInMillis(), gmt);
		final double S = JD - 2451545.0;
		final double T = S/36525;
		final double T0 = adjust24(6.697374558 + 2400.051336 * T + 0.000025862 * T * T);
		final double GST = greenwich_sidereal_time;
		final double LCT = adjust24(0.9972695663 * (GST - T0));

		c.set(Calendar.HOUR_OF_DAY,  hour_of_decimal_hours(LCT));
		c.set(Calendar.MINUTE,       minute_of_decimal_hours(LCT));
		c.set(Calendar.SECOND, (int) second_of_decimal_hours(LCT));

		return c.getTimeInMillis();
	}

	// 14. Converting GST to LST
	public static final double greenwich_sidereal_to_local_sidereal_time(double greenwich_sidereal_time, double longitude)
	{
		double GST_hrs = greenwich_sidereal_time;
		double lon_deg = longitude;
		double lon_hrs = degrees_to_hours(lon_deg);
		double LST_hrs = adjust24(GST_hrs + lon_hrs);

		return LST_hrs;
	}
	
	// 15. Converting LST to GST
	public static final double local_sidereal_to_greenwich_sidereal_time(double local_sidereal_time, double longitude)
	{
		double LST_hrs = local_sidereal_time;
		double lon_deg = longitude;
		double lon_hrs = degrees_to_hours(lon_deg);
		double GST_hrs = adjust24(LST_hrs - lon_hrs);

		return GST_hrs;
	}

	// Coordinate systems, page 25
	
	// 27. Ecliptic to equatorial coordiante systems, page 40
	public static final class EclipticToEquatorial {
		public final double   right_ascension;		// right ascension (hours)
		public final double   declination;			// declination (degrees)
		public final long     epoch;				// epoch
		public final TimeZone timezone;				// time zone of the epoch

		public EclipticToEquatorial(double ecliptic_longitude_deg, double ecliptic_latitude_deg, long ms, TimeZone tz)
		{
			timezone = tz;
			epoch    = ms;

			final double beta_deg    = ecliptic_latitude_deg;
			final double beta_rad    = degrees_to_radians(beta_deg);
			final double lambda_deg  = ecliptic_longitude_deg;
			final double lambda_rad  = degrees_to_radians(lambda_deg);
			final double epsilon_deg = obliquity(epoch, timezone);
			final double epsilon_rad = degrees_to_radians(epsilon_deg);
			final double alpha_rad   = Math.atan2(Math.sin(lambda_rad) * Math.cos(epsilon_rad) - Math.tan(beta_rad) * Math.sin(epsilon_rad), Math.cos(lambda_rad));
			final double alpha_hrs   = radians_to_hours(alpha_rad);
			final double delta_rad   = Math.asin(Math.sin(beta_rad) * Math.cos(epsilon_rad) + Math.cos(beta_rad) * Math.sin(epsilon_rad) * Math.sin(lambda_rad));
			final double delta_deg   = radians_to_degrees(delta_rad);

			right_ascension = alpha_hrs;
			declination     = delta_deg;
		}

		public EclipticToEquatorial(double ecliptic_longitude_deg, double ecliptic_latitude_deg)
		{
			timezone = TimeZone.getTimeZone("GMT");
			Calendar c1980 = Calendar.getInstance(timezone);
			c1980.set(1980, Calendar.JANUARY, 26, 11, 43, 57);
			epoch = c1980.getTimeInMillis();

			final double beta_deg    = ecliptic_latitude_deg;
			final double beta_rad    = degrees_to_radians(beta_deg);
			final double lambda_deg  = ecliptic_longitude_deg;
			final double lambda_rad  = degrees_to_radians(lambda_deg);
			final double epsilon_deg = 23.441884;							// obliquity for Jan 26, 1980 at 11:43:57 GMT
			final double epsilon_rad = degrees_to_radians(epsilon_deg);
			final double alpha_rad   = Math.atan2(Math.sin(lambda_rad) * Math.cos(epsilon_rad) - Math.tan(beta_rad) * Math.sin(epsilon_rad), Math.cos(lambda_rad));
			final double alpha_hrs   = radians_to_hours(alpha_rad);
			final double delta_rad   = Math.asin(Math.sin(beta_rad) * Math.cos(epsilon_rad) + Math.cos(beta_rad) * Math.sin(epsilon_rad) * Math.sin(lambda_rad));
			final double delta_deg   = radians_to_degrees(delta_rad);

			right_ascension = alpha_hrs;
			declination     = delta_deg;
		}
		
		public static final double obliquity(long ms, TimeZone tz)
		{
			final double JD = julian_day_number(ms, tz);
			final double T = (JD - 2451545.0) / 36525.0;
			final double delta_epsilon_deg = (46.815 * T + 0.0006 * T * T - 0.00181 * T * T * T) / 3600;
			final double epsilon_deg = 23.439292 - delta_epsilon_deg;

			return epsilon_deg;
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}

	// 32. The angle between two celestial objects
	/**
	 * Compute the angle between two locations on the celestial sphere. 
	 * Right ascension coordinates are in hours, declination coordinates are in degrees. 
	 * The result is in degrees.
	 * @param ra1 right ascension of the first point in hours
	 * @param de1 declination of the first point in degrees
	 * @param ra2 right ascension of the second point in hours
	 * @param de2 declination of the second point in degrees
	 * @return the angle separating the first and second points in degrees
	 */
	public static final double angle_between_celestial_objects(double ra1, double de1, double ra2, double de2)
	{
		final double alpha1_hrs = ra1;
		final double alpha1_rad = hours_to_radians(alpha1_hrs);
		final double delta1_deg = de1;
		final double delta1_rad = degrees_to_radians(delta1_deg);
		final double alpha2_hrs = ra2;
		final double alpha2_rad = hours_to_radians(alpha2_hrs);
		final double delta2_deg = de2;
		final double delta2_rad = degrees_to_radians(delta2_deg);

		final double angle_rad = angle_between_celestial_objects_rad(alpha1_rad, delta1_rad, alpha2_rad, delta2_rad);

		return radians_to_degrees(angle_rad);
	}

	/**
	 * Compute the angle between two locations on the celestial sphere. 
	 * Right ascension and declination coordinates are in degrees. 
	 * The result is in degrees.
	 * @param ra1 right ascension of the first point in degrees
	 * @param de1 declination of the first point in degrees
	 * @param ra2 right ascension of the second point in degrees
	 * @param de2 declination of the second point in degrees
	 * @return the angle separating the first and second points in degrees
	 */
	public static final double angle_between_celestial_objects_deg(double ra1, double de1, double ra2, double de2)
	{
		final double alpha1_deg = ra1;
		final double alpha1_rad = degrees_to_radians(alpha1_deg);
		final double delta1_deg = de1;
		final double delta1_rad = degrees_to_radians(delta1_deg);
		final double alpha2_deg = ra2;
		final double alpha2_rad = degrees_to_radians(alpha2_deg);
		final double delta2_deg = de2;
		final double delta2_rad = degrees_to_radians(delta2_deg);

		final double angle_rad = angle_between_celestial_objects_rad(alpha1_rad, delta1_rad, alpha2_rad, delta2_rad);

		return radians_to_degrees(angle_rad);
	}

	/**
	 * Compute the angle between two locations on the celestial sphere. 
	 * Right ascension and declination coordinates are in radians. 
	 * The result is in radians.
	 * @param ra1 right ascension of the first point in radians
	 * @param de1 declination of the first point in radians
	 * @param ra2 right ascension of the second point in radians
	 * @param de2 declination of the second point in radians
	 * @return the angle separating the first and second points in radians
	 */
	public static final double angle_between_celestial_objects_rad(double ra1, double de1, double ra2, double de2)
	{
		final double alpha1_rad = ra1;
		final double delta1_rad = de1;
		final double alpha2_rad = ra2;
		final double delta2_rad = de2;
		
		final double cos_d = Math.sin(delta1_rad) * Math.sin(delta2_rad) + Math.cos(delta1_rad) * Math.cos(delta2_rad) * Math.cos(alpha1_rad - alpha2_rad);

		return Math.acos(cos_d);
	}

	/**
	 * direction_from_co_to_co - direction from celestial object to celestial object
	 * 
	 * @param ra1
	 * @param de1
	 * @param ra2
	 * @param de2
	 * @return
	 */
	public static final double direction_from_co_to_co(double ra1, double de1, double ra2, double de2)
	{
		final double alpha1_hrs = ra1;
		final double alpha1_rad = hours_to_radians(alpha1_hrs);
		final double delta1_deg = de1;
		final double delta1_rad = degrees_to_radians(delta1_deg);
		final double alpha2_hrs = ra2;
		final double alpha2_rad = hours_to_radians(alpha2_hrs);
		final double delta2_deg = de2;
		final double delta2_rad = degrees_to_radians(delta2_deg);

		final double angle_rad = - Math.PI / 2 + Math.atan2((delta2_rad - delta1_rad), (alpha2_rad - alpha1_rad));
		final double angle_deg = radians_to_degrees(angle_rad);

		return angle_deg;
	}


	// 33. Rising and setting
	public static class RiseAndSetTime {
		public final double   right_ascension;		// equatorial right ascension of a star or other object
		public final double   declination;			// equatorial de_B1950 of a star or other object
		public final double   latitude;				// geographic latitude of observer
		public final double   longitude;			// geographic longitude of observer
		public final double   rise_azimuth;			// azimuth of where the object will cross the horizon
		public final double   set_azimuth;			// azimuth of where the object will cross the horizon
		public final long     time_in_millis;		// approximate time/date of observation
		public final boolean  always_visible;		// is the object always visible (always above the horizon)
		public final boolean  rises_and_sets;		// does the object rise and set (sometimes visible, sometimes not)
		public final boolean  never_visible;		// is the object never visible (always below the horizon)
		public final double   local_rise_time;		// local civil time the object rises above the horizon (decimal hours)
		public final double   local_set_time;		// local civil time the object crosses below the horizon (decimal hours)
		public final long     rise_time_in_millis;	// local civil rise time in milliseconds from the start of the epoch 
		public final long     set_time_in_millis;	// local civil set time in milliseconds from the start of the epoch
		public final TimeZone timezone;				// local civil time zone

		// calculate the local civil time of a stellar object rising above and setting below the horizon 
		public RiseAndSetTime(double ra, double dec, double lat, double lon, long ms, TimeZone tz)
		{
			right_ascension = ra;
			declination     = dec;
			latitude        = lat;
			longitude       = lon;
			time_in_millis  = ms;
			timezone        = tz;
			if (0 <= latitude) {
				if ((90 - latitude) <= declination) {
					always_visible      = true;
					rises_and_sets      = false;
					never_visible       = false;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				} else if (declination < (latitude - 90)) {
					always_visible      = false;
					rises_and_sets      = false;
					never_visible       = true;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				}
			} else if (latitude < 0) {
				if (declination <= (-90 - latitude)) {
					always_visible      = true;
					rises_and_sets      = false;
					never_visible       = false;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				} else if ((latitude + 90) < declination) {
					always_visible      = false;
					rises_and_sets      = false;
					never_visible       = true;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				}
			}
			always_visible = false;
			rises_and_sets = true;
			never_visible  = false;
			
			final double alpha_hrs = right_ascension;
			final double delta_deg = declination;
			final double delta_rad = degrees_to_radians(delta_deg);
			final double phi_deg   = latitude;
			final double phi_rad   = degrees_to_radians(phi_deg);
			final double cos_Ar    = Math.sin(delta_rad) / Math.cos(phi_rad);
			final double Ar_rad    = Math.acos(cos_Ar);
			final double Ar_deg    = radians_to_degrees(Ar_rad);
			final double As_deg    = 360 - Ar_deg;

			final double H_rad = Math.acos(- Math.tan(phi_rad) * Math.tan(delta_rad));
			final double H_hrs = radians_to_hours(H_rad);
			final double LSTr  = adjust24(alpha_hrs - H_hrs);
			final double LSTs  = adjust24(alpha_hrs + H_hrs);
			final double GSTr  = local_sidereal_to_greenwich_sidereal_time(LSTr, longitude);
			final double GSTs  = local_sidereal_to_greenwich_sidereal_time(LSTs, longitude);
			final long   UTr   = greenwich_sidereal_to_civil_time(GSTr, time_in_millis);
			final long   UTs   = greenwich_sidereal_to_civil_time(GSTs, time_in_millis);

			Calendar c = Calendar.getInstance(timezone);
			c.clear();
			c.setTimeInMillis(UTr);
			local_rise_time     = c.get(Calendar.HOUR_OF_DAY) + (c.get(Calendar.MINUTE) + (c.get(Calendar.SECOND) + c.get(Calendar.MILLISECOND) / millis_per_second) / seconds_per_minute) / minutes_per_hour;

			c.clear();
			c.setTimeInMillis(UTs);
			local_set_time      = c.get(Calendar.HOUR_OF_DAY) + (c.get(Calendar.MINUTE) + (c.get(Calendar.SECOND) + c.get(Calendar.MILLISECOND) / millis_per_second) / seconds_per_minute) / minutes_per_hour;

			rise_azimuth        = Ar_deg;
			set_azimuth         = As_deg;
			rise_time_in_millis = UTr;
			set_time_in_millis  = UTs;
		}

		public String toString()
		{
			if (always_visible) {
				// always visible
				return "always visible";
			} else if (never_visible) {
				// never visible
				return "never visible";
			}
			
			// rises and sets
			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(rise_time_in_millis);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			return String.format("rise %s, set %s, %02d/%02d/%04d, %s%s", 
					CelestialCalculations.decimal_hours_to_string(local_rise_time), 
					CelestialCalculations.decimal_hours_to_string(local_set_time),
					month, day_of_month, year,
					timezone.getDisplayName(),
					(c.get(Calendar.DST_OFFSET) != 0) ? " (DST)" : "");
		}
	}

	// 34. Precession, page 56
	public static final class Precession {
		public final double   right_ascension;		// right ascension
		public final double   declination;			// declination
		public final long     epoch;				// epoch
		public final TimeZone timezone;				// time zone of the epoch

		// convert RA,DEC from epoch1 (e.g., B1950) to epoch2 (e.g., J2000)
		public Precession(double ra, double dec, long epoch1, long epoch2, TimeZone tz)
		{
			timezone = tz;
			final double JD_1          = julian_day_number(epoch1, timezone);			// julian date of epoch 1
			final double T_1           = (JD_1 - 2451545) / 36525;
			final double zeta_A_1_deg  = 0.6406161 * T_1 + 0.0000839 * T_1 * T_1 + 0.0000050 * T_1 * T_1 * T_1;
			final double z_A_1_deg     = 0.6406161 * T_1 + 0.0003041 * T_1 * T_1 + 0.0000051 * T_1 * T_1 * T_1;
			final double theta_A_1_deg = 0.5567530 * T_1 - 0.0001185 * T_1 * T_1 - 0.0000116 * T_1 * T_1 * T_1;
			final double zeta_A_1_rad  = degrees_to_radians(zeta_A_1_deg);
			final double z_A_1_rad     = degrees_to_radians(z_A_1_deg);
			final double theta_A_1_rad = degrees_to_radians(theta_A_1_deg);
			final double CX_1          = Math.cos(zeta_A_1_rad);
			final double SX_1          = Math.sin(zeta_A_1_rad);
			final double CZ_1          = Math.cos(z_A_1_rad);
			final double SZ_1          = Math.sin(z_A_1_rad);
			final double CT_1          = Math.cos(theta_A_1_rad);
			final double ST_1          = Math.sin(theta_A_1_rad);
			final double[][] P_1 = {
					{   CX_1 * CT_1 * CZ_1 - SX_1 * SZ_1,   CX_1 * CT_1 * SZ_1 + SX_1 * CZ_1,   CX_1 * ST_1 },
					{ - SX_1 * CT_1 * CZ_1 - CX_1 * SZ_1, - SX_1 * CT_1 * SZ_1 + CX_1 * CZ_1, - SX_1 * ST_1 },
					{ - ST_1 * CZ_1,                      - ST_1 * SZ_1,                        CT_1        },
			};
			final double alpha_1_hrs = ra;
			final double alpha_1_rad = hours_to_radians(alpha_1_hrs);
			final double delta_1_deg = dec;
			final double delta_1_rad = degrees_to_radians(delta_1_deg);
			final double x = Math.cos(alpha_1_rad) * Math.cos(delta_1_rad);
			final double y = Math.sin(alpha_1_rad) * Math.cos(delta_1_rad);
			final double z = Math.sin(delta_1_rad);
			final double[] v = { x, y, z, };
			final double[] s = Matrix.times(P_1, v);

			final double JD_2      = julian_day_number(epoch2, timezone);			// julian date of epoch 2
			final double T_2       = (JD_2 - 2451545) / 36525;
			final double zeta_A_2_deg  = 0.6406161 * T_2 + 0.0000839 * T_2 * T_2 + 0.0000050 * T_2 * T_2 * T_2;
			final double z_A_2_deg     = 0.6406161 * T_2 + 0.0003041 * T_2 * T_2 + 0.0000051 * T_2 * T_2 * T_2;
			final double theta_A_2_deg = 0.5567530 * T_2 - 0.0001185 * T_2 * T_2 - 0.0000116 * T_2 * T_2 * T_2;
			final double zeta_A_2_rad  = degrees_to_radians(zeta_A_2_deg);
			final double z_A_2_rad     = degrees_to_radians(z_A_2_deg);
			final double theta_A_2_rad = degrees_to_radians(theta_A_2_deg);
			final double CX_2          = Math.cos(zeta_A_2_rad);
			final double SX_2          = Math.sin(zeta_A_2_rad);
			final double CZ_2          = Math.cos(z_A_2_rad);
			final double SZ_2          = Math.sin(z_A_2_rad);
			final double CT_2          = Math.cos(theta_A_2_rad);
			final double ST_2          = Math.sin(theta_A_2_rad);
			final double[][] P_2_T = {
					{   CX_2 * CT_2 * CZ_2 - SX_2 * SZ_2,   CX_2 * CT_2 * SZ_2 + SX_2 * CZ_2,   CX_2 * ST_2 },
					{ - SX_2 * CT_2 * CZ_2 - CX_2 * SZ_2, - SX_2 * CT_2 * SZ_2 + CX_2 * CZ_2, - SX_2 * ST_2 },
					{ - ST_2 * CZ_2,                      - ST_2 * SZ_2,                        CT_2        },
			};
			final double[][] P_2 = Matrix.transpose(P_2_T);
			final double[] w = Matrix.times(P_2, s);
			final double m = w[0];
			final double n = w[1];
			final double p = w[2];
			final double alpha_2_rad = Math.atan2(n, m);
			final double alpha_2_deg = radians_to_degrees(alpha_2_rad);
			final double delta_2_rad = Math.asin(p);
			final double delta_2_deg = radians_to_degrees(delta_2_rad);

			right_ascension = degrees_to_hours(alpha_2_deg);
			declination     = delta_2_deg;
			epoch           = epoch2;
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}

	// 35. Nutation, page 60
	public static final class Nutation {
		public final double   longitude;		// nutation in longitude (degrees)
		public final double   obliquity;		// nutation in obliquity (degrees)
		public final long     epoch;			// epoch of the nutation
		public final TimeZone timezone;			// time zone of the epoch
	
		public Nutation(long ms, TimeZone tz)
		{
			timezone = tz;
			epoch    = ms;
			
			final double JD    = julian_day_number(epoch, timezone);
			final double T     = (JD - 2415020.0) / 36525.0;
			final double A     = 100.002136 * T;
			final double L_deg       = adjust360(279.6967 + 360 * (A - (long) A));
			final double L_rad = degrees_to_radians(L_deg);
			final double B     = 5.372617 * T;
			final double OMEGA_deg = adjust360(259.1833 - 360 * (B - (long) B));
			final double OMEGA_rad = degrees_to_radians(OMEGA_deg);
	
			longitude = -17.2 * Math.sin(OMEGA_rad) - 1.3 * Math.sin(2 * L_rad);
			obliquity =   9.2 * Math.cos(OMEGA_rad) + 0.5 * Math.cos(2 * L_rad);
		}

		public String toString()
		{
			return String.format("%f, %f", longitude, obliquity);
		}
	}

	// The Sun, page 83

	// 46 Calculating the position of the sun, page 86
	public static final class SolarLocation {
		public final double   right_ascension;		// apparent solar equatorial right ascension
		public final double   declination;			// apparent solar equatorial declination
		public final double   M_solar_deg;			// mean solar anomaly in degrees
		public final double   lambda_solar_deg;		// solar longitude in degrees
		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone

		public static final double epsilon_g_1990_deg = 279.403303;		// ecliptic longitude at epoch 1990.0
		public static final double omega_g_1990_deg   = 282.768422;		// ecliptic longitude of perigee
		public static final double e                  = 0.016713;		// eccentricity of orbit

		public SolarLocation(long millis, TimeZone tz)
		{
			time_in_millis  = millis;
			timezone        = tz;

			Calendar c1990 = Calendar.getInstance(timezone);
			c1990.clear();
			c1990.set(1990, Calendar.JANUARY, 0, 0, 0, 0);
			c1990.set(Calendar.MILLISECOND, 0);
			final long ms1990 = c1990.getTimeInMillis();
			final double D = (int) ((time_in_millis - ms1990) / millis_per_day);
			final double N = adjust360(D * 360.0 / 365.242191);
			final double Ms_deg = adjust360(N + epsilon_g_1990_deg - omega_g_1990_deg);
			M_solar_deg = Ms_deg;
			final double M_solar_rad = degrees_to_radians(M_solar_deg);
			final double Ec = 360 * e * Math.sin(M_solar_rad) / Math.PI;
			final double ls_deg = adjust360(N + Ec + epsilon_g_1990_deg);
			lambda_solar_deg = ls_deg;

			final EclipticToEquatorial eq = new EclipticToEquatorial(lambda_solar_deg, 0);

			right_ascension = eq.right_ascension;
			declination     = eq.declination;
		}

		public String toString()
		{
			return String.format("%s, %s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination), timezone.getDisplayName());
		}
	}

	// The planets, comets, and binary stars, page 101

	// 54. Calculating the coordinates of a planet, page 103
	public static final class PlanetLocation {
		public static class Table {
			public final double period;				// orbital period (tropical years)
			public final double lon_at_epoch;		// longitude at epoch (degrees)
			public final double lon_of_peri;		// longitude of the perihelion (degrees)
			public final double eccentricity;		// eccentricity of the orbit
			public final double semi_major_axis;	// semi-major axis of the orbit (A.U.)
			public final double inclination;		// inclination of the orbit (degrees)
			public final double lon_asc_node;		// longitude of the ascending node (degrees)
			public final double ang_diam_1au;		// angular diameter at 1 A.U. (arcsec)
			public final double vis_mag_1au;		// visual magnitude at 1 A.U.

			public Table(double p, double le, double lp, double e, double sma, double inc, double lan, double ad, double vm)
			{
				period          = p;
				lon_at_epoch    = le;
				lon_of_peri     = lp;
				eccentricity    = e;
				semi_major_axis = sma;
				inclination     = inc;
				lon_asc_node    = lan;
				ang_diam_1au    = ad;
				vis_mag_1au     = vm;
			}
		}

		public static final int MERCURY = 0; 
		public static final int VENUS   = 1; 
		public static final int EARTH   = 2; 
		public static final int MARS    = 3; 
		public static final int JUPITER = 4; 
		public static final int SATURN  = 5; 
		public static final int URANUS  = 6; 
		public static final int NEPTUNE = 7; 
		public static final int PLUTO   = 8; 
		public static final double NaN = Double.NaN; 
		public static final Table[] planets = {
			//	              Tp     epsilon           m         e          a          i       OMEGA  theta0     V0
			new Table(  0.240852,  60.750646,  77.299833, 0.205633,  0.387099,  7.004540,  48.212740,   6.74, -0.42),	// mercury
			new Table(  0.615211,  88.455855, 131.430236, 0.006778,  0.723332,  3.394535,  76.589820,  16.92, -4.40),	// venus
			new Table(  1.000040,  99.403305, 102.768413, 0.016713,  1.000000,       NaN,        NaN,    NaN,   NaN),	// earth
			new Table(  1.880932, 240.739474, 335.874939, 0.093396,  1.523688,  1.849736,  49.480308,   9.36, -1.52),	// mars
			new Table( 11.863075,  90.638185,  14.170747, 0.048482,  5.202561,  1.303613, 100.353142, 196.74, -9.40),	// jupiter
			new Table( 29.471362, 287.690033,  92.861407, 0.055581,  9.554747,  2.488980, 113.576139, 165.60, -8.88),	// saturn
			new Table( 84.039492, 271.063148, 172.884833, 0.046321, 19.218140,  0.773059,  73.926961,  65.80, -7.19),	// uranus
			new Table(164.792460, 282.349556,  48.009758, 0.009003, 30.109570,  1.770646, 131.670599,  62.20, -6.87),	// neptune
			new Table(246.770270, 221.412700, 224.133000, 0.246240, 39.341400, 17.142000, 110.144000,   8.20, -1.00),	// pluto
		};

		public static class Equatorial {
			public final double right_ascension;
			public final double declination;
			public Equatorial(double ra, double de)
			{
				right_ascension = ra;
				declination     = de;
			}
		}

		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone

		public PlanetLocation(long ms, TimeZone tz)
		{
			time_in_millis = ms;
			timezone       = tz;
		}

		public final Equatorial inner_planet(int planet, long ms, TimeZone tz)
		{
			return null;
		}

		public final Equatorial outer_planet(int planet, long ms, TimeZone tz)
		{
			return null;
		}
	}

	// 55. Finding the approximate positions of the planets, page 111
	public static final class ApproximatePlanetLocation {
		public static class Equatorial {
			public final double right_ascension;
			public final double declination;
			public Equatorial(double ra, double de)
			{
				right_ascension = ra;
				declination     = de;
			}

			public String toString()
			{
				return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
			}
		}

		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone
		public final Equatorial[] locations = new Equatorial[9];

		public static final int MERCURY = 0; 
		public static final int VENUS   = 1; 
		public static final int EARTH   = 2; 
		public static final int MARS    = 3; 
		public static final int JUPITER = 4; 
		public static final int SATURN  = 5; 
		public static final int URANUS  = 6; 
		public static final int NEPTUNE = 7; 
		public static final int PLUTO   = 8; 

		public ApproximatePlanetLocation(long ms, TimeZone tz)
		{
			time_in_millis = ms;
			timezone       = tz;

			locations[MERCURY] = inner_planet(MERCURY, ms, tz);
			locations[VENUS]   = inner_planet(VENUS,   ms, tz);
			locations[EARTH]   = null;
			locations[MARS]    = outer_planet(MARS,    ms, tz);
			locations[JUPITER] = outer_planet(JUPITER, ms, tz);
			locations[SATURN]  = outer_planet(SATURN,  ms, tz);
			locations[URANUS]  = outer_planet(URANUS,  ms, tz);
			locations[NEPTUNE] = outer_planet(NEPTUNE, ms, tz);
			locations[PLUTO]   = outer_planet(PLUTO,   ms, tz);
		}

		public static final Equatorial inner_planet(int planet, long ms, TimeZone tz)
		{
			if (planet < MERCURY || VENUS < planet) {
				return null;
			}

			Calendar c1990 = Calendar.getInstance(tz);
			c1990.clear();
			c1990.set(1990, Calendar.JANUARY, 0, 0, 0, 0);
			c1990.set(Calendar.MILLISECOND, 0);
			long ms1990 = c1990.getTimeInMillis();
			final double D = (int) ((ms - ms1990) / millis_per_day);
			final double l_deg = adjust360((360.0 / 365.242191) * (D / PlanetLocation.planets[planet].period) + PlanetLocation.planets[planet].lon_at_epoch);
			final double l_rad = degrees_to_radians(l_deg);
			final double L_deg = adjust360((360.0 / 365.242191) * (D / PlanetLocation.planets[EARTH].period) + PlanetLocation.planets[EARTH].lon_at_epoch);
			final double L_rad = degrees_to_radians(L_deg);
			final double a     = PlanetLocation.planets[planet].semi_major_axis;
			final double y_rad = a * Math.sin(L_rad - l_rad);
			final double x_rad = 1 - a * Math.cos(L_rad - l_rad);
			final double lambda_rad = Math.PI + L_rad + Math.atan2(y_rad, x_rad);
			final double lambda_deg = radians_to_degrees(lambda_rad);

			final EclipticToEquatorial q = new EclipticToEquatorial(lambda_deg, 0);
			
			return new Equatorial(q.right_ascension, q.declination);
		}

		public static final Equatorial outer_planet(int planet, long ms, TimeZone tz)
		{
			if (planet < MARS || PLUTO < planet) {
				return null;
			}

			Calendar c1990 = Calendar.getInstance(tz);
			c1990.clear();
			c1990.set(1990, Calendar.JANUARY, 0, 0, 0, 0);
			c1990.set(Calendar.MILLISECOND, 0);
			long ms1990 = c1990.getTimeInMillis();
			final double D = (int) ((ms - ms1990) / millis_per_day);
			final double l_deg = adjust360((360.0 / 365.242191) * (D / PlanetLocation.planets[planet].period) + PlanetLocation.planets[planet].lon_at_epoch);
			final double l_rad = degrees_to_radians(l_deg);
			final double L_deg = adjust360((360.0 / 365.242191) * (D / PlanetLocation.planets[EARTH].period) + PlanetLocation.planets[EARTH].lon_at_epoch);
			final double L_rad = degrees_to_radians(L_deg);
			final double y_rad = Math.sin(l_rad - L_rad);
			final double a     = PlanetLocation.planets[planet].semi_major_axis;
			final double x_rad = a - Math.cos(l_rad - L_rad);
			final double lambda_rad = Math.atan2(y_rad, x_rad) + l_rad;
			final double lambda_deg = radians_to_degrees(lambda_rad);

			EclipticToEquatorial q = new EclipticToEquatorial(lambda_deg, 0);
			
			return new Equatorial(q.right_ascension, q.declination);
		}
	}

	// The moon and eclipses, page 138

	// 65. Calculating the moon's position, page 142
	public static final class LunarLocation {
		public final double   right_ascension;		// apparent lunar right ascension
		public final double   declination;			// apparent lunar de_B1950
		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone
		public final SolarLocation sol;				// position of the sun
		public final double   illumination;			// brightness of the moon in the range [0 - 1] 

		public static final double l0_deg = 318.351648;		// moon's mean longitude at epoch Jan 0.0, 1990
		public static final double P0_deg =  36.340410;		// mean longitude of the perigee at the epoch
		public static final double N0_deg = 318.510107;		// Mean longitude of the node at the epoch
		public static final double i_deg  =   5.145396;		// Inclination of the Moon's orbit
		public static final double e      =   0.054900;		// Eccentricity of the Moon's orbit
		public static final double a      = 384401;			// Semi-major axis of the Moon's orbit
		public static final double theta0 =   0.5181;		// Moon's angular size at distance a from the Earth
		public static final double pi0    =   0.9507;		// Parallax at distance a from the Earth

		public LunarLocation(long ms, TimeZone tz)
		{
			time_in_millis = ms;
			timezone       = tz;

			Calendar c1990 = Calendar.getInstance(timezone);
			c1990.set(1990, Calendar.JANUARY, 0, 0, 0, 0);
			long ms1990 = c1990.getTimeInMillis();
			final double D = (time_in_millis - ms1990) / millis_per_day;

			sol = new SolarLocation(time_in_millis, timezone);
			final double lambda_solar_deg = sol.lambda_solar_deg;		// solar longitude in degrees
			final double lambda_solar_rad = degrees_to_radians(lambda_solar_deg);
			final double M_solar_deg      = sol.M_solar_deg;			// mean solar anomaly in degrees
			final double M_solar_rad      = degrees_to_radians(M_solar_deg);
			final double l_deg            = adjust360(13.1763966 * D + l0_deg);
			final double Mm               = adjust360(l_deg - 0.111404 * D - P0_deg);
			final double N_deg            = adjust360(N0_deg - 0.0529539 * D);
			final double C_deg            = l_deg - lambda_solar_deg;
			final double t_deg            = adjust360(2 * C_deg - Mm);
			final double t_rad            = degrees_to_radians(t_deg);
			final double Ev_deg           = 1.2739 * Math.sin(t_rad);
			final double Ac_deg           = 0.1858 * Math.sin(M_solar_rad);
			final double A3_deg           = 0.3700 * Math.sin(M_solar_rad);
			final double Mpm_deg          = Mm + Ev_deg - Ac_deg - A3_deg;
			final double Mpm_rad          = degrees_to_radians(Mpm_deg);
			final double Ec_deg           = 6.2886 * Math.sin(Mpm_rad);
			final double A4_deg           = 0.2140 * Math.sin(2 * Mpm_rad);
			final double lp_deg           = adjust360(l_deg + Ev_deg + Ec_deg - Ac_deg + A4_deg);
			final double lp_rad           = degrees_to_radians(lp_deg);
			final double V_deg            = 0.6583 * Math.sin(2 * (lp_rad - lambda_solar_rad));
			final double lpp_deg          = lp_deg + V_deg;
			final double lpp_rad          = degrees_to_radians(lpp_deg);
			final double Np_deg           = N_deg - 0.16 * Math.sin(M_solar_rad);
			final double Np_rad           = degrees_to_radians(Np_deg);
			final double x                = Math.cos(lpp_rad - Np_rad);
			final double i_rad            = degrees_to_radians(i_deg);
			final double y                = Math.sin(lpp_rad - Np_rad) * Math.cos(i_rad);
			final double lambda_m_deg     = Np_deg + radians_to_degrees(Math.atan2(y, x));
			final double beta_m_rad       = Math.asin(Math.sin(lpp_rad - Np_rad) * Math.sin(i_rad));
			final double beta_m_deg       = radians_to_degrees(beta_m_rad);

			EclipticToEquatorial moon = new EclipticToEquatorial(lambda_m_deg, beta_m_deg, ms, tz);

			right_ascension = adjust24(moon.right_ascension);
			declination     = adjust90(moon.declination);
			
			final double D_rad = lp_rad - lambda_solar_rad;
			illumination    = (1 - Math.cos(D_rad)) / 2;
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}


	// 70. Moonrise and moonset, page 151
	// Note: This is my own iterative refinement method and not from the book
	public static class LunarRiseAndSetTime {
		public final double   latitude;				// geographic latitude of observer
		public final double   longitude;			// geographic longitude of observer
		public final long     time_in_millis;		// approximate time/date of observation
		public final boolean  always_visible;		// is the object always visible (always above the horizon)
		public final boolean  rises_and_sets;		// does the object rise and set (sometimes visible, sometimes not)
		public final boolean  never_visible;		// is the object never visible (always below the horizon)
		public final double   local_rise_time;		// local civil time the object rises above the horizon (decimal hours)
		public final double   local_set_time;		// local civil time the object crosses below the horizon (decimal hours)
		public final long     rise_time_in_millis;	// local civil rise time in milliseconds from the start of the epoch 
		public final long     set_time_in_millis;	// local civil set time in milliseconds from the start of the epoch
		public final TimeZone timezone;				// local civil time zone

		// calculate the local civil time of a stellar object rising above and setting below the horizon 
		public LunarRiseAndSetTime(double lat, double lon, long ms, TimeZone tz)
		{
			latitude        = lat;
			longitude       = lon;
			time_in_millis  = ms;
			timezone        = tz;
			always_visible = false;
			rises_and_sets = true;
			never_visible  = false;

			long   dt_prev = Long.MAX_VALUE;
			double da_prev = Double.MAX_VALUE;
			long   t0      = ms;
			LunarLocation  loc_a = new LunarLocation(t0, tz);
			RiseAndSetTime ras_a = new RiseAndSetTime(loc_a.right_ascension, loc_a.declination, lat, lon, t0, tz);
			LunarLocation  loc_b = new LunarLocation(ras_a.rise_time_in_millis, tz);
			long   dt      = ras_a.rise_time_in_millis - loc_a.time_in_millis;
			double da      = angle_between_celestial_objects(loc_a.right_ascension, loc_a.declination, loc_b.right_ascension, loc_b.declination);
			long   lrt_ms  = ras_a.rise_time_in_millis;
			double lrt     = ras_a.local_rise_time;
			while (0 <= dt && dt < dt_prev && da < da_prev) {
				dt_prev = dt;
				da_prev = da;
				t0      = (loc_a.time_in_millis + ras_a.rise_time_in_millis) / 2;
				lrt_ms  = ras_a.rise_time_in_millis;
				lrt     = ras_a.local_rise_time;

				loc_a = new LunarLocation(t0, tz);
				ras_a = new RiseAndSetTime(loc_a.right_ascension, loc_a.declination, lat, lon, t0, tz);
				loc_b = new LunarLocation(ras_a.rise_time_in_millis, tz);

				dt = ras_a.rise_time_in_millis - loc_a.time_in_millis;
				da = angle_between_celestial_objects(loc_a.right_ascension, loc_a.declination, loc_b.right_ascension, loc_b.declination);

				/*/
				System.out.printf("loc_a=%s%n", loc_a);
				System.out.printf("ras_a=%s%n", ras_a);
				System.out.printf("loc_b=%s%n", loc_b);
				System.out.printf("t=%d dtp=%d dap=%f dt=%d da=%f%n", t0, dt_prev, da_prev, dt, da);
				/*/
			}

			local_rise_time     = lrt;
			rise_time_in_millis = lrt_ms;


			dt_prev = Long.MAX_VALUE;
			da_prev = Double.MAX_VALUE;
			t0      = ms;
			loc_a   = new LunarLocation(t0, tz);
			ras_a   = new RiseAndSetTime(loc_a.right_ascension, loc_a.declination, lat, lon, t0, tz);
			loc_b   = new LunarLocation(ras_a.set_time_in_millis, tz);
			dt      = ras_a.set_time_in_millis - loc_a.time_in_millis;
			da      = angle_between_celestial_objects(loc_a.right_ascension, loc_a.declination, loc_b.right_ascension, loc_b.declination);
			long   lst_ms  = ras_a.set_time_in_millis;
			double lst     = ras_a.local_set_time;
			while (0 <= dt && dt < dt_prev && da < da_prev) {
				dt_prev = dt;
				da_prev = da;
				t0      = (loc_a.time_in_millis + ras_a.set_time_in_millis) / 2;
				lst_ms  = ras_a.set_time_in_millis;
				lst     = ras_a.local_set_time;

				loc_a = new LunarLocation(t0, tz);
				ras_a = new RiseAndSetTime(loc_a.right_ascension, loc_a.declination, lat, lon, t0, tz);
				loc_b = new LunarLocation(ras_a.set_time_in_millis, tz);

				dt = ras_a.set_time_in_millis - loc_a.time_in_millis;
				da = angle_between_celestial_objects(loc_a.right_ascension, loc_a.declination, loc_b.right_ascension, loc_b.declination);
			}

			local_set_time      = lst;
			set_time_in_millis  = lst_ms;
		}

		public String toString()
		{
			if (always_visible) {
				// always visible
				return "always visible";
			} else if (never_visible) {
				// never visible
				return "never visible";
			}
			
			// rises and sets
			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(rise_time_in_millis);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			return String.format("rise %s, set %s, %02d/%02d/%04d, %s%s", 
					CelestialCalculations.decimal_hours_to_string(local_rise_time), 
					CelestialCalculations.decimal_hours_to_string(local_set_time),
					month, day_of_month, year,
					timezone.getDisplayName(),
					(c.get(Calendar.DST_OFFSET) != 0) ? " (DST)" : "");
		}
	}

	public static class LunarPhases {
		public final long     time_in_millis;		// approximate time/date of observation
		public final long     new_moon;				// 
		public final long     first_quarter;		// 
		public final long     full_moon;			// 
		public final long     third_quarter;		// 
		public final TimeZone timezone;				// local civil time zone

		public LunarPhases(long ms, TimeZone tz)
		{
			Calendar c = Calendar.getInstance(tz);
			c.setTimeInMillis(ms);
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+28);

			LunarLocation min_loc = null;
			LunarLocation max_loc = null;
			long sms = ms;
			long ems = c.getTimeInMillis();
			int  intervals = 28000;
			for (int i=0; i <= intervals; i++) {
				long lms = sms + (long) (i * (double) (ems - sms) / (double) intervals);

				LunarLocation loc = new LunarLocation(lms, tz);

				if (min_loc == null || loc.illumination < min_loc.illumination) {
					min_loc = loc;
				}

				if (max_loc == null || max_loc.illumination < loc.illumination) {
					max_loc = loc;
				}
			}
			
			time_in_millis = ms;
			timezone       = tz;

			new_moon       = min_loc.time_in_millis;
			full_moon      = max_loc.time_in_millis;
			
			long fq;
			long tq;
			if (new_moon < full_moon) {
				long delta = (full_moon - new_moon) / 2;
				if (ms < new_moon - delta) {
					fq = new_moon + delta;
					tq = new_moon - delta;
				} else {
					fq = full_moon - delta;
					tq = full_moon + delta;
				}
			} else {	// full_moon < new_moon
				long delta = (new_moon - full_moon) / 2;
				if (ms < full_moon - delta) {
					fq = full_moon - delta;
					tq = full_moon + delta;
				} else {
					fq = new_moon + delta;
					tq = new_moon - delta;
				}
			}

			c.setTimeInMillis(fq);
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)-1);
			sms = c.getTimeInMillis();
			c.setTimeInMillis(fq);
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
			ems = c.getTimeInMillis();
			LunarLocation q01_loc = null;
			for (int i=0; i <= intervals; i++) {
				long lms = sms + (long) (i * (double) (ems - sms) / (double) intervals);

				LunarLocation loc = new LunarLocation(lms, tz);

				if (q01_loc == null || Math.abs(loc.illumination - 0.5) < Math.abs(q01_loc.illumination - 0.5)) {
					q01_loc = loc;
				}
			}

			first_quarter  = q01_loc.time_in_millis;

			c.setTimeInMillis(tq);
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)-2);
			sms = c.getTimeInMillis();
			c.setTimeInMillis(tq);
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+2);
			ems = c.getTimeInMillis();
			LunarLocation q03_loc = null;
			for (int i=0; i <= intervals; i++) {
				long lms = sms + (long) (i * (double) (ems - sms) / (double) intervals);

				LunarLocation loc = new LunarLocation(lms, tz);

				if (q03_loc == null || Math.abs(loc.illumination - 0.5) < Math.abs(q03_loc.illumination - 0.5)) {
					q03_loc = loc;
				}
			}

			third_quarter  = q03_loc.time_in_millis;
		}
	}
	
	public static void main(String[] args)
	{
		/*/
		TimeZone timezone = TimeZone.getTimeZone("US/Mountain");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1985, Calendar.FEBRUARY, 17, 6, 0, 0);
		long time_in_millis = c.getTimeInMillis();
		System.out.println(julian_day_number(time_in_millis, timezone));
		/*/

		/*/
		TimeZone timezone = TimeZone.getTimeZone("US/Mountain");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1950, Calendar.JANUARY, 1, 0, 0, 0);
		long epoch1 = c.getTimeInMillis();
		c.clear();
		c.set(1979, Calendar.JUNE, 1, 0, 0, 0);
		long epoch2 = c.getTimeInMillis();
		double ra_hrs  = hms_to_decimal_hours(9, 10, 43);
		double dec_deg = dms_to_decimal_degrees(14, 23, 25);
		System.out.println(new Precession(ra_hrs, dec_deg, epoch1, epoch2, timezone));

		c.clear();
		c.set(1988, Calendar.SEPTEMBER, 1, 0, 0, 0);
		System.out.println(new Nutation(c.getTimeInMillis(), timezone));

		TimeZone timezone = TimeZone.getTimeZone("US/Eastern");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1980, Calendar.JULY, 27, 0, 0, 0);
		long solar_millis = c.getTimeInMillis();
		System.out.println(new SolarLocation(solar_millis, timezone));

		TimeZone timezone = TimeZone.getTimeZone("GMT");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1979, Calendar.FEBRUARY, 26, 16, 0, 50);
		long lunar_millis = c.getTimeInMillis();
		System.out.println(new LunarLocation(lunar_millis, timezone));

		System.out.println(local_civil_time_to_universal_time(System.currentTimeMillis(), TimeZone.getDefault()));

		tz = TimeZone.getDefault();
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		c.clear();
		c = Calendar.getInstance(tz);
		c.set(1980, Calendar.APRIL, 22, 0, 0, 0);
		ms = c.getTimeInMillis();
		double gst = hms_to_decimal_hours(4, 40, 5);
		ms = greenwich_sidereal_to_local_civil_time(gst, ms, tz);
		c.setTimeZone(gmt);
		c.setTimeInMillis(ms);
		System.out.printf("%02dh %02dm %02d.%03ds%n", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND));

		double local_sidereal_time = hms_to_decimal_hours(0, 24, 5.23);
		double longitude           = -64;
		System.out.println(local_sidereal_to_greenwich_sidereal_time(local_sidereal_time, longitude));
		double greenwich_sidereal_time = hms_to_decimal_hours(4, 40, 5.23);
		System.out.println(greenwich_sidereal_to_local_sidereal_time(greenwich_sidereal_time, longitude));

		TimeZone tz = TimeZone.getTimeZone("GMT");
		double lat = 30;
		double lon = 64;
		Calendar c = Calendar.getInstance(tz);
		c.clear();
		c.set(1980, Calendar.AUGUST, 24, 0, 0, 0);
		long ms = c.getTimeInMillis();
		System.out.println(new LunarRiseAndSetTime(lat, lon, ms, tz));

		TimeZone tz = TimeZone.getDefault();
		long ms = System.currentTimeMillis();
		LunarPhases lp = new LunarPhases(ms, tz);
		Calendar c = Calendar.getInstance(tz);
		c.setTimeInMillis(lp.new_moon);
		System.out.printf("new moon:  %02dh %02dm %02d.%03ds %02d/%02d/%4d%n", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
		c.setTimeInMillis(lp.first_quarter);
		System.out.printf("1st qtr:   %02dh %02dm %02d.%03ds %02d/%02d/%4d%n", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
		c.setTimeInMillis(lp.full_moon);
		System.out.printf("full moon: %02dh %02dm %02d.%03ds %02d/%02d/%4d%n", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
		c.setTimeInMillis(lp.third_quarter);
		System.out.printf("3rd qtr:   %02dh %02dm %02d.%03ds %02d/%02d/%4d%n", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));

		TimeZone tz = TimeZone.getDefault();
		Calendar c = Calendar.getInstance(tz);
		c.clear();
		c.set(1988, Calendar.NOVEMBER, 22, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		long ms = c.getTimeInMillis();
		System.out.println(new ApproximatePlanetLocation(ms, tz));

		double ra = parse_right_ascension("185.636875");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		System.out.printf("%s: %d: %f%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), hours_to_degrees(hms_to_decimal_hours(12,22,32.85)));
		ra = parse_right_ascension("12h 22m 32.85s");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12h 22m 33s");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12h 22m 32s.85");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12:22:32.85");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12 22.5475");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12h 22.5475m");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12h 22m.5475");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("12:22.5475");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("185d38m12.80s");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("185.636875d");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		ra = parse_right_ascension("185d.636875");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_hours_to_string(ra));
		/*/

		// TODO
		double dec = parse_declination("29.8959861111111");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_degrees_to_str_dms(dec));
		dec = parse_declination("29.8959861111111d");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_degrees_to_str_dms(dec));
		dec = parse_declination("29d.8959861111111");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_degrees_to_str_dms(dec));
		dec = parse_declination("+29 53 45");
		System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.FILE(), NightSkyAtAGlance.LINE(), decimal_degrees_to_str_dms(dec));
	}
}
