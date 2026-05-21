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


import java.awt.image.BufferedImage;
import java.awt.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lib.astro.PracticalAstronomy;
import lib.math.misc.Round;
import lib.pack.PackRaw;

/**
 * <p> The image database consists of root directory and a collection of 
 * subdirectories, numbered 000 through 359, each representing a unique
 * right ascension, and each containing 180 tiles for that right ascension. 
 * All tiles are named xxxyyy.pxdb, where "xxx" is the three digit right 
 * ascension and "yyy" is the signed two digit declination (-90 to +89), 
 * both in whole degrees, of the lower left corner of the tile. "pxdb" is 
 * an abbreviation for "pixel database". The total size, when all tiles 
 * are stored on disk, is about 250 gigabytes.
 * 
 * <p> Individual tiles represent a 1 degree x 1 degree patch of sky. Files 
 * contain pixels stored as a 2D array of byte-sized grey scale values, and 
 * all data are compressed before storing in a file.
 * 
 * <p> The number of pixels varies from one tile to the next, based on what 
 * could be downloaded from the web. The dimension sizes are recorded as two 
 * short values at the beginning of the file, and pixels are stored as bytes 
 * in order of [0][0] to [max][max], with the second dimension on the inner
 * loop. The primary dimension represents the right ascension, the secondary 
 * dimension represents the declination of the pixel. All pixels are linearly
 * spaced, which causes a distortion in the right ascension for tiles that are
 * closer to the north and south poles.
 */
public class ImageDatabase {
	/**
	 * File extension used for patches of sky in the image database.
	 */
	public static final String file_ext = "pxdb";

	/**
	 * Tile cache. Each tile is indexed by its location (right ascension 
	 * and declination, in whole degrees) in equatorial coordinates.
	 */
	public final Hashtable<Integer, Tile> cache = new Hashtable<Integer, Tile>();

	/**
	 * Location of the root directory for the file cache. A location of "null"
	 * indicates that all image database services are needed, but all data is
	 * to be retrieved directly from the web. Tiles that are missing from the 
	 * database are also retrieved from the web, then written to the database
	 * (like a file cache).
	 */
	public final String root_dir_name;

	/**
	 * Create a new ImageDatabase object. "root" represents the name of the 
	 * root directory of the database. Below the root are subdirectories 
	 * containing tiles (1 degree x 1 degree patches of sky), as described in 
	 * the class comments. A directory structure that is incomplete (is missing 
	 * some tiles) causes missing tiles to be downloaded from the web and stored 
	 * in the database as they are neede. A null directory causes tiles to be 
	 * downloaded from the web, also as they are needed.
	 * 
	 * @param root File system location of the root database directory.
	 */
	public ImageDatabase(String root) 
	{
		root_dir_name = (root == null || root.equals("")) ? null : root;
	}

	/**
	 * Clears the temporary image cache of all accumulated tiles. This routine is
	 * needed because image files are large and memory is usually smaller than the
	 * 250 gigabyte database.
	 */
	public void clear_cache()
	{
		cache.clear();
	}


	/**
	 * Create a buffered image of the indicated size and location in the sky.
	 * 
	 * @param ctr_ra_hrs Image center right ascension in hours.
	 * @param ctr_de_deg Image center declination in degrees.
	 * @param width_min  Image width in R.A. in minutes of arc (1/60th degree).
	 * @param height_min Image height in declination in minutes of arc (1/60th degree).
	 * @param width_px   Image width in pixels.
	 * @param height_px  Image height in pixels.
	 * 
	 * @return A buffered image of the indicated part of the sky.
	 * @throws IOException 
	 */
	public byte[][] get_image(double ctr_ra_deg, double ctr_de_deg, double width_min, double height_min, int width_px, int height_px) throws IOException
	{
		double width_deg  = width_min  / PracticalAstronomy.minutes_per_degree;
		double height_deg = height_min / PracticalAstronomy.minutes_per_degree;

		// allocate space for the new image
		byte[][] grey = new byte[width_px][height_px];
		MapEquatorialToPlane map = new MapEquatorialToPlane(ctr_ra_deg, ctr_de_deg, width_deg, height_deg, width_px, height_px);
		
		for (int i=0; i < grey.length; i++) {
			for (int j=0; j < grey[i].length; j++) {
				// get the equatorial coordinates of this pixel
				double[] eq = map.pl_pu_to_eq_deg(i, j);
				
				if (eq != null) {
					double eq_ra_deg = eq[0];
					double eq_de_deg = eq[1];
	
					// lookup the tile that contains those equatorial coordinates
					Tile tile = lookup(eq_ra_deg, eq_de_deg);

					if (tile != null) {
						// get the pixel that corresponds to those equatorial coordinates
						int pixel = tile.get_pixel(eq_ra_deg, eq_de_deg);

						// store the pixel in the corresponding location in the grey array
						grey[i][j] = (byte) (pixel & 0xFF);

						/*/
						System.out.printf("%s: %4d: grey[%3d][%3d] = (%s, %s)", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), i, j, 
								PracticalAstronomy.decimal_degrees_to_str_dms(eq_ra_deg), 
								PracticalAstronomy.decimal_degrees_to_str_dms(eq_de_deg));
						System.out.printf(" = 0x%02X", (int) (pixel & 0xFF));
						System.out.printf("%n");
						/*/
					}
				}
			}
		}

		// clear the tile cache when we're done
		clear_cache();

		return grey;
	}


