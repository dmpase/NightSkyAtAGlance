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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.cities.UsCitiesEntry;
import nightskyataglance.util.PersistentState;

public class SetUsCityDialog extends Dialog implements WindowListener {
	private static final long serialVersionUID = -2519693274364184533L;

	public final MainFrame frame;
	public final SetUsCityDialog dialog;

	public int digits = 10;

	private JTextField search     = new JTextField("", 36);
	private JTextField city_name  = new JTextField("", 25);
	private JTextField state_name = new JTextField("", 10);
	private JTextField zone_name  = new JTextField("", 25);
	private JButton    cancel     = new JButton("Cancel");
	private JButton    save       = new JButton("Save");

	UsCitiesEntry      city       = null;
	
	private JLabel search_jl      = new JLabel("\"City, State\" or Zip Code");
	private JLabel city_name_jl   = new JLabel("City");
	private JLabel state_name_jl  = new JLabel("State");
	private JLabel zone_name_jl   = new JLabel("Time Zone");
	
	private void set_font(Font font)
	{
		setFont(font);
		search.setFont(font);
		city_name.setFont(font);
		state_name.setFont(font);
		zone_name.setFont(font);
		cancel.setFont(font);
		save.setFont(font);
		search_jl.setFont(font);
		city_name_jl.setFont(font);
		state_name_jl.setFont(font);
		zone_name_jl.setFont(font);
	}

