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

import java.util.*;

public abstract class LeastSquares {
    
    public enum Search {                               // each search tests the corners and faces of a cube
        FAST,                                          // one cube, radius = delta
        DEEP,                                          // uses cube_count concentric cubes
        GENETIC,                                       // one cube, radius = random()*delta
    }
    
    public enum Error {                                // error minimization function to be used
        ABSOLUTE,                                      // absolute error, SQRT(SUM(data[i]-estimate[i])**2)
        RELATIVE,                                      // relative error, SQRT(SUM((data[i]-estimate[i])/data[i])**2)
        QUARTIC,                                       // same as relative, but to the fourth power 
    }

    Search  search        = Search.FAST;               // search approach to find parameters
    Error   error         = Error.ABSOLUTE;            // error function
    boolean neg_coeff_ok  = false;                     // negative coefficients are allowed

    public int    loops          = 10000000;           // max number of searches before bailing
    public int    count          = 5;                  // quit when delta_norm < min_delta_norm this many times
    public double min_delta_norm = 1E-10;              // minimum changes to norm to end a search
    public double min_delta      = 1E-10;              // minimum changes to coefficients to end a search
    public double max_delta      = 1024;               // max search diameter
    public double cool_rate      = 0.75;               // must be < 1.00, how fast search cube shrinks
    public int    cube_count     = 4;                  // used in DEEP search only

    static final double UNDEFINED = Double.NEGATIVE_INFINITY;

    abstract public double eval(double[] x, double[] c);
    abstract public int c_len(double[] x);

    public LeastSquares()
    {
    }

    public LeastSquares(Search search)
    {
        this.search         = search;
    }

    public LeastSquares(Search search, Error error)
    {
        this.search         = search;
        this.error          = error;
    }

    public LeastSquares(Search search, Error error, boolean neg_coeff_ok)
    {
        this.search         = search;
        this.error          = error;
        this.neg_coeff_ok   = neg_coeff_ok;
    }

    // compute the least squares, i.e., the best estimate for the model coefficients 
    // given X values (xv) and measured Y values (yv). the best set of coefficients
    // is the one that produces the least error between the model and actual 
    // measurements for the available data. 
    //
    // the model (i.e., the sub-class that inherits this class as its superclass)
    // must provide two functions, listed here as abstract. they are: eval, which
    // evaluates the model for a given X and set of coefficients, and c_len, which
    // gives the number of coefficients the model will use for an input vector X.
    // the coefficents are returned as part of the State class (field c).
    public State ls(double[][] xv, double[] yv) 
    {
        State state = new State();
        
        state.c  = zero(c_len(xv[0]));         	       // model coefficients c = 0
        state.n0 = norm(xv, state.c, yv);              // n0 = norm(c) (norm is a type of error)
        state.n1 = Double.POSITIVE_INFINITY;           // n1 = best norm so far
        Random generator = new Random( 19580427 );

        int m = 0;
        double delta = max_delta;
        for (state.i=0; state.i < loops; state.i++) {  // new search, cube centered at c and max width
            delta = max_delta;
            while (min_delta < delta) {                // search until width is smaller than min_delta
                switch (search) {
                case DEEP :                            // search multiple concentric hypercubes
                    double[] cbest = clone(state.c);
                    double nb = state.n0;
                    for (int k=1; k <= cube_count; k++) {
                        double[] ctry = clone(state.c);
                        state.n1 = search(xv, ctry, yv, k*delta/cube_count);
                        if (state.n1 < nb) {
                            cbest = ctry;
                            nb = state.n1;
                        }
                    }
                    state.c = cbest;
                    state.n1 = nb;
                    break;
                case FAST :                            // search only the outermost hypercube
                    state.n1 = search(xv, state.c, yv, delta);
                    break;
                case GENETIC :                         // search hypercubes of randomly shrinking width
                    state.n1 = search(xv, state.c, yv, generator.nextDouble()*delta);
                    break;
                }

                delta *= cool_rate;
            }

                                                    // when count searches in a row produce 
                                                    // improvements of less than min_delta_norm,
                                                    // we've found the best coefficients and 
                                                    // the program is done.
            if ((state.n0-state.n1) < min_delta_norm) {
                m += 1;
                if (count < m) {
//                    System.out.println("("+i+") "+"delta c = "+delta+", delta norm = "+(n0-n1)+", norm = "+n1);
                    break;
                }
            } else {
                m = 0;
            }

            if (0 < state.i && state.i%1000 == 0) { 
                System.out.print("("+state.i+") "+(state.n0-state.n1)+" c={"); 
                print(state.c); 
                System.out.println("}"); 
            }
                        
            state.n0 = state.n1;
        }
        
        return state;
    }
    