	/**
	 * class Tile represents a 1 degree x 1 degree patch of the sky from which an image may be constructed.
	 */
	public class Tile {
		/**
		 * Right ascension, in hours, of the image left hand side.
		 */
		public final double min_ra_hrs;
		
		/**
		 * Right ascension, in hours, of the image center.
		 */
		public final double ctr_ra_hrs;

		/**
		 * Right ascension, in hours, of the image right hand side.
		 */
		public final double max_ra_hrs;

		/**
		 * Right ascension, in degrees, of the image left hand side.
		 */
		public final double min_ra_deg;

		/**
		 * Right ascension, in degrees, of the image center.
		 */
		public final double ctr_ra_deg;

		/**
		 * Right ascension, in degrees, of the image right hand side.
		 */
		public final double max_ra_deg;

		/**
		 * Right ascension, in radians, of the image left hand side.
		 */
		public final double min_ra_rad;

		/**
		 * Right ascension, in radians, of the image center.
		 */
		public final double ctr_ra_rad;

		/**
		 * Right ascension, in radians, of the image right hand side.
		 */
		public final double max_ra_rad;

		/**
		 * Declination, in degrees, of the image bottom.
		 */
		public final double min_de_deg;

		/**
		 * Declination, in degrees, of the image center.
		 */
		public final double ctr_de_deg;

		/**
		 * Declination, in degrees, of the image top.
		 */
		public final double max_de_deg;

		/**
		 * Declination, in radians, of the image bottom.
		 */
		public final double min_de_rad;

		/**
		 * Declination, in radians, of the image center.
		 */
		public final double ctr_de_rad;

		/**
		 * Declination, in radians, of the image top.
		 */
		public final double max_de_rad;

		/**
		 * Right ascension, in pixels, of the image left hand side (0).
		 */
		public final double min_ra_px;

		/**
		 * Right ascension, in pixels, of the image center.
		 */
		public final double ctr_ra_px;

		/**
		 * Right ascension, in pixels, of the image right hand side (last+1).
		 */
		public final double max_ra_px;

		/**
		 * Number of pixels in the right ascension direction.
		 */
		public final double len_ra_px;

		/**
		 * Right ascension, pixels per degree.
		 */
		public final double ra_px_per_deg;

		/**
		 * Right ascension, pixels per radian.
		 */
		public final double ra_px_per_rad;

		/**
		 * Declination, in pixels, of the image bottom (last+1).
		 */
		public final double min_de_px;

		/**
		 * Declination, in pixels, of the image center.
		 */
		public final double ctr_de_px;

		/**
		 * Declination, in pixels, of the image top (0).
		 */
		public final double max_de_px;

		/**
		 * Number of pixels in the declination direction.
		 */
		public final double len_de_px;

		/**
		 * Declination, pixels per degree.
		 */
		public final double de_px_per_deg;

		/**
		 * Declination, pixels per radian.
		 */
		public final double de_px_per_rad;

		/**
		 * <p> Pixel data, stored as a 2D array of bytes. The first (i) dimension, data[i][*], 
		 * represents pixels along the image horizontal axis, i.e., right ascension. The second 
		 * (j) dimension, data[*][j], represents pixels along the image vertical axis, i.e., 
		 * declination. The upper left hand corner (ULC) maps to data[min_ra_px][max_de_px] == data[0][0]. 
		 * The lower right hand corner (LRC) maps to data[max_ra_px][min_de_px] == data[len-1][len-1]. 
		 * 
		 * <p> All values in the array are unsigned grey scale bytes, in the range 0 to 255. 
		 * To obtain a value from the array, use the expression (short) (data[i][j] & 0xFF). 
		 */
		public final byte[][] data;

		// note that image data is stored as image[width][height], using the map below:
		// ULC (upper left corner) is at pixel (0,0) and equatorial coordinate (min_ra,max_de).
		// equatorial coordinate (min_ra,min_de) is located at the image LLC (lower left corner).
		// ra_deg and de_deg are rounded down to the next nearest integer before processing.
		// 
		//               (min_ra,max_de) = (0,0) +-------------------+ (max_ra,max_de) = (max_x,0)
		//                                       |ULC      N      URC|
		//                                       |                   |
		//                                       |W                 E|
		//                                       |                   |
		//                                       |LLC      S      LRC|
		// (ra,de) = (min_ra,min_de) = (0,max_y) +-------------------+ (max_ra,min_de) = (max_x,max_y)

