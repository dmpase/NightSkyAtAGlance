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


import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.stars.catalog.DsoFilter;
import lib.stars.catalog.DsoType;
import nightskyataglance.util.PersistentState;

public class FilterDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2519693274364184533L;

	public final MainFrame frame;
	public final FilterDialog dialog;

	int spaces = 10;
	public final JTextField magnitude_min_tf = new JTextField("", spaces);
	public final JTextField magnitude_max_tf = new JTextField("", spaces);
	public final JTextField diameter_min_tf  = new JTextField("", spaces);
	public final JTextField diameter_max_tf  = new JTextField("", spaces);

	public final JCheckBox star                    = new JCheckBox();
	public final JCheckBox double_star             = new JCheckBox();
	public final JCheckBox variable_star           = new JCheckBox();
	public final JCheckBox asterism                = new JCheckBox();
	public final JCheckBox open_cluster            = new JCheckBox();

	public final JCheckBox nebula                  = new JCheckBox();
	public final JCheckBox dark_nebula             = new JCheckBox();
	public final JCheckBox diffuse_nebula          = new JCheckBox();
	public final JCheckBox emission_nebula         = new JCheckBox();
	public final JCheckBox gaseous_nebula          = new JCheckBox();
	public final JCheckBox planetary_nebula        = new JCheckBox();
	public final JCheckBox reflection_nebula       = new JCheckBox();
	public final JCheckBox supernova_remnant       = new JCheckBox();
	public final JCheckBox open_cluster_and_nebula = new JCheckBox();

	public final JCheckBox galaxy                  = new JCheckBox();
	public final JCheckBox barred_irregular_galaxy = new JCheckBox();
	public final JCheckBox barred_spiral_galaxy    = new JCheckBox();
	public final JCheckBox dwarf_spheroidal_galaxy = new JCheckBox();
	public final JCheckBox elliptical_galaxy       = new JCheckBox();
	public final JCheckBox galaxy_cluster          = new JCheckBox();
	public final JCheckBox globular_cluster        = new JCheckBox();
	public final JCheckBox interacting_galaxy      = new JCheckBox();
	public final JCheckBox irregular_galaxy        = new JCheckBox();
	public final JCheckBox lenticular_galaxy       = new JCheckBox();
	public final JCheckBox spiral_galaxy           = new JCheckBox();
	public final JCheckBox starburst_galaxy        = new JCheckBox();
	public final JCheckBox supergiant_elliptical_galaxy = new JCheckBox();

	public final JCheckBox unidentified            = new JCheckBox();

	public final JButton stars    = new JButton("Stars     ");
	public final JButton nebulae  = new JButton("Nebulae ");
	public final JButton galaxies = new JButton("Galaxies");
	
	public boolean stars_b    = true;
	public boolean nebulae_b  = true;
	public boolean galaxies_b = true;

	public final JButton cancel = new JButton("Cancel");
	public final JButton clear  = new JButton("Clear");
	public final JButton remove = new JButton("Remove");
	public final JButton save   = new JButton("Save");
	
	public final JLabel magnitude_jl                    = new JLabel("Magnitude");
	public final JLabel max_mag_jl                      = new JLabel("Max (Least Bright)");
	public final JLabel min_mag_jl                      = new JLabel("Min (Brightest)");
	public final JLabel diameter_jl                     = new JLabel("Diameter (')");
	public final JLabel min_dia_jl                      = new JLabel("Min (Smallest)");
	public final JLabel max_dia_jl                      = new JLabel("Max (Largest)");
	public final JLabel star_jl                         = new JLabel(DsoType.star[0]);
	public final JLabel double_star_jl                  = new JLabel(DsoType.double_star[0]);
	public final JLabel variable_star_jl                = new JLabel(DsoType.variable_star[0]);
	public final JLabel asterism_jl                     = new JLabel(DsoType.asterism[0]);
	public final JLabel open_cluster_jl                 = new JLabel(DsoType.open_cluster[0]);
	public final JLabel nebula_jl                       = new JLabel(DsoType.nebula[0]);
	public final JLabel dark_nebula_jl                  = new JLabel(DsoType.dark_nebula[0]);
	public final JLabel diffuse_nebula_jl               = new JLabel(DsoType.diffuse_nebula[0]);
	public final JLabel emission_nebula_jl              = new JLabel(DsoType.emission_nebula[0]);
	public final JLabel gaseous_nebula_jl               = new JLabel(DsoType.gaseous_nebula[0]);
	public final JLabel planetary_nebula_jl             = new JLabel(DsoType.planetary_nebula[0]);
	public final JLabel reflection_nebula_jl            = new JLabel(DsoType.reflection_nebula[0]);
	public final JLabel supernova_remnant_jl            = new JLabel(DsoType.supernova_remnant[0]);
	public final JLabel open_cluster_and_nebula_jl      = new JLabel(DsoType.open_cluster_and_nebula[0]);
	public final JLabel galaxy_jl                       = new JLabel(DsoType.galaxy[0]);
	public final JLabel barred_irregular_galaxy_jl      = new JLabel(DsoType.barred_irregular_galaxy[0]);
	public final JLabel barred_spiral_galaxy_jl         = new JLabel(DsoType.barred_spiral_galaxy[0]);
	public final JLabel dwarf_spheroidal_galaxy_jl      = new JLabel(DsoType.dwarf_spheroidal_galaxy[0]);
	public final JLabel elliptical_galaxy_jl            = new JLabel(DsoType.elliptical_galaxy[0]);
	public final JLabel galaxy_cluster_jl               = new JLabel(DsoType.galaxy_cluster[0]);
	public final JLabel globular_cluster_jl             = new JLabel(DsoType.globular_cluster[0]);
	public final JLabel interacting_galaxy_jl           = new JLabel(DsoType.interacting_galaxy[0]);
	public final JLabel irregular_galaxy_jl             = new JLabel(DsoType.irregular_galaxy[0]);
	public final JLabel lenticular_galaxy_jl            = new JLabel(DsoType.lenticular_galaxy[0]);
	public final JLabel spiral_galaxy_jl                = new JLabel(DsoType.spiral_galaxy[0]);
	public final JLabel starburst_galaxy_jl             = new JLabel(DsoType.starburst_galaxy[0]);
	public final JLabel supergiant_elliptical_galaxy_jl = new JLabel(DsoType.supergiant_elliptical_galaxy[0]);
	public final JLabel unidentified_jl                 = new JLabel(DsoType.unidentified[0]);

	public TimeZone timezone = null;
	
	private void set_font(Font font)
	{
		setFont(font);
		magnitude_min_tf.setFont(font);
		magnitude_max_tf.setFont(font);
		diameter_min_tf .setFont(font);
		diameter_max_tf .setFont(font);

		magnitude_min_tf.setFont(font);
		magnitude_max_tf.setFont(font);
		diameter_min_tf .setFont(font);
		diameter_max_tf .setFont(font);

		star                        .setFont(font);
		double_star                 .setFont(font);
		variable_star               .setFont(font);
		asterism                    .setFont(font);
		open_cluster                .setFont(font);

		nebula                      .setFont(font);
		dark_nebula                 .setFont(font);
		diffuse_nebula              .setFont(font);
		emission_nebula             .setFont(font);
		gaseous_nebula              .setFont(font);
		planetary_nebula            .setFont(font);
		reflection_nebula           .setFont(font);
		supernova_remnant           .setFont(font);
		open_cluster_and_nebula     .setFont(font);

		galaxy                      .setFont(font);
		barred_irregular_galaxy     .setFont(font);
		barred_spiral_galaxy        .setFont(font);
		dwarf_spheroidal_galaxy     .setFont(font);
		elliptical_galaxy           .setFont(font);
		galaxy_cluster              .setFont(font);
		globular_cluster            .setFont(font);
		interacting_galaxy          .setFont(font);
		irregular_galaxy            .setFont(font);
		lenticular_galaxy           .setFont(font);
		spiral_galaxy               .setFont(font);
		starburst_galaxy            .setFont(font);
		supergiant_elliptical_galaxy.setFont(font);

		unidentified                .setFont(font);

		stars   .setFont(font);
		nebulae .setFont(font);
		galaxies.setFont(font);
		cancel  .setFont(font);
		clear   .setFont(font);
		remove  .setFont(font);
		save    .setFont(font);

		// TODO
		magnitude_jl.setFont(font);
		max_mag_jl.setFont(font);
		min_mag_jl.setFont(font);
		diameter_jl.setFont(font);
		min_dia_jl.setFont(font);
		max_dia_jl.setFont(font);
		star_jl.setFont(font);
		double_star_jl.setFont(font);
		variable_star_jl.setFont(font);
		asterism_jl.setFont(font);
		open_cluster_jl.setFont(font);
		nebula_jl.setFont(font);
		dark_nebula_jl.setFont(font);
		diffuse_nebula_jl.setFont(font);
		emission_nebula_jl.setFont(font);
		gaseous_nebula_jl.setFont(font);
		planetary_nebula_jl.setFont(font);
		reflection_nebula_jl.setFont(font);
		supernova_remnant_jl.setFont(font);
		open_cluster_and_nebula_jl.setFont(font);
		galaxy_jl.setFont(font);
		barred_irregular_galaxy_jl.setFont(font);
		barred_spiral_galaxy_jl.setFont(font);
		dwarf_spheroidal_galaxy_jl.setFont(font);
		elliptical_galaxy_jl.setFont(font);
		galaxy_cluster_jl.setFont(font);
		globular_cluster_jl.setFont(font);
		interacting_galaxy_jl.setFont(font);
		irregular_galaxy_jl.setFont(font);
		lenticular_galaxy_jl.setFont(font);
		spiral_galaxy_jl.setFont(font);
		starburst_galaxy_jl.setFont(font);
		supergiant_elliptical_galaxy_jl.setFont(font);
		unidentified_jl.setFont(font);
	}

	public FilterDialog(MainFrame f, String name)
	{
		super(f, name, true);

		Font font = PersistentState.get_font();
		set_font(font);

		frame    = f;
		dialog   = this;
		timezone = PersistentState.timezone;

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
		if (frame.filter == null) {
			magnitude_min_tf.setText("-999999");
			magnitude_max_tf.setText("999999");
			diameter_min_tf .setText("0");
			diameter_max_tf .setText("999999");
			
			magnitude_min_tf.setHorizontalAlignment(JTextField.RIGHT);
			magnitude_max_tf.setHorizontalAlignment(JTextField.RIGHT);
			diameter_min_tf.setHorizontalAlignment(JTextField.RIGHT);
			diameter_max_tf.setHorizontalAlignment(JTextField.RIGHT);

			star                        .setSelected(false);
			double_star                 .setSelected(false);
			variable_star               .setSelected(false);
			asterism                    .setSelected(false);
			open_cluster                .setSelected(false);

			nebula                      .setSelected(false);
			dark_nebula                 .setSelected(false);
			diffuse_nebula              .setSelected(false);
			emission_nebula             .setSelected(false);
			gaseous_nebula              .setSelected(false);
			planetary_nebula            .setSelected(false);
			reflection_nebula           .setSelected(false);
			supernova_remnant           .setSelected(false);
			open_cluster_and_nebula     .setSelected(false);

			galaxy                      .setSelected(false);
			barred_irregular_galaxy     .setSelected(false);
			barred_spiral_galaxy        .setSelected(false);
			dwarf_spheroidal_galaxy     .setSelected(false);
			elliptical_galaxy           .setSelected(false);
			galaxy_cluster              .setSelected(false);
			globular_cluster            .setSelected(false);
			interacting_galaxy          .setSelected(false);
			irregular_galaxy            .setSelected(false);
			lenticular_galaxy           .setSelected(false);
			spiral_galaxy               .setSelected(false);
			starburst_galaxy            .setSelected(false);
			supergiant_elliptical_galaxy.setSelected(false);

			unidentified                .setSelected(false);
		} else {
			magnitude_min_tf.setText(String.format("%f", frame.filter.min_mag));
			magnitude_max_tf.setText(String.format("%f", frame.filter.max_mag));
			diameter_min_tf .setText(String.format("%f", frame.filter.min_dia));
			diameter_max_tf .setText(String.format("%f", frame.filter.max_dia));

			star                        .setSelected(frame.filter.type_list[DsoType.STAR]);
			double_star                 .setSelected(frame.filter.type_list[DsoType.DOUBLE_STAR]);
			variable_star               .setSelected(frame.filter.type_list[DsoType.VARIABLE_STAR]);
			asterism                    .setSelected(frame.filter.type_list[DsoType.ASTERISM]);
			open_cluster                .setSelected(frame.filter.type_list[DsoType.OPEN_CLUSTER]);

			nebula                      .setSelected(frame.filter.type_list[DsoType.NEBULA]);
			dark_nebula                 .setSelected(frame.filter.type_list[DsoType.DARK_NEBULA]);
			diffuse_nebula              .setSelected(frame.filter.type_list[DsoType.DIFFUSE_NEBULA]);
			emission_nebula             .setSelected(frame.filter.type_list[DsoType.EMISSION_NEBULA]);
			gaseous_nebula              .setSelected(frame.filter.type_list[DsoType.GASEOUS_NEBULA]);
			planetary_nebula            .setSelected(frame.filter.type_list[DsoType.PLANETARY_NEBULA]);
			reflection_nebula           .setSelected(frame.filter.type_list[DsoType.REFLECTION_NEBULA]);
			supernova_remnant           .setSelected(frame.filter.type_list[DsoType.SUPERNOVA_REMNANT]);
			open_cluster_and_nebula     .setSelected(frame.filter.type_list[DsoType.OPEN_CLUSTER_AND_NEBULA]);

			galaxy                      .setSelected(frame.filter.type_list[DsoType.GALAXY]);
			barred_irregular_galaxy     .setSelected(frame.filter.type_list[DsoType.BARRED_IRREGULAR_GALAXY]);
			barred_spiral_galaxy        .setSelected(frame.filter.type_list[DsoType.BARRED_SPIRAL_GALAXY]);
			dwarf_spheroidal_galaxy     .setSelected(frame.filter.type_list[DsoType.DWARF_SPHEROIDAL_GALAXY]);
			elliptical_galaxy           .setSelected(frame.filter.type_list[DsoType.ELLIPTICAL_GALAXY]);
			galaxy_cluster              .setSelected(frame.filter.type_list[DsoType.GALAXY_CLUSTER]);
			globular_cluster            .setSelected(frame.filter.type_list[DsoType.GLOBULAR_CLUSTER]);
			interacting_galaxy          .setSelected(frame.filter.type_list[DsoType.INTERACTING_GALAXY]);
			irregular_galaxy            .setSelected(frame.filter.type_list[DsoType.IRREGULAR_GALAXY]);
			lenticular_galaxy           .setSelected(frame.filter.type_list[DsoType.LENTICULAR_GALAXY]);
			spiral_galaxy               .setSelected(frame.filter.type_list[DsoType.SPIRAL_GALAXY]);
			starburst_galaxy            .setSelected(frame.filter.type_list[DsoType.STARBURST_GALAXY]);
			supergiant_elliptical_galaxy.setSelected(frame.filter.type_list[DsoType.SUPERGIANT_ELLIPTICAL_GALAXY]);

			unidentified                .setSelected(frame.filter.type_list[DsoType.UNIDENTIFIED]);
		}
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;

		int row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(magnitude_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(max_mag_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(magnitude_max_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(min_mag_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(magnitude_min_tf, gbc);


		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(diameter_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(min_dia_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(diameter_min_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(max_dia_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(diameter_max_tf, gbc);


		row += 1;

		JPanel dso_panel = new JPanel(new GridBagLayout());

		int rt = 0;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(0, 0, 0, 10);		// top, left, bottom, right
		dso_panel.add(stars, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(star, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(star_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(double_star, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(double_star_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(variable_star, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(variable_star_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(asterism, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(asterism_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(open_cluster, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(open_cluster_jl, gbc);


		rt += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(0, 0, 0, 10);		// top, left, bottom, right
		dso_panel.add(nebulae, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(nebula, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(dark_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(dark_nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(diffuse_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(diffuse_nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(emission_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(emission_nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(gaseous_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(gaseous_nebula_jl, gbc);


		rt += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(planetary_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(planetary_nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(reflection_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(reflection_nebula_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(supernova_remnant, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(supernova_remnant_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(open_cluster_and_nebula, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(open_cluster_and_nebula_jl, gbc);


		rt += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(0, 0, 0, 10);		// top, left, bottom, right
		dso_panel.add(galaxies, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(barred_irregular_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(barred_irregular_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(barred_spiral_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(barred_spiral_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(dwarf_spheroidal_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(dwarf_spheroidal_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(elliptical_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(elliptical_galaxy_jl, gbc);


		rt += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(galaxy_cluster, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(galaxy_cluster_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(globular_cluster, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(globular_cluster_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(interacting_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(interacting_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(irregular_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(irregular_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(lenticular_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(lenticular_galaxy_jl, gbc);


		rt += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(spiral_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(spiral_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(starburst_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(starburst_galaxy_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(supergiant_elliptical_galaxy, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(supergiant_elliptical_galaxy_jl, gbc);
		
		
		rt += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		dso_panel.add(unidentified, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = rt;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		dso_panel.add(unidentified_jl, gbc);


		rt += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(0, 0, 0, 0);		// top, left, bottom, right
		panel.add(dso_panel, gbc);


		row += 1;


		JPanel button_panel = new JPanel(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(cancel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(clear, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(remove, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(save, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(button_panel, gbc);


		save.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				double min_mag = Double.parseDouble(magnitude_min_tf.getText());
				double max_mag = Double.parseDouble(magnitude_max_tf.getText());
				double max_dia = Double.parseDouble(diameter_max_tf.getText());
				double min_dia = Double.parseDouble(diameter_min_tf.getText());

				boolean[] list = new boolean[DsoType.NUMBER_OF_DSO_TYPES];
				list[DsoType.STAR]                         = star.isSelected();
				list[DsoType.DOUBLE_STAR]                  = double_star.isSelected();
				list[DsoType.VARIABLE_STAR]                = variable_star.isSelected();
				list[DsoType.ASTERISM]                     = asterism.isSelected();
				list[DsoType.OPEN_CLUSTER]                 = open_cluster.isSelected();
				list[DsoType.NEBULA]                       = nebula.isSelected();
				list[DsoType.DARK_NEBULA]                  = dark_nebula.isSelected();
				list[DsoType.DIFFUSE_NEBULA]               = diffuse_nebula.isSelected();
				list[DsoType.EMISSION_NEBULA]              = emission_nebula.isSelected();
				list[DsoType.GASEOUS_NEBULA]               = gaseous_nebula.isSelected();
				list[DsoType.PLANETARY_NEBULA]             = planetary_nebula.isSelected();
				list[DsoType.REFLECTION_NEBULA]            = reflection_nebula.isSelected();
				list[DsoType.SUPERNOVA_REMNANT]            = supernova_remnant.isSelected();
				list[DsoType.OPEN_CLUSTER_AND_NEBULA]      = open_cluster_and_nebula.isSelected();
				list[DsoType.GALAXY]                       = galaxy.isSelected();
				list[DsoType.BARRED_IRREGULAR_GALAXY]      = barred_irregular_galaxy.isSelected();
				list[DsoType.BARRED_SPIRAL_GALAXY]         = barred_spiral_galaxy.isSelected();
				list[DsoType.DWARF_SPHEROIDAL_GALAXY]      = dwarf_spheroidal_galaxy.isSelected();
				list[DsoType.ELLIPTICAL_GALAXY]            = elliptical_galaxy.isSelected();
				list[DsoType.GALAXY_CLUSTER]               = galaxy_cluster.isSelected();
				list[DsoType.GLOBULAR_CLUSTER]             = globular_cluster.isSelected();
				list[DsoType.INTERACTING_GALAXY]           = interacting_galaxy.isSelected();
				list[DsoType.IRREGULAR_GALAXY]             = irregular_galaxy.isSelected();
				list[DsoType.LENTICULAR_GALAXY]            = lenticular_galaxy.isSelected();
				list[DsoType.SPIRAL_GALAXY]                = spiral_galaxy.isSelected();
				list[DsoType.STARBURST_GALAXY]             = starburst_galaxy.isSelected();
				list[DsoType.SUPERGIANT_ELLIPTICAL_GALAXY] = supergiant_elliptical_galaxy.isSelected();
				list[DsoType.UNIDENTIFIED]                 = unidentified.isSelected();
				
				frame.filter = new DsoFilter(min_dia, max_dia, min_mag, max_mag, list);

				dispose();
			}
		});

		stars.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				star                        .setSelected(stars_b);
				double_star                 .setSelected(stars_b);
				variable_star               .setSelected(stars_b);
				asterism                    .setSelected(stars_b);
				open_cluster                .setSelected(stars_b);

				stars_b = ! stars_b;
			}
		});

		nebulae.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				nebula                      .setSelected(nebulae_b);
				dark_nebula                 .setSelected(nebulae_b);
				diffuse_nebula              .setSelected(nebulae_b);
				emission_nebula             .setSelected(nebulae_b);
				gaseous_nebula              .setSelected(nebulae_b);
				planetary_nebula            .setSelected(nebulae_b);
				reflection_nebula           .setSelected(nebulae_b);
				supernova_remnant           .setSelected(nebulae_b);
				open_cluster_and_nebula     .setSelected(nebulae_b);

				nebulae_b = ! nebulae_b;
			}
		});

		galaxies.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				galaxy                      .setSelected(galaxies_b);
				barred_irregular_galaxy     .setSelected(galaxies_b);
				barred_spiral_galaxy        .setSelected(galaxies_b);
				dwarf_spheroidal_galaxy     .setSelected(galaxies_b);
				elliptical_galaxy           .setSelected(galaxies_b);
				galaxy_cluster              .setSelected(galaxies_b);
				globular_cluster            .setSelected(galaxies_b);
				interacting_galaxy          .setSelected(galaxies_b);
				irregular_galaxy            .setSelected(galaxies_b);
				lenticular_galaxy           .setSelected(galaxies_b);
				spiral_galaxy               .setSelected(galaxies_b);
				starburst_galaxy            .setSelected(galaxies_b);
				supergiant_elliptical_galaxy.setSelected(galaxies_b);

				galaxies_b = ! galaxies_b;
			}
		});

		remove.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				frame.filter = null;
				dispose();
			}
		});

		clear.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				magnitude_min_tf.setText("-999999");
				magnitude_max_tf.setText("999999");
				diameter_min_tf .setText("0");
				diameter_max_tf .setText("999999");

				star                        .setSelected(false);
				double_star                 .setSelected(false);
				variable_star               .setSelected(false);
				asterism                    .setSelected(false);
				open_cluster                .setSelected(false);

				nebula                      .setSelected(false);
				dark_nebula                 .setSelected(false);
				diffuse_nebula              .setSelected(false);
				emission_nebula             .setSelected(false);
				gaseous_nebula              .setSelected(false);
				planetary_nebula            .setSelected(false);
				reflection_nebula           .setSelected(false);
				supernova_remnant           .setSelected(false);
				open_cluster_and_nebula     .setSelected(false);

				galaxy                      .setSelected(false);
				barred_irregular_galaxy     .setSelected(false);
				barred_spiral_galaxy        .setSelected(false);
				dwarf_spheroidal_galaxy     .setSelected(false);
				elliptical_galaxy           .setSelected(false);
				galaxy_cluster              .setSelected(false);
				globular_cluster            .setSelected(false);
				interacting_galaxy          .setSelected(false);
				irregular_galaxy            .setSelected(false);
				lenticular_galaxy           .setSelected(false);
				spiral_galaxy               .setSelected(false);
				starburst_galaxy            .setSelected(false);
				supergiant_elliptical_galaxy.setSelected(false);

				unidentified                .setSelected(false);
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	dispose();
			}
		});
		

		return panel;
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
