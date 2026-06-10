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


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.jar.*;
import java.util.TimeZone;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import lib.astro.CameraCatalog;
import lib.astro.CelestialCalculations;
import lib.astro.CelestialCalculations.Equatorial;
import lib.astro.CelestialCalculations.Galactic;
import lib.astro.CelestialCalculations.RiseAndSetTime;
import lib.astro.CelestialCalculations.SolarLocation;
import lib.astro.EyepieceCatalog;
import lib.astro.OpticalTubeAssemblyCatalog;
import lib.astro.PracticalAstronomy;
import lib.astro.PracticalAstronomy.LunarLocation;
import lib.astro.PracticalAstronomy.LunarPhases;
import lib.astro.PracticalAstronomy.LunarRiseAndSetTime;
import lib.astro.ReducerCatalog;
import lib.cities.UsCitiesCatalog;
import lib.cities.WorldCitiesCatalog;
import lib.astro.PracticalAstronomy.ApproximatePlanetLocation;
import lib.sphere.Angle;
import lib.sphere.SiderealTime;
import lib.stars.catalog.ArpCatalog;
import lib.stars.catalog.ArpEntry;
import lib.stars.catalog.Bayer;
import lib.stars.catalog.BayerEntry;
import lib.stars.catalog.CaldwellCatalog;
import lib.stars.catalog.CaldwellEntry;
import lib.stars.catalog.DsoAlias;
import lib.stars.catalog.DsoAlias.Element;
import lib.stars.catalog.DsoFilter;
import lib.stars.catalog.DsoType;
import lib.stars.catalog.FavoriteDsoCatalog;
import lib.stars.catalog.Herschel2500;
import lib.stars.catalog.Herschel400;
import lib.stars.catalog.HerschelCatalog;
import lib.stars.catalog.HerschelEntry;
import lib.stars.catalog.IAUCatalog;
import lib.stars.catalog.IAUEntry;
import lib.stars.catalog.MessierCatalog;
import lib.stars.catalog.MessierEntry;
import lib.stars.catalog.NamedDsos;
import lib.stars.catalog.NgcIcCatalog;
import lib.stars.catalog.NgcIcEntry;
import lib.stars.catalog.SharplessCatalog;
import lib.stars.catalog.SharplessEntry;
import lib.stars.catalog.UgcCatalog;
import lib.stars.catalog.UgcEntry;
import lib.stars.catalog.VizierCatalog;
import lib.stars.catalog.VsxCatalog;
import lib.stars.catalog.VsxEntry;
import lib.stars.catalog.WdsCatalog;
import lib.stars.catalog.WdsEntry;
import lib.stars.catalog.WebbAnonymousGalaxies;
import lib.stars.catalog.WebbClustersOfGalaxies;
import lib.stars.catalog.WebbDoubleStars;
import lib.stars.catalog.WebbGalaxies;
import lib.stars.catalog.WebbGalaxiesEntry;
import lib.stars.catalog.WebbGaseousNebulae;
import lib.stars.catalog.WebbGaseousNebulaeEntry;
import lib.stars.catalog.WebbGlobularClusters;
import lib.stars.catalog.WebbGlobularClustersEntry;
import lib.stars.catalog.WebbOpenClusters;
import lib.stars.catalog.WebbOpenClustersEntry;
import lib.stars.catalog.WebbPlanetaryNebulae;
import lib.stars.catalog.WebbPlanetaryNebulaeEntry;
import lib.stars.catalog.WebbSouthernSky;
import lib.stars.catalog.WebbVariableStars;
import lib.stars.catalog.YaleBrightStarAscCatalog;
import lib.stars.catalog.YaleBrightStarAscEntry;
import lib.time.TimeOfDay;
import lib.util.DoubleRectangle;
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;


@SuppressWarnings("unused")
public class MainFrame extends Frame implements MouseListener, MouseMotionListener, WindowListener {
	private static final long serialVersionUID = 8844285282912490565L;

	private JPanel main_panel = new JPanel();
	
	public Graphics graphics = null;

	public boolean    use_current_time = true;
	public TimeKeeper time_keeper      = new TimeKeeper(1);
	public int        sleep_ms         = 100;

	public double min_ra_hrs  = -12;
	public double mid_ra_hrs  =   0;
	public double max_ra_hrs  = +12;
	public double min_sol_hrs = -12;
	public double max_sol_hrs = +12;
	public double min_dec_deg = -90;
	public double max_dec_deg = +90;

	public double midnight_hrs = 0.0;
	public double sunset_hrs   = min_sol_hrs + 6.0;
	public double sunrise_hrs  = max_sol_hrs - 6.0;
	public double twilight_pm  = sunset_hrs  + 18.0 / 15.0;
	public double twilight_am  = sunrise_hrs - 18.0 / 15.0;

	public double moonrise_hrs   = 0.0;
	public double moonset_hrs    = 0.0;
	public double lunar_fullness = 0.0;
	public long   new_moon_ms    = TimeOfDay.current_zulu();
	public long   waxing_moon_ms = TimeOfDay.current_zulu();
	public long   full_moon_ms   = TimeOfDay.current_zulu();
	public long   waning_moon_ms = TimeOfDay.current_zulu();

	public boolean midnight_centered    = false;
	public boolean show_time_markers    = true;
	public boolean show_zenith_marker   = true;
	public boolean show_horizon_lines   = true;
	public boolean show_horizon_plus_15 = false;
	public boolean show_horizon_plus_30 = false;
	public boolean show_ecliptic_plane  = true;
	public boolean show_galactic_plane  = true;

	public enum GridStyle { SOLAR_GRID, SIDEREAL_GRID, NO_GRID }
	public GridStyle grid_style = GridStyle.NO_GRID;

	public boolean show_constellations            = false;
	public boolean show_constellation_areas       = false;
	public boolean show_primary_stars             = false;
	public boolean show_named_stars               = true;
	public boolean show_yale_bright_stars         = true;
	public boolean show_sun_moon_planets          = true;
	public boolean show_caldwell                  = false;
	public boolean show_messier                   = false;
	public boolean show_sharpless                 = false;
	public boolean show_herschel                  = false;
	public boolean show_herschel_400              = false;
	public boolean show_herschel_2500             = false;
	public boolean show_arp                       = false;
	public boolean show_ic                        = false;
	public boolean show_ngc                       = false;
	public boolean show_ugc                       = false;
	public boolean show_vsx                       = false;
	public boolean show_wds                       = false;
	public boolean show_webb_double_stars         = false;
	public boolean show_webb_planetary_nebulae    = false;
	public boolean show_webb_gaseous_nebulae      = false;
	public boolean show_webb_open_clusters        = false;
	public boolean show_webb_globular_clusters    = false;
	public boolean show_webb_galaxies             = false;
	public boolean show_webb_clusters_of_galaxies = false;
	public boolean show_webb_anonymous_galaxies   = false;
	public boolean show_webb_southern_sky         = false;
	public boolean show_webb_variable_stars       = false;
	public boolean show_favorite_dsos             = false;
	public boolean show_zoom                      = false;
	public boolean show_legend                    = true;

	public double ra_maj_scale  =  1;				//  1 hour per tick
	public double dec_maj_scale = 15;				// 15 degrees per tick

	public double ra_min_scale  = 20.0/60.0;		// 20 minutes per tick
	public double dec_min_scale =  5;				//  5 degrees per tick

	public static Color background           = null; // new Color(0,0,0);
	public static Color foreground           = null; // Color.WHITE;
	public static Color text_color           = null; // Color.MAGENTA;
	public static Color major_line_color     = null; // Color.GRAY.darker().darker().darker().darker().darker();
	public static Color minor_line_color     = null; // Color.GRAY.darker().darker().darker().darker().darker().darker().darker();
	public static Color scale_text_color     = null; // Color.GRAY.darker();
	public static Color sunset_color         = null; // Color.ORANGE.darker().darker().darker();
	public static Color sunrise_color        = null; // sunset_color;
	public static Color twilight_color       = null; // Color.RED.darker();
	public static Color midnight_color       = null; // Color.BLUE.darker();
	public static Color current_time_color   = null; // Color.GREEN.darker().darker().darker();
	public static Color constellation_color  = null; // Color.CYAN.darker();
	public static Color new_moon_color       = null; // Color.CYAN.darker();
	public static Color waxing_moon_color    = null; // Color.CYAN.darker();
	public static Color full_moon_color      = null; // Color.CYAN.darker();
	public static Color waning_moon_color    = null; // Color.CYAN.darker();
	public static Color lunar_fullness_color = null; // Color.CYAN.darker();
	public static Color moonrise_color       = null; // Color.CYAN.darker();
	public static Color moonset_color        = null; // Color.CYAN.darker();
	public static Color sun_color            = null;
	public static Color gas_giant_color      = null;
	public static Color inner_planet_color   = null;
	public static Color mars_color           = null;
	public static Color zoom_color           = null;
	public static Color target_color         = null;

	public static Color caldwell_color       = null;
	public static Color herschel_color       = null;
	public static Color messier_color        = null;
	public static Color sharpless_color      = null;

	public static Color arp_color            = null;
	public static Color ic_color             = null;
	public static Color ngc_color            = null;
	public static Color ugc_color            = null;
	public static Color vsx_color            = null;
	public static Color wds_color            = null;

	public static Color double_stars_color         = null;
	public static Color planetary_nebulae_color    = null;
	public static Color gaseous_nebulae_color      = null;
	public static Color open_clusters_color        = null;
	public static Color globular_clusters_color    = null;
	public static Color galaxies_color             = null;
	public static Color clusters_of_galaxies_color = null;
	public static Color anonymous_galaxies_color   = null;
	public static Color southern_skies_color       = null;
	public static Color variable_stars_color       = null;

	public static Color cluster_color              = null;
	public static Color galaxy_color               = null;
	public static Color nebula_color               = null;

	public static Color horizon_line_color         = null;
	public static Color horizon_plus_15_color      = null;
	public static Color horizon_plus_30_color      = null;
	public static Color ecliptic_plane_color       = null;
	public static Color galactic_plane_color       = null;

	public Dimension       screen_size    = null;	// total screen size (w,h)
	public Insets          screen_insets  = null;	// insets from total screen area (top, left, bottom, right)
	public Dimension       usable_size    = null;	// usable screen size (w,h) in pixels
	public Rectangle       usable_area    = null;	// usable screen area (x,y,w,h) in pixels
	public Dimension       used_size      = null;	// portion of the screen (w,h) that is used
	public Rectangle       used_area      = null;	// area of the screen (x,y,w,h) that is used
	public Dimension       display_size   = null;	// portion of the screen (w,h) that is used
	public Rectangle       display_area   = null;	// area of the screen below the menu (x,y,w,h) that is used
	public Point           display_center = null;
	public Insets          display_insets = new Insets(1,7,8,8);			// border around chart area for coordinate scales (T,L,B,R)

	public DoubleRectangle chart_area_deg = null;	// chart area in degrees
	public Rectangle       chart_area     = null;	// chart area in pixels
	public Insets          chart_insets   = new Insets(100,100,100,100);	// insets ... (top, left, bottom, right)

	public static int      menu_height = 50;

	public Paint paint = new Paint_NWSE();

	public long         system_solar_time_ms   = System.currentTimeMillis();
	public long         current_solar_time_ms  = 0;
	public long         solar_time_offset_ms   = 0;
	public long         noon0_solar_time_ms    = Long.MIN_VALUE;
	public long         midnight_solar_time_ms = Long.MIN_VALUE;
	public long         noon1_solar_time_ms    = Long.MIN_VALUE;

	public long         current_sidereal_time_ms  = Long.MIN_VALUE;
	public long         noon0_sidereal_time_ms    = Long.MIN_VALUE;
	public long         midnight_sidereal_time_ms = Long.MIN_VALUE;
	public long         noon1_sidereal_time_ms    = Long.MIN_VALUE;

	public enum ScaleStyle { HOURS_ONLY, HOURS_MINUTES }
	public ScaleStyle scale_style = ScaleStyle.HOURS_ONLY;

	public DsoAlias.Element[][] dso_alias = null;

	public String[] data_dir = {
			System.getProperty("java.class.path") + "/data/nightsky/catalogs/",
			System.getProperty("user.dir") + "/data/nightsky/catalogs/",
			System.getProperty("user.home") + "/data/nightsky/catalogs/",

			System.getProperty("java.class.path") + "/data/night sky/catalogs/",
			System.getProperty("user.dir") + "/data/night sky/catalogs/",
			System.getProperty("user.home") + "/data/night sky/catalogs/",

			"C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/nightsky/catalogs/",
			"D:/dmpase/home/projects/org.hypercomputing/data/nightsky/catalogs/",
			"//magrathea/dsk/dmpase/home/projects/org.hypercomputing/data/nightsky/catalogs/",
	};

	public String iau_csn_name            = "IAU-CSN.txt";							// IAU Catalog of Star Names
	public String ybsc_name               = "bsc5.dat";								// Yale Bright Star Catalog
	public String caldwell_name           = "Caldwell Catalog.csv";					// Caldwell Catalog
	public String herschel400_name        = "Herschel 400 Catalog go.csv";			// Herschel 400 Catalog
	public String herschel2500_name       = "Herschel 2500 Catalog.csv";			// Herschel 2500 Catalog
	public String messier_name            = "Messier Catalog.txt";					// Messier Catalog
	public String named_dsos_name         = "Named-DSOs.csv";						// Named DSOs
	public String sharpless_name          = "sharpless.asu.csv";					// Sharpless Catalog
	public String arp_path                = "arpord.dat";							// Arp Catalog
	public String ngc_ic_path             = "NGC+IC-J2000.txt";						// NGC+IC-J2000/NGC+IC-J2000.txt
	public String ugc_path                = "ugc.dat.txt";							// Uppsala General Catalog
	public String vsx_path                = "aavso.vsx.10.tsv";						// Variable Star Index (VSX)
	public String wds_path                = "wds.summ_con.txt";						// Washington Double Star Catalog
	public String double_stars_path       = "WS DSO V1 - Double Stars.txt";
	public String planetary_nebulae_path  = "WS DSO V2 - Planetary Nebulae.txt";
	public String gaseous_nebulae_path    = "WS DSO V2 - Gaseous Nebulae.txt";
	public String open_clusters_path      = "WS DSO V3 - Open Clusters.txt";
	public String globular_clusters_path  = "WS DSO V3 - Globular Clusters.txt";
	public String galaxies_path           = "WS DSO V4 - Galaxies.txt";
	public String galaxy_clusters_path    = "WS DSO V5 - Clusters of Galaxies.txt";	// 
	public String anonymous_galaxies_path = "WS DSO V6 - Anonymous Galaxies.txt";
	public String southern_sky_path       = "WS DSO V7 - Southern Sky.txt";
	public String variable_stars_path     = "WS DSO V8 - Variable Stars.txt";
	public String favorite_dsos_path      = "Favorite DSOs.txt";

	public String us_cities_path          = "uscities.csv";
	public String world_cities_path       = "worldcities.csv";

	public String ota_path                = "ota.csv";
	public String reducer_path            = "reducer.csv";
	public String camera_path             = "camera.csv";
	public String eyepiece_path           = "eyepiece.csv";

	public enum Catalogs {SOLAR_SYSTEM, IAU_CSN, YALE_BSC, CALDWELL, HERSCHEL, MESSIER, 
		SHARPLESS, ARP, NGC, IC, UGC, VSX, WDS, GCVS,
		DOUBLE_STARS, PLANETARY_NEBULAE, GASEOUS_NEBULAE, OPEN_CLUSTERS, GLOBULAR_CLUSTERS, 
		GALAXIES, GALAXY_CLUSTERS, ANONYMOUS_GALAXIES, SOUTHERN_SKY, VARIABLE_STARS, FAVORITES,
		CUSTOM}

	public static final int SUN     = 0;
	public static final int MOON    = 1;
	public static final int MERCURY = 2;
	public static final int VENUS   = 3;
	public static final int MARS    = 4;
	public static final int JUPITER = 5;
	public static final int SATURN  = 6;
	public static final int URANUS  = 7;
	public static final int NEPTUNE = 8;
	public static final String[] solar_system_names = { "Sun", "Moon", "Mercury", "Venus", "Mars", 	"Jupiter", "Saturn", "Uranus", "Neptune", };
	// public NameRaDec[] solar_system = new NameRaDec[solar_system_names.length];

	public IAUCatalog               iau_csn             = null;		// catalog of named stars
	public YaleBrightStarAscCatalog ybsc                = null;		// stars down to magnitude 8
	public CaldwellCatalog          caldwell            = null;		// Caldwell catalog of 109 objects
	public Herschel400              herschel_400        = null;		// Herschel catalog of 400 objects
	public Herschel2500             herschel_2500       = null;		// Herschel catalog of 2500 objects
	public MessierCatalog           messier             = null;		// Messier objects
	public NamedDsos                named_dsos          = null;		// Named DSOs
	public SharplessCatalog         sharpless           = null;		// Sharpless objects
	public ArpCatalog               arp                 = null;		// Arp catalog of peculiar galaxies
	public NgcIcCatalog             ngc_ic              = null;		// NGC and IC catalogs
	public UgcCatalog               ugc                 = null;		// UGC catalog of galaxies
	public VsxCatalog               vsx                 = null;		// Variable Star Index Catalog (VSX)
	public WdsCatalog               wds                 = null;		// Washington Double Star Catalog (WDS)
	public WebbDoubleStars          double_stars        = null;
	public WebbPlanetaryNebulae     planetary_nebulae   = null;
	public WebbGaseousNebulae       gaseous_nebulae     = null;
	public WebbOpenClusters         open_clusters       = null;
	public WebbGlobularClusters     globular_clusters   = null;
	public WebbGalaxies             galaxies            = null;
	public WebbClustersOfGalaxies   galaxy_clusters     = null;
	public WebbAnonymousGalaxies    anonymous_galaxies  = null;
	public WebbSouthernSky          southern_sky        = null;
	public WebbVariableStars        webb_variable_stars = null;
	public FavoriteDsoCatalog       favorite_dsos       = null;

	public String                   vizier_db_name      = data_dir + "catalog.dat.txt";			// Vizier/catalog.dat.txt";
	public VizierCatalog            vizier  = null;

	public UsCitiesCatalog          us_cities          = null;
	public WorldCitiesCatalog       world_cities       = null;

	public OpticalTubeAssemblyCatalog otas             = null;
	public ReducerCatalog             reducers         = null;
	public CameraCatalog              cameras          = null;
	public EyepieceCatalog            eyepieces        = null;
	
