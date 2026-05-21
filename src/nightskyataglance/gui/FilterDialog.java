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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimeZone;

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

	public final Checkbox star                    = new Checkbox();
	public final Checkbox double_star             = new Checkbox();
	public final Checkbox variable_star           = new Checkbox();
	public final Checkbox asterism                = new Checkbox();
	public final Checkbox open_cluster            = new Checkbox();

	public final Checkbox nebula                  = new Checkbox();
	public final Checkbox dark_nebula             = new Checkbox();
	public final Checkbox diffuse_nebula          = new Checkbox();
	public final Checkbox emission_nebula         = new Checkbox();
	public final Checkbox gaseous_nebula          = new Checkbox();
	public final Checkbox planetary_nebula        = new Checkbox();
	public final Checkbox reflection_nebula       = new Checkbox();
	public final Checkbox supernova_remnant       = new Checkbox();
	public final Checkbox open_cluster_and_nebula = new Checkbox();

	public final Checkbox galaxy                  = new Checkbox();
	public final Checkbox barred_irregular_galaxy = new Checkbox();
	public final Checkbox barred_spiral_galaxy    = new Checkbox();
	public final Checkbox dwarf_spheroidal_galaxy = new Checkbox();
	public final Checkbox elliptical_galaxy       = new Checkbox();
	public final Checkbox galaxy_cluster          = new Checkbox();
	public final Checkbox globular_cluster        = new Checkbox();
	public final Checkbox interacting_galaxy      = new Checkbox();
	public final Checkbox irregular_galaxy        = new Checkbox();
	public final Checkbox lenticular_galaxy       = new Checkbox();
	public final Checkbox spiral_galaxy           = new Checkbox();
	public final Checkbox starburst_galaxy        = new Checkbox();
	public final Checkbox supergiant_elliptical_galaxy = new Checkbox();

	public final Checkbox unidentified            = new Checkbox();

	public final Button stars    = new Button("Stars     ");
	public final Button nebulae  = new Button("Nebulae ");
	public final Button galaxies = new Button("Galaxies");
	
	public boolean stars_b    = true;
	public boolean nebulae_b  = true;
	public boolean galaxies_b = true;

	public final Button cancel = new Button("Cancel");
	public final Button clear  = new Button("Clear");
	public final Button remove = new Button("Remove");
	public final Button save   = new Button("Save");

	public TimeZone timezone = null;

	public FilterDialog(MainFrame f, String name)
	{
		super(f, name, true);
		
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

			star                        .setState(false);
			double_star                 .setState(false);
			variable_star               .setState(false);
			asterism                    .setState(false);
			open_cluster                .setState(false);

			nebula                      .setState(false);
			dark_nebula                 .setState(false);
			diffuse_nebula              .setState(false);
			emission_nebula             .setState(false);
			gaseous_nebula              .setState(false);
			planetary_nebula            .setState(false);
			reflection_nebula           .setState(false);
			supernova_remnant           .setState(false);
			open_cluster_and_nebula     .setState(false);

			galaxy                      .setState(false);
			barred_irregular_galaxy     .setState(false);
			barred_spiral_galaxy        .setState(false);
			dwarf_spheroidal_galaxy     .setState(false);
			elliptical_galaxy           .setState(false);
			galaxy_cluster              .setState(false);
			globular_cluster            .setState(false);
			interacting_galaxy          .setState(false);
			irregular_galaxy            .setState(false);
			lenticular_galaxy           .setState(false);
			spiral_galaxy               .setState(false);
			starburst_galaxy            .setState(false);
			supergiant_elliptical_galaxy.setState(false);

			unidentified                .setState(false);
		} else {
			magnitude_min_tf.setText(String.format("%f", frame.filter.min_mag));
			magnitude_max_tf.setText(String.format("%f", frame.filter.max_mag));
			diameter_min_tf .setText(String.format("%f", frame.filter.min_dia));
			diameter_max_tf .setText(String.format("%f", frame.filter.max_dia));

			star                        .setState(frame.filter.type_list[DsoType.STAR]);
			double_star                 .setState(frame.filter.type_list[DsoType.DOUBLE_STAR]);
			variable_star               .setState(frame.filter.type_list[DsoType.VARIABLE_STAR]);
			asterism                    .setState(frame.filter.type_list[DsoType.ASTERISM]);
			open_cluster                .setState(frame.filter.type_list[DsoType.OPEN_CLUSTER]);

			nebula                      .setState(frame.filter.type_list[DsoType.NEBULA]);
			dark_nebula                 .setState(frame.filter.type_list[DsoType.DARK_NEBULA]);
			diffuse_nebula              .setState(frame.filter.type_list[DsoType.DIFFUSE_NEBULA]);
			emission_nebula             .setState(frame.filter.type_list[DsoType.EMISSION_NEBULA]);
			gaseous_nebula              .setState(frame.filter.type_list[DsoType.GASEOUS_NEBULA]);
			planetary_nebula            .setState(frame.filter.type_list[DsoType.PLANETARY_NEBULA]);
			reflection_nebula           .setState(frame.filter.type_list[DsoType.REFLECTION_NEBULA]);
			supernova_remnant           .setState(frame.filter.type_list[DsoType.SUPERNOVA_REMNANT]);
			open_cluster_and_nebula     .setState(frame.filter.type_list[DsoType.OPEN_CLUSTER_AND_NEBULA]);

			galaxy                      .setState(frame.filter.type_list[DsoType.GALAXY]);
			barred_irregular_galaxy     .setState(frame.filter.type_list[DsoType.BARRED_IRREGULAR_GALAXY]);
			barred_spiral_galaxy        .setState(frame.filter.type_list[DsoType.BARRED_SPIRAL_GALAXY]);
			dwarf_spheroidal_galaxy     .setState(frame.filter.type_list[DsoType.DWARF_SPHEROIDAL_GALAXY]);
			elliptical_galaxy           .setState(frame.filter.type_list[DsoType.ELLIPTICAL_GALAXY]);
			galaxy_cluster              .setState(frame.filter.type_list[DsoType.GALAXY_CLUSTER]);
			globular_cluster            .setState(frame.filter.type_list[DsoType.GLOBULAR_CLUSTER]);
			interacting_galaxy          .setState(frame.filter.type_list[DsoType.INTERACTING_GALAXY]);
			irregular_galaxy            .setState(frame.filter.type_list[DsoType.IRREGULAR_GALAXY]);
			lenticular_galaxy           .setState(frame.filter.type_list[DsoType.LENTICULAR_GALAXY]);
			spiral_galaxy               .setState(frame.filter.type_list[DsoType.SPIRAL_GALAXY]);
			starburst_galaxy            .setState(frame.filter.type_list[DsoType.STARBURST_GALAXY]);
			supergiant_elliptical_galaxy.setState(frame.filter.type_list[DsoType.SUPERGIANT_ELLIPTICAL_GALAXY]);

			unidentified                .setState(frame.filter.type_list[DsoType.UNIDENTIFIED]);
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
		panel.add(new Label("Magnitude"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(new Label("Max (Least Bright)"), gbc);

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
		panel.add(new Label("Min (Brightest)"), gbc);

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
		panel.add(new Label("Diameter (')"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(new Label("Min (Smallest)"), gbc);

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
		panel.add(new Label("Max (Largest)"), gbc);

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
		dso_panel.add(new Label(DsoType.star[0]), gbc);

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
		dso_panel.add(new Label(DsoType.double_star[0]), gbc);

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
		dso_panel.add(new Label(DsoType.variable_star[0]), gbc);

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
		dso_panel.add(new Label(DsoType.asterism[0]), gbc);

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
		dso_panel.add(new Label(DsoType.open_cluster[0]), gbc);


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
		dso_panel.add(new Label(DsoType.nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.dark_nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.diffuse_nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.emission_nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.gaseous_nebula[0]), gbc);


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
		dso_panel.add(new Label(DsoType.planetary_nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.reflection_nebula[0]), gbc);

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
		dso_panel.add(new Label(DsoType.supernova_remnant[0]), gbc);

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
		dso_panel.add(new Label(DsoType.open_cluster_and_nebula[0]), gbc);


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
		dso_panel.add(new Label(DsoType.galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.barred_irregular_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.barred_spiral_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.dwarf_spheroidal_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.elliptical_galaxy[0]), gbc);


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
		dso_panel.add(new Label(DsoType.galaxy_cluster[0]), gbc);

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
		dso_panel.add(new Label(DsoType.globular_cluster[0]), gbc);

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
		dso_panel.add(new Label(DsoType.interacting_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.irregular_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.lenticular_galaxy[0]), gbc);


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
		dso_panel.add(new Label(DsoType.spiral_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.starburst_galaxy[0]), gbc);

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
		dso_panel.add(new Label(DsoType.supergiant_elliptical_galaxy[0]), gbc);
		
		
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
		dso_panel.add(new Label(DsoType.unidentified[0]), gbc);


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
				list[DsoType.STAR]                         = star.getState();
				list[DsoType.DOUBLE_STAR]                  = double_star.getState();
				list[DsoType.VARIABLE_STAR]                = variable_star.getState();
				list[DsoType.ASTERISM]                     = asterism.getState();
				list[DsoType.OPEN_CLUSTER]                 = open_cluster.getState();
				list[DsoType.NEBULA]                       = nebula.getState();
				list[DsoType.DARK_NEBULA]                  = dark_nebula.getState();
				list[DsoType.DIFFUSE_NEBULA]               = diffuse_nebula.getState();
				list[DsoType.EMISSION_NEBULA]              = emission_nebula.getState();
				list[DsoType.GASEOUS_NEBULA]               = gaseous_nebula.getState();
				list[DsoType.PLANETARY_NEBULA]             = planetary_nebula.getState();
				list[DsoType.REFLECTION_NEBULA]            = reflection_nebula.getState();
				list[DsoType.SUPERNOVA_REMNANT]            = supernova_remnant.getState();
				list[DsoType.OPEN_CLUSTER_AND_NEBULA]      = open_cluster_and_nebula.getState();
				list[DsoType.GALAXY]                       = galaxy.getState();
				list[DsoType.BARRED_IRREGULAR_GALAXY]      = barred_irregular_galaxy.getState();
				list[DsoType.BARRED_SPIRAL_GALAXY]         = barred_spiral_galaxy.getState();
				list[DsoType.DWARF_SPHEROIDAL_GALAXY]      = dwarf_spheroidal_galaxy.getState();
				list[DsoType.ELLIPTICAL_GALAXY]            = elliptical_galaxy.getState();
				list[DsoType.GALAXY_CLUSTER]               = galaxy_cluster.getState();
				list[DsoType.GLOBULAR_CLUSTER]             = globular_cluster.getState();
				list[DsoType.INTERACTING_GALAXY]           = interacting_galaxy.getState();
				list[DsoType.IRREGULAR_GALAXY]             = irregular_galaxy.getState();
				list[DsoType.LENTICULAR_GALAXY]            = lenticular_galaxy.getState();
				list[DsoType.SPIRAL_GALAXY]                = spiral_galaxy.getState();
				list[DsoType.STARBURST_GALAXY]             = starburst_galaxy.getState();
				list[DsoType.SUPERGIANT_ELLIPTICAL_GALAXY] = supergiant_elliptical_galaxy.getState();
				list[DsoType.UNIDENTIFIED]                 = unidentified.getState();
				
				frame.filter = new DsoFilter(min_dia, max_dia, min_mag, max_mag, list);

				dispose();
			}
		});

		stars.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				star                        .setState(stars_b);
				double_star                 .setState(stars_b);
				variable_star               .setState(stars_b);
				asterism                    .setState(stars_b);
				open_cluster                .setState(stars_b);

				stars_b = ! stars_b;
			}
		});

		nebulae.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				nebula                      .setState(nebulae_b);
				dark_nebula                 .setState(nebulae_b);
				diffuse_nebula              .setState(nebulae_b);
				emission_nebula             .setState(nebulae_b);
				gaseous_nebula              .setState(nebulae_b);
				planetary_nebula            .setState(nebulae_b);
				reflection_nebula           .setState(nebulae_b);
				supernova_remnant           .setState(nebulae_b);
				open_cluster_and_nebula     .setState(nebulae_b);

				nebulae_b = ! nebulae_b;
			}
		});

		galaxies.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				galaxy                      .setState(galaxies_b);
				barred_irregular_galaxy     .setState(galaxies_b);
				barred_spiral_galaxy        .setState(galaxies_b);
				dwarf_spheroidal_galaxy     .setState(galaxies_b);
				elliptical_galaxy           .setState(galaxies_b);
				galaxy_cluster              .setState(galaxies_b);
				globular_cluster            .setState(galaxies_b);
				interacting_galaxy          .setState(galaxies_b);
				irregular_galaxy            .setState(galaxies_b);
				lenticular_galaxy           .setState(galaxies_b);
				spiral_galaxy               .setState(galaxies_b);
				starburst_galaxy            .setState(galaxies_b);
				supergiant_elliptical_galaxy.setState(galaxies_b);

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

				star                        .setState(false);
				double_star                 .setState(false);
				variable_star               .setState(false);
				asterism                    .setState(false);
				open_cluster                .setState(false);

				nebula                      .setState(false);
				dark_nebula                 .setState(false);
				diffuse_nebula              .setState(false);
				emission_nebula             .setState(false);
				gaseous_nebula              .setState(false);
				planetary_nebula            .setState(false);
				reflection_nebula           .setState(false);
				supernova_remnant           .setState(false);
				open_cluster_and_nebula     .setState(false);

				galaxy                      .setState(false);
				barred_irregular_galaxy     .setState(false);
				barred_spiral_galaxy        .setState(false);
				dwarf_spheroidal_galaxy     .setState(false);
				elliptical_galaxy           .setState(false);
				galaxy_cluster              .setState(false);
				globular_cluster            .setState(false);
				interacting_galaxy          .setState(false);
				irregular_galaxy            .setState(false);
				lenticular_galaxy           .setState(false);
				spiral_galaxy               .setState(false);
				starburst_galaxy            .setState(false);
				supergiant_elliptical_galaxy.setState(false);

				unidentified                .setState(false);
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
