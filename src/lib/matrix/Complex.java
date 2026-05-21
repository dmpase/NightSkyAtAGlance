package lib.matrix;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

// keep these references, they may have useful ideas:
// https://www.youtube.com/watch?v=spUNpyF58BY
// https://introcs.cs.princeton.edu/java/97data/TRIANGLE.java
// https://www.developer.com/java/fun-with-java-understanding-the-fast-fourier-transform-fft-algorithm/

public class Complex {
	public final double real;
	public final double imag;
	
	public static final Complex zero      = new Complex(0,0);
	public static final Complex one       = new Complex(1,0);
	public static final Complex minus_one = new Complex(-1,0);
	public static final Complex minus_i   = new Complex(0,-1);

	public static final Complex ZERO      = new Complex(0,0);
	public static final Complex ONE       = new Complex(1,0);
	public static final Complex I         = new Complex(0,1);
	public static final Complex MINUS_ONE = new Complex(-1,0);
	public static final Complex MINUS_I   = new Complex(0,-1);

	public Complex() 
	{
		real = 0;
		imag = 0;
	}

	public Complex(double a) 
	{
		real = a;
		imag = 0;
	}

	public Complex(double a, double b) 
	{
		real = a;
		imag = b;
	}

	public Complex(Complex rhs) 
	{
		real = rhs.real;
		imag = rhs.imag;
	}
	
	public Complex neg()
	{
		return new Complex(-real, -imag);
	}
	
	public Complex add(double rhs)
	{
		// from http://mathworld.wolfram.com/ComplexAddition.html
		return new Complex(real + rhs, imag);
	}
	
	public Complex add(Complex rhs)
	{
		// from http://mathworld.wolfram.com/ComplexAddition.html
		return new Complex(real + rhs.real, imag + rhs.imag);
	}
	
	public Complex plus(double rhs) 
	{
		return add(rhs);
	}
	
	public Complex plus(Complex rhs) 
	{
		return add(rhs);
	}
	
	public Complex sub(double rhs)
	{
		// from http://mathworld.wolfram.com/ComplexSubtraction.html
		return new Complex(real - rhs, imag);
	}
	
	public Complex sub(Complex rhs)
	{
		// from http://mathworld.wolfram.com/ComplexSubtraction.html
		return new Complex(real - rhs.real, imag - rhs.imag);
	}
	
	public Complex minus(double rhs) 
	{
		return sub(rhs);
	}
	
	public Complex minus(Complex rhs) 
	{
		return sub(rhs);
	}
	
	public Complex mult(double rhs)
	{
		// from http://mathworld.wolfram.com/ComplexMultiplication.html
		double a = real;
		double b = imag;
		double c = rhs;
		
		return new Complex(a * c, b * c);
	}
	
	public Complex mult(Complex rhs)
	{
		// from http://mathworld.wolfram.com/ComplexMultiplication.html
		double a = real;
		double b = imag;
		double c = rhs.real;
		double d = rhs.imag;
		
		return new Complex(a * c - b * d, a * d + b * c);
	}

	public Complex times(double rhs)
	{
		return mult(rhs);
	}

	public Complex times(Complex rhs)
	{
		return mult(rhs);
	}
	
	public Complex div(double rhs)
	{
		// from http://mathworld.wolfram.com/ComplexDivision.html
		double a = real;
		double b = imag;
		double c = rhs;
		
		return new Complex(a/c, b/c);
	}
	
	public Complex div(Complex rhs)
	{
		// from http://mathworld.wolfram.com/ComplexDivision.html
		double a = real;
		double b = imag;
		double c = rhs.real;
		double d = rhs.imag;
		
		return new Complex((a*c+b*d)/(c*c+d*d), (b*c-a*d)/(c*c+d*d));
	}
	
	public Complex divide(double rhs)
	{
		return div(rhs);
	}
	
	public Complex divide(Complex rhs)
	{
		return div(rhs);
	}

