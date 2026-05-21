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

import lib.sphere.Angle;


public class MessierEntry implements Comparable<MessierEntry> {
	
	public final String name;
	public final int    number;
	public final String ngc_ic;
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

	public MessierEntry(String str, NgcIcCatalog ngc) 
	{
		if (str != null) {
			String[] field = str.split("[|]");
			if (field != null) {
				name          = field[1].trim();
				number        = Integer.parseInt(name.substring(1));
				ngc_ic        = (field[2].charAt(0) == '-') ? null : field[2].trim();
				common_name   = field[3].trim();
				dso_type      = field[4].trim();
				distance_kly  = field[5].trim();
				constellation = field[6].trim();
				amag          = Double.parseDouble(field[7].trim());
				ra_str        = field[8].trim();
				String[] raf  = ra_str.split("[ ]");
				ra_hrs        = Double.parseDouble(raf[0].replaceAll("[^.0-9+-]", ""));
				ra_min        = Double.parseDouble(raf[1].replaceAll("[^.0-9+-]", ""));
				ra_sec        = (raf.length < 3) ? 0 : Double.parseDouble(raf[2].replaceAll("[^.0-9+-]", ""));
				ra_rad        = Angle.hms_to_rad(ra_hrs, ra_min, ra_sec);
				ra_dhrs       = Angle.rad_to_hrs(ra_rad);
				dec_str       = field[9].trim();
				String[] decf = dec_str.split("[ ]");
				boolean sign = dec_str.charAt(0) == '-';
				dec_deg       = Double.parseDouble(decf[0].replaceAll("[^.0-9+-]", ""));
				dec_min       = Double.parseDouble(decf[1].replaceAll("[^.0-9+-]", ""));
				dec_sec       = (decf.length < 3) ? 0 : Double.parseDouble(decf[2].replaceAll("[^.0-9+-]", ""));
				dec_rad       = (sign ? -1 : 1) * Angle.dms_to_rad(dec_deg, dec_min, dec_sec);
				dec_ddeg      = Angle.rad_to_deg(dec_rad);
				int idx       = (ngc == null) ? -1 : ngc.find_idx(ngc_ic);
				diameter      = (0 <= idx && idx < ngc.elts.length) ? ngc.elts[idx].ang_diam : 0;
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
		if ((ngc_ic == null || ngc_ic.equals("") || ngc_ic.equals("-")) && (common_name == null || common_name.equals("") || common_name.equals("-"))) {
			str = String.format("%s", name);
		} else if (ngc_ic == null || ngc_ic.equals("") || ngc_ic.equals("-")) {
			str = String.format("%s (%s)", name, common_name);
		} else if (common_name == null || common_name.equals("") || common_name.equals("-")) {
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

	@Override public int compareTo(MessierEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, MessierEntry[] list)
	{
		name = name.replaceAll("[Mm][Ee][Ss][Ss][Ii][Ee][Rr]", "M").replace('m', 'M').replaceAll("[ ]", "");

		return find(name, list, 0, list.length);
	}

	private static int find(String name, MessierEntry[] list, int lower, int upper)
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

	// locate the Messier object by its NGC, IC, or Sharpless equivalent 
	public static int find_alias_idx(String ngc_ic_sh, MessierEntry[] list)
	{
		int index = -1;
		
		if (ngc_ic_sh != null && ! ngc_ic_sh.equals("")) {
			ngc_ic_sh = ngc_ic_sh.trim().toUpperCase().replaceAll("[ ][ ]*", "");
			for (int i=0; i < list.length; i++) {
				MessierEntry elt = list[i];
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
		return String.format("M%3d, %8s, %s, %s, %s, %.2f ra=%02.0fh %02.0fm %02.2fs dec=%02.0f° %02.0f' %02.2f\"", 
				number, ngc_ic, common_name, dso_type, constellation, amag, ra_hrs, ra_min, ra_sec, dec_deg, dec_min, dec_sec);
	}
}
