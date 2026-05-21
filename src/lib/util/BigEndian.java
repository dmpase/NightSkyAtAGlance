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

public class BigEndian {

    public static final int         SIZEOF_BOOLEAN = 1;
    public static final int         SIZEOF_BYTE    = 1;
    public static final int         SIZEOF_CHAR    = 2;
    public static final int         SIZEOF_SHORT   = 2;
    public static final int         SIZEOF_INT     = 4;
    public static final int         SIZEOF_LONG    = 8;
    public static final int         SIZEOF_FLOAT   = 4;
    public static final int         SIZEOF_DOUBLE  = 8;

    public static final int         BITS_PER_BYTE  = 8;
    
    public static final int         NULL           = ~0;
	
	public static byte[] toBytes(boolean v)
	{
		byte[] result = new byte[SIZEOF_BOOLEAN];
		
		result[0] = (byte)(v ? 0x01 : 0x00);

		return result;
	}

	public static byte[] toBytes(byte v)
	{
		byte[] result = new byte[SIZEOF_BYTE];
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (v & 0xff);
			v = (byte)(v >> BITS_PER_BYTE);
		}

		return result;
	}

	public static byte[] toBytes(short v)
	{
		byte[] result = new byte[SIZEOF_SHORT];
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (v & 0xff);
			v = (short)(v >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static byte[] toBytes(char v)
	{
		byte[] result = new byte[SIZEOF_CHAR];
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (v & 0xff);
			v = (char)(v >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static byte[] toBytes(int v)
	{
		byte[] result = new byte[SIZEOF_INT];
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (v & 0xff);
			v = (int)(v >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static byte[] toBytes(long v)
	{
		byte[] result = new byte[SIZEOF_LONG];
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (v & 0xff);
			v = (long)(v >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static byte[] toBytes(float v)
	{
		byte[] result = new byte[SIZEOF_FLOAT];
		
		int m = Float.floatToRawIntBits(v);
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (m & 0xff);
			m = (int)(m >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static byte[] toBytes(double v)
	{
		byte[] result = new byte[SIZEOF_DOUBLE];
		
		long m = Double.doubleToRawLongBits(v);
		
		for (int i=result.length-1; 0 <= i; i--) {
			result[i] = (byte) (m & 0xff);
			m = (long)(m >> BITS_PER_BYTE);
		}

		return result;
	}
	
	public static boolean toBoolean(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return result != 0;
	}
	
	public static byte toByte(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return (byte) result;
	}
	
	public static char toChar(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return (char) result;
	}
	
	public static short toShort(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return (short) result;
	}
	
	public static int toInt(byte[] a)
	{
		long result = 0;

		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return (int) result;
	}
	
	public static long toLong(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}

		return (long) result;
	}

	public static float toFloat(byte[] a)
	{
		int result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return Float.intBitsToFloat(result);
	}

	public static double toDouble(byte[] a)
	{
		long result = 0;
		
		if (a != null) {
			for (int i=a.length-1; 0 <= i; i--) {
				result |= (long) ((long)(a[i] & 0xffL) << (BITS_PER_BYTE*(a.length - i - 1)));
			}
		}
		
		return Double.longBitsToDouble(result);
	}
	
	/*
	public static void main(String[] a) throws IOException
	{
		double v = (double) 3.14159;
		System.out.println(v);
		System.out.println(to_double(to_bytes(v)));
		File fd = File.createTempFile("xxx", ".tmp");
		RandomAccessFile raf = new RandomAccessFile(fd, "rw");
		raf.write(to_bytes(v));
		raf.seek(0);
		v = raf.readDouble();
		raf.close();
		fd.delete();
		System.out.println(v);
	}
	*/
}
