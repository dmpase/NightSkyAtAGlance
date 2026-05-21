package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2025 - 2026 Douglas M. Pase                                   *
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


/*/
Byte-by-byte Description of file: catalog
--------------------------------------------------------------------------------
   Bytes Format  Units   Label    Explanations
--------------------------------------------------------------------------------
   1-  4  I4     ---     HR       [1/9110]+ Harvard Revised Number
                                    = Bright Star Number
   5- 14  A10    ---     Name     Name, generally Bayer and/or Flamsteed name
  15- 25  A11    ---     DM       Durchmusterung Identification (zone in bytes 17-19)
  26- 31  I6     ---     HD       [1/225300]? Henry Draper Catalog Number
  32- 37  I6     ---     SAO      [1/258997]? SAO Catalog Number
  38- 41  I4     ---     FK5      ? FK5 star Number
      42  A1     ---     IRflag   [I] I if infrared source
      43  A1     ---    r_IRflag *[ ':] Coded reference for infrared source
      44  A1     ---    Multiple *[AWDIRS] Double or multiple-star code
  45- 49  A5     ---     ADS      Aitken's Double Star Catalog (ADS) designation
  50- 51  A2     ---     ADScomp  ADS number components
  52- 60  A9     ---     VarID    Variable star identification
  61- 62  I2     h       RAh1900  ?Hours RA, equinox B1900, epoch 1900.0 (1)
  63- 64  I2     min     RAm1900  ?Minutes RA, equinox B1900, epoch 1900.0 (1)
  65- 68  F4.1   s       RAs1900  ?Seconds RA, equinox B1900, epoch 1900.0 (1)
      69  A1     ---     DE-1900  ?Sign Dec, equinox B1900, epoch 1900.0 (1)
  70- 71  I2     deg     DEd1900  ?Degrees Dec, equinox B1900, epoch 1900.0 (1)
  72- 73  I2     arcmin  DEm1900  ?Minutes Dec, equinox B1900, epoch 1900.0 (1)
  74- 75  I2     arcsec  DEs1900  ?Seconds Dec, equinox B1900, epoch 1900.0 (1)
  76- 77  I2     h       RAh      ?Hours RA, equinox J2000, epoch 2000.0 (1)
  78- 79  I2     min     RAm      ?Minutes RA, equinox J2000, epoch 2000.0 (1)
  80- 83  F4.1   s       RAs      ?Seconds RA, equinox J2000, epoch 2000.0 (1)
      84  A1     ---     DE-      ?Sign Dec, equinox J2000, epoch 2000.0 (1)
  85- 86  I2     deg     DEd      ?Degrees Dec, equinox J2000, epoch 2000.0 (1)
  87- 88  I2     arcmin  DEm      ?Minutes Dec, equinox J2000, epoch 2000.0 (1)
  89- 90  I2     arcsec  DEs      ?Seconds Dec, equinox J2000, epoch 2000.0 (1)
  91- 96  F6.2   deg     GLON     ?Galactic longitude (1)
  97-102  F6.2   deg     GLAT     ?Galactic latitude (1)
 103-107  F5.2   mag     Vmag     ?Visual magnitude (1)
     108  A1     ---   n_Vmag    *[ HR] Visual magnitude code
     109  A1     ---   u_Vmag     [ :?] Uncertainty flag on V
 110-114  F5.2   mag     B-V      ? B-V color in the UBV system
     115  A1     ---   u_B-V      [ :?] Uncertainty flag on B-V
 116-120  F5.2   mag     U-B      ? U-B color in the UBV system
     121  A1     ---   u_U-B      [ :?] Uncertainty flag on U-B
 122-126  F5.2   mag     R-I      ? R-I   in system specified by n_R-I
     127  A1     ---   n_R-I      [CE:?D] Code for R-I system (Cousin, Eggen)
 128-147  A20    ---     SpType   Spectral type
     148  A1     ---   n_SpType   [evt] Spectral type code
 149-154  F6.3 arcsec/yr pmRA    *?Annual proper motion in RA J2000, FK5 system
 155-160  F6.3 arcsec/yr pmDE     ?Annual proper motion in Dec J2000, FK5 system
     161  A1     ---   n_Parallax [D] D indicates a dynamical parallax,
                                    otherwise a trigonometric parallax
 162-166  F5.3   arcsec  Parallax ? Trigonometric parallax (unless n_Parallax)
 167-170  I4     km/s    RadVel   ? Heliocentric Radial Velocity
 171-174  A4     ---   n_RadVel  *[V?SB123O ] Radial velocity comments
 175-176  A2     ---   l_RotVel   [<=> ] Rotational velocity limit characters
 177-179  I3     km/s    RotVel   ? Rotational velocity, v sin i
     180  A1     ---   u_RotVel   [ :v] uncertainty and variability flag on
                                    RotVel
 181-184  F4.1   mag     Dmag     ? Magnitude difference of double,
                                    or brightest multiple
 185-190  F6.1   arcsec  Sep      ? Separation of components in Dmag
                                    if occultation binary.
 191-194  A4     ---     MultID   Identifications of components in Dmag
 195-196  I2     ---     MultCnt  ? Number of components assigned to a multiple
     197  A1     ---     NoteFlag [*] a star indicates that there is a note
                                    (see file notes)
/*/

