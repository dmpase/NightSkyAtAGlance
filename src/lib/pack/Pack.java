package lib.pack;

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

import java.io.*;

public class Pack {
	/*
	 * this class...
	 */
	
	/* format for each data type...
	 * 
	 * null    = { 0 }
	 * boolean = { 'b', value?1:0 }
	 * byte    = { 'B', bits }
	 * char    = { 'c', bits:0-7, bits:8-15 }
	 * short   = { 's', bits:0-7, bits:8-15 }
	 * int     = { 'i', bits:0-7, bits:8-15, bits:16-23, bits:24-31 }
	 * float   = { 'f', bits:0-7, bits:8-15, bits:16-23, bits:24-31 }
	 * long    = { 'l', bits:0-7, bits:8-15, bits:16-23, bits:24-31, bits:32-39, bits:40-47, bits:48-55, bits:56-63 }
	 * double  = { 'd', bits:0-7, bits:8-15, bits:16-23, bits:24-31, bits:32-39, bits:40-47, bits:48-55, bits:56-63 }
	 * String  = { 's', size:0-7, size:8-15, size:16-23, size:24-31, char:0, char:1, ... }
	 * Array   = { '[', size:0-7, size:8-15, size:16-23, size:24-31, value:0, value:1, ... }
	 */
	
	// message data types
	public static final byte NULL           =  0 ;
	public static final byte BOOLEAN        = 'b';
	public static final byte BYTE           = 'B';
	public static final byte CHAR           = 'c';
	public static final byte SHORT          = 's';
	public static final byte INT            = 'i';
	public static final byte FLOAT          = 'f';
	public static final byte LONG           = 'l';
	public static final byte DOUBLE         = 'd';
	public static final byte STRING         = 'S';
	public static final byte ARRAY          = '[';

	// size of the data without the type indicator
	private static final int SIZEOF_NULL    = 0;
	private static final int SIZEOF_BOOLEAN = 1;
	private static final int SIZEOF_BYTE    = 1;
	private static final int SIZEOF_CHAR    = 2;
	private static final int SIZEOF_SHORT   = 2;
	private static final int SIZEOF_INT     = 4;
	private static final int SIZEOF_FLOAT   = 4;
	private static final int SIZEOF_LONG    = 8;
	private static final int SIZEOF_DOUBLE  = 8;

	// message data sizes
	public static final int SIZE_TYPE    = 1;								// bytes to represent the data type
	public static final int SIZE_NULL    = SIZE_TYPE+SIZEOF_NULL;			// NULL + contents
	public static final int SIZE_BOOLEAN = SIZE_TYPE+SIZEOF_BOOLEAN;		// BOOLEAN + contents
	public static final int SIZE_BYTE    = SIZE_TYPE+SIZEOF_BYTE;			// BYTE + contents
	public static final int SIZE_CHAR    = SIZE_TYPE+SIZEOF_CHAR;			// CHAR + contents
	public static final int SIZE_SHORT   = SIZE_TYPE+SIZEOF_SHORT;			// SHORT + contents
	public static final int SIZE_INT     = SIZE_TYPE+SIZEOF_INT;			// INT + contents
	public static final int SIZE_FLOAT   = SIZE_TYPE+SIZEOF_FLOAT;			// FLOAT + contents
	public static final int SIZE_LONG    = SIZE_TYPE+SIZEOF_LONG;			// LONG + contents
	public static final int SIZE_DOUBLE  = SIZE_TYPE+SIZEOF_DOUBLE;			// DOUBLE + contents
	public static final int SIZE_STRING  = SIZE_TYPE+SIZEOF_INT;			// STRING + size in bytes (+ length added at run time)
	public static final int SIZE_ARRAY   = SIZE_TYPE+SIZE_TYPE+SIZEOF_INT;	// ARRAY + type + size in elements (+ length added at run time)
	
	
	public static int sizeof(int type)
	{
		int result = 0;

		switch (type) {
		case NULL :
			result = SIZE_NULL; 
			break;
		case BOOLEAN :
			result = SIZE_BOOLEAN; 
			break;
		case BYTE :
			result = SIZE_BYTE; 
			break;
		case CHAR :
			result = SIZE_CHAR; 
			break;
		case SHORT :
			result = SIZE_SHORT; 
			break;
		case INT :
			result = SIZE_INT; 
			break;
		case FLOAT :
			result = SIZE_FLOAT; 
			break;
		case LONG :
			result = SIZE_LONG; 
			break;
		case DOUBLE :
			result = SIZE_DOUBLE; 
			break;
		case STRING :
			result = SIZE_STRING; 
			break;
		}

		return result;
	}

