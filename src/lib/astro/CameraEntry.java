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


public class CameraEntry implements Comparable<CameraEntry> {
	
	public final String name;
	public final double sensor_width_mm;
	public final double sensor_height_mm;
	public final double sensor_width_px;
	public final double sensor_height_px;
	public final double pixel_size_um;
	public final String sensor_form;
	public final String lens_interface;
	public final String color;
	public final double max_exposure_sec;
	public final double min_exposure_sec;
	public final double bit_depth;
	public final double well;
	public final double noise;
	public final double quantum_efficiency;
	public final boolean editable;

	public CameraEntry(String str) 
	{
		if (str != null) {
			// System.out.printf("%s: %d: str='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), str);
			String[] field = str.split("[,]");
			if (field != null) {
				name               = field [ 0];
				sensor_width_mm    = (field.length < 2 || field[ 1] == null || field[ 1].equals("")) ? Double.NaN : Double.parseDouble(field[ 1]);
				sensor_height_mm   = (field.length < 3 || field[ 2] == null || field[ 2].equals("")) ? Double.NaN : Double.parseDouble(field[ 2]);
				sensor_width_px    = (field.length < 4 || field[ 3] == null || field[ 3].equals("")) ? Double.NaN : Double.parseDouble(field[ 3]);
				sensor_height_px   = (field.length < 5 || field[ 4] == null || field[ 4].equals("")) ? Double.NaN : Double.parseDouble(field[ 4]);
				pixel_size_um      = (field.length < 6 || field[ 5] == null || field[ 5].equals("")) ? Double.NaN : Double.parseDouble(field[ 5]);
				sensor_form        = field [ 6];
				lens_interface     = field [ 7];
				color              = field [ 8];
				max_exposure_sec   = (field.length < 10 || field[ 9] == null || field[ 9].equals("")) ? Double.NaN : Double.parseDouble(field[ 9]);
				min_exposure_sec   = (field.length < 11 || field[10] == null || field[10].equals("")) ? Double.NaN : Double.parseDouble(field[10]);
				bit_depth          = (field.length < 12 || field[11] == null || field[11].equals("")) ? Double.NaN : Double.parseDouble(field[11]);
				well               = (field.length < 13 || field[12] == null || field[12].equals("")) ? Double.NaN : Double.parseDouble(field[12]);
				noise              = (field.length < 14 || field[13] == null || field[13].equals("")) ? Double.NaN : Double.parseDouble(field[13]);
				quantum_efficiency = (field.length < 15 || field[14] == null || field[14].equals("")) ? Double.NaN : Double.parseDouble(field[14]);
				editable           = (field.length < 16 || field[15] == null || field[15].equals("")) ? false      : field[15].equalsIgnoreCase("true");
			} else {
				name               = null;
				sensor_width_mm    = Double.NaN;
				sensor_height_mm   = Double.NaN;
				sensor_width_px    = Double.NaN;
				sensor_height_px   = Double.NaN;
				pixel_size_um      = Double.NaN;
				sensor_form        = null;
				lens_interface     = null;
				color              = null;
				max_exposure_sec   = Double.NaN;
				min_exposure_sec   = Double.NaN;
				bit_depth          = Double.NaN;
				well               = Double.NaN;
				noise              = Double.NaN;
				quantum_efficiency = Double.NaN;
				editable           = false;
			}
		} else {
			name               = null;
			sensor_width_mm    = Double.NaN;
			sensor_height_mm   = Double.NaN;
			sensor_width_px    = Double.NaN;
			sensor_height_px   = Double.NaN;
			pixel_size_um      = Double.NaN;
			sensor_form        = null;
			lens_interface     = null;
			color              = null;
			max_exposure_sec   = Double.NaN;
			min_exposure_sec   = Double.NaN;
			bit_depth          = Double.NaN;
			well               = Double.NaN;
			noise              = Double.NaN;
			quantum_efficiency = Double.NaN;
			editable           = false;
		}
	}

	public CameraEntry(String n, 
			double swm, double shm, 
			double swp, double shp, 
			String sf, String li, String co, 
			double mxe, double mne, 
			double bd,  double we, 
			double no, double qe, 
			boolean mod) 
	{
		name               = n;
		sensor_width_mm    = swm;
		sensor_height_mm   = shm;
		sensor_width_px    = swp;
		sensor_height_px   = shp;
		pixel_size_um      = (1000*sensor_width_mm/sensor_width_px + 1000*sensor_height_mm/sensor_height_px) / 2;

		if (sf == null || sf.trim().equals("")) {
			if        (35 <= sensor_width_mm && sensor_width_mm <= 37 && 23 <= sensor_height_mm && sensor_height_mm <= 25) {
				sensor_form = "Full";
			} else if (22 <= sensor_width_mm && sensor_width_mm <= 25 && 14 <= sensor_height_mm && sensor_height_mm <= 17) {
				sensor_form = "APS-C";
			} else if (13 <= sensor_width_mm && sensor_width_mm <= 14 &&  8 <= sensor_height_mm && sensor_height_mm <=  9) {
				sensor_form = "1/1.5\"";
			} else if (11 <= sensor_width_mm && sensor_width_mm <= 12 &&  6 <= sensor_height_mm && sensor_height_mm <=  7) {
				sensor_form = "1/1.2\"";
			} else if ( 5 <= sensor_width_mm && sensor_width_mm <=  6 &&  3 <= sensor_height_mm && sensor_height_mm <=  4) {
				sensor_form = "1/2.8\"";
			} else {
				sensor_form = "";
			}
		} else {
			sensor_form = "";
		}

		lens_interface     = li;
		color              = co;
		max_exposure_sec   = mxe;
		min_exposure_sec   = mne;
		bit_depth          = bd;
		well               = we;
		noise              = no;
		quantum_efficiency = qe;
		editable           = mod;
	}

	public boolean equals(CameraEntry rhs)
	{
		return name.equals(rhs.name) && 
				sensor_width_mm == rhs.sensor_width_mm && sensor_height_mm == rhs.sensor_height_mm && 
				sensor_width_px == rhs.sensor_width_px && sensor_height_px == rhs.sensor_height_px && 
				pixel_size_um == rhs.pixel_size_um && 
				sensor_form.equals(rhs.sensor_form) && lens_interface.equals(rhs.lens_interface) && color.equals(rhs.color) && 
				max_exposure_sec == rhs.max_exposure_sec && min_exposure_sec == rhs.min_exposure_sec && 
				bit_depth == rhs.bit_depth && noise == rhs.noise && quantum_efficiency == rhs.quantum_efficiency;
	}

	@Override public String toString()
	{
		return String.format("%s,%f,%f,%f,%f,%f,%s,%s,%s,%f,%f,%f,%f,%f,%f,%s", 
			name, 
			sensor_width_mm,  sensor_height_mm, 
			sensor_width_px,  sensor_height_px, 
			pixel_size_um,    sensor_form, 
			lens_interface,   color, 
			max_exposure_sec, min_exposure_sec, 
			bit_depth,        well, 
			noise,            quantum_efficiency, 
			editable);
	}

	@Override public int compareTo(CameraEntry obj) 
	{
		return name.compareTo(obj.name);
	}
}
