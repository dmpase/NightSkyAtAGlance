package lib.math.matrix;

/*******************************************************************************
 * Copyright (c) 1988-2025 Douglas M. Pase                                     *
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
 * Matrix                                                                     *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    November 30, 2025                                                 *
 *                                                                            *
 ******************************************************************************/

public class Transform3D1 {

 /****************************************************************************\
 *                                                                            *
 * Available functions:                                                       *
 *                                                                            *
 *  static double[] apply(double[][] T, double[] v)                           *
 *                                                                            *
 *  static double[][] compose(double[][] lhs, double[][] rhs)                 *
 *                                                                            *
 *  static void print( double[] a )                                           *
 *  static void print( double[][] a )                                         *
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
 *  static double[][] unit()                                                  *
 *  static double[][] zero()                                                  *
 *                                                                            *
 \****************************************************************************/
    
    public static double[] apply(double[][] T, double[] v)
    {
    	return Matrix.times( T, v );
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

    public static void print( double[] a )
    {
    	Matrix.print(a);
    }

    public static void print( double[][] a )
    {
    	Matrix.print(a);
    }

    public static double[][] reflect_x()
    {
    	return new double[][]{{-1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    }

    public static double[][] reflect_y()
    {
    	return new double[][]{{1,0,0,0},{0,-1,0,0},{0,0,1,0},{0,0,0,1}};
    }

    public static double[][] reflect_z()
    {
    	return new double[][]{{1,0,0,0},{0,1,0,0},{0,0,-1,0},{0,0,0,1}};
    }

    /**
     * Create a transformation matrix T that rotates a 3D vector v=[x,y,z,1] in XY by an angle theta, 
     * e.g., Tv = [x',y',z,1].
     * 
     * @param theta Rotation angle in radians.
     * @return A 4x4 transformation matrix that applies rotation to a vector.
     */
    public static double[][] rotate_xy(double theta)
    {
    	return Matrix.rotate_Tp( 4, theta, 0, 1 );
    }

    /**
     * Create a transformation matrix T that rotates a 3D vector v=[x,y,z,1] in YZ by an angle theta, 
     * e.g., Tv = [x,y',z',1].
     * 
     * @param theta Rotation angle in radians.
     * @return A 4x4 transformation matrix that applies rotation to a vector.
     */
    public static double[][] rotate_yz(double theta)
    {
    	return Matrix.rotate_Tp( 4, theta, 1, 2 );
    }

    /**
     * Create a transformation matrix T that rotates a 3D vector v=[x,y,z,1] in XZ by an angle theta, 
     * e.g., Tv = [x',y,z',1].
     * 
     * @param theta Rotation angle in radians.
     * @return A 4x4 transformation matrix that applies rotation to a vector.
     */
    public static double[][] rotate_xz(double theta)
    {
    	return Matrix.rotate_Tp( 4, theta, 0, 2 );
    }

    /**
     * Create a transformation matrix T that scales a 3D vector v=[x,y,z,1] by a scalar s, 
     * e.g., Tv = [sx,sy,sz,1].
     * 
     * @param s Scalar multiplier.
     * @return A 4x4 transformation matrix that scales a vector.
     */
    public static double[][] scale(double s)
    {
    	return new double[][]{{s,0,0,0},{0,s,0,0},{0,0,s,0},{0,0,0,1}};
    }

    /**
     * Create a transformation matrix T that scales a 3D vector v=[x,y,z,1] by a vector s, 
     * e.g., Tv = [s[0]*x,s[1]*y,s[2]*z,1].
     * 
     * @param s Scalar multiplier.
     * @return A 4x4 transformation matrix that scales a vector.
     */
    public static double[][] scale(double[] s)
    {
    	return new double[][]{{s[0],0,0,0},{0,s[1],0,0},{0,0,s[2],0},{0,0,0,1}};
    }

    /**
     * Transform the value of x such that x' = x + sy*y + sz*z. 
     * 
     * @param sy Shear in y.
     * @param sz Shear in z.
     * @return A 4x4 transformation matrix that applies shear to a vector.
     */
    public static double[][] shear_x(double sy, double sz)
    {
    	return new double[][]{{1,sy,sz,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    }

    /**
     * Transform the value of y such that y' = y + sx*x + sz*z. 
     * 
     * @param sx Shear in x.
     * @param sz Shear in z.
     * @return A 4x4 transformation matrix that applies shear to a vector.
     */
    public static double[][] shear_y(double sx, double sz)
    {
    	return new double[][]{{1,0,0,0},{sx,1,sz,0},{0,0,1,0},{0,0,0,1}};
    }

    /**
     * Transform the value of z such that z' = z + sx*x + sy*y. 
     * 
     * @param sx Shear in x.
     * @param sy Shear in y.
     * @return A 4x4 transformation matrix that applies shear to a vector.
     */
    public static double[][] shear_z(double sx, double sy)
    {
    	return new double[][]{{1,0,0,0},{0,1,0,0},{sx,sy,1,0},{0,0,0,1}};
    }

    /**
     * Create a transformation matrix T that translates a 3D vector v=[x,y,z,1] by a scalar s, 
     * e.g., Tv = [x+s,y+s,z+s,1].
     * 
     * @param s Scalar addend.
     * @return A 4x4 transformation matrix that translates a vector.
     */
    public static double[][] translate(double s)
    {
    	return new double[][]{{1,0,0,s},{0,1,0,s},{0,0,1,s},{0,0,0,1}};
    }

    /**
     * Create a transformation matrix T that translates a 3D vector v=[x,y,z,1] by a vector s, 
     * e.g., Tv = [x+s[0],y+s[1],z+s[2],1].
     * 
     * @param s Vector addend.
     * @return A 4x4 transformation matrix that translates a vector.
     */
    public static double[][] translate(double[] s)
    {
    	return new double[][]{{1,0,0,s[0]},{0,1,0,s[1]},{0,0,1,s[2]},{0,0,0,1}};
    }

    public static void main( String[] args )
    {
    	System.out.println( "vector" );
    	double[] v = { Math.sqrt( 2.0 ) / 2.0, Math.sqrt( 2.0 ) / 2.0 };
    	print( v );
    	double theta = Math.PI / 4.0;
    	System.out.println( "solution" );
    	v = Matrix.rotate( v, theta, 0, 1 );
    	print( v );
    }
}