	public DsoFilter filter          = null;
	public DsoAlias  dso_alias_table = null;

	public Element selected_dso = null;

	public Element angle_of_separation_dso_0 = null;
	public Element angle_of_separation_dso_1 = null;

	public final MainFrame mainframe;

	public final int max_dso_name_len = 75;

	public MainFrame() throws IOException
	{
    	super(NightSkyAtAGlance.title);
    	addNotify();

    	String path = null;
    	String[] alternate_paths = { 
			System.getProperty("java.class.path") + "/data/nightsky/catalogs/",
			System.getProperty("user.dir") + "/data/nightsky/catalogs/",
			System.getProperty("user.home") + "/data/nightsky/catalogs/",

			System.getProperty("java.class.path") + "/data/night sky/catalogs/",
			System.getProperty("user.dir") + "/data/night sky/catalogs/",
			System.getProperty("user.home") + "/data/night sky/catalogs/",

    		"/data/nightsky/catalogs/",
    		"D:/home/projects/org.hypercomputing/data/nightsky/catalogs/",
			"D:/dmpase/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"E:/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"//magrathea/dsk/dmpase/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    	};
    	for (String p: alternate_paths) {
    		File f = new File(p);
    		if (f.isDirectory()) {
    			path = p;
    			break;
    		}
    	}
    	if (path == null) {
    		path = "/data/nightsky/catalogs/";
    	}
    	// System.out.printf("%s: %d: path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), path);
    	LoadDatabases db = LoadDatabases.load(this, path);

    	// System.out.printf("%s: %d: path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), path);

    	// long start = System.currentTimeMillis();

    	otas               = new OpticalTubeAssemblyCatalog(path + ota_path);
    	reducers           = new ReducerCatalog            (path + reducer_path);
    	cameras            = new CameraCatalog             (path + camera_path);
    	eyepieces          = new EyepieceCatalog           (path + eyepiece_path);

    	mainframe = this;

    	make_menu_bar();
    	
    	reset_color();
    	
		addWindowListener     (this);
		addMouseListener      (this);
		addMouseMotionListener(this);
		addKeyListener        (new KeyboardEventHandler(this));
		addComponentListener  (new ComponentEventHandler(this));

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		screen_size = toolkit.getScreenSize();
		GraphicsConfiguration gc = getGraphicsConfiguration();
		screen_insets = toolkit.getScreenInsets(gc);
		
		usable_size    = new Dimension((screen_size.width - screen_insets.left - screen_insets.right), 
				(screen_size.height - screen_insets.top - screen_insets.bottom));
		usable_area    = new Rectangle(screen_insets.left, screen_insets.top, 
				usable_size.width - screen_insets.left - screen_insets.right, 
				usable_size.height - screen_insets.top - screen_insets.bottom);

		used_size      = usable_size;
		used_area      = new Rectangle(0, 0, used_size.width, used_size.height);

		display_area   = new Rectangle(0, menu_height, used_size.width, used_size.height - menu_height);
		display_center = new Point(display_area.x + display_area.width/2, display_area.y + display_area.height/2); 

		chart_area   = new Rectangle(
				display_area.x + display_insets.left + chart_insets.left, 
				display_area.y + display_insets.top + chart_insets.top, 
				display_area.width  - display_insets.left - display_insets.right - chart_insets.left - chart_insets.right, 
				display_area.height - display_insets.top  - display_insets.bottom - chart_insets.top - chart_insets.bottom);

		pack();
		
		add(main_panel);

		Point upper_left = new Point((usable_size.width - used_size.width)/2, (usable_size.height - used_size.height)/2);
		setLocation(upper_left);
		setSize(used_size);
		setVisible(true);

		display_size = getSize();
		
		time_keeper.start();

		// PersistentState.load_state();
		Font font = PersistentState.get_font();
		setFont(font);
		// FontMetrics fm = graphics.getFontMetrics(font);
		// PersistentState.menu_height = get_menu_height(fm);

		// System.out.printf("%s: %3d: screen  size  : %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), screen_size);
		// System.out.printf("%s: %3d: screen  insets: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), screen_insets);
		// System.out.printf("%s: %3d: usable  size  : %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), usable_size);
		// System.out.printf("%s: %3d: usable  area  : %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), usable_area);
		// System.out.printf("%s: %3d: display area  : %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), display_area);
		// System.out.printf("%s: %3d: chart   area  : %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), chart_area);
	}

	public static int get_menu_height(FontMetrics fm)
	{
		return 30 + fm.getHeight();
	}

	public void paint(Graphics g)
	{
		paint.paint(g);
	}
	
	public abstract class Paint {
		public final Point top_scale_offset      = new Point(-25,-10);
		public final Point left_scale_offset     = new Point(-55,  5); 
		public final Point right_scale_offset    = new Point( 10,  5);
		public final Point bottom_scale_offset   = new Point(-25,+25); 

		public final Point top_label_offset      = new Point(+15,-10);
		public final Point right_label_offset    = new Point(+60,+05);

		public String top_label            = null;
		public String left_label           = null;
		public String right_label          = null;
		public String bottom_label         = null;
		public String solar_label          = "Solar";
		public String sidereal_label       = "Sidereal";
		public String sunset_label         = "Sunset";
		public String twilight_PM_label    = "Twilight PM";
		public String twilight_AM_label    = "Twilight AM";
		public String sunrise_label        = "Sunrise";
		public String new_moon_label       = "New Moon";
		public String waxing_moon_label    = "First Quarter";
		public String full_moon_label      = "Full Moon";
		public String waning_moon_label    = "Third Quarter";
		public String lunar_fullness_label = "Illumination";
		public String moonrise_label       = "Moon Rise";
		public String moonset_label        = "Moon Set";

		public String sun_label            = "Sun";
		public String moon_label           = "Moon";
		public String mercury_label        = "Mercury";
		public String venus_label          = "Venus";
		public String mars_label           = "Mars";
		public String jupiter_label        = "Jupiter";
		public String saturn_label         = "Saturn";
		public String uranus_label         = "Uranus";
		public String neptune_label        = "Neptune";

		public String current_latitude_label  = "Latitude";
		public String current_longitude_label = "Longitude";

		public final String degree_symbol = new String(Character.toChars(0x00B0));

		public abstract Point ra_dec_to_xy(double ra, double dec);
		public abstract Point solar_dec_to_xy(double hrs, double dec);
		public abstract int hrs_to_width(double hrs);
		public abstract int deg_to_width(double deg);
		public abstract double x_to_ra(int x);
		public abstract double y_to_dec(int y);
		public abstract double x_to_solar_hrs(int x);

		public void paint(Graphics g) 
		{
			graphics = g;

			{	// keep the screen alive
				Point mouse_pointer = MouseInfo.getPointerInfo().getLocation();
				Robot robot;
				try {
					robot = new Robot();
					robot.mouseMove(mouse_pointer.x, mouse_pointer.y+1);
					robot.mouseMove(mouse_pointer.x, mouse_pointer.y);
				} catch (AWTException e) {
					;
				}
			}

			current_solar_time_ms = system_solar_time_ms + solar_time_offset_ms;

			menu_height = PersistentState.menu_height;

			FontMetrics font_metrics = g.getFontMetrics();
			int font_height   = font_metrics.getHeight();
			int top_border    = 0;
			int top_margin    = (int) (top_border + PersistentState.chart_margin);	// TODO TOP MARGIN
			int top           = top_margin + menu_height + 4 * font_height;

			int bottom_border = 20;
			int bottom_margin = (int) (bottom_border + 1.5 * font_height);
			int bottom        = bottom_margin + 3 * font_height;

			int sidereal_width = font_metrics.stringWidth("Sidereal");
			int east_width     = font_metrics.stringWidth("East");
			int solar_width    = font_metrics.stringWidth("Solar");
			int west_width     = font_metrics.stringWidth("West");
			int north_width    = font_metrics.stringWidth("North");
			int south_width    = font_metrics.stringWidth("South");
			int text_width     = Math.max(Math.max(sidereal_width, solar_width), Math.max(Math.max(east_width, west_width), Math.max(north_width, south_width)));

			int dec_width      = font_metrics.stringWidth("+000");
			int left_border    = 20;
			int left           = left_border + dec_width + text_width;
			int right_border   = 20;
			int right          = right_border + dec_width + text_width;

			chart_insets = new Insets(top, left, bottom, right);
			// System.out.printf("%s: %3d: chart insets: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), chart_insets);

			display_size = getSize();
			display_area = new Rectangle(0, menu_height, display_size.width, display_size.height - menu_height);

			chart_area   = new Rectangle(
					display_area.x      + display_insets.left + chart_insets.left, 
					display_area.y      + display_insets.top  + chart_insets.top, 
					display_area.width  - display_insets.left - display_insets.right  - chart_insets.left - chart_insets.right, 
					display_area.height - display_insets.top  - display_insets.bottom - chart_insets.top  - chart_insets.bottom);

			Image img  = createImage(display_size.width, display_size.height);
			if (img == null) System.exit(0);
			Graphics h = img.getGraphics();

			// color the entire window background
			h.setColor(background);
			h.fillRect(display_area.x, display_area.y, display_area.width, display_area.height);

			DsoAlias.Element[][] dso_alias_buf = new DsoAlias.Element[chart_area.width][chart_area.height];

			Angle longitude = new Angle(PersistentState.true_longitude_deg, Angle.Scale.DEGREES);

	    	// compute the solar limits
	    	Calendar current_time_zone = Calendar.getInstance(PersistentState.timezone);
	    	current_time_zone.setTimeInMillis(current_solar_time_ms);
	    	int current_solar_hour   = current_time_zone.get(Calendar.HOUR_OF_DAY);
	    	int current_solar_minute = current_time_zone.get(Calendar.MINUTE);
	    	int current_solar_second = current_time_zone.get(Calendar.SECOND);
	    	current_sidereal_time_ms = SiderealTime.get_local_sidereal_ms(current_solar_time_ms,  longitude);
	    	double current_decimal_hrs = current_solar_hour + current_solar_minute / 60.0 + current_solar_second / 3600.0;

	    	Calendar local_noon0_zone    = Calendar.getInstance(PersistentState.timezone);
	    	Calendar local_midnight_zone = Calendar.getInstance(PersistentState.timezone);
	    	Calendar local_noon1_zone    = Calendar.getInstance(PersistentState.timezone);

	    	local_noon0_zone   .setTimeInMillis(current_solar_time_ms);
	    	local_midnight_zone.setTimeInMillis(current_solar_time_ms);
	    	local_noon1_zone   .setTimeInMillis(current_solar_time_ms);

	    	if (12 <= local_midnight_zone.get(Calendar.HOUR_OF_DAY)) {
	    		local_noon0_zone   .set(Calendar.DAY_OF_MONTH, local_noon0_zone   .get(Calendar.DAY_OF_MONTH)+0);
	    		local_midnight_zone.set(Calendar.DAY_OF_MONTH, local_midnight_zone.get(Calendar.DAY_OF_MONTH)+1);
	    		local_noon1_zone   .set(Calendar.DAY_OF_MONTH, local_noon1_zone   .get(Calendar.DAY_OF_MONTH)+1);
			} else {
	    		local_noon0_zone   .set(Calendar.DAY_OF_MONTH, local_noon0_zone   .get(Calendar.DAY_OF_MONTH)-1);
	    		local_midnight_zone.set(Calendar.DAY_OF_MONTH, local_midnight_zone.get(Calendar.DAY_OF_MONTH)+0);
	    		local_noon1_zone   .set(Calendar.DAY_OF_MONTH, local_noon1_zone   .get(Calendar.DAY_OF_MONTH)+0);
			}

	    	local_noon0_zone.set(Calendar.HOUR_OF_DAY, 12);
    		local_noon0_zone.set(Calendar.MINUTE,       0);
    		local_noon0_zone.set(Calendar.SECOND,       0);
    		local_noon0_zone.set(Calendar.MILLISECOND,  0);
    		noon0_solar_time_ms    = local_noon0_zone   .getTimeInMillis();
	    	noon0_sidereal_time_ms = SiderealTime.get_local_sidereal_ms(noon0_solar_time_ms,    longitude);

    		local_midnight_zone.set(Calendar.HOUR_OF_DAY, 0);
    		local_midnight_zone.set(Calendar.MINUTE,      0);
    		local_midnight_zone.set(Calendar.SECOND,      0);
    		local_midnight_zone.set(Calendar.MILLISECOND, 0);
	    	midnight_solar_time_ms    = local_midnight_zone.getTimeInMillis();
	    	midnight_sidereal_time_ms = SiderealTime.get_local_sidereal_ms(midnight_solar_time_ms, longitude);

    		local_noon1_zone.set(Calendar.HOUR_OF_DAY, 12);
    		local_noon1_zone.set(Calendar.MINUTE,       0);
    		local_noon1_zone.set(Calendar.SECOND,       0);
    		local_noon1_zone.set(Calendar.MILLISECOND,  0);
    		noon1_solar_time_ms    = local_noon1_zone   .getTimeInMillis();
	    	noon1_sidereal_time_ms = SiderealTime.get_local_sidereal_ms(noon1_solar_time_ms,    longitude);

    		if (midnight_centered) {
	    		min_sol_hrs = -12.0;
		    	max_sol_hrs = +12.0;
		    	current_decimal_hrs = (max_sol_hrs < current_decimal_hrs) ? current_decimal_hrs - 24 : current_decimal_hrs;
		    	current_decimal_hrs = (current_decimal_hrs < min_sol_hrs) ? current_decimal_hrs + 24 : current_decimal_hrs;
    		} else {
	    		min_sol_hrs = current_decimal_hrs - 12.0;
		    	max_sol_hrs = current_decimal_hrs + 12.0;
    		}

	    	// compute the sidereal limits
    		if (midnight_centered) {
		    	min_ra_hrs = (double) noon0_sidereal_time_ms    / (double) SiderealTime.ms_per_hour;
		    	mid_ra_hrs = (double) midnight_sidereal_time_ms / (double) SiderealTime.ms_per_hour;
		    	max_ra_hrs = (double) noon1_sidereal_time_ms    / (double) SiderealTime.ms_per_hour;
    		} else {
		    	mid_ra_hrs = (double) current_sidereal_time_ms  / (double) SiderealTime.ms_per_hour;
		    	min_ra_hrs = mid_ra_hrs - 12;
		    	max_ra_hrs = mid_ra_hrs + 12;
    		}
	    	
	    	min_ra_hrs -= (max_ra_hrs < (min_ra_hrs + 24)) ? 24 : 0;
	    	mid_ra_hrs -= (max_ra_hrs < (mid_ra_hrs + 24)) ? 24 : 0;


	    	// compute sunrise, sunset, and twilight hours
			SolarLocation solar_location = new SolarLocation(current_solar_time_ms, PersistentState.timezone);
			RiseAndSetTime solar_rise_and_set = new RiseAndSetTime(solar_location.right_ascension, 
					solar_location.declination, PersistentState.true_latitude_deg, PersistentState.true_longitude_deg, 
					current_solar_time_ms, PersistentState.timezone);

			double solar_ra = PracticalAstronomy.adjust24(solar_location.right_ascension);
			double solar_de = PracticalAstronomy.adjust90(solar_location.declination);
	    	String solar_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(solar_ra), PracticalAstronomy.minute_of_decimal_hours(solar_ra),
	    			(solar_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(solar_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(solar_de)));
	    	
			LunarLocation       lunar_location     = new LunarLocation(current_solar_time_ms, PersistentState.timezone);
			LunarRiseAndSetTime lunar_rise_and_set = new LunarRiseAndSetTime(PersistentState.true_latitude_deg, PersistentState.true_longitude_deg, current_solar_time_ms, PersistentState.timezone);
			LunarPhases         lunar_phases       = new LunarPhases(current_solar_time_ms, PersistentState.timezone);

			double lunar_ra = PracticalAstronomy.adjust24(lunar_location.right_ascension);
			double lunar_de = PracticalAstronomy.adjust90(lunar_location.declination);
	    	String lunar_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(lunar_ra), PracticalAstronomy.minute_of_decimal_hours(lunar_ra),
	    			(lunar_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(lunar_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(lunar_de)));

			lunar_fullness = lunar_location.illumination;
			
			Calendar c = Calendar.getInstance(PersistentState.timezone);
			c.setTimeInMillis(lunar_phases.new_moon);
			String new_moon_date_value    = String.format("%02d / %02d / %4d", c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
			c.setTimeInMillis(lunar_phases.first_quarter);
			String waxing_moon_date_value = String.format("%02d / %02d / %4d", c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
			c.setTimeInMillis(lunar_phases.full_moon);
			String full_moon_date_value   = String.format("%02d / %02d / %4d", c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
			c.setTimeInMillis(lunar_phases.third_quarter);
			String waning_moon_date_value = String.format("%02d / %02d / %4d", c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));

			ApproximatePlanetLocation planets = new ApproximatePlanetLocation(current_solar_time_ms, PersistentState.timezone);
			double mercury_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.MERCURY].right_ascension);
			double mercury_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.MERCURY].declination);
	    	String mercury_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(mercury_ra), PracticalAstronomy.minute_of_decimal_hours(mercury_ra),
	    			(mercury_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(mercury_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(mercury_de)));

			double venus_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.VENUS].right_ascension);
			double venus_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.VENUS].declination);
	    	String venus_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(venus_ra), PracticalAstronomy.minute_of_decimal_hours(venus_ra),
	    			(venus_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(venus_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(venus_de)));

			double mars_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.MARS].right_ascension);
			double mars_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.MARS].declination);
	    	String mars_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(mars_ra), PracticalAstronomy.minute_of_decimal_hours(mars_ra),
	    			(mars_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(mars_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(mars_de)));

			double jupiter_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.JUPITER].right_ascension);
			double jupiter_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.JUPITER].declination);
	    	String jupiter_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(jupiter_ra), PracticalAstronomy.minute_of_decimal_hours(jupiter_ra),
	    			(jupiter_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(jupiter_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(jupiter_de)));

			double saturn_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.SATURN].right_ascension);
			double saturn_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.SATURN].declination);
	    	String saturn_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(saturn_ra), PracticalAstronomy.minute_of_decimal_hours(saturn_ra),
	    			(saturn_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(saturn_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(saturn_de)));

			double uranus_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.URANUS].right_ascension);
			double uranus_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.URANUS].declination);
	    	String uranus_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(uranus_ra), PracticalAstronomy.minute_of_decimal_hours(uranus_ra),
	    			(uranus_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(uranus_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(uranus_de)));

			double neptune_ra = PracticalAstronomy.adjust24(planets.locations[ApproximatePlanetLocation.NEPTUNE].right_ascension);
			double neptune_de = PracticalAstronomy.adjust90(planets.locations[ApproximatePlanetLocation.NEPTUNE].declination);
	    	String neptune_value = String.format("%02dh %02dm, %s%02d%s %02d'", 
	    			PracticalAstronomy.hour_of_decimal_hours(neptune_ra), PracticalAstronomy.minute_of_decimal_hours(neptune_ra),
	    			(neptune_de < 0) ? "-" : "+", PracticalAstronomy.degree_of_decimal_degrees(Math.abs(neptune_de)), PracticalAstronomy.degree_symbol, PracticalAstronomy.minute_of_decimal_degrees(Math.abs(neptune_de)));

	    	if (dso_alias_table != null) {
		    	dso_alias_table.update("Sun",     solar_ra,   solar_de);
		    	dso_alias_table.update("Moon",    lunar_ra,   lunar_de);
		    	dso_alias_table.update("Mercury", mercury_ra, mercury_de);
		    	dso_alias_table.update("Venus",   venus_ra,   venus_de);
		    	dso_alias_table.update("Mars",    mars_ra,    mars_de);
		    	dso_alias_table.update("Jupiter", jupiter_ra, jupiter_de);
		    	dso_alias_table.update("Saturn",  saturn_ra,  saturn_de);
		    	dso_alias_table.update("Uranus",  uranus_ra,  uranus_de);
		    	dso_alias_table.update("Neptune", neptune_ra, neptune_de);
	    	}

	    	sunset_hrs    = solar_rise_and_set.local_set_time;
	    	sunset_hrs   += (sunset_hrs  < min_sol_hrs) ? 24 : 0;
	    	sunset_hrs   -= (max_sol_hrs < sunset_hrs ) ? 24 : 0;

	    	sunrise_hrs   = solar_rise_and_set.local_rise_time;
	    	sunrise_hrs  += (sunrise_hrs < min_sol_hrs) ? 24 : 0;
	    	sunrise_hrs  -= (max_sol_hrs < sunrise_hrs) ? 24 : 0;

	    	twilight_pm   = sunset_hrs  + 18.0 / 15.0;
	    	twilight_pm  += (twilight_pm < min_sol_hrs) ? 24 : 0;
	    	twilight_pm  -= (max_sol_hrs < twilight_pm) ? 24 : 0;

	    	midnight_hrs += (midnight_hrs < min_sol_hrs) ? 24 : 0;
	    	midnight_hrs -= (max_sol_hrs < midnight_hrs) ? 24 : 0;

	    	twilight_am   = sunrise_hrs - 18.0 / 15.0;
	    	twilight_am  += (twilight_am < min_sol_hrs) ? 24 : 0;
	    	twilight_am  -= (max_sol_hrs < twilight_am) ? 24 : 0;

	    	moonrise_hrs = lunar_rise_and_set.local_rise_time;
	    	moonrise_hrs += (moonrise_hrs < min_sol_hrs) ? 24 : 0;
	    	moonrise_hrs -= (max_sol_hrs < moonrise_hrs) ? 24 : 0;

	    	moonset_hrs = lunar_rise_and_set.local_set_time;
	    	moonset_hrs += (moonset_hrs < min_sol_hrs) ? 24 : 0;
	    	moonset_hrs -= (max_sol_hrs < moonset_hrs) ? 24 : 0;
	    	
	    	// TODO
			h.setColor(text_color);
	    	Rectangle top_box      = h.getFontMetrics().getStringBounds(top_label,      h).getBounds();
	    	Rectangle right_box    = h.getFontMetrics().getStringBounds(right_label,    h).getBounds();
	    	Rectangle bottom_box   = h.getFontMetrics().getStringBounds(bottom_label,   h).getBounds();
	    	
	    	int start_nws = display_size.width - Math.max(top_box.width, Math.max(right_box.width, bottom_box.width)) - 25;

			Point pt = ra_dec_to_xy(min_ra_hrs, max_dec_deg);
			Point pr = ra_dec_to_xy(min_ra_hrs, (min_dec_deg+max_dec_deg)/2);
			Point pb = ra_dec_to_xy(min_ra_hrs, min_dec_deg);

			h.drawString(top_label,    start_nws, pt.y + top_label_offset.y);
			h.drawString(right_label,  start_nws, pr.y + right_label_offset.y);
			h.drawString(bottom_label, start_nws, pb.y + bottom_scale_offset.y);

	    	Rectangle sidereal_box = h.getFontMetrics().getStringBounds(sidereal_label, h).getBounds();
	    	Rectangle left_box     = h.getFontMetrics().getStringBounds(left_label,     h).getBounds();
	    	Rectangle solar_box    = h.getFontMetrics().getStringBounds(solar_label,    h).getBounds();

	    	int start_ses = 25 + 0*Math.max(solar_box.width, Math.max(left_box.width, sidereal_box.width));

			h.drawString(sidereal_label, start_ses, pt.y + top_label_offset.y);
			h.drawString(left_label,     start_ses, pr.y + right_label_offset.y);
			h.drawString(solar_label,    start_ses, pb.y + bottom_scale_offset.y);

			
			Point frame_max = solar_dec_to_xy(min_sol_hrs, min_dec_deg);
			Point frame_min = solar_dec_to_xy(max_sol_hrs, max_dec_deg);

			// draw the vertical chart labels on the left and right (de_B1950) sides
	    	double mind, maxd, spad;
			mind = (int) (min_dec_deg/dec_maj_scale);
			maxd = (int) Math.ceil(max_dec_deg/dec_maj_scale);
			spad = maxd - mind;
			h.setColor(scale_text_color);
			for (int i=0; i <= spad; i++) {
				double dec = (i + mind) * dec_maj_scale;
				
				int xl = chart_area.x;
				int xr = chart_area.x + chart_area.width;
				int y  = (int) (chart_area.y + chart_area.height - i * chart_area.height / spad);

				if (scale_style == ScaleStyle.HOURS_ONLY) {
					String str = ((dec < 0) ? String.format("%03.0f", dec) : String.format("+%02.0f", dec)) + degree_symbol;
					h.drawString(str, xl + left_scale_offset .x + 20, y + left_scale_offset .y);
					h.drawString(str, xr + right_scale_offset.x     , y + right_scale_offset.y);
				} else if (scale_style == ScaleStyle.HOURS_MINUTES) {
					String str = ((dec < 0) ? String.format("%03.0f", dec) : String.format("+%02.0f", dec)) + degree_symbol + String.format(" %02d'", dec_minutes(dec));
					h.drawString(str, xl + left_scale_offset .x, y + left_scale_offset .y);
					h.drawString(str, xr + right_scale_offset.x, y + right_scale_offset.y);
				}
			}

			// draw the right ascension chart labels
			// top is sidereal time, bottom is solar time
			double minr, maxr, spar;
			minr = (int) (min_ra_hrs/ra_maj_scale);
			maxr = (int) (max_ra_hrs/ra_maj_scale);
			spar = maxr - minr;
			h.setColor(scale_text_color);
			for (int i=0; i <= spar; i++) {
				double hrs = (i + minr) * ra_maj_scale;
				Point t = ra_dec_to_xy(hrs, max_dec_deg);
				
				if (t.x < frame_min.x || frame_max.x < t.x) continue;

				if (scale_style == ScaleStyle.HOURS_ONLY) {
					String sh = String.format("%02d", hours(hrs));
					h.drawString(sh,  t.x + top_scale_offset.x + 15, t.y + top_scale_offset.y);
					h.drawString("h", t.x + top_scale_offset.x + 30, t.y + top_scale_offset.y - 5);
				} else if (scale_style == ScaleStyle.HOURS_MINUTES) {
					String sh = String.format("%02d    %02d", hours(hrs), ra_minutes(hrs));
					h.drawString(sh,  t.x + top_scale_offset.x,      t.y + top_scale_offset.y);
					h.drawString("h", t.x + top_scale_offset.x + 15, t.y + top_scale_offset.y - 5);
					h.drawString("m", t.x + top_scale_offset.x + 41, t.y + top_scale_offset.y - 5);
				}
			}

			// draw the solar time chart labels
			double mins, maxs, spas;
			mins = (int) (min_sol_hrs/ra_maj_scale);
			maxs = (int) (max_sol_hrs/ra_maj_scale);
			spas = maxs - mins;
			h.setColor(scale_text_color);
			for (int i=0; i <= spas; i++) {
				double hrs = (i + mins) * ra_maj_scale;
				
				int hour   = (int) hrs;
				int minute = (int) (hrs * 60) % 60;

		    	Calendar local_time_zone = Calendar.getInstance(PersistentState.timezone);

		    	// is midnight today or tomorrow?
		    	if (12 <= local_time_zone.get(Calendar.HOUR_OF_DAY)) {
		    		local_time_zone.set(Calendar.DAY_OF_MONTH, local_time_zone.get(Calendar.DAY_OF_MONTH)+1);
				}
		    	local_time_zone.set(Calendar.HOUR_OF_DAY, hour);
		    	local_time_zone.set(Calendar.MINUTE,      minute);
		    	local_time_zone.set(Calendar.SECOND,      0);
		    	local_time_zone.set(Calendar.MILLISECOND, 0);

		    	// solar_dec_to_xy(gmt_solar_time_in_millis, min_dec_deg, longitude);
		    	Point t = solar_dec_to_xy(hrs, min_dec_deg);
				
				if (t.x < frame_min.x || frame_max.x < t.x) continue;

				if (scale_style == ScaleStyle.HOURS_ONLY) {
					String sh = String.format("%02d", hours(hrs));
					h.drawString(sh,  t.x + bottom_scale_offset.x + 15, t.y + bottom_scale_offset.y);
					h.drawString("h", t.x + bottom_scale_offset.x + 30, t.y + bottom_scale_offset.y - 5);
				} else if (scale_style == ScaleStyle.HOURS_MINUTES) {
					String sh = String.format("%02d    %02d", hours(hrs), ra_minutes(hrs));
					h.drawString(sh,  t.x + bottom_scale_offset.x,      t.y + bottom_scale_offset.y);
					h.drawString("h", t.x + bottom_scale_offset.x + 15, t.y + bottom_scale_offset.y - 5);
					h.drawString("m", t.x + bottom_scale_offset.x + 41, t.y + bottom_scale_offset.y - 5);
				}
			}

			if (grid_style != GridStyle.NO_GRID) {
				// draw the left and right (de_B1950) chart minor lines (horizontal)
				mind = (int) (min_dec_deg/dec_min_scale);
				maxd = (int) Math.ceil(max_dec_deg/dec_min_scale);
				spad = maxd - mind;
				h.setColor(minor_line_color);
				for (int i=0; i <= spad; i++) {
					int xr = (int) (chart_area.x + chart_area.width);
					int y  = (int) (chart_area.y + chart_area.height - i * chart_area.height / spad);
					
					h.drawLine(chart_area.x, y, xr, y);
				}
	
				// draw the left and right (de_B1950) chart major lines (horizontal)
				mind = (int) (min_dec_deg/dec_maj_scale);
				maxd = (int) Math.ceil(max_dec_deg/dec_maj_scale);
				spad = maxd - mind;
				h.setColor(major_line_color);
				for (int i=0; i <= spad; i++) {
					int xr = chart_area.x + chart_area.width;
					int y  = (int) (chart_area.y + chart_area.height - i * chart_area.height / spad);
					
					h.drawLine(chart_area.x, y, xr, y);
				}
	
				// draw the right ascension chart minor lines (vertical)
				if (grid_style == GridStyle.SOLAR_GRID) {
					minr = (int) (min_sol_hrs/ra_min_scale);
					maxr = (int) (max_sol_hrs/ra_min_scale);
				} else {
					minr = (int) (min_ra_hrs/ra_min_scale);
					maxr = (int) (max_ra_hrs/ra_min_scale);
				}
				spar = maxr - minr;
				h.setColor(minor_line_color);
				for (int i=0; i <= spar; i++) {
					double hrs = (i + minr) * ra_min_scale;
					Point t = null;
					Point b = null;
					if (grid_style == GridStyle.SOLAR_GRID) {
						t = solar_dec_to_xy(hrs, max_dec_deg);
						b = solar_dec_to_xy(hrs, min_dec_deg);
					} else {
						t = ra_dec_to_xy(hrs, max_dec_deg);
						b = ra_dec_to_xy(hrs, min_dec_deg);
					}

					if (chart_area.x <= t.x && t.x <= chart_area.x + chart_area.width && chart_area.x <= b.x && b.x <= chart_area.x + chart_area.width) {
						h.drawLine(t.x, t.y, b.x, b.y);
					}
				}
	
				// draw the right ascension chart major lines
				if (grid_style == GridStyle.SOLAR_GRID) {
					minr = (int) (min_sol_hrs/ra_maj_scale);
					maxr = (int) (max_sol_hrs/ra_maj_scale);
				} else {
					minr = (int) (min_ra_hrs/ra_maj_scale);
					maxr = (int) (max_ra_hrs/ra_maj_scale);
				}
				spar = maxr - minr;
				h.setColor(major_line_color);
				for (int i=0; i <= spar; i++) {
					double hrs = (i + minr) * ra_maj_scale;
					Point t = null;
					Point b = null;
					if (grid_style == GridStyle.SOLAR_GRID) {
						t = solar_dec_to_xy(hrs, max_dec_deg);
						b = solar_dec_to_xy(hrs, min_dec_deg);
					} else {
						t = ra_dec_to_xy(hrs, max_dec_deg);
						b = ra_dec_to_xy(hrs, min_dec_deg);
					}
					if (chart_area.x <= t.x && t.x <= chart_area.x + chart_area.width && chart_area.x <= b.x && b.x <= chart_area.x + chart_area.width) {
						h.drawLine(t.x, t.y, b.x, b.y);
					}
				}
			}

			// draw the outer frame
			h.setColor(major_line_color);
			h.drawRect(frame_min.x, frame_min.y, (frame_max.x - frame_min.x - 1), (frame_max.y - frame_min.y - 1));
			// System.out.printf("min=(%d,%d) max=(%d,%d)%n", fn.x, fn.y, fx.x, fx.y);

			if (show_time_markers) {
				double short_bar_len = 2;
				double long_bar_len  = 4;
				// draw the midnight line
				midnight_hrs = (midnight_hrs < min_sol_hrs) ? midnight_hrs + 24 : midnight_hrs;
				midnight_hrs = (max_sol_hrs < midnight_hrs) ? midnight_hrs - 24 : midnight_hrs;
				Point mx  = solar_dec_to_xy(midnight_hrs, max_dec_deg);
				Point mx5 = solar_dec_to_xy(midnight_hrs, max_dec_deg - short_bar_len);
				Point mn  = solar_dec_to_xy(midnight_hrs, min_dec_deg);
				Point mn5 = solar_dec_to_xy(midnight_hrs, min_dec_deg + short_bar_len);
				h.setColor(midnight_color);
				drawLine(h, chart_area, mx.x, mx.y, mx5.x, mx5.y);
				drawLine(h, chart_area, mn.x, mn.y, mn5.x, mn5.y);
	
				// draw the sunset line
				Point ssx  = solar_dec_to_xy(sunset_hrs, max_dec_deg);
				Point ssx5 = solar_dec_to_xy(sunset_hrs, max_dec_deg - short_bar_len);
				Point ssn  = solar_dec_to_xy(sunset_hrs, min_dec_deg);
				Point ssn5 = solar_dec_to_xy(sunset_hrs, min_dec_deg + short_bar_len);
				h.setColor(sunset_color);
				drawLine(h, chart_area, ssx.x, ssx.y, ssx5.x, ssx5.y);
				drawLine(h, chart_area, ssn.x, ssn.y, ssn5.x, ssn5.y);

				// draw the sunrise line
				Point srx  = solar_dec_to_xy(sunrise_hrs, max_dec_deg);
				Point srx5 = solar_dec_to_xy(sunrise_hrs, max_dec_deg - short_bar_len);
				Point srn  = solar_dec_to_xy(sunrise_hrs, min_dec_deg);
				Point srn5 = solar_dec_to_xy(sunrise_hrs, min_dec_deg + short_bar_len);
				h.setColor(sunrise_color);
				drawLine(h, chart_area, srx.x, srx.y, srx5.x, srx5.y);
				drawLine(h, chart_area, srn.x, srn.y, srn5.x, srn5.y);
	
				// draw the evening astronomical twilight line
				Point tpx  = solar_dec_to_xy(twilight_pm, max_dec_deg);
				Point tpx5 = solar_dec_to_xy(twilight_pm, max_dec_deg - short_bar_len);
				Point tpn  = solar_dec_to_xy(twilight_pm, min_dec_deg);
				Point tpn5 = solar_dec_to_xy(twilight_pm, min_dec_deg + short_bar_len);
				h.setColor(twilight_color);
				drawLine(h, chart_area, tpx.x, tpx.y, tpx5.x, tpx5.y);
				drawLine(h, chart_area, tpn.x, tpn.y, tpn5.x, tpn5.y);
	
				// draw the morning astronomical twilight line
				Point tax  = solar_dec_to_xy(twilight_am, max_dec_deg);
				Point tax5 = solar_dec_to_xy(twilight_am, max_dec_deg - short_bar_len);
				Point tan  = solar_dec_to_xy(twilight_am, min_dec_deg);
				Point tan5 = solar_dec_to_xy(twilight_am, min_dec_deg + short_bar_len);
				drawLine(h, chart_area, tax.x, tax.y, tax5.x, tax5.y);
				drawLine(h, chart_area, tan.x, tan.y, tan5.x, tan5.y);
	
				// draw the current time line
		    	Point ctx  = solar_dec_to_xy(current_decimal_hrs, max_dec_deg);
		    	Point ctx5 = solar_dec_to_xy(current_decimal_hrs, max_dec_deg - long_bar_len);
				Point ctn  = solar_dec_to_xy(current_decimal_hrs, min_dec_deg);
				Point ctn5 = solar_dec_to_xy(current_decimal_hrs, min_dec_deg + long_bar_len);
				h.setColor(current_time_color);
				drawLine(h, chart_area, ctx.x, ctx.y, ctx5.x, ctx5.y);
				drawLine(h, chart_area, ctn.x, ctn.y, ctn5.x, ctn5.y);
				// System.out.println("min="+min_sol_hrs + " cur="+current_hrs + " max="+max_sol_hrs + " ctx="+ctx+" ctn="+ctn);
				// System.out.println("chart="+chart_area+" display="+display_area);

				// draw the moon rise line
				Point mrx  = solar_dec_to_xy(moonrise_hrs, max_dec_deg);
				Point mrx5 = solar_dec_to_xy(moonrise_hrs, max_dec_deg - short_bar_len);
				Point mrn  = solar_dec_to_xy(moonrise_hrs, min_dec_deg);
				Point mrn5 = solar_dec_to_xy(moonrise_hrs, min_dec_deg + short_bar_len);
				h.setColor(moonrise_color);
				drawLine(h, chart_area, mrx.x, mrx.y, mrx5.x, mrx5.y);
				drawLine(h, chart_area, mrn.x, mrn.y, mrn5.x, mrn5.y);
				
				// draw the moon set line
				Point msx  = solar_dec_to_xy(moonset_hrs, max_dec_deg);
				Point msx5 = solar_dec_to_xy(moonset_hrs, max_dec_deg - short_bar_len);
				Point msn  = solar_dec_to_xy(moonset_hrs, min_dec_deg);
				Point msn5 = solar_dec_to_xy(moonset_hrs, min_dec_deg + short_bar_len);
				h.setColor(moonset_color);
				drawLine(h, chart_area, msx.x, msx.y, msx5.x, msx5.y);
				drawLine(h, chart_area, msn.x, msn.y, msn5.x, msn5.y);
			}

	    	Point top_left = solar_dec_to_xy(min_sol_hrs, max_dec_deg);
			h.setColor(current_time_color);

			double current_solar_time = CelestialCalculations.hms_to_decimal_hours(current_solar_hour, current_solar_minute, current_solar_second);
	    	String current_solar_time_value = CelestialCalculations.decimal_hours_to_string(current_solar_time);

	    	int current_sidereal_hour   = (int) ( current_sidereal_time_ms / SiderealTime.ms_per_hour     );
			int current_sidereal_minute = (int) ((current_sidereal_time_ms / SiderealTime.ms_per_min) % 60);
			int current_sidereal_second = (int) ((current_sidereal_time_ms / SiderealTime.ms_per_sec) % 60);
			double current_sidereal_time = CelestialCalculations.hms_to_decimal_hours(current_sidereal_hour, current_sidereal_minute, current_sidereal_second);
	    	String current_sidereal_time_value = CelestialCalculations.decimal_hours_to_string(current_sidereal_time);

	    	String current_latitude_value = CelestialCalculations.decimal_degrees_to_string(PersistentState.true_latitude_deg); 
	    	String current_longitude_value = CelestialCalculations.decimal_degrees_to_string(PersistentState.true_longitude_deg);

	    	Calendar cal = Calendar.getInstance(PersistentState.timezone);
	    	cal.setTimeInMillis(current_solar_time_ms);
	    	String date_text = String.format("%s %d, %d", cal.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, getLocale()), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
	    	String tz_text = PersistentState.timezone.getID().replace("/", " / ") + ((cal.get(Calendar.DST_OFFSET) != 0) ? " (DST)" : "");

	    	String sunset_time_value      = CelestialCalculations.decimal_hours_to_string((sunset_hrs  < 0) ? sunset_hrs  + 24 : ((24 <= sunset_hrs ) ? sunset_hrs  - 24 : sunset_hrs));
	    	String twilight_PM_time_value = CelestialCalculations.decimal_hours_to_string((twilight_pm < 0) ? twilight_pm + 24 : ((24 <= twilight_pm) ? twilight_pm - 24 : twilight_pm));
	    	String twilight_AM_time_value = CelestialCalculations.decimal_hours_to_string((twilight_am < 0) ? twilight_am + 24 : ((24 <= twilight_am) ? twilight_am - 24 : twilight_am));
	    	String sunrise_time_value     = CelestialCalculations.decimal_hours_to_string((sunrise_hrs < 0) ? sunrise_hrs + 24 : ((24 <= sunrise_hrs) ? sunrise_hrs - 24 : sunrise_hrs));

	    	String lunar_fullness_value   = String.format("%.2f%%", 100*lunar_fullness);
	    	String moonrise_time_value    = CelestialCalculations.decimal_hours_to_string(PracticalAstronomy.adjust24(moonrise_hrs));
	    	String moonset_time_value     = CelestialCalculations.decimal_hours_to_string(PracticalAstronomy.adjust24(moonset_hrs));

	    	int ctr_x = display_center.x;
	    	ColorLabelValue[][] clv = {
	    		{ 
	    			new ColorLabelValue(sun_color,            sun_label,               solar_value), 
	    			new ColorLabelValue(inner_planet_color,   mercury_label,           mercury_value), 
	    			new ColorLabelValue(inner_planet_color,   venus_label,             venus_value), 
	    			new ColorLabelValue(mars_color,           mars_label,              mars_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(gas_giant_color,      jupiter_label,           jupiter_value), 
	    			new ColorLabelValue(gas_giant_color,      saturn_label,            saturn_value), 
	    			new ColorLabelValue(gas_giant_color,      uranus_label,            uranus_value), 
	    			new ColorLabelValue(gas_giant_color,      neptune_label,           neptune_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(new_moon_color,       new_moon_label,          new_moon_date_value), 
	    			new ColorLabelValue(waxing_moon_color,    waxing_moon_label,       waxing_moon_date_value), 
	    			new ColorLabelValue(full_moon_color,      full_moon_label,         full_moon_date_value), 
	    			new ColorLabelValue(waning_moon_color,    waning_moon_label,       waning_moon_date_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(moonrise_color,       moon_label,              lunar_value), 
	    			new ColorLabelValue(lunar_fullness_color, lunar_fullness_label,    lunar_fullness_value), 
	    			new ColorLabelValue(moonrise_color,       moonrise_label,          moonrise_time_value), 
	    			new ColorLabelValue(moonset_color,        moonset_label,           moonset_time_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(sunset_color,         sunset_label,            sunset_time_value), 
	    			new ColorLabelValue(twilight_color,       twilight_PM_label,       twilight_PM_time_value), 
	    			new ColorLabelValue(twilight_color,       twilight_AM_label,       twilight_AM_time_value), 
	    			new ColorLabelValue(sunrise_color,        sunrise_label,           sunrise_time_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(current_time_color,   solar_label,             current_solar_time_value), 
	    			new ColorLabelValue(current_time_color,   sidereal_label,          current_sidereal_time_value), 
	    			new ColorLabelValue(current_time_color,   current_latitude_label,  current_latitude_value), 
	    			new ColorLabelValue(current_time_color,   current_longitude_label, current_longitude_value), 
	    		},
	    		{ 
	    			new ColorLabelValue(current_time_color,   "Date",                  date_text), 
	    			new ColorLabelValue(current_time_color,   "Time Zone",             tz_text), 
	    			new ColorLabelValue(current_time_color,   "City",                  PersistentState.city), 
	    			new ColorLabelValue(current_time_color,   "State / Country",       PersistentState.state), 
	    		},
	    	};
	    	
	    	show_values(h, ctr_x, 10, 15, menu_height+5, clv);

	    	// paint the horizon line
	    	show_horizon_lines(h, dso_alias_buf);

			show_horizon_plus_15(h, dso_alias_buf);

			show_horizon_plus_30(h, dso_alias_buf);

			show_ecliptic_plane(h, dso_alias_buf);

			show_galactic_plane(h, dso_alias_buf);

			show_zenith_marker(h, dso_alias_buf, current_decimal_hrs);

			show_constellations(h, dso_alias_buf);

			show_constellation_areas(h, dso_alias_buf);

			show_primary_stars(h, dso_alias_buf);
			
			show_yale_bright_stars(h, dso_alias_buf);

			show_named_stars(h, dso_alias_buf);

			show_caldwell(h, dso_alias_buf);

			show_herschel_400(h, dso_alias_buf);

			show_herschel_2500(h, dso_alias_buf);

			show_messier(h, dso_alias_buf);

			show_sharpless(h, dso_alias_buf);

			show_arp(h, dso_alias_buf);

			show_ngc(h, dso_alias_buf);

			show_ugc(h, dso_alias_buf);

			show_vsx(h, dso_alias_buf);

			show_wds(h, dso_alias_buf);

			show_webb_double_stars(h, dso_alias_buf);

			show_webb_planetary_nebulae(h, dso_alias_buf);

			show_webb_gaseous_nebulae(h, dso_alias_buf);

			show_webb_open_clusters(h, dso_alias_buf);

			show_webb_globular_clusters(h, dso_alias_buf);

			show_webb_galaxies(h, dso_alias_buf);

			show_webb_clusters_of_galaxies(h, dso_alias_buf);

			show_webb_anonymous_galaxies(h, dso_alias_buf);

			show_webb_southern_sky(h, dso_alias_buf);

			show_webb_variable_stars(h, dso_alias_buf);

			show_favorite_dsos(h, dso_alias_buf);

			// paint the sun, moon, and planets on the screen
			show_solar_system(h, dso_alias_buf, solar_location, lunar_location, planets);

			// draw the galactic center (Sagittarius A*)
			show_galactic_center(h);

			// target DSO
			show_selected_dso(h, selected_dso);
			
			show_angle_of_separation_dso(h);

			// legend
			show_legend(h, show_legend);

			dso_alias = dso_alias_buf;
			g.drawImage(img, used_area.x, used_area.y, null);
		}
		
		// TODO
		private final void show_angle_of_separation_dso(Graphics h)
		{
			if (angle_of_separation_dso_0 != null && angle_of_separation_dso_1 != null) {
				drawTarget(h, angle_of_separation_dso_0.ra_hrs(), angle_of_separation_dso_0.de_deg(), 5, Color.GREEN);
				drawDso   (h, angle_of_separation_dso_0.ra_hrs(), angle_of_separation_dso_0.de_deg(), angle_of_separation_dso_0.type());

				drawTarget(h, angle_of_separation_dso_1.ra_hrs(), angle_of_separation_dso_1.de_deg(), 5, Color.GREEN);
				drawDso   (h, angle_of_separation_dso_1.ra_hrs(), angle_of_separation_dso_1.de_deg(), angle_of_separation_dso_1.type());

				Color c = h.getColor();
				h.setColor(Color.GREEN);
				double ra0 = angle_of_separation_dso_0.ra_hrs();
				ra0 += (ra0 <  min_ra_hrs) ? 24 : 0;
				ra0 -= (max_ra_hrs <= ra0) ? 24 : 0;

				double ra1 = angle_of_separation_dso_1.ra_hrs();
				ra1 += (ra1 <  min_ra_hrs) ? 24 : 0;
				ra1 -= (max_ra_hrs <= ra1) ? 24 : 0;

				Point p0 = ra_dec_to_xy(ra0, angle_of_separation_dso_0.de_deg());
				Point p1 = ra_dec_to_xy(ra1, angle_of_separation_dso_1.de_deg());
				h.drawLine(p0.x, p0.y, p1.x, p1.y);
				h.setColor(c);
			}
		}

		private final void show_horizon_lines(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_horizon_lines) {
				for (int i=0; i < 360; i++) {
					double current_sidereal_time_hrs = (double) current_sidereal_time_ms / (double) CelestialCalculations.millisec_per_hour;
					CelestialCalculations.Equatorial p = CelestialCalculations.horizontal_to_equatorial_coordinates(0, i, PersistentState.true_latitude_deg, current_sidereal_time_hrs);

					if (max_ra_hrs < p.right_ascension) {
						p = new CelestialCalculations.Equatorial(p.right_ascension - 24, p.declination);
					} else if (p.right_ascension < min_ra_hrs) {
						p = new CelestialCalculations.Equatorial(p.right_ascension + 24, p.declination);
					}
	
					if (min_ra_hrs <= p.right_ascension && p.right_ascension <= max_ra_hrs) {
						Point xy = ra_dec_to_xy(p.right_ascension, p.declination);
						drawOval(h, chart_area, xy.x, xy.y, 1, 1, horizon_line_color);
					}
				}
			}
		}

		private final void show_horizon_plus_15(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_horizon_plus_15) {
				// paint the line 15 degrees above the horizon
				for (int i=0; i < 360; i++) {
					double current_sidereal_time_hrs = (double) current_sidereal_time_ms / (double) CelestialCalculations.millisec_per_hour;
					CelestialCalculations.Equatorial p = CelestialCalculations.horizontal_to_equatorial_coordinates(15, i, PersistentState.true_latitude_deg, current_sidereal_time_hrs);

					if (max_ra_hrs < p.right_ascension) {
						p = new CelestialCalculations.Equatorial(p.right_ascension - 24, p.declination);
					} else if (p.right_ascension < min_ra_hrs) {
						p = new CelestialCalculations.Equatorial(p.right_ascension + 24, p.declination);
					}
	
					if (min_ra_hrs <= p.right_ascension && p.right_ascension <= max_ra_hrs) {
						Point xy = ra_dec_to_xy(p.right_ascension, p.declination);
						drawOval(h, chart_area, xy.x, xy.y, 1, 1, horizon_plus_15_color);
					}
				}
			}
		}

		private final void show_horizon_plus_30(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_horizon_plus_30) {
				// paint the line 30 degrees above the horizon
				for (int i=0; i < 360; i++) {
					double current_sidereal_time_hrs = (double) current_sidereal_time_ms / (double) CelestialCalculations.millisec_per_hour;
					CelestialCalculations.Equatorial p = CelestialCalculations.horizontal_to_equatorial_coordinates(30, i, PersistentState.true_latitude_deg, current_sidereal_time_hrs);

					if (max_ra_hrs < p.right_ascension) {
						p = new CelestialCalculations.Equatorial(p.right_ascension - 24, p.declination);
					} else if (p.right_ascension < min_ra_hrs) {
						p = new CelestialCalculations.Equatorial(p.right_ascension + 24, p.declination);
					}
	
					if (min_ra_hrs <= p.right_ascension && p.right_ascension <= max_ra_hrs) {
						Point xy = ra_dec_to_xy(p.right_ascension, p.declination);
						drawOval(h, chart_area, xy.x, xy.y, 1, 1, horizon_plus_30_color);
					}
				}
			}
		}

		private final void show_ecliptic_plane(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_ecliptic_plane) {
				for (int i=0; i < 360; i++) {
					CelestialCalculations.Ecliptic ec = new CelestialCalculations.Ecliptic(0, i, 1, 0, 2000);
					CelestialCalculations.Equatorial eq = ec.to_equatorial();
					Point xy = ra_dec_to_xy(eq.right_ascension, eq.declination);
					drawOval(h, chart_area, xy.x, xy.y, 1, 1, ecliptic_plane_color);
					Point yz = ra_dec_to_xy(eq.right_ascension - 24, eq.declination);
					drawOval(h, chart_area, yz.x, yz.y, 1, 1, ecliptic_plane_color);
					Point zx = ra_dec_to_xy(eq.right_ascension + 24, eq.declination);
					drawOval(h, chart_area, zx.x, zx.y, 1, 1, ecliptic_plane_color);
				}
			}
		}

		private final void show_galactic_plane(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_galactic_plane) {
				for (int i=0; i < 360; i++) {
					CelestialCalculations.Galactic gp = new CelestialCalculations.GalacticJ2000(0, i);
					CelestialCalculations.Equatorial eq = gp.to_equatorial();
					Point xy = ra_dec_to_xy(eq.right_ascension, eq.declination);
					drawOval(h, chart_area, xy.x, xy.y, 1, 1, galactic_plane_color);
					Point yz = ra_dec_to_xy(eq.right_ascension - 24, eq.declination);
					drawOval(h, chart_area, yz.x, yz.y, 1, 1, galactic_plane_color);
					Point zx = ra_dec_to_xy(eq.right_ascension + 24, eq.declination);
					drawOval(h, chart_area, zx.x, zx.y, 1, 1, galactic_plane_color);
				}
			}
		}

		private final void show_zenith_marker(Graphics h, Element[][] dso_alias_buf, double current_decimal_hrs)
		{
			if (show_zenith_marker) {
				// draw the current zenith marker
				drawZenith(h, current_decimal_hrs, PersistentState.true_latitude_deg, 5, current_time_color);
			}
		}

		private final void show_constellations(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_constellations && ybsc != null) {
				h.setColor(constellation_color);
				for (String[][][] cons: ConstellationOutline.outline) {
					for (String[][] pair: cons) {
						String[] star0 = pair[0];
						String   con0 = star0[0];
						String   bay0 = YaleBrightStarAscCatalog.get_bayer_tld(star0[1]);
						String   sub0 = star0[2];
						YaleBrightStarAscEntry ybse0 = ybsc.get(con0, bay0, sub0);
						double ra0 = (ybse0.RA < min_ra_hrs) ? ybse0.RA + 24 : ((max_ra_hrs < ybse0.RA) ? ybse0.RA - 24 : ybse0.RA);
						double de0 = ybse0.DE;
						Point  p0  = ra_dec_to_xy(ra0, de0);
	
						String[] star1 = pair[1];
						String   con1 = star1[0];
						String   bay1 = YaleBrightStarAscCatalog.get_bayer_tld(star1[1]);
						String   sub1 = star1[2];
						YaleBrightStarAscEntry ybse1 = ybsc.get(con1, bay1, sub1);
						double ra1 = (ybse1.RA < min_ra_hrs) ? ybse1.RA + 24 : ((max_ra_hrs < ybse1.RA) ? ybse1.RA - 24 : ybse1.RA);
						double de1 = ybse1.DE;
						Point  p1  = ra_dec_to_xy(ra1, de1);
						
						if (Math.abs(p0.x - p1.x) < (chart_area.width/2.5)) {
							drawLine(h, chart_area, p0.x, p0.y, p1.x, p1.y);
						}
					}
				}
			}
		}

		private final void show_constellation_areas(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_constellation_areas) {
			}
		}

		private final void show_primary_stars(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_primary_stars && ybsc != null) {
				for (String[][][] cons: ConstellationOutline.outline) {
					for (String[][] pair: cons) {
						String[] star0 = pair[0];
						String   con0 = star0[0];
						String   bay0 = YaleBrightStarAscCatalog.get_bayer_tld(star0[1]);
						String   sub0 = star0[2];
						YaleBrightStarAscEntry ybse0 = ybsc.get(con0, bay0, sub0);
						double ra0  = (ybse0.RA < min_ra_hrs) ? ybse0.RA + 24 : ((max_ra_hrs < ybse0.RA) ? ybse0.RA - 24 : ybse0.RA);
						double de0  = ybse0.DE;
						double mag0 = ybse0.Vmag;
						Element e0 = (dso_alias_table != null) ? dso_alias_table.get(ybse0) : null;
						drawStar(h, ra0, de0, mag0, dso_alias_buf, e0);

						String[] star1 = pair[1];
						String   con1 = star1[0];
						String   bay1 = YaleBrightStarAscCatalog.get_bayer_tld(star1[1]);
						String   sub1 = star1[2];
						YaleBrightStarAscEntry ybse1 = ybsc.get(con1, bay1, sub1);
						double ra1  = (ybse1.RA < min_ra_hrs) ? ybse1.RA + 24 : ((max_ra_hrs < ybse1.RA) ? ybse1.RA - 24 : ybse1.RA);
						double de1  = ybse1.DE;
						double mag1 = ybse1.Vmag;
						Mag cw1 = Mag.mag_to_cw(mag1);
						Element e1 = (dso_alias_table != null) ? dso_alias_table.get(ybse1) : null;
						drawStar(h, ra1, de1, mag1, dso_alias_buf, e1);
					}
				}
			}
		}

		private final void show_yale_bright_stars(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_yale_bright_stars && ybsc != null) {
				for (int i=0; i < ybsc.elts.length; i++) {
					YaleBrightStarAscEntry elt = ybsc.elts[i];
					double ra  = (elt.RA < min_ra_hrs) ? elt.RA + 24 : ((max_ra_hrs < elt.RA) ? elt.RA - 24 : elt.RA);
					double de  = elt.DE;
					double mag = elt.Vmag;
					Point p = ra_dec_to_xy(ra, de);
					Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
					drawStar(h, ra, de, mag, dso_alias_buf, e);
				}
			}
		}

		private final void show_named_stars(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_named_stars && iau_csn != null && ybsc != null) {
				for (int i=0; i < iau_csn.elts.length; i++) {
					IAUEntry elt = iau_csn.elts[i];
					double ra  = (elt.ra_hrs < min_ra_hrs) ? elt.ra_hrs + 24 : ((max_ra_hrs < elt.ra_hrs) ? elt.ra_hrs - 24 : elt.ra_hrs);
					double de  = elt.dec_deg;
					double mag = elt.mag;
					Point p = ra_dec_to_xy(ra, de);
					Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
					drawStar(h, ra, de, mag, dso_alias_buf, e);
				}
			}
		}

		private final void show_caldwell(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_caldwell && caldwell != null) {
				for (int i=0; i < caldwell.elts.length; i++) {
					CaldwellEntry elt = caldwell.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.dec_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_herschel_400(Graphics h, Element[][] dso_alias_buf)
		{
	    	// System.out.printf("%s: %d: h04.show=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), show_herschel_400);
			if (show_herschel_400 && herschel_400 != null) {
		    	// System.out.printf("%s: %d: h04.len=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), herschel_400.elts.length);
				for (int i=0; i < herschel_400.elts.length; i++) {
					Herschel400.Element elt = herschel_400.elts[i];	// TODO
			    	// System.out.printf("%s: %d: h04=>%s< filter=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt.toString(), elt.matches(filter));
					if (elt.matches(filter)) {
						double ra = (elt.ra_hrs < min_ra_hrs) ? elt.ra_hrs + 24 : ((max_ra_hrs < elt.ra_hrs) ? elt.ra_hrs - 24 : elt.ra_hrs);
						double de = elt.de_deg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
				    	// if (e == null) System.out.printf("%s: %d: elt=>>>%s<<< filter=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt.toString(), elt.matches(filter));
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_herschel_2500(Graphics h, Element[][] dso_alias_buf)
		{
	    	// System.out.printf("%s: %d: h25.show=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), show_herschel_2500);
			if (show_herschel_2500 && herschel_2500 != null) {
		    	// System.out.printf("%s: %d: h25.len=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), herschel_2500.elts.length);
				for (int i=0; i < herschel_2500.elts.length; i++) {
					Herschel2500.Element elt = herschel_2500.elts[i];
			    	// System.out.printf("%s: %d: h25=>%s< filter=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt.toString(), elt.matches(filter));
					if (elt.matches(filter)) {
						double ra = (elt.ra_hrs < min_ra_hrs) ? elt.ra_hrs + 24 : ((max_ra_hrs < elt.ra_hrs) ? elt.ra_hrs - 24 : elt.ra_hrs);
						double de = elt.de_deg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_messier(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_messier && messier != null) {
				for (int i=0; i < messier.elts.length; i++) {
					MessierEntry elt = messier.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.dec_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_sharpless(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_sharpless && sharpless != null) {
				for (int i=0; i < sharpless.elts.length; i++) {
					SharplessEntry elt = sharpless.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.de_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_arp(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_arp && arp != null) {
				for (int i=0; i < arp.elts.length; i++) {
					ArpEntry elt = arp.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.de_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_ngc(Graphics h, Element[][] dso_alias_buf)
		{
			if ((show_ngc || show_ic) && ngc_ic != null) {
				for (int i=0; i < ngc_ic.elts.length; i++) {
					NgcIcEntry elt = ngc_ic.elts[i];
					if (filter == null || elt.matches(filter)) {
						if ((show_ngc && elt.is_ngc) || (show_ic && ! elt.is_ngc)) {
							double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
							double de = elt.dec_ddeg;
							Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
							drawDso(h, ra, de, dso_alias_buf, e);
						}
					}
				}
			}
		}

		private final void show_ugc(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_ugc && ugc != null) {
				for (int i=0; i < ugc.elts.length; i++) {
					UgcEntry elt = ugc.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.de_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_vsx(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_vsx && vsx != null) {
				for (int i=0; i < vsx.elts.length; i++) {
					VsxEntry elt = vsx.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.de_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_wds(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_wds && wds != null) {
				for (int i=0; i < wds.elts.length; i++) {
					WdsEntry elt = wds.elts[i];
					if (filter == null || elt.matches(filter)) {
						double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
						double de = elt.de_ddeg;
						Element e = (dso_alias_table != null) ? dso_alias_table.get(elt) : null;
						drawDso(h, ra, de, dso_alias_buf, e);
					}
				}
			}
		}

		private final void show_webb_double_stars(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_double_stars && double_stars != null) {
			}
		}

		private final void show_webb_planetary_nebulae(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_planetary_nebulae && planetary_nebulae != null) {
				for (int i=0; i < planetary_nebulae.elts.length; i++) {
					WebbPlanetaryNebulaeEntry elt = planetary_nebulae.elts[i];
					double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
					double de = elt.de_ddeg;
					String name = String.format("Webb %d (%s) in %s", elt.webb_idx, elt.catalog_id, BayerEntry.find_name(elt.constellation));
					drawDso(h, ra, de, dso_alias_buf, dso_alias_table.find(elt.catalog_id));
				}
			}
		}

		private final void show_webb_gaseous_nebulae(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_gaseous_nebulae && gaseous_nebulae != null) {
				for (int i=0; i < gaseous_nebulae.elts.length; i++) {
					WebbGaseousNebulaeEntry elt = gaseous_nebulae.elts[i];
					double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
					double de = elt.de_ddeg;
					String name = String.format("Webb %d (%s) in %s", elt.webb_idx, elt.catalog_id, BayerEntry.find_name(elt.constellation));
					drawDso(h, ra, de, dso_alias_buf, dso_alias_table.find(elt.catalog_id));
				}
			}
		}

		private final void show_webb_open_clusters(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_open_clusters && open_clusters != null) {
				for (int i=0; i < open_clusters.elts.length; i++) {
					WebbOpenClustersEntry elt = open_clusters.elts[i];
					double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
					double de = elt.de_ddeg;
					String name = String.format("Webb %d (%s) in %s", elt.webb_idx, elt.catalog_id, BayerEntry.find_name(elt.constellation));
					drawDso(h, ra, de, dso_alias_buf, dso_alias_table.find(elt.catalog_id));
				}
			}
		}

		private final void show_webb_globular_clusters(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_globular_clusters && globular_clusters != null) {
				for (int i=0; i < globular_clusters.elts.length; i++) {
					WebbGlobularClustersEntry elt = globular_clusters.elts[i];
					double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
					double de = elt.de_ddeg;
					String name = String.format("Webb %d (%s) in %s", elt.webb_idx, elt.catalog_id, BayerEntry.find_name(elt.constellation));
					drawDso(h, ra, de, dso_alias_buf, dso_alias_table.find(elt.catalog_id));
				}
			}
		}

		private final void show_webb_galaxies(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_galaxies && galaxies != null) {
				for (int i=0; i < galaxies.elts.length; i++) {
					WebbGalaxiesEntry elt = galaxies.elts[i];
					double ra = (elt.ra_dhrs < min_ra_hrs) ? elt.ra_dhrs + 24 : ((max_ra_hrs < elt.ra_dhrs) ? elt.ra_dhrs - 24 : elt.ra_dhrs);
					double de = elt.de_ddeg;
					String name = String.format("Webb %d (%s) in %s", elt.webb_idx, elt.catalog_id, BayerEntry.find_name(elt.constellation));
					drawDso(h, ra, de, dso_alias_buf, dso_alias_table.find(elt.catalog_id));
				}
			}
		}

		private final void show_webb_clusters_of_galaxies(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_clusters_of_galaxies && galaxy_clusters != null) {
			}
		}

		private final void show_webb_anonymous_galaxies(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_anonymous_galaxies && anonymous_galaxies != null) {
			}
		}

		private final void show_webb_southern_sky(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_southern_sky && southern_sky != null) {
			}
		}

		private final void show_webb_variable_stars(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_webb_variable_stars && webb_variable_stars != null) {
			}
		}

		private final void show_favorite_dsos(Graphics h, Element[][] dso_alias_buf)
		{
			if (show_favorite_dsos && favorite_dsos != null) {
			}
		}

		private final void show_solar_system(Graphics h, Element[][] dso_alias_buf, SolarLocation solar_location, LunarLocation lunar_location, ApproximatePlanetLocation planets)
		{
			if (show_sun_moon_planets) {
				int planet_width = 5;

				// paint neptune
				double neptune_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.NEPTUNE].right_ascension;
				double neptune_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.NEPTUNE].declination;
				int neptune_width = planet_width;
				drawPlanet(h, neptune_right_ascension, neptune_declination, neptune_width, gas_giant_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Neptune"));

				// paint uranus
				double uranus_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.URANUS].right_ascension;
				double uranus_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.URANUS].declination;
				int uranus_width = planet_width;
				drawPlanet(h, uranus_right_ascension, uranus_declination, uranus_width, gas_giant_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Uranus"));

				// paint saturn
				double saturn_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.SATURN].right_ascension;
				double saturn_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.SATURN].declination;
				int saturn_width = planet_width;
				drawPlanet(h, saturn_right_ascension, saturn_declination, saturn_width, gas_giant_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Saturn"));

				// paint jupiter
				double jupiter_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.JUPITER].right_ascension;
				double jupiter_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.JUPITER].declination;
				int jupiter_width = planet_width;
				drawPlanet(h, jupiter_right_ascension, jupiter_declination, jupiter_width, gas_giant_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Jupiter"));

				// paint mars
				double mars_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.MARS].right_ascension;
				double mars_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.MARS].declination;
				int mars_width = planet_width;
				drawPlanet(h, mars_right_ascension, mars_declination, mars_width, mars_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Mars"));

				// paint venus
				double venus_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.VENUS].right_ascension;
				double venus_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.VENUS].declination;
				int venus_width = planet_width;
				drawPlanet(h, venus_right_ascension, venus_declination, venus_width, inner_planet_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Venus"));

				// paint mercury
				double mercury_right_ascension = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.MERCURY].right_ascension;
				double mercury_declination     = planets.locations[PracticalAstronomy.ApproximatePlanetLocation.MERCURY].declination;
				int mercury_width = planet_width;
				drawPlanet(h, mercury_right_ascension, mercury_declination, mercury_width, inner_planet_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Mercury"));

				// paint the moon
				double lunar_right_ascension = lunar_location.right_ascension;
				double lunar_declination     = lunar_location.declination;
				int lunar_width = 8;
				drawMoon(h, lunar_right_ascension, lunar_declination, lunar_width, shade(blue_table, lunar_fullness), dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Moon"));

				// paint the sun
				double solar_right_ascension = solar_location.right_ascension;
				double solar_declination = solar_location.declination;
				int solar_width = deg_to_width(0.75);
				drawSun(h, solar_right_ascension, solar_declination, solar_width, sun_color, dso_alias_buf, (dso_alias_table == null) ? null : dso_alias_table.find("Sun"));
			}
		}

		private final void show_galactic_center(Graphics h)
		{
			Galactic   ga = new Galactic(0,0);
			Equatorial eq = ga.to_equatorial();
			if (dso_alias_table != null) dso_alias_table.update("", eq.right_ascension, eq.declination);
			drawPlus(h, eq.right_ascension, eq.declination, 5, galactic_plane_color);
		}

		// TODO draw selected DSO
		private final void show_selected_dso(Graphics h, Element elt)
		{
			if (elt != null) {
				// draw a cross over the targeted DSO
				drawTarget(h, elt.ra_hrs(), elt.de_deg(), 5, target_color);
				drawDso   (h, elt.ra_hrs(), elt.de_deg(), elt.type());

				String[] line = (dso_alias_table == null) ? null : dso_alias_table.info_text(elt, max_dso_name_len);
				if (line != null) {
				   	int height = h.getFontMetrics().getStringBounds(line[0], h).getBounds().height;
					Point info_box_origin = ra_dec_to_xy(max_ra_hrs, min_dec_deg);
					info_box_origin.x += 5;
					if (line.length < 5) {
						// info_box_origin.y  = used_size.height - 4 * height;
						info_box_origin.y += 5 * height/2;
					} else {
						info_box_origin.y -= line.length * height - height/2;
					}
					for (int i=0; i < line.length; i++) {
						// drawString(h, chart_area, information_box.line[i], info_box_origin.x, info_box_origin.y + i * height, Color.white);
						h.setColor(Color.white);
						h.drawString(line[i], info_box_origin.x, info_box_origin.y + i * height);
					}
				}
			}
		}

		// TODO draw legend
		private final void show_legend(Graphics h, boolean show)
		{
			if (show) {
			   	int height = h.getFontMetrics().getStringBounds("Legend", h).getBounds().height;
			   	int width  = h.getFontMetrics().getStringBounds("Galactic Plane Galaxy Center Moon Inner Planets Galaxies Variable Stars -1 2 5", h).getBounds().width;
				Point legend_origin = ra_dec_to_xy(min_ra_hrs, min_dec_deg);
	
				legend_origin.x -= width + 8 * 25;
				legend_origin.y +=  2 * height + height/2;
				int offset = 0;
	
				int horizon_width = deg_to_width(0.75);
				drawDots(h,                                   legend_origin.x + offset,                     legend_origin.y + 0 * height,         5, ecliptic_plane_color);
				drawString(h, display_area, "Ecliptic Plane", legend_origin.x + offset + 2 * horizon_width, legend_origin.y + 0 * height + height/4, Color.WHITE);
	
				drawDots(h,                                   legend_origin.x + offset,                     legend_origin.y + 1 * height,         5, galactic_plane_color);
				drawString(h, display_area, "Galactic Plane", legend_origin.x + offset + 2 * horizon_width, legend_origin.y + 1 * height + height/4, Color.WHITE);
	
				drawDots(h,                                   legend_origin.x + offset,                     legend_origin.y + 2 * height,         5, current_time_color);
				drawString(h, display_area, "Horizon",        legend_origin.x + offset + 2 * horizon_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
				offset += horizon_width + 2 * horizon_width + h.getFontMetrics().getStringBounds("Galactic Plane", h).getBounds().width + 10;
	
				int target_width = deg_to_width(0.75);
				drawZenith(h,                                legend_origin.x + offset,                    legend_origin.y + 0 * height,         5, target_color);
				drawString(h, display_area, "DSO Target",    legend_origin.x + offset + 2 * target_width, legend_origin.y + 0 * height + height/4, Color.WHITE);
	
				drawZenith(h,                                legend_origin.x + offset,                    legend_origin.y + 1 * height,         5, galactic_plane_color);
				drawString(h, display_area, "Galaxy Center", legend_origin.x + offset + 2 * target_width, legend_origin.y + 1 * height + height/4, Color.WHITE);
	
				drawZenith(h,                                legend_origin.x + offset,                    legend_origin.y + 2 * height,         5, current_time_color);
				drawString(h, display_area, "Zenith",        legend_origin.x + offset + 2 * target_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += target_width + 2 * target_width + h.getFontMetrics().getStringBounds("Galaxy Center", h).getBounds().width + 10;
	
				int solar_width = deg_to_width(0.75);
				drawSun   (h,                         legend_origin.x + offset,                   legend_origin.y,                      solar_width,  sun_color);
				drawString(h, display_area, "Sun",    legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				int lunar_width = 8;
				drawMoon  (h,                         legend_origin.x + offset,                   legend_origin.y +     height,         lunar_width,  shade(blue_table, lunar_fullness));
				drawString(h, display_area, "Moon",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
	
				offset += solar_width + 2 * solar_width + h.getFontMetrics().getStringBounds("Moon", h).getBounds().width + 10;
	
				int planet_width = 5;
				drawPlanet(h,                                legend_origin.x + offset,                   legend_origin.y               , planet_width, inner_planet_color);
				drawString(h, display_area, "Inner Planets", legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawPlanet(h,                                legend_origin.x + offset,                   legend_origin.y +     height, planet_width,  mars_color);
				drawString(h, display_area, "Mars",          legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawPlanet(h,                                legend_origin.x + offset,                   legend_origin.y + 2 * height, planet_width,  gas_giant_color);
				drawString(h, display_area, "Gas Giants",    legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += planet_width + 2 * solar_width + h.getFontMetrics().getStringBounds("Inner Planets", h).getBounds().width + 10;
				drawDso(h,                                legend_origin.x + offset,                   legend_origin.y               , DsoType.nebula[0]);
				drawString(h, display_area, "Nebulae",    legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawDso(h,                                legend_origin.x + offset,                   legend_origin.y +     height, DsoType.globular_cluster[0]);
				drawString(h, display_area, "Clusters",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawDso(h,                                legend_origin.x + offset,                   legend_origin.y + 2 * height, DsoType.galaxy[0]);
				drawString(h, display_area, "Galaxies",   legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += planet_width + 2 * solar_width + h.getFontMetrics().getStringBounds("Galaxies", h).getBounds().width + 10;
				drawDso(h,                                    legend_origin.x + offset,                   legend_origin.y               , DsoType.double_star[0]);
				drawString(h, display_area, "Double Stars",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawDso(h,                                    legend_origin.x + offset,                   legend_origin.y +     height, DsoType.variable_star[0]);
				drawString(h, display_area, "Variable Stars", legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawDso(h,                                  legend_origin.x + offset,                   legend_origin.y + 2 * height, DsoType.unidentified[0]);
				drawString(h, display_area, "Unidentified", legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += planet_width + 2 * solar_width + h.getFontMetrics().getStringBounds("Variable Stars", h).getBounds().width + 10;
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y               , Mag.mag_to_cw(-1.0));
				drawString(h, display_area, "-1",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y +     height,   Mag.mag_to_cw(0));
				drawString(h, display_area, "0",    legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y + 2 * height,   Mag.mag_to_cw(1));
				drawString(h, display_area, "1",    legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += planet_width + 2 * solar_width + h.getFontMetrics().getStringBounds("-1", h).getBounds().width + 10;
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y               , Mag.mag_to_cw(2));
				drawString(h, display_area, "2",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y +     height,   Mag.mag_to_cw(3));
				drawString(h, display_area, "3",    legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y + 2 * height,   Mag.mag_to_cw(4));
				drawString(h, display_area, "4",    legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
	
	
				offset += planet_width + 2 * solar_width + h.getFontMetrics().getStringBounds("-1", h).getBounds().width + 10;
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y               , Mag.mag_to_cw(5));
				drawString(h, display_area, "5",   legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y +     height,   Mag.mag_to_cw(6));
				drawString(h, display_area, "6",    legend_origin.x + offset + 2 * solar_width, legend_origin.y +     height + height/4, Color.WHITE);
	
				drawStar(h,                         legend_origin.x + offset,                   legend_origin.y + 2 * height,   Mag.mag_to_cw(7));
				drawString(h, display_area, "7",    legend_origin.x + offset + 2 * solar_width, legend_origin.y + 2 * height + height/4, Color.WHITE);
			}
		}
		
		private int rescale_pixel_size(int px)
		{
			int rescaled_size = (int) (PersistentState.dso_icon_scale_factor * px);
			// rescaled_size = px;
			// System.out.printf("%s: %4d: rescale_pixel_size : px=%d disf=%3.1f rescaled size=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), px, PersistentState.dso_icon_scale_factor, rescaled_size);

			return rescaled_size;
		}

		private final void drawLine(Graphics g, Rectangle chart, int x0, int y0, int x1, int y1)
		{
			if (chart.x <= x0 && x0 <= chart.x + chart.width && chart.y <= y0 && y0 <= chart.y + chart.height &&
				chart.x <= x1 && x1 <= chart.x + chart.width && chart.y <= y1 && y1 <= chart.y + chart.height) {
				g.drawLine(x0, y0, x1, y1);
			}
		}

		private final void drawOval(Graphics g, Rectangle chart, int x, int y, int width, int height, Color color)
		{
			width  = rescale_pixel_size(width);
			height = rescale_pixel_size(height);
			if (chart.x <= x && x + width <= chart.x + chart.width && chart.y <= y && y + height <= chart.y + chart.height) {
				g.setColor(color);
				g.drawOval(x, y, rescale_pixel_size(width), rescale_pixel_size(height));
			}
		}

		private final void fillOval(Graphics g, Rectangle chart, int x, int y, int width, int height, Color color)
		{
			width  = rescale_pixel_size(width);
			height = rescale_pixel_size(height);
			if (chart.x <= x && x + width <= chart.x + chart.width && chart.y <= y && y + height <= chart.y + chart.height) {
				g.setColor(color);
				g.fillOval(x, y, rescale_pixel_size(width), rescale_pixel_size(height));
			}
		}

		private final void drawRect(Graphics g, Rectangle chart, int x, int y, int width, int height, Color color)
		{
			width  = rescale_pixel_size(width);
			height = rescale_pixel_size(height);
			if (chart.x <= x && x + width <= chart.x + chart.width && chart.y <= y && y + height <= chart.y + chart.height) {
				g.setColor(color);
				g.drawRect(x, y, rescale_pixel_size(width), rescale_pixel_size(height));
			}
		}

		private final void fillRect(Graphics g, Rectangle chart, int x, int y, int width, int height, Color color)
		{
			width  = rescale_pixel_size(width);
			height = rescale_pixel_size(height);
			if (chart.x <= x && x + width <= chart.x + chart.width && chart.y <= y && y + height <= chart.y + chart.height) {
				g.setColor(color);
				g.fillRect(x, y, rescale_pixel_size(width), rescale_pixel_size(height));
			}
		}

		private final void drawString(Graphics g, Rectangle chart, String s, int x, int y, Color color)
		{
			if (chart.x <= x && x <= chart.x + chart.width && chart.y <= y && y <= chart.y + chart.height) {
				g.setColor(color);
				g.drawString(s, x, y);
			}
		}

		private final void drawCircle(Graphics g, double ra, double dec, int diam_px, Color color)
		{
			diam_px = rescale_pixel_size(diam_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				g.drawOval(p.x - diam_px/2, p.y - diam_px/2, diam_px, diam_px);
			}
		}

		private final void fillCircle(Graphics g, double ra, double dec, int diam_px, Color color)
		{
			diam_px = rescale_pixel_size(diam_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				g.fillOval(p.x - diam_px/2, p.y - diam_px/2, diam_px, diam_px);
			}
		}

		private final void drawOval(Graphics g, double ra, double dec, int ra_px, int de_px, Color color)
		{
			ra_px = rescale_pixel_size(ra_px);
			de_px = rescale_pixel_size(de_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				g.drawOval(p.x - ra_px/2, p.y - ra_px/2, ra_px, de_px);
			}
		}

		private final void fillOval(Graphics g, double ra, double dec, int ra_px, int de_px, Color color)
		{
			ra_px = rescale_pixel_size(ra_px);
			de_px = rescale_pixel_size(de_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				g.fillOval(p.x - ra_px/2, p.y - ra_px/2, ra_px, de_px);
			}
		}

		private final void drawDso(Graphics g, double ra, double dec, Element[][] dso_alias_buf, Element elt)
		{
			if (elt != null) {
				ra += (ra <  min_ra_hrs) ? 24 : 0;
				ra -= (max_ra_hrs <= ra) ? 24 : 0;
				if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
					Point p = ra_dec_to_xy(ra, dec);
					drawDso(g, p.x, p.y, elt.type());
					DsoTypeColorSize tcs = DsoTypeColorSize.find(elt.type());
					add_to_dso_alias(dso_alias_buf, chart_area, p, 2*tcs.width,  2*tcs.height, elt);
				}
			}
		}

		private final void drawDso(Graphics g, double ra, double dec, String dso_type)
		{
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				drawDso(g, p.x, p.y, dso_type);
			}
		}

		private final void drawDso(Graphics g, int x, int y, String dso_type)
		{
			if (dso_type != null) {
				DsoTypeColorSize tcs = DsoTypeColorSize.find(dso_type);
				int width   = tcs.width;
				int height  = tcs.height; 
				Color color = tcs.color;
				g.setColor(color);
				g.fillOval(x - width/2, y - height/2, width, height);
			}
		}

		private final void drawPlanet(Graphics g, double ra, double dec, int diam_px, Color color, DsoAlias.Element[][] dso_alias_buf, Element elt)
		{
			diam_px = rescale_pixel_size(diam_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				g.fillOval(p.x - diam_px/2, p.y - diam_px/2, diam_px, diam_px);
				add_to_dso_alias(dso_alias_buf, chart_area, p, 2*diam_px,  2*diam_px, elt);
			}
		}

		private final void drawPlanet(Graphics g, int x, int y, int diam_px, Color color)
		{
			diam_px = rescale_pixel_size(diam_px);
			g.setColor(color);
			g.fillOval(x - diam_px/2, y - diam_px/2, diam_px, diam_px);
		}

		private final void drawMoon(Graphics g, double ra, double dec, int diam_px, Color color, Element[][] dso_alias_buf, Element elt)
		{
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				drawMoon(g, p.x, p.y, diam_px, color);
				add_to_dso_alias(dso_alias_buf, chart_area, p, 2*diam_px,  2*diam_px, elt);
			}
		}

		private final void drawMoon(Graphics g, int x, int y, int diam_px, Color color)
		{
			diam_px = rescale_pixel_size(diam_px);
			g.setColor(color);
			g.fillOval(x - diam_px/2, y - diam_px/2, diam_px, diam_px);
			g.setColor(shade(blue_table, 0.5));
			g.drawOval(x - diam_px/2, y - diam_px/2, diam_px, diam_px);
		}

		private final void drawSun(Graphics g, double ra, double dec, int diam_px, Color color, Element[][] dso_alias_buf, Element elt)
		{
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				drawSun(g, p.x, p.y, diam_px, color);
				add_to_dso_alias(dso_alias_buf, chart_area, p, 2*diam_px,  2*diam_px, elt);
			}
		}

		private final void drawSun(Graphics g, int x, int y, int diam_px, Color color)
		{
			diam_px = rescale_pixel_size(diam_px);
			g.setColor(color);
			g.fillOval(x - diam_px/2, y - diam_px/2, diam_px, diam_px);
			double root_two_over_two = Math.sqrt(2) / 2;
			g.drawLine(x - diam_px, y, x + diam_px, y);
			g.drawLine(x, y - diam_px, x, y + diam_px);
			g.drawLine((int) (x - root_two_over_two*diam_px), (int) (y - root_two_over_two*diam_px), (int) (x + root_two_over_two*diam_px), (int) (y + root_two_over_two*diam_px));
			g.drawLine((int) (x + root_two_over_two*diam_px), (int) (y - root_two_over_two*diam_px), (int) (x - root_two_over_two*diam_px), (int) (y + root_two_over_two*diam_px));
		}

		private final void drawStar(Graphics g, double ra, double dec, double mag, Element[][] dso_alias_buf, Element elt)
		{
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Mag cw = Mag.mag_to_cw(mag);
				int diam_px = cw.width;
				Point p = ra_dec_to_xy(ra, dec);
				drawStar(g, p.x, p.y, cw);
				add_to_dso_alias(dso_alias_buf, chart_area, p, 2*diam_px,  2*diam_px, elt);
			}
		}

		private final void drawStar(Graphics g, int x, int y, Mag cw)
		{
			int width = rescale_pixel_size(cw.width);
			g.setColor(cw.color);
			g.fillOval(x - width/2, y - width/2, width, width);
		}

		private final void drawTarget(Graphics g, double ra, double dec, int len_px, Color color)
		{
			len_px = rescale_pixel_size(len_px);
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point p = ra_dec_to_xy(ra, dec);
				g.setColor(color);
				drawLine(g, chart_area, p.x - 2*len_px, p.y,            p.x - len_px, p.y);
				drawLine(g, chart_area, p.x + 2*len_px, p.y,            p.x + len_px, p.y);
				drawLine(g, chart_area, p.x,            p.y - 2*len_px, p.x,          p.y - len_px);
				drawLine(g, chart_area, p.x,            p.y + 2*len_px, p.x,          p.y + len_px);
			}
		}

		private final void drawPlus(Graphics g, double ra, double dec, int len_px, Color color)
		{
			ra += (ra <  min_ra_hrs) ? 24 : 0;
			ra -= (max_ra_hrs <= ra) ? 24 : 0;
			if (min_ra_hrs <= ra && ra <= max_ra_hrs && min_dec_deg <= dec && dec <= max_dec_deg) {
				Point ctx = ra_dec_to_xy(ra, dec);
				g.setColor(color);
		    	drawZenith(g, ctx.x, ctx.y, len_px, color);
			}
		}

		private final void drawZenith(Graphics g, double ra_hrs, double de_deg, int len_px, Color color)
		{
			if (min_sol_hrs <= ra_hrs && ra_hrs <= max_sol_hrs && min_dec_deg <= de_deg && de_deg <= max_dec_deg) {
		    	Point ctx  = solar_dec_to_xy(ra_hrs, de_deg);
		    	drawZenith(g, ctx.x, ctx.y, len_px, color);
			}
		}

		private final void drawZenith(Graphics g, int x, int y, int len_px, Color color)
		{
			len_px = rescale_pixel_size(len_px);
			g.setColor(color);
			g.drawLine(x - len_px, y,          x + len_px, y);
			g.drawLine(x,          y - len_px, x,          y + len_px);
		}

		private final void drawDots(Graphics g, int x, int y, int len_px, Color color)
		{
			g.setColor(color);
			for (int i=0; i < 2*len_px; i+=2) {
				g.drawLine(x - len_px + i, y, x - len_px + i, y);
			}
		}
	}

	private void show_values(Graphics h, int ctr, int gap, int blk_gap, int top, ColorLabelValue[][] clv)
	{
		// find the width of each block
		int[] len = new int[clv.length];
		int   wid = 0;
		for (int i=0; i < clv.length; i++) {
			ColorLabelValue[] c = clv[i];
			len[i] = 0;
			for (int j=0; j < c.length; j++) {
		    	int l_width = h.getFontMetrics().stringWidth(c[j].label);
		    	int v_width = h.getFontMetrics().stringWidth(c[j].value);
		    	int width   = l_width + gap + v_width;
				len[i]      = (len[i] < width) ? width : len[i];
			}
			wid += len[i];
		}
		wid += (clv.length - 1) * blk_gap;
		int start = ctr - wid / 2;
		int ht = h.getFontMetrics().getHeight();
		for (int i=0; i < clv.length; i++) {
			for (int j=0; j < clv[i].length; j++) {
				h.setColor(clv[i][j].color);
				h.drawString(clv[i][j].label, start, top + (j+1) * ht);
		    	int vw = h.getFontMetrics().stringWidth(clv[i][j].value);
				h.drawString(clv[i][j].value, start + len[i] - vw, top + (j+1) * ht);
			}
			start += len[i] + blk_gap;
		}
	}

	private static final Color[] blue_table = {
			new Color(0x0000FF), new Color(0x0101FF), new Color(0x0202FF), new Color(0x0303FF),
			new Color(0x0404FF), new Color(0x0505FF), new Color(0x0606FF), new Color(0x0707FF),
			new Color(0x0808FF), new Color(0x0909FF), new Color(0x0a0aFF), new Color(0x0b0bFF),
			new Color(0x0c0cFF), new Color(0x0d0dFF), new Color(0x0e0eFF), new Color(0x0f0fFF),

			new Color(0x1010FF), new Color(0x1111FF), new Color(0x1212FF), new Color(0x1313FF),
			new Color(0x1414FF), new Color(0x1515FF), new Color(0x1616FF), new Color(0x1717FF),
			new Color(0x1818FF), new Color(0x1919FF), new Color(0x1a1aFF), new Color(0x1b1bFF),
			new Color(0x1c1cFF), new Color(0x1d1dFF), new Color(0x1e1eFF), new Color(0x1f1fFF),

			new Color(0x2020FF), new Color(0x2121FF), new Color(0x2222FF), new Color(0x2323FF),
			new Color(0x2424FF), new Color(0x2525FF), new Color(0x2626FF), new Color(0x2727FF),
			new Color(0x2828FF), new Color(0x2929FF), new Color(0x2a2aFF), new Color(0x2b2bFF),
			new Color(0x2c2cFF), new Color(0x2d2dFF), new Color(0x2e2eFF), new Color(0x2f2fFF),

			new Color(0x3030FF), new Color(0x3131FF), new Color(0x3232FF), new Color(0x3333FF),
			new Color(0x3434FF), new Color(0x3535FF), new Color(0x3636FF), new Color(0x3737FF),
			new Color(0x3838FF), new Color(0x3939FF), new Color(0x3a3aFF), new Color(0x3b3bFF),
			new Color(0x3c3cFF), new Color(0x3d3dFF), new Color(0x3e3eFF), new Color(0x3f3fFF),

			new Color(0x4040FF), new Color(0x4141FF), new Color(0x4242FF), new Color(0x4343FF),
			new Color(0x4444FF), new Color(0x4545FF), new Color(0x4646FF), new Color(0x4747FF),
			new Color(0x4848FF), new Color(0x4949FF), new Color(0x4a4aFF), new Color(0x4b4bFF),
			new Color(0x4c4cFF), new Color(0x4d4dFF), new Color(0x4e4eFF), new Color(0x4f4fFF),

			new Color(0x5050FF), new Color(0x5151FF), new Color(0x5252FF), new Color(0x5353FF),
			new Color(0x5454FF), new Color(0x5555FF), new Color(0x5656FF), new Color(0x5757FF),
			new Color(0x5858FF), new Color(0x5959FF), new Color(0x5a5aFF), new Color(0x5b5bFF),
			new Color(0x5c5cFF), new Color(0x5d5dFF), new Color(0x5e5eFF), new Color(0x5f5fFF),

			new Color(0x6060FF), new Color(0x6161FF), new Color(0x6262FF), new Color(0x6363FF),
			new Color(0x6464FF), new Color(0x6565FF), new Color(0x6666FF), new Color(0x6767FF),
			new Color(0x6868FF), new Color(0x6969FF), new Color(0x6a6aFF), new Color(0x6b6bFF),
			new Color(0x6c6cFF), new Color(0x6d6dFF), new Color(0x6e6eFF), new Color(0x6f6fFF),

			new Color(0x7070FF), new Color(0x7171FF), new Color(0x7272FF), new Color(0x7373FF),
			new Color(0x7474FF), new Color(0x7575FF), new Color(0x7676FF), new Color(0x7777FF),
			new Color(0x7878FF), new Color(0x7979FF), new Color(0x7a7aFF), new Color(0x7b7bFF),
			new Color(0x7c7cFF), new Color(0x7d7dFF), new Color(0x7e7eFF), new Color(0x7f7fFF),

			new Color(0x8080FF), new Color(0x8181FF), new Color(0x8282FF), new Color(0x8383FF),
			new Color(0x8484FF), new Color(0x8585FF), new Color(0x8686FF), new Color(0x8787FF),
			new Color(0x8888FF), new Color(0x8989FF), new Color(0x8a8aFF), new Color(0x8b8bFF),
			new Color(0x8c8cFF), new Color(0x8d8dFF), new Color(0x8e8eFF), new Color(0x8f8fFF),

			new Color(0x9090FF), new Color(0x9191FF), new Color(0x9292FF), new Color(0x9393FF),
			new Color(0x9494FF), new Color(0x9595FF), new Color(0x9696FF), new Color(0x9797FF),
			new Color(0x9898FF), new Color(0x9999FF), new Color(0x9a9aFF), new Color(0x9b9bFF),
			new Color(0x9c9cFF), new Color(0x9d9dFF), new Color(0x9e9eFF), new Color(0x9f9fFF),

			new Color(0xa0a0FF), new Color(0xa1a1FF), new Color(0xa2a2FF), new Color(0xa3a3FF),
			new Color(0xa4a4FF), new Color(0xa5a5FF), new Color(0xa6a6FF), new Color(0xa7a7FF),
			new Color(0xa8a8FF), new Color(0xa9a9FF), new Color(0xaaaaFF), new Color(0xababFF),
			new Color(0xacacFF), new Color(0xadadFF), new Color(0xaeaeFF), new Color(0xafafFF),

			new Color(0xb0b0FF), new Color(0xb1b1FF), new Color(0xb2b2FF), new Color(0xb3b3FF),
			new Color(0xb4b4FF), new Color(0xb5b5FF), new Color(0xb6b6FF), new Color(0xb7b7FF),
			new Color(0xb8b8FF), new Color(0xb9b9FF), new Color(0xbabaFF), new Color(0xbbbbFF),
			new Color(0xbcbcFF), new Color(0xbdbdFF), new Color(0xbebeFF), new Color(0xbfbfFF),

			new Color(0xc0c0FF), new Color(0xc1c1FF), new Color(0xc2c2FF), new Color(0xc3c3FF),
			new Color(0xc4c4FF), new Color(0xc5c5FF), new Color(0xc6c6FF), new Color(0xc7c7FF),
			new Color(0xc8c8FF), new Color(0xc9c9FF), new Color(0xcacaFF), new Color(0xcbcbFF),
			new Color(0xccccFF), new Color(0xcdcdFF), new Color(0xceceFF), new Color(0xcfcfFF),

			new Color(0xd0d0FF), new Color(0xd1d1FF), new Color(0xd2d2FF), new Color(0xd3d3FF),
			new Color(0xd4d4FF), new Color(0xd5d5FF), new Color(0xd6d6FF), new Color(0xd7d7FF),
			new Color(0xd8d8FF), new Color(0xd9d9FF), new Color(0xdadaFF), new Color(0xdbdbFF),
			new Color(0xdcdcFF), new Color(0xddddFF), new Color(0xdedeFF), new Color(0xdfdfFF),

			new Color(0xe0e0FF), new Color(0xe1e1FF), new Color(0xe2e2FF), new Color(0xe3e3FF),
			new Color(0xe4e4FF), new Color(0xe5e5FF), new Color(0xe6e6FF), new Color(0xe7e7FF),
			new Color(0xe8e8FF), new Color(0xe9e9FF), new Color(0xeaeaFF), new Color(0xebebFF),
			new Color(0xececFF), new Color(0xededFF), new Color(0xeeeeFF), new Color(0xefefFF),

			new Color(0xf0f0FF), new Color(0xf1f1FF), new Color(0xf2f2FF), new Color(0xf3f3FF),
			new Color(0xf4f4FF), new Color(0xf5f5FF), new Color(0xf6f6FF), new Color(0xf7f7FF),
			new Color(0xf8f8FF), new Color(0xf9f9FF), new Color(0xfafaFF), new Color(0xfbfbFF),
			new Color(0xfcfcFF), new Color(0xfdfdFF), new Color(0xfefeFF), new Color(0xffffFF),
	};

	public static final Color shade(Color[] table, double b)
	{
		Color c = null;
		if (b <= 0) {
			c = table[0];
		} else if (1 <= b) {
			c = table[table.length-1];
		} else {
			c = table[(int)(b * table.length)];
		}
		
		return c;
	}

	// TODO class Mag
	public static class Mag {
		private static final double max_v =  8.0;
		private static final double min_v = -1.5;
		private static final double span  = max_v - min_v;
		private static final Mag[] mag = {
				new Mag(5, new Color(0xFFFFFF)),		// -1.5 to -1.0
				new Mag(5, new Color(0xF0F0F0)),		// -1.0 to -0.5
				new Mag(5, new Color(0xE0E0E0)),		// -0.5 to +0.0
				new Mag(5, new Color(0xD0D0D0)),		// +0.0 to +0.5
				new Mag(5, new Color(0xC0C0C0)),		// +0.5 to +1.0
				new Mag(4, new Color(0xB0B0B0)),		// +1.0 to +1.5
				new Mag(4, new Color(0xA0A0A0)),		// +1.5 to +2.0
				new Mag(4, new Color(0x808080)),		// +2.0 to +2.5
				new Mag(3, new Color(0x707070)),		// +2.5 to +3.0
				new Mag(3, new Color(0x606060)),		// +3.0 to +3.5
				new Mag(3, new Color(0x505050)),		// +3.5 to +4.0
				new Mag(3, new Color(0x404040)),		// +4.0 to +4.5
				new Mag(3, new Color(0x303030)),		// +4.5 to +5.0
				new Mag(2, new Color(0x202020)),		// +5.0 to +5.5
				new Mag(2, new Color(0x101010)),		// +5.5 to +6.0
				new Mag(2, new Color(0x0C0C0C)),		// +6.0 to +6.5
				new Mag(2, new Color(0x080808)),		// +6.5 to +7.0
				new Mag(2, new Color(0x040404)),		// +7.0 to +7.5
				new Mag(2, new Color(0x020202)),		// +7.5 to +8.0
			};

		public final int   width;
		public final Color color;
		public  Mag(int w, Color c)
		{
			width = w;
			color = c;
		}

		public static final Mag mag_to_cw(double ma)
		{
			int idx = (int)(mag.length * ((ma - min_v)/span));
			idx = (idx < 0) ? 0 : ((idx < mag.length) ? idx : mag.length - 1);

			return mag[idx];
		}
	}
	
	public int hours(double h)
	{
		h += 24000;
		return ((int) h) % 24;
	}
	
	public int ra_minutes(double h) 
	{
		h += 24000;
		return (int) ((h - (int) h) * 60);
	}
	
	public int dec_minutes(double h) 
	{
		h = Math.abs(h);
		return (int) ((h - (int) h) * 60);
	}


	public class Paint_NWSE extends Paint {
		public Paint_NWSE()
		{
			top_label           = "North";
			right_label         = "West";
			bottom_label        = "South";
			left_label          = "East";
		}

		@Override public Point ra_dec_to_xy(double ra, double dec)
		{
			double mind = min_dec_deg;
			double maxd = max_dec_deg;
			double spnd = maxd - mind;

			int miny = chart_area.y + chart_area.height;
			int maxy = chart_area.y;
			double spny = maxy - miny;

			double minr = min_ra_hrs;
			double maxr = max_ra_hrs;
			double spnr = maxr - minr;

			int minx = chart_area.x + chart_area.width;
			int maxx = chart_area.x;
			double spnx = maxx - minx;

			int x = (int)(spnx*(ra  - minr)/spnr + minx);
			int y = (int)(spny*(dec - mind)/spnd + miny);

			return new Point(x, y);
		}

		@Override public Point solar_dec_to_xy(double hrs, double dec)
		{
				double mind = min_dec_deg;
				double maxd = max_dec_deg;
				double spnd = maxd - mind;

				int miny = chart_area.y + chart_area.height;
				int maxy = chart_area.y;
				double spny = maxy - miny;

				double minh = min_sol_hrs;
				double maxh = max_sol_hrs;
				double spnh = maxh - minh;

				int minx = chart_area.x + chart_area.width;
				int maxx = chart_area.x;
				double spnx = maxx - minx;

				int x = (int)(spnx*(hrs - minh)/spnh + minx);
				int y = (int)(spny*(dec - mind)/spnd + miny);

				return new Point(x, y);
		}
		
		@Override public double x_to_ra(int x)
		{
			double minr = min_ra_hrs;
			double maxr = max_ra_hrs;
			double spnr = maxr - minr;

			int minx = chart_area.x + chart_area.width;
			int maxx = chart_area.x;
			double spnx = maxx - minx;

			double ra = (double) (spnr * (double) (x - minx) / (double) spnx + minr);

			return ra;
		}

		@Override public double y_to_dec(int y)
		{
			double mind = min_dec_deg;
			double maxd = max_dec_deg;
			double spnd = maxd - mind;

			int miny = chart_area.y + chart_area.height;
			int maxy = chart_area.y;
			double spny = maxy - miny;

			double dec = (double) (spnd * (double) (y - miny) / (double) spny + mind);

			return dec;
		}

		@Override public double x_to_solar_hrs(int x)
		{
			double minh = min_sol_hrs;
			double maxh = max_sol_hrs;
			double spnh = maxh - minh;

			int minx = chart_area.x + chart_area.width;
			int maxx = chart_area.x;
			double spnx = maxx - minx;

			double hrs = (double) (spnh * (double) (x  - minx) / (double) spnx + minh);

			return hrs;
		}

		@Override public int hrs_to_width(double hrs)
		{
			return deg_to_width(15 * hrs);
		}

		@Override public int deg_to_width(double deg)
		{
			return (int) (deg * chart_area.width / (max_dec_deg - min_dec_deg));
		}
	}

	
    public static final String FILE        = "File";
    public static final String LOCATION    = "Time & Location";
    public static final String APPEARANCE  = "Appearance";
    public static final String CATALOGS    = "Catalogs";
    public static final String EQUIPMENT   = "Equipment";
    public static final String HELP        = "Help";

    public static final MenuBar mbar = new MenuBar();

    public final Menu file        = new FileMenu        (this, FILE);
    public final Menu location    = new TimeLocationMenu(this, LOCATION);
    public final Menu appearance  = new AppearanceMenu  (this, APPEARANCE);
    public final Menu catalogs    = new CatalogMenu     (this, CATALOGS);
    public final Menu help        = new HelpMenu        (this, HELP);

    public void make_menu_bar()
    {
		setFont(PersistentState.get_font());

    	mbar.add(file);
    	mbar.add(location);
    	mbar.add(appearance);
    	mbar.add(catalogs);
    	mbar.add(help);

    	setMenuBar(mbar);
    }

    public void set_menu_font(Font font)
    {
    	if (font != null) {
    		mbar.setFont(font);
    		file.setFont(font);
    		location.setFont(font);
    		appearance.setFont(font);
    		catalogs.setFont(font);
    		help.setFont(font);
    	}
    }

    public void exit()
    {
    	// System.out.printf("%s: %d: exiting...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
    	dispose();
    	PersistentState.save_state();
		System.exit(0);
    }

    public static class NameRaDec {
    	public final String name;
    	public final double right_ascension;
    	public final double declination;
 
    	public NameRaDec(String n, double r, double d)
    	{
    		name            = n;
    		right_ascension = r;
    		declination     = d;
    	}
    }

    // TODO
    public double custom_right_ascension;
	public double custom_declination;

    private void add_to_dso_alias(Element[][] buf, Rectangle chart, Point p, int width, int height, Element elt)
    {
    	if (elt != null) {
			for (int i=0; i < width; i++) {
				int x = p.x - chart.x - width/2 + i;
				if (0 <= x && x < buf.length) {
					for (int j=0; j < height; j++) {
						int y = p.y - chart.y - width/2 + j;
						if (0 <= y && y < buf[x].length) {
							buf[x][y] = elt;
						}
					}
				}
			}
    	}
    }

	public static void brighter()
	{
		background           = background          .brighter();
		foreground           = foreground          .brighter();
		text_color           = text_color          .brighter();
		major_line_color     = major_line_color    .brighter();
		minor_line_color     = minor_line_color    .brighter();
		scale_text_color     = scale_text_color    .brighter();
		sunset_color         = sunset_color        .brighter();
		sunrise_color        = sunrise_color       .brighter();
		twilight_color       = twilight_color      .brighter();
		midnight_color       = midnight_color      .brighter();
		current_time_color   = current_time_color  .brighter();
		constellation_color  = constellation_color .brighter();
		new_moon_color       = new_moon_color      .brighter();
		waxing_moon_color    = waxing_moon_color   .brighter();
		full_moon_color      = full_moon_color     .brighter();
		waning_moon_color    = waning_moon_color   .brighter();
		lunar_fullness_color = lunar_fullness_color.brighter();
		moonrise_color       = moonrise_color      .brighter();
		moonset_color        = moonset_color       .brighter();
		sun_color            = sun_color           .brighter();
		gas_giant_color      = gas_giant_color     .brighter();
		inner_planet_color   = inner_planet_color  .brighter();
		mars_color           = mars_color          .brighter();
		zoom_color           = zoom_color          .brighter();
	}
	
	public static void darker()
	{
		background           = background          .darker();
		foreground           = foreground          .darker();
		text_color           = text_color          .darker();
		major_line_color     = major_line_color    .darker();
		minor_line_color     = minor_line_color    .darker();
		scale_text_color     = scale_text_color    .darker();
		sunset_color         = sunset_color        .darker();
		sunrise_color        = sunrise_color       .darker();
		twilight_color       = twilight_color      .darker();
		midnight_color       = midnight_color      .darker();
		current_time_color   = current_time_color  .darker();
		constellation_color  = constellation_color .darker();
		new_moon_color       = new_moon_color      .darker();
		waxing_moon_color    = waxing_moon_color   .darker();
		full_moon_color      = full_moon_color     .darker();
		waning_moon_color    = waning_moon_color   .darker();
		lunar_fullness_color = lunar_fullness_color.darker();
		moonrise_color       = moonrise_color      .darker();
		moonset_color        = moonset_color       .darker();
		sun_color            = sun_color           .darker();
		gas_giant_color      = gas_giant_color     .darker();
		inner_planet_color   = inner_planet_color  .darker();
		mars_color           = mars_color          .darker();
		zoom_color           = zoom_color          .darker();
	}

	public static void reset_color()
	{
		background                 = new Color(0,0,0);
		foreground                 = Color.WHITE;
		text_color                 = Color.MAGENTA;
		major_line_color           = Color.GRAY.darker().darker().darker().darker();
		minor_line_color           = Color.GRAY.darker().darker().darker().darker().darker().darker();
		scale_text_color           = Color.GRAY.darker();
		sunset_color               = Color.ORANGE;
		sunrise_color              = sunset_color;
		twilight_color             = Color.RED;
		midnight_color             = new Color(0x2020FF);
		current_time_color         = Color.GREEN;
		constellation_color        = Color.CYAN;
		Color moon                 = shade(blue_table, 0.50);
		new_moon_color             = moon;
		waxing_moon_color          = moon;
		full_moon_color            = moon;
		waning_moon_color          = moon;
		lunar_fullness_color       = moon;
		moonrise_color             = moon;
		moonset_color              = moon;
		sun_color                  = Color.YELLOW.brighter();
		gas_giant_color            = Color.CYAN;
		inner_planet_color         = Color.orange.darker();
		mars_color                 = new Color(0xFF4010);
		zoom_color                 = Color.MAGENTA;
		target_color               = Color.RED;

		caldwell_color             = Color.MAGENTA;
		herschel_color             = Color.MAGENTA;
		messier_color              = Color.MAGENTA;
		sharpless_color            = Color.MAGENTA;

		arp_color                  = Color.MAGENTA;
		ic_color                   = Color.MAGENTA;
		ngc_color                  = Color.MAGENTA;
		ugc_color                  = Color.MAGENTA;
		vsx_color                  = Color.MAGENTA;
		wds_color                  = Color.MAGENTA;

		cluster_color              = Color.CYAN;
		galaxy_color               = new Color( 0x8040FF );
		nebula_color               = Color.GREEN;

		double_stars_color         = Color.MAGENTA;
		planetary_nebulae_color    = Color.MAGENTA;
		gaseous_nebulae_color      = Color.MAGENTA;
		open_clusters_color        = Color.MAGENTA;
		globular_clusters_color    = Color.MAGENTA;
		galaxies_color             = Color.MAGENTA;
		clusters_of_galaxies_color = Color.MAGENTA;
		anonymous_galaxies_color   = Color.MAGENTA;
		southern_skies_color       = Color.MAGENTA;
		variable_stars_color       = Color.MAGENTA;

		horizon_line_color         = new Color( 0x00FF00 );
		horizon_plus_15_color      = new Color( 0x20FF20 );
		horizon_plus_30_color      = new Color( 0x40FF40 );
		ecliptic_plane_color       = Color.YELLOW;
		galactic_plane_color       = Color.BLUE;
	}
	
	public static class ColorLabelValue {
		public final Color  color;
		public final String label;
		public final String value;
		
		public ColorLabelValue(Color c, String l, String v)
		{
			color = c;
			label = l;
			value = v;
		}
	}
    
	JPopupMenu popupMenu = new JPopupMenu();
	
	@Override public void mouseClicked(MouseEvent evt)
	{
		Point e = evt.getPoint();
		Point p = new Point(e.x - chart_area.x, e.y - chart_area.y);

		if (0 < p.x && p.x < chart_area.width && 0 < p.y && p.y < chart_area.height) {
			double r = paint.x_to_ra(e.x);
			double d = paint.y_to_dec(e.y);

			if (evt.isAltDown()) {
				if (evt.isShiftDown() && ! evt.isControlDown()) {
					// ALT click -- selects end points for angle of separation calculations, SHFT click for the first end point, CTRL click for the second end point
					alt_shft_click(r, d, evt.getClickCount());
				} else if (! evt.isShiftDown() && evt.isControlDown()) {
					alt_ctrl_click(r, d, evt.getClickCount());
				} else if (evt.isShiftDown() && evt.isControlDown()) {
					alt_shft_ctrl_click(r, d, evt.getClickCount());
				} else {
					alt_click(r, d, evt.getClickCount());
				}
			} else if (evt.isAltDown() || evt.isControlDown() || evt.isShiftDown()) {
				selected_dso = null;
			} else {
				if (dso_alias != null && 0 <= p.x && p.x < dso_alias.length && dso_alias[p.x] != null && 0 <= p.y && p.y < dso_alias[p.x].length && dso_alias[p.x][p.y] != null) {
					selected_dso = dso_alias[p.x][p.y];
				} else {
					if (dso_alias_table != null) {
						selected_dso = dso_alias_table.find("Custom");
						selected_dso.update(paint.x_to_ra(e.x), paint.y_to_dec(e.y));
					}					
				}
			}
		}
	}
	@Override public void mouseEntered(MouseEvent evt) {}
	@Override public void mouseExited(MouseEvent evt)  {}
	@Override public void mousePressed(MouseEvent evt) {}
	@Override public void mouseReleased(MouseEvent evt){}
	@Override public void mouseDragged(MouseEvent evt) {}
	@Override public void mouseMoved(MouseEvent evt)
	{
		Point e = evt.getPoint();
		Point p = new Point(e.x - chart_area.x, e.y - chart_area.y);
		popupMenu.removeAll();
		if (0 < p.x && p.x < chart_area.width && 0 < p.y && p.y < chart_area.height) {
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			if (dso_alias != null && 0 <= p.x && p.x < dso_alias.length && dso_alias[p.x] != null && 0 <= p.y && p.y < dso_alias[p.x].length) {
				if (dso_alias[p.x][p.y] != null) {
					// TODO JMenuItem item = new JMenuItem(frame.dso_alias_table.common(frame.dso_alias[p.x][p.y]));
			        JMenuItem item = new JMenuItem(dso_alias[p.x][p.y].names()[0]);
			        popupMenu.add(item);
			        popupMenu.show(getComponentAt(e), e.x, e.y);
				} else {
					if (null == null) {
				        JMenuItem item = new JMenuItem(String.format("%s, %s", 
				        		PracticalAstronomy.decimal_hours_to_str_hm(paint.x_to_ra(e.x)), 
				        		PracticalAstronomy.decimal_degrees_to_str_dm(paint.y_to_dec(e.y))));
				        popupMenu.add(item);
				        popupMenu.show(getComponentAt(e), e.x, e.y);
					} else {
						popupMenu.setVisible(false);
					}
				}
			} else {
				popupMenu.setVisible(false);
			}
		} else {
			popupMenu.setVisible(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override public void windowActivated(WindowEvent evt)   {}
	@Override public void windowClosed(WindowEvent evt)      {}
	@Override public void windowClosing(WindowEvent evt)     { exit(); }
	@Override public void windowDeactivated(WindowEvent evt) {}
	@Override public void windowDeiconified(WindowEvent evt) {}
	@Override public void windowIconified(WindowEvent evt)   {}
	@Override public void windowOpened(WindowEvent evt)      {}

    // TODO various mouse clicks 
	public void click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d:               click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d:     SHFT      click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void ctrl_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d:          CTRL click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_ctrl_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d:     SHFT CTRL click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_ctrl_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d: ALT      CTRL click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d: ALT SHFT      click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d: ALT           click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_ctrl_click(double r, double d, int clicks)
	{
		System.out.printf("%s: %d: ALT SHFT CTRL click: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}
	public void press(double r, double d)
	{
		System.out.printf("%s: %d:               press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_press(double r, double d)
	{
		System.out.printf("%s: %d:     SHFT      press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void ctrl_press(double r, double d)
	{
		System.out.printf("%s: %d:          CTRL press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_ctrl_press(double r, double d)
	{
		System.out.printf("%s: %d:     SHFT CTRL press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_ctrl_press(double r, double d)
	{
		System.out.printf("%s: %d: ALT      CTRL press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_press(double r, double d)
	{
		System.out.printf("%s: %d: ALT SHFT      press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_press(double r, double d)
	{
		System.out.printf("%s: %d: ALT           press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_ctrl_press(double r, double d)
	{
		System.out.printf("%s: %d: ALT SHFT CTRL press: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}
	public void release(double r, double d)
	{
		System.out.printf("%s: %d:               release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_release(double r, double d)
	{
		System.out.printf("%s: %d:     SHFT      release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void ctrl_release(double r, double d)
	{
		System.out.printf("%s: %d:          CTRL release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void shft_ctrl_release(double r, double d)
	{
		System.out.printf("%s: %d:     SHFT CTRL release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_ctrl_release(double r, double d)
	{
		System.out.printf("%s: %d: ALT      CTRL release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_release(double r, double d)
	{
		System.out.printf("%s: %d: ALT SHFT      release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_release(double r, double d)
	{
		System.out.printf("%s: %d: ALT           release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public void alt_shft_ctrl_release(double r, double d)
	{
		System.out.printf("%s: %d: ALT SHFT CTRL release: set P0 RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	public static class LoadDatabases extends Thread {
    	public static LoadDatabases load(MainFrame mf, String p)
    	{
    		LoadDatabases db = new LoadDatabases(mf, p);
    		db.start();
    		
    		return db;
    	}

    	private final MainFrame frame;
    	private final String    path;

    	public LoadDatabases(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		LoadIAU iau = new LoadIAU(frame, path);
    		iau.start();

    		LoadYBS ybs = new LoadYBS(frame, path);
    		ybs.start();

    		LoadNGC ngc = new LoadNGC(frame, path);
    		ngc.start();

    		LoadDso dso = new LoadDso(frame, path);
    		dso.start();

    		LoadSh2 sh2 = new LoadSh2(frame, path);
    		sh2.start();

    		LoadUGC ugc = new LoadUGC(frame, path);
    		ugc.start();

    		LoadVSX vsx = new LoadVSX(frame, path);
    		vsx.start();

    		LoadWDS wds = new LoadWDS(frame, path);
    		wds.start();

    		LoadCal cal = new LoadCal(frame, path, ngc);
    		cal.start();

    		LoadMes mes = new LoadMes(frame, path, ngc);
    		mes.start();

    		LoadArp arp = new LoadArp(frame, path, ngc, ugc, mes);
    		arp.start();

    		LoadH04 h04 = new LoadH04(frame, path, ngc);
    		h04.start();

    		LoadH25 h25 = new LoadH25(frame, path, ngc, ugc, h04);
    		h25.start();
    		
    		LoadAlias ali = new LoadAlias(frame, path, iau, ybs, ngc, dso, cal, h04, h25, mes, sh2, ugc, arp, vsx, wds);
    		ali.start();

    		LoadUsCities us = new LoadUsCities(frame, path);
    		us.start();

    		LoadWorldCities world = new LoadWorldCities(frame, path);
    		world.start();

    		LoadWebbDS wsc = new LoadWebbDS(frame, path);
    		wsc.start();

    		LoadWebbPN wpn = new LoadWebbPN(frame, path);
    		wpn.start();

    		LoadWebbGN wgn = new LoadWebbGN(frame, path);
    		wgn.start();

    		LoadWebbOC woc = new LoadWebbOC(frame, path);
    		woc.start();

    		LoadWebbGC wgc = new LoadWebbGC(frame, path);
    		wgc.start();

    		LoadWebbSG wsg = new LoadWebbSG(frame, path);
    		wsg.start();

    		LoadWebbCG wcg = new LoadWebbCG(frame, path);
    		wcg.start();

    		LoadWebbAG wag = new LoadWebbAG(frame, path);
    		wag.start();

    		LoadWebbSS wss = new LoadWebbSS(frame, path);
    		wss.start();

    		LoadWebbVS wvs = new LoadWebbVS(frame, path);
    		wvs.start();

    		LoadFavoriteDSOs fav = new LoadFavoriteDSOs(frame, path);
    		fav.start();
    	}
    }

    public static class LoadIAU extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadIAU(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.iau_csn = new IAUCatalog(path + frame.iau_csn_name);
				CatalogMenu.nds.setEnabled(true);
				// System.out.printf("%s: %d: IAU done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadYBS extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadYBS(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.ybsc = new YaleBrightStarAscCatalog(path + frame.ybsc_name);
				CatalogMenu.ybs.setEnabled(true);
				// System.out.printf("%s: %d: YBS done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadNGC extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadNGC(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.ngc_ic = new NgcIcCatalog(path + frame.ngc_ic_path);
				CatalogMenu.ngc.setEnabled(true);
				CatalogMenu. ic.setEnabled(true);
				// System.out.printf("%s: %d: NGC done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadCal extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadNGC   ngc;

    	public LoadCal(MainFrame mf, String p, LoadNGC n)
    	{
    		frame = mf;
    		path  = p;
    		ngc   = n;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			ngc.join();
				frame.caldwell = new CaldwellCatalog(path + frame.caldwell_name, frame.ngc_ic);
				CatalogMenu.cal.setEnabled(true);
				// System.out.printf("%s: %d: Caldwell done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadMes extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadNGC   ngc;

    	public LoadMes(MainFrame mf, String p, LoadNGC n)
    	{
    		frame = mf;
    		path  = p;
    		ngc   = n;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			ngc.join();
				frame.messier = new MessierCatalog(path + frame.messier_name, frame.ngc_ic);
				CatalogMenu.mes.setEnabled(true);
				// System.out.printf("%s: %d: Messier done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadDso extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadDso(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.named_dsos = new NamedDsos(path + frame.named_dsos_name);
				// System.out.printf("%s: %d: Named DSOs done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadSh2 extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadSh2(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.sharpless = new SharplessCatalog(path + frame.sharpless_name);
				CatalogMenu.sh2.setEnabled(true);
				// System.out.printf("%s: %d: Sharpless2 done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadUGC extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadUGC(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
				frame.ugc = new UgcCatalog(path + frame.ugc_path);
				CatalogMenu.ugc.setEnabled(true);
				// System.out.printf("%s: %d: UGC done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadVSX extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadVSX(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
		    	frame.vsx = new VsxCatalog(path + frame.vsx_path);
				CatalogMenu.vsx.setEnabled(true);
				// System.out.printf("%s: %d: VSX done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWDS extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWDS(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
		    	frame.wds = new WdsCatalog(path + frame.wds_path);
				CatalogMenu.wds.setEnabled(true);
				// System.out.printf("%s: %d: WDS done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadH04 extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadNGC   ngc;

    	public LoadH04(MainFrame mf, String p, LoadNGC n)
    	{
    		frame = mf;
    		path  = p;
    		ngc   = n;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			ngc.join();
    			frame.herschel_400 = new Herschel400(path + frame.herschel400_name, frame.ngc_ic);
				CatalogMenu.h04.setEnabled(true);
				// System.out.printf("%s: %d: Herschel 400 done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadH25 extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadNGC   ngc;
    	private final LoadUGC   ugc;
    	private final LoadH04   h04;

    	public LoadH25(MainFrame mf, String p, LoadNGC n, LoadUGC u, LoadH04 h)
    	{
    		frame = mf;
    		path  = p;
    		ngc   = n;
    		ugc   = u;
    		h04   = h;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			ngc.join();
    			ugc.join();
    			h04.join();
    	    	frame.herschel_2500 = new Herschel2500(path + frame.herschel2500_name, frame.ngc_ic, frame.ugc, frame.herschel_400);
				CatalogMenu.h25.setEnabled(true);
				// System.out.printf("%s: %d: Herschel 2,500 done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadArp extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadNGC   ngc;
    	private final LoadUGC   ugc;
    	private final LoadMes   mes;

    	public LoadArp(MainFrame mf, String p, LoadNGC n, LoadUGC u, LoadMes m)
    	{
    		frame = mf;
    		path  = p;
    		ngc   = n;
    		ugc   = u;
    		mes   = m;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			ngc.join();
    			ugc.join();
    			mes.join();
    			frame.arp = new ArpCatalog(path + frame.arp_path, frame.ngc_ic, frame.ugc, frame.messier);
				CatalogMenu.arp.setEnabled(true);
				// System.out.printf("%s: %d: Arp done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadAlias extends Thread {
    	private final MainFrame frame;
    	private final String    path;
    	private final LoadIAU   iau;
    	private final LoadYBS   ybs;
    	private final LoadNGC   ngc;
    	private final LoadDso   dso;
    	private final LoadCal   cal;
    	private final LoadH04   h04;
    	private final LoadH25   h25;
    	private final LoadMes   mes;
    	private final LoadSh2   sh2;
    	private final LoadUGC   ugc;
    	private final LoadArp   arp;
    	private final LoadVSX   vsx;
    	private final LoadWDS   wds;

    	public LoadAlias(MainFrame mf, String p, LoadIAU i, LoadYBS y, LoadNGC n, LoadDso d, LoadCal c, LoadH04 h0, LoadH25 h2, LoadMes m, LoadSh2 s, LoadUGC u, LoadArp a, LoadVSX v, LoadWDS w)
    	{
    		frame = mf;
    		path  = p;
    		iau   = i;
    		ybs   = y;
    		ngc   = n;
    		dso   = d;
    		cal   = c;
    		h04   = h0;
    		h25   = h2;
    		mes   = m;
    		sh2   = s;
    		ugc   = u;
    		arp   = a;
    		vsx   = v;
    		wds   = w;
    	}

    	// dso_alias = new DsoAlias(iau_csn, ybsc, ngc_ic, caldwell, herschel, messier, sharpless, ugc, arp, vsx, wds);
    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			iau.join();
    			ybs.join();
    			ngc.join();
    			dso.join();
    			cal.join();
    			h04.join();
    			h25.join();
    			mes.join();
    			sh2.join();
    			ugc.join();
    			arp.join();
    			vsx.join();
    			wds.join();
				frame.dso_alias_table = new DsoAlias(frame.iau_csn, frame.ybsc, frame.ngc_ic, 
						frame.caldwell, frame.herschel_400, frame.herschel_2500, frame.messier, 
						frame.named_dsos, frame.sharpless, frame.ugc, frame.arp, frame.vsx, frame.wds);
		    	FileMenu.fv.setEnabled(true);
		    	FileMenu.fs.setEnabled(true);
		    	FileMenu.fa.setEnabled(true);
				// System.out.printf("%s: %d: DSO Alias done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadUsCities extends Thread {
    	private final MainFrame frame;
    	private final String path;

    	public LoadUsCities(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}
    	
    	public void run()
    	{
			try {
    			long ms = System.currentTimeMillis();
				frame.us_cities = new UsCitiesCatalog(path + frame.us_cities_path);
				TimeLocationMenu.us.setEnabled(true);
				// System.out.printf("%s: %d: US Cities done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWorldCities extends Thread {
    	private final MainFrame frame;
    	private final String path;

    	public LoadWorldCities(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}
    	
    	public void run()
    	{
			try {
    			long ms = System.currentTimeMillis();
				frame.world_cities = new WorldCitiesCatalog(path + frame.world_cities_path);
				TimeLocationMenu.world.setEnabled(true);
				// System.out.printf("%s: %d: World Cities done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbDS extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbDS(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			frame.double_stars = new WebbDoubleStars(path + frame.double_stars_path);
				CatalogMenu.wsc.setEnabled(true);
				// System.out.printf("%s: %d: Webb Double Stars done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbPN extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbPN(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.planetary_nebulae = new WebbPlanetaryNebulae(path + frame.planetary_nebulae_path);
				CatalogMenu.wpn.setEnabled(true);
				// System.out.printf("%s: %d: Webb Planetary Nebulae done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbGN extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbGN(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.gaseous_nebulae = new WebbGaseousNebulae(path + frame.gaseous_nebulae_path);
				CatalogMenu.wgn.setEnabled(true);
				// System.out.printf("%s: %d: Webb Gaseous Nebulae done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbOC extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbOC(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.open_clusters = new WebbOpenClusters(path + frame.open_clusters_path);
				CatalogMenu.woc.setEnabled(true);
				// System.out.printf("%s: %d: Webb open Clusters done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbGC extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbGC(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			frame.globular_clusters = new WebbGlobularClusters(path + frame.globular_clusters_path);
				CatalogMenu.wgc.setEnabled(true);
				// System.out.printf("%s: %d: Webb Globular Clusters done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbSG extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbSG(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.galaxies = new WebbGalaxies(path + frame.galaxies_path);
				CatalogMenu.wsg.setEnabled(true);
				// System.out.printf("%s: %d: Webb Galaxies done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbCG extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbCG(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.galaxy_clusters = new WebbClustersOfGalaxies(path + frame.galaxy_clusters_path);
				CatalogMenu.wcg.setEnabled(true);
				// System.out.printf("%s: %d: Webb Clusters of Galaxies done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbAG extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbAG(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.anonymous_galaxies = new WebbAnonymousGalaxies(path + frame.anonymous_galaxies_path);
				CatalogMenu.wag.setEnabled(true);
				// System.out.printf("%s: %d: Webb Anonymous Galaxies done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbSS extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbSS(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			frame.southern_sky = new WebbSouthernSky(path + frame.southern_sky_path);
				CatalogMenu.wss.setEnabled(true);
				// System.out.printf("%s: %d: Webb Southern Sky done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadWebbVS extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadWebbVS(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    	    	frame.webb_variable_stars = new WebbVariableStars(path + frame.variable_stars_path);
				CatalogMenu.wvs.setEnabled(true);
				// System.out.printf("%s: %d: Webb Variable Stars done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static class LoadFavoriteDSOs extends Thread {
    	private final MainFrame frame;
    	private final String    path;

    	public LoadFavoriteDSOs(MainFrame mf, String p)
    	{
    		frame = mf;
    		path  = p;
    	}

    	public void run()
    	{
    		try {
    			long ms = System.currentTimeMillis();
    			frame.favorite_dsos = new FavoriteDsoCatalog(path + frame.favorite_dsos_path);
				CatalogMenu.fav.setEnabled(true);
				// System.out.printf("%s: %d: Webb Favorite DSOs done. (%.3f)%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), (System.currentTimeMillis() - ms)/1000.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }


    public class TimeKeeper extends Thread {
    	public boolean pause    = false;
    	public TimeKeeper(int milliseconds)
    	{
    		sleep_ms = milliseconds;
    	}
    	
    	public void run()
    	{
    		while (true) {
    			try {
    				if (use_current_time) {
    					system_solar_time_ms = System.currentTimeMillis();
    				}
					Calendar c = Calendar.getInstance(PersistentState.timezone);
					int s = c.get(Calendar.SECOND);
					int m = c.get(Calendar.MILLISECOND);
					int ms = ((100*sleep_ms - 1000*s - m) % sleep_ms);
					ms = ((ms < 0) ? sleep_ms : ms) + 50;
					ms = (int) 500 - (m % 500);
					ms = sleep_ms;
					sleep(ms);
				} catch (InterruptedException e) {
					;
				}
    			if (! pause && mainframe.getGraphics() != null) {
    				mainframe.paint(mainframe.getGraphics());
    			}
    		}
    	}
    }
}
