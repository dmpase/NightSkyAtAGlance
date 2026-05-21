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
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import lib.base.Base;
import lib.base.Base64;

public class Encode {

	public static InputStream  input    = System.in;
	public static OutputStream output   = System.out;
	public static String       password = "nEj@ha6Plugh";
	public static String       in_file  = "System.in";
	public static String       ou_file  = "System.out";
	public static String       tp_file  = "output.txt";
	
	public static byte[]       input_buffer   = new byte[3*1024*1024];		// make the size a factor of 3 (in Base64, 3 bytes inflates evenly into 4)


	public static byte[] encode_stream(String password, InputStream input, OutputStream output, boolean compress, boolean encrypt, boolean base64)
	{
		byte[] hash = null;
		
		try {
			InputStream  in = null;
			OutputStream ou = null;

			// compress/deflate the input
			in = input;
			File tmp = File.createTempFile("temp", "zip");
			tmp.deleteOnExit();
			ou = new FileOutputStream(tmp.getCanonicalPath());

			if (compress) {
				hash = deflate(in, ou);
			} else {
				hash = copy(in, ou);
			}
			if (in != System.in) {
				in.close();
			}
			ou.close();

			// encrypt the data
			in = new FileInputStream(tmp.getCanonicalPath());
			tmp = File.createTempFile("temp", "zip");
			tmp.deleteOnExit();
			ou = new FileOutputStream(tmp.getCanonicalPath());

			if (encrypt) {
				encrypt(password, in, ou);
			} else {
				copy(in, ou);
			}
			in.close();
			ou.close();

			// encode the data with a Base64 encoding
			in = new FileInputStream(tmp.getCanonicalPath());
			ou = output;

			if (base64) {
				encode(password, in, ou);
			} else {
				copy(in, ou);
			}
			in.close();
			if (ou != System.out && ou != System.err) {
				ou.close();
			}
		} catch (Exception e) {
			System.out.println(" 88: "+e);
			e.printStackTrace();
			System.exit(1);
		}
		
		return hash;
	}
	
