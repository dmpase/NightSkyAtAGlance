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


public class Bayer implements Comparable<Bayer> {

	public final String constellation;	// original constellation
	public final String bayer;			// original bayer designation
	public final String flamsteed;		// original flamsteed number

	public final String cst_tld;		// constellation three-letter abbreviation
	public final int    cst_id;			// constellation index
	public final String bay0;			// Greek or Latin letter
	public final int    bay0_id;		// index of bay0
	public final int    bay1;			// superscript number
	public final int    fl;				// flamsteed number

	public Bayer(String cons, String bay, String flam) 
	{
		constellation = (cons == null || cons.trim().equals("")) ? null : cons.trim();
		bayer         = (bay  == null || bay .trim().equals("")) ? null : bay .trim();
		flamsteed     = (flam == null || flam.trim().equals("")) ? null : flam.trim();

		cst_id = find_cst_id(constellation);
		if (cst_id < 0 || cons_list.length <= cst_id) {
			cst_tld = null;
		} else {
			cst_tld = cons_list[cst_id][2];
		}

		if (bayer != null && ! bayer.equals("")) {
			String alpha = null;
			int sub = 0;
			int idx;
			boolean done = false;
			for (idx=0; ! done && idx < bayer_list.length; idx++) {
				for (int j=0; ! done && j < bayer_list[idx].length; j++) {
					if (bayer.equals(bayer_list[idx][j])) {
						alpha = bayer_list[idx][0];
						done = true;
					} else if (bayer.matches(bayer_list[idx][j]+"0[0-9]")) {
						alpha = bayer_list[idx][0];
						String sub_str = bayer.substring(bayer.length() - 1);
						sub = Integer.parseInt(sub_str);
						done = true;
					} else if (bayer.matches(bayer_list[idx][j]+"[1-9][0-9]")) {
						alpha = bayer_list[idx][0];
						String sub_str = bayer.substring(bayer.length() - 2);
						sub = Integer.parseInt(sub_str);
						done = true;
					} else if (bayer.matches(bayer_list[idx][j]+"[0-9]")) {
						alpha = bayer_list[idx][0];
						String sub_str = bayer.substring(bayer.length() - 1);
						sub = Integer.parseInt(sub_str);
						done = true;
					}
				}
			}
			bay0    = alpha;
			bay0_id = done ? idx - 1 : Integer.MAX_VALUE;
			bay1    = sub;
		} else {
			bay0    = null;
			bay0_id = Integer.MAX_VALUE;
			bay1    = 0;
		}

		fl = (flamsteed == null || flamsteed.equals("") || ! flamsteed.matches("[0-9][0-9]*")) ? 0 : Integer.parseInt(flamsteed);
	}

	public Bayer(String cons, int flam) 
	{
		constellation = (cons == null || cons.trim().equals("")) ? null : cons.trim();
		bayer         = null;
		flamsteed     = String.format("%d", flam);

		cst_id = find_cst_id(constellation);
		if (cst_id < 0 || cons_list.length <= cst_id) {
			cst_tld = null;
		} else {
			cst_tld = cons_list[cst_id][2];
		}

		bay0    = null;
		bay0_id = Integer.MAX_VALUE;
		bay1    = 0;

		fl = flam;
	}

	@Override public int compareTo(Bayer rhs) 
	{
		// not equal if they don't have the same constellation
		if (cst_id != rhs.cst_id) {
			// constellations don't match
			return cst_id - rhs.cst_id;
		} else if (cst_id == Integer.MAX_VALUE && rhs.cst_id == Integer.MAX_VALUE) {
			// both constellations are bogus, but do they match?
			if (constellation == null && rhs.constellation == null) {
				// constellations match, check bayer or flamsteed
			} else if (constellation == null) {
				// constellation values don't match
				return -1;
			} else if (rhs.constellation == null) {
				// constellation values don't match
				return +1;
			} else {
				int cst = constellation.compareToIgnoreCase(rhs.constellation);
				if (cst != 0) {
					// constellations don't match
					return cst;
				}
			}
		}

		// constellations match, check the bayer designation
		if (bay0 != null && rhs.bay0 != null) {
			// both stars have standard bayer designations
			if (bay0_id != rhs.bay0_id) {
				return bay0_id - rhs.bay0_id;
			}
			return bay1 - rhs.bay1;
		} else if (bayer != null && rhs.bayer != null) {
			// bayer designation is valid but non-standard
			return bayer.compareTo(rhs.bayer);
		}

		// one or both bayer designations isn't valid, check flamsteed
		return fl - rhs.fl;
	}

