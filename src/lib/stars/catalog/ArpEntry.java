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


public class ArpEntry implements Comparable<ArpEntry> {
	
	public final String name;				// Arp XXX
	public final int    number;				// Arp number
	public final String common_name;		
	public final String rah_str;			// right ascension (J2000 hours)
	public final double ra_hrs;				// right ascension (J2000 hours)
	public final String ram_str;			// right ascension (J2000 minutes)
	public final double ra_min;				// right ascension (J2000 minutes)
	public final double ra_dhrs;			// right ascension (J2000 decimal hours)
	public final String ded_str;			// declination (J2000 degrees)
	public final double de_deg;				// declination (J2000 degrees)
	public final String dem_str;			// declination (J2000 minutes)
	public final double de_min;				// declination (J2000 minutes)
	public final double de_ddeg;			// declination (J2000 decimal degrees)
	public final double size;				// long dimension (arcmin) of Arp's original photo
	public final String orient;				// orientation of Arp's photo [N,S,E,W,?]
	public final double fl_245;				// Focal length for CB245 CCD Camera
	public final double fl_ST6;				// Focal length for SBIG ST6 CCD Camera
	public final double fl_ST5;				// Focal length for SBIG ST5 CCD Camera
	public final double amag;				// apparent magnitude
	
	public enum Membership {IC, NGC, MESSIER, UGC, UNKNOWN}
	public final Membership catalog;

	public ArpEntry(String str, NgcIcCatalog ngc, UgcCatalog ugc, MessierCatalog messier) 
	{
		if (str != null) {
			String[] field = new String[12];
			field[ 0] = str.substring( 0,  3);			// Arp number from original catalog
			field[ 1] = str.substring( 4, 21);			// Common name (group or brightest)
			field[ 2] = str.substring(21, 23);			// Right Ascension J2000 (hours)
			field[ 3] = str.substring(24, 29);			// Right Ascension J2000 (minutes)
			field[ 4] = str.substring(30, 31);			// Declination J2000 (sign)
			field[ 5] = str.substring(31, 33);			// Declination J2000 (degrees)
			field[ 6] = str.substring(34, 38);			// Declination J2000 (minutes)
			field[ 7] = str.substring(39, 43);			// Long dimension (arcmin) of Arp's original photo
			field[ 8] = str.substring(44, 45);			// Orientation of Arp's photo [N,S,E,W,?]
			field[ 9] = str.substring(46, 49);			// Focal length for CB245 CCD Camera
			field[10] = str.substring(50, 53);			// Focal length for SBIG ST6 CCD Camera
			field[11] = str.substring(54, 57);			// Focal length for SBIG ST5 CCD Camera

			name        = "Arp " + field[0].trim();
			number      = Integer.parseInt(field[ 0].trim());
			common_name = field[1].trim();
			rah_str     = field[2].trim();
			ra_hrs      = Double.parseDouble(rah_str);
			ram_str     = field[3].trim();
			ra_min      = Double.parseDouble(ram_str);
			ra_dhrs     = PracticalAstronomy.hms_to_decimal_hours(ra_hrs, ra_min);
			ded_str     = field[5].trim();
			de_deg      = Double.parseDouble(ded_str);
			dem_str     = field[6].trim();
			de_min      = Double.parseDouble(dem_str);
			de_ddeg     = (field[4].charAt(0) == '-' ? -1 : 1) * PracticalAstronomy.dms_to_decimal_degrees(de_deg, de_min);
			size        = Double.parseDouble(field[7].trim());
			orient      = field[8].trim();
			fl_245      = Double.parseDouble(field[ 9].trim());
			fl_ST6      = Double.parseDouble(field[10].trim());
			fl_ST5      = Double.parseDouble(field[11].trim());
			if (common_name.matches("[Ii][Cc].*")) {
				catalog = Membership.IC;
				NgcIcEntry elt = (ngc != null) ?  ngc.find(common_name) : null;
				amag    = (elt != null) ? elt.app_mag : 9999;
			} else if (common_name.matches("[Nn][Gg][Cc].*")) {
				catalog = Membership.NGC;
				NgcIcEntry elt = (ngc != null) ?  ngc.find(common_name) : null;
				amag    = (elt != null) ? elt.app_mag : 9999;
			} else if (common_name.matches("[Uu][Gg][Cc].*")) {
				catalog = Membership.UGC;
				UgcEntry elt = (ugc != null) ? ugc.find(common_name) : null;
				amag    = (elt != null) ? elt.photo_mag : 9999;
			} else if (common_name.matches("[Mm][Ee][Ss][Ss][Ii][Ee][Rr].*")) {
				catalog = Membership.MESSIER;
				MessierEntry elt = (messier != null) ? messier.find(common_name) : null;
				amag    = (elt != null) ? elt.amag : 9999;
			} else {
				catalog = Membership.UNKNOWN;
				amag    = 9999;
			}
		} else {
			name        = null;
			number      = Integer.MIN_VALUE;
			common_name = null;
			rah_str     = null;
			ra_hrs      = Double.NaN;
			ram_str     = null;
			ra_min      = Double.NaN;
			ra_dhrs     = Double.NaN;
			ded_str     = null;
			de_deg      = Double.NaN;
			dem_str     = null;
			de_min      = Double.NaN;
			de_ddeg     = Double.NaN;
			size        = Double.NaN;
			orient      = null;
			fl_245      = Double.NaN;
			fl_ST6      = Double.NaN;
			fl_ST5      = Double.NaN;
			amag        = Double.NaN;
			catalog     = Membership.UNKNOWN;
		}
	}

	public String name()
	{
		String str = "";
		if (common_name == null || common_name.equals("") || common_name.equals("-")) {
			str = String.format("%s", name);
		} else {
			str = String.format("%s (%s)", name, common_name);
		}

		return str;
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(size, amag, DsoType.type_list[DsoType.IRREGULAR_GALAXY].list[0]);
	}

	@Override public int compareTo(ArpEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, ArpEntry[] list)
	{
		return find(name.replace('m', 'M').replaceAll("[ ]", ""), list, 0, list.length);
	}

	private static int find(String name, ArpEntry[] list, int lower, int upper)
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

	@Override public String toString()
	{
		return String.format("%s, %d, %s, %f, %f, %.1f %s %.0f %.0f %.0f", name, number, common_name, ra_dhrs, de_ddeg, size, orient, fl_245, fl_ST6, fl_ST5);
	}
}
