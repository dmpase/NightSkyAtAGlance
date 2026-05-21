package lib.pack;

/*******************************************************************************
 * Copyright (c) 2025-2926 Douglas M. Pase                                     *
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


import lib.util.Compress;

public class PackRaw {
	public static final int sizeof_byte   = Byte.BYTES;
	public static final int sizeof_short  = Short.BYTES;
	public static final int sizeof_int    = Integer.BYTES;
	public static final int sizeof_long   = Long.BYTES;
	public static final int sizeof_float  = Float.BYTES;
	public static final int sizeof_double = Double.BYTES;

	public static int sizeof(byte   data) { return Byte.BYTES;    }
	public static int sizeof(short  data) { return Short.BYTES;   }
	public static int sizeof(int    data) { return Integer.BYTES; }
	public static int sizeof(long   data) { return Long.BYTES;    }
	public static int sizeof(float  data) { return Float.BYTES;   }
	public static int sizeof(double data) { return Double.BYTES;  }

	public static int sizeof(byte  [] data) { return data.length * Byte.BYTES;    }
	public static int sizeof(short [] data) { return data.length * Short.BYTES;   }
	public static int sizeof(int   [] data) { return data.length * Integer.BYTES; }
	public static int sizeof(long  [] data) { return data.length * Long.BYTES;    }
	public static int sizeof(float [] data) { return data.length * Float.BYTES;   }
	public static int sizeof(double[] data) { return data.length * Double.BYTES;  }

	public static int sizeof(byte  [][] data) { return data.length * data[0].length * Byte.BYTES;    }
	public static int sizeof(short [][] data) { return data.length * data[0].length * Short.BYTES;   }
	public static int sizeof(int   [][] data) { return data.length * data[0].length * Integer.BYTES; }
	public static int sizeof(long  [][] data) { return data.length * data[0].length * Long.BYTES;    }
	public static int sizeof(float [][] data) { return data.length * data[0].length * Float.BYTES;   }
	public static int sizeof(double[][] data) { return data.length * data[0].length * Double.BYTES;  }

	public static byte[] copy(byte[] tgt, int offset, byte[] src)
	{
		for (int i=0; i < src.length && (i+offset) < tgt.length; i++) {
			tgt[i+offset] = src[i];
		}
		
		return tgt;
	}

	public static byte[] dup(byte[] src)
	{
		byte[] tgt = new byte[src.length];

		for (int i=0; i < src.length; i++) {
			tgt[i] = src[i];
		}
		
		return tgt;
	}

	public static byte[] to_bytes(byte  data) 
	{ 
		return new byte[] {(byte) ((data >>  0) & 0xFF)}; 
	}

	public static byte[] to_bytes(short data) 
	{ 
		return new byte[] {
				(byte) ((data >>  8) & 0xFF), 
				(byte) ((data >> 0) & 0xFF)};
	}

	public static byte[] to_bytes(int   data) 
	{ 
		return new byte[] {
				(byte) ((data >> 24) & 0xFF), 
				(byte) ((data >> 16) & 0xFF), 
				(byte) ((data >> 8) & 0xFF), 
				(byte) ((data >> 0) & 0xFF)};
	}

	public static byte[] to_bytes(long  data) 
	{ 
		return new byte[] {
				(byte) ((data >> 56) & 0xFF), 
				(byte) ((data >> 48) & 0xFF), 
				(byte) ((data >> 40) & 0xFF), 
				(byte) ((data >> 32) & 0xFF), 
				(byte) ((data >> 24) & 0xFF), 
				(byte) ((data >> 16) & 0xFF), 
				(byte) ((data >>  8) & 0xFF), 
				(byte) ((data >>  0) & 0xFF)};
	}

	public static byte[] to_bytes(float data)
	{
		int bits = Float.floatToRawIntBits(data);
		return to_bytes(bits);
	}

	public static byte[] to_bytes(double data)
	{
		long bits = Double.doubleToRawLongBits(data);
		return to_bytes(bits);
	}

	public static byte[] to_bytes(byte[] data) 
	{ 
		return dup(data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, byte[] data) 
	{
		for (int i=0; i < data.length; i++) {
			tgt[i+offset] = data[i];
		}

		return tgt;
	}

	public static byte[] to_bytes(short[] data) 
	{
		byte[] tgt = new byte[sizeof(data)];

		int offset = 0;
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[] tgt, int offset, short[] data) 
	{
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(int[] data) 
	{
		byte[] tgt = new byte[sizeof(data)];

		int offset = 0;
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[] tgt, int offset, int[] data) 
	{
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(long[] data) 
	{
		byte[] tgt = new byte[sizeof(data)];

		int offset = 0;
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[] tgt, int offset, long[] data) 
	{
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(float[] data) 
	{
		byte[] tgt = new byte[sizeof(data)];

		int offset = 0;
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[] tgt, int offset, float[] data) 
	{
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(double[] data) 
	{
		byte[] tgt = new byte[sizeof(data)];

		int offset = 0;
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[] tgt, int offset, double[] data) 
	{
		int size = sizeof(data[0]);
		for (int i=0; i < data.length; i++) {
			copy(tgt, offset, to_bytes(data[i]));
			offset += size;
		}

		return tgt;
	}

	public static byte[] to_bytes(byte[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, byte[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}

	public static byte[] to_bytes(short[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, short[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}

	public static byte[] to_bytes(int[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, int[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}

	public static byte[] to_bytes(long[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, long[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}

	public static byte[] to_bytes(float[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, float[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}

	public static byte[] to_bytes(double[][] data) 
	{
		byte[] tgt = new byte[sizeof(data)];
		int offset = 0;

		return to_bytes(tgt, offset, data);
	}

	public static byte[] to_bytes(byte[] tgt, int offset, double[][] data) 
	{
		int size = sizeof(data[0][0]);
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(tgt, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		return tgt;
	}


	public static byte to_byte(byte[] data)
	{
		int offset = 0;
		return to_byte(data, offset);
	}

	public static byte to_byte(byte[] data, int offset)
	{
		return data[offset];
	}

	public static short to_short(byte[] data)
	{
		int offset = 0;
		return to_short(data, offset);
	}

	public static short to_short(byte[] data, int offset)
	{
		long value = (((long) data[offset+0] & 0xFFL) << 8L) | 
					 (((long) data[offset+1] & 0xFFL) << 0L);

		return (short) value;
	}

	public static int to_int(byte[] data)
	{
		int offset = 0;
		return to_int(data, offset);
	}

	public static int to_int(byte[] data, int offset)
	{
		long value = (((long) data[offset+0] & 0xFFL) << 24L) | 
					 (((long) data[offset+1] & 0xFFL) << 16L) | 
					 (((long) data[offset+2] & 0xFFL) <<  8L) | 
					 (((long) data[offset+3] & 0xFFL) <<  0L);

		return (int) value;
	}

	public static long to_long(byte[] data)
	{
		int offset = 0;
		return to_long(data, offset);
	}

	public static long to_long(byte[] data, int offset)
	{
		long value = (((long) data[offset+0] & 0xFFL) << 56L) | 
					 (((long) data[offset+1] & 0xFFL) << 48L) | 
					 (((long) data[offset+2] & 0xFFL) << 40L) | 
					 (((long) data[offset+3] & 0xFFL) << 32L) |
					 (((long) data[offset+4] & 0xFFL) << 24L) | 
					 (((long) data[offset+5] & 0xFFL) << 16L) | 
					 (((long) data[offset+6] & 0xFFL) <<  8L) | 
					 (((long) data[offset+7] & 0xFFL) <<  0L);

		return (long) value;
	}

	public static float to_float(byte[] data)
	{
		int offset = 0;
		return to_float(data, offset);
	}

	public static float to_float(byte[] data, int offset)
	{
		int value = to_int(data, offset);

		return Float.intBitsToFloat(value);
	}

	public static double to_double(byte[] data)
	{
		int offset = 0;
		return to_double(data, offset);
	}

	public static double to_double(byte[] data, int offset)
	{
		long value = to_long(data, offset);

		return Double.longBitsToDouble(value);
	}


	public static byte[] to_byte_array(int dim, byte[] data)
	{
		byte[] tgt = new byte[dim];
		int offset = 0;

		return to_byte_array(tgt, data, offset);
	}

	public static byte[] to_byte_array(int dim, byte[] data, int offset)
	{
		byte[] tgt = new byte[dim];

		return to_byte_array(tgt, data, offset);
	}

	public static byte[] to_byte_array(byte[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_byte(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static short[] to_short_array(int dim, byte[] data)
	{
		short[] tgt = new short[dim];
		int offset = 0;

		return to_short_array(tgt, data, offset);
	}

	public static short[] to_short_array(int dim, byte[] data, int offset)
	{
		short[] tgt = new short[dim];

		return to_short_array(tgt, data, offset);
	}

	public static short[] to_short_array(short[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_short(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static int[] to_int_array(int dim, byte[] data)
	{
		int[] tgt = new int[dim];
		int offset = 0;

		return to_int_array(tgt, data, offset);
	}

	public static int[] to_int_array(int dim, byte[] data, int offset)
	{
		int[] tgt = new int[dim];

		return to_int_array(tgt, data, offset);
	}

	public static int[] to_int_array(int[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_int(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static long[] to_long_array(int dim, byte[] data)
	{
		long[] tgt = new long[dim];
		int offset = 0;

		return to_long_array(tgt, data, offset);
	}

	public static long[] to_long_array(int dim, byte[] data, int offset)
	{
		long[] tgt = new long[dim];

		return to_long_array(tgt, data, offset);
	}

	public static long[] to_long_array(long[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_long(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static float[] to_float_array(int dim, byte[] data)
	{
		float[] tgt = new float[dim];
		int offset = 0;

		return to_float_array(tgt, data, offset);
	}

	public static float[] to_float_array(int dim, byte[] data, int offset)
	{
		float[] tgt = new float[dim];

		return to_float_array(tgt, data, offset);
	}

	public static float[] to_float_array(float[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_float(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static double[] to_double_array(int dim, byte[] data)
	{
		double[] tgt = new double[dim];
		int offset = 0;

		return to_double_array(tgt, data, offset);
	}

	public static double[] to_double_array(int dim, byte[] data, int offset)
	{
		double[] tgt = new double[dim];

		return to_double_array(tgt, data, offset);
	}

	public static double[] to_double_array(double[] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = to_double(data, offset);
			offset += sizeof(tgt[i]);
		}

		return tgt;
	}

	public static byte[][] to_byte_array(int dim1, int dim2, byte[] data)
	{
		byte[][] tgt = new byte[dim1][dim2];
		int offset = 0;

		return to_byte_array(tgt, data, offset);
	}

	public static byte[][] to_byte_array(int dim1, int dim2, byte[] data, int offset)
	{
		byte[][] tgt = new byte[dim1][dim2];

		return to_byte_array(tgt, data, offset);
	}

	public static byte[][] to_byte_array(byte[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_byte(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(int dim1, int dim2, byte[] data)
	{
		short[][] tgt = new short[dim1][dim2];
		int offset = 0;

		return to_short_array(tgt, data, offset);
	}

	public static short[][] to_short_array(int dim1, int dim2, byte[] data, int offset)
	{
		short[][] tgt = new short[dim1][dim2];

		return to_short_array(tgt, data, offset);
	}

	public static short[][] to_short_array(short[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_short(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(int dim1, int dim2, byte[] data)
	{
		int[][] tgt = new int[dim1][dim2];
		int offset = 0;

		return to_int_array(tgt, data, offset);
	}

	public static int[][] to_int_array(int dim1, int dim2, byte[] data, int offset)
	{
		int[][] tgt = new int[dim1][dim2];

		return to_int_array(tgt, data, offset);
	}

	public static int[][] to_int_array(int[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_int(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(int dim1, int dim2, byte[] data)
	{
		long[][] tgt = new long[dim1][dim2];
		int offset = 0;

		return to_long_array(tgt, data, offset);
	}

	public static long[][] to_long_array(int dim1, int dim2, byte[] data, int offset)
	{
		long[][] tgt = new long[dim1][dim2];

		return to_long_array(tgt, data, offset);
	}

	public static long[][] to_long_array(long[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_long(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(int dim1, int dim2, byte[] data)
	{
		float[][] tgt = new float[dim1][dim2];
		int offset = 0;

		return to_float_array(tgt, data, offset);
	}

	public static float[][] to_float_array(int dim1, int dim2, byte[] data, int offset)
	{
		float[][] tgt = new float[dim1][dim2];

		return to_float_array(tgt, data, offset);
	}

	public static float[][] to_float_array(float[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_float(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(int dim1, int dim2, byte[] data)
	{
		double[][] tgt = new double[dim1][dim2];
		int offset = 0;

		return to_double_array(tgt, data, offset);
	}

	public static double[][] to_double_array(int dim1, int dim2, byte[] data, int offset)
	{
		double[][] tgt = new double[dim1][dim2];

		return to_double_array(tgt, data, offset);
	}

	public static double[][] to_double_array(double[][] tgt, byte[] data, int offset)
	{
		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_double(data, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}


	public static byte[] to_deflated_bytes(byte[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static byte[][] to_inflated_byte_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		byte[][] tgt = new byte[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_byte(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static byte[] to_deflated_bytes(short[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static short[][] to_inflated_short_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		short[][] tgt = new short[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_short(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static byte[] to_deflated_bytes(int[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static int[][] to_inflated_int_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		int[][] tgt = new int[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_int(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static byte[] to_deflated_bytes(long[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static long[][] to_inflated_long_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		long[][] tgt = new long[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_long(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static byte[] to_deflated_bytes(float[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static float[][] to_inflated_float_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		float[][] tgt = new float[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_float(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}

	public static byte[] to_deflated_bytes(double[][] data) 
	{
		int size = sizeof(data[0][0]);

		byte[] inflated = new byte[2 * sizeof_int + sizeof(data)];
		int offset = 0;

		copy(inflated, offset, to_bytes((int)data.length));
		offset += sizeof((int)data.length);

		copy(inflated, offset, to_bytes((int)data[0].length));
		offset += sizeof((int)data[0].length);

		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				copy(inflated, offset, to_bytes(data[i][j]));
				offset += size;
			}
		}

		byte[] deflated = Compress.deflate(inflated);

		return deflated;
	}

	public static double[][] to_inflated_double_2d_array(byte[] deflated)
	{
		byte[] inflated = Compress.inflate(deflated);

		int offset = 0;
		int dim1 = to_int(inflated, offset);
		offset += sizeof_int;
		int dim2 = to_int(inflated, offset);
		offset += sizeof_int;
		
		double[][] tgt = new double[dim1][dim2];

		for (int i=0; i < tgt.length; i++) {
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = to_double(inflated, offset);
				offset += sizeof(tgt[i][j]);
			}
		}

		return tgt;
	}


	public static byte[] to_byte_array(short[] data)
	{
		int dim1 = data.length;
		byte[] tgt = new byte[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (byte) data[i];
		}

		return tgt;
	}

	public static byte[] to_byte_array(int[] data)
	{
		int dim1 = data.length;
		byte[] tgt = new byte[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (byte) data[i];
		}

		return tgt;
	}

	public static byte[] to_byte_array(long[] data)
	{
		int dim1 = data.length;
		byte[] tgt = new byte[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (byte) data[i];
		}

		return tgt;
	}

	public static byte[] to_byte_array(float[] data)
	{
		int dim1 = data.length;
		byte[] tgt = new byte[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (byte) data[i];
		}

		return tgt;
	}

	public static byte[] to_byte_array(double[] data)
	{
		int dim1 = data.length;
		byte[] tgt = new byte[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (byte) data[i];
		}

		return tgt;
	}

	public static short[] to_short_array(byte[] data)
	{
		int dim1 = data.length;
		short[] tgt = new short[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (short) data[i];
		}

		return tgt;
	}

	public static short[] to_short_array(int[] data)
	{
		int dim1 = data.length;
		short[] tgt = new short[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (short) data[i];
		}

		return tgt;
	}

	public static short[] to_short_array(long[] data)
	{
		int dim1 = data.length;
		short[] tgt = new short[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (short) data[i];
		}

		return tgt;
	}

	public static short[] to_short_array(float[] data)
	{
		int dim1 = data.length;
		short[] tgt = new short[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (short) data[i];
		}

		return tgt;
	}

	public static short[] to_short_array(double[] data)
	{
		int dim1 = data.length;
		short[] tgt = new short[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (short) data[i];
		}

		return tgt;
	}

	public static int[] to_int_array(byte[] data)
	{
		int dim1 = data.length;
		int[] tgt = new int[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (int) data[i];
		}

		return tgt;
	}

	public static int[] to_int_array(short[] data)
	{
		int dim1 = data.length;
		int[] tgt = new int[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (int) data[i];
		}

		return tgt;
	}

	public static int[] to_int_array(long[] data)
	{
		int dim1 = data.length;
		int[] tgt = new int[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (int) data[i];
		}

		return tgt;
	}

	public static int[] to_int_array(float[] data)
	{
		int dim1 = data.length;
		int[] tgt = new int[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (int) data[i];
		}

		return tgt;
	}

	public static int[] to_int_array(double[] data)
	{
		int dim1 = data.length;
		int[] tgt = new int[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (int) data[i];
		}

		return tgt;
	}

	public static long[] to_long_array(byte[] data)
	{
		int dim1 = data.length;
		long[] tgt = new long[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (long) data[i];
		}

		return tgt;
	}

	public static long[] to_long_array(short[] data)
	{
		int dim1 = data.length;
		long[] tgt = new long[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (long) data[i];
		}

		return tgt;
	}

	public static long[] to_long_array(int[] data)
	{
		int dim1 = data.length;
		long[] tgt = new long[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (long) data[i];
		}

		return tgt;
	}

	public static long[] to_long_array(float[] data)
	{
		int dim1 = data.length;
		long[] tgt = new long[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (long) data[i];
		}

		return tgt;
	}

	public static long[] to_long_array(double[] data)
	{
		int dim1 = data.length;
		long[] tgt = new long[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (long) data[i];
		}

		return tgt;
	}

	public static float[] to_float_array(byte[] data)
	{
		int dim1 = data.length;
		float[] tgt = new float[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (float) data[i];
		}

		return tgt;
	}

	public static float[] to_float_array(short[] data)
	{
		int dim1 = data.length;
		float[] tgt = new float[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (float) data[i];
		}

		return tgt;
	}

	public static float[] to_float_array(int[] data)
	{
		int dim1 = data.length;
		float[] tgt = new float[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (float) data[i];
		}

		return tgt;
	}

	public static float[] to_float_array(long[] data)
	{
		int dim1 = data.length;
		float[] tgt = new float[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (float) data[i];
		}

		return tgt;
	}

	public static float[] to_float_array(double[] data)
	{
		int dim1 = data.length;
		float[] tgt = new float[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (float) data[i];
		}

		return tgt;
	}

	public static double[] to_double_array(byte[] data)
	{
		int dim1 = data.length;
		double[] tgt = new double[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (double) data[i];
		}

		return tgt;
	}

	public static double[] to_double_array(short[] data)
	{
		int dim1 = data.length;
		double[] tgt = new double[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (double) data[i];
		}

		return tgt;
	}

	public static double[] to_double_array(int[] data)
	{
		int dim1 = data.length;
		double[] tgt = new double[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (double) data[i];
		}

		return tgt;
	}

	public static double[] to_double_array(long[] data)
	{
		int dim1 = data.length;
		double[] tgt = new double[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (double) data[i];
		}

		return tgt;
	}

	public static double[] to_double_array(float[] data)
	{
		int dim1 = data.length;
		double[] tgt = new double[dim1];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = (double) data[i];
		}

		return tgt;
	}

	public static byte[][] to_byte_array(short[][] data)
	{
		int dim1 = data.length;
		byte[][] tgt = new byte[dim1][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new byte[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (byte) data[i][j];
			}
		}

		return tgt;
	}

	public static byte[][] to_byte_array(int[][] data)
	{
		byte[][] tgt = new byte[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new byte[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (byte) data[i][j];
			}
		}

		return tgt;
	}

	public static byte[][] to_byte_array(long[][] data)
	{
		byte[][] tgt = new byte[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new byte[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (byte) data[i][j];
			}
		}

		return tgt;
	}

	public static byte[][] to_byte_array(float[][] data)
	{
		byte[][] tgt = new byte[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new byte[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (byte) data[i][j];
			}
		}

		return tgt;
	}

	public static byte[][] to_byte_array(double[][] data)
	{
		byte[][] tgt = new byte[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new byte[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (byte) data[i][j];
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(byte[][] data)
	{
		short[][] tgt = new short[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new short[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (short) data[i][j];
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(int[][] data)
	{
		short[][] tgt = new short[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new short[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (short) data[i][j];
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(long[][] data)
	{
		short[][] tgt = new short[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new short[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (short) data[i][j];
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(float[][] data)
	{
		short[][] tgt = new short[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new short[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (short) data[i][j];
			}
		}

		return tgt;
	}

	public static short[][] to_short_array(double[][] data)
	{
		short[][] tgt = new short[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new short[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (short) data[i][j];
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(byte[][] data)
	{
		int[][] tgt = new int[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new int[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (int) data[i][j];
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(short[][] data)
	{
		int[][] tgt = new int[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new int[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (int) data[i][j];
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(long[][] data)
	{
		int[][] tgt = new int[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new int[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (int) data[i][j];
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(float[][] data)
	{
		int[][] tgt = new int[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new int[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (int) data[i][j];
			}
		}

		return tgt;
	}

	public static int[][] to_int_array(double[][] data)
	{
		int[][] tgt = new int[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new int[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (int) data[i][j];
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(byte[][] data)
	{
		long[][] tgt = new long[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new long[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (long) data[i][j];
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(short[][] data)
	{
		long[][] tgt = new long[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new long[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (long) data[i][j];
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(int[][] data)
	{
		long[][] tgt = new long[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new long[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (long) data[i][j];
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(float[][] data)
	{
		long[][] tgt = new long[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new long[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (long) data[i][j];
			}
		}

		return tgt;
	}

	public static long[][] to_long_array(double[][] data)
	{
		long[][] tgt = new long[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new long[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (long) data[i][j];
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(byte[][] data)
	{
		float[][] tgt = new float[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new float[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (float) data[i][j];
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(short[][] data)
	{
		float[][] tgt = new float[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new float[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (float) data[i][j];
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(int[][] data)
	{
		float[][] tgt = new float[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new float[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (float) data[i][j];
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(long[][] data)
	{
		float[][] tgt = new float[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new float[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (float) data[i][j];
			}
		}

		return tgt;
	}

	public static float[][] to_float_array(double[][] data)
	{
		float[][] tgt = new float[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new float[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (float) data[i][j];
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(byte[][] data)
	{
		double[][] tgt = new double[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new double[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (double) data[i][j];
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(short[][] data)
	{
		double[][] tgt = new double[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new double[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (double) data[i][j];
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(int[][] data)
	{
		double[][] tgt = new double[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new double[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (double) data[i][j];
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(long[][] data)
	{
		double[][] tgt = new double[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new double[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (double) data[i][j];
			}
		}

		return tgt;
	}

	public static double[][] to_double_array(float[][] data)
	{
		double[][] tgt = new double[data.length][];
		for (int i=0; i < tgt.length; i++) {
			tgt[i] = new double[data[i].length];
			for (int j=0; j < tgt[i].length; j++) {
				tgt[i][j] = (double) data[i][j];
			}
		}

		return tgt;
	}
}
