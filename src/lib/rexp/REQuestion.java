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


class REQuestion extends Unary {

	public final String cn = this.getClass().getName();

    REQuestion(ParseTree l)
    {
    	super( RegularExpression.OPTIONAL, "?", l );
    }

	MatchEntry prefix( String str, int start, int finish )
    {
    	final String fn = cn+".prefix";
    	System.out.println(fn+" "+left_next+value+" matching "+str.substring(start, finish));
    	
    	MatchEntry result = null;
 
    	if (str != null && 0 <= start && start < finish && finish <= str.length()) {
    		// try to match the closure pattern once
    		System.out.println(fn+" matching "+left_next+" against "+str.substring(start, finish));
    		MatchEntry left_result = left_next.prefix(str, start, finish);
    		if (left_result != null) {
    			// optional part matches, let's see if the rest of the pattern also matches
    			if (next != null) {
	    			result = next.prefix(str, left_result.finish, finish);
	    			if (result != null) {
	        			// both optional and the rest of the pattern matches
	    				result.start = start;
	    			} else {
	    				// optional part matched, but the rest didn't
	    				// try again with the non-optional part only
	    				result = next.prefix(str, start, finish);
	    			}
    			} else {
    				result = left_result;
    			}
    		} else {
				// optional part didn't match, 
				// try again with the non-optional part only
    			if (next != null) {
    				result = next.prefix(str, start, finish);
    			} else {
    				result = new MatchEntry(start, start);
    			}
    		}
    	} else {
			result = new MatchEntry(start, start);
    	}
    	
    	return result;
    }
}
