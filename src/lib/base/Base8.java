package lib.base;

/*******************************************************************************
 * Copyright (c) 1988-2019 Douglas M. Pase                                     *
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

public class Base8 extends Base
{
	public Base8()
	{
		super(3, "01234567");
	}
	
	public Base8(String str)
	{
		super(3, str);
	}
	
	// 3 bytes (3x8 bits) == 8 octal digits (8x3 bits)
	public byte[] encode(byte[] ba)
	{
		String result = "";
		
		int byte_idx = 0;
		for (int i=0; i < (ba.length/bits); i++) {
			byte_idx = i*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>5) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+0])<<1) & mask) | (((0xFF&(int)ba[byte_idx+1])>>7) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask) | (((0xFF&(int)ba[byte_idx+2])>>6) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+2])>>3) & mask);
			int d7 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		if ((ba.length % bits) == 0) {
			;
		} else if ((ba.length % bits) == 1) {
			byte_idx = (ba.length/bits)*bits;

			int d0 = (((0xFF&(int)ba[byte_idx+0])>>5) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+0])<<1) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2];
		} else if ((ba.length % bits) == 2) {
			byte_idx = (ba.length/bits)*bits;

			int d0 = (((0xFF&(int)ba[byte_idx+0])>>5) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>2) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+0])<<1) & mask) | (((int)ba[byte_idx+1]>>7) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+1])>>1) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+1])<<2) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5];
		}
		
		return result.getBytes();
	}

	// 3 bytes (3x8 bits) == 8 octal digits (8x3 bits)
	// 1 o.d. == 1 byte  -> (1*3+7)/8 == 1
	// 2 o.d. == 1 byte  -> (2*3+7)/8 == 1
	// 3 o.d. == 2 bytes -> (3*3+7)/8 == 2
	// 4 o.d. == 2 bytes -> (4*3+7)/8 == 2
	// 5 o.d. == 2 bytes -> (5*3+7)/8 == 2
	// 6 o.d. == 3 bytes -> (6*3+7)/8 == 3
	// 7 o.d. == 3 bytes -> (7*3+7)/8 == 3
	// 8 o.d. == 3 bytes -> (8*3+7)/8 == 3
	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length*bits + 5)/8;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < (ba.length/8); i++) {
			char_idx = i*8;	// 8 o.d.  per 3 bytes
			byte_idx = i*3;	// 3 bytes per 8 o.d.

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];
			int i6 = indx_set[ba[char_idx+6]];
			int i7 = indx_set[ba[char_idx+7]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
			result[byte_idx+1] = (byte)((i2&0x1)<<7 | i3<<4 | i4<<1 | i5>>2);
			result[byte_idx+2] = (byte)((i5&0x3)<<6 | i6<<3 | i7<<0);
		}
		
		if ((ba.length % 8) == 0) {
			;
		} else if ((ba.length % 8) == 1) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];

			result[byte_idx+0] = (byte)(i0<<5);
		} else if ((ba.length % 8) == 2) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;
			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2);
		} else if ((ba.length % 8) == 3) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
		} else if ((ba.length % 8) == 4) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
			result[byte_idx+1] = (byte)((i2&0x1)<<7 | i3<<4);
		} else if ((ba.length % 8) == 5) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
			result[byte_idx+1] = (byte)((i2&0x1)<<7 | i3<<4 | i4<<1);
		} else if ((ba.length % 8) == 6) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-2;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
			result[byte_idx+1] = (byte)((i2&0x1)<<7 | i3<<4 | i4<<1 | i5>>2);
		} else if ((ba.length % 8) == 7) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-3;

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];
			int i2 = indx_set[ba[char_idx+2]];
			int i3 = indx_set[ba[char_idx+3]];
			int i4 = indx_set[ba[char_idx+4]];
			int i5 = indx_set[ba[char_idx+5]];
			int i6 = indx_set[ba[char_idx+6]];

			result[byte_idx+0] = (byte)(i0<<5       | i1<<2 | i2>>1);
			result[byte_idx+1] = (byte)((i2&0x1)<<7 | i3<<4 | i4<<1 | i5>>2);
			result[byte_idx+2] = (byte)((i5&0x3)<<6 | i6<<3);
		}

		return result;
	}

	// 8 (160/3 = 53r1)
	// 00000000 | 00000000 | 00000000
	// 00011122 | 23334445 | 55666777
	
	public static void main(String[] args)
	{
		Base b = new Base8();
		byte[] ba = {'a', //'n', //'z', //'b', //'c', //'d', //'e', //'f', //'g', //'h', //'i', //'j', 
				};
		System.out.println(ba.length + " " + new String(ba));
		String e = new String(b.encode(ba));
		System.out.println(e.length() + " " + e);
		byte[] bb = b.decode(e.getBytes());
		System.out.println(bb.length + " " + new String(bb));
	}
}
