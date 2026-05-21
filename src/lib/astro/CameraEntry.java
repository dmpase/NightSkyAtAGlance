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


public class CameraEntry {
	
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

	public CameraEntry(String str) 
	{
		if (str != null) {
			String[] field = str.split("[,]");
			if (field != null) {
				name             = field [ 0];
				sensor_width_mm  = (field.length < 2 || field[ 1] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 1]);
				sensor_height_mm = (field.length < 3 || field[ 2] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 2]);
				sensor_width_px  = (field.length < 4 || field[ 3] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 3]);
				sensor_height_px = (field.length < 5 || field[ 4] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 4]);
				pixel_size_um    = (field.length < 6 || field[ 5] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 5]);
				sensor_form      = field [ 6];
				lens_interface  = field [ 7];
				color            = field [ 8];
				max_exposure_sec = (field.length < 10 || field[ 9] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[ 9]);
				min_exposure_sec = (field.length < 11 || field[10] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[10]);
				bit_depth        = (field.length < 12 || field[11] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[11]);
				well             = (field.length < 13 || field[12] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[12]);
				noise            = (field.length < 14 || field[13] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[13]);
			} else {
				name             = null;
				sensor_width_mm  = Double.NaN;
				sensor_height_mm = Double.NaN;
				sensor_width_px  = Double.NaN;
				sensor_height_px = Double.NaN;
				pixel_size_um    = Double.NaN;
				sensor_form      = null;
				lens_interface  = null;
				color            = null;
				max_exposure_sec = Double.NaN;
				min_exposure_sec = Double.NaN;
				bit_depth        = Double.NaN;
				well             = Double.NaN;
				noise            = Double.NaN;
			}
		} else {
			name             = null;
			sensor_width_mm  = Double.NaN;
			sensor_height_mm = Double.NaN;
			sensor_width_px  = Double.NaN;
			sensor_height_px = Double.NaN;
			pixel_size_um    = Double.NaN;
			sensor_form      = null;
			lens_interface  = null;
			color            = null;
			max_exposure_sec = Double.NaN;
			min_exposure_sec = Double.NaN;
			bit_depth        = Double.NaN;
			well             = Double.NaN;
			noise            = Double.NaN;
		}
	}

	@Override public String toString()
	{
		return null;
	}
}
