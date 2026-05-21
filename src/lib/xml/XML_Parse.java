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

import java.io.*;

import lib.util.*;

public class XML_Parse {
	// structure of all XML files is:
	// Array of elements
	//   -- element 0
	//      -- name
	//      -- array of parameters
	//         -- parameter 0
	//            -- name, value
	//         -- parameter 1
	//            -- name, value
	//            ...
	//         -- parameter n-1
	//            -- name, value
	//      -- array of sub-elements
	//         -- element 0
	//            ...
	//         -- element n-1
	//      ...
	
	// Element {
	//     Tag tag;
	//     Element[] list;
	//     String text;
	// };
	//
	// Tag {
	//     String name;
	//     Parameter[] list;
	// };
	// 
	// Parameter {
	//     String name;
	//     String value;
	// };
	
	private static final byte OPEN_TAG  = '<';
	private static final byte CLOSE_TAG = '>';
	private static final byte UNTAG     = '/';
	private static final byte SINGLE_Q  = '\'';
	private static final byte DOUBLE_Q  = '"';
	
	Stack<XML_Tag> stack = new Stack<XML_Tag>();

	XML_Input input;
	
	public XML_Parse(byte[] bytes)
	{
		input = new XML_Input(bytes);	
	}
	
	public XML_Parse(String text)
	{
		input = new XML_Input(text);	
	}
	
	public XML_Parse(String dir, String file) throws FileNotFoundException
	{
		input = new XML_FileInput(dir, file);	
	}
	
	public XML_Parse(File file) throws FileNotFoundException
	{
		input = new XML_FileInput(file);	
	}
	
	public XML_Parse(XML_Input input)
	{
		this.input = input;
	}

	
	public XML_Element[] parse_elist()
	{
//		System.out.println("XML_Parse.parse_elist: entering " + blah());

		Queue<XML_Element> queue = new Queue<XML_Element>();
		
		while (!input.eof()) {
			parse_comment();
			if        (input.look_ahead(0) == OPEN_TAG && input.look_ahead(1) != UNTAG) {
				XML_Element e = parse_element_tag();
				queue.append(e);
			} else if (input.look_ahead(0) == OPEN_TAG && input.look_ahead(1) == UNTAG) {
				break;
			} else {				
				XML_Element e = parse_element_text();
				queue.append(e);
			}				
		}
		
		XML_Element[] result = null;
		if (0 < queue.length()) {
			result = new XML_Element[queue.length()];
			for (int i=0; i < result.length; i++) {
				result[i] = queue.remove();
			}
		}
		
//		System.out.println("XML_Parse.parse_elist: leaving " + blah());

		return result;
	}

	private XML_Element parse_element_tag()
	{
//		System.out.println("XML_Parse.parse_element_tag: entering " + blah());
		
		// parse text and tagged elements until an untag or EOF is found
		XML_Element result = new XML_Element();

		XML_Tag tag = parse_tag();
		result.tag = tag;
		
		result.list = parse_elist();
		parse_comment();
		if (!input.eof() && input.look_ahead() == OPEN_TAG) {
			int k = input.loc;
			XML_Tag atag = parse_tag();
			// if the next tag is an anti-tag and the name matches 
			// the opening tag, we're done. otherwise, back up the
			// text to the beginning of the anti-tag before leaving.
			if (atag.anti_tag) {
				if (tag.name.compareToIgnoreCase(atag.name) != 0) {
					input.loc = k;
					result.atag_found = false;
				} else {
					result.atag_found = true;
				}
			}
		}

//		System.out.println("XML_Parse.parse_element_tag: leaving " + blah());

		return result;
	}

	private XML_Element parse_element_text()
	{
//		System.out.println("XML_Parse.parse_element_text: entering " + blah());
		
		// parse text and tagged elements until an untag or EOF is found
		XML_Element result = new XML_Element();
		result.tag = new XML_Tag(XML_Element.TEXT_NAME);
		result.text = "";
		result.atag_found = false;
		while (!input.eof()) {
			parse_comment();
			byte c = input.look_ahead();
			if (c == OPEN_TAG) {
				break;
			} else if (
					input.look_ahead(0) == '&' && 
					input.look_ahead(1) == 'a' &&
					input.look_ahead(2) == 'm' &&
					input.look_ahead(3) == 'p' &&
					input.look_ahead(4) == ';') {
							
				result.text += (char) '&';
				input.skip(5);
				break;
			} else if (
					input.look_ahead(0) == '&' && 
					input.look_ahead(1) == 'g' &&
					input.look_ahead(2) == 't' &&
					input.look_ahead(3) == ';') {
							
				input.skip(4);
				result.text += (char) '>';
				break;
			} else if (
					input.look_ahead(0) == '&' && 
					input.look_ahead(1) == 'l' &&
					input.look_ahead(2) == 't' &&
					input.look_ahead(3) == ';') {
							
				input.skip(4);
				result.text += (char) '<';
				break;
			} else {
				result.text += (char) c;
			}
			input.next();
		}

//		System.out.println("XML_Parse.parse_element_text: leaving " + blah());

		return result;
	}
	