	public static byte get_data_type(byte[] ba)
	{
		assert ba != null;

		return get_data_type(ba, 0);
	}
	
	public static byte get_data_type(byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx < ba.length;

		return ba[idx];
	}
	
	public static void set_data_type(byte[] ba, byte type)
	{
		assert ba != null;

		set_data_type(ba, 0, type);
	}
	
	public static void set_data_type(byte[] ba, int idx, byte type)
	{
		assert ba != null && 0 <= idx && idx < ba.length;

		ba[idx] = type;
	}

	
	
	
	
	public static byte data_type(boolean m)
	{
		return PackBoolean.data_type();
	}
	
	public static int data_size(boolean m)
	{
		return PackBoolean.data_size();
	}
	
	public static byte[] pack(boolean m)
	{
		return PackBoolean.pack(m);
	}

	public static byte[] pack(boolean m, byte[] b)
	{
		return PackBoolean.pack(m,b);
	}

	public static int pack(boolean m, byte[] ba, int idx)
	{
		return PackBoolean.pack(m, ba, idx);
	}
	
	public static boolean unpackBoolean(byte[] ba)
	{
		return PackBoolean.unpackBoolean(ba);
	}
	
	public static boolean unpackBoolean(byte[] ba, int idx)
	{
		return PackBoolean.unpackBoolean(ba, idx);
	}

	public static boolean readBoolean(InputStream is) throws IOException
	{
		return PackBoolean.readBoolean(is);
	}

	public static boolean readBoolean(RandomAccessFile raf) throws IOException
	{
		return PackBoolean.readBoolean(raf);
	}

	public static int write(OutputStream os, boolean value) throws IOException
	{
		return PackBoolean.write(os, value);
	}

	public static int write(RandomAccessFile raf, boolean value) throws IOException
	{
		return PackBoolean.write(raf, value);
	}

	
	
	
	public static byte data_type(byte m)
	{
		return PackByte.data_type();
	}
	
	public static int data_size(byte m)
	{
		return PackByte.data_size();
	}
	
	public static byte[] pack(byte m)
	{
		return PackByte.pack(m);
	}
	
	public static byte[] pack(byte m, byte[] b)
	{
		return PackByte.pack(m, b);
	}
	
	public static int pack(byte m, byte[] ba, int idx)
	{
		return PackByte.pack(m, ba, idx);
	}

	public static byte unpackByte(byte[] ba)
	{
		return PackByte.unpackByte(ba);
	}

	public static byte unpackByte(byte[] ba, int idx)
	{
		return PackByte.unpackByte(ba, idx);
	}
	
	public static byte readByte(InputStream is) throws IOException
	{
		return PackByte.readByte(is);
	}

	public static byte readByte(RandomAccessFile raf) throws IOException
	{
		return PackByte.readByte(raf);
	}

	public static int write(OutputStream os, byte value) throws IOException
	{
		return PackByte.write(os, value);
	}

	public static int write(RandomAccessFile raf, byte value) throws IOException
	{
		return PackByte.write(raf, value);
	}

	
	
	
	public static byte data_type(char m)
	{
		return PackChar.data_type();
	}
	
	public static int data_size(char m)
	{
		return PackChar.data_size();
	}
	
	public static byte[] pack(char m)
	{
		return PackChar.pack(m);
	}
	
	public static byte[] pack(char m, byte[] b)
	{
		return PackChar.pack(m, b);
	}
	
	public static int pack(char m, byte[] ba, int idx)
	{
		return PackChar.pack(m, ba, idx);
	}

	public static char unpackChar(byte[] ba)
	{
		return PackChar.unpackChar(ba);
	}

	public static char unpackChar(byte[] ba, int idx)
	{
		return PackChar.unpackChar(ba, idx);
	}
	
