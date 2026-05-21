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

import java.util.*;

import lib.util.Set;

@SuppressWarnings("unused")
class Parser {

	public final String cn = this.getClass().getName();

    public Parser()
    {
    }

    public Parser( TokenValue[] k, TokenValue l, TokenValue t )
    {
    	keywords = k;
    	letter   = l;
    	text_end = t;
    }

				// support (), [], ?, *, +, ., ^, $ and |
				// use embedded "\" to indicate the next character
				// is a metacharacter

				// support the following left-associative grammar:
					// ( 1) start => exp
					// ( 2)     |  ^ exp
					// ( 3)     |  exp $
					// ( 4)     |  ^ exp $

					// ( 5) exp => exp exp
					// ( 6)     |  exp | exp
					// ( 7)     |  exp ?
					// ( 8)     |  exp *
					// ( 9)     |  exp +
					// (10)     |  ( exp )
					// (11)     |  .
					// (12)     |  [  char ... char ]
					// (13)     |  [- char ... char ]
					// (14)     |  char
    public ParseTree parse( String pattern )
    {
    	token_stream = new Tokenizer( pattern, keywords, enter_char_class, exit_char_class, letter, text_end );

    	Stack<StackEntry> s = new Stack<StackEntry>();
    	s.push( new StackEntry( null, null, 0 ) );

    	TokenValue token = token_stream.next_token();
    	// System.out.println( "next token: " + tts[token.type] + " : '" + token.value + "'");

    	// for the case [...0-9...]
    	char prev_m1 = 0;
    	char prev_m2 = 0;

    	ParseTree result = null;
    	while ( true ) {
    		// determine the action
    		StackEntry top = (StackEntry) s.peek();

    		// look at the input and state at the top of the stack
    		int action = actions[ top.state ][ token.type ][ ACTION ];
    		int target = actions[ top.state ][ token.type ][ TARGET ];
    		
    		// System.out.println("actions["+ top.state +"]["+ token +"] = "+ats[action]+" "+target);

    		if (action == ACCEPT) {
    			// ACCEPT:
    			// System.out.println( "ACCEPT" );
    			result = ((StackEntry) s.peek()).exp;
    			break;
    		} else if (action == SHIFT) {
    			// SHIFT S:
    			int state = target;

    			// System.out.println( "SHIFT " + state + " (" + token + ")" );

    			// push the input token and state S onto the stack
    			s.push( new StackEntry( token, null, state ) );
    			token = token_stream.next_token();

    			// System.out.println( "next token: " + tts[token.type] + " : '" + token.value + "'");

    		} else if (action == REDUCE) {
    			// REDUCE P:
    			int production = target;
    			int rule       = prod_data[ production ][ RULE ];
    			int num_syms   = prod_data[ production ][ SYMBOLS ];

    			// System.out.println( "REDUCE " + production );

    			// pop all elements of production P off the stack
    			StackEntry[] elements = new StackEntry[ num_syms ];
    			for (int i=0; i < elements.length; i++) {
    				elements[ elements.length - 1 - i ] = (StackEntry) s.pop();
    			}

    			ParseTree exp = null;
    			// take whatever action the user has defined for
    			// the recognition of this production, such as
				// building a structure representing P from the
				// elements ($1, $2, $3, ...)
    			switch ( production ) {
    			case  0:	// ( 0) $accept : start $end
    				// System.out.println( "( 0) $accept : exp $end" );
    				break;
    			case  1:	// ( 1) start : exp
    				// System.out.println( "( 1) start : exp" );
    				exp = elements[0].exp;
    				break;
    			case  2:	// ( 2) start : CARAT exp
    				// System.out.println( "( 2) start : CARAT exp" );
    				exp = new RESeq( new RECarat(), elements[1].exp );
    				break;
    			case  3:	// ( 3) start : exp DOLLAR
    				// System.out.println( "( 3) start : exp DOLLAR" );
    				exp = new RESeq( elements[0].exp, new REDollar() );
    				break;
    			case  4:	// ( 4) start : CARAT exp DOLLAR
    				// System.out.println( "( 4) exp : exp exp" );
    				exp = new RESeq( new RECarat(), elements[1].exp );
    				exp = new RESeq( exp, new REDollar() );
    				break;
    			case  5:	// ( 5) exp : exp BAR exp
    				// System.out.println( "( 5) exp : exp BAR exp" );
    				exp = new REOr( elements[0].exp, elements[2].exp );
    				break;
    			case  6:	// ( 6) exp : exp exp
    				// System.out.println( "( 6) exp : exp exp" );
    				exp = new RESeq( elements[0].exp, elements[1].exp );
    				break;
    			case  7:	// ( 7) exp : exp PLUS
    				// System.out.println( "( 7) exp : exp PLUS" );
    				exp = new REPlus( elements[0].exp );
    				break;
				case  8:	// ( 8) exp : exp SPLAT
					// System.out.println( "( 8) exp : exp SPLAT" );
				    exp = new RESplat( elements[0].exp );
				    break;
				case  9:	// ( 9) exp : exp QUESTION
					// System.out.println( "( 9) exp : exp QUESTION" );
				    exp = new REQuestion( elements[0].exp );
				    break;
				case 10:	// (10) exp : LPAREN exp RPAREN
					// System.out.println( "(10) exp : LPAREN exp RPAREN" );
				    exp = elements[1].exp;
				    break;
				case 11:	// (11) exp : DOT
					// System.out.println( "(11) exp : DOT" );
				    exp = new RECharClass( (new Set( 0x100 )).inverse() );
				    break;
				case 12:	// (12) exp : LETTER
					// System.out.println( "(12) exp : LETTER" );
				    exp = new RELetter( elements[0].token.value.charAt(0) );
				    break;
				case 13:	// (13) exp : LBRACK list RBRACK
					// System.out.println( "(13) exp : LBRACK exp RBRACK" );
				    exp = elements[1].exp;
				    break;
				case 14:	// (14) exp : NLBRACK list RBRACK
					// System.out.println( "(14) exp : NLBRACK exp RBRACK" );
				    RECharClass ncc = (RECharClass) elements[1].exp;
				    ((Set)ncc.value).inverse();
				    exp = ncc;
				    break;
				case 15:	// (15) list : list LETTER
					// System.out.println( "(15) list : list LETTER" );
				    RECharClass cc = (RECharClass) elements[0].exp;
				    ((Set)cc.value).set( elements[1].token.value.charAt(0) );
				    exp = cc;

				    // handle the case [...0-9...] 
				    if (prev_m1 == '-') {
				    	char cur = elements[1].token.value.charAt(0);
				    	for (int i=prev_m2; i < cur; i++) {
						    ((Set)cc.value).set( i );
				    	}
					    ((Set)cc.value).clear( '-' );
				    } else {
					    prev_m2 = prev_m1;
					    prev_m1 = elements[1].token.value.charAt(0);
				    }
				    break;
				case 16:	// (16) list : LETTER
					// System.out.println( "(16) list : LETTER" );
				    Set charclass = new Set( 0x100 );
				    charclass.set( elements[0].token.value.charAt(0) );
				    exp = new RECharClass( charclass );
				    
				    // for the case [...0-9...]
				    prev_m2 = 0;
				    prev_m1 = elements[0].token.value.charAt(0);
				    break;
				default :	// error!
					// System.out.println( "(default) : error!" );
				    break;
				}

    			// find the new state associated with the state on
				// the top of the stack and the production P using
				// the goto table
    			int top_state = ((StackEntry)s.peek()).state;
    			int state = goto_map[ top_state ][ rule ];
    			
    			// System.out.println("state = goto_map[" + top_state + "][" + rule + "] = " + state);

				// push the new value ($$) and state onto the stack
    			s.push( new StackEntry( null, exp, state ) );
    		} else {
    			// ERROR:
    			 System.out.println( "ERROR: error!" );
    			break;
    		}
    	}

    	return result;
    }