	@Override public String toString()
	{
		String str = "";
		
		if (0 < fl) {
			str = String.format("%02d", fl);
		} else {
			str = String.format("%2s", "");
		}

		if (0 <= bay0_id && bay0_id < bayer_list.length) {
			str = str + " " + String.format("%-3s", bayer_list[bay0_id][0]);
			if (0 < bay1) {
				str = str + " " + String.format("%02d", bay1);
			} else {
				str = str + " " + String.format("%2s", "");
			}
		} else if (bayer != null) {
			str = str + " " + String.format("%-3s %2s", bayer, "");
		} else {
			str = str + " " + String.format("%-3s %2s", "", "");
		}

		if (0 <= cst_id && cst_id < cons_list.length) {
			if (str.equals("")) {
				str = cons_list[cst_id][2];
			} else {
				str = str + " " + String.format("%s", cons_list[cst_id][2]);
			}
		}
		
		return str;
	}

	// find the constellation id (or index)
	public static int find_cst_id(String name)
	{
		int result = Integer.MAX_VALUE;
		
		if (name != null && ! name.equals("")) {
			for (int i=0; i < cons_list.length; i++) {
				for (int j=0; j < cons_list[i].length; j++) {
					if (name.equalsIgnoreCase(cons_list[i][j])) {
						return i;
					}
				}
			}
		}
		
		return result;
	}
	
	public static final String get_cons_tld(String name)
	{
		String result = null;

		for (String[] e: cons_list) {
			for (String s: e) {
				if (name.equalsIgnoreCase(s)) {
					return e[2];
				}
			}
		}
		
		return result;
	}
	
	public static int get_bayer_idx(String name)
	{
		int result = Integer.MAX_VALUE;
		
		if (name != null && ! name.equals("")) {
			for (int i=0; i < bayer_list.length; i++) {
				for (int j=0; j < bayer_list[i].length; j++) {
					if (name.equalsIgnoreCase(bayer_list[i][j])) {
						return i;
					}
				}
			}
		}
		
		return result;
	}
	
	public static String get_bayer_tld(String name)
	{
		for (String[] e: bayer_list) {
			for (String s: e) {
				if (s.equalsIgnoreCase(name)) return e[0];
			}
		}

		return name;
	}
	
	public static String[] parse(String name)
	{
		String[] result = null;

		if (name != null && ! name.equals("")) {
			String lc = name.toLowerCase().replaceAll("[ -]", "");
			if (lc.matches("h[rd][0-9][0-9]*")) {								// HR ddd... or HD ddd...
				result = new String[2];
				result[0] = lc.substring(0, 2).toUpperCase();
				result[1] = lc.substring(2).replaceAll("^0*", "");
			} else if (lc.matches("[0-9][0-9]*[a-z][a-z]*")) {					// 80 Peg or 80 Pegasus or 80 Pegasi
				result = new String[2];
				String cons = null;
				for (int i=0; i < lc.length(); i++) {
					if (lc.charAt(i) < '0' || '9' < lc.charAt(i)) {
						result[1] = lc.substring(0, i);
						cons = lc.substring(i);
						break;
					}
				}

				for (String[] list: cons_list) {
					for (String c: list) {
						if (cons.equals(c.toLowerCase())) {
							result[0] = list[2];
							break;
						}
					}
				}
			} else if (lc.matches("[a-z][a-z.]*")) {							// Alf Peg or Alpha Peg or Alpha Pegasus or Alpha Pegasi
				result = new String[2];
				String cons = null;
				for (String[] list: bayer_list) {
					boolean found = false;
					for (int i=list.length-1; 0 <= i; i--) {
						String elt = list[i].toLowerCase();
						if (lc.startsWith(elt)) {
							result[1] = list[0];
							cons = lc.substring(elt.length());
							found = true;
							break;
						}
					}
					if (found) break;
				}

				for (String[] list: cons_list) {
					for (String c: list) {
						if (cons.equals(c.toLowerCase())) {
							result[0] = list[2];
							break;
						}
					}
				}
			} else if (lc.matches("[a-z][a-z.]*[0-9][0-9]*[a-z][a-z]*")) {		// Pi 3 Ori or ...
				result = new String[3];
				String cons = null;
				String sub  = null;
				for (String[] list: bayer_list) {
					boolean found = false;
					for (int i=list.length-1; 0 <= i; i--) {
						String elt = list[i].toLowerCase();
						if (lc.startsWith(elt)) {
							result[1] = list[0];
							sub = lc.substring(elt.length());
							found = true;
							break;
						}
					}
					if (found) break;
				}

				for (int i=0; i < lc.length(); i++) {
					if (sub.charAt(i) < '0' || '9' < sub.charAt(i)) {
						result[2] = sub.substring(0, i).replaceAll("^0*", "");
						cons = sub.substring(i);
						break;
					}
				}

				for (String[] list: cons_list) {
					for (String c: list) {
						if (cons.equals(c.toLowerCase())) {
							result[0] = list[2];
							break;
						}
					}
				}
			}
		}

		return result;
	}