		/**
		 * <p> Create a tile for the given equatorial coordinates, in degrees. Image data is read from disk, 
		 * if it's available. If the image is not available on disk, the image is obtained from the web 
		 * and converted to an image stored on disk. The image is downloaded from URL:
		 * "https://archive.stsci.edu/cgi-bin/dss_search?v=%s&r=%fd&d=%f&e=J2000&w=%d&h=%d&f=gif"
		 * 
		 * <p> The parameters are: band (red or blue), image center right ascension in decimal degrees, 
		 * image center declination in decimal degrees, width in minutes of arc, and height in minutes 
		 * of arc. (See the function download_tile_from_web() for details.)
		 * 
		 * @param llc_ra_deg The right ascension coordinate, in degrees, of the lower left corner of the tile image.
		 * @param llc_de_deg The declination coordinate, in degrees, of the lower left corner of the time image.
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public Tile(double llc_ra_deg, double llc_de_deg) throws IllegalArgumentException, IOException
		{
			if (llc_ra_deg < 0 || 360 <= llc_ra_deg || llc_de_deg < -90 || 90 < llc_de_deg) {
				throw new IllegalArgumentException("0 <= ra < 360 && -90 <= de <= 90");
			}

			llc_de_deg = (llc_de_deg == 90) ? 89 : llc_de_deg;

			min_ra_deg = Round.round_to_neg_inf(llc_ra_deg);
			max_ra_deg = min_ra_deg + 1;
			ctr_ra_deg = (min_ra_deg + max_ra_deg) / 2;

			min_ra_hrs = PracticalAstronomy.degrees_to_hours(min_ra_deg);
			ctr_ra_hrs = PracticalAstronomy.degrees_to_hours(ctr_ra_deg);
			max_ra_hrs = PracticalAstronomy.degrees_to_hours(max_ra_deg);

			min_ra_rad = Math.toRadians(min_ra_deg);
			ctr_ra_rad = Math.toRadians(ctr_ra_deg);
			max_ra_rad = Math.toRadians(max_ra_deg);

			min_de_deg = Round.round_to_neg_inf(llc_de_deg);
			max_de_deg = min_de_deg + 1;
			ctr_de_deg = (min_de_deg + max_de_deg) / 2;

			min_de_rad = Math.toRadians(min_de_deg);
			ctr_de_rad = Math.toRadians(ctr_de_deg);
			max_de_rad = Math.toRadians(max_de_deg);

			byte[][] tmp_data = read(min_ra_deg, min_de_deg);
			if (tmp_data == null) {
				// if the data is not on disk, it needs to be brought down from
				// the web, converted to a pxdb file, and stored in the database.
				tmp_data = download_tile_from_web(llc_ra_deg, llc_de_deg);
				write(llc_ra_deg, llc_de_deg, tmp_data);

				if (tmp_data == null) {
					throw new IllegalArgumentException("data not available");
				}
			}

			data = tmp_data;

			len_ra_px = data.length;
			min_ra_px = 0;
			max_ra_px = len_ra_px - 1;
			ctr_ra_px = len_ra_px / 2;

			ra_px_per_deg = (len_ra_px / (max_ra_deg - min_ra_deg));
			ra_px_per_rad = (len_ra_px / (max_ra_rad - min_ra_rad));

			len_de_px = data[0].length;
			min_de_px = len_de_px - 1;
			max_de_px = 0;
			ctr_de_px = len_de_px / 2;

			de_px_per_deg = (len_de_px / (max_de_deg - min_de_deg));
			de_px_per_rad = (len_de_px / (max_de_rad - min_de_rad));
		}

		// no projection from equatorial sphere to image plane is needed to locate a pixel
		// because all pixels are stored and located linearly by R.A. and dec.
		// this means stars closer to the poles are increasingly smeared horizontally.
		// equatorial north pole is dark (i.e., 0), but polaris is just off the north pole.

		// images grow left-to-right and top-to-bottom, that is, the upper left-hand corner
		// of the image is pixel (0,0), where the first coordinate is the vertical and the 
		// second coordinate is the horizontal. note the dimensions in image pixel coordinates
		// is reversed compared to equatorial (ra,de) coordinates. the data array is stored
		// according to the equatorial ordering, though, as data[ra][de], rather than the 
		// standard pixel ordering.

		/**
		 * Convert the right ascension and declination to pixel coordinates for this tile, then
		 * return the contents of the pixel it corresponds to. If the pixel is outside of the tile,
		 * return 0.
		 * 
		 * @param ra_deg Right ascension of the pixel, in degrees.
		 * @param de_deg Declination of the pixel, in degrees.
		 * @return The contents of the pixel, in grey scale (range 0-255).
		 */
		public int get_pixel(double ra_deg, double de_deg)
		{
			if (data == null || ra_deg < 0 || 360 <= ra_deg || de_deg < -90 || 90 <= de_deg) return 0;

			int[]  px = get_ra_de_px(ra_deg, de_deg);
			int ra_px = px[0];
			int de_px = px[1];
			int pixel = get_pixel(ra_px, de_px);

			/*/
			System.out.printf("%s: %4d: (%s, %s) = data[%,4d][%,4d] = 0x%02X%n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), 
					PracticalAstronomy.decimal_degrees_to_str_dms(ra_deg), 
					PracticalAstronomy.decimal_degrees_to_str_dms(de_deg),
					ra_px, de_px, pixel);
			/*/

			return pixel;
		}

