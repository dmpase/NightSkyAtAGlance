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
 * util.Stack                                                                 *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    August 23, 2008                                                   *
 *                                                                            *
 ******************************************************************************/

public class Stack<T> {

	public Stack()
	{
	}

				// push an object on the top of the stack
	public synchronized void push( T obj )
	{
		Stack<T> tmp = new Stack<T>();
		tmp.content  = obj;
		stack_count += 1;
		tmp.next     = this.next;
		this.next    = tmp;
		notifyAll();
	}

				// pop an object from the top of the stack
				// if there is nothing in the stack, return null
	public synchronized T pop()
	{
		T result = null;

		if (this.next == null) {
			;			// nothing on the stack
		} else {
				// something is on the stack
			Stack<T> tmp = this.next;
			this.next = this.next.next;
			result = tmp.content;
			stack_count -= 1;
		}

		return result;
	}
	
	public synchronized T top()
	{
		T result = null;

		if (this.next == null) {
			;			// nothing on the stack
		} else {
				// something is on the stack
			result = this.next.content;
		}

		return result;
	}


				// return the number of objects in the queue
	public int length()
	{
		return stack_count;
	}

	private T        content     = null;
	private Stack<T> next        = null;
	private int      stack_count = 0;

	public static void main( String[] args )
	{
		Stack<String> q = new Stack<String>();
		for (int i=0; i < args.length; i++) {
			q.push( args[i] );
		}

		while ( q.length() > 0 ) {
			System.out.println( q.length() + " " + q.pop() );
		}
	}
}
