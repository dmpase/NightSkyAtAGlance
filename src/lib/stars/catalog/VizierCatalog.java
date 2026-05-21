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
import lib.util.Queue;

public class VizierCatalog {
	
	public final File file;
	public final VizierEntry[]     entry;
	public final SortByHD[]        hd;
	public final SortByDM[]        dm;
	public final SortByGC[]        gc;
	public final SortByHR[]        hr;
	public final SortByHip[]       hip;
	public final SortByRA[]        ra;
	public final SortByDec[]       dec;
	public final SortByVmag[]      vmag;
	public final SortByFlamsteed[] flamsteed;
	public final SortByBayer[]     bayer;

	public VizierCatalog(String file_name) throws IOException 
	{
		file = new File(file_name);
		if (file == null) {
			throw new FileNotFoundException();
		} else if (! file.isFile()) {
			throw new FileNotFoundException(file.getCanonicalPath());
		}

		// open, read, and close the file
		byte[] buf = new byte[(int) file.length()];
		RandomAccessFile catalog = new RandomAccessFile(file, "r");
		catalog.read(buf);
		catalog.close();
		
		// break up the file into lines
		Queue<String> lines = new Queue<String>();
		int start = 0;
		for (int i=0; i < buf.length; i++) {
			if ((buf[i] == '\n' || buf[i] == '\r') && start == i) {
				start += 1;
			} else if (buf[i] == '\n' || buf[i] == '\r') {
				byte[] line = new byte[i - start];
				for (int j=0; j < line.length; j++) {
					line[j] = buf[start + j];
				}
				start = i + 1;

				String str = new String(line);
				if (str.startsWith("#") || str.startsWith("------") || str.startsWith("      |") || str.startsWith("    HD|") || buf.length < 1) {
					;
				} else {
					lines.append(str);
				}
			}
		}
		
		entry     = new VizierEntry    [lines.length()];
		hd        = new SortByHD       [lines.length()];
		dm        = new SortByDM       [lines.length()];
		gc        = new SortByGC       [lines.length()];
		hr        = new SortByHR       [lines.length()];
		hip       = new SortByHip      [lines.length()];
		ra        = new SortByRA       [lines.length()];
		dec       = new SortByDec      [lines.length()];
		vmag      = new SortByVmag     [lines.length()];
		flamsteed = new SortByFlamsteed[lines.length()];
		bayer     = new SortByBayer    [lines.length()];
		for (int i=0; i < entry.length; i++) {
			entry[i] = new VizierEntry(lines.remove());
			
			hd       [i] = new SortByHD       (entry[i]);
			dm       [i] = new SortByDM       (entry[i]);
			gc       [i] = new SortByGC       (entry[i]);
			hr       [i] = new SortByHR       (entry[i]);
			hip      [i] = new SortByHip      (entry[i]);
			ra       [i] = new SortByRA       (entry[i]);
			dec      [i] = new SortByDec      (entry[i]);
			vmag     [i] = new SortByVmag     (entry[i]);
			flamsteed[i] = new SortByFlamsteed(entry[i]);
			bayer    [i] = new SortByBayer    (entry[i]);
		}
		
		Arrays.sort(hd);
		Arrays.sort(dm);
		Arrays.sort(gc);
		Arrays.sort(hr);
		Arrays.sort(hip);
		Arrays.sort(ra);
		Arrays.sort(dec);
		Arrays.sort(vmag);
		Arrays.sort(flamsteed);
		Arrays.sort(bayer);
	}

