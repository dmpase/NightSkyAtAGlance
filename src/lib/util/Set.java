package lib.util;

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

/******************************************************************************
 *                                                                            *
 * dmpase.container.Set                                                       *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    March 15, 2001                                                    *
 *                                                                            *
 ******************************************************************************/

public class Set implements Cloneable {

	// AUTO SIZE EXTENSION

    public Set( int max_elements )
    {
		max_elts = max_elements;
	
		int max_units = (max_elts + unit_size - 1) / unit_size;
	
		bit_set = new long[ max_units ];
    }

    public int length()
    {
		return max_elts;
    }

    public boolean get(int attribute)
    {
		if (attribute < 0 || max_elts <= attribute) return false;
	
		int i = attribute / unit_size;
		int j = attribute % unit_size;
	
		return (bit_set[i] & (1L << j)) != 0;
    }

    public void set(int attribute)
    {
		if (attribute < 0 || max_elts <= attribute) return;
	
		int i = attribute / unit_size;
		int j = attribute % unit_size;
	
		bit_set[i] |= 1L << j;
    }

    public void clear(int attribute)
    {
		if (attribute < 0 || max_elts <= attribute) return;
	
		int i = attribute / unit_size;
		int j = attribute % unit_size;
	
		bit_set[i] &= ~(1L << j);
    }

    public Set union(Set rhs)
    {
		if (rhs == null || bit_set.length != rhs.bit_set.length ) return null;
	
		for (int i=0; i < bit_set.length; i++) {
		    bit_set[i] |= rhs.bit_set[i];
		}
	
		return this;
    }

    public Set intersect(Set rhs)
    {
		if (rhs == null || bit_set.length != rhs.bit_set.length ) return null;
	
		for (int i=0; i < bit_set.length; i++) {
		    bit_set[i] &= rhs.bit_set[i];
		}
	
		return this;
    }

    public Set minus(Set rhs)
    {
		if (rhs == null || bit_set.length != rhs.bit_set.length ) return null;
	
		for (int i=0; i < bit_set.length; i++) {
		    bit_set[i] &= ~rhs.bit_set[i];
		}
	
		return this;
    }

    public Set inverse()
    {
		for (int i=0; i < bit_set.length; i++) {
		    bit_set[i] = ~bit_set[i];
		}
	
		return this;
    }

    public Object clone() throws CloneNotSupportedException
    {
		Set clone = (Set) super.clone();
	
		return clone;
    }
    
    public String toString()
    {
    	String str = "";
    	
    	for (int i=0; i < max_elts; i++) {
    		if (get(i)) {
    			if (i == '.') {
        			str += 'T';
    			} else if (' ' <= i && i < 127) {
        			str += (char)i;
    			} else {
        			str += "_";
    			}
    		} else {
    			str += ".";
    		}
    	}

    	return str;
    }

    private final int unit_size = 64;

    private int max_elts = 0;

    private long[] bit_set;
}
