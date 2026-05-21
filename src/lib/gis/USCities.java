package lib.gis;

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
import java.io.IOException;
import java.io.RandomAccessFile;

import lib.util.Queue;

public class USCities {

	// database is located at //magrathea/dsk/dmpase/home/projects/org.hypercomputing/data/gis/uscities.csv
	// city	city_ascii	state_id	state_name	county_fips	county_name	lat	lng	population	density	source	military	incorporated	timezone	ranking	zips	id

	public USCities(String db_name) throws IOException 
	{
		File db_file = new File(db_name);
		byte[] buf = new byte[(int) db_file.length()];
		RandomAccessFile raf = new RandomAccessFile(db_file, "r");
		raf.read(buf);
		raf.close();
		Queue<Entry> queue = new Queue<Entry>();
		int i = 0;
		while (i < buf.length) {
			int len = 0;
			for ( ; i+len < buf.length && buf[i+len] != '\n'; len++) {
				;
			}
			String line = new String(buf, i, len-1).trim();
			System.out.printf("%4d: %4d: %s%n", i,len,line);
			if (i == 0) {
				i += len+1;
				continue;
			} else {
				i += len+1;
			}
			queue.append(new Entry(line));
			// if (2200 < i) break;
		}
	}
	
	public String find(String city, String state)
	{
		return null;
	}
	
	public static class Entry {
		public final String   city;
		public final String   city_ascii;
		public final String   state_id;
		public final String   state_name;
		public final String   county_fips;
		public final String   county_name;
		public final double   latitude;
		public final double   longitude;
		public final double   population;
		public final double   density;
		public final String   source;
		public final boolean  military;
		public final boolean  incorporated;
		public final String   time_zone;
		public final String   ranking;
		public final String[] zips;
		public final String   id;
		
		enum State { 
			START,		// before '"'       in "...","..."	START   + '"' -> INSIDE
			INSIDE, 	// after first '"' 	in "...","..."	INSIDE  + '"' -> OUTSIDE
			OUTSIDE, 	// after second '"'	in "...","..."	OUTSIDE + ',' -> START
		};

		public Entry(String line)
		{
			Queue<String> queue = new Queue<String>();
			State state = State.START;
			int field_start = 0;
			for (int i=0; i < line.length(); i++) {
				if (state == State.START && line.charAt(i) == '"') {
					state = State.INSIDE;
					field_start = i + 1;
				} else if (state == State.START) {
					;
				} else if (state == State.INSIDE && line.charAt(i) == '"') {
					String field = line.substring(field_start, i).trim();
					queue.append(field);
					state = State.OUTSIDE;
				} else if (state == State.INSIDE) {
					;
				} else if (state == State.OUTSIDE && line.charAt(i) == ',') {
					state = State.START;
				}
			}
			
			String[] field = new String[queue.length()];
			for (int i=0; i < field.length; i++) {
				field[i] = queue.remove();
			}

			this.city         = field[0];
			this.city_ascii   = field[1];
			this.state_id     = field[2];
			this.state_name   = field[3];
			this.county_fips  = field[4];
			this.county_name  = field[5];
			this.latitude     = Double.parseDouble(field[6]);
			this.longitude    = Double.parseDouble(field[7]);
			this.population   = Double.parseDouble(field[8]);
			this.density      = Double.parseDouble(field[9]);
			this.source       = field[10];
			this.military     = field[11].equals("TRUE");
			this.incorporated = field[12].equals("TRUE");
			this.time_zone    = field[13];
			this.ranking      = field[14];
			this.zips         = field[15].split("[ ]");
			this.id           = field[16];
		}
	}

	public static void main(String[] args) throws IOException 
	{
		USCities db = new USCities("//magrathea/dsk/dmpase/home/projects/org.hypercomputing/data/gis/uscities.csv");
		System.out.printf("%n", db.find("Albuquerque", "NM"));
	}
}
