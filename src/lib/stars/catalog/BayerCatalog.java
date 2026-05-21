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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import lib.util.Queue;

public class BayerCatalog {
	
	public final String       catalog;
	public final File         file;
	public final BayerEntry[] bayer;

	public BayerCatalog(String c) throws IOException 
	{
		catalog = c;
		file = new File(c);

		byte[] buf = new byte[(int)file.length()];
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.read(buf);
		raf.close();

		Queue<BayerEntry> queue = new Queue<BayerEntry>();
		int first = 0;
		if ((buf[0]&0xff) == 0xef && (buf[1]&0xff) == 0xbb && (buf[2]&0xff) == 0xbf) {
			first = 3;
		}
		for (; first < buf.length; ) {
			int last=0, end=0;
			for (last=first; last < buf.length; last++) {
				if (buf[last] == '\n' || buf[last] == '\r') {
					end = last;
					while (last < buf.length && (buf[last] == '\n' || buf[last] == '\r')) {
						last += 1;
					}
					break;
				}
			}
			if (buf[first] == '#') {
				first = last;
				continue;
			}
			
			BayerEntry ent = new BayerEntry(buf, first, (end-first));
			queue.append(ent);
			first = last;
		}

		bayer = new BayerEntry[queue.length()];
		for (int i=0; i < bayer.length; i++) {
			bayer[i] = queue.remove();
		}
		Arrays.sort(bayer);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException 
	{
		String c = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/IAU/BayerCatalog.csv";
		BayerCatalog bc = new BayerCatalog(c);
	}
}
