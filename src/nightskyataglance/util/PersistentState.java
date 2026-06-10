package nightskyataglance.util;

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


import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import nightskyataglance.NightSkyAtAGlance;

public class PersistentState {

	public static String home_path = System.getProperty("user.home") + System.getProperty("file.separator") + "." + 
			NightSkyAtAGlance.CLASS(1).substring(NightSkyAtAGlance.CLASS(1).lastIndexOf('.')+1, NightSkyAtAGlance.CLASS(1).length()) + 
			System.getProperty("file.separator");

	public static Font default_font = new Font(Font.DIALOG, Font.PLAIN, 12);
	public static Font get_default_font() { return default_font; }

	public static LookAndFeel default_ui_mgr = UIManager.getLookAndFeel();	// Motif, Windows, Mac, Metal
	public static LookAndFeel get_default_ui_mgr() { return default_ui_mgr; }

	public static final String cache_path_file = "cache_path.nsc";
	public static String  cache_path   = home_path + "cache";
	public static boolean cache_images = false;

	public static final String location_file = "location.nsc";
	public static String   city     = "Albuquerque";
	public static String   state    = "New Mexico";
	public static String   tz_name  = "America/Denver";
	public static TimeZone timezone = TimeZone.getTimeZone(tz_name);

	public static double   true_latitude_deg  =   35.106766;
	public static double   true_longitude_deg = -106.5346;

	public static final String fonts_file = "fonts.nsc";
	public static Font font = default_font;
	public static Font set_font(Font f)       { if (f != null) { Font tmp = font; font = f; return tmp; } else { return font; } }
	public static Font get_font()             { return font; }
	public static Font get_main_font()        { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }
	public static Font get_menu_font()        { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }
	public static Font get_button_font()      { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }
	public static Font get_checkbox_font()    { return new Font(font.getFamily(), Font.BOLD,  font.getSize()); }
	public static Font get_combobox_font()    { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }
	public static Font get_label_font()       { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }
	public static Font get_radiobutton_font() { return new Font(font.getFamily(), Font.BOLD,  font.getSize()); }
	public static Font get_textfield_font()   { return new Font(font.getFamily(), Font.PLAIN, font.getSize()); }

	public static final String ui_mgr_file   = "ui_mgr.nsc";
	public static LookAndFeel ui_mgr         = default_ui_mgr;
	public static LookAndFeel set_ui_mgr(LookAndFeel m) { if (m != null) { LookAndFeel tmp = ui_mgr; ui_mgr = m; return tmp; } else { return ui_mgr; } }

	public static final String menu_height_file   = "menu_height.nsc";
	public static int menu_height  = 48;

	public static final String chart_margin_file  = "chart_margin.nsc";
	public static int chart_margin =  0;

	public static final String dso_icon_scale_factor_file = "dso_icon_scale_factor.nsc";
	public static double dso_icon_scale_factor = 1.0;

	public static String[] ui_mgr_list = null;
	public static String[] font_list   = null;


	public static void load_state() throws IOException
	{
		// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_path);
		File home_file = new File(home_path);
		if (home_file.isDirectory() && home_file.canRead()) {
			// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_file.getCanonicalPath());
			find_ui_mgrs();
			load_cache_path();
			load_location();
			load_menu_height();
			find_fonts();
			load_fonts();
			load_ui_mgr();
			load_dso_scale();
			load_chart_margin();
		}
	}

	public static void save_state()
	{
		File home_file = new File(home_path);
		// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_path);
		if (home_file.exists() || home_file.mkdirs()) {
			// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_path);
			save_cache_path();
			save_location();
			save_menu_height();
			save_fonts();
			save_ui_mgr();
			save_dso_scale();
			save_chart_margin();
		}
	}

