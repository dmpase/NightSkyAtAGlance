package lib.math.misc;

/*******************************************************************************
 * Copyright (c) 2021 Douglas M. Pase                                          *
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


public class Round {

	public static final double round_to_zero(double x)
	{
		return (x < 0) ? Math.ceil(x) : Math.floor(x);
	}


	public static final double round_to_pos_inf(double x)
	{
		return Math.ceil(x);
	}


	public static final double round_to_neg_inf(double x)
	{
		return Math.floor(x);
	}


	public static final double round_to_nearest(double x)
	{
		double nearest = 0;
		
		if (0 == x) {
			nearest = 0;
		} else if (0 < x) {
			double value = x;
			double frac = FRAC(value);
			if (frac < 0.5) {
				nearest = round_to_zero(value);
			} else if (frac == 0.5) {
				if (((int) value & 1) == 0) {
					nearest = round_to_zero(value);
				} else {
					nearest = round_to_pos_inf(value);
				}
			} else {
				nearest = round_to_pos_inf(value);
			}
		} else {
			double value = Math.abs(x);
			double frac = FRAC(value);
			if (frac < 0.5) {
				nearest = round_to_zero(value);
			} else if (frac == 0.5) {
				if (((int) value & 1) == 1) {
					nearest = round_to_zero(value);
				} else {
					nearest = round_to_pos_inf(value);
				}
			} else {
				nearest = round_to_pos_inf(value);
			}
			nearest = -nearest;
		}

		return nearest;
	}
	
	
	public static final double fix(double x)
	{
		return round_to_zero(x);
	}
	
	
	public static final double integer(double x)
	{
		return round_to_neg_inf(x);
	}
	
	
	public static final double FIX(double x)
	{
		return round_to_zero(x);
	}
	
	
	public static final double INT(double x)
	{
		return round_to_neg_inf(x);
	}
	
	
	public static final double ABS(double x)
	{
		return Math.abs(x);
	}
	
	
	public static final double FRAC(double x)
	{
		return Math.abs(x - FIX(x));
	}
	
	
	public static final double round(double x)
	{
		return round_to_nearest(x);
	}
	
	
	public static final double ROUND(double x)
	{
		return round_to_nearest(x);
	}
	
	
	public static final double MOD(double x, double y)
	{
		double remainder = x - ((int)(x/y)*y);
		remainder = (remainder < 0) ? remainder + y : remainder;

		return remainder;
	}

	
	public static final double radians(double degrees)
	{
		return Math.toRadians(degrees);
	}

	
	public static final double degrees(double radians)
	{
		return Math.toDegrees(radians);
	}
	
	public static void main(String[] args)
	{
		System.out.println(ROUND( 2.5));
		System.out.println(ROUND( 1.8));
		System.out.println(ROUND( 1.5));
		System.out.println(ROUND( 1.4));
		System.out.printf("%.16f\n", ROUND( 0.0));
		System.out.println(ROUND(-1.4));
		System.out.println(ROUND(-1.5));
		System.out.println(ROUND(-1.8));
		System.out.println(ROUND(-2.5));
	}
}
