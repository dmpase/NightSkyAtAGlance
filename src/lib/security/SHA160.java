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

import java.security.MessageDigest;
import java.io.*;

public class SHA160 extends Digest {
    public static final String digest_name = "SHA1";

    private static MessageDigest digest      = init();

	public static final int bits_per_word    =  32;
	public static final int bits_per_digest  = 160;
	public static final int bits_per_block   = 512;

	public static final int bytes_per_word   = bits_per_word   / bits_per_byte;
	public static final int bytes_per_block  = bits_per_block  / bits_per_byte;
	public static final int words_per_block  = bits_per_block  / bits_per_word;
	public static final int bytes_per_digest = bits_per_digest / bits_per_byte;
	public static final int words_per_digest = bits_per_digest / bits_per_word;

	public static final int bits_per_dword   = 64;
	public static final int bytes_per_dword  = bits_per_dword  / bits_per_byte;

	public SHA160()
	{
		super();
		digest.reset();
	}

	public SHA160(byte[] msg)
	{
		super();
		digest.reset();
		digest.update(msg);
		digest_bytes  = digest.digest();
		digest_string = to_hex(digest_bytes);
	}
	
	public SHA160(File fd) throws FileNotFoundException, IOException
	{
		super();
		digest.reset();
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(fd);
		for (int r=fis.read(buf); 0 < r; r=fis.read(buf)) {
			digest.update(buf, 0, r);
		}
		fis.close();
		
		digest_bytes  = digest.digest();
		digest_string = to_hex(digest_bytes);
	}
	
	public SHA160(File dir, String file) throws FileNotFoundException, IOException
	{
		super();
		File fd = new File(dir, file);
		digest.reset();
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(fd);
		for (int r=fis.read(buf); 0 < r; r=fis.read(buf)) {
			digest.update(buf, 0, r);
		}
		fis.close();
		
		digest_bytes  = digest.digest();
		digest_string = to_hex(digest_bytes);
	}

	public SHA160(InputStream fis) throws IOException
	{
		super();
		digest.reset();
		byte[] buf = new byte[4096];
		for (int r=fis.read(buf); 0 < r; r=fis.read(buf)) {
			digest.update(buf, 0, r);
		}
		fis.close();
		
		digest_bytes  = digest.digest();
		digest_string = to_hex(digest_bytes);
	}
	
	public String name()
	{
		return digest_name;
	}
	
	public synchronized void set(byte[] h)
	{
		if (h == null || h.length == bytes_per_digest) {
			digest_bytes = h;
			if (h != null) {
				digest_string = new String(h);
			}
		}
	}

	public synchronized void set(String h)
	{
		if (h == null || h.length() == 2*bytes_per_digest) {
			digest_string = h;
			if (h != null) {
				digest_bytes = new byte[bytes_per_digest];
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

	
	
	public static MessageDigest init()
	{
		try {
			MessageDigest tmp = MessageDigest.getInstance(digest_name);
			if (tmp != null) {
				tmp.update("test message".getBytes());
				byte[] hash = tmp.digest();
				if (hash != null) {
					digest = tmp;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return digest;
	}

	public int bits_per_digest()
	{
		return bits_per_digest;
	}

	public void update(byte[] b) 
	{
		digest.update(b);
	}

	public byte[] digest() 
	{
		return digest.digest();
	}

	public void reset() 
	{
		digest.reset();
	}

	public static void main(String[] args)
	{
		byte[] x = (new SHA160("abc".getBytes())).digest_bytes;
		System.out.println(to_hex(x));
	}
}
