package lib.math.matrix;

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

// see:
// (1) https://mathworld.wolfram.com/Quaternion.html
// (2) https://math.ucr.edu/home/baez/octonions/conway_smith/
// (3) https://en.wikipedia.org/wiki/Quaternion

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class Quaternion {
	public final double r;
	public final double i;
	public final double j;
	public final double k;
	
	public static final Quaternion zero      = new Quaternion(0,0,0,0);
	public static final Quaternion one       = new Quaternion(1,0,0,0);
	public static final Quaternion minus_one = new Quaternion(-1,0,0,0);
	public static final Quaternion minus_i   = new Quaternion(0,-1,0,0);
	public static final Quaternion minus_j   = new Quaternion(0,0,-1,0);
	public static final Quaternion minus_k   = new Quaternion(0,0,0,-1);

	public static final Quaternion ZERO      = new Quaternion(0,0,0,0);
	public static final Quaternion ONE       = new Quaternion(1,0,0,0);
	public static final Quaternion I         = new Quaternion(0,1,0,0);
	public static final Quaternion J         = new Quaternion(0,0,1,0);
	public static final Quaternion K         = new Quaternion(0,0,0,1);
	public static final Quaternion MINUS_ONE = new Quaternion(-1,0,0,0);
	public static final Quaternion MINUS_I   = new Quaternion(0,-1,0,0);
	public static final Quaternion MINUS_J   = new Quaternion(0,0,-1,0);
	public static final Quaternion MINUS_K   = new Quaternion(0,0,0,-1);

	public Quaternion() 
	{
		r = i = j = k = 0;
	}

	public Quaternion(double a) 
	{
		r = a;
		i = j = k = 0;
	}

	public Quaternion(double a, double b) 
	{
		r = a;
		i = b;
		j = k = 0;
	}

	public Quaternion(double a, double b, double c) 
	{
		r = a;
		i = b;
		j = c;
		k = 0;
	}

	public Quaternion(double a, double b, double c, double d) 
	{
		r = a;
		i = b;
		j = c;
		k = d;
	}

	public Quaternion(Complex a) 
	{
		r = a.real;
		i = a.imag;
		j = 0;
		k = 0;
	}

	public Quaternion(Quaternion rhs) 
	{
		r = rhs.r;
		i = rhs.i;
		j = rhs.j;
		k = rhs.k;
	}
	
	public Quaternion neg()
	{
		return new Quaternion(-r, -i, -j, -k);
	}
	
	public Quaternion add(double rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r + rhs, i, j, k);
	}
	
	public Quaternion add(Complex rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r + rhs.real, i + rhs.imag, j, k);
	}
	
	public Quaternion add(Quaternion rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r + rhs.r, i + rhs.i, j + rhs.j, k + rhs.k);
	}
	
	public Quaternion plus(double rhs) 
	{
		return add(rhs);
	}
	
	public Quaternion plus(Complex rhs) 
	{
		return add(rhs);
	}
	
	public Quaternion plus(Quaternion rhs) 
	{
		return add(rhs);
	}
	
	public Quaternion sub(double rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r - rhs, i, j, k);
	}
	
	public Quaternion sub(Complex rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r - rhs.real, i - rhs.imag, j, k);
	}
	
	public Quaternion sub(Quaternion rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r - rhs.r, i - rhs.i, j - rhs.j, k - rhs.k);
	}
	
	public Quaternion minus(double rhs) 
	{
		return sub(rhs);
	}
	
	public Quaternion minus(Complex rhs) 
	{
		return sub(rhs);
	}
	
	public Quaternion minus(Quaternion rhs) 
	{
		return sub(rhs);
	}
	
	public Quaternion mult(double rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r * rhs, i * rhs, j * rhs, k * rhs);
	}
	
	public Quaternion mult(Complex rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		double a =   r*rhs.real - i*rhs.imag;
		double b =   r*rhs.imag + i*rhs.real;
		double c = - j*rhs.real - k*rhs.imag;
		double d = - j*rhs.imag - k*rhs.real;
		
		return new Quaternion(a, b, c, d);
	}
	
	public Quaternion mult(Quaternion rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		double a = r*rhs.r - i*rhs.i - j*rhs.j - k*rhs.k;
		double b = r*rhs.i + i*rhs.r + j*rhs.k - k*rhs.j;
		double c = r*rhs.j - i*rhs.k - j*rhs.r - k*rhs.i;
		double d = r*rhs.k - i*rhs.j - j*rhs.i - k*rhs.r;
		
		return new Quaternion(a, b, c, d);
	}

	public Quaternion times(double rhs)
	{
		return mult(rhs);
	}

	public Quaternion times(Complex rhs)
	{
		return mult(rhs);
	}

	public Quaternion times(Quaternion rhs)
	{
		return mult(rhs);
	}
	
	public Quaternion inv()
	{
		return conj().div(norm_square());
	}
	
	public Quaternion inverse()
	{
		return inv();
	}
	
	public Quaternion div(double rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r/rhs, i/rhs, j/rhs, k/rhs);
	}
	
	public Quaternion div(Complex rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return mult(new Quaternion(rhs).inv());
	}
	
	public Quaternion div(Quaternion rhs)
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return mult(rhs.inv());
	}
	
	public Quaternion divide(double rhs)
	{
		return div(rhs);
	}
	
	public Quaternion divide(Complex rhs)
	{
		return div(rhs);
	}
	
	public Quaternion divide(Quaternion rhs)
	{
		return div(rhs);
	}

	public Quaternion conj() 
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return new Quaternion(r, -i, -j, -k);
	}
	
	public Quaternion conjugate()
	{
		return conj();
	}

	public double abs() 
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return Math.sqrt(r*r + i*i + j*j + k*k);
	}
	
	public double norm()
	{
		return abs();
	}

	public double abs_square() 
	{
		// from http://mathworld.wolfram.com/Quaternion.html
		return r*r + i*i + j*j + k*k;
	}
	
	public double norm_square()
	{
		return abs_square();
	}


	/*
	 * unsure from here down...
	 */

	public double arg() 
	{
		// from http://mathworld.wolfram.com/ComplexArgument.html
		return Math.atan2(i, r);
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
	//     r = y[k] * cos(-2*pi*f*t[k]),
	//     i = y[k] * sin(-2*pi*f*t[k])
	
	// this routine computes the center of mass of the product of the samples and the reference oscillator.
	// the reference oscillator beats at f cycles per unit time, and the n samples span a total of T time units. 
	// see: 3Blue1Brown, "But what is the ComplexFourier Transform? A visual introduction.", 
	// https://www.youtube.com/watch?v=spUNpyF58BY .
	public static Quaternion fourier(double[] y, double f, double T)
	{
		double real = 0;
		double imag = 0;
		double delta = T/(y.length - 1);
		for (int k=0; k < y.length; k++) {
			double t = k * delta;
			real += y[k] * Math.sin(2.0 * Math.PI * f * t);
			imag += y[k] * Math.cos(2.0 * Math.PI * f * t);
		}
		
		real /= 0.5 * y.length;
		imag /= 0.5 * y.length;

		return new Quaternion(real, imag);
	}
	
	public static Quaternion[] fourier(double[] y, double[] f, double time)
	{
		Quaternion[] c = new Quaternion[f.length];
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
	public static double[] inverse(Quaternion[] c, double minf, double maxf, int samples, double time)
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
	

    public Quaternion add(double x, double y)
    {
		double a = r;
		double b = i;
		double c = x;
		double d = y;
	
		return new Quaternion(a + c, b + d);
    }

    
    public Quaternion sub(double x, double y)
    {
		double a = r;
		double b = i;
		double c = x;
		double d = y;

		return new Quaternion(a - c, b - d);
    }

    
    public Quaternion mult(double x, double y)
    {
		double a = r;
		double b = i;
		double c = x;
		double d = y;
	
		return new Quaternion(a*c - b*d, a*d + b*c);
    }

    
    public Quaternion div(double x, double y)
    {
		double a = r;
		double b = i;
		double c = x;
		double d = y;
		double e = c*c + d*d;
	
		return new Quaternion((a*c + b*d)/e, (b*c - a*d)/e);
    }

    // e ** (r,i) = e ** r * (cos(i), i * sin(i))
    public Quaternion exp()
    {
		double a = r;
		double b = i;
		
		return Quaternion.exp(a, b);
    }

    // e ** (r,i) = e ** r * (cos(i), i * sin(i))
    public static Quaternion exp(double real)
    {
		double a = real;
		double b = 0;
	
		return Quaternion.exp(a, b);
    }

    // e ** (r,i) = e ** r * (cos(i), i * sin(i))
    public static Quaternion exp(double real, double imag)
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
				return new Quaternion(Math.cos(b), Math.sin(b));
			}
		} else if (b == 0) {
			if (a == 1) {
				return new Quaternion(Math.E, 0);
			} else {
				return new Quaternion(Math.exp(a), 0);
			}
		}

		double r = Math.exp(a);

		return new Quaternion(r * Math.cos(b), r * Math.sin(b));
    }

    public static Quaternion fourier(double f, double t0, double t1, double[] g)
    {
		Quaternion sum = new Quaternion();
		for (int i=0; g != null && i < g.length; i++) {
		    Quaternion w = wrap(f, t0, t1, g, i);
		    sum = sum.add(w);
		}
		
		Quaternion d = sum.div(g.length);

		return new Quaternion(d.r, d.i);
    }

    public static Quaternion fourier(double f, double t0, double t1, double[] g, PrintStream out)
    {
		Quaternion sum = new Quaternion();
		for (int i=0; g != null && i < g.length; i++) {
		    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
		    Quaternion w = wrap(f, t0, t1, g, i, out);
		    sum = sum.add(w);
		    
			out.printf("%6.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f,%8.4f\n", t, g[i], w.r, w.i, w.abs(), sum.r, sum.i, sum.abs());					    
		}

		return sum.div(g.length);
    }
    
    public static Quaternion wrap(double f, double t0, double t1, double[] g, int i)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Quaternion e = Quaternion.exp(0, 2 * Math.PI * f * t);
	    Quaternion w = (new Quaternion(g[i])).mult(e);

	    return w;
    }
    
    public static Quaternion wrap(double f, double t0, double t1, double[] g, int i, PrintStream out)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Quaternion e = Quaternion.exp(0, 2 * Math.PI * f * t);
	    Quaternion w = (new Quaternion(g[i],0)).mult(e);

	    out.printf("%8.4f,%8.4f,", e.r, e.i);					    

	    return w;
    }
    
    public static Quaternion wrap(double f, double t0, double t1, Quaternion[] g, int i)
    {
	    double t = t0 + (t1 - t0) * (double) i / (double) (g.length - 1);
	    Quaternion e = Quaternion.exp(0, 2 * Math.PI * f * t);
	    Quaternion w = g[i].mult(e);

	    return w;
    }
    
    public double angle()
    {
    	return Math.atan2(i, r);
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
    	return "("+r+", "+i+")";
    }

    
    
    
	public static void main(String[] args) throws FileNotFoundException
	{
		double[] a = { 3.50, 3.00, 2.50, 2.00, };			// signal amplitude
		double[] f = { 1.50, 2.50, 3.00, 9.50, };			// signal frequency
		double[] p = { 0.00, 2.57, 3.14, 0.00, };			// signal phase/offset
		int fdn = 0;										// lower active frequency index
		int fup = 3;										// upper active frequency index

		double time = 5;									// signal duration (in time units, e.g., seconds)
		double samples_per_unit_time = 100;					// discrete samples per unit time
		int samples = (int) (time * samples_per_unit_time);	// number of discrete samples
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
		PrintStream ps1 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex2.in"));
		ps1.println("# plot [0:"+(samples/(2*time))+"][-4:4] " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:2 with lines linetype 1 title \"Real\", " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:3 with lines linetype 2 title \"Imag\", " +
				"\"..\\\\Desktop\\\\ex2.in\" using 1:4 with lines linetype 3 title \"Amplitude\"");
		ps1.println("#   freq     r     i     ampl    phase");
		for (double w=0.01; w < max_frequency; w += 0.01) {
			Quaternion c = fourier(y, w, time);
			ps1.printf("%8.4f %8.4f %8.4f %8.4f %8.4f\n", w, c.r, c.i, c.abs(), Math.atan2(c.i, c.r));
		}
		ps1.close();
		
		System.out.println("Sample size = " + samples);
		System.out.println("frequency      r      i amplitude       %     phase     diff");
		for (int j=fdn; j <= fup && j < a.length; j++) {
			Quaternion c = fourier(y, f[j], time);
			double amp = c.abs();
			double ea = 100*(amp - a[j])/a[j];
			double phase = c.phase();
			double ep = (phase - p[j]);
			System.out.printf("%9.4f %9.4f %9.4f %9.4f %6.2f%% %9.4f %8.4f\n", f[j], c.r, c.i, amp, ea, phase, ep);
		}
	}
}
