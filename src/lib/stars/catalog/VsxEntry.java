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


public class VsxEntry implements Comparable<VsxEntry> {
	//                                                                                              l_min|                             l_Period|
	//      n_OID|                                                            l_max|    u_max|    f_min| |    u_min|                  u_Epoch| |             u_Period|
	//      OID| |                          Name|V|                          Type| |    max| |  n_max| | |    min| |   n_min|         Epoch| | |             Period| |                           Sp|  RAJ2000|DEJ2000
	//         | |                              | |                              | |    mag| |       | | |    mag| |        |             d| | |                  d| |                             |      deg|deg
    // --------|-|------------------------------|-|------------------------------|-|-------|-|-------|-|-|-------|-|--------|--------------|-|-|-------------------|-|-----------------------------|---------|---------
    //  8278100| |Gaia DR3 4685168858707787776  |0|L                             | | 16.590| |G      | | | 17.740| |G       |              | | |                   | |K                            |000.00006|-75.86906
    //  2535232| |Gaia DR3 2881873169572728832  |0|ROT                           | | 16.590| |G      | | | 16.640| |G       |              | | |     2.354070000000| |G                            |000.00013|+39.89248
    //  2535233| |Gaia DR3 4918216945285915648  |0|RR                            | | 17.070| |G      | | | 17.600| |G       |              | | |                   | |F                            |000.00019|-59.55921
	//  2535245| |Gaia DR3 429941452002367616   |0|DSCT|GDOR|SXPHE               | | 13.190| |G      | | | 13.230| |G       |              | | |                   | |B                            |000.00243|+62.36470
	// 00000000|0|000000000000000000000000000000|0|000000000000000000000000000000|0|0000000|0|0000000|0|0|0111111|1|11111111|11111111111111|1|1|1111111111111111111|1|11111111111111111111111111111|111111111|122222222
	// 00000000|0|111111111222222222233333333334|4|444444555555555566666666667777|7|7778888|8|8889999|9|9|9000000|0|01111111|11222222222233|3|3|3334444444444555555|5|56666666666777777777788888888|899999999|900000000
	// 01234567|9|123456789012345678901234567890|2|456789012345678901234567890123|5|7890123|5|7890123|5|7|9012345|7|90123456|89012345678901|3|5|7890123456789012345|7|90123456789012345678901234567|901234567|901234567
	//        0|1|                             2|3|                             4|5|      6|7|      8|9|0|     11|2|      13|            14|5|6|                 17|8|                           19|       20|       21 

	public final String name;				// star name
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double max_mag;			// magnitude at maximum brightness
	public final double min_mag;			// magnitude at minimum brightness
	public final double period;				// period in days

	public VsxEntry(String str) 
	{
		if (str != null) {
			String[] field = new String[22];
			field[ 2] = str.substring( 11,  41).trim();
			field[ 6] = str.substring( 77,  84).trim();
			field[11] = str.substring( 99, 106).trim();
			field[17] = str.substring(137, 156).trim();
			field[20] = str.substring(189, 198).trim();
			field[21] = str.substring(199, 208).trim();

			name = field[2];
			String max = field[6];
			max_mag = (max == null || max.equals("")) ? Double.NaN : Double.parseDouble(max);
			String min = field[11];
			min_mag = (min == null || min.equals("")) ? Double.POSITIVE_INFINITY : Double.parseDouble(min);
			String per = field[17];
			period = (per == null || per.equals("")) ? Double.POSITIVE_INFINITY : Double.parseDouble(per);
			String ras = field[20];
			double rad = (ras == null || ras.equals("")) ? Double.NaN : Double.parseDouble(ras);
			ra_dhrs = PracticalAstronomy.degrees_to_hours(rad);
			String des = field[21];
			de_ddeg = (des == null || des.equals("")) ? Double.NaN : Double.parseDouble(des);
		} else {
			name    = null;
			ra_dhrs = Double.NaN;
			de_ddeg = Double.NaN;
			max_mag = Double.NaN;
			min_mag = Double.NaN;
			period  = Double.NaN;
		}
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(max_mag, DsoType.type_list[DsoType.VARIABLE_STAR].list[0]);
	}

	@Override public int compareTo(VsxEntry rhs)
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
	public static int find(String name, VsxEntry[] list)
	{
		return find(name.replaceAll("[ ]", ""), list, 0, list.length);
	}

	private static int find(String name, VsxEntry[] list, int lower, int upper)
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
		return name;
	}

	@Override public String toString()
	{
		return String.format("%-30s, RA %s, DEC %s, max %5.2f, min %5.2f, period %8.2f (days)", 
				name(),
				PracticalAstronomy.decimal_hours_to_str_hms(ra_dhrs), 
				PracticalAstronomy.decimal_degrees_to_str_dms(de_ddeg), 
				max_mag, min_mag, period);
	}
}
