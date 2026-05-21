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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import lib.util.Queue;

public class SimbadCatalog {

	public final File file;
	public final SimbadEntry[] entry;

	public SimbadCatalog(File c) throws IOException 
	{
		// check for a valid file that exists
		if (c == null) {
			throw new FileNotFoundException();
		} else if (! c.isFile()) {
			throw new FileNotFoundException(c.getCanonicalPath());
		}
		
		file = c;

		// open, read, and close the file
		byte[] buf = new byte[(int) file.length()];
		RandomAccessFile catalog = new RandomAccessFile(file, "r");
		catalog.read(buf);
		catalog.close();
		
		// break up the file into lines
		Queue<String> lines = new Queue<String>();
		int start = 0;
		for (int i=0; i < buf.length; i++) {
			if (buf[i] == '\n' || buf[i] == '\r') {
				if (start == i) {
					start += 1;
				} else {
					byte[] line = new byte[i - start];
					for (int j=0; j < line.length; j++) {
						line[j] = buf[start + j];
					}
					start = i + 1;

					String str = new String(line).trim();
					if (str.startsWith("#") || str.startsWith("main_id") || str.startsWith("-----------------") || buf.length < 1) {
						;
					} else {
						lines.append(str);
					}
				}
			}
		}

		System.out.println(lines.length());
		entry = new SimbadEntry[ lines.length() ];
		for (int i=0; i < entry.length; i++) {
			entry[i] = new SimbadEntry(lines.remove());
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException 
	{
		String path = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/Vizier/simbad.v1.txt";
		File file = new File(path);
		SimbadCatalog simbad = new SimbadCatalog(file);
	}
}