	public static char readChar(InputStream is) throws IOException
	{
		return PackChar.readChar(is);
	}

	public static char readChar(RandomAccessFile raf) throws IOException
	{
		return PackChar.readChar(raf);
	}

	public static int write(OutputStream os, char value) throws IOException
	{
		return PackChar.write(os, value);
	}

	public static int write(RandomAccessFile raf, char value) throws IOException
	{
		return PackChar.write(raf, value);
	}

	
	
	
	public static byte data_type(short m)
	{
		return PackShort.data_type();
	}
	
	public static int data_size(short m)
	{
		return PackShort.data_size();
	}
	
	public static byte[] pack(short m)
	{
		return PackShort.pack(m);
	}
	
	public static byte[] pack(short m, byte[] b)
	{
		return PackShort.pack(m, b);
	}
	
	public static int pack(short m, byte[] ba, int idx)
	{
		return PackShort.pack(m, ba, idx);
	}
	
	public static short unpackShort(byte[] ba)
	{
		return PackShort.unpackShort(ba);
	}
	
	public static short unpackShort(byte[] ba, int idx)
	{
		return PackShort.unpackShort(ba, idx);
	}
	
	public static short readShort(InputStream is) throws IOException
	{
		return PackShort.readShort(is);
	}

	public static short readShort(RandomAccessFile raf) throws IOException
	{
		return PackShort.readShort(raf);
	}

	public static int write(OutputStream os, short value) throws IOException
	{
		return PackShort.write(os, value);
	}

	public static int write(RandomAccessFile raf, short value) throws IOException
	{
		return PackShort.write(raf, value);
	}
	
	
	
	
	public static byte data_type(int m)
	{
		return PackInt.data_type();
	}
	
	public static int data_size(int m)
	{
		return PackInt.data_size();
	}
	
	public static byte[] pack(int m)
	{
		return PackInt.pack(m);
	}
	
	public static byte[] pack(int m, byte[] b)
	{
		return PackInt.pack(m, b);
	}
	
	public static int pack(int m, byte[] ba, int idx)
	{
		return PackInt.pack(m, ba, idx);
	}
	
	public static int unpackInt(byte[] ba)
	{
		return PackInt.unpackInt(ba);
	}
	
	public static int unpackInt(byte[] ba, int idx)
	{
		return PackInt.unpackInt(ba, idx);
	}
	
	public static int readInt(InputStream is) throws IOException
	{
		return PackInt.readInt(is);
	}

	public static int readInt(RandomAccessFile raf) throws IOException
	{
		return PackInt.readInt(raf);
	}

	public static int write(OutputStream os, int value) throws IOException
	{
		return PackInt.write(os, value);
	}

	public static int write(RandomAccessFile raf, int value) throws IOException
	{
		return PackInt.write(raf, value);
	}

	
	
	
	public static byte data_type(float m)
	{
		return PackFloat.data_type();
	}
	
	public static int data_size(float m)
	{
		return PackFloat.data_size();
	}
	
	public static byte[] pack(float m)
	{
		return PackFloat.pack(m);
	}
	
	public static byte[] pack(float m, byte[] b)
	{
		return PackFloat.pack(m, b);
	}
	
	public static int pack(float m, byte[] ba, int idx)
	{
		return PackFloat.pack(m, ba, idx);
	}
	
	public static float unpackFloat(byte[] ba)
	{
		return PackFloat.unpackFloat(ba);
	}
	
	public static float unpackFloat(byte[] ba, int idx)
	{
		return PackFloat.unpackFloat(ba, idx);
	}
	
	public static float readFloat(InputStream is) throws IOException
	{
		return PackFloat.readFloat(is);
	}

	public static float readFloat(RandomAccessFile raf) throws IOException
	{
		return PackFloat.readFloat(raf);
	}

	public static int write(OutputStream os, float value) throws IOException
	{
		return PackFloat.write(os, value);
	}

	public static int write(RandomAccessFile raf, float value) throws IOException
	{
		return PackFloat.write(raf, value);
	}
	
	
	
	
	public static byte data_type(long m)
	{
		return PackLong.data_type();
	}
	
