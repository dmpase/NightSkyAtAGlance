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

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import nightskyataglance.NightSkyAtAGlance;

public class OrientationMenu extends Menu {
	private static final long serialVersionUID = 7693871871934518L;

	public final MainFrame frame;

	public static final String ORIENT_NESW = "N/E/S/W";
	public static final String ORIENT_NWSE = "North on Top, West on Right";
	public static final String ORIENT_SENW = "S/E/N/W";
	public static final String ORIENT_SWNE = "S/W/N/E";

	public OrientationMenu(MainFrame f, String name)
	{
		super(name);
		frame = f;

    	MenuItem nesw = new MenuItem(ORIENT_NESW);
    	MenuItem nwse = new MenuItem(ORIENT_NWSE);
    	MenuItem senw = new MenuItem(ORIENT_SENW);
    	MenuItem swne = new MenuItem(ORIENT_SWNE);

    	nesw.setEnabled(false); add(nesw);
    	nwse.setEnabled(true); add(nwse);
    	senw.setEnabled(false); add(senw);
    	swne.setEnabled(false); add(swne);

    	addActionListener(new Listener());
	}

	public class Listener implements ActionListener {
		public Listener()
		{
		}
	
		@Override public void actionPerformed(ActionEvent evt)
		{
			//System.out.println(evt);
			Cursor old = frame.getCursor();
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			String what = evt.getActionCommand();
			if (ORIENT_NESW.equals(what)) {
				frame.set_NESW();
			} else if (ORIENT_NWSE.equals(what)) {
				frame.set_NWSE();
			} else if (ORIENT_SENW.equals(what)) {
				frame.set_SENW();
			} else if (ORIENT_SWNE.equals(what)) {
				frame.set_SWNE();
			} else {
		    	System.out.printf("%s: %3d: else (%s)...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), what);
			}
			frame.setCursor(old);
		}
	
	}
}
