package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2025-2025 Douglas M. Pase                                     *
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


public class WebbVariableStars {

	public final String     catalog;
	public final File       file;
	public final WebbVariableStarsEntry[] elts;

	public WebbVariableStars(String c) throws IOException 
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

		// convert the file into an array of objects
		int line_start = 0;

		Queue<WebbVariableStarsEntry> all = new Queue<WebbVariableStarsEntry>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = (new String(buf, line_start, line_len));
			if (! line.equals("") && line.charAt(0) == '|' && '0' <= line.charAt(4) && line.charAt(4) <= '9' && 60 <= line.length()) {
				WebbVariableStarsEntry ent = new WebbVariableStarsEntry(line);
				all.append(ent);
			}
			
			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && (buf[line_start] == '\n' || buf[line_start] == '\r'); line_start++) {
				;
			}
		}

		elts = new WebbVariableStarsEntry[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
	}

	public int find_idx(int webb_idx)
	{
		int result = -1;
		
		if (0 < webb_idx) {
			result = WebbVariableStarsEntry.find(webb_idx, elts);
		}
		
		return result;
	}

	public WebbVariableStarsEntry find(int webb_idx)
	{
		WebbVariableStarsEntry result = null;
		int idx = find_idx(webb_idx);

		if (0 <= idx && idx < elts.length) {
			result = elts[idx];
		}
		
		return result;
	}
}
