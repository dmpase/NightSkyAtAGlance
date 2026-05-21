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

public class Base16 extends Base
{
	public Base16()
	{
		super(4, "0123456789abcdef");
	}
	
	public Base16(String str)
	{
		super(4, str);
	}
	
	@Override
	public byte[] encode(byte[] ba)
	{
		String result = "";
		
		int byte_idx = 0;
		for (int i=0; i < ba.length/bits; i++) {
			byte_idx = i*bits;
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>4) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>0) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])>>0) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+2])>>4) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);
			int d6 = (((0xFF&(int)ba[byte_idx+3])>>4) & mask);
			int d7 = (((0xFF&(int)ba[byte_idx+3])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		if ((ba.length % bits) == 0) {
			;
		} else if ((ba.length % bits) == 1) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>4) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1];
		} else if ((ba.length % bits) == 2) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>4) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>0) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3];
		} else if ((ba.length % bits) == 3) {
			byte_idx = (ba.length/bits)*bits;
			
			int d0 = (((0xFF&(int)ba[byte_idx+0])>>4) & mask);
			int d1 = (((0xFF&(int)ba[byte_idx+0])>>0) & mask);
			int d2 = (((0xFF&(int)ba[byte_idx+1])>>4) & mask);
			int d3 = (((0xFF&(int)ba[byte_idx+1])>>0) & mask);
			int d4 = (((0xFF&(int)ba[byte_idx+2])>>4) & mask);
			int d5 = (((0xFF&(int)ba[byte_idx+2])>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5];
		}
		
		return result.getBytes();
	}

	// 1 byte (1x8 bits) == 2 hex digits (2x4 bits)
	// 1 h.d. == 1 byte  -> (1+1)/2 == 1
	// 2 h.d. == 1 byte  -> (2+1)/2 == 1
	@Override
	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length + 1)/2;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < (ba.length/2); i++) {
			char_idx = i*2;	// 2 h.d.  per 1 bytes
			byte_idx = i*1;	// 1 bytes per 2 h.d.

			int i0 = indx_set[ba[char_idx+0]];
			int i1 = indx_set[ba[char_idx+1]];

			result[byte_idx+0] = (byte)(i0<<4 | i1<<0);
		}
		
		if ((ba.length % 2) == 0) {
			;
		} else if ((ba.length % 2) == 1) {
			char_idx = (ba.length/2);
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]];

			result[byte_idx+0] = (byte)(i0<<4);
		}

		return result;
	}
	
	// 16 (160/4 = 40)
	// 00000000
	// 00001111

	public static void main(String[] args)
	{
		Base b = new Base16();
		byte[] ba = {'a', 'n', 'z', 'b', 'c' };
		System.out.println(new String(ba));
		String e = new String(b.encode(ba));
		System.out.println(e);
		byte[] bb = b.decode(e.getBytes());
		System.out.println(new String(bb));
	}
}
