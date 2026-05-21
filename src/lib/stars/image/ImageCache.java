package lib.stars.image;

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


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lib.astro.CelestialCalculations;
import lib.astro.PracticalAstronomy;
import lib.math.misc.Round;
import lib.time.TimeOfDay;
import lib.util.Queue;
import nightskyataglance.NightSkyAtAGlance;

// https://archive.stsci.edu/cgi-bin/dss_form?action=search&resolver=SIMBAD&radius=10.0

public class ImageCache {
	public final String root_dir_name;
	public final String red_dir_name;
	public final String blue_dir_name;

	// the cache is organized as cache[ra][de]
	public static final ImageList[][] cache = new ImageList[720][361];
	
	public static final int min_valid_size = 10000;

	/**
	 * Initialize, but do not fill, the image cache.
	 * @param cache_root_dir Directory on disk where the image cache resides.
	 * @throws IOException
	 */
	public ImageCache(String cache_root_dir) throws IOException
	{
		while (cache_root_dir.charAt(cache_root_dir.length()-1) == '/') {
			cache_root_dir = cache_root_dir.substring(0, cache_root_dir.length()-1);
		}
		root_dir_name = cache_root_dir;
		red_dir_name  = cache_root_dir + "/red";
		blue_dir_name = cache_root_dir + "/blue";
	}
	
	public void clear_cache()
	{
		for (int i=0; i < cache.length; i++) {
			for (int j=0; j < cache[i].length; j++) {
				cache[i][j] = null;
			}
		}
	}

	/**
	 * Look-up the location of an image tile on disk. Tiles are centered on whole or half degrees.
	 * Tiles where RA=ddd+0.0, Dec=dd+0.0 or RA=ddd+0.5, Dec=dd+0.5 are 60x60 minutes of arc in size.
	 * Tiles where RA=ddd+0.5, Dec=dd+0.0 or RA=ddd+0.0, Dec=dd+0.5 are 30x30 or 15x15 minutes of arc in size.
	 * 
	 * @param ctr_ra_deg Right ascension, in degrees, of the desired tile.
	 * @param ctr_de_deg Declination, in degrees, of the desired tile.
	 * @throws IOException
	 */
	public final ImageList lookup_cache(double ctr_ra_deg, double ctr_de_deg) throws IOException
	{
		ctr_ra_deg = (ctr_ra_deg <   0) ? ctr_ra_deg + 360 : ((360 <= ctr_ra_deg) ? ctr_ra_deg - 360 : ctr_ra_deg);
		ctr_de_deg = (ctr_de_deg < -90) ?              -90 : (( 90 <  ctr_de_deg) ?               90 : ctr_de_deg);

		// check for a valid right ascension and declination
		if (ctr_ra_deg < 0 || 360 <= ctr_ra_deg || ctr_de_deg < -90 || 90 < ctr_de_deg) {
			return null;
		}

		// make sure the new image is centered on a cached image
		ctr_ra_deg = ra_to_ra(ctr_ra_deg);
		ctr_de_deg = de_to_de(ctr_de_deg);

		// pull the tile from the cache in memory, if it exists
		int ridx = ra_to_idx(ctr_ra_deg);
		int didx = de_to_idx(ctr_de_deg);
		ImageList elt = cache[ridx][didx];

		if (elt == null) {
			// tile is not in the cache, so create a new cache entry
			elt = cache[ridx][didx] = new ImageList(this, ctr_ra_deg, ctr_de_deg);
		}

		// if this is our first visit, read the tile from disk or download it from the web
		if (elt.image == null && ! read_tile_from_disk(elt)) {
			// image is not on disk
			if (download_tile_from_web_to_disk(elt)) {
				// image successfully downloaded to disk (maybe)
				if (read_tile_from_disk(elt)) {
					// we successfully downloaded the image from the web and stored it on disk, AND...
					// we were able to read the tile from disk
				} else {
					// we were unable to read the new image from disk, so...
					// what we downloaded from the web was not really an image, 
					// or we couldn't write the new image to disk
					cache[ridx][didx] = null;
				}
			} else {
				// we were not able to download the image from the web
				cache[ridx][didx] = null;
			}
		} else {
			// we were able to read the tile from disk
		}

		return cache[ridx][didx];
	}

