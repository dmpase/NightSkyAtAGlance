package lib.astro;

/*******************************************************************************
 * Copyright (c) 2021 Douglas M. Pase                                          *
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


public class Angle {
	
	// hms -> hours, minutes, seconds
	// hrs -> hours
	// min -> minutes
	// sec -> seconds
	// dms -> degrees, minutes, seconds
	// deg -> degrees
	// rad -> radians
	
	public static double hms_to_hrs(int h, int m, double s)
	{
		return h + (m + s / 60.0) / 60.0; 
	}
	
	
	public static double hms_to_deg(int h, int m, double s)
	{
		return hrs_to_deg(hms_to_hrs(h, m, s)); 
	}
	
	
	public static double hms_to_rad(int h, int m, double s)
	{
		return deg_to_rad(hms_to_deg(h, m, s)); 
	}
	
	
	public static double dms_to_hrs(int d, int m, double s)
	{
		return deg_to_hrs(dms_to_deg(d, m, s)); 
	}
	
	
	public static double dms_to_deg(int d, int m, double s)
	{
		return d + (m + s / 60.0) / 60.0; 
	}
	
	
	public static double dms_to_rad(int d, int m, double s)
	{
		return deg_to_rad(dms_to_deg(d, m, s)); 
	}

	
	public static double hrs_to_deg(double hours)
	{
		return 15 * hours;
	}

	
	public static double hrs_to_rad(double hours)
	{
		return deg_to_rad(hrs_to_deg(hours));
	}


	public static double deg_to_hrs(double degrees)
	{
		return degrees / 15;
	}


	public static double deg_to_rad(double degrees)
	{
		return degrees * Math.PI / 180;
	}


	public static double rad_to_deg(double radians)
	{
		return radians * 180 / Math.PI;
	}


	public static double rad_to_hrs(double radians)
	{
		return deg_to_hrs(rad_to_deg(radians));
	}

	
	public static final boolean N = true;
	public static final boolean S = false;
	
	// convert deg:min:sec to decimal degrees
	public static double dms_to_ddegrees(int degrees, int minutes, double seconds)
	{
		boolean sign = (0 < degrees) || (degrees == 0 && 0 < minutes) || (degrees == 0 && minutes == 0 && 0 < seconds) ||
				(degrees == 0 && minutes == 0 && seconds == 0);
		
		return (sign ? 1 : -1) * (Math.abs(degrees) + Math.abs(minutes)/60e0 + Math.abs(seconds)/3600e0);
	}

	// convert sign deg:min:sec to decimal degrees
	// degrees, minutes, seconds are all positive values
	public static double sdms_to_ddegrees(boolean is_positive, int degrees, int minutes, double seconds)
	{
		return (is_positive ? 1 : -1) * (Math.abs(degrees) + Math.abs(minutes)/60 + Math.abs(seconds)/3600);
	}

	// degrees, minutes are positive values
	public static double sdms_to_ddegrees(boolean is_positive, int degrees, double minutes)
	{
		return (is_positive ? 1 : -1) * (Math.abs(degrees) + Math.abs(minutes)/60);
	}

	public static boolean ddegrees_to_sign(double ddegrees)
	{
		return 0 <= ddegrees;
	}

	// unsigned whole number of degrees from a signed decimal degrees value
	public static int ddegrees_to_degrees(double ddegrees)
	{
		return (int) Math.abs(ddegrees);
	}

	// unsigned whole number of minutes (excluding whole degrees) from a signed decimal value
	public static double ddegrees_to_minutes(double ddegrees)
	{
		ddegrees = Math.abs(ddegrees);

		return 60*(ddegrees - ((int)ddegrees));
	}

	// unsigned whole number of seconds (excluding whole degrees and minutes) from a signed decimal value
	public static double ddegrees_to_seconds(double ddegrees)
	{
		ddegrees = Math.abs(ddegrees);

		return 60*(60*ddegrees - ((int)(60*ddegrees)));
	}

	// hh:mm:ss to decimal hours
	public static double hms_to_dhours(int hours, int minutes, double seconds)
	{
		return (double)hours + (double)minutes/60E0 + seconds/3600E0;
	}
	
	public static double hms_to_ddegrees(int hours, int minutes, double seconds)
	{
		return 15*((double)hours + (double)minutes/60E0 + seconds/3600E0);
	}
	
	public static double dhours_to_ddegrees(double dhours)
	{
		return 15 * dhours;
	}
	
	public static double ddegrees_to_dhours(double ddegrees)
	{
		return ddegrees/15;
	}

	public static double dhours_to_minutes(double dhours)
	{
		return 60 * (dhours - (int)dhours);
	}

	public static double dhours_to_seconds(double dhours)
	{
		return 60 * ((60*dhours) - (int)(60*dhours));
	}
}
