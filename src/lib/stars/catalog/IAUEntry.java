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
import java.io.UnsupportedEncodingException;

import lib.sphere.Angle;
import nightskyataglance.NightSkyAtAGlance;

public class IAUEntry implements Comparable<IAUEntry> {
	
	public final String rec;			// complete record
	public final String name;			// name in ascii
	public final String diacritic;		// name in UTF-8
	public final String designation;	// star identifier (i.e., harvard, XO, or other)
	public final int    hr;				// yale bright star catalog
	public final String id_4;			// bayer name
	public final int    flamsteed;		// flamsteed number
	public final String id_5;			// not sure...
	public final String constellation;	// three letter constellation ID
	public final String wds;			// washington double star catalog component id
	public final String multiple;		
	public final String magnitude;		// visual magnitude
	public final double mag;			// visual magnitude
	public final String band;
	public final String hipparcos;
	public final String hd;
	public final String ra;
	public final double ra_deg;
	public final double ra_hrs;
	public final double ra_rad;
	public final String dec;
	public final double dec_deg;
	public final double dec_rad;
	public final String date;

	public IAUEntry(byte[] buf, int first, int len) throws UnsupportedEncodingException 
	{
		rec = new String(buf, first, len);

		int nam_start = first;
		int nam_len   = 17;
//		System.out.println("071: start="+nam_start+" len="+nam_len+" name='"+new String(buf, nam_start, nam_len, "UTF-8")+"'");
		name      = new String(buf, nam_start, nam_len, "UTF-8").trim();
//		System.out.println("name='"+name+"'");

		int dia_start = nam_start + nam_len + 1;
		int dia_end   = dia_start + 17;
		for (; buf[dia_end] != ' '; dia_end++) {
//			System.out.println("078: end="+dia_end+" buf["+(dia_end)+"]='"+((char)buf[dia_end])+"'");
		}
//		System.out.println("080: end="+dia_end+" buf["+(dia_end)+"]='"+((char)buf[dia_end])+"'");
		
		int dia_len   = dia_end - dia_start;
		diacritic   = new String(buf, dia_start, dia_len, "UTF-8").trim();
//		System.out.println("diac='"+diacritic+"'");
		
		int des_start = dia_start + dia_len + 1;
		for (; buf[des_start] == ' '; des_start++) {
//			System.out.println("088: start="+des_start+" buf["+(des_start)+"]='"+((char)buf[des_start])+"'");
		}
//		System.out.println("090: start="+des_start+" buf["+(des_start)+"]='"+((char)buf[des_start])+"'");

		int des_len   = 12;
//		System.out.println("093: start="+des_start+" len="+des_len+" desi='"+new String(buf, des_start, des_len, "UTF-8")+"'");
		designation = new String(buf, des_start, des_len).trim();
		hr = designation.startsWith("HR ") ? Integer.parseInt(designation.substring(2).trim()) : 0 ;
//		System.out.println("desi="+designation+" ra="+ra);

		int id4_start = des_start + des_len + 1;
		int id4_len   = 5;
//		System.out.println("i: start="+id4_start+" len="+id4_len+" id_4='"+new String(buf, id4_start, id4_len, "UTF-8")+"'");
		String id4 = new String(buf, id4_start, id4_len).trim();
		if (id4.matches("[0-9]*")) {
			id_4 = "";
			flamsteed = Integer.parseInt(id4);
		} else {
			id_4 = (id4.equals("_")) ? "" : id4;
			flamsteed = 0;
		}
//		System.out.println("id_4='"+id_4+"' flamsteed="+flamsteed);

		int id5_start = id4_start + id4_len;
		for (; buf[id5_start] == ' '; id5_start++) {
//			System.out.println("j: start="+id5_start+" buf["+(id5_start)+"]='"+((char)buf[id5_start])+"'");
		}
//		System.out.println("k: start="+id5_start+" buf["+(id5_start)+"]='"+((char)buf[id5_start])+"'");

		int id5_end   = id5_start;
		for (; buf[id5_end] != ' '; id5_end++) {
//			System.out.println("-: end="+id5_end+" buf["+(id5_end)+"]='"+((char)buf[id5_end])+"'");
		}
		for (; buf[id5_end] == ' '; id5_end++) {
//			System.out.println("l: end="+id5_end+" buf["+(id5_end)+"]='"+((char)buf[id5_end])+"'");
		}
//		System.out.println("m: end="+id5_end+" buf["+(id5_end)+"]='"+((char)buf[id5_end])+"'");

		int id5_len   = id5_end - id5_start;
//		System.out.println("n: start="+id5_start+" len="+id5_len+" id_5='"+new String(buf, id5_start, id5_len, "UTF-8")+"'");
		String id5 = new String(buf, id5_start, id5_len, "UTF-8").trim();
		id_5 = (id5.equals("_")) ? "" : id5;
//		System.out.println("id_5='"+id_5+"'");

		int con_start = id5_end;
		for (; buf[con_start] == ' '; con_start++) {
//			System.out.println("143: sta="+con_start+" buf["+(con_start)+"]='"+((char)buf[con_start])+"'");
		}
//		System.out.println("145: sta="+con_start+" buf["+(con_start)+"]='"+((char)buf[con_start])+"'");
		int con_len   = 3;
//		System.out.println("q: start="+con_start+" len="+con_len+" cons='"+new String(buf, con_start, con_len, "UTF-8")+"'");
		String con = new String(buf, con_start, con_len, "UTF-8").trim();
		constellation = (con.equals("_")) ? "" : con;
//		System.out.println("cons='"+cons_list+"'");

//		System.out.println(new String(buf, con_start, len-(con_start-first)));

		int wds_start = con_start + con_len + 1;
		int wds_len   = 4;
//		System.out.println("t: start="+wds_start+" len="+wds_len+" wds ='"+new String(buf, wds_start, wds_len, "UTF-8")+"'");
		String wds = new String(buf, wds_start, wds_len, "UTF-8").trim();
		this.wds = (wds.equals("_")) ? "" : wds;
//		System.out.println("wds ='"+this.wds+"'");

		int mul_start = wds_start + wds_len + 1;
		int mul_len   = 10;
//		System.out.println("w: start="+mul_start+" len="+mul_len+" mult='"+new String(buf, mul_start, mul_len, "UTF-8")+"'");
		String mul = new String(buf, mul_start, mul_len, "UTF-8").trim();
		multiple = (mul.equals("_")) ? "" : mul;
//		System.out.println("mult='"+multiple+"'");

		int mag_start = mul_start + mul_len + 1;
		int mag_len   = 5;
//		System.out.println("161: start="+mag_start+" len="+mag_len+" magn='"+new String(buf, mag_start, mag_len, "UTF-8")+"'");
		String mag = new String(buf, mag_start, mag_len, "UTF-8").trim();
		magnitude = (mag.equals("_")) ? "" : mag;
//		System.out.println("magn='"+magnitude+"'");
		this.mag = (mag.equals("_")) ? Double.POSITIVE_INFINITY : Double.parseDouble(magnitude);

		int bnd_start = mag_start + mag_len + 1;
		int bnd_len   = 2;
//		System.out.println("169: start="+bnd_start+" len="+bnd_len+" band='"+new String(buf, bnd_start, bnd_len, "UTF-8")+"'");
		String bnd = new String(buf, bnd_start, bnd_len, "UTF-8").trim();
		band = (bnd.equals("_")) ? "" : bnd;
//		System.out.println("band='"+band+"'");

		int hip_start = bnd_start + bnd_len + 1;
		int hip_len   = 6;
//		System.out.println("226: start="+hip_start+" len="+hip_len+" hipp='"+new String(buf, hip_start, hip_len, "UTF-8")+"'");
		String hip = new String(buf, hip_start, hip_len, "UTF-8").trim();
		hipparcos = (hip.equals("_")) ? "" : hip;
//		System.out.println("hipp='"+hipparcos+"'");

		int hd_start = hip_start + hip_len +1;
		int hd_len   = 6;
//		System.out.println("235: start="+hd_start+" len="+hd_len+" hd  ='"+new String(buf, hd_start, hd_len, "UTF-8")+"'");
		String hd = new String(buf, hd_start, hd_len, "UTF-8").trim();
		this.hd = (hd.equals("_")) ? "" : hd;
//		System.out.println("hd  ='"+hd+"'");

		int ra_start = hd_start + hd_len + 1;
		int ra_len   = 10;
//		System.out.println("245: start="+ra_start+" len="+ra_len+" ra  ='"+new String(buf, ra_start, ra_len, "UTF-8")+"'");
		String ra = new String(buf, ra_start, ra_len, "UTF-8").trim();
		this.ra = (ra.equals("_")) ? "" : ra;
//		System.out.println("ra  ='"+this.ra+"'");
		ra_deg = Double.parseDouble(this.ra);
		ra_hrs = Angle.deg_to_hrs(ra_deg);
		ra_rad = Angle.deg_to_rad(ra_deg);

		int dec_start = ra_start + ra_len + 1;
		int dec_len   = 10;
//		System.out.println("258: start="+dec_start+" len="+dec_len+" dec ='"+new String(buf, dec_start, dec_len, "UTF-8")+"'");
		String dec = new String(buf, dec_start, dec_len, "UTF-8").trim();
		this.dec = (dec.equals("_")) ? "" : dec;
//		System.out.println("dec ='"+this.dec+"'");
		dec_deg = Double.parseDouble(dec);
		dec_rad = Angle.deg_to_rad(dec_deg);

		int dat_start = dec_start + dec_len + 1;
		int dat_len   = 10;
//		System.out.println("270: start="+dat_start+" len="+dat_len+" date='"+new String(buf, dat_start, dat_len, "UTF-8")+"'");
		String dat = new String(buf, dat_start, dat_len, "UTF-8").trim();
		this.date = (dec.equals("_")) ? "" : dat;
//		System.out.println("date='"+this.date+"'");
		
//		System.out.println(toString());

//		if (++count == 8) System.exit(0);
	}
	
