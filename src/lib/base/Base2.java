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

public class Base2 extends Base {

	public Base2()
	{
		super(1, "01");
	}
	
	public Base2(String str)
	{
		super(1, str);
	}
	
	public byte[] encode(byte[] ba)
	{
		String result = "";
		
		int byte_idx = 0;
		for (int i=0; i < (ba.length/bits)*bits; i++) {
			byte_idx = i*bits;

			int d0 = (((int)ba[byte_idx+0]>>7) & mask);
			int d1 = (((int)ba[byte_idx+0]>>6) & mask);
			int d2 = (((int)ba[byte_idx+0]>>5) & mask);
			int d3 = (((int)ba[byte_idx+0]>>4) & mask);
			int d4 = (((int)ba[byte_idx+0]>>3) & mask);
			int d5 = (((int)ba[byte_idx+0]>>2) & mask);
			int d6 = (((int)ba[byte_idx+0]>>1) & mask);
			int d7 = (((int)ba[byte_idx+0]>>0) & mask);

			result += (char)char_set[d0] + "" + (char)char_set[d1] + "" + (char)char_set[d2] + "" + (char)char_set[d3] + "" +
					(char)char_set[d4] + "" + (char)char_set[d5] + "" + (char)char_set[d6] + "" + (char)char_set[d7];
		}
		
		return result.getBytes();
	}

	public byte[] decode(byte[] ba)
	{
		int byte_ct = (ba.length*bits + 7)/8;

		byte[] result = new byte[byte_ct];

		int char_idx = 0;
		int byte_idx = 0;
		for (int i=0; i < ba.length*bits/8; i++) {
			char_idx = i*8;
			byte_idx = i;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;
			int i3 = indx_set[ba[char_idx+3]] << 4;
			int i4 = indx_set[ba[char_idx+4]] << 3;
			int i5 = indx_set[ba[char_idx+5]] << 2;
			int i6 = indx_set[ba[char_idx+6]] << 1;
			int i7 = indx_set[ba[char_idx+7]] << 0;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3 | i4 | i5 | i6 | i7);
		}
		
		if ((ba.length % 8) == 0) {
			;
		} else if ((ba.length % 8) == 1) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;

			result[byte_idx] = (byte)(i0);
		} else if ((ba.length % 8) == 2) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;

			result[byte_idx] = (byte)(i0 | i1);
		} else if ((ba.length % 8) == 3) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;

			result[byte_idx] = (byte)(i0 | i1 | i2);
		} else if ((ba.length % 8) == 4) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;
			int i3 = indx_set[ba[char_idx+3]] << 4;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3);
		} else if ((ba.length % 8) == 5) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;
			int i3 = indx_set[ba[char_idx+3]] << 4;
			int i4 = indx_set[ba[char_idx+4]] << 3;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3 | i4);
		} else if ((ba.length % 8) == 6) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;
			int i3 = indx_set[ba[char_idx+3]] << 4;
			int i4 = indx_set[ba[char_idx+4]] << 3;
			int i5 = indx_set[ba[char_idx+5]] << 2;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3 | i4 | i5);
		} else if ((ba.length % 8) == 7) {
			char_idx = (ba.length/8)*8;
			byte_idx = result.length-1;

			int i0 = indx_set[ba[char_idx+0]] << 7;
			int i1 = indx_set[ba[char_idx+1]] << 6;
			int i2 = indx_set[ba[char_idx+2]] << 5;
			int i3 = indx_set[ba[char_idx+3]] << 4;
			int i4 = indx_set[ba[char_idx+4]] << 3;
			int i5 = indx_set[ba[char_idx+5]] << 2;
			int i6 = indx_set[ba[char_idx+6]] << 1;

			result[byte_idx] = (byte)(i0 | i1 | i2 | i3 | i4 | i5 | i6);
		}

		return result;
	}
	
	// 2 (160/1 = 160)
	// 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
	// 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
	// 01234567
	
	public static void main(String[] args)
	{
		Base b = new Base2();
		byte[] ba = {'a', 'n', 'z', 'b', 'c' };
		System.out.println(new String(ba));
		String e = new String(b.encode(ba));
		System.out.println(e);
		byte[] bb = b.decode(e.getBytes());
		System.out.println(new String(bb));
	}
}
