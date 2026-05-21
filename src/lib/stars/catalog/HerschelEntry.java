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


public class HerschelEntry implements Comparable<HerschelEntry> {
	
	public final String name;
	public final int    number;				// Herschel number
	public final String cald_or_mess;		// Caldwell or Messier number
	public final String ngc_ic;				// NGC, IC, or UGC catalog
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

	public HerschelEntry(String str, NgcIcCatalog ngc) 
	{
		if (str != null) {
			String[] field = str.split("[,]");
			if (field != null) {
				name          = "H"+field[0].trim();
				number        = Integer.parseInt(field[0].trim());
				cald_or_mess  = field[1].trim();
				ngc_ic        = field[2].trim();
				common_name   = field[3].trim();
				dso_type      = field[4].trim();
				distance_kly  = field[5].trim();
				constellation = field[6].trim();
				int idx = ngc.find_idx(ngc_ic);
				amag          = (7 < field.length && ! field[7].trim().equals("")) ? Double.parseDouble(field[7].trim()) : ngc.elts[idx].app_mag;
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
			} else {
				name          = null;
				number        = Integer.MIN_VALUE;
				cald_or_mess  = null;
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
			cald_or_mess  = null;
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

	@Override public int compareTo(HerschelEntry rhs)
	{
		return Integer.compare(number, rhs.number);
	}

	@Override public String toString()
	{
		return String.format("H%d, %8s, %s, %s, %s, %.2f ra=%02.0fh %02.0fm %02.2fs dec=%02.0f° %02.0f' %02.2f\"", 
				number, ngc_ic, common_name, dso_type, constellation, amag, ra_hrs, ra_min, ra_sec, dec_deg, dec_min, dec_sec);
	}

	public static void main(String[] args) throws IOException 
	{
		String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String ngc_name  = home_dir + "NGC+IC-J2000.txt";
		NgcIcCatalog ngc = new NgcIcCatalog(ngc_name);
		String c1s = "26,M76,NGC 650,Little Dumbbell Nebula,Planetary Nebula,3.4,Perseus,12,,NGC 651,Cork Nebula";
		HerschelEntry c1 = new HerschelEntry(c1s, ngc);
		System.out.printf("%s%n", c1);
	}
}
