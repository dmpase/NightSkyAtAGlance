package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 1988-2022 Douglas M. Pase                                     *
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

import java.io.IOException;

import lib.astro.PracticalAstronomy;


public class SharplessEntry implements Comparable<SharplessEntry> {

	public final String ra_str;				// right ascension in degrees, J2000
	public final double ra_ddeg;
	public final double ra_dhrs;
	public final double ra_hrs;
	public final double ra_min;
	public final double ra_sec;
	public final String de_str;				// declination in degrees, J2000
	public final double de_ddeg;
	public final double de_deg;
	public final double de_min;
	public final double de_sec;
	public final int    number;				// Sharpless number
	public final String name;
	public final double diameter;
	public final int    form;
	public final int    structure;
	public final int    brightness;
	public final int    stars;

	public SharplessEntry(String str) 
	{
		if (str != null && ! str.equals("")) {
			String[] field = str.split("[,]");
			ra_str        = field[0].trim();
			ra_ddeg       = Double.parseDouble(ra_str);
			ra_dhrs       = PracticalAstronomy.degrees_to_hours(ra_ddeg);
			ra_hrs        = PracticalAstronomy.hour_of_decimal_hours(ra_dhrs);
			ra_min        = PracticalAstronomy.minute_of_decimal_hours(ra_dhrs);
			ra_sec        = PracticalAstronomy.second_of_decimal_hours(ra_dhrs);
			de_str        = field[1].trim();
			de_ddeg       = Double.parseDouble(de_str);
			de_deg        = PracticalAstronomy.degree_of_decimal_degrees(de_ddeg);
			de_min        = PracticalAstronomy.minute_of_decimal_degrees(de_ddeg);
			de_sec        = PracticalAstronomy.second_of_decimal_degrees(de_ddeg);
			number        = Integer.parseInt(field[2].trim());
			name          = String.format("Sh2-%d", number);
			diameter      = Integer.parseInt(field[9].trim());
			form          = Integer.parseInt(field[10].trim());
			structure     = Integer.parseInt(field[11].trim());
			brightness    = Integer.parseInt(field[12].trim());
			stars         = Integer.parseInt(field[13].trim());
		} else {
			ra_str        = null;
			ra_ddeg       = Double.NaN;
			ra_dhrs       = Double.NaN;
			ra_hrs        = Double.NaN;
			ra_min        = Double.NaN;
			ra_sec        = Double.NaN;
			de_str        = null;
			de_ddeg       = Double.NaN;
			de_deg        = Double.NaN;
			de_min        = Double.NaN;
			de_sec        = Double.NaN;
			number        = Integer.MIN_VALUE;
			name          = null;
			diameter      = Integer.MIN_VALUE;
			form          = Integer.MIN_VALUE;
			structure     = Integer.MIN_VALUE;
			brightness    = Integer.MIN_VALUE;
			stars         = Integer.MIN_VALUE;
		}
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(diameter, 0, DsoType.type_list[DsoType.NEBULA].list[0]);
	}

	@Override public int compareTo(SharplessEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, SharplessEntry[] list)
	{
		return Integer.parseInt(name.substring(4)) - 1;
	}

	@Override public String toString()
	{
		return String.format("%s %f %f %d %.0f %d %d %d %d", name, ra_ddeg, de_ddeg, number, diameter, form, structure, brightness, stars);
	}

	public static void main(String[] args) throws IOException 
	{
		String c1s = "239.713366,-26.120520,   1,315.2, 19.0,347.2, 20.2,15 52 48.0,-25 50 00, 150,3,2,3, 1";
		SharplessEntry c1 = new SharplessEntry(c1s);
		System.out.printf("%s%n", c1);
	}
}
