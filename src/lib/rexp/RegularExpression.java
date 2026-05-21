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

@SuppressWarnings("unused")
public class RegularExpression {

	public final String cn = this.getClass().getName();

    public static final short LETTER    = 0;
    public static final short OR        = 1;
    public static final short CLOSURE0  = 2;
    public static final short CLOSURE1  = 3;
    public static final short OPTIONAL  = 4;
    public static final short CHARCLASS = 5;
    public static final short START     = 6;
    public static final short END       = 7;
    public static final short SEQUENCE  = 8;

    public final ParseTree pattern;
    public final ParseTree next;

    public RegularExpression( String ps )
    {
    	Parser p = new Parser();
		pattern = p.parse( ps );

		if (pattern != null) {
		    next = pattern.flatten_tree();
		} else {
		    next = null;
		}
    }

    public String toString()
    {
    	return pattern.toString();
    }


    // patterns that work so far:
    // a
    // [a-z]
    // exp exp
    // exp *
    // exp +
    // exp ?
    // exp | exp
    // ( exp )
    
    // still to go:
    // ^ exp
    // exp $
    
    // check if the pattern matches a 
	// prefix of the string.  if so,
	// return the prefix.
    public String prefix( String str )
    {
    	return ( str!= null) ? prefix( str, 0, str.length() ) : null;
    }

    // check if the pattern matches a 
	// prefix of the string.  if so,
	// return the prefix.
    public String prefix( String str, int start )
    {
    	return ( str!= null) ? prefix( str, start, str.length() ) : null;
    }

    // check if the pattern matches a 
	// prefix of the string.  if so,
	// return the prefix.
	public String prefix( String str, int start, int finish )
    {
    	final String fn = cn+".prefix";
    	// System.out.println(fn+" '"+str+"' "+start+" "+finish);
	
    	String result = null;

    	if (str != null) {
    		MatchEntry match = next.prefix(str, start, finish);
    		result = (match != null) ? str.substring(match.start, match.finish) : null;
    	}

    	return result;
    }
    
//    @SuppressWarnings("unused")
	public static void main( String[] args )
    {
		// a, aaa..., [a-z]... 
//    	RegularExpression re = new RegularExpression( "[abc]+[0-9]*" );
    	RegularExpression re;
//    	re = new RegularExpression( "0([.][0-9]*)?" );
    	re = new RegularExpression( "0[Xx][0-9a-fA-F]+|[0-9]+([.][0-9]*)?|[0-9]*[.][0-9]+" );
    	re = new RegularExpression( "[0-9]+" );

    	System.out.println( "Print...");
		System.out.println( "pt=" + re.toString() );
    	System.out.println( "nt=" + re.next.next_to_string() );

    	String str = "127";
    	System.out.println( "pr=" + re.prefix(str) );
    }
}
