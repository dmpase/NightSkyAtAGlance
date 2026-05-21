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


public class PackString extends Pack {

	public static void packStringTypeSize(byte[] ba, int idx, int size)
	{
		assert ba != null && idx+SIZE_STRING <= ba.length;
		
		set_data_type(ba, idx, STRING);
		ba[idx+SIZE_TYPE+0] = (byte)((size >>  0) & 0xff);
		ba[idx+SIZE_TYPE+1] = (byte)((size >>  8) & 0xff);
		ba[idx+SIZE_TYPE+2] = (byte)((size >> 16) & 0xff);
		ba[idx+SIZE_TYPE+3] = (byte)((size >> 24) & 0xff);
	}
	
	public static int unpackStringSize(byte[] ba)
	{
		assert ba != null && get_data_type(ba) == STRING && SIZE_STRING <= ba.length;

		return unpackStringSize(ba, 0);
	}
	
	public static int unpackStringSize(byte[] ba, int idx)
	{
		assert ba != null && get_data_type(ba,idx) == STRING && idx+SIZE_STRING <= ba.length;
		
		int result = 0;
		
		result |= (((int) ba[idx+SIZE_TYPE+0]) & 0xff) <<  0;
		result |= (((int) ba[idx+SIZE_TYPE+1]) & 0xff) <<  8;
		result |= (((int) ba[idx+SIZE_TYPE+2]) & 0xff) << 16;
		result |= (((int) ba[idx+SIZE_TYPE+3]) & 0xff) << 24;
		
		return result;
	}
	
	public static byte data_type()
	{
		return STRING;
	}
	
	public static byte data_type(String m)
	{
		return STRING;
	}
	
	public static int data_size(String m)
	{
		int result = SIZE_STRING;
		
		if (m != null) {
			result += m.length();
		}
		
		return result;
	}
	
	public static byte[] pack(String m)
	{
		byte[] ba = new byte[data_size(m)];
		int idx = 0;
		
		pack(m, ba, idx);
			
		return ba;
	}
	
	public static byte[] pack(String m, byte[] b)
	{
		byte[] ba = new byte[b.length+data_size(m)];
		copy(ba, b);

		pack(m, ba, b.length);
		
		return ba;
	}

	public static int pack(String m, byte[] ba, int idx)
	{
		int len = data_size(m);
//		System.out.println("pack(S): m=\""+m+"\" ba.len="+ba.length+" length="+length);
		assert ba != null && 0 <= idx && idx+len <= ba.length;
//		System.out.println("pack(S): "+(ba != null && 0 <= idx && idx+length <= ba.length));

		if (m == null) {
			packStringTypeSize(ba, idx, 0);
		} else {
			byte[] s = m.getBytes();
//			System.out.println("pack(S): s.len="+s.length+" idx="+idx);
			packStringTypeSize(ba, idx, s.length);
			
			assert idx+len <= ba.length;
			for (int i=0; i < s.length; i++) {
				ba[idx+SIZE_STRING+i] = s[i];
			}
		}
		
		return idx+len;
	}
	
	public static String unpackString(byte[] ba)
	{
		assert ba != null && SIZE_STRING <= ba.length && get_data_type(ba) == STRING;

		return unpackString(ba, 0);
	}
	
	public static String unpackString(byte[] ba, int idx)
	{
		assert ba != null && 0 <= idx && idx+SIZE_STRING <= ba.length && get_data_type(ba,idx) == STRING;

		String result = null;

		int len = unpackStringSize(ba, idx);
		if (0 < len) {
			byte[] m = new byte[len];
			
			assert idx+SIZE_STRING+len <= ba.length;
			for (int i=0; i < m.length; i++) {
				m[i] = ba[idx+SIZE_STRING+i];
			}

			result = new String(m);
		}
		
		return result;
	}
	
	public static String readString(InputStream is) throws IOException
	{
		String result = null;

		// read the string header
//		System.out.println("readString: SIZE_STRING="+SIZE_STRING);
		byte[] buf = new byte[SIZE_STRING];
    	@SuppressWarnings("unused")
		int r = is.read(buf, 0, SIZE_STRING);
//		int total = buf.length;
//		System.out.println("readString: total="+total+" r="+r);
    	
    	// pull the data type, should be String
    	@SuppressWarnings("unused")
    	byte type = get_data_type(buf);
//   	System.out.println("readString: type='"+toString(type)+"'");
    	
    	// pull the string length (bytes, not characters)
    	int hlen  = unpackStringSize(buf, 0);
//    	System.out.println("readString: h.len="+buf.length+" hlen="+hlen);

    	// pull the actual string
    	if (0 < hlen) {
    		byte[] str = new byte[hlen];
    		r += is.read(str);
    		result = new String(str);
//        	System.out.println("readString: result.len="+result.length());
    	}

//    	System.out.println("readString: total="+total+" r="+r);

		return result;
	}

	@SuppressWarnings("unused")
	public static String readString(RandomAccessFile raf) throws IOException
	{
		String result = null;

		// read the string header
		byte[] buf = new byte[Pack.SIZE_STRING];
		int r = raf.read(buf, 0, Pack.SIZE_STRING);
    	
    	// pull the data type, should be String
    	byte type = Pack.get_data_type(buf);
    	
    	// pull the string length (bytes, not characters)
    	int hlen  = unpackStringSize(buf, 0);

    	// pull the actual string
    	if (0 < hlen) {
    		byte[] str = new byte[hlen];
    		r += raf.read(str);
    		result = new String(str);
    	}

		return result;
	}

	public static int write(OutputStream os, String value) throws IOException
	{
		byte[] buf = pack(value);
    	os.write(buf);

		return buf.length;
	}

	public static int write(RandomAccessFile raf, String value) throws IOException
	{
		byte[] buf = pack(value);
    	raf.write(buf);

		return buf.length;
	}
}
