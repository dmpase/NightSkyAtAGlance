package nightskyataglance.util;

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


public class RaDec {
	public final Long   enc;
	public final double ra;
	public final double dec;
	public final int    width;
	public final int    height;
	
	private static final int ds =  0;
	private static final int ss = 16;
	private static final int hs = 20;
	private static final int rs = 32;
	private static final int ws = 48;
	private static long encode(double r, double d, int w, int h)
	{
		long t = 0;

		t += (long) Math.abs(d*10)             << ds;	// 10 x declination
		t += (long) (d < 0 ? 0x1L : 0x0L)      << ss;	// declination sign
		t += (long) h                          << hs;	// height
		t += (long) (0xFFFF & (short)(10.0*r)) << rs;	// 10 x right ascension
		t += (long) w                          << ws;	// width

		return t;
	}

	private static double decode_r(long e)
	{
		return ((e >> rs) & 0x0FFFFL) / 10.0;
	}

	private static double decode_d(long e)
	{
		return ((((e >> ss) & 0x1) == 0) ? 1 : -1) * ((e >> ds) & 0x0FFFFL) / 10.0;
	}

	private static int decode_w(long e)
	{
		return (int) ((e >> ws) & 0x0FFFFL);
	}

	private static int decode_h(long e)
	{
		return (int) ((e >> hs) & 0x0FFFL);
	}

	public RaDec(double r, double d) 
	{
		ra     = r; 
		dec    = d;
		width  = 60;
		height = 60;
		enc    = Long.valueOf(encode(ra, dec, width, height));
	}

	public RaDec(double r, double d, int w, int h) 
	{
		ra     = r; 
		dec    = d;
		width  = w;
		height = h;
		enc    = Long.valueOf(encode(ra, dec, width, height));
	}

	public RaDec(long e) 
	{
		ra     = decode_r(e); 
		dec    = decode_d(e);
		width  = decode_w(e);
		height = decode_h(e);
		enc    = Long.valueOf(e);
	}

	public RaDec(Long en) 
	{
		long e = en.longValue();
		ra     = decode_r(e); 
		dec    = decode_d(e);
		width  = decode_w(e);
		height = decode_h(e);
		enc    = en;
	}

	public boolean equals(RaDec obj) { return enc.longValue() == obj.enc.longValue(); }
	public int     hashCode() { return enc.hashCode(); }
	public String  toString() { return String.format("ra=%.2f de=%.2f w=%d h=%d en=%016x", ra, dec, width, height, enc); }
}
