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

public class Location {
	public Angle latitude;
	public Angle longitude;
	
	public static final boolean N = true;
	public static final boolean S = false;
	public static final boolean E = true;
	public static final boolean W = false;

	public Location(
		double lat_deg, double lat_min, double lat_sec, boolean north,
		double lon_deg, double lon_min, double lon_sec, boolean east)
	{
		double lat = (north ? 1.0 : -1.0) * (lat_deg + lat_min/60.0 + lat_sec/3600.0);
		double lon = (east  ? 1.0 : -1.0) * (lon_deg + lon_min/60.0 + lon_sec/3600.0);

		// normalize the latitude to -360 < latitude < 360
		if (-360.0 < lat && lat < 360.0) {
			;
		} else {
			double sign = (0 <= lat) ? 1 : -1;
			double abs = Math.abs(lat);
			double mul = Math.rint(abs/360);

			lat = sign * (abs - mul * 360);
		}

		// normalize the latitude to -90 <= latitude <= +90
		if (-90.0 <= lat && lat <= 90.0) {
			// we're already normalized
			;
		} else if (90.0 < lat && lat <= 270.0) {
			// we've wrapped around over the north pole
			lat = 180.0 - lat;
			lon = lon + 180.0;
		} else if (270.0 < lat && lat <= 360.0) {
			// we've wrapped around over the north pole and under the south pole
			lat = lat - 360.0;
		} else if (-270.0 <= lat && lat < -90.0) {
			// we've wrapped around under the south pole
			lat = -180.0 - lat;
			lon = lon + 180.0;
		} else if (-360.0 <= lat && lat < -270.0) {
			// we've wrapped around under the south pole and over the north pole
			lat = lat + 360.0;
		}

		// normalize the longitude to -360 < longitude < +360
		if (-360.0 < lon && lon < 360.0) {
			;
		} else {
			double sign = (0 <= lon) ? 1 : -1;
			double abs = Math.abs(lon);
			double mul = Math.rint(abs/360);

			lon = sign * (abs - mul * 360);
		}

		// normalize the longitude to -180 <= longitude <= +180
		if (-180.0 <= lon && lon <= 180.0) {
			// we're already normalized
			;
		} else if (180.0 < lon) {
			lon = lon - 360.0;
		} else if (lon < -180.0) {
			lon = lon + 360.0;
		}

		latitude  = new Angle(lat, Angle.Scale.DEGREES);
		longitude = new Angle(lon, Angle.Scale.DEGREES);
	}
	
	public Location()
	{
		latitude  = new Angle(0, Angle.Scale.DEGREES);
		longitude = new Angle(0, Angle.Scale.DEGREES);
	}

	public Location(Angle latitude, Angle longitude)
	{
		this.latitude  = latitude;
		this.longitude = longitude;
	}
	
	public String toString()
	{
		String result = "";

		boolean north = (0 <= latitude.degrees);
		int lat_deg  = (int) latitude.deg();
		int lat_min  = (int) latitude.min();
		int lat_sec  = (int) latitude.sec();
		int lat_fra  = (int) Math.rint(1000*(latitude.sec()-Math.floor(latitude.sec())));

		if (lat_deg < 10) {
			result += "0" + lat_deg;
		} else {
			result += lat_deg;
		}
		
		result += "\u00B0";
		result += " ";
		
		if (lat_min < 10) {
			result += "0" + lat_min;
		} else {
			result += lat_min;
		}
		
		result += "'";
		result += " ";
		
		if (lat_sec < 10) {
			result += "0" + lat_sec;
		} else {
			result += lat_sec;
		}
		
		if (lat_fra < 10) {
			result += ".00" + lat_fra;
		} else if (lat_fra < 100) {
				result += ".0" + lat_fra;
		} else {
			result += "." + lat_fra;
		}
		
		result += "\"";
		result += (north) ? "N" : "S";
		
		result += ", ";

		boolean east = (0 <= longitude.degrees);
		int lon_deg  = (int) longitude.deg();
		int lon_min  = (int) longitude.min();
		int lon_sec  = (int) Math.floor(longitude.sec());
		int lon_fra  = (int) Math.rint(1000*(longitude.sec()-Math.floor(longitude.sec())));
		
		if (lon_deg < 10) {
			result += "00" + lon_deg;
		} else if (lon_deg < 100) {
			result += "0" + lon_deg;
		} else {
			result += lon_deg;
		}
		
		result += "\u00B0";
		result += " ";
		
		if (lon_min < 10) {
			result += "0" + lon_min;
		} else {
			result += lon_min;
		}
		
		result += "'";
		result += " ";
		
		if (lon_sec < 10) {
			result += "0" + lon_sec;
		} else {
			result += lon_sec;
		}
		
		if (lon_fra < 10) {
			result += ".00" + lon_fra;
		} else if (lon_fra < 100) {
				result += ".0" + lon_fra;
		} else {
			result += "." + lon_fra;
		}
		
		result += "\"";
		result += (east) ? "E" : "W";
		
		return result;
	}
	
