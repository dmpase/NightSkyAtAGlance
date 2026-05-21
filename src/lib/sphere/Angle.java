package lib.sphere;

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

public class Angle {
	public final double degrees;
	public final double radians;
	public final double hours;

	public enum Scale {DEGREES, RADIANS, HOURS}
	
	public static final boolean E = true;
	public static final boolean W = false;
	
	public Angle()
	{
		degrees = 0;
		radians = 0;
		hours   = 0;
	}
	
	public Angle(double sign, double angle, double min, double sec, Scale scale)
	{
		double deg = 0;
		double rad = 0;
		double hrs = 0;
		sign = (sign < 0) ? -1 : ((0 < sign) ? 1 : 0);
		
		switch (scale) {
		case DEGREES :
			deg = dms_to_deg(sign, angle, min, sec);
			rad = deg_to_rad(deg);
			hrs = deg_to_hrs(deg);
			break;
		case RADIANS :
			rad = sign * Math.abs(angle);
			deg = rad_to_deg(rad);
			hrs = rad_to_hrs(rad);
			break;
		case HOURS :
			hrs = hms_to_hrs(sign, angle, min, sec);
			deg = hrs_to_deg(hrs);
			rad = hrs_to_rad(hrs);
			break;
		}
		
		degrees = deg;
		radians = rad;
		hours   = hrs;
	}
	
	public Angle(double angle, double min, double sec, Scale scale)
	{
		double deg = 0;
		double rad = 0;
		double hrs = 0;
		
		switch (scale) {
		case DEGREES :
			deg = dms_to_deg(angle, min, sec);
			rad = deg_to_rad(deg);
			hrs = deg_to_hrs(deg);
			break;
		case RADIANS :
			rad = angle;
			deg = rad_to_deg(rad);
			hrs = rad_to_hrs(rad);
			break;
		case HOURS :
			hrs = hms_to_hrs(angle, min, sec);
			deg = hrs_to_deg(hrs);
			rad = hrs_to_rad(hrs);
			break;
		}
		
		degrees = deg;
		radians = rad;
		hours   = hrs;
	}
	
	public Angle(double angle, Scale scale)
	{
		double deg = 0;
		double rad = 0;
		double hrs = 0;
		
		switch (scale) {
		case DEGREES :
			deg = angle;
			rad = deg_to_rad(deg);
			hrs = deg_to_hrs(deg);
			break;
		case RADIANS :
			rad = angle;
			deg = rad_to_deg(rad);
			hrs = rad_to_hrs(rad);
			break;
		case HOURS :
			hrs = angle;
			deg = hrs_to_deg(hrs);
			rad = hrs_to_rad(hrs);
			break;
		}
		
		degrees = deg;
		radians = rad;
		hours   = hrs;
	}
	

	public Angle(double lon_deg, double lon_min, double lon_sec, boolean east)
	{
		double longitude = (east  ? 1.0 : -1.0) * (lon_deg + lon_min/60.0 + lon_sec/3600.0);

		// normalize the longitude to -360 < longitude < +360
		if (-360.0 < longitude && longitude < 360.0) {
			;
		} else {
			double sign = (0 <= longitude) ? 1 : -1;
			double abs = Math.abs(longitude);
			double mul = Math.rint(abs/360);

			longitude = sign * (abs - mul * 360);
		}

		// normalize the longitude to -180 <= longitude <= +180
		if (-180.0 <= longitude && longitude <= 180.0) {
			// we're already normalized
			;
		} else if (180.0 < longitude) {
			longitude = longitude - 360.0;
		} else if (longitude < -180.0) {
			longitude = longitude + 360.0;
		}

		degrees = longitude;
		radians = deg_to_rad(degrees);
		hours   = deg_to_hrs(degrees);
	}


	private Angle(double deg, double rad, double hrs)
	{
		degrees = deg;
		radians = rad;
		hours   = hrs;
	}


	public Angle(Angle a)
	{
		degrees = a.degrees;
		radians = a.radians;
		hours   = a.hours;
	}


	public double deg()
	{
		double abs = Math.abs(degrees);
		double deg = Math.floor(abs);
		
		return deg;
	}

