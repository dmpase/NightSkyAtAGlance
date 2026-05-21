package nightskyataglance.gui;

/*******************************************************************************
 * Copyright (c) 2026 Douglas M. Pase                                          *
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


import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

// import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class SelectDateDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -6831971413625375245L;

	private int        cal_day_size = 3;
	private Button     save    = new Button(" Save ");
	private Button     today   = new Button(" Today ");
	private Button     cancel  = new Button("Cancel");
	private Button     dn_year = new Button(" << ");
	private JTextField date_tf = new JTextField("", 22);
	private JTextField year_tf = new JTextField("", 6);
	private JTextField day_tf  = new JTextField("", 3);
	private Button     up_year = new Button(" >> ");
	private Button[][] cal_days  = {
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
		{ new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), new Button(String.format("    ", cal_day_size)), },
	};
	private JComboBox<String> months = new JComboBox<String>(month_abbrev);


	public long       initial_ms     = System.currentTimeMillis();
	public long       selected_ms    = System.currentTimeMillis();
	public Calendar   cal            = Calendar.getInstance(PersistentState.timezone);
	public String     date           = "";
	public int        year           = cal.get(Calendar.YEAR);
	public int        month          = cal.get(Calendar.MONTH);
	public int        day_of_month   = cal.get(Calendar.DAY_OF_MONTH);
	public int        day_of_week    = cal.get(Calendar.DAY_OF_WEEK) - 1;
	public Locale     current_locale = Locale.getDefault();
	public DateFormat date_format    = DateFormat.getDateInstance(DateFormat.MEDIUM, current_locale);
	public boolean    saved          = false;

	public SelectDateDialog(MainFrame f, String name, boolean modality, long ms, TimeZone tz) 
	{
		super(f, name, modality);
		
		initial_ms = selected_ms = ms;
		cal.setTimeInMillis(initial_ms);
		year           = cal.get(Calendar.YEAR);
		month          = cal.get(Calendar.MONTH);
		day_of_month   = cal.get(Calendar.DAY_OF_MONTH);
		day_of_week    = cal.get(Calendar.DAY_OF_WEEK) - 1;
		// System.out.printf("%s: %d: year=%d month=%d dom=%d dow=%d date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), year, month, day_of_month, day_of_week, date);

		addWindowListener(this);

		add(build_widget());

		setResizable(false);

		pack();
		Dimension size = getSize();
		size.height += 50;
		size.width  += 50;
		setSize(size);

		center();

		setVisible(true);
	}

	private static final String[] weekday_names = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	// private static final String[] month_names   = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private static final String[] month_abbrev  = { "Jan  ", "Feb ", "Mar ", "Apr ", "May ", "Jun ", "Jul ", "Aug ", "Sep ", "Oct ", "Nov ", "Dec " };
	private static final int[]    days_in_month = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	JPanel build_widget()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;
		
		int row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		date_tf.setEditable(false);
		panel.add(date_tf, gbc);
		
		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		months.setSelectedIndex(cal.get(Calendar.MONTH));
		panel.add(months, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		day_tf.setText(String.format("%d", cal.get(Calendar.DAY_OF_MONTH)));
		day_tf.setHorizontalAlignment(JTextField.CENTER);
		panel.add(day_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(dn_year, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		year_tf.setText(String.format("%d", cal.get(Calendar.YEAR)));
		year_tf.setHorizontalAlignment(JTextField.CENTER);
		panel.add(year_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(up_year, gbc);
		
		
		row += 1;

		for (int i=0; i < 7; i++) {
			gbc = new GridBagConstraints();
			gbc.gridx     = i;
			gbc.gridy     = row;
			gbc.anchor    = GridBagConstraints.CENTER;
			gbc.gridwidth = 1;
			gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
			Button b = new Button(String.format("%s", weekday_names[i]));
			b.setEnabled(false);
			panel.add(b, gbc);
		}

		
		row += 1;

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		int dim = cal.get(Calendar.DAY_OF_YEAR) - doy;
		int off = 1 + Calendar.SUNDAY - dow;
		int dic = cal_days.length * cal_days[0].length;

		for (int i=0; i < dic; i++) {
			int r = i / cal_days[0].length;
			int c = i % cal_days[0].length;
			gbc = new GridBagConstraints();
			gbc.gridx     = c;
			gbc.gridy     = row + r;
			gbc.anchor    = GridBagConstraints.CENTER;
			gbc.gridwidth = 1;
			gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
			int dom = i + off;
			Button b = cal_days[r][c];
			if (1 <= dom && dom <= dim) {
				b.setLabel(String.format("%2d", dom));
				panel.add(b, gbc);
				b.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent e) 
					{
						day_tf.setText(String.format("%2d", dom));
						int day   = dom;
						show_date(year, month, day);
					}
				});
			} else {
				b.setEnabled(false);
				b.setLabel("    ");
				panel.add(b, gbc);
			}
		}

		show_date(year, month, day_of_month);

		
		row += cal_days.length;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(cancel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(today, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(save, gbc);

		
		months.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				int year  = Integer.parseInt(year_tf.getText().trim());
				int month = months.getSelectedIndex();
				int day   = Integer.parseInt(day_tf.getText().trim());

				show_date(year, month, day);
			}
		});


		year_tf.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				int year  = Integer.parseInt(year_tf.getText().trim());
				int month = months.getSelectedIndex();
				int day   = Integer.parseInt(day_tf.getText().trim());

				show_date(year, month, day);
			}
		});


		dn_year.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				int year  = Integer.parseInt(year_tf.getText().trim()) - 1;
				int month = months.getSelectedIndex();
				int day   = Integer.parseInt(day_tf.getText().trim());

				show_date(year, month, day);
			}
		});

		
		up_year.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				int year  = Integer.parseInt(year_tf.getText().trim()) + 1;
				int month = months.getSelectedIndex();
				int day   = Integer.parseInt(day_tf.getText().trim());

				show_date(year, month, day);
			}
		});


		save.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				saved = true;
				dispose();
			}
		});

		today.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				selected_ms = System.currentTimeMillis();
				cal.setTimeInMillis(selected_ms);
				int y = cal.get(Calendar.YEAR);
				int m = cal.get(Calendar.MONTH);
				int d = cal.get(Calendar.DAY_OF_MONTH);
				day_tf.setText(String.format("%d", d));

				Locale currentLocale = Locale.getDefault();
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
				date = weekday_names[day_of_week] + ", " + df.format(cal.getTimeInMillis());

				show_date(y, m, d);
			}
		});
		

		// cancel.setFocusTraversalKeysEnabled(false);
		cancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				selected_ms = initial_ms;
				cal.setTimeInMillis(selected_ms);

				year         = cal.get(Calendar.YEAR);
				month        = cal.get(Calendar.MONTH);
				day_of_month = cal.get(Calendar.DAY_OF_MONTH);
				day_of_week  = cal.get(Calendar.DAY_OF_WEEK) - 1;

				Locale currentLocale = Locale.getDefault();
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
				date = weekday_names[day_of_week] + ", " + df.format(cal.getTimeInMillis());

				saved = false;
		    	dispose();
			}
		});
		
		return panel;
	}


	public static String show_date(long ms)
	{
		Calendar   cal            = Calendar.getInstance(PersistentState.timezone);
		cal.setTimeInMillis(ms);
		int        day_of_week    = cal.get(Calendar.DAY_OF_WEEK) - 1;
		Locale     current_locale = Locale.getDefault();
		DateFormat date_format    = DateFormat.getDateInstance(DateFormat.MEDIUM, current_locale);
		String     today          = weekday_names[day_of_week] + ", " + date_format.format(cal.getTimeInMillis());

		return today;
	}

	private void show_date(int y, int m, int d)
	{
		// System.out.printf("%s: %d: called from line %d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), NightSkyAtAGlance.LINE(3));
		year         = y;
		month        = m;
		day_of_month = d;

		year_tf.setText(String.format("%d", year));
		months.setSelectedIndex(month);
		day_tf.setText(String.format("%d", day_of_month));

		boolean leap = (year % 4 == 0) && (year % 400 != 0);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day_of_month);
		day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;					// day of week

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dow = cal.get(Calendar.DAY_OF_WEEK);							// day of week
		int dim = days_in_month[month] + ((leap && month == 1) ? 1 : 0);	// days in month
		int off = 1 + Calendar.SUNDAY - dow;								// offset
		int dic = cal_days.length * cal_days[0].length;						// days in calendar

		for (int i=0; i < dic; i++) {
			int r = i / cal_days[0].length;
			int c = i % cal_days[0].length;
			int dom = i + off;
			Button b = cal_days[r][c];
			ActionListener[] a = b.getActionListeners();
			if (0 < a.length) b.removeActionListener(a[0]);
			if (1 <= dom && dom <= dim) {
				b.setLabel(String.format("%2d", dom));
				b.setEnabled(true);
				b.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent e) 
					{
						// System.out.printf("%s: %d: dom=%d dow=%d src='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dom, dow, e.getSource());
						day_tf.setText(String.format("%2d", dom));
						day_of_month = dom;
						day_of_week  = c;

						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, month);
						cal.set(Calendar.DAY_OF_MONTH, day_of_month);
						day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;						// day of week
						selected_ms = cal.getTimeInMillis();

						date = show_date(selected_ms);
						date_tf.setText(date);

						// System.out.printf("%s: %d: dom=%d dow=%d date='%s' src='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), day_of_month, day_of_week, date, e.getSource());
					}
				});
			} else {
				b.setEnabled(false);
				b.setLabel("    ");
			}
		}

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day_of_month);
		day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;							// day of week

		date = weekday_names[day_of_week] + ", " + date_format.format(cal.getTimeInMillis());
		date_tf.setText(date);
		selected_ms = cal.getTimeInMillis();

		// System.out.printf("%s: %d: y=%d m=%d d=%d zone='%s' date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), y, m, d, PersistentState.timezone.getDisplayName(), date);
	}

	
	
	private void center()
	{
		Dimension size = getSize();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		GraphicsConfiguration gc = getGraphicsConfiguration();
		Insets insets = toolkit.getScreenInsets(gc);
		Dimension screenSize = toolkit.getScreenSize();
		
		setLocation((screenSize.width-insets.right-size.width)/2, (screenSize.height-insets.top-size.height)/2);
	}

	@Override public void windowClosing(WindowEvent e) 
	{
		dispose();
	}

	@Override public void windowClosed(WindowEvent e)
	{
		dispose();
	}

	@Override public void windowOpened(WindowEvent e)      {}
	@Override public void windowIconified(WindowEvent e)   {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowActivated(WindowEvent e)   {}
	@Override public void windowDeactivated(WindowEvent e) {}
}
