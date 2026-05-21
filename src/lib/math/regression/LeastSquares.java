package lib.math.regression;

/*******************************************************************************
 * Copyright (c) 2025 Douglas M. Pase                                          *
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


public class LeastSquares {
	// simple linear regression, y = a + bx
	// see: https://www.geeksforgeeks.org/maths/linear-regression-formula/
	public final double slope;
	public final double y_intercept;
	public final double err;
	public LeastSquares(double[] x, double[] y)
	{
		if (x == null || y == null || x.length != y.length) {
			slope       = Double.NaN;
			y_intercept = Double.NaN;
			err         = Double.NaN;
			return;
		}

		double x_sum  = 0;
		double y_sum  = 0;
		double x2_sum = 0;
		double xy_sum = 0;
		for (int i=0; i < x.length; i++) {
			x_sum  += x[i];
			y_sum  += y[i];
			x2_sum += x[i] * x[i];
			xy_sum += x[i] * y[i];
		}
		
		long n = x.length;
		double a = (y_sum * x2_sum - x_sum * xy_sum) / (n * x2_sum - x_sum * x_sum);
		double b = (n * xy_sum - x_sum * y_sum) / (n * x2_sum - x_sum * x_sum);

		slope       = b;
		y_intercept = a;

		double e2_sum = 0;
		for (int i=0; i < x.length; i++) {
			double yi = slope * x[i] + y_intercept;
			e2_sum += (yi - y[i]) * (yi - y[i]);
		}
		
		err = Math.sqrt(e2_sum);
	}
}