	public String get_bayer()
	{
		String result = null;


		return result;
	}
	
	public String name()
	{
		return name + " (" + (id_4.equals("") ? (flamsteed < 1 ? "-" : String.format("%d", flamsteed)) : id_4) + " " + constellation + ")";
	}
	
	
	@Override public String toString()
	{
		String str = String.format("[name=%s,designation=%s,%s%sconstellation=%s,%s%smag=%.2f,%s%s%sRA=%s(%02d:%02d:%05.2f),Dec=%s(%s%02d:%02d:%05.2f),date=%s]", 
				name, 
//				diacritic, 
				designation,
				(id_4.equals("")) ? "" : "ID="+id_4+",",
				(0 < flamsteed) ? String.format("flamsteed=%d,", flamsteed) : "",
//				id_5,
				constellation, 
				(wds.equals("")) ? "" : "WDS="+wds+",",
				(multiple == null || multiple.equals("")) ? "" : "binary="+multiple+",",
				mag, 
				(band.equals("")) ? "" : "band="+band+",",
				(hipparcos.equals("")) ? "" : "hipparcos="+hipparcos+",",
				(hd.equals("")) ? "" : "HD="+hd+",",
				ra, (int)ra_hrs, (int)(60*(ra_hrs-(int)ra_hrs)), 60*(60*ra_hrs - (int)(60*ra_hrs)),
				dec, (dec_deg < 0) ? "-" : "+", (int)Math.abs(dec_deg), (int)(60*(Math.abs(dec_deg)-(int)Math.abs(dec_deg))), 60*(60*Math.abs(dec_deg) - (int)(60*Math.abs(dec_deg))),
				date);

		return str;
	}

