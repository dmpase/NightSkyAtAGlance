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

import lib.util.Queue;

public class WorldCitiesEntry implements Comparable<WorldCitiesEntry> {
	// "city","city_ascii","lat","lng","country","iso2","iso3","admin_name","capital","population","id"
	// "Tokyo","Tokyo","35.6870","139.7495","Japan","JP","JPN","Tokyo","primary","37785000","1392685764"
	public final String   city_utf8;			// 
	public final String   city_ascii;			// 
	public final double   latitude;				// 
	public final double   longitude;			// 
	public final String   country;				// 
	public final String   iso2;					// 
	public final String   iso3;					// 
	public final String   admin_name;			// 
	public final String   capital;				// 
	public final double   population;			// 
	public final long     id;					// 

	public WorldCitiesEntry(String str) 
	{
		if (str != null) {
			String[] field = split(str);
			for (int i=0; i < field.length; i++) {
				field[i] = field[i].replaceAll("[\"]", "").trim();
			}

			city_utf8    = field[0];
			city_ascii   = field[1];
			latitude     = Double.parseDouble(field[2]);
			longitude    = Double.parseDouble(field[3]);
			country      = field[4];
			iso2         = field[5];
			iso3         = field[6];
			admin_name   = field[7];
			capital      = field[8];
			population   = (field[9] == null || field[9].equals("")) ? 0 : Double.parseDouble(field[9]);
			id           = Long.parseLong(field[10]);
		} else {
			city_utf8    = null;
			city_ascii   = null;
			latitude     = Double.NaN;
			longitude    = Double.NaN;
			country      = null;
			iso2         = null;
			iso3         = null;
			admin_name   = null;
			capital      = null;
			population   = Double.NaN;
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

	@Override public int compareTo(WorldCitiesEntry rhs)
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

		return 0;
	}
	
	// returns the index of the searched-for item
	// returning the index allows searches for nearby items
	public static int find(String name, WorldCitiesEntry[] list)
	{
		return find(name, list, 0, list.length);
	}

	private static int find(String name, WorldCitiesEntry[] list, int lower, int upper)
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
	
	public String time_zone()
	{
		int offset = (int) ((longitude + 7.5)/15);
		return String.format("GMT%c%d", ((offset < 0) ? '-' : '+'), Math.abs(offset));
	}

	@Override public String toString()
	{
		return String.format("%s, %f, %f, %s (%s/%s)", city_ascii, latitude, longitude, country, iso2, iso3);
	}
}
