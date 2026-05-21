package lib.math.regression;

import java.util.Random;

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


public abstract class Regression {
	public abstract int    len();
	public abstract double err();
	public abstract double eval(double[] x);
	
	protected abstract double eval(double[] x, double[] a);

	protected double[] simulated_annealing(double[][] x, double[] y, double rate, double min_rad, int iters)
	{
		int len = len();
		double[] a = zero(len);
		double best_err = norm(x, y, a);
		double rad = Double.MAX_VALUE;
		while (min_rad <= rad) {
			for (int i=0; i < iters; i++) {
				double[] c = generate_coefficients(a, rad);
				double err = norm(x, y, c);
				if (err < best_err) {
					a = c;
					best_err = err;
				}
			}
			rad = rate * rad;
		}

		return a;
	}

	public double norm(double[][] x, double[] y)
	{
		double e2_sum = 0;
		for (int i=0; i < x.length; i++) {
			double yi = eval(x[i]);
			e2_sum += (yi - y[i]) * (yi - y[i]);
		}
		double e = Math.sqrt(e2_sum);

		return e;
	}

	private double norm(double[][] x, double[] y, double[] a)
	{
		double e2_sum = 0;
		for (int i=0; i < x.length; i++) {
			double yi = eval(x[i], a);
			e2_sum += (yi - y[i]) * (yi - y[i]);
		}
		double e = Math.sqrt(e2_sum);

		return e;
	}

	private static Random r = new Random();
	private static double[] generate_coefficients(double[] a, double max)
	{
		double[] c = copy(a);
		for (int i=0; i < c.length; i++) {
			c[i] += (2 * r.nextDouble() - 1) * max;
		}

		return c;
	}

	private static double[] copy(double[] a)
	{
		double[] c = new double[a.length];
		for (int i=0; i < c.length; i++) {
			c[i] = a[i];
		}

		return c;
	}

	private static double[] zero(int len)
	{
		double[] z = new double[len];
		for (int i=0; i < z.length; i++) {
			z[i] = 0;
		}

		return z;
	}
}
