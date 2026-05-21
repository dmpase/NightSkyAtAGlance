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

public class VizierEntry implements Comparable<VizierEntry> {

	private static int count = 0;
	
	public final int    id;				// VizieR Record Number (VIZ)
	public final int    hd;				// Henry Draper Catalog (HD)
	public final String dm;				// Durchmusterung Identification from HD Catalog (DM)
	public final int    gc;				// Boss General Catalog (GC)
	public final int    hr;				// Harvard Revised ID of the Yale Bright Star Catalog (HR)
	public final int    hip;			// Hipparcos Catalog ID (HIP)
	public final Angle  ra;				// right ascension
	public final Angle  dec;			// de_B1950
	public final double vmag;			// visual magnitude
	public final Bayer  bayer;			// full BayerCatalog designation (e.g., alf01 ORI)

	private final int HD    = 0;
	private final int DM    = 1;
	private final int GC    = 2;
	private final int HR    = 3;
	private final int HIP   = 4;
	private final int POS   = 5;
	private final int VMAG  = 6;
	private final int FL    = 7;
	private final int BAYER = 8;
	private final int CST   = 9;

	private final int RA_HRS  = 0;
	private final int RA_MIN  = 1;
	private final int RA_SEC  = 2;
	private final int DEC_DEG = 3;
	private final int DEC_MIN = 4;
	private final int DEC_SEC = 5;

	public VizierEntry(String str) 
	{
		String[] fields = str.split("[|]");

		String hd_str    = fields[HD   ].trim();
		String dm_str    = fields[DM   ].trim();
		String gc_str    = fields[GC   ].trim();
		String hr_str    = fields[HR   ].trim();
		String hip_str   = fields[HIP  ].trim();
		String pos_str   = fields[POS  ].trim();
		String vmag_str  = fields[VMAG ].trim();
		String fl_str    = fields[FL   ].trim();
		String bayer_str = fields[BAYER].trim();
		String cst_str   = fields[CST  ].trim();

		id   = ++count;
//		System.out.printf(" 88: %4d: %s%n", id, str);

		hd   = (hd_str.equals("")) ? 0 : Integer.parseInt(hd_str);
		dm   = dm_str;
		gc   = (gc_str.equals("")) ? 0 : Integer.parseInt(gc_str);
		hr   = (hr_str.equals("")) ? 0 : Integer.parseInt(hr_str);
		hip  = (hip_str.equals("")) ? 0 : Integer.parseInt(hip_str);

		String[] pos = pos_str.split("[ ][ ]*");
		double ra_hrs = Double.parseDouble(pos[RA_HRS]);
		double ra_min = Double.parseDouble(pos[RA_MIN]);
		double ra_sec = Double.parseDouble(pos[RA_SEC]);
		ra   = new Angle( ra_hrs,  ra_min,  ra_sec, Angle.Scale.HOURS);

		double sign = (pos[DEC_DEG].charAt(0) == '-') ? -1 : +1;
		pos[DEC_DEG].replace('-', '+');
		double dec_deg = Double.parseDouble(pos[DEC_DEG]);
		double dec_min = Double.parseDouble(pos[DEC_MIN]);
		double dec_sec = Double.parseDouble(pos[DEC_SEC]);
		dec  = new Angle(sign, dec_deg, dec_min, dec_sec, Angle.Scale.DEGREES);

		vmag = (vmag_str.equals("")) ? 0 : Double.parseDouble(vmag_str);
		
		bayer = new Bayer(cst_str, bayer_str, fl_str);
	}
	
	@Override public String toString()
	{
		String result = String.format("[id=%d,hd=%d,dm=%s,gc=%d,ra=%d,hip=%d,ra=%f(%02d:%02d:%05.2f),dec=%f(%s%02d:%02d:%05.2f),vmag=%.2f,bay='%s']", 
				id,
				hd,
				dm,
				gc,
				hr,
				hip,
				ra.radians, (int)(ra.hours), (int)(60*(ra.hours-(int)ra.hours)), 60*(60*ra.hours - (int)(60*ra.hours)),
				dec.radians, (dec.degrees < 0) ? "-" : "+", (int)Math.abs(dec.degrees), (int)(60*(Math.abs(dec.degrees)-(int)Math.abs(dec.degrees))), 60*(60*Math.abs(dec.degrees) - (int)(60*Math.abs(dec.degrees))),
				vmag,
				bayer.toString());

		return result;
	}

	// return the index of the desired element
	public static int find(String name, VizierEntry[] list)
	{
		int result = Integer.MIN_VALUE;
		
		if (name != null) {
			name = name.trim().replaceAll("[ ]", "").toUpperCase();
			if (name.substring(0, 3).equals("VIZ")) {
				int id = Integer.parseInt(name.substring(3));
				result = find(id, list, 0, list.length);
			}
		}

		return result;
	}

	// return the index of the desired element
	public static int find(int id, VizierEntry[] list)
	{
		return find(id, list, 0, list.length);
	}

	public static int find(int id, VizierEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = Integer.compare(list[middle].id, id);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(id, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(id, list, lower, middle);
		}
		
		return result;
	}

	public int compareTo(int rhs) 
	{
		return Integer.compare(id, rhs);
	}

	@Override public int compareTo(VizierEntry rhs) 
	{
		return compareTo(rhs.id);
	}
}
