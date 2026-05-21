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

import lib.fit.LeastSquares.Error;


public class VoltageDistance {

//    public static double[][] xvolt = {{3.10}, {1.78}, { 1.43}, { 1.20}, {0.997}, { 0.88}, {0.79}, {0.69}, {0.622}, {0.59}, };
//    public static double[]   yinch = { 4.33,   7.87,   10.24,   12.99,   16.54,   20.47,  24.02,  32.28,  41.34,   49.21, };

    public static double[][] xvolt = {{3.20}, {2.50}, { 2.00}, };
    public static double[]   yinch = { 3.00,   6.50,    8.24,  };


    public static void main(String[] argc)
    {
    	LeastSquares ls = new Power3(LeastSquares.Search.FAST, Error.ABSOLUTE, true);
        ls.min_delta_norm = 1E-16;
        ls.min_delta      = 1E-16;
        State state = ls.ls(xvolt, yinch);

        System.out.println("delta norm = " + (state.n0 - state.n1) + ", " + "norm = "+state.n1);
        LeastSquares.print(state.c);
        System.out.println();
        
        for (int i=0; i < xvolt.length; i++) {
	        double[] x = xvolt[i];
	        double y = ls.eval(x, state.c);
//	        System.out.println("f("+x[0]+")="+y+"("+yinch[i]+") ... "+((100*(y-yinch[i])/yinch[i])));
	        System.out.printf("f(%5.3f)=%6.3f (%5.2f) ... %6.2f%%\n", x[0], y, yinch[i], ((100*(y-yinch[i])/yinch[i])));
        }
        double[] x = { 3.0 };
        double y = ls.eval(x, state.c);
        System.out.printf("f(%5.3f)=%6.3f\n", x[0], y);
        x[0] = 2.75;
        y = ls.eval(x, state.c);
        System.out.printf("f(%5.3f)=%6.3f\n", x[0], y);
    }
}
