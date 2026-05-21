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

/******************************************************************************
 *                                                                            *
 * Sphere                                                                     *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    January 29, 2015                                                  *
 *                                                                            *
 ******************************************************************************/

public class Sphere {
	// Earth's mean radius overall = 6371 km, 3440.06 nm, 3958.77 mi
	public final double radius;	// Earth mean radius (km)
	
	public Sphere()
	{
		radius = 6371;
	}
	
	public Sphere(double r)
	{
		radius = r;
	}
	
	// http://www.movable-type.co.uk/scripts/latlong.html

	// great circle range in kilometers
	public double great_circle_range( Location p1, Location p2 )
	{
		double delta_lat = (p2.latitude.radians - p1.latitude.radians);
		double delta_lon = (p2.longitude.radians - p1.longitude.radians);
		double a = 
			Math.sin(delta_lat/2.0) * Math.sin(delta_lat/2.0) +
			Math.cos(p1.latitude.radians) * Math.cos(p2.latitude.radians) * 
			Math.sin(delta_lon/2.0) * Math.sin(delta_lon/2.0);

		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		double d = radius * c;

		return d;
	}

	// initial great circle bearing in degrees
	public Angle great_circle_initial_bearing( Location p1, Location p2 )
	{
		double y = Math.sin(p2.longitude.radians - p1.longitude.radians) * Math.cos(p2.latitude.radians);
		double x = 
			Math.cos(p1.latitude.radians) * Math.sin(p2.latitude.radians) -
			Math.sin(p1.latitude.radians) * Math.cos(p2.latitude.radians)*
			Math.cos(p2.longitude.radians - p1.longitude.radians)
			;

		double bearing = Math.atan2(y, x);

		if (bearing < 0) {
			bearing += 2*Math.PI;
		} else if (2*Math.PI < bearing) {
			bearing -= 2*Math.PI;
		}
		
		return new Angle(bearing, Angle.Scale.RADIANS);
	}

	// final great circle bearing in degrees
	public Angle great_circle_final_bearing( Location p1, Location p2 )
	{
		Angle rib = great_circle_initial_bearing( p2, p1 );

		double bearing = rib.radians;
		if (bearing <= 0) {
			bearing = rib.radians + Math.PI;
		} else {
			bearing = rib.radians - Math.PI;
		}

		if (bearing < 0) {
			bearing += 2*Math.PI;
		} else if (2*Math.PI < bearing) {
			bearing -= 2*Math.PI;
		}
		
		return new Angle(bearing, Angle.Scale.RADIANS);
	}

	// great circle midpoint in latitude and longitude
	public Location great_circle_midpoint( Location p1, Location p2 )
	{

		double Bx = Math.cos(p2.latitude.radians) * Math.cos(p2.longitude.radians - p1.longitude.radians);
		double By = Math.cos(p2.latitude.radians) * Math.sin(p2.longitude.radians - p1.longitude.radians);

		double latitude = Math.atan2(
			Math.sin(p1.latitude.radians) + Math.sin(p2.latitude.radians),
			Math.sqrt( (Math.cos(p1.latitude.radians)+Bx)*(Math.cos(p1.latitude.radians)+Bx) + By*By ));

		double longitude = p1.longitude.radians + Math.atan2(By, Math.cos(p1.latitude.radians) + Bx);

		Location p3 = new Location(
				new Angle(latitude,  Angle.Scale.RADIANS), 
				new Angle(longitude, Angle.Scale.RADIANS));

		return p3;
	}

	// great circle destination given origin, initial bearing and range
	public Location great_circle_destination( Location p1, Angle bearing, double range )
	{
		double latitude = Math.asin( Math.sin(p1.latitude.radians)*Math.cos(range/radius) +
                Math.cos(p1.latitude.radians)*Math.sin(range/radius)* Math.cos(bearing.radians) );
		double longitude = p1.longitude.radians + Math.atan2(Math.sin(bearing.radians) *
				Math.sin(range/radius) * Math.cos(p1.latitude.radians),
				Math.cos(range/radius) - Math.sin(p1.latitude.radians)*Math.sin(latitude));

		Location p2 = new Location(
				new Angle(latitude,  Angle.Scale.RADIANS), 
				new Angle(longitude, Angle.Scale.RADIANS));

		return p2;
	}

