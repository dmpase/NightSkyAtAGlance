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
import java.util.Arrays;

import lib.sphere.Angle;
import lib.sphere.Location;
import lib.sphere.Sphere;

public class YaleBrightStarBinCatalog {
	
	public final File file;
	public final YaleBrightStarHeader  header;
	public final YaleBrightStarBinEntry[] entry;
	public final SortByRA[]     ra;
	public final SortByDec[]    dec;
	public final SortByMag[]    mag;

	public YaleBrightStarBinCatalog(String c) throws IOException
	{
		// check for a valid file name
		if (c == null || c.equals("")) {
			throw new FileNotFoundException();
		}

		// check that the file exists
		file = new File(c);
		if (file == null) {
			throw new FileNotFoundException();
		} else if (! file.isFile()) {
			throw new FileNotFoundException(c);
		}

		// open the file, read the header, allocate space for the entries
		RandomAccessFile catalog = new RandomAccessFile(file, "r");
		header  = new YaleBrightStarHeader(catalog);
		entry   = new YaleBrightStarBinEntry[header.STARN];
		ra      = new SortByRA[header.STARN];
		dec     = new SortByDec[header.STARN];
		mag     = new SortByMag[header.STARN];
		
		// read and sort the entries
		for (int i=0; i < entry.length; i++) {
			entry[i] = new YaleBrightStarBinEntry(catalog, header);
			ra   [i] = new SortByRA(entry[i]);
			dec  [i] = new SortByDec(entry[i]);
			mag  [i] = new SortByMag(entry[i]);
		}
		Arrays.sort(entry);
		Arrays.sort(ra);
		Arrays.sort(dec);
		Arrays.sort(mag);

		catalog.close();
	}

	public YaleBrightStarBinCatalog(File c) throws IOException
	{
		// check for a valid file that exists
		if (c == null) {
			throw new FileNotFoundException();
		} else if (! c.isFile()) {
			throw new FileNotFoundException(c.getCanonicalPath());
		}

		// open the file, read the header, allocate space for the entries
		file    = c;
		RandomAccessFile catalog = new RandomAccessFile(file, "r");
		header  = new YaleBrightStarHeader(catalog);
		entry   = new YaleBrightStarBinEntry[header.STARN];
		ra      = new SortByRA[header.STARN];
		dec     = new SortByDec[header.STARN];
		mag     = new SortByMag[header.STARN];
		
		// read and sort the entries
		for (int i=0; i < entry.length; i++) {
			entry[i] = new YaleBrightStarBinEntry(catalog, header);
			ra   [i] = new SortByRA(entry[i]);
			dec  [i] = new SortByDec(entry[i]);
			mag  [i] = new SortByMag(entry[i]);
		}
		Arrays.sort(entry);
		Arrays.sort(ra);
		Arrays.sort(dec);
		Arrays.sort(mag);

		catalog.close();
	}
	
	public YaleBrightStarBinEntry find(String name)
	{
		YaleBrightStarBinEntry result = null;
		
		int idx = YaleBrightStarBinEntry.find(name, entry);
		if (0 <= idx && idx < entry.length) {
			result = entry[idx];
		}

		return result;
	}

	// search for the star with the specified catalog index
	public YaleBrightStarBinEntry search_by_index(int index)
	{
		YaleBrightStarBinEntry result = null;
		
		if (0 < index && index <= entry.length) {
			result = entry[index-1];
		}

		return result;
	}

	// search for the star nearest to the specified right ascension (h,m,s)
	public YaleBrightStarBinEntry search_by_ra(double hours, double minutes, double seconds)
	{
		double radians = Angle.hms_to_rad(hours, minutes, seconds);
		YaleBrightStarBinEntry result = search_by_ra(radians);

		return result;
	}

	// search for the star nearest to the specified right ascension (radians)
	public YaleBrightStarBinEntry search_by_ra(double radians)
	{
		YaleBrightStarBinEntry result = null;

		// normalize the R.A. to 0 <= R.A. < 2*PI
		radians = norm_two_pi(radians);

		// binary search for the star with the nearest R.A.
		int min_idx = 0;
		int max_idx = ra.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (ra[mid_idx].data.SRA0 < radians) {
				min_idx = mid_idx;
			} else if (radians < ra[mid_idx].data.SRA0) {
				max_idx = mid_idx;
			} else {
				result = ra[mid_idx].data;
				break;
			}

			if (Math.abs(radians - ra[min_idx].data.SRA0) < Math.abs(radians - ra[max_idx-1].data.SRA0)) {
				result = ra[min_idx].data;
			} else {
				result = ra[max_idx-1].data;
			}
		}

