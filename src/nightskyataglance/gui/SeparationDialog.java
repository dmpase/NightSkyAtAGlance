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
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.astro.PracticalAstronomy;
import lib.stars.catalog.DsoAlias;
import lib.stars.catalog.DsoAlias.Element;
import nightskyataglance.util.PersistentState;

public class SeparationDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -6351387267852458181L;

	public final MainFrame main_frame;
	public final SeparationDialog dialog;

	public static final JLabel search0_jl  = new JLabel("Search");
	public static final JLabel type0_jl    = new JLabel("Type");
	public static final JLabel found0_jl   = new JLabel("Found");
	public static final JLabel ra0_jl      = new JLabel("RA");
	public static final JLabel dec0_jl     = new JLabel("DEC");
	public static final JLabel mag0_jl     = new JLabel("Mag.");
	public static final JLabel size0_jl     = new JLabel("Dia. (')");
	public static final JLabel deep0_jl    = new JLabel("Deep Space Object (from)");

	public static final JLabel search1_jl  = new JLabel("Search");
	public static final JLabel type1_jl    = new JLabel("Type");
	public static final JLabel found1_jl   = new JLabel("Found");
	public static final JLabel ra1_jl      = new JLabel("RA");
	public static final JLabel dec1_jl     = new JLabel("DEC");
	public static final JLabel mag1_jl     = new JLabel("Mag.");
	public static final JLabel dia1_jl     = new JLabel("Dia. (')");
	public static final JLabel deep1_jl    = new JLabel("Deep Space Object (to)");

	public static final JLabel angle_jl      = new JLabel("Separation");
	public static final JLabel direction_jl  = new JLabel("Direction");
	public static final JLabel separation_jl = new JLabel("Angle of Separation");

	public static final JTextField search0_tf = new JTextField("", 25);
	public static final JTextField found0_tf  = new JTextField("", 56);
	public static final JTextField type0_tf   = new JTextField("", 25);
	public static final JTextField ra0_tf     = new JTextField("",  9);
	public static final JTextField dec0_tf    = new JTextField("",  9);
	public static final JTextField mag0_tf    = new JTextField("",  9);
	public static final JTextField size0_tf   = new JTextField("",  9);

	public static final JTextField search1_tf = new JTextField("", 25);
	public static final JTextField found1_tf  = new JTextField("", 56);
	public static final JTextField type1_tf   = new JTextField("", 25);
	public static final JTextField ra1_tf     = new JTextField("",  9);
	public static final JTextField dec1_tf    = new JTextField("",  9);
	public static final JTextField mag1_tf    = new JTextField("",  9);
	public static final JTextField size1_tf   = new JTextField("",  9);

	public static final JTextField angle_tf     = new JTextField("", 25);
	public static final JTextField direction_tf = new JTextField("", 20);

	public static final Button nearest0_b = new Button("Nearest DSO");
	public static final Button nearest1_b = new Button("Nearest DSO");
	public static final Button compute_b  = new Button("Calculate");
	public static final Button clear_b    = new Button("Clear");
	public static final Button close_b    = new Button("Close");
	
	public static boolean listeners_are_loaded = false;

	private static final String underscore = "_________________________";


	public TimeZone timezone = null;

	public SeparationDialog(MainFrame f, String name)
	{
		super(f, name, true);
		
		main_frame = f;
		dialog     = this;
		timezone   = PersistentState.timezone;

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
		found0_tf.setEditable(false);
		type0_tf .setEditable(false);
		ra0_tf   .setEditable(true);
		dec0_tf  .setEditable(true);
		mag0_tf  .setEditable(false);
		size0_tf .setEditable(false);

		found1_tf.setEditable(false);
		type1_tf .setEditable(false);
		ra1_tf   .setEditable(true);
		dec1_tf  .setEditable(true);
		mag1_tf  .setEditable(false);
		size1_tf .setEditable(false);

		angle_tf    .setEditable(false);
		direction_tf.setEditable(false);

		if (main_frame.angle_of_separation_dso_0 != null) {
			found0_tf.setText(main_frame.angle_of_separation_dso_0.names()[0]);
			type0_tf .setText(main_frame.angle_of_separation_dso_0.type());
			ra0_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(main_frame.angle_of_separation_dso_0.ra_hrs()));
			dec0_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(main_frame.angle_of_separation_dso_0.ra_hrs()));
			mag0_tf  .setText(String.format("%.1f", main_frame.angle_of_separation_dso_0.min_mag()));
			size0_tf .setText(String.format("%.0f", main_frame.angle_of_separation_dso_0.min_mag()));
		}

		if (main_frame.angle_of_separation_dso_1 != null) {
			found1_tf.setText(main_frame.angle_of_separation_dso_1.names()[0]);
			type1_tf .setText(main_frame.angle_of_separation_dso_1.type());
			ra1_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(main_frame.angle_of_separation_dso_1.ra_hrs()));
			dec1_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(main_frame.angle_of_separation_dso_1.ra_hrs()));
			mag1_tf  .setText(String.format("%.1f", main_frame.angle_of_separation_dso_1.min_mag()));
			size1_tf .setText(String.format("%.0f", main_frame.angle_of_separation_dso_1.min_mag()));
		}

		if (main_frame.angle_of_separation_dso_0 != null && main_frame.angle_of_separation_dso_1 != null) {
			double angle = PracticalAstronomy.angle_between_celestial_objects(
					main_frame.angle_of_separation_dso_0.ra_hrs(), 
					main_frame.angle_of_separation_dso_0.de_deg(), 
					main_frame.angle_of_separation_dso_1.ra_hrs(), 
					main_frame.angle_of_separation_dso_1.de_deg());
			angle_tf.setText(PracticalAstronomy.decimal_degrees_to_str_dms(angle));
		} else {
			angle_tf.setText("");
		}

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;

		int row = 0;

		JPanel search_panel0 = new JPanel(new GridBagLayout());
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

		int dso0_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel0.add(search0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 3;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(search0_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 14, 1, 5);		// top, left, bottom, right
		search_panel0.add(type0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(type0_tf, gbc);


		dso0_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel0.add(found0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(found0_tf, gbc);


		dso0_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_panel0.add(ra0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(ra0_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 30, 1, 0);		// top, left, bottom, right
		search_panel0.add(dec0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(dec0_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 15, 1, 10);		// top, left, bottom, right
		search_panel0.add(mag0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_panel0.add(mag0_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 20, 1, 10);		// top, left, bottom, right
		search_panel0.add(size0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = dso0_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		search_panel0.add(size0_tf, gbc);


		JPanel search_label0_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_label0_panel.add(deep0_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 1;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_label0_panel.add(nearest0_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_label0_panel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_panel0, gbc);

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

		JPanel search1_panel = new JPanel(new GridBagLayout());
		int dso1_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search1_panel.add(search1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 3;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(search1_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 14, 1, 5);		// top, left, bottom, right
		search1_panel.add(type1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(type1_tf, gbc);


		dso1_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search1_panel.add(found1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(found1_tf, gbc);


		dso1_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search1_panel.add(ra1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(ra1_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 30, 1, 0);		// top, left, bottom, right
		search1_panel.add(dec1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(dec1_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 15, 1, 10);		// top, left, bottom, right
		search1_panel.add(mag1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search1_panel.add(mag1_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 20, 1, 10);		// top, left, bottom, right
		search1_panel.add(dia1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = dso1_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		search1_panel.add(size1_tf, gbc);


		JPanel search_label1_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		search_label1_panel.add(deep1_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 1;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		search_label1_panel.add(nearest1_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_label1_panel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search1_panel, gbc);


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

		/*
		JPanel date_range_panel = new JPanel(new GridBagLayout());
		int date_range_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(date_time_jl, gbc);

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
		*/

		row += 1;

		JPanel calculation_panel = new JPanel(new GridBagLayout());
		int calculation_row = 0;

		// TODO
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		calculation_panel.add(angle_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_panel.add(angle_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 30, 1, 5);		// top, left, bottom, right
		calculation_panel.add(direction_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = calculation_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_panel.add(direction_tf, gbc);


		JPanel calculation_label_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		calculation_label_panel.add(separation_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 1;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		// calculation_label_panel.add(compute_b, gbc);
		

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

		JPanel button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		button_panel.add(clear_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		button_panel.add(close_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		panel.add(button_panel, gbc);


		if (! listeners_are_loaded) {
			listeners_are_loaded = false;
			search0_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					if (main_frame != null && main_frame.dso_alias_table != null) {
						String search_text = search0_tf.getText();
						main_frame.angle_of_separation_dso_0 = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
						fill_in_search_fields_dso0(main_frame.angle_of_separation_dso_0);

						fill_in_calculation();
					}
				}
			});
			search0_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// TODO
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					String search_text = (search0_tf.getText() + (char) evt.getKeyChar()).trim();
					main_frame.angle_of_separation_dso_0 = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
					fill_in_search_fields_dso0(main_frame.angle_of_separation_dso_0);

					fill_in_calculation();
				}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e){}
			});
	
			nearest0_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					double ra = PracticalAstronomy.parse_right_ascension(ra0_tf.getText());
					double de = PracticalAstronomy.parse_declination(dec0_tf.getText());
					if (main_frame.dso_alias_table != null) {
						main_frame.angle_of_separation_dso_0 = main_frame.dso_alias_table.find_nearest(ra, de);
						search0_tf.setText(main_frame.angle_of_separation_dso_0.names()[0]);
						fill_in_search_fields_dso0(main_frame.angle_of_separation_dso_0);

						fill_in_calculation();
					}
				}
			});

			search1_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					if (main_frame != null && main_frame.dso_alias_table != null) {
						String search_text = search1_tf.getText();
						main_frame.angle_of_separation_dso_1 = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
						fill_in_search_fields_dso1(main_frame.angle_of_separation_dso_1);

						fill_in_calculation();
					}
				}
			});
			search1_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// TODO
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					String search_text = (search1_tf.getText() + (char) evt.getKeyChar()).trim();
					main_frame.angle_of_separation_dso_1 = (main_frame.dso_alias_table != null) ? main_frame.dso_alias_table.find(search_text) : null;
					fill_in_search_fields_dso1(main_frame.angle_of_separation_dso_1);

					fill_in_calculation();
				}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e){}
			});
	
			nearest1_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					double ra = PracticalAstronomy.parse_right_ascension(ra1_tf.getText());
					double de = PracticalAstronomy.parse_declination(dec1_tf.getText());
					if (main_frame.dso_alias_table != null) {
						main_frame.angle_of_separation_dso_1 = main_frame.dso_alias_table.find_nearest(ra, de);
						search1_tf.setText(main_frame.angle_of_separation_dso_1.names()[0]);
						fill_in_search_fields_dso1(main_frame.angle_of_separation_dso_1);

						fill_in_calculation();
					}
				}
			});

			clear_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					main_frame.angle_of_separation_dso_0 = null;
					main_frame.angle_of_separation_dso_1 = null;
					fill_in_search_fields_dso0(main_frame.angle_of_separation_dso_0);
					fill_in_search_fields_dso1(main_frame.angle_of_separation_dso_1);
					search0_tf.setText("");
					search1_tf.setText("");
				}
			});

			close_b.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
			});
		}

		return panel;
	}


	public void fill_in_calculation()
	{
		if (main_frame.angle_of_separation_dso_0 != null && main_frame.angle_of_separation_dso_1 != null) {
			double ra0 = main_frame.angle_of_separation_dso_0.ra_hrs();
			double de0 = main_frame.angle_of_separation_dso_0.de_deg();

			double ra1 = main_frame.angle_of_separation_dso_1.ra_hrs();
			double de1 = main_frame.angle_of_separation_dso_1.de_deg();

			ra0 += (ra0 <    0) ? 24 : 0;
			ra0 -= (24  <= ra0) ? 24 : 0;

			ra1 += (ra1 <    0) ? 24 : 0;
			ra1 -= (24  <= ra1) ? 24 : 0;

			double angle = PracticalAstronomy.angle_between_celestial_objects(ra0, de0, ra1, de1);
			angle_tf.setText(PracticalAstronomy.decimal_degrees_to_str_dms(angle));

			double direction = PracticalAstronomy.direction_from_co_to_co( ra0, de0, ra1, de1);
			if (0 <= direction) {
				direction_tf.setText(PracticalAstronomy.decimal_degrees_to_str_dms(direction));
			} else if (direction <= -180) {
				direction_tf.setText(PracticalAstronomy.decimal_degrees_to_str_dms(direction+360));				
			} else {
				String str = String.format("%s (%s)", 
						PracticalAstronomy.decimal_degrees_to_str_dms(direction),
						PracticalAstronomy.decimal_degrees_to_str_dms(direction+360));
				direction_tf.setText(str);				
			}
		} else {
			angle_tf.setText("");
			direction_tf.setText("");
		}
	}


	public void fill_in_search_fields_dso0(Element elt)
	{
		main_frame.angle_of_separation_dso_0 = elt;
		// System.out.printf("%s: %d: search='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());

		if (elt == null) {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), "null");
			found0_tf.setText("");
			type0_tf .setText("");
			ra0_tf   .setText("");
			dec0_tf  .setText("");
			mag0_tf  .setText("");
			size0_tf .setText("");
		} else {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), frame.dso_alias_table.name(elt));
			found0_tf.setText(main_frame.dso_alias_table.name(elt));
			type0_tf .setText(elt.type());
			ra0_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(elt.ra_hrs()));
			dec0_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(elt.de_deg()));
			if (elt.max_mag() != DsoAlias.Element.NO_MAG) {
				if (elt.max_mag() == elt.min_mag()) {
					mag0_tf  .setText(String.format("%.1f", elt.max_mag()));
				} else {
					mag0_tf  .setText(String.format("%.1f/%.1f", elt.max_mag(), elt.min_mag()));
				}
			} else {
				mag0_tf  .setText("");
			}
			size0_tf .setText(String.format("%.1f", elt.size_min()));
		}
	}

	public void fill_in_search_fields_dso1(Element elt)
	{
		main_frame.angle_of_separation_dso_1 = elt;
		// System.out.printf("%s: %d: search='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());

		if (elt == null) {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), "null");
			found1_tf.setText("");
			type1_tf .setText("");
			ra1_tf   .setText("");
			dec1_tf  .setText("");
			mag1_tf  .setText("");
			size1_tf .setText("");
		} else {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), frame.dso_alias_table.name(elt));
			found1_tf.setText(main_frame.dso_alias_table.name(elt));
			type1_tf .setText(elt.type());
			ra1_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(elt.ra_hrs()));
			dec1_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(elt.de_deg()));
			if (elt.max_mag() != DsoAlias.Element.NO_MAG) {
				if (elt.max_mag() == elt.min_mag()) {
					mag1_tf  .setText(String.format("%.1f", elt.max_mag()));
				} else {
					mag1_tf  .setText(String.format("%.1f/%.1f", elt.max_mag(), elt.min_mag()));
				}
			} else {
				mag1_tf  .setText("");
			}
			size1_tf .setText(String.format("%.1f", elt.size_min()));
		}
	}

	// TODO
	public void fill_in_separation_fields()
	{
		
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

	@Override public void windowActivated(WindowEvent arg0)   {}
	@Override public void windowClosed(WindowEvent arg0)      {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0)   {}
	@Override public void windowOpened(WindowEvent arg0)      {}
}
