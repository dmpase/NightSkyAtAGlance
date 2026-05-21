package lib.base;

/*******************************************************************************
 * Copyright (c) 1988-2020 Douglas M. Pase                                     *
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

public class Base64 extends Base
{
	public Base64()
	{
		super(6, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-+");
		        //0000000000111111111122222222223333333333444444444455555555556666
		        //0123456789012345678901234567890123456789012345678901234567890123
	}

	public Base64(String str)
	{
		super(6, str);
	}
	
	public byte[] encode(byte[] ba, int off, int len)
	{
		byte[] tmp = new byte[len];
		
		for (int i=0; i < len; i++) {
			tmp[i] = ba[i+off];
		}
		
		return encode(tmp);
	}
	
	public byte[] encode(byte[] ba)
	{
		String result = "";

		if (ba == null) return null;
		
		int byte_idx = 0;
		for (int i=0; i < ba.length/bits; i++) {
			byte_idx = i*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask) | (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask) | (((0xFF&(int)ba[byte_idx+2])>>6) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+3])>>2) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+3])<<4) & mask) | (((0xFF&(int)ba[byte_idx+4])>>4) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+4])<<2) & mask) | (((0xFF&(int)ba[byte_idx+5])>>6) & mask);
			int d7 = (((0xFF&(int)ba[byte_idx+5])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		if ((ba.length % bits) == 0) {
			;
		} else if ((ba.length % bits) == 1) {
			byte_idx = (ba.length/bits)*bits;

			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1];
		} else if ((ba.length % bits) == 2) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask) | (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2];
		} else if ((ba.length % bits) == 3) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask) | (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask) | (((0xFF&(int)ba[byte_idx+2])>>6) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3];
		} else if ((ba.length % bits) == 4) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask) | (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask) | (((0xFF&(int)ba[byte_idx+2])>>6) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+3])>>2) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+3])<<4) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5];
		} else if ((ba.length % bits) == 5) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<4) & mask) | (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask) | (((0xFF&(int)ba[byte_idx+2])>>6) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+3])>>2) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+3])<<4) & mask) | (((0xFF&(int)ba[byte_idx+4])>>4) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+4])<<2) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6];
		}
		
		return result.getBytes();
	}

	
	// 64 (160/5 = 32)
	// 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
	// 00000011 11112222 22333333 44444455 55556666 66777777 88888899 99990000 00111111 22222233
	//
	// 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
	// 33334444 44555555 66666677 77778888 88999999 00000011 11112222 22333333 44444455 55556666
	
	public byte[] decode(byte[] ba, int off, int len)
	{
		byte[] tmp = new byte[len];
		
		for (int i=0; i < len; i++) {
			tmp[i] = ba[i+off];
		}
		
		return decode(tmp);
	}

	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length*bits+3)/8;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < (ba.length/4); i++) {
			char_idx = i*4;	// 4 dig.  per 3 bytes
			byte_idx = i*3;	// 3 bytes per 4 dig.

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];

			result[byte_idx+0] = (byte)((i0       )<<2 | i1>>4);
			result[byte_idx+1] = (byte)((i1 & 0x0F)<<4 | i2>>2);
			result[byte_idx+2] = (byte)((i2 & 0x03)<<6 | i3>>0);
		}
		
		if ((ba.length % 8) == 0) {
			;
		} else if ((ba.length % 4) == 1) {
			char_idx = (ba.length/4)*4;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];

			result[byte_idx+0] = (byte)((i0       )<<2);
		} else if ((ba.length % 4) == 2) {
			char_idx = (ba.length/4)*4;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];

			result[byte_idx+0] = (byte)((i0       )<<2 | i1>>4);
		} else if ((ba.length % 4) == 3) {
			char_idx = (ba.length/4)*4;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];

			result[byte_idx+0] = (byte)((i0       )<<2 | i1>>4);
			result[byte_idx+1] = (byte)((i1 & 0x0F)<<4 | i2>>2);
		}

		return result;
	}
}