		return result;
	}
	
	// normalize the R.A. to 0 <= R.A. < 2*PI
	public static double norm_two_pi(double radians)
	{
		if (radians < 0) {
			radians -= 2*Math.PI * (int)(radians/(2*Math.PI) - 1);
		} else if (2*Math.PI <= radians) {
			radians -= 2*Math.PI * (int)(radians/(2*Math.PI));
		}
		
		if (radians == 2*Math.PI) {
			radians = 0;
		}

		return radians;
	}

	// search for the star with the nearest specified de_B1950 (d,m,s)
	public YaleBrightStarBinEntry search_by_dec(double degrees, double minutes, double seconds)
	{
		double radians = Angle.dms_to_rad(degrees, minutes, seconds);
		YaleBrightStarBinEntry result = search_by_dec(radians);

		return result;
	}

	// search for the star with the nearest specified de_B1950 (radians)
	public YaleBrightStarBinEntry search_by_dec(double radians)
	{
		YaleBrightStarBinEntry result = null;

		// normalize the de_B1950 to -PI/2 <= dec <= +PI/2
		radians = norm_half_pi(radians);

		// binary search for the star with the nearest de_B1950
		int min_idx = 0;
		int max_idx = dec.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (radians < dec[mid_idx].data.SDEC0) {
				min_idx = mid_idx;
			} else if (dec[mid_idx].data.SDEC0 < radians) {
				max_idx = mid_idx;
			} else {
				result = dec[mid_idx].data;
				break;
			}

			if (Math.abs(radians - dec[min_idx].data.SDEC0) < Math.abs(radians - dec[max_idx-1].data.SDEC0)) {
				result = dec[min_idx].data;
			} else {
				result = dec[max_idx-1].data;
			}
		}

		return result;
	}
	
	// normalize the de_B1950 to -PI/2 <= dec <= +PI/2
	public static double norm_half_pi(double radians)
	{
		radians = norm_two_pi(radians);
		if (radians <= Math.PI/2) {
			;
		} else if (radians <= 3*Math.PI/2) {
			radians = Math.PI - radians;
		} else {
			radians = radians - 2*Math.PI;
		}

		return radians;
	}

	// search for the star nearest the coordinate (ra,dec)
	public YaleBrightStarBinEntry search_by_ra_dec(double ra, double dec)
	{
		// normalize the coordinates
		ra  = norm_two_pi(ra);
		dec = norm_half_pi(dec);

		// create a unit sphere and set the location of the coordinate 
		// as latitude and longitude on that sphere
		Sphere s = new Sphere(1.0);
		Angle lat0 = new Angle(dec, Angle.Scale.RADIANS);
		Angle lon0 = new Angle(ra,  Angle.Scale.RADIANS);
		Location loc0 = new Location(lat0, lon0);

		// pick a (probably bad) first guess
		YaleBrightStarBinEntry result = entry[0];

		// find the distance using the Haversine equation
		Angle lat1 = new Angle(result.SDEC0, Angle.Scale.RADIANS);
		Angle lon1 = new Angle(result.SRA0,  Angle.Scale.RADIANS);
		Location loc1 = new Location(lat1, lon1);
		double d = s.great_circle_range(loc0, loc1);

		// search all other stars for a closer star to the coordinate
		for (int i=1; i < entry.length; i++) {
			// select another (probably better) guess
			YaleBrightStarBinEntry guess = entry[i];
			
			// find the distance using the Haversine equation
			Angle lat2 = new Angle(guess.SDEC0, Angle.Scale.RADIANS);
			Angle lon2 = new Angle(guess.SRA0,  Angle.Scale.RADIANS);
			Location loc2 = new Location(lat2, lon2);
			double c = s.great_circle_range(loc0, loc2);

			// if the new guess is better, make it the current best guess
			if (c < d) {
				result = guess;
				lat1 = lat2;
				lon1 = lon2;
				loc1 = loc2;
				d = c;
			}
		}

		// return the best guess that we found
		return result;
	}

	
	// search for the star nearest the coordinate (ra,dec)
	// this routine is faster, but not as accurate
	public YaleBrightStarBinEntry search_by_ra_dec2(double ra, double declination)
	{
		// normalize the coordinates
		ra  = norm_two_pi(ra);
		declination = norm_half_pi(declination);

		// create a unit sphere and set the location of the coordinate 
		// as latitude and longitude on that sphere
		Sphere s = new Sphere(1.0);
		Angle lat0 = new Angle(declination, Angle.Scale.RADIANS);
		Angle lon0 = new Angle(ra,  Angle.Scale.RADIANS);
		Location loc0 = new Location(lat0, lon0);

		// pick a (probably bad) first guess
		YaleBrightStarBinEntry result = search_by_dec(declination);
		int ctr = result.XN0 - 1;

		// find the distance using the Haversine equation
		Angle lat1 = new Angle(result.SDEC0, Angle.Scale.RADIANS);
		Angle lon1 = new Angle(result.SRA0,  Angle.Scale.RADIANS);
		Location loc1 = new Location(lat1, lon1);
		double d = s.great_circle_range(loc0, loc1);

		// search all other stars for a closer star to the coordinate
		for (int i=1; i < entry.length; i++) {
			// is our search over? (are we out of bounds or farther in dec than the current best distance, on both sides?)
			if ((ctr-i < 0 || d < Math.abs(dec[ctr].data.SDEC0 - dec[ctr-i].data.SDEC0)) && 
					(entry.length <= ctr+i || d < Math.abs(dec[ctr].data.SDEC0 - dec[ctr+i].data.SDEC0))) break;
			for (int j=-1; j < 2; j+=2) {
				int idx = ctr + i*j;
				if (idx < 0 || entry.length <= idx) continue;
				
				// select another (probably better) guess
				YaleBrightStarBinEntry guess = dec[idx].data;
				
				// find the distance using the Haversine equation
				Angle lat2 = new Angle(guess.SDEC0, Angle.Scale.RADIANS);
				Angle lon2 = new Angle(guess.SRA0,  Angle.Scale.RADIANS);
				Location loc2 = new Location(lat2, lon2);
				double c = s.great_circle_range(loc0, loc2);
	
				// if the new guess is better, make it the current best guess
				if (c < d) {
					result = guess;
					lat1 = lat2;
					lon1 = lon2;
					loc1 = loc2;
					d = c;
				}
			}
		}

		// return the best guess that we found
		return result;
	}
	
	public double range()
	{
		double result = 0;
		
		return result;
	}

	// search for the star nearest to the specified visual magnitude
	public YaleBrightStarBinEntry search_by_mag(double magnitude)
	{
		YaleBrightStarBinEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = mag.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (mag[mid_idx].data.MAG < magnitude) {
				min_idx = mid_idx;
			} else if (magnitude < mag[mid_idx].data.MAG) {
				max_idx = mid_idx;
			} else {
				result = mag[mid_idx].data;
				break;
			}

			if (Math.abs(magnitude - mag[min_idx].data.MAG) < Math.abs(magnitude - mag[max_idx-1].data.MAG)) {
				result = mag[min_idx].data;
			} else {
				result = mag[max_idx-1].data;
			}
		}

		return result;
	}

	public static class SortByRA implements Comparable<SortByRA> {
		
		public final YaleBrightStarBinEntry data;
	
		public SortByRA(YaleBrightStarBinEntry entry) 
		{
			data = entry;
		}
	
		@Override public String toString()
		{
			String str = data.toString();
			
			return str;
		}
	
		// sort smallest to largest
		@Override public int compareTo(SortByRA rhs) 
		{
			int result = 0;
			if (data.SRA0 < rhs.data.SRA0) {
				result = -1;
			} else if (rhs.data.SRA0 < data.SRA0) {
				result = +1;
			}
			return result;
		}
	}

	public static class SortByDec implements Comparable<SortByDec> {

		public final YaleBrightStarBinEntry data;
		
		public SortByDec(YaleBrightStarBinEntry entry) 
		{
			data = entry;
		}
		
		@Override public String toString()
		{
			String str = data.toString();
			
			return str;
		}

		// sort largest to smallest
		@Override public int compareTo(SortByDec rhs) 
		{
			int result = 0;
			if (data.SDEC0 < rhs.data.SDEC0) {
				result = +1;
			} else if (rhs.data.SDEC0 < data.SDEC0) {
				result = -1;
			}
			return result;
		}
	}

	public static class SortByMag implements Comparable<SortByMag> {
	
		public final YaleBrightStarBinEntry data;
		
		public SortByMag(YaleBrightStarBinEntry entry) 
		{
			data = entry;
		}
		
		@Override public String toString()
		{
			String str = data.toString();
			
			return str;
		}
	
		// sort smallest to largest
		@Override public int compareTo(SortByMag rhs) 
		{
			int result = 0;
			if (data.MAG < rhs.data.MAG) {
				result = -1;
			} else if (rhs.data.MAG < data.MAG) {
				result = +1;
			}
			return result;
		}
	}


	public static void main(String[] args) throws IOException 
	{
		String path = "//magrathea/dsk/dmpase/home/Astronomy/Catalogs/Yale Bright Star Catalog/BSC5";
		YaleBrightStarBinCatalog bsc = new YaleBrightStarBinCatalog(path);
		/*
		System.out.printf("%s%n", bsc.file.getCanonicalFile());
		System.out.printf("%s%n", bsc.header.toString());
		System.out.printf("%s%n", bsc.entry[0].toString());
		System.out.printf("%s%n", bsc.entry[bsc.entry.length-1].toString());
		System.out.printf("%s%n", bsc.ra[0].toString());
		System.out.printf("%s%n", bsc.ra[bsc.ra.length-1].toString());
		System.out.printf("%s%n", bsc.dec[0].toString());
		System.out.printf("%s%n", bsc.dec[bsc.dec.length-1].toString());
		System.out.println();
		System.out.printf("%s%n", bsc.search_by_ra(Math.PI/4).toString());
		System.out.printf("%s%n", bsc.search_by_dec(Math.PI/4).toString());
		System.out.printf("%s%n", bsc.search_by_ra_dec(Math.PI/4,Math.PI/4).toString());
		System.out.printf("%s%n", bsc.search_by_ra_dec(0,0).toString());
		System.out.printf("%s%n", bsc.search_by_ra_dec(0, Math.PI/2).toString());
		System.out.println();
		System.out.printf("%-20s %s%n", "Alpheratz",            bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  8, 23.25988), Angle.dms_to_rad( 29,  5, 25.5520)).toString());
		System.out.printf("%-20s %s%n", "Sirrah",               bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  8, 23.25988), Angle.dms_to_rad( 29,  5, 25.5520)).toString());
		System.out.printf("%-20s %s%n", "Sirah",                bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  8, 23.25988), Angle.dms_to_rad( 29,  5, 25.5520)).toString());

		System.out.printf("%-20s %s%n", "Mirach",               bsc.search_by_ra_dec(Angle.hms_to_rad( 1,  9, 43.92388), Angle.dms_to_rad( 35, 37, 14.0075)).toString());
		System.out.printf("%-20s %s%n", "Merach",               bsc.search_by_ra_dec(Angle.hms_to_rad( 1,  9, 43.92388), Angle.dms_to_rad( 35, 37, 14.0075)).toString());
		System.out.printf("%-20s %s%n", "Mirac",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1,  9, 43.92388), Angle.dms_to_rad( 35, 37, 14.0075)).toString());
		System.out.printf("%-20s %s%n", "Mizar",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1,  9, 43.92388), Angle.dms_to_rad( 35, 37, 14.0075)).toString());

		System.out.printf("%-20s %s%n", "Almach",               bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  3, 53.95229), Angle.dms_to_rad( 42, 19, 47.0223)).toString());
		System.out.printf("%-20s %s%n", "Almaach",              bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  3, 53.95229), Angle.dms_to_rad( 42, 19, 47.0223)).toString());
		System.out.printf("%-20s %s%n", "Almak",                bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  3, 53.95229), Angle.dms_to_rad( 42, 19, 47.0223)).toString());
		System.out.printf("%-20s %s%n", "Almaak",               bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  3, 53.95229), Angle.dms_to_rad( 42, 19, 47.0223)).toString());
		System.out.printf("%-20s %s%n", "Alamak",               bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  3, 53.95229), Angle.dms_to_rad( 42, 19, 47.0223)).toString());

		System.out.printf("%-20s %s%n", "Adhil",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 22, 20.42008), Angle.dms_to_rad( 45, 31, 43.5962)).toString());

		System.out.printf("%-20s %s%n", "Altair",               bsc.search_by_ra_dec(Angle.hms_to_rad(19, 50, 46.99855), Angle.dms_to_rad(  8, 52,  5.9563)).toString());

		System.out.printf("%-20s %s%n", "Alshain",              bsc.search_by_ra_dec(Angle.hms_to_rad(19, 55, 18.79256), Angle.dms_to_rad(  6, 24, 24.3425)).toString());
		System.out.printf("%-20s %s%n", "Alschairn",            bsc.search_by_ra_dec(Angle.hms_to_rad(19, 55, 18.79256), Angle.dms_to_rad(  6, 24, 24.3425)).toString());

		System.out.printf("%-20s %s%n", "Deneb el Okab",        bsc.search_by_ra_dec(Angle.hms_to_rad(18, 59, 37.36161), Angle.dms_to_rad( 15,  4,  5.8807)).toString());

		System.out.printf("%-20s %s%n", "Okab",                 bsc.search_by_ra_dec(Angle.hms_to_rad(19,  5, 24.60802), Angle.dms_to_rad( 13, 51, 48.5182)).toString());

		System.out.printf("%-20s %s%n", "Tarazed",              bsc.search_by_ra_dec(Angle.hms_to_rad(19, 46, 15.58029), Angle.dms_to_rad( 10, 36, 47.7408)).toString());
		System.out.printf("%-20s %s%n", "Reda",                 bsc.search_by_ra_dec(Angle.hms_to_rad(19, 46, 15.58029), Angle.dms_to_rad( 10, 36, 47.7408)).toString());

		System.out.printf("%-20s %s%n", "Sadalmelek",           bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());
		System.out.printf("%-20s %s%n", "El Melik",             bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());
		System.out.printf("%-20s %s%n", "Rucbah",               bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());
		System.out.printf("%-20s %s%n", "Saad el Melik",        bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());
		System.out.printf("%-20s %s%n", "Sadalmelik",           bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());
		System.out.printf("%-20s %s%n", "Sadlamulk",            bsc.search_by_ra_dec(Angle.hms_to_rad(22,  5, 47.03593), Angle.dms_to_rad(  0,-19, 11.4568)).toString());

		System.out.printf("%-20s %s%n", "Sadalsuud",            bsc.search_by_ra_dec(Angle.hms_to_rad(21, 31, 33.53171), Angle.dms_to_rad( -5, 34, 16.2320)).toString());
		System.out.printf("%-20s %s%n", "Saad el Sund",         bsc.search_by_ra_dec(Angle.hms_to_rad(21, 31, 33.53171), Angle.dms_to_rad( -5, 34, 16.2320)).toString());

		System.out.printf("%-20s %s%n", "Situla",               bsc.search_by_ra_dec(Angle.hms_to_rad(22, 37, 45.38049), Angle.dms_to_rad( -4, 13, 40.9939)).toString());

		System.out.printf("%-20s %s%n", "Skat",                 bsc.search_by_ra_dec(Angle.hms_to_rad(22, 54, 39.01250), Angle.dms_to_rad(-15, 49, 14.9530)).toString());
		System.out.printf("%-20s %s%n", "Scheat",               bsc.search_by_ra_dec(Angle.hms_to_rad(22, 54, 39.01250), Angle.dms_to_rad(-15, 49, 14.9530)).toString());

		System.out.printf("%-20s %s%n", "Albali",               bsc.search_by_ra_dec(Angle.hms_to_rad(20, 47, 40.55260), Angle.dms_to_rad( -9, 29, 44.7877)).toString());

		System.out.printf("%-20s %s%n", "Sadachbia",            bsc.search_by_ra_dec(Angle.hms_to_rad(22, 21, 39.37542), Angle.dms_to_rad( -1, 23, 14.4031)).toString());
		System.out.printf("%-20s %s%n", "Sadalachbia",          bsc.search_by_ra_dec(Angle.hms_to_rad(22, 21, 39.37542), Angle.dms_to_rad( -1, 23, 14.4031)).toString());

		System.out.printf("%-20s %s%n", "Ancha",                bsc.search_by_ra_dec(Angle.hms_to_rad(22, 16, 50.03635), Angle.dms_to_rad( -7, 46, 59.8480)).toString());

		System.out.printf("%-20s %s%n", "Hamal",                bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  7, 10.40570), Angle.dms_to_rad( 23, 27, 44.7032)).toString());
		System.out.printf("%-20s %s%n", "Hemal",                bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  7, 10.40570), Angle.dms_to_rad( 23, 27, 44.7032)).toString());
		System.out.printf("%-20s %s%n", "Ras Hammel",           bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  7, 10.40570), Angle.dms_to_rad( 23, 27, 44.7032)).toString());
		System.out.printf("%-20s %s%n", "El Nath",              bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  7, 10.40570), Angle.dms_to_rad( 23, 27, 44.7032)).toString());
		System.out.printf("%-20s %s%n", "Arietis",              bsc.search_by_ra_dec(Angle.hms_to_rad( 2,  7, 10.40570), Angle.dms_to_rad( 23, 27, 44.7032)).toString());

		System.out.printf("%-20s %s%n", "Sheratan",             bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 54, 38.41099), Angle.dms_to_rad( 20, 48, 28.9133)).toString());
		System.out.printf("%-20s %s%n", "Sharatan",             bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 54, 38.41099), Angle.dms_to_rad( 20, 48, 28.9133)).toString());
		System.out.printf("%-20s %s%n", "Al Sharatain",         bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 54, 38.41099), Angle.dms_to_rad( 20, 48, 28.9133)).toString());

		System.out.printf("%-20s %s%n", "Botein",               bsc.search_by_ra_dec(Angle.hms_to_rad( 3, 11, 37.76465), Angle.dms_to_rad( 19, 43, 36.0397)).toString());
		System.out.printf("%-20s %s%n", "Botejn",               bsc.search_by_ra_dec(Angle.hms_to_rad( 3, 11, 37.76465), Angle.dms_to_rad( 19, 43, 36.0397)).toString());

		System.out.printf("%-20s %s%n", "Mesarthim",            bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 53, 31.81479), Angle.dms_to_rad( 19, 17, 37.8790)).toString());

		System.out.printf("%-20s %s%n", "Capella",              bsc.search_by_ra_dec(Angle.hms_to_rad( 5, 16, 41.35871), Angle.dms_to_rad( 45, 59, 52.7693)).toString());
		System.out.printf("%-20s %s%n", "Alhajoth",             bsc.search_by_ra_dec(Angle.hms_to_rad( 5, 16, 41.35871), Angle.dms_to_rad( 45, 59, 52.7693)).toString());
		System.out.printf("%-20s %s%n", "Hokulei",              bsc.search_by_ra_dec(Angle.hms_to_rad( 5, 16, 41.35871), Angle.dms_to_rad( 45, 59, 52.7693)).toString());

		System.out.printf("%-20s %s%n", "Menkalinan",           bsc.search_by_ra_dec(Angle.hms_to_rad( 5, 59, 31.72293), Angle.dms_to_rad( 44, 56, 50.7573)).toString());

		System.out.printf("%-20s %s%n", "Haedus",               bsc.search_by_ra_dec(Angle.hms_to_rad( 5,  6, 30.89337), Angle.dms_to_rad( 41, 14,  4.1127)).toString());

		System.out.printf("%-20s %s%n", "Hassaleh",             bsc.search_by_ra_dec(Angle.hms_to_rad( 4, 56, 59.62109), Angle.dms_to_rad( 33,  9, 57.9585)).toString());
		System.out.printf("%-20s %s%n", "Kabdhilinan",          bsc.search_by_ra_dec(Angle.hms_to_rad( 4, 56, 59.62109), Angle.dms_to_rad( 33,  9, 57.9585)).toString());

		System.out.printf("%-20s %s%n", "Saclateni",            bsc.search_by_ra_dec(Angle.hms_to_rad( 5,  2, 28.69085), Angle.dms_to_rad( 41,  4, 32.9342)).toString());

		System.out.printf("%-20s %s%n", "Merga",                bsc.search_by_ra_dec(Angle.hms_to_rad(14, 49, 18.67062), Angle.dms_to_rad( 46,  6, 58.3369)).toString());
		System.out.printf("%-20s %s%n", "Marrha",               bsc.search_by_ra_dec(Angle.hms_to_rad(14, 49, 18.67062), Angle.dms_to_rad( 46,  6, 58.3369)).toString());
		System.out.printf("%-20s %s%n", "El Mara el Musalsela", bsc.search_by_ra_dec(Angle.hms_to_rad(14, 49, 18.67062), Angle.dms_to_rad( 46,  6, 58.3369)).toString());
		System.out.printf("%-20s %s%n", "Falx Italica",         bsc.search_by_ra_dec(Angle.hms_to_rad(14, 49, 18.67062), Angle.dms_to_rad( 46,  6, 58.3369)).toString());

		System.out.printf("%-20s %s%n", "Arcturus",             bsc.search_by_ra_dec(Angle.hms_to_rad(14, 15, 39.70000), Angle.dms_to_rad( 19, 10, 56.0000)).toString());
		System.out.printf("%-20s %s%n", "Alramech",             bsc.search_by_ra_dec(Angle.hms_to_rad(14, 15, 39.70000), Angle.dms_to_rad( 19, 10, 56.0000)).toString());
		System.out.printf("%-20s %s%n", "Abramech",             bsc.search_by_ra_dec(Angle.hms_to_rad(14, 15, 39.70000), Angle.dms_to_rad( 19, 10, 56.0000)).toString());

		System.out.printf("%-20s %s%n", "Nekkar",               bsc.search_by_ra_dec(Angle.hms_to_rad(15,  1, 56.76238), Angle.dms_to_rad( 40, 23, 26.0406)).toString());
		System.out.printf("%-20s %s%n", "Merez",                bsc.search_by_ra_dec(Angle.hms_to_rad(15,  1, 56.76238), Angle.dms_to_rad( 40, 23, 26.0406)).toString());

		System.out.printf("%-20s %s%n", "Izar",                 bsc.search_by_ra_dec(Angle.hms_to_rad(14, 44, 59.22000), Angle.dms_to_rad( 27,  4, 27.2000)).toString());
		System.out.printf("%-20s %s%n", "Mirak",                bsc.search_by_ra_dec(Angle.hms_to_rad(14, 44, 59.22000), Angle.dms_to_rad( 27,  4, 27.2000)).toString());
		System.out.printf("%-20s %s%n", "Pulcherrima",          bsc.search_by_ra_dec(Angle.hms_to_rad(14, 44, 59.22000), Angle.dms_to_rad( 27,  4, 27.2000)).toString());
		System.out.printf("%-20s %s%n", "Mirach",               bsc.search_by_ra_dec(Angle.hms_to_rad(14, 44, 59.22000), Angle.dms_to_rad( 27,  4, 27.2000)).toString());
		System.out.printf("%-20s %s%n", "Mirac",                bsc.search_by_ra_dec(Angle.hms_to_rad(14, 44, 59.22000), Angle.dms_to_rad( 27,  4, 27.2000)).toString());

		System.out.printf("%-20s %s%n", "Muphrid",              bsc.search_by_ra_dec(Angle.hms_to_rad(13, 54, 41.07892), Angle.dms_to_rad( 18, 23, 51.7946)).toString());
		System.out.printf("%-20s %s%n", "Saak",                 bsc.search_by_ra_dec(Angle.hms_to_rad(13, 54, 41.07892), Angle.dms_to_rad( 18, 23, 51.7946)).toString());

		System.out.printf("%-20s %s%n", "Seginus",              bsc.search_by_ra_dec(Angle.hms_to_rad(14, 32,  4.67180), Angle.dms_to_rad( 38, 18, 29.7043)).toString());
		System.out.printf("%-20s %s%n", "Haris",                bsc.search_by_ra_dec(Angle.hms_to_rad(14, 32,  4.67180), Angle.dms_to_rad( 38, 18, 29.7043)).toString());
		System.out.printf("%-20s %s%n", "Ceginus",              bsc.search_by_ra_dec(Angle.hms_to_rad(14, 32,  4.67180), Angle.dms_to_rad( 38, 18, 29.7043)).toString());
		System.out.printf("%-20s %s%n", "Segin",                bsc.search_by_ra_dec(Angle.hms_to_rad(14, 32,  4.67180), Angle.dms_to_rad( 38, 18, 29.7043)).toString());

		System.out.printf("%-20s %s%n", "Alkalurops",           bsc.search_by_ra_dec(Angle.hms_to_rad(15, 24, 29.42836), Angle.dms_to_rad( 37, 22, 37.7577)).toString());
		System.out.printf("%-20s %s%n", "Inkalunis",            bsc.search_by_ra_dec(Angle.hms_to_rad(15, 24, 29.42836), Angle.dms_to_rad( 37, 22, 37.7577)).toString());
		System.out.printf("%-20s %s%n", "Icalurus",             bsc.search_by_ra_dec(Angle.hms_to_rad(15, 24, 29.42836), Angle.dms_to_rad( 37, 22, 37.7577)).toString());
		System.out.printf("%-20s %s%n", "Clava",                bsc.search_by_ra_dec(Angle.hms_to_rad(15, 24, 29.42836), Angle.dms_to_rad( 37, 22, 37.7577)).toString());
		System.out.printf("%-20s %s%n", "Venabulum",            bsc.search_by_ra_dec(Angle.hms_to_rad(15, 24, 29.42836), Angle.dms_to_rad( 37, 22, 37.7577)).toString());

		System.out.printf("%-20s %s%n", "Algedi",               bsc.search_by_ra_dec(Angle.hms_to_rad(20, 18,  3.25595), Angle.dms_to_rad(-12, 32, 41.4684)).toString());
		System.out.printf("%-20s %s%n", "Gredi",                bsc.search_by_ra_dec(Angle.hms_to_rad(20, 18,  3.25595), Angle.dms_to_rad(-12, 32, 41.4684)).toString());
		System.out.printf("%-20s %s%n", "Segunda Giedi",        bsc.search_by_ra_dec(Angle.hms_to_rad(20, 18,  3.25595), Angle.dms_to_rad(-12, 32, 41.4684)).toString());
		System.out.printf("%-20s %s%n", "Algiedi Segunda",      bsc.search_by_ra_dec(Angle.hms_to_rad(20, 18,  3.25595), Angle.dms_to_rad(-12, 32, 41.4684)).toString());

		System.out.printf("%-20s %s%n", "Dabih",                bsc.search_by_ra_dec(Angle.hms_to_rad(20, 21,  0.70000), Angle.dms_to_rad(-14, 46, 53.0000)).toString());
		System.out.printf("%-20s %s%n", "Dabikh",               bsc.search_by_ra_dec(Angle.hms_to_rad(20, 21,  0.70000), Angle.dms_to_rad(-14, 46, 53.0000)).toString());
		System.out.printf("%-20s %s%n", "Dikhabda",             bsc.search_by_ra_dec(Angle.hms_to_rad(20, 21,  0.70000), Angle.dms_to_rad(-14, 46, 53.0000)).toString());

		System.out.printf("%-20s %s%n", "Deneb Algedi",         bsc.search_by_ra_dec(Angle.hms_to_rad(21, 47,  2.44424), Angle.dms_to_rad(-16,  7, 38.2335)).toString());
		System.out.printf("%-20s %s%n", "Deneb Algiedi",        bsc.search_by_ra_dec(Angle.hms_to_rad(21, 47,  2.44424), Angle.dms_to_rad(-16,  7, 38.2335)).toString());
		System.out.printf("%-20s %s%n", "Scheddi",              bsc.search_by_ra_dec(Angle.hms_to_rad(21, 47,  2.44424), Angle.dms_to_rad(-16,  7, 38.2335)).toString());

		System.out.printf("%-20s %s%n", "Nashira",              bsc.search_by_ra_dec(Angle.hms_to_rad(21, 40,  5.45648), Angle.dms_to_rad(-16, 39, 44.307184)).toString());

		System.out.printf("%-20s %s%n", "Canopus",              bsc.search_by_ra_dec(Angle.hms_to_rad( 6, 23, 57.10988), Angle.dms_to_rad(-52, 41, 44.381000)).toString());
		System.out.printf("%-20s %s%n", "Suhayl",               bsc.search_by_ra_dec(Angle.hms_to_rad( 6, 23, 57.10988), Angle.dms_to_rad(-52, 41, 44.381000)).toString());
		System.out.printf("%-20s %s%n", "Suhel",                bsc.search_by_ra_dec(Angle.hms_to_rad( 6, 23, 57.10988), Angle.dms_to_rad(-52, 41, 44.381000)).toString());
		System.out.printf("%-20s %s%n", "Suhail",               bsc.search_by_ra_dec(Angle.hms_to_rad( 6, 23, 57.10988), Angle.dms_to_rad(-52, 41, 44.381000)).toString());

		System.out.printf("%-20s %s%n", "Miaplacidus",          bsc.search_by_ra_dec(Angle.hms_to_rad( 9, 13, 11.97746), Angle.dms_to_rad(-69, 43,  1.947300)).toString());

		System.out.printf("%-20s %s%n", "Aspidiske",            bsc.search_by_ra_dec(Angle.hms_to_rad( 9, 17,  5.40686), Angle.dms_to_rad(-59, 16, 30.835300)).toString());
		System.out.printf("%-20s %s%n", "Scutulum",             bsc.search_by_ra_dec(Angle.hms_to_rad( 9, 17,  5.40686), Angle.dms_to_rad(-59, 16, 30.835300)).toString());
		System.out.printf("%-20s %s%n", "Turais",               bsc.search_by_ra_dec(Angle.hms_to_rad( 9, 17,  5.40686), Angle.dms_to_rad(-59, 16, 30.835300)).toString());
		System.out.printf("%-20s %s%n", "Tureis",               bsc.search_by_ra_dec(Angle.hms_to_rad( 9, 17,  5.40686), Angle.dms_to_rad(-59, 16, 30.835300)).toString());

		System.out.printf("%-20s %s%n", "Schedar",              bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 40, 30.44110), Angle.dms_to_rad(+56, 32, 14.392000)).toString());

		System.out.printf("%-20s %s%n", "Caph",                 bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  9, 10.68518), Angle.dms_to_rad(+59,  8, 59.212000)).toString());
		System.out.printf("%-20s %s%n", "Chaph",                bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  9, 10.68518), Angle.dms_to_rad(+59,  8, 59.212000)).toString());
		System.out.printf("%-20s %s%n", "Kaff",                 bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  9, 10.68518), Angle.dms_to_rad(+59,  8, 59.212000)).toString());
		System.out.printf("%-20s %s%n", "Al Sanam al Nakah",    bsc.search_by_ra_dec(Angle.hms_to_rad( 0,  9, 10.68518), Angle.dms_to_rad(+59,  8, 59.212000)).toString());

		System.out.printf("%-20s %s%n", "Ruchbah",              bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 25, 48.95147), Angle.dms_to_rad(+60, 14,  7.022500)).toString());
		System.out.printf("%-20s %s%n", "Ksora",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 25, 48.95147), Angle.dms_to_rad(+60, 14,  7.022500)).toString());
		System.out.printf("%-20s %s%n", "Rucba",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 25, 48.95147), Angle.dms_to_rad(+60, 14,  7.022500)).toString());
		System.out.printf("%-20s %s%n", "Rucbar",               bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 25, 48.95147), Angle.dms_to_rad(+60, 14,  7.022500)).toString());

		System.out.printf("%-20s %s%n", "Segin",                bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 54, 23.72618), Angle.dms_to_rad(+63, 40, 12.372200)).toString());

		System.out.printf("%-20s %s%n", "Achird",               bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 49,  6.29070), Angle.dms_to_rad(+57, 48, 54.675800)).toString());

		System.out.printf("%-20s %s%n", "Marfak",               bsc.search_by_ra_dec(Angle.hms_to_rad( 1, 11,  6.16225), Angle.dms_to_rad(+55,  8, 59.647200)).toString());

		System.out.printf("%-20s %s%n", "Rigil Kentaurus",      bsc.search_by_ra_dec(Angle.hms_to_rad(14, 39, 36.49400), Angle.dms_to_rad(-60, 50,  2.373700)).toString());
		System.out.printf("%-20s %s%n", "Toliman",              bsc.search_by_ra_dec(Angle.hms_to_rad(14, 39, 35.06311), Angle.dms_to_rad(-60, 50, 15.099200)).toString());

		System.out.printf("%-20s %s%n", "Alderamin",            bsc.search_by_ra_dec(Angle.hms_to_rad(21, 18, 34.77150), Angle.dms_to_rad(+62, 35,  8.061000)).toString());

		System.out.printf("%-20s %s%n", "Alfirk",               bsc.search_by_ra_dec(Angle.hms_to_rad(21, 28, 39.59685), Angle.dms_to_rad(+70, 33, 38.574700)).toString());

		System.out.printf("%-20s %s%n", "Errai",                bsc.search_by_ra_dec(Angle.hms_to_rad(23, 39, 20.85200), Angle.dms_to_rad(+77, 37, 56.190000)).toString());

		System.out.printf("%-20s %s%n", "Erakis",               bsc.search_by_ra_dec(Angle.hms_to_rad(21, 43, 30.46090), Angle.dms_to_rad(+58, 46, 48.166000)).toString());

		System.out.printf("%-20s %s%n", "Al Kidr",              bsc.search_by_ra_dec(Angle.hms_to_rad(20, 29, 34.86518), Angle.dms_to_rad(+62, 59, 38.621600)).toString());

		System.out.printf("%-20s %s%n", "Kurhah",               bsc.search_by_ra_dec(Angle.hms_to_rad(22,  3, 47.45500), Angle.dms_to_rad(+64, 37, 40.710000)).toString());

		System.out.printf("%-20s %s%n", "Al Kalb al Rai",       bsc.search_by_ra_dec(Angle.hms_to_rad(22, 29, 52.97918), Angle.dms_to_rad(+78, 49, 27.428200)).toString());

		System.out.printf("%-20s %s%n", "Castula",              bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 56, 39.90413), Angle.dms_to_rad(+59, 10, 51.800600)).toString());

		System.out.printf("%-20s %s%n", "Menkar",               bsc.search_by_ra_dec(Angle.hms_to_rad( 3,  2, 16.77307), Angle.dms_to_rad(+ 4,  5, 23.059600)).toString());
		System.out.printf("%-20s %s%n", "Menkab",               bsc.search_by_ra_dec(Angle.hms_to_rad( 3,  2, 16.77307), Angle.dms_to_rad(+ 4,  5, 23.059600)).toString());

		System.out.printf("%-20s %s%n", "Diphda",               bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 43, 35.37090), Angle.dms_to_rad(-17, 59, 11.782700)).toString());
		System.out.printf("%-20s %s%n", "Deneb Kaitos",         bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 43, 35.37090), Angle.dms_to_rad(-17, 59, 11.782700)).toString());
		System.out.printf("%-20s %s%n", "Difda al Thani",       bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 43, 35.37090), Angle.dms_to_rad(-17, 59, 11.782700)).toString());
		System.out.printf("%-20s %s%n", "Rana Segunda",         bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 43, 35.37090), Angle.dms_to_rad(-17, 59, 11.782700)).toString());

		System.out.printf("%-20s %s%n", "",               bsc.search_by_ra_dec(Angle.hms_to_rad( 0, 39, 19.67518), Angle.dms_to_rad(+30, 51, 39.6783)).toString());

		System.out.printf("%-20s %s%n", " 7 Aql", bsc.search_by_ra_dec(Angle.hms_to_rad(18, 51,  5.40889), Angle.dms_to_rad( -3, 15, 39.9996)).toString());
		System.out.printf("%-20s %s%n", " 8 Aql", bsc.search_by_ra_dec(Angle.hms_to_rad(18, 51, 22.15810), Angle.dms_to_rad( -3, 19,  4.2832)).toString());
		System.out.printf("%-20s %s%n", " 8 Aqr", bsc.search_by_ra_dec(Angle.hms_to_rad(20, 59, 54.82678), Angle.dms_to_rad(-13,  3,  5.8699)).toString());
		System.out.printf("%-20s %s%n", " 9 Aqr", bsc.search_by_ra_dec(Angle.hms_to_rad(21,  1,  8.33321), Angle.dms_to_rad(-13, 31, 47.9702)).toString());
		System.out.printf("%-20s %s%n", "10 Aqr", bsc.search_by_ra_dec(Angle.hms_to_rad(21,  0, 32.660  ), Angle.dms_to_rad(- 5, 28, 38.4600)).toString());
		*/
		System.out.printf("%-20s %s%n", "70 Cnc", bsc.search_by_ra_dec(Angle.hms_to_rad( 9,  4,  9.86704), Angle.dms_to_rad( 27, 53,  9.86704)).toString());
		System.out.printf("%-20s %s%n", "78 Cnc", bsc.search_by_ra_dec(Angle.hms_to_rad( 9,  9,  2.31165), Angle.dms_to_rad( 17, 28, 10.7518 )).toString());
	}
}