	public Complex conj() 
	{
		// from http://mathworld.wolfram.com/ComplexDivision.html
		return new Complex(real, -imag);
	}
	
	public Complex conjugate()
	{
		return conj();
	}

	public double abs() 
	{
		// from http://mathworld.wolfram.com/AbsoluteValue.html
		return Math.sqrt(real*real + imag*imag);
	}

	public double abs_square() 
	{
		// from http://mathworld.wolfram.com/AbsoluteSquare.html
		return real*real + imag*imag;
	}

	// the argument of a complex number z is the angle in radians of
	// z with the real axis
	public double arg() 
	{
		// from http://mathworld.wolfram.com/ComplexArgument.html
		return Math.atan2(imag, real);
	}

	public double phase() 
	{
		// from http://mathworld.wolfram.com/ComplexArgument.html
		return arg();
	}

	
	// (1) a+bi = r*e**i*theta -> radius=r, angle=theta (radians), a = r*cos(theta), b = r*sin(theta)
	// (2) x*y = (e**ln(x))*(e**ln(y)) = e**(ln(x)+ln(y))
	// (3) a+bi = e**(R+t*i) -> radius r=e**R, angle = t (radians), a = r*cos(t), b = r*sin(t)
	// (4) ComplexFourier (1/N)*sum(g(t(k))*e**(-2*pi*i*f*t(k))), t(k) = time for sample k, f = frequency, g(t(k)) = amplitude at time t(k)

	// y is a sequence of uniformly spaced amplitude samples
	// f is the frequency given in cycles per unit time (e.g., Hz or cycles per second)
	// T is elapsed time of the set of samples, given in units of time (e.g., seconds);
	
	// note: y[0] occurs at time 0, y[n-1] occurs at time T, y[k] occurs at time (k*T)/n.
	// a sample k lasts for delta=T/n time units and occurs at time t[k]=k*delta=k*T/n.
	// its contribution to the center of mass is:
	//     y[k] * e**(-2*pi*i*f*t[k]) ->
	//     real = y[k] * cos(-2*pi*f*t[k]),
	//     imag = y[k] * sin(-2*pi*f*t[k])
	
	// this routine computes the center of mass of the product of the samples and the reference oscillator.
	// the reference oscillator beats at f cycles per unit time, and the n samples span a total of T time units. 
	// see: 3Blue1Brown, "But what is the ComplexFourier Transform? A visual introduction.", 
	// https://www.youtube.com/watch?v=spUNpyF58BY .
	public static Complex fourier(double[] y, double f, double time)
	{
		double real = 0;
		double imag = 0;
		double delta = time/(y.length - 1);
		for (int k=0; k < y.length; k++) {
			double t = k * delta;
			real += y[k] * Math.sin(2.0 * Math.PI * f * t);
			imag += y[k] * Math.cos(2.0 * Math.PI * f * t);
		}
		
		real /= 0.5 * y.length;
		imag /= 0.5 * y.length;

		return new Complex(real, imag);
	}
	
	public static Complex[] fourier(double[] y, double[] f, double time)
	{
		Complex[] c = new Complex[f.length];
		for (int k=0; k < c.length; k++) {
			c[k] = fourier(y, f[k], time);
		}

		return c;
	}

	// c is the complex output of the ComplexFourier transform
	// minf is the minimum frequency represented by c (i.e., f at c[0])
	// maxf is the maximum frequency represented by c (i.e., f at c[c.length-1])
	// samples is the number of samples to be included in the result (y.length)
	// time is the signal duration, the number of time units the samples represent
	// see: https://www.youtube.com/watch?v=tUoE_pyAZn4 (at time 0:55).
	public static double[] inverse(Complex[] c, double minf, double maxf, int samples, double time)
	{
		double[] y = new double[samples];
		for (int j=0; j < y.length; j++) {
			y[j] = 0;
		}
		
		for (int k=0; k < c.length; k++) {
			double f     = minf + k * maxf/(c.length - 1);
			double amp   = c[k].abs();
			double phase = c[k].phase();
			for (int j=0; j < y.length; j++) {
				double theta = j*time/(samples - 1);
				y[j] += amp * Math.sin( 2.0 * Math.PI * f * theta + phase );
			}
		}
		
		return y;
	}
	