	public static int data_size(long m)
	{
		return PackLong.data_size();
	}
	
	public static byte[] pack(long m)
	{
		return PackLong.pack(m);
	}
	
	public static byte[] pack(long m, byte[] b)
	{
		return PackLong.pack(m, b);
	}
	
	public static int pack(long m, byte[] ba, int idx)
	{
		return PackLong.pack(m, ba, idx);
	}
	
	public static long unpackLong(byte[] ba)
	{
		return PackLong.unpackLong(ba);
	}
	
	public static long unpackLong(byte[] ba, int idx)
	{
		return PackLong.unpackLong(ba, idx);
	}
	
	public static long readLong(InputStream is) throws IOException
	{
		return PackLong.readLong(is);
	}

	public static long readLong(RandomAccessFile raf) throws IOException
	{
		return PackByte.readLong(raf);
	}

	public static int write(OutputStream os, long value) throws IOException
	{
		return PackLong.write(os, value);
	}

	public static int write(RandomAccessFile raf, long value) throws IOException
	{
		return PackLong.write(raf, value);
	}

	
	
	
	public static byte data_type(double m)
	{
		return PackDouble.data_type();
	}
	
	public static int data_size(double m)
	{
		return PackDouble.data_size();
	}
	
	public static byte[] pack(double m)
	{
		return PackDouble.pack(m);
	}
	
	public static byte[] pack(double m, byte[] b)
	{
		return PackDouble.pack(m, b);
	}
	
	public static int pack(double m, byte[] ba, int idx)
	{
		return PackDouble.pack(m, ba, idx);
	}
	
	public static double unpackDouble(byte[] ba)
	{
		return PackDouble.unpackDouble(ba);
	}
	
	public static double unpackDouble(byte[] ba, int idx)
	{
		return PackDouble.unpackDouble(ba, idx);
	}
	
	public static double readDouble(InputStream is) throws IOException
	{
		return PackDouble.readDouble(is);
	}

	public static double readDouble(RandomAccessFile raf) throws IOException
	{
		return PackDouble.readDouble(raf);
	}

	public static int write(OutputStream os, double value) throws IOException
	{
		return PackDouble.write(os, value);
	}

	public static int write(RandomAccessFile raf, double value) throws IOException
	{
		return PackDouble.write(raf, value);
	}

	
	

	
	public static byte data_type(String m)
	{
		return PackString.data_type();
	}
	
	public static int data_size(String m)
	{
		return PackString.data_size(m);
	}
	
	public static byte[] pack(String m)
	{
		return PackString.pack(m);
	}
	
	public static byte[] pack(String m, byte[] b)
	{
		return PackString.pack(m, b);
	}

	public static int pack(String m, byte[] ba, int idx)
	{
		return PackString.pack(m, ba, idx);
	}
	
	public static String unpackString(byte[] ba)
	{
		return PackString.unpackString(ba);
	}
	
	public static String unpackString(byte[] ba, int idx)
	{
		return PackString.unpackString(ba, idx);
	}
	
	public static String readString(InputStream is) throws IOException
	{
		return PackString.readString(is);
	}

	public static String readString(RandomAccessFile raf) throws IOException
	{
		return PackString.readString(raf);
	}

	public static int write(OutputStream os, String value) throws IOException
	{
		return PackString.write(os, value);
	}

	public static int write(RandomAccessFile raf, String value) throws IOException
	{
		return PackString.write(raf, value);
	}
	

	
	
	public static byte data_type(boolean[] m)
	{
		return PackBooleanArray.data_type();
	}
	
	public static int data_size(boolean[] m)
	{
		return PackBooleanArray.data_size(m);
	}
	
	public static byte[] pack(boolean[] m)
	{
		return PackBooleanArray.pack(m);
	}
	
	public static int pack(boolean[] m, byte[] ba, int idx)
	{
		return PackBooleanArray.pack(m, ba, idx);
	}
	
	public static boolean[] unpackBooleanArray(byte[] ba)
	{
		return PackBooleanArray.unpackBooleanArray(ba);
	}
	
	public static boolean[] unpackBooleanArray(byte[] ba, int idx)
	{
		return PackBooleanArray.unpackBooleanArray(ba, idx);
	}
	