	public boolean download_tile_from_web_to_disk(ImageList tile) throws IOException
	{
		double ra = tile.ctr_ra_deg;
		double de = tile.ctr_de_deg;

		ra = (ra <   0) ? ra + 360 : ((360 <= ra) ? ra - 360 : ra);
		de = (de < -90) ?      -90 : (( 90 <  de) ?       90 : de);

		if (ra < 0 || 360 <= ra || de < -90 || 90 < de) {
			return false;
		}

		// make sure the new image is centered on a cached image
		ra = ra_to_ra(ra);
		de = de_to_de(de);
		
		// find the image width in minutes of arc
		int width;
		int height;
		int ridx = ra_to_idx(ra);
		int didx = de_to_idx(de);
		if (((ridx % 2) == 0 && (didx % 2) == 0) || ((ridx % 2) == 1 && (didx % 2) == 1)) {
			// size = 60 x 60
			width = height = 60;
		} else {
			// size = 30 x 30
			width = height = 30;
		}

		String red_band = "poss2ukstu_red";
		String blu_band = "poss2ukstu_blue";
		String band     = red_band;

		File red_cache = new File(String.format("%s/%03d", red_dir_name, (int) ra));
		if (! red_cache.exists() && ! red_cache.mkdirs()) {
			System.out.printf("Unable to create '%s'%n", red_cache.getCanonicalPath());
	        return false;
		}

		File blu_cache = new File(String.format("%s/%03d", blue_dir_name, (int) ra));
		if (! blu_cache.exists() && ! blu_cache.mkdirs()) {
			System.out.printf("Unable to create '%s'%n", blu_cache.getCanonicalPath());
	        return false;
		}

		String file_name = (width == 60 && height == 60) ? 
			String.format("%06.2f%s%05.2f.gif", ra, ((de<0)?"-":"+"), Math.abs(de)) : 
			String.format("%06.2f%s%05.2f.%dx%d.gif", ra, ((de<0)?"-":"+"), Math.abs(de), width, height);
		File red_fd = new File(red_cache, file_name);
		File blu_fd = new File(blu_cache, file_name);
		File fd     = red_fd;

		// download the image from the web
		int max_err = 2;
		for (int t=0; t < max_err; t++) {
			if (red_fd.exists() && min_valid_size < red_fd.length()) {
				// a valid file exists on disk in red directory
		        System.out.printf("red   '%s' size=%,10d %s ***%n", red_fd.getPath(), red_fd.length(), TimeOfDay.local_time(red_fd.lastModified()));
		        return true;
			} else if (blu_fd.exists() && min_valid_size < blu_fd.length()) {
				// a valid file exists on disk in blue directory
		        System.out.printf("blue  '%s' size=%,10d %s ***%n", blu_fd.getPath(), blu_fd.length(), TimeOfDay.local_time(blu_fd.lastModified()));
		        return true;
			} else if (red_fd.exists() && blu_fd.exists()) {
				// an invalid file exists in both red and blue directories
		        System.out.printf("bad   '%s' size=%,10d %s ***%n", blu_fd.getPath(), blu_fd.length(), TimeOfDay.local_time(blu_fd.lastModified()));
		        return false;
			} else if (red_fd.exists() && ! blu_fd.exists()) {
				// an invalid file exists in red but no version in blue, try downloading blue
				band = blu_band;
				fd   = blu_fd;
			} else {
				// no version exists in red, try downloading red
				band = red_band;
				fd   = red_fd;
			}

			// download the image
			try {
				String uri_fmt = "https://archive.stsci.edu/cgi-bin/dss_search?v=%s&r=%fd&d=%f&e=J2000&w=%d&h=%d&f=gif";
				String uri_str = String.format(uri_fmt, band, ra, de, width, height);
				URI uri = new URI(uri_str);
				URL url = uri.toURL();
		        InputStream is = url.openStream(); 
				byte[] buf = is.readAllBytes();
		        is.close();

		        RandomAccessFile raf = new RandomAccessFile(fd, "rw");
		        raf.write(buf);
		        raf.close();

				if (fd.exists() && min_valid_size <= fd.length()) {
					System.out.printf("wrote '%s' size=%,10d %s%n",     fd.getPath(), buf.length, TimeOfDay.local_time(System.currentTimeMillis()));
				} else {
					System.out.printf("error '%s' size=%,10d %s !!!%n", fd.getPath(), buf.length, TimeOfDay.local_time(System.currentTimeMillis()));
				}
			} catch (Exception e) {
				System.out.println(e);
		        try {
		        	// if something goes wrong, sleep 15 seconds
					Thread.sleep(15 * 1000);
				} catch (InterruptedException ie) {
					;
				}
			}
		}

		// check whether the download was successful
		if (fd.exists() && min_valid_size <= fd.length()) return true;

		return false;
	}

