package lib.astro;

/*******************************************************************************
 * Copyright (c) 2025 Douglas M. Pase                                          *
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
import java.util.Enumeration;
import java.util.Hashtable;

import lib.util.Queue;

public class CameraCatalog {
	
	public CameraEntry[] elts;
	public Hashtable<String,CameraEntry> table = new Hashtable<String,CameraEntry>();

	public CameraCatalog(String c) throws IOException 
	{
		File file    = new File(c);
		
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
		Queue<CameraEntry> all = new Queue<CameraEntry>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = (new String(buf, line_start, line_len)).trim();
			if (line.charAt(0) != '#' && ! line.equals("")) {
				CameraEntry ent = new CameraEntry(line);
				all.append(ent);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && (buf[line_start] == '\n' || buf[line_start] == '\r'); line_start++) {
				;
			}
		}

		elts = new CameraEntry[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
			table.put(elts[i].name.toLowerCase(), elts[i]);
		}
	}

	public CameraCatalog(CameraEntry[] e)
	{
		elts = e;
		for (CameraEntry elt: e) {
			table.put(elt.name.toLowerCase(), elt);
		}
	}

	// cat0 is the local catalog with modified entries, cat1 is the built-in catalog with new equipment
	// if a cat0 entry is modified, the modified entry is retained (cat1 entries are never modified)
	// if a cat0 entry is NOT modified and an updated entry appears in cat1, the cat1 entry is retained
	// an entry in cat0 that is not in cat1 (added by the user) is retained
	// an entry in cat1 that is not in cat0 (added to the master list) is retained
	public static CameraCatalog merge(CameraCatalog cat0, CameraCatalog cat1) 
	{
		Hashtable<String,CameraEntry> all = new Hashtable<String,CameraEntry>();

		if (cat0 != null) {
			for (CameraEntry elt: cat0.elts) {
				all.put(elt.name, elt);
			}
		}

		// modified overrides non-modified (members of the original catalog)
		// cat1 overrides cat0 if cat0 is NOT modified or cat1 IS modified
		if (cat1 != null) {
			for (CameraEntry elt1: cat1.elts) {
				CameraEntry elt0 = all.get(elt1.name);
				if (elt0 == null || ! elt0.editable || elt1.editable) {
					all.put(elt1.name, elt1);
				}
			}
		}

		CameraEntry[] elts = new CameraEntry[all.size()];
		Enumeration<String> keys = all.keys();
		for (int i=0; i < elts.length; i++) {
			String key = keys.nextElement();
			elts[i] = all.get(key);
		}
		Arrays.sort(elts);

		return new CameraCatalog(elts);
	}

	public static CameraCatalog merge(CameraCatalog cat, CameraEntry elt)
	{
		Hashtable<String,CameraEntry> all = new Hashtable<String,CameraEntry>();

		if (cat != null) {
			for (CameraEntry e: cat.elts) {
				all.put(e.name.strip().toLowerCase(), e);
			}
		}

		// modified overrides non-modified (members of the original catalog)
		// cat1 overrides cat0 if cat0 is NOT modified or cat1 IS modified
		if (elt != null) {
			CameraEntry elt0 = all.get(elt.name.strip().toLowerCase());
				if (elt0 == null || ! elt0.editable || elt.editable) {
					all.put(elt.name.strip().toLowerCase(), elt);
				}
		}

		CameraEntry[] elts = new CameraEntry[all.size()];
		Enumeration<String> keys = all.keys();
		for (int i=0; i < elts.length; i++) {
			String key = keys.nextElement();
			elts[i] = all.get(key);
		}
		Arrays.sort(elts);

		return new CameraCatalog(elts);
	}

	public void remove(String name)
	{
		if (name != null) {
			name = name.strip().toLowerCase();
			if (! name.equals("")) {
				table.remove(name);
				elts = new CameraEntry[table.size()];
				Enumeration<String> keys = table.keys();
				for (int i=0; keys.hasMoreElements() && i < elts.length; i++) {
					String key = keys.nextElement();
					elts[i] = table.get(key);
				}
				Arrays.sort(elts);
			}
		}
	}
}