	// search for the star with the specified HD index
	public VizierEntry search_by_hd(int hd0)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = hd.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (hd[mid_idx].data.hd < hd0) {
				min_idx = mid_idx;
			} else if (hd0 < hd[mid_idx].data.hd) {
				max_idx = mid_idx;
			} else {
				result = hd[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star with the specified HD index
	public VizierEntry search_by_dm(String dm0)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = dm.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (dm[mid_idx].data.dm.compareToIgnoreCase(dm0) < 0) {
				min_idx = mid_idx;
			} else if (0 < dm[mid_idx].data.dm.compareToIgnoreCase(dm0)) {
				max_idx = mid_idx;
			} else {
				result = dm[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star with the specified HD index
	public VizierEntry search_by_gc(int gc0)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = gc.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (gc[mid_idx].data.gc < gc0) {
				min_idx = mid_idx;
			} else if (gc0 < gc[mid_idx].data.gc) {
				max_idx = mid_idx;
			} else {
				result = gc[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star with the specified HD index
	public VizierEntry search_by_hr(int hr0)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = hr.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (hr[mid_idx].data.hr < hr0) {
				min_idx = mid_idx;
			} else if (hr0 < hr[mid_idx].data.hr) {
				max_idx = mid_idx;
			} else {
				result = hr[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star with the specified HD index
	public VizierEntry search_by_hip(int hip0)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = hip.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (hip[mid_idx].data.hip < hip0) {
				min_idx = mid_idx;
			} else if (hip0 < hip[mid_idx].data.hip) {
				max_idx = mid_idx;
			} else {
				result = hip[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star nearest to the specified right ascension (radians)
	public VizierEntry search_by_ra(double radians)
	{
		VizierEntry result = null;

		// normalize the R.A. to 0 <= R.A. < 2*PI
		radians = norm_two_pi(radians);

		// binary search for the star with the nearest R.A.
		int min_idx = 0;
		int max_idx = ra.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (ra[mid_idx].data.ra.radians < radians) {
				min_idx = mid_idx;
			} else if (radians < ra[mid_idx].data.ra.radians) {
				max_idx = mid_idx;
			} else {
				result = ra[mid_idx].data;
				break;
			}

			if (Math.abs(radians - ra[min_idx].data.ra.radians) < Math.abs(radians - ra[max_idx-1].data.ra.radians)) {
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
	public VizierEntry search_by_dec(double degrees, double minutes, double seconds)
	{
		double radians = Angle.dms_to_rad(degrees, minutes, seconds);
		VizierEntry result = search_by_dec(radians);

		return result;
	}

	// search for the star with the nearest specified de_B1950 (radians)
	public VizierEntry search_by_dec(double radians)
	{
		VizierEntry result = null;

		// normalize the de_B1950 to -PI/2 <= dec <= +PI/2
		radians = norm_half_pi(radians);

		// binary search for the star with the nearest de_B1950
		int min_idx = 0;
		int max_idx = dec.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (radians < dec[mid_idx].data.dec.radians) {
				min_idx = mid_idx;
			} else if (dec[mid_idx].data.dec.radians < radians) {
				max_idx = mid_idx;
			} else {
				result = dec[mid_idx].data;
				break;
			}

			if (Math.abs(radians - dec[min_idx].data.dec.radians) < Math.abs(radians - dec[max_idx-1].data.dec.radians)) {
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
	public VizierEntry search_by_ra_dec(double ra, double dec)
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
		VizierEntry result = entry[0];

		// find the distance using the Haversine equation
		Angle lat1 = new Angle(result.dec.radians, Angle.Scale.RADIANS);
		Angle lon1 = new Angle(result.ra.radians,  Angle.Scale.RADIANS);
		Location loc1 = new Location(lat1, lon1);
		double d = s.great_circle_range(loc0, loc1);

		// search all other stars for a closer star to the coordinate
		for (int i=1; i < entry.length; i++) {
			// select another (probably better) guess
			VizierEntry guess = entry[i];
			
			// find the distance using the Haversine equation
			Angle lat2 = new Angle(guess.dec.radians, Angle.Scale.RADIANS);
			Angle lon2 = new Angle(guess.ra.radians,  Angle.Scale.RADIANS);
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

	// search for the star nearest to the specified visual magnitude
	public VizierEntry search_by_mag(double magnitude)
	{
		VizierEntry result = null;

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = vmag.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (vmag[mid_idx].data.vmag < magnitude) {
				min_idx = mid_idx;
			} else if (magnitude < vmag[mid_idx].data.vmag) {
				max_idx = mid_idx;
			} else {
				result = vmag[mid_idx].data;
				break;
			}

			if (Math.abs(magnitude - vmag[min_idx].data.vmag) < Math.abs(magnitude - vmag[max_idx-1].data.vmag)) {
				result = vmag[min_idx].data;
			} else {
				result = vmag[max_idx-1].data;
			}
		}

		return result;
	}

	// search for the star with the specified constellation and flamsteed number
	public VizierEntry search_by_fl(String cst0, int fl0)
	{
		VizierEntry result = null;
		
		Bayer bay = new Bayer(cst0, fl0);

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = flamsteed.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			int cmp = flamsteed[mid_idx].data.bayer.compareTo(bay);
			if (cmp < 0) {
				min_idx = mid_idx;
			} else if (0 < cmp) {
				max_idx = mid_idx;
			} else {
				result = flamsteed[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// search for the star with the specified constellation and bayer designation
	public VizierEntry search_by_bayer(String cst0, int fl0)
	{
		VizierEntry result = null;
		Bayer bay0 = new Bayer(cst0, fl0);

		// binary search for the star with the nearest magnitude
		int min_idx = 0;
		int max_idx = bayer.length;
		while (1 < (max_idx - min_idx)) {
			int mid_idx = (min_idx + max_idx)/2;
			if (bayer[mid_idx].data.bayer.compareTo(bay0) < 0) {
				min_idx = mid_idx;
			} else if (0 < bayer[mid_idx].data.bayer.compareTo(bay0)) {
				max_idx = mid_idx;
			} else {
				result = bayer[mid_idx].data;
				break;
			}
		}

		return result;
	}

	// return the index of the desired element
	public VizierEntry find(String name)
	{
		VizierEntry result = null;
		
		if (name != null) {
			name = name.trim().replaceAll("[ ]", "").toUpperCase();
			if (name.substring(0, 3).equals("VIZ")) {			// VizieR primary key
				int id = Integer.parseInt(name.substring(3));
				int idx = VizierEntry.find(id, entry, 0, entry.length);
				if (0 <= idx && idx < entry.length) {
					result = entry[idx];
				}
			} else if (name.substring(0, 2).equals("HD")) {		// Henry Draper Catalog
				int id = Integer.parseInt(name.substring(2));
				int idx = SortByHD.find(id, hd, 0, hd.length);
				if (0 <= idx && idx < hd.length) {
					result = hd[idx].data;
				}
			} else if (name.substring(0, 2).equals("GC"))  {	// Boss General Catalog
				int id = Integer.parseInt(name.substring(2));
				int idx = SortByGC.find(id, gc, 0, gc.length);
				if (0 <= idx && idx < gc.length) {
					result = gc[idx].data;
				}
			} else if (name.substring(0, 2).equals("HR"))  {	// Harvard Revised ID of the Yale Bright Star Catalog
				int id = Integer.parseInt(name.substring(2));
				int idx = SortByHR.find(id, hr, 0, hr.length);
				if (0 <= idx && idx < hr.length) {
					result = hr[idx].data;
				}
			} else if (name.substring(0, 3).equals("HIP")) {	// Hipparcos Catalog
				int id = Integer.parseInt(name.substring(3));
				int idx = SortByHip.find(id, hip, 0, hip.length);
				if (0 <= idx && idx < hip.length) {
					result = hip[idx].data;
				}
			} else if (name.substring(0, 2).equals("RA"))  {	// Right Ascension
			} else if (name.substring(0, 3).equals("DEC")) {	// Declination
			} else {											// Bayer or Flamsteed Designation
			}
		}

		return result;
	}

	public static class SortByHD implements Comparable<SortByHD> {
		
		public final VizierEntry data;
		
		public SortByHD(VizierEntry ent)
		{
			data = ent;
		}

		// return the index of the desired element
		public static int find(int id, SortByHD[] list)
		{
			return find(id, list, 0, list.length);
		}

		public static int find(int hd, SortByHD[] list, int lower, int upper)
		{
			int result;
			int middle = (lower + upper) / 2;
			int comparison = Integer.compare(list[middle].data.hd, hd);
			
			if (comparison == 0) {
				// name == list[middle]
				result = middle;
			} else if (middle == lower) {
				// no more elements to search
				result = -1;
			} else if (comparison < 0) {
				// list[middle] < name
				result = find(hd, list, middle, upper);
			} else {
				// name < list[middle]
				result = find(hd, list, lower, middle);
			}
			
			return result;
		}
		
		public int compareTo(int hd)
		{
			return data.hd - hd;
		}

		@Override public int compareTo(SortByHD rhs) 
		{
			return data.hd - rhs.data.hd;
		}
	}
	
	public static class SortByDM implements Comparable<SortByDM> {
		
		public final VizierEntry data;
		
		public SortByDM(VizierEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByDM rhs) 
		{
			return data.dm.compareTo(rhs.data.dm);
		}
	}
	
	public static class SortByGC implements Comparable<SortByGC> {
		
		public final VizierEntry data;
		
		public SortByGC(VizierEntry ent)
		{
			data = ent;
		}

		// return the index of the desired element
		public static int find(int gc, SortByGC[] list)
		{
			return find(gc, list, 0, list.length);
		}

		public static int find(int gc, SortByGC[] list, int lower, int upper)
		{
			int result;
			int middle = (lower + upper) / 2;
			int comparison = Integer.compare(list[middle].data.gc, gc);
			
			if (comparison == 0) {
				// name == list[middle]
				result = middle;
			} else if (middle == lower) {
				// no more elements to search
				result = -1;
			} else if (comparison < 0) {
				// list[middle] < name
				result = find(gc, list, middle, upper);
			} else {
				// name < list[middle]
				result = find(gc, list, lower, middle);
			}
			
			return result;
		}
		
		public int compareTo(int gc)
		{
			return data.gc - gc;
		}

		@Override public int compareTo(SortByGC rhs) 
		{
			return data.gc - rhs.data.gc;
		}
	}
	
	public static class SortByHR implements Comparable<SortByHR> {
		
		public final VizierEntry data;
		
		public SortByHR(VizierEntry ent)
		{
			data = ent;
		}

		// return the index of the desired element
		public static int find(int hr, SortByHR[] list)
		{
			return find(hr, list, 0, list.length);
		}

		public static int find(int hr, SortByHR[] list, int lower, int upper)
		{
			int result;
			int middle = (lower + upper) / 2;
			int comparison = Integer.compare(list[middle].data.hr, hr);
			
			if (comparison == 0) {
				// name == list[middle]
				result = middle;
			} else if (middle == lower) {
				// no more elements to search
				result = -1;
			} else if (comparison < 0) {
				// list[middle] < name
				result = find(hr, list, middle, upper);
			} else {
				// name < list[middle]
				result = find(hr, list, lower, middle);
			}
			
			return result;
		}
		
		public int compareTo(int hr)
		{
			return data.hr - hr;
		}

		@Override public int compareTo(SortByHR rhs) 
		{
			return data.hr - rhs.data.hr;
		}
	}
	
	public static class SortByHip implements Comparable<SortByHip> {
		
		public final VizierEntry data;
		
		public SortByHip(VizierEntry ent)
		{
			data = ent;
		}

		// return the index of the desired element
		public static int find(int hip, SortByHip[] list)
		{
			return find(hip, list, 0, list.length);
		}

		public static int find(int hip, SortByHip[] list, int lower, int upper)
		{
			int result;
			int middle = (lower + upper) / 2;
			int comparison = Integer.compare(list[middle].data.hip, hip);
			
			if (comparison == 0) {
				// name == list[middle]
				result = middle;
			} else if (middle == lower) {
				// no more elements to search
				result = -1;
			} else if (comparison < 0) {
				// list[middle] < name
				result = find(hip, list, middle, upper);
			} else {
				// name < list[middle]
				result = find(hip, list, lower, middle);
			}
			
			return result;
		}
		
		public int compareTo(int hip)
		{
			return data.hip - hip;
		}

		@Override public int compareTo(SortByHip rhs) 
		{
			return data.hip - rhs.data.hip;
		}
	}
	
	public static class SortByRA implements Comparable<SortByRA> {
		
		public final VizierEntry data;
		
		public SortByRA(VizierEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByRA rhs) 
		{
			if (data.ra.radians - rhs.data.ra.radians < 0) {
				return -1;
			} else if (0 < data.ra.radians - rhs.data.ra.radians) {
				return +1;
			}
			
			return 0;
		}
	}
	
	public static class SortByDec implements Comparable<SortByDec> {
		
		public final VizierEntry data;
		
		public SortByDec(VizierEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByDec rhs) 
		{
			if (data.dec.radians - rhs.data.dec.radians < 0) {
				return -1;
			} else if (0 < data.dec.radians - rhs.data.dec.radians) {
				return +1;
			}
			
			return 0;
		}
	}
	
	public static class SortByVmag implements Comparable<SortByVmag> {
		
		public final VizierEntry data;
		
		public SortByVmag(VizierEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByVmag rhs) 
		{
			if (data.vmag - rhs.data.vmag < 0) {
				return -1;
			} else if (0 < data.vmag - rhs.data.vmag) {
				return +1;
			}
			
			return 0;
		}
	}
	
	public static class SortByFlamsteed implements Comparable<SortByFlamsteed> {
		
		public final VizierEntry data;
		
		public SortByFlamsteed(VizierEntry ent)
		{
			data = ent;
		}

		public int compareTo(String cst0, int fl0) 
		{
			/*
			// not equal if they don't have the same constellation
			if (data.bayer.cst_id != rhs.data.bayer.cst_id) {
				// constellations don't match
				return data.bayer.cst_id - rhs.data.bayer.cst_id;
			} else if (data.bayer.cst_id == Integer.MAX_VALUE && rhs.data.bayer.cst_id == Integer.MAX_VALUE) {
				// both constellations are bogus, but do they match?
				if (data.bayer.constellation == null && rhs.data.bayer.constellation == null) {
					// constellations match, check bayer or flamsteed
				} else if (data.bayer.constellation == null) {
					// constellation values don't match
					return -1;
				} else if (rhs.data.bayer.constellation == null) {
					// constellation values don't match
					return +1;
				} else {
					int cst = data.bayer.constellation.compareToIgnoreCase(rhs.data.bayer.constellation);
					if (cst != 0) {
						// constellations don't match
						return cst;
					}
				}
			}

			// ignore the bayer designations, check flamsteed
			return data.bayer.fl - rhs.data.bayer.fl;
			*/

			// TODO
			return 0;
		}

		@Override public int compareTo(SortByFlamsteed rhs) 
		{
			// not equal if they don't have the same constellation
			if (data.bayer.cst_id != rhs.data.bayer.cst_id) {
				// constellations don't match
				return data.bayer.cst_id - rhs.data.bayer.cst_id;
			} else if (data.bayer.cst_id == Integer.MAX_VALUE && rhs.data.bayer.cst_id == Integer.MAX_VALUE) {
				// both constellations are bogus, but do they match?
				if (data.bayer.constellation == null && rhs.data.bayer.constellation == null) {
					// constellations match, check bayer or flamsteed
				} else if (data.bayer.constellation == null) {
					// constellation values don't match
					return -1;
				} else if (rhs.data.bayer.constellation == null) {
					// constellation values don't match
					return +1;
				} else {
					int cst = data.bayer.constellation.compareToIgnoreCase(rhs.data.bayer.constellation);
					if (cst != 0) {
						// constellations don't match
						return cst;
					}
				}
			}

			// ignore the bayer designations, check flamsteed
			return data.bayer.fl - rhs.data.bayer.fl;
		}
	}
	
	public static class SortByBayer implements Comparable<SortByBayer> {
		
		public final VizierEntry data;
		
		public SortByBayer(VizierEntry ent)
		{
			data = ent;
		}

		@Override public int compareTo(SortByBayer rhs) 
		{
			return data.bayer.compareTo(rhs.data.bayer);
		}
	}

	public static void main(String[] args) throws IOException
	{
		String path = "\\\\magrathea\\dsk\\dmpase\\home\\Astronomy\\Catalogs\\Vizier\\catalog.dat.txt";
		new VizierCatalog(path);
	}
}
