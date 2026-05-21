package lib.xml;

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

public class XML_Input {
	public String text;
	public byte[] bytes;
	public int loc = 0;
	
	public XML_Input()
	{
	}
	
	public XML_Input(byte[] bytes)
	{
		this.text  = new String(bytes);
		this.bytes = bytes;
	}
	
	public XML_Input(String text)
	{
		this.text  = text;
		this.bytes = text.getBytes();
	}
	
	public XML_Input(String[] array)
	{
		text = "";
		for (int i=0; i < array.length; i++) {
			this.text += array[i] + " ";
		}
		bytes = text.getBytes();
	}
	
	byte next()
	{
		return eof() ? (byte)-1 : bytes[loc++];
	}
	
	byte look_ahead()
	{
		return eof() ? (byte)-1 : bytes[loc];
	}
	
	byte look_ahead(int i)
	{
		int idx = loc + i;
		return (bytes != null && 0 <= idx && idx < bytes.length) ? bytes[idx] : (byte)-1;
	}
	
	void skip(int count)
	{
		loc += count;
		loc = (loc < 0) ? 0 : loc;
		loc = (bytes.length < loc) ? bytes.length : loc;
	}
	
	boolean eof()
	{
		return (this.bytes == null) || (bytes.length <= loc);
	}
	
	public String toString()
	{
		String result = "text["+loc+"]=" 
			+ ((text != null && 0 <= loc && loc < text.length())?("'"+text.charAt(loc)+"'"):"''")
			+ " text="+((text != null) ? "\""+text+"\"" : "null");
		
		return result;
	}
}