public class YaleBrightStarAscEntry {

	public final int     HR;
	public final String  name;
	public final String  flamsteed;
	public final String  bayer;
	public final String  subname;
	public final String  constellation;
	public final String  DM;
	public final int     HD;
	public final int     SAO;
	public final int     FKS;
	public final boolean IRflag;
	public final byte    r_IRflag;
	public final byte    Multiple;
	public final int     ADS;
	public final String  ADScomp;
	public final String  VarID;
	public final int     RAh1900;
	public final int     RAm1900;
	public final float   RAs1900;
	public final boolean DE_1900;
	public final int     DEd1900;
	public final int     DEm1900;
	public final int     DEs1900;
	public final int     RAh;
	public final int     RAm;
	public final float   RAs;
	public final float   RA;
	public final boolean DE_;
	public final int     DEd;
	public final int     DEm;
	public final int     DEs;
	public final float   DE;
	public final float   GLON;
	public final float   GLAT;
	public final float   Vmag;
	public final byte    n_Vmag;
	public final byte    u_Vmag;

	public YaleBrightStarAscEntry() 
	{
		HR            = 0;
		name          = "";
		flamsteed     = "";
		bayer         = "";
		subname       = "";
		constellation = "";
		DM            = "";
		HD            = 0;
		SAO           = 0;
		FKS           = 0;
		IRflag        = false;
		r_IRflag      = 0;
		Multiple      = 0;
		ADS           = 0;
		ADScomp       = "";
		VarID         = "";
		RAh1900       = 0;
		RAm1900       = 0;
		RAs1900       = 0;
		DE_1900       = true;
		DEd1900       = 0;
		DEm1900       = 0;
		DEs1900       = 0;
		RAh           = 0;
		RAm           = 0;
		RAs           = 0;
		RA            = 0;
		DE_           = true;
		DEd           = 0;
		DEm           = 0;
		DEs           = 0;
		DE            = 0;
		GLON          = 0;
		GLAT          = 0;
		Vmag          = 0;
		n_Vmag        = 0;
		u_Vmag        = 0;
	}
	
