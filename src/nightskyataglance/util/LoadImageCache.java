package nightskyataglance.util;

/*******************************************************************************
 * Copyright (c) 2025-2026 Douglas M. Pase                                     *
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
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

import lib.astro.PracticalAstronomy;
import lib.stars.catalog.ArpCatalog;
import lib.stars.catalog.ArpEntry;
import lib.stars.catalog.CaldwellCatalog;
import lib.stars.catalog.CaldwellEntry;
import lib.stars.catalog.HerschelCatalog;
import lib.stars.catalog.HerschelEntry;
import lib.stars.catalog.MessierCatalog;
import lib.stars.catalog.MessierEntry;
import lib.stars.catalog.NgcIcCatalog;
import lib.stars.catalog.NgcIcEntry;
import lib.stars.catalog.SharplessCatalog;
import lib.stars.catalog.SharplessEntry;
import lib.stars.catalog.UgcCatalog;
import lib.stars.catalog.UgcEntry;
import lib.stars.catalog.YaleBrightStarAscCatalog;
import lib.stars.catalog.YaleBrightStarAscEntry;
import lib.time.TimeOfDay;
import lib.util.Permute;
import lib.util.Queue;

public class LoadImageCache {
	public static void load_tiles(String cache, int seed) throws IOException
	{
		String cache_red  = cache + "red";
		String cache_blue = cache + "blue";

		Queue<RaDec> que0 = new Queue<RaDec>();
		Queue<RaDec> que1 = new Queue<RaDec>();
		Queue<RaDec> que2 = new Queue<RaDec>();
		for (int ra=0; ra < 360; ra++) {
			for (int de=-90; de < 90; de++) {
				que0.append(new RaDec(ra + 0.0, de + 0.0, 60, 60));
				que1.append(new RaDec(ra + 0.5, de + 0.5, 60, 60));
				que2.append(new RaDec(ra + 0.0, de + 0.5, 30, 30));
				que2.append(new RaDec(ra + 0.5, de + 0.0, 30, 30));
			}
			que0.append(new RaDec(ra + 0.0, 90.0, 60, 60));
			que2.append(new RaDec(ra + 0.5, 90.0, 30, 30));
		}

		Random r = (0 < seed) ? new Random(seed) : null;
		Queue<RaDec> que = new Queue<RaDec>();

		RaDec[] a0 = new RaDec[que0.length()];
		for (int i=0; i < a0.length; i++) { a0[i] = que0.remove(); }
		Permute.permute(r, a0);
		// que.append(a0);

		RaDec[] a1 = new RaDec[que1.length()];
		for (int i=0; i < a1.length; i++) { a1[i] = que1.remove(); }
		Permute.permute(r, a1);
		// que.append(a1);

		RaDec[] a2 = new RaDec[que2.length()];
		for (int i=0; i < a2.length; i++) { a2[i] = que2.remove(); }
		Permute.permute(r, a2);
		que.append(a2);

		Hashtable<Long,String> h = new Hashtable<Long,String>();
		Queue<RaDec> dne = new Queue<RaDec>();
		while (0 < que.length()) {
			RaDec elt = que.remove();
			double ra = elt.ra;
			double de = elt.dec;
			if (0 <= ra && ra < 360 && -90 <= de && de <= 90 && h.get(elt.enc) == null) {
				dne.append(elt);
			}
			h.put(elt.enc, "*");
		}

		RaDec[] ra_dec = new RaDec[dne.length()];
		for (int i=0; i < ra_dec.length; i++) { ra_dec[i] = dne.remove(); }
		LoadImageCache.load_cache(ra_dec, cache_red, cache_blue);
	}
	
	public static void load_dso_images(String catalogs, int seed) throws IOException
	{
		String ngc_ic_name             = "NGC+IC-J2000.txt";					// NGC+IC-J2000/NGC+IC-J2000.txt
		String ybsc_name               = "bsc5.dat";							// Yale Bright Star Catalog
		String caldwell_name           = "Caldwell Catalog.csv";				// Caldwell Catalog
		String herschel_name           = "Herschel 400 Catalog.csv";			// Herschel Catalog
		String messier_name            = "Messier Catalog.txt";					// Messier Catalog
		String sharpless_name          = "sharpless.asu.csv";					// Sharpless Catalog
		String arp_name                = "arpord.dat";							// Arp Catalog
		String ugc_name                = "ugc.dat.txt";							// Uppsala General Catalog
		NgcIcCatalog             ngc_ic    = new NgcIcCatalog            (catalogs + ngc_ic_name);
		YaleBrightStarAscCatalog ybsc      = new YaleBrightStarAscCatalog(catalogs + ybsc_name);
		CaldwellCatalog          caldwell  = new CaldwellCatalog         (catalogs + caldwell_name, ngc_ic);
		HerschelCatalog          herschel  = new HerschelCatalog         (catalogs + herschel_name, ngc_ic);
		MessierCatalog           messier   = new MessierCatalog          (catalogs + messier_name,  ngc_ic);
		SharplessCatalog         sharpless = new SharplessCatalog        (catalogs + sharpless_name);
		UgcCatalog               ugc       = new UgcCatalog              (catalogs + ugc_name);
		ArpCatalog               arp       = new ArpCatalog              (catalogs + arp_name,      ngc_ic, ugc, messier);

		Queue<RaDec> ctr = new Queue<RaDec>();
		Queue<RaDec> off = new Queue<RaDec>();

		for (NgcIcEntry elt: ngc_ic.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.dec_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (YaleBrightStarAscEntry elt: ybsc.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.RA);
			double de = (int) elt.DE;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (CaldwellEntry elt: caldwell.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.dec_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (HerschelEntry elt: herschel.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.dec_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (MessierEntry elt: messier.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.dec_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (SharplessEntry elt: sharpless.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.de_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (ArpEntry elt: arp.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.de_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		for (UgcEntry elt: ugc.elts) {
			double ra = (int) PracticalAstronomy.hours_to_degrees(elt.ra_dhrs);
			double de = (int) elt.de_ddeg;
			ctr.append(new RaDec( 0.0 + ra,  0.0 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, -0.5 + de, 60, 60));
			off.append(new RaDec(-0.5 + ra, +0.5 + de, 60, 60));
			off.append(new RaDec(+0.0 + ra, +0.5 + de, 30, 30));
			off.append(new RaDec(+0.0 + ra, -0.5 + de, 30, 30));
			off.append(new RaDec(+0.5 + ra, +0.0 + de, 30, 30));
			off.append(new RaDec(-0.5 + ra, +0.0 + de, 30, 30));
		}

		Queue<RaDec> que = new Queue<RaDec>();

		RaDec[] ctr_a = new RaDec[ctr.length()];
		for (int i=0; i < ctr_a.length; i++) {
			ctr_a[i] = ctr.remove();
		}
		if (0 < seed) Permute.permute(seed, ctr_a);
		for (RaDec elt: ctr_a) {
			que.append(elt);
		}

		RaDec[] off_a = new RaDec[off.length()];
		for (int i=0; i < off_a.length; i++) {
			off_a[i] = off.remove();
		}
		if (0 < seed) Permute.permute(seed, off_a);
		for (RaDec elt: off_a) {
			que.append(elt);
		}

		Hashtable<Long,String> h = new Hashtable<Long,String>();

		String cache = "D:/data/nightsky/cache/r2";
		Queue<RaDec> dne = new Queue<RaDec>();
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		long sum = 0;
		long cnt = 0;
		int j = 0;
		while (0 < que.length()) {
			RaDec elt = que.remove();
			double ra = elt.ra;
			double de = elt.dec;
			String dir = String.format("%s/%03d", cache, (int) ra);
			String file_name = String.format("%06.2f%s%05.2f.gif", ra, ((de<0)?"-":"+"), Math.abs(de));
			File fd = new File(dir, file_name);
			if (ra < 0 || 360 <= ra || de < -90 || 90 < de) {
				if (fd != null && fd.exists()) {
			        System.out.printf("*%n%,8d bound '%s' size=%,10d %s ******%n*%n", j, fd.getPath(), fd.length(), TimeOfDay.local_time(fd.lastModified()));
					fd.delete();
				}
			} else if (fd != null && fd.exists()) {
				if (fd.length() < 500000) {
					System.out.printf("%,8d tiny  '%s' size=%,10d %s ******%n", j, fd.getPath(), fd.length(), TimeOfDay.local_time(fd.lastModified()));
//					fd.delete();
//					dne.append(elt);
					h.put(elt.enc, "***");
				} else if (fd.length() < 4000000) {
					System.out.printf("%,8d small '%s' size=%,10d %s ***%n", j, fd.getPath(), fd.length(), TimeOfDay.local_time(fd.lastModified()));
					h.put(elt.enc, "***");
				} else {
					// System.out.printf("%,8d clean '%s' size=%,10d %s ***%n", j, fd.getPath(), fd.length(), TimeOfDay.local_time(fd.lastModified()));
					long len = fd.length();
					min = Math.min(min, len);
					max = Math.max(max, len);
					sum = sum + len;
					cnt = cnt + 1;
				}
				j++;
			} else {
				if (h.get(elt.enc) == null) {
					dne.append(elt);
					h.put(elt.enc, "***");
				}
			}
		}
		que = dne;

		RaDec[] ra_dec = new RaDec[que.length()];
		for (int i=0; i < ra_dec.length; i++) {
			ra_dec[i] = que.remove();
		}

		String cache_red  = "D:/data/nightsky/cache/red";
		String cache_blue = "D:/data/nightsky/cache/blue";

		System.out.printf("File size: min=%,d avg=%,d max=%,d sum=%,d cnt=%,d%n", min, sum/cnt, max, sum, cnt);
		System.out.printf("DSO tiles: ctr=%,d off=%,d dne=%,d%n", ctr_a.length, off_a.length, ra_dec.length);
		LoadImageCache.load_cache(ra_dec, cache_red, cache_blue);
	}

	public static void load_cache(RaDec[] ra_dec, String cache_red, String cache_blue) throws IOException
	{
		for (int j=0; j < ra_dec.length; j++) {
			load_cache(ra_dec[j], cache_red, cache_blue, j);
		}
	}

	public static void load_cache(RaDec ra_dec, String red_cache_dir, String blue_cache_dir, int j) throws IOException
	{
		double ra = ra_dec.ra;
		double de = ra_dec.dec;
		int width  = ra_dec.width;
		int height = ra_dec.height;

		if (ra < 0 || 360 <= ra || de < -90 || 90 < de || width <= 0 || 120 < width || height <= 0 || 120 < height) {
			return;
		}

		String red_band  = "poss2ukstu_red";
		String blue_band = "poss2ukstu_blue";
		String band      = red_band;

		File red_cache = new File(String.format("%s/%03d", red_cache_dir, (int) ra));
		if (! red_cache.exists() && ! red_cache.mkdirs()) {
			System.out.printf("Cache '%s' does not exist%n", red_cache.getCanonicalPath());
	        return;
		}

		File blue_cache = new File(String.format("%s/%03d", blue_cache_dir, (int) ra));
		if (! blue_cache.exists() && ! blue_cache.mkdirs()) {
			System.out.printf("Cache '%s' does not exist%n", blue_cache.getCanonicalPath());
	        return;
		}

		String file_name = (width == 60 && height == 60) ? 
				String.format("%06.2f%s%05.2f.gif", ra, ((de<0)?"-":"+"), Math.abs(de)) : 
				String.format("%06.2f%s%05.2f.%dx%d.gif", ra, ((de<0)?"-":"+"), Math.abs(de), width, height);
		File red_fd  = new File(red_cache,  file_name);
		File blue_fd = new File(blue_cache, file_name);
		File fd      = red_fd;
		if (red_fd.exists() && 10000 < red_fd.length()) {
	        System.out.printf("%,8d red   '%s' size=%,10d %s ***%n", j, red_fd.getPath(), red_fd.length(), TimeOfDay.local_time(red_fd.lastModified()));
	        return;
		} else if (blue_fd.exists()) {
	        System.out.printf("%,8d blue  '%s' size=%,10d %s ***%n", j, red_fd.getPath(), red_fd.length(), TimeOfDay.local_time(red_fd.lastModified()));
	        return;
		} else if (red_fd.exists()) {
			band = blue_band;
			fd   = blue_fd;
		} else {
			band = red_band;
			fd   = red_fd;
		}

		int max_err = 1;
		for (int t=0; t < max_err; t++) {
			if (! fd.exists() || fd.length() <= 10000) {
				try {
					String uri_str = String.format("https://archive.stsci.edu/cgi-bin/dss_search?v=%s&r=%fd&d=%f&e=J2000&w=%d&h=%d&f=gif", band, ra, de, width, height);
					URI uri = new URI(uri_str);
					URL url = uri.toURL();
			        InputStream is = url.openStream(); 
					byte[] ch = is.readAllBytes();
			        is.close();

			        RandomAccessFile raf = new RandomAccessFile(fd, "rw");
			        raf.write(ch);
			        raf.close();

			        System.out.printf("%,8d wrote '%s' size=%,10d %s%n", j, fd.getPath(), ch.length, TimeOfDay.local_time(System.currentTimeMillis()));
			        try {
			        	long ms = (long) (30 * 1000 * (double) (width * height) / 3600.0);
			        	ms = 5 * 1000;
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						;
					}
			        break;
				} catch (Exception e) {
					System.out.println(e);
			        try {
						Thread.sleep(5 * 60 * 1000);
					} catch (InterruptedException ie) {
						;
					}
				}
			} else {
		        System.out.printf("%,8d exist '%s' size=%,d%n", j, red_fd.getPath(), red_fd.length());
		        break;
			}
		}
	}
}