	// great circle intersection given origin, initial bearing and range
	public Location great_circle_intersection( Location p1, Angle bearing1, Location p2, Angle bearing2 )
	{
		Location p3 = null;
		
		double psi_1 = p1.latitude.radians;
		double lamda_1 = p1.longitude.radians;
		double psi_2 = p2.latitude.radians;
		double lamda_2 = p2.longitude.radians;
		double theta_13 = bearing1.radians;
		double theta_23 = bearing2.radians;
		double delta_psi = psi_2 - psi_1;
		double delta_lamda = lamda_2 - lamda_1;
		double delta_12 = 2.0 * Math.asin( Math.sqrt( Math.sin(delta_psi/2)*Math.sin(delta_psi/2) + Math.cos(psi_1)*Math.cos(psi_2)*Math.sin(delta_lamda/2)*Math.sin(delta_lamda/2) ) );

//		System.out.println("psi_1="+psi_1);
//		System.out.println("lambda_1="+lamda_1);
//		System.out.println("psi_2="+psi_2);
//		System.out.println("lambda_2="+lamda_2);
//		System.out.println("theta_13="+theta_13);
//		System.out.println("theta_23="+theta_23);
//		System.out.println("delta_psi="+delta_psi);
//		System.out.println("delta_lamda="+delta_lamda);
//		System.out.println("delta_12="+delta_12);
		
		if (delta_12 == 0) return null;
		
		double theta_a = Math.acos( ( Math.sin(psi_2) - Math.sin(psi_1)*Math.cos(delta_12) ) / ( Math.sin(delta_12)*Math.cos(psi_1) ) );
		if (theta_a == Double.NaN) theta_a = 0;
//		System.out.println("theta_a="+theta_a);
		double theta_b = Math.acos( ( Math.sin(psi_1) - Math.sin(psi_2)*Math.cos(delta_12) ) / ( Math.sin(delta_12)*Math.cos(psi_2) ) );
//		System.out.println("theta_b="+theta_b);
		
		double theta_12 = Math.sin(lamda_2 - lamda_1) > 0 ? theta_a : 2.0 * Math.PI - theta_a;
		double theta_21 = Math.sin(lamda_2 - lamda_1) > 0 ? 2.0 * Math.PI - theta_b : theta_b;

//		System.out.println("theta_12="+theta_12);
//		System.out.println("theta_21="+theta_21);

		double alpha_1 = mod((theta_13 - theta_12 + Math.PI), (2.0 * Math.PI)) - Math.PI; // angle 2-1-3
		double alpha_2 = mod((theta_21 - theta_23 + Math.PI), (2.0 * Math.PI)) - Math.PI; // angle 1-2-3

//		System.out.println("mod="+mod((theta_13 - theta_12 + Math.PI), (2.0 * Math.PI)));
//		System.out.println("-+="+(theta_13 - theta_12 + Math.PI));
//		System.out.println("2PI="+(2.0 * Math.PI));
//		System.out.println("alpha_1="+alpha_1);
//		System.out.println("alpha_2="+alpha_2);

		if (Math.sin(alpha_1) == 0 && Math.sin(alpha_2) == 0) {
			System.out.println("infinite intersections");
			return null; // infinite intersections
		}
		if (Math.sin(alpha_1) * Math.sin(alpha_2) < 0) {
			System.out.println("sin("+alpha_1+")="+Math.sin(alpha_1));
			System.out.println("sin("+alpha_2+")="+Math.sin(alpha_2));
			System.out.println("ambiguous intersections");
			return null;        // ambiguous intersection
		}

		double alpha_3  = Math.acos( -Math.cos(alpha_1) * Math.cos(alpha_2) + Math.sin(alpha_1) * Math.sin(alpha_2) * Math.cos(delta_12) );
		double delta_13 = Math.atan2( Math.sin(delta_12) * Math.sin(alpha_1) * Math.sin(alpha_2), Math.cos(alpha_2) + Math.cos(alpha_1) * Math.cos(alpha_3) );
		double psi_3 = Math.asin( Math.sin(psi_1)*Math.cos(delta_13) + Math.cos(psi_1) * Math.sin(delta_13) * Math.cos(theta_13) );
		double delta_lambda_13 = Math.atan2( Math.sin(theta_13)*Math.sin(delta_13)*Math.cos(psi_1), Math.cos(delta_13) - Math.sin(psi_1) * Math.sin(psi_3) );
		double lambda_3 = lamda_1 + delta_lambda_13;

//		System.out.println("alpha_3="+alpha_3);
//		System.out.println("delta_13="+delta_13);
//		System.out.println("psi_3="+psi_3);
//		System.out.println("delta_lambda_13="+delta_lambda_13);
//		System.out.println("lambda_3="+lambda_3);

		p3 = new Location(new Angle(psi_3, Angle.Scale.RADIANS), new Angle(mod(lambda_3+3*Math.PI, 2.0*Math.PI)-Math.PI, Angle.Scale.RADIANS));

//		System.out.println("p3="+p3);

		return p3;
	}

