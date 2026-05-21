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
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.astro.PlanetaryAlignment;
import lib.astro.PracticalAstronomy;
import lib.stars.catalog.DsoAlias;
import lib.stars.catalog.DsoAlias.Element;
import lib.time.TimeOfDay;
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class AlignmentDialog extends Dialog implements WindowListener, KeyListener {
	private static final long serialVersionUID = -6351387267852458181L;

	public final MainFrame main_frame;
	public final AlignmentDialog dialog;

	public static final Checkbox sun_cb     = new Checkbox();
	public static final Checkbox moon_cb    = new Checkbox();
	public static final Checkbox mercury_cb = new Checkbox();
	public static final Checkbox venus_cb   = new Checkbox();
	public static final Checkbox mars_cb    = new Checkbox();
	public static final Checkbox jupiter_cb = new Checkbox();
	public static final Checkbox saturn_cb  = new Checkbox();
	public static final Checkbox uranus_cb  = new Checkbox();
	public static final Checkbox neptune_cb = new Checkbox();
	public static final Checkbox dso_cb     = new Checkbox();

	public static final JLabel sun_jl     = new JLabel("Sun");
	public static final JLabel moon_jl    = new JLabel("Moon");
	public static final JLabel mercury_jl = new JLabel("Mercury");
	public static final JLabel venus_jl   = new JLabel("Venus");
	public static final JLabel mars_jl    = new JLabel("Mars");
	public static final JLabel jupiter_jl = new JLabel("Jupiter");
	public static final JLabel saturn_jl  = new JLabel("Saturn");
	public static final JLabel uranus_jl  = new JLabel("Uranus");
	public static final JLabel neptune_jl = new JLabel("Neptune");
	public static final JLabel dso_jl     = new JLabel("DSO");

	public static boolean sun_e     = false;
	public static boolean moon_e    = false;
	public static boolean mercury_e = false;
	public static boolean venus_e   = false;
	public static boolean mars_e    = false;
	public static boolean jupiter_e = false;
	public static boolean saturn_e  = false;
	public static boolean uranus_e  = false;
	public static boolean neptune_e = false;
	public static boolean dso_e     = false;

	public static final JLabel search_jl  = new JLabel("Search");
	public static final JLabel type_jl    = new JLabel("Type");
	public static final JLabel found_jl   = new JLabel("Found");
	public static final JLabel ra_jl      = new JLabel("RA");
	public static final JLabel dec_jl     = new JLabel("DEC");
	public static final JLabel mag_jl     = new JLabel("Mag.");
	public static final JLabel dia_jl     = new JLabel("Dia. (')");
	public static final JLabel deep_jl    = new JLabel("Deep Space Object");

	public static final JLabel date_range_jl = new JLabel("Date Range");
	public static final JLabel alignment_jl  = new JLabel("Alignment Objects");
	public static final JLabel nearest_jl    = new JLabel("Closest Approach");

	public static final JTextField search_tf = new JTextField("", 25);
	public static final JTextField found_tf  = new JTextField("", 56);
	public static final JTextField type_tf   = new JTextField("", 25);
	public static final JTextField ra_tf     = new JTextField("",  9);
	public static final JTextField dec_tf    = new JTextField("",  9);
	public static final JTextField mag_tf    = new JTextField("",  9);
	public static final JTextField size_tf   = new JTextField("",  9);

	public static final JLabel     start_jl  = new JLabel("Start Date");
	public static final JLabel     end_jl    = new JLabel("End Date");
	public static final JTextField start_tf  = new JTextField("", 15);
	public static final JTextField end_tf    = new JTextField("", 15);
	public static final Button     start_b   = new Button("Select Date");
	public static final Button     end_b     = new Button("Select Date");

	public static final JLabel sun_ra_de_jl     = new JLabel("Sun");
	public static final JLabel moon_ra_de_jl    = new JLabel("Moon");
	public static final JLabel mercury_ra_de_jl = new JLabel("Mercury");
	public static final JLabel venus_ra_de_jl   = new JLabel("Venus");
	public static final JLabel mars_ra_de_jl    = new JLabel("Mars");
	public static final JLabel jupiter_ra_de_jl = new JLabel("Jupiter");
	public static final JLabel saturn_ra_de_jl  = new JLabel("Saturn");
	public static final JLabel uranus_ra_de_jl  = new JLabel("Uranus");
	public static final JLabel neptune_ra_de_jl = new JLabel("Neptune");
	public static final JLabel dso_ra_de_jl     = new JLabel("DSO");

	private static final int ra_de_width = 20;
	public static final JTextField sun_ra_de_tf     = new JTextField(ra_de_width);
	public static final JTextField moon_ra_de_tf    = new JTextField(ra_de_width);
	public static final JTextField mercury_ra_de_tf = new JTextField(ra_de_width);
	public static final JTextField venus_ra_de_tf   = new JTextField(ra_de_width);
	public static final JTextField mars_ra_de_tf    = new JTextField(ra_de_width);
	public static final JTextField jupiter_ra_de_tf = new JTextField(ra_de_width);
	public static final JTextField saturn_ra_de_tf  = new JTextField(ra_de_width);
	public static final JTextField uranus_ra_de_tf  = new JTextField(ra_de_width);
	public static final JTextField neptune_ra_de_tf = new JTextField(ra_de_width);
	public static final JTextField dso_ra_de_tf     = new JTextField(ra_de_width);

	public static final JLabel date_jl           = new JLabel("Date");
	public static final JLabel avg_jl            = new JLabel("Avg. Dist.");
	public static final JLabel sum_jl            = new JLabel("Sum");
	public static final JLabel ssq_jl            = new JLabel("Sum of Sq.");

	public static final JTextField date_tf       = new JTextField(ra_de_width);
	public static final JTextField avg_tf        = new JTextField(ra_de_width);
	public static final JTextField sum_tf        = new JTextField(ra_de_width);
	public static final JTextField ssq_tf        = new JTextField(ra_de_width);

	public static final Button nearest_b   = new Button("Nearest DSO");
	public static final Button compute_b   = new Button("Calculate   ");
	public static final Button goto_date_b = new Button("Go To Date");
	public static final Button close_b     = new Button("Close");

	private static final String underscore = "_________________________";


	public static TimeZone timezone = PersistentState.timezone;
	public static long     start_ms = System.currentTimeMillis();
	public static long     end_ms   = System.currentTimeMillis();
	public static Element  dso      = null;

	public static long     best_time_ms = 0;

	public AlignmentDialog(MainFrame f, String name)
	{
		super(f, name, true);
		
		main_frame = f;
		dialog     = this;
		timezone   = PersistentState.timezone;

		compute_b.setEnabled(true);
		goto_date_b.setEnabled(best_time_ms != 0);

		Calendar cal = Calendar.getInstance(timezone);
		start_ms = cal.getTimeInMillis();

		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
		end_ms = cal.getTimeInMillis();

		addWindowListener(this);

		JPanel panel = set_pos_dms();
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
	
	JPanel set_pos_dms()
	{
		found_tf.setEditable(false);
		type_tf .setEditable(false);
		ra_tf   .setEditable(true);
		dec_tf  .setEditable(true);
		mag_tf  .setEditable(false);
		size_tf .setEditable(false);
		start_tf.setEditable(false);
		end_tf  .setEditable(false);

		sun_ra_de_tf    .setEditable(false);
		moon_ra_de_tf   .setEditable(false);
		mercury_ra_de_tf.setEditable(false);
		venus_ra_de_tf  .setEditable(false);
		mars_ra_de_tf   .setEditable(false);
		jupiter_ra_de_tf.setEditable(false);
		saturn_ra_de_tf .setEditable(false);
		uranus_ra_de_tf .setEditable(false);
		neptune_ra_de_tf.setEditable(false);
		dso_ra_de_tf    .setEditable(false);
		date_tf         .setEditable(false);
		avg_tf          .setEditable(false);
		sum_tf          .setEditable(false);
		ssq_tf          .setEditable(false);

		start_tf.setText(SelectDateDialog.show_date(start_ms));
		end_tf  .setText(SelectDateDialog.show_date(end_ms));

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;

		int top    =  0;
		int left   =  5;
		int bottom =  0;
		int right  =  0;

		int row = 0;

		JPanel search_panel = new JPanel(new GridBagLayout());
		// search_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(0, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(0, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);


		row += 1;

		int dso_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel.add(search_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 3;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(search_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 14, 1, 5);		// top, left, bottom, right
		search_panel.add(type_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(type_tf, gbc);


		dso_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel.add(found_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(found_tf, gbc);


		dso_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel.add(ra_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(ra_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 30, 1, 0);		// top, left, bottom, right
		search_panel.add(dec_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(dec_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 15, 1, 10);		// top, left, bottom, right
		search_panel.add(mag_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel.add(mag_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 20, 1, 10);		// top, left, bottom, right
		search_panel.add(dia_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		search_panel.add(size_tf, gbc);


		JPanel search_label_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_label_panel.add(deep_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 1;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_label_panel.add(nearest_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_label_panel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		row += 1;

		JPanel solar_system_panel = new JPanel(new GridBagLayout());
		int align_row = 0;

		top    =  0;
		left   =  5;
		bottom =  0;
		right  =  0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(sun_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(sun_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(moon_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(moon_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(mercury_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(mercury_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(venus_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(venus_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(mars_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(mars_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(jupiter_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 11;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(jupiter_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 12;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(saturn_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 13;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(saturn_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 14;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(uranus_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 15;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(uranus_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 16;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(neptune_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 17;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(neptune_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 18;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(dso_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 19;
		gbc.gridy     = align_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(top, left, bottom, right);		// top, left, bottom, right
		solar_system_panel.add(dso_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(alignment_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(solar_system_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		row += 1;

		JPanel date_range_panel = new JPanel(new GridBagLayout());
		int date_range_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		date_range_panel.add(start_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		date_range_panel.add(start_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 30);		// top, left, bottom, right
		date_range_panel.add(start_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		date_range_panel.add(end_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		date_range_panel.add(end_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = date_range_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		date_range_panel.add(end_b, gbc);

		date_range_row += 1;


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(date_range_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(date_range_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		row += 1;

		JPanel calculation_panel = new JPanel(new GridBagLayout());
		int calculation_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(sun_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(sun_ra_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(jupiter_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(jupiter_ra_de_tf, gbc);
		
		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(moon_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(moon_ra_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(saturn_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(saturn_ra_de_tf, gbc);
		
		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(mercury_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(mercury_ra_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(uranus_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(uranus_ra_de_tf, gbc);
		
		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(venus_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(venus_ra_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(neptune_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(neptune_ra_de_tf, gbc);
		
		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(mars_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(mars_ra_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(dso_ra_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(dso_ra_de_tf, gbc);

		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(date_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(date_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(avg_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(avg_tf, gbc);

		calculation_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(sum_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(sum_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(ssq_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 30);		// top, left, bottom, right
		calculation_panel.add(ssq_tf, gbc);
		
		calculation_row += 1;


		JPanel calculation_label_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_label_panel.add(nearest_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 1;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_label_panel.add(compute_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 2;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_label_panel.add(goto_date_b, gbc);
		

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(calculation_label_panel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(calculation_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		panel.add(close_b, gbc);

		close_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				// System.out.printf("%s: %d: %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE()); 
				compute_b.setEnabled(true);
				dispose();
			}
		});

		ActionListener[] a = start_b.getActionListeners();
		// System.out.printf("%s: %d: start.length=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), a.length); 
		if (a.length < 1) {
			search_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					if (main_frame != null && main_frame.dso_alias_table != null) {
						String search_text = search_tf.getText();
						main_frame.angle_of_separation_dso_0 = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
						fill_in_search_fields_dso(main_frame.angle_of_separation_dso_0);
					}
				}
			});
			search_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// TODO
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					String search_text = (search_tf.getText() + (char) evt.getKeyChar()).trim();
					dso = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
					fill_in_search_fields_dso(dso);
				}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e){}
			});
	
			nearest_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					double ra = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					double de = PracticalAstronomy.parse_declination(dec_tf.getText());
					if (main_frame.dso_alias_table != null) {
						dso = main_frame.dso_alias_table.find_nearest(ra, de);
						search_tf.setText(dso.names()[0]);
						fill_in_search_fields_dso(dso);
					}
				}
			});
			
			sun_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					sun_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			moon_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					moon_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			mercury_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					mercury_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			venus_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					venus_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			mars_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					mars_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			jupiter_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					jupiter_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			saturn_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					saturn_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			uranus_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					uranus_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			neptune_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					neptune_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			dso_cb.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) 
				{
					dso_e = e.getStateChange() == ItemEvent.SELECTED;
				}
			});

			start_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(start_ms)); 
			    	SelectDateDialog s = new SelectDateDialog(main_frame, "Set Start Date", true, start_ms, PersistentState.timezone);
			    	if (s.saved) {
				    	start_tf.setText(s.date);
				    	start_ms = s.selected_ms;
			    	}
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(start_ms)); 
				}
			});

			end_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(end_ms)); 
					SelectDateDialog s = new SelectDateDialog(main_frame, "Set End Date", true, end_ms, PersistentState.timezone);
			    	if (s.saved) {
				    	end_tf.setText(s.date);
				    	end_ms = s.selected_ms;
					}
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(end_ms)); 
				}
			});

			compute_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					compute_b.setEnabled(false);
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(end_ms));
					long delta_time_ms = 60 * 60 * 1000;
					PlanetaryAlignment pa = new PlanetaryAlignment(timezone, start_ms, end_ms, delta_time_ms, sun_e, moon_e, mercury_e, venus_e, mars_e, jupiter_e, saturn_e, uranus_e, neptune_e, dso_e ? dso : null);
			    	if (pa.valid) {
						long days = 15;
						delta_time_ms = 60 * 1000;
						long s_ms = Math.max(start_ms, pa.best_cst - days * 24 * 60 * 60 * 1000);
			    		long e_ms = Math.min(end_ms,   pa.best_cst + days * 24 * 60 * 60 * 1000);
						pa = new PlanetaryAlignment(timezone, s_ms, e_ms, delta_time_ms, sun_e, moon_e, mercury_e, venus_e, mars_e, jupiter_e, saturn_e, uranus_e, neptune_e, dso_e ? dso : null);

						long minutes = 15;
						delta_time_ms = 1000;
						s_ms = Math.max(start_ms, pa.best_cst - minutes * 60 * 1000);
			    		e_ms = Math.min(end_ms,   pa.best_cst + minutes * 60 * 1000);
						pa = new PlanetaryAlignment(timezone, s_ms, e_ms, delta_time_ms, sun_e, moon_e, mercury_e, venus_e, mars_e, jupiter_e, saturn_e, uranus_e, neptune_e, dso_e ? dso : null);

						if (pa.best_elt[PlanetaryAlignment.SUN] != null) sun_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.SUN].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.SUN].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.MOON] != null) moon_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.MOON].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.MOON].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.MERCURY] != null) mercury_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.MERCURY].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.MERCURY].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.VENUS] != null) venus_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.VENUS].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.VENUS].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.MARS] != null) mars_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.MARS].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.MARS].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.JUPITER] != null) jupiter_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.JUPITER].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.JUPITER].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.SATURN] != null) saturn_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.SATURN].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.SATURN].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.URANUS] != null) uranus_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.URANUS].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.URANUS].de_deg()))
				    		);

						if (pa.best_elt[PlanetaryAlignment.NEPTUNE] != null) neptune_ra_de_tf.setText(String.format("RA %s, DE %s", 
			    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.NEPTUNE].ra_hrs()),
			    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.NEPTUNE].de_deg()))
				    		);

			    		if (dso_e && dso != null) {
			    			dso_ra_de_tf.setText(String.format("RA %s, DE %s", 
				    				PracticalAstronomy.decimal_hours_to_str_hms(pa.best_elt[PlanetaryAlignment.DSO].ra_hrs()),
				    				PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_elt[PlanetaryAlignment.DSO].de_deg()))
					    		);
			    		}

			    		// TODO fill in date, avg (degrees), sum, ssq
			    		date_tf.setText(TimeOfDay.show_date_time(pa.best_cst, timezone));
			    		avg_tf.setText(PracticalAstronomy.decimal_degrees_to_str_dms(pa.best_avg));
			    		sum_tf.setText(String.format("%f", pa.best_sum));
			    		ssq_tf.setText(String.format("%f", pa.best_ssq));
			    		
			    		best_time_ms = pa.best_cst;
			    		goto_date_b.setEnabled(best_time_ms != 0);
			    	} else {
			    		sun_ra_de_tf    .setText("");
			    		moon_ra_de_tf   .setText("");
			    		mercury_ra_de_tf.setText("");
			    		venus_ra_de_tf  .setText("");
			    		mars_ra_de_tf   .setText("");
			    		jupiter_ra_de_tf.setText("");
			    		saturn_ra_de_tf .setText("");
			    		uranus_ra_de_tf .setText("");
			    		neptune_ra_de_tf.setText("");
			    		dso_ra_de_tf    .setText("");

			    		date_tf.setText("");
			    		avg_tf .setText("");
			    		sum_tf .setText("");
			    		ssq_tf .setText("");

			    		best_time_ms = 0;
			    		goto_date_b.setEnabled(best_time_ms != 0);
			    	}
					// System.out.printf("%s: %d: date='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), SelectDateDialog.show_date(end_ms)); 
					compute_b.setEnabled(true);
				}
			});

			goto_date_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					long system_ms = System.currentTimeMillis();
					main_frame.solar_time_offset_ms = best_time_ms - system_ms;
			    	dispose();
				}
			});
		}

		return panel;
	}


	public void fill_in_search_fields_dso(Element elt)
	{
		dso = elt;
		// System.out.printf("%s: %d: search='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());

		if (elt == null) {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), "null");
			found_tf.setText("");
			type_tf .setText("");
			ra_tf   .setText("");
			dec_tf  .setText("");
			mag_tf  .setText("");
			size_tf .setText("");
		} else {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), frame.dso_alias_table.name(elt));
			found_tf.setText(main_frame.dso_alias_table.name(elt));
			type_tf .setText(elt.type());
			ra_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(elt.ra_hrs()));
			dec_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(elt.de_deg()));
			if (elt.max_mag() != DsoAlias.Element.NO_MAG) {
				if (elt.max_mag() == elt.min_mag()) {
					mag_tf.setText(String.format("%.1f", elt.max_mag()));
				} else {
					mag_tf.setText(String.format("%.1f/%.1f", elt.max_mag(), elt.min_mag()));
				}
			} else {
				mag_tf.setText("");
			}
			size_tf.setText(String.format("%.1f", elt.size_min()));
			
			dso_cb.setState(true);
		}
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
		compute_b.setEnabled(true);
		dispose();
	}

	@Override public void windowActivated(WindowEvent arg0)   {}
	@Override public void windowClosed(WindowEvent arg0)      {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0)   {}
	@Override public void windowOpened(WindowEvent arg0)      {}

	@Override public void keyTyped(KeyEvent e)    {}
	@Override public void keyPressed(KeyEvent e)  {}
	@Override public void keyReleased(KeyEvent e) {}
}
