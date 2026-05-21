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


public class UgcEntry implements Comparable<UgcEntry> {
	
	public final String name;				// UGS XXX
	public final int    number;				// UGS number
	public final String rad_str;			// right ascension (J2000 decimal degrees)
	public final double ra_ddeg;			// right ascension (J2000 decimal degrees)
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final String ded_str;			// declination (J2000 degrees)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final String mcg;				// MCG identifier
	public final int    poss;				// POSS identifier
	public final double maj_axis_blue;		// Major axis (arcmin) as measured on the POSS blue print
	public final double min_axis_blue;		// Minor axis (arcmin) as measured on the POSS blue print
	public final double pos_angle;			// Position angle (degrees)
	public final double maj_axis_red;		// Major axis (arcmin) as measured on the POSS red print
	public final double min_axis_red;		// Minor axis (arcmin) as measured on the POSS red print
	public final String hubble;				// Classification in the Hubble system
	public final double photo_mag;			// Photographic magnitude
	public final double rad_vel;			// Radial velocity (km/s)
	public final int    incline;			// Inclination to the line of sight for spirals [0/7]

	public UgcEntry(String str) 
	{
		if (str != null) {
			String[] field = new String[20];
			field[ 0] = str.substring(  0,   8);			// Right ascension J2000 (deg)
			field[ 1] = str.substring(  9,  17);			// Declination J2000 (deg)
			field[ 2] = str.substring( 18,  23);			// UGC number
			field[ 3] = str.substring( 24,  25);			// "A" if the galaxy is from the Addenda list of the published catalogue; otherwise blank
			field[ 4] = str.substring( 26,  28);			// Right ascension 1950 (hours)
			field[ 5] = str.substring( 29,  33);			// Right ascension 1950 (minutes)
			field[ 6] = str.substring( 34,  37);			// Declination 1950 (degrees)
			field[ 7] = str.substring( 38,  40);			// Declination 1950 (minutes)
			field[ 8] = str.substring( 41,  54);			// MCG
			field[ 9] = str.substring( 55,  59);			// POSS
			field[10] = str.substring( 60,  66);			// Major axis (arcmin) as measured on the POSS blue print
			field[11] = str.substring( 67,  72);			// Minor axis (arcmin) as measured on the POSS blue print
			field[12] = str.substring( 73,  76);			// Position angle (degrees)
			field[13] = str.substring( 77,  84);			// Classification in the Hubble system
			field[14] = str.substring( 85,  89);			// Photographic magnitude
			field[15] = str.substring( 90,  95);			// Radial velocity (km/s)
			field[16] = str.substring( 96, 102);			// Major axis (arcmin) as measured on the POSS red print
			field[17] = str.substring(103, 108);			// Minor axis (arcmin) as measured on the POSS red print
			field[18] = str.substring(109, 110);			// Inclination to the line of sight for spirals [0/7]
			field[19] = str.substring(111, 119);			// Arcsecond position by Cotton and Condon

			name          = "UGC " + field[2].trim();
			number        = Integer.parseInt(field[2].trim());
			rad_str       = field[0].trim();
			ra_ddeg       = Double.parseDouble(rad_str);
			ra_dhrs       = PracticalAstronomy.degrees_to_hours(ra_ddeg);
			ded_str       = field[1].trim();
			de_ddeg       = Double.parseDouble(ded_str);
			mcg           = field[8].trim();
			poss          = Integer.parseInt(field[9].trim());
			maj_axis_blue = (! field[10].trim().equals("")) ? Double.parseDouble(field[10].trim()) : 0;
			min_axis_blue = (! field[11].trim().equals("")) ? Double.parseDouble(field[11].trim()) : 0;
			pos_angle     = (! field[12].trim().equals("")) ? Double.parseDouble(field[12].trim()) : 0;
			hubble        = field[13].trim();
			photo_mag     = (! field[14].trim().equals("")) ? Double.parseDouble(field[14].trim()) : 9999;
			rad_vel       = (! field[15].trim().equals("")) ? Double.parseDouble(field[15].trim()) : 0;
			maj_axis_red  = (! field[16].trim().equals("")) ? Double.parseDouble(field[16].trim()) : 0;
			min_axis_red  = (! field[17].trim().equals("")) ? Double.parseDouble(field[17].trim()) : 0;
			incline       = (! field[18].trim().equals("")) ? Integer.parseInt(field[18].trim())   : 0;
		} else {
			name          = null;
			number        = Integer.MIN_VALUE;
			rad_str       = null;
			ra_ddeg       = Double.NaN;
			ra_dhrs       = Double.NaN;
			ded_str       = null;
			de_ddeg       = Double.NaN;
			mcg           = null;
			poss          = Integer.MIN_VALUE;
			maj_axis_blue = Double.NaN;
			min_axis_blue = Double.NaN;
			pos_angle     = Double.NaN;
			hubble        = null;
			photo_mag     = Double.NaN;
			rad_vel       = Double.NaN;
			maj_axis_red  = Double.NaN;
			min_axis_red  = Double.NaN;
			incline       = Integer.MIN_VALUE;
		}
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(maj_axis_blue, photo_mag, DsoType.type_list[DsoType.GALAXY].list[0]);
	}

	@Override public int compareTo(UgcEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, UgcEntry[] list)
	{
		int index = -1;

		if (name != null && name.matches("[Uu][Gg][Cc].*") && list != null) {
			name = name.toUpperCase();
			String ugc_name = name.replaceAll("UGC *", "").replaceAll("[ A-Za-z+]*", "");
			int number = Integer.parseInt(ugc_name);
			index = find(number, list, 0, list.length);
		}
		return index;
	}

	private static int find(int number, UgcEntry[] list, int lower, int upper)
	{
		int result = -1;
		
		for (int i=0; i < list.length; i++) {
			UgcEntry elt = list[i];
			if (elt.number == number) {
				result = i;
				break;
			}
		}
		
		return result;
	}

	@Override public String toString()
	{
		return String.format("%s, %d, %f, %f", name, number, ra_dhrs, de_ddeg);
	}

	public static void main(String[] args) throws IOException 
	{
		// String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String c1s = "000.6411 +16.6450     1   00 00.0 +16 22 MCG+03-01-015 1195   1.50  1.30     DBL SYS 14.9                      Position";
		UgcEntry c1 = new UgcEntry(c1s);
		System.out.printf("%s%n", c1);
	}
}