	public double mod(double x, double d)
	{
		double m = Double.NaN;

		if (0 < d) {
			if (0 <= x && x < d) {
				m = x;
			} else if (d <= x) {
				double f = Math.floor(x/d);
				m = x - f*d;
			} else if (x < 0) {
				x += (1 + Math.floor(Math.abs(x/d)))*d;
				double f = Math.floor(x/d);
				m = x - f*d;
			}
		} else if (d < 0) {
		}

//		System.out.println("x="+x+" d="+d+" m="+m);
		
		return m;
	}


	// rhumb line range in kilometers
	public double rhumb_line_range( Location p1, Location p2 )
	{
		double delta_lat = (p2.latitude.radians  - p1.latitude.radians);
		double delta_lon = (p2.longitude.radians - p1.longitude.radians);
		double delta_psi = Math.log(Math.tan(Math.PI/4+p2.latitude.radians/2)/Math.tan(Math.PI/4+p1.latitude.radians/2));
		double q = Math.abs(delta_psi) > 10e-12 ? delta_lat/delta_psi : Math.cos(p1.latitude.radians); // E-W course becomes ill-conditioned with 0/0

		// if delta_lon over 180 take shorter rhumb across anti-meridian:
		if (Math.abs(delta_lon) > Math.PI) {
			delta_lon = (delta_lon>0) ? -(2*Math.PI-delta_lon) : (2*Math.PI+delta_lon);
		}

		double dist = Math.sqrt(delta_lat*delta_lat + q*q*delta_lon*delta_lon) * radius;
		
		return dist;
	}

	// rhumb line initial bearing in degrees
	public Angle rhumb_line_initial_bearing( Location p1, Location p2 )
	{
		double delta_lon = (p2.longitude.radians - p1.longitude.radians);
		double delta_psi = Math.log(Math.tan(Math.PI/4+p2.latitude.radians/2)/Math.tan(Math.PI/4+p1.latitude.radians/2));

		// if delta_lon over 180 take shorter rhumb across anti-meridian:
		if (Math.abs(delta_lon) > Math.PI) {
			delta_lon = (delta_lon>0) ? -(2*Math.PI-delta_lon) : (2*Math.PI+delta_lon);
		}

		double bearing = Math.atan2(delta_lon, delta_psi);

		if (bearing < 0) {
			bearing += 2*Math.PI;
		} else if (2*Math.PI < bearing) {
			bearing -= 2*Math.PI;
		}
		
		return new Angle(bearing, Angle.Scale.RADIANS);
	}

	// rhumb line bearing in degrees
	public Angle rhumb_line_final_bearing( Location p1, Location p2 )
	{
		Angle rib = rhumb_line_initial_bearing( p2, p1 );

		double bearing = rib.radians;
		if (bearing <= 0) {
			bearing = rib.radians + Math.PI;
		} else {
			bearing = rib.radians - Math.PI;
		}

		if (bearing < 0) {
			bearing += 2*Math.PI;
		} else if (2*Math.PI < bearing) {
			bearing -= 2*Math.PI;
		}
		
		return new Angle(bearing, Angle.Scale.RADIANS);
	}

	// rhumb line midpoint in latitude and longitude
	public Location rhumb_line_midpoint( Location p1, Location p2 )
	{
		if (Math.abs(p2.longitude.radians - p1.longitude.radians) > Math.PI) {
			// crossing anti-meridian
			p1 = new Location(p1.latitude, new Angle(p1.longitude.radians + 2*Math.PI, Angle.Scale.RADIANS));
		}

		double latitude = (p1.latitude.radians+p2.latitude.radians)/2;
		double f1 = Math.tan(Math.PI/4 + p1.latitude.radians/2);
		double f2 = Math.tan(Math.PI/4 + p2.latitude.radians/2);
		double f3 = Math.tan(Math.PI/4 + latitude/2);
		double longitude = ( (p2.longitude.radians - p1.longitude.radians) * Math.log(f3) + 
				p1.longitude.radians * Math.log(f2) - p2.longitude.radians * Math.log(f1) ) / Math.log(f2/f1);

		if (Double.isInfinite(longitude)) {
			longitude = (p1.longitude.radians + p2.longitude.radians)/2; // parallel of latitude
		}

		longitude = (longitude + 3*Math.PI) % (2*Math.PI) - Math.PI;  // normalise to -180..+180

		Location p3 = new Location(	new Angle(latitude, Angle.Scale.RADIANS), new Angle(longitude, Angle.Scale.RADIANS));

		return p3;
	}