		/**
		 * Return the contents of the indicated pixel, in grey scale (range 0-255).
		 * @param ra_px Pixel coordinate in the right ascension (horizontal) direction.
		 * @param de_px Pixel coordinate in the declination (vertical) direction.
		 * @return The contents of the pixel, in grey scale (range 0-255).
		 */
		public int get_pixel(int ra_px, int de_px)
		{
			// System.out.printf("%s: %4d: (%d, %d) = [%,4d][%,4d]%n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), data.length, data[0].length, ra_px, de_px);

			if (data == null || ra_px < 0 || data.length <= ra_px || de_px < 0 || data[ra_px].length <= de_px) return 0;

			int pixel = (int) (data[ra_px][de_px] & 0xFF);
			// System.out.printf("%s: %4d: data[%,4d][%,4d]=0x%02X%n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), ra_px, de_px, pixel);
			
			return pixel;
		}

		/**
		 * Convert the equatorial coordinates of (right ascension, declination), in degrees, to pixel 
		 * coordinates (r.a. in pixels, dec. in pixels) specific to this tile.
		 * @param ra_deg Right ascension of the pixel, in degrees.
		 * @param de_deg Declination of the pixel, in degrees.
		 * @return int[]{r.a. pixel index, declination pixel index}.
		 */
		public int[] get_ra_de_px(double ra_deg, double de_deg)
		{
			// System.out.printf("%s: %4d: in:(%f, %f) deg:(%f,%f) px:(%f,%f) ppd:(%f,%f) %n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), ra_deg, de_deg, min_ra_deg, min_de_deg, min_ra_px, min_de_px, ra_px_per_deg, de_px_per_deg);

			int ra_px = (int) Round.round_to_nearest(min_ra_px + (ra_deg - min_ra_deg) * ra_px_per_deg);
			int de_px = (int) Round.round_to_nearest(min_de_px - (de_deg - min_de_deg) * de_px_per_deg);
			// System.out.printf("%s: %4d: (%d, %d) = [%,4d][%,4d]%n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), data.length, data[0].length, ra_px, de_px);

			return new int[] {ra_px, de_px};
		}

		/**
		 * Convert the pixel coordinates of (right ascension in pixels, declination in pixels) to 
		 * equatorial coordinates, in degrees.
		 * @param ra_px Pixel coordinate in the right ascension (horizontal) direction.
		 * @param de_px Pixel coordinate in the declination (vertical) direction.
		 * @return double[]{r.a. in degrees, declination in degrees}.
		 */
		public double[] get_ra_de_deg(int ra_px, int de_px)
		{
			double ra_deg = min_ra_deg + (ra_px - min_ra_px) / ra_px_per_deg;
			double de_deg = min_de_deg + (de_px - min_de_px) / de_px_per_deg;

			return new double[] {ra_deg, de_deg};
		}
	}

	/**
	 * Convert the equatorial coordinates to pixel coordinates.
	 * 
	 * @param ra_deg     Equatorial right ascension, in degrees.
	 * @param de_deg     Equatorial declination, in degrees.
	 * @param llc_ra_deg Image minimum right ascension (lower left corner), in degrees.
	 * @param llc_de_deg Image minimum declination (lower left corner), in degrees.
	 * @param width_deg  Image width in degrees.
	 * @param height_deg Image height in degrees.
	 * @param width_px   Image width in pixels.
	 * @param height_px  Image height in pixels.
	 * @return Pixel indexes in the image data.
	 */
	public static int[] get_ra_de_px(double ra_deg, double de_deg, double llc_ra_deg, double llc_de_deg, double width_deg, double height_deg, double width_px, double height_px)
	{
		double min_ra_px = 0;
		double max_ra_px = width_px;
		double min_de_px = height_px;
		double max_de_px = 0;
		double ra_px_per_deg = ((max_ra_px - min_ra_px) / width_deg);
		double de_px_per_deg = ((max_de_px - min_de_px) / height_deg);

		int ra_px = (int) Round.round_to_nearest(min_ra_px + (ra_deg - llc_ra_deg) * ra_px_per_deg);
		int de_px = (int) Round.round_to_nearest(min_de_px + (de_deg - llc_de_deg) * de_px_per_deg);

		return new int[] {ra_px, de_px};
	}