	public static boolean[] readBooleanArray(InputStream is) throws IOException
	{
		return PackBooleanArray.readBooleanArray(is);
	}

	public static boolean[] readBooleanArray(RandomAccessFile raf) throws IOException
	{
		return PackBooleanArray.readBooleanArray(raf);
	}

	public static int write(OutputStream os, boolean[] value) throws IOException
	{
		return PackBooleanArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, boolean[] value) throws IOException
	{
		return PackBooleanArray.write(raf, value);
	}
	
	
	
	public static byte data_type(byte[] m)
	{
		return PackByteArray.data_type();
	}

	public static int data_size(byte[] m)
	{
		return PackByteArray.data_size(m);
	}
	
	public static byte[] pack(byte[] m)
	{
		return PackByteArray.pack(m);
	}
	
	public static int pack(byte[] m, byte[] ba, int idx)
	{
		return PackByteArray.pack(m, ba, idx);
	}
	
	public static byte[] unpackByteArray(byte[] ba)
	{
		return PackByteArray.unpackByteArray(ba);
	}
	
	public static byte[] unpackByteArray(byte[] ba, int idx)
	{
		return PackByteArray.unpackByteArray(ba, idx);
	}
	
	public static byte[] readByteArray(InputStream is) throws IOException
	{
		return PackByteArray.readByteArray(is);
	}
	
	public static byte[] readByteArray(InputStream is, int len) throws IOException
	{
		return PackByteArray.readByteArray(is, len);
	}

	public static byte[] readByteArray(RandomAccessFile raf) throws IOException
	{
		return PackByteArray.readByteArray(raf);
	}

	public static int write(OutputStream os, byte[] value) throws IOException
	{
		return PackByteArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, byte[] value) throws IOException
	{
		return PackByteArray.write(raf, value);
	}


	
	public static byte data_type(char[] m)
	{
		return PackCharArray.data_type();
	}
	
	public static int data_size(char[] m)
	{
		return PackCharArray.data_size(m);
	}
	
	public static byte[] pack(char[] m)
	{
		return PackCharArray.pack(m);
	}
	
	public static int pack(char[] m, byte[] ba, int idx)
	{
		return PackCharArray.pack(m, ba, idx);
	}
	
	public static char[] unpackCharArray(byte[] ba)
	{
		return PackCharArray.unpackCharArray(ba);
	}
	
	public static char[] unpackCharArray(byte[] ba, int idx)
	{
		return PackCharArray.unpackCharArray(ba, idx);
	}
	
	public static char[] readCharArray(InputStream is) throws IOException
	{
		return PackCharArray.readCharArray(is);
	}

	public static char[] readCharArray(RandomAccessFile raf) throws IOException
	{
		return PackCharArray.readCharArray(raf);
	}

	public static int write(OutputStream os, char[] value) throws IOException
	{
		return PackCharArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, char[] value) throws IOException
	{
		return PackCharArray.write(raf, value);
	}


	
	public static byte data_type(short[] m)
	{
		return PackShortArray.data_type();
	}

	public static int data_size(short[] m)
	{
		return PackShortArray.data_size(m);
	}
	
	public static byte[] pack(short[] m)
	{
		return PackShortArray.pack(m);
	}
	
	public static int pack(short[] m, byte[] ba, int idx)
	{
		return PackShortArray.pack(m, ba, idx);
	}
	
	public static short[] unpackShortArray(byte[] ba)
	{
		return PackShortArray.unpackShortArray(ba);
	}
	
	public static short[] unpackShortArray(byte[] ba, int idx)
	{
		return PackShortArray.unpackShortArray(ba, idx);
	}
	
	public static short[] readShortArray(InputStream is) throws IOException
	{
		return PackShortArray.readShortArray(is);
	}

	public static short[] readShortArray(RandomAccessFile raf) throws IOException
	{
		return PackShortArray.readShortArray(raf);
	}

	public static int write(OutputStream os, short[] value) throws IOException
	{
		return PackShortArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, short[] value) throws IOException
	{
		return PackShortArray.write(raf, value);
	}


	
	public static byte data_type(int[] m)
	{
		return PackIntArray.data_type();
	}

