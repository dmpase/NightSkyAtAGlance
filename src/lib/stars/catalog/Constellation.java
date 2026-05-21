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

public class Constellation {
	
	public final File file;
	public final ConstellationEntry[] constellation;

	public Constellation(String path) throws IOException 
	{
		file = new File(path);
		byte[] buf = new byte[(int)file.length()];
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.read(buf);
		raf.close();

		Queue<ConstellationEntry> queue = new Queue<ConstellationEntry>();
		for (int first=0; first < file.length(); ) {
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
			if (first == 0) {
				first = last;
				continue;
			}
			
			String str = new String(buf,first,(end - first));
			ConstellationEntry ce = new ConstellationEntry(str);
			queue.append(ce);
			first = last;
		}

		constellation = new ConstellationEntry[queue.length()];
		for (int i=0; i < constellation.length; i++) {
			constellation[i] = queue.remove();
		}
	}

	public Constellation(File c) throws IOException 
	{
		file = c;
		byte[] buf = new byte[(int)file.length()];
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.read(buf);
		raf.close();

		Queue<ConstellationEntry> queue = new Queue<ConstellationEntry>();
		for (int first=0; first < file.length(); ) {
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
			if (first == 0) {
				first = last;
				continue;
			}
			
			String str = new String(buf,first,(end - first));
			ConstellationEntry ce = new ConstellationEntry(str);
			queue.append(ce);
			first = last;
		}

		constellation = new ConstellationEntry[queue.length()];
		for (int i=0; i < constellation.length; i++) {
			constellation[i] = queue.remove();
		}
		Arrays.sort(constellation);
	}

	public ConstellationEntry find_by_name(String name)
	{
		for (ConstellationEntry e: constellation) {
			if (name.equalsIgnoreCase(e.name)) {
				return e;
			}
		}

		return null;
	}

	public ConstellationEntry find_by_genetive(String genetive)
	{
		for (ConstellationEntry e: constellation) {
			if (genetive.equalsIgnoreCase(e.genetive)) {
				return e;
			}
		}

		return null;
	}

	public ConstellationEntry find_by_tld(String tld)
	{
		for (ConstellationEntry e: constellation) {
			if (tld.equalsIgnoreCase(e.tld)) {
				return e;
			}
		}

		return null;
	}

	public ConstellationEntry find_by_fld(String fld)
	{
		for (ConstellationEntry e: constellation) {
			if (fld.equalsIgnoreCase(e.fld)) {
				return e;
			}
		}

		return null;
	}

	public ConstellationEntry find(String str)
	{
		for (ConstellationEntry e: constellation) {
			if (str.equalsIgnoreCase(e.name) || str.equalsIgnoreCase(e.genetive) || str.equalsIgnoreCase(e.tld) || str.equalsIgnoreCase(e.fld)) {
				return e;
			}
		}

		return null;
	}

	public String get_tldr(String str)
	{
		for (ConstellationEntry e: constellation) {
			if (str.equalsIgnoreCase(e.name) || str.equalsIgnoreCase(e.genetive) || str.equalsIgnoreCase(e.tld) || str.equalsIgnoreCase(e.fld)) {
				return e.tldr;
			}
		}

		return null;
	}

	public static void main(String[] args) throws IOException
	{
		String c = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/Constellations/Constellations.csv";
		Constellation cons = new Constellation(c);

		for (ConstellationEntry ce: cons.constellation) {
			System.out.println(ce);
		}

		System.out.println(cons.find_by_name("Boötes"));
		System.out.println(cons.find_by_genetive("Bootis"));
		System.out.println(cons.find_by_tld("Vol"));
		System.out.println(cons.find_by_fld("Caml"));

		System.out.println(cons.find("Boötes"));
		System.out.println(cons.find("Bootis"));
		System.out.println(cons.find("Vol"));
		System.out.println(cons.find("Caml"));
	}
}
