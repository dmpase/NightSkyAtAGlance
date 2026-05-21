package lib.fit;

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


public class Power2 extends LeastSquares {

    public Power2()
    {
	super();
    }

    public Power2(Search search)
    {
        super(search);
    }

    public Power2(Search search, Error error)
    {
        super(search, error);
    }

    public Power2(Search search, Error error, boolean neg_coeff_ok)
    {
        super(search, error, neg_coeff_ok);
    }
    
    double  max_power = 10;

    // evaluate y, given x and model coefficients c
    // y = c0*x0**c1 + c2*x1**c3 + ...

    //          c1        c3
    // y = c0*x0   + c2*x1   + ... 

    public double eval(double[] x, double[] c) 
    {
        double value = 0;

        for (int i=0; i < x.length; i++) {
            if (c[2*i] < 0 || max_power < Math.abs(c[2*i+1])) {
                return Double.POSITIVE_INFINITY;
            }
            value += c[2*i] * Math.pow(x[i], c[2*i+1]);
        }

        value = (Double.isNaN(value)     ) ? Double.POSITIVE_INFINITY : value;
        value = (Double.isInfinite(value)) ? Double.POSITIVE_INFINITY : value;

        return value;
    }

                    // how many coefficients do we need?
    public int c_len(double[] x)
    {
        return 2*x.length;
    }
}
