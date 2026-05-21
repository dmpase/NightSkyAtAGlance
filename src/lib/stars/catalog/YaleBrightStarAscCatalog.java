package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2025 Douglas M. Pase                                          *
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import lib.util.Queue;

public class YaleBrightStarAscCatalog {
	public static final int BSC_RECORD_LEN = 198;

	public final File file;
	public final YaleBrightStarAscEntry[] elts;

	public YaleBrightStarAscCatalog(String path) throws IOException 
	{
		// check for a valid file name
		if (path == null || path.equals("")) {
			throw new FileNotFoundException();
		}
		
		file = new File(path);

		byte[] buf = new byte[10*1024*1024];
		int len = 0;

		if (file.isFile()) {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			len = raf.read(buf);
			raf.close();
		} else {
			try(InputStream input_stream = getClass().getResourceAsStream(path)) {
			    if (input_stream == null) {
			        throw new FileNotFoundException("File '" + path + "' not found!");
			    }
			    
			    int ch = input_stream.read();
			    for (len=0; len < buf.length && 0 <= ch; len++) {
			    	buf[len] = (byte) ch;
			    	ch = input_stream.read();
			    }

			    input_stream.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}

		// open the file, read the header, allocate space for the entries
		Queue<YaleBrightStarAscEntry> queue = new Queue<YaleBrightStarAscEntry>();
		int fp = 0;
		byte[] rec = new byte[BSC_RECORD_LEN];
		while (0 <= fp && fp < len) {
			int new_fp = read_line(buf, fp, len, rec);
			int rec_len = new_fp - fp;
			if (rec_len < 0) {
				System.out.println("YaleBrightStarAscCatalog: fp="+fp+" rec="+((0 < rec_len) ? new String(rec) : "null"));
				break;
			}

			String prefix = new String(rec, 5, 9);
			boolean rec_not_ok =
					prefix.equalsIgnoreCase("NOVA 1572")  || 
					prefix.equalsIgnoreCase("47    Tuc")  || 
					prefix.equalsIgnoreCase("M 31  And")  || 
					prefix.equalsIgnoreCase("NOVA 1901")  || 
					prefix.equalsIgnoreCase("NOVA 1891")  || 
					prefix.equalsIgnoreCase("NOVA 1903")  || 
					prefix.equalsIgnoreCase(" NGC 2281")  || 
					prefix.equalsIgnoreCase("M 67     ")  || 
					prefix.equalsIgnoreCase("NGC 2808 ")  || 
					prefix.equalsIgnoreCase("NOVA 1848")  || 
					prefix.equalsIgnoreCase("NOVA 1604")  || 
					prefix.equalsIgnoreCase("NOVA 1899")  || 
					prefix.equalsIgnoreCase("NOVA 1670")  || 
					prefix.equalsIgnoreCase("NOVA 1876");
			if (! rec_not_ok) {
				YaleBrightStarAscEntry ent = new YaleBrightStarAscEntry(rec, rec_len);
				queue.append(ent);
				if (ent.DE == 0 && ent.RA == 0) {
					System.out.println("Yale BSC: 107: '" + new String(rec) + "'");
				}
			}
			
			fp = new_fp;
		}
		
		elts = new YaleBrightStarAscEntry[queue.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = queue.remove();
		}

		for (int i=0; i < elts.length; i++) {
			// System.out.println(elts[i]);
		}
	}

	public int get_idx = -1;
	public final YaleBrightStarAscEntry get(String c, String b, String s)
	{
		String bay = (b != null) ? get_bayer_tld(b) : "";
		String sub = (s != null) ? s : "";
		String con = (c != null) ? get_cons_tld(c)  : "";
		
		for (int i=0; i < elts.length; i++) {
			YaleBrightStarAscEntry e = elts[i];
			if (e.match(con, bay, sub)) {
				get_idx = i;
				return e;
			}
		}
		
		return null;
	}

	public final YaleBrightStarAscEntry get(int hr)
	{
		for (int i=0; i < elts.length; i++) {
			YaleBrightStarAscEntry e = elts[i];
			if (hr == e.HR) {
				get_idx = i;
				return e;
			}
		}
		
		return null;
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

	public static final String get_cons_tld(String name)
	{
		for (String[] e: cons_list) {
			for (String s: e) {
				if (s.equalsIgnoreCase(name)) return e[2];
			}
		}

		return name;
	}

	public static final String[][] bayer_list = {
		{ "alp",	"alpha",	"Alpha",	"alf", },
		{ "bet",	"beta",		"Beta",		},
		{ "gam",	"gamma",	"Gamma",	},
		{ "del",	"delta",	"Delta",	},
		{ "eps",	"epsilon",	"Epsilon",	},
		{ "zet",	"zeta",		"Zeta",		},
		{ "eta",	"eta",		"Eta",		},
		{ "the",	"theta",	"Theta",	"tet",	},
		{ "iot",	"iota",		"Iota",		},
		{ "kap",	"kappa",	"Kappa",	},
		{ "lam",	"lambda",	"Lambda",	},
		{ "mu",		"mu",		"Mu",		"mu.", },
		{ "nu",		"nu",		"Nu",		"nu.", },
		{ "xi",		"xi",		"Xi",	    "ksi", },
		{ "omi",	"omicron",	"Omicron",	},
		{ "pi",		"pi",		"Pi",		"pi.", },
		{ "rho",	"rho",		"Rho",		},
		{ "sig",	"sigma",	"Sigma",	},
		{ "tau",	"tau",		"Tau",		},
		{ "ups",	"upsilon",	"Upsilon",	},
		{ "phi",	"phi",		"Phi",		},
		{ "chi",	"chi",		"Chi",		},
		{ "psi",	"psi",		"Psi",		},
		{ "ome",	"omega",	"Omega",	},
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
	
	public static String get_bayer_tld(String name)
	{
		for (String[] e: bayer_list) {
			for (String s: e) {
				if (s.equalsIgnoreCase(name)) return e[0];
			}
		}

		return name;
	}

	public static int read_line(byte[] buf, int fp, int len, byte[] rec)
	{
		int i;
		for (i=fp; i < len && i < buf.length && (i - fp) < rec.length; i++) {
				rec[i-fp] = buf[i];
				if (buf[i] == '\n') {
					return i+1;
				}
		}
		
		return i;
	}

	public static int read_line(RandomAccessFile raf, byte[] buf)
	{
		int i = 0;
		for (i=0; i < buf.length; i++) {
			try {
				buf[i] = raf.readByte();
				if (buf[i] == '\n') {
					return i+1;
				}
			} catch (IOException e) {
				return -1;
			}
		}
		
		return i;
	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/Yale Bright Star Catalog/BSC5.dat";
		YaleBrightStarAscCatalog ybsac = new YaleBrightStarAscCatalog(path);

		System.out.println(ybsac.elts[328]);
		System.out.println(ybsac.get("79",    "",  "Pisces"));
		System.out.println(ybsac.get("psi",   "2", "Pisces"));
		System.out.println(ybsac.get("psi",   "2", "Pisces"));
		System.out.println(ybsac.elts[6020]);
		System.out.println(ybsac.get("delta", "1", "Apus"));
		System.out.println(ybsac.get("51",    "",  "Andromeda"));
	}
}