    public Complex add(double x, double y)
    {
		double a = real;
		double b = imag;
		double c = x;
		double d = y;
	
		return new Complex(a + c, b + d);
    }

    
    public Complex sub(double x, double y)
    {
		double a = real;
		double b = imag;
		double c = x;
		double d = y;

		return new Complex(a - c, b - d);
    }

    
    public Complex mult(double x, double y)
    {
		double a = real;
		double b = imag;
		double c = x;
		double d = y;
	
		return new Complex(a*c - b*d, a*d + b*c);
    }

    
    public Complex div(double x, double y)
    {
		double a = real;
		double b = imag;
		double c = x;
		double d = y;
		double e = c*c + d*d;
	
		return new Complex((a*c + b*d)/e, (b*c - a*d)/e);
    }

    // e ** (real,imag) = e ** real * (cos(imag), i * sin(imag))
    public Complex exp()
    {
		double a = real;
		double b = imag;
		
		return Complex.exp(a, b);
    }

    // e ** (real,imag) = e ** real * (cos(imag), i * sin(imag))
    public static Complex exp(double real)
    {
		double a = real;
		double b = 0;
	
		return Complex.exp(a, b);
    }

    // e ** (real,imag) = e ** real * (cos(imag), i * sin(imag))
    public static Complex exp(double real, double imag)
    {
		double a = real;
		double b = imag;
		
		if (a == 0) {
			if (b == 0) {
				return ONE;
			} else if (b == Math.PI/2) {
				return I;
			} else if (b == Math.PI) {
				return MINUS_ONE;
			} else if (b == 3*Math.PI/2) {
				return MINUS_I;
			} else if (b == 2*Math.PI) {
				return ONE;
			} else if (b == -Math.PI/2) {
				return MINUS_I;
			} else if (b == -Math.PI) {
				return MINUS_ONE;
			} else if (b == -3*Math.PI/2) {
				return I;
			} else if (b == -2*Math.PI) {
				return ONE;
			} else {
				return new Complex(Math.cos(b), Math.sin(b));
			}
		} else if (b == 0) {
			if (a == 1) {
				return new Complex(Math.E, 0);
			} else {
				return new Complex(Math.exp(a), 0);
			}
		}

		double r = Math.exp(a);

		return new Complex(r * Math.cos(b), r * Math.sin(b));
    }

    public static Complex fourier(double f, double t0, double t1, double[] g)
    {
		Complex sum = new Complex();
		for (int i=0; g != null && i < g.length; i++) {
		    Complex w = wrap(f, t0, t1, g, i);
		    sum = sum.add(w);
		}
		
		Complex d = sum.div(g.length);

		return new Complex(d.real, d.imag);
    }

    public static Complex fourier(double f, double t0, double t1, double[] g, PrintStream out)
    {
		Complex sum = new Complex();
		for (int i=0; g != null && i < g.length; i++) {
		    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
		    Complex w = wrap(f, t0, t1, g, i, out);
		    sum = sum.add(w);
		    
			out.printf("%6.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f\n", t, g[i], w.real, w.imag, w.abs(), sum.real, sum.imag, sum.abs());					    
		}

		return sum.div(g.length);
    }
    
    public static Complex wrap(double f, double t0, double t1, double[] g, int i)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Complex e = Complex.exp(0, 2 * Math.PI * f * t);
	    Complex w = (new Complex(g[i])).mult(e);

