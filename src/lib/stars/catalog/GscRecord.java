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

public class GscRecord {
	public final int    id;				// guide star catalog ID
	public final int    reg;			// region
	public final double ra;				// right ascension
	public final double dec;			// de_B1950
	public final double poserr;			// position error
	public final double m;				// magnitude
	public final double merr;			// magnitude error
	public final int    mb;				// magnitude band
	public final int    cl;				// (star) class
	public final String plate;			// plate ID
	public final char   multiple;		// multiple
	public       double dist;			// 
	public       double posang;			// 
	public final double epoch;			// 
	
	public static final int band[] = { 0,1,6,8,10,11,12,13,14,18,4,3,16	};
	
	public GscRecord(RandomAccessFile raf, GscHeader h) throws IOException 
	{
		byte[] buf = new byte[12];
		raf.read(buf);
		short[] c = ba_to_sa(buf);
		
		int id = (c[0] & 127);
		id <<= 7;
		id |= (c[1] >> 1);
		this.id = id;
		
		int da = ((c[1]&1)<< 8) | c[2];
		da <<= 8;
		da |= c[3];
		da <<= 8;
		da |= c[4];
		da >>= 3;

		int dd = c[4] & 7;
		dd <<= 8;
		dd |= c[5];
		dd <<= 8;
		dd |= c[6];

		int dp = c[7];
		dp <<= 1;
		dp |= c[8] >> 7;

		int mag = c[9];
		mag <<= 3;
		mag |= c[10] >> 5;

		int dm = c[8] & 127;

		int ba = (c[10] >> 1) & 15;
		this.mb = band[ba];
		this.cl = (c[11] >> 4) & 7;

		int pl = c[11] & 15;
		this.plate = h.plate[pl];
		this.epoch = (0 < h.epoch[pl]) ? h.epoch[pl] - 2000 : 0;
		
		int mul = c[10] & 1;
		this.multiple = (mul == 0) ? 'F' : 'T';
		
		double tra = (double) da / h.scale_ra + h.amin;
		tra = (tra < 0) ? tra + 360 : ((360 <= tra) ? tra -360 : tra);
		this.ra = tra;
		
		this.dec = (double) dd / h.scale_dec + h.dmin;
		this.poserr = (double) dp / h.scale_pos;
		this.m = (double) mag / h.scale_mag + h.magoff;
		this.merr = (double) dm / h.scale_mag;
		this.reg = h.region;
	}
	
	public GscRecord(byte[] buf, GscHeader h) throws IOException 
	{
		short[] c = ba_to_sa(buf);
		
		int id = (c[0] & 127);
		id <<= 7;
		id |= (c[1] >> 1);
		this.id = id;
		
		int da = ((c[1]&1)<< 8) | c[2];
		da <<= 8;
		da |= c[3];
		da <<= 8;
		da |= c[4];
		da >>= 3;

		int dd = c[4] & 7;
		dd <<= 8;
		dd |= c[5];
		dd <<= 8;
		dd |= c[6];

		int dp = c[7];
		dp <<= 1;
		dp |= c[8] >> 7;

		int mag = c[9];
		mag <<= 3;
		mag |= c[10] >> 5;

		int dm = c[8] & 127;

		int ba = (c[10] >> 1) & 15;
		this.mb = band[ba];
		this.cl = (c[11] >> 4) & 7;

		int pl = c[11] & 15;
		this.plate = h.plate[pl];
		this.epoch = (0 < h.epoch[pl]) ? h.epoch[pl] - 2000 : 0;
		
		int mul = c[10] & 1;
		this.multiple = (mul == 0) ? 'F' : 'T';
		
		double tra = (double) da / h.scale_ra + h.amin;
		tra = (tra < 0) ? tra + 360 : ((360 <= tra) ? tra -360 : tra);
		this.ra = tra;
		
		this.dec = (double) dd / h.scale_dec + h.dmin;
		this.poserr = (double) dp / h.scale_pos;
		this.m = (double) mag / h.scale_mag + h.magoff;
		this.merr = (double) dm / h.scale_mag;
		this.reg = h.region;
	}
	
	private static short[] ba_to_sa(byte[] ba) 
	{
		short[] sa = null;
		
		if (ba != null) {
			sa = new short[ba.length];
			for (int i=0; i < sa.length; i++) {
				sa[i] = (short)(ba[i] & 0xFF);
			}
		}
		
		return sa;
	}
}
