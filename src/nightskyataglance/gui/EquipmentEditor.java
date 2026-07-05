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


import java.awt.Button;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.astro.PracticalAstronomy;
import lib.stars.catalog.DsoAlias.Element;
import lib.astro.CameraCatalog;
import lib.astro.CameraEntry;
import lib.astro.EyepieceCatalog;
import lib.astro.EyepieceEntry;
import lib.astro.OpticalTubeAssemblyCatalog;
import lib.astro.OpticalTubeAssemblyEntry;
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

@SuppressWarnings("unused")
public class EquipmentEditor extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2519693274364184533L;

	public final MainFrame main_frame;
	public final EquipmentEditor dialog;

	private JComboBox<String> ota_cb;
	private String[] ota_list;
	private Hashtable<String,OpticalTubeAssemblyEntry> editable_ota_table = null;

	private static final String add_new = "Add New";

	private static final JLabel ota_aperture_jl     = new JLabel("Aperture (mm)");
	private static final JLabel ota_focal_len_jl    = new JLabel("Focal Length (mm)");
	private static final JLabel ota_fratio_jl       = new JLabel("F-Ratio");
	private static final JLabel ota_reducer_jl      = new JLabel("Barlow/Reducer (x)");
	private static final JLabel ota_native_jl       = new JLabel("Native");
	private static final JLabel ota_effective_jl    = new JLabel("Effective");
	private static final JLabel ota_jl              = new JLabel("Optical Tube Assembly");
	private static final JLabel ota_name_jl         = new JLabel("Name");
	private static final JLabel ota_index_jl        = new JLabel("Index");

	private static final JTextField ota_name            = new JTextField("", 20);
	private static final JTextField ota_index           = new JTextField("", 5);
	private static final JTextField ota_aperture_mm     = new JTextField("", 14);
	private static final JTextField ota_focal_length_mm = new JTextField("", 14);
	private static final JTextField ota_f_ratio         = new JTextField("", 14);
	private static final JTextField eff_aperture_mm     = new JTextField("", 14);
	private static final JTextField eff_focal_length_mm = new JTextField("", 14);
	private static final JTextField eff_f_ratio         = new JTextField("", 14);
	private static final JTextField ota_reducer         = new JTextField("", 14);

	private JComboBox<String> eye_cb;
	private String[] eye_list;
	private Hashtable<String,EyepieceEntry> editable_eye_table = null;

	private final JLabel eye_name_jl      = new JLabel("Name");
	private final JLabel eye_index_jl     = new JLabel("Index");
	private final JLabel eye_focal_len_jl = new JLabel("Focal Length (mm)");
	private final JLabel eye_app_fov_jl   = new JLabel("Apparent Field of View (" + new String(Character.toChars(0x00B0)) + ")");
	private final JLabel eyepiece_jl      = new JLabel("Eyepiece");

	private final JTextField eye_name        = new JTextField("", 20);
	private final JTextField eye_index       = new JTextField("", 5);
	private final JTextField eye_focal_length_mm       = new JTextField("", 10);
	private final JTextField eye_app_field_of_view_deg = new JTextField("", 12);

	private JComboBox<String> cam_cb;
	private String[] cam_list;
	private Hashtable<String,CameraEntry> editable_cam_table = null;

	private final JLabel cam_sensor_width_mm_jl  = new JLabel("Sensor Width (mm)");
	private final JLabel cam_sensor_height_mm_jl = new JLabel("Sensor Height (mm)");
	private final JLabel cam_sensor_width_px_jl  = new JLabel("Sensor Width (px)");
	private final JLabel cam_sensor_height_px_jl = new JLabel("Sensor Height (px)");
	private final JLabel cam_sensor_area_mpx_jl  = new JLabel("Sensor Area (Mpx)");
	private final JLabel cam_min_expo_sec_jl     = new JLabel("Min Exposure (sec)");
	private final JLabel cam_pixel_size_jl       = new JLabel("Pixel Size (" + new String(Character.toChars(0x03BC)) + "m)");
	private final JLabel cam_sensor_bit_depth_jl = new JLabel("Sensor Bit Depth");
	private final JLabel cam_lens_interface_jl   = new JLabel("Lens Interface");
	private final JLabel cam_color_jl            = new JLabel("Color");
	private final JLabel cam_max_expo_sec_jl     = new JLabel("Max Exposure (sec)");
	private final JLabel cam_e_well_depth_jl     = new JLabel("Electron Well Depth (ke)");
	private final JLabel cam_read_noise_jl       = new JLabel("Read Noise (e)");
	private final JLabel cam_quantum_eff_jl      = new JLabel("Quantum Efficiency (%)");
	private final JLabel cam_form_factor_jl      = new JLabel("Form Factor");
	private final JLabel camera_jl               = new JLabel("Camera");

	private final JLabel     cam_name_jl     = new JLabel("Name");
	private final JTextField cam_name        = new JTextField("", 20);
	private final JLabel     cam_index_jl    = new JLabel("Index");
	private final JTextField cam_index       = new JTextField("", 5);

	private int spaces = 10;
	private final JTextField cam_sensor_width_mm    = new JTextField("", spaces);
	private final JTextField cam_sensor_height_mm   = new JTextField("", spaces);
	private final JTextField cam_sensor_width_px    = new JTextField("", spaces);
	private final JTextField cam_sensor_height_px   = new JTextField("", spaces);
	private final JTextField cam_sensor_area_mpx    = new JTextField("", spaces);
	private final JTextField cam_pixel_size_um      = new JTextField("", spaces);
	private final JTextField cam_form_factor        = new JTextField("", spaces);
	private final JTextField cam_lens_interface     = new JTextField("", spaces);
	private final JTextField cam_color              = new JTextField("", spaces);
	private final JTextField cam_max_exposure_sec   = new JTextField("", spaces);
	private final JTextField cam_min_exposure_sec   = new JTextField("", spaces);
	private final JTextField cam_bit_depth          = new JTextField("", spaces);
	private final JTextField cam_well_depth         = new JTextField("", spaces);
	private final JTextField cam_noise              = new JTextField("", spaces);
	private final JTextField cam_quantum_eff        = new JTextField("", spaces);

	private final String underscore = "________________________";

	private JButton close       = new JButton("Close");

	private JButton ota_create  = new JButton("Create");
	private JButton ota_delete  = new JButton("Delete");
	private JButton eye_create  = new JButton("Create");
	private JButton eye_delete  = new JButton("Delete");
	private JButton cam_create  = new JButton("Create");
	private JButton cam_delete  = new JButton("Delete");

	private static int cam_state = -1;
	
	private boolean set_action_listeners = true;

	public EquipmentEditor(MainFrame f, String name)
	{
		super(f, name, true);

		main_frame    = f;
		dialog   = this;

		// set component font and alignment
		Font font = PersistentState.get_font();
		setFont(font);

		setup_otas();
		set_ota_laf(font);
		init_ota_fields();

		setup_eyes();
		set_eye_laf(font);
		init_eye_fields();

		setup_cams();
		set_cam_laf(font);
		init_cam_fields();

		close.setFont(font);

		// calculate_fields();

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
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;
		int row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(""), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		row += 1;


		JPanel ota_panel = new JPanel(new GridBagLayout());
		// ota_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int ota_row = 0;

		JPanel ota_name_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_name_panel.add(ota_name_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_name_panel.add(ota_name, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_name_panel.add(ota_index_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_name_panel.add(ota_index, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_name_panel, gbc);


		ota_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_aperture_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_focal_len_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_fratio_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_reducer_jl, gbc);


		ota_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_native_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_aperture_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_focal_length_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_f_ratio, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_reducer, gbc);


		ota_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_effective_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(eff_aperture_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(eff_focal_length_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(eff_f_ratio, gbc);


		ota_row += 1;

		JPanel ota_button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_button_panel.add(ota_create, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_button_panel.add(ota_delete, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = ota_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		ota_panel.add(ota_button_panel, gbc);


		ota_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(ota_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(ota_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(ota_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(""), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);


		row += 1;

		JPanel eye_panel = new JPanel(new GridBagLayout());
		// eye_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int eye_row = 0;

		JPanel eye_name_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_name_panel.add(eye_name_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_name_panel.add(eye_name, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_name_panel.add(eye_index_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_name_panel.add(eye_index, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_name_panel, gbc);


		eye_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_focal_len_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_app_fov_jl, gbc);

		eye_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_focal_length_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_app_field_of_view_deg, gbc);


		eye_row += 1;

		JPanel eye_button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_button_panel.add(eye_create, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_button_panel.add(eye_delete, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_button_panel, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(eyepiece_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(eye_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(eye_panel, gbc);

		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(""), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new Label(underscore+underscore+underscore+underscore), gbc);


		row += 1;


		JPanel cam_panel = new JPanel(new GridBagLayout());
		// cam_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int cam_row = 0;

		JPanel cam_name_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_name_panel.add(cam_name_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_name_panel.add(cam_name, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_name_panel.add(cam_index_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_name_panel.add(cam_index, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_name_panel, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_width_mm_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_height_mm_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_width_px_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_height_px_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_area_mpx_jl, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_width_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_height_mm, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_width_px, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_height_px, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_area_mpx, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_min_expo_sec_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_max_expo_sec_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_lens_interface_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_color_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_form_factor_jl, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_min_exposure_sec, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_max_exposure_sec, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_lens_interface, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_color, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_form_factor, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_sensor_bit_depth_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_e_well_depth_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_read_noise_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_quantum_eff_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_pixel_size_jl, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_bit_depth, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_well_depth, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_noise, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_quantum_eff, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_pixel_size_um, gbc);


		cam_row += 1;

		JPanel cam_button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_button_panel.add(cam_create, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_button_panel.add(cam_delete, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_button_panel, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(camera_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(cam_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(cam_panel, gbc);


		row += 1;

		JPanel button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(close, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(button_panel, gbc);
		

		if (set_action_listeners) {
			set_action_listeners = false;
			ota_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt);
					// System.out.printf("%s: %d: mod='%d'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt.getModifiers());
					if (evt.getModifiers() != 0) {
						// System.out.printf("%s: %d: mod='%d'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt.getModifiers());
						int    idx  = ota_cb.getSelectedIndex();
						String name = (0 < idx && idx < ota_cb.getItemCount()) ? ota_cb.getItemAt(idx).strip() : null;
						String key  = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.toLowerCase();
						// System.out.printf("%s: %d: idx=%d name='%s' key='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, name, key);
						if (key == null) {
							ota_name           .setText("");
							ota_index          .setText("");
							ota_aperture_mm    .setText("");
							ota_focal_length_mm.setText("");
							ota_f_ratio        .setText("");
							ota_reducer        .setText("");
							eff_aperture_mm    .setText("");
							eff_focal_length_mm.setText("");
							eff_f_ratio        .setText("");

							ota_create.setText("Create");
							ota_create.setEnabled(false);
							ota_delete.setEnabled(false);
						} else {
							OpticalTubeAssemblyEntry elt = editable_ota_table.get(key);
							// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
							ota_name           .setText(elt.name);
							ota_index          .setText(String.format("%d",   idx));
							ota_aperture_mm    .setText(String.format("%.1f", elt.aperture));
							ota_focal_length_mm.setText(String.format("%.1f", elt.focal_length));
							ota_f_ratio        .setText(String.format("%.2f", elt.f_ratio));
							ota_reducer        .setText(String.format("%.2f", elt.reducer));
							eff_aperture_mm    .setText(String.format("%.1f", elt.aperture));
							eff_focal_length_mm.setText(String.format("%.1f", elt.focal_length*elt.reducer));
							eff_f_ratio        .setText(String.format("%.2f", (elt.focal_length*elt.reducer)/elt.aperture));

							ota_create.setText("Update");
							ota_create.setEnabled(true);
							ota_delete.setEnabled(true);
						}
					}
				}
			});

			// TODO 
			ota_name.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt);
					String name     = ota_name.getText();
					int    pos      = ota_name.getCaretPosition();
					int    key_char = evt.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if (key_char == KeyEvent.VK_BACK_SPACE) {
						// System.out.printf("%s: %d: BS  name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else if (key_char == KeyEvent.VK_DELETE) {
						// System.out.printf("%s: %d: DEL name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else {
						name = (name.substring(0,pos) + (char) key_char + name.substring(pos, name.length()));
						// System.out.printf("%s: %d: '%c' name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, name);
					}
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					String key = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.strip().toLowerCase();
					// System.out.printf("%s: %d: name='%s' key='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key);
					if (key == null) {
						// no name (empty field or "Add New"), must be new (no create, no delete)
						// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+0+")");
						ota_cb.setSelectedIndex(0);
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+0+")");

						// ota_create.setText("Create");
						// ota_create.setEnabled(false);
						// ota_delete.setEnabled(false);
					} else {
						// there is a name, does it exist, and is it editable?
						OpticalTubeAssemblyEntry elt = PersistentState.otas.table.get(key);
						// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
						if (elt == null) {
							// name is reasonable and not found, must be new (create only)
							// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
							if (ota_cb.getSelectedIndex() != 0) {
								// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+0+")");
								ota_cb.setSelectedIndex(0);
								// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+0+")");
							}
							ota_create.setText("Create");
							ota_create.setEnabled(true);
							ota_delete.setEnabled(false);
						} else {
							if (elt.editable) {
								// element exists and is editable (update, delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								for (int i=1; i < ota_list.length; i++) {
									if (ota_cb.getItemAt(i).strip().toLowerCase().equalsIgnoreCase(key)) {
										// System.out.printf("%s: %d: idx=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i);
										// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+i+")");
										ota_cb.setSelectedIndex(i);
										// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+i+")");
										ota_index.setText(String.format("%d", i));
										break;
									}
								}
								
								// TODO
								/*
								ota_aperture_mm    .setText(String.format("%.1f", elt.aperture));
								ota_focal_length_mm.setText(String.format("%.1f", elt.focal_length));
								ota_f_ratio        .setText(String.format("%.2f", elt.f_ratio));
								ota_reducer        .setText(String.format("%.2f", elt.reducer));
								eff_aperture_mm    .setText(String.format("%.1f", elt.aperture));
								eff_focal_length_mm.setText(String.format("%.1f", elt.focal_length*elt.reducer));
								eff_f_ratio        .setText(String.format("%.2f", (elt.focal_length*elt.reducer)/elt.aperture));
								*/

								ota_create.setText("Update");
								ota_create.setEnabled(true);
								ota_delete.setEnabled(true);
							} else {
								// element exists and is NOT editable (no create, no delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								ota_create.setText("Update");
								ota_create.setEnabled(false);
								ota_delete.setEnabled(false);
							}
						}
					}
					if (key_char == KeyEvent.VK_TAB) {
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "TAB");
						ota_aperture_mm.requestFocus();
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			ota_aperture_mm.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt);
					String text = ota_aperture_mm.getText();
					int pos = ota_aperture_mm.getCaretPosition();
					int key_char = evt.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key_char && key_char <= KeyEvent.VK_9) || key_char == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key_char + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}

					if (text.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && 
							ota_focal_length_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
						OpticalTubeAssemblyEntry elt = calc_ota_fields(ota_name.getText(), text, ota_focal_length_mm.getText(), ota_reducer.getText());
	
						if (elt != null) {
							// ota_aperture_mm    .setText(String.format("%.0f", rv.aperture));
							ota_focal_length_mm.setText(String.format("%.1f", elt.focal_length));
							ota_f_ratio        .setText(String.format("%.2f", elt.f_ratio));
							ota_reducer        .setText(String.format("%.2f", elt.reducer));
							eff_aperture_mm    .setText(String.format("%.1f", elt.eff_aperture));
							eff_focal_length_mm.setText(String.format("%.1f", elt.eff_f_length));
							eff_f_ratio        .setText(String.format("%.2f", elt.eff_f_ratio));
						}
					}
					if (key_char == KeyEvent.VK_TAB) {
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "TAB");
						ota_focal_length_mm.requestFocus();
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			ota_focal_length_mm.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt);
					String text = ota_focal_length_mm.getText();
					int pos = ota_focal_length_mm.getCaretPosition();
					int key_char = evt.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key_char && key_char <= KeyEvent.VK_9) || key_char == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key_char + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}

					if (ota_aperture_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") 
							&& text.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
						OpticalTubeAssemblyEntry elt = calc_ota_fields(ota_name.getText(), ota_aperture_mm.getText(), text, ota_reducer.getText());
	
						if (elt != null) {
							ota_aperture_mm    .setText(String.format("%.1f", elt.aperture));
							// ota_focal_length_mm.setText(String.format("%.0f", rv.f_length));
							ota_f_ratio        .setText(String.format("%.2f", elt.f_ratio));
							ota_reducer        .setText(String.format("%.2f", elt.reducer));
							eff_aperture_mm    .setText(String.format("%.1f", elt.eff_aperture));
							eff_focal_length_mm.setText(String.format("%.1f", elt.eff_f_length));
							eff_f_ratio        .setText(String.format("%.2f", elt.eff_f_ratio));
						}
					}
					if (key_char == KeyEvent.VK_TAB) {
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "TAB");
						ota_reducer.requestFocus();
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			ota_reducer.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), evt);
					String text = ota_reducer.getText();
					int pos = ota_reducer.getCaretPosition();
					int key_char = evt.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key_char && key_char <= KeyEvent.VK_9) || key_char == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key_char + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}

					if (ota_aperture_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && 
							ota_focal_length_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") &&
							text.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
						OpticalTubeAssemblyEntry elt = calc_ota_fields(ota_name.getText(), ota_aperture_mm.getText(), ota_focal_length_mm.getText(), text);
	
						if (elt != null) {
							ota_aperture_mm    .setText(String.format("%.1f", elt.aperture));
							ota_focal_length_mm.setText(String.format("%.1f", elt.focal_length));
							ota_f_ratio        .setText(String.format("%.2f", elt.f_ratio));
							// ota_reducer        .setText(String.format("%.2f", rv.reducer));
							eff_aperture_mm    .setText(String.format("%.1f", elt.eff_aperture));
							eff_focal_length_mm.setText(String.format("%.1f", elt.eff_f_length));
							eff_f_ratio        .setText(String.format("%.2f", elt.eff_f_ratio));
						}
					}
					if (key_char == KeyEvent.VK_TAB) {
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "TAB");
						ota_name.requestFocus();
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			eye_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					int idx = eye_cb.getSelectedIndex();
					if (idx == 0) {
						eye_name.setText("");

						eye_create.setText("Create");
						eye_create.setEnabled(false);
						eye_delete.setEnabled(false);
					} else {
						String name = (0 < idx && idx < eye_cb.getItemCount()) ? eye_cb.getItemAt(idx).strip() : null;
						String key  = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.toLowerCase();
						// System.out.printf("%s: %d: idx=%d name='%s' key='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, name, key);
						EyepieceEntry elt = editable_eye_table.get(key);
						eye_name                 .setText(                      elt.name);
						eye_focal_length_mm      .setText(String.format("%.1f", elt.focal_length_mm));
						eye_app_field_of_view_deg.setText(String.format("%.1f", elt.apparent_field_of_view_deg));

						eye_create.setText("Update");
						eye_create.setEnabled(true);
						eye_delete.setEnabled(true);
					}
				}
			});

			// TODO
			eye_name.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String name  = eye_name.getText();
					int pos      = eye_name.getCaretPosition();
					int key_char = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if (key_char == KeyEvent.VK_BACK_SPACE) {
						// System.out.printf("%s: %d: BS  name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else if (key_char == KeyEvent.VK_DELETE) {
						// System.out.printf("%s: %d: DEL name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else {
						name = (name.substring(0,pos) + (char) key_char + name.substring(pos, name.length()));
						// System.out.printf("%s: %d: '%c' name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, name);
					}
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					String key = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.strip().toLowerCase();
					// System.out.printf("%s: %d: name='%s' key='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key);
					if (key == null) {
						// no name, must be new (no create, no delete)
						// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+0+")");
						eye_cb.setSelectedIndex(0);
						// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+0+")");
					} else {
						// there is a name, does it exist, and is it editable?
						EyepieceEntry elt = PersistentState.eyepieces.table.get(key);
						if (elt == null) {
							if (eye_cb.getSelectedIndex() != 0) {
								// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+0+")");
								eye_cb.setSelectedIndex(0);
								// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+0+")");
							}
							eye_create.setText("Create");
							eye_create.setEnabled(true);
							eye_delete.setEnabled(false);
						} else {
							if (elt.editable) {
								// element exists and is editable (update, delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								for (int i=1; i < eye_cb.getItemCount(); i++) {
									if (eye_list[i].strip().toLowerCase().equalsIgnoreCase(key)) {
										// System.out.printf("%s: %d: idx=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i);
										// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Jump "+i+")");
										eye_cb.setSelectedIndex(i);
										// System.out.printf("%s: %d: evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), "Select Index (Return "+i+")");
										eye_index.setText(String.format("%d", i));
										break;
									}
								}

								eye_create.setText("Update");
								eye_create.setEnabled(true);
								eye_delete.setEnabled(true);
							} else {
								// element exists and is NOT editable (no create, no delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								eye_create.setText("Update");
								eye_create.setEnabled(false);
								eye_delete.setEnabled(false);
							}
						}
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			cam_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					cam_state = cam_cb.getSelectedIndex();
					if (cam_state == 0) {
						cam_name.setText("");

						cam_create.setText("Create");
						cam_create.setEnabled(false);
						cam_delete.setEnabled(false);
					} else {
						CameraEntry elt = editable_cam_table.get(cam_list[cam_state].strip().toLowerCase());

						cam_name.setText(elt.name);
						cam_sensor_width_mm .setText(String.format("%.2f", elt.sensor_width_mm));
						cam_sensor_height_mm.setText(String.format("%.2f", elt.sensor_height_mm));
						cam_sensor_width_px .setText(String.format("%.0f", elt.sensor_width_px));
						cam_sensor_height_px.setText(String.format("%.0f", elt.sensor_height_px));

						cam_min_exposure_sec.setText(String.format("%.6f", elt.min_exposure_sec));
						cam_max_exposure_sec.setText(String.format("%.0f", elt.max_exposure_sec));
						cam_lens_interface  .setText(elt.lens_interface);
						cam_color           .setText(elt.color);
		
						cam_bit_depth       .setText(String.format("%.0f", elt.bit_depth));
						cam_well_depth      .setText(String.format("%.1f", elt.well));
						cam_noise           .setText(String.format("%.1f", elt.noise));
						cam_quantum_eff     .setText(String.format("%.1f", elt.quantum_efficiency));

						cam_sensor_area_mpx .setText(String.format("%.1f", elt.sensor_width_px * elt.sensor_height_px / 1e6));
						cam_form_factor     .setText(elt.sensor_form);
						cam_pixel_size_um   .setText(String.format("%.2f", elt.pixel_size_um));

						cam_create.setText("Update");
						cam_create.setEnabled(true);
						cam_delete.setEnabled(true);
					}
				}
			});

			// TODO
			cam_name.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String name = cam_name.getText();
					int pos = cam_name.getCaretPosition();
					int key = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if (key == KeyEvent.VK_BACK_SPACE) {
						// System.out.printf("%s: %d: BS  name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else if (key == KeyEvent.VK_DELETE) {
						// System.out.printf("%s: %d: DEL name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					} else {
						name = (name.substring(0,pos) + (char) key + name.substring(pos, name.length()));
						// System.out.printf("%s: %d: '%c' name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, name);
					}
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
					if (name == null || name.strip().equals("")) {
						// no name, must be new (no create, no delete)
						// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
						set_cam_fields(0, true);
						cam_cb.setSelectedIndex(0);
						cam_create.setText("Create");
						cam_create.setEnabled(false);
						cam_delete.setEnabled(false);
					} else {
						// there is a name, does it exist, and is it editable?
						CameraEntry elt = PersistentState.cameras.table.get(name.strip().toLowerCase());
						if (elt == null) {
							set_cam_fields(0, true);
							if (name.equalsIgnoreCase(add_new)) {
								// stupid name ("Add New"), don't use it
								// ota_name.setText(name);
								cam_create.setText("Create");
								cam_create.setEnabled(false);
								cam_delete.setEnabled(false);
							} else {
								// name is reasonable and not found, must be new (create only)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								if (cam_cb.getSelectedIndex() != 0) {
									cam_cb.setSelectedIndex(0);
									// ota_name.setText(name.substring(0, name.length()));
								}
								cam_create.setText("Create");
								cam_create.setEnabled(true);
								cam_delete.setEnabled(false);
							}
						} else {
							if (elt.editable) {
								// element exists and is editable (update, delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								for (int i=1; i < cam_list.length; i++) {
									if (cam_list[i].strip().toLowerCase().equalsIgnoreCase(name.strip().toLowerCase())) {
										// System.out.printf("%s: %d: idx=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i);
										cam_cb.setSelectedIndex(i);
										set_cam_fields(i, false);
										break;
									}
								}
								cam_create.setText("Update");
								cam_create.setEnabled(true);
								cam_delete.setEnabled(true);
							} else {
								// element exists and is NOT editable (no create, no delete)
								// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name);
								cam_create.setText("Update");
								cam_create.setEnabled(false);
								cam_delete.setEnabled(false);
							}
						}
					}
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			cam_sensor_width_mm.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String text = cam_sensor_width_mm.getText();
					int pos = cam_sensor_width_mm.getCaretPosition();
					int key = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key && key <= KeyEvent.VK_9) || key == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}
					CameraEntry elt = calc_cam_fields(cam_name.getText(), 
							text,                           cam_sensor_height_mm.getText(),
							cam_sensor_width_px.getText(),  cam_sensor_height_px.getText(),
							cam_lens_interface.getText(),   cam_color.getText(),
							cam_max_exposure_sec.getText(), cam_min_exposure_sec.getText(),
							cam_bit_depth.getText(),        cam_well_depth.getText(), 
							cam_noise.getText(),            cam_quantum_eff.getText());

					if (elt != null) {
						cam_form_factor.setText(elt.sensor_form);
						cam_pixel_size_um.setText(String.format("%.2f", elt.pixel_size_um));
					}
					// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			cam_sensor_height_mm.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String text = cam_sensor_height_mm.getText();
					int pos = cam_sensor_height_mm.getCaretPosition();
					int key = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key && key <= KeyEvent.VK_9) || key == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}
					CameraEntry elt = calc_cam_fields(cam_name.getText(), 
							cam_sensor_width_mm.getText(),  text,
							cam_sensor_width_px.getText(),  cam_sensor_height_px.getText(),
							cam_lens_interface.getText(),   cam_color.getText(),
							cam_max_exposure_sec.getText(), cam_min_exposure_sec.getText(),
							cam_bit_depth.getText(),        cam_well_depth.getText(), 
							cam_noise.getText(),            cam_quantum_eff.getText());

					if (elt != null) {
						cam_form_factor.setText(elt.sensor_form);
						cam_pixel_size_um.setText(String.format("%.2f", elt.pixel_size_um));
					}
					// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			cam_sensor_width_px.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String text = cam_sensor_width_px.getText();
					int pos = cam_sensor_width_px.getCaretPosition();
					int key = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key && key <= KeyEvent.VK_9) || key == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}
					CameraEntry elt = calc_cam_fields(cam_name.getText(), 
							cam_sensor_width_mm.getText(),  cam_sensor_height_mm.getText(),
							text,                           cam_sensor_height_px.getText(),
							cam_lens_interface.getText(),   cam_color.getText(),
							cam_max_exposure_sec.getText(), cam_min_exposure_sec.getText(),
							cam_bit_depth.getText(),        cam_well_depth.getText(), 
							cam_noise.getText(),            cam_quantum_eff.getText());

					if (elt != null) {
						cam_sensor_area_mpx.setText(String.format("%.1f", elt.sensor_width_px * elt.sensor_height_px / 1e6));
						cam_pixel_size_um  .setText(String.format("%.2f", elt.pixel_size_um));
					}
					// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			cam_sensor_height_px.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) 
				{
					String text = cam_sensor_height_px.getText();
					int pos = cam_sensor_height_px.getCaretPosition();
					int key = e.getKeyChar();
					// System.out.printf("%s: %d: orig='%s' pos=%d key=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text, pos, (key==KeyEvent.VK_BACK_SPACE)?"bs":((key==KeyEvent.VK_DELETE)?"del":"'"+(char)key+"'"));
					if ((KeyEvent.VK_0 <= key && key <= KeyEvent.VK_9) || key == KeyEvent.VK_PERIOD) {
						text = (text.substring(0,pos) + (char) key + text.substring(pos, text.length()));
						// System.out.printf("%s: %d: '%c' %d text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (char) key, key, text);
					} else {
						// System.out.printf("%s: %d: *** text='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), text);
					}
					CameraEntry elt = calc_cam_fields(cam_name.getText(), 
							cam_sensor_width_mm.getText(),  cam_sensor_height_mm.getText(),
							cam_sensor_width_px.getText(),  text,
							cam_lens_interface.getText(),   cam_color.getText(),
							cam_max_exposure_sec.getText(), cam_min_exposure_sec.getText(),
							cam_bit_depth.getText(),        cam_well_depth.getText(), 
							cam_noise.getText(),            cam_quantum_eff.getText());

					if (elt != null) {
						cam_sensor_area_mpx.setText(String.format("%.1f", elt.sensor_width_px * elt.sensor_height_px / 1e6));
						cam_pixel_size_um  .setText(String.format("%.2f", elt.pixel_size_um));
					}
					// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
				}

				@Override public void keyPressed (KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
			});

			ota_create.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					if (chk_ota_fields()) {
						if (ota_create.getText().equalsIgnoreCase("Create")) {
							// JOptionPane.showMessageDialog(main_frame, "All OTA fields appear to be well formed.\nCreate the new record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);

							ota_cb.addItem(ota_name.getText());
						} else if (ota_create.getText().equalsIgnoreCase("Update")) {
							// JOptionPane.showMessageDialog(main_frame, "All OTA fields appear to be well formed.\nUpdate the existing record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);
						} else {
						}

						OpticalTubeAssemblyEntry elt = calc_ota_fields(ota_name.getText().strip(), ota_aperture_mm.getText(), ota_focal_length_mm.getText(), ota_reducer.getText());
						OpticalTubeAssemblyCatalog cat = PersistentState.otas;
						PersistentState.otas = OpticalTubeAssemblyCatalog.merge(cat, elt);
						editable_ota_table.put(ota_name.getText().strip().toLowerCase(), elt);

						ota_create.setText("Update");
						ota_create.setEnabled(true);
						ota_delete.setEnabled(true);
					}
				}
			});

			eye_create.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					if (chk_eye_fields()) {
						if (eye_create.getText().equalsIgnoreCase("Create")) {
							// JOptionPane.showMessageDialog(main_frame, "All eyepiece fields appear to be well formed.\nCreate the new record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);

							eye_cb.addItem(eye_name.getText());
						} else if (eye_create.getText().equalsIgnoreCase("Update")) {
							// JOptionPane.showMessageDialog(main_frame, "All eyepiece fields appear to be well formed.\nUpdate the existing record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);
						} else {
						}

						EyepieceEntry elt = calc_eye_fields(eye_name.getText().strip(), eye_focal_length_mm.getText(), eye_app_field_of_view_deg.getText());
						EyepieceCatalog cat = PersistentState.eyepieces;
						PersistentState.eyepieces = EyepieceCatalog.merge(cat, elt);
						editable_eye_table.put(eye_name.getText().strip().toLowerCase(), elt);

						eye_create.setText("Update");
						eye_create.setEnabled(true);
						eye_delete.setEnabled(true);
					}
				}
			});

			cam_create.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					if (chk_cam_fields()) {
						if (cam_create.getText().equalsIgnoreCase("Create")) {
							// JOptionPane.showMessageDialog(main_frame, "All camera fields appear to be well formed.\nCreate the new record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);

							cam_cb.addItem(cam_name.getText().strip());
						} else if (cam_create.getText().equalsIgnoreCase("Update")) {
							// JOptionPane.showMessageDialog(main_frame, "All camera fields appear to be well formed.\nUpdate the existing record.", "Input Status", JOptionPane.INFORMATION_MESSAGE);
						} else {
						}

						CameraEntry elt = calc_cam_fields(cam_name.getText(), 
								cam_sensor_width_mm.getText(),  cam_sensor_height_mm.getText(),
								cam_sensor_width_px.getText(),  cam_sensor_height_px.getText(),
								cam_lens_interface.getText(),   cam_color.getText(),
								cam_max_exposure_sec.getText(), cam_min_exposure_sec.getText(),
								cam_bit_depth.getText(),        cam_well_depth.getText(), 
								cam_noise.getText(),            cam_quantum_eff.getText());
						// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
						// System.out.printf("%s: %d: QE.='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), cam_quantum_eff.getText());

						CameraCatalog cat = PersistentState.cameras;
						PersistentState.cameras = CameraCatalog.merge(cat, elt);
						editable_cam_table.put(cam_name.getText().strip().toLowerCase(), elt);

						cam_create.setText("Update");
						cam_create.setEnabled(true);
						cam_delete.setEnabled(true);
					}
				}
			});

			ota_delete.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					int    idx  = ota_cb.getSelectedIndex();
					String name = (0 < idx && idx < ota_cb.getItemCount()) ? ota_cb.getItemAt(idx).strip() : null;
					String key  = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.toLowerCase();

					if (key == null) {
						JOptionPane.showMessageDialog(main_frame,
							    "Unable to remove '" + name + "', it's not in the OTA table.",
							    "Remove Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						OpticalTubeAssemblyEntry elt = editable_ota_table.get(key);
						// System.out.printf("%s: %d: idx=%d name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, name, key, elt);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_ota_table.size(), key, editable_ota_table.get(key));
						editable_ota_table.remove(key);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_ota_table.size(), key, editable_ota_table.get(key));
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.otas.table.size(), key, PersistentState.otas.table.get(key));
						PersistentState.otas.remove(key);
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.otas.table.size(), key, PersistentState.otas.table.get(key));

						dispose();
					}
				}
			});

			eye_delete.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					int    idx  = eye_cb.getSelectedIndex();
					String name = (0 < idx && idx < eye_cb.getItemCount()) ? eye_cb.getItemAt(idx).strip() : null;
					String key  = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.toLowerCase();

					if (key == null) {
						JOptionPane.showMessageDialog(main_frame,
							    "Unable to remove '" + name + "', it's not in the eyepiece table.",
							    "Remove Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						EyepieceEntry elt = editable_eye_table.get(key);
						// System.out.printf("%s: %d: idx=%d name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, name, key, elt);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_eye_table.size(), key, editable_eye_table.get(key));
						editable_eye_table.remove(key);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_eye_table.size(), key, editable_eye_table.get(key));
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.eyepieces.table.size(), key, PersistentState.eyepieces.table.get(key));
						PersistentState.eyepieces.remove(key);
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.eyepieces.table.size(), key, PersistentState.eyepieces.table.get(key));

						dispose();
					}
				}
			});

			cam_delete.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					int    idx  = cam_cb.getSelectedIndex();
					String name = (0 < idx && idx < cam_cb.getItemCount()) ? cam_cb.getItemAt(idx).strip() : null;
					String key  = (name == null || name.equals("") || name.equalsIgnoreCase(add_new)) ? null : name.toLowerCase();

					if (key == null) {
						JOptionPane.showMessageDialog(main_frame,
							    "Unable to remove '" + name + "', it's not in the camera table.",
							    "Remove Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						CameraEntry elt = editable_cam_table.get(key);
						// System.out.printf("%s: %d: idx=%d name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), idx, name, key, elt);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_cam_table.size(), key, editable_cam_table.get(key));
						editable_cam_table.remove(key);
						// System.out.printf("%s: %d: et len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), editable_cam_table.size(), key, editable_cam_table.get(key));
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.cameras.table.size(), key, PersistentState.cameras.table.get(key));
						PersistentState.cameras.remove(key);
						// System.out.printf("%s: %d: ps len=%d key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.cameras.table.size(), key, PersistentState.cameras.table.get(key));

						dispose();
					}
				}
			});
		}

		close.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				// TODO 
				// ota_state = ota_cb.getSelectedIndex();
				// eye_state = eye_cb.getSelectedIndex();
				cam_state = cam_cb.getSelectedIndex();

		    	dispose();
			}
		});

		return panel;
	}

	private boolean chk_eye_fields()
	{
		if (! eye_focal_length_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", eye_focal_len_jl.getText(), eye_focal_length_mm.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! eye_app_field_of_view_deg.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", eye_app_fov_jl.getText(), eye_app_field_of_view_deg.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void set_eye_laf(Font font)
	{
		eye_cb.setFont(font);

		eye_focal_len_jl         .setFont(font);
		eye_app_fov_jl           .setFont(font);
		eyepiece_jl              .setFont(font);
		eye_name_jl              .setFont(font);
		eye_index_jl             .setFont(font);

		eye_name                 .setFont(font);
		eye_index                .setFont(font);
		eye_index                .setEditable(false);
		eye_index                .setHorizontalAlignment(JTextField.RIGHT);

		eye_focal_length_mm      .setFont(font);
		eye_app_field_of_view_deg.setFont(font);

		eye_focal_length_mm      .setHorizontalAlignment(JTextField.RIGHT);
		eye_app_field_of_view_deg.setHorizontalAlignment(JTextField.RIGHT);

		eye_create.setFont(font);
		eye_delete.setFont(font);
	}

	private void init_eye_fields()
	{
		eye_cb.setSelectedIndex(0);

		eye_name                 .setText("");
		eye_focal_length_mm      .setText("");
		eye_app_field_of_view_deg.setText("");

		eye_create.setText("Create");
		eye_create.setEnabled(false);
		eye_delete.setEnabled(false);
	}

	private EyepieceEntry calc_eye_fields(String na, String str_f_len_mm, String str_app_fov_deg)
	{
		EyepieceEntry rv = null;

		if (str_f_len_mm.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && str_app_fov_deg.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			double focal_length_mm            = Double.parseDouble(str_f_len_mm);
			double apparent_field_of_view_deg = Double.parseDouble(str_app_fov_deg);
			double eye_relief_mm              = 0;
			rv = new EyepieceEntry(na, focal_length_mm, apparent_field_of_view_deg, eye_relief_mm, true);
		}

		return rv;
	}

	private void setup_eyes()
	{
		// collect the editable OTAs into list
		editable_eye_table = new Hashtable<String,EyepieceEntry>();
		for (EyepieceEntry elt: PersistentState.eyepieces.elts) {
			// System.out.printf("%s: %d: eye='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
			if (elt.editable) {
				editable_eye_table.put(elt.name.toLowerCase(), elt);
			}
		}

		// create the combo box list
		eye_list = new String[editable_eye_table.size()+1];
		Enumeration<String> eye_keys = editable_eye_table.keys();
		for (int i=0; i < (eye_list.length-1) && eye_keys.hasMoreElements(); i++) {
			// System.out.printf("%s: %d: ota[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, main_frame.otas.elts[i]);
			String key = eye_keys.nextElement();
			eye_list[i] = editable_eye_table.get(key).name;
		}
		eye_list[eye_list.length-1] = "";
		Arrays.sort(eye_list);
		int idx = 0;
		eye_list[idx] = add_new;
		for (int i=0; i < eye_list.length; i++) {
			// System.out.printf("%s: %d: eye[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, eye_list[i]);
		}

		eye_cb = new JComboBox<String>(eye_list);
		eye_cb.setSelectedIndex(idx);
	}

	private boolean chk_cam_fields()
	{
		if (! cam_sensor_width_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_sensor_width_mm_jl.getText(), cam_sensor_width_mm.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_sensor_height_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_sensor_height_mm_jl.getText(), cam_sensor_height_mm.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_sensor_width_px.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_sensor_width_px_jl.getText(), cam_sensor_width_px.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_sensor_height_px.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_sensor_height_px_jl.getText(), cam_sensor_height_px.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_min_exposure_sec.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_min_expo_sec_jl.getText(), cam_min_exposure_sec.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_max_exposure_sec.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_max_expo_sec_jl.getText(), cam_max_exposure_sec.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_bit_depth.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_sensor_bit_depth_jl.getText(), cam_bit_depth.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_well_depth.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_e_well_depth_jl.getText(), cam_well_depth.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! cam_noise.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", cam_read_noise_jl.getText(), cam_noise.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void set_cam_laf(Font font)
	{
		cam_cb.setFont(font);

		cam_sensor_width_mm_jl .setFont(font);
		cam_sensor_height_mm_jl.setFont(font);
		cam_sensor_width_px_jl .setFont(font);
		cam_sensor_height_px_jl.setFont(font);
		cam_min_expo_sec_jl    .setFont(font);
		cam_max_expo_sec_jl    .setFont(font);
		cam_pixel_size_jl      .setFont(font);
		cam_sensor_bit_depth_jl.setFont(font);
		cam_lens_interface_jl  .setFont(font);
		cam_color_jl           .setFont(font);
		cam_form_factor_jl     .setFont(font);
		cam_e_well_depth_jl    .setFont(font);
		cam_read_noise_jl      .setFont(font);
		cam_sensor_area_mpx_jl .setFont(font);
		cam_quantum_eff_jl     .setFont(font);

		camera_jl              .setFont(font);

		cam_sensor_area_mpx   .setFont(font);
		cam_sensor_width_mm   .setFont(font);
		cam_sensor_height_mm  .setFont(font);
		cam_sensor_width_px   .setFont(font);
		cam_sensor_height_px  .setFont(font);
		cam_pixel_size_um     .setFont(font);
		cam_lens_interface    .setFont(font);
		cam_color             .setFont(font);
		cam_form_factor       .setFont(font);
		cam_min_exposure_sec  .setFont(font);
		cam_max_exposure_sec  .setFont(font);
		cam_bit_depth         .setFont(font);
		cam_well_depth        .setFont(font);
		cam_noise             .setFont(font);
		cam_quantum_eff       .setFont(font);

		cam_pixel_size_um     .setEditable(false);
		cam_sensor_area_mpx   .setEditable(false);
		cam_form_factor       .setEditable(false);

		cam_sensor_width_mm   .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_height_mm  .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_width_px   .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_height_px  .setHorizontalAlignment(JTextField.RIGHT);
		cam_pixel_size_um     .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_area_mpx   .setHorizontalAlignment(JTextField.RIGHT);
		cam_lens_interface    .setHorizontalAlignment(JTextField.CENTER);
		cam_color             .setHorizontalAlignment(JTextField.CENTER);
		cam_form_factor       .setHorizontalAlignment(JTextField.CENTER);
		cam_max_exposure_sec  .setHorizontalAlignment(JTextField.RIGHT);
		cam_min_exposure_sec  .setHorizontalAlignment(JTextField.RIGHT);
		cam_bit_depth         .setHorizontalAlignment(JTextField.RIGHT);
		cam_well_depth        .setHorizontalAlignment(JTextField.RIGHT);
		cam_noise             .setHorizontalAlignment(JTextField.RIGHT);
		cam_quantum_eff       .setHorizontalAlignment(JTextField.RIGHT);

		cam_name_jl.setFont(font);
		cam_name.setFont(font);

		cam_index_jl.setFont(font);
		cam_index.setFont(font);
		cam_index.setEditable(false);
		cam_index.setHorizontalAlignment(JTextField.RIGHT);

		cam_create.setFont(font);
		cam_delete.setFont(font);
	}

	private void init_cam_fields()
	{
		set_cam_fields(0, true);
		cam_cb.setSelectedIndex(0);
		cam_name  .setText("");

		cam_create.setText("Create");
		cam_create.setEnabled(false);
		cam_delete.setEnabled(false);
	}

	private void set_cam_fields(int state, boolean set_name)
	{
		if (state <= 0 || cam_list.length <= state) {
			// System.out.printf("%s: %d: state=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), state);
			cam_state = 0;
			cam_index           .setText("");
		} else {
			// System.out.printf("%s: %d: state=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), state);
			cam_state = state;
			String name = cam_list[cam_state];
			CameraEntry elt = editable_cam_table.get(name.strip().toLowerCase());
			if (set_name) cam_name.setText(elt.name);
			cam_index           .setText(String.format("%d", cam_state));
			cam_sensor_width_mm .setText(String.format("%.2f", elt.sensor_width_mm));
			cam_sensor_height_mm.setText(String.format("%.2f", elt.sensor_height_mm));
			cam_sensor_width_px .setText(String.format("%.0f", elt.sensor_width_px));
			cam_sensor_height_px.setText(String.format("%.0f", elt.sensor_height_px));
			cam_pixel_size_um   .setText(String.format("%.2f", (1000*elt.sensor_width_mm/elt.sensor_width_px + 1000*elt.sensor_height_mm/elt.sensor_height_px)/2));
			cam_lens_interface  .setText(elt.lens_interface);
			cam_color           .setText(elt.color);
			cam_form_factor     .setText(elt.sensor_form);
			cam_bit_depth       .setText(String.format("%.0f", elt.bit_depth));
			cam_well_depth      .setText(String.format("%.1f", elt.well));
			cam_noise           .setText(String.format("%.1f", elt.noise));
			cam_quantum_eff     .setText(String.format("%.0f", elt.quantum_efficiency));
			cam_sensor_area_mpx .setText(String.format("%.1f", elt.sensor_width_px*elt.sensor_height_px/1e6));
		}
	}

	private void set_cam_fields(CameraEntry elt)
	{
		if (elt == null) {
			cam_name            .setText("");
			cam_index           .setText("");
			cam_sensor_width_mm .setText("");
			cam_sensor_height_mm.setText("");
			cam_sensor_width_px .setText("");
			cam_sensor_height_px.setText("");
			cam_sensor_area_mpx .setText("");
			cam_pixel_size_um   .setText("");
			cam_lens_interface  .setText("");
			cam_color           .setText("");
			cam_form_factor     .setText("");
			cam_bit_depth       .setText("");
			cam_well_depth      .setText("");
			cam_noise           .setText("");
			cam_quantum_eff     .setText("");
		} else {
			cam_name            .setText(elt.name);
			cam_index           .setText(String.format("%d",   cam_state));
			cam_sensor_width_mm .setText(String.format("%.2f", elt.sensor_width_mm));
			cam_sensor_height_mm.setText(String.format("%.2f", elt.sensor_height_mm));
			cam_sensor_width_px .setText(String.format("%.0f", elt.sensor_width_px));
			cam_sensor_height_px.setText(String.format("%.0f", elt.sensor_height_px));
			cam_sensor_area_mpx .setText(String.format("%.1f", elt.sensor_width_px*elt.sensor_height_px/1e6));
			cam_pixel_size_um   .setText(String.format("%.2f", (1000*elt.sensor_width_mm/elt.sensor_width_px + 1000*elt.sensor_height_mm/elt.sensor_height_px)/2));
			cam_lens_interface  .setText(elt.lens_interface);
			cam_color           .setText(elt.color);
			cam_form_factor     .setText(elt.sensor_form);
			cam_bit_depth       .setText(String.format("%.0f", elt.bit_depth));
			cam_well_depth      .setText(String.format("%.1f", elt.well));
			cam_noise           .setText(String.format("%.1f", elt.noise));
			cam_quantum_eff     .setText(String.format("%.0f", elt.quantum_efficiency));
		}
	}

	private CameraEntry calc_cam_fields(String na, 
			String swm, String shm, 
			String swp, String shp, 
			String li,  String c, 
			String mxe, String mne, 
			String bit, String wel, 
			String noi, String qef)
	{
		CameraEntry elt = null;

		if (swm.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && 
			shm.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && 
			swp.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") && 
			shp.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {

			String name               = na;
			double sensor_width_mm    = Double.parseDouble(swm);
			double sensor_height_mm   = Double.parseDouble(shm);
			double sensor_width_px    = Double.parseDouble(swp);
			double sensor_height_px   = Double.parseDouble(shp);
			double pixel_size_um      = (1000*sensor_width_mm/sensor_width_px + 1000*sensor_height_mm/sensor_height_px)/2;
			String sensor_form        = "";
			String lens_interface     = li;
			String color              = c;
			double max_exposure_sec   = mxe.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(mxe) : Double.NaN;
			double min_exposure_sec   = mne.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(mne) : Double.NaN;
			double bit_depth          = bit.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(bit) : Double.NaN;
			double well               = wel.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(wel) : Double.NaN;
			double noise              = noi.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(noi) : Double.NaN;
			double quantum_efficiency = qef.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*") ? Double.parseDouble(qef) : Double.NaN;

			elt = new CameraEntry(name, sensor_width_mm, sensor_height_mm, sensor_width_px, sensor_height_px, 
				sensor_form, lens_interface, color, max_exposure_sec, min_exposure_sec, bit_depth, well, noise, quantum_efficiency, true);
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);

			cam_sensor_area_mpx.setText(String.format("%.1f", elt.sensor_width_px*elt.sensor_height_px/1e6));
			cam_pixel_size_um  .setText(String.format("%.2f", (1000*elt.sensor_width_mm/elt.sensor_width_px + 1000*elt.sensor_height_mm/elt.sensor_height_px)/2));
			cam_form_factor    .setText(elt.sensor_form);
		}

		return elt;
	}

	private void setup_cams()
	{
		// collect the editable OTAs into list
		editable_cam_table = new Hashtable<String,CameraEntry>();
		for (CameraEntry elt: PersistentState.cameras.elts) {
			// System.out.printf("%s: %d: ota='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
			if (elt.editable) {
				editable_cam_table.put(elt.name.toLowerCase(), elt);
			}
		}

		// create the combo box list
		cam_list = new String[editable_cam_table.size()+1];
		Enumeration<String> cam_keys = editable_cam_table.keys();
		for (int i=0; i < (cam_list.length-1) && cam_keys.hasMoreElements(); i++) {
			// System.out.printf("%s: %d: ota[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, main_frame.otas.elts[i]);
			String key = cam_keys.nextElement();
			cam_list[i] = editable_cam_table.get(key).name;
		}
		cam_list[cam_list.length-1] = "";
		Arrays.sort(cam_list);
		cam_state = 0;
		cam_list[cam_state] = add_new;
		for (int i=0; i < cam_list.length; i++) {
			// System.out.printf("%s: %d: cam[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, cam_list[i]);
		}

		cam_cb = new JComboBox<String>(cam_list);
		cam_cb.setSelectedIndex(cam_state);
	}

	private boolean chk_ota_fields()
	{
		if (! ota_aperture_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", ota_aperture_jl.getText(), ota_aperture_mm.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! ota_focal_length_mm.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", ota_focal_len_jl.getText(), ota_focal_length_mm.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (! ota_reducer.getText().matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) {
			JOptionPane.showMessageDialog(main_frame,
				    String.format("The '%s' field contains '%s', which is not a valid number.", ota_reducer_jl.getText(), ota_reducer.getText()),
				    "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void set_ota_laf(Font font)
	{
		ota_cb.setFont(font);

		ota_aperture_jl    .setFont(font);
		ota_focal_len_jl   .setFont(font);
		ota_fratio_jl      .setFont(font);
		ota_reducer_jl     .setFont(font);
		ota_native_jl      .setFont(font);
		ota_effective_jl   .setFont(font);
		ota_jl             .setFont(font);
		ota_name_jl        .setFont(font);
		ota_index_jl       .setFont(font);

		ota_name           .setFont(font);
		ota_index          .setFont(font);
		ota_aperture_mm    .setFont(font);
		ota_focal_length_mm.setFont(font);
		ota_f_ratio        .setFont(font);
		ota_reducer        .setFont(font);
		eff_aperture_mm    .setFont(font);
		eff_focal_length_mm.setFont(font);
		eff_f_ratio        .setFont(font);

		ota_index          .setHorizontalAlignment(JTextField.RIGHT);
		ota_aperture_mm    .setHorizontalAlignment(JTextField.RIGHT);
		ota_focal_length_mm.setHorizontalAlignment(JTextField.RIGHT);
		ota_f_ratio        .setHorizontalAlignment(JTextField.RIGHT);
		ota_reducer        .setHorizontalAlignment(JTextField.RIGHT);
		eff_aperture_mm    .setHorizontalAlignment(JTextField.RIGHT);
		eff_focal_length_mm.setHorizontalAlignment(JTextField.RIGHT);
		eff_f_ratio        .setHorizontalAlignment(JTextField.RIGHT);

		ota_index          .setEditable(false);
		ota_f_ratio        .setEditable(false);
		eff_aperture_mm    .setEditable(false);
		eff_focal_length_mm.setEditable(false);
		eff_f_ratio        .setEditable(false);

		ota_create.setFont(font);
		ota_delete.setFont(font);
	}

	private void init_ota_fields()
	{
		ota_cb.setSelectedIndex(0);

		ota_name           .setText("");
		ota_index          .setText("");
		ota_aperture_mm    .setText("");
		ota_focal_length_mm.setText("");
		ota_f_ratio        .setText("");
		ota_reducer        .setText("");
		eff_aperture_mm    .setText("");
		eff_focal_length_mm.setText("");
		eff_f_ratio        .setText("");

		ota_create.setText("Create");
		ota_create.setEnabled(false);
		ota_delete.setEnabled(false);
	}

	private OpticalTubeAssemblyEntry calc_ota_fields(String str_name, String str_aperature, String str_f_length, String str_reducer)
	{
		double aperture = (str_aperature.matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) ? Double.parseDouble(str_aperature) : Double.POSITIVE_INFINITY;
		double f_length = (str_f_length .matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) ? Double.parseDouble(str_f_length)  : Double.POSITIVE_INFINITY;
		double reducer  = (str_reducer  .matches("[0-9]*[.][0-9][0-9]*|[0-9][0-9]*[.][0-9]*|[0-9][0-9]*")) ? Double.parseDouble(str_reducer)   : 1.0;

		OpticalTubeAssemblyEntry elt = new OpticalTubeAssemblyEntry(str_name, aperture, f_length, reducer, true);

		return elt;
	}

	private void setup_otas()
	{
		// collect the editable OTAs into list
		int ota_count = 0;
		editable_ota_table = new Hashtable<String,OpticalTubeAssemblyEntry>();
		for (OpticalTubeAssemblyEntry elt: PersistentState.otas.elts) {
			// System.out.printf("%s: %d: ota='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
			if (elt.editable) {
				editable_ota_table.put(elt.name.toLowerCase(), elt);
			}
		}

		// create the combo box list
		ota_list = new String[editable_ota_table.size()+1];
		Enumeration<String> ota_keys = editable_ota_table.keys();
		for (int i=0; i < (ota_list.length-1) && ota_keys.hasMoreElements(); i++) {
			// System.out.printf("%s: %d: ota[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, main_frame.otas.elts[i]);
			String key = ota_keys.nextElement();
			ota_list[i] = editable_ota_table.get(key).name;
		}
		ota_list[ota_list.length-1] = "";
		Arrays.sort(ota_list);

		int idx = 0;
		ota_list[idx] = add_new;
		// for (int i=0; i < ota_list.length; i++) {
			// System.out.printf("%s: %d: ota[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, ota_list[i]);
		// }

		ota_cb = new JComboBox<String>(ota_list);
		ota_cb.setSelectedIndex(idx);
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

	@Override public void windowActivated  (WindowEvent arg0) {}
	@Override public void windowClosed     (WindowEvent arg0) {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified  (WindowEvent arg0) {}
	@Override public void windowOpened     (WindowEvent arg0) {}
}
