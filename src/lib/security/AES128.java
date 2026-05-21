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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;


public class AES128 extends Code {
    private SecretKeySpec secret_key = null;
    private Cipher        cipher     = null;

    public static final String cipher_name  = (String) Code.codes[18][0];
	public static final int    aes_key_bits = (int)    Code.codes[18][1];
	public static final int    aes_key_size = aes_key_bits/8;

    // create an instance of AES, set up the secret rsa_key
    public AES128(String password)
    {
    	super(password, aes_key_size);

    	// set the 16-byte mash-up to be the encryption rsa_key
    	secret_key = new SecretKeySpec(key, cipher_name);

    	try {
    		cipher = Cipher.getInstance(cipher_name);
    	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
    		e.printStackTrace();
    	}
    }

    // create an instance of AES, set up the secret rsa_key
    public AES128(byte[] my_key) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
    	super(my_key);

    	// set the 16-byte mash-up to be the encryption rsa_key
    	secret_key = new SecretKeySpec(key, cipher_name);
    	cipher = Cipher.getInstance(cipher_name);
    }

    @Override
    public void init(int m) throws InvalidKeyException
    {
    	mode = m;
    	if (mode == ENCRYPT_MODE) {
    		cipher.init(Cipher.ENCRYPT_MODE, secret_key);
    	} else if (mode == DECRYPT_MODE) {
    		cipher.init(Cipher.DECRYPT_MODE, secret_key);
    	}
    }

    @Override
    public byte[] update(byte[] clr)
    {
		byte[] out = null;

		try {
			out = cipher.update(clr);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

    	return out;
    }
    
    @Override
    public byte[] update(byte[] clr, int off, int len)
    {
		byte[] out = null;

		try {
			out = cipher.update(clr, off, len);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

    	return out;
    }
    
    @Override
    public int update(byte[] clr, int off, int len, byte[] enc)
    {
		int out = 0;

		try {
			out = cipher.update(clr, off, len, enc);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}

    	return out;
    }
    
    public int do_final(byte[] enc, int off) throws IllegalBlockSizeException, ShortBufferException, BadPaddingException
    {
    	int result = 0;

		int out = cipher.doFinal(enc, 0);
		result = out;

		return result;
    }
    
    @Override
    public byte[] do_final() throws IllegalBlockSizeException, BadPaddingException
    {
    	byte[] result = null;
    	
    	result = cipher.doFinal();
	    
    	return result;
    }
    
    @Override
    public byte[] do_final(byte[] buf) throws IllegalBlockSizeException, BadPaddingException
    {
    	byte[] result = null;
    	
		byte[] enc = cipher.doFinal(buf);
		result = enc;
	    
	    return result;
    }
    
    @Override
    public byte[] do_final(byte[] buf, int off, int len)
    {
    	byte[] result = null;
    	
	    try {
			byte[] enc = cipher.doFinal(buf, off, len);
			result = enc;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	    
    	return result;
    }
}
