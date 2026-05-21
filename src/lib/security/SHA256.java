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

public class SHA256 extends Digest {

	// public SHA_256(String dirname, String filename) throws FileNotFoundException, IOException
	// public SHA_256(File fd) throws FileNotFoundException, IOException
	// public SHA_256(String text)
	// public SHA_256(byte[] msg)
	
	/*
	 * See http://www.itl.nist.gov/fipspubs/fip180-1.htm for SHA256 standard
	 * 
	 * Implement the algorithm from the document Secure Hash Standard (SHS)
	 * http://csrc.nist.gov/publications/fips/fips180-3/fips180-3_final.pdf
	 * Section 6.2.2, pages 21, 22.
	 */
	
    public static final String digest_name = "SHA-256";

	public byte[] digest_bytes  = new byte[bytes_per_digest];
	public int [] digest_words  = null;
	public String digest_string = null;

	public static final int bits_per_word    =  32;
	public static final int bits_per_digest  = 256;
	public static final int bits_per_block   = 512;

	public static final int bytes_per_word   = bits_per_word   / bits_per_byte;
	public static final int bytes_per_block  = bits_per_block  / bits_per_byte;
	public static final int words_per_block  = bits_per_block  / bits_per_word;
	public static final int bytes_per_digest = bits_per_digest / bits_per_byte;
	public static final int words_per_digest = bits_per_digest / bits_per_word;

	public static final int bits_per_dword   = 64;
	public static final int bytes_per_dword  = bits_per_dword  / bits_per_byte;

	private int[]  M = new int[words_per_block];
	private int[]  H = new int[words_per_digest];
	private int[]  W = new int[64];


	public SHA256(String dirname, String filename) throws FileNotFoundException, IOException
	{
		File file = new File(dirname, filename);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		long len = raf.length();

		compute_init(H, W);
		
		byte[] b = new byte[bytes_per_block];
		
		int rd_len = 0;
		for (long i=0; i < len && 0 <= rd_len; i += rd_len) {
			rd_len = raf.read(b);
			
			if (rd_len == 0) continue;
			
			add_data(b, rd_len, M, H, W);
		}
		
		end_data(M, H, W);
		
		raf.close();
	}

	public SHA256(File dir, String filename) throws FileNotFoundException, IOException
	{
		File file = new File(dir, filename);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		long len = raf.length();

		compute_init(H, W);
		
		byte[] b = new byte[bytes_per_block];
		
		int rd_len = 0;
		for (long i=0; i < len && 0 <= rd_len; i += rd_len) {
			rd_len = raf.read(b);
			
			if (rd_len == 0) continue;
			
			add_data(b, rd_len, M, H, W);
		}
		
		end_data(M, H, W);
		
		raf.close();
	}
	
	public SHA256(File fd) throws FileNotFoundException, IOException
	{
		RandomAccessFile raf = new RandomAccessFile(fd, "r");
		long len = raf.length();

		compute_init(H, W);

		byte[] b = new byte[bytes_per_block];

		int rd_len = 0;
		for (long i=0; i < len; i += rd_len) {
			rd_len = raf.read(b);
			
			add_data(b, rd_len, M, H, W);
		}
		
		end_data(M, H, W);
		
		raf.close();
	}
	
	public SHA256(String text)
	{
		byte[] msg = text.getBytes();
		compute_hash(msg, msg.length);
	}

	public SHA256(byte[] msg)
	{
		compute_hash(msg, msg.length);
	}

	public SHA256()
	{
		compute_init(H, W);
	}

	
	
	public String toString()
	{
		return digest_string;
	}
	

	public void update(byte[] msg) 
	{
		add_data(msg, msg.length, M, H, W);
	}

	public byte[] digest() 
	{
		end_data(M, H, W);
		return digest_bytes;
	}

	public void reset() 
	{
		compute_init(H, W);
	}

	public String name() 
	{
		return digest_name;
	}
	

	
	private void compute_hash(byte[] msg, int len)
	{
		compute_init(H, W);
		
		add_data(msg, len, M, H, W);
		
		end_data(M, H, W);
	}


	private void compute_init(int[] H, int[] W)
	{
	    assert (H != null && H.length == words_per_digest);
	    assert (W != null && W.length == 64);
	    assert (K != null && K.length == 64);

		for (int i=0; i < H.length; i++) {
			H[i] = init_H[i];
		}
		
		for (int i=0; i < W.length; i++) {
			W[i] = 0;
		}
		
		msg_bits = 0;
	}
	
	private byte[] block   = new byte[bytes_per_block];
	private int    blk_len = 0;

