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


public class DsoType {
	public final int type;
	public final String[] list;


	public static final int STAR                         =  0;
	public static final int DOUBLE_STAR                  =  1;
	public static final int VARIABLE_STAR                =  2;
	public static final int ASTERISM                     =  3;
	public static final int OPEN_CLUSTER                 =  4;
	public static final int NEBULA                       =  5;
	public static final int DARK_NEBULA                  =  6;
	public static final int DIFFUSE_NEBULA               =  7;
	public static final int EMISSION_NEBULA              =  8;
	public static final int GASEOUS_NEBULA               =  9;
	public static final int PLANETARY_NEBULA             = 10;
	public static final int REFLECTION_NEBULA            = 11;
	public static final int SUPERNOVA_REMNANT            = 12;
	public static final int OPEN_CLUSTER_AND_NEBULA      = 13;
	public static final int GALAXY                       = 14;
	public static final int BARRED_IRREGULAR_GALAXY      = 15;
	public static final int BARRED_SPIRAL_GALAXY         = 16;
	public static final int DWARF_SPHEROIDAL_GALAXY      = 17;
	public static final int ELLIPTICAL_GALAXY            = 18;
	public static final int GALAXY_CLUSTER               = 19;
	public static final int GLOBULAR_CLUSTER             = 20;
	public static final int INTERACTING_GALAXY           = 21;
	public static final int IRREGULAR_GALAXY             = 22;
	public static final int LENTICULAR_GALAXY            = 23;
	public static final int SPIRAL_GALAXY                = 24;
	public static final int STARBURST_GALAXY             = 25;
	public static final int SUPERGIANT_ELLIPTICAL_GALAXY = 26;
	public static final int ROCKY_PLANET  	             = 27;
	public static final int GAS_GIANT                    = 28;
	public static final int MOON                         = 29;
	public static final int MINOR_PLANET                 = 30;
	public static final int CUSTOM                       = 31;
	public static final int UNIDENTIFIED                 = 32;

	public static final int NUMBER_OF_DSO_TYPES          = UNIDENTIFIED + 1;

	public static final String[] star                         = { "Star",        };
	public static final String[] double_star                  = { "Double Star", };
	public static final String[] variable_star                = { "Variable Star", };
	public static final String[] asterism                     = { "Asterism", };
	public static final String[] open_cluster                 = { "Open Cluster", "open star cluster", "Milky Way star cloud", };

	public static final String[] nebula                       = { "Nebula", "H II region nebula", };
	public static final String[] dark_nebula                  = { "Dark Nebula", };
	public static final String[] diffuse_nebula               = { "Diffuse Nebula", };
	public static final String[] emission_nebula              = { "Emission Nebula", };
	public static final String[] gaseous_nebula               = { "Gaseous Nebula", };
	public static final String[] planetary_nebula             = { "Planetary Nebula", };
	public static final String[] reflection_nebula            = { "Reflection Nebula", };
	public static final String[] supernova_remnant            = { "Supernova remnant", };
	public static final String[] open_cluster_and_nebula      = { "Open Cluster and Nebula", "nebula with cluster", "H II region nebula with cluster",  "OPEN STAR CLUSTER AND EMISSION NEBULA",  };

	public static final String[] galaxy                       = { "Galaxy", };
	public static final String[] barred_irregular_galaxy      = { "Barred Irregular Galaxy", };
	public static final String[] barred_spiral_galaxy         = { "Barred Spiral Galaxy", };
	public static final String[] dwarf_spheroidal_galaxy      = { "Dwarf Spheroidal Galaxy", "dwarf elliptical galaxy", };
	public static final String[] elliptical_galaxy            = { "Elliptical Galaxy", "elliptical or lenticular galaxy", };
	public static final String[] galaxy_cluster               = { "Galaxy Cluster", };
	public static final String[] globular_cluster             = { "Globular Cluster", "GLOBULAR CLUSTER EXTENDED GALACTIC OR EXTRAGALACTIC", };
	public static final String[] interacting_galaxy           = { "Interacting Galaxy", };
	public static final String[] irregular_galaxy             = { "Irregular Galaxy", };
	public static final String[] lenticular_galaxy            = { "Lenticular Galaxy", };
	public static final String[] spiral_galaxy                = { "Spiral Galaxy", };
	public static final String[] starburst_galaxy             = { "Starburst Galaxy", };
	public static final String[] supergiant_elliptical_galaxy = { "Supergiant Elliptical Galaxy", };

