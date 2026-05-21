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


public class GnomonicProjector extends Projector {

    /**
     * @param eq_ra_deg center of the tile on the equatorial sphere in right ascension in degrees
     * @param eq_de_deg center of the tile on the equatorial sphere in declination in degrees
     */
    public GnomonicProjector(double eq_ra_deg, double eq_de_deg)
    {
    	super(eq_ra_deg, eq_de_deg);
    }

    /**
     * Project an equatorial position (RA, Dec) to the tangent plane (x, y) coordinates.
     * x and y are in units of radians on the tangent plane.
     *
     * @param eq_ra_rad right ascension in radians
     * @param eq_de_rad declination in radians
     * @return {planar right ascension, planar declination} in radians
     */
    @Override public double[] sphere_to_plane(double eq_ra_rad, double eq_de_rad) 
    {
        double cos_de      = Math.cos(eq_de_rad);
        double sin_de      = Math.sin(eq_de_rad);
        double cos_ctr_de  = Math.cos(ctr_de_rad);
        double sin_ctr_de  = Math.sin(ctr_de_rad);

        double diff_ra     = eq_ra_rad - ctr_ra_rad;

        double cos_diff_ra = Math.cos(diff_ra);
        double sin_diff_ra = Math.sin(diff_ra);

        // denominator cos(c) for gnomonic projection
        double cos_c       = sin_ctr_de * sin_de + cos_ctr_de * cos_de * cos_diff_ra;

        if (cos_c <= 0) {
            // point is on or beyond horizon of this projection
            return null;
        }

        // projection to x and y, in radians ...
        double pl_ra_rad = (cos_de * sin_diff_ra) / cos_c;
        double pl_de_rad = (cos_ctr_de * sin_de - sin_ctr_de * cos_de * cos_diff_ra) / cos_c;

        return new double[]{pl_ra_rad, pl_de_rad};
    }

    /**
     * Project (x, y), in radians, from the tangent plane back to (r.a., dec.), also in radians.
     *
     * @param pl_ra_rad planar right ascension (horizontal, in radians)
     * @param pl_de_rad planar declination (vertical, in radians)
     * @return right ascension and declination, both in radians
     */
    @Override public double[] plane_to_sphere(double pl_ra_rad, double pl_de_rad)
    {
        double rho = Math.hypot(pl_ra_rad, pl_de_rad);

        // special case: zero offset means it is the center
        if (rho == 0) {
            return new double[]{ctr_ra_rad, ctr_de_rad};
        }

        double rho_atan = Math.atan(rho);

        double rho_atan_sin = Math.sin(rho_atan);
        double rho_atan_cos = Math.cos(rho_atan);

        double ctr_de_sin = Math.sin(ctr_de_rad);
        double ctr_de_cos = Math.cos(ctr_de_rad);

        double eq_ra_rad = ctr_ra_rad + Math.atan2(pl_ra_rad * rho_atan_sin, (rho * ctr_de_cos * rho_atan_cos - pl_de_rad * ctr_de_sin * rho_atan_sin));
        double eq_de_rad = Math.asin(rho_atan_cos * ctr_de_sin + (pl_de_rad * rho_atan_sin * ctr_de_cos / rho));

        return new double[]{eq_ra_rad, eq_de_rad};
    }
}