	    return w;
    }
    
    public static Complex wrap(double f, double t0, double t1, double[] g, int i, PrintStream out)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Complex e = Complex.exp(0, 2 * Math.PI * f * t);
	    Complex w = (new Complex(g[i],0)).mult(e);

	    out.printf("%8.4f,%8.4f,", e.real, e.imag);					    

	    return w;
    }
    
    public static Complex wrap(double f, double t0, double t1, Complex[] g, int i)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Complex e = Complex.exp(0, 2 * Math.PI * f * t);
	    Complex w = g[i].mult(e);

	    return w;
    }
    
    public double angle()
    {
    	return Math.atan2(imag, real);
    }

    public void print(PrintStream out)
    {
		out.print(toString());
    }

    public void println(PrintStream out)
    {
		out.println(toString());
    }
    
    public String toString()
    {
    	return "("+real+", "+imag+")";
    }

    
    
    
	public static void main(String[] args) throws FileNotFoundException
	{
		double[] a = { 3.50, 3.00, 2.50, 2.00, };			// signal amplitude
		double[] f = { 1.50, 2.50, 3.00, 9.50, };			// signal frequency
		double[] p = { 0.00, 2.57, 3.14, 0.00, };			// signal phase/offset
		int fdn = 0;										// lower active frequency index
		int fup = 3;										// upper active frequency index

		double time = 20;									// signal duration (in time units, e.g., seconds)
		double samples_per_unit_time = 1000;				// discrete samples per unit time
		int samples = (int) (time * samples_per_unit_time);	// number of discrete samples
		@SuppressWarnings("unused")
		double max_frequency = samples/(2*time);			// max detectable frequency is samples/(2*time)
		double[] y = new double[samples];					// signal samples

		PrintStream ps0 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex1.in"));
		ps0.println("# plot [0:"+time+"][-10:10] \"..\\\\Desktop\\\\ex1.in\" with lines linetype 1");
		ps0.println("#   freq     ampl");
		for (int k=0; k < samples; k++) {
			double theta = k*time/(samples - 1);
			y[k] = 0;
			for (int j=fdn; j <= fup && j < a.length; j++) {
				y[k] += a[j] * Math.sin( 2.0 * Math.PI * f[j] * theta + p[j] );
			}
			ps0.printf("%8.4f %8.4f\n", theta, y[k]);
		}
		ps0.close();
		
		// w is the winding frequency, that is, the frequency you are testing
		// against the signal samples
		/*
		PrintStream ps1 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex2.in"));
		ps1.println("# plot [0:"+(samples/(2*time))+"][-4:4] " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:2 with lines linetype 1 title \"Real\", " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:3 with lines linetype 2 title \"Imag\", " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:4 with lines linetype 3 title \"Amplitude\"");
		ps1.println("#   freq     real     imag     ampl    phase");
	
		long start = System.nanoTime();
		for (double w=0.01; w < max_frequency; w += 0.01) {
			@SuppressWarnings("unused")
			Complex c = fourier(y, w, time);
		}
		long finish = System.nanoTime();
		System.out.printf("Compute time (ns) = %d, sec = %f\n", finish-start, (double)(finish-start)/1000000000.0);

		for (double w=0.01; w < max_frequency; w += 0.01) {
			Complex c = fourier(y, w, time);
			ps1.printf("%8.4f %8.4f %8.4f %8.4f %8.4f\n", w, c.real, c.imag, c.abs(), Math.atan2(c.imag, c.real));
		}
		ps1.close();
		*/
		
		System.out.println("Sample size = " + samples);
		System.out.println("frequency      real      imag amplitude       %     phase     diff");
		for (int j=fdn; j <= fup && j < a.length; j++) {
			Complex c = fourier(y, f[j], time);		// samples, frequency, time (scale in seconds)
			double amp = c.abs();
			double ea = 100*(amp - a[j])/a[j];
			double phase = c.phase();
			double ep = (phase - p[j]);
			System.out.printf("%9.4f %9.4f %9.4f %9.4f %6.2f%% %9.4f %8.4f\n", f[j], c.real, c.imag, amp, ea, phase, ep);
		}
	}
}
