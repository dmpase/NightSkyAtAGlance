package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2026 Douglas M. Pase                                          *
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
import java.util.Arrays;

import lib.astro.PracticalAstronomy;
import lib.util.Queue;
import nightskyataglance.NightSkyAtAGlance;


public class Herschel2500 {
	// https://www.go-astronomy.com/herschel-objects.htm

	private static String arabic(String roman)
	{
		if (roman == null) return null;
		if (roman.equalsIgnoreCase("I"))    return "1";
		if (roman.equalsIgnoreCase("II"))   return "2";
		if (roman.equalsIgnoreCase("III"))  return "3";
		if (roman.equalsIgnoreCase("IV"))   return "4";
		if (roman.equalsIgnoreCase("V"))    return "5";
		if (roman.equalsIgnoreCase("VI"))   return "6";
		if (roman.equalsIgnoreCase("VII"))  return "7";
		if (roman.equalsIgnoreCase("VIII")) return "8";
		if (roman.equalsIgnoreCase("IX"))   return "9";
		if (roman.equalsIgnoreCase("X"))    return "10";
		return null;
	}

	public class Element implements Comparable<Element> {
		// Class,Number,NGC/IC,Con,H400
		public final String name;
		public final String her_class;
		public final String number;
		public final String alt;
		public final String ngc;
		public final String h400;

		public final String type;

		public final double ra_hrs;
		public final double de_deg;
		public final double amag;
		public final double diameter;
		public final String constellation;

		public Element(String str, NgcIcCatalog ngc_cat, UgcCatalog ugc_cat, Herschel400 her_400)
		{
			if (str != null && ngc_cat != null && her_400 != null) {
				// System.out.printf("%s%n", str);
				String[] field = str.split("[,]");
				her_class     = field[0].trim();
				number        = field[1].trim();
				name          = String.format("H%s-%s", her_class, number);
				alt           = String.format("H%s-%s", arabic(her_class), number);
				ngc           = field[2].replaceAll("[ ]","");
				constellation = field[3].trim();

				if (field.length < 5 || field[4] == null || field[4].trim().equals("")) {
					h400 = null;
				} else {
					Herschel400.Element elt = her_400.find_ngc(ngc);
					h400 = (elt == null) ? null : elt.name;
				}

				NgcIcEntry ent = ngc_cat.find(ngc);
				if (ent != null) {
					ra_hrs        = ent.ra_dhrs;
					de_deg        = ent.dec_ddeg;
					amag          = ent.app_mag;
					diameter      = ent.ang_diam;
					type          = ent.dso_class;
				} else {
					UgcEntry ugc = ugc_cat.find(ngc);
					ra_hrs        = ugc.ra_ddeg;
					de_deg        = ugc.de_ddeg;
					amag          = ugc.photo_mag;
					diameter      = Math.max(ugc.maj_axis_red, ugc.maj_axis_blue);
					type          = DsoType.galaxy[0];
				}
			} else {
				her_class     = null;
				number        = null;
				name          = null;
				alt           = null;
				ngc           = null;
				constellation = null;
				h400          = null;
				type          = null;

				ra_hrs        = Double.NaN;
				de_deg        = Double.NaN;
				amag          = Double.POSITIVE_INFINITY;
				diameter      = 0;
			}
		}

		public Element(String str, NgcIcCatalog ngc_cat, UgcCatalog ugc_cat, HerschelCatalog her)
		{
			if (str != null && ngc_cat != null && her != null) {
				// System.out.printf("%s%n", str);
				String[] field = str.split("[,]");
				her_class     = field[0].trim();
				number        = field[1].trim();
				name          = String.format("H%s-%s", her_class, number);
				alt           = String.format("H%s-%s", arabic(her_class), number);
				ngc           = field[2].replaceAll("[ ]","");
				constellation = field[3].trim();

				if (field.length < 5 || field[4] == null || field[4].trim().equals("")) {
					h400 = null;
				} else {
					HerschelEntry elt = her.find(ngc);
					h400 = (elt == null) ? null : elt.name;
				}

				NgcIcEntry ent = ngc_cat.find(ngc);
				if (ent != null) {
					ra_hrs        = ent.ra_dhrs;
					de_deg        = ent.dec_ddeg;
					amag          = ent.app_mag;
					diameter      = ent.ang_diam;
					type          = ent.dso_class;
				} else {
					UgcEntry ugc = ugc_cat.find(ngc);
					ra_hrs        = ugc.ra_ddeg;
					de_deg        = ugc.de_ddeg;
					amag          = ugc.photo_mag;
					diameter      = Math.max(ugc.maj_axis_red, ugc.maj_axis_blue);
					type          = DsoType.galaxy[0];
				}
			} else {
				her_class     = null;
				number        = null;
				name          = null;
				alt           = null;
				ngc           = null;
				constellation = null;
				h400          = null;
				type          = null;

				ra_hrs        = Double.NaN;
				de_deg        = Double.NaN;
				amag          = Double.POSITIVE_INFINITY;
				diameter      = 0;
			}
		}
		
