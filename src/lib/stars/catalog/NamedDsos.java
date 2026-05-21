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
import java.util.Hashtable;

import lib.astro.PracticalAstronomy;
import lib.util.Queue;

@SuppressWarnings("unused")
public class NamedDsos {
	// https://www.go-astronomy.com/herschel-objects.htm

	public class Element implements Comparable<Element> {
		// NGC Number,Herschel,Type,Common,Constellation
		public final String ngc;
		public final String name;
		
		public Element(String str)
		{
			if (str != null) {
				String[] field = str.split("[,]");
				ngc  = field[0].trim();
				name = field[1].trim();
			} else {
				ngc  = null;
				name = null;
			}
		}

		@Override public int compareTo(Element rhs)
		{
			return ngc.compareTo(rhs.ngc);
		}

		@Override public String toString()
		{
			return String.format("%s, %s", ngc, name);
		}
	}

	public final Element[] elts;
	public final Hashtable<String,Element> name_tbl = new Hashtable<String,Element>();
	public final Hashtable<String,Element> ngc_tbl  = new Hashtable<String,Element>();

	public NamedDsos(String path, String filename) throws IOException 
	{
		String c = null;
		if (path != null && path.endsWith("/")) {
			c = String.format("%s%s", path, filename);
		} else if (path != null && ! path.endsWith("/")) {
			c = String.format("%s/%s", path, filename);
		} else if (path == null) {
			c = filename;
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
			if (! line.startsWith("#") && ! line.trim().equals("")) {
				Element elt = new Element(line);
				all.append(elt);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		elts = new Element[all.length()];
		for (int i=0; i < elts.length; i++) {
			Element elt = elts[i] = all.remove();
			name_tbl.put(xform(elt.name), elt);
			ngc_tbl .put(xform(elt.ngc),  elt);
		}
		Arrays.sort(elts);
	}

	public NamedDsos(String path_name) throws IOException 
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
				Element elt = new Element(line);
				all.append(elt);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		elts = new Element[all.length()];
		for (int i=0; i < elts.length; i++) {
			Element elt = elts[i] = all.remove();
			name_tbl.put(xform(elt.name), elt);
			ngc_tbl .put(xform(elt.ngc),  elt);
		}
		Arrays.sort(elts);
	}

	public Element find_name(String name)
	{
		return name_tbl.get(xform(name));
	}

	public Element find_ngc(String ngc)
	{
		return ngc_tbl.get(xform(ngc));
	}

	public Element find(String str)
	{
		Element elt = find_name(str);
		if (elt == null) elt = find_ngc(str);

		return elt;
	}
	
	private String xform(String name)
	{
		return (name == null) ? null : name.toLowerCase().replaceAll("[ -]","") ;
	}

	public static void main(String[] args) throws IOException 
	{
		String home_dir  = "e:/home/projects/org.hypercomputing/data/nightsky/catalogs/";

		String dso_name   = home_dir + "Named-DSOs.txt";	// TODO
		NamedDsos dso = new NamedDsos(dso_name);
		for (Element elt: dso.elts) {
			System.out.printf("%s%n", elt.toString());
		}
	}
}