	public SetUsCityDialog(MainFrame f, String name)
	{
		super(f, name, true);

		set_font(PersistentState.get_font());

		frame    = f;
		dialog   = this;

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

		search.requestFocus();
		city_name.setEditable(false);
		state_name.setEditable(false);
		zone_name.setEditable(false);

		city_name.setText(PersistentState.city);
		state_name.setText(PersistentState.state);
		zone_name.setText(PersistentState.timezone.getID());

		int row = 0;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(search_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(search, gbc);


		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(city_name_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(state_name_jl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 5);		// top, left, bottom, right
		panel.add(zone_name_jl, gbc);
		
		
		row += 1;
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 0, 1, 0);		// top, left, bottom, right
		panel.add(city_name, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 1;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(state_name, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(1, 5, 1, 0);		// top, left, bottom, right
		panel.add(zone_name, gbc);


		row += 1;

		gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(cancel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx     = 2;
		gbc.gridy     = row;
		gbc.anchor    = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.insets    = new Insets(10, 0, 0, 0);		// top, left, bottom, right
		panel.add(save, gbc);
		

		search.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
				String search_text = search.getText();
				if (5 == search_text.length() && search_text.matches("[0-9][0-9][0-9][0-9][0-9]")) {
			    	UsCitiesEntry zip_city = frame.us_cities.find_zip(search_text);
			    	if (zip_city != null) {
			    		PersistentState.city     = zip_city.city_ascii;
			    		PersistentState.state    = zip_city.state_name;
			    		PersistentState.timezone = TimeZone.getTimeZone(zip_city.timezone);
			    		PersistentState.true_latitude_deg  = zip_city.latitude;
			    		PersistentState.true_longitude_deg = zip_city.longitude;
			    	}
				} else {
					String[] fields = search_text.split(",");
			    	UsCitiesEntry first = null;
			    	if (fields.length == 1) {
			    		// city_name only
				    	first = frame.us_cities.find_first(fields[0].trim(), "");
			    	} else if (fields.length == 2) {
				    	first = frame.us_cities.find_first(fields[0].trim(), fields[1].trim());
			    	}
			    	if (first != null) {
			    		PersistentState.city     = first.city_ascii;
			    		PersistentState.state    = first.state_name;
			    		PersistentState.timezone = TimeZone.getTimeZone(first.timezone);
			    		PersistentState.true_latitude_deg  = first.latitude;
			    		PersistentState.true_longitude_deg = first.longitude;
			    	}
				}
				dispose();
			}
		});
		search.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent evt) 
			{
				String search_text = (search.getText() + (char) evt.getKeyChar()).trim();
				if (0 == search_text.length()) {
					city_name.setText(PersistentState.city);
					state_name.setText(PersistentState.state);
					zone_name.setText(PersistentState.timezone.getID());
					city = null;
				} else if (5 == search_text.length() && search_text.matches("[0-9][0-9][0-9][0-9][0-9]")) {
			    	UsCitiesEntry zip_city = frame.us_cities.find_zip(search_text);
			    	if (zip_city != null) {
				    	city_name.setText(zip_city.city_ascii);
				    	state_name.setText(zip_city.state_name);
				    	zone_name.setText(zip_city.timezone);
				    	city = zip_city;
			    	} else {
						city_name.setText(PersistentState.city);
						state_name.setText(PersistentState.state);
						zone_name.setText(PersistentState.timezone.getID());
						city = null;
			    	}
				} else {
					String[] fields = search_text.split(",");
			    	UsCitiesEntry first = null;
			    	if (fields.length == 1) {
			    		// city_name only
				    	first = frame.us_cities.find_first(fields[0].trim(), "");
			    	} else if (fields.length == 2) {
				    	first = frame.us_cities.find_first(fields[0].trim(), fields[1].trim());
			    	}
			    	if (first == null) {
			    		city_name.setText(PersistentState.city);
			    		state_name.setText(PersistentState.state);
			    		zone_name.setText(PersistentState.timezone.getID());
						city = null;
			    	} else {
				    	city_name.setText(first.city_ascii);
				    	state_name.setText(first.state_name);
				    	zone_name.setText(first.timezone);
						city = first;
			    	}
				}
			}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e){}
		});
		

		save.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	if (city != null) {
		    		PersistentState.city     = city.city_ascii;
		    		PersistentState.state    = city.state_name;
		    		PersistentState.timezone = TimeZone.getTimeZone(city.timezone);
		    		PersistentState.true_latitude_deg  = city.latitude;
		    		PersistentState.true_longitude_deg = city.longitude;
		    	}
		    	dispose();
			}
		});

		cancel.setFocusTraversalKeysEnabled(false);
		cancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent evt) 
			{
		    	dispose();
			}
		});


		return panel;
	}


	public static final String[][] state_names = {
			{ "Alabama",		"AL", }, 
			{ "Alaska",			"AK", }, 
			{ "Arizona",		"AZ", }, 
			{ "Arkansas",		"AR", }, 
			{ "California",		"CA", }, 
			{ "Colorado",		"CO", }, 
			{ "Connecticut",	"CT", }, 
			{ "Deleware",		"DE", }, 
			{ "Florida",		"FL", }, 
			{ "Georgia",		"GA", }, 
			{ "Hawaii",			"HI", }, 
			{ "Idaho",			"ID", }, 
			{ "Illinois", 		"IL", }, 
			{ "Indianna",		"IN", }, 
			{ "Iowa",			"IA", }, 
			{ "Kansas",			"KS", }, 
			{ "Kentucky",		"KY", }, 
			{ "Louisianna",		"LA", }, 
			{ "Maine",			"ME", }, 
			{ "Maryland",		"MD", }, 
			{ "Massachusetts",	"MA", }, 
			{ "Michigan",		"MI", }, 
			{ "Minnesota",		"MN", }, 
			{ "Mississippi",	"MS", }, 
			{ "Missouri",		"MO", }, 
			{ "Montana",		"MT", }, 
			{ "Nebraska",		"NE", },
			{ "Nevada",			"NV", }, 
			{ "New Hampshire",	"NH", }, 
			{ "New Jersey",		"NJ", }, 
			{ "New Mexico",		"NM", }, 
			{ "New York",		"NY", }, 
			{ "North Carolina",	"NC", }, 
			{ "North Dakota",	"ND", }, 
			{ "Ohio",			"OH", }, 
			{ "Oklahoma",		"OK", }, 
			{ "Oregon",			"OR", }, 
			{ "Pennsylvania",	"PA", }, 
			{ "Rhode Island",	"RI", }, 
			{ "South Carolina",	"SC", }, 
			{ "South Dakota",	"SD", }, 
			{ "Tennessee",		"TN", }, 
			{ "Texas",			"TX", }, 
			{ "Utah",			"UT", }, 
			{ "Vermont",		"VT", }, 
			{ "Virginia",		"VA", }, 
			{ "Washington",		"WA", }, 
			{ "West Virginia",	"WV", }, 
			{ "Wisconsin",		"WI", }, 
			{ "Wyoming",		"WY", },
	};

	
	
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
