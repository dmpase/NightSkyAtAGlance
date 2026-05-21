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

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nightskyataglance.NightSkyAtAGlance;

public class HelpMenu extends Menu {
	private static final long serialVersionUID = 7693871871934518L;

	public final MainFrame frame;

	public static final String LICENSE         = "License";
	public static final String MAGNITUDE_CHART = "Magnitude Chart";
	public static final String HELP_PAGE1      = "About " + NightSkyAtAGlance.title + " (Page 1)";
	public static final String HELP_PAGE2      = "About " + NightSkyAtAGlance.title + " (Page 2)";
	public static final String HELP_PAGE3      = "About " + NightSkyAtAGlance.title + " (Page 3)";
	public static final String STSCI           = "ST ScI Acknowledgement";
	public static final String DSO_IMAGES      = "DSO Images";
	public static final String ASTROMETRICS    = "Astrometrics";
	public static final String NEARBY_DSOS     = "Nearby DSOs";

	public HelpMenu(MainFrame f, String name)
	{
		super(name);
		frame = f;

    	MenuItem lic = new MenuItem(LICENSE);
    	lic.setEnabled(true);
    	add(lic);
    	MenuItem sts = new MenuItem(STSCI);
    	sts.setEnabled(true);
    	add(sts);
    	addSeparator();
    	MenuItem pg1 = new MenuItem(HELP_PAGE1);
    	pg1.setEnabled(true);
    	add(pg1);
    	MenuItem pg2 = new MenuItem(HELP_PAGE2);
    	pg2.setEnabled(true);
    	add(pg2);
    	MenuItem pg3 = new MenuItem(HELP_PAGE3);
    	pg3.setEnabled(true);
    	add(pg3);
    	addSeparator();
    	MenuItem dso = new MenuItem(DSO_IMAGES);
    	dso.setEnabled(true);
    	add(dso);
    	MenuItem ast = new MenuItem(ASTROMETRICS);
    	ast.setEnabled(true);
    	add(ast);
    	MenuItem near = new MenuItem(NEARBY_DSOS);
    	near.setEnabled(true);
    	add(near);
    	MenuItem mag = new MenuItem(MAGNITUDE_CHART);
    	mag.setEnabled(true);
    	add(mag);

    	addActionListener(new Listener());
	}

	public class Listener implements ActionListener {
		public String title = null;
		public final String msg1 = ""
				+ NightSkyAtAGlance.title + "\n"
				+ "\n"
				+ "Version " + NightSkyAtAGlance.version + ", Copyright (c) 2026 Douglas M. Pase\n"
				+ "\n"
				+ "This program displays the complete sky in a single star chart. Chart elements include solar and sidereal time, sunrise, sunset\n"
				+ "astronomical twilight, and lunar and planetary positions, as well as the usual stars, constellations, galaxies, and nebulae.\n"
				+ "It also shows what portion of sky is visible for any given latitude, longitude, date, and time.\n"
				+ "\n"
				+ "The program is designed to assist the user in identifying, selecting, and locating astronomical objects that might be\n"
				+ "interesting to observe, with a telescope, binoculars, or the unaided eye. Interactive filters allow the user to select\n"
				+ "objects based on a wide range of criteria, including type, size, brightness, and location.\n"
				+ "\n"
				+ "Data sources for this program include:\n"
				+ "Constellations and Stars\n"
				+ " 1) Constellation names and component stars were retrieved from https://en.wikipedia.org/\n"
				+ " 2) Database of named stars was retrieved from https://www.iau.org/public/themes/naming_stars\n"
				+ " 3) Yale Bright Star Catalog was retrieved from http://tdc-www.harvard.edu/catalogs/bsc5.html\n"
				+ "\n"
				+ "Sun, moon, and planet positions were calculated using algorithms described in:\n"
				+ " 4) \"Practical astronomy with your calculator\", by Peter Duffett-Smith, 3rd ed., 1988, Cambridge University Press.\n"
				+ " 5) \"Astronomical Formulae for Calculators\", by Jean Meeus, 3rd ed., 1985, Willmann-Bell, Inc.\n"
				+ " 6) \"Astronomical Algorithms\", by Jean Meeus, 2nd ed., 1991, Willmann-Bell, Inc.\n"
				+ " 7) \"Celestial Calculations: A Gentle Introduction to Computational Astronomy,\" by Jackie L. Lawrence, 2018, \n"
				+ "    The MIT Press, Cambridge, London, England,\n"
				+ "\n"
				+ "Page 1\n"
				+ "\n"
				;

