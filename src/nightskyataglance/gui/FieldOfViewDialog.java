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
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

@SuppressWarnings("unused")
public class FieldOfViewDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2519693274364184533L;

	public final MainFrame main_frame;
	public final FieldOfViewDialog dialog;

	public static final JLabel dso_search_jl           = new JLabel("Search");
	public static final JLabel dso_type_jl             = new JLabel("Type");
	public static final JLabel dso_found_jl            = new JLabel("Found");
	public static final JLabel dso_ra_jl               = new JLabel("RA");
	public static final JLabel dso_dec_jl              = new JLabel("DEC");
	public static final JLabel dso_mag_jl              = new JLabel("Mag.");
	public static final JLabel dso_dia_jl              = new JLabel("Dia. (')");
	public static final JLabel dso_dso_jl              = new JLabel("Deep Space Object");

	public static final JLabel ota_aperture_jl         = new JLabel("Aperture (mm)");
	public static final JLabel ota_focal_len_jl        = new JLabel("Focal Length (mm)");
	public static final JLabel ota_fratio_jl           = new JLabel("F-Ratio");
	public static final JLabel ota_reducer_jl          = new JLabel("Barlow/Reducer (x)");
	public static final JLabel ota_native_jl           = new JLabel("Native");
	public static final JLabel ota_effective_jl        = new JLabel("Effective");
	public static final JLabel ota_ota_jl              = new JLabel("Optical Tube Assembly");

	public static final JLabel eye_focal_len2_jl       = new JLabel("Focal Length (mm)");
	public static final JLabel eye_app_fov_jl          = new JLabel("Apparent Field of View (" + new String(Character.toChars(0x00B0)) + ")");
	public static final JLabel eye_mag2_jl             = new JLabel("Magnification");
	public static final JLabel eye_fov_deg_jl          = new JLabel("Field of View (" + new String(Character.toChars(0x00B0)) + ")");
	public static final JLabel eye_fov_min_jl          = new JLabel("Field of View (')");
	public static final JLabel eye_eyepiece_jl         = new JLabel("Eyepiece");
	public static final JLabel cam_sensor_width_mm_jl  = new JLabel("Sensor Width (mm)");
	public static final JLabel cam_sensor_height_mm_jl = new JLabel("Sensor Height (mm)");
	public static final JLabel cam_sensor_width_px_jl  = new JLabel("Sensor Width (px)");
	public static final JLabel cam_sensor_height_px_jl = new JLabel("Sensor Height (px)");
	public static final JLabel cam_min_expo_sec_jl     = new JLabel("Min Exposure (sec)");
	public static final JLabel cam_pixel_size_jl       = new JLabel("Pixel Size (" + new String(Character.toChars(0x03BC)) + "m)");
	public static final JLabel cam_sensor_bit_depth_jl = new JLabel("Sensor Bit Depth");
	public static final JLabel cam_lens_interface_jl   = new JLabel("Lens Interface");
	public static final JLabel cam_color_jl            = new JLabel("Color");
	public static final JLabel cam_form_factor_jl      = new JLabel("Form Factor");
	public static final JLabel cam_max_expo_sec_jl     = new JLabel("Max Exposure (sec)");
	public static final JLabel cam_e_well_depth_jl     = new JLabel("Electron Well Depth (ke)");
	public static final JLabel cam_read_noise_jl       = new JLabel("Read Noise (e)");
	public static final JLabel cam_quantum_eff_jl      = new JLabel("Quantum Efficiency (%)");
	public static final JLabel cam_sensor_area_jl      = new JLabel("Sensor Area (Mpx)");
	public static final JLabel cam_width_fov_deg_jl    = new JLabel("Width FoV (" + new String(Character.toChars(0x00B0)) + ")");
	public static final JLabel cam_width_fov_min_jl    = new JLabel("Width FoV (')");
	public static final JLabel cam_height_fov_deg_jl   = new JLabel("Height FoV (" + new String(Character.toChars(0x00B0)) + ")");
	public static final JLabel cam_height_fov_min_jl   = new JLabel("Height FoV (')");
	public static final JLabel cam_arcsec_per_px_jl    = new JLabel("Arcsec (\") per Pixel");
	public static final JLabel cam_camera_jl           = new JLabel("Camera");
	public static final JLabel img_ra_jl               = new JLabel("RA");
	public static final JLabel img_de_jl               = new JLabel("DEC");
	public static final JLabel img_width_min_jl        = new JLabel("Width (')");
	public static final JLabel height_min_jl           = new JLabel("Height (')");
	public static final JLabel image_jl                = new JLabel("Image");
	public static final JLabel url_jl                  = new JLabel("URL");
	public static final JLabel web_jl                  = new JLabel("Web");

	int spc = 50;
	public static final JTextField search_tf = new JTextField("", 25);
	public static final JTextField found_tf  = new JTextField("", 55);
	public static final JTextField type_tf   = new JTextField("", 25);
	public static final JTextField ra_tf     = new JTextField("",  9);
	public static final JTextField dec_tf    = new JTextField("",  9);
	public static final JTextField mag_tf    = new JTextField("",  9);
	public static final JTextField size_tf   = new JTextField("",  9);
	public static final JTextField web_tf    = new JTextField("", 50);

	public final JComboBox<String> ota_cb;
	public final String[]   ota_list;
	public static final JTextField ota_aperture_mm           = new JTextField("", 14);
	public static final JTextField ota_focal_length_mm       = new JTextField("", 14);
	public static final JTextField ota_f_ratio               = new JTextField("", 14);
	public static final JTextField ota_reducer               = new JTextField("", 14);
	public static final JTextField eff_aperture_mm           = new JTextField("", 14);
	public static final JTextField eff_focal_length_mm       = new JTextField("", 14);
	public static final JTextField eff_f_ratio               = new JTextField("", 14);

	public final JComboBox<String> eyepiece_cb;
	public final String[]   eyepiece_list;
	public static final JTextField eye_focal_length_mm       = new JTextField("", 10);
	public static final JTextField eye_app_field_of_view_deg = new JTextField("", 12);
	public static final JTextField eye_magnification         = new JTextField("", 10);
	public static final JTextField eye_field_of_view_deg     = new JTextField("", 10);
	public static final JTextField eye_field_of_view_min     = new JTextField("", 10);

	public static int spaces = 10;
	public final JComboBox<String> camera_cb;
	public final String[]   camera_list;
	public static final JTextField cam_sensor_width_mm    = new JTextField("", spaces);
	public static final JTextField cam_sensor_height_mm   = new JTextField("", spaces);
	public static final JTextField cam_sensor_width_px    = new JTextField("", spaces);
	public static final JTextField cam_sensor_height_px   = new JTextField("", spaces);
	public static final JTextField cam_pixel_size_um      = new JTextField("", spaces);

	public static final JTextField cam_lens_interface     = new JTextField("", spaces);
	public static final JTextField cam_color              = new JTextField("", spaces);
	public static final JTextField cam_form_factor        = new JTextField("", spaces);
	public static final JTextField cam_max_exposure_sec   = new JTextField("", spaces);
	public static final JTextField cam_min_exposure_sec   = new JTextField("", spaces);
	public static final JTextField cam_bit_depth          = new JTextField("", spaces);

	public static final JTextField cam_well_depth         = new JTextField("", spaces);
	public static final JTextField cam_noise              = new JTextField("", spaces);
	public static final JTextField cam_quantum_eff        = new JTextField("", spaces);
	public static final JTextField cam_width_fov_deg      = new JTextField("", spaces);
	public static final JTextField cam_width_fov_min      = new JTextField("", spaces);
	public static final JTextField cam_height_fov_deg     = new JTextField("", spaces);
	public static final JTextField cam_height_fov_min     = new JTextField("", spaces);
	public static final JTextField cam_pixel_fov_sec      = new JTextField("", spaces);
	public static final JTextField cam_sensor_area_mpx    = new JTextField("", spaces);

	public static final int IMG_CAMERA   = 0;
	public static final int IMG_DSO      = 1 + IMG_CAMERA;
	public static final int IMG_EYEPIECE = 1 + IMG_DSO;
	public static final int IMG_CUSTOM   = 1 + IMG_EYEPIECE;
	public static final String[] img_list = { "Camera", "DSO", "Eyepiece", "Custom", };
	public static final JComboBox<String> img_cb = new JComboBox<String>(img_list);
	public static final JTextField img_ra_tf         = new JTextField("", 9);
	public static final JTextField img_de_tf         = new JTextField("", 9);
	public static final JTextField img_width_min_tf  = new JTextField("", 9);
	public static final JTextField img_height_min_tf = new JTextField("", 9);

	public static final int WEB_WIKIPEDIA  = 0;
	public static final int WEB_GROKIPEDIA = 1 + WEB_WIKIPEDIA;
	public static final int WEB_DUCKDUCKGO = 1 + WEB_GROKIPEDIA;
	public static final int WEB_GOOGLE     = 1 + WEB_DUCKDUCKGO;
	public static final String[] web_list = { "Wikipedia", "Grokipedia", "DuckDuckGo", "Google", };
	public final JComboBox<String> web_cb = new JComboBox<String>(web_list);

	private static final String underscore = "________________________";

	public final JButton close   = new JButton("Close");
	public final JButton nearest = new JButton("Nearest DSO");
	public final JButton image   = new JButton("Image");
	public final JButton web     = new JButton("Web");

	private static int ota_state = -1;
	private static int eye_state = -1;
	private static int cam_state = -1;
	private static int img_state = -1;
	private static int web_state = WEB_WIKIPEDIA;

	// TODO
	private void set_dso_laf(Font font)
	{
		dso_search_jl.setFont(font);
		dso_type_jl.setFont(font);
		dso_found_jl.setFont(font);
		dso_ra_jl.setFont(font);
		dso_dec_jl.setFont(font);
		dso_mag_jl.setFont(font);
		dso_dia_jl.setFont(font);
		dso_dso_jl.setFont(font);
	}

	private void set_ota_laf(Font font)
	{
		ota_aperture_jl    .setFont(font);
		ota_focal_len_jl   .setFont(font);
		ota_fratio_jl      .setFont(font);
		ota_reducer_jl     .setFont(font);
		ota_native_jl      .setFont(font);
		ota_effective_jl   .setFont(font);
		ota_ota_jl         .setFont(font);

		ota_aperture_mm    .setFont(font);
		ota_focal_length_mm.setFont(font);
		ota_f_ratio        .setFont(font);
		ota_reducer        .setFont(font);
		eff_aperture_mm    .setFont(font);
		eff_focal_length_mm.setFont(font);
		eff_f_ratio        .setFont(font);

		ota_f_ratio        .setEditable(false);
		eff_aperture_mm    .setEditable(false);
		eff_focal_length_mm.setEditable(false);
		eff_f_ratio        .setEditable(false);

		ota_aperture_mm    .setHorizontalAlignment(JTextField.RIGHT);
		ota_focal_length_mm.setHorizontalAlignment(JTextField.RIGHT);
		ota_f_ratio        .setHorizontalAlignment(JTextField.RIGHT);
		ota_reducer        .setHorizontalAlignment(JTextField.RIGHT);
		eff_aperture_mm    .setHorizontalAlignment(JTextField.RIGHT);
		eff_focal_length_mm.setHorizontalAlignment(JTextField.RIGHT);
		eff_f_ratio        .setHorizontalAlignment(JTextField.RIGHT);
	}

	private void set_eye_laf(Font font)
	{
		eye_focal_len2_jl.setFont(font);
		eye_app_fov_jl.setFont(font);
		eye_mag2_jl.setFont(font);
		eye_fov_deg_jl.setFont(font);
		eye_fov_min_jl.setFont(font);
		eye_eyepiece_jl.setFont(font);
	}

	private void set_cam_laf(Font font)
	{
		cam_sensor_width_mm_jl.setFont(font);
		cam_sensor_height_mm_jl.setFont(font);
		cam_sensor_width_px_jl.setFont(font);
		cam_sensor_height_px_jl.setFont(font);
		cam_min_expo_sec_jl.setFont(font);
		cam_pixel_size_jl.setFont(font);
		cam_sensor_bit_depth_jl.setFont(font);
		cam_lens_interface_jl.setFont(font);
		cam_color_jl.setFont(font);
		cam_form_factor_jl.setFont(font);
		cam_max_expo_sec_jl.setFont(font);
		cam_e_well_depth_jl.setFont(font);
		cam_read_noise_jl.setFont(font);
		cam_quantum_eff_jl.setFont(font);
		cam_sensor_area_jl.setFont(font);
		cam_width_fov_deg_jl.setFont(font);
		cam_width_fov_min_jl.setFont(font);
		cam_height_fov_deg_jl.setFont(font);
		cam_height_fov_min_jl.setFont(font);
		cam_arcsec_per_px_jl.setFont(font);
		cam_camera_jl.setFont(font);
	}

	private void set_unk_laf(Font font)
	{
	}

	public FieldOfViewDialog(MainFrame f, String name)
	{
		super(f, name, true);

		main_frame = f;
		dialog     = this;

		Font font = PersistentState.get_font();

		setFont(font);
		set_dso_laf(font);
		set_ota_laf(font);
		set_eye_laf(font);
		set_cam_laf(font);

		img_ra_jl.setFont(font);
		img_de_jl.setFont(font);
		img_width_min_jl.setFont(font);
		height_min_jl.setFont(font);
		image_jl.setFont(font);
		url_jl.setFont(font);
		web_jl.setFont(font);
		
		nearest.setFont(font);
		image.setFont(font);
		web.setFont(font);
		close.setFont(font);

		eye_magnification        .setFont(font);
		eye_field_of_view_deg    .setFont(font);
		eye_field_of_view_min    .setFont(font);

		eye_focal_length_mm      .setFont(font);
		eye_app_field_of_view_deg.setFont(font);

		eye_magnification        .setEditable(false);
		eye_field_of_view_deg    .setEditable(false);
		eye_field_of_view_min    .setEditable(false);

		cam_width_fov_deg     .setFont(font);
		cam_width_fov_min     .setFont(font);
		cam_height_fov_deg    .setFont(font);
		cam_height_fov_min    .setFont(font);
		cam_pixel_fov_sec     .setFont(font);
		cam_sensor_area_mpx   .setFont(font);

		cam_sensor_width_mm   .setFont(font);
		cam_sensor_height_mm  .setFont(font);
		cam_sensor_width_px   .setFont(font);
		cam_sensor_height_px  .setFont(font);
		cam_pixel_size_um     .setFont(font);
		cam_lens_interface    .setFont(font);
		cam_color             .setFont(font);
		cam_form_factor       .setFont(font);
		cam_max_exposure_sec  .setFont(font);
		cam_min_exposure_sec  .setFont(font);
		cam_bit_depth         .setFont(font);
		cam_well_depth        .setFont(font);
		cam_noise             .setFont(font);
		cam_quantum_eff       .setFont(font);

		img_ra_tf                .setFont(font);
		img_de_tf                .setFont(font);
		img_width_min_tf         .setFont(font);
		img_height_min_tf        .setFont(font);

		cam_form_factor       .setEditable(false);
		cam_pixel_size_um     .setEditable(false);
		cam_width_fov_deg     .setEditable(false);
		cam_width_fov_min     .setEditable(false);
		cam_height_fov_deg    .setEditable(false);
		cam_height_fov_min    .setEditable(false);
		cam_pixel_fov_sec     .setEditable(false);
		cam_sensor_area_mpx   .setEditable(false);

		eye_focal_length_mm      .setHorizontalAlignment(JTextField.RIGHT);
		eye_app_field_of_view_deg.setHorizontalAlignment(JTextField.RIGHT);
		eye_magnification        .setHorizontalAlignment(JTextField.RIGHT);
		eye_field_of_view_deg    .setHorizontalAlignment(JTextField.RIGHT);
		eye_field_of_view_min    .setHorizontalAlignment(JTextField.RIGHT);

		cam_sensor_width_mm   .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_height_mm  .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_width_px   .setHorizontalAlignment(JTextField.RIGHT);
		cam_sensor_height_px  .setHorizontalAlignment(JTextField.RIGHT);
		cam_pixel_size_um     .setHorizontalAlignment(JTextField.RIGHT);
		cam_pixel_fov_sec     .setHorizontalAlignment(JTextField.RIGHT);
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
		cam_width_fov_deg     .setHorizontalAlignment(JTextField.RIGHT);
		cam_width_fov_min     .setHorizontalAlignment(JTextField.RIGHT);
		cam_height_fov_deg    .setHorizontalAlignment(JTextField.RIGHT);
		cam_height_fov_min    .setHorizontalAlignment(JTextField.RIGHT);

		img_ra_tf                .setEditable(false);
		img_de_tf                .setEditable(false);

		img_ra_tf                .setHorizontalAlignment(JTextField.CENTER);
		img_de_tf                .setHorizontalAlignment(JTextField.CENTER);
		img_width_min_tf               .setHorizontalAlignment(JTextField.RIGHT);
		img_height_min_tf                .setHorizontalAlignment(JTextField.RIGHT);

		if (main_frame.selected_dso != null) {
			search_tf.setText(main_frame.selected_dso.names()[0]);
			// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
		}
		fill_in_search_fields(main_frame.selected_dso);

		ota_list      = new String[PersistentState.otas.elts.length];
		for (int i=0; i < PersistentState.otas.elts.length; i++) {
			// System.out.printf("%s: %d: ota[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, main_frame.otas.elts[i]);
			ota_list[i] = PersistentState.otas.elts[i].name.trim();
			if (ota_state < 0 ) {
				if (ota_list[i].replaceAll(" ", "").equalsIgnoreCase("Meade 10\" f/10 ACF".replaceAll(" ", ""))) {
					ota_state = i;
				}
			}
		}
		ota_cb = new JComboBox<String>(ota_list);
		ota_cb.setSelectedIndex(ota_state);
		ota_cb.setFont(font);

		eyepiece_list = new String[PersistentState.eyepieces.elts.length];
		for (int i=0; i < PersistentState.eyepieces.elts.length; i++) {
			eyepiece_list[i] = PersistentState.eyepieces.elts[i].name.trim();
			if (eye_state < 0) {
				if (eyepiece_list[i].equalsIgnoreCase("TeleVue Nagler 31mm")) {
					eye_state = i;
				}
			}
		}
		eyepiece_cb = new JComboBox<String>(eyepiece_list);
		eyepiece_cb.setSelectedIndex(eye_state);
		eyepiece_cb.setFont(font);

		camera_list   = new String[PersistentState.cameras.elts.length];
		for (int i=0; i < PersistentState.cameras.elts.length; i++) {
			camera_list[i] = PersistentState.cameras.elts[i].name.trim();
			if (cam_state < 0) {
				if (camera_list[i].equalsIgnoreCase("ZWO ASI2400MC Pro")) {
					cam_state = i;
				}
			}
		}
		camera_cb = new JComboBox<String>(camera_list);
		camera_cb.setSelectedIndex(cam_state);
		camera_cb.setFont(font);
		
		for (int i=0; i < img_list.length; i++) {
			if (img_state < 0) {
				if (img_list[i].equalsIgnoreCase("Camera")) {
					img_state = i;
				}
			}
		}
		img_cb.setSelectedIndex(img_state);
		img_cb.setFont(font);

		web_cb.setSelectedIndex(web_state);
		web_cb.setFont(font);

		ota_aperture_mm    .setText(String.format("%.0f", PersistentState.otas.elts[ota_state].aperture));
		ota_focal_length_mm.setText(String.format("%.0f", PersistentState.otas.elts[ota_state].focal_length));
		ota_f_ratio        .setText(String.format("%.2f", PersistentState.otas.elts[ota_state].f_ratio));
		ota_reducer        .setText(String.format("%.2f", PersistentState.otas.elts[ota_state].reducer));

		eye_focal_length_mm      .setText(String.format("%.1f", PersistentState.eyepieces.elts[eye_state].focal_length_mm));
		eye_app_field_of_view_deg.setText(String.format("%.1f", PersistentState.eyepieces.elts[eye_state].apparent_field_of_view_deg));

		cam_sensor_width_mm .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].sensor_width_mm));
		cam_sensor_height_mm.setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].sensor_height_mm));
		cam_sensor_width_px .setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].sensor_width_px));
		cam_sensor_height_px.setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].sensor_height_px));
		cam_pixel_size_um   .setText(String.format("%.2f", PersistentState.cameras.elts[cam_state].pixel_size_um));

		cam_lens_interface  .setText(PersistentState.cameras.elts[cam_state].lens_interface);
		cam_color           .setText(PersistentState.cameras.elts[cam_state].color);
		cam_form_factor     .setText(PersistentState.cameras.elts[cam_state].sensor_form);
		cam_max_exposure_sec.setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].max_exposure_sec));
		cam_min_exposure_sec.setText(String.format("%.6f", PersistentState.cameras.elts[cam_state].min_exposure_sec));

		cam_bit_depth       .setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].bit_depth));
		cam_well_depth      .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].well));
		cam_noise           .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].noise));
		cam_quantum_eff     .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].quantum_efficiency));

		calculate_fields();

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
		mag_tf  .setEditable(true);
		size_tf .setEditable(true);

		ra_tf   .setHorizontalAlignment(JTextField.CENTER);
		dec_tf  .setHorizontalAlignment(JTextField.CENTER);
		mag_tf  .setHorizontalAlignment(JTextField.CENTER);
		size_tf .setHorizontalAlignment(JTextField.CENTER);

		image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
		web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));

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


		JPanel dso_panel = new JPanel(new GridBagLayout());
		// search_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int dso_row = 0;

		dso_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		dso_panel.add(dso_search_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 3;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(search_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		dso_panel.add(dso_type_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(type_tf, gbc);


		dso_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		dso_panel.add(dso_found_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(found_tf, gbc);


		dso_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		dso_panel.add(dso_ra_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(ra_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		dso_panel.add(dso_dec_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(dec_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		dso_panel.add(dso_mag_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		dso_panel.add(mag_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		dso_panel.add(dso_dia_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = dso_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		dso_panel.add(size_tf, gbc);

		dso_row += 1;


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(dso_dso_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(nearest, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(dso_panel, gbc);

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


		JPanel ota_panel = new JPanel(new GridBagLayout());
		// ota_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int ota_row = 0;

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


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(ota_ota_jl, gbc);

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

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_focal_len2_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_app_fov_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_mag2_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_fov_deg_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_fov_min_jl, gbc);


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

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_magnification, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		eye_panel.add(eye_field_of_view_deg, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = eye_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		eye_panel.add(eye_field_of_view_min, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(eye_eyepiece_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(eyepiece_cb, gbc);

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
		cam_panel.add(cam_sensor_area_jl, gbc);


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

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_width_fov_deg_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_width_fov_min_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_height_fov_deg_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_height_fov_min_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_arcsec_per_px_jl, gbc);


		cam_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_width_fov_deg, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_width_fov_min, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_height_fov_deg, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_height_fov_min, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = cam_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		cam_panel.add(cam_pixel_fov_sec, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(cam_camera_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(camera_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(cam_panel, gbc);


		row += 1;

		JPanel img_panel = new JPanel(new GridBagLayout());
		// search_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int img_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		img_panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		img_panel.add(new Label(underscore+underscore+underscore+underscore), gbc);


		img_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		img_panel.add(img_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		img_panel.add(img_ra_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		img_panel.add(img_ra_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		img_panel.add(img_de_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		img_panel.add(img_de_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		img_panel.add(img_width_min_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		img_panel.add(img_width_min_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 10, 1, 1);		// top, left, bottom, right
		img_panel.add(height_min_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = img_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		img_panel.add(img_height_min_tf, gbc);

		img_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(image_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(img_panel, gbc);

		row += 1;


		JPanel web_panel = new JPanel(new GridBagLayout());
		// search_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int web_row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = web_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		web_panel.add(new Label(underscore), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = web_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		web_panel.add(new Label(underscore+underscore+underscore+underscore), gbc);

		web_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = web_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		web_panel.add(web_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = web_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		web_panel.add(url_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = web_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 2);		// top, left, bottom, right
		web_panel.add(web_tf, gbc);

		web_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(web_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(web_panel, gbc);


		row += 1;

		JPanel button_panel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(image, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(web, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		button_panel.add(new JLabel("     "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
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
		

		if (true) {
			ota_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					ota_state = ota_cb.getSelectedIndex();
	
					ota_aperture_mm    .setText(String.format("%.0f", PersistentState.otas.elts[ota_state].aperture));
					ota_focal_length_mm.setText(String.format("%.0f", PersistentState.otas.elts[ota_state].focal_length));
					ota_f_ratio        .setText(String.format("%.2f", PersistentState.otas.elts[ota_state].f_ratio));
					ota_reducer        .setText(String.format("%.2f", PersistentState.otas.elts[ota_state].reducer));
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
	
			eyepiece_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					eye_state = eyepiece_cb.getSelectedIndex();
					img_state = IMG_EYEPIECE;
					img_cb.setSelectedIndex(img_state);
	
					eye_focal_length_mm      .setText(String.format("%.1f", PersistentState.eyepieces.elts[eye_state].focal_length_mm));
					eye_app_field_of_view_deg.setText(String.format("%.1f", PersistentState.eyepieces.elts[eye_state].apparent_field_of_view_deg));
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
	
			camera_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					cam_state = camera_cb.getSelectedIndex();
					img_state = IMG_CAMERA;
					img_cb.setSelectedIndex(img_state);
	
					cam_sensor_width_mm .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].sensor_width_mm));
					cam_sensor_height_mm.setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].sensor_height_mm));
					cam_sensor_width_px .setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].sensor_width_px));
					cam_sensor_height_px.setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].sensor_height_px));
					cam_pixel_size_um   .setText(String.format("%.2f", PersistentState.cameras.elts[cam_state].pixel_size_um));
	
					cam_lens_interface  .setText(PersistentState.cameras.elts[cam_state].lens_interface);
					cam_color           .setText(PersistentState.cameras.elts[cam_state].color);
					cam_form_factor     .setText(PersistentState.cameras.elts[cam_state].sensor_form);
					cam_max_exposure_sec.setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].max_exposure_sec));
					cam_min_exposure_sec.setText(String.format("%.6f", PersistentState.cameras.elts[cam_state].min_exposure_sec));
	
					cam_bit_depth       .setText(String.format("%.0f", PersistentState.cameras.elts[cam_state].bit_depth));
					cam_well_depth      .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].well));
					cam_noise           .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].noise));
					cam_quantum_eff     .setText(String.format("%.1f", PersistentState.cameras.elts[cam_state].quantum_efficiency));
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
			
			img_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					img_state = img_cb.getSelectedIndex();
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
	
	
			// TODO
			search_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					Element f = main_frame.dso_alias_table.find(search_tf.getText());
					if (f == null) {
						found_tf.setText("");
					} else {
						main_frame.selected_dso = f;
						try {
							calculate_fields();
						} catch (Exception ex) {
							System.out.println(ex);
							ex.printStackTrace();
						}
					}
				}
			});
			search_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// TODO
					// System.out.printf("%s: %d: name='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());
					String search_text = (search_tf.getText() + (char) evt.getKeyChar()).trim();
					if (main_frame.dso_alias_table != null) fill_in_search_fields(main_frame.dso_alias_table.find(search_text));
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e){}
			});
	
			ra_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					search_tf.setText("Custom");
					found_tf.setText("Custom");
					type_tf.setText("Custom");
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					main_frame.custom_right_ascension = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					main_frame.custom_declination     = PracticalAstronomy.parse_declination(dec_tf.getText());
					image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
					web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
					dec_tf.requestFocus();
				}
			});
			ra_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) {}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e)
				{
					search_tf.setText("Custom");
					found_tf.setText("Custom");
					type_tf.setText("Custom");
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					main_frame.custom_right_ascension = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					main_frame.custom_declination     = PracticalAstronomy.parse_declination(dec_tf.getText());
					image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
					web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
				}
			});
	
			dec_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					search_tf.setText("Custom");
					found_tf.setText("Custom");
					type_tf.setText("Custom");
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					main_frame.custom_right_ascension = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					main_frame.custom_declination     = PracticalAstronomy.parse_declination(dec_tf.getText());
					image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
					web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
					mag_tf.requestFocus();
				}
			});
			dec_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) {}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e)
				{
					search_tf.setText("Custom");
					found_tf.setText("Custom");
					type_tf.setText("Custom");
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					main_frame.custom_right_ascension = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					main_frame.custom_declination     = PracticalAstronomy.parse_declination(dec_tf.getText());
					image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
					web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
				}
			});
	
			mag_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					size_tf.requestFocus();
				}
			});
			mag_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) {}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e)
				{
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
	
			size_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
					ota_cb.requestFocus();
				}
			});
			size_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) {}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e)
				{
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
			
			web_cb.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					try {
						web_state = web_cb.getSelectedIndex();
						Element elt = main_frame.dso_alias_table.find(search_tf.getText());
						web_tf.setText(generate_url(elt, web_state));
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			});
	
			// TODO
			/*
			 * https://en.wikipedia.org/wiki/NGC_891
			 * https://en.wikipedia.org/wiki/IC_1101
			 * https://en.wikipedia.org/wiki/UGC_2885
			 * https://en.wikipedia.org/wiki/Sh_2-155
			 * 
			 * https://grokipedia.com/page/ngc_4556
			 * https://grokipedia.com/page/IC_1101
			 * https://grokipedia.com/page/ugc_4881
			 * https://grokipedia.com/page/sh_2_155
			 * 
			 * https://duckduckgo.com/?q=sh-2+155
			 * https://duckduckgo.com/?q=ic+1101
			 * https://duckduckgo.com/?q=ugc+2885
			 * https://duckduckgo.com/?q=ngc+891
			 * 
			 * https://www.google.com/search?q=IC+1101
			 * https://www.google.com/search?q=sh-2+155
			 * https://www.google.com/search?q=NGC+4889
			 * https://www.google.com/search?q=UGC+2885
			 */
			web_tf.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					Element f = (main_frame.dso_alias_table == null) ? null : main_frame.dso_alias_table.find(search_tf.getText());
					if (f == null) {
						found_tf.setText("");
					} else {
						main_frame.selected_dso = f;
						try {
							calculate_fields();
						} catch (Exception ex) {
							System.out.println(ex);
							ex.printStackTrace();
						}
					}
				}
			});
			web_tf.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent evt) 
				{
					// TODO
					String search_text = (search_tf.getText() + (char) evt.getKeyChar()).trim();
					if (main_frame.dso_alias_table != null) fill_in_search_fields(main_frame.dso_alias_table.find(search_text));
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
				@Override public void keyPressed(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e){}
			});
	
			web.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(web_tf.getText()));
							dispose();
						} catch (IOException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
	
					main_frame.selected_dso = (main_frame.dso_alias_table == null) ? null : main_frame.dso_alias_table.find(search_tf.getText());
					main_frame.custom_right_ascension = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					main_frame.custom_declination     = PracticalAstronomy.parse_declination(dec_tf.getText());
				}
			});
	
			image.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					// TODO
					int width_amin  = (int) (Double.parseDouble(img_width_min_tf.getText().trim()));
					int height_amin = (int) (Double.parseDouble(img_height_min_tf.getText().trim()));
					// create_image(frame.selected_dso, found_tf.getText(), img_ra_tf.getText().trim(), img_de_tf.getText().trim(), width_amin, height_amin);
					if (0 < width_amin && width_amin <= ImageFrame.max_width_amin && 0 < height_amin && height_amin <= ImageFrame.max_height_amin) {
						dispose();
						create_image(main_frame.selected_dso, width_amin, height_amin);
					} else {
						String msg = String.format("Your requested image size is %d' x %d'.%n", width_amin, height_amin) + 
								"Images must be smaller than 2"+PracticalAstronomy.degree_symbol+" x 2"+PracticalAstronomy.degree_symbol+" (120' x 120').";
						JOptionPane.showMessageDialog(null, msg, "Image Size Error", JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
	
			nearest.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					double ra = PracticalAstronomy.parse_right_ascension(ra_tf.getText());
					double de = PracticalAstronomy.parse_declination(dec_tf.getText());
					if (main_frame.dso_alias_table != null) {
						main_frame.selected_dso = main_frame.dso_alias_table.find_nearest(ra, de);
						search_tf.setText(main_frame.selected_dso.names()[0]);
						fill_in_search_fields(main_frame.selected_dso);
					}
				}
			});
	
			close.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent evt) 
				{
					ota_state = ota_cb.getSelectedIndex();
					eye_state = eyepiece_cb.getSelectedIndex();
					cam_state = camera_cb.getSelectedIndex();
					img_state = img_cb.getSelectedIndex();
					web_state = web_cb.getSelectedIndex();
	
			    	dispose();
				}
			});
	
			ActionListener calculate = new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) 
				{
					try {
						calculate_fields();
					} catch (Exception ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
			};
	
			ota_focal_length_mm.addActionListener(calculate);
			ota_reducer.addActionListener(calculate);
			eye_focal_length_mm.addActionListener(calculate);
			eye_app_field_of_view_deg.addActionListener(calculate);
			cam_sensor_width_mm.addActionListener(calculate);
		}

		return panel;
	}


	public void fill_in_search_fields(Element elt)
	{
		main_frame.selected_dso = elt;
		// System.out.printf("%s: %d: search='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), search_tf.getText());

		if (elt == null) {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), "null");
			found_tf.setText("");
			type_tf .setText("");
			ra_tf   .setText("");
			dec_tf  .setText("");
			mag_tf  .setText("");
			size_tf .setText("");
			web_tf  .setText("");
			img_ra_tf.setText("");
			img_de_tf.setText("");
		} else {
			// System.out.printf("%s: %d: elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(3), frame.dso_alias_table.name(elt));
			found_tf.setText(main_frame.dso_alias_table.name(elt));
			type_tf .setText(elt.type());
			ra_tf   .setText(PracticalAstronomy.decimal_hours_to_str_hms(elt.ra_hrs()));
			dec_tf  .setText(PracticalAstronomy.decimal_degrees_to_str_dms(elt.de_deg()));
			img_ra_tf.setText(ra_tf.getText());
			img_de_tf.setText(dec_tf.getText());
			if (elt.max_mag() != 9999) {
				if (elt.max_mag() == elt.min_mag()) {
					mag_tf  .setText(String.format("%.1f", elt.max_mag()));
				} else {
					mag_tf  .setText(String.format("%.1f/%.1f", elt.max_mag(), elt.min_mag()));
				}
			} else {
				mag_tf  .setText("");
			}
			size_tf .setText(String.format("%.1f", elt.size_min()));
			web_state = web_cb.getSelectedIndex();
			web_tf.setText(generate_url(elt, web_state));
		}

		image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
		web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
	}

	
	private String generate_url(Element elt, int web_idx)
	{
		String url = null;
		String dso_name = null;

		if (elt != null) {
			dso_name = elt.names()[0];
		}

		if (dso_name != null && ! dso_name.equalsIgnoreCase("Custom")) {
			if (web_idx == WEB_WIKIPEDIA) {
				dso_name = dso_name.replaceAll(" ", "_");
				url = String.format("https://en.wikipedia.org/wiki/%s", dso_name);
			} else if (web_idx == WEB_GROKIPEDIA) {
				dso_name = dso_name.replaceAll(" ", "_");
				url = String.format("https://grokipedia.com/page/%s", dso_name);
			} else if (web_idx == WEB_DUCKDUCKGO) {
				dso_name = dso_name.replaceAll(" ", "+");
				url = String.format("https://duckduckgo.com/?q=%s", dso_name);
			} else if (web_idx == WEB_GOOGLE) {
				dso_name = dso_name.replaceAll(" ", "+");
				url = String.format("https://www.google.com/search?q=%s", dso_name);
			}
		}
		
		return url;
	}

	private void calculate_fields()
	{
		double ota_aperture      = Double.parseDouble(ota_aperture_mm.getText());
		double ota_focal_length  = Double.parseDouble(ota_focal_length_mm.getText());
		double ota_f_r           = ota_focal_length / ota_aperture;
		ota_f_ratio.setText(String.format("%.2f", ota_f_r));

		double red_mag           = Double.parseDouble(ota_reducer.getText());

		double eff_aperture      = ota_aperture;
		double eff_focal_length  = red_mag * ota_focal_length;
		double eff_f_r           = eff_focal_length / eff_aperture;

		eff_aperture_mm    .setText(String.format("%.0f", eff_aperture));
		eff_focal_length_mm.setText(String.format("%.0f", eff_focal_length));
		eff_f_ratio        .setText(String.format("%.2f", eff_f_r));

		double eye_focal_length  = Double.parseDouble(eye_focal_length_mm.getText());
		double eye_app_fov       = Double.parseDouble(eye_app_field_of_view_deg.getText());
		double sensor_width_mm   = Double.parseDouble(cam_sensor_width_mm.getText());
		double sensor_width_px   = Double.parseDouble(cam_sensor_width_px.getText());
		double sensor_height_mm  = Double.parseDouble(cam_sensor_height_mm.getText());
		double sensor_height_px  = Double.parseDouble(cam_sensor_height_px.getText());
		double sensor_area_mpx   = sensor_width_px * sensor_height_px / 1e6;

		double eye_mag           = ota_focal_length * red_mag / eye_focal_length;
		double eye_fov_deg       = eye_app_fov / eye_mag;
		double eye_fov_min       = eye_fov_deg * 60;

		eye_magnification    .setText(String.format("%.1f", eye_mag));
		eye_field_of_view_deg.setText(String.format("%.4f", eye_fov_deg));
		eye_field_of_view_min.setText(String.format("%.2f", eye_fov_min));

		double cam_wid_fov_deg   = 2 * Math.atan2(sensor_width_mm / 2, eff_focal_length) * 180 / Math.PI;
		double cam_wid_fov_min   = cam_wid_fov_deg * 60;
		double cam_ht_fov_deg    = 2 * Math.atan2(sensor_height_mm / 2, eff_focal_length) * 180 / Math.PI;
		double cam_ht_fov_min    = cam_ht_fov_deg * 60;
		double pix_fov_sec       = cam_wid_fov_min / sensor_width_px;

		cam_width_fov_deg  .setText(String.format("%.4f", cam_wid_fov_deg));
		cam_width_fov_min  .setText(String.format("%.2f", cam_wid_fov_min));
		cam_height_fov_deg .setText(String.format("%.4f", cam_ht_fov_deg));
		cam_height_fov_min .setText(String.format("%.2f", cam_ht_fov_min));
		cam_pixel_fov_sec  .setText(String.format("%.4f", pix_fov_sec));
		cam_sensor_area_mpx.setText(String.format("%.1f", sensor_area_mpx));

		int img_idx = img_cb.getSelectedIndex();
		if (img_list[img_idx].equalsIgnoreCase("Camera")) {
			img_ra_tf.setText(ra_tf.getText());
			img_de_tf.setText(dec_tf.getText());
			img_width_min_tf.setText(cam_width_fov_min.getText());
			img_height_min_tf.setText(cam_height_fov_min.getText());
		} else if (img_list[img_idx].equalsIgnoreCase("DSO")) {
			img_ra_tf.setText(ra_tf.getText());
			img_de_tf.setText(dec_tf.getText());
			img_width_min_tf.setText(size_tf.getText());
			img_height_min_tf.setText(size_tf.getText());
		} else if (img_list[img_idx].equalsIgnoreCase("Eyepiece")) {
			img_ra_tf.setText(ra_tf.getText());
			img_de_tf.setText(dec_tf.getText());
			img_width_min_tf.setText(eye_field_of_view_min.getText());
			img_height_min_tf.setText(eye_field_of_view_min.getText());
		} else if (img_list[img_idx].equalsIgnoreCase("Custom")) {
			img_ra_tf.setText(ra_tf.getText());
			img_de_tf.setText(dec_tf.getText());
		}

		image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
		web.setEnabled(web_tf != null && ! web_tf.getText().equals(""));
	}

	// TODO
	private void create_image(Element elt, double wamin, double hamin)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen_size = toolkit.getScreenSize();
		int w_max_px = (int) (3 * screen_size.width  / 4);
		int h_max_px = (int) (3 * screen_size.height / 4);
		int w_vw_px = w_max_px;
		int h_vw_px = (int) (hamin * w_vw_px / wamin);

		int h_vh_px = h_max_px;
		int w_vh_px = (int) (wamin * h_vh_px / hamin);

		int w_px;
		int h_px;
		if (h_max_px < h_vw_px) {
			w_px = w_vh_px;
			h_px = h_vh_px;
		} else {
			w_px = w_vw_px;
			h_px = h_vw_px;
		}

		// System.out.printf("%s: %4d: creating image frame... elt=%s w %f, h %f%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt.toString(), wamin, hamin);

		ImageFrame imf = new ImageFrame(main_frame, elt, wamin, hamin, w_px, h_px);
	    imf.addMouseListener(imf);
	    imf.addMouseMotionListener(imf);
	    imf.addWindowListener(imf);
	    imf.addComponentListener(imf);
	    imf.requestFocus();
	    imf.setResizable(false);
		imf.pack();
		imf.setVisible(true);
	}

	@SuppressWarnings("unused")
	private void create_image_obsolete(Element elt, String title, String ra, String de, double wamin, double hamin)
	{
		image.setEnabled(false);
		try {
			if (elt.names()[0].equalsIgnoreCase("Custom")) {
				title = String.format("RA %s, DE %s, %.0f' x %.0f'", ra, de, wamin, hamin);
			} else if (title == null || title.equals(""))  {
				title = String.format("%s RA %s, DE %s, %.0f' x %.0f'", elt.names()[0], ra, de, wamin, hamin);
			}
			double ra_dhrs = PracticalAstronomy.parse_right_ascension(ra);
			double de_ddeg = PracticalAstronomy.parse_declination(de);

			ra = ra.replaceAll("[hms\"']", "").replaceAll("[ ]", "+");
			de = de.replaceAll("[dms"+PracticalAstronomy.degree_symbol+"\"']", "").replaceAll("[ ]", "+");
			wamin  = wamin + 1 - 1e-12;
			hamin  = hamin + 1 - 1e-12;
			String uri_str = String.format("https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%s&d=%s&e=J2000&w=%.0f&h=%.0f&f=gif", ra, de, wamin, hamin);

			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screen_size = toolkit.getScreenSize();
			int sw = 2 * screen_size.width  / 3;
			int sh = 2 * screen_size.height / 3;

			int w0 = sw;
			int h0 = (int) (sw * hamin / wamin);
			int w1 = (int) (sh * wamin / hamin);
			int h1 = sh;
			int width, height;
			if (w0 <= sw && h0 <= sh) {
				width  = w0;
				height = h0;
			} else {
				width  = w1;
				height = h1;
			}

			if (120 <= wamin || 120 <= hamin) {
				String msg = String.format("Your requested image size is %.3f' x %.3f'.%n") + 
						"Images must be smaller than 2"+PracticalAstronomy.degree_symbol+" x 2"+PracticalAstronomy.degree_symbol+" (120' x 120').";
				JOptionPane.showMessageDialog(main_frame, msg, "Image Size Error", JOptionPane.PLAIN_MESSAGE);
				
				// rescale the image to fit within 120' x 120'
				double w,h;
				if (hamin < wamin) {
					w = 120;
					h = w * (hamin / wamin);
				} else {
					h = 120;
					w = h * (wamin / hamin);
				}
				wamin = w;
				hamin = h;
			}

			if (0 < wamin && wamin <= 120 && 0 < hamin && hamin <= 120) {
				URI uri = new URI(uri_str);
				URL url = uri.toURL();
		        InputStream is = url.openStream(); 
				byte[] ch = is.readAllBytes();
		        is.close();

			    ImageIcon icon = new ImageIcon(ch);
			    JLabel label = new JLabel(icon);
				icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
			 
			    JFrame f = new JFrame(title);
			    f.getContentPane().add(label);
			    ImageMouseListener iml = new ImageMouseListener(f, ra_dhrs, de_ddeg, wamin, hamin, width, height, main_frame);
			    f.addMouseListener(iml);
			    f.addMouseMotionListener(iml);
	
			    f.pack();
			    f.setLocationRelativeTo(null);
			    f.setVisible(true);
			} else {
				String msg = 
						String.format("Something is wrong with our requested image size of %.3f' x %.3f'.%n") + 
						"Images must be greater than zeron and smaller than 2"+PracticalAstronomy.degree_symbol+" x 2"+PracticalAstronomy.degree_symbol+" (120' x 120').";
				JOptionPane.showMessageDialog(main_frame, msg, "Image Size Error", JOptionPane.PLAIN_MESSAGE);
			}
		} catch (URISyntaxException | IOException e) {
			JOptionPane.showMessageDialog(main_frame, e.getMessage(), "Network Problem", JOptionPane.PLAIN_MESSAGE);
			// e.printStackTrace();
		}
		image.setEnabled(! ra_tf.getText().trim().equals("") && ! dec_tf.getText().trim().equals("") && ! img_width_min_tf.getText().trim().equals("") && ! img_height_min_tf.getText().trim().equals(""));
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
		ota_state = ota_cb.getSelectedIndex();
		eye_state = eyepiece_cb.getSelectedIndex();
		cam_state = camera_cb.getSelectedIndex();
		img_state = img_cb.getSelectedIndex();
		web_state = web_cb.getSelectedIndex();

		dispose();
	}

	@Override public void windowActivated(WindowEvent arg0)   {}
	@Override public void windowClosed(WindowEvent arg0)      {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0)   {}
	@Override public void windowOpened(WindowEvent arg0)      {}
}
