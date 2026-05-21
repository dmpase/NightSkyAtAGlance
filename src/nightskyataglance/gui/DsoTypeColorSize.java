package nightskyataglance.gui;

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
 * infringement of that parties intellectual property rights.                  *
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


import java.awt.Color;

import lib.stars.catalog.DsoType;

public class DsoTypeColorSize {
	public final int   type;
	public final Color color;
	public final int   width;
	public final int   height;

	public static final DsoTypeColorSize[] table = {
		new DsoTypeColorSize(DsoType.STAR,                         Color.WHITE,   1, 1),
		new DsoTypeColorSize(DsoType.DOUBLE_STAR,                  new Color( 0xFF6000 ), 3, 3),
		new DsoTypeColorSize(DsoType.VARIABLE_STAR,                Color.ORANGE,  3, 3),
		new DsoTypeColorSize(DsoType.ASTERISM,                     Color.WHITE,   5, 5),
		new DsoTypeColorSize(DsoType.OPEN_CLUSTER,                 Color.CYAN,    5, 5),

		new DsoTypeColorSize(DsoType.NEBULA,                       Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.DARK_NEBULA,                  Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.DIFFUSE_NEBULA,               Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.EMISSION_NEBULA,              Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.GASEOUS_NEBULA,               Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.PLANETARY_NEBULA,             Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.REFLECTION_NEBULA,            Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.SUPERNOVA_REMNANT,            Color.MAGENTA, 6, 3),
		new DsoTypeColorSize(DsoType.OPEN_CLUSTER_AND_NEBULA,      Color.CYAN,    6, 3),

		new DsoTypeColorSize(DsoType.GALAXY,                       new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.BARRED_IRREGULAR_GALAXY,      new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.BARRED_SPIRAL_GALAXY,         new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.DWARF_SPHEROIDAL_GALAXY,      Color.CYAN,            4, 4),
		new DsoTypeColorSize(DsoType.ELLIPTICAL_GALAXY,            new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.GALAXY_CLUSTER,               new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.GLOBULAR_CLUSTER,             Color.CYAN,            4, 4),
		new DsoTypeColorSize(DsoType.INTERACTING_GALAXY,           new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.IRREGULAR_GALAXY,             new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.LENTICULAR_GALAXY,            new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.SPIRAL_GALAXY,                new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.STARBURST_GALAXY,             new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.SUPERGIANT_ELLIPTICAL_GALAXY, new Color( 0x8040FF ), 6, 4),

		new DsoTypeColorSize(DsoType.ROCKY_PLANET,                 new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.GAS_GIANT,                    new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.MOON,                         new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.MINOR_PLANET,                 new Color( 0x8040FF ), 6, 4),
		new DsoTypeColorSize(DsoType.CUSTOM,                       new Color( 0x8040FF ), 6, 4),

		new DsoTypeColorSize(DsoType.UNIDENTIFIED,                 Color.RED,             3, 3),
	};

	public DsoTypeColorSize(int t, Color c, int w, int h)
	{
		type   = t;
		color  = c;
		width  = w;
		height = h;
	}
	
	public static final DsoTypeColorSize find(String type)
	{
		if (type != null) {
			int idx = DsoType.get_type(type);
			idx = (0 <= idx && idx <= DsoType.UNIDENTIFIED) ? idx : DsoType.UNIDENTIFIED;
	
			return table[idx];
		}
		
		return null;
	}
}