	public static String canonical(String name)
	{
		String result = null;

		String[] tokens = parse(name);
		if (tokens != null && tokens[0].matches("[Hh][DdRr]")) {
			result = tokens[0].toUpperCase() + " " + tokens[1].replaceAll("^0*", "");
		} else if (tokens != null && tokens.length == 2) {
			result = tokens[1] + " " + tokens[0];
		} else if (tokens != null && tokens.length == 3) {
			result = tokens[1] + tokens[2] + " " + tokens[0];
		}
	
		return result;
	}


	public static final String[][] cons_list = {
		{	"Andromeda",			"Andromedae",			"And",	"Andr",	},
		{	"Antlia",				"Antliae",				"Ant",	"Antl",	},
		{	"Apus",					"Apodis",				"Aps",	"Apus",	},
		{	"Aquarius",				"Aquarii",				"Aqr",	"Aqar",	},
		{	"Aquila",				"Aquilae",				"Aql",	"Aqil",	},
		{	"Ara",					"Arae",					"Ara",	"Arae",	},
		{	"Aries",				"Arietis",				"Ari",	"Arie",	},
		{	"Auriga",				"Aurigae",				"Aur",	"Auri",	},
		{	"Bootes",				"Bootis",				"Boo",	"Boot",		"Boötes",	"Boötis",	"Boö",	"Boöt",	},
		{	"Caelum",				"Caeli",				"Cae",	"Cael",	},
		{	"Camelopardalis",		"Camelopardalis",		"Cam",	"Caml",	},
		{	"Cancer",				"Cancri",				"Cnc",	"Canc",	},
		{	"Canes Venatici",		"Canum Venaticorum",	"CVn",	"CVen",	},
		{	"Canis Major",			"Canis Majoris",		"CMa",	"CMaj",	},
		{	"Canis Minor",			"Canis Minoris",		"CMi",	"CMin",	},
		{	"Capricornus",			"Capricorni",			"Cap",	"Capr",	},
		{	"Carina",				"Carinae",				"Car",	"Cari",	},
		{	"Cassiopeia",			"Cassiopeiae",			"Cas",	"Cass",	},
		{	"Centaurus",			"Centauri",				"Cen",	"Cent",	},
		{	"Cepheus",				"Cephei",				"Cep",	"Ceph",	},
		{	"Cetus",				"Ceti",					"Cet",	"Ceti",	},
		{	"Chamaeleon",			"Chamaeleontis",		"Cha",	"Cham",	},
		{	"Circinus",				"Circini",				"Cir",	"Circ",	},
		{	"Columba",				"Columbae",				"Col",	"Colm",	},
		{	"Coma Berenices",		"Comae Berenices",		"Com",	"Coma",	},
		{	"Corona Australis",		"Coronae Australis",	"CrA",	"CorA",	},
		{	"Corona Borealis",		"Coronae Borealis",		"CrB",	"CorB",	},
		{	"Corvus",				"Corvi",				"Crv",	"Corv",	},
		{	"Crater",				"Crateris",				"Crt",	"Crat",	},
		{	"Crux",					"Crucis",				"Cru",	"Crux",	},
		{	"Cygnus",				"Cygni",				"Cyg",	"Cygn",	},
		{	"Delphinus",			"Delphini",				"Del",	"Dlph",	},
		{	"Dorado",				"Doradus",				"Dor",	"Dora",	},
		{	"Draco",				"Draconis",				"Dra",	"Drac",	},
		{	"Equuleus",				"Equulei",				"Equ",	"Equl",	},
		{	"Eridanus",				"Eridani",				"Eri",	"Erid",	},
		{	"Fornax",				"Fornacis",				"For",	"Forn",	},
		{	"Gemini",				"Geminorum",			"Gem",	"Gemi",	},
		{	"Grus",					"Gruis",				"Gru",	"Grus",	},
		{	"Hercules",				"Herculis",				"Her",	"Herc",	},
		{	"Horologium",			"Horologii",			"Hor",	"Horo",	},
		{	"Hydra",				"Hydrae",				"Hya",	"Hyda",	},
		{	"Hydrus",				"Hydri",				"Hyi",	"Hydi",	},
		{	"Indus",				"Indi",					"Ind",	"Indi",	},
		{	"Lacerta",				"Lacertae",				"Lac",	"Lacr",	},
		{	"Leo",					"Leonis",				"Leo",	"Leon",	},
		{	"Leo Minor",			"Leonis Minoris",		"LMi",	"LMin",	},
		{	"Lepus",				"Leporis",				"Lep",	"Leps",	},
		{	"Libra",				"Librae",				"Lib",	"Libr",	},
		{	"Lupus",				"Lupi",					"Lup",	"Lupi",	},
		{	"Lynx",					"Lyncis",				"Lyn",	"Lync",	},
		{	"Lyra",					"Lyrae",				"Lyr",	"Lyra",	},
		{	"Mensa",				"Mensae",				"Men",	"Mens",	},
		{	"Microscopium",			"Microscopii",			"Mic",	"Micr",	},
		{	"Monoceros",			"Monocerotis",			"Mon",	"Mono",	},
		{	"Musca",				"Muscae",				"Mus",	"Musc",	},
		{	"Norma",				"Normae",				"Nor",	"Norm",	},
		{	"Octans",				"Octantis",				"Oct",	"Octn",	},
		{	"Ophiuchus",			"Ophiuchi",				"Oph",	"Ophi",	},
		{	"Orion",				"Orionis",				"Ori",	"Orio",	},
		{	"Pavo",					"Pavonis",				"Pav",	"Pavo",	},
		{	"Pegasus",				"Pegasi",				"Peg",	"Pegs",	},
		{	"Perseus",				"Persei",				"Per",	"Pers",	},
		{	"Phoenix",				"Phoenicis",			"Phe",	"Phoe",	},
		{	"Pictor",				"Pictoris",				"Pic",	"Pict",	},
		{	"Pisces",				"Piscium",				"Psc",	"Pisc",	},
		{	"Piscis Austrinus",		"Piscis Austrini",		"PsA",	"PscA",	},
		{	"Puppis",				"Puppis",				"Pup",	"Pupp",	},
		{	"Pyxis",				"Pyxidis",				"Pyx",	"Pyxi",	},
		{	"Reticulum",			"Reticuli",				"Ret",	"Reti",	},
		{	"Sagitta",				"Sagittae",				"Sge",	"Sgte",	},
		{	"Sagittarius",			"Sagittarii",			"Sgr",	"Sgtr",	},
		{	"Scorpius",				"Scorpii",				"Sco",	"Scor",	},
		{	"Sculptor",				"Sculptoris",			"Scl",	"Scul",	},
		{	"Scutum",				"Scuti",				"Sct",	"Scut",	},
		{	"Serpens",				"Serpentis",			"Ser",	"Serp",		"Serpens Caput",	"Serpens Cauda",	},
		{	"Sextans",				"Sextantis",			"Sex",	"Sext",	},
		{	"Taurus",				"Tauri",				"Tau",	"Taur",	},
		{	"Telescopium",			"Telescopii",			"Tel",	"Tele",	},
		{	"Triangulum",			"Trianguli",			"Tri",	"Tria",	},
		{	"Triangulum Australe",	"Trianguli Australis",	"TrA",	"TrAu",	},
		{	"Tucana",				"Tucanae",				"Tuc",	"Tucn",	},
		{	"Ursa Major",			"Ursae Majoris",		"UMa",	"UMaj",	},
		{	"Ursa Minor",			"Ursae Minoris",		"UMi",	"UMin",	},
		{	"Vela",					"Velorum",				"Vel",	"Velr",	},
		{	"Virgo",				"Virginis",				"Vir",	"Virg",	},
		{	"Volans",				"Volantis",				"Vol",	"Voln",	},
		{	"Vulpecula",			"Vulpeculae",			"Vul",	"Vulp"	},
	};

