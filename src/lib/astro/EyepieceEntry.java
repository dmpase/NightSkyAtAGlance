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


public class EyepieceEntry implements Comparable<EyepieceEntry> {
	
	public final String name;
	public final double focal_length_mm;
	public final double apparent_field_of_view_deg;
	public final double eye_relief_mm;
	public final boolean editable;

	public EyepieceEntry(String str) 
	{
		if (str != null) {
			String[] field = str.split("[,]");
			if (field != null && 1 <= field.length) {
				name                       = field[0];
				focal_length_mm            = (field.length < 2 || field[1] == null || field[1].equals("")) ? Double.NaN : Double.parseDouble(field[1]);
				apparent_field_of_view_deg = (field.length < 3 || field[2] == null || field[2].equals("")) ? Double.NaN : Double.parseDouble(field[2]);
				eye_relief_mm              = (field.length < 4 || field[3] == null || field[3].equals("")) ? Double.NaN : Double.parseDouble(field[3]);
				editable                   = (field.length < 5 || field[4] == null || field[4].equals("")) ? false      : field[4].equalsIgnoreCase("true");
			} else {
				name                       = null;
				focal_length_mm            = Double.NaN;
				apparent_field_of_view_deg = Double.NaN;
				eye_relief_mm              = Double.NaN;
				editable                   = false;
			}
		} else {
			name                       = null;
			focal_length_mm            = Double.NaN;
			apparent_field_of_view_deg = Double.NaN;
			eye_relief_mm              = Double.NaN;
			editable                   = false;
		}
	}

	public boolean equals(EyepieceEntry rhs)
	{
		return name.equals(rhs.name) && 
			focal_length_mm == rhs.focal_length_mm && 
			apparent_field_of_view_deg == rhs.apparent_field_of_view_deg && 
			eye_relief_mm == rhs.eye_relief_mm;
	}

	public EyepieceEntry(String n, double f, double a, double e, boolean m) 
	{
		name                       = n;
		focal_length_mm            = f;
		apparent_field_of_view_deg = a;
		eye_relief_mm              = e;
		editable                   = m;
	}

	@Override public String toString()
	{
		return String.format("%s,%f,%f,%f,%s", name, focal_length_mm, apparent_field_of_view_deg, apparent_field_of_view_deg, editable);
	}

	@Override public int compareTo(EyepieceEntry obj) 
	{
		return name.compareTo(obj.name);
	}
}
