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


import java.io.UnsupportedEncodingException;

import lib.util.Queue;

public class BayerEntry implements Comparable<BayerEntry> {

	public final String   rec;
	public final String   constellation;	// constellation three letter designation, e.g., 
	public final String   bayer_id;			// bayer designation, e.g.,
	public final String   sub_id;			// bayer sub-ID, e.g., 
	public final String   wds;				// washington double star catalog designation, e.g., 
	public final int      flamsteed;		// greater than zero if bayer_id is a Flamsteed number
	public final int      hr;				// harvard revised index of the yale bright star catalog
	public final double   ra_hrs;
	public final double   ra_min;
	public final double   ra_sec;
	public final double   dec_deg;
	public final double   dec_min;
	public final double   dec_sec;
	public final double   mag;
	public final String   iau;				// official star name accepted by the international astronomers' union
	public final String[] name;				// additional, unofficial names for the star

	private static final int CONS =  0;
	private static final int BAYR =  1;
	private static final int SBID =  2;
	private static final int WDS  =  3;
	private static final int FLAM =  4;
	private static final int HR   =  5;
	private static final int RAH  =  6;
	private static final int RAM  =  7;
	private static final int RAS  =  8;
	private static final int DECD =  9;
	private static final int DECM = 10;
	private static final int DECS = 11;
	private static final int MAG  = 12;
	private static final int IAU  = 13;

