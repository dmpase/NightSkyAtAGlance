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


public class CaldwellEntry implements Comparable<CaldwellEntry> {
	
	public final NgcIcCatalog ngc;
	
	public final String name;
	public final int    number;
	public final String ngc_ic;				// NGC, IC, or Sharpless catalog
	public final String common_name;
	public final String dso_type;
	public final String distance_kly;
	public final String constellation;
	public final double amag;
	public final String ra_str;
	public final double ra_hrs;
	public final double ra_min;
	public final double ra_sec;
	public final double ra_rad;
	public final double ra_dhrs;
	public final String dec_str;
	public final double dec_deg;
	public final double dec_min;
	public final double dec_sec;
	public final double dec_rad;
	public final double dec_ddeg;
	public final double diameter;

	public CaldwellEntry(String str, NgcIcCatalog ngc) 
	{
		this.ngc = ngc;
		if (str != null) {
			String[] field = str.split("[,]");
			if (field != null) {
				name          = field[0].trim();
				number        = Integer.parseInt(name.substring(1));
				ngc_ic        = field[1].trim();
				common_name   = field[2].trim();
				dso_type      = field[3].trim();
				distance_kly  = field[4].trim();
				constellation = field[5].trim();
				amag          = (6 < field.length) ? Double.parseDouble(field[6].trim()) : 0;
				if (ngc_ic.equals("Sh2-155")) {			// Sharpless2 155, cave nebula
					ra_str        = "22h 57m 17.14s";
					ra_hrs        = 22;
					ra_min        = 57;
					ra_sec        = 17.14;
					ra_dhrs       = PracticalAstronomy.hms_to_decimal_hours(ra_hrs, ra_min, ra_sec);
					ra_rad        = PracticalAstronomy.hours_to_radians(ra_dhrs);
					dec_str       = "+62° 28' 33.4\"";
					dec_deg       = 62;
					dec_min       = 28;
					dec_sec       = 33.4;
					dec_ddeg      = PracticalAstronomy.dms_to_decimal_degrees(dec_deg, dec_min, dec_sec);
					dec_rad       = PracticalAstronomy.degrees_to_radians(dec_ddeg);
					diameter      = 50;		// 50' x 30'
				} else if (ngc_ic.equals("Mel 25")) {	// Melotte 25, hyades open cluster
					ra_str        = "4h 27m";
					ra_hrs        = 4;
					ra_min        = 27;
					ra_sec        = 0;
					ra_dhrs       = PracticalAstronomy.hms_to_decimal_hours(ra_hrs, ra_min, ra_sec);
					ra_rad        = PracticalAstronomy.hours_to_radians(ra_dhrs);
					dec_str       = "+15° 52'";
					dec_deg       = 15;
					dec_min       = 52;
					dec_sec       = 0;
					dec_ddeg      = PracticalAstronomy.dms_to_decimal_degrees(dec_deg, dec_min, dec_sec);
					dec_rad       = PracticalAstronomy.degrees_to_radians(dec_ddeg);
					diameter      = 330;		// 330'
				} else if (ngc_ic.equals("")) {			// coal sack
					ra_str        = "12h 50m";
					ra_hrs        = 12;
					ra_min        = 50;
					ra_sec        = 0;
					ra_dhrs       = PracticalAstronomy.hms_to_decimal_hours(ra_hrs, ra_min, ra_sec);
					ra_rad        = PracticalAstronomy.hours_to_radians(ra_dhrs);
					dec_str       = "−62° 30'";
					dec_deg       = -62;
					dec_min       = 30;
					dec_sec       = 0;
					dec_ddeg      = PracticalAstronomy.dms_to_decimal_degrees(dec_deg, dec_min, dec_sec);
					dec_rad       = PracticalAstronomy.degrees_to_radians(dec_ddeg);
					diameter      = 7 * 60;		// 7 x 5 (degrees) or 420' x 300'
				} else {						// all other DSOs
					int idx = ngc.find_idx(ngc_ic);
					ra_str        = String.format("%.0fh %.1fm", ngc.elts[idx].ra_hrs, ngc.elts[idx].ra_min);
					ra_hrs        = ngc.elts[idx].ra_hrs;
					ra_min        = ngc.elts[idx].ra_min;
					ra_sec        = 0;
					ra_rad        = ngc.elts[idx].ra_rad;
					ra_dhrs       = ngc.elts[idx].ra_dhrs;
					dec_str       = String.format("%.0f%s %.1fm", ngc.elts[idx].dec_deg, new String((Character.toChars(0x00B0))), ngc.elts[idx].dec_min);
					dec_deg       = ngc.elts[idx].dec_deg;
					dec_min       = ngc.elts[idx].dec_min;
					dec_sec       = 0;
					dec_rad       = ngc.elts[idx].dec_rad;
					dec_ddeg      = ngc.elts[idx].dec_ddeg;
					diameter      = ngc.elts[idx].ang_diam;
				}
			} else {
				name          = null;
				number        = Integer.MIN_VALUE;
				ngc_ic        = null;
				common_name   = null;
				dso_type      = null;
				distance_kly  = null;
				constellation = null;
				amag          = Double.NaN;
				ra_str        = null;
				ra_hrs        = Double.NaN;
				ra_min        = Double.NaN;
				ra_sec        = Double.NaN;
				ra_rad        = Double.NaN;
				ra_dhrs       = Double.NaN;
				dec_str       = null;
				dec_deg       = Double.NaN;
				dec_min       = Double.NaN;
				dec_sec       = Double.NaN;
				dec_rad       = Double.NaN;
				dec_ddeg      = Double.NaN;
				diameter      = Double.NaN;
			}
		} else {
			name          = null;
			number        = Integer.MIN_VALUE;
			ngc_ic        = null;
			common_name   = null;
			dso_type      = null;
			distance_kly  = null;
			constellation = null;
			amag          = Double.NaN;
			ra_str        = null;
			ra_hrs        = Double.NaN;
			ra_min        = Double.NaN;
			ra_sec        = Double.NaN;
			ra_rad        = Double.NaN;
			ra_dhrs       = Double.NaN;
			dec_str       = null;
			dec_deg       = Double.NaN;
			dec_min       = Double.NaN;
			dec_sec       = Double.NaN;
			dec_rad       = Double.NaN;
			dec_ddeg      = Double.NaN;
			diameter      = Double.NaN;
		}
	}

