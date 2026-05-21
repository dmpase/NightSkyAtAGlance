package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 1988-2022 Douglas M. Pase                                     *
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
import java.io.RandomAccessFile;

public class YaleBrightStarHeader {
	
	// Integer*4 STAR0=0		Subtract from star number to get sequence number
	// Integer*4 STAR1=1		First star number in file
	// Integer*4 STARN=9110  	Number of stars in file
	// Integer*4 STNUM=1		0 if no star i.d. numbers are present
	//                      	1 if star i.d. numbers are in catalog file
	//							2 if star i.d. numbers are  in file
	// Logical*4 MPROP=1		1 if proper motion is included
	//							0 if no proper motion is included
	// Integer*4 NMAG=-1		Number of magnitudes present (-1=J2000 instead of B1950)
	// Integer*4 NBENT=32		Number of bytes per star entry
	
	public final int STAR0;
	public final int STAR1;
	public final int STARN;
	public final int STNUM;
	public final int MPROP;
	public final int NMAG;
	public final int NBENT;
	
	public YaleBrightStarHeader(RandomAccessFile raf) throws IOException 
	{
		byte[] buf = new byte[28];
		raf.read(buf);

		STAR0 = (((int)buf[0*4+3]&0xff) << (3*8)) | (((int)buf[0*4+2]&0xff) << (2*8)) | (((int)buf[0*4+1]&0xff) << (1*8)) | (((int)buf[0*4+0]&0xff) << (0*8));
		STAR1 = (((int)buf[1*4+3]&0xff) << (3*8)) | (((int)buf[1*4+2]&0xff) << (2*8)) | (((int)buf[1*4+1]&0xff) << (1*8)) | (((int)buf[1*4+0]&0xff) << (0*8));
		int x = (((int)buf[2*4+3]&0xff) << (3*8)) | (((int)buf[2*4+2]&0xff) << (2*8)) | (((int)buf[2*4+1]&0xff) << (1*8)) | (((int)buf[2*4+0]&0xff) << (0*8));
		STARN = (0 < x) ? x : -x;
		STNUM = (((int)buf[3*4+3]&0xff) << (3*8)) | (((int)buf[3*4+2]&0xff) << (2*8)) | (((int)buf[3*4+1]&0xff) << (1*8)) | (((int)buf[3*4+0]&0xff) << (0*8));
		MPROP = (((int)buf[4*4+3]&0xff) << (3*8)) | (((int)buf[4*4+2]&0xff) << (2*8)) | (((int)buf[4*4+1]&0xff) << (1*8)) | (((int)buf[4*4+0]&0xff) << (0*8));
		int y = (((int)buf[5*4+3]&0xff) << (3*8)) | (((int)buf[5*4+2]&0xff) << (2*8)) | (((int)buf[5*4+1]&0xff) << (1*8)) | (((int)buf[5*4+0]&0xff) << (0*8));
		NMAG  = (0 < x) ? y : -y;
		NBENT = (((int)buf[6*4+3]&0xff) << (3*8)) | (((int)buf[6*4+2]&0xff) << (2*8)) | (((int)buf[6*4+1]&0xff) << (1*8)) | (((int)buf[6*4+0]&0xff) << (0*8));
	}
	
	public String toString()
	{
		String str = String.format("[STAR0=%d,STAR1=%d,STARN=%d,STNUM=%d,MPROP=%d,NMAG=%d,NBENT=%d]", STAR0, STAR1, STARN, STNUM, MPROP, NMAG, NBENT);
		
		return str;
	}
}
