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
 * dmpase.container.QueueInt                                                  *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    November 30, 1999                                                 *
 *          January 15, 2011 - updated for type safety.                       *
 *          August 21, 2024 - added class with integer contents.              *
 *                                                                            *
 ******************************************************************************/

public class QueueInt {

	public QueueInt()
	{
	}

				// append an object to the end of the queue
	public synchronized void append( int obj )
	{
		QueueInt tmp = new QueueInt();
		tmp.content  = obj;
		queue_count += 1;

		if (next == null) {
	    			// nothing in Queue
			next      = tmp;
			next.next = tmp;

			notifyAll();
		} else {
				// one or more items in Queue
			tmp.next  = next.next;
			next.next = tmp;
			next      = tmp;
		}
	}

				// remove an object from the head of the queue
				// if there is nothing in the queue, return null
	public synchronized int remove()
	{
		int result = 0x80000000;

		if (next == null) {
			;			// nothing in Queue
		} else if (next.next == next) {
				// one item in Queue
			result = next.content;
			next   = null;
			queue_count -= 1;
		} else {
				// two or more items in Queue
			result = next.next.content;
			next.next = next.next.next;
			queue_count -= 1;
		}

		return result;
	}

				// remove one instance of a specified object 
				// from the queue.  the return value indicates 
				// whether something was removed from the queue.
	public synchronized boolean remove( int obj )
	{
		boolean result = false;

		if (this.next == null) {
			;			// nothing in Queue
		} else if (this.next.next == this.next) {
				// one item in Queue
			if (this.next.content == obj) {
				next = null;
				queue_count -= 1;
				result = true;
			}
		} else {
				// two or more items in Queue
			QueueInt marker = this.next;
			for (int i=0; i < queue_count; i++) {
				if (marker.next.content == obj) {
					marker.next = marker.next.next;
					queue_count -= 1;
					result = true;
					break;
				}

				marker = marker.next;
			}
		}

		return result;
	}

				// remove an object from the head of the queue
				// if there is nothing in the queue, wait until
				// something arrives
	public synchronized int bremove() throws InterruptedException
	{
		int result = 0x80000000;

				// nothing in Queue
		while (this.next == null) {
			wait();
		}

		if (next.next == next) {
				// one item in Queue
			result = next.content;
			next = null;
			queue_count -= 1;
		} else {
				// two or more items in Queue
			result = next.next.content;
			next.next = next.next.next;
			queue_count -= 1;
		}

		return result;
	}

				// retrieve the head of the queue without
				// affecting the contents of the queue
	public synchronized int head()
	{
		int result = 0x80000000;

		if (next == null) {
			;			// nothing in Queue
		} else {
				// one or more items in Queue
			result = next.next.content;
		}

		return result;
	}

				// search the queue for obj without
				// affecting the contents of the queue
	public synchronized boolean contains( int obj )
	{
		if (next == null) {	// nothing in Queue
			return false;
		}

		QueueInt marker = this.next;
		for (int i=0; i < queue_count; i++) {
			if (marker.content == obj) return true;

			marker = marker.next;
		}

		return false;
	}

				// return the ith element of the queue
	public synchronized int elt(int idx)
	{
		int result = 0x80000000;
		if (next == null || queue_count <= idx) {	// nothing in Queue
			return result;
		}

		QueueInt marker = this.next.next;
		for (int i=0; i < idx; i++) {
			marker = marker.next;
		}
		result = marker.content;

		return result;
	}

				// return the number of objects in the queue
	public synchronized int length()
	{
		return queue_count;
	}

				// append a queue of objects to the end of the queue
	public synchronized void merge( QueueInt tail )
	{
		for (int i=0; i < tail.length(); i++ ) {
			int tmp = tail.remove();
			this.append( tmp );
			tail.append( tmp );
		}
	}

	public synchronized void randomize()
	{
		QueueInt t = new QueueInt();
		
		while (0 < this.length()) {
			int passes = (1+(int)(127*Math.random())) % this.length();
			for (int k=0; k < passes; k++) {
				append(remove());
			}
			t.append(remove());
		}

		while (0 < this.length()) {
			append(t.remove());
		}
	}

	private int      content     = 0x80000000;
	private QueueInt next        = null;
	private int      queue_count = 0;

	public static void main( String[] args )
	{
		int[] arg = {'a', 'b', 'c', 'd', };
		QueueInt q = new QueueInt();
		for (int i=0; i < arg.length; i++) {
			q.append( arg[i] );
		}

		for (int i=0; i < q.length(); i++) {
			System.out.println( i + " " + q.elt(i) );
		}
		
		System.out.println();

		while ( q.length() > 0 ) {
			System.out.println( q.length() + " " + q.remove() );
		}
	}
}
