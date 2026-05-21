package lib.stars.image;

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


import lib.sphere.Angle;
import lib.sphere.Location;
import lib.sphere.Sphere;

public class ImageMap {
	public final double ctr_ra_deg;		// center right ascension of the image in degrees
	public final double ctr_de_deg;		// center declination of the image in degrees
	public final double w_amin;			// width of the image in minutes of arc
	public final double h_amin;			// height of the image in minutes of arc
	public final double w_px;			// width of the image in pixels
	public final double h_px;			// height of the image in pixels

	public final double ctr_x_px;		// center x of the image in pixels
	public final double ctr_y_px;		// center y of the image in pixels
	public final double cir_y_px;		// circumference of the sphere in pixels (measured in y)
	public final double dia_y_px;		// diameter of the sphere in pixels (measured in y)
	public final double rad_y_px;		// radius of the sphere in pixels (measured in y)

	public final Location center;
	public final Sphere   sphere;

	public static final int RA  = 0;
	public static final int DEC = 1;
	public static final int X   = 0;
	public static final int Y   = 1;

	public static final double seconds_per_minute = 60;
	public static final double minutes_per_degree = 60;
	public static final double seconds_per_degree = seconds_per_minute * minutes_per_degree;

	// translate between image equatorial coordinates and pixels.
	// the image is oriented with the top pointing north.
	// the origin in pixels (0,0) is in the upper left corner.
	// the center of the image in equatorial coordinates is (ctr_ra_deg,ctr_de_deg).
	// equatorial coordinates are expressed in degrees rather than hours or radians.
	public ImageMap(double ra, double dec, double we, double he, double wp, double hp) 
	{
		ctr_ra_deg = ra;
		ctr_de_deg = dec;
		w_amin     = we;
		h_amin     = he;
		w_px       = wp;
		h_px       = hp;
		
		ctr_x_px   = wp / 2.0;
		ctr_y_px   = hp / 2.0;

		double height_in_degrees = h_amin / minutes_per_degree;
		double pixels_per_degree = h_px / height_in_degrees;
		cir_y_px   = pixels_per_degree * 360;
		dia_y_px   = cir_y_px / Math.PI;
		rad_y_px   = dia_y_px / 2;
		sphere     = new Sphere(rad_y_px);

		Angle lat  = new Angle(ctr_de_deg, Angle.Scale.DEGREES);
		Angle lon  = new Angle(ctr_ra_deg, Angle.Scale.DEGREES);
		center     = new Location(lat, lon);
	}

	public double[] px_to_eq(double x, double y)
	{
		double degrees = bearing_from_ctr_deg(x, y);
		Angle  bearing = new Angle(degrees, Angle.Scale.DEGREES);
		double range   = range_from_ctr_px(x, y);
		Location loc = sphere.great_circle_destination(center, bearing, range);
		double[] array = new double[2];
		array[X] = loc.longitude.degrees;
		array[Y] = loc.latitude.degrees;

		return array;
	}

	public int[] eq_to_px(double ra_deg, double de_deg)
	{
		// use sphere.great_circle_range()
		return null;
	}

	public double bearing_from_ctr_deg(double x, double y)
	{
		double degrees = angle_in_xy_from_ctr_deg(x,y);
		double bearing = degrees_to_bearing(degrees);

		return bearing;
	}

	public double range_from_ctr_px(double x, double y)
	{
		return distance(ctr_x_px, ctr_y_px, x, y);
	}

	public double angle_in_xy_from_ctr_deg(double x, double y)
	{
		double radians = Math.atan2(y, x);
		
		return radians_to_degrees(radians);
	}

	public static double degrees_to_bearing(double deg)
	{
		double b = 90 - deg;
		b += (b   <  0) ? 360 : 0;
		b -= (360 <= b) ? 360 : 0;

		return b;
	}

	public static double bearing_to_degrees(double bearing)
	{
		double d = 90 - bearing;
		d += (d   <  0) ? 360 : 0;
		d -= (360 <= d) ? 360 : 0;

		return d;
	}

	public static double degrees_to_radians(double degrees)
	{
		double radians = Math.PI * degrees / 180;

		return radians;
	}

	public static double radians_to_degrees(double radians)
	{
		double degrees = 180 * radians / Math.PI;

		return degrees;
	}

	public static double bearing_to_radians(double bearing)
	{
		double degrees = bearing_to_degrees(bearing);
		double radians = degrees_to_radians(degrees);
		
		return radians;
	}

	public static double radians_to_bearing(double radians)
	{
		double degrees = radians_to_degrees(radians);
		double bearing = degrees_to_bearing(degrees);
		
		return bearing;
	}
	
	public static double distance(double x0, double y0, double x1, double y1)
	{
		return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
	}

	public static void main(String[] args) 
	{
	}
}
