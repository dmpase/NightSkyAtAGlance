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


import java.awt.Image;

import lib.astro.PracticalAstronomy;

public class ImageList {
	public ImageCache cache;			// path to the top level cache directory

	public String    image_path;		// path to the image, including the image name (null if DNE)
	public double    ctr_ra_deg;		// right ascension, in degrees, of the image center
	public double    ctr_de_deg;		// declination, in degrees, of the image center
	public double    ctr_ra_px;			// right ascension, in pixels, of the image center
	public double    ctr_de_px;			// declination, in pixels, of the image center
	public double    min_ra_deg;		// minimum ra edge (not normalized, ctr - width/2)
	public double    max_ra_deg;		// maximum ra edge (not normalized, ctr + width/2)
	public double    min_de_deg;		// minimum de edge (not normalized, ctr - height/2)
	public double    max_de_deg;		// maximum de edge (not normalized, ctr + height/2)
	public double    width_min;			// image width in minutes of arc
	public double    height_min;		// image height in minutes of arc
	public double    width_px;			// image width in pixels
	public double    height_px;			// image height in pixels
	public double    ra_px_per_min;		// pixels per minute of arc in right ascension
	public double    ra_px_per_deg;		// pixels per degree of arc in right ascension
	public double    ra_px_per_rad;		// pixels per radian of arc in right ascension
	public double    de_px_per_min;		// pixels per minute of arc in declination
	public double    de_px_per_deg;		// pixels per degree of arc in declination
	public double    de_px_per_rad;		// pixels per radian of arc in declination
	public double    px_per_rad;		// pixels per radian (average of right ascension and declination)
	public int       length;			// number of bytes in the file
	public Image     image;				// 
	public Projector projector;			// 
	
	public ImageList()
	{
		cache         = null;
		image_path    = null;
		ctr_ra_deg    = Double.NaN;
		ctr_de_deg    = Double.NaN;
		ctr_ra_px     = Double.NaN;
		ctr_de_px     = Double.NaN;
		min_ra_deg    = Double.NaN;
		max_ra_deg    = Double.NaN;
		min_de_deg    = Double.NaN;
		max_de_deg    = Double.NaN;
		width_min     = Double.NaN;
		height_min    = Double.NaN;
		width_px      = Double.NaN;
		height_px     = Double.NaN;
		ra_px_per_min = Double.NaN;
		ra_px_per_deg = Double.NaN;
		ra_px_per_rad = Double.NaN;
		de_px_per_min = Double.NaN;
		de_px_per_deg = Double.NaN;
		de_px_per_rad = Double.NaN;
		px_per_rad    = Double.NaN;
		length        = 0;
		image         = null;
		projector     = null;
	}

	public ImageList(ImageCache cache, double ctr_ra_deg, double ctr_de_deg)
	{
		this.cache      = cache;
		this.image_path = null;
		this.ctr_ra_deg = ctr_ra_deg;
		this.ctr_de_deg = ctr_de_deg;

		this.ctr_ra_px     = Double.NaN;
		this.ctr_de_px     = Double.NaN;
		this.min_ra_deg    = Double.NaN;
		this.max_ra_deg    = Double.NaN;
		this.min_de_deg    = Double.NaN;
		this.max_de_deg    = Double.NaN;
		this.width_min     = Double.NaN;
		this.height_min    = Double.NaN;
		this.width_px      = Double.NaN;
		this.height_px     = Double.NaN;
		this.ra_px_per_min = Double.NaN;
		this.ra_px_per_deg = Double.NaN;
		this.ra_px_per_rad = Double.NaN;
		this.de_px_per_min = Double.NaN;
		this.de_px_per_deg = Double.NaN;
		this.de_px_per_rad = Double.NaN;
		this.px_per_rad    = Double.NaN;
		this.length        = 0;
		this.image         = null;
		this.projector     = null;
	}

