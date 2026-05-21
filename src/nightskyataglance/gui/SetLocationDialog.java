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


import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class SetLocationDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2094592825327755320L;

	private int digits = 10;
	private JRadioButton north = new JRadioButton();
	private JRadioButton south = new JRadioButton();
	private JTextField lat_deg = new JTextField("", digits);
	private JTextField lat_min = new JTextField("", digits);
	private JTextField lat_sec = new JTextField("", digits);
	private JRadioButton east  = new JRadioButton();
	private JRadioButton west  = new JRadioButton();
	private JTextField lon_deg = new JTextField("", digits);
	private JTextField lon_min = new JTextField("", digits);
	private JTextField lon_sec = new JTextField("", digits);

	private JButton save       = new JButton("Save");
	private JButton cancel     = new JButton("Cancel");

	public final MainFrame frame;

	public SetLocationDialog(MainFrame f, String name)
	{
		super(f, name, true);
		
		frame = f;

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
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel("Degrees"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel("Minutes"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel("Seconds"), gbc);

		
		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel("Latitude"), gbc);

		lat_deg.requestFocus();
		lat_deg.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(lat_deg, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String(Character.toChars(0x00B0))), gbc);

		lat_min.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(lat_min, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String((Character.toChars(0x0027)))), gbc);

		lat_sec.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(lat_sec, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String((Character.toChars(0x0022)))), gbc);

		ButtonGroup nsbg = new ButtonGroup();
		north.setSelected(true);
		nsbg.add(north);
		nsbg.add(south);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(north, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel("N"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(south, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel("S"), gbc);

		
		row += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel("Longtitude"), gbc);

		lon_deg.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(lon_deg, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String((Character.toChars(0x00B0)))), gbc);

		lon_min.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(lon_min, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String((Character.toChars(0x0027)))), gbc);

		lon_sec.setHorizontalAlignment(JTextField.RIGHT);
		gbc = new GridBagConstraints();
		gbc.gridx     = 5;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(lon_sec, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel(new String((Character.toChars(0x0022)))), gbc);

		ButtonGroup ewbg = new ButtonGroup();
		west.setSelected(true);
		ewbg.add(east);
		ewbg.add(west);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 7;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(east, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 8;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel("E"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 9;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(west, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 10;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(new JLabel("W"), gbc);

		
		row += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(cancel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 6;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 5;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(save, gbc);
		
		
		lat_deg.setFocusTraversalKeysEnabled(false);
		lat_deg.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER lat_deg->lat_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				lat_min.requestFocus();
			}
		});
		lat_deg.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lat_deg->lat_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_min.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		lat_min.setFocusTraversalKeysEnabled(false);
		lat_min.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER lat_min->lat_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				lat_sec.requestFocus();
			}
		});
		lat_min.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lat_min->lat_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_sec.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x lat_min->lat_deg...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_deg.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		lat_sec.setFocusTraversalKeysEnabled(false);
		lat_sec.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER lat_sec->north...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				north.requestFocus();
			}
		});
		lat_sec.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lat_sec->north...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					north.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x lat_sec->lat_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_min.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		
		north.setFocusTraversalKeysEnabled(false);
		north.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
		    	System.out.printf("%s: %3d: ENTER north->south...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				south.requestFocus();
			}
		});
		north.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x north->south...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					south.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x north->lat_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_sec.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt){}
		});
		
		south.setFocusTraversalKeysEnabled(false);
		south.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
		    	System.out.printf("%s: %3d: ENTER south->lon_deg...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				lon_deg.requestFocus();
			}
		});
		south.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x south->lon_deg...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_deg.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x south->north...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					north.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		
		lon_deg.setFocusTraversalKeysEnabled(false);
		lon_deg.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
		    	System.out.printf("%s: %3d: ENTER lon_deg->lon_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				lon_min.requestFocus();
			}
		});
		lon_deg.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lon_deg->lon_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_min.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x lat_deg->south...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					south.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		lon_min.setFocusTraversalKeysEnabled(false);
		lon_min.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
		    	System.out.printf("%s: %3d: ENTER lon_min->lon_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				lon_sec.requestFocus();
			}
		});
		lon_min.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lon_min->lon_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_sec.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x lon_min->lon_deg...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_deg.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		lon_sec.setFocusTraversalKeysEnabled(false);
		lon_sec.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
		    	System.out.printf("%s: %3d: ENTER lon_sec->east...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
		    	east.requestFocus();
			}
		});
		lon_sec.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x lon_sec->east...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					east.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x lon_sec->lon_min...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_min.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		
		
		east.setFocusTraversalKeysEnabled(false);
		east.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER east->west...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				west.requestFocus();
			}
		});
		east.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	System.out.printf("%s: %3d: %x %x...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x east->west...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					west.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x east->lon_sec...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lon_sec.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt){}
		});

		west.setFocusTraversalKeysEnabled(false);
		west.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER west->save...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
				save.requestFocus();
			}
		});
		west.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x west->save...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					save.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x west->east...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					east.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt){}
		});


		save.setFocusTraversalKeysEnabled(false);
		save.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
		    	// TODO 
		    	try {
			    	double latd = (lat_deg.getText() == null || lat_deg.getText().trim().equals("")) ? 0 : Double.parseDouble(lat_deg.getText().trim());
			    	double latm = (lat_min.getText() == null || lat_min.getText().trim().equals("")) ? 0 : Double.parseDouble(lat_min.getText().trim());
			    	double lats = (lat_sec.getText() == null || lat_sec.getText().trim().equals("")) ? 0 : Double.parseDouble(lat_sec.getText().trim());
			    	
			    	double lond = (lon_deg.getText() == null || lon_deg.getText().trim().equals("")) ? 0 : Double.parseDouble(lon_deg.getText().trim());
			    	double lonm = (lon_min.getText() == null || lon_min.getText().trim().equals("")) ? 0 : Double.parseDouble(lon_min.getText().trim());
			    	double lons = (lon_sec.getText() == null || lon_sec.getText().trim().equals("")) ? 0 : Double.parseDouble(lon_sec.getText().trim());

			    	PersistentState.true_latitude_deg  = (north.isSelected() ? +1 : -1) * Math.abs(latd + latm/60.0 + lats/3600.0);
			    	PersistentState.true_longitude_deg = (east .isSelected() ? +1 : -1) * Math.abs(lond + lonm/60.0 + lons/3600.0);
			    	
					int offset     = (int) ((PersistentState.true_longitude_deg + 7.5)/15);
					String tz_name = String.format("GMT%c%d", ((offset < 0) ? '-' : '+'), Math.abs(offset));
					PersistentState.timezone = TimeZone.getTimeZone(tz_name);
		    	} catch (NumberFormatException e) {
			    	// TODO 
		    	}
		    	dispose();
			}
		});
		save.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: TAB %x %x save->cancel...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					cancel.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x save->west...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					west.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt){}
		});

		cancel.setFocusTraversalKeysEnabled(false);
		cancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	System.out.printf("%s: %3d: ENTER...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
		    	// TODO 
		    	dispose();
			}
		});
		cancel.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
		    	if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() != KeyEvent.SHIFT_DOWN_MASK) {
		    		System.out.printf("%s: %3d: TAB %x %x cancel->lat_deg...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					lat_deg.requestFocus();
		    	} else if (evt.getKeyChar() == KeyEvent.VK_TAB && (int) evt.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
			    	System.out.printf("%s: %3d: SHFT TAB %x %x cancel->save...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (int) evt.getKeyChar(), (int) evt.getModifiersEx());
					save.requestFocus();
		    	}
			}
			@Override public void keyPressed(KeyEvent evt) {}
			@Override public void keyReleased(KeyEvent evt){}
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
