package nightskyataglance;

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


import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.UIManager;

import nightskyataglance.gui.HelpMenu;
import nightskyataglance.gui.MainFrame;
import nightskyataglance.util.LoadImageCache;
import nightskyataglance.util.PersistentState;

/*******************************************************************************
 *                                                                             *
 * New Features                                                                *
 *                                                                             *
 * o Add "Angle of Separation" menu, dialog (SeparationDialog), and analysis   *
 *                                                                             *
 * o Add "Closest Approach" menu, dialog (ApproachDialog), and analysis        *
 *                                                                             *
 * o Add primitive image cache menu and feature                                *
 *                                                                             *
 *******************************************************************************/

public class NightSkyAtAGlance {

	public static final String title   = "Night Sky at a Glance";
	public static       String version = read_version_file("/data/nightsky/version.txt");

	public static final String fallback_version = "2026.06.21.00";

	public NightSkyAtAGlance() 
	{
	}

	public static void main(String[] args) throws IOException, URISyntaxException 
	{
		PersistentState.load_state();
		PersistentState.font = (PersistentState.font != null) ? PersistentState.font : PersistentState.default_font;

		// hierarchy for options such as font families, font sizes, and DSO icon scales:
		// 1. command line
		// 2. persistent state (stored from a previous use), when it exists
		// 3. system default (e.g., Dialog-PLAIN-12 and 1.0)
		for (int i=0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("--version") || args[i].equalsIgnoreCase("-version")) {
				// print the version number and exit
				System.out.println(version);
				System.exit(0);
			} else if (args[i].equalsIgnoreCase("--license") || args[i].equalsIgnoreCase("-license")) {
				// print the license and exit
				System.out.println(HelpMenu.Listener.license);
				System.exit(0);
			} else if (args[i].equalsIgnoreCase("--font")        || args[i].equalsIgnoreCase("-font") 
					|| args[i].equalsIgnoreCase("--font-name")   || args[i].equalsIgnoreCase("-font-name") 
					|| args[i].equalsIgnoreCase("--font-family") || args[i].equalsIgnoreCase("-font-family")) {
				if ((i+1) < args.length && ! args[i+1].startsWith("-")) {
					i += 1;
					if (args[i].equalsIgnoreCase("list")) {
						// TODO list the available fonts and exit
						for (String family: PersistentState.font_list) {
							System.out.printf("%s%n", family);
						}
						System.exit(0);
					} else if (args[i].equalsIgnoreCase("default")) {
						// TODO use the default font
						PersistentState.font = PersistentState.default_font;
					} else {
						// TODO use the specified font family
						PersistentState.font = new Font(args[i], PersistentState.font.getStyle(), PersistentState.font.getSize());
					}
				} else {
					// font name must have an argument
					// print "missing font name" and exit
					System.out.printf("missing font name%n");
					System.exit(0);
				}
			} else if (args[i].equalsIgnoreCase("--font-size") || args[i].equalsIgnoreCase("-font-size")
					|| args[i].equalsIgnoreCase("--size")      || args[i].equalsIgnoreCase("-size")) {
				if ((i+1) < args.length && ! args[i+1].startsWith("-")) {
					i += 1;
					if (args[i].equalsIgnoreCase("default")) {
						// use the default font size (12 pt)
						int font_size = PersistentState.default_font.getSize();
						PersistentState.font = new Font(PersistentState.font.getFamily(), PersistentState.font.getStyle(), font_size);
					} else if (args[i].matches("[1-9][0-9]*")) {
						// use the font size from the command line
						int font_size = Integer.parseInt(args[i]);
						PersistentState.font = new Font(PersistentState.font.getFamily(), PersistentState.font.getStyle(), font_size);
					} else {
						// font size must have an integer argument
						// print "missing font size" and exit
						System.out.printf("missing font size%n");
						System.exit(0);
					}
				} else {
					// font size must have an integer argument
					// print "missing font size" and exit
					System.out.printf("missing font size%n");
					System.exit(0);
				}
			} else if (args[i].equalsIgnoreCase("--menu-height") || args[i].equalsIgnoreCase("-menu-height")
					|| args[i].equalsIgnoreCase("--height")      || args[i].equalsIgnoreCase("-height")) {
				if ((i+1) < args.length && ! args[i+1].startsWith("-")) {
					i += 1;
					if (args[i].equalsIgnoreCase("default")) {
						// TODO use the default menu height
						System.out.printf("missing code to compute default menu height...%n");
						System.exit(0);
					} else if (args[i].matches("[1-9][0-9]*")) {
						PersistentState.menu_height = Integer.parseInt(args[i]);
					} else {
						// menu height must have an integer argument
						// print "missing menu height" and exit
						System.out.printf("missing menu height%n");
						System.exit(0);
					}
				} else {
					// menu height must have an integer argument
					// print "missing menu height" and exit
					System.out.printf("missing menu height%n");
					System.exit(0);
				}
			} else if (args[i].equalsIgnoreCase("--icon-scale") || args[i].equalsIgnoreCase("-icon-scale")
					|| args[i].equalsIgnoreCase("--scale")      || args[i].equalsIgnoreCase("-scale")) {
				if ((i+1) < args.length && ! args[i+1].startsWith("-")) {
					i += 1;
					if (args[i].equalsIgnoreCase("default")) {
						// use the default icon scale (i.e., 1.0)
						PersistentState.dso_icon_scale_factor = 1.0;
					} else if (args[i].matches("[1-9][0-9]*") || args[i].matches("[0-9][0-9]*[.][0-9]*") || args[i].matches("[.][0-9][0-9]*")) {
						PersistentState.dso_icon_scale_factor = Double.parseDouble(args[i]);
					} else {
						// menu height must have an integer argument
						// print "missing menu height" and exit
						System.out.printf("missing menu height%n");
						System.exit(0);
					}
				} else {
					// menu height must have an integer argument
					// print "missing menu height" and exit
					System.out.printf("missing menu height%n");
					System.exit(0);
				}
			} else if (args[i].equalsIgnoreCase("--ui") || args[i].equalsIgnoreCase("-ui")) {
				if ((i+1) < args.length) {
					i += 1;
					if (args[i].equalsIgnoreCase("list")) {
						for (String family: PersistentState.ui_mgr_list) {
							System.out.printf("%s%n", family);
						}
						System.exit(0);
					} else if (args[i].equalsIgnoreCase("default")) {
						// get the default look-and-feel manager
						PersistentState.set_ui_mgr(UIManager.getLookAndFeel().getID());
					} else {
						PersistentState.set_ui_mgr(args[i]);
					}
				} else {
					System.out.printf("missing UI manager%n");
					System.exit(0);
				}
			} else if (args[i].equalsIgnoreCase("--path") || args[i].equalsIgnoreCase("-path") 
					|| args[i].equalsIgnoreCase("--catalog-path") || args[i].equalsIgnoreCase("-catalog-path")) {
				if ((i+1) < args.length) {
					i += 1;
					String[] path = new String[MainFrame.alternate_paths.length + 1];
					path[0] = args[i];
					for (int j=0; j < MainFrame.alternate_paths.length; j++) {
						path[j+1] = MainFrame.alternate_paths[j];
					}
				}
			} else if (args[i].equalsIgnoreCase("--url") || args[i].equalsIgnoreCase("-url")) {
				arg_url();
				System.exit(0);
			} else if (0 < args.length && (args[0].equalsIgnoreCase("--dso") || args[0].equalsIgnoreCase("-dso"))) {
				String path = "D:\\home\\projects\\org.hypercomputing\\data\\nightsky\\catalogs\\";
				int seed =  0;
				if (args.length == 2) {
					if (args[1].matches("[0-9][0-9]*")) {
						seed = Integer.parseInt(args[1]);
					} else {
						path = args[1];
					}
				} else if (args.length == 3) {
					if (args[1].matches("[0-9][0-9]*")) {
						seed = Integer.parseInt(args[1]);
						path = args[2];
					} else if (args[2].matches("[0-9][0-9]*")) {
						seed = Integer.parseInt(args[2]);
						path = args[1];
					} else {
						System.exit(0);
					}
				}
				LoadImageCache.load_dso_images(path, seed);
				System.exit(0);
			} else if (args[i].equalsIgnoreCase("--cache") || args[i].equalsIgnoreCase("-cache")) {
				i += 1;
				String cache = "D:/data/nightsky/cache/";
				int seed = (i < args.length) ? (args[i].equalsIgnoreCase("date") ? (int) (0x0000000000FFFFFFL & System.currentTimeMillis()) : Integer.parseInt(args[i])) : 0;
				LoadImageCache.load_tiles(cache, seed);
				System.exit(0);
			} else {				// --help or error...
				System.out.printf("usage: java -jar nightskyataglance.NightSkyAtAGlance args...%n");
				System.out.printf("-version%n");
				System.out.printf("-license%n");
				System.out.printf("%n");
				System.out.printf("-UI list%n");
				System.out.printf("-UI <UI name>%n");
				System.out.printf("%n");
				System.out.printf("-font == -font-name == -font-family%n");
				System.out.printf("-font list%n");
				System.out.printf("-font <name or name-size>%n");
				System.out.printf("%n");
				System.out.printf("-font-size == -size%n");
				System.out.printf("-size default%n");
				System.out.printf("-size <point size>%n");
				System.out.printf("%n");
				System.out.printf("-menu-height == -height%n");
				System.out.printf("-height default%n");
				System.out.printf("-height <height in pixels>%n");
				System.out.printf("%n");
				System.out.printf("-icon-scale == -scale%n");
				System.out.printf("-scale <DSO icon multiplier (float)>%n");
				System.exit(0);
			}
		}

