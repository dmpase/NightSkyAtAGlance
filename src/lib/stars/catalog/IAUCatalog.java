package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 1988-2025 Douglas M. Pase                                     *
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

import lib.util.Queue;

public class IAUCatalog {
	
	public final File       file;
	public final IAUEntry[] elts;

	public IAUCatalog(String c) throws IOException 
	{
		file = new File(c);
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

		Queue<IAUEntry> queue = new Queue<IAUEntry>();
		for (int first=0; first < len; ) {
			int last=0, end=0;
			for (last=first; last < len; last++) {
				if (buf[last] == '\n' || buf[last] == '\r') {
					end = last;
					while (last < len && (buf[last] == '\n' || buf[last] == '\r')) {
						last += 1;
					}
					break;
				}
			}
			if (buf[first] == '#') {
				first = last;
				continue;
			}

			String rec = new String(buf, first, end-first);
			byte[] ba  = rec.getBytes();
			IAUEntry ent = new IAUEntry(ba, 0, ba.length);
			queue.append(ent);
			first = last;
		}

		elts   = new IAUEntry   [queue.length()];
		for (int i=0; i < elts.length; i++) {
			elts  [i] = queue.remove();
		}
		Arrays.sort(elts  );
	}

	public int find_idx(String name)
	{
		int result = -1;
		
		if (name != null) {
			result = IAUEntry.find_idx_by_name(name, elts);
		}
		
		return result;
	}

	public IAUEntry find(String name)
	{
		IAUEntry result = null;
		int idx = find_idx(name);

		if (0 <= idx && idx < elts.length) {
			result = elts[idx];
		}
		
		return result;
	}

	public IAUEntry find(int hr)
	{
		return IAUEntry.find_elt_by_hr(hr, elts);
	}
	
	public static class SortByRA implements Comparable<SortByRA> {
		
		public final IAUEntry data;
		
		public SortByRA(IAUEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByRA rhs) 
		{
			return data.hr - rhs.data.hr;
		}

		@Override public String toString()
		{
			return data.toString();
		}
	}
	
	public static class SortByMag implements Comparable<SortByMag> {
		
		public final IAUEntry data;
		
		public SortByMag(IAUEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByMag rhs) 
		{
			int result = 0;
			if (data.mag < rhs.data.mag) {
				result = -1;
			} else if (rhs.data.mag < data.mag) {
				result = +1;
			}
			return result;
		}

		@Override public String toString()
		{
			return data.toString();
		}
	}
	
	public IAUEntry find(String tld, String bayer)
	{
		for (IAUEntry e: elts) {
			if (tld.equalsIgnoreCase(e.constellation) && bayer.equalsIgnoreCase(e.id_4)) {
				return e;
			}
		}

		return null;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException 
	{
		String c = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/IAU/IAU-CSN.txt";
		IAUCatalog iau = new IAUCatalog(c);
		
		String path = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/Yale Bright Star Catalog/BSC5";
		YaleBrightStarBinCatalog bsc = new YaleBrightStarBinCatalog(path);
	}
}