                    // print a vector
    public static void print(double[] c)
    {
    	System.out.print(c[0]);
        for (int i=1; i < c.length; i++) {
            System.out.print(", "+c[i]);
        }
    }
    
    // search the faces, edges and corners of a hypercube for 
    // a set of coefficients that fits more closely (lower error)
    // than the input coefficients. the width of the cube is delta.
    // return the new coefficients in c, and the norm (error) as
    // the return value of the function.
    public double search(double[][] xv, double[] c, double[] yv, double delta)
    {
                    // norm index triple
        double[] cbest = clone(c);
        double nbest = norm(xv, c, yv);

        final int clen = c.length;
        int max = (int) Math.pow(3, clen);
        for (int v=0; v < max; v++) {
            double[] ctry = clone(c);
            int p=1;        // p = 3**i
            boolean neg_coeff = false;    // are any coefficients negative?
            for (int i=0; i < clen; i++) {
                ctry[i] += ((v/p)%3 - 1) * delta;
                
                            // stop immediately if coefficient is negative
                neg_coeff = (! neg_coeff_ok) & (ctry[i] < 0);
                if (neg_coeff) break;
                
                p *= 3;
            }

                            // skip this coefficient set if any are negative
            if (neg_coeff) continue;
            
            double ntry = norm(xv, ctry, yv);
            if (ntry < nbest) {
                copy(cbest, ctry);
                nbest = ntry;
            }
        }
        copy(c, cbest);
        
        return nbest;
    }

    // compute the L2 norm (error) -- the L2 norm is defined as
    // the square root of the sums of the squares of the differences
    // the difference is the estimate minus the data.
    // relative error divides the difference by the data,
    // absolute error does not.
    public double norm(double[][] xv, double[] c, double[] yv) 
    {
        double result = 0;

	int len = (xv.length < yv.length) ? xv.length : yv.length;

        for (int i=0; i < len; i++) {
                    // if we are missing either x or y for this 
                    // particular DDR, skip it and move on
            if (check(xv[i]) || check(yv[i])) continue;

            double diff = eval(xv[i], c) - yv[i];

            switch (error) {
            case ABSOLUTE :
                result += diff * diff;
                break;
            case RELATIVE :
                diff = diff / yv[i];
                result += diff * diff;
                break;
            case QUARTIC :
                diff = diff / yv[i];
                result += diff * diff * diff * diff;
                break;
            }
        }

        return Math.sqrt(result);
    }


    // allocates space and copies values
    public static double[] clone(double[] c)
    {
        double[] r = new double[c.length];
        
        for (int i=0; i < c.length; i++) {
            r[i] = c[i];
        }
        
        return r;
    }

    // allocates space and copies the max of two values
    public static double[] clone_max(double[] c, double[] d)
    {
        double[] r = null;
        
        if (c.length < d.length) {
            r = new double[d.length];

            for (int i=0; i < c.length; i++) {
                r[i] = (c[i] < d[i]) ? d[i] : c[i];
            }

            for (int i=c.length; i < d.length; i++) {
                r[i] = d[i];
            }
        } else {
            r = new double[c.length];

            for (int i=0; i < d.length; i++) {
                r[i] = (c[i] < d[i]) ? d[i] : c[i];
            }

            for (int i=d.length; i < c.length; i++) {
                r[i] = c[i];
            }
        }
        
        return r;
    }
    
    // copies values but doesn't allocate space
    public static void copy(double[] t, double[] s)
    {
        for (int i=0; i < s.length; i++) {
            t[i] = s[i];
        }
    }
    
    // create an array of zeros
    public static double[] zero(int len)
    {
        double[] c = new double[len];
        
        for (int i=0; i < c.length; i++) {
            c[i] = 0;
        }
        
        return c;
    }

    // check whether there are any undefined values
    // in either x or y. if so, return true.
    public static boolean check(double y)
    {
        return (y == UNDEFINED);
    }

    // check whether there are any undefined values
    // in either x or y. if so, return true.
    public static boolean check(double[] x)
    {
        boolean result = false;
        
        for (int i=0; i < x.length && ! result; i++) {
            result = (x[i] == UNDEFINED);            
        }

        return result;
    }
}
