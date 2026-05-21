package lib.math.matrix;

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

/******************************************************************************
 *                                                                            *
 * Transform2D1                                                               *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    November 30, 2025                                                 *
 *                                                                            *
 ******************************************************************************/

public class Transform2D1 {

 /****************************************************************************\
 *                                                                            *
 * Available functions:                                                       *
 *                                                                            *
 *  static double[][] reflect_x()                                             *
 *  static double[][] reflect_y()                                             *
 *                                                                            *
 *  static double[][] rotate(double theta)                                    *
 *                                                                            *
 *  static double[][] scale(double s)                                         *
 *  static double[][] scale(double[] s)                                       *
 *                                                                            *
 *  static double[][] shear_x(double sx)                                      *
 *  static double[][] shear_y(double sy)                                      *
 *                                                                            *
 *  static double[][] translate(double s)                                     *
 *  static double[][] translate(double[] s)                                   *
 *                                                                            *
 *  static double[][] unit()                                                  *
 *  static double[][] zero()                                                  *
 *                                                                            *
 *  static double[][] compose(double[][] lhs, double[][] rhs)                 *
 *                                                                            *
 *  static void print( double[] a )                                           *
 *  static void print( double[][] a )                                         *
 *                                                                            *
 \****************************************************************************/


    /**
     * Create a transformation matrix T that translates a 2D vector v=[x,y,1] by a scalar s, 
     * e.g., Tv = [x+s,y+s,1].
     * 
     * @param s Scalar addend.
     * @return A 3x3 transformation matrix that translates a vector.
     */
    public static double[][] translate(double s)
    {
    	return new double[][]{{1,0,s},{0,1,s},{0,0,1}};
    }

    /**
     * Create a transformation matrix T that translates a 2D vector v=[x,y,1] by a vector s, 
     * e.g., Tv = [x+s0,y+s1,1].
     * 
     * @param s Scalar addend.
     * @return A 3x3 transformation matrix that translates a vector.
     */
    public static double[][] translate(double[] s)
    {
    	return new double[][]{{1,0,s[0]},{0,1,s[1]},{0,0,1}};
    }

    /**
     * Create a transformation matrix T that scales a 2D vector v=[x,y,1] by a scalar s, 
     * e.g., Tv = [s*x,s*y,1].
     * 
     * @param s Scalar multiplier.
     * @return A 3x3 transformation matrix that scales a vector.
     */
    public static double[][] scale(double s)
    {
    	return new double[][]{{s,0,0},{0,s,0},{0,0,1}};
    }

    /**
     * Create a transformation matrix T that scales a 2D vector v=[x,y,1] by a vector s, 
     * e.g., Tv = [s[0]*x,s[1]*y,1].
     * 
     * @param s Scalar multiplier.
     * @return A 3x3 transformation matrix that scales a vector.
     */
    public static double[][] scale(double[] s)
    {
    	return new double[][]{{s[0],0,0},{0,s[1],0},{0,0,1}};
    }

    /**
     * Create a transformation matrix T that rotates a 2D vector v=[x,y,1] by an angle theta, 
     * e.g., Tv = [x',y',1].
     * 
     * @param theta Rotation angle in radians.
     * @return A 3x3 transformation matrix that applies rotation to a vector.
     */
    public static double[][] rotate(double theta)
    {
    	return Matrix.rotate_Tp( 3, theta, 0, 1 );
    }

    /**
     * Transform the value of x such that x' = x + sy*y. 
     * 
     * @param sy Shear in y.
     * @return A 3x3 transformation matrix that applies shear to a vector.
     */
    public static double[][] shear_x(double sx)
    {
    	return new double[][]{{1,sx,0},{0,1,0},{0,0,1}};
    }

    /**
     * Transform the value of y such that y' = y + sx*x. 
     * 
     * @param sx Shear in x.
     * @return A 3x3 transformation matrix that applies shear to a vector.
     */
    public static double[][] shear_y(double sy)
    {
    	return new double[][]{{1,0,0},{sy,1,0},{0,0,1}};
    }

    public static double[][] reflect_x()
    {
    	return new double[][]{{-1,0,0},{0,1,0},{0,0,1}};
    }

    public static double[][] reflect_y()
    {
    	return new double[][]{{1,0,0},{0,-1,0},{0,0,1}};
    }

    /**
     * Create a 2x2 unit matrix.
     * 
     * @return A 2x2 unit matrix.
     */
    public static double[][] unit()
    {
    	return Matrix.unit(3, 3);
    }

    /**
     * Create a 2x2 zero matrix.
     * 
     * @return A 2x2 zero matrix.
     */
    public static double[][] zero()
    {
    	return Matrix.zero(3, 3);
    }

    /**
     * Compose two transformations T1 and T2 = T1 o T2 = T1 x T2.
     * This implies Tn o ... o T1 o T0 = Tn x ... x T1 x T0.
     * Looking at T0, T1, ..., Tn as linear operators for a vector v,
     * Tn(...T1(T0(v))...) = (Tn o ... o T1 o T0)(v) = Tn x ... x T1 x T0 x v. 
     * In other words, the transform on the right (T2) is applied first.
     * 
     * @param lhs Transformation to apply second.
     * @param rhs Transformation to apply first.
     * @return lhs o rhs = lhs x rhs.
     */
    public static double[][] compose(double[][] lhs, double[][] rhs)
    {
    	return Matrix.compose( lhs, rhs );
    }

    public static double[] apply(double[][] lhs, double[] rhs)
    {
    	return Matrix.times(lhs, rhs);
    }

    public static double[] apply(double[] lhs, double[][] rhs)
    {
    	return Matrix.times(lhs, Matrix.transpose(rhs));
    }

    public static void print( double[] a )
    {
    	Matrix.print(a);
    }

    public static void print( double[][] a )
    {
    	Matrix.print(a);
    }

    public static void main( String[] args )
    {
    	System.out.println( "vector" );
    	double[] v = { Math.sqrt( 2.0 ) / 2.0, Math.sqrt( 2.0 ) / 2.0, 1 };
    	Matrix.print( v );
    	double theta = Math.PI / 4.0;
    	System.out.println( "solution" );
    	double[][] T = Transform2D1.rotate( theta );
    	v = Transform2D1.apply(v, T);
    	Matrix.print( v );
    }
}