    private static final short START = 1;
    private static final short EXP   = 2;
    private static final short LIST  = 3;

    private static final short ACCEPT = 0;
    private static final short SHIFT  = 1;
    private static final short REDUCE = 2;
    private static final short ERROR  = 3;

	private static final String[] ats = { "ACCEPT", "SHIFT", "REDUCE", "ERROR" };

    private static final short RULE    = 0;
    private static final short SYMBOLS = 1;

				// prod_data[ <production> ][ <value> ]
				// <value> is one of { RULE | SYMBOLS }
    private static final short[][] prod_data = {
		{ ACCEPT, 2 },		// ( 0)	$accept : start $end
		{ START,  1 },		// ( 1)	start : exp
		{ START,  2 },		// ( 2)	start : CARAT exp
		{ START,  2 },		// ( 3)	start : exp DOLLAR
		{ START,  3 },		// ( 4)	start : CARAT exp DOLLAR
		{ EXP,    3 },		// ( 5)	exp   : exp BAR exp
		{ EXP,    2 },		// ( 6)	exp   : exp exp
		{ EXP,    2 },		// ( 7)	exp   : exp PLUS
		{ EXP,    2 },		// ( 8)	exp   : exp SPLAT
		{ EXP,    2 },		// ( 9)	exp   : exp QUESTION
		{ EXP,    3 },		// (10)	exp   : LPAREN exp RPAREN
		{ EXP,    1 },		// (11)	exp   : DOT
		{ EXP,    1 },		// (12)	exp   : LETTER
		{ EXP,    3 },		// (13)	exp   : LBRACK list RBRACK
		{ EXP,    3 },		// (14)	exp   : NLBRACK list RBRACK
		{ LIST,   2 },		// (15)	list  : list LETTER
		{ LIST,   1 },		// (16)	list  : LETTER
    };

