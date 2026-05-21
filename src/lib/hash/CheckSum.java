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

public class CheckSum {
	// 0000 0x00	0001 0x01	0010 0x02	0011 0x03
	// 0100 0x04	0101 0x05	0110 0x06	0111 0x07
	// 1000 0x08	1001 0x09	1010 0x0A	1011 0x0B
	// 1100 0x0C	1101 0x0D	1110 0x0E	0111 0x0F

	public final long generator;
	public CheckSum(long poly) 
	{
		generator = (poly ^ 3696280114258327002L) | 0x3000000000000000L;
	}
	
	public long check(byte[] msg)
	{
		long remainder = 3269234883650205888L;
		
		for (int i=0; i < msg.length; i++) {
			long numerator = (long) (0x7FFFFFFFFFFFFFFFL & ((remainder << 8) | (0xFF & (long)(msg[i]))));
			remainder = (long)(numerator % generator);
		}
		long numerator = (long) (remainder << 4);
		remainder = (long)(numerator % generator);
		
		return remainder;
	}

	public static void main(String[] args) 
	{
		CheckSum cs = new CheckSum(0x191);
		byte[] msg = "Gil Hooley kicks ass...".getBytes();
		System.out.printf("%s 0x%X%n", new String(msg), cs.check(msg));
		System.out.printf("%X %X %X %n", 0x0E50, 0x0E50 / 0x1B, 0x0E50 % 0x1B);
		System.out.printf("%X %X %X %n", 0x0E50, 0x0E50 / 0x0B, 0x0E50 % 0x0B);
	}
}