	public static int data_size(int[] m)
	{
		return PackIntArray.data_size(m);
	}
	
	public static byte[] pack(int[] m)
	{
		return PackIntArray.pack(m);
	}
	
	public static int pack(int[] m, byte[] ba, int idx)
	{
		return PackIntArray.pack(m, ba, idx);
	}
	
	public static int[] unpackIntArray(byte[] ba)
	{
		return PackIntArray.unpackIntArray(ba);
	}
	
	public static int[] unpackIntArray(byte[] ba, int idx)
	{
		return PackIntArray.unpackIntArray(ba, idx);
	}
	
	public static int[] readIntArray(InputStream is) throws IOException
	{
		return PackIntArray.readIntArray(is);
	}

	public static int[] readIntArray(RandomAccessFile raf) throws IOException
	{
		return PackIntArray.readIntArray(raf);
	}

	public static int write(OutputStream os, int[] value) throws IOException
	{
		return PackIntArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, int[] value) throws IOException
	{
		return PackIntArray.write(raf, value);
	}


	
	public static byte data_type(float[] m)
	{
		return PackFloatArray.data_type();
	}

	public static int data_size(float[] m)
	{
		return PackFloatArray.data_size(m);
	}
	
	public static byte[] pack(float[] m)
	{
		return PackFloatArray.pack(m);
	}
	
	public static int pack(float[] m, byte[] ba, int idx)
	{
		return PackFloatArray.pack(m, ba, idx);
	}
	
	public static float[] unpackFloatArray(byte[] ba)
	{
		return PackFloatArray.unpackFloatArray(ba);
	}
	
	public static float[] unpackFloatArray(byte[] ba, int idx)
	{
		return PackFloatArray.unpackFloatArray(ba, idx);
	}
	
	public static float[] readFloatArray(InputStream is) throws IOException
	{
		return PackFloatArray.readFloatArray(is);
	}

	public static float[] readFloatArray(RandomAccessFile raf) throws IOException
	{
		return PackFloatArray.readFloatArray(raf);
	}

	public static int write(OutputStream os, float[] value) throws IOException
	{
		return PackFloatArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, float[] value) throws IOException
	{
		return PackFloatArray.write(raf, value);
	}


	
	public static byte data_type(long[] m)
	{
		return PackLongArray.data_type();
	}

	public static int data_size(long[] m)
	{
		return PackLongArray.data_size(m);
	}
	
	public static byte[] pack(long[] m)
	{
		return PackLongArray.pack(m);
	}
	
	public static int pack(long[] m, byte[] ba, int idx)
	{
		return PackLongArray.pack(m, ba, idx);
	}
	
	public static long[] unpackLongArray(byte[] ba)
	{
		return PackLongArray.unpackLongArray(ba);
	}
	
	public static long[] unpackLongArray(byte[] ba, int idx)
	{
		return PackLongArray.unpackLongArray(ba, idx);
	}
	
	public static long[] readLongArray(InputStream is) throws IOException
	{
		return PackLongArray.readLongArray(is);
	}

	public static long[] readLongArray(RandomAccessFile raf) throws IOException
	{
		return PackLongArray.readLongArray(raf);
	}

	public static int write(OutputStream os, long[] value) throws IOException
	{
		return PackLongArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, long[] value) throws IOException
	{
		return PackLongArray.write(raf, value);
	}


	
	public static byte data_type(double[] m)
	{
		return PackDoubleArray.data_type();
	}

	public static int data_size(double[] m)
	{
		return PackDoubleArray.data_size(m);
	}
	
	public static byte[] pack(double[] m)
	{
		return PackDoubleArray.pack(m);
	}
	
	public static int pack(double[] m, byte[] ba, int idx)
	{
		return PackDoubleArray.pack(m, ba, idx);
	}
	
	public static double[] unpackDoubleArray(byte[] ba)
	{
		return PackDoubleArray.unpackDoubleArray(ba);
	}
	
	public static double[] unpackDoubleArray(byte[] ba, int idx)
	{
		return PackDoubleArray.unpackDoubleArray(ba, idx);
	}
	