	public String name()
	{
		String str = "";
		if ((ngc_ic == null || ngc_ic.equals("")) && (common_name == null || common_name.equals(""))) {
			str = String.format("%s", name);
		} else if (ngc_ic == null || ngc_ic.equals("")) {
			str = String.format("%s (%s)", name, common_name);
		} else if (common_name == null || common_name.equals("")) {
			str = String.format("%s (%s)", name, ngc_ic);
		} else {
			str = String.format("%s (%s, %s)", name, ngc_ic, common_name);
		}

		return str;
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(diameter, amag, dso_type);
	}

	@Override public int compareTo(CaldwellEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, CaldwellEntry[] list)
	{
		return find(name.replace('c', 'C').replaceAll("[ ]", ""), list, 0, list.length);
	}

	private static int find(String name, CaldwellEntry[] list, int lower, int upper)
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

	// locate the Caldwell object by its NGC, IC, or Sharpless equivalent 
	public static int find_alias_idx(String ngc_ic_sh, CaldwellEntry[] list)
	{
		int index = -1;
		
		if (ngc_ic_sh != null && ! ngc_ic_sh.equals("")) {
			ngc_ic_sh = ngc_ic_sh.trim().toUpperCase().replaceAll("[ ][ ]*", "");
			for (int i=0; i < list.length; i++) {
				CaldwellEntry elt = list[i];
				if (elt.ngc_ic != null && elt.ngc_ic.trim().toUpperCase().replaceAll("[ ][ ]*", "").equals(ngc_ic_sh)) {
					index = i;
					break;
				}
			}
		}

		return index;
	}

	@Override public String toString()
	{
		return String.format("C%3d, %8s, %s, %s, %s, %.2f ra=%02.0fh %02.0fm %02.2fs dec=%02.0f° %02.0f' %02.2f\"", 
				number, ngc_ic, common_name, dso_type, constellation, amag, ra_hrs, ra_min, ra_sec, dec_deg, dec_min, dec_sec);
	}
}