	@Override public int compareTo(IAUEntry rhs) 
	{
		return name.compareToIgnoreCase(rhs.name);
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find_idx_by_name(String name, IAUEntry[] list)
	{
		return find_idx_by_name(name, list, 0, list.length);
	}

	private static int find_idx_by_name(String name, IAUEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = list[middle].name.compareToIgnoreCase(name);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find_idx_by_name(name, list, middle, upper);
		} else {
			// name < list[middle]
			result = find_idx_by_name(name, list, lower, middle);
		}
		
		return result;
	}

	public static IAUEntry find_elt_by_name(String name, IAUEntry[] list)
	{
		int idx = find_idx_by_name(name, list);
		if (0 <= idx && idx < list.length) {
			return list[idx];
		}
		
		return null;
	}

	public static IAUEntry find_elt_by_hr(int hr, IAUEntry[] list)
	{
		for (IAUEntry elt: list) {
			if (hr == elt.hr) {
				return elt;
			}
		}
		
		return null;
	}

	public static IAUEntry find_elt_by_hd(int hd, IAUEntry[] list)
	{
		String HD = String.format("%d", hd);
		for (IAUEntry elt: list) {
			if (HD.equals(elt.hd)) {
				return elt;
			}
		}
		
		return null;
	}

	public static IAUEntry find_elt_by_hd(String HD, IAUEntry[] list)
	{
		for (IAUEntry elt: list) {
			if (HD.equals(elt.hd)) {
				return elt;
			}
		}
		
		return null;
	}

	public static IAUEntry find_elt_by_designation(String designation, IAUEntry[] list)
	{
		for (IAUEntry elt: list) {
			if (designation.equalsIgnoreCase(elt.designation)) {
				return elt;
			}
		}
		
		return null;
	}

