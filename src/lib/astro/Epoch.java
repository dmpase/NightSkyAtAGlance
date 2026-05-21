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


import lib.matrix.Matrix;

public class Epoch {
	public final double ra_B1950_hrs;
	public final double de_B1950_deg;
	public final double ra_J2000_hrs;
	public final double de_J2000_deg;
	
	public final static double[][] rotation_matrix = {
			{ 0.999926, -0.011179, -0.004859 },
			{ 0.011179,  0.999938, -0.000027 },
			{ 0.004859, -0.000027,  0.999988 },
	};

	// http://www.stargazing.net/kepler/b1950.html
	public static final Epoch to_B1950(double ra2000_hrs, double de2000_deg)
	{
		// double ra2000_deg = CelestialCalculations.decimal_hours_to_degrees(ra2000_hrs);
		double ra2000_rad = CelestialCalculations.decimal_hours_to_radians(ra2000_hrs);
		double de2000_rad = CelestialCalculations.decimal_degrees_to_radians(de2000_deg);

		double x = Math.cos(ra2000_rad) * Math.cos(de2000_rad);
		double y = Math.sin(ra2000_rad) * Math.cos(de2000_rad);
		double z = Math.sin(de2000_rad);
		double[] position_2000 = { x, y, z };
		double[] position_1950 = Matrix.times(position_2000, rotation_matrix);

		double r_rad = Math.atan2(position_1950[1], position_1950[0]);
		double r_deg = CelestialCalculations.radians_to_decimal_degrees(r_rad);
		r_deg = (position_1950[0] < 0) ? r_deg + 180 : r_deg;
		r_deg = (0 < position_1950[0] && position_1950[1] < 0) ? r_deg + 360 : r_deg;
		double ra1950_hrs = CelestialCalculations.decimal_degrees_to_hours(r_deg);

		double de1950_rad = Math.asin(position_1950[2]);
		double de1950_deg = CelestialCalculations.radians_to_decimal_degrees(de1950_rad);

		return new Epoch(ra1950_hrs, de1950_deg, ra2000_hrs, de2000_deg);
	}

	public static final Epoch to_J2000(double ra1950_hrs, double de1950_deg)
	{
		// double ra1950_deg = CelestialCalculations.decimal_hours_to_degrees(ra1950_hrs);
		double ra1950_rad = CelestialCalculations.decimal_hours_to_radians(ra1950_hrs);
		double de1950_rad = CelestialCalculations.decimal_degrees_to_radians(de1950_deg);

		double x = Math.cos(ra1950_rad) * Math.cos(de1950_rad);
		double y = Math.sin(ra1950_rad) * Math.cos(de1950_rad);
		double z = Math.sin(de1950_rad);
		double[] position_1950 = { x, y, z };
		double[] position_2000 = Matrix.times(rotation_matrix, position_1950);

		double r_rad = Math.atan2(position_2000[1], position_2000[0]);
		double r_deg = CelestialCalculations.radians_to_decimal_degrees(r_rad);
		r_deg = (position_2000[0] < 0) ? r_deg + 180 : r_deg;
		r_deg = (0 < position_2000[0] && position_2000[1] < 0) ? r_deg + 360 : r_deg;
		double ra2000_hrs = CelestialCalculations.decimal_degrees_to_hours(r_deg);

		double de2000_rad = Math.asin(position_2000[2]);
		double de2000_deg = CelestialCalculations.radians_to_decimal_degrees(de2000_rad);

		return new Epoch(ra1950_hrs, de1950_deg, ra2000_hrs, de2000_deg);
	}
	
	public String toString()
	{
		return String.format("ra1950=%16.12f, de1950=%16.12f, ra2000=%16.12f, de2000=%16.12f", ra_B1950_hrs, de_B1950_deg, ra_J2000_hrs, de_J2000_deg);
	}

	private Epoch(double ra1950_hrs, double de1950_deg, double ra2000_hrs, double de2000_deg) 
	{
		ra_B1950_hrs = ra1950_hrs;
		de_B1950_deg = de1950_deg;
		ra_J2000_hrs = ra2000_hrs;
		de_J2000_deg = de2000_deg;
	}
	
	public static void main(String[] args)
	{
		double ra_B1950_hrs = 18.896667;
		double de_B1950_deg = 43.883333;
		Epoch e2000 = to_J2000(ra_B1950_hrs, de_B1950_deg);
		System.out.println(e2000);
		Epoch e1950 = to_B1950(e2000.ra_J2000_hrs, e2000.de_J2000_deg);
		System.out.println(e1950);
		System.out.printf("%.12f, %.12f%n", Math.abs(e2000.ra_B1950_hrs - e1950.ra_B1950_hrs), Math.abs(e2000.de_B1950_deg - e1950.de_B1950_deg));
	}
}
