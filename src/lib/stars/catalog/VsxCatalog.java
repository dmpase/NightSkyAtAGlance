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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
// import java.io.RandomAccessFile;

import lib.util.Queue;

public class VsxCatalog {
	
	public final String     catalog;
	public final File       file;
	public final VsxEntry[] elts;

	public VsxCatalog(String c) throws IOException 
	{
		catalog = c;
		file    = new File(c);
		
		// open the file
		InputStream input_stream = null;
		if (file.isFile()) {
			input_stream = new FileInputStream(file);
		}

		if (input_stream == null) {
			try {
				input_stream = getClass().getResourceAsStream(c);
			    if (input_stream == null) {
			        throw new FileNotFoundException("File '" + c + "' not found!");
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}

		// double limit = 15;
		// RandomAccessFile raf = new RandomAccessFile(String.format("data/nightsky/catalogs/aavso.vsx.%.0f.tsv", limit), "rw");
		
		// convert the file into an array of objects
		byte[] buf = new byte[1024];
		Queue<VsxEntry> all = new Queue<VsxEntry>();
		boolean eof = false;
		while (! eof) {
			// find the next line
			int ch;
			for (ch=input_stream.read(); ch == '\n' || ch == '\r'; ch=input_stream.read()) {
				;
			}
			
			if (ch < 0) break;

			int len;
			for (len=0; len < buf.length && 0 <= ch && ch != '\n' && ch != '\r'; len++) {
		    	buf[len] = (byte) ch;
		    	ch = input_stream.read();
			}

			String line = (new String(buf, 0, len));
			if (line != null && ! line.equals("") && 200 <= line.length() && line.matches("[ ]*[0-9][0-9]*[|].*")) {
				VsxEntry ent = new VsxEntry(line);
				all.append(ent);

				/*
				if (ent.max_mag <= limit) {
					buf[len++] = '\n';
					raf.write(buf, 0, len);

					if (all.length()%5000 == 0) {
						System.out.printf("%,10d: %s%n", all.length(), ent.toString());
					}
				}
			} else {
				buf[len++] = '\n';
				raf.write(buf, 0, len);
				*/
			}
			
			eof = ch < 0;
		}

		input_stream.close();
		// raf.close();

		elts = new VsxEntry[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
	}

	public int find_idx(String name)
	{
		int result = -1;
		
		if (name != null) {
			result = VsxEntry.find(name, elts);
		}
		
		return result;
	}

	public VsxEntry find(String name)
	{
		VsxEntry result = null;
		int idx = find_idx(name);

		if (0 <= idx && idx < elts.length) {
			result = elts[idx];
		}
		
		return result;
	}

	public static void main(String[] args) throws IOException 
	{
		String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/nightsky/catalogs/";
		String her_name  = home_dir + "aavso-vsx.tsv";
		VsxCatalog m = new VsxCatalog(her_name);
		for (VsxEntry vs : m.elts) {
			System.out.printf("%s%n", vs.toString());
		}
	}
}