	public static IAUEntry find_elt_by_bayer(String bayer, IAUEntry[] list)
	{
		if (bayer != null && ! bayer.equals("")) {
			bayer = bayer.replaceAll("[ -]", "").toLowerCase();
			String[] tokens = Bayer.parse(bayer);
			if (tokens != null && 1 < tokens.length) {
				if (bayer.matches("[Hh][Rr][0-9][0-9]*")) {										// HR ddd...
					System.out.printf("%s: %4d:%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
					int HR = Integer.parseInt(tokens[1]);
					return find_elt_by_hr(HR, list);
				} else if (bayer.matches("[Hh][Dd][0-9][0-9]*")) {								// HD ddd...
					System.out.printf("%s: %4d:%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
					String HD = tokens[1];
					return find_elt_by_hd(HD, list);
				} else if (bayer.matches("[0-9][0-9]*[a-zA-Z][a-zA-Z]*")) {						// 80 Peg or 80 Pegasus or 80 Pegasi
					System.out.printf("%s: %4d: cons='%s' flam='%s' %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), tokens[0], tokens[1]);
					String constellation = tokens[0];
					int flamsteed = Integer.parseInt(tokens[1]);
					for (IAUEntry elt: list) {
						if (constellation != null && constellation.equalsIgnoreCase(elt.constellation)) {
							System.out.printf("%s: %4d: cons='%s' elt.cons='%s' flam='%s' elt.flam='%d' %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
									tokens[0], elt.constellation, tokens[1], elt.flamsteed);
							if (constellation != null && constellation.equalsIgnoreCase(elt.constellation) && flamsteed == elt.flamsteed) {
							return elt;
							}
						}
					}
				} else if (bayer.matches("[a-zA-Z][a-zA-Z.]*")) {								// Alf Peg or Alpha Peg or Alpha Pegasus or Alpha Pegasi
					System.out.printf("%s: %4d:%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
					String constellation = tokens[0];
					String alpha         = tokens[1];
					for (IAUEntry elt: list) {
						if (constellation != null && constellation.equalsIgnoreCase(elt.constellation) && alpha != null && alpha.equalsIgnoreCase(elt.id_4)) {
							return elt;
						}
					}
				} else if (bayer.matches("[a-zA-Z][a-zA-Z.]*[0-9][0-9]*[a-zA-Z][a-zA-Z]*")) {	// Pi 3 Ori or ...
					System.out.printf("%s: %4d:%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
					String constellation = tokens[0];
					String alpha         = tokens[1] + String.format("%02d", Integer.parseInt(tokens[2]));
					for (IAUEntry elt: list) {
						if (constellation.equalsIgnoreCase(elt.constellation) && alpha.equalsIgnoreCase(elt.id_4)) {
							return elt;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public static String get_short_greek(String bayer)
	{
		String result = bayer;

		for (String[] e: abbr) {
			for (String s: e) {
				if (bayer.equalsIgnoreCase(s)) {
					return e[0];
				}
			}
		}

		return result;
	}

	private static final String[][] abbr = {
			{ "alf",	"alpha",	"Alpha",	},
			{ "bet",	"beta",		"Beta",		},
			{ "gam",	"gamma",	"Gamma",	},
			{ "del",	"delta",	"Delta",	},
			{ "eps",	"epsilon",	"Epsilon",	},
			{ "zet",	"zeta",		"Zeta",		},
			{ "eta",	"eta",		"Eta",		},
			{ "tet",	"theta",	"Theta",	},
			{ "iot",	"iota",		"Iota",		},
			{ "kap",	"kappa",	"Kappa",	},
			{ "lam",	"lambda",	"Lambda",	},
			{ "mu.",	"mu",		"Mu",		},
			{ "nu.",	"nu",		"Nu",		},
			{ "ksi",	"xi",		"Xi",		},
			{ "omi",	"omicron",	"Omicron",	},
			{ "pi.",	"pi",		"Pi",		},
			{ "rho",	"rho",		"Rho",		},
			{ "sig",	"sigma",	"Sigma",	},
			{ "tau",	"tau",		"Tau",		},
			{ "ups",	"upsilon",	"Upsilon",	},
			{ "phi",	"phi",		"Phi",		},
			{ "khi",	"chi",		"Chi",		},
			{ "psi",	"psi",		"Psi",		},
			{ "ome",	"omega",	"Omega",	},
	};
	
	public static void main(String[] args) throws IOException
	{
		String iau_csn_name = "IAU-CSN.txt";
		String path = "D:/home/projects/org.hypercomputing/data/nightsky/catalogs/";
		IAUCatalog iau_csn = new IAUCatalog(path + iau_csn_name);
		
		// Angetenar
		IAUEntry elt = find_elt_by_bayer("tau02 Eri", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("HR 850", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("HD 17824", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("eta Dra", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_name("Angetenar", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_hr(850, iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_hd(17824, iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("90 Psc", iau_csn.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("13 Cas", iau_csn.elts);
		System.out.println(elt);
	}
}