		new MainFrame();
	}

	public static String read_version_file(String name)
	{
		String str = null;

		try {
			File file = new File(name);
			byte[] buf = new byte[256];
			if (file.isFile()) {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				int len = raf.read(buf);
				raf.close();
				str = new String(buf, 0, len).replaceFirst("[ \r\n]", "");
			} else {
				C c = new C();
				try(InputStream input_stream = c.getClass().getResourceAsStream(name)) {
				    if (input_stream == null) {
				        throw new FileNotFoundException("File '" + name + "' not found!");
				    }
	
				    int len;
				    int ch = input_stream.read();
				    for (len=0; len < buf.length && ' ' <= ch; len++) {
				    	buf[len] = (byte) ch;
				    	ch = input_stream.read();
				    }
				    input_stream.close();
	
					str = new String(buf, 0, len).replaceFirst("[ ]", "");
				} catch (IOException e) {
				    // e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		str = (str == null || str.equals("")) ? fallback_version : str;
		
		return str;
	}
	
	public static final void arg_url()
	{
		String url_str = "https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=05+34+31.78&d=%2B22+01+02.6&e=J2000&h=15.0&w=15.0&f=gif";
	    try {
	    	URI uri = new URI(url_str);
	        InputStream is = uri.toURL().openStream(); 
	        OutputStream os = new FileOutputStream("sombrero.gif");
	        byte[] ch = is.readAllBytes();
	        os.write(ch);
	        is.close();
	        os.close();
	    } catch (Exception e) {
	    	
	    }
	}
	
	static class C {
	}

	public static final int LINE()
	{
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	public static final String FILE()
	{
		return Thread.currentThread().getStackTrace()[2].getFileName();
	}
	
	public static final String CLASS()
	{
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}
	
	public static final String METHOD()
	{
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public static final int LINE(int k)
	{
		return Thread.currentThread().getStackTrace()[k].getLineNumber();
	}
	
	public static final String FILE(int k)
	{
		return Thread.currentThread().getStackTrace()[k].getFileName();
	}
	
	public static final String CLASS(int k)
	{
		return Thread.currentThread().getStackTrace()[k].getClassName();
	}
	
	public static final String METHOD(int k)
	{
		return Thread.currentThread().getStackTrace()[k].getMethodName();
	}
}