	public static final String[] rocky_planet                 = { "Rocky Planet", };
	public static final String[] gas_giant                    = { "Gas Giant", };
	public static final String[] moon                         = { "Moon", };
	public static final String[] minor_planet                 = { "Minor Planet", };
	public static final String[] custom                       = { "Custom", };

	public static final String[] unidentified                 = { "Unidentified", };

	public static DsoType[] type_list = {
		new DsoType(STAR,                         star),
		new DsoType(DOUBLE_STAR,                  double_star),
		new DsoType(VARIABLE_STAR,                variable_star),
		new DsoType(ASTERISM,                     asterism),
		new DsoType(OPEN_CLUSTER,                 open_cluster),

		new DsoType(NEBULA,                       nebula),
		new DsoType(DARK_NEBULA,                  dark_nebula),
		new DsoType(DIFFUSE_NEBULA,               diffuse_nebula),
		new DsoType(EMISSION_NEBULA,              emission_nebula),
		new DsoType(GASEOUS_NEBULA,               gaseous_nebula),
		new DsoType(PLANETARY_NEBULA,             planetary_nebula),
		new DsoType(REFLECTION_NEBULA,            reflection_nebula),
		new DsoType(SUPERNOVA_REMNANT,            supernova_remnant),
		new DsoType(OPEN_CLUSTER_AND_NEBULA,      open_cluster_and_nebula),

		new DsoType(GALAXY,                       galaxy),
		new DsoType(BARRED_IRREGULAR_GALAXY,      barred_irregular_galaxy),
		new DsoType(BARRED_SPIRAL_GALAXY,         barred_spiral_galaxy),
		new DsoType(DWARF_SPHEROIDAL_GALAXY,      dwarf_spheroidal_galaxy),
		new DsoType(ELLIPTICAL_GALAXY,            elliptical_galaxy),
		new DsoType(GALAXY_CLUSTER,               galaxy_cluster),
		new DsoType(GLOBULAR_CLUSTER,             globular_cluster),
		new DsoType(INTERACTING_GALAXY,           interacting_galaxy),
		new DsoType(IRREGULAR_GALAXY,             irregular_galaxy),
		new DsoType(LENTICULAR_GALAXY,            lenticular_galaxy),
		new DsoType(SPIRAL_GALAXY,                spiral_galaxy),
		new DsoType(STARBURST_GALAXY,             starburst_galaxy),
		new DsoType(SUPERGIANT_ELLIPTICAL_GALAXY, supergiant_elliptical_galaxy),

		new DsoType(ROCKY_PLANET,                 rocky_planet),
		new DsoType(GAS_GIANT,                    gas_giant),
		new DsoType(MOON,                         moon),
		new DsoType(MINOR_PLANET,                 minor_planet),
		new DsoType(CUSTOM,                       custom),

		new DsoType(UNIDENTIFIED,                 unidentified),
	};

	public DsoType(int t, String[] n)
	{
		type = t;
		list = n;
	}
	
	public static int get_type(String s)
	{
		if (s == null) return UNIDENTIFIED;

		s = s.replaceAll("[ ]", "");
		for (DsoType elt: type_list) {
			for (String str: elt.list) {
				if (s.equalsIgnoreCase(str.replaceAll("[ ]", ""))) {
					return elt.type;
				}
			}
		}

		return UNIDENTIFIED;
	}
}