	// rhumb line destination given origin, bearing and range
	public Location rhumb_line_destination( Location p1, Angle bearing, double range )
	{
		// see http://williams.best.vwh.net/avform.htm#Rhumb

	    double delta      = range / radius;
	    double delta_lat  = delta * Math.cos(bearing.radians);

	    double latitude   = p1.latitude.radians + delta_lat;
	    // check for some daft bugger going past the pole, normalise latitude if so
	    if (Math.abs(latitude) > Math.PI/2) {
	    	latitude = latitude > 0 ? Math.PI - latitude : -Math.PI - latitude;
	    }

	    double delta_psi  = Math.log(Math.tan(latitude/2+Math.PI/4)/Math.tan(p1.latitude.radians/2+Math.PI/4));
	    double q = Math.abs(delta_psi) > 10e-12 ? delta_lat / delta_psi : Math.cos(p1.latitude.radians); // E-W course becomes ill-conditioned with 0/0

	    double delta_lon = delta*Math.sin(bearing.radians)/q;

	    double longitude = p1.longitude.radians + delta_lon;

	    longitude = (longitude + 3*Math.PI) % (2*Math.PI) - Math.PI; // normalise to -180..+180
		
		Location p2 = new Location( new Angle(latitude, Angle.Scale.RADIANS), new Angle(longitude, Angle.Scale.RADIANS));

		return p2;
	}

	public static void main(String[] args) 
	{
		Sphere earth = new Sphere();
		Location gc1 = new Location(50,  3, 59, Location.N, 5, 42, 53, Location.W);
		Location gc2 = new Location(58, 38, 38, Location.N, 3,  4, 12, Location.W);

		double   gcr = earth.great_circle_range(gc1, gc2);
		Angle    gci = earth.great_circle_initial_bearing(gc1, gc2);
		Angle    gcf = earth.great_circle_final_bearing(gc1, gc2);
		Location gcm = earth.great_circle_midpoint(gc1, gc2); 
		
    	System.out.println( gc1 + " -> " + gc2);
    	System.out.println("   gcr=" + gcr);
    	System.out.println("   gci=" + gci);
    	System.out.println("   gcf=" + gcf);
    	System.out.println("   gcm=" + gcm);
    	
    	System.out.println();

		Location rl1 = new Location(50, 21, 59, Location.N,  4,  8,  2, Location.W);
		Location rl2 = new Location(42, 21,  4, Location.N, 71,  2, 27, Location.W);

		double   rlr = earth.rhumb_line_range(rl1, rl2);
		Angle    rli = earth.rhumb_line_initial_bearing(rl1, rl2);
		Angle    rlf = earth.rhumb_line_final_bearing(rl1, rl2);
		Location rlm = earth.rhumb_line_midpoint(rl1, rl2); 
		
    	System.out.println( rl1 + " -> " + rl2);
    	System.out.println("   rlr=" + rlr);
    	System.out.println("   rli=" + rli);
    	System.out.println("   rlf=" + rlf);
    	System.out.println("   rlm=" + rlm);
    	
    	Location ip1 = new Location(51.8853, 0, 0, Location.N, 0.2545, 0, 0, Location.E);
    	Angle b1 = new Angle(108.55, Angle.Scale.DEGREES);
    	Location ip2 = new Location(49.0034, 0, 0, Location.N, 2.5735, 0, 0, Location.E);
    	Angle b2 = new Angle( 32.44, Angle.Scale.DEGREES);
    	Location ip3 = earth.great_circle_intersection(ip1, b1, ip2, b2);

    	System.out.println();
    	System.out.println(ip1.str_deg() + " bearing " + b1.degrees);
    	System.out.println(ip2.str_deg() + " bearing " + b2.degrees);
    	System.out.println("   intersects at " + ip3);
	}
}