	public static byte[] decode_stream(String password, InputStream input, OutputStream output, boolean compress, boolean encrypt, boolean base64)
	{
		byte[] hash = null;
		
		try {
			InputStream  in = null;
			OutputStream ou = null;

			// decode the data from a Base64 encoding
			in = input;
			File tmp = File.createTempFile("temp", "zip");
			tmp.deleteOnExit();
			ou = new FileOutputStream(tmp.getCanonicalPath());
			
			if (base64) {
				decode(password, in, ou);
			} else {
				copy(in, ou);
			}
			if (in != System.in) {
				in.close();
			}
			ou.close();

			// decrypt the data
			in = new FileInputStream(tmp.getCanonicalPath());
			tmp = File.createTempFile("temp", "zip");
			tmp.deleteOnExit();
			ou = new FileOutputStream(tmp.getCanonicalPath());
			
			if (encrypt) {
				decrypt(password, in, ou);
			} else {
				copy(in, ou);
			}
			in.close();
			ou.close();

			// decompress/inflate the data
			in = new FileInputStream(tmp.getCanonicalPath());
			ou = output;

			if (compress) {
				hash = inflate(in, ou);
			} else {
				hash = copy(in, ou);
			}
			in.close();
			if (ou != System.out && ou != System.err) {
				ou.close();
			}
		} catch (Exception e) {
			System.out.println("148: "+e);
			e.printStackTrace();
			System.exit(1);
		}
		
		return hash;
	}

	
	public static void encrypt(String password, InputStream input, OutputStream output)
	{
		byte[] clr = new byte[32*1024];
		byte[] enc = new byte[clr.length];
		
		try {
			// get a new cipher instance
			Code code = new AES128(password);
			code.init(Code.ENCRYPT_MODE);
			
			// read clear text from file, write it as encrypted text
			while (0 < input.available()) {
				// read
				int len=input.read(clr);
				if (0 < len) {
					// encrypt the buffer
					int out = code.update(clr, 0, len, enc);
				
					// write encrypted text to the file
					output.write(enc, 0, out);
				}
			}

			// clear out any remaining bytes from the cipher
			enc = code.do_final();
			if (enc != null && 0 < enc.length) {
				output.write(enc);
			}
		} catch (Exception e) {
			System.out.println("186: "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void decrypt(String password, InputStream input, OutputStream output)
	{
		byte[] enc = new byte[32*1024];
		byte[] clr = new byte[enc.length];
		
		try {
			// get a new cipher instance
			Code code = new AES128(password);
			code.init(Code.DECRYPT_MODE);

			// read encrypted text from file, write it as clear text
			while (0 < input.available()) {
				// read
				int len=input.read(enc);
				if (0 < len) {
					// decrypt the buffer
					int out = code.update(enc, 0, len, clr);
				
					// write clear text to the file
					output.write(clr, 0, out);
				}
			}

			// clear out any remaining bytes from the cipher
			clr = code.do_final();
			if (clr != null && 0 < clr.length) {
				output.write(clr);
			}
		} catch (Exception e) {
			System.out.println("221: "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static int password_to_seed(String pass_str)
	{
		int seed = 0;

		byte[] password = (new SHA160(pass_str.getBytes())).digest_bytes;
		for (int i=0; password != null && i < password.length; i+=4) {
			seed ^= (((int)password[i+0] & 0xff) <<  0) | (((int)password[i+1] & 0xff) <<  8) | (((int)password[i+2] & 0xff) << 16) | (((int)password[i+3] & 0x3f) << 24); 
		}

		return seed;
	}

	public static final int b64_input_line_size  = 3*20;
	public static final int b64_output_line_size = 4*b64_input_line_size/3;
	
	public static void encode(String password, InputStream input, OutputStream output)
	{
		int seed = password_to_seed(password);
		Base base = new Base64(permute(seed, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-+"));
		byte[] crlf = { '\r', '\n' };
		byte[] input_buffer = new byte[b64_input_line_size];
		
		try {
			while (0 < input.available()) {
				// read
				int br = input.read(input_buffer);
				if (0 < br) {
					byte[] encode_buffer = base.encode(input_buffer, 0, br);
					output.write(encode_buffer);
					output.write(crlf);
				}
			}
		} catch (Exception e) {
			System.out.println("260: "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void decode(String password, InputStream input, OutputStream output)
	{
		int seed = password_to_seed(password);
		Base base = new Base64(permute(seed, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-+"));
		byte[] input_buffer = new byte[b64_output_line_size+2];

		try {
			while (0 < input.available()) {
				// read
				int br = input.read(input_buffer);
				if (2 < br) {
					byte[] decode_buffer = base.decode(input_buffer, 0, br-2);
					output.write(decode_buffer);
				}
			}
		} catch (Exception e) {
			System.out.println("282: "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static byte[] deflate(InputStream input, OutputStream output)
	{
		byte[] hash = null;
		
		try {
			OutputStream zipout = new DeflaterOutputStream(output);
			MessageDigest digest = MessageDigest.getInstance("SHA1");

			while (0 < input.available()) {
				// read
				int br = input.read(input_buffer);
				if (0 < br) {
					// compress
					zipout.write(input_buffer, 0, br);
				
					// hash
					digest.update(input_buffer, 0, br);
				}
			}

			zipout.close();

			hash = digest.digest();
		} catch (Exception e) {
			System.out.println("313: "+e);
			e.printStackTrace();
			System.exit(1);
		}
		
		return hash;
	}

	
	public static byte[] inflate(InputStream input, OutputStream output)
	{
		InputStream zipin = new InflaterInputStream(input);
		
		byte[] hash = null;

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");

			while (0 < zipin.available()) {
				int br = zipin.read(input_buffer);
				if (0 < br) {
					output.write(input_buffer, 0, br);
				
					// hash
					digest.update(input_buffer, 0, br);
				}
			}

			zipin.close();

			hash = digest.digest();
		} catch (Exception e) {
			System.out.println("344: "+e);
			e.printStackTrace();
			System.exit(1);
		}
		
		return hash;
	}
	
	public static byte[] copy(InputStream input, OutputStream output)
	{
		byte[] hash = null;

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");

			while (0 < input.available()) {
				// read
				int br = input.read(input_buffer);
				if (0 < br) {
					// compress
					output.write(input_buffer, 0, br);
	
					// hash
					digest.update(input_buffer, 0, br);
				}
			}

			hash = digest.digest();
		} catch (Exception e) {
			System.out.println("373: "+e);
			e.printStackTrace();
			System.exit(1);
		}

		return hash;
	}

	public static void compare(InputStream f0, InputStream f1)
	{
		byte[] b0 = new byte[32*1024*1024];
		byte[] b1 = new byte[32*1024*1024];
		
		try {
			boolean match = true;
			int i0 = f0.read(b0);
			int i1 = f1.read(b1);
			if (i0 != i1) {
				System.out.println("391: files are not the same length. len0="+i0+", len1 = "+i1);
			} else {
				for (int i=0; i < i0; i++) {
					if (b0[i] != b1[i]) {
						match = false;
						System.out.println("396: files do not match at byte: "+i);
						break;
					}
				}
				if (match) {
					System.out.println("401: files match.");
				}
			}
		} catch (Exception e) {
			System.out.println("405: "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// find a random permutation of the string
	public static String permute(int seed, String string)
	{
		String result = null;
		
		if (string != null) {
			byte[] buf = string.getBytes();
			Random r = new Random(seed);
			for (int i=0; i < string.length(); i++) {
				// compute a random index in the range 0 <= index < string.length()
				int idx = r.nextInt(string.length());
				idx = idx % string.length();
				idx = (0 < idx) ? idx : 0;

				// swap the current byte with the random byte
				byte tmp = buf[idx];
				buf[idx] = buf[i];
				buf[i]   = tmp;
			}
			result = new String(buf);
		}
		
		return result;
	}
}