	public static double[] readDoubleArray(InputStream is) throws IOException
	{
		return PackDoubleArray.readDoubleArray(is);
	}

	public static double[] readDoubleArray(RandomAccessFile raf) throws IOException
	{
		return PackDoubleArray.readDoubleArray(raf);
	}

	public static int write(OutputStream os, double[] value) throws IOException
	{
		return PackDoubleArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, double[] value) throws IOException
	{
		return PackDoubleArray.write(raf, value);
	}

	
	
	public static byte data_type(String[] m)
	{
		return PackStringArray.data_type();
	}

	public static int data_size(String[] m)
	{
		return PackStringArray.data_size(m);
	}
	
	public static byte[] pack(String[] m)
	{
		return PackStringArray.pack(m);
	}
	
	public static byte[] pack(String[] m, byte[] b)
	{
		return PackStringArray.pack(m, b);
	}
	
	public static int pack(String[] m, byte[] ba, int idx)
	{
		return PackStringArray.pack(m, ba, idx);
	}
	
	public static String[] unpackStringArray(byte[] ba)
	{
		return PackStringArray.unpackStringArray(ba);
	}
	
	public static String[] unpackStringArray(byte[] ba, int idx)
	{
		return PackStringArray.unpackStringArray(ba, idx);
	}
	
	public static String[] readStringArray(InputStream is) throws IOException
	{
		return PackStringArray.readStringArray(is);
	}

	public static String[] readStringArray(RandomAccessFile raf) throws IOException
	{
		return PackStringArray.readStringArray(raf);
	}

	public static int write(OutputStream os, String[] value) throws IOException
	{
		return PackStringArray.write(os, value);
	}

	public static int write(RandomAccessFile raf, String[] value) throws IOException
	{
		return PackStringArray.write(raf, value);
	}

	
	

	
	
	
	
	
	
	
	
	
	
	public static int next(byte[] ba, int idx)
	{
		assert ba != null;
		assert 0 <= idx && idx <= ba.length;
		
		if (idx == ba.length) {
			return ba.length; 
		}

		switch (ba[idx]) {
		case BOOLEAN:
			idx += SIZE_BOOLEAN;
			break;
		case BYTE:
			idx += SIZE_BYTE;
			break;
		case CHAR:
			idx += SIZE_CHAR;
			break;
		case SHORT:
			idx += SIZE_SHORT;
			break;
		case INT:
			idx += SIZE_INT;
			break;
		case FLOAT:
			idx += SIZE_FLOAT;
			break;
		case LONG:
			idx += SIZE_LONG;
			break;
		case DOUBLE:
			idx += SIZE_DOUBLE;
			break;
		case STRING:
			assert idx+SIZE_STRING <= ba.length;
			int len = PackString.unpackStringSize(ba, idx);
			idx += SIZE_STRING+len;
			break;
		case ARRAY:
			int elts = PackArray.unpackArraySize(ba, idx);
			idx += SIZE_ARRAY;
			for (int j=0; j < elts; j++) {
				idx = next(ba, idx);
			}
			break;
		}

		assert idx <= ba.length;
		
		return idx;
	}
	
	public static int length(byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx < ba.length;
		
		int len = 0;

		switch (ba[idx]) {
		case BOOLEAN:
			len = SIZE_BOOLEAN;
			break;
		case BYTE:
			len = SIZE_BYTE;
			break;
		case CHAR:
			len = SIZE_CHAR;
			break;
		case SHORT:
			len = SIZE_SHORT;
			break;
		case INT:
			len = SIZE_INT;
			break;
		case FLOAT:
			len = SIZE_FLOAT;
			break;
		case LONG:
			len = SIZE_LONG;
			break;
		case DOUBLE:
			len = SIZE_DOUBLE;
			break;
		case STRING:
			assert idx+SIZE_STRING <= ba.length;
			len = SIZE_STRING + PackString.unpackStringSize(ba, idx);
			break;
		case ARRAY:
			int end = next(ba, idx);
			len = end - idx;
			break;
		}

		assert idx + len <= ba.length;
		
		return len;
	}
	