	// parse just the tag
	private XML_Tag parse_tag()
	{
		XML_Tag result = null;
		
		parse_comment();
		byte c = input.look_ahead();
		if (c == OPEN_TAG) {
			c = input.next();
			result = new XML_Tag();
			
			// found '<', now eliminate white space
			parse_ws();
			
			c = input.look_ahead();
			if (c == UNTAG) {			// </...
				result.anti_tag = true;
				c = input.next();
				result.name = parse_name();
			} else if (c == '!') {
				if (   input.look_ahead(0) == '!' 
					&& input.look_ahead(1) == '-'
					&& input.look_ahead(2) == '-') {
					
					while (!(input.look_ahead(0) == '-' && input.look_ahead(1) == '-' && input.look_ahead(2) == '>')) {
						input.next();
					}
				}
			} else {
				result.anti_tag = false;
				result.name = parse_name();
				parse_ws();
				// get the parameter list (if it's there)
				result.list = parse_plist();
			}

			// looking for '>'
			c = input.look_ahead();
			while (!input.eof() && c != CLOSE_TAG) {
				c = input.next();
			}
			if (input.look_ahead() == CLOSE_TAG) {
				input.next();
			}
		}

		return result;
	}
	
	private void parse_ws()
	{
		while (!input.eof()) {
			byte c = input.look_ahead();
			if (' ' < c) {
				break;
			}
			c = input.next();
		}
	}
	
	private void parse_comment()
	{
		while (	input.look_ahead(0) == '<' && 
				input.look_ahead(1) == '!' &&
				input.look_ahead(2) == '-' &&
				input.look_ahead(3) == '-') {
				
			while (!input.eof()) {
				if (input.look_ahead(0) == '-' && 
					input.look_ahead(1) == '-' &&
					input.look_ahead(2) == '>') {
						
					input.next();
					input.next();
					input.next();
					break;
				}
				input.next();
			}
		}
	}
	
	private String parse_name()
	{
		String name = "";
		
		while (!input.eof()) {
			byte c = input.look_ahead();
			if (('A' <= c && c <= 'Z') ||
				('a' <= c && c <= 'z') ||
				('0' <= c && c <= '9') ||
				('_' == c || c == '$') ||
				('-' == c || c == '!')) {
				name += (char) c;
				input.next();
			} else {
				break;
			}
		}

		return name;
	}
	
	private String parse_value()
	{
		String value = "";
		
		byte delimiter = input.look_ahead();
		if (delimiter != SINGLE_Q && delimiter != DOUBLE_Q) {
			return null;
		}
		input.next();
		
		while (!input.eof()) {
			byte c = input.next();
			if (c == delimiter) {
				break;
//			} else if (c == '\\') {
//				c = input.next();
//				result += (char) c;
			} else {
				value += (char) c;
			}
		}

		return value;
	}
	
	// <plist>     -> <parameter> ... <parameter>
	private XML_Parameter[] parse_plist()
	{
		Queue<XML_Parameter> q = new Queue<XML_Parameter>();

		int last = -1;
		parse_ws();
		while (!input.eof()) {
			if (input.loc == last) {
				System.exit(0);
			} else {
				last = input.loc;
			}
			byte c = input.look_ahead();
			if (c == CLOSE_TAG) {
				break;
			} else {
				XML_Parameter p = parse_parameter();
				q.append(p);
			}
			parse_ws();
		}
		
		XML_Parameter[] result = null;
		if (0 < q.length()) {
			result = new XML_Parameter[q.length()];
			for (int i=0; i < result.length; i++) {
				result[i] = q.remove();
			}
		}

		return result;
	}

	// <parameter> -> <name>
	// <parameter> -> <name> '='
	// <parameter> -> <name> '=' <name>
	// <parameter> -> <name> '=' <string>
	private XML_Parameter parse_parameter()
	{
//		System.out.println("XML_Parse.parse_parameter: entering " + blah());
		
//		if (input.loc == 16) MainFrame.exit();
		
		XML_Parameter result = new XML_Parameter();
		parse_ws();
		result.name = parse_name();
		parse_ws();
		byte c = input.look_ahead();
		if (c == '=') {
			input.next();
			c = input.look_ahead();
			if (c == SINGLE_Q || c == DOUBLE_Q) {
				result.value = parse_value();
			} else if (('A' <= c && c <= 'Z') ||
					('a' <= c && c <= 'z') ||
					('0' <= c && c <= '9')) {
				result.value = parse_name();
			} else {
				result.value = null;
			}
		} else {
			result.value = null;
		}

//		System.out.println("XML_Parse.parse_parameter: leaving " + blah());

		return result;
	}
	
	/*
	public static void main(String[] args)
	{
		XML_Element e0 = new XML_Element("This is a bunch of words.");
		e0.print("");
		XML_Element[] e1 = new XML_Element[1];
		e1[0] = e0;
		e1 = XML_Element.get_words(e1);
		for (int i=0; i < e1.length; i++) {
			e1[i].print("");
		}
		System.exit(0);
		
		System.out.println("dir =\""+System.getProperty("user.dir" )+"\"");
		System.out.println("home=\""+System.getProperty("user.home")+"\"");
		System.out.println("name=\""+System.getProperty("user.name")+"\"");
		try {
			XML_Input input = new XML_FileInput(System.getProperty("user.dir"),"TestDriver.xml");
			XML_Parse xml = new XML_Parse(input);
			XML_Element[] e = xml.parse_elist();
			e = XML_Element.remove_empty_elements(e);
			e = XML_Element.flatten(e);
			
			System.out.println(e.length);
			for (int i=0; i < e.length; i++) {
				e[i].print("");
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			XML_Input input = new XML_FileInput(
				"E:\\home\\projects\\Questions\\subjects\\Coast Guard Auxiliary\\Boating Skills & Seamanship",
				"Broken.xml");
			System.out.println("input={"+input.toString()+"}");
			XML_Parse xml = new XML_Parse(input);
			XML_Element[] e = xml.parse_elist();
			e = XML_Element.remove_empty_elements(e);
			e = XML_Element.flatten(e);
		
			System.out.println(e.length);
			for (int i=0; i < e.length; i++) {
				e[i].print("");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	*/
}