	private void add_data(byte[] msg, int len, int[] M, int[] H, int[] W)
	{
	    assert (M != null && M.length == words_per_block);
	    
	    if (msg == null || len <= 0) return;

	    assert (0 <= len && len <= msg.length);

		msg_bits += bits_per_byte * len;
		
	    if ((blk_len + len) < bytes_per_block) {
	    	// not enough data to fill a block, so copy it to surplus
	    	for (int i=0; i < len; i++) {
	    		block[blk_len+i] = msg[i];
	    	}
	    	blk_len += len;
	    } else {
	    	// we have at least one block, add the block to the hash
	    	for (int i=0; i < (bytes_per_block - blk_len); i++) {
	    		block[blk_len+i] = msg[i];
	    	}
	    	copy_block(M, block);
			compute_block(M, H, W);
			
			// if there's more, do more
			for (int base=bytes_per_block-blk_len; base < len; base += bytes_per_block) {
				if ((len - base) < bytes_per_block) {
					// not enough to fill another block, so copy the surplus to block 
					blk_len = len - base;
					for (int i=0; i < blk_len; i++) {
						block[i] = msg[base+i];
					}
				} else {
					// enough to fill (at least) one more block, so add it to the hash
					blk_len = 0;
					for (int i=0; i < bytes_per_block; i++) {
						block[i] = msg[base+i];
					}
			    	copy_block(M, block);
					compute_block(M, H, W);
				}
			}
	    }
	}
	
	private void end_data(int[] M, int[] H, int[] W)
	{
		for (int i=blk_len; i < block.length; i++) {
			block[i] = 0;
		}
		if (blk_len < 56) {
			block[blk_len++] = (byte) 0x80;
			for (int i=0; i < bytes_per_dword; i++) {
				block[block.length - 1 - i] = (byte) (0xFF & (msg_bits >> (8*i)));
			}
			
	    	copy_block(M, block);
			compute_block(M, H, W);
		} else {
	    	copy_block(M, block);
			compute_block(M, H, W);
			
			block[0] = (byte) 0x80;
			for (int i=1; i < (block.length - bytes_per_dword); i++) {
				block[i] = 0;
			}
			
			for (int i=0; i < bytes_per_dword; i++) {
				block[block.length - 1 - i] = (byte) (0xFF & (msg_bits >> (8*i)));
			}
	    	copy_block(M, block);
			compute_block(M, H, W);
		}
		
		// copy the hash value from H to digest_bytes, digest_words and digest_string
		digest_words = H;

		for (int i=0; i < H.length; i++) {
			digest_bytes[i*bytes_per_word + 0] = (byte) ((H[i] >> 24) & 0xFF);
			digest_bytes[i*bytes_per_word + 1] = (byte) ((H[i] >> 16) & 0xFF);
			digest_bytes[i*bytes_per_word + 2] = (byte) ((H[i] >>  8) & 0xFF);
			digest_bytes[i*bytes_per_word + 3] = (byte) ((H[i] >>  0) & 0xFF);
		}
		
		// System.out.println("B: "+to_hex(digest_bytes));

		digest_string = "";
		for (int i=0; i < digest_bytes.length; i++) {
			int up = (digest_bytes[i] >> 4) & 0xF;
			int dn = (digest_bytes[i] >> 0) & 0xF;
			digest_string += (0 <= up && up <= 9) ? (char)(up+'0') : (char)(up-10+'A');
			digest_string += (0 <= dn && dn <= 9) ? (char)(dn+'0') : (char)(dn-10+'A');
		}
	}
	
	private static final void copy_block(int[] M, byte[] msg)
	{
		for (int i=0; i < M.length; i++) {
			int mi = i*bytes_per_word;
			M[i] =  (msg[mi+0] << 24) & 0xFF000000 |
					(msg[mi+1] << 16) & 0x00FF0000 |
					(msg[mi+2] <<  8) & 0x0000FF00 |
					(msg[mi+3] <<  0) & 0x000000FF;
		}
	}

	private static final void compute_block(int[] M, int[] H, int[] W)
	{
	    int a, b, c, d, e, f, g, h;
	    int T1, T2;
	    
        // Step 1. Prepare W[i]

        // copy M into W.
	    for (int t=0; t < M.length; t++) {
	    	W[t] = M[t];
	    }

	    // initialize the rest of W.
	    for (int t=16; t < 64; t++) {
	    	W[t] = sigma_1(W[t- 2]) + W[t- 7] + sigma_0(W[t-15]) + W[t-16];
	    }

	    // Step 2. Initialize the working values
	    a = H[0];
	    b = H[1];
	    c = H[2];
	    d = H[3];
	    e = H[4];
	    f = H[5];
	    g = H[6];
	    h = H[7];

	    // Step 3.
	    for (int t=0; t < 64; t++) {
	    	T1 = h + SIGMA_1(e) + Ch(e,f,g) + K[t] + W[t];

	    	T2 = SIGMA_0(a) + Maj(a,b,c);

	    	h = g;
	    	g = f;
	    	f = e;
	    	e = d + T1;
	    	d = c;
	    	c = b;
	    	b = a;
	    	a = T1 + T2; 
	    }

        // Step 4. Compute the ith intermediate H[i]
	    
	    // System.out.println("H: "+to_hex(H));

	    H[0] += a;               // H[0] += a
	    H[1] += b;               // H[1] += b
	    H[2] += c;               // H[2] += c
	    H[3] += d;               // H[3] += d
	    H[4] += e;               // H[4] += e
	    H[5] += f;               // H[5] += f
	    H[6] += g;               // H[6] += g
	    H[7] += h;               // H[7] += h
	    
	    // System.out.println("H: "+to_hex(H));
	}

	
	private static final int SHR(int n, int x)
	{
		return (x >> n) & ~(-1 << (32-n));
	}
	
