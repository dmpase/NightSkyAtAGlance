package nightskyataglance.gui;

/*******************************************************************************
 * Copyright (c) 2025-2026 Douglas M. Pase                                     *
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
 * infringement of that parties intellectual property rights.                  *
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyboardEventHandler implements KeyListener {
	public final MainFrame frame;

	public KeyboardEventHandler(MainFrame f)
	{
		frame = f;
	}
	

	@Override public void keyPressed(KeyEvent evt)
	{
		// System.out.println(evt);
		int kc = evt.getKeyCode();
		switch (kc) {
		case KeyEvent.VK_ALT :
			// System.out.printf("%s %d ... ALT  %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			this.alt = true;
			break;
		case KeyEvent.VK_CONTROL :
			// System.out.printf("%s %d ... CTRL %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			this.ctrl = true;
			break;
		case KeyEvent.VK_SHIFT :
			// System.out.printf("%s %d ... SHFT %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			this.shift = true;
			break;
		default :
			this.key = kc;
			break;
		}
	}

	@Override public void keyReleased(KeyEvent evt)
	{
		// System.out.println(evt);
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_ALT :
			// System.out.printf("%s %d ... alt %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			this.alt = false;
			break;
		case KeyEvent.VK_CONTROL :
			// System.out.printf("%s %d ... ctrl%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			this.ctrl = false;
			break;
		case KeyEvent.VK_SHIFT :
			this.shift = false;
			// System.out.printf("%s %d ... shft%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			break;
		default :
			char key_char = evt.getKeyChar();
			int  key_code = evt.getKeyCode();
			if (key_char == '+') {
				// System.out.printf("%s %d ... %4s %4s %4s PLUS%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
				frame.time_keeper.pause = true;
				frame.show_zoom = true;
			} else if (key_char == '-') {
				// System.out.printf("%s %d ... %4s %4s %4s MINUS%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
				frame.time_keeper.pause = true;
				frame.show_zoom = true;
			} else if (key_char == '0') {
				// System.out.printf("%s %d ... %4s %4s %4s 0%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '1') {
				// System.out.printf("%s %d ... %4s %4s %4s 1%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '2') {
				// System.out.printf("%s %d ... %4s %4s %4s 2%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '3') {
				// System.out.printf("%s %d ... %4s %4s %4s 3%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '4') {
				// System.out.printf("%s %d ... %4s %4s %4s 4%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '5') {
				// System.out.printf("%s %d ... %4s %4s %4s 5%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '6') {
				// System.out.printf("%s %d ... %4s %4s %4s 6%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '7') {
				// System.out.printf("%s %d ... %4s %4s %4s 7%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '8') {
				// System.out.printf("%s %d ... %4s %4s %4s 8%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_char == '9') {
				// System.out.printf("%s %d ... %4s %4s %4s 9%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_code == KeyEvent.VK_ESCAPE) {	// escape
				// System.out.printf("%s %d ... %4s %4s %4s ESC%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
				frame.time_keeper.pause = false;
				frame.show_zoom = false;
			} else if (key_code == KeyEvent.VK_ENTER) {	// enter
				// System.out.printf("%s %d ... %4s %4s %4s ENTER%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
				frame.time_keeper.pause = false;
				frame.show_zoom = false;
			} else if (key_code == KeyEvent.VK_UP) {	// up
				// System.out.printf("%s %d ... %4s %4s %4s UP   %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_code == KeyEvent.VK_DOWN) {	// down
				// System.out.printf("%s %d ... %4s %4s %4s DOWN %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_code == KeyEvent.VK_LEFT) {	// left
				// System.out.printf("%s %d ... %4s %4s %4s LEFT %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			} else if (key_code == KeyEvent.VK_RIGHT) {	// right
				// System.out.printf("%s %d ... %4s %4s %4s RIGHT%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (alt?"ALT":""), (ctrl?"CTRL":""), (shift?"SHFT":""));
			}
			break;
		}
	}

	@Override public void keyTyped(KeyEvent evt)
	{
   		// System.out.println(evt);
	}

	@SuppressWarnings("unused")
	private int     key   = 0x0;
	@SuppressWarnings("unused")
	private boolean ctrl  = false;
	@SuppressWarnings("unused")
	private boolean alt   = false;
	@SuppressWarnings("unused")
	private boolean shift = false;
}

