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

@SuppressWarnings("unused")
abstract class Unary extends ParseTree {

	public final String cn = "lex.Unary";

	public final ParseTree left;

	public ParseTree left_next = null;

    Unary(int t, Object v, ParseTree l)
    {
    	super( t, v );

    	left = l;
    }

    public String toString()
    {
    	String result = "(" + value.toString() + " " + left .toString() + ")";

    	return result;
    }

    public String toString(int depth)
    {
    	String result = "";

    	for (int i=0; i < depth; i++) {
    		result += " ";
    	}

    	result += value.toString() + "\n";

    	result += left .toString( depth + 1 );

    	return result;
    }

    public String next_to_string()
    {
    	String result = "(" + value.toString() + " " + ((left_next == null) ? "null" : left_next.next_to_string()) + ")";
    	if (next != null) result += next.next_to_string();
    	
    	return result;
    }

	protected void flatten_tree( Stack<ParseTree> visit )
    {
    	// exp *: 
    	String fn = cn+".flatten_tree";
    	// System.out.println(fn+" "+this);

    	visit.push(this);    	
    	
    	left_next  = left.flatten_tree();
    }
}
