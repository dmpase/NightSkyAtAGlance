package lib.stars.image;

/*******************************************************************************
 * Copyright (c) 2026 Douglas M. Pase                                          *
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


import lib.math.misc.Round;

public class MapEquatorialToPlane extends GnomonicProjector {

    // pixels per radian on the tangent plane
    private final double pu_per_rad; // image scale in pixels per radian

    // pixel coordinates of the projection center in the output image
    private final double ctr_ra_pu;		// horizontal (right ascension) image center in planar units (pixels)
    private final double ctr_de_pu;		// vertical (declination) image center in planar units (pixels)
    private final int    width_pu;		// output image width in planar units (pixels, image center to image edge)
    private final int    height_pu;		// output image height in planar units (pixels, image center to image edge)

    /** 
     * Create a mapping from equatorial coordinates (right ascension and declination) to pixels in an image.
     * 
     * @param ctr_ra_deg right ascension of image center in degrees
     * @param ctr_de_deg declination of image center in degrees
     * @param scale image scale in pixels per radian
     * @param w_pu output image width in planar units (pixels)
     * @param h_pu output image height in planar units (pixels)
     */
    public MapEquatorialToPlane(double ctr_ra_deg, double ctr_de_deg, double scale, int w_pu, int h_pu) 
    {
        super(ctr_ra_deg, ctr_de_deg);

        this.pu_per_rad = scale;
        this.width_pu   = w_pu;
        this.height_pu  = h_pu;

        // place projection center at the image center
        this.ctr_ra_pu = w_pu / 2.0;
        this.ctr_de_pu = h_pu / 2.0;
    }
    
    public MapEquatorialToPlane(double ctr_ra_deg, double ctr_de_deg, double width_deg, double height_deg, int w_pu, int h_pu) 
    {
    	super(ctr_ra_deg, ctr_de_deg);

    	/*
		System.out.printf("%s: %3d: RA %s (%f), DE %s, w %s/%d, h %s/%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(PracticalAstronomy.degrees_to_hours(ctr_ra_deg)), ctr_ra_deg, 
				PracticalAstronomy.decimal_degrees_to_str_dms(ctr_de_deg),
				PracticalAstronomy.decimal_degrees_to_str_dms(width_deg),  w_pu,
				PracticalAstronomy.decimal_degrees_to_str_dms(height_deg), h_pu);
		*/

    	this.pu_per_rad = Math.max(w_pu / Math.toRadians(width_deg), h_pu / Math.toRadians(height_deg));
        this.width_pu   = w_pu;
        this.height_pu  = h_pu;

        // place projection center at the image center
        this.ctr_ra_pu = width_pu  / 2.0;
        this.ctr_de_pu = height_pu / 2.0;
    }

    /**
     * Convert equatorial coordinates (right ascension and declination) in degrees
     * to planar coordinates in planar units (pixels).
     *
     * @param eq_ra_deg equatorial x in degrees in the horizontal (right ascension) dimension
     * @param eq_de_deg equatorial y in degrees in the vertical (declination) dimension
     * @return int[]{pl_ra_pu, pl_de_pu} or null if outside the projected hemisphere
     */
    public int[] eq_deg_to_pl_pu(double eq_ra_deg, double eq_de_deg) 
    {
    	double eq_ra_rad = Math.toRadians(eq_ra_deg);
    	double eq_de_rad = Math.toRadians(eq_de_deg);
 
    	double[] plane = sphere_to_plane(eq_ra_rad, eq_de_rad);
        if (plane == null) {
            return null; // outside projection
        }

        double pl_ra_rad = plane[0]; // planar coordinates in radians
        double pl_de_rad = plane[1];

        // scale and translate to pixels in the plane
        double pl_ra_pu = ctr_ra_pu - pu_per_rad * pl_ra_rad;
        double pl_de_pu = ctr_de_pu - pu_per_rad * pl_de_rad; // minus so +y is up

        int xi = (int) Round.round_to_nearest(pl_ra_pu);
        int yi = (int) Round.round_to_nearest(pl_de_pu);

        if (xi < (ctr_ra_pu-width_pu/2) || (ctr_ra_pu+width_pu/2) <= xi || yi < (ctr_de_pu-height_pu/2) || (ctr_de_pu+height_pu/2) <= yi) {
            return null; // outside image bounds
        }

        return new int[]{xi, yi};
    }

    /**
     * Convert planar coordinates (in pixels) to equatorial coordinates (right ascension and declination in degrees).
     *
     * @param pl_ra_pu planar x in planar units (pixels) in the horizontal (right ascension) dimension
     * @param pl_de_pu planar y in planar units (pixels) in the vertical (declination) dimension
     * @return double[]={equatorial right ascension in degrees, equatorial declination in degrees}
     */
    public double[] pl_pu_to_eq_deg(int pl_ra_pu, int pl_de_pu) 
    {
    	// left and right appear to be reversed...
    	// original: double pl_ra_rad =  (pl_ra_pu - ctr_ra_pu) / pu_per_rad;
        double pl_ra_rad = -(pl_ra_pu - ctr_ra_pu) / pu_per_rad;

        // up and down (i.e., declination) appears to work correctly
    	// original: double pl_de_rad = -(pl_de_pu - ctr_de_pu) / pu_per_rad;
        double pl_de_rad = -(pl_de_pu - ctr_de_pu) / pu_per_rad;

        double[] eq = plane_to_sphere(pl_ra_rad, pl_de_rad);

        double eq_ra_rad = eq[0];
        double eq_de_rad = eq[1];

        double eq_ra_deg = Math.toDegrees(eq_ra_rad);
        double eq_de_deg = Math.toDegrees(eq_de_rad);

        return new double[]{eq_ra_deg, eq_de_deg};
    }
}