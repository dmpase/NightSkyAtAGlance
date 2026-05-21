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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;


public class PackShortArray extends PackArray {
	
	public static byte data_type()
	{
		return ARRAY;
	}
	
	public static byte data_type(short[] m)
	{
		return ARRAY;
	}

	public static int data_size(short[] m)
	{
		int result = SIZE_ARRAY;
		
		if (m != null) {
			result += SIZE_SHORT * m.length;
		}
		
		return result;
	}
	
	public static byte[] pack(short[] m)
	{
		byte[] ba = new byte[data_size(m)];
		int idx = 0;
		
		pack(m, ba, idx);

		return ba;
	}
	
	public static int pack(short[] m, byte[] ba, int idx)
	{
		int len = data_size(m);
		
		assert ba != null && 0 <= idx && idx+len <= ba.length;

		packArrayTypeSize(ba, idx, SHORT, m.length);
		if (m != null) {
			int j = idx+SIZE_ARRAY;
			for (int i=0; i < m.length; i++) {
				j = pack(m[i], ba, j);
			}
		}
		
		return idx + len;
	}
	
	public static short[] unpackShortArray(byte[] ba)
	{
		assert ba != null && SIZE_ARRAY <= ba.length && get_data_type(ba) == ARRAY;

		return unpackShortArray(ba, 0);
	}
	
	public static short[] unpackShortArray(byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx+SIZE_ARRAY <= ba.length && get_data_type(ba,idx) == ARRAY;
		assert unpackArrayType(ba, idx) == SHORT;

		int elts = unpackArraySize(ba, idx);
		short[] m = null;
		
		if (0 < elts) {
			m = new short[elts];

			int j = idx+SIZE_ARRAY;
			for (int i=0; i < m.length; i++) {
				m[i] = unpackShort(ba, j);
				j = next(ba, j);
			}
		}
		
		return m;
	}
	
	public static short[] readShortArray(InputStream is) throws IOException
	{
		short[] result = null;

		// read the array header
		byte[] buf = new byte[SIZE_ARRAY];
    	is.read(buf);
    	byte type = unpackArrayType(buf, 0);
    	assert type == SHORT;
    	int  size = unpackArraySize(buf, 0);

    	if (0 < size) {
    		result = new short[size];
    		for (int i=0; i < size; i++) {
    			result[i] = readShort(is);
    		}
    	}

		return result;
	}

	public static short[] readShortArray(RandomAccessFile raf) throws IOException
	{
		short[] result = null;

		// read the array header
		byte[] buf = new byte[Pack.SIZE_ARRAY];
    	raf.read(buf);
    	byte type = unpackArrayType(buf, 0);
    	assert type == Pack.SHORT;
    	int  size = unpackArraySize(buf, 0);

    	if (0 < size) {
    		result = new short[size];
    		for (int i=0; i < size; i++) {
    			result[i] = readShort(raf);
    		}
    	}

		return result;
	}

	public static int write(OutputStream os, short[] value) throws IOException
	{
		byte[] buf = pack(value);
    	os.write(buf);

		return buf.length;
	}

	public static int write(RandomAccessFile raf, short[] value) throws IOException
	{
		byte[] buf = pack(value);
    	raf.write(buf);

		return buf.length;
	}
}
