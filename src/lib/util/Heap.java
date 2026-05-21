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
 * Heap                                                                       *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    February 15, 2000                                                 *
 *                                                                            *
 ******************************************************************************/

public class Heap<Type extends Comparable<Type>> {

				// this constructor creates a max-heap
				// using the incremental expandable array
				// as its underlying storage structure
    public Heap()
    {
    	contents = new Array<Type>();
    }

				// this constructor creates a max-heap
				// using the specified expandable array
				// as its underlying storage structure
    public Heap(Array<Type> a)
    {
    	contents = a;
    }

				// this constructor creates a heap
				// using the incremental expandable array
				// as its underlying storage structure.
				// if max_heap is set to true, the heap
				// becomes a max-heap.  if false, the heap
				// becomes a min-heap.
    public Heap(boolean mh)
    {
    	max_heap = mh;
    	contents = new Array<Type>();
    }

				// this constructor creates a heap
				// using the specified expandable array
				// as its underlying storage structure
				// if max_heap is set to true, the heap
				// becomes a max-heap.  if false, the heap
				// becomes a min-heap.
    public Heap(boolean mh, Array<Type> c)
    {
    	max_heap = mh;
    	contents = c;
    }
    
    public synchronized void set_max_heap(boolean mh)
    {
    	max_heap = mh;
    }
    
    public synchronized boolean get_max_heap()
    {
    	return max_heap;
    }

				// append an object to the end of the heap
    public synchronized void append( Type obj )
    {
    	// place the new element at the bottom
    	int index = heap_count;
    	contents.set( index, obj );
    	heap_count += 1;

    	// then percolate upwards until no change
    	// or it is the root
    	boolean changed = true;
    	while (0 < index && changed) {
    		int parent_index  = (index - 1) / 2;
    		Type parent_obj = contents.get( parent_index );
    		if ((0 < obj.compareTo( parent_obj )) == max_heap) {
    			contents.set( index, parent_obj );
    			contents.set( parent_index, obj );
    			index = parent_index;
    		} else {
    			changed = false;
    		}
    	}
    }

    // remove an object from the top of the heap
    public synchronized Type remove()
    {
    	// check that the heap is non-empty
    	if (heap_count == 0) return null;

    	// remove the top element from the root
    	Type result = contents.get( 0 );

    	// get the last element
    	heap_count -= 1;
    	Type last_obj = contents.get( heap_count );

    	// place the last element at the root
    	int index = 0;
    	contents.set( index, last_obj );

    	// percolate downwards until no change
    	// or it is at the bottom
    	boolean changed = true;
    	while (changed) {
    		// find the left and right children
    		int left  = 2 * index + 1;
    		int right = 2 * index + 2;

    		// select the child
    		int child = -1;
    		Type child_obj = null;
    		if (heap_count <= left) {
				// if the left is empty, we're done
    			break;
    		} else if (heap_count <= right) {
				// if the right is empty, use the left
    			child = left;
    			child_obj = contents.get( left  );
    		} else {
    			Type left_obj  = contents.get( left  );
    			Type right_obj = contents.get( right );

    			if ((0 < left_obj.compareTo( right_obj )) == max_heap) {
    				// if the left < right (for min-heaps) or
    				// the right < left (for max-heaps), use left
    				child = left;
    				child_obj = left_obj;
    			} else {
    				// otherwise use the right
    				child = right;
    				child_obj = right_obj;
    			}
    		}

    		// if child and parent are out of order
    		// then reverse the order of the two
    		if ((0 < child_obj.compareTo( last_obj )) == max_heap) {
    			contents.set( index, child_obj );
    			contents.set( child, last_obj  );
    			index = child;
    		} else {
    			changed = false;
    		}
    	}

    	return result;
    }

				// retrieve the top of the heap without
				// affecting the contents of the heap
    public synchronized Type next()
    {
		if (heap_count < 1) {
			return null;
		} else {
			return contents.get( 0 );
		}
    }

				// return the number of objects in the heap
    public int length()
    {
    	return heap_count;
    }

    private boolean     max_heap   = true;
    private Array<Type> contents   = null;
    private int         heap_count = 0;

    public static void main( String[] args )
    {
		Heap<String> h = new Heap<String>();
		for (int i=0; i < args.length; i++) {
		    h.append( args[i] );
		}
	
		while ( h.length() > 0 ) {
		    System.out.println( h.length() + " " + h.remove() );
		}
    }
}
