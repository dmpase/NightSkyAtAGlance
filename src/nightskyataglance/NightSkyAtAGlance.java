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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;

import nightskyataglance.gui.HelpMenu;
import nightskyataglance.gui.MainFrame;
import nightskyataglance.util.LoadImageCache;

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

	public static final String fallback_version = "2026.04.25.00";

	public NightSkyAtAGlance() 
	{
	}

	public static void main(String[] args) throws IOException, URISyntaxException 
	{
		if (0 < args.length && (args[0].equalsIgnoreCase("--version") || args[0].equalsIgnoreCase("-version"))) {
			System.out.println(version);
		} else if (0 < args.length && (args[0].equalsIgnoreCase("--license") || args[0].equalsIgnoreCase("-license"))) {
			System.out.println(HelpMenu.Listener.license);
		} else if (0 < args.length && (args[0].equalsIgnoreCase("--url") || args[0].equalsIgnoreCase("-url"))) {
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
		} else if (0 < args.length && (args[0].equalsIgnoreCase("--dso") || args[0].equalsIgnoreCase("-dso"))) {
			String path = "D:\\home\\projects\\org.hypercomputing\\data\\nightsky\\catalogs\\";
			int seed = (args.length == 2) ? Integer.parseInt(args[1]) : 0;
			LoadImageCache.load_dso_images(path, seed);
		} else if (0 < args.length && (args[0].equalsIgnoreCase("--cache") || args[0].equalsIgnoreCase("-cache"))) {
			String cache      = "D:/data/nightsky/cache/";
			int seed = (args.length == 2) ? (args[1].equalsIgnoreCase("date") ? (int) (0x0000000000FFFFFFL & System.currentTimeMillis()) : Integer.parseInt(args[1])) : 0;
			LoadImageCache.load_tiles(cache, seed);
		} else {
			new MainFrame();
		}
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
