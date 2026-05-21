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


import java.io.IOException;
import java.io.RandomAccessFile;

public class YaleBrightStarBinEntry implements Comparable<YaleBrightStarBinEntry> {
	
	public final int    XN0;		// Catalog number of star
	public final double SRA0;		// B1950 Right Ascension (radians)
	public final double SDEC0;		// B1950 Declination (radians)
	public final String IS;			// Spectral type (2 characters)
	public final float  MAG;		// Magnitude * 100
	public final double XRPM;		// R.A. proper motion (radians per year)
	public final double XDPM;		// Dec. proper motion (radians per year)

	public final double ra_angle;	// RA angle in hours
	public final int    ra_hrs;		// RA hours
	public final int    ra_min;		// RA minutes
	public final double ra_sec;		// RA seconds, including fractions

	public final double dec_angle;	// Dec. angle in degrees
	public final int    dec_deg;	// Dec. degrees
	public final int    dec_min;	// Dec. minutes
	public final double dec_sec;	// Dec. seconds, including fractions

	
	public YaleBrightStarBinEntry(RandomAccessFile raf, YaleBrightStarHeader header) throws IOException 
	{
		byte[] xn0b = new byte[4];
		raf.read(xn0b);
		int xn0 = 
				(((int)xn0b[3]&0xff) << (3*8)) | 
				(((int)xn0b[2]&0xff) << (2*8)) | 
				(((int)xn0b[1]&0xff) << (1*8)) | 
				(((int)xn0b[0]&0xff) << (0*8));
		XN0 = (int) Float.intBitsToFloat(xn0) - header.STAR0;

		byte[] sra0b = new byte[8];
		raf.read(sra0b);
		long sra0 = 
				(((long)sra0b[7]&0xff) << (7*8)) | 
				(((long)sra0b[6]&0xff) << (6*8)) | 
				(((long)sra0b[5]&0xff) << (5*8)) | 
				(((long)sra0b[4]&0xff) << (4*8)) | 
				(((long)sra0b[3]&0xff) << (3*8)) | 
				(((long)sra0b[2]&0xff) << (2*8)) | 
				(((long)sra0b[1]&0xff) << (1*8)) | 
				(((long)sra0b[0]&0xff) << (0*8));
		SRA0  = Double.longBitsToDouble(sra0);

		byte[] sdec0b = new byte[8];
		raf.read(sdec0b);
		long sdec0 = 
				(((long)sdec0b[7]&0xff) << (7*8)) | 
				(((long)sdec0b[6]&0xff) << (6*8)) | 
				(((long)sdec0b[5]&0xff) << (5*8)) | 
				(((long)sdec0b[4]&0xff) << (4*8)) | 
				(((long)sdec0b[3]&0xff) << (3*8)) | 
				(((long)sdec0b[2]&0xff) << (2*8)) | 
				(((long)sdec0b[1]&0xff) << (1*8)) | 
				(((long)sdec0b[0]&0xff) << (0*8));
		SDEC0  = Double.longBitsToDouble(sdec0);

		byte[] isb = new byte[2];
		raf.read(isb);
		IS    = "" + (char)isb[0] + (char)isb[1];

		byte[] magb = new byte[2];
		raf.read(magb);
		short mag = (short)((((int)magb[1]&0xff) << (1*8)) | (((int)magb[0]&0xff) << (0*8)));
		MAG   = (float) (mag/100.0);

		byte[] xrpmb = new byte[4];
		raf.read(xrpmb);
		int xrpm = 
				(((int)xrpmb[3]&0xff) << (3*8)) | 
				(((int)xrpmb[2]&0xff) << (2*8)) | 
				(((int)xrpmb[1]&0xff) << (1*8)) | 
				(((int)xrpmb[0]&0xff) << (0*8));
		XRPM = (int) Float.intBitsToFloat(xrpm);

		byte[] xdpmb = new byte[4];
		raf.read(xdpmb);
		int xdpm = 
				(((int)xdpmb[3]&0xff) << (3*8)) | 
				(((int)xdpmb[2]&0xff) << (2*8)) | 
				(((int)xdpmb[1]&0xff) << (1*8)) | 
				(((int)xdpmb[0]&0xff) << (0*8));
		XDPM = (int) Float.intBitsToFloat(xdpm);

		ra_angle = Math.abs(SRA0) * 12 / Math.PI;
		ra_hrs   = (int) ra_angle;
		ra_min   = (int) ((ra_angle - ra_hrs) * 60);
		ra_sec   = (((ra_angle - ra_hrs) * 60) - ra_min) * 60;

		dec_angle = Math.abs(SDEC0) * 180 / Math.PI;
		dec_deg   = (int) dec_angle;
		dec_min   = (int) ((dec_angle - dec_deg) * 60);
		dec_sec   = (((dec_angle - dec_deg) * 60) - dec_min) * 60;
	}

	// return the index of the desired element
	public static int find(String name, YaleBrightStarBinEntry[] list)
	{
		int result = Integer.MIN_VALUE;
		
		if (name != null) {
			name = name.trim().replaceAll("[ ]", "").toUpperCase();
			if (name.substring(0, 2).equals("HR")) {
				int xn0 = Integer.parseInt(name.substring(2));
				result = find(xn0, list, 0, list.length);
			}
		}

		return result;
	}

	// return the index of the desired element
	public static int find(int xn0, YaleBrightStarBinEntry[] list)
	{
		return find(xn0, list, 0, list.length);
	}

	private static int find(int xn0, YaleBrightStarBinEntry[] list, int lower, int upper)
	{
		int result;
		int middle = (lower + upper) / 2;
		int comparison = Integer.compare(list[middle].XN0, xn0);
		
		if (comparison == 0) {
			// name == list[middle]
			result = middle;
		} else if (middle == lower) {
			// no more elements to search
			result = -1;
		} else if (comparison < 0) {
			// list[middle] < name
			result = find(xn0, list, middle, upper);
		} else {
			// name < list[middle]
			result = find(xn0, list, lower, middle);
		}
		
		return result;
	}
	
	@Override public String toString()
	{
		String str = String.format("[XN0=%4d,SRA0=%f(%02d:%02d:%05.2f),SDEC0=%s%f(%s%02d:%02d:%05.2f),IS=%s,MAG=%s%4.2f,XRPM=%f,XDPM=%f]", 
			XN0, SRA0, ra_hrs, ra_min, ra_sec, ((0<=SDEC0)?"+":"-"), Math.abs(SDEC0), ((0<=SDEC0)?"+":"-"), dec_deg, dec_min, dec_sec, 
			IS, ((0<=MAG)?"+":"-"), Math.abs(MAG), XRPM, XDPM);
		
		return str;
	}

	public int compareTo(int xn0)
	{
		int result = 0;
		if (XN0 < xn0) {
			result = -1;
		} else if (xn0 < XN0) {
			result = +1;
		}
		return result;
	}

	@Override public int compareTo(YaleBrightStarBinEntry rhs) 
	{
		int result = 0;
		if (XN0 < rhs.XN0) {
			result = -1;
		} else if (rhs.XN0 < XN0) {
			result = +1;
		}
		return result;
	}
}
