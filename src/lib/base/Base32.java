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

public class Base32 extends Base
{
	public Base32()
	{
		super(5, "0123456789abcdefghijklmnopqrstuv");
		//        00000000001111111111222222222233
		//        01234567890123456789012345678901
	}
	
	public Base32(String str)
	{
		super(5, str);
	}
	
	@Override
	public byte[] encode(byte[] ba)
	{
		String result = "";
		
		int byte_idx = 0;
		for (int i=0; i < ba.length/bits; i++) {
			byte_idx = i*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>3) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<2) & mask) | (((0xFF&(int)ba[byte_idx+1])>>6) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])<<4) & mask) | (((0xFF&(int)ba[byte_idx+2])>>4) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+2])<<1) & mask) | (((0xFF&(int)ba[byte_idx+3])>>7) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+3])>>2) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+3])<<3) & mask) | (((0xFF&(int)ba[byte_idx+4])>>5) & mask);
			int d7 = (((0xFF&(int)ba[byte_idx+4])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		if ((ba.length % bits) == 0) {
			;
		} else if ((ba.length % bits) == 1) {
			byte_idx = (ba.length/bits)*bits;

			int d0 = (((0xFF&(int)ba[byte_idx+0])>>3) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<2) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1];
		} else if ((ba.length % bits) == 2) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>3) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<2) & mask) | (((0xFF&(int)ba[byte_idx+1])>>6) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])<<4) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3];
		} else if ((ba.length % bits) == 3) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>3) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<2) & mask) | (((0xFF&(int)ba[byte_idx+1])>>6) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])<<4) & mask) | (((0xFF&(int)ba[byte_idx+2])>>4) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+2])<<1) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4];
		} else if ((ba.length % bits) == 4) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>3) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])<<2) & mask) | (((0xFF&(int)ba[byte_idx+1])>>6) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])<<4) & mask) | (((0xFF&(int)ba[byte_idx+2])>>4) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+2])<<1) & mask) | (((0xFF&(int)ba[byte_idx+3])>>7) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+3])>>2) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+3])<<3) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6];
		}
		
		return result.getBytes();
	}

	// 1 dig. == 1 byte  -> (1*5+3)/8 == 1
	// 2 dig. == 2 bytes -> (2*5+3)/8 == 2
	// 3 dig. == 2 bytes -> (3*5+3)/8 == 2
	// 4 dig. == 3 bytes -> (4*5+3)/8 == 3
	// 5 dig. == 4 bytes -> (5*5+3)/8 == 4
	// 6 dig. == 4 bytes -> (6*5+3)/8 == 4
	// 7 dig. == 5 bytes -> (7*5+3)/8 == 5
	// 8 dig. == 5 bytes -> (8*5+3)/8 == 5
	@Override
	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length*bits + 3)/8;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < (ba.length/8); i++) {
			char_idx = i*8;	// 8 dig.  per 5 bytes
			byte_idx = i*5;	// 5 bytes per 8 dig.

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];
			int i6 = indx_set[ba[char_idx+6]];
			int i7 = indx_set[ba[char_idx+7]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1 | i3>>4);
			result[byte_idx+2] = (byte)((i3 & 0x0F)<<4 | i4>>1);
			result[byte_idx+3] = (byte)((i4 & 0x01)<<7 | i5<<2 | i6>>3);
			result[byte_idx+4] = (byte)((i6 & 0x07)<<5 | i7<<0);
		}
		
		if ((ba.length % 8) == 0) {
			;
		} else if ((ba.length % 8) == 1) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];

			result[byte_idx+0] = (byte)((i0       )<<3);
		} else if ((ba.length % 8) == 2) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
		} else if ((ba.length % 8) == 3) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1);
		} else if ((ba.length % 8) == 4) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1 | i3>>4);
		} else if ((ba.length % 8) == 5) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-3;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1 | i3>>4);
			result[byte_idx+2] = (byte)((i3 & 0x0F)<<4 | i4>>1);
		} else if ((ba.length % 8) == 6) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-4;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1 | i3>>4);
			result[byte_idx+2] = (byte)((i3 & 0x0F)<<4 | i4>>1);
			result[byte_idx+3] = (byte)((i4 & 0x01)<<7 | i5<<2);
		} else if ((ba.length % 8) == 7) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-4;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];
			int i6 = indx_set[ba[char_idx+6]];

			result[byte_idx+0] = (byte)((i0       )<<3 | i1>>2);
			result[byte_idx+1] = (byte)((i1 & 0x03)<<6 | i2<<1 | i3>>4);
			result[byte_idx+2] = (byte)((i3 & 0x0F)<<4 | i4>>1);
			result[byte_idx+3] = (byte)((i4 & 0x01)<<7 | i5<<2 | i6>>3);
		}

		return result;
	}
}