		public final String msg2 = ""
				+ "Double Stars, Galaxies, and Nebulae\n"
				+ " 8) Database of Caldwell objects was retrieved from https://en.wikipedia.org/wiki/Caldwell_catalogue\n"
				+ " 9) Database of Herschel objects was retrieved from https://en.wikipedia.org/wiki/Herschel_400_Catalogue\n"
				+ "10) Database of Messier  objects was retrieved from https://en.wikipedia.org/wiki/Messier_object\n"
				+ "11) Sharpless catalog of emission nebulae (Sh-2) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR-2\n"
				+ "12) Arp Peculiar Galaxies (ARP) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR-2\n"
				+ "13) Index Catalog (IC) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR-2\n"
				+ "14) New General Catalog (NGC) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR-2\n"
				+ "15) Uppsala General Catalog of Galaxies (UGC) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR-2\n"
				+ "16) Variable Star Index Catalog (VSX) was retrieved from https://vizier.cds.unistra.fr/viz-bin/VizieR?-source=B%2Fvsx\n"
				+ "17) Washington Double Star Catalog (WDS) was retrieved from https://www.astro.gsu.edu/wds\n"
				+ "\n"
				+ "Webb Society Deep Space Object Collections, compiled by the Webb Society and edited by Kenneth Glyn Jones\n"
				+ "18) \"Webb Society Deep-Sky Observer's Handbook, Vol. 1, Double Stars\", January 1986, Enslow Pub. Inc.\n"
				+ "19) \"Webb Society Deep-Sky Observer's Handbook, Vol. 2, Planetary and Gaseous Nebulae\", August 1979, Enslow Pub. Inc.\n"
				+ "20) \"Webb Society Deep-Sky Observer's Handbook, Vol. 3, Open and Globular Clusters\", January 1980, Enslow Pub. Inc.\n"
				+ "21) \"Webb Society Deep-Sky Observer's Handbook, Vol. 4, Galaxies\", January 1981, Enslow Pub. Inc.\n"
				+ "22) \"Webb Society Deep-Sky Observer's Handbook, Vol. 5, Clusters of Galaxies\", January 1982, Enslow Pub. Inc.\n"
				+ "23) \"Webb Society Deep-Sky Observer's Handbook, Vol. 6, Anonymous Galaxies\", January 1987, Enslow Pub. Inc.\n"
				+ "24) \"Webb Society Deep-Sky Observer's Handbook, Vol. 7, The Southern Sky\", April 1987, Enslow Pub. Inc.\n"
				+ "25) \"Webb Society Deep-Sky Observer's Handbook, Vol. 8, Variable Stars\", October 1990, Enslow Pub. Inc.\n"
				+ "\n"
				+ "Page 2\n"
				+ "\n"
				;

		public final String msg3 = ""
				+ "Cities and Towns\n"
				+ "26) Database of US cities provided courtesy of https://simplemaps.com/data/us-cities\n"
				+ "27) Database of world cities provided courtesy of https://simplemaps.com/data/world-cities\n"
				+ "\n"
				+ "Additional Links\n"
				+ "28) Wikipedia Lists of Astronomical Objects, https://en.wikipedia.org/wiki/Lists_of_astronomical_objects\n"
				+ "29) DSO Images, https://archive.stsci.edu/cgi-bin/dss_form\n"
				+ "30) Field of View Calculator, https://astronomy.tools/calculators/field_of_view\n"
				+ "31) CDS Portal, http://cdsportal.u-strasbg.fr/\n"
				+ "\n"
				+ "Page 3\n"
				+ "\n"
				;

		public final String msg4 = ""
				+ "Magnitude values generally apply to point-like objects such as\n"
				+ "stars. Galaxies and nebulae are diffuse, so subtract an integer.\n"
				+ "(https://www.cruxis.com/scope/limitingmagnitude.htm)\n"
				+ "\n"
				+ "OTA                               Magnitude Limit\n"
				+ "Naked-eye (city)                                 4\n"
				+ "Naked-eye (suburbs)                        5\n"
				+ "Naked-eye (dark sky)                        6\n"
				+ "Binoculars                                          10\n"
				+ "  4\" (100mm) telescope                   13.5\n"
				+ "  6\" (150mm) telescope                   14.3\n"
				+ "  8\" (200mm) telescope                   14.8\n"
				+ "10\" (250mm) telescope                   15.2\n"
				+ "12\" (300mm) telescope                   15.5\n"
				+ "14\" (350mm) telescope                   15.8\n"
				+ "16\" (400mm) telescope                   16\n"
				+ "Hubble Space Telescope                 30\n"
				+ "James Webb Space Telescope     35\n"
				;

