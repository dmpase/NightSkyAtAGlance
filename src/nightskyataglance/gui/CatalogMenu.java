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

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class CatalogMenu extends Menu {
	private static final long serialVersionUID = 7693871871934518L;

	public final MainFrame main_frame;

	public static final String CONSTELLAITONS            = "Constellations";
	public static final String CONSTELLAITON_AREAS       = "Constellation Areas";
	public static final String PRIMARY_STARS             = "Primary Stars";
	public static final String NAMED_STARS               = "Named Stars";
	public static final String YALE_BSC                  = "Yale Bright Stars";
	public static final String SUN_MOON_PLANETS          = "Sun, Moon, Planets";
	public static final String CALDWELL                  = "Caldwell  109";
	public static final String HERSCHEL_400              = "Herschel 400";
	public static final String HERSCHEL_2500             = "Herschel 2,500";
	public static final String MESSIER                   = "Messier  109";
	public static final String SHARPLESS                 = "Sharpless (Sh-2)";
	public static final String ARP_CATALOG               = "Arp Peculiar Galaxies (ARP)";
	public static final String IC_CATALOG                = "Index Catalog (IC)";
	public static final String NGC_CATALOG               = "New General Catalog (NGC)";
	public static final String UGC_CATALOG               = "Uppsala General Catalogue of Galaxies (UGC)";
	public static final String VSX_CATALOG               = "Variable Star Index Catalog (VSX)";
	public static final String WDS_CATALOG               = "Washington Double Star Catalog (WDS)";
	public static final String WEBB_DOUBLE_STARS         = "Webb Society: Double Stars";
	public static final String WEBB_PLANETARY_NEBULAE    = "Webb Society: Planetary Nebulae";
	public static final String WEBB_GASEOUS_NEBULAE      = "Webb Society: Gaseous Nebulae";
	public static final String WEBB_OPEN_CLUSTERS        = "Webb Society: Open Clusters";
	public static final String WEBB_GLOBULAR_CLUSTERS    = "Webb Society: Globular Clusters";
	public static final String WEBB_GALAXIES             = "Webb Society: Galaxies";
	public static final String WEBB_CLUSTERS_OF_GALAXIES = "Webb Society: Clusters of Galaxies";
	public static final String WEBB_ANONYMOUS_GALAXIES   = "Webb Society: Anonymous Galaxies";
	public static final String WEBB_SOUTHERN_SKY         = "Webb Society: The Southern Sky";
	public static final String WEBB_VARIABLE_STARS       = "Webb Society: Variable Stars";
	public static final String FAVORITE_DSOS             = "Favorite DSOs";

	public static final MenuItem cns = new MenuItem(CONSTELLAITONS);
	public static final MenuItem cna = new MenuItem(CONSTELLAITON_AREAS);
	public static final MenuItem pri = new MenuItem(PRIMARY_STARS);
	public static final MenuItem nds = new MenuItem(NAMED_STARS);
	public static final MenuItem ybs = new MenuItem(YALE_BSC);
	public static final MenuItem pla = new MenuItem(SUN_MOON_PLANETS);
	public static final MenuItem cal = new MenuItem(CALDWELL);
	public static final MenuItem h04 = new MenuItem(HERSCHEL_400);
	public static final MenuItem h25 = new MenuItem(HERSCHEL_2500);
	public static final MenuItem mes = new MenuItem(MESSIER);
	public static final MenuItem sh2 = new MenuItem(SHARPLESS);
	public static final MenuItem arp = new MenuItem(ARP_CATALOG);
	public static final MenuItem ic  = new MenuItem(IC_CATALOG);
	public static final MenuItem ngc = new MenuItem(NGC_CATALOG);
	public static final MenuItem ugc = new MenuItem(UGC_CATALOG);
	public static final MenuItem vsx = new MenuItem(VSX_CATALOG);
	public static final MenuItem wds = new MenuItem(WDS_CATALOG);
	public static final MenuItem wsc = new MenuItem(WEBB_DOUBLE_STARS);
	public static final MenuItem wpn = new MenuItem(WEBB_PLANETARY_NEBULAE);
	public static final MenuItem wgn = new MenuItem(WEBB_GASEOUS_NEBULAE);
	public static final MenuItem woc = new MenuItem(WEBB_OPEN_CLUSTERS);
	public static final MenuItem wgc = new MenuItem(WEBB_GLOBULAR_CLUSTERS);
	public static final MenuItem wsg = new MenuItem(WEBB_GALAXIES);
	public static final MenuItem wcg = new MenuItem(WEBB_CLUSTERS_OF_GALAXIES);
	public static final MenuItem wag = new MenuItem(WEBB_ANONYMOUS_GALAXIES);
	public static final MenuItem wss = new MenuItem(WEBB_SOUTHERN_SKY);
	public static final MenuItem wvs = new MenuItem(WEBB_VARIABLE_STARS);
	public static final MenuItem fav = new MenuItem(FAVORITE_DSOS);

	public static boolean aux_enabled = false;

	public CatalogMenu(MainFrame f, String name)
	{
		super(name);
		main_frame = f;

    	add(cns);
    	add(cna); cna.setEnabled(false);
    	add(pri);
    	add(nds); nds.setEnabled(main_frame.iau_csn             != null);
    	add(ybs); ybs.setEnabled(main_frame.ybsc                != null);
    	addSeparator();
    	add(pla);
    	addSeparator();
    	add(cal); cal.setEnabled(main_frame.caldwell            != null);
    	add(h04); h04.setEnabled(main_frame.herschel_400        != null);
    	add(h25); h25.setEnabled(main_frame.herschel_2500       != null);
    	add(mes); mes.setEnabled(main_frame.messier             != null);
    	addSeparator();
    	add(arp); arp.setEnabled(main_frame.arp                 != null);
    	add(ic);   ic.setEnabled(main_frame.ngc_ic              != null);
    	add(ngc); ngc.setEnabled(main_frame.ngc_ic              != null);
    	add(sh2); sh2.setEnabled(main_frame.sharpless           != null);
    	add(ugc); ugc.setEnabled(main_frame.ugc                 != null);
    	add(vsx); vsx.setEnabled(main_frame.vsx                 != null);
    	add(wds); wds.setEnabled(main_frame.wds                 != null);
    	// add(cvs);
    	addSeparator();
    	add(wsc); wsc.setEnabled(aux_enabled && main_frame.double_stars        != null);
    	add(wpn); wpn.setEnabled(aux_enabled && main_frame.planetary_nebulae   != null);
    	add(wgn); wgn.setEnabled(aux_enabled && main_frame.gaseous_nebulae     != null);
    	add(woc); woc.setEnabled(aux_enabled && main_frame.open_clusters       != null);
    	add(wgc); wgc.setEnabled(aux_enabled && main_frame.globular_clusters   != null);
    	add(wsg); wsg.setEnabled(aux_enabled && main_frame.galaxies            != null);
    	add(wcg); wcg.setEnabled(aux_enabled && main_frame.galaxy_clusters     != null);
    	add(wag); wag.setEnabled(aux_enabled && main_frame.anonymous_galaxies  != null);
    	add(wss); wss.setEnabled(aux_enabled && main_frame.southern_sky        != null);
    	add(wvs); wvs.setEnabled(aux_enabled && main_frame.webb_variable_stars != null);
    	addSeparator();
    	add(fav); fav.setEnabled(aux_enabled && main_frame.favorite_dsos       != null);

		setFont(PersistentState.get_font());

    	addActionListener(new Listener());
	}

	public class Listener implements ActionListener {
		public Listener()
		{
		}
	
		@Override public void actionPerformed(ActionEvent evt)
		{
			//System.out.println(evt);
			Cursor old = main_frame.getCursor();
			main_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			String what = evt.getActionCommand();
			if (CONSTELLAITONS.equals(what)) {
				main_frame.show_constellations = ! main_frame.show_constellations;
			} else if (CONSTELLAITON_AREAS.equals(what)) {
				main_frame.show_constellation_areas  = ! main_frame.show_constellation_areas;
			} else if (PRIMARY_STARS.equals(what)) {
				main_frame.show_primary_stars  = ! main_frame.show_primary_stars;
			} else if (NAMED_STARS.equals(what)) {
				main_frame.show_named_stars  = ! main_frame.show_named_stars;
			} else if (YALE_BSC.equals(what)) {
				main_frame.show_yale_bright_stars   = ! main_frame.show_yale_bright_stars;
			} else if (SUN_MOON_PLANETS.equals(what)) {
				main_frame.show_sun_moon_planets = ! main_frame.show_sun_moon_planets;
			} else if (CALDWELL.equals(what)) {
				main_frame.show_caldwell       = ! main_frame.show_caldwell;
			} else if (HERSCHEL_400.equals(what)) {
				main_frame.show_herschel_400   = ! main_frame.show_herschel_400;
			} else if (HERSCHEL_2500.equals(what)) {
				main_frame.show_herschel_2500  = ! main_frame.show_herschel_2500;
			} else if (MESSIER.equals(what)) {
				main_frame.show_messier        = ! main_frame.show_messier;
			} else if (SHARPLESS.equals(what)) {
				main_frame.show_sharpless       = ! main_frame.show_sharpless;
			} else if (ARP_CATALOG.equals(what)) {
				main_frame.show_arp            = ! main_frame.show_arp;
			} else if (IC_CATALOG.equals(what)) {
				main_frame.show_ic             = ! main_frame.show_ic;
			} else if (NGC_CATALOG.equals(what)) {
				main_frame.show_ngc            = ! main_frame.show_ngc;
			} else if (UGC_CATALOG.equals(what)) {
				main_frame.show_ugc            = ! main_frame.show_ugc;
			} else if (VSX_CATALOG.equals(what)) {
				main_frame.show_vsx            = ! main_frame.show_vsx;
			} else if (WDS_CATALOG.equals(what)) {
				main_frame.show_wds            = ! main_frame.show_wds;
			} else if (WEBB_DOUBLE_STARS.equals(what)) {
				main_frame.show_webb_double_stars = ! main_frame.show_webb_double_stars;
			} else if (WEBB_PLANETARY_NEBULAE.equals(what)) {
				main_frame.show_webb_planetary_nebulae = ! main_frame.show_webb_planetary_nebulae;
			} else if (WEBB_GASEOUS_NEBULAE.equals(what)) {
				main_frame.show_webb_gaseous_nebulae = ! main_frame.show_webb_gaseous_nebulae;
			} else if (WEBB_OPEN_CLUSTERS.equals(what)) {
				main_frame.show_webb_open_clusters = ! main_frame.show_webb_open_clusters;
			} else if (WEBB_GLOBULAR_CLUSTERS.equals(what)) {
				main_frame.show_webb_globular_clusters = ! main_frame.show_webb_globular_clusters;
			} else if (WEBB_GALAXIES.equals(what)) {
				main_frame.show_webb_galaxies = ! main_frame.show_webb_galaxies;
			} else if (WEBB_CLUSTERS_OF_GALAXIES.equals(what)) {
				main_frame.show_webb_clusters_of_galaxies = ! main_frame.show_webb_clusters_of_galaxies;
			} else if (WEBB_ANONYMOUS_GALAXIES.equals(what)) {
				main_frame.show_webb_anonymous_galaxies = ! main_frame.show_webb_anonymous_galaxies;
			} else if (WEBB_SOUTHERN_SKY.equals(what)) {
				main_frame.show_webb_southern_sky = ! main_frame.show_webb_southern_sky;
			} else if (WEBB_VARIABLE_STARS.equals(what)) {
				main_frame.show_webb_variable_stars = ! main_frame.show_webb_variable_stars;
			} else if (FAVORITE_DSOS.equals(what)) {
				main_frame.show_favorite_dsos = ! main_frame.show_favorite_dsos;
			} else {
		    	System.out.printf("%s: %3d: else (%s)...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), what);
			}
			main_frame.paint(main_frame.getGraphics());
			main_frame.setCursor(old);
		}
	
	}
}
