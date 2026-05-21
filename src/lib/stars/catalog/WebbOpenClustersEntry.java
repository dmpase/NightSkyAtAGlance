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


public class WebbOpenClustersEntry implements Comparable<WebbOpenClustersEntry> {
	public final int    webb_idx;			// Webb Society Catalog Index
	public final String catalog_id;			// Catalog identifier, e.g., NGC or IC
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double m;					// apparent magnitude
	public final double diameter;			// apparent diameter in seconds of arc 
	public final String type;				// 
	public final String constellation;		// 
	public final double aperture;			// Telescope diameter (inches)
	public final double magnification;		// Magnification (x)

	public WebbOpenClustersEntry(String str) 
	{
		if (str != null && 65 <= str.length()) {
			// 00000000001111111111222222222233333333334444444444555555555566666
			// 01234567890123456789012345678901234567890123456789012345678901234
			// |WS  |Cat          |RA     |Dec   |m   |AD  |Type   |Con|Dia|Mag|
			// | 162|NGC 129      |00 28.5|+60 05|10.0|11.0|IV  2 p|Cas| 10| 80|
			// | 163|King 14      |00 30.4|+63 01|  - | 7.3|III 2 p|Cas| 10| 80|

			String[] field = new String[10];
			field[0] = str.substring( 1,  5).trim();			// Webb Society Catalog Index
			field[1] = str.substring( 6, 19).trim();			// Catalog identifier, e.g., NGC or IC
			field[2] = str.substring(20, 27).trim();			// Right Ascension (hrs min)
			field[3] = str.substring(28, 34).trim();			// Declination (deg min)
			field[4] = str.substring(35, 39).trim();			// Apparent magnitude m
			field[5] = str.substring(40, 44).trim();			// Apparent diameter in seconds of arc
			field[6] = str.substring(45, 52).trim();			// Type
			field[7] = str.substring(53, 56).trim();			// constellation
			field[8] = str.substring(57, 60).trim();			// Aperture
			field[9] = str.substring(61, 64).trim();			// Magnification

			webb_idx      = Integer.parseInt(field[ 0]);
			catalog_id    = field[1];
			String[] s    = field[2].split(" ");
			ra_dhrs       = PracticalAstronomy.hms_to_decimal_hours(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			s = field[3].split(" ");
			de_ddeg       = PracticalAstronomy.dms_to_decimal_degrees(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			m             = (! field[4].equals("") && ! field[4].equals("-")) ? Double.parseDouble(field[4]) : 0;
			diameter      = (! field[5].equals("")) ? Double.parseDouble(field[5]) : 0;
			type          = field[6];
			constellation = field[7];
			aperture      = (! field[8].equals("")) ? Double.parseDouble(field[8]) : 0;
			magnification = (! field[9].equals("")) ? Double.parseDouble(field[9]) : 0;
		} else {
			webb_idx      = Integer.MIN_VALUE;
			catalog_id    = null;
			ra_dhrs       = Double.NaN;
			de_ddeg       = Double.NaN;
			m             = Double.NaN;
			diameter      = Double.NaN;
			type          = null;
			constellation = null;
			aperture      = Double.NaN;
			magnification = Double.NaN;
		}
	}

	@Override public int compareTo(WebbOpenClustersEntry rhs)
	{
		return Integer.compare(webb_idx, rhs.webb_idx);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(int webb_idx, WebbOpenClustersEntry[] list)
	{
		return find(webb_idx, list, 0, list.length);
	}

	private static int find(int webb_idx, WebbOpenClustersEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = list[middle].webb_idx - webb_idx;
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(webb_idx, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(webb_idx, list, lower, middle);
		}
		
		return result;
	}

	@Override public String toString()
	{
		return String.format("WS%d, %s, %f, %f, %f, %f, %f, %f, %s", webb_idx, catalog_id, ra_dhrs, de_ddeg, m, diameter, aperture, magnification, constellation);
	}

	public static void main(String[] args) throws IOException 
	{
		// String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String c1s = "| 162|NGC 129      |00 28.5|+60 05|10.0|11.0|IV  2 p|Cas| 10| 80|";
		WebbOpenClustersEntry c1 = new WebbOpenClustersEntry(c1s);
		System.out.printf("%s%n", c1);
	}
}
