package lib.cities;

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
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Hashtable;

import lib.util.Queue;

public class UsCitiesCatalog {
	
	public final String          catalog;
	public final File            file;
	public final UsCitiesEntry[] elts;

	public final Hashtable<String,UsCitiesEntry> zips = new Hashtable<String,UsCitiesEntry>();

	public UsCitiesCatalog(String c) throws IOException 
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
		
		// skip the first line
		int line_start = 0;
		while (line_start < len && buf[line_start] != '\n' && buf[line_start] != '\r') {
			line_start++;
		}
		while (line_start < len && (buf[line_start] == '\n' || buf[line_start] == '\r')) {
			line_start++;
		}

		// process lines 2 through the end of the file
		Queue<UsCitiesEntry> all = new Queue<UsCitiesEntry>();
		while (line_start < len) {
			// find the next line
			int line_len = 0;
			for ( ; line_start+line_len < len; line_len++) {
				if (buf[line_start+line_len] == '\n' || buf[line_start+line_len] == '\r') {
					break;
				}
			}

			String line = (new String(buf, line_start, line_len));
			if (line.charAt(0) != '#' && ! line.equals("")) {
				UsCitiesEntry ent = new UsCitiesEntry(line);
				all.append(ent);
			}

			// advance to the start of the next line
			for (line_start+=line_len; line_start < len && (buf[line_start] == '\n' || buf[line_start] == '\r'); line_start++) {
				;
			}
		}

		// city,city_ascii,state_id,state_name,county_fips,county_name,lat,lng,population,density,source,military,incorporated,timezone,ranking,zips,id
		// GNTO,GNTO,NM,New Mexico,99999,Valencia,34.522221,-106.8481616,0,0.0,shape,FALSE,FALSE,America/Denver,9,87006,9999999999
		// General Nathan Twining Observatory,General Nathan Twining Observatory,NM,New Mexico,99999,Valencia,34.522221,-106.8481616,0,0.0,shape,FALSE,FALSE,America/Denver,9,87006,9999999999
		all.append(new UsCitiesEntry("GNTO,GNTO,NM,New Mexico,99999,Valencia,34.522221,-106.8481616,0,0.0,shape,FALSE,FALSE,America/Denver,9,87006,9999999999"));
		all.append(new UsCitiesEntry("General Nathan Twining Observatory,General Nathan Twining Observatory,NM,New Mexico,99999,Valencia,34.522221,-106.8481616,0,0.0,shape,FALSE,FALSE,America/Denver,9,87006,9999999999"));
		all.append(new UsCitiesEntry("White Ridge,White Ridge,NM,New Mexico,99999,Sandoval,35.4985115,-106.8412982,0,0.0,shape,FALSE,FALSE,America/Denver,9,87053,9999999999"));
		all.append(new UsCitiesEntry("Cosmic Campground,Cosmic Campground,NM,New Mexico,99999,Catron,33.4792463100206, -108.92268895185092,0,0.0,shape,FALSE,FALSE,America/Denver,9,88039,9999999999"));
		all.append(new UsCitiesEntry("El Malpais,El Malpais,NM,New Mexico,99999,Cibola,34.8725786,-107.8901718,0,0.0,shape,FALSE,FALSE,America/Denver,9,87020,9999999999"));
		all.append(new UsCitiesEntry("La Ventana,La Ventana,NM,New Mexico,99999,Cibola,34.8725786,-107.8901718,0,0.0,shape,FALSE,FALSE,America/Denver,9,87020,9999999999"));
		all.append(new UsCitiesEntry("El Calderon,El Calderon,NM,New Mexico,99999,Cibola,34.9698293,-108.0079719,0,0.0,shape,FALSE,FALSE,America/Denver,9,87020,9999999999"));
		all.append(new UsCitiesEntry("Point of the Mountain Overlook,Point of the Mountain Overlook,AZ,Arizona,99999,Apache,34.0481717,-109.3574849,0,0.0,shape,FALSE,FALSE,America/Denver,9,85925,9999999999"));
		all.append(new UsCitiesEntry("Greens Peak,Greens Peak,AZ,Arizona,99999,Apache,34.1119038,-109.5747167,0,0.0,shape,FALSE,FALSE,America/Denver,9,85925,9999999999"));
		all.append(new UsCitiesEntry("City of Rocks,City of Rocks,NM,New Mexico,99999,Grant,32.5914741,-107.9745457,0,0.0,shape,FALSE,FALSE,America/Denver,9,88034,9999999999"));
		all.append(new UsCitiesEntry("Onizuka Center for International Astronomy,Onizuka Center for International Astronomy,HI,Hawaii,99999,Hawaii,19.7614445,-155.4561919,0,0.0,shape,FALSE,FALSE,Pacific/Honolulu,9,96720,9999999999"));
		all.append(new UsCitiesEntry("Mauna Kea,Mauna Kea,HI,Hawaii,99999,Hawaii,19.8249988,-155.4747391,0,0.0,shape,FALSE,FALSE,Pacific/Honolulu,9,96720,9999999999"));

		elts = new UsCitiesEntry[all.length()];
		for (int i=0; i < elts.length; i++) {
			elts[i] = all.remove();
		}
		Arrays.sort(elts);

		for (UsCitiesEntry elt: elts) {
			for (String zip: elt.zips) {
				zips.put(zip, elt);
			}
		}
	}

	public UsCitiesEntry find_zip(String zip)
	{
		return zips.get(zip);
	}

	public UsCitiesEntry find_first(String city, String state)
	{
		city  = city.trim().toLowerCase();
		state = state.trim().toLowerCase();
		if (state == null || state.equals("")) {
			// ignore the state
			for (UsCitiesEntry elt: elts) {
				if (elt.city_ascii.toLowerCase().startsWith(city)) {
					return elt;
				}
			}
		} else {
			if (state.length() == 2) {
				// we have a postal code for the state name
				for (UsCitiesEntry elt: elts) {
					if (elt.city_ascii.toLowerCase().startsWith(city) && elt.state_id.toLowerCase().equalsIgnoreCase(state)) {
						return elt;
					}
				}
			}

			// we have a state name
			for (UsCitiesEntry elt: elts) {
				if (elt.city_ascii.toLowerCase().startsWith(city) && elt.state_name.toLowerCase().startsWith(state)) {
					return elt;
				}
			}
		}
		
		return null;
	}

	public int find_idx(String name)
	{
		int result = -1;
		
		if (name != null) {
			result = UsCitiesEntry.find(name, elts);
		}
		
		return result;
	}

	public UsCitiesEntry find(String name)
	{
		UsCitiesEntry result = null;
		int idx = find_idx(name);

		if (0 <= idx && idx < elts.length) {
			result = elts[idx];
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException 
	{
		String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String her_name  = home_dir + "uscities.csv";
		UsCitiesCatalog m = new UsCitiesCatalog(her_name);
		for (UsCitiesEntry city : m.elts) {
			System.out.printf("%s%n", city.toString());
		}
	}
}
