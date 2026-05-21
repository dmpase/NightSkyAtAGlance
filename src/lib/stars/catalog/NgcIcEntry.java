package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 1988-2023 Douglas M. Pase                                     *
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

public class NgcIcEntry implements Comparable<NgcIcEntry> {

	public final String  name;
	public final boolean is_ngc;
	public final int     number;
	public final double  ra_hrs;
	public final double  ra_min;
	public final double  ra_rad;
	public final double  ra_dhrs;
	public final double  dec_deg;
	public final double  dec_min;
	public final double  dec_rad;
	public final double  dec_ddeg;
	public final double  app_mag;
	public final String  app_mag_flag;
	public final double  ang_diam;
	public final String  source_type;
	public final double  lii;
	public final double  bii;
	public final String  ref_revision;
	public final String  constellation;
	public final String  description;
	public final String  dso_class;

	public NgcIcEntry(String str)
	{
		if (str != null) {
			String[] field = str.split("[|]");
			if (field != null) {
				name          = field[1].trim();
				is_ngc        = name.substring(0,3).equalsIgnoreCase("NGC");
				number        = (is_ngc) ? Integer.parseInt(name.substring(3).trim()) : Integer.parseInt(name.substring(2).trim());

				String[] ra_field = field[2].split("[ ]");
				ra_hrs        = Double.parseDouble(ra_field[0]);
				ra_min        = Double.parseDouble(ra_field[1]);
				ra_rad        = Angle.hms_to_rad(ra_hrs, ra_min, 0);
				ra_dhrs       = Angle.rad_to_hrs(ra_rad);

				String[] dec_field = field[3].split("[ ]");
				dec_deg       = Double.parseDouble(dec_field[0]);
				dec_min       = Double.parseDouble(dec_field[1]);
				dec_rad       = Angle.dms_to_rad(dec_deg, dec_min, 0);
				dec_ddeg      = Angle.rad_to_deg(dec_rad);

				app_mag       = (field[4] == null || field[4].trim().equals("")) ? 9999 : Double.parseDouble(field[4]);
				app_mag_flag  = (field[5] == null) ? null : field[5].trim();

				ang_diam      = (field[7] == null || field[7].trim().equals("")) ? 0 : Double.parseDouble(field[7]);
				source_type   = (field[8] == null) ? null : field[8].trim();
				lii           = (field [9] == null || field [9].trim().equals("")) ? Double.NaN : Double.parseDouble(field [9]);
				bii           = (field[10] == null || field[10].trim().equals("")) ? Double.NaN : Double.parseDouble(field[10]);
				ref_revision  = (field[11] == null) ? null : field[11].trim();
				constellation = (field[12] == null) ? null : field[12].trim();
				description   = (field[13] == null) ? null : field[13].trim();
				dso_class     = (field[14] == null) ? null : field[14].trim();
			} else {
				name          = null;
				is_ngc        = false;
				number        = Integer.MIN_VALUE;
				ra_hrs        = Double.NaN;
				ra_min        = Double.NaN;
				ra_rad        = Double.NaN;
				ra_dhrs       = Double.NaN;
				dec_deg       = Double.NaN;
				dec_min       = Double.NaN;
				dec_rad       = Double.NaN;
				dec_ddeg      = Double.NaN;
				app_mag       = Double.NaN;
				app_mag_flag  = null;
				ang_diam      = Double.NaN;
				source_type   = null;
				lii           = Double.NaN;
				bii           = Double.NaN;
				ref_revision  = null;
				constellation = null;
				description   = null;
				dso_class     = null;
			}
		} else {
			name          = null;
			is_ngc        = false;
			number        = Integer.MIN_VALUE;
			ra_hrs        = Double.NaN;
			ra_min        = Double.NaN;
			ra_rad        = Double.NaN;
			ra_dhrs       = Double.NaN;
			dec_deg       = Double.NaN;
			dec_min       = Double.NaN;
			dec_rad       = Double.NaN;
			dec_ddeg      = Double.NaN;
			app_mag       = Double.NaN;
			app_mag_flag  = null;
			ang_diam      = Double.NaN;
			source_type   = null;
			lii           = Double.NaN;
			bii           = Double.NaN;
			ref_revision  = null;
			constellation = null;
			description   = null;
			dso_class     = null;
		}
	}
	