	public boolean read_tile_from_disk(ImageList tile) throws IOException
	{
		// we don't know which tile to read
		if (tile == null) return false;

		// check for a valid right ascension and declination
		if (tile.ctr_ra_deg < 0 || 360 <= tile.ctr_ra_deg || tile.ctr_de_deg < -90 || 90 < tile.ctr_de_deg) {
			return false;
		}

		// make sure the new image is centered on a cached image
		tile.ctr_ra_deg = ra_to_ra(tile.ctr_ra_deg);
		tile.ctr_de_deg = de_to_de(tile.ctr_de_deg);

		if (tile.image == null) {
			// we have not read this image from disk before
			// width and height in degree minutes of arc
			short[][] size_list = {{60,60}, {30,30}, {15,15}};
			for (short[] size: size_list) {
				short width_min  = size[0];
				short height_min = size[1];
				String fn = (width_min == 60 && height_min == 60) ?
					String.format("%06.2f%s%05.2f.gif",       tile.ctr_ra_deg, ((tile.ctr_de_deg<0)?"-":"+"), Math.abs(tile.ctr_de_deg)) :
					String.format("%06.2f%s%05.2f.%dx%d.gif", tile.ctr_ra_deg, ((tile.ctr_de_deg<0)?"-":"+"), Math.abs(tile.ctr_de_deg), width_min, height_min);

				tile.width_min  = width_min;
				tile.height_min = height_min;

				double width_deg  = width_min  / PracticalAstronomy.minutes_per_degree;
				double height_deg = height_min / PracticalAstronomy.minutes_per_degree;
				tile.min_ra_deg = tile.ctr_ra_deg - width_deg  / 2;
				tile.max_ra_deg = tile.ctr_ra_deg + width_deg  / 2;
				tile.min_de_deg = tile.ctr_de_deg - height_deg / 2;
				tile.max_de_deg = tile.ctr_de_deg + height_deg / 2;

				String[] band_list = {red_dir_name, blue_dir_name};
				for (String band: band_list) {
					String dir = String.format("%s/%03d", band, (int) tile.ctr_ra_deg);
					File   fd  = new File(dir, fn);

					if (fd.exists() && min_valid_size < fd.length()) {
						tile.image_path = fd.getCanonicalPath();
						tile.length     = (int) fd.length();
						byte[] buf      = new byte[tile.length];
				        RandomAccessFile raf = new RandomAccessFile(fd, "r");
				        raf.readFully(buf);
				        raf.close();
				        ImageIcon icon = new ImageIcon(buf);
				        if (icon != null && (tile.image=icon.getImage()) != null) {
				        	// we successfully read the image from disk and created an Image
				        	tile.width_px      = tile.image.getWidth(null);
				        	tile.height_px     = tile.image.getHeight(null);
				        	tile.ctr_ra_px     = (tile.width_px  + 1) / 2;
				        	tile.ctr_de_px     = (tile.height_px + 1) / 2;
	
				        	tile.ra_px_per_min = tile.width_px / tile.width_min;
				        	tile.ra_px_per_deg = tile.ra_px_per_min * PracticalAstronomy.minutes_per_degree;
				        	tile.ra_px_per_rad = tile.ra_px_per_deg * PracticalAstronomy.degrees_per_radian;
				        	tile.de_px_per_min = tile.height_px / tile.height_min;
				        	tile.de_px_per_deg = tile.de_px_per_min * PracticalAstronomy.minutes_per_degree;
				        	tile.de_px_per_rad = tile.de_px_per_deg * PracticalAstronomy.degrees_per_radian;
				        	tile.px_per_rad    = Math.min(tile.ra_px_per_rad, tile.de_px_per_rad);
				        	tile.projector     = new GnomonicProjector(tile.ctr_ra_deg, tile.ctr_de_deg);
							break;
						} else {
							tile.image_path = null;
							tile.length     = 0;
				        }
					}
				}
				if (tile.image != null) break;
			}
		}
		
		return (tile.image != null);
	}