	public static void load_cache_path()
	{
		File fd = new File(home_path, cache_path_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				cache_images = raf.readBoolean();
				cache_path  = raf.readUTF();
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void save_cache_path()
	{
		File fd = new File(home_path, cache_path_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.canWrite() || ! fd.exists()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeBoolean(cache_images);
				raf.writeUTF(cache_path);
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void load_location()
	{
		File fd = new File(home_path, location_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				city      = raf.readUTF();
				state     = raf.readUTF();
				tz_name   = raf.readUTF();

				true_latitude_deg  = raf.readDouble();
				true_longitude_deg = raf.readDouble();
				raf.close();

				timezone = TimeZone.getTimeZone(tz_name);
			} catch (IOException e) {
				;
			}
		}
	}

	public static void save_location()
	{
		File fd = new File(home_path, location_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.canWrite() || ! fd.exists()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeUTF(city);
				raf.writeUTF(state);
				raf.writeUTF(timezone.getID());

				raf.writeDouble(true_latitude_deg);
				raf.writeDouble(true_longitude_deg);
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}


	private static final String[] font_patterns = {
		".*[Aa][Rr][Ii][Ee][Ll].*",	
		".*[Cc][Oo][Mm][Ii][Cc].*",	
		".*[Cc][Oo][Uu][Rr][Ii][Ee][Rr].*",	
		".*[Dd][Ii][Aa][Ll][Oo][Gg].*",	
		".*[Ff][Rr][Ee][Ee].*",	
		".*[Gg][Aa][Rr][Aa][Mm][Oo][Nn][Dd].*",	
		".*[Hh][Ee][Ll][Vv][Ee][Tt][Ii][Cc][Aa].*",	
		".*[Mm][Oo][Nn][Oo][Ss][Pp][Aa][Cc][Ee][Dd].*",	
		".*[Nn][Ii][Mm][Bb][Uu][Ss].*",	
		".*[Rr][Oo][Mm][Aa][Nn].*",	
		"[Ss][Ee][Rr][Ii][Ff].*",
		".*[Ss][Aa][Nn][Ss].*[Ss][Ee][Rr][Ii][Ff].*",
		".*[Tt][Ii][Mm][Ee][Ss].*",	
		".*",	
	};

	/*
	private static final String[] font_exceptions = {
		".*[Jj][Ee][Tt][Bb][Rr][Aa][Ii][Nn][Ss].*",
		".*[Kk][Hh][Mm][Ee][Rr].*",	
		".*[Nn][Oo][Tt][Oo].*",	
	};
	*/

	public static void find_fonts()
	{
		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Hashtable<String,String> que = new Hashtable<String,String>();

		/*
		for (int i=0; i < names.length; i++) {
			String font = names[i];
			boolean found = false;
			for (String pattern: font_exceptions) {
				if (font.matches(pattern)) {
					found = true;
					break;
				}
			}
			if (! found) {
				que.put(font, font);
			}
		}
		*/

		for (int i=0; i < names.length; i++) {
			String font = names[i];
			boolean found = false;
			for (String pattern: font_patterns) {
				if (font.matches(pattern)) {
					found = true;
					break;
				}
			}
			if (found) {
				que.put(font, font);
			}
		}

		font_list = new String[que.size()];
		Enumeration<String> keys = que.keys();
		for (int i=0; i < font_list.length && keys.hasMoreElements(); i++) {
			font_list[i] = keys.nextElement();
		}
		Arrays.sort(font_list);

		/*
		for (int i=0; i < font_list.length; i++) {
			System.out.printf("%s: %d: font[%d]='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), i, font_list[i]);
		}
		 */
	}

	public static void load_fonts(boolean use_saved_fonts, String font_family, int font_size, Font default_font)
	{
		File fd = new File(home_path, fonts_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (use_saved_fonts && fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");

				String family = raf.readUTF();
				int    style  = raf.readInt();
				int    size   = raf.readInt();
				font = new Font(family, style, size);

				raf.close();
			} catch (IOException e) {
				;
			}
		} else if (font_family != null && 0 < font_size) {
			// both font family and font size were specified
			// e.g., --font-family dialog --font-size 12
			font = new Font(font_family, Font.PLAIN, font_size);
		} else if (font_family != null) {
			// font family was specified, but not font size
			// e.g., --font-family dialog
			font = new Font(font_family, Font.PLAIN, 12);
		} else if (0 < font_size) {
			// font size was specified, but not font family
			// e.g., --font-size 12
			font = new Font("Dialog", Font.PLAIN, font_size);
		} else if (default_font != null) {
			// neither font family nor size were specified, but a default font was provided
			font = default_font;
		} else {
			// none of the above was specified, fall back on 12pt Dialog
			font = new Font("Dialog", Font.PLAIN, 12);
		}
	}

	public static void load_fonts()
	{
		File fd = new File(home_path, fonts_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");

				String family = raf.readUTF();
				int    style  = raf.readInt();
				int    size   = raf.readInt();
				font = new Font(family, style, size);

				raf.close();
			} catch (IOException e) {
				;
			}
		} else {
			font = default_font;
		}
	}

	public static void save_fonts()
	{
		File fd = new File(home_path, fonts_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (font != null && (fd.canWrite() || ! fd.exists())) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeUTF(font.getFamily());
				raf.writeInt(font.getStyle());
				raf.writeInt(font.getSize());

				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void find_ui_mgrs()
	{
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		ui_mgr_list = new String[infos.length];
		for (int i=0; i < infos.length; i++) {
			LookAndFeelInfo info = infos[i];
			ui_mgr_list[i] = info.getName();
		}
		Arrays.sort(ui_mgr_list);
	}

	public static void set_ui_mgr(String ui_mgr_name)
	{
		try {
			ui_mgr = UIManager.createLookAndFeel(ui_mgr_name);
			UIManager.setLookAndFeel(ui_mgr);
		} catch (UnsupportedLookAndFeelException e) {
			System.out.printf("%s: %d: unsupported look-and-feel (UI manager) '%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), ui_mgr_name);
		}
	}

	public static void load_ui_mgr()
	{
		File fd = new File(home_path, ui_mgr_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			String ui_mgr_name = null;
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				ui_mgr_name = raf.readUTF();
				raf.close();
				set_ui_mgr(ui_mgr_name);
			} catch (IOException e) {
				System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), e.getMessage());
			}
		}
	}

	public static void save_ui_mgr()
	{
		File fd = new File(home_path, ui_mgr_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (ui_mgr != null && (fd.canWrite() || ! fd.exists())) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeUTF(ui_mgr.getID());
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void load_menu_height()
	{
		File fd = new File(home_path, menu_height_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				menu_height = raf.readInt();
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void save_menu_height()
	{
		File fd = new File(home_path, menu_height_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.canWrite() || ! fd.exists()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeInt(menu_height);
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void load_dso_scale()
	{
		File fd = new File(home_path, dso_icon_scale_factor_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				dso_icon_scale_factor = raf.readDouble();
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void save_dso_scale()
	{
		File fd = new File(home_path, dso_icon_scale_factor_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.canWrite() || ! fd.exists()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeDouble(dso_icon_scale_factor);
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void load_chart_margin()
	{
		File fd = new File(home_path, chart_margin_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.isFile() && fd.canRead()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "r");
				chart_margin = raf.readInt();
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	public static void save_chart_margin()
	{
		File fd = new File(home_path, chart_margin_file);
		// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
		if (fd.canWrite() || ! fd.exists()) {
			// System.out.printf("%s: %d: fd='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), fd.getAbsoluteFile());
			try {
				RandomAccessFile raf = new RandomAccessFile(fd, "rw");
				raf.writeInt(chart_margin);
				raf.close();
			} catch (IOException e) {
				;
			}
		}
	}

	/*/
	 	java.runtime.name='OpenJDK Runtime Environment'
		java.vm.version='21.0.10+7-LTS'
		sun.boot.library.path='/app/eclipse/plugins/org.eclipse.justj.openjdk.hotspot.jre.full.linux.x86_64_21.0.10.v20260205-0638/jre/lib'
		java.vm.vendor='Eclipse Adoptium'
		java.vendor.url='https://adoptium.net/'
		path.separator=':'
		java.vm.name='OpenJDK 64-Bit Server VM'
		user.country='US'
		sun.java.launcher='SUN_STANDARD'
		java.vm.specification.name='Java Virtual Machine Specification'
		user.dir='/home/dmpase/eclipse-workspace/org.hypercomputing'
		java.vm.compressedOopsMode='Zero based'
		java.runtime.version='21.0.10+7-LTS'
		os.arch='amd64'
		java.io.tmpdir='/tmp'
		line.separator='%n'
		java.vm.specification.vendor='Oracle Corporation'
		stderr.encoding='UTF-8'
		os.name='Linux'
		sun.jnu.encoding='UTF-8'
		stdout.encoding='UTF-8'
		java.library.path=':/usr/java/packages/lib:/usr/lib64:/lib64:/lib:/usr/lib'
		jdk.debug='release'
		java.class.version='65.0'
		java.specification.name='Java Platform API Specification'
		sun.management.compiler='HotSpot 64-Bit Tiered Compilers'
		os.version='6.17.0-29-generic'
		user.home='/home/dmpase'
		jdk.module.path='/home/dmpase/eclipse-workspace/org.hypercomputing/bin'
		file.encoding='UTF-8'
		java.specification.version='21'
		user.name='dmpase'
		java.class.path=''
		java.vm.specification.version='21'
		sun.arch.data.model='64'
		sun.java.command='org.hypercomputing/nightskyataglance.util.PersistentState'
		java.home='/app/eclipse/plugins/org.eclipse.justj.openjdk.hotspot.jre.full.linux.x86_64_21.0.10.v20260205-0638/jre'
		user.language='en'
		java.specification.vendor='Oracle Corporation'
		java.vm.info='mixed mode, sharing'
		java.version='21.0.10'
		native.encoding='UTF-8'
		java.vendor='Eclipse Adoptium'
		file.separator='/'
		java.version.date='2026-01-20'
		jdk.module.main='org.hypercomputing'
		jdk.module.main.class='nightskyataglance.util.PersistentState'
		java.vendor.url.bug='https://github.com/adoptium/adoptium-support/issues'
		sun.io.unicode.encoding='UnicodeLittle'
		sun.cpu.endian='little'
		java.vendor.version='Temurin-21.0.10+7'
	/*/

	/*/
	public static void main(String[] args)
	{
		Properties p = System.getProperties();

		Enumeration<?> n = p.propertyNames();
		while (n.hasMoreElements()) {
			String key = n.nextElement().toString();
			System.out.printf("%s='%s'%n", key, System.getProperty(key));
		}
		find_fonts(null);
	}
	/*/
}
