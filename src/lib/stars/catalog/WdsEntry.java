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

import lib.astro.PracticalAstronomy;

// Washington Double Star Catalog
public class WdsEntry implements Comparable<WdsEntry> {
	// 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111
	// 000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999000000000011111111112222222222333333333344444
	// 123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234
	// 00000+7530A  1248      1904 1982    5 246 235   0.8   0.6 10.27 11.5  A7IV      +034+005          +74 1056      000006.64+752859.8  Cep
	// 00000+4004ES 2543AB    1931 2015    6 252 253   4.8   4.4 12.1  13.1            +008-004 -011-002               000003.66+400519.4  And
	// 00023-2943B   631      1925 1927    3 306 330   2.0   3.0  5.00 13.   B4III     +019+016          -3019790 NX   000219.91-294313.6  Scl  zet Scl
	// 00022+2705HSW   1AE    1993 1998    2 310 309  93.6 100.9  5.83 15.67 G5VbFe-2  +830-989 -024+002          NU   000210.18+270455.6  Peg  85 Peg
	public final String name;				// WDS xxxxx[+-]xxxx
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double mag_a;				// magnitude of brighter star
	public final double mag_b;				// magnitude of lesser star
	public final String constellation;		// three-letter constellation code
	public final String bayer;				// constellation and bayer/flamsteed designator
	
	public WdsEntry(String str) 
	{
		if (str != null && 135 <= str.length()) {
			name = str.substring(0, 10).trim();

			String hrs, min, deg;
			double h, m, d;
			hrs = str.substring(0, 2).trim();
			h = Double.parseDouble(hrs);
			min = str.substring(2, 5).trim();
			m = Double.parseDouble(min) / 10;
			ra_dhrs = PracticalAstronomy.hms_to_decimal_hours(h, m);

			deg = str.substring(5, 8).trim();
			d = Double.parseDouble(deg);
			min = str.substring(8, 10).trim();
			m = Double.parseDouble(min);
			de_ddeg = PracticalAstronomy.dms_to_decimal_degrees(d, m);

			String ma = str.substring(58, 63).replaceAll("[ ]", "");
			String mb = str.substring(64, 68).replaceAll("[ ]", "");
			mag_a = (ma == null || ma.equals("") || ma.equals(".")) ? 0 : Double.parseDouble(ma);
			mag_b = (mb == null || mb.equals("") || mb.equals(".")) ? 0 : Double.parseDouble(mb);

			constellation = str.substring(132, 135).trim();

			bayer = (135 < str.length()) ? str.substring(135).trim() : "";
		} else {
			name = constellation = bayer = null;
			ra_dhrs = de_ddeg = Double.NaN;
			mag_a = mag_b = Double.NaN;
		}
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches((mag_b < mag_a) ? mag_a : mag_b, DsoType.type_list[DsoType.VARIABLE_STAR].list[0]);
	}

	@Override public int compareTo(WdsEntry rhs)
	{
		if (ra_dhrs < rhs.ra_dhrs) {
			return -1;
		} else if (rhs.ra_dhrs < ra_dhrs) {
			return +1;
		} else {
			if (de_ddeg < rhs.de_ddeg) {
				return -1;
			} else if (rhs.de_ddeg < de_ddeg) {
				return +1;
			} else {
				return 0;
			}
		}
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, WdsEntry[] list)
	{
		return find(name.replaceAll("[Ww][Dd][Ss]", "").replaceAll("[ ]", ""), list, 0, list.length);
	}

	private static int find(String name, WdsEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = list[middle].name.compareTo(name);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(name, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(name, list, lower, middle);
		}
		
		return result;
	}
	
	public String name()
	{
		return String.format("WDS %s%s", name, (bayer == null || bayer.equals("")) ? "" : " (" + bayer + ")");
	}

	@Override public String toString()
	{
		return String.format("%s, %s, %s, %.2f, %.2f", 
				name(),
				PracticalAstronomy.decimal_hours_to_str_hms(ra_dhrs), 
				PracticalAstronomy.decimal_degrees_to_str_dms(de_ddeg), 
				mag_a, mag_b);
	}
}