		public static final String license = ""
				+ "\n"
				+ "Copyright (c) 2026 Douglas M. Pase                                          \n"
				+ "All rights reserved.                                                        \n"
				+ "Redistribution and use in source and binary forms, with or without          \n"
				+ "modification, are permitted provided that the following conditions          \n"
				+ "are met:                                                                    \n"
				+ "o      Redistributions of source code must retain the above copyright       \n"
				+ "        notice, this list of conditions and the following disclaimer.       \n"
				+ "o      Redistributions in binary form must reproduce the above copyright    \n"
				+ "        notice, this list of conditions and the following disclaimer in     \n"
				+ "        the documentation and/or other materials provided with the          \n"
				+ "        distribution.                                                       \n"
				+ "o      Neither the name of the copyright holder nor the names of its        \n"
				+ "        contributors may be used to endorse or promote products derived     \n"
				+ "        from this software without specific prior written permission.       \n"
				+ "                                                                            \n"
				+ "The copyright holders provide no reassurances that the source code provided \n"
				+ "does not infringe any patent, copyright, or any other intellectual property \n"
				+ "rights of third parties. The copyright holders disclaim any liability to    \n"
				+ "any recipient for claims brought against recipient by any third party for   \n"
				+ "infringement of that parties intellectual property rights.                  \n"
				+ "                                                                            \n"
				+ "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" \n"
				+ "AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE   \n"
				+ "IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE  \n"
				+ "ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE   \n"
				+ "LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR         \n"
				+ "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF        \n"
				+ "SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS    \n"
				+ "INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN     \n"
				+ "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)     \n"
				+ "ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF      \n"
				+ "THE POSSIBILITY OF SUCH DAMAGE.                                             \n"
				+ "\n"
				;

		public static final String stsci = ""
				+ "The Digitized Sky Surveys were produced at the Space Telescope Science Institute under U.S. Government grant NAG W-2166.   \n"
				+ "The images of these surveys are based on photographic data obtained using the Oschin Schmidt Telescope on Palomar Mountain \n"
				+ "and the UK Schmidt Telescope. The plates were processed into the present compressed digital form with the permission of    \n"
				+ "these institutions.\n"
				+ "\n"
				+ "The National Geographic Society - Palomar Observatory Sky Atlas (POSS-I) was made by the California Institute of Technology\n"
				+ "with grants from the National Geographic Society.\n"
				+ "\n"
				+ "The Second Palomar Observatory Sky Survey (POSS-II) was made by the California Institute of Technology with funds from the \n"
				+ "National Science Foundation, the National Geographic Society, the Sloan Foundation, the Samuel Oschin Foundation, and the  \n"
				+ "Eastman Kodak Corporation.\n"
				+ "\n"
				+ "The Oschin Schmidt Telescope is operated by the California Institute of Technology and Palomar Observatory.\r\n"
				+ "\r\n"
				+ "The UK Schmidt Telescope was operated by the Royal Observatory Edinburgh, with funding from the UK Science and Engineering \n"
				+ "Research Council (later the UK Particle Physics and Astronomy Research Council), until 1988 June, and thereafter by the    \n"
				+ "Anglo-Australian Observatory. The blue plates of the southern Sky Atlas and its Equatorial Extension (together known as the\n"
				+ "SERC-J), as well as the Equatorial Red (ER), and the Second Epoch [red] Survey (SES) were all taken with the UK Schmidt.   \n"
				+ "\n"
				+ "All data are subject to the copyright given in the copyright summary. Copyright information specific to individual plates  \n"
				+ "is provided in the downloaded FITS headers.\n"
				+ "\n"
				+ "Supplemental funding for sky-survey work at the ST ScI is provided by the European Southern Observatory. "
				+ "\n"
				;

		public static final String dso_images = ""
				+ "Create an image of a DSO:\n"
				+ "(1) Bring up the Field of View menu (CTRL-F or File->Field of View)\n"
				+ "(2) Select an object, e.g., by name in the search bar\n"
				+ "(3) Select the OTA and eyepiece or camera for viewing\n"
				+ "(4) Click the 'Image' button\n"
				+ "(5) Iconify the main window to see the image\n"
				+ "The image will be visible in another window.\n"
				+ "\n"
				+ "For example, to see an image of NGC 891:\n"
				+ "(1) Bring up the Field of View menu\n"
				+ "(2) Type 'NGC 891' or 'Silver Sliver Galaxy' in the search field\n"
				+ "(3) Under Optical Tube Assembly, select 'Meade 10\" f/10 ACF'\n"
				+ "(4) Under Camera, select 'ZWO ASI183MC Pro'\n"
				+ "(5) Under Image, select 'Camera'\n"
				+ "(6) Click the 'Image' button at the bottom of the panel\n"
				+ "(7) Iconify the main window to see the image window\n"
				+ "\n"
				+ "To take measurements, see 'Astrometrics'.\n"
				+ "\n"
				;