	/**
	 * Convert the equatorial coordinates to pixel coordinates.
	 * 
	 * @param ra_px      Pixel index in right ascension.
	 * @param de_px      Pixel index in declination.
	 * @param llc_ra_deg Image minimum right ascension (lower left corner), in degrees.
	 * @param llc_de_deg Image minimum declination (lower left corner), in degrees.
	 * @param width_deg  Image width in degrees.
	 * @param height_deg Image height in degrees.
	 * @param width_px   Image width in pixels.
	 * @param height_px  Image height in pixels.
	 * @return Corresponding right ascension and declination, in degrees, as double[]{ra, de}.
	 */
	public static double[] get_ra_de_deg(int ra_px, int de_px, double llc_ra_deg, double llc_de_deg, double width_deg, double height_deg, double width_px, double height_px)
	{
		double min_ra_px = 0;
		double max_ra_px = width_px;
		double min_de_px = height_px;
		double max_de_px = 0;
		double ra_px_per_deg = ((max_ra_px - min_ra_px) / width_deg);
		double de_px_per_deg = ((max_de_px - min_de_px) / height_deg);

		double ra_deg = llc_ra_deg + (ra_px - min_ra_px) / ra_px_per_deg;
		double de_deg = llc_de_deg + (de_px - min_de_px) / de_px_per_deg;

		return new double[] {ra_deg, de_deg};
	}

	/**
	 * Retrieve a tile for use. The tile may be: (1) in the cache, 
	 * (2) on disk, or (3) on the web. Once a tile is successfully
	 * retrieved, it resides in all three locations. All tiles
	 * represent a 1 degree x 1 degree patch of the sky. 
	 * 
	 * <p> Tiles are identified by their equatorial location (right 
	 * ascension and declination), and any location within the tile
	 * is acceptable as an address. 
	 * 
	 * @param ra_deg Right ascension of the lower left corner of the tile.
	 * @param de_deg Declination of the lower left corner of the tile.
	 * @return The requested tile.
	 * @throws IOException
	 */
	public Tile lookup(double ra_deg, double de_deg) throws IOException
	{
		// check the cache for the tile
		Tile elt = cache.get(pack(ra_deg, de_deg));

		if (elt == null) {
			// look up the data on disk, or if not available, on the web
			elt = new Tile(ra_deg, de_deg);
			if (elt.data != null) {
				// data is available, add the tile to the cache
				cache.put(pack(ra_deg, de_deg), elt);
			} else {
				// data is not available, that is, it is not stored in the 
				// image database and not available on the web
				elt = null;
			}
		}

		return elt;
	}

	/**
	 * Truncate the right ascension and declination to integers, and
	 * pack both integers into an Integer object.
	 * 
	 * @param ra_deg
	 * @param de_deg
	 * @return An Integer object containing the truncated equatorial coordinates.
	 */
	public Integer pack(double ra_deg, double de_deg)
	{
		short ra = (short) Round.round_to_neg_inf(ra_deg);
		short de = (short) Round.round_to_neg_inf(de_deg);
		return Integer.valueOf((0xFFFF0000 & (ra << 16)) | (0x0000FFFF & (de << 0)));
	}

	/**
	 * Read the specified tile from disk.
	 * 
	 * @param ra_deg Right ascension, in degrees, of some point within the tile boundaries.
	 * @param de_deg Declination, in degrees, of some point within the tile boundaries.
	 * @return
	 * @throws IOException
	 */
	public byte[][] read(double ra_deg, double de_deg) throws IOException
	{
		double llc_ra_deg = Round.round_to_neg_inf(ra_deg);
		double llc_de_deg = Round.round_to_neg_inf(de_deg);

		if (llc_ra_deg < 0 || 360 <= llc_ra_deg || llc_de_deg < -90 || 90 <= llc_de_deg) return null;

		if (root_dir_name == null) return null;

		String file_name = get_file_name(llc_ra_deg, llc_de_deg);
		File fd = new File(file_name);
		if (! fd.canRead()) {
			return null;
		}

		byte[] deflated = new byte[(int) fd.length()];
		RandomAccessFile raf = new RandomAccessFile(fd, "r");
		raf.readFully(deflated);
		raf.close();

		byte[][] data = PackRaw.to_inflated_byte_2d_array(deflated);

		return data;
	}

	/**
	 * Write the uncompressed (inflated) data to the specified tile. 
	 * 
	 * @param ra_deg Right ascension, in degrees, of some point within the tile boundaries.
	 * @param de_deg Declination, in degrees, of some point within the tile boundaries.
	 * @param inflated
	 * @return true if successful, false otherwise.
	 * @throws IOException
	 */
	public boolean write(double ra_deg, double de_deg, byte[][] inflated) throws IOException
	{
		double llc_ra_deg = Round.round_to_neg_inf(ra_deg);
		double llc_de_deg = Round.round_to_neg_inf(de_deg);

		if (llc_ra_deg < 0 || 360 <= llc_ra_deg || llc_de_deg < -90 || 90 < llc_de_deg) return false;

		if (root_dir_name == null || inflated == null) return true;

		String dir_name = get_dir_name(llc_ra_deg);
		File fd = new File(dir_name);
		if (! fd.isDirectory() && ! fd.mkdirs()) {
			// failed to make the necessary directories
			return false;
		}

		byte[] deflated = PackRaw.to_deflated_bytes(inflated);

		String file_name = get_file_name(llc_ra_deg, llc_de_deg);
		RandomAccessFile raf = new RandomAccessFile(file_name, "rw");
		raf.setLength(0);
		raf.write(deflated);
		raf.close();

		return true;
	}

