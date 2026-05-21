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


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TimeZone;

import nightskyataglance.NightSkyAtAGlance;

public class PersistentState {

	public static String home_path = System.getProperty("user.home") + System.getProperty("file.separator") + "." + 
			NightSkyAtAGlance.CLASS(1).substring(NightSkyAtAGlance.CLASS(1).lastIndexOf('.')+1, NightSkyAtAGlance.CLASS(1).length()) + 
			System.getProperty("file.separator");

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

	public static void load_state() throws IOException
	{
		// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_path);
		File home_file = new File(home_path);
		if (home_file.isDirectory() && home_file.canRead()) {
			// System.out.printf("%s: %d: home='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), home_file.getCanonicalPath());
			load_cache_path();
			load_location();
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
}
