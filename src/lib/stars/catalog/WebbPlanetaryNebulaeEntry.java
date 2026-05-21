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


public class WebbPlanetaryNebulaeEntry implements Comparable<WebbPlanetaryNebulaeEntry> {
	public final int    webb_idx;			// Webb Society Catalog Index
	public final String catalog_id;			// Catalog identifier, e.g., NGC or IC
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double m_n;				// 
	public final double m_s;				// 
	public final double maj_axis;			// apparent diameter in seconds of arc 
	public final double min_axis;			// apparent diameter in seconds of arc 
	public final String type;				// 
	public final double r;					// 
	public final String star;				// 
	public final double RV;					// 
	public final String constellation;		// 

	public WebbPlanetaryNebulaeEntry(String str) 
	{
		if (str != null && 88 <= str.length()) {
			// 00000000001111111111222222222233333333334444444444555555555566666666667777777777888888888
			// 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
			// |WS  |Cat     |RA     |Dec   |m(n)   |m(s)   |AD     |Type    |r    |Star    |RV    |Con|
			// |   1|NGC 40  |00 17.1|+72 23|10.7   |11.6pvi| 60x38 |IIIb    | 1.17|WC8     | -20.5|Cep|



			String[] field = new String[12];
			field[ 0] = str.substring( 1,  5).trim();			// Webb Society Catalog Index
			field[ 1] = str.substring( 6, 14).trim();			// Catalog identifier, e.g., NGC or IC
			field[ 2] = str.substring(15, 22).trim();			// Right Ascension (hrs min)
			field[ 3] = str.substring(23, 29).trim();			// Declination
			field[ 4] = str.substring(30, 34).trim();			// m(n)
			field[ 5] = str.substring(38, 42).trim();			// m(s)
			field[ 6] = str.substring(46, 53).trim();			// Apparent diameter in seconds of arc
			field[ 7] = str.substring(54, 62).trim();			// Type
			field[ 8] = str.substring(63, 68).trim();			// r
			field[ 9] = str.substring(69, 77).trim();			// Star
			field[10] = str.substring(78, 84).trim();			// RV
			field[11] = str.substring(85, 88).trim();			// Constellation

			webb_idx    = Integer.parseInt(field[ 0]);
			catalog_id  = field[1];
			String[] s  = field[2].split(" ");
			ra_dhrs     = PracticalAstronomy.hms_to_decimal_hours(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			s = field[3].split(" ");
			de_ddeg     = PracticalAstronomy.dms_to_decimal_degrees(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			m_n         = (! field[4].equals("")) ? Double.parseDouble(field[4]) : 0;
			m_s         = (! field[5].equals("")) ? Double.parseDouble(field[5]) : 0;
			if (field[6].equals("")) {
				maj_axis = min_axis = 0;
			} else {
				s = field[6].split("x");
				if (1 == s.length) {
					maj_axis = min_axis = Double.parseDouble(s[0]);
				} else if (1 < s.length) {
					maj_axis = Double.parseDouble(s[0]);
					min_axis = Double.parseDouble(s[1]);
				} else {
					maj_axis = min_axis = 0;
				}
			}
			type          = field[7];
			r             = (! field[8].equals("")) ? Double.parseDouble(field[8]) : 0;
			star          = field[9];
			RV            = (! field[10].equals("")) ? Double.parseDouble(field[10]) : 0;
			constellation = field[11];
		} else {
			webb_idx      = Integer.MIN_VALUE;
			catalog_id    = null;
			ra_dhrs       = Double.NaN;
			de_ddeg       = Double.NaN;
			m_n           = Double.NaN;
			m_s           = Double.NaN;
			maj_axis      = Double.NaN;
			min_axis      = Double.NaN;
			type          = null;
			r             = Double.NaN;
			star          = null;
			RV            = Double.NaN;
			constellation = null;
		}
	}

	@Override public int compareTo(WebbPlanetaryNebulaeEntry rhs)
	{
		return Integer.compare(webb_idx, rhs.webb_idx);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(int webb_idx, WebbPlanetaryNebulaeEntry[] list)
	{
		return find(webb_idx, list, 0, list.length);
	}

	private static int find(int webb_idx, WebbPlanetaryNebulaeEntry[] list, int lower, int upper)
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
		return String.format("WS%d, %s, %f, %f, %f x %f, %s", webb_idx, catalog_id, ra_dhrs, de_ddeg, maj_axis, min_axis, constellation);
	}

	public static void main(String[] args) throws IOException 
	{
		// String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String c1s = "|   1|NGC 40  |00 17.1|+72 23|10.7   |11.6pvi| 60x38 |IIIb    | 1.17|WC8     | -20.5|Cep|";
		WebbPlanetaryNebulaeEntry c1 = new WebbPlanetaryNebulaeEntry(c1s);
		System.out.printf("%s%n", c1);
	}
}