	public YaleBrightStarAscEntry(byte[] rec, int len) 
	{
		len -= 1;	// remove the terminating '\n'

															// start - end, offset=(start-1), length=(end-start+1)
		String HR_str  = new String(rec,  0, 4);				// [1 -   4] = Harvard Revised Catalog Number
		HR             = Integer.parseInt(HR_str.trim());
		name           = new String(rec,  4, 10).trim();		// [  5- 14] = name
		flamsteed      = new String(rec,  4,  3).trim();		// [  5-  7] = Flamsteed number
		bayer          = new String(rec,  7,  3).trim();		// [  8- 10] = Bayer (Greek or Latin letter)
		subname        = new String(rec, 10,  1).trim();		// [ 11- 11] = Sub-name
		constellation  = new String(rec, 11,  3).trim();		// [ 12- 14] = Constellation
		DM             = new String(rec, 14, 11).trim();		// [ 15- 25] = Durchmusterung Identification (zone in bytes 17-19)
		String HD_str  = new String(rec, 25,  6).trim();		// [ 26- 31] = Henry Draper Catalog Number
		HD             = HD_str.equalsIgnoreCase("")  ? 0 : Integer.parseInt(HD_str.trim());
		String SAO_str = new String(rec, 31,  6).trim();		// [ 32- 37] = SAO Catalog Number
		SAO            = SAO_str.equalsIgnoreCase("") ? 0 : Integer.parseInt(SAO_str);
		String FKS_str = new String(rec, 37,  4).trim();		// [ 38- 41] = FKS Catalog Number
		FKS            = FKS_str.equalsIgnoreCase("") ? 0 : Integer.parseInt(FKS_str);
		IRflag         = rec[41] == 'I';						// [ 42- 42] = 'I' means it is an infrared source
		r_IRflag       = rec[42];								// [ 43- 43] coded reference for infrared source
		Multiple       = rec[43];								// [ 44- 44] [AWDIRS] Double or multiple-star code
		String ADS_str = new String(rec, 44,  5).trim();		// [ 45- 49] = Aitken's Double Star Catalog (ADS) designation
		ADS            = ADS_str.equalsIgnoreCase("") ? 0 : Integer.parseInt(ADS_str);
		ADScomp        = new String(rec, 49,  2).trim();		// [ 50- 51] = ADS number components
		VarID          = new String(rec, 51,  9).trim();		// [ 52- 60] = Variable star identification
		String RAh19ks = new String(rec, 60,  2).trim();		// [ 61- 62] = Hours RA, equinox B1900, epoch 1900.0 (1)
		RAh1900        = RAh19ks.equalsIgnoreCase("") ? 0 : Integer.parseInt(RAh19ks);
		String RAm19ks = new String(rec, 62,  2).trim();		// [ 63- 64] = Minutes RA, equinox B1900, epoch 1900.0 (1)
		RAm1900        = RAm19ks.equalsIgnoreCase("") ? 0 : Integer.parseInt(RAm19ks);
		String RAs19ks = new String(rec, 64,  4).trim();		// [ 65- 68] = Seconds RA, equinox B1900, epoch 1900.0 (1)
		RAs1900        = RAs19ks.equalsIgnoreCase("") ? 0 : Float.parseFloat(RAs19ks);
		DE_1900        = rec[68] != '-';						// [ 69- 69] = Sign DEC, equinox B1900, epoch 1900.0 (1)
		String DEh19ks = new String(rec, 69,  2).trim();		// [ 70- 71] = Hours DEC, equinox B1900, epoch 1900.0 (1)
		DEd1900        = DEh19ks.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEh19ks);
		String DEm19ks = new String(rec, 71,  2).trim();		// [ 72- 73] = Minutes DEC, equinox B1900, epoch 1900.0 (1)
		DEm1900        = DEm19ks.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEm19ks);
		String DEs19ks = new String(rec, 73,  2).trim();		// [ 74- 75] = Seconds DEC, equinox B1900, epoch 1900.0 (1)
		DEs1900        = DEs19ks.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEs19ks);
		String RAhs    = new String(rec, 75,  2).trim();		// [ 76- 77] = Hours RA, equinox J2000, epoch 2000.0 (1)
		RAh            = RAhs.equalsIgnoreCase("") ? 0 : Integer.parseInt(RAhs);
		String RAms    = new String(rec, 77,  2).trim();		// [ 78- 79] = Minutes RA, equinox J2000, epoch 2000.0 (1)
		RAm            = RAms.equalsIgnoreCase("") ? 0 : Integer.parseInt(RAms);
		String RAss    = new String(rec, 79,  4).trim();		// [ 80- 83] = Seconds RA, equinox J2000, epoch 2000.0 (1)
		RAs            = RAss.equalsIgnoreCase("") ? 0 : Float.parseFloat(RAss);
		RA             = (float) ((double) RAh + (double) RAm / 60.0 + (double) RAs / 3600.0);
		DE_            = rec[83] != '-';						// [ 84- 84] = Sign DEC, equinox J2000, epoch 2000.0 (1)
		String DEds    = new String(rec, 84,  2).trim();		// [ 85- 86] = Hours DEC, equinox J2000, epoch 2000.0 (1)
		DEd            = DEds.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEds);
		String DEms    = new String(rec, 86,  2).trim();		// [ 87- 88] = Minutes DEC, equinox J2000, epoch 2000.0 (1)
		DEm            = DEms.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEms);
		String DEss    = new String(rec, 88,  2).trim();		// [ 89- 90] = Seconds DEC, equinox J2000, epoch 2000.0 (1)
		DEs            = DEss.equalsIgnoreCase("") ? 0 : Integer.parseInt(DEss);
		DE             = (float) ((DE_ ? 1 : -1 ) * ((double) DEd + (double) DEm / 60.0 + (double) DEs / 3600.0));
		String GLON_s  = new String(rec, 90,  6).trim();		// [ 91- 96] = Galactic longitude (1)
		GLON           = GLON_s.equalsIgnoreCase("") ? 0 : Float.parseFloat(GLON_s);
		String GLAT_s  = new String(rec, 96,  6).trim();		// [ 97-102] = Galactic latitude (1)
		GLAT           = GLAT_s.equalsIgnoreCase("") ? 0 : Float.parseFloat(GLAT_s);
		String Vmag_s  = new String(rec,102,  5).trim();		// [103-107] = Visual magnitude (1)
		Vmag           = Vmag_s.equalsIgnoreCase("") ? 0 : Float.parseFloat(Vmag_s);
		n_Vmag         = rec[107];								// [108-108] Visual magnitude code
		u_Vmag         = rec[108];								// [109-109] Uncertainty flag on V
	}
	
	public final String name()
	{
		String result = "";
		if (bayer != null && ! bayer.equals("")) {
			String gen = BayerEntry.find_genitive(constellation);
			String bay = BayerEntry.find_greek(bayer);
			result = bay + " " + gen;
		} else if (flamsteed != null && ! flamsteed.matches("")) {
			String gen = BayerEntry.find_genitive(constellation);
			String fla = flamsteed;
			result = fla + " " + gen;
		} else {
		}

		return result;
	}
	
	public static int find_idx(YaleBrightStarAscEntry[] list, String name)
	{
		int result = -1;

		if (name != null && ! name.equals("") && list != null) {
			String lc = name.toLowerCase();
			if (name.matches("[Hh][Rr][0-9][0-9]*")) {									// HR ddd...
				String[] tokens = Bayer.parse(name);
				if (tokens != null && tokens.length == 2) {
					int HR = Integer.parseInt(tokens[1]);
					for (int i=0; i < list.length; i++) {
						if (list[i].HR == HR) {
							result = i;
							break;
						}
					}
				}
			} else if (name.matches("[Hh][Dd][0-9][0-9]*")) {							// HD ddd...
				String[] tokens = Bayer.parse(name);
				if (tokens != null && tokens.length == 2) {
					int HD = Integer.parseInt(tokens[1]);
					for (int i=0; i < list.length; i++) {
						if (list[i].HD == HD) {
							result = i;
							break;
						}
					}
				}
			} else if (lc.matches("[0-9][0-9]*[a-z][a-z]*")) {					// 80 Peg or 80 Pegasus or 80 Pegasi
				String[] tokens = Bayer.parse(name);
				if (tokens != null && tokens.length == 2) {
					String constellation = tokens[0];
					String flamsteed     = tokens[1];
					for (int i=0; i < list.length; i++) {
						if (constellation.equalsIgnoreCase(constellation) && flamsteed.equalsIgnoreCase(list[i].flamsteed)) {
							result = i;
							break;
						}
					}
				}
			} else if (lc.matches("[a-z][a-z.]*")) {							// Alf Peg or Alpha Peg or Alpha Pegasus or Alpha Pegasi
				String[] tokens = Bayer.parse(name);
				if (tokens != null && tokens.length == 2) {
					String constellation = tokens[0];
					String bayer         = tokens[1];
					for (int i=0; i < list.length; i++) {
						if (constellation.equalsIgnoreCase(constellation) && bayer.equalsIgnoreCase(list[i].bayer)) {
							result = i;
							break;
						}
					}
				}
			} else if (lc.matches("[a-z][a-z.]*[0-9][0-9]*[a-z][a-z]*")) {		// Pi 3 Ori or ...
				String[] tokens = Bayer.parse(name);
				if (tokens != null && tokens.length == 3) {
					String constellation = tokens[0];
					String bayer         = tokens[1];
					String subname       = tokens[2];
					for (int i=0; i < list.length; i++) {
						if (constellation.equalsIgnoreCase(constellation) && bayer.equalsIgnoreCase(list[i].bayer) &&  subname.equalsIgnoreCase(list[i].subname)) {
							result = i;
							break;
						}
					}
				}
			}
		}

		return result;
	}
	
	public final boolean match(String c, String b, String s)
	{
		return c.equalsIgnoreCase(constellation) && (b.equalsIgnoreCase(flamsteed) || b.equalsIgnoreCase(bayer)) && s.equalsIgnoreCase(subname);
	}
	
	public static final String degree_symbol = new String(Character.toChars(0x00B0));
	public String toString()
	{
		String result = "[";
		
		result += String.format("%d,", HR);
		result += String.format("%s", name);
		result += "(";
		result += String.format("%s,", flamsteed);
		result += String.format("%s,", bayer);
		result += String.format("%s,", subname);
		result += String.format("%s",  constellation);
		result += "),";
		result += String.format("%s,", DM);
		result += String.format("%d,", HD);
		result += String.format("%d,", SAO);
		result += String.format("%d,", FKS);
		result += String.format("%s,", IRflag);
		result += String.format("%c,", r_IRflag);
		result += String.format("%c,", Multiple);
		result += String.format("%d,", ADS);
		result += String.format("%s,", ADScomp);
		result += String.format("%s,", VarID);
		result += String.format("%02dh %02dm %04.1fs,", RAh1900, RAm1900, RAs1900);
		result += String.format("%s%02d%s %02d' %02d\",", DE_1900?"+":"-", DEd1900, degree_symbol, DEm1900, DEs1900);
		result += String.format("%02dh %02dm %04.1fs,", RAh, RAm, RAs);
		result += String.format("%s%02d%s %02d' %02d\",", DE_?"+":"-", DEd, degree_symbol, DEm, DEs);
		result += String.format("%6.2f,", GLON);
		result += String.format("%6.2f,", GLAT);
		result += String.format("%5.2f,", Vmag);
		result += String.format("%c,", n_Vmag);
		result += String.format("%c,", u_Vmag);

		return result + "]";
	}

	public static YaleBrightStarAscEntry find_elt_by_hr(int hr, YaleBrightStarAscEntry[] list)
	{
		for (YaleBrightStarAscEntry elt: list) {
			if (hr == elt.HR) {
				return elt;
			}
		}
		
		return null;
	}

	public static YaleBrightStarAscEntry find_elt_by_hd(int hd, YaleBrightStarAscEntry[] list)
	{
		for (YaleBrightStarAscEntry elt: list) {
			if (hd == elt.HD) {
				return elt;
			}
		}
		
		return null;
	}

	public static YaleBrightStarAscEntry find_elt_by_hd(String HD, YaleBrightStarAscEntry[] list)
	{
		int hd = Integer.parseInt(HD);
		for (YaleBrightStarAscEntry elt: list) {
			if (hd == elt.HD) {
				return elt;
			}
		}
		
		return null;
	}

	public static YaleBrightStarAscEntry find_elt_by_bayer(String bayer, YaleBrightStarAscEntry[] list)
	{
		if (bayer != null && ! bayer.equals("")) {
			bayer = bayer.replaceAll("[ -]", "").toLowerCase();
			String[] tokens = Bayer.parse(bayer);
			if (tokens != null && 1 < tokens.length) {
				if (bayer.matches("[Hh][Rr][0-9][0-9]*")) {										// HR ddd...
					int HR = Integer.parseInt(tokens[1]);
					return find_elt_by_hr(HR, list);
				} else if (bayer.matches("[Hh][Dd][0-9][0-9]*")) {								// HD ddd...
					String HD = tokens[1];
					return find_elt_by_hd(HD, list);
				} else if (bayer.matches("[0-9][0-9]*[a-zA-Z][a-zA-Z]*")) {						// 80 Peg or 80 Pegasus or 80 Pegasi
					String constellation = tokens[0];
					int flamsteed = Integer.parseInt(tokens[1]);
					for (YaleBrightStarAscEntry elt: list) {
						if (constellation != null && constellation.equalsIgnoreCase(elt.constellation) && elt.flamsteed != null && ! elt.flamsteed.equals("")) {
							int elt_flam = Integer.parseInt(elt.flamsteed);
							if (flamsteed == elt_flam) {
								return elt;
							}
						}
					}
				} else if (bayer.matches("[a-zA-Z][a-zA-Z.]*")) {								// Alf Peg or Alpha Peg or Alpha Pegasus or Alpha Pegasi
					String constellation = tokens[0];
					String alpha         = tokens[1];
					for (YaleBrightStarAscEntry elt: list) {
						if (constellation != null && constellation.equalsIgnoreCase(elt.constellation)) {
							if (elt.bayer != null && alpha.equalsIgnoreCase(elt.bayer)) {
								return elt;
							}
						}
					}
				} else if (bayer.matches("[a-zA-Z][a-zA-Z.]*[0-9][0-9]*[a-zA-Z][a-zA-Z]*")) {	// Pi 3 Ori or ...
					String constellation = tokens[0];
					String alpha         = tokens[1];
					int sub              = Integer.parseInt(tokens[2]);
					for (YaleBrightStarAscEntry elt: list) {
						if (elt.constellation != null && constellation.equalsIgnoreCase(elt.constellation)) {
							if (elt.bayer != null && alpha.equalsIgnoreCase(elt.bayer)) {
								if (elt.subname != null && sub == Integer.parseInt(elt.subname)) {
									return elt;
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) throws IOException
	{
		String bsc_name = "bsc5.dat";
		String path = "D:/home/projects/org.hypercomputing/data/nightsky/catalogs/";
		YaleBrightStarAscCatalog bsc = new YaleBrightStarAscCatalog(path + bsc_name);

		YaleBrightStarAscEntry elt = find_elt_by_bayer("tau02 Eri", bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("HR 850", bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("HD 17824", bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("eta Dra", bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_hr(850, bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_hd(17824, bsc.elts);
		System.out.println(elt);
		elt = find_elt_by_bayer("23 And", bsc.elts);
		System.out.println(elt);
	}
}