	public static final String[][] bayer_list = {
		{ "Alp",	"alpha",	"Alpha",	"alf", },
		{ "Bet",	"beta",		"Beta",		},
		{ "Gam",	"gamma",	"Gamma",	},
		{ "Del",	"delta",	"Delta",	},
		{ "Eps",	"epsilon",	"Epsilon",	},
		{ "Zet",	"zeta",		"Zeta",		},
		{ "Eta",	"eta",		"Eta",		},
		{ "The",	"theta",	"Theta",	"tet",	},
		{ "Iot",	"iota",		"Iota",		},
		{ "Kap",	"kappa",	"Kappa",	},
		{ "Lam",	"lambda",	"Lambda",	},
		{ "Mu",		"mu",		"Mu",		"mu.", },
		{ "Nu",		"nu",		"Nu",		"nu.", },
		{ "Ksi",	"xi",		"Xi",		},
		{ "Omi",	"omicron",	"Omicron",	},
		{ "Pi",		"pi",		"Pi",		"pi.", },
		{ "Rho",	"rho",		"Rho",		},
		{ "Sig",	"sigma",	"Sigma",	},
		{ "Tau",	"tau",		"Tau",		},
		{ "Ups",	"upsilon",	"Upsilon",	},
		{ "Phi",	"phi",		"Phi",		},
		{ "Khi",	"chi",		"Chi",		},
		{ "Psi",	"psi",		"Psi",		},
		{ "Ome",	"omega",	"Omega",	},
		{ "a",	},
		{ "b",	},
		{ "c",	},
		{ "d",	},
		{ "e",	},
		{ "f",	},
		{ "g",	},
		{ "h",	},
		{ "i",	},
		{ "j",	},
		{ "k",	},
		{ "l",	},
		{ "m",	},
		{ "n",	},
		{ "o",	},
		{ "p",	},
		{ "q",	},
		{ "r",	},
		{ "s",	},
		{ "t",	},
		{ "u",	},
		{ "v",	},
		{ "w",	},
		{ "x",	},
		{ "y",	},
		{ "z",	},
		{ "A",	},
		{ "B",	},
		{ "C",	},
		{ "D",	},
		{ "E",	},
		{ "F",	},
		{ "G",	},
		{ "H",	},
		{ "I",	},
		{ "J",	},
		{ "K",	},
		{ "L",	},
		{ "M",	},
		{ "N",	},
		{ "O",	},
		{ "P",	},
		{ "Q",	},
		{ "R",	},
		{ "S",	},
		{ "T",	},
		{ "U",	},
		{ "V",	},
		{ "W",	},
		{ "X",	},
		{ "Y",	},
		{ "Z",	},
	};
	
	public static void main(String[] args)
	{
		String[] names = {
			" 33|     |Psc",
			" 21|alf  |And",
			"   |kap01|Scl",
			"   |AO   |Cas",
			"   |pi.  |Tuc",
			"   |bet03|Tuc",
			" 17|phi01|Cet",
			"   |     |Ori",
		};

		final int FLAM = 0;
		final int BAYR = 1;
		final int CONS = 2;
		for (String s: names) {
			String[] field = s.split("[|]");
			Bayer b = new Bayer(field[CONS], field[BAYR], field[FLAM]);
			System.out.printf("%s%n", b.toString());
		}

		System.out.println("----------");
		String[] bayer = {
				"80  Peg",
				"Alf And",
				"Pi  Tuc",
				"Pi. Tuc",
				"Pi 3 Tuc",
				"Hr 00997",
				"hD 120971",
				"Advil Bendhuvi",
		};
		for (String s: bayer) {
			System.out.printf("%10s -- ", s);
			String[] p = parse(s);
			for (String e: p) {
				System.out.print(e + " ");
			}
			System.out.println(" -- " + canonical(s));
		}
	}
}
