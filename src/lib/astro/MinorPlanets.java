package lib.astro;

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

public class MinorPlanets {

	/*
	 * https://duckduckgo.com/?t=ffab&q=calculate+the+orbit+of+ceres&atb=v503-1&ia=web
	 * https://duckduckgo.com/?t=ffab&q=orbital+elements+of+ceres&atb=v503-1&ia=web
	 * https://en.wikipedia.org/wiki/Ceres_(dwarf_planet)#Rotation_and_axial_tilt
	 * https://thatsmaths.com/2021/06/24/gauss-predicts-the-orbit-of-ceres/
	 * https://theskylive.com/ceres-info
	 * https://www.princeton.edu/~willman/planetary_systems/Sol/Ceres/
	 * https://www.spacereference.org/asteroid/1-ceres-a801-aa
	 * https://in-the-sky.org/data/object.php?id=A1
	 * 
	 * https://www.universeguide.com/asteroid/112/ceres
	 * Facts and Figures
	 * Type							Asteroid
	 * Mass							939,300 (10^15) kg
	 * Radius (Size)				469.70 km
	 * Rotation Period				9.074 hours
	 * Absolute Magnitude			3.33
	 * Albedo						0.09
	 * Aphelion (Furthest)			2.9773 A.U.
	 * Perihelion (Nearest)			2.5577 A.U.
	 * Mean Anomaly					334.32719
	 * Longitude Of Ascending Node	80.26642°
	 * Argument of Perihelion		73.53162°
	 * Approx. Diameter				960 x 932
	 * Near Earth ObjectNo
	 * Mean Orbit Velocity (km/h)	1.00
	 * Average Orbit Distance (km)	1.00
	 * Orbital Period				4.6 Yrs
	 * Orbital Inclination			10.5868°
	 * Orbital Eccentricity			0.0786358
	 * Semi-Major Axis 				2.7666191 A.U.
	 * Source(s)					Minor Planet Center
	 * 
	 * https://minorplanetcenter.net/
	 * https://minorplanetcenter.net/dwarf_planets
	 * Dwarf Planets
	 * The International Astronomical Union defines a dwarf planet as a celestial body that
	 *   (a) is in orbit around the Sun,
	 *   (b) has sufficient mass for its self-gravity to overcome rigid body forces so that 
	 *       it assumes a hydrostatic equilibrium (nearly round) shape,
	 *   (c) has not cleared the neighbourhood around its orbit, and
	 *   (d) is not a satellite. 
	 * object	ω (°)	☊ (°)	i (°)	e		q (AU)	a (AU)	M (°)	n (°/day)	Q (AU)	H (mag)	P (yrs)	T				Epoch (1) 
	 * Ceres	73.3	 80.2	10.6	0.08	 2.55	 2.77	231.5	0.214		 2.99	 3.4	  4.60	2022-12-06.5	2025-11-21.0 (134340) 
	 * Pluto	113.2	110.3	17.2	0.25	29.62	39.34	 53.1	0.004		49.06	-0.6	247		1989-06-27.8	2025-11-21.0 (136199) 
	 * Eris		150.7	 36.0	43.9	0.44	38.28	68.00	211.5	0.002		97.71	-1.3	561		1696-07-11.3	2025-11-21.0 (136472) 
	 * Makemake	297.1	 79.3	29.0	0.16	38.21	45.51	169.3	0.003		52.81	-0.2	307		2188-07-07.9	2025-11-21.0 (136108) 
	 * Haumea	240.9	121.8	28.2	0.20	34.59	43.01	222.3	0.003		51.42	 0.1	282		2133-09-30.4	2025-11-21.0 
	 * 
	 * PA				OMEGA	i		e				a											Tp
	 * 
	 * The columns consist of:
	 * object — number and name
	 * ω — argument of perihelion (in degrees)
	 * ☊ — ascending node (in degrees)
	 * i — inclination (in degrees)
	 * e — eccentricity
	 * q — perihelion distance (in astronomical units)
	 * a — semimajor axis (in astronomical units)
	 * M — mean anomaly (in degrees)
	 * n — mean daily motion (in degrees per day)
	 * Q — aphelion distance (in astronomical units)
	 * H — absolute magnitude
	 * P — period (in years)
	 * T — date of perihelion passage 
	 * Epoch — epoch of the orbital elements
	 * 
	 * 
	 * https://en.wikipedia.org/wiki/NGC_891
	 * https://en.wikipedia.org/wiki/IC_1101
	 * https://en.wikipedia.org/wiki/UGC_2885
	 * https://en.wikipedia.org/wiki/Sh_2-155
	 * 
	 * https://grokipedia.com/page/ngc_4556
	 * https://grokipedia.com/page/IC_1101
	 * https://grokipedia.com/page/ugc_4881
	 * https://grokipedia.com/page/sh_2_155
	 * 
	 * https://duckduckgo.com/?q=sh-2+155
	 * https://duckduckgo.com/?q=ic+1101
	 * https://duckduckgo.com/?q=ugc+2885
	 * https://duckduckgo.com/?q=ngc+891
	 * 
	 * https://www.google.com/search?q=IC+1101
	 * https://www.google.com/search?q=sh-2+155
	 * https://www.google.com/search?q=NGC+4889
	 * https://www.google.com/search?q=UGC+2885
	 */
}
