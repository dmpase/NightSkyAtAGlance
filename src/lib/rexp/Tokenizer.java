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

class Tokenizer {

	public final String cn = this.getClass().getName();

    String value = null;
    int    pos   = -1;
    
    public boolean escape = false;

    private TokenValue[] keywords = null;
    private TokenValue   text_end = null;
    private TokenValue   letter   = null;
    private TokenValue[] enter_char_class = null;
    private TokenValue[] exit_char_class  = null;

    Tokenizer( String v, TokenValue[] kwd, TokenValue[] enter, TokenValue[] exit, TokenValue let, TokenValue eot )
    {
		value = v;
		pos   = 0;
	
		keywords = kwd;
		letter   = let;
		enter_char_class = enter;
		exit_char_class  = exit;
		text_end = eot;
    }

    private boolean in_char_class = false;
    
    TokenValue next_token()
    {
    	TokenValue result = null;

    	// are we at the end of the text?
    	if ( value.length() <= pos ) {
    		return text_end;
    	}

    	// select the set of tokens to use...
    	// the set of tokens we recognize depends on whether we're in a character class
        TokenValue[] keys = null;
    	if (in_char_class && exit_char_class != null) {
    		// we are in a character class, search for the end token (e.g., ']')
    		keys = exit_char_class;
    	} else {
    		// we are not in a character class, so search from the larger set
    		keys = keywords;
    	}
 
    	// search for a token
    	String sub = value.substring( pos );
    	// System.out.println("Searching Keyword List... " + sub);
    	for (int i=0; i < keys.length; i++) {
    		// System.out.println("Checking " + keys[i].value + ".");
		    if ( sub.startsWith( keys[i].value ) ) {
		    	// System.out.println("Found '" + sub.substring( 0, keys[i].value.length() ) + "' (" + keys[i].value.length() + ") "+ keys[i].type);
		    	result = new TokenValue( keys[i].type, sub.substring( 0, keys[i].value.length() ) );
		    	pos += keys[i].value.length();
		    	break;
		    }
    	}

    	// did we find one? if not, call it a letter
    	// but if we did, did it start or end a character class?
    	if (result != null) {
    		// token found, did it start or end a character class?
        	if (! in_char_class && enter_char_class != null) {
        		// we're not in a character class, did this start one?
	    		for (int i=0; i < enter_char_class.length; i++) {
	    			if (sub.startsWith(enter_char_class[i].value)) {
	    				in_char_class = true;
	    				break;
	    			}
	    		}
        	} else if (in_char_class && exit_char_class != null) {
        		// we're in a character class, did this end one?
	    		for (int i=0; i < exit_char_class.length; i++) {
	    			if (sub.startsWith(exit_char_class[i].value)) {
	    				in_char_class = false;
	    				break;
	    			}
	    		}
        	}
    	} else if (escape && 1 < sub.length() && sub.charAt( 0 ) == '\\') {
    		// \0dd  -- character specified in octal
    		// \\    -- the character '\\'
    		// \0xXX -- character specified in hexadecimal
    		// \t    -- the character '\t'
    		// \b    -- the character '\b'
    		// \n    -- the character '\n'
    		// \r    -- the character '\r'
    		switch (sub.charAt(1)) {
    		case '\\' :
        		result = new TokenValue( letter.type, "\\" );
        		pos += 2;
        		break;
    		case 't' :
        		result = new TokenValue( letter.type, "\t" );
        		pos += 2;
        		break;
    		case 'b' :
        		result = new TokenValue( letter.type, "\b" );
        		pos += 2;
        		break;
    		case 'n' :
        		result = new TokenValue( letter.type, "\n" );
        		pos += 2;
        		break;
    		case 'r' :
        		result = new TokenValue( letter.type, "\r" );
        		pos += 2;
        		break;
    		case '0' :
    			// \0dd or \0xXX
    			// needs more work
    			if (sub.charAt(2) == 'x' || sub.charAt(2) == 'X') {
    			} else if ('0' <= sub.charAt(2) && sub.charAt(2) <= '7') {
    			} else {
            		result = new TokenValue( letter.type, ""+(char)sub.charAt(1) );
            		pos += 2;
    			}
        		break;
    		default :
        		result = new TokenValue( letter.type, ""+(char)sub.charAt(1) );
        		pos += 2;
        		break;
    		}
    	} else {
    		// System.out.println("Found '" + sub.substring( 0, 1 ) + "' (" + 1 + ") "+letter.type);
    		result = new TokenValue( letter.type, "" + sub.charAt( 0 ) );
    		pos += 1;
    	}    	

    	return result;
    }
}