	public NgcIcEntry(
			String name, 
			boolean is_ngc, 
			int number, 
			double ra_hrs, 
			double ra_min, 
			double ra_rad, 
			double ra_dhrs, 
			double dec_deg, 
			double dec_min, 
			double dec_rad,
			double dec_ddeg,
			double app_mag,
			String app_mag_flag,
			double ang_diam,
			String source_type,
			double lii,
			double bii,
			String ref_revision,
			String constellation,
			String description,
			String dso_class)
	{
		this.name         = name;
		this.is_ngc       = is_ngc;
		this.number       = number;
		this.ra_hrs       = ra_hrs;
		this.ra_min       = ra_min;
		this.ra_rad       = ra_rad;
		this.ra_dhrs      = ra_dhrs;
		this.dec_deg      = dec_deg;
		this.dec_min      = dec_min;
		this.dec_rad      = dec_rad;
		this.dec_ddeg     = dec_ddeg;
		this.app_mag      = app_mag;
		this.app_mag_flag = app_mag_flag;
		this.ang_diam     = ang_diam;
		this.source_type  = source_type;
		this.lii          = lii;
		this.bii          = bii;
		this.ref_revision = ref_revision;
		this.constellation = constellation;
		this.description   = description;
		this.dso_class     = dso_class;
	}

	public NgcIcEntry update(double app_mag, double ang_diam)
	{
		return new NgcIcEntry(name, is_ngc, number, ra_hrs, ra_min, ra_rad, ra_dhrs, dec_deg, dec_min, dec_rad, dec_ddeg, app_mag, app_mag_flag, ang_diam, source_type, lii, bii, ref_revision, constellation, description, dso_class);
	}

	public boolean matches(DsoFilter filter)
	{
		return filter.matches(ang_diam, app_mag, dso_class);
	}

	public int compareTo(boolean is_ngc, int number) 
	{
		int value;
		if (this.is_ngc == is_ngc) {
			if (this.number == number) {
				value = 0;
			} else if (this.number < number) {
				value = -1;
			} else {
				value = +1;
			}
		} else if (this.is_ngc) {
			value = +1;
		} else {
			value = -1;
		}

		return value;
	}

	@Override public int compareTo(NgcIcEntry rhs) 
	{
		return this.compareTo(rhs.is_ngc, rhs.number);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, NgcIcEntry[] list)
	{
		name = name.trim().toUpperCase();
		boolean is_ngc = 3 < name.length() && name.startsWith("NGC");
		boolean is_ic  = 2 < name.length() && name.startsWith("IC");
		if (is_ngc && name.substring(3).trim().matches("[0-9]+")) {
			int number = Integer.parseInt(name.substring(3).trim());
			return find(true, number, list, 0, list.length);
		} else if (is_ic && name.substring(2).trim().matches("[0-9]+")) {
			int number = Integer.parseInt(name.substring(2).trim());
			return find(false, number, list, 0, list.length);
		}

		return Integer.MIN_VALUE;
	}

	public static int find(boolean is_ngc, int number, NgcIcEntry[] list)
	{
		return find(is_ngc, number, list, 0, list.length);
	}

	private static int find(boolean is_ngc, int number, NgcIcEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = list[middle].compareTo(is_ngc, number);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(is_ngc, number, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(is_ngc, number, list, lower, middle);
		}
		
		return result;
	}
	
	public String toString()
	{
		return String.format("%s %4d ra=%.0fh %.1f' dec=%.0f%s %.1f' mag=%.2f dia=%.1f'", is_ngc?"NGC":"IC ", number, ra_hrs, ra_min, dec_deg, new String((Character.toChars(0x00B0))), dec_min, app_mag, ang_diam);
	}

	public static void main(String[] args) 
	{
		String ic1101s = "|IC 1101 |15 10.9|+05 46|       |            |                  |            |U          |  6.49| 50.57|DD          |Vir          |eF, vS, *13 f 1s .5, *13 p 2s                        |                                       UNIDENTIFIED|";
		NgcIcEntry ic1101 = new NgcIcEntry(ic1101s);
		System.out.printf("name='%s' ra hrs=%f ra min=%f dec deg=%f dec min=%f%n", ic1101.name, ic1101.ra_hrs, ic1101.ra_min, ic1101.dec_deg, ic1101.dec_min);
	}
}
