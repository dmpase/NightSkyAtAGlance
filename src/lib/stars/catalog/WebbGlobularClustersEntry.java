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


public class WebbGlobularClustersEntry implements Comparable<WebbGlobularClustersEntry> {
	public final int    webb_idx;			// Webb Society Catalog Index
	public final String catalog_id;			// Catalog identifier, e.g., NGC or IC
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double m;					// apparent magnitude
	public final double diameter;			// apparent diameter in seconds of arc 
	public final String con_cl;				// Concentration classes of clusters
	public final String constellation;		// 

	public WebbGlobularClustersEntry(String str) 
	{
		if (str != null && 58 <= str.length()) {
			// 0000000000111111111122222222223333333333444444444455555555
			// 0123456789012345678901234567890123456789012345678901234567
			// |WS  |Cat          |RA     |Dec   |m    |AD  |Con Cl |Con|
			// | 367|NGC 1904     |05 23.2|-24 33| 7.84| 7.8|V      |Lep|

			String[] field = new String[8];
			field[0] = str.substring( 1,  5).trim();			// Webb Society Catalog Index
			field[1] = str.substring( 6, 19).trim();			// Catalog identifier, e.g., NGC or IC
			field[2] = str.substring(20, 27).trim();			// Right Ascension (hrs min)
			field[3] = str.substring(28, 34).trim();			// Declination (deg min)
			field[4] = str.substring(35, 40).trim();			// Apparent magnitude m
			field[5] = str.substring(41, 45).trim();			// Apparent diameter in seconds of arc
			field[6] = str.substring(46, 53).trim();			// Concentration classes of clusters
			field[7] = str.substring(54, 57).trim();			// constellation

			webb_idx      = Integer.parseInt(field[ 0]);
			catalog_id    = field[1];
			String[] s    = field[2].split(" ");
			ra_dhrs       = PracticalAstronomy.hms_to_decimal_hours(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			s = field[3].split(" ");
			de_ddeg       = PracticalAstronomy.dms_to_decimal_degrees(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			m             = (! field[4].equals("") && ! field[4].equals("-")) ? Double.parseDouble(field[4]) : 0;
			diameter      = (! field[5].equals("")) ? Double.parseDouble(field[5]) : 0;
			con_cl        = field[6];
			constellation = field[7];
		} else {
			webb_idx      = Integer.MIN_VALUE;
			catalog_id    = null;
			ra_dhrs       = Double.NaN;
			de_ddeg       = Double.NaN;
			m             = Double.NaN;
			diameter      = Double.NaN;
			con_cl        = null;
			constellation = null;
		}
	}

	@Override public int compareTo(WebbGlobularClustersEntry rhs)
	{
		return Integer.compare(webb_idx, rhs.webb_idx);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(int webb_idx, WebbGlobularClustersEntry[] list)
	{
		return find(webb_idx, list, 0, list.length);
	}

	private static int find(int webb_idx, WebbGlobularClustersEntry[] list, int lower, int upper)
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
		return String.format("WS%d, %s, %f, %f, %f, %f, %s", webb_idx, catalog_id, ra_dhrs, de_ddeg, m, diameter, constellation);
	}

	public static void main(String[] args) throws IOException 
	{
		// String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String c1s = "| 367|NGC 1904     |05 23.2|-24 33| 7.84| 7.8|V      |Lep|";
		WebbGlobularClustersEntry c1 = new WebbGlobularClustersEntry(c1s);
		System.out.printf("%s%n", c1);
	}
}
