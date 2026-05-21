package lib.math.fourier;

/*******************************************************************************
 * Copyright (c) 2024 Douglas M. Pase                                          *
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


import lib.matrix.Complex;

public class ComplexFourierRowMajor extends ComplexFourier {

	private final double data[];
	private static final int REAL = 0;
	private static final int IMAG = 1;
	private final int rows = 2;
	private final int cols;
	
	public ComplexFourierRowMajor(double real[], double imag[]) throws IllegalArgumentException
	{
		if (real == null && imag == null) {
			throw new IllegalArgumentException("real and imag cannot both be null.");
		} else if (real == null) {
			cols = imag.length;
			data = new double[rows * cols];
			for (int i=0; i < imag.length; i++) {
				data[idx(i)+REAL] = 0;
				data[idx(i)+IMAG] = imag[i];
			}
		} else if (imag == null) {
			cols = real.length;
			data = new double[rows * cols];
			for (int i=0; i < real.length; i++) {
				data[idx(i)+REAL] = real[i];
				data[idx(i)+IMAG] = 0;
			}
		} else if (real.length != imag.length) {
			throw new IllegalArgumentException("real and imag must be the same length.");
		} else {
			cols = real.length;
			data = new double[rows * cols];
			for (int i=0; i < real.length; i++) {
				data[idx(i)+REAL] = real[i];
				data[idx(i)+IMAG] = imag[i];
			}
		}
	}
	
	public ComplexFourierRowMajor(double real[]) throws IllegalArgumentException
	{
		if (real == null) {
			throw new IllegalArgumentException("real cannot be null.");
		}
		
		cols = real.length;
		data = new double[rows * cols];
		for (int i=0; i < real.length; i++) {
			data[idx(i)+REAL] = real[i];
			data[idx(i)+IMAG] = 0;
		}
	}
	
	public ComplexFourierRowMajor(double cplx[][]) throws IllegalArgumentException
	{
		if (cplx == null) {
			throw new IllegalArgumentException("cplx cannot be null.");
		}
		
		cols = cplx.length;
		data = new double[rows * cols];
		for (int i=0; i < cplx.length; i++) {
			data[idx(i)+REAL] = cplx[i][REAL];
			data[idx(i)+IMAG] = cplx[i][IMAG];
		}
	}
	
	public ComplexFourierRowMajor(Complex cplx[]) throws IllegalArgumentException
	{
		if (cplx == null) {
			throw new IllegalArgumentException("cplx cannot be null.");
		}
		
		cols = cplx.length;
		data = new double[rows * cols];
		for (int i=0; i < cplx.length; i++) {
			data[idx(i)+REAL] = cplx[i].real;
			data[idx(i)+IMAG] = cplx[i].imag;
		}
	}
	
	private int idx(int i)
	{
		return i * cols;
	}
	
	public double real(int i)
	{
		return data[idx(i)+REAL];
	}

	public double imag(int i)
	{
		return data[idx(i)+IMAG];
	}

	public static void main(String[] args) 
	{
	}
}
