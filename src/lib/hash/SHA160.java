package lib.hash;

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

import java.security.MessageDigest;
import java.io.*;

public class SHA160 {
    private static String        digest_name = "SHA1";
    private static MessageDigest digest      = init(digest_name);
	private static int           LENGTH      = -1;

	public byte[] digest_bytes  = null;
	public String digest_string = null;
	
	public SHA160()
	{
		digest_bytes  = null;
		digest_string = null;
	}

	public SHA160(byte[] msg)
	{
		digest.update(msg);
		digest_bytes  = digest.digest();
		digest_string = toString();
	}

	public SHA160(byte[] msg, int offset, int len)
	{
		digest.update(msg, offset, len);
		digest_bytes  = digest.digest();
		digest_string = toString();
	}
	
	public SHA160(File fd) throws FileNotFoundException, IOException
	{
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(fd);
		for (int r=fis.read(buf); 0 < r; r=fis.read(buf)) {
			digest.update(buf, 0, r);
		}
		fis.close();
		
		digest_bytes  = digest.digest();
		digest_string = toString();
	}
	
	public SHA160(InputStream fis) throws IOException
	{
		byte[] buf = new byte[4096];
		for (int r=fis.read(buf); 0 < r; r=fis.read(buf)) {
			digest.update(buf, 0, r);
		}
		fis.close();
		
		digest_bytes  = digest.digest();
		digest_string = toString();
	}
	
	public static int length()
	{
		return LENGTH;
	}
	
	public static String name()
	{
		return digest_name;
	}
	
	public synchronized void set(byte[] h)
	{
		if (h == null || h.length == LENGTH) {
			digest_bytes = h;
			if (h != null) {
				digest_string = new String(h);
			}
		}
	}

	public synchronized boolean equals(SHA160 h)
	{
		if (h == null) {
			return false;
		} else if (digest_bytes == h.digest_bytes) {
			return true;
		} else if (digest_bytes == null || h.digest_bytes == null || digest_bytes.length != h.digest_bytes.length) {
			return false;
		} else {
			for (int i=0; i < digest_bytes.length; i++) {
				if (digest_bytes[i] != h.digest_bytes[i]) {
					return false;
				}
			}
			return true;
		}
	}
	

	public synchronized boolean equals(String s)
	{
		boolean result = false;
	
		if (digest_string == null) {
			digest_string = toString();
		}
		result = digest_string.equalsIgnoreCase(s);
		
		return result;
	}

	
	public synchronized String toString()
	{
		if (digest_string == null) {
			digest_string = "";
			char[] a = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			
			for (int i=0; digest_bytes != null && i < digest_bytes.length; i++) {
				byte b = digest_bytes[i];
				int lh = (b >> 0) & 0xf;
				int uh = (b >> 8) & 0xf;
				digest_string += a[uh] + "" + a[lh];
			}
		}
		
		return digest_string;
	}
	
	public static MessageDigest init(String name)
	{
		try {
			MessageDigest tmp = MessageDigest.getInstance(name);
			if (tmp != null) {
				tmp.update("test message".getBytes());
				byte[] hash = tmp.digest();
				if (hash != null) {
					digest = tmp;
					LENGTH = hash.length;
					digest_name = name;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return digest;
	}
}
