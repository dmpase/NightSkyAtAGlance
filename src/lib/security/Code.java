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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



// @SuppressWarnings("unused")
public abstract class Code {
    protected byte[] key  = null;
    protected int    mode = ENCRYPT_MODE;

    public static final int ENCRYPT_MODE = 0;
    public static final int DECRYPT_MODE = 1;

    public final int key_bits;										// rsa_key length in bits
    public final int key_size;										// rsa_key length in bytes
    public static final String digest_name = SHA160.digest_name;	// hash function to create encryption keys

    public abstract void   init(int mode) throws InvalidKeyException;
    public abstract byte[] update(byte[] clr);
    public abstract byte[] update(byte[] clr, int off, int len);
    public abstract int    update(byte[] clr, int off, int len, byte[] enc);
    public abstract byte[] do_final(byte[] enc, int off, int len);
    public abstract byte[] do_final(byte[] enc) throws IllegalBlockSizeException, BadPaddingException;
    public abstract byte[] do_final() throws IllegalBlockSizeException, BadPaddingException;

    public static Object[][] codes = {
    		{ "AES/CBC/NoPadding",						 128, },	//  0
    		{ "AES/CBC/PKCS5Padding",					 128, },	//  1
    		{ "AES/ECB/NoPadding",						 128, },	//  2
    		{ "AES/ECB/PKCS5Padding",					 128, },	//  3
    		{ "DES/CBC/NoPadding",						  56, },	//  4
    		{ "DES/CBC/PKCS5Padding",					  56, },	//  5
    		{ "DES/ECB/NoPadding",						  56, },	//  6
    		{ "DES/ECB/PKCS5Padding",					  56, },	//  7
    		{ "DESede/CBC/NoPadding",					 168, },	//  8
    		{ "DESede/CBC/PKCS5Padding",				 168, },	//  9
    		{ "DESede/ECB/NoPadding",					 168, },	// 10
    		{ "DESede/ECB/PKCS5Padding",				 168, },	// 11
    		{ "RSA/ECB/PKCS1Padding",					1024, },	// 12
    		{ "RSA/ECB/PKCS1Padding",					2048, },	// 13
    	    { "RSA/ECB/OAEPWithSHA-1AndMGF1Padding",	1024, },	// 14
    	    { "RSA/ECB/OAEPWithSHA-1AndMGF1Padding",	2048, },	// 15
    	    { "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",	1024, },	// 16
    	    { "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",	2048, },	// 17
    		{ "AES",									 128, },	// 18
    		{ "AES",									 256, },	// 19
    };

	public static Code create_code(String name, byte[] my_key) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		Code result = null;
		
		if (name.equals(AES128.cipher_name)) {
			result = new AES128(my_key);
		}

