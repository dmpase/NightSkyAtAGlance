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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Hashtable;

import lib.util.Queue;

public class NgcIcCatalog {
	
	public final String       catalog;
	public final File         file;
	public final NgcIcEntry[] elts;

	public NgcIcCatalog(String c) throws IOException 
	{
		catalog = c;
		file    = new File(c);

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
		
		// skip the first four non-blank lines
		int line_start = 0;
		for (int i=0; i < 4; i++) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		// convert the file into an array of objects
		Queue<NgcIcEntry> all = new Queue<NgcIcEntry>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = new String(buf, line_start, line_len);
			NgcIcEntry ent = new NgcIcEntry(line);
			all.append(ent);

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && buf[line_start] < ' '; line_start++) {
				;
			}
		}

		elts = new NgcIcEntry[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
		Arrays.sort(elts);
		
		update();
	}

	public int find_idx(String name)
	{
		int result = -1;
		
		if (name != null) {
			result = NgcIcEntry.find(name, elts);
		}
		
		return result;
	}

	public NgcIcEntry find(String name)
	{
		NgcIcEntry result = null;
		int idx = find_idx(name);
		
		if (0 <= idx && idx < elts.length) {
			result = elts[idx];
		}
		
		return result;
	}

	public NgcIcEntry find(boolean is_ngc, int number)
	{
		NgcIcEntry result = null;
		
		if (0 < number) {
			int idx = NgcIcEntry.find(is_ngc, number, elts);
			if (0 <= idx && idx < elts.length) {
				result = elts[idx];
			}
		}
		
		return result;
	}

	public NgcIcEntry find_ngc(int number)
	{
		NgcIcEntry result = null;
		
		if (0 < number) {
			int idx = NgcIcEntry.find(true, number, elts);
			if (0 <= idx && idx < elts.length) {
				result = elts[idx];
			}
		}
		
		return result;
	}

	public NgcIcEntry find_ic(int number)
	{
		NgcIcEntry result = null;
		
		if (0 < number) {
			int idx = NgcIcEntry.find(false, number, elts);
			if (0 <= idx && idx < elts.length) {
				result = elts[idx];
			}
		}
		
		return result;
	}
	
	private void update()
	{
		update(find("ic  1101").update(13.70, 1.2));	// https://cseligman.com/text/atlas/ic11.htm#ic1101
		update(find("ic  4333").update(14.10, 1.6));	// https://en.wikipedia.org/wiki/List_of_NGC_objects_(1001%E2%80%932000)#
		update(find("ic  2051").update(11.60, 2.6));
		update(find("ngc 2144").update(13.00, 1.6));	// https://cseligman.com/text/atlas/ngc21.htm#2144
		update(find("ngc 7637").update(13.00, 2.5));	// https://cseligman.com/text/atlas/ngc76.htm#7637
		update(find("ic  4545").update(12.90, 2.0));	// https://cseligman.com/text/atlas/ic45.htm#ic4545
		update(find("ngc 3195").update(11.60, 0.6));	// https://cseligman.com/text/atlas/ngc31a.htm#3195
	}
	
	private void update(NgcIcEntry ent)
	{
		if (ent != null) {
			int idx = find_idx(ent.name);
			elts[idx] = ent;
		}
	}

	public static void main(String[] args) throws IOException 
	{
		String file_name = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/NGC+IC-J2000/NGC+IC-J2000.txt";
		NgcIcCatalog ngc = new NgcIcCatalog(file_name);
		for (NgcIcEntry dso : ngc.elts) {
			System.out.printf("%s%n", dso.toString());
		}
		NgcIcEntry ic1101 = ngc.find("ic 1101");
		System.out.printf("%s%n", ic1101.toString());
		NgcIcEntry ngc4545 = ngc.find_ngc(4545);
		System.out.printf("%s%n", ngc4545.toString());
		NgcIcEntry ngc0895 = ngc.find(true, 895);
		System.out.printf("%s%n", ngc0895.toString());
		Hashtable<String,String> ht = new Hashtable<String,String>();
		for (NgcIcEntry dso : ngc.elts) {
			if (dso.dso_class != null) {
				ht.put(dso.dso_class, dso.dso_class);
			}
		}
		for (String str: ht.keySet()) {
			System.out.printf("'%s'\n", str);
		}
	}
}
