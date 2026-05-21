package lib.security;

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

public abstract class Digest {
	public byte[] digest_bytes  = null;
	public String digest_string = null;

	public static final int bits_per_byte    =   8;
	public int bytes_per_digest() {return bits_per_digest()/bits_per_byte;}
	public abstract int bits_per_digest();

	public abstract void   update(byte[] b);
	public abstract byte[] digest();
	public abstract void   reset();
	public abstract String name();
	
	public static Digest create_digest(String name)
	{
		if (name == null) {
			return null;
		} else if (name.equalsIgnoreCase(SHA160.digest_name)) {
			return new SHA160();
		} else if (name.equalsIgnoreCase(SHA256.digest_name)) {
			return new SHA256();
		}
		
		return null;
	}

	
	public static byte[] hash(String sha, byte[] msg)
	{
		if (sha == null) {
			return null;
		} else if (sha.equalsIgnoreCase(SHA160.digest_name)) {
			return (new SHA160(msg)).digest_bytes;
		} else if (sha.equalsIgnoreCase(SHA256.digest_name)) {
			return (new SHA256(msg)).digest_bytes;
		}
		
		return null;
	}
	
	public static byte[] hash(String sha, String str)
	{
		byte[] msg = str.getBytes();

		if (sha == null) {
			return null;
		} else if (sha.equalsIgnoreCase(SHA160.digest_name)) {
			return (new SHA160(msg)).digest_bytes;
		} else if (sha.equalsIgnoreCase(SHA256.digest_name)) {
			return (new SHA256(msg)).digest_bytes;
		}
		
		return null;
	}
	
	public static byte[] hash(String sha, File file) throws FileNotFoundException, IOException
	{
		if (sha == null) {
			return null;
		} else if (sha.equalsIgnoreCase(SHA160.digest_name)) {
			return (new SHA160(file)).digest_bytes;
		} else if (sha.equalsIgnoreCase(SHA256.digest_name)) {
			return (new SHA256(file)).digest_bytes;
		}
		
		return null;
	}
	
	public static byte[] hash(String sha, File dir, String file) throws FileNotFoundException, IOException
	{
		if (sha == null) {
			return null;
		} else if (sha.equalsIgnoreCase(SHA160.digest_name)) {
			return (new SHA160(dir, file)).digest_bytes;
		} else if (sha.equalsIgnoreCase(SHA256.digest_name)) {
			return (new SHA256(dir, file)).digest_bytes;
		}
		
		return null;
	}
	
	
	
	public Digest()
	{
		digest_bytes  = null;
		digest_string = null;
	}
	
	public int length()
	{
		return bytes_per_digest();
	}
	
	public synchronized void set(byte[] h)
	{
		if (h == null || h.length == bytes_per_digest()) {
			digest_bytes = h;
			if (h != null) {
				digest_string = new String(h);
			}
		}
	}

	public synchronized void set(String h)
	{
		if (h == null || h.length() == 2*bytes_per_digest()) {
			digest_string = h;
			if (h != null) {
				digest_bytes = new byte[bytes_per_digest()];
				for (int i=0; i < digest_bytes.length; i++) {
					int ub = (int) digest_string.charAt(2*i);
					ub = ('0' <= ub && ub <= '9') ? (ub - '0') : 
						 ('a' <= ub && ub <= 'f') ? (ub - 'a') :
						 ('A' <= ub && ub <= 'F') ? (ub - 'A') : 0 ;
					int lb = (int) digest_string.charAt(2*i+1);
					lb = ('0' <= lb && lb <= '9') ? (lb - '0') : 
						 ('a' <= lb && lb <= 'f') ? (lb - 'a') :
						 ('A' <= lb && lb <= 'F') ? (lb - 'A') : 0 ;
					int b = ((ub << 4) | lb) & 0xff;
					digest_bytes[i] = (byte) b;
				}
			}
		}
	}

	public synchronized boolean equals(Digest h)
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
			digest_string = to_hex(digest_bytes);
		}
		
		return digest_string;
	}
	
	public static final String to_hex(byte a)
	{
		String r = "";
		
		for (int i=8-4; 0 <= i; i-=4) {
			int b = (a >> i) & 0xF;
			r += (b < 10) ? (char)(b+'0') : (char)(b-10+'a');
		}
		
		return r;
	}
	
	public static final String to_hex(byte[] a)
	{
		String r = "";

		for (int i=0; i < a.length; i++) {
			r += to_hex(a[i]);
		}
		
		return r;
	}
	
	public static final String to_hex(int a)
	{
		String r = "";
		
		for (int i=32-4; 0 <= i; i-=4) {
			int b = (a >> i) & 0xF;
			r += (b < 10) ? (char)(b+'0') : (char)(b-10+'A');
		}
		
		return r;
	}
	
	public static final String to_hex(int[] a)
	{
		String r = "";

		for (int i=0; i < a.length; i++) {
			r += " " + to_hex(a[i]);
		}
		
		return r;
	}
	
	public static boolean equals(byte[] a, byte[] b)
	{
		boolean result = false;
		
		if (a == null && b == null) {
			result = true;
		} else if (a != null && b != null && a.length == b.length) {
			result = true;
			for (int i=0; i < a.length; i++) {
				if (a[i] != b[i]) {
					result = false;
					break;
				}
			}
		}
		
		return result;
	}

	
	
	public static void main(String[] args)
	{
		System.out.println(Digest.to_hex(Digest.hash(SHA160.digest_name,"abc".getBytes())));
		System.out.println(Digest.to_hex(Digest.hash(SHA256.digest_name,"abc".getBytes())));
	}
}
