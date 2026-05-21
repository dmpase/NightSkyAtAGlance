package lib.util;

/*******************************************************************************
 * Copyright (c) 1988-2021 Douglas M. Pase                                     *
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


import java.util.Random;

public class Permute {
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

	public static int[] permute(int seed, int[] array, int max)
	{
		int[] result = null;
		
		if (array != null) {
			result = new int[array.length];
			for (int i=0; i < array.length; i++) {
				result[i] = array[i];
			}

			Random r = new Random(seed);
			for (int i=0; i < result.length && i < max; i++) {
				// compute a random index in the range of 0 <= index < string.length()
				int idx = r.nextInt(result.length);
				idx = idx % result.length;
				idx = (0 < idx) ? idx : 0;

				// swap the current value with the random value
				int tmp = result[idx];
				result[idx] = result[i];
				result[i]   = tmp;
			}
		}
		
		return result;
	}

	public static int[] permute(Random r, int[] array, int max)
	{
		int[] result = null;
		
		if (array != null) {
			result = new int[array.length];
			for (int i=0; i < array.length; i++) {
				result[i] = array[i];
			}

			for (int i=0; i < result.length && i < max; i++) {
				// compute a random index in the range of 0 <= index < string.length()
				int idx = r.nextInt(result.length);
				idx = idx % result.length;
				idx = (0 < idx) ? idx : 0;

				// swap the current value with the random value
				int tmp = result[idx];
				result[idx] = result[i];
				result[i]   = tmp;
			}
		}
		
		return result;
	}


	public static <T> void permute(Random r, T[] array, T[] result)
	{
		if (array != null && result != null) {
			for (int i=0; i < array.length; i++) {
				result[i] = array[i];
			}

			for (int i=0; i < result.length; i++) {
				// compute a random index in the range of 0 <= index < string.length()
				int idx = r.nextInt(result.length);
				idx = idx % result.length;
				idx = (0 < idx) ? idx : 0;

				// swap the current value with the random value
				T tmp = result[idx];
				result[idx] = result[i];
				result[i]   = tmp;
			}
		}
	}

	public static void permute(int seed, Object[] array)
	{
		Random r = new Random(seed);
		permute(r, array);
	}

	public static void permute(Random r, Object[] array)
	{
		if (array != null && r != null) {
			for (int i=0; i < array.length; i++) {
				// compute a random index in the range of 0 <= index < array.length
				int idx = r.nextInt(array.length);
				idx = idx % array.length;
				idx = (0 < idx) ? idx : 0;

				// swap the current value with the random value
				Object tmp = array[idx];
				array[idx] = array[i];
				array[i]   = tmp;
			}
		}
	}
}
