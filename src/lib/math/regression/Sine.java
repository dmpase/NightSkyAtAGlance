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


// y = a[0]*sin(a[1]*x[0]+a[2]) + a[3]*sin(a[4]*x[1]+a[5] + ... + a[3n+0]*sin(a[3n+1]*x[n]+a[3n+2])
// a[3i+0] = amplitude
// a[3i+1] = frequency
// a[3i+2] = phase
public class Sine extends Regression {
	public final int      len;
	public final double[] a;
	public final double   err;

	@Override public int    len() { return len; }
	@Override public double err() { return err; }

	@Override public double eval(double[] x)
	{
		return eval(x, a);
	}

	@Override public double eval(double[] x, double[] a)
	{
		if (x == null || a == null || 3 * x.length != a.length) {
			return Double.NaN;
		}

		double y_sum = 0;
		for (int i=0; i < x.length; i++) {
			y_sum += a[3*i+0] * Math.sin(a[3*i+1] * x[i] + a[3*i+2]);
		}
		double y = y_sum;

		return y;
	}
	
	@Override public String toString()
	{
		String s = String.format("[%f", a[0]);
		for (int i=1; i < a.length; i++) {
			s = String.format("%s, %f", s, a[i]);
		}
		s = String.format("%s] err=%f", s, err);

		return s;
	}

	public Sine(double[][] x, double[] y)
	{
		if (x == null || y == null || x.length != y.length || x.length < 1 || x[0].length < 1) {
			len = 0;
			a   = null;
			err = Double.NaN;
			return;
		}

		len = 3 * x[0].length;
		a   = simulated_annealing(x, y, (1 - 1e-3), 1e-15, 5);
		err = norm(x, y);
	}
}

