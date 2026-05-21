package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2021 Douglas M. Pase                                          *
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


import java.io.IOException;
import java.io.RandomAccessFile;

public class GscHeader {
	public final int      len;			// 
	public final int      ver;			// 
	public final int      region;		// 
	public final int      nobj;			// 
	public final double   amin;			// 
	public final double   amax;			// 
	public final double   dmin;			// 
	public final double   dmax;			// 
	public final double   magoff;
	public final double   scale_ra;
	public final double   scale_dec;
	public final double   scale_pos;
	public final double   scale_mag;
	public final int      npl;
	public final String[] list;
	public final String[] plate;
	public final double[] epoch;

	private static final int VERSION    =  0;
	private static final int REGION     =  1;
	private static final int NOBJ       =  2;
	private static final int AMIN       =  3;
	private static final int AMAX       =  4;
	private static final int DMIN       =  5;
	private static final int DMAX       =  6;
	private static final int MAGOFF     =  7;
	private static final int SCALE_RA   =  8;
	private static final int SCALE_DEC  =  9;
	private static final int SCALE_POS  = 10;
	private static final int SCALE_MAG  = 11;
	private static final int NPL        = 12;
	private static final int LIST       = 13;
	
	public GscHeader(RandomAccessFile raf) throws IOException 
	{
		// read the header length plus the first space
		byte[] hdr_len = new byte[4];
		raf.read(hdr_len);
		String hdr_len_str = new String(hdr_len, 0, 3);
		len = Integer.parseInt(hdr_len_str);
		
		// read the rest of the header
		byte[] header_buf = new byte[len];
		raf.read(header_buf);
		String header_str = new String(header_buf);
		
		// split the header into separate fields
		String[] field = header_str.split("[ ]");
	
		// get the file format version
		ver       = Integer.parseInt   (field[VERSION]);

		// get the region ID
		field[REGION].replaceAll("^0*", "");
		field[REGION] = field[REGION].equals("") ? "0" : field[REGION];
		region    = Integer.parseInt   (field[REGION]);

		nobj      = Integer.parseInt   (field[NOBJ]);
		amin      = Double .parseDouble(field[AMIN]);
		amax      = Double .parseDouble(field[AMAX]);
		dmin      = Double .parseDouble(field[DMIN]);
		dmax      = Double .parseDouble(field[DMAX]);
		magoff    = Double .parseDouble(field[MAGOFF]);
		scale_ra  = Double .parseDouble(field[SCALE_RA]);
		scale_dec = Double .parseDouble(field[SCALE_DEC]);
		scale_pos = Double .parseDouble(field[SCALE_POS]);
		scale_mag = Double .parseDouble(field[SCALE_MAG]);
		npl       = Integer.parseInt   (field[NPL]);
		list      = new String[field.length - LIST];
		plate     = new String[(field.length - LIST)/2];
		epoch     = new double[(field.length - LIST)/2];
		for (int i=0; i < list.length; i++) {
			list[i] = field[LIST + i];
		}
		for (int i=0; i < plate.length; i++) {
			plate[i] = field[LIST + i];
			epoch[i] = (Character.isDigit(field[LIST + plate.length + i].charAt(0))) ?
				 Double.parseDouble(field[LIST + plate.length + i]) : 0;
		}
	}
}
