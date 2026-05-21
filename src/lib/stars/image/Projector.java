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


public abstract class Projector {

    // location on the ecliptic sphere that is tangent to the projection plane
    public final double ctr_ra_deg;		// right ascension of tile center in degrees
    public final double ctr_de_deg;		// declination     of tile center in degrees
    public final double ctr_ra_rad;		// right ascension of tile center in radians
    public final double ctr_de_rad;		// declination     of tile center in radians

    /**
     * @param eq_ra_deg center of tile right ascension in degrees
     * @param eq_de_deg center of tile declination     in degrees
     */
	public Projector(double eq_ra_deg, double eq_de_deg)
	{
        ctr_ra_deg = eq_ra_deg;
        ctr_de_deg = eq_de_deg;
        ctr_ra_rad = Math.toRadians(ctr_ra_deg);
        ctr_de_rad = Math.toRadians(ctr_de_deg);
	}

    /*
     *  Projection Name		Properties										Used For
     *  ---------------     ---------------------------------------------   ---------------------
     *  Gnomonic			Great circles -> straight lines					Constellation mapping
     *  Stereographic		Conformal (angle-preserving)					Star charts
     *  Orthographic		Looks like a viewpoint from infinite distance	Planetaria
     *  Mercator-like		Conformal, used in some map making				Special star atlases
     *  Hammer–Aitoff		Equal-area										Full-sky maps
     *  Mollweide			Equal-area										CMB, galactic surveys
     */
    
    /**
     * Project a location on the equatorial sphere (r.a., dec.) to tangent plane (x, y) coordinates.
     * Right ascension and declination on the equatorial sphere, and x and y on the tangent plane 
     * are all in radians.
     *
     * @param eq_ra_deg right ascension in degrees
     * @param eq_de_deg declination in degrees
     * @return {x, y} on the tangent plane in radians
     */
    public abstract double[] sphere_to_plane(double eq_ra_deg, double eq_de_deg);

    /**
     * Map the tangent plane (x, y) coordinates back to (r.a., dec.) on the equatorial sphere.
     * Right ascension and declination on the equatorial sphere, and x and y on the tangent plane 
     * are all in radians.
     *
     * @param pl_ra_rad tangent plane x coordinate in radians
     * @param pl_de_rad tangent plane y coordinate in radians
     * @return {right ascension, declination} on the equatorial sphere in radians
     */
    public abstract double[] plane_to_sphere(double pl_ra_rad, double pl_de_rad);
}
