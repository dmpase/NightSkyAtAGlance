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

import lib.util.Queue;

public class XML_Element {
	// XML_Tag tag
	//     String name
	//     boolean anti_tag
	//     XML_Parameter[] list
	//         String name
	//         String value
	// XML_Element[] list
	// String text
	// boolean atag_found
	
	public static String TEXT_NAME = "@text";

	public XML_Tag       tag  = null;
	public XML_Element[] list = null;
	
	public String        text = null;
	public boolean       atag_found = false;
	
	public XML_Element()
	{
	}
	
	public XML_Element(String s)
	{
		tag  = new XML_Tag(TEXT_NAME);
		list = null;
		text = s;
		atag_found = false;
	}
	
	public XML_Element(String[] sa)
	{
		String s = "";
		for (int i=0; i < sa.length; i++) {
			s += sa[i];
		}
		
		tag  = new XML_Tag(TEXT_NAME);
		list = null;
		text = s;
		atag_found = false;
	}
	
	public String name()
	{
		return this.tag.name;
	}

	public void print()
	{
		print("");
	}
	
	public void print(String prefix)
	{
		if (tag  != null) tag.print(prefix);
		System.out.println(prefix+atag_found);
		if (text != null) System.out.println(prefix+text);
		for (int i=0; list != null && i < list.length; i++) {
			list[i].print(prefix+" ");
		}
	}
	
	public void print_text()
	{
		if (text != null) System.out.print(text+" ");
		for (int i=0; list != null && i < list.length; i++) {
			list[i].print_text();
		}
	}
	
	
	public static XML_Element[] get_words(XML_Element[] e)
	{
		if (e == null) return null;
		
		Queue<XML_Element> queue = new Queue<XML_Element>();
		for (int i=0; i < e.length; i++) {
			e[i].list = get_words(e[i].list);
		}

		for (int i=0; i < e.length; i++) {
			if (e[i].tag.name.equals(TEXT_NAME)) {
				get_words(e[i].text, queue);
			} else {
				queue.append(e[i]);
			}
			e[i].list = get_words(e[i].list);
		}
		
		XML_Element[] result = new XML_Element[queue.length()];
		for (int i=0; i < result.length; i++) {
			result[i] = queue.remove();
		}
		
		return result;
	}
	
	public static XML_Element[] flatten(XML_Element[] e)
	{
		if (e == null) return null;
		
		for (int i=0; i < e.length; i++) {
			e[i] = flatten(e[i]);
		}
		
		Queue<XML_Element> queue = new Queue<XML_Element>();
		
		for (int i=0; i < e.length-1; i++) {
			queue.append(e[i]);
		}

		if (e[e.length-1].atag_found) {
			queue.append(e[e.length-1]);
		} else {
			XML_Element[] tail = flatten(e[e.length-1].list);
			e[e.length-1].list = null;
			queue.append(e[e.length-1]);
			for (int i=0; tail != null && i < tail.length; i++) {
				queue.append(tail[i]);				
			}
		}
		
		XML_Element[] result = new XML_Element[queue.length()];
		for (int i=0; i < result.length; i++) {
			result[i] = queue.remove();
		}
		
		return result;
	}
	
	private static XML_Element flatten(XML_Element e)
	{
		e.list = flatten(e.list);
		
		for (int i=0; e.list != null && i < e.list.length-1; i++) {
			e.list[i] = flatten(e.list[i]);
		}

		return e;
	}

	public static XML_Element[] find_all_by_tag(String name, XML_Element[] e)
	{
		Queue<XML_Element> queue = new Queue<XML_Element>();
		
		find_all_local(name, e, queue);
		
		XML_Element[] result = new XML_Element[queue.length()];
		for (int i=0; i < result.length; i++) {
			result[i] = queue.remove();
		}
		
		return result;
	}
	
	private static void find_all_local(String name, XML_Element[] e, Queue<XML_Element> queue)
	{
		for (int i=0; e != null && i < e.length; i++) {
			if (e[i] == null) continue;
			if (e[i].tag == null) continue;
			if (e[i].tag.name == null) continue;
			if (e[i].tag.name.equalsIgnoreCase(name)) {
								// if it matches the name (i.e., <TAG>... "TAG"==name)
								// it's what we're looking for and we stop right here.
								// we DO NOT look any deeper into the structure.
				queue.append(e[i]);
			} else {
								// if it doesn't match, we dive deeper.
				find_all_local(name, e[i].list, queue);
			}
		}
	}
	
	public static XML_Element find(String name, XML_Element[] e)
	{
		XML_Element result = null;
		
		for (int i=0; result == null && e != null && i < e.length; i++) {
			if (name.equalsIgnoreCase(e[i].tag.name)) {
				result = e[i];
				break;
			}
			result = find(name, e[i].list);
		}
		
		return result;
	}
	

	
	// <file>      -> <elist>
	// <elist>     -> <element> ... <element>
	// <element>   -> <tag> <elist> <untag>
	// <element>   -> <text>
	// <tag>       -> '<' <name> '>'
	// <tag>       -> '<' <name> <plist> '>'
	// <untag>     -> '<' '/' <name> '>'
	// <plist>     -> <parameter> ... <parameter>
	// <parameter> -> <name>
	// <parameter> -> <name> '=' <value>
	// <value>     -> ''' <string> '''
	// <value>     -> '"' <string> '"'
	// <value>     -> <name>
	// <text>      -> <chars> | &amp; | &gt; | &lt; | ...
	
	// <file>      -> <elist>
	// <elist>     -> <element> ... <element>
	public static XML_Element[] remove_empty_elements(XML_Element[] tree)
	{
		if (tree == null) return null;
		
		Queue<XML_Element> queue = new Queue<XML_Element>();
		
		for (int i=0; i < tree.length; i++) {
			if (tree[i].tag.name.compareTo(TEXT_NAME) == 0) {
				char[] text = tree[i].text.toCharArray();
				for (int j=0; j < text.length; j++) {
					if (' ' < text[j]) {
						queue.append(tree[i]);
						break;
					}
				}
			} else {
				tree[i].list = remove_empty_elements(tree[i].list);
				queue.append(tree[i]);
			}
		}
		
		XML_Element[] result = null;
		if (0 < queue.length()) {
			result = new XML_Element[queue.length()];
			for (int i=0; i < result.length; i++) {
				result[i] = queue.remove();
			}
		}

		return result;
	}
	
	private static void get_words(String text, Queue<XML_Element> queue)
	{
		byte[] bytes = text.getBytes();
		
		String s = null;
		for (int i=0; i < bytes.length; i++) {
			if (bytes[i] <= ' ' && s == null) {
				// we're in the middle of white space
			} else if (bytes[i] <= ' ' && s != null) {
				// we've just transitioned to white space
				queue.append(new XML_Element(s));
				s = null;
			} else if (' ' < bytes[i] && s == null) {
				// we've just transitioned to a new word
				s = "" + (char) bytes[i];
			} else {
				// we're in the middle of a word
				s += (char) bytes[i];
			}
		}
		if (s != null) {
			// we ended with a word
			queue.append(new XML_Element(s));
		}
	}
}