	public BayerEntry(byte[] buf, int first, int len) 
	{
		String str = null;
		try {
			str = new String(buf, first, len, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		rec = str;
		String[] field = rec.split("[,]");
//		System.out.printf(" 81: first=%d len=%d fields=%d rec='%s'%n", first, len, field.length, rec);
		
		constellation  = (field.length <= CONS || field[CONS] == null || field[CONS].trim().equals("")) ? "" : find_tld(field[CONS]);
		bayer_id       = (field.length <= BAYR || field[BAYR] == null || field[BAYR].trim().equals("")) ? "" : field[BAYR].trim();
		sub_id         = (field.length <= SBID || field[SBID] == null || field[SBID].trim().equals("")) ? "" : field[SBID].trim();
		wds            = (field.length <= WDS  || field[WDS ] == null || field[WDS ].trim().equals("")) ? "" : field[WDS ].trim();
		flamsteed      = (field.length <= FLAM || field[FLAM] == null || field[FLAM].trim().equals("")) ? 0  : Integer.parseInt(field[FLAM].trim());
		hr             = (field.length <= HR   || field[HR  ] == null || field[HR  ].trim().equals("")) ? 0  : Integer.parseInt(field[HR  ].trim());
		ra_hrs         = (field.length <= RAH  || field[RAH ] == null || field[RAH ].trim().equals("")) ? 0  : Double.parseDouble(field[RAH ].trim());
		ra_min         = (field.length <= RAM  || field[RAM ] == null || field[RAM ].trim().equals("")) ? 0  : Double.parseDouble(field[RAM ].trim());
		ra_sec         = (field.length <= RAS  || field[RAS ] == null || field[RAS ].trim().equals("")) ? 0  : Double.parseDouble(field[RAS ].trim());
		dec_deg        = (field.length <= DECD || field[DECD] == null || field[DECD].trim().equals("")) ? 0  : Double.parseDouble(field[DECD].trim());
		dec_min        = (field.length <= DECM || field[DECM] == null || field[DECM].trim().equals("")) ? 0  : Double.parseDouble(field[DECM].trim());
		dec_sec        = (field.length <= DECS || field[DECS] == null || field[DECS].trim().equals("")) ? 0  : Double.parseDouble(field[DECS].trim());
		mag            = (field.length <= MAG  || field[MAG ] == null || field[MAG ].trim().equals("")) ? 0  : Double.parseDouble(field[MAG ].trim());
		iau            = (field.length <= IAU  || field[IAU ] == null || field[IAU ].trim().equals("")) ? "" : field[IAU ].trim();
		if (IAU < field.length) {
			Queue<String> queue = new Queue<String>();
			for (int i=IAU; i < field.length; i++) {
				if (field[i] != null && ! field[i].equals("")) {
					queue.append(field[i].trim());
				}
			}
			
			if (0 < queue.length()) {
				name = new String[queue.length()];
				for (int i=0; i < name.length; i++) {
					name[i] = queue.remove();
				}
			} else {
				name = null;
			}
		} else {
			name = null;
		}

//		System.out.printf("117: %s%n", toString());
	}

	public static int compare_const(String name0, String name1)
	{
		int result = 0;
		
		return result;
	}

	public static int compare_greek(String name0, String name1)
	{
		int result = 0;
		
		return result;
	}

	public static int compare_wds(String name0, String name1)
	{
		int result = 0;
		
		return result;
	}

	public static int search_const(String name)
	{
		int result = -1;
		
		for (int i=0; i < cons_list.length; i++) {
			for (int j=0; j < cons_list[i].length; j++) {
				if (name.equalsIgnoreCase(cons_list[i][j])) {
					return i;
				}
			}
		}

		return result;
	}

	public static int search_greek(String name)
	{
		int result = -1;
		
		for (int i=0; i < greek.length; i++) {
			for (int j=0; j < greek[i].length; j++) {
				if (name.equalsIgnoreCase(greek[i][j])) {
					return i;
				}
			}
		}

		return result;
	}

	public static int search_wds(String name)
	{
		int result = -1;

		return result;
	}

	public static String find_tld(String name)
	{
		String result = "";
		
		for (int i=0; i < cons_list.length; i++) {
			for (int j=0; j < cons_list[i].length; j++) {
				if (name.equalsIgnoreCase(cons_list[i][j])) {
					return cons_list[i][2];
				}
			}
		}
		
		return result;
	}

	public static String find_name(String name)
	{
		String result = name;
		
		for (int i=0; i < cons_list.length; i++) {
			for (int j=0; j < cons_list[i].length; j++) {
				if (name.equalsIgnoreCase(cons_list[i][j])) {
					return cons_list[i][0];
				}
			}
		}
		
		return result;
	}

	public static String find_genitive(String name)
	{
		String result = name;
		
		for (int i=0; i < cons_list.length; i++) {
			for (int j=0; j < cons_list[i].length; j++) {
				if (name.equalsIgnoreCase(cons_list[i][j])) {
					return cons_list[i][1];
				}
			}
		}
		
		return result;
	}

	public static String find_greek(String name)
	{
		String result = "";
		
		for (int i=0; i < greek.length; i++) {
			for (int j=0; j < greek[i].length; j++) {
				if (name.equalsIgnoreCase(greek[i][j])) {
					return greek[i][0];
				}
			}
		}
		
		return result;
	}
	
	public String toString()
	{
		String result = String.format("[cons=%s%s%s%s%s,ra=%d%s", 
				constellation, 
				(bayer_id != null && ! bayer_id.equals("")) ? ",bayer="+bayer_id : "",
				(sub_id   != null && ! sub_id  .equals("")) ? ",sub="+sub_id     : "",
				(wds      != null && ! wds     .equals("")) ? ",wds="+wds        : "",
				(0 < flamsteed) ? String.format(",flamsteed=%d", flamsteed)      : "",
				hr,
				(iau      != null && ! iau     .equals("")) ? ",iau="+iau        : "");
		
		if (name != null) {
			int start = 0;
			if (name[0] == null || name[0].equals("")) {
				start = 1;
			}
			result += ",name=(" + name[start];
			for (int i=start+1; i < name.length; i++) {
				result += "," + name[i];
			}
			result += ")";
		}
		result += "]";
		
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
		{	"Boötes",				"Boötis",				"Boö",	"Boöt",		"Bootes",				"Bootis",				"Boo",	"Boot",	},
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
		{	"Hydrus",				"Hydri",				"Hys",	"Hydi",	},
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
		{	"Picsces",				"Piscium",				"Psc",	"Pisc",	},
		{	"Piscis Austrinus",		"Piscis Austrini",		"PsA",	"PscA",	},
		{	"Puppis",				"Puppis",				"Pup",	"Pupp",	},
		{	"Pyxis",				"Pyxidis",				"Pyx",	"Pyxi",	},
		{	"Reticulum",			"Reticuli",				"Ret",	"Reti",	},
		{	"Sagitta",				"Sagittae",				"Sge",	"Sgte",	},
		{	"Sagittarius",			"Sagittarii",			"Sgr",	"Sgtr",	},
		{	"Scorpius",				"Scorpii",				"Sco",	"Scor",	},
		{	"Sculptor",				"Sculptoris",			"Scl",	"Scul",	},
		{	"Scutum",				"Scuti",				"Sct",	"Scut",	},
		{	"Serpens Caput",		"Serpentis",			"Ser",	"Serp",	},
		{	"Serpens Cauda",		"Serpentis",			"Ser",	"Serp",	},
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

	// http://simbad.u-strasbg.fr/Pages/guide/chA.htx
	public static final String[][] greek = {
		{	"Alpha",	"alf", "alp", },
		{	"Beta",		"bet", },
		{	"Gamma",	"gam", },
		{	"Delta",	"del", },
		{	"Epsilon",	"eps", },
		{	"Zeta",		"zet", },
		{	"Eta",		"eta", },
		{	"Theta",	"tet", "the", },
		{	"Iota",		"iot", },
		{	"Kappa",	"kap", },
		{	"Lambda",	"lam", },
		{	"Mu",		"mu.", },
		{	"Nu",		"nu.", },
		{	"Xi",		"ksi", },
		{	"Omicron",	"omi", },
		{	"Pi",		"pi.", },
		{	"Rho",		"rho", },
		{	"Sigma",	"sig", },
		{	"Tau",		"tau", },
		{	"Upsilon",	"ups", },
		{	"Phi",		"phi", },
		{	"Chi",		"chi", },
		{	"Psi",		"psi", },
		{	"Omega",	"ome", },
	};

	@Override public int compareTo(BayerEntry o) 
	{
		return 0;
	}
	
	public static void main(String[] args)
	{
		System.out.printf("con='%s' gen='%s'%n", "And", find_genitive("And"));
		System.out.printf("con='%s' gen='%s'%n", "boo", find_genitive("boo"));
		System.out.printf("con='%s' gen='%s'%n", "Psc", find_genitive("Psc"));
	}
}
