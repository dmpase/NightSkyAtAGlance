package nightskyataglance.gui;

/*******************************************************************************
 * Copyright (c) 2025 Douglas M. Pase                                          *
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
 * infringement of that party's intellectual property rights.                  *
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lib.astro.PracticalAstronomy;
import nightskyataglance.NightSkyAtAGlance;

public class ShowImage {

	public static void show_image(String cache, double ra_dhrs, double de_ddeg, double width_arcmin, double height_arcmin, int width_pixels, int height_pixels) 
	{
		// round the RA and DEC to the nearest minute of arc
		double ra_ddeg = ra_dhrs * 15;
		int ra_deg = (int) ra_ddeg;
		int ra_min = ((int) (ra_ddeg * 60)) % 60;
		int de_deg = (int) de_ddeg;
		int de_min = ((int) (de_ddeg * 60)) % 60;

		// read the file, either from disk, or the Internet, as available
		byte[] ch = null;
		if ((new File(cache)).isDirectory()) {
			String file_name = String.format("%s/%d_%d_%d_%d_60_60.gif", cache, ra_deg, ra_min, de_deg, de_min);
			if ((new File(file_name)).isFile()) {
				try {
			        InputStream is = new FileInputStream(file_name);
			        ch = is.readAllBytes();
			        is.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				String uri_str = String.format(
						"https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%d+%d&d=%dD+%d&e=J2000&h=%d&w=%d&f=gif", 
						ra_deg, ra_min, de_deg, de_min, 60, 60);
				try {
					URI uri = new URI(uri_str);
			        InputStream is = uri.toURL().openStream(); 
			        OutputStream os = new FileOutputStream(file_name);
			        ch = is.readAllBytes();
			        os.write(ch);
			        is.close();
			        os.close();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			String uri_str = String.format(
					"https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%d+%d&d=%dD+%d&e=J2000&h=%d&w=%d&f=gif", 
					ra_deg, ra_min, de_deg, de_min, 60, 60);
			try {
				URI uri = new URI(uri_str);
		        InputStream is = uri.toURL().openStream(); 
		        ch = is.readAllBytes();
		        is.close();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void show_image(String ra_dhrs, String de_ddeg, String height_arcmin, String width_arcmin) 
	{
		try {
			String ra = ra_dhrs.trim().replaceAll("[dhms\"'"+PracticalAstronomy.degree_symbol+"]", " ").replaceAll("[ ][ ]*", "+");
			String de = de_ddeg.trim().replaceAll("[dhms\"'"+PracticalAstronomy.degree_symbol+"]", " ").replaceAll("[ ][ ]*", "+");
			String w  = height_arcmin.trim();
			String h  = width_arcmin.trim();
			String uri_str = String.format("https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%s&d=%s&e=J2000&h=%s&w=%s&f=gif", ra, de, w, h);
			System.out.printf("%s: %3d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), uri_str);
			URI uri = new URI(uri_str);
			URL url = uri.toURL();
		    Icon icon = new ImageIcon(url);
		    JLabel label = new JLabel(icon);
		 
		    JFrame f = new JFrame("GIF Image");
		    f.getContentPane().add(label);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    f.pack();
		    f.setLocationRelativeTo(null);
		    f.setVisible(true);
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void show_image(double ra_dhrs, double de_ddeg, int height_arcmin, int width_arcmin) 
	{
		// round the RA and DEC to the nearest minute of arc
		double ra_ddeg = ra_dhrs * 15;
		int ra_deg =  (int)  ra_ddeg;
		int ra_min = ((int) (ra_ddeg * 60)) % 60;
		int de_deg =  (int)  de_ddeg;
		int de_min = ((int) (de_ddeg * 60)) % 60;

		// read the file from the Internet
		String uri_str = String.format(
			"https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%d+%d&d=%dD+%d&e=J2000&h=%d&w=%d&f=gif", 
			ra_deg, ra_min, de_deg, de_min, 60, 60);
		byte[] ch = null;
		try {
			URI uri = new URI(uri_str);
	        InputStream is = uri.toURL().openStream(); 
	        ch = is.readAllBytes();
	        is.close();

		    Icon icon = new ImageIcon(ch);
		    JLabel label = new JLabel(icon);
		 
		    JFrame f = new JFrame("GIF Image");
		    f.getContentPane().add(label);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    f.pack();
		    f.setLocationRelativeTo(null);
		    f.setVisible(true);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// display the image
	}

	public static void main(String[] args) 
	{
		try {
			String ra = "05+34+31.78";
			String de = "22+01+02.6";
			String w  = "15";
			String h  = "15";
			String uri_str = String.format("https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%s&d=%s&e=J2000&h=%s&w=%s&f=gif", ra, de, w, h);
			System.out.printf("%s: %3d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), uri_str);
			URI uri = new URI(uri_str);
			URL url = uri.toURL();
		    Icon icon = new ImageIcon(url);
		    JLabel label = new JLabel(icon);
		 
		    JFrame f = new JFrame("GIF Image");
		    f.getContentPane().add(label);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    f.pack();
		    f.setLocationRelativeTo(null);
		    f.setVisible(true);
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
