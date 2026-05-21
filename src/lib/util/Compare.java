package lib.util;

/*******************************************************************************
 * Copyright (c) 2020-2025 Douglas M. Pase                                     *
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

public class Compare {

	// compare two byte arrays x and y
	public static boolean compare(byte[] x, byte[] y)
	{
		boolean ans = false;
		
		if (x == y) {
			ans = true;
		} else if (x != null && y != null && x.length == y.length) {
			ans = true;
			for (int i=0; i < x.length; i++) {
				if (x[i] != y[i]) {
					ans = false;
					break;
				}
			}
		}
		
		return ans;
	}


	// compare two byte arrays x and y
	public static boolean compare(byte[] x, int x_off, byte[] y, int y_off, int len)
	{
		boolean ans = false;
		
		if (x == y && x_off == y_off) {
			ans = true;
		} else if (x != null && y != null && (x_off + len) < x.length && (y_off + len) < y.length) {
			ans = true;
			for (int i=0; i < len; i++) {
				if (x[x_off + i] != y[y_off + i]) {
					ans = false;
					break;
				}
			}
		}
		
		return ans;
	}


	// compare two short arrays x and y
	public static boolean compare(short[] x, short[] y)
	{
		boolean ans = false;
		
		if (x == y) {
			ans = true;
		} else if (x != null && y != null && x.length == y.length) {
			ans = true;
			for (int i=0; i < x.length; i++) {
				if (x[i] != y[i]) {
					ans = false;
					break;
				}
			}
		}
		
		return ans;
	}

	// compare two int arrays x and y
	public static boolean compare(int[] x, int[] y)
	{
		boolean ans = false;
		
		if (x == y) {
			ans = true;
		} else if (x != null && y != null && x.length == y.length) {
			ans = true;
			for (int i=0; i < x.length; i++) {
				if (x[i] != y[i]) {
					ans = false;
					break;
				}
			}
		}
		
		return ans;
	}

	// compare two long arrays x and y
	public static boolean compare(long[] x, long[] y)
	{
		boolean ans = false;
		
		if (x == y) {
			ans = true;
		} else if (x != null && y != null && x.length == y.length) {
			ans = true;
			for (int i=0; i < x.length; i++) {
				if (x[i] != y[i]) {
					ans = false;
					break;
				}
			}
		}
		
		return ans;
	}
}
