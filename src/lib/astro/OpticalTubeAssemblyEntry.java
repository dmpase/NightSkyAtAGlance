package lib.astro;

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


public class OpticalTubeAssemblyEntry implements Comparable<OpticalTubeAssemblyEntry> {
	
	public final String  name;
	public final double  aperture;
	public final double  focal_length;
	public final double  f_ratio;
	public final double  reducer;

	public final double eff_aperture;
	public final double eff_f_length;
	public final double eff_f_ratio;

	public final boolean editable;

	public OpticalTubeAssemblyEntry(String str)
	{
    	// System.out.printf("%s: %d: str='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), str);
		if (str != null) {
			String[] field = str.split("[,]");
			if (field != null && 5 <= field.length) {
				name         = field[0].trim();
				aperture     = Double.parseDouble(field[1]);
				focal_length = Double.parseDouble(field[2]);
				f_ratio      = Double.parseDouble(field[3]);
				reducer      = Double.parseDouble(field[4]);

				eff_aperture = aperture;
				eff_f_length = focal_length * reducer;
				eff_f_ratio  = f_ratio      * reducer;

				editable     = (field.length < 6 || field[5] == null || field[5].equals("")) ? false : field[5].equalsIgnoreCase("true");
			} else {
				name         = null;
				aperture     = Double.NaN;
				focal_length = Double.NaN;
				f_ratio      = Double.NaN;
				reducer      = Double.NaN;
				eff_aperture = Double.NaN;
				eff_f_length = Double.NaN;
				eff_f_ratio  = Double.NaN;
				editable     = false;
			}
		} else {
			name         = null;
			aperture     = Double.NaN;
			focal_length = Double.NaN;
			f_ratio      = Double.NaN;
			reducer      = Double.NaN;
			eff_aperture = Double.NaN;
			eff_f_length = Double.NaN;
			eff_f_ratio  = Double.NaN;
			editable     = false;
		}
    	// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), toString());
	}

	public OpticalTubeAssemblyEntry(String n, double a, double l, double r, boolean m) 
	{
		name         = n;
		aperture     = a;
		focal_length = l;
		f_ratio      = focal_length / aperture;
		reducer      = r;

		eff_aperture = aperture;
		eff_f_length = focal_length * reducer;
		eff_f_ratio  = f_ratio      * reducer;

		editable     = m;
	}

	public boolean equals(OpticalTubeAssemblyEntry rhs)
	{
		return name.equals(rhs.name) && aperture == rhs.aperture && focal_length == rhs.focal_length && f_ratio == rhs.focal_length && reducer == rhs.reducer;
	}

	@Override public String toString()
	{
		return String.format("%s,%f,%f,%f,%f,%s", name, aperture, focal_length, f_ratio, reducer, editable);
	}

	@Override public int compareTo(OpticalTubeAssemblyEntry obj)
	{
		return name.compareTo(obj.name);
	}
}
