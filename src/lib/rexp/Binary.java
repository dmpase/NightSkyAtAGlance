package lib.rexp;

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

import java.util.Stack;

// import java.util.Stack;

@SuppressWarnings("unused")
abstract class Binary extends ParseTree {

	public final String cn = "lex.Binary";

    public final ParseTree left;
    public final ParseTree right;

    public ParseTree left_next;
    public ParseTree right_next;

    Binary(int t, Object v, ParseTree l, ParseTree r)
    {
		super( t, v );
	
		left  = l;
		right = r;
    }

    public String toString(int depth)
    {
		String result = "";
	
		for (int i=0; i < depth; i++) {
		    result += " ";
		}
	
		result += value.toString() + "\n";
		
		if (left == null) {
			result += "null";
		} else {
			result += left .toString( depth + 1 );
		}
		result += value;
		if (right == null) {
			result += "null";
		} else {
			result += right.toString( depth + 1 );
		}
	
		return result;
    }

    public String toString()
    {
		String result = "(" + 
			((value == null) ? "null" : value.toString()) + 
			" " + 
			((left  == null) ? "null" : left .toString()) + 
		    " " + 
		    ((right == null) ? "null" : right.toString()) + 
			")";
	
		return result;
    }

    protected void flatten_tree( Stack<ParseTree> visit )
    {
    	// exp | exp: 
    	String fn = cn+".flatten_tree";
    	// System.out.println(fn+" "+this);

    	visit.push(this);    	
    	
    	left_next  = left .flatten_tree();
    	right_next = right.flatten_tree();
    }
}
