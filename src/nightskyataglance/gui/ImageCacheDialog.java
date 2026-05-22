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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nightskyataglance.util.PersistentState;

public class ImageCacheDialog extends Dialog implements WindowListener, KeyListener {
	private static final long serialVersionUID = -6351387267852458181L;

	public final MainFrame frame;
	public final ImageCacheDialog dialog;

	public static final JCheckBox  cache_cb      = new JCheckBox();
	public static final JLabel     cache_jl      = new JLabel("Cache Images Locally");
	public static final JLabel     cache_path_jl = new JLabel("Cache Path");
	public static final JTextField cache_path_tf = new JTextField("", 56);
	public static final Button     browse_b      = new Button("Browse");
	public static final Button     save_b        = new Button("  Save  ");
	public static final Button     cancel_b      = new Button("Cancel");

	public ImageCacheDialog(MainFrame f, String name)
	{
		super(f, name, true);

		frame    = f;
		dialog   = this;

		cache_cb.setSelected(PersistentState.cache_images);
		cache_path_tf.setText(PersistentState.cache_path);

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
		cache_path_tf.setEditable(true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;

		int row = 0;

		JPanel box_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		box_panel.add(cache_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		box_panel.add(cache_cb, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		panel.add(box_panel, gbc);


		row += 1;

		JPanel file_panel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		file_panel.add(cache_path_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		file_panel.add(cache_path_tf, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = 0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		file_panel.add(browse_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(file_panel, gbc);


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
		button_panel.add(cancel_b, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(button_panel, gbc);

		// TODO write the new cache_path to cache_file
		cache_cb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e)
			{
			}
		});

		browse_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				JFileChooser j = new JFileChooser(PersistentState.cache_path);
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (j.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File cache_dir = j.getSelectedFile();
					try {
						PersistentState.cache_path = cache_dir.getCanonicalPath();
						cache_path_tf.setText(PersistentState.cache_path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// the "save" button does the following:
		// 1. retrieves the cache state (cache or don't)
		// 2. verifies the cache path exists
		// 3. creates the cache directory path if it doesn't already exist
		// 4. saves the cache state (cache or don't) and the cache path to a state file
		save_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				PersistentState.cache_images = cache_cb.isSelected();
				// System.out.printf("248: cache='%s' path='%s'%n", cache_images, cache_path);
				try {
					File cache_dir = new File(PersistentState.cache_path);
					PersistentState.cache_path = cache_dir.getCanonicalPath();
					cache_path_tf.setText(PersistentState.cache_path);
					if (! cache_dir.isDirectory()) {
						cache_dir.mkdirs();
					}
					PersistentState.cache_images = PersistentState.cache_images && cache_dir.isDirectory();
					PersistentState.save_cache_path();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});

		cancel_b.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