	/**
	 * Get a list of all tiles in the image cache that overlap with the area centered on (ra,de) with the width and height.
	 * 
	 * @param ctr_ra_deg Right ascension, in degrees, of the center of the area.
	 * @param ctr_de_deg Declination, in degrees, of the center of the area.
	 * @param width_min Width of the area in minutes of arc (1/60th degree).
	 * @param height_min Height of the area in minutes of arc (1/60th degree).
	 * @return An array of ImageList objects, that describe each tile in the array.
	 * @throws IOException
	 */
	public ImageList[] get_image_list(double ctr_ra_deg, double ctr_de_deg, double width_min, double height_min) throws IOException
	{
		// double ctr_ra_deg = PracticalAstronomy.hours_to_degrees(ctr_ra_hrs);
		double width_deg  = width_min  / PracticalAstronomy.minutes_per_degree;
		double height_deg = height_min / PracticalAstronomy.minutes_per_degree;
		// System.out.printf("%.3f %.3f%n", width_deg, height_deg);

		double img_min_ra_deg = ctr_ra_deg - width_deg / 2;			// left boundary of image
		double img_max_ra_deg = ctr_ra_deg + width_deg / 2;			// right boundary of image

		double img_min_de_deg = ctr_de_deg - height_deg / 2;		// bottom boundary of image
		img_min_de_deg = (img_min_de_deg < -90) ? -90 : img_min_de_deg;
		double img_max_de_deg = ctr_de_deg + height_deg / 2;		// top boundary of image
		img_max_de_deg = ( 90 < img_max_de_deg) ?  90 : img_max_de_deg;
		
		// System.out.printf("(%.3f, %.3f) %.3f %.3f %.3f %.3f%n", ctr_ra_deg, ctr_de_deg, img_min_ra_deg, img_max_ra_deg, img_min_de_deg, img_max_de_deg);

		double ra_min_deg = Round.round_to_neg_inf(2*img_min_ra_deg)/2;
		double ra_max_deg = Round.round_to_pos_inf(2*img_max_ra_deg)/2;
		double de_min_deg = Round.round_to_neg_inf(2*img_min_de_deg)/2;
		double de_max_deg = Round.round_to_pos_inf(2*img_max_de_deg)/2;
		Queue<ImageList> que = new Queue<ImageList>();
		for (double ra=ra_min_deg-width_deg; ra <= ra_max_deg+width_deg; ra+=0.5) {
			double r = (ra < 0) ? ra + 360 : ((360 <= ra) ? ra - 360 : ra);
			for (double de=de_min_deg-height_deg; de <= de_max_deg+height_deg; de+=0.5) {
				double d = (de < -90) ? -90 : ((90 < de) ? 90 : de);
				ImageList tile = lookup_cache(r, d);
				if (intersects_bounding_box(tile, img_min_ra_deg, img_max_ra_deg, img_min_de_deg, img_max_de_deg)) {
					que.append(tile);
				}
			}
		}
		
		ImageList[] list = new ImageList[que.length()];
		for (int i=0; i < list.length; i++) {
			list[i] = que.remove();
		}

		return list;
	}

	/**
	 * Determine whether the tile intersects the specified bounding box.
	 * 
	 * @param tile Tile to be checked.
	 * @param min_ra_deg Left side of the bounding box.
	 * @param max_ra_deg Right side of the bounding box.
	 * @param min_de_deg Top of the bounding box.
	 * @param max_de_deg Bottom of the bounding box.
	 * @return True if the tile intersects the bounding box, false otherwise.
	 */
	public static boolean intersects_bounding_box(ImageList tile, double min_ra_deg,  double max_ra_deg,  double min_de_deg,  double max_de_deg)
	{
		if (tile.max_ra_deg < min_ra_deg || max_ra_deg < tile.min_ra_deg) return false;
		if (tile.max_de_deg < min_de_deg || max_de_deg < tile.min_de_deg) return false;

		return true;
	}