	/**
	 * 
	 * 
	 * @param ctr_ra_hrs Image center right ascension in hours.
	 * @param ctr_de_deg Image center declination in degrees.
	 * @param width_min  Image width in R.A. minutes of arc (1/60th degree).
	 * @param height_min Image height in declination minutes of arc (1/60th degree).
	 * @param width_px   Image width in pixels.
	 * @param height_px  Image height in pixels.
	 */
	public ImageList(ImageCache cache, double ctr_ra_hrs, double ctr_de_deg, double width_min, double height_min, int width_px, int height_px)
	{
		this.cache         = cache;
		this.image_path    = null;
		this.ctr_ra_deg    = PracticalAstronomy.hours_to_degrees(ctr_ra_hrs);
		this.ctr_de_deg    = ctr_de_deg;
		this.ctr_ra_px     = (width_px  + 1) / 2;
		this.ctr_de_px     = (height_px + 1) / 2;
		this.width_min     = width_min;
		this.height_min    = height_min;
		this.width_px      = width_px;
		this.height_px     = height_px;

		double width_deg   = this.width_min  / PracticalAstronomy.minutes_per_degree;
		double height_deg  = this.height_min / PracticalAstronomy.minutes_per_degree;
		this.min_ra_deg    = this.ctr_ra_deg - width_deg  / 2;
		this.max_ra_deg    = this.ctr_ra_deg + width_deg  / 2;
		this.min_de_deg    = this.ctr_de_deg - height_deg / 2;
		this.max_de_deg    = this.ctr_de_deg + height_deg / 2;

		this.ra_px_per_min = this.width_px / this.width_min;
		this.ra_px_per_deg = this.ra_px_per_min * PracticalAstronomy.minutes_per_degree;
		this.ra_px_per_rad = this.ra_px_per_deg * PracticalAstronomy.degrees_per_radian;
		this.de_px_per_min = this.height_px / this.height_min;
		this.de_px_per_deg = this.de_px_per_min * PracticalAstronomy.minutes_per_degree;
		this.de_px_per_rad = this.de_px_per_deg * PracticalAstronomy.degrees_per_radian;
		this.px_per_rad    = (this.ra_px_per_rad + this.de_px_per_rad) / 2;
		this.length        = 0;
		this.image         = null;
		this.projector     = new GnomonicProjector(ctr_ra_deg, ctr_de_deg);
	}


	public double[] sphere_to_plane(double ra_deg, double de_deg) 
    {
		// System.out.printf("%s: %4d: (%f,%f)eq deg%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ra_deg, de_deg);

		// convert the equatorial sphere coordinates in degrees to radians
        double ra_rad = Math.toRadians(ra_deg);
        double de_rad = Math.toRadians(de_deg);
        // System.out.printf("%s: %4d: (%f,%f)eq rad%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ra_rad, de_rad);

        // project the equatorial sphere coordinates onto the image plane
		double[] rad = projector.sphere_to_plane(ra_rad, de_rad);
		double x_rad = rad[0];
		double y_rad = rad[1];
		// System.out.printf("%s: %4d: (%f,%f)im rad%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), x_rad, y_rad);

        // convert the image planar coordinates in radians to pixels
		// origin of the plane is at the center of the image
        double x_px  = ctr_ra_px + x_rad * ra_px_per_rad;
        double y_px  = ctr_de_px - y_rad * de_px_per_rad;
        // System.out.printf("%s: %4d: (%f,%f)im pix%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), x_px, y_px);

    	return new double[] {x_px, y_px};
    }


    public double[] plane_to_sphere(double x_px, double y_px)
    {
    	// System.out.printf("%s: %4d: (%f,%f)im pix%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), x_px, y_px);

		// convert the image planar coordinates in pixels to radians
    	double x_rad = (x_px - ctr_ra_px) / ra_px_per_rad;
    	double y_rad = (y_px - ctr_de_px) / de_px_per_rad;
    	// System.out.printf("%s: %4d: (%f,%f)im rad%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), x_rad, y_rad);

    	// project the image planar coordinates onto the equatorial sphere
    	double[] rad = projector.plane_to_sphere(x_rad, y_rad);
		double ra_rad = rad[0];
		double de_rad = rad[1];
		// System.out.printf("%s: %4d: (%f,%f)eq rad%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ra_rad, de_rad);

        // convert the equatorial sphere coordinates in radians to degrees
        double ra_deg = Math.toDegrees(ra_rad);
        double de_deg = Math.toDegrees(de_rad);
        // System.out.printf("%s: %4d: (%f,%f)eq deg%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ra_deg, de_deg);

    	return new double[] {ra_deg, de_deg};
    }


	public String toString()
	{
		return String.format("%-55s (%.2f, %.2f) (w=%.0f, h=%.0f) (%.2f, %.2f, %.2f, %.2f) px=(w=%,5.0f, h=%,5.0f) len=%,10d", 
			image_path+",", ctr_ra_deg, ctr_de_deg, width_min, height_min, 
			min_ra_deg, max_ra_deg, min_de_deg, max_de_deg,
			width_px, height_px, length);
	}
}