		// returns the index of the searched-for item
		// returning the index allows searches for nearby items
		public int find(String name, Element[] list)
		{
			return find(name, list, 0, list.length);
		}

		private int find(String name, Element[] list, int lower, int upper)
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

		public boolean matches(DsoFilter filter)
		{
			return filter == null || filter.matches(diameter, amag, type);
		}

		@Override public int compareTo(Element rhs)
		{
			return name.compareTo(rhs.name);
		}

		@Override public String toString()
		{
			return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %.1f, %.1f'", name, alt, h400, ngc, type, constellation, 
				PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs), PracticalAstronomy.decimal_degrees_to_str_dms(de_deg), amag, diameter);
		}
	}
	
	public final Element[] elts;

	public Herschel2500(String path, String name, NgcIcCatalog ngc_ic, UgcCatalog ugc_cat, Herschel400 her_400) throws IOException 
	{
		String c = null;
		if (path != null && path.endsWith("/")) {
			c = String.format("%s%s", path, name);
		} else if (path != null && ! path.endsWith("/")) {
			c = String.format("%s/%s", path, name);
		} else if (path == null) {
			c = name;
		}
		File file = new File(c);

		// read the file into memory
		byte[] buf = new byte[10*1024*1024];
		int len = 0;

		if (file.isFile()) {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			len = raf.read(buf);
			raf.close();
		} else {
			try(InputStream input_stream = getClass().getResourceAsStream(c)) {
			    if (input_stream == null) {
			        throw new FileNotFoundException("File '" + c + "' not found!");
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

		int line_start = 0;

		// convert the file into an array of objects
		Queue<Element> all = new Queue<Element>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = new String(buf, line_start, line_len);
			if (! line.startsWith("#")) {
				Element elt = new Element(line, ngc_ic, ugc_cat, her_400);
				all.append(elt);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		elts = new Element[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
		Arrays.sort(elts);
	}

	public Herschel2500(String path_name, NgcIcCatalog ngc_ic, UgcCatalog ugc_cat, Herschel400 her_400) throws IOException 
	{
		String c = path_name;
		File file = new File(c);

		// read the file into memory
		byte[] buf = new byte[10*1024*1024];
		int len = 0;

		if (file.isFile()) {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			len = raf.read(buf);
			raf.close();
		} else {
			try(InputStream input_stream = getClass().getResourceAsStream(c)) {
			    if (input_stream == null) {
			        throw new FileNotFoundException("File '" + c + "' not found!");
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

		int line_start = 0;

		// convert the file into an array of objects
		Queue<Element> all = new Queue<Element>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = new String(buf, line_start, line_len);
			if (! line.startsWith("#")) {
				Element elt = new Element(line, ngc_ic, ugc_cat, her_400);
				all.append(elt);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		elts = new Element[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
		Arrays.sort(elts);
	}

	public static void main(String[] args) throws IOException 
	{
		String home_dir  = "e:/home/projects/org.hypercomputing/data/nightsky/catalogs/";

		String ngc_name  = home_dir + "NGC+IC-J2000.txt";
		NgcIcCatalog ngc_ic = new NgcIcCatalog(ngc_name);

		String ugc_name  = home_dir + "ugc.dat.txt";
		UgcCatalog ugc_cat = new UgcCatalog(ugc_name);

		String h40_name   = home_dir + "Herschel 400 Catalog go.csv";
		Herschel400 h40_cat = new Herschel400(h40_name, ngc_ic);

		String her_name   = home_dir + "Herschel 2500 Catalog.csv";
		Herschel2500 her = new Herschel2500(her_name, ngc_ic, ugc_cat, h40_cat);
		for (Element elt: her.elts) {
			System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt.toString());
		}
	}
}
