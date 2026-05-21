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


import java.util.TimeZone;

import lib.astro.CelestialCalculations.SolarLocation;
import lib.astro.PracticalAstronomy.ApproximatePlanetLocation;
import lib.astro.PracticalAstronomy.LunarLocation;
import lib.stars.catalog.DsoType;
import lib.stars.catalog.DsoAlias.Element;

public class PlanetaryAlignment {
	public static int DSO     = 0;
	public static int SUN     = DSO     + 1;
	public static int MOON    = SUN     + 1;
	public static int MERCURY = MOON    + 1;
	public static int VENUS   = MERCURY + 1;
	public static int MARS    = VENUS   + 1;
	public static int JUPITER = MARS    + 1;
	public static int SATURN  = JUPITER + 1;
	public static int URANUS  = SATURN  + 1;
	public static int NEPTUNE = URANUS  + 1;
	public static int LAST    = NEPTUNE + 1;

	Element[] elts   = new Element[LAST];
	boolean[] active = new boolean[LAST];

	public Element[] best_elt = new Element[LAST];
	public double    best_sum = Double.MAX_VALUE;
	public double    best_ssq = Double.MAX_VALUE;
	public long      best_cst = Long.MIN_VALUE;
	public double    best_avg = Double.MAX_VALUE;
	public boolean   valid    = true;

	public PlanetaryAlignment(TimeZone timezone, long start_time_ms, long end_time_ms, long delta_time_ms,
		boolean sun, boolean moon, boolean mercury, boolean venus, boolean mars, boolean jupiter, 
		boolean saturn, boolean uranus, boolean neptune, Element dso)
	{
		elts[DSO    ] = dso;
		elts[SUN    ] = new Element(new String[] {"Sun"    },  0   , -0.50, -26.74, -26.74, 60*0.536, DsoType.star[0]);
		elts[MOON   ] = new Element(new String[] {"Moon"   },  0   , +2.50,  -2.50, -12.90, 60*0.536, DsoType.moon[0]);
		elts[MERCURY] = new Element(new String[] {"Mercury"}, 23.25, -5.00,  -2.48,  +7.25,  13.0/60, DsoType.rocky_planet[0]);
		elts[VENUS  ] = new Element(new String[] {"Venus"  },  1.00, +5.50,  -4.92,  -2.98,  66.0/60, DsoType.rocky_planet[0]);
		elts[MARS   ] = new Element(new String[] {"Mars"   }, 23.00, -7.25,  -2.94,   1.86,  15.0/60, DsoType.rocky_planet[0]);
		elts[JUPITER] = new Element(new String[] {"Jupiter"},  6.50, 23.00,  -2.94,  -1.66,  40.0/60, DsoType.gas_giant[0]);
		elts[SATURN ] = new Element(new String[] {"Saturn" },  0.50, +3.50,  -0.55,  +1.17,  17.0/60, DsoType.gas_giant[0]);
		elts[URANUS ] = new Element(new String[] {"Uranus" },  4,    21.00,   5.38,   6.03,   3.7/60, DsoType.gas_giant[0]);
		elts[NEPTUNE] = new Element(new String[] {"Neptune"},  0.00,  0.50,   7.67,   8.00,   2.3/60, DsoType.gas_giant[0]);

		active[DSO    ] = (dso != null);
		active[SUN    ] = sun;
		active[MOON   ] = moon;
		active[MERCURY] = mercury;
		active[VENUS  ] = venus;
		active[MARS   ] = mars;
		active[JUPITER] = jupiter;
		active[SATURN ] = saturn;
		active[URANUS ] = uranus;
		active[NEPTUNE] = neptune;

		int count = 0;
		for (int i=0; i < elts.length; i++) {
			for (int j=i+1; j < elts.length; j++) {
				count += (active[i] && active[j]) ? 1 : 0;
			}
		}
		
		if (start_time_ms <= end_time_ms && 0 < delta_time_ms) {
			valid = true;
		} else if (end_time_ms <= start_time_ms && delta_time_ms < 0) {
			valid = true;
		} else {
			valid = false;
			return;
		}

		for (long current_solar_time_ms=start_time_ms; 0 < count && delta_time_ms != 0 && current_solar_time_ms <= end_time_ms; current_solar_time_ms+=delta_time_ms) {
			SolarLocation solar_location = new SolarLocation(current_solar_time_ms, timezone);
			elts[SUN    ].set_ra_hrs(PracticalAstronomy.adjust24(solar_location.right_ascension));
			elts[SUN    ].set_de_deg(PracticalAstronomy.adjust90(solar_location.declination));

			LunarLocation lunar_location = new LunarLocation(current_solar_time_ms, timezone);
			elts[MOON   ].set_ra_hrs(PracticalAstronomy.adjust24(lunar_location.right_ascension));
			elts[MOON   ].set_de_deg(PracticalAstronomy.adjust90(lunar_location.declination));

			ApproximatePlanetLocation planets = new ApproximatePlanetLocation(current_solar_time_ms, timezone);
			elts[MERCURY].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.MERCURY].right_ascension));
			elts[MERCURY].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.MERCURY].declination));

			elts[VENUS  ].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.VENUS  ].right_ascension));
			elts[VENUS  ].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.VENUS  ].declination));

			elts[MARS   ].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.MARS   ].right_ascension));
			elts[MARS   ].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.MARS   ].declination));

			elts[JUPITER].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.JUPITER].right_ascension));
			elts[JUPITER].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.JUPITER].declination));

			elts[SATURN ].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.SATURN ].right_ascension));
			elts[SATURN ].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.SATURN ].declination));

			elts[URANUS ].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.URANUS ].right_ascension));
			elts[URANUS ].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.URANUS ].declination));

			elts[NEPTUNE].set_ra_hrs(PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.NEPTUNE].right_ascension));
			elts[NEPTUNE].set_de_deg(PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.NEPTUNE].declination));
			
			double sum = 0;
			double ssq = 0;
			for (int i=0; i < elts.length; i++) {
				for (int j=i+1; j < elts.length; j++) {
					if (active[i] && active[j]) {
						double dist = PracticalAstronomy.angle_between_celestial_objects(elts[i].ra_hrs(), elts[i].de_deg(), elts[j].ra_hrs(), elts[j].de_deg());
						sum += dist;
						ssq += dist * dist;
					}
				}
			}

			if (ssq < best_ssq) {
				best_cst = current_solar_time_ms;
				best_sum = sum;
				best_ssq = ssq;
				for (int i=0; i < elts.length; i++) {
					best_elt[i] = new Element(elts[i]);
				}
			}
		}

		best_avg = (0 < count) ? best_sum / count : 0;
	}
}
