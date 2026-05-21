package lib.math.matrix;

/*******************************************************************************
 * Copyright (c) 1988-2024 Douglas M. Pase                                     *
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


import java.io.FileNotFoundException;

import lib.util.QueueInt;

public class Signal {
	
	public final double frequency;
	public final double amplitude;
	public final double phase;

	public Signal(double f, double a, double p) 
	{
		frequency = f;
		amplitude = a;
		phase     = p;
	}

	public Signal(double[] v) 
	{
		frequency = v[0];
		amplitude = v[1];
		phase     = v[2];
	}
	
	public static double[] frequency_to_time(double sample_period, double samples_per_unit_time, Signal[] signal)
	{
		int sample_count = (int) (sample_period * samples_per_unit_time);	// number of discrete samples
		
		return frequency_to_time(sample_period, sample_count, signal);
	}

	public static double[] frequency_to_time(double sample_period, int sample_count, Signal[] signal)
	{
		double[] sample = null;						// signal samples
		
		if (0 < sample_period && 0 < sample_count) {
			sample = new double[sample_count];
			for (int i=0; i < sample_count; i++) {
				double theta = 2.0 * Math.PI * i * sample_period / (sample_count - 1);
				sample[i] = 0;
				for (int j=0; j < signal.length; j++) {
					sample[i] += signal[j].amplitude * Math.sin( signal[j].frequency * theta + signal[j].phase );
				}
			}
		}

		return sample;
	}
	
	public static Signal[] time_to_frequency_dft(double[] samples, double sample_period, double epsilon)
	{
		Signal[] signals = null;
		
		if (samples != null && 0 < sample_period) {
			Complex[] dft = dft(samples);
			int sample_count = samples.length;
			
			
			/*
			PrintStream ps1 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex1.in"));
			ps1.printf("# plot [0:%d][-500:500] \"..\\\\Desktop\\\\ex1.in\" using 1:2 with lines linetype 1 title \"real\", \"..\\\\Desktop\\\\ex1.in\" using 1:3 with lines linetype 2 title \"imag\", \"..\\\\Desktop\\\\ex1.in\" using 1:4 with lines linetype 3 title \"abs\"\n", ft.length);
			ps1.println("#index     real     imag      abs");
			for (int i=0; i < ft.length; i++) {
				ps1.printf("%6d %8.4f %8.4f %8.4f\n", i, ft[i].real, ft[i].imag, ft[i].abs());
			}
			ps1.close();
			*/

			
			double[] len = new double[sample_count];
			for (int i=0; i < sample_count; i++) {
				dft[i] = dft[i].div((double)sample_count/2);
				len[i] = dft[i].abs();
			}
			
			int[] idx = find_local_max(len, epsilon);
			
			if (idx != null) {
				signals = new Signal[idx.length];
				double samples_per_unit_time = samples.length / sample_period;
				for (int i=0; i < idx.length; i++) {
					double frequency = samples_per_unit_time * idx[i] / (sample_count - 1);
					Complex c = Complex.fourier(samples, frequency, sample_period);		// samples, frequency, time (scale in seconds)
					double amplitude = c.abs();
					double phase = c.phase();
					
					if (frequency == 0) {
						amplitude /= 2;
					}
					
					signals[i] = new Signal(frequency, amplitude, phase);
				}
			}
		}
		
		return signals;
	}
    
    // Discrete Fourier Transform, inspired by 
    // C Language Algorithms for Digital Signal Processing, 
    // by Paul M. Embree and Bruce Kimble,
    // (c) 1991, Prentice Hall
    // original source located at: https://github.com/jfargentino/cdsp
    public static Complex[] dft(Complex[] data_in)
    {
    	int N = data_in.length;
    	Complex[] data_out = new Complex[N];
    	Complex[] cf = new Complex[N];
    	double arg = 2 * Math.PI / N;
        for (int i=0 ; i < N ; i++) {
            cf[i] = new Complex(Math.cos(arg*i), - Math.sin(arg*i));
        }

        for (int i=0; i < N; i++) {
        	data_out[i] = new Complex(data_in[0]);
        	for (int j=1; j < N; j++) {
            	int p = (int)((long)((long)j*(long)i) % N);
        		double real = data_out[i].real + data_in[j].real * cf[p].real - data_in[j].imag * cf[p].imag;
        		double imag = data_out[i].imag + data_in[j].real * cf[p].imag + data_in[j].imag * cf[p].real;
        		data_out[i] = new Complex(real, imag);
        	}
        }
        
        return data_out;
    }
    
    public static Complex[] dft(double[] real)
    {
    	Complex[] cplx = new Complex[real.length];
    	
    	for (int i=0; real != null && i < real.length; i++) {
    		cplx[i] = new Complex(real[i]);
    	}

    	return dft(cplx);
    }

    public static Complex[] idft(Complex[] data_in)
    {
    	int N = data_in.length;
    	Complex[] data_out = new Complex[N];
    	Complex[] cf = new Complex[N];
    	double arg = 2 * Math.PI / N;
        for (int i=0 ; i < N ; i++) {
            cf[i] = new Complex(Math.cos(arg*i)/N, Math.sin(arg*i)/N);
        }
    	
        for (int i=0; i < N; i++) {
        	data_out[i] = new Complex(data_in[0].real * cf[0].real, data_in[0].imag * cf[0].real);
        	for (int j=1; j < N; j++) {
            	int p = (int)((long)(j*i) % N);
        		double real = data_out[i].real + data_in[j].real * cf[p].real - data_in[j].imag * cf[p].imag;
        		double imag = data_out[i].imag + data_in[j].real * cf[p].imag + data_in[j].imag * cf[p].real;
        		data_out[i] = new Complex(real, imag);
            }
        }
    	
    	return data_out;
    }
    
    public static int[] find_local_max(double[] x, double epsilon)
    {
    	QueueInt queue = new QueueInt();

    	double max = Double.NEGATIVE_INFINITY;
		int    idx = Integer.MIN_VALUE;

    	boolean recorded = true;
    	for (int i=0; i < x.length/2; i++) {
    		if (x[i] <= epsilon) {
    			// zero
    			if (! recorded) {
    				queue.append(idx);
    				recorded = true;
    				max = Double.NEGATIVE_INFINITY;
    			}
    		} else if (max < x[i]) {
    			// on our way up
    			max = x[i];
    			idx = i;
    			recorded = false;
    		} else {
    			// on our way down
    		}
    	}
    	
    	int len = queue.length();
    	int[] result = null;
    	if (0 < len) {
    		result = new int[len];
	    	for (int i=0; i < len; i++) {
	    		result[i] = queue.remove();
	    	}
    	}
    	
    	return result;
    }
    
    public static Complex[] pfft(double[] r)
    {
    	Complex[] y = null;
    	
    	if (r != null) {
    		int len = (Integer.bitCount(r.length) == 1) ? r.length : 2 * Integer.highestOneBit(r.length);
    		Complex[] x = new Complex[len];

    		// copy the original c into x
    		for (int i=0; i < r.length; i++) {
    			x[i] = new Complex(r[i]);
    		}
    		
    		// zero pad the remainder
    		for (int i=r.length; i < x.length; i++) {
    			x[i] = new Complex(0);
    		}
    		
    		y = pfft(x, false);
    	}

    	return y;
    }
    
    public static Complex[] pfft(Complex[] r) 
    {
    	return pfft(r, true);
    }

    // Princeton Java TRIANGLE routine, based on Cooley and Tukey.
    // c    - array of complex input samples
    // copy - copy (preserve) the input samples
    //
    // note: if copy == false and the number of samples is not an 
    // integer power of two, the array will be rounded up to the 
    // next suitable size and zero padded.
    //
    // if copy == true, the number of samples in the array must be 
    // an integer power of two or an IllegalArgumentException will
    // be raised.
    //
    // https://introcs.cs.princeton.edu/java/97data/TRIANGLE.java.html
    public static Complex[] pfft(Complex[] c, boolean copy) 
    {
        // base case
        if (c.length == 1) {
        	return new Complex[] { c[0] };
        }
        
		Complex[] x = null;
        if (copy) {
        	// we need to make a copy of the input array
            int len = (Integer.bitCount(c.length) == 1) ? c.length : 2 * Integer.highestOneBit(c.length);
    		x = new Complex[len];

    		// copy the original c into x
    		for (int i=0; i < c.length; i++) {
    			x[i] = new Complex(c[i]);
    		}
    		
    		// zero pad the remainder
    		for (int i=c.length; i < x.length; i++) {
    			x[i] = new Complex(0);
    		}
        } else {
            // radix 2 Cooley-Tukey TRIANGLE
            if (Integer.bitCount(c.length) != 1) {
                throw new IllegalArgumentException("n is not a power of 2");
            }
            
            x = c;
        }
		
    	int n = x.length;

        // compute TRIANGLE of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] evenFFT = pfft(even, false);

        // compute TRIANGLE of odd terms
        Complex[] odd  = even;  // reuse the array (to avoid n log n space)
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddFFT = pfft(odd, false);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
        }

        return y;
    }
	
	public static Signal[] time_to_frequency_pfft(double[] samples, double sample_period, double epsilon)
	{
		Signal[] signals = null;
		
		if (samples != null && 0 < sample_period) {
			Complex[] pfft = pfft(samples);
			int sample_count = samples.length;
			
			
			/*
			PrintStream ps1 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex1.in"));
			ps1.printf("# plot [0:%d][-500:500] \"..\\\\Desktop\\\\ex1.in\" using 1:2 with lines linetype 1 title \"real\", \"..\\\\Desktop\\\\ex1.in\" using 1:3 with lines linetype 2 title \"imag\", \"..\\\\Desktop\\\\ex1.in\" using 1:4 with lines linetype 3 title \"abs\"\n", ft.length);
			ps1.println("#index     real     imag      abs");
			for (int i=0; i < ft.length; i++) {
				ps1.printf("%6d %8.4f %8.4f %8.4f\n", i, pfft[i].real, pfft[i].imag, pfft[i].abs());
			}
			ps1.close();
			*/

			
			double[] len = new double[sample_count];
			for (int i=0; i < sample_count; i++) {
				pfft[i] = pfft[i].div((double)sample_count/2);
				len[i]  = pfft[i].abs();
			}
			
			int[] idx = find_local_max(len, epsilon);
			
			if (idx != null) {
				signals = new Signal[idx.length];
				double samples_per_unit_time = samples.length / sample_period;
				for (int i=0; i < idx.length; i++) {
					double frequency = samples_per_unit_time * idx[i] / (sample_count - 1);
					Complex c = Complex.fourier(samples, frequency, sample_period);
					double amplitude = c.abs();
					double phase = c.phase();
		
					if (frequency == 0) {
						amplitude /= 2;
					}
					
					signals[i] = new Signal(frequency, amplitude, phase);
				}
			}
		}
		
		return signals;
	}
	
	public static double[] rectangle(int width, double[] y0)
	{
		double[] y1 = null;
		
		if (y0 != null) {
			y1 = rectangle(width, y0.length - width, y0);
		}
		
		return y1;
	}
	
	public static double[] rectangle(int start, int end, double[] y0)
	{
		double[] y1 = null;
		
		if (y0 != null) {
			y1 = new double[y0.length];
			for (int i=0; i < y0.length; i++) {
				if (i < start || end < i) {
					y1[i] = 0;
				} else {
					y1[i] = y0[i];
				}
			}
		}

		return y1;
	}
	
	public static double[] triangle(double[] y0)
	{
		double[] y1 = null;

		if (y0 != null) {
			double N = y0.length;
			y1 = new double[(int) N];
			for (int i=0; i < N; i++) {
				double factor = 1 - 2 * Math.abs(i - N/2) / N;
				y1[i] = factor * y0[i];
			}
		}

		return y1;
	}
	
	public static double L2(double[] y0, double[] y1)
	{
		double result = 0;
		
		if (y0 != null && y1 != null && y0.length == y1.length) {
			for (int i=0; i < y0.length; i++) {
				result += (y1[i] - y0[i]) * (y1[i] - y0[i]);
			}
			
			result = Math.sqrt(result);
		} else {
			result = 1.0/0.0;		// positive infinity
		}
		
		return result;
	}
	

	enum Transform { DFT, FFT, }
	enum Window    { NULL, RECTANGLE, TRIANGLE, }

	public static void main(String[] args) throws FileNotFoundException 
	{
		double A0 = 27.5; //A0 = 32;
		Signal[] signals = {
				//         frequency amplitude         phase
				new Signal((1>> 1)*A0,     7.50, -4*Math.PI/8),	//     0   Hz
				new Signal((1<< 0)*A0,     7.00,  0*Math.PI/8),	//    27.5 Hz
				new Signal((1<< 1)*A0,     6.50,  1*Math.PI/8),	//    55   Hz
				new Signal((1<< 2)*A0,     6.00,  2*Math.PI/8),	//   110   Hz
				new Signal((1<< 3)*A0,     5.50,  3*Math.PI/8),	//   220   Hz
				new Signal((1<< 4)*A0,     5.00,  4*Math.PI/8),	//   440   Hz
				new Signal((1<< 5)*A0,     4.50,  5*Math.PI/8),	//   880   Hz
				new Signal((1<< 6)*A0,     4.00,  6*Math.PI/8),	//  1760   Hz
				new Signal((1<< 7)*A0,     3.50,  7*Math.PI/8),	//  3520   Hz
				new Signal((1<< 8)*A0,     3.00,  8*Math.PI/8),	//  7040   Hz
				new Signal((1<< 9)*A0,     2.50,  9*Math.PI/8),	// 14040   Hz
				new Signal((1<<10)*A0,     2.00, 10*Math.PI/8),	// 28080   Hz
		};

		Transform transform = Transform.FFT;
		Window    window    = Window.NULL;
		double    noise     = 0.00;

		double sample_period = 2;
		double samples_per_unit_time = 1 << 20;
		double[] y0 = frequency_to_time(sample_period, samples_per_unit_time, signals);

		// add noise to the signal
		for (int i=0; 0 < noise && i < y0.length; i++) {
			y0[i] += (2*Math.random() - 1) * noise;
		}
		
		// apply a window to the data
		switch (window) {
		case NULL:
			break;
		case RECTANGLE:
			y0 = rectangle(10, y0.length-10, y0);
			break;
		case TRIANGLE:
			y0 = triangle(y0);
			break;
		}

		/*
		long s0 = System.nanoTime();
		PrintStream ps0 = new PrintStream(new File(System.getProperty("user.home") + "\\Desktop\\ex0.in"));
		ps0.printf("# plot [0:%f][-10:10] \"..\\\\Desktop\\\\ex0.in\" with lines linetype 1\n", sample_period);
		ps0.println("#   time     ampl");
		for (int i=0; i < y.length; i++) {
			double theta = i * sample_period/(y.length - 1);
			ps0.printf("%8.4f %8.4f\n", theta, y[i]);
		}
		ps0.close();
		long f0 = System.nanoTime();
		System.out.printf("%s: %d: ps0 time is %.2f seconds\n", CLASS(), LINE(), (f0-s0)/1000000000.0);
		*/

		long s1 = System.nanoTime();
		Signal[] analysis = null;
		switch (transform) {
		case DFT:
			analysis = time_to_frequency_dft(y0, sample_period, 0.01);
			break;
		case FFT:
			analysis = time_to_frequency_pfft(y0, sample_period, 0.01);
			break;
		}
		long f1 = System.nanoTime();

		double max_frequency = y0.length/(2*sample_period);		// max detectable frequency is samples/(2*time)
		System.out.printf("sample period = %.2f sec, samples/sec = %.0f, total samples = %d\n", sample_period, samples_per_unit_time, y0.length);
		System.out.printf("max detectable frequency = %.0f Hz, compute time = %.3f sec\n", max_frequency, (double)(f1-s1)/1000000000.0);

		System.out.printf("%2s %10s %10s %10s %10s %10s %10s %10s %10s %10s %14s\n", "i", "frequency", "original", "%", "amplitude", "original", "%", "phase", "original", "%", "samples/cycle");
		for (int i=0; analysis != null && i < analysis.length && i < signals.length; i++) {
			double phase = (Math.PI < signals[i].phase) ? signals[i].phase - 2*Math.PI : signals[i].phase;
			double samples_per_cycle = samples_per_unit_time / signals[i].frequency;
			System.out.printf("%2d %10.4f %10.4f %10.4f %10.4f %10.4f %10.4f %10.4f %10.4f %10.4f %14.4f\n", i,
					analysis[i].frequency, signals[i].frequency, 100 * (analysis[i].frequency - signals[i].frequency) / signals[i].frequency,
					analysis[i].amplitude, signals[i].amplitude, 100 * (analysis[i].amplitude - signals[i].amplitude) / signals[i].amplitude, 
					analysis[i].phase,     phase,                100 * (analysis[i].phase     - phase               ) / phase,
					samples_per_cycle);
		}
		
		double[] y1 = frequency_to_time(sample_period, samples_per_unit_time, signals);
		System.out.printf("%f\n", 100*L2(y0, y1)/y0.length);
	}
	

	public static final int LINE()
	{
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	public static final String FILE()
	{
		return Thread.currentThread().getStackTrace()[2].getFileName();
	}
	
	public static final String CLASS()
	{
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}
	
	public static final String METHOD()
	{
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
}
