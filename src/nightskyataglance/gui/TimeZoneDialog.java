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


import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;

import lib.util.Queue;
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class TimeZoneDialog extends Dialog {
	private static final long serialVersionUID = 2997686506146528274L;

	String category = null;

	public final MainFrame frame;

	public String   timezone_master_category_list[] = null;
	public String   timezone_list[]                 = null;
	public String   timezone_display_name_list[]    = null;
	public String   timezone_id_list[]              = null;
	public String   timezone_category_list[]        = null;

	public String   category_display_name_table[][] = null;
	public String   category_timezone_id_table[][]  = null;
	public String[] us_short_table = null;
	public String[] us_long_table  = null;
	public int[]    cat_idx        = null;
	
	public Hashtable<String,Integer> timezone_index = null;
	public Hashtable<String,Integer> category_index = new Hashtable<String,Integer>();

    public static final String ZONE_LOCAL     = "Local";
    public static final String ZONE_MISC      = "Misc";
    public static final String ZONE_UTC       = "UTC";

	public enum DaylightSavingsTime { LOCAL, STANDARD, DAYLIGHT }
	public DaylightSavingsTime dst = DaylightSavingsTime.LOCAL;


	public TimeZoneDialog(String title, MainFrame f)
	{
		super(f, true);
		setTitle(title);

		setFont(PersistentState.get_font());

		frame = f;

		timezone_list = TimeZone.getAvailableIDs();
		bubble_sort(timezone_list);
		
		// extract the category names into a hash table
		Hashtable<String,Integer> category_sizes = new Hashtable<String,Integer>();
		category_sizes.put(ZONE_MISC, Integer.valueOf(0));
		category_sizes.put(ZONE_UTC,  Integer.valueOf(0));
		
		Queue<String> category_name_queue    = new Queue<String>();
		Queue<String> display_name_queue     = new Queue<String>();
		Queue<String> timezone_id_queue = new Queue<String>();

		// discover the system category names, the time zone short names, and time zone long names
		for (int i=0; i < timezone_list.length; i++) {
			int idx = timezone_list[i].lastIndexOf('/');
			if (0 <= idx) {
				String cat  = timezone_list[i].substring(0, idx);
				String zone = timezone_list[i].substring(idx+1);

				// System.out.printf("%s: %3d: list[%3d]='%-35s' cat='%-25s' zone='%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, timezone_list[i], cat, zone);

				Integer count = category_sizes.get(cat);
				if (count == null) {
					count = Integer.valueOf(1);
				} else {
					count = Integer.valueOf(count.intValue()+1);
				}
				category_sizes.put(cat, count);

				category_name_queue   .append(cat);
				display_name_queue    .append(zone);
				timezone_id_queue.append(timezone_list[i]);
			} else {
				Integer count = category_sizes.get(ZONE_MISC);
				category_sizes.put(ZONE_MISC, Integer.valueOf(count.intValue()+1));

				// System.out.printf("%s: %3d: list[%3d]='%-35s' cat='%-25s' zone='%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, timezone_list[i], ZONE_MISC, timezone_list[i]);

				category_name_queue.append(ZONE_MISC);
				display_name_queue .append(timezone_list[i]);
				timezone_id_queue  .append(timezone_list[i]);
			}
		}

		// add in the UTC time zones
		String[] utc_tz = {
				"GMT-23", "GMT-22", "GMT-21", "GMT-20",	"GMT-19", "GMT-18", "GMT-17", "GMT-16",
				"GMT-15", "GMT-14", "GMT-13", "GMT-12", "GMT-11", "GMT-10", "GMT-9",  "GMT-8",
				"GMT-7",  "GMT-6",  "GMT-5",  "GMT-4",  "GMT-3",  "GMT-2",  "GMT-1",  "GMT-0",
				"GMT+0",  "GMT+1",  "GMT+2",  "GMT+3",  "GMT+4",  "GMT+5",  "GMT+6",  "GMT+7",
				"GMT+8",  "GMT+9",  "GMT+10", "GMT+11", "GMT+12", "GMT+13", "GMT+14", "GMT+15",
				"GMT+16", "GMT+17", "GMT+18", "GMT+19", "GMT+20", "GMT+21", "GMT+22", "GMT+23",
				"GMT", "Greenwich", "UCT", "Universal", "UTC", "Zulu",
				};
		for (int i=0; i < utc_tz.length; i++) {
			Integer count = category_sizes.get(ZONE_UTC);
			category_sizes.put(ZONE_UTC, Integer.valueOf(count.intValue()+1));

			category_name_queue.append(ZONE_UTC);
			display_name_queue .append(utc_tz[i]);
			timezone_id_queue  .append(utc_tz[i]);
		}

		// System.out.printf("%s: %3d: categories=%d queue=%d\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), category_sizes.size(), category_name_queue.size());

		// place the category names into a list
		timezone_master_category_list = new String[category_sizes.size()];
		Enumeration<String> k = category_sizes.keys();
		for (int i=0; i < category_sizes.size(); i++) {
			String key = k.nextElement();
			timezone_master_category_list[i] = key;
		}
		bubble_sort(timezone_master_category_list);
		
		// map the category name to an array index
		category_index = new Hashtable<String,Integer>();			// maps a category name to an array index
		for (int i=0; i < timezone_master_category_list.length; i++) {
			category_index.put(timezone_master_category_list[i], Integer.valueOf(i));
		}

		/*
		for (int i=0; i < timezone_master_category_list.length; i++) {
			System.out.printf("%s: %3d: '%20s'=%3d\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), timezone_master_category_list[i], category_sizes.get(timezone_master_category_list[i]).intValue());
		}
		*/
		
		timezone_display_name_list = new String[display_name_queue .size()];	// contains the short names of all time zones
		timezone_id_list           = new String[timezone_id_queue  .size()];	// contains the ids of all time zones
		timezone_category_list     = new String[category_name_queue.size()];	// contains the category name of all time zones
		timezone_index             = new Hashtable<String,Integer>();			// maps a short or long time zone name to an array index
		for (int i=0; i < timezone_display_name_list.length; i++) {
			timezone_display_name_list[i] = display_name_queue .remove();
			timezone_id_list[i]           = timezone_id_queue  .remove();
			timezone_category_list[i]     = category_name_queue.remove();

			timezone_index.put(timezone_id_list[i],  Integer.valueOf(i));
		}

		// allocate space for the short and long name tables -- table[category][zone]
		category_display_name_table = new String[timezone_master_category_list.length][];
		category_timezone_id_table  = new String[timezone_master_category_list.length][];
		for (int i=0; i < timezone_master_category_list.length; i++) {
			String key = timezone_master_category_list[i];
			int size = category_sizes.get(key).intValue();
			if (size < 1) {
				System.out.printf("%s: %3d: category[%d]='%s' size=%d\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, key, size);
				System.exit(-1);
			}
			category_display_name_table[i] = new String[size];
			category_timezone_id_table [i] = new String[size];
		}

		int[] used = new int[category_index.size()];
		for (int i=0; i < used.length; i++) {
			used[i] = 0;
		}
		for (int i=0; i < timezone_display_name_list.length; i++) {
			int ci = category_index.get(timezone_category_list[i]).intValue();
			if (category_display_name_table[ci] == null) {
				category_display_name_table[ci] = new String[category_sizes.get(timezone_category_list[i]).intValue()];
			}

			int ti = timezone_index.get(timezone_id_list[i]).intValue();
			category_display_name_table[ci][used[ci]] = timezone_display_name_list[ti];
			category_timezone_id_table [ci][used[ci]] = timezone_id_list[ti];
			
			used[ci] += 1;
		}

		/*
		for (int i=0; i < timezone_master_category_list.length; i++) {
			String category = timezone_master_category_list[i];
			int size = category_sizes.get(category).intValue();
			for (int j=0; j < size; j++) {
				System.out.printf("%s: %3d: category[%2d]='%20s' timezone[%3d]='%-35s' '%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, category, j, category_timezone_id_table[i][j], category_display_name_table[i][j]);
			}
		}
		*/
		
		for (int i=0; i < timezone_master_category_list.length; i++) {
			bubble_sort(category_display_name_table[i]);
			bubble_sort(category_timezone_id_table [i]);
		}
		
  		
		int cols = 3;
		int rows = (timezone_master_category_list.length + 1 + cols - 1) / cols;

		Panel panel = new Panel(new GridLayout(rows, cols));
		
		for (int j=0; j < rows; j++) {
			for (int i=0; i < cols; i++) {
				int idx = i*rows + j;
				if (idx < timezone_master_category_list.length) {
					// System.out.printf("%s: %3d: i=%d j=%d category[%2d]='%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, j, idx, timezone_master_category_list[idx]);
					Button button = new Button(timezone_master_category_list[idx]);
					panel.add(button);
					button.addActionListener(new ActionListener() {
						@Override public void actionPerformed(ActionEvent e)
						{
							// System.out.printf("%s: %3d: category[%d] = '%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, timezone_master_category_list[idx]);
							remove(panel);
							setTitle(timezone_master_category_list[idx]);
							add(make_zone_panel(idx, timezone_master_category_list[idx], category_display_name_table[idx], category_timezone_id_table[idx]));
							pack();
							center();
						}
					});
				} else if (idx == (rows*cols - 1)) {
					// System.out.printf("%s: %3d: i=%d j=%d category[%d] = '%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, j, idx, "cancel");
					Button cancel = new Button("Cancel");
					cancel.addActionListener(new ActionListener() {
						@Override public void actionPerformed(ActionEvent e)
						{
							dispose();
						}
					});
					panel.add(cancel);
				} else {
					// System.out.printf("%s: %3d: i=%d j=%d category[%d] = '%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, j, idx, "blank");
					Button blank = new Button("");
					blank.setEnabled(false);
					panel.add(blank);
				}
			}
		}
  		
    	addWindowListener(new DialogEventHandler(this));
    	
		add(panel);

		setResizable(false);

		pack();
		Dimension size = getSize();
		size.height += 50;
		size.width  += 50;
		setSize(size);

		center();

		setVisible(true);
	}
	
	
	public static Hashtable<Integer, Point> grid_size = null;
	
	
	public Panel make_zone_panel(int idx, String category, String[] list, String[] tz_id)
	{
		if (grid_size == null) {
			grid_size = new Hashtable<Integer, Point>();

			grid_size.put(Integer.valueOf(  1), new Point( 3, 1));	// Arctic
			grid_size.put(Integer.valueOf(  2), new Point( 4, 1));	// America/Kentucky, Chile
			grid_size.put(Integer.valueOf(  3), new Point( 5, 1));	// America/North_Dakota, Mexico
			grid_size.put(Integer.valueOf(  4), new Point( 6, 1));	// Brazil
			grid_size.put(Integer.valueOf(  8), new Point( 5, 2));	// America/Indiana, Canada
			grid_size.put(Integer.valueOf( 11), new Point( 6, 2));	// Indian
			grid_size.put(Integer.valueOf( 12), new Point( 5, 3));	// Antarctica, Atlantic, US
			grid_size.put(Integer.valueOf( 13), new Point( 5, 3));	// America/Argentina, SystemV
			grid_size.put(Integer.valueOf( 23), new Point( 6, 4));	// Australia
			grid_size.put(Integer.valueOf( 35), new Point( 8, 5));	// Etc
			grid_size.put(Integer.valueOf( 44), new Point( 9, 5));	// Pacific
			grid_size.put(Integer.valueOf( 54), new Point( 8, 7));	// Africa, UTC
			grid_size.put(Integer.valueOf( 64), new Point(11, 6));	// Europe
			grid_size.put(Integer.valueOf( 66), new Point(10, 7));	// Misc
			grid_size.put(Integer.valueOf( 99), new Point(10,10));	// Asia
			grid_size.put(Integer.valueOf(142), new Point(15,10));	// America
		}
		
		Point xy = grid_size.get(Integer.valueOf(list.length));
		if (xy == null) {
			int x = (int) (Math.sqrt(list.length)+ 1); 
			xy = new Point(x, x);
		}

		int rows = xy.x;
		int cols = xy.y;
		
		// TODO
		Panel panel = new Panel(new GridLayout(0, cols));
		// System.out.printf("%s: %3d: idx=%d category='%s' len=%d rows=%d cols=%d rows*cols=%d\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, category, list.length, rows, cols, rows*cols);
		
		for (int r=0; r < rows; r++) {
			for (int c=0; c < cols; c++) {
				int i = c * rows + r;
				if (i < list.length) {
					Button button = new Button(list[i]);
					button.addActionListener(new ZoneActionListener(tz_id[i]));
					panel.add(button);
				} else if (i < (rows * cols - 1)) {
					Button button = new Button("");
					button.setEnabled(false);
					panel.add(button);
				} else {
					Button b = new Button("Cancel");
					b.addActionListener(new ActionListener() {
						@Override public void actionPerformed(ActionEvent e) 
						{
							dispose();
						}
					});
					panel.add(b);
				}
			}
		}
		
		return panel;
	}
	
	class ZoneActionListener implements ActionListener
	{
		final String tz_id;
		
		ZoneActionListener(String id)
		{
			tz_id = id;
		}

		@Override public void actionPerformed(ActionEvent e) 
		{
			PersistentState.timezone = TimeZone.getTimeZone(tz_id);
			dispose();
		}
	}
	
	
	public void center()
	{
		Dimension size = getSize();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		GraphicsConfiguration gc = getGraphicsConfiguration();
		Insets insets = toolkit.getScreenInsets(gc);
		Dimension screenSize = toolkit.getScreenSize();
		
		setLocation((screenSize.width-insets.right-size.width)/2, (screenSize.height-insets.top-size.height)/2);
	}
    
	
	static void bubble_sort(String[] a)
	{
		for (int i=0; i < a.length; i++) {
			for (int j=1; j < a.length; j++) {
				if (a[j] != null && 0 < a[j-1].compareToIgnoreCase(a[j])) {
					String tmp = a[j-1];
					a[j-1] = a[j];
					a[j] = tmp;
				}
			}
		}
	}
}
