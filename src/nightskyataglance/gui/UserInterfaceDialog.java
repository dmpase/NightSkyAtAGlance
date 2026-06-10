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


import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class UserInterfaceDialog extends Dialog implements WindowListener, KeyListener {
	private static final long serialVersionUID = -6351387267852458181L;

	public final MainFrame main_frame;
	public final UserInterfaceDialog dialog;

	public static Font font = null;
	public static Font reset_font = null;

	public static JLabel ui_jl           = new JLabel("IU Manager");
	public static JLabel font_jl         = new JLabel("Font Family");
	public static JLabel size_jl         = new JLabel("Size (pt)");
	public static JLabel height_jl       = new JLabel("Height (px)");
	public static JLabel example_jl      = new JLabel("Example");
	public static JLabel chart_margin_jl = new JLabel("Chart Margin (px)");

	public static JTextField example_tf = new JTextField(25);

	public static JComboBox<String> ui_cb     = null;
	public static JComboBox<String> font_cb   = null;
	public static JTextField        size_tf   = new JTextField(4);
	public static JTextField        height_tf = new JTextField(5);
	public static JTextField        chart_margin_tf = new JTextField("0", 9);

	public static final JButton     save_b    = new JButton("  Save  ");
	public static final JButton     cancel_b  = new JButton("Cancel");
	public static final JButton     reset_b   = new JButton(" Reset ");

	public static boolean saved  = false;
	public static String  ui_mgr = null;
	public static String  family = null;
	public static int     size   = -1;

	public static JLabel     menu_height_jl = new JLabel("Menu Height (px)");
	public static JLabel     current_jl     = new JLabel("Current");
	public static JTextField current_tf     = new JTextField(4);
	public static JLabel     suggested_jl   = new JLabel("Suggested");
	public static JTextField suggested_tf   = new JTextField(4);
	public static JLabel     new_jl         = new JLabel("New");
	public static JTextField new_tf         = new JTextField(4);

	public static JLabel     dso_icon_size_jl = new JLabel("DSO Icon Size Factor");
	public static JTextField dso_icon_scale_factor_tf = new JTextField("1.000", 9);
	
	private void set_font(Font font)
	{
		setFont(font);

		ui_jl.setFont(font);
		font_jl.setFont(font);
		size_jl.setFont(font);
		height_jl.setFont(font);
		example_jl.setFont(font);
		example_tf.setFont(font);
		ui_cb.setFont(font);
		font_cb.setFont(font);
		size_tf.setFont(font);
		height_tf.setFont(font);
		save_b.setFont(font);
		cancel_b.setFont(font);
		reset_b.setFont(font);
		menu_height_jl.setFont(font);
		current_jl.setFont(font);
		current_tf.setFont(font);
		suggested_jl.setFont(font);
		suggested_tf.setFont(font);
		new_jl.setFont(font);
		new_tf.setFont(font);
		chart_margin_jl.setFont(font);
		chart_margin_tf.setFont(font);
		dso_icon_size_jl.setFont(font);
		dso_icon_scale_factor_tf.setFont(font);
	}
	
	private int find_font_idx(Font font, String[] list)
	{
		int idx = 0;

		if (font != null && list != null) {
			String current_font = font.getFamily();
	    	// System.out.printf("%s: %3d: current font='%s'...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), current_font);
			for (int i=0; i < list.length; i++) {
				if (current_font.equalsIgnoreCase(list[i])) {
					idx = i;
					break;
				}
			}
		}

		return idx;
	}

	public UserInterfaceDialog(MainFrame f, String name)
	{
		super(f, name, true);

		// System.out.printf("%s: %3d: new UserInterfaceDialog%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());

    	main_frame = f;
		dialog     = this;

		// print_font();

		font   = reset_font = PersistentState.get_font();
		family = font.getFamily();
		size   = font.getSize();
    	height_tf.setHorizontalAlignment(JTextField.RIGHT);
    	height_tf.setEditable(false);
    	if (main_frame.graphics != null) {
			FontMetrics fm = main_frame.graphics.getFontMetrics(main_frame.getFont());
			height_tf.setText(String.format("%d", fm.getHeight()));
    	}

    	example_tf.setText(family+" "+" "+size);
    	example_tf.setFont(PersistentState.font);
    	example_tf.setEditable(false);
    	current_tf.setText(String.format("%d", MainFrame.menu_height));
    	current_tf.setHorizontalAlignment(JTextField.RIGHT);
    	current_tf.setEditable(false);
    	suggested_tf.setHorizontalAlignment(JTextField.RIGHT);
    	suggested_tf.setEditable(false);
    	if (main_frame.graphics != null) {
			FontMetrics fm = main_frame.graphics.getFontMetrics(main_frame.getFont());
			suggested_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
			new_tf.setText(String.format("%d", MainFrame.menu_height));
    	}
    	new_tf.setHorizontalAlignment(JTextField.RIGHT);
    	chart_margin_tf.setText(String.format("%d", PersistentState.chart_margin));
    	chart_margin_tf.setHorizontalAlignment(JTextField.RIGHT);
    	dso_icon_scale_factor_tf.setText(String.format("%5.3f", PersistentState.dso_icon_scale_factor));
    	dso_icon_scale_factor_tf.setHorizontalAlignment(JTextField.RIGHT);

		PersistentState.find_ui_mgrs();
		ui_cb = new JComboBox<String>(PersistentState.ui_mgr_list);
		// TODO ui_cb.setFont(PersistentState.get_combobox_font());
		LookAndFeel laf = UIManager.getLookAndFeel();
		String laf_name = laf.getID();
    	// System.out.printf("%s: %3d: laf='%s'...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), laf_name);
		for (int i=0; i < PersistentState.ui_mgr_list.length; i++) {
			if (laf_name.equalsIgnoreCase(PersistentState.ui_mgr_list[i])) {
				ui_cb.setSelectedIndex(i);
				// System.out.printf("%s: %3d: ui '%s' (%d)\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.ui_mgr_list[i], i);
				break;
			}
		}

		PersistentState.find_fonts();
    	// System.out.printf("%s: %3d: dialog font='%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dialog.getFont());
		font_cb = new JComboBox<String>(PersistentState.font_list);
		font_cb.setSelectedIndex(find_font_idx(font, PersistentState.font_list));

		size = dialog.getFont().getSize();
		size_tf.setText(String.format("%d", size));
		size_tf.setHorizontalAlignment(JTextField.RIGHT);

		set_font(PersistentState.get_font());

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

	private JPanel set_pos_dms()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;

		int row = 0;

		JPanel box_panel = new JPanel(new GridBagLayout());
		int box_row = 0;
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(ui_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(font_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(size_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(height_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(example_jl, gbc);

		box_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(ui_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(font_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(size_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(height_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(example_tf, gbc);

		box_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(menu_height_jl, gbc);

		JPanel height_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		height_panel.add(current_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		height_panel.add(current_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 6, 1, 5);		// top, left, bottom, right
		height_panel.add(suggested_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		height_panel.add(suggested_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 6, 1, 5);		// top, left, bottom, right
		height_panel.add(new_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		height_panel.add(new_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(height_panel, gbc);

		box_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(chart_margin_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(chart_margin_tf, gbc);

		box_row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(dso_icon_size_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = box_row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(dso_icon_scale_factor_tf, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		panel.add(box_panel, gbc);

		row += 1;

		JPanel button_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		button_panel.add(save_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
//		button_panel.add(reset_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 1, 0);		// top, left, bottom, right
		button_panel.add(cancel_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(button_panel, gbc);

		// TODO write the new cache_path to cache_file
		ui_cb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED) {
			    	ui_mgr = (String) e.getItem();
			    	// System.out.printf("%s: %3d: ui='%s'...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ui_mgr);
				}
			}
		});

		font_cb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED) {
			    	family = (String) e.getItem();
					font = new Font(family, Font.PLAIN, size);
			    	example_tf.setFont(font);
			    	example_tf.setText(family+" "+" "+size);
			    	// System.out.printf("%s: %3d: font='%s'...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), family);
			    	if (main_frame.graphics != null) {
						FontMetrics fm = main_frame.graphics.getFontMetrics(font);
						suggested_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
						height_tf.setText(String.format("%d", fm.getHeight()));
						new_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
			    	}
				}
			}
		});

		size_tf.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt)    {}
			@Override public void keyPressed (KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt) 
			{
				String cmd = size_tf.getText();
				if (cmd.matches("[0-9][0-9]*") && main_frame.graphics != null) {
					size = Integer.parseInt(cmd);
					font = new Font(family, Font.PLAIN, size);
			    	example_tf.setFont(font);
			    	example_tf.setText(family+" "+" "+size);
			    	if (main_frame.graphics != null) {
						FontMetrics fm = main_frame.graphics.getFontMetrics(font);
						suggested_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
						height_tf.setText(String.format("%d", fm.getHeight()));
						new_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
			    	}
				}
				// System.out.printf("%s: %3d: size='%s' cmd='%s' evt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), size_tf.getText(), cmd, evt);
			}
		});
		size_tf.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				String cmd = e.getActionCommand();
				if (cmd.matches("[0-9][0-9]*") && main_frame.graphics != null) {
					size = Integer.parseInt(cmd);
					font = new Font(family, Font.PLAIN, size);
			    	example_tf.setFont(font);
			    	example_tf.setText(family+" "+" "+size);
			    	// System.out.printf("%s: %3d: font='%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), family);
			    	if (main_frame.graphics != null) {
						FontMetrics fm = main_frame.graphics.getFontMetrics(font);
						suggested_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
						height_tf.setText(String.format("%d", fm.getHeight()));
						new_tf.setText(String.format("%d", MainFrame.get_menu_height(fm)));
			    	}
				}
			}
		});

		save_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				if (new_tf.getText().matches("[1-9][0-9]*")) {
					PersistentState.menu_height = Integer.parseInt(new_tf.getText());
				}
				if (size_tf.getText().matches("[1-9][0-9]*")) {
					size = Integer.parseInt(size_tf.getText());
			    	// System.out.printf("%s: %3d: set font size to %d px%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), size);
					font = new Font(family, Font.PLAIN, size);
				}
				if (chart_margin_tf.getText().matches("[0-9][0-9]*") || chart_margin_tf.getText().matches("-[0-9][0-9]*")) {
					PersistentState.chart_margin = Integer.parseInt(chart_margin_tf.getText());
				}
				if (dso_icon_scale_factor_tf.getText().matches("[1-9][0-9]*") || dso_icon_scale_factor_tf.getText().matches("[0-9][0-9]*[.][0-9]*")) {
					PersistentState.dso_icon_scale_factor = Double.parseDouble(dso_icon_scale_factor_tf.getText());
				}

				PersistentState.set_font(font);
				main_frame.set_menu_font(font);
				main_frame.setFont(PersistentState.get_font());

				if (ui_mgr != null) {
					try {
						LookAndFeel new_laf = UIManager.createLookAndFeel(ui_mgr);
						UIManager.setLookAndFeel(new_laf);
						PersistentState.set_ui_mgr(new_laf);
					} catch (UnsupportedLookAndFeelException e1) {
				    	System.out.printf("%s: %3d: unsupported LookAndFeel '%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ui_mgr);
						e1.printStackTrace();
					}
				}
				saved = true;
				dispose();
			}
		});

		reset_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				font = reset_font;
				font_cb.setSelectedIndex(find_font_idx(font, PersistentState.font_list));
				set_font(font);
			}
		});

		cancel_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				saved = false;
				dispose();
			}
		});

		return panel;
	}

	public void print_font()
	{
    	System.out.printf("%s: %3d: font......................'%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_font());
    	System.out.printf("%s: %3d: Main font.................'%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_main_font());
    	System.out.printf("%s: %3d: Menu Bar font.............'%s'\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_menu_font());
    	System.out.printf("%s: %3d: Dialog JButton font.......'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_button_font());
    	System.out.printf("%s: %3d: Dialog JCheckBox font.....'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_checkbox_font());
    	System.out.printf("%s: %3d: Dialog JComboBox..........'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_combobox_font());
    	System.out.printf("%s: %3d: Dialog JLabel font........'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_label_font());
    	System.out.printf("%s: %3d: Dialog JRadioButton font..'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_radiobutton_font());
    	System.out.printf("%s: %3d: Dialog JTextField font....'%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PersistentState.get_textfield_font());
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

	@Override public void keyTyped(KeyEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override public void keyPressed(KeyEvent e) 
	{
		// TODO Auto-generated method stub
	}

	@Override public void keyReleased(KeyEvent e) 
	{
		// TODO Auto-generated method stub
	}
}