	public String str_deg()
	{
		String r = "";
		boolean north = (0 <= latitude.degrees);
		int lat_deg  = (int) latitude.deg();
		int lat_fra  = (int) Math.rint((1000000 * (Math.abs(latitude.degrees) - lat_deg)));

		if (lat_deg < 10) {
			r += "0" + lat_deg;
		} else {
			r += lat_deg;
		}

		if (lat_fra < 10) {
			r += ".00000" + lat_fra;
		} else if (lat_fra < 100) {
			r += ".0000"  + lat_fra;
		} else if (lat_fra < 1000) {
			r += ".000"   + lat_fra;
		} else if (lat_fra < 10000) {
			r += ".00"    + lat_fra;
		} else if (lat_fra < 100000) {
			r += ".0"    + lat_fra;
		} else {
			r += "."      + lat_fra;
		}

		r += "\u00B0";
		r += (north) ? "N" : "S";
		r += ", ";
		
		boolean east = (0 <= longitude.degrees);
		int lon_deg  = (int) longitude.deg();
		int lon_fra  = (int) Math.rint((1000000 * (Math.abs(longitude.degrees) - lon_deg)));

		if (lon_deg < 10) {
			r += "00" + lon_deg;
		} else if (lon_deg < 100) {
			r += "0" + lon_deg;
		} else {
			r += lon_deg;
		}

		if (lon_fra < 10) {
			r += ".00000" + lon_fra;
		} else if (lon_fra < 100) {
			r += ".0000"  + lon_fra;
		} else if (lon_fra < 1000) {
			r += ".000"   + lon_fra;
		} else if (lon_fra < 10000) {
			r += ".00"    + lon_fra;
		} else if (lon_fra < 100000) {
			r += ".0"    + lon_fra;
		} else {
			r += "."     + lon_fra;
		}

		r += "\u00B0";
		r += (east) ? "E" : "W";
		
		return r;
	}
	
	public String str_min()
	{
		String r = "";
		boolean north = (0 <= latitude.degrees);
		int lat_deg  = (int) latitude.deg();
		int lat_min  = (int) latitude.min();
		int lat_fra  = (int) Math.rint((100000 * (60*(Math.abs(latitude.degrees) - lat_deg) - lat_min)));

		if (lat_deg < 10) {
			r += "0" + lat_deg;
		} else {
			r += lat_deg;
		}
		r += "\u00B0";
		r += " ";

		if (lat_min < 10) {
			r += "0" + lat_min;
		} else {
			r += lat_min;
		}

		if (lat_fra < 10) {
			r += ".0000" + lat_fra;
		} else if (lat_fra < 100) {
			r += ".000"  + lat_fra;
		} else if (lat_fra < 1000) {
			r += ".00"   + lat_fra;
		} else if (lat_fra < 10000) {
			r += ".0"    + lat_fra;
		} else {
			r += "."      + lat_fra;
		}

		r += "'";
		
		r += (north) ? "N" : "S";
		r += ", ";

		boolean east  = (0 <= longitude.degrees);
		int lon_deg  = (int) longitude.deg();
		int lon_min  = (int) longitude.min();
		int lon_fra  = (int) Math.rint((100000 * (60*(Math.abs(longitude.degrees) - lon_deg) - lon_min)));
		
		if (lon_deg < 10) {
			r += "00" + lon_deg;
		} else if (lon_deg < 100) {
			r += "0" + lon_deg;
		} else {
			r += lon_deg;
		}
		r += "\u00B0";
		r += " ";

		if (lon_min < 10) {
			r += "0" + lon_min;
		} else {
			r += lon_min;
		}

		if (lon_fra < 10) {
			r += ".0000" + lon_fra;
		} else if (lon_fra < 100) {
			r += ".000"  + lon_fra;
		} else if (lon_fra < 1000) {
			r += ".00"   + lon_fra;
		} else if (lon_fra < 10000) {
			r += ".0"    + lon_fra;
		} else {
			r += "."     + lon_fra;
		}

		r += "'";
		
		r += (east) ? "E" : "W";
		
		return r;
	}
	
	public String str_sec()
	{
		String r = "";
		boolean north = (0 <= latitude.degrees);
		int lat_deg  = (int) latitude.deg();
		int lat_min  = (int) latitude.min();
		int lat_sec  = (int) latitude.sec();
		int lat_fra  = (int) Math.rint((1000 * (60*(60*(Math.abs(latitude.degrees) - lat_deg) - lat_min)-lat_sec)));

		if (lat_deg < 10) {
			r += "0" + lat_deg;
		} else {
			r += lat_deg;
		}
		r += "\u00B0";
		r += " ";

		if (lat_min < 10) {
			r += "0" + lat_min;
		} else {
			r += lat_min;
		}

		r += "'";
		r += " ";

		if (lat_sec < 10) {
			r += "0" + lat_sec;
		} else {
			r += lat_sec;
		}

		if (lat_fra < 10) {
			r += ".00" + lat_fra;
		} else if (lat_fra < 100) {
			r += ".0"  + lat_fra;
		} else {
			r += "."      + lat_fra;
		}

		r += "\"";
		
		r += (north) ? "N" : "S";
		r += ", ";

		boolean east  = (0 <= longitude.degrees);
		int lon_deg  = (int) longitude.deg();
		int lon_min  = (int) longitude.min();
		int lon_sec  = (int) longitude.sec();
		int lon_fra  = (int) Math.rint((1000 * (60*(60*(Math.abs(longitude.degrees) - lon_deg) - lon_min)-lon_sec)));
		
		if (lon_deg < 10) {
			r += "00" + lon_deg;
		} else if (lon_deg < 100) {
			r += "0" + lon_deg;
		} else {
			r += lon_deg;
		}
		r += "\u00B0";
		r += " ";

		if (lon_min < 10) {
			r += "0" + lon_min;
		} else {
			r += lon_min;
		}

		r += "'";
		r += " ";

		if (lon_sec < 10) {
			r += "0" + lon_sec;
		} else {
			r += lon_sec;
		}

		if (lon_fra < 10) {
			r += ".00" + lon_fra;
		} else if (lon_fra < 100) {
			r += ".0"  + lon_fra;
		} else {
			r += "."      + lon_fra;
		}

		r += "\"";
		
		r += (east) ? "E" : "W";
		
		return r;
	}
}
