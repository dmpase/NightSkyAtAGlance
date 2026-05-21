package lib.util;

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

public class ByteArray {
	
	public static byte[] to_bytes(boolean v)
	{
		byte[] result = new byte[1];
		
		result[0] = (byte)(v ? 0x01 : 0x00);

		return result;
	}
	
	public static byte[] to_bytes(byte v)
	{
		byte[] result = new byte[1];
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (v & 0xff);
			v = (byte)(v >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(short v)
	{
		byte[] result = new byte[2];
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (v & 0xff);
			v = (short)(v >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(char v)
	{
		byte[] result = new byte[2];
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (v & 0xff);
			v = (char)(v >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(int v)
	{
		byte[] result = new byte[4];
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (v & 0xff);
			v = (int)(v >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(long v)
	{
		byte[] result = new byte[8];
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (v & 0xff);
			v = (long)(v >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(float v)
	{
		byte[] result = new byte[4];
		
		int m = Float.floatToRawIntBits(v);
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (m & 0xff);
			m = (int)(m >> 8);
		}

		return result;
	}
	
	public static byte[] to_bytes(double v)
	{
		byte[] result = new byte[8];
		
		long m = Double.doubleToRawLongBits(v);
		
		for (int i=0; i < result.length; i++) {
			result[i] = (byte) (m & 0xff);
			m = (long)(m >> 8);
		}

		return result;
	}
	
	public static boolean to_boolean(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return result != 0;
	}
	
	public static byte to_byte(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return (byte) result;
	}
	
	public static char to_char(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return (char) result;
	}
	
	public static short to_short(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return (short) result;
	}
	
	public static int to_int(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return (int) result;
	}
	
	public static long to_long(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}

		return (long) result;
	}

	public static float to_float(byte[] a)
	{
		int result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return Float.intBitsToFloat(result);
	}

	public static double to_double(byte[] a)
	{
		long result = 0;
		
		for (int i=0; a != null && i < a.length; i++) {
			result |= (long) ((int)(a[i] & 0xff) << (8*i));
		}
		
		return Double.longBitsToDouble(result);
	}
	
	/*
	public static void main(String[] a)
	{
		int k = 0xleadbeef;
		System.out.println(k);
		System.out.println(to_int(to_bytes(k)));
	}
	*/
}
