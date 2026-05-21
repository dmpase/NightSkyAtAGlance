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


public class Array<Type> {

    public Array()
    {
		contents = new Object[ 1<<BITS_PER_LEVEL ][][];
		for (int i=0; i < contents.length; i++) {
			contents[i] = null;
		}
    }

    public Array(int max_size)
    {
    	double size_of_level = Math.pow(max_size, 1.0/3.0);
    	double bits = Math.ceil(Math.log(size_of_level) / Math.log(2.0));
    	BITS_PER_LEVEL = (int) bits;

    	contents = new Object[ 1<<BITS_PER_LEVEL ][][];
		for (int i=0; i < contents.length; i++) {
			contents[i] = null;
		}
    }

    public synchronized void set( int index, Type obj )
    {
		if (index   < 0) return;
		if (max_idx < index) max_idx = index;
	
		int L0 = (index >> (2 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
		int L1 = (index >> (1 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
		int L2 = (index >> (0 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
	
					// do we need to expand the array?
		if (contents[L0] == null) {
			contents[L0] = new Object[ 1<<BITS_PER_LEVEL ][];
			for (int i=0; i < contents[L0].length; i++) {
				contents[L0][i] = null;
			}
		}
	
		if (contents[L0][L1] == null) {
		    contents[L0][L1] = new Object[ 1<<BITS_PER_LEVEL ];
			for (int i=0; i < contents[L0][L1].length; i++) {
				contents[L0][L1][i] = null;
			}
		}
	
					// place the new element in the array
		contents[ L0 ][ L1 ][ L2 ] = obj;
    }

    @SuppressWarnings("unchecked")
	public synchronized Type get( int index )
    {
		if (index < 0 || max_idx < index) return null;
	
		int L0 = (index >> (2 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
		int L1 = (index >> (1 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
		int L2 = (index >> (0 * BITS_PER_LEVEL)) & ((1<<BITS_PER_LEVEL) - 1);
	
		if (contents[L0]     == null) return null;
		if (contents[L0][L1] == null) return null;
	
		return (Type) contents[ L0 ][ L1 ][ L2 ];
    }
    
    public synchronized int length()
    {
    	return max_idx+1;
    }
    
    public synchronized void clear()
    {
    	for (int i=0; i < contents.length; i++) {
    		contents[i] = null;
    		max_idx = -1;
    	}
    }
    

    private int          max_idx        = -1;
    private int          BITS_PER_LEVEL = 8;
    private Object[][][] contents       = null;

    public static void main( String[] args )
    {
    	Array<String> iea = new Array<String>(128);
		for (int i=0; i < args.length; i++) {
		    iea.set(i, args[i]);
		}
	
		for (int i=0; i < args.length; i++) {
		    System.out.println(i + " " + (String) iea.get(i));
		}
    }
}