		return result;
	}

	public static Code create_code(String name, String password) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		Code result = null;
		
		byte[] my_key = create_key(name, password);
		
		if (name.equals(AES128.cipher_name)) {
			result = new AES128(my_key);
		}

		return result;
	}

	public static byte[] create_key(String name, String password)
	{
		byte[] result = null;
		
		if (name.equals(AES128.cipher_name)) {
			result = password_to_key(password, AES128.aes_key_size, digest_name);
		}

		return result;
	}

    // set up the secret rsa_key
	public Code(String password, int len)
    {
		key_bits = len * 8;
		key_size = len;
    	key = password_to_key(password, len, digest_name);
    }

    // set up the secret rsa_key
    public Code(byte[] key)
    {
		key_bits = key.length * 8;
		key_size = key.length;
    	this.key = key;
    }

    // encrypt a byte array
    public byte[] encrypt(byte[] clr) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
		init(ENCRYPT_MODE);
    	
	    byte[] enc = do_final(clr);
    	
		return enc;
    }

    // encrypt a byte array
    public byte[] encrypt(byte[] clr, int off, int len) throws InvalidKeyException
    {
		init(ENCRYPT_MODE);
    	
	    byte[] enc = do_final(clr, off, len);
    	
		return enc;
    }

    // decrypt an encrypted byte array
    public byte[] decrypt(byte[] enc) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
    	init(DECRYPT_MODE);

    	byte[] clr = do_final(enc);
    	
		return clr;
    }

    // decrypt an encrypted byte array
    public byte[] decrypt(byte[] enc, int off, int len) throws InvalidKeyException
    {
    	init(DECRYPT_MODE);
   
    	byte[] clr = do_final(enc, off, len);    	

    	return clr;
    }


    // encrypt and copy a clear-text file to an encrypted file
    public void copy_clr_to_enc(File src_clr_file, File tgt_enc_file) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		byte[] clr = new byte[1024];
		byte[] enc = new byte[clr.length];
		
		// get a new cipher instance
	    init(ENCRYPT_MODE);

		// read clear text from file, write it as encrypted text
		InputStream  cis = new FileInputStream(src_clr_file);
		OutputStream eos = new FileOutputStream(tgt_enc_file);
		for (int len=cis.read(clr); 0 < len; len=cis.read(clr)) {
			// encrypt the buffer
			int out = update(clr, 0, len, enc);
		
			// write encrypted text to the file
			eos.write(enc, 0, out);
		}

		// clear out any remaining bytes from the cipher
		enc = do_final();
		if (enc != null && 0 < enc.length) {
			eos.write(enc);
		}
		
		cis.close();
		eos.close();
	}

    public void copy_enc_to_clr(File src_enc_file, long off, long length, File tgt_clr_file) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
	{
		byte[] enc = new byte[1024];
		byte[] clr = new byte[enc.length];

		// get a new cipher instance
		init(DECRYPT_MODE);

		// read clear text from file, write it as encrypted text
	    RandomAccessFile eis = new RandomAccessFile(src_enc_file, "r");
	    RandomAccessFile cos = new RandomAccessFile(tgt_clr_file, "rw");
	    eis.seek(off);
	    long remaining = length;
	    int bs = (enc.length < remaining) ? enc.length : (int) remaining;
	    while (0 < bs) {
	    	int len = eis.read(enc, 0, bs);
			// decrypt the buffer
			int out = update(enc, 0, len, clr);

			// write encrypted text to the file
			cos.write(clr, 0, out);
			
			remaining -= len;
			bs = (enc.length < remaining) ? enc.length : (int) remaining;
	    }

		// clear out any remaining bytes from the cipher
		clr = do_final();
		if (clr != null && 0 < clr.length) {
			cos.write(clr);
		}
		
		eis.close();
		cos.close();
	}

    public void copy_enc_to_clr(File src_enc_file, File tgt_clr_file) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		byte[] enc = new byte[1024];
		byte[] clr = new byte[enc.length];
		
		// get a new cipher instance
		init(DECRYPT_MODE);

		// read clear text from file, write it as compressed text
		InputStream  eis = new FileInputStream(src_enc_file);
		OutputStream cos = new FileOutputStream(tgt_clr_file);
		for (int len=eis.read(enc); 0 < len; len=eis.read(enc)) {
			// decrypt the buffer
			int out = update(enc, 0, len, clr);
		
			// write decrypted text to the file
			cos.write(clr, 0, out);
		}

		// clear out any remaining bytes from the cipher
		clr = do_final();
		if (clr != null && 0 < clr.length) {
			cos.write(clr);
		}
		
		eis.close();
		cos.close();
	}

	public static byte[] password_to_key(String password, int len, String digest)
	{
	    return password_to_key(password.getBytes(), len, digest);
	}

	public static byte[] password_to_key(byte[] password, int len, String digest)
	{
		byte[] hash = Digest.hash(digest, password);

	    // mash the hash into k bytes and use that as the encryption rsa_key
	    byte[] result = Arrays.copyOf(hash, len);
	    for (int i=result.length; i < hash.length; i++) {
	    	int t = i % result.length;
	    	result[t] ^= hash[i];
	    }

	    return result;
	}


	/*
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
    	Code aes = Code.create_code(AES.cipher_name, "nEjethroZappa!");
	
		String clr = "123456789012345";

		Base b2 = new Base2();
		Base b32 = new Base64();

		byte[] cip0 = aes.encrypt(clr.getBytes());

		String enc2 = b2.encode(cip0);
		for (int i=0; i < enc2.length(); i++) {
			if (i != 0 && i%b32.bits == 0) System.out.print(" ");
			System.out.print(enc2.charAt(i));
		}
		System.out.println();

		String enc32 = b32.encode(cip0);
		System.out.println(enc32);

		System.out.println(new String(b32.char_set));

		byte[] dec32 = b32.decode(enc32);
		
		for (int i=0; i < cip0.length; i++) {
			System.out.print(((int)cip0[i])+":");
		}
		System.out.println();
		for (int i=0; i < dec32.length; i++) {
			System.out.print(((int)dec32[i])+":");
		}
		System.out.println();
		
		String decryptedString = new String(aes.decrypt(dec32));
		System.out.println(decryptedString.length()+": "+decryptedString);
    }
    */
}