    private static final short ACTION = 0;
    private static final short TARGET = 1;

				// actions[ <state> ][ <token> ][ <value> ]
				// <value> is one of { ACTION | TARGET }
    private static final short[][][] actions = {
		{			// state 0
					// $accept : . start $end  (0)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { SHIFT,   4 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 1
					// exp : LETTER .  (12)
		    { REDUCE, 12 },		// LETTER
		    { REDUCE, 12 },		// LPAREN
		    { REDUCE, 12 },		// RPAREN
		    { REDUCE, 12 },		// BAR
		    { REDUCE, 12 },		// SPLAT
		    { REDUCE, 12 },		// PLUS
		    { REDUCE, 12 },		// QUESTION
		    { REDUCE, 12 },		// DOT
		    { REDUCE, 12 },		// CARAT
		    { REDUCE, 12 },		// DOLLAR
		    { REDUCE, 12 },		// LBRACK
		    { REDUCE, 12 },		// RBRACK
		    { REDUCE, 12 },		// NLBRACK
		    { REDUCE, 12 },		// END
		}, {			// state 2
					// exp : LPAREN . exp RPAREN  (10)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 3
					// exp : DOT .  (11)
		    { REDUCE, 11 },		// LETTER
		    { REDUCE, 11 },		// LPAREN
		    { REDUCE, 11 },		// RPAREN
		    { REDUCE, 11 },		// BAR
		    { REDUCE, 11 },		// SPLAT
		    { REDUCE, 11 },		// PLUS
		    { REDUCE, 11 },		// QUESTION
		    { REDUCE, 11 },		// DOT
		    { REDUCE, 11 },		// CARAT
		    { REDUCE, 11 },		// DOLLAR
		    { REDUCE, 11 },		// LBRACK
		    { REDUCE, 11 },		// RBRACK
		    { REDUCE, 11 },		// NLBRACK
		    { REDUCE, 11 },		// END
		}, {			// state 4
					// start : CARAT . exp  (2)
					// start : CARAT . exp DOLLAR  (4)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 5
					// exp : LBRACK . list RBRACK  (13)
		    { SHIFT,  11 },		// LETTER
		    { ERROR,   0 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { ERROR,   0 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { ERROR,   0 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { ERROR,   0 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 6
					// exp : NLBRACK . list RBRACK  (14)
		    { SHIFT,  11 },		// LETTER
		    { ERROR,   0 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { ERROR,   0 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { ERROR,   0 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { ERROR,   0 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 7
					// $accept : start . $end  (0)
		    { ERROR,   0 },		// LETTER
		    { ERROR,   0 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { ERROR,   0 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { ERROR,   0 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { ERROR,   0 },		// NLBRACK
		    { ACCEPT,  0 },		// END
		}, {			// state 8
					// start : exp .  (1)
					// start : exp . DOLLAR  (3)
					// exp : exp . BAR exp  (5)
					// exp : exp . exp  (6)
					// exp : exp . PLUS  (7)
					// exp : exp . SPLAT  (8)
					// exp : exp . QUESTION  (9)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { SHIFT,  14 },		// BAR
		    { SHIFT,  15 },		// SPLAT
		    { SHIFT,  16 },		// PLUS
		    { SHIFT,  17 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { SHIFT,  18 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { REDUCE,  1 },		// END
		}, {			// state 9
					// exp : exp . BAR exp  (5)
					// exp : exp . exp  (6)
					// exp : exp . PLUS  (7)
					// exp : exp . SPLAT  (8)
					// exp : exp . QUESTION  (9)
					// exp : LPAREN exp . RPAREN  (10)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { SHIFT,  20 },		// RPAREN
		    { SHIFT,  14 },		// BAR
		    { SHIFT,  15 },		// SPLAT
		    { SHIFT,  16 },		// PLUS
		    { SHIFT,  17 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 10
					// start : CARAT exp .  (2)
					// start : CARAT exp . DOLLAR  (4)
					// exp : exp . BAR exp  (5)
					// exp : exp . exp  (6)
					// exp : exp . PLUS  (7)
					// exp : exp . SPLAT  (8)
					// exp : exp . QUESTION  (9)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { SHIFT,  14 },		// BAR
		    { SHIFT,  15 },		// SPLAT
		    { SHIFT,  16 },		// PLUS
		    { SHIFT,  17 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { SHIFT,  21 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { REDUCE,  2 },		// END
		}, {			// state 11
					// list : LETTER .  (16)
		    { REDUCE, 16 },		// LETTER
		    { REDUCE, 16 },		// LPAREN
		    { REDUCE, 16 },		// RPAREN
		    { REDUCE, 16 },		// BAR
		    { REDUCE, 16 },		// SPLAT
		    { REDUCE, 16 },		// PLUS
		    { REDUCE, 16 },		// QUESTION
		    { REDUCE, 16 },		// DOT
		    { REDUCE, 16 },		// CARAT
		    { REDUCE, 16 },		// DOLLAR
		    { REDUCE, 16 },		// LBRACK
		    { REDUCE, 16 },		// RBRACK
		    { REDUCE, 16 },		// NLBRACK
		    { REDUCE, 16 },		// END
		}, {			// state 12
					// exp : LBRACK list . RBRACK  (13)
					// list : list . LETTER  (15)
		    { SHIFT,  22 },		// LETTER
		    { ERROR,   0 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { ERROR,   0 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { ERROR,   0 },		// LBRACK
		    { SHIFT,  23 },		// RBRACK
		    { ERROR,   0 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 13
					// exp : NLBRACK list . RBRACK  (14)
					// list : list . LETTER  (15)
		    { SHIFT,  22 },		// LETTER
		    { ERROR,   0 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { ERROR,   0 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { ERROR,   0 },		// LBRACK
		    { SHIFT,  24 },		// RBRACK
		    { ERROR,   0 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 14
					// exp : exp BAR . exp  (5)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { ERROR,   0 },		// RPAREN
		    { ERROR,   0 },		// BAR
		    { ERROR,   0 },		// SPLAT
		    { ERROR,   0 },		// PLUS
		    { ERROR,   0 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { ERROR,   0 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { ERROR,   0 },		// END
		}, {			// state 15
					// exp : exp SPLAT .  (8)
		    { REDUCE,  8 },		// LETTER
		    { REDUCE,  8 },		// LPAREN
		    { REDUCE,  8 },		// RPAREN
		    { REDUCE,  8 },		// BAR
		    { REDUCE,  8 },		// SPLAT
		    { REDUCE,  8 },		// PLUS
		    { REDUCE,  8 },		// QUESTION
		    { REDUCE,  8 },		// DOT
		    { REDUCE,  8 },		// CARAT
		    { REDUCE,  8 },		// DOLLAR
		    { REDUCE,  8 },		// LBRACK
		    { REDUCE,  8 },		// RBRACK
		    { REDUCE,  8 },		// NLBRACK
		    { REDUCE,  8 },		// END
		}, {			// state 16
					// exp : exp PLUS .  (7)
		    { REDUCE,  7 },		// LETTER
		    { REDUCE,  7 },		// LPAREN
		    { REDUCE,  7 },		// RPAREN
		    { REDUCE,  7 },		// BAR
		    { REDUCE,  7 },		// SPLAT
		    { REDUCE,  7 },		// PLUS
		    { REDUCE,  7 },		// QUESTION
		    { REDUCE,  7 },		// DOT
		    { REDUCE,  7 },		// CARAT
		    { REDUCE,  7 },		// DOLLAR
		    { REDUCE,  7 },		// LBRACK
		    { REDUCE,  7 },		// RBRACK
		    { REDUCE,  7 },		// NLBRACK
		    { REDUCE,  7 },		// END
		}, {			// state 17
					// exp : exp QUESTION .  (9)
		    { REDUCE,  9 },		// LETTER
		    { REDUCE,  9 },		// LPAREN
		    { REDUCE,  9 },		// RPAREN
		    { REDUCE,  9 },		// BAR
		    { REDUCE,  9 },		// SPLAT
		    { REDUCE,  9 },		// PLUS
		    { REDUCE,  9 },		// QUESTION
		    { REDUCE,  9 },		// DOT
		    { REDUCE,  9 },		// CARAT
		    { REDUCE,  9 },		// DOLLAR
		    { REDUCE,  9 },		// LBRACK
		    { REDUCE,  9 },		// RBRACK
		    { REDUCE,  9 },		// NLBRACK
		    { REDUCE,  9 },		// END
		}, {			// state 18
					// start : exp DOLLAR .  (3)
		    { REDUCE,  3 },		// LETTER
		    { REDUCE,  3 },		// LPAREN
		    { REDUCE,  3 },		// RPAREN
		    { REDUCE,  3 },		// BAR
		    { REDUCE,  3 },		// SPLAT
		    { REDUCE,  3 },		// PLUS
		    { REDUCE,  3 },		// QUESTION
		    { REDUCE,  3 },		// DOT
		    { REDUCE,  3 },		// CARAT
		    { REDUCE,  3 },		// DOLLAR
		    { REDUCE,  3 },		// LBRACK
		    { REDUCE,  3 },		// RBRACK
		    { REDUCE,  3 },		// NLBRACK
		    { REDUCE,  3 },		// END
		}, {			// state 19
					// exp : exp . BAR exp  (5)
					// exp : exp . exp  (6)
					// exp : exp exp .  (6)
					// exp : exp . PLUS  (7)
					// exp : exp . SPLAT  (8)
					// exp : exp . QUESTION  (9)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { REDUCE,  6 },		// RPAREN
		    { REDUCE,  6 },		// BAR
		    { SHIFT,  15 },		// SPLAT
		    { SHIFT,  16 },		// PLUS
		    { SHIFT,  17 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { REDUCE,  6 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { REDUCE,  6 },		// END
		}, {			// state 20
					// exp : LPAREN exp RPAREN .  (10)
		    { REDUCE, 10 },		// LETTER
		    { REDUCE, 10 },		// LPAREN
		    { REDUCE, 10 },		// RPAREN
		    { REDUCE, 10 },		// BAR
		    { REDUCE, 10 },		// SPLAT
		    { REDUCE, 10 },		// PLUS
		    { REDUCE, 10 },		// QUESTION
		    { REDUCE, 10 },		// DOT
		    { REDUCE, 10 },		// CARAT
		    { REDUCE, 10 },		// DOLLAR
		    { REDUCE, 10 },		// LBRACK
		    { REDUCE, 10 },		// RBRACK
		    { REDUCE, 10 },		// NLBRACK
		    { REDUCE, 10 },		// END
		}, {			// state 21
					// start : CARAT exp DOLLAR .  (4)
		    { REDUCE,  4 },		// LETTER
		    { REDUCE,  4 },		// LPAREN
		    { REDUCE,  4 },		// RPAREN
		    { REDUCE,  4 },		// BAR
		    { REDUCE,  4 },		// SPLAT
		    { REDUCE,  4 },		// PLUS
		    { REDUCE,  4 },		// QUESTION
		    { REDUCE,  4 },		// DOT
		    { REDUCE,  4 },		// CARAT
		    { REDUCE,  4 },		// DOLLAR
		    { REDUCE,  4 },		// LBRACK
		    { REDUCE,  4 },		// RBRACK
		    { REDUCE,  4 },		// NLBRACK
		    { REDUCE,  4 },		// END
		}, {			// state 22
					// list : list LETTER .  (15)
		    { REDUCE, 15 },		// LETTER
		    { REDUCE, 15 },		// LPAREN
		    { REDUCE, 15 },		// RPAREN
		    { REDUCE, 15 },		// BAR
		    { REDUCE, 15 },		// SPLAT
		    { REDUCE, 15 },		// PLUS
		    { REDUCE, 15 },		// QUESTION
		    { REDUCE, 15 },		// DOT
		    { REDUCE, 15 },		// CARAT
		    { REDUCE, 15 },		// DOLLAR
		    { REDUCE, 15 },		// LBRACK
		    { REDUCE, 15 },		// RBRACK
		    { REDUCE, 15 },		// NLBRACK
		    { REDUCE, 15 },		// END
		}, {			// state 23
					// exp : LBRACK list RBRACK .  (13)
		    { REDUCE, 13 },		// LETTER
		    { REDUCE, 13 },		// LPAREN
		    { REDUCE, 13 },		// RPAREN
		    { REDUCE, 13 },		// BAR
		    { REDUCE, 13 },		// SPLAT
		    { REDUCE, 13 },		// PLUS
		    { REDUCE, 13 },		// QUESTION
		    { REDUCE, 13 },		// DOT
		    { REDUCE, 13 },		// CARAT
		    { REDUCE, 13 },		// DOLLAR
		    { REDUCE, 13 },		// LBRACK
		    { REDUCE, 13 },		// RBRACK
		    { REDUCE, 13 },		// NLBRACK
		    { REDUCE, 13 },		// END
		}, {			// state 24
					// exp : NLBRACK list RBRACK .  (14)
		    { REDUCE, 14 },		// LETTER
		    { REDUCE, 14 },		// LPAREN
		    { REDUCE, 14 },		// RPAREN
		    { REDUCE, 14 },		// BAR
		    { REDUCE, 14 },		// SPLAT
		    { REDUCE, 14 },		// PLUS
		    { REDUCE, 14 },		// QUESTION
		    { REDUCE, 14 },		// DOT
		    { REDUCE, 14 },		// CARAT
		    { REDUCE, 14 },		// DOLLAR
		    { REDUCE, 14 },		// LBRACK
		    { REDUCE, 14 },		// RBRACK
		    { REDUCE, 14 },		// NLBRACK
		    { REDUCE, 14 },		// END
		}, {			// state 25
					// exp : exp . BAR exp  (5)
					// exp : exp BAR exp .  (5)
					// exp : exp . exp  (6)
					// exp : exp . PLUS  (7)
					// exp : exp . SPLAT  (8)
					// exp : exp . QUESTION  (9)
		    { SHIFT,   1 },		// LETTER
		    { SHIFT,   2 },		// LPAREN
		    { REDUCE,  5 },		// RPAREN
		    { REDUCE,  5 },		// BAR
		    { SHIFT,  15 },		// SPLAT
		    { SHIFT,  16 },		// PLUS
		    { SHIFT,  17 },		// QUESTION
		    { SHIFT,   3 },		// DOT
		    { ERROR,   0 },		// CARAT
		    { REDUCE,  5 },		// DOLLAR
		    { SHIFT,   5 },		// LBRACK
		    { ERROR,   0 },		// RBRACK
		    { SHIFT,   6 },		// NLBRACK
		    { REDUCE,  5 },		// END                             DMP (was 4)
		},
    };

				// goto_map[ <state> ][ <rule> ] = new state
    private static final short[][] goto_map = {
	// ACCEPT
	    // START
		// EXP
		    // LIST
		{ -1,  7,  8, -1 },	// state  0: start goto 7; exp goto 8
		{ -1, -1, -1, -1 },	// state  1: 
		{ -1, -1,  9, -1 },	// state  2: exp goto 9
		{ -1, -1, -1, -1 },	// state  3:
		{ -1, -1, 10, -1 },	// state  4: exp goto 10
		{ -1, -1, -1, 12 },	// state  5: list goto 12
		{ -1, -1, -1, 13 },	// state  6: list goto 13
		{ -1, -1, -1, -1 },	// state  7:
		{ -1, -1, 19, -1 },	// state  8: exp goto 19
		{ -1, -1, 19, -1 },	// state  9: exp goto 19
		{ -1, -1, 19, -1 },	// state 10: exp goto 19
		{ -1, -1, -1, -1 },	// state 11:
		{ -1, -1, -1, -1 },	// state 12:
		{ -1, -1, -1, -1 },	// state 13:
		{ -1, -1, 25, -1 },	// state 14: exp goto 25
		{ -1, -1, -1, -1 },	// state 15:
		{ -1, -1, -1, -1 },	// state 16:
		{ -1, -1, -1, -1 },	// state 17:
		{ -1, -1, -1, -1 },	// state 18:
		{ -1, -1, 19, -1 },	// state 19: exp goto 19
		{ -1, -1, -1, -1 },	// state 20:
		{ -1, -1, -1, -1 },	// state 21:
		{ -1, -1, -1, -1 },	// state 22:
		{ -1, -1, -1, -1 },	// state 23:
		{ -1, -1, -1, -1 },	// state 24:
		{ -1, -1, 19, -1 },	// state 25: exp goto 19
    };

    
    // list of tokens and assigned values
    public static final short LETTER   =  0;		// a
    public static final short LPAREN   =  1;		// (
	public static final short RPAREN   =  2;		// )
	public static final short BAR      =  3;		// |
	public static final short SPLAT    =  4;		// *
	public static final short PLUS     =  5;		// +
	public static final short QUESTION =  6;		// ?
	public static final short DOT      =  7;		// .
	public static final short CARAT    =  8;		// ^
	public static final short DOLLAR   =  9;		// $
	public static final short LBRACK   = 10;		// [
	public static final short RBRACK   = 11;		// ]
	public static final short NLBRACK  = 12;		// [-
	public static final short END      = 13;		// end-of-string

    private static final String[] tts = { 
    	"LETTER", 
    	"LPAREN", 
    	"RPAREN", 
    	"BAR", 
    	"SPLAT", 
    	"PLUS", 
    	"QUESTION", 
    	"DOT", 
    	"CARAT", 
    	"DOLLAR", 
    	"LBRACK", 
    	"RBRACK", 
    	"NLBRACK", 
    	"END", 
   	};

    private Tokenizer  token_stream;
    private TokenValue letter   = new TokenValue( Parser.LETTER,   "a" );
    private TokenValue lparen   = new TokenValue( Parser.LPAREN,   "("  );
    private TokenValue rparen   = new TokenValue( Parser.RPAREN,   ")"  );
    private TokenValue bar      = new TokenValue( Parser.BAR,      "|"  );
    private TokenValue splat    = new TokenValue( Parser.SPLAT,    "*"  );
    private TokenValue plus     = new TokenValue( Parser.PLUS,     "+"  );
    private TokenValue question = new TokenValue( Parser.QUESTION, "?"  );
    private TokenValue dot      = new TokenValue( Parser.DOT,       "."  );
    private TokenValue carat    = new TokenValue( Parser.CARAT,     "^"  );
    private TokenValue dollar   = new TokenValue( Parser.DOLLAR,    "$"  );
    private TokenValue nlbrack  = new TokenValue( Parser.NLBRACK,   "[-" );
    private TokenValue lbrack   = new TokenValue( Parser.LBRACK,    "["  );
    private TokenValue rbrack   = new TokenValue( Parser.RBRACK,    "]" );
    private TokenValue text_end = new TokenValue( Parser.END,       "$END" );

    private TokenValue[] keywords = {
    	letter,
		lparen,
		rparen,
		bar,
		splat,
		plus,
		question,
		dot,
		carat,
		dollar,
		nlbrack,
		lbrack,
		rbrack,
    };

    private TokenValue[] enter_char_class = {
		nlbrack,
		lbrack,
    };

    private TokenValue[] exit_char_class = {
		rbrack,
    };
}
