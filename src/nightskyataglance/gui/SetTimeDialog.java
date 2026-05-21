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
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nightskyataglance.util.PersistentState;

public class SetTimeDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2519693274364184533L;

	public final MainFrame frame;
	public final SetTimeDialog dialog;

	public final JComboBox<String> hour_cb   = new JComboBox<String>(hour_names);
	public final JComboBox<String> minute_cb = new JComboBox<String>(minute_names);
	public final JComboBox<String> second_cb = new JComboBox<String>(minute_names);

	public final Button cancel = new Button("Cancel");
	public final Button save   = new Button("Save");
	public final Button now    = new Button("Now");

	public TimeZone timezone = null;

	public SetTimeDialog(MainFrame f, String name)
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

	private static final String[] hour_names = { 
		"00", "01", "02", "03", "04", "05", 
		"06", "07", "08", "09", "10", "11", 
		"12", "13", "14", "15", "16", "17", 
		"18", "19", "20", "21", "22", "23", 
	};

	private static final String[] minute_names = { 
			"00", "01", "02", "03", "04", "05",	"06", "07", "08", "09", 
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
			"20", "21", "22", "23", "24", "25",	"26", "27", "28", "29", 
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", 
			"40", "41", "42", "43", "44", "45",	"46", "47", "48", "49", 
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59", 
	};
	
	JPanel set_pos_dms()
	{
		Calendar cal = Calendar.getInstance(timezone);
		cal.setTimeInMillis(frame.current_solar_time_ms);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = null;
		
		int row = 0;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 5);		// top, left, bottom, right
		hour_cb.setSelectedIndex(h);
		panel.add(hour_cb, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel(":"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		minute_cb.setSelectedIndex(m);
		panel.add(minute_cb, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(new JLabel(":"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 4;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		second_cb.setSelectedIndex(s);
		panel.add(second_cb, gbc);
		
		
		row += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(cancel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(now, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 3;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 2;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(save, gbc);
		
		
		save.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				int h = hour_cb.getSelectedIndex();
				int m = minute_cb.getSelectedIndex();
				int s = second_cb.getSelectedIndex();
				long system_ms = System.currentTimeMillis();
				Calendar cal = Calendar.getInstance(PersistentState.timezone);
				cal.setTimeInMillis(system_ms + frame.solar_time_offset_ms);
				cal.set(Calendar.HOUR_OF_DAY, h);
				cal.set(Calendar.MINUTE,      m);
				cal.set(Calendar.SECOND,      s);
				long corrected_ms = cal.getTimeInMillis();
				frame.solar_time_offset_ms = corrected_ms - system_ms;
		    	dispose();
			}
		});

		now.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				frame.solar_time_offset_ms = 0;
				dispose();
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