	private static final int ROTR(int n, int x)
	{
		return SHR(n,x) | (x << (32-n));
	}
	
	private static final int Ch(int x, int y, int z)
	{
		return (x&y) ^ ((~x)&z);
	}
	
	private static final int Maj(int x, int y, int z)
	{
		return ((x&y) ^ (x&z) ^ (y&z));
	}
	
	private static final int SIGMA_0(int x)
	{
		return ROTR( 2,x) ^ ROTR(13,x) ^ ROTR(22,x);
	}
	
	private static final int SIGMA_1(int x)
	{
		return ROTR( 6,x) ^ ROTR(11,x) ^ ROTR(25,x);
	}
	
	private static final int sigma_0(int x)
	{
		return ROTR( 7,x) ^ ROTR(18,x) ^ SHR( 3,x);
	}
	
	private static final int sigma_1(int x)
	{
		return ROTR(17,x) ^ ROTR(19,x) ^ SHR(10,x);
	}

	private static final int[] K = {
		0x428a2f98,		//  0
		0x71374491,		//  1
		0xb5c0fbcf,		//  2
		0xe9b5dba5,		//  3
		0x3956c25b,		//  4
		0x59f111f1,		//  5
		0x923f82a4,		//  6
		0xab1c5ed5,		//  7
		0xd807aa98,		//  8
		0x12835b01,		//  9
		0x243185be,		// 10
		0x550c7dc3,		// 11
		0x72be5d74,		// 12
		0x80deb1fe,		// 13
		0x9bdc06a7,		// 14
		0xc19bf174,		// 15
		0xe49b69c1,		// 16
		0xefbe4786,		// 17
		0x0fc19dc6,		// 18
		0x240ca1cc,		// 19
		0x2de92c6f,		// 20
		0x4a7484aa,		// 21
		0x5cb0a9dc,		// 22
		0x76f988da,		// 23
		0x983e5152,		// 24
		0xa831c66d,		// 25
		0xb00327c8,		// 26
		0xbf597fc7,		// 27
		0xc6e00bf3,		// 28
		0xd5a79147,		// 29
		0x06ca6351,		// 30
		0x14292967,		// 31
		0x27b70a85,		// 32
		0x2e1b2138,		// 33
		0x4d2c6dfc,		// 34
		0x53380d13,		// 35
		0x650a7354,		// 36
		0x766a0abb,		// 37
		0x81c2c92e,		// 38
		0x92722c85,		// 39
		0xa2bfe8a1,		// 40
		0xa81a664b,		// 41
		0xc24b8b70,		// 42
		0xc76c51a3,		// 43
		0xd192e819,		// 44
		0xd6990624,		// 45
		0xf40e3585,		// 46
		0x106aa070,		// 47
		0x19a4c116,		// 48
		0x1e376c08,		// 49
		0x2748774c,		// 50
		0x34b0bcb5,		// 51
		0x391c0cb3,		// 52
		0x4ed8aa4a,		// 53
		0x5b9cca4f,		// 54
		0x682e6ff3,		// 55
		0x748f82ee,		// 56
		0x78a5636f,		// 57
		0x84c87814,		// 58
		0x8cc70208,		// 59
		0x90befffa,		// 60
		0xa4506ceb,		// 61
		0xbef9a3f7,		// 62
		0xc67178f2,		// 63
	};
	
	private static final int[] init_H = {
		0x6a09e667,		// H[0]
		0xbb67ae85,		// H[1]
		0x3c6ef372,		// H[2]
		0xa54ff53a,		// H[3]
		0x510e527f,		// H[4]
		0x9b05688c,		// H[5]
		0x1f83d9ab,		// H[6]
		0x5be0cd19,		// H[7]
	};

	public int bits_per_digest()
	{
		return bits_per_digest;
	}
	
	public long   msg_bits = 0;
	


	public static void main(String[] args)
	{
		System.out.println(new SHA256("abc"));
	}
}