		public static final String astrometrics = ""
				+ "To take measurements in an image window:\n"
				+ "double click                       - shows the location of the DSO\n"
				+ "SHIFT double click            - sets main frame DSO selection\n"
				+ "CTRL press, drag, release - shows the location and distance between the end points\n"
				+ "CTRL SHIFT click              - erases all temporary selections\n"
				+ "\n"
				+ "For example, to measure the angular width of NGC 891:\n"
				+ "(1) Bring up an image of NGC 891 using the instructions in 'DSO Images'\n"
				+ "(2) Press the CTRL key and hold it down\n"
				+ "(3) Move the mouse to the upper left corner of NGC 891\n"
				+ "(4) Press the left mouse button and drag the mouse to the lower right corner\n"
				+ "(5) Release the mouse button, then release the CTRL key\n"
				+ "\n"
				+ "The metric will show the coordinates of the upper left and lower right points,\n"
				+ "and the angular distance between them. The angular distance is obscured by the\n"
				+ "center coordinate of the DSO. To make the angular distance visible, select a new\n"
				+ "custom coordinate using SHIFT double click on a location to one side.\n"
				+ "\n"
				+ "To identify other DSOs in an image, see 'Nearby DSOs'.\n"
				+ "\n"
				;

		public static final String nearby_dsos = ""
				+ "To identify nearby DSOs in an image window:\n"
				+ "(1) Select the desired DSO in the image window using SHIFT double click\n"
				+ "(2) Return to the main window and select the Field of View panel (CTRL-F)\n"
				+ "(3) Press the 'Nearest DSO' button to identify the selected DSO\n"
				+ "\n"
				+ "For example, to identify a galaxy near NGC 891:\n"
				+ "(1) Bring up an image of NGC 891,\n"
				+ "    (a) Bring up the Field of View menu (CTRL-F)\n"
				+ "    (b) Type 'NGC 891' in the search field\n"
				+ "    (c) Under Optical Tube Assembly, select 'Meade 8\" f/10 ACF'\n"
				+ "    (d) Under Eyepiece, select 'TeleVue Nagler 31mm'\n"
				+ "    (e) Under Image, select 'Eyepiece'\n"
				+ "    (f) Click the 'Image' button at the bottom of the panel\n"
				+ "    (g) Iconify the main window to see the new image\n"
				+ "(2) Select the galaxy in the lower left corner using SHIFT double click\n"
				+ "     (a red crosshair should appear over the location)\n"
				+ "(3) Return to the main window and select the Field of View panel (CTRL-F)\n"
				+ "     (the Search field should contain the word 'Custom')\n"
				+ "(4) Press the 'Nearest DSO' button\n"
				+ "(5) The Search field should now contain 'NGC 910', the selected galaxy\n"
				+ "\n"
				;

		public Listener()
		{
			title = "About " + NightSkyAtAGlance.title;
		}
	
		@Override public void actionPerformed(ActionEvent evt)
		{
			//System.out.println(evt);
			Cursor old = frame.getCursor();
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			String what = evt.getActionCommand();
			if (HELP_PAGE1.equals(what)) {
				JOptionPane.showMessageDialog(frame, msg1, title, JOptionPane.PLAIN_MESSAGE);
			} else if (HELP_PAGE2.equals(what)) {
				JOptionPane.showMessageDialog(frame, msg2, title, JOptionPane.PLAIN_MESSAGE);
			} else if (HELP_PAGE3.equals(what)) {
				JOptionPane.showMessageDialog(frame, msg3, title, JOptionPane.PLAIN_MESSAGE);
			} else if (LICENSE.equals(what)) {
				JOptionPane.showMessageDialog(frame, license, "License", JOptionPane.PLAIN_MESSAGE);
			} else if (MAGNITUDE_CHART.equals(what)) {
				JOptionPane.showMessageDialog(frame, msg4, "Magnitude Chart", JOptionPane.PLAIN_MESSAGE);
			} else if (STSCI.equals(what)) {
				JOptionPane.showMessageDialog(frame, stsci, STSCI, JOptionPane.PLAIN_MESSAGE);
			} else if (DSO_IMAGES.equals(what)) {
				JOptionPane.showMessageDialog(frame, dso_images, DSO_IMAGES, JOptionPane.PLAIN_MESSAGE);
			} else if (ASTROMETRICS.equals(what)) {
				JOptionPane.showMessageDialog(frame, astrometrics, ASTROMETRICS, JOptionPane.PLAIN_MESSAGE);
			} else if (NEARBY_DSOS.equals(what)) {
				JOptionPane.showMessageDialog(frame, nearby_dsos, NEARBY_DSOS, JOptionPane.PLAIN_MESSAGE);
			} else {
		    	System.out.printf("%s: %3d: else (%s)...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), what);
			}
			frame.setCursor(old);
		}
	
	}
}