	// join (merge) a and b into a new buffer, and return the result
	public static byte[] join(byte[] a, byte[] b)
	{
		int len = ((a != null)?a.length:0) + ((b != null)?b.length:0);
		if (len <= 0) return null;
		
		byte[] result = new byte[len];
		
		int k = 0;
		if (a != null) {
			for (int i=0; i < a.length; i++,k++) {
				result[k] = a[i];
			}
		}
		if (b != null) {
			for (int i=0; i < b.length; i++,k++) {
				result[k] = b[i];
			}
		}
		
		return result;
	}
	
	// join (merge) a, b and c into a new buffer, and return the result
	public static byte[] join(byte[] a, byte[] b, byte[] c)
	{
		int len = ((a != null)?a.length:0) + ((b != null)?b.length:0) + ((c != null)?c.length:0);
		if (len <= 0) return null;
		
		byte[] result = new byte[len];
		
		int k = 0;
		if (a != null) {
			for (int i=0; i < a.length; i++,k++) {
				result[k] = a[i];
			}
		}
		if (b != null) {
			for (int i=0; i < b.length; i++,k++) {
				result[k] = b[i];
			}
		}
		if (c != null) {
			for (int i=0; i < c.length; i++,k++) {
				result[k] = c[i];
			}
		}
		
		return result;
	}

	// copy b into a
	public static void copy(byte[] a, byte[] b)
	{
		assert a != null && b != null && b.length <= a.length;
		
		for (int i=0; i < b.length; i++) {
			a[i] = b[i];
		}
	}
	
	// copy buf2 into buf1
	public static void copy(byte[] buf1, int off1, byte[] buf2, int off2, int len)
	{
		assert buf1 != null && (off1+len) <= buf1.length;
		assert (buf2 != null) ? (off2+len) <= buf2.length : len == 0;
		
		for (int i=0; i < len; i++) {
			buf1[off1+i] = buf2[off2+i];
		}
	}

	
	

	
	

	
	public static String toString(byte[] ba)
	{
		String result = "ba=null";
		
		if (ba != null) {
			result = ""+ba+" ba.length="+ba.length+" ";
			for (int i=0; i < ba.length; i=next(ba,i)) {
				result += toString(ba, i) + " ";
			}
		}
		
		return result;
	}
	
	public static String toString(byte[] ba, int idx)
	{
		assert 0 <= idx && idx < ba.length;
		
		String result = "";
		
		switch (get_data_type(ba, idx)) {
		case NULL:
			result += "NULL";
			break;
		case BOOLEAN:
			result += "b="+unpackBoolean(ba, idx);
			break;
		case BYTE:
			result += "B="+unpackByte(ba, idx);
			break;
		case CHAR:
			result += "C="+unpackChar(ba, idx);
			break;
		case SHORT:
			result += "S="+unpackShort(ba, idx);
			break;
		case INT:
			result += "I="+unpackInt(ba, idx);
			break;
		case LONG:
			result += "L="+unpackLong(ba, idx);
			break;
		case FLOAT:
			result += "F="+unpackFloat(ba, idx);
			break;
		case DOUBLE:
			result += "D="+unpackDouble(ba, idx);
			break;
		case STRING:
			result += "\""+unpackString(ba, idx)+"\"";
			break;
		case ARRAY:
			int elts = PackArray.unpackArraySize(ba, idx);
			idx += SIZE_ARRAY;
			result += "[";
			for (int i=0; i < elts; i++) {
				result += toString(ba, idx) + ",";
				idx = next(ba, idx);
			}
			result += "]";
			break;
		}
		
		return result;
	}
	
	public static String toString(byte type)
	{
		String result = "";
		
		switch (type) {
		case NULL:
			result += "NULL";
			break;
		case BOOLEAN:
			result += "BOOLEAN";
			break;
		case BYTE:
			result += "BYTE";
			break;
		case CHAR:
			result += "CHAR";
			break;
		case SHORT:
			result += "SHORT";
			break;
		case INT:
			result += "INT";
			break;
		case LONG:
			result += "LONG";
			break;
		case FLOAT:
			result += "FLOAT";
			break;
		case DOUBLE:
			result += "DOUBLE";
			break;
		case STRING:
			result += "STRING";
			break;
		case ARRAY:
			result = "[]";
			break;
		}
		
		return result;
	}
}