	/**
	 * Download the requested 1 degree x 1 degree image from the web.
	 * 
	 * @param ra_deg Right ascension, in degrees, of some point within the tile boundaries.
	 * @param de_deg Declination, in degrees, of some point within the tile boundaries.
	 * @return The image data as a 2D byte array of gray scale values.
	 * @throws IOException
	 */
	public static byte[][] download_tile_from_web(double ra_deg, double de_deg) throws IOException
	{
		double llc_ra_deg = Round.round_to_neg_inf(ra_deg);
		double llc_de_deg = Round.round_to_neg_inf(de_deg);

		double ctr_ra_deg = llc_ra_deg + 0.5;
		double ctr_de_deg = llc_de_deg + 0.5;

		double ra = ctr_ra_deg;
		double de = ctr_de_deg;

		ra = (ra <   0) ? ra + 360 : ((360 <= ra) ? ra - 360 : ra);
		de = (de < -90) ?      -90 : (( 90 <  de) ?       90 : de);

		if (ra < 0 || 360 <= ra || de < -90 || 90 < de) {
			return null;
		}
		
		// image width and height in minutes of arc, and minimum size in bytes
		int width  = 60;
		int height = 60;
		int min_valid_size = 100000;

		String red_band = "poss2ukstu_red";
		String blu_band = "poss2ukstu_blue";
		String uri_fmt  = "https://archive.stsci.edu/cgi-bin/dss_search?v=%s&r=%fd&d=%f&e=J2000&w=%d&h=%d&f=gif";

		// download the image from the web
		byte[][] tile_data = null;
		int max_err = 2;
		for (int t=0; t < max_err; t++) {
			// download the image from the red band
			try {
				String uri_str = String.format(uri_fmt, red_band, ra, de, width, height);
				URI uri = new URI(uri_str);
				URL url = uri.toURL();
		        InputStream is = url.openStream(); 
				byte[] buf = is.readAllBytes();
		        is.close();

		        if (buf.length < min_valid_size) {
		        	uri_str = String.format(uri_fmt, blu_band, ra, de, width, height);
		        	uri = new URI(uri_str);
					url = uri.toURL();
					is = url.openStream(); 
					buf = is.readAllBytes();
			        is.close();
		        }

		        ImageIcon icon = new ImageIcon(buf);
		        Image image = icon.getImage();
				BufferedImage buffered_image = CreateImage.create_buffered_image(image);
				tile_data = CreateImage.rgb_to_grey_byte(CreateImage.get_image_data(buffered_image));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return tile_data;
	}

	/**
	 * Compute the directory name from the equatorial right ascension.
	 * 
	 * @param ra_deg Right ascension, in degrees, of some point within the tile.
	 * @return The directory name where the tile may be stored.
	 */
	private String get_dir_name(double ra_deg)
	{
		if (root_dir_name == null) return null;

		String dir = String.format("%s/%03d", root_dir_name, (int) ra_deg);
		
		return dir;
	}

	/**
	 * Compute the file name, including the directory path, of the tile containing this equatorial coordinate.
	 * 
	 * @param ra_deg Right ascension, in degrees, of some point within the tile boundaries.
	 * @param de_deg Declination, in degrees, of some point within the tile boundaries.
	 * @return The file name, including all path elements.
	 */
	private String get_file_name(double ra_deg, double de_deg)
	{
		if (root_dir_name == null) return null;

		String de_sign = (de_deg < 0) ? "-" : "+";
		String dir = String.format("%s/%03d/%03d%s%02d.%s", root_dir_name, (int) ra_deg, (int) ra_deg, de_sign, (int) Math.abs(de_deg), file_ext);
		
		return dir;
	}

	/**
	 * Show an image.
	 * 
	 * @param tile Tile representing the image to be shown.
	 */
	public static void show(Tile tile)
	{
		String title = PracticalAstronomy.decimal_degrees_to_str_dm(tile.min_ra_deg) + ", " + PracticalAstronomy.decimal_degrees_to_str_dm(tile.min_de_deg);
		show(title, tile);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param tile Tile representing the image to be shown.
	 */
	public static void show(String title, Tile tile)
	{
		show(title, tile.data);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param tile Tile representing the image to be shown.
	 * @param width Image width in pixels.
	 * @param height Image height in pixels.
	 */
	public static void show(String title, Tile tile, int width, int height)
	{
		show(title, tile.data, width, height);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param image Image to be shown.
	 */
	public static void show(String title, Image image)
	{
		show(title, image, 1000, 1000);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param image Image to be shown.
	 * @param width Image width in pixels.
	 * @param height Image height in pixels.
	 */
	public static void show(String title, Image image, int width, int height)
	{
		// PackRaw.to_byte_array(data);
		BufferedImage buffered_image = CreateImage.create_buffered_image(image);
		Image img = buffered_image.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		buffered_image = CreateImage.create_buffered_image(img);
		ImageIcon icon = new ImageIcon(buffered_image);
		JLabel label = new JLabel(icon);
		JFrame g = new JFrame(title);
	    g.getContentPane().add(label);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param image BufferedImage to be shown.
	 */
	public static void show(String title, BufferedImage buffered_image)
	{
		show(title, buffered_image, 1000, 1000);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param image BufferedImage to be shown.
	 * @param width Image width in pixels.
	 * @param height Image height in pixels.
	 */
	public static void show(String title, BufferedImage buffered_image, int width, int height)
	{
		// PackRaw.to_byte_array(data);
		Image img = buffered_image.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		buffered_image = CreateImage.create_buffered_image(img);
		ImageIcon icon = new ImageIcon(buffered_image);
		JLabel label = new JLabel(icon);
		JFrame g = new JFrame(title);
	    g.getContentPane().add(label);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param data 2D byte array containing a grey scale representation of the image to be shown.
	 */
	public static void show(String title, byte[][] data)
	{
		show(title, data, 1000, 1000);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param data 2D byte array containing a grey scale representation of the image to be shown.
	 * @param width Image width in pixels.
	 * @param height Image height in pixels.
	 */
	public static void show(String title, byte[][] data, int width, int height)
	{
		// PackRaw.to_byte_array(data);
		BufferedImage buffered_image1 = CreateImage.create_buffered_image(data);
		Image img = buffered_image1.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		buffered_image1 = CreateImage.create_buffered_image(img);
		ImageIcon icon1 = new ImageIcon(buffered_image1);
		JLabel label1 = new JLabel(icon1);
		JFrame g = new JFrame(title);
	    g.getContentPane().add(label1);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param data   2D byte array containing a grey scale representation of the image to be shown.
	 * @param width  Image width in pixels.
	 * @param height Image height in pixels.
	 * @param alpha  Transparency.
	 */
	public static void show(String title, byte[][] data, int width, int height, int alpha)
	{
		// PackRaw.to_byte_array(data);
		BufferedImage buffered_image1 = CreateImage.create_buffered_image(data, alpha);
		Image img = buffered_image1.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		buffered_image1 = CreateImage.create_buffered_image(img);
		ImageIcon icon1 = new ImageIcon(buffered_image1);
		JLabel label1 = new JLabel(icon1);
		JFrame g = new JFrame(title);
	    g.getContentPane().add(label1);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param data 2D short array containing a grey scale representation of the image to be shown.
	 */
	public static void show(String title, short[][] data)
	{
		show(title, data, 1000, 1000);
	}

	/**
	 * Show an image.
	 * 
	 * @param title Image title.
	 * @param data 2D short array containing a grey scale representation of the image to be shown.
	 * @param width Image width in pixels.
	 * @param height Image height in pixels.
	 */
	public static void show(String title, short[][] data, int width, int height)
	{
		// PackRaw.to_byte_array(data);
		BufferedImage buffered_image1 = CreateImage.create_buffered_image(data);
		Image img = buffered_image1.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		buffered_image1 = CreateImage.create_buffered_image(img);
		ImageIcon icon1 = new ImageIcon(buffered_image1);
		JLabel label1 = new JLabel(icon1);
		JFrame g = new JFrame(title);
	    g.getContentPane().add(label1);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}

	/*
	 * Build a Tile object and store it on disk, based on the contents of an image cache.
	 * 
	 * @param cache Image cache.
	 * @param llc_ra_deg Right ascension, in degrees, of the lower left corner of the tile to be created. 
	 * @param llc_de_deg Declination, in degrees, of the lower left corner of the tile to be created.
	 * @param width_min Tile width, in minutes of arc.
	 * @param height_min Tile height, in minutes of arc.
	 * @throws IOException
	 */
	/*/
	public void build_tile_from_cache(ImageCache cache, double llc_ra_deg, double llc_de_deg, double width_min, double height_min) throws IOException
	{
		// make sure right ascension and declination are truncated to whole degree values
		llc_ra_deg = Round.round_to_neg_inf(llc_ra_deg);
		llc_de_deg = Round.round_to_neg_inf(llc_de_deg);

		// get the name of the tile to be created
		String file_name = get_file_name(llc_ra_deg, llc_de_deg);
		File fd = new File(file_name);
		if (fd.exists()) {
			return;
		}

		// get the image width in degrees rather than minutes of arc
		double width_deg  = width_min  / PracticalAstronomy.minutes_per_degree;
		double height_deg = height_min / PracticalAstronomy.minutes_per_degree;

		// find the center of the image
		double ctr_ra_deg = llc_ra_deg + width_deg  / 2;
		double ctr_de_deg = llc_de_deg + height_deg / 2;

		// look-up the image in the file cache
		ImageList elt = cache.lookup_cache(ctr_ra_deg, ctr_de_deg);
		BufferedImage image = CreateImage.create_buffered_image(elt.image);
		byte[][] elt_data = CreateImage.rgb_to_grey_byte(CreateImage.get_image_data(image));

		// transfer the data from the cached image to the database tile
		int width_px  = (int) elt.width_px;
		int height_px = (int) elt.height_px;
		byte[][] data = new byte[width_px][height_px];	// new image
		for (int ra_px=0; ra_px < data.length; ra_px++) {
			for (int de_px=0; de_px < data[ra_px].length; de_px++) {
				// get the equatorial location of the pixel in the tile
				double[] deg = get_ra_de_deg(ra_px, de_px, llc_ra_deg, llc_de_deg, width_deg, height_deg, width_px, height_px);
				double ra_deg = deg[0];
				double de_deg = deg[1];

				// find the corresponding pixel in the cached image
				double[] px = elt.sphere_to_plane(ra_deg, de_deg);
				int elt_ra = (int) Round.round_to_nearest(px[0]);
				int elt_de = (int) Round.round_to_nearest(px[1]);
				if (0 <= elt_ra && elt_ra < elt_data.length && 0 <= elt_de && elt_de < elt_data[0].length) {
					data[ra_px][de_px] = elt_data[elt_ra][elt_de];
				}
			}
		}
		
		elt_data = null;

		write(llc_ra_deg, llc_de_deg, data);
	}
	/*/


	/*
	 * Generate a full image database from images stored in the image cache.
	 * 
	 * @param database_name
	 * @param cache_name
	 * @throws IOException
	 */
	/*/
	public static void gen_database(String database_name, String cache_name) throws IOException 
	{
		ImageDatabase database = new ImageDatabase(database_name);
		ImageCache    cache    = new ImageCache(cache_name);
		Runtime       runtime  = Runtime.getRuntime();

		for (double llc_ra_deg=0; llc_ra_deg < 360; llc_ra_deg++) {
			for (double llc_de_deg=-90; llc_de_deg < 90; llc_de_deg++) {
				database.build_tile_from_cache(cache, llc_ra_deg, llc_de_deg, 60, 60);
				System.out.printf("%s: %4d: %3.0f, %3.0f %n", NightSkyAtAGlance.METHOD(), NightSkyAtAGlance.LINE(), llc_ra_deg, llc_de_deg);
			}
			System.out.printf("before: procs=%d, memory=%,14d, time=%s%n", runtime.availableProcessors(), runtime.freeMemory(), TimeOfDay.current_time());
			database.clear_cache();
			cache.clear_cache();
			runtime.gc();
			System.out.printf("after : procs=%d, memory=%,14d, time=%s%n", runtime.availableProcessors(), runtime.freeMemory(), TimeOfDay.current_time());
		}
	}
	/*/

	public static void main(String[] args) throws IOException 
	{
		String database_name = "D:\\data\\nightsky\\database\\";

		ImageDatabase database = new ImageDatabase(database_name);

		/*/
		String cache_name    = "D:\\data\\nightsky\\cache\\";
		gen_database(database_name, cache_name);
		/*/


		double llc_ra_deg = 0;
		double llc_de_deg = 0;
		String title = null;
		int width_px  = 1000;
		int height_px = 1000;
		Tile tile = null;

		// silver sliver galaxy
		llc_ra_deg = PracticalAstronomy.hours_to_degrees(2, 22, 36);
		llc_de_deg = PracticalAstronomy.dms_to_decimal_degrees(42, 21, 00);

		title = PracticalAstronomy.decimal_degrees_to_str_dms((int) llc_ra_deg) + ", " + PracticalAstronomy.decimal_degrees_to_str_dms((int) llc_de_deg);
		tile = database.lookup(llc_ra_deg, llc_de_deg);
		show(title, tile, width_px, height_px);

		double ctr_ra_deg = llc_ra_deg - 1*PracticalAstronomy.hours_to_degrees(00, 01, 15);
		double ctr_de_deg = llc_de_deg;
		double width_min  = 60 / 1;
		double height_min = 60 / 1;
		byte[][] grey = database.get_image(ctr_ra_deg, ctr_de_deg, width_min, height_min, width_px, height_px);
		title = PracticalAstronomy.decimal_degrees_to_str_dms(ctr_ra_deg) + ", " + PracticalAstronomy.decimal_degrees_to_str_dms(ctr_de_deg);
		show(title, grey, width_px, height_px, 0xFF);

		System.out.printf("%f, %f%n", ctr_ra_deg, ctr_de_deg);
		System.out.printf("%s, %s%n", PracticalAstronomy.decimal_hours_to_str_hms(PracticalAstronomy.degrees_to_hours(ctr_ra_deg)), PracticalAstronomy.decimal_degrees_to_str_dms(ctr_de_deg));
		System.out.printf("%s, %f%n", PracticalAstronomy.decimal_hours_to_str_hms(PracticalAstronomy.degrees_to_hours(0.3125)), 
				PracticalAstronomy.hours_to_degrees(00, 01, 15));
		System.out.printf("%f, %f%n", PracticalAstronomy.hours_to_degrees(2, 22, 36), PracticalAstronomy.hours_to_degrees(PracticalAstronomy.hms_to_decimal_hours(2, 22, 36)));
	}
}
