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

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class TimeLocationMenu extends Menu {
	private static final long serialVersionUID = 7693871871934518L;

	public final MainFrame frame;

	public static final String SET_US_CITY    = "Set US City";
	public static final String SET_WORLD_CITY = "Set World City";
	public static final String SET_LAT_LON    = "Set Latitude & Longitude";
	public static final String SET_TIME_ZONE  = "Set Time Zone";
	public static final String SET_DATE       = "Set Date";
	public static final String SET_TIME       = "Set Time";
	public static final String SET_HEARTBEAT  = "Set Heartbeat Period";

	public static final MenuItem us    = new MenuItem(SET_US_CITY,    new MenuShortcut(KeyEvent.VK_U));
	public static final MenuItem world = new MenuItem(SET_WORLD_CITY, new MenuShortcut(KeyEvent.VK_W));
	public static final MenuItem loc   = new MenuItem(SET_LAT_LON,    new MenuShortcut(KeyEvent.VK_L));
	public static final MenuItem zone  = new MenuItem(SET_TIME_ZONE,  new MenuShortcut(KeyEvent.VK_Z));
	public static final MenuItem date  = new MenuItem(SET_DATE,       new MenuShortcut(KeyEvent.VK_D));
	public static final MenuItem time  = new MenuItem(SET_TIME,       new MenuShortcut(KeyEvent.VK_T));
	public static final MenuItem beat  = new MenuItem(SET_HEARTBEAT);

	public TimeLocationMenu(MainFrame f, String name)
	{
		super(name);
		frame = f;

		setFont(PersistentState.get_font());

    	add(us);    us   .setEnabled(frame.us_cities    != null);
    	add(world); world.setEnabled(frame.world_cities != null);
    	add(loc);
    	addSeparator();
    	add(zone);
    	add(date);
    	add(time);
    	addSeparator();
    	add(beat);

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
			if (SET_US_CITY.equals(what)) {
		    	new SetUsCityDialog(frame, SET_US_CITY);
			} else if (SET_WORLD_CITY.equals(what)) {
		    	new SetWorldCityDialog(frame, SET_WORLD_CITY);
			} else if (SET_LAT_LON.equals(what)) {
		    	new SetLocationDialog(frame, SET_LAT_LON);
			} else if (SET_TIME_ZONE.equals(what)) {
		    	new TimeZoneDialog(SET_TIME_ZONE, frame);
			} else if (SET_DATE.equals(what)) {
		    	new SetDateDialog(frame, SET_DATE, true, System.currentTimeMillis(), PersistentState.timezone);
			} else if (SET_TIME.equals(what)) {
		    	new SetTimeDialog(frame, SET_TIME);
			} else if (SET_HEARTBEAT.equals(what)) {
		    	new SetHeartbeatDialog(frame, SET_HEARTBEAT);
			} else {
		    	System.out.printf("%s: %3d: else...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			}
			frame.setCursor(old);
		}
	}
}