	/**
	 * Translate a right ascension coordinate (in degrees) to an image cache index.
	 * 
	 * @param ra Right ascension value to be translated.
	 * @return Image cache index.
	 */
	public static int ra_to_idx(double ra)
	{
		int idx = (int) (2 * ra);
		idx = (idx < 0 || 720 <= idx) ? -1 : idx;

		return idx;
	}

	/**
	 * Translate an image cache index to a right ascension coordinate.
	 * 
	 * @param idx Image cache index to be translated.
	 * @return Right ascension coordinate (in degrees) of the center of the corresponding tile.
	 */
	public static double idx_to_ra(int idx)
	{
		return (double) ((double) idx / 2.0);
	}

	/**
	 * Translate an arbitrary right ascension coordinate (in degrees) to the right ascension 
	 * of the center of the corresponding tile. 
	 * 
	 * @param ra Right ascension coordinate to be translated.
	 * @return The right ascension of the center of the corresponding tile.
	 */
	public static double ra_to_ra(double ra)
	{
		return idx_to_ra(ra_to_idx(ra));
	}

	/**
	 * Translate an arbitrary declination to the index of the corresponding image cache tile.
	 * 
	 * @param de Declination (in degrees) to be translated.
	 * @return Index of the corresponding tile.
	 */
	public static int de_to_idx(double de)
	{
		int idx = (int) (2 * de + 180);
		idx = (idx < 0 || 361 <= idx) ? -1 : idx;

		return idx;
	}

	/**
	 * Translate an image cache index to the declination (in degrees) of the center of the
	 * corresponding image cache tile.
	 * 
	 * @param idx Index to be translated.
	 * @return The declination of the center of the corresponding tile.
	 */
	public static double idx_to_de(int idx)
	{
		return (double) ((double) (idx - 180) / 2.0);
	}

	/**
	 * Translate an arbitrary declination (in degrees) to the declination of the center of the
	 * corresponding image cache tile.
	 * 
	 * @param de Declination to be translated.
	 * @return Declination of the center of the corresponding tile.
	 */
	public static double de_to_de(double de)
	{
		return idx_to_de(de_to_idx(de));
	}


	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage image_to_buffered_image(Image img)
	{
	    if (img instanceof BufferedImage) {
	        return (BufferedImage) img;
	    }

	    // create a buffered image with transparency
	    BufferedImage buf_img = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // draw the image on to the buffered image
	    Graphics2D br = buf_img.createGraphics();
	    br.drawImage(img, 0, 0, null);
	    br.dispose();

	    // return the buffered image
	    return buf_img;
	}

	public static void main(String[] argv) throws IOException
	{
		String d = "D:\\data\\nightsky\\cache";
		ImageCache cache = new ImageCache(d);
		ImageList[] list = cache.get_image_list(42.325, -82.675, 30, 30);
		System.out.println(list.length);
		for (ImageList il: list) {
			System.out.printf("%s: %4d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), il.toString());
		}

		double lmc_ra_hrs = CelestialCalculations.hms_to_decimal_hours(5, 23, 0);
		double lmc_ra_deg = CelestialCalculations.decimal_hours_to_degrees(lmc_ra_hrs);
		double lmc_de_deg = CelestialCalculations.dms_to_decimal_degrees(-69, 45, 0);
		ImageList elt = cache.lookup_cache(lmc_ra_deg, lmc_de_deg);
		System.out.println(elt);

		File fd = new File(d);
		System.out.println(fd.getUsableSpace());

		BufferedImage bi = image_to_buffered_image(elt.image);

		ImageIcon icon = new ImageIcon(bi);
		JLabel label = new JLabel(icon);
		JFrame f = new JFrame("Animation");
	    f.getContentPane().add(label);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.pack();
	    f.setLocationRelativeTo(null);
	    f.setVisible(true);
	}
}
