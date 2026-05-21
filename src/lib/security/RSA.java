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

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;


public class RSA {

    private static char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String to_string(byte[] ba)
    {
		String r = "";
	
		for (int i=0; ba != null && i < ba.length; i++) {
		    int b0 = (ba[i] >> 0) & 0x0f;
		    int b4 = (ba[i] >> 4) & 0x0f;
		    r += hex[b4]+""+hex[b0];
		}
	
		return r;
    }

    public static void main(String[] argv)
    {
    	byte[] msg = "This is clear text.".getBytes();

		try {
			int key_size = 2048;
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	    	keyGen.initialize(key_size);

	    	KeyPair    key_pair     = keyGen.generateKeyPair();
	        PrivateKey orig_pri_key = key_pair.getPrivate();
	        PublicKey  orig_pub_key = key_pair.getPublic();

		    byte[] pub_key_ba = orig_pub_key.getEncoded();
		    byte[] pri_key_ba = orig_pri_key.getEncoded();

			KeyFactory kf = KeyFactory.getInstance("RSA");

			PKCS8EncodedKeySpec pri_spec = new PKCS8EncodedKeySpec(pri_key_ba);
			PrivateKey pri_key = kf.generatePrivate(pri_spec);

			X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pub_key_ba);
			PublicKey pub_key = kf.generatePublic(pub_spec);

			System.out.println("Original  : "+new String(msg));
			
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pri_key);
			byte[] cpr = cipher.doFinal(msg);

			cipher.init(Cipher.DECRYPT_MODE, pub_key);
			byte[] clr = cipher.doFinal(cpr);

			System.out.println("Clear text: "+new String(clr));
			
			cipher.init(Cipher.ENCRYPT_MODE, pub_key);
			byte[] sig = cipher.doFinal(msg);

			cipher.init(Cipher.DECRYPT_MODE, pri_key);
			byte[] con = cipher.doFinal(sig);
			
			System.out.println("Confirmed : "+new String(con));
		} catch (Exception e) {
		    System.err.println(e.getMessage());
		}
    }
}