	public double min()
	{
		double abs = Math.abs(degrees);
		double deg = Math.floor(abs);
		double min = Math.floor((abs - deg) * 60);
		
		return min;
	}

	public double sec()
	{
		double abs = Math.abs(degrees);
		double deg = Math.floor(abs);
		double min = Math.floor((abs - deg) * 60);
		double sec = (abs - deg - min/60) * 3600;

		return sec;
	}
	
	public boolean sign()
	{
		return (0 <= radians);
	}

	public static double deg_to_rad( double angle )
	{
		return angle * Math.PI / 180.0;
	}

	public static double deg_to_hrs( double angle )
	{
		return angle / 15.0;
	}

	public static double rad_to_deg( double angle )
	{
		return angle * 180.0 / Math.PI;
	}

	public static double rad_to_hrs( double angle )
	{
		return angle * 12.0 / Math.PI;
	}

	public static double hrs_to_deg( double angle )
	{
		return angle * 15.0;
	}

	public static double hrs_to_rad( double angle )
	{
		return angle * Math.PI / 12.0;
	}

	public static double dms_to_deg( double sign, double degrees, double min, double sec )
	{
		sign = (sign < 0) ? -1 : ((0 < sign) ? 1 : 0);
		return sign * (Math.abs(degrees) + Math.abs(min)/60 + Math.abs(sec)/3600); 
	}

	public static double dms_to_deg( double degrees, double min, double sec )
	{
		boolean sign = degrees < 0 || (degrees == 0 && min < 0) || (degrees == 0 && min == 0 && sec < 0);
		return (sign ? -1 : 1) * (Math.abs(degrees) + Math.abs(min)/60 + Math.abs(sec)/3600); 
	}

	public static double hms_to_hrs( double sign, double hours, double min, double sec )
	{
		sign = (sign < 0) ? -1 : ((0 < sign) ? 1 : 0);
		return sign * (Math.abs(hours) + Math.abs(min)/60 + Math.abs(sec)/3600); 
	}

	public static double hms_to_hrs( double hours, double min, double sec )
	{
		boolean sign = hours < 0 || (hours == 0 && min < 0) || (hours == 0 && min == 0 && sec < 0);
		return (sign ? -1 : 1) * (Math.abs(hours) + Math.abs(min)/60 + Math.abs(sec)/3600); 
	}

	public static double dms_to_rad( double degrees, double min, double sec )
	{
		return deg_to_rad(dms_to_deg(degrees, min, sec)); 
	}

	public static double hms_to_rad( double hours, double min, double sec )
	{
		return hrs_to_rad(hms_to_hrs(hours, min, sec)); 
	}
	
	public static Angle copy(Angle a)
	{
		Angle r = new Angle(a.degrees, a.radians, a.hours);
		
		return r;
	}
	
	public Angle add(Angle a)
	{
		double deg = this.degrees + a.degrees;

		// normalize the longitude to -360 < longitude < +360
		if (-360.0 < deg && deg < 360.0) {
			;
		} else {
			double sign = (0 <= deg) ? 1 : -1;
			double abs = Math.abs(deg);
			double mul = Math.rint(abs/360);

			deg = sign * (abs - mul * 360);
		}

		// normalize the longitude to -180 <= longitude <= +180
		if (-180.0 <= deg && deg <= 180.0) {
			// we're already normalized
			;
		} else if (180.0 < deg) {
			deg = deg - 360.0;
		} else if (deg < -180.0) {
			deg = deg + 360.0;
		}

		Angle r = new Angle(deg, deg_to_rad(deg), deg_to_hrs(deg));
		
		return r;
	}
	
	public String toString()
	{
		String result = sign() ? "" : "-";
		

		int deg  = (int) deg();
		int min  = (int) min();
		int sec  = (int) Math.rint(sec());
		
		if (deg < 10) {
			result += "00" + deg;
		} else if (deg < 100) {
			result += "0" + deg;
		} else {
			result += deg;
		}
		
		result += "\u00B0";
		result += " ";
		
		if (min < 10) {
			result += "0" + min;
		} else {
			result += min;
		}
		
		result += "'";
		result += " ";
		
		if (sec < 10) {
			result += "0" + sec;
		} else {
			result += sec;
		}
		
		result += "\"";

		return result;
	}
}
