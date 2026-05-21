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


public class PackInt extends Pack {
	
	public static byte data_type()
	{
		return INT;
	}
	
	public static int data_size()
	{
		return SIZE_INT;
	}
	
	public static byte data_type(int m)
	{
		return INT;
	}
	
	public static int data_size(int m)
	{
		return SIZE_INT;
	}
	
	public static byte[] pack(int m)
	{
		byte[] ba = new byte[data_size(m)];
		int idx = 0;
		
		pack(m, ba, idx);
		
		return ba;
	}
	
	public static byte[] pack(int m, byte[] b)
	{
		byte[] ba = new byte[b.length+data_size(m)];
		copy(ba, b);
		
		pack(m, ba, b.length);
		
		return ba;
	}
	
	public static int pack(int m, byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx+data_size(m) <= ba.length;

		set_data_type(ba, idx, INT);
		
		for (int i=idx+SIZE_TYPE; i < idx+data_size(m); i++) {
			ba[i] = (byte) (m & 0xff);
			m = m >> 8;
		}
		
		return idx+data_size(m);
	}
	
	public static int unpackInt(byte[] ba)
	{
		assert ba != null && SIZE_INT <= ba.length && get_data_type(ba) == INT;
		
		return unpackInt(ba, 0);
	}
	
	public static int unpackInt(byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx+SIZE_INT <= ba.length && get_data_type(ba,idx) == INT;

		int m = 0;
		
		for (int i=idx+SIZE_INT-1; idx+SIZE_TYPE <= i; i--) {
			m = (m << 8) | (ba[i] & 0xff);
		}
		
		return m;
	}
	
	public static int readInt(InputStream is) throws IOException
	{
		int result = 0;

		byte[] buf = new byte[Pack.sizeof(Pack.INT)];
    	is.read(buf, 0, Pack.sizeof(Pack.INT));
    	byte type = Pack.get_data_type(buf);
    	assert type == Pack.INT;
    	result = Pack.unpackInt(buf);

		return result;
	}

	public static int readInt(RandomAccessFile raf) throws IOException
	{
		int result = 0;

		byte[] buf = new byte[Pack.sizeof(Pack.INT)];
    	raf.read(buf, 0, Pack.sizeof(Pack.INT));
    	byte type = Pack.get_data_type(buf);
    	assert type == Pack.INT;
    	result = Pack.unpackInt(buf);

		return result;
	}

	public static int write(OutputStream os, int value) throws IOException
	{
		byte[] buf = pack(value);
    	os.write(buf);

		return buf.length;
	}

	public static int write(RandomAccessFile raf, int value) throws IOException
	{
		byte[] buf = pack(value);
    	raf.write(buf);

		return buf.length;
	}
}
