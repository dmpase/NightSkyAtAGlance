package nightskyataglance.gui;

/*******************************************************************************
 * Copyright (c) 2025-2025 Douglas M. Pase                                     *
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

public class AppearanceMenu extends Menu {
	private static final long serialVersionUID = 7693871871934518L;

	public final MainFrame main_frame;

	public static final String MIDNIGHT_CENTERED = "Center on Midnight";
	public static final String CUR_TIME_CENTERED = "Center on Current Time";
	public static final String SIDEREAL_GRID     = "Sidereal Grid";
	public static final String SOLAR_GRID        = "Solar Grid";
	public static final String NO_GRID           = "No Grid";
	public static final String HORIZON_LINES     = "Horizon Line";
	public static final String HORIZON_PLUS_15   = "Horizon Plus 15" + new String(Character.toChars(0x00B0));
	public static final String HORIZON_PLUS_30   = "Horizon Plus 30" + new String(Character.toChars(0x00B0));
	public static final String ECLIPTIC_PLANE    = "Ecliptic Plane";
	public static final String GALACTIC_PLANE    = "Galactic Plane";
	public static final String TIME_MARKERS      = "Time Markers";
	public static final String ZENITH_MARKER     = "Zenith Marker";
	public static final String BRIGHTER          = "Brighter";
	public static final String DARKER            = "Darker";
	public static final String RESET_COLORS      = "Reset Colors";
	public static final String HOURS_ONLY        = "Hours Only";
	public static final String HOURS_MINUTES     = "Hours & Minutes";

	public AppearanceMenu(MainFrame f, String name)
	{
		super(name);
		main_frame = f;

    	MenuItem mnc = new MenuItem(MIDNIGHT_CENTERED);
    	MenuItem ctc = new MenuItem(CUR_TIME_CENTERED);
    	MenuItem sid = new MenuItem(SIDEREAL_GRID);
    	MenuItem sol = new MenuItem(SOLAR_GRID);
    	MenuItem nog = new MenuItem(NO_GRID);
    	MenuItem hor = new MenuItem(HORIZON_LINES);
    	MenuItem h15 = new MenuItem(HORIZON_PLUS_15);
    	MenuItem h30 = new MenuItem(HORIZON_PLUS_30);
    	MenuItem ecl = new MenuItem(ECLIPTIC_PLANE);
    	MenuItem gal = new MenuItem(GALACTIC_PLANE);
    	// MenuItem stm = new MenuItem(TIME_MARKERS);
    	MenuItem zen = new MenuItem(ZENITH_MARKER);
    	// MenuItem ltr = new MenuItem(BRIGHTER);
    	// MenuItem dkr = new MenuItem(DARKER);
    	// MenuItem rst = new MenuItem(RESET_COLORS);
    	MenuItem hrs = new MenuItem(HOURS_ONLY);
    	MenuItem ham = new MenuItem(HOURS_MINUTES);

    	mnc.setEnabled(true);
    	add(mnc);
    	ctc.setEnabled(true);
    	add(ctc);
    	addSeparator();
    	sid.setEnabled(true);
    	add(sid);
    	sol.setEnabled(true);
    	add(sol);
    	nog.setEnabled(true);
    	add(nog);
    	addSeparator();
    	hor.setEnabled(true);
    	add(hor);
    	add(h15);
    	add(h30);
    	add(ecl);
    	add(gal);
    	// stm.setEnabled(true);
    	// add(stm);
    	zen.setEnabled(true);
    	add(zen);
    	addSeparator();
    	/*
    	ltr.setEnabled(true);
    	add(ltr);
    	dkr.setEnabled(true);
    	add(dkr);
    	rst.setEnabled(true);
    	add(rst);
    	addSeparator();
    	*/
    	add(hrs);
    	hrs.setEnabled(true);
    	add(ham);
    	ham.setEnabled(true);

    	addActionListener(new Listener());
	}

	public class Listener implements ActionListener {
		public Listener()
		{
		}
	
		@Override public void actionPerformed(ActionEvent evt)
		{
			//System.out.println(evt);
			Cursor old = main_frame.getCursor();
			main_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			String what = evt.getActionCommand();
			if (MIDNIGHT_CENTERED.equals(what)) {
				main_frame.midnight_centered = true;
			} else if (CUR_TIME_CENTERED.equals(what)) {
				main_frame.midnight_centered = false;
			} else if (SIDEREAL_GRID.equals(what)) {
				main_frame.grid_style = MainFrame.GridStyle.SIDEREAL_GRID;
			} else if (SOLAR_GRID.equals(what)) {
				main_frame.grid_style = MainFrame.GridStyle.SOLAR_GRID;
			} else if (NO_GRID.equals(what)) {
				main_frame.grid_style = MainFrame.GridStyle.NO_GRID;
			} else if (HORIZON_LINES.equals(what)) {
				main_frame.show_horizon_lines = ! main_frame.show_horizon_lines;
			} else if (HORIZON_PLUS_15.equals(what)) {
				main_frame.show_horizon_plus_15 = ! main_frame.show_horizon_plus_15;
			} else if (HORIZON_PLUS_30.equals(what)) {
				main_frame.show_horizon_plus_30 = ! main_frame.show_horizon_plus_30;
			} else if (ECLIPTIC_PLANE.equals(what)) {
				main_frame.show_ecliptic_plane = ! main_frame.show_ecliptic_plane;
			} else if (GALACTIC_PLANE.equals(what)) {
				main_frame.show_galactic_plane = ! main_frame.show_galactic_plane;
			} else if (TIME_MARKERS.equals(what)) {
				main_frame.show_time_markers = ! main_frame.show_time_markers;
			} else if (ZENITH_MARKER.equals(what)) {
				main_frame.show_zenith_marker = ! main_frame.show_zenith_marker;
			} else if (BRIGHTER.equals(what)) {
				MainFrame.brighter();
			} else if (DARKER.equals(what)) {
				MainFrame.darker();
			} else if (RESET_COLORS.equals(what)) {
				MainFrame.reset_color();
			} else if (HOURS_ONLY.equals(what)) {
				main_frame.scale_style = MainFrame.ScaleStyle.HOURS_ONLY;
			} else if (HOURS_MINUTES.equals(what)) {
				main_frame.scale_style = MainFrame.ScaleStyle.HOURS_MINUTES;
			} else {
		    	System.out.printf("%s: %3d: else (%s)...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), what);
			}
			main_frame.paint(main_frame.getGraphics());
			main_frame.setCursor(old);
		}
	
	}
}
