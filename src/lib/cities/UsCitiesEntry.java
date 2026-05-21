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

import java.io.IOException;

import lib.util.Queue;

public class UsCitiesEntry implements Comparable<UsCitiesEntry> {
	// city,city_ascii,state_id,state_name,county_fips,county_name,lat,lng,population,density,source,military,incorporated,timezone,ranking,zips,id
	// Aaronsburg,Aaronsburg,PA,Pennsylvania,42027,Centre,40.9042,-77.4513,706,307.9,shape,FALSE,FALSE,America/New_York,3,16820,1840005152
	public final String   city_utf8;			// 
	public final String   city_ascii;			// 
	public final String   state_id;				// 
	public final String   state_name;			// 
	public final long     county_fips;			// 
	public final String   county_name;			// 
	public final double   latitude;				// 
	public final double   longitude;			// 
	public final double   population;			// 
	public final double   density;				// 
	public final String   source;				// 
	public final boolean  military;				// 
	public final boolean  incorporated;			// 
	public final String   timezone;				// 
	public final long     ranking;				// 
	public final String[] zips;					// 
	public final long     id;					// 

	public UsCitiesEntry(String str) 
	{
		if (str != null) {
			String[] field = split(str);
			for (int i=0; i < field.length; i++) {
				field[i] = field[i].replaceAll("[\"]", "").trim();
			}

			city_utf8    = field[0];
			city_ascii   = field[1];
			state_id     = field[2];
			state_name   = field[3];
			county_fips  = Long.parseLong(field[4]);
			county_name  = field[5];
			latitude     = Double.parseDouble(field[6]);
			longitude    = Double.parseDouble(field[7]);
			population   = Double.parseDouble(field[8]);
			density      = Double.parseDouble(field[9]);
			source       = field[10];
			military     = field[11].equalsIgnoreCase("true");
			incorporated = field[12].equalsIgnoreCase("true");
			timezone     = field[13];
			ranking      = Long.parseLong(field[14]);
			zips         = field[15].split("[ ]");
			id           = Long.parseLong(field[16]);
		} else {
			city_utf8    = null;
			city_ascii   = null;
			state_id     = null;
			state_name   = null;
			county_fips  = Long.MIN_VALUE;
			county_name  = null;
			latitude     = Double.NaN;
			longitude    = Double.NaN;
			population   = Double.NaN;
			density      = Double.NaN;
			source       = null;
			military     = false;
			incorporated = false;
			timezone     = null;
			ranking      = Long.MIN_VALUE;
			zips         = null;
			id           = Long.MIN_VALUE;
		}
	}
	
	private static final int BEGIN  = 0;
	private static final int STRING = 1;
	private static final int NOSTR  = 2;
	private static String[] split(String str)
	{
		Queue<String> queue = new Queue<String>();
		int start = 0;
		int len   = str.length();
		int state = BEGIN;
		for (int i=0; i < len; i++) {
			if (state == BEGIN && str.charAt(i) == '"') {
				start += 1;
				state  = STRING;
			} else if (state == BEGIN && str.charAt(i) == ',') {
				queue.append("");
				start = i + 1;
				state = BEGIN;
			} else if (state == BEGIN && i == (len-1)) {
				queue.append(str.substring(start, i+1));
			} else if (state == BEGIN) {
				state = NOSTR;
				start = i;
			} else if (state == STRING && str.charAt(i) == '"' && i == (len-2)) {
				queue.append(str.substring(start, i));
				queue.append("");
				i += 1;
				start = i + 1;
				state = BEGIN;
			} else if (state == STRING && str.charAt(i) == '"') {
				queue.append(str.substring(start, i));
				i += 1;
				start = i + 1;
				state = BEGIN;
			} else if (state == STRING) {
				state  = STRING;
			} else if (state == NOSTR && str.charAt(i) == ',' && i == (len-1)) {
				queue.append(str.substring(start, i));
				queue.append("");
				start = i + 1;
				state = BEGIN;
			} else if (state == NOSTR && str.charAt(i) == ',') {
				queue.append(str.substring(start, i));
				start = i + 1;
				state = BEGIN;
			} else if (state == NOSTR && i == (len-1)) {
				queue.append(str.substring(start, i+1));
				start = i + 1;
				state = BEGIN;
			} else {
			}
		}
		
		int size = queue.size();
		String[] res = new String[size];
		for (int i=0; i < size && 0 < queue.length(); i++) {
			res[i] = queue.remove();
		}

		return res;
	}

	@Override public int compareTo(UsCitiesEntry rhs)
	{
		for (int i=0; i < city_ascii.length() && i < rhs.city_ascii.length(); i++) {
			if (city_ascii.charAt(i) < rhs.city_ascii.charAt(i)) {
				return -1;
			} else if (rhs.city_ascii.charAt(i) < city_ascii.charAt(i)) {
				return +1;
			}
		}

		if (city_ascii.length() < rhs.city_ascii.length()) {
			return -1;
		} else if (rhs.city_ascii.length() < city_ascii.length()) {
			return +1;
		}

		for (int i=0; i < state_name.length() && i < rhs.state_name.length(); i++) {
			if (state_name.charAt(i) < rhs.state_name.charAt(i)) {
				return -1;
			} else if (rhs.state_name.charAt(i) < state_name.charAt(i)) {
				return +1;
			}
		}

		if (state_name.length() < rhs.state_name.length()) {
			return -1;
		} else if (rhs.state_name.length() < state_name.length()) {
			return +1;
		}

		return 0;
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, UsCitiesEntry[] list)
	{
		return find(name, list, 0, list.length);
	}

	private static int find(String name, UsCitiesEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = list[middle].city_ascii.compareTo(name);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(name, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(name, list, lower, middle);
		}
		
		return result;
	}

	@Override public String toString()
	{
		return String.format("%s, %s (%s), %f, %f, %s, %s, %s", city_ascii, state_name, state_id, latitude, longitude, military, incorporated, timezone);
	}

	public static void main(String[] args) throws IOException 
	{
		// String home_dir  = "C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/night sky/";
		String c1s = "\"Indian, Island\",\"Indian, Island\",PA,Pennsylvania,42027,Centre,40.9042,-77.4513,706,307.9,shape,FALSE,FALSE,America/New_York,3,16820,1840005152";
		UsCitiesEntry c1 = new UsCitiesEntry(c1s);
		System.out.printf("%s%n", c1);

		String[] sa = split("\"a,b\",\"b\",");
		System.out.printf("sa.len = %d%n", sa.length);
		for (String s: sa) {
			System.out.printf("s = '%s'%n", s);
		}
	}
}
