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

public class Base4 extends Base
{
	public Base4()
	{
		super(2, "0123");
	}
	
	public Base4(String str)
	{
		super(2, str);
	}

	public byte[] encode(byte[] ba)
	{
		String result = "";
		
		int byte_idx = 0;
		for (int i=0; i < (ba.length/bits); i++) {
			byte_idx = i*bits;
			int d0 = (((int)ba[byte_idx+0]>>6) & mask);
			int d1 = (((int)ba[byte_idx+0]>>4) & mask);
			int d2 = (((int)ba[byte_idx+0]>>2) & mask);
			int d3 = (((int)ba[byte_idx+0]>>0) & mask);
			int d4 = (((int)ba[byte_idx+1]>>6) & mask);
			int d5 = (((int)ba[byte_idx+1]>>4) & mask);
			int d6 = (((int)ba[byte_idx+1]>>2) & mask);
			int d7 = (((int)ba[byte_idx+1]>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		if ((ba.length % bits) == 0) {
			;
		} else if ((ba.length % bits) == 1) {
			byte_idx = (ba.length/bits)*bits;

			int d0 = (((int)ba[byte_idx+0]>>6) & mask);
			int d1 = (((int)ba[byte_idx+0]>>4) & mask);
			int d2 = (((int)ba[byte_idx+0]>>2) & mask);
			int d3 = (((int)ba[byte_idx+0]>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3];
		}
		
		return result.getBytes();
	}

	// 1 bytes (1x8 bits) == 4 quart. digits (4x2 bits)
	// 1 q.d. == 1 byte  -> (1+3)/4 == 1
	// 2 q.d. == 1 byte  -> (2+3)/4 == 1
	// 3 q.d. == 1 byte  -> (3+3)/4 == 1
	// 4 q.d. == 1 byte  -> (4+3)/4 == 1
	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length + 3)/4;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < ba.length/4; i++) {
			char_idx = i*4;
			byte_idx = i;

			int i0 = indx_set[ba[char_idx+0]] << 6;
			int i1 = indx_set[ba[char_idx+1]] << 4;
			int i2 = indx_set[ba[char_idx+2]] << 2;
			int i3 = indx_set[ba[char_idx+3]] << 0;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3);
		}
		
		if ((ba.length % 4) == 0) {
			;
		} else if ((ba.length % 4) == 1) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 6;

			result[byte_idx] = (byte)(i0);
		} else if ((ba.length % 4) == 2) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 6;
			int i1 = indx_set[ba[char_idx+1]] << 4;

			result[byte_idx] = (byte)(i0 | i1);
		} else if ((ba.length % 4) == 3) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 6;
			int i1 = indx_set[ba[char_idx+1]] << 4;
			int i2 = indx_set[ba[char_idx+2]] << 2;

			result[byte_idx] = (byte)(i0 | i1 | i2);
		}

		return result;
	}

	// 4 (160/2 = 80)
	// 00000000
	// 00112233

	public static void main(String[] args)
	{
		Base b = new Base4();
		byte[] ba = {'a', 'n', 'z', 'b', 'c', 'd', 'e', 'f', 'g', };
		System.out.println(new String(ba));
		String e = new String(b.encode(ba));
		System.out.println(e);
		byte[] bb = b.decode(e.getBytes());
		System.out.println(new String(bb));
	}
}
