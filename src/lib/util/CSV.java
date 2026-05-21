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

import java.util.*;

// import utils.Queue;

/******************************************************************************
 *                                                                            *
 * dmpase.strings.CSV                                                         *
 *                                                                            *
 * Author:  Douglas M. Pase                                                   *
 *                                                                            *
 * Date:    November 30, 1999                                                 *
 *                                                                            *
 ******************************************************************************/

public class CSV {

	public static String[] parse_csv_line( String str, String separators )
	{
		StringTokenizer st = new StringTokenizer(str, separators, true);
		Queue<String> all_fields = new Queue<String>();

		boolean expect_value = true;
		boolean token_is_value = false;
		while ( st.hasMoreTokens() ) {
			String token = st.nextToken( separators );
			token_is_value = (separators.indexOf(token) < 0);

			if (expect_value && token_is_value) {
				// expected a value, found a value
				// check for quoted values
				if (token.startsWith( "'" )) {
					while (! token.endsWith( "'" ) && st.hasMoreTokens()) {
						token = token + st.nextToken( "'" );
					}
				} else if (token.startsWith( "\"" )) {
					while (! token.endsWith( "\"" ) && st.hasMoreTokens()) {
						token = token + st.nextToken( "\"" );
					}
				}
				all_fields.append( token );
				expect_value = false;
			} else if (expect_value && ! token_is_value) {
				// expected a value, found a delimiter
				all_fields.append( (String) null );
				expect_value = true;
			} else if (! expect_value && ! token_is_value) {
				// expected a delimiter, found a delimiter
				expect_value = true;
			} else {
				// expected a delimiter, found a value
				// this is an error in ???
				System.err.println( "get_csv_fields: ERROR!" );
				System.err.println( "expected a delimiter, found a value" );

				all_fields.append( token );
				expect_value = false;
			}
		}
				// record a missing, trailing value
		if ( ! token_is_value ) {
			all_fields.append( (String) null );
		}

		String[] result = new String[ all_fields.length() ];

		for (int i=0; 0 < all_fields.length(); i++) {
			result[i] = (String) all_fields.remove();
		}

		return result;
 	}

	public static void remove_quotes( String[] fields )
	{
		if (fields == null) return;
		
		for (int i=0; i < fields.length; i++) {
			if (fields[i] == null) continue;
			if (fields[i].charAt(0) == fields[i].charAt(fields[i].length()-1) &&
			   (fields[i].charAt(0) == '"' || fields[i].charAt(0) == '\'')) {
				
				fields[i] = fields[i].substring(1,fields[i].length()-1);
			}
		}
	}

	public static float toFloat( String field )
	{
		return (float) toDouble(field);
	}

	public static double toDouble( String field )
	{
		double result = 0;
		
		if (field == null) return result;
		
		boolean negative = false;
		int start = 0;
		
									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}
		
									// leading -
		if (start < field.length() && field.charAt(start) == '-') {
			negative = true;
			start += 1;
		}
		
									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}
		
									// skip leading $
		if (start < field.length() && field.charAt(start) == '$') {
			start += 1;
		}
		
									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}
		
									// leading -
		if (start < field.length() && field.charAt(start) == '-') {
			negative = true;
			start += 1;
		}
		
									// more leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}

		boolean fraction = false;
		double power = 0.1;
		for (int i=start; i < field.length(); i++) {
			if ('0' <= field.charAt(i) && field.charAt(i) <= '9') {
				if (! fraction) {
					result = result * 10 + field.charAt(i) - '0';
				} else {
					result = result + (field.charAt(i) - '0') * power;
					power /= 10.0;
				}
			} else if (field.charAt(i) == '.') {
				fraction = true;
			} else if (field.charAt(i) == ',') {
				continue;
			} else {
				break;
			}
		}
		
		return (negative) ? -result : result;
	}

	public static short toShort( String field )
	{
		return (short) toLong(field);
	}

	public static int toInt( String field )
	{
		return (int) toLong(field);
	}

	public static long toLong( String field )
	{
		long result = 0;
		
		if (field == null) return result;
		
		boolean negative = false;
		int start = 0;
		
									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}

									// leading -
		if (start < field.length() && field.charAt(start) == '-') {
			negative = true;
			start += 1;
		}

									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}

									// skip leading $
		if (start < field.length() && field.charAt(start) == '$') {
			start += 1;
		}

									// leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}

									// leading -
		if (start < field.length() && field.charAt(start) == '-') {
			negative = true;
			start += 1;
		}

									// more leading ws
		for (int i=start; i < field.length(); i++) {
			if (' ' < field.charAt(start)) {
				break;
			} else {
				start += 1;
			}
		}

		for (int i=start; i < field.length(); i++) {
			if ('0' <= field.charAt(i) && field.charAt(i) <= '9') {
				result = result * 10 + field.charAt(i) - '0';
			} else if (field.charAt(i) == ',') {
				continue;
			} else {
				break;
			}
		}
		
		return (negative) ? -result : result;
	}
	
	public static void main(String[] args)
	{
		String[] tokens = parse_csv_line("\"a\",b,c,1", ",;");
		for (int i=0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
		}
	}
}
