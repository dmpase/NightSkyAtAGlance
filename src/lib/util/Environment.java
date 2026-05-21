package lib.util;

/*******************************************************************************
 * Copyright (c) 1988-2020 Douglas M. Pase                                     *
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

import java.net.*;

import lib.hash.SHA160;

public class Environment {
	public static final String file_separator  = System.getProperty("file.separator");
	public static final String java_class_path = System.getProperty("java.class.path");
	public static final String java_home       = System.getProperty("java.home");
	public static final String java_vendor     = System.getProperty("java.vendor");
	public static final String java_vendor_url = System.getProperty("java.vendor.url");
	public static final String java_version    = System.getProperty("java.version");
	public static final String line_separator  = System.getProperty("line.separator");
	public static final String os_arch         = System.getProperty("os.arch");
	public static final String os_name         = System.getProperty("os.name");
	public static final String os_version      = System.getProperty("os.version");
	public static final String path_separator  = System.getProperty("path.separator");
	public static final String user_dir        = System.getProperty("user.dir");
	public static final String user_home       = System.getProperty("user.home");
	public static final String user_name       = System.getProperty("user.name");
	
	public static final int    default_port    = 9123;

	public static       String host_name       = null; // get_host_name();
	public static       String host_ip_address = null; // get_host_ip_addr();
	private static      int[]  host_ip_quad    = new int[4];
	
	public static       String host_mac_str    = null; // get_host_mac_addr();
	
	public static String get_host_name()
	{
		String result = null;

		try {
			InetAddress local = InetAddress.getLocalHost();
			result = local.getHostName();
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}
	
	public static String get_host_ip_addr()
	{
		String result = "";

		try {
			byte[] addr = Inet4Address.getLocalHost().getAddress();
			if (addr != null) {
				int a = (addr[0] < 0) ? addr[0]+256 : addr[0];
				host_ip_quad[0] = a;
				result += a;
				for (int i=1; i < addr.length; i++) {
					a = (addr[i] < 0) ? addr[i]+256 : addr[i];
					host_ip_quad[i] = a;
					result += "."+a;
				}
			} else {
				result = "127.0.0.1";
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}
	
	public static String get_host_mac_addr()
	{
		String result = "";

		try {
			InetAddress local = InetAddress.getLocalHost();

			byte[] mac = NetworkInterface.getByInetAddress(local).getHardwareAddress();
			result = mac_to_string(mac);
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}
	
	public static boolean is_alpha(char c)
	{
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}
	
	public static boolean is_digit(char c)
	{
		return '0' <= c && c <= '9';
	}
	
	public static long stol(String s)
	{
		int sign = 1;
		long result = 0;
		byte[] b = s.getBytes();
		
		int start=0;
		for (int i=0; i < b.length; i++) {
			if (' ' < b[i]) {
				start = i;
				break;
			}
		}
		if (b[start] == '-') {
			sign = -1;
			start++;
		}

		if (start < b.length-1 && b[start] == '0' && (b[start+1] == 'x' || b[start+1] == 'X')) {
			// the number is in hex
			for (int i=start+2; i < b.length; i++) {
				if ('0' <= b[i] && b[i] <= '9') {
					result = result*16 + b[i] - '0';
				} else if ('a' <= b[i] && b[i] <= 'f') {
					result = result*16 + 10 + b[i] - 'a';
				} else if ('A' <= b[i] && b[i] <= 'F') {
					result = result*16 + 10 + b[i] - 'A';
				} else {
					break;
				}
			}
		} else if (start < b.length && b[start] == '0') {
			// the number is in octal
			for (int i=start+1; i < b.length; i++) {
				if ('0' <= b[i] && b[i] <= '7') {
					result = result*8 + b[i] - '0';
				} else {
					break;
				}
			}
		} else {
			// the number is in decimal
			for (int i=start; i < b.length; i++) {
				if ('0' <= b[i] && b[i] <= '9') {
					result = result*10 + b[i] - '0';
				} else {
					break;
				}
			}
		}
		
		return sign * result;
	}
	
	public static int stoi(String s)
	{
		return (int) stol(s);
	}
	
	static String ltohex(long v)
	{
		String result = "0x";
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "a", "b", "c", "d", "e", "f"};

		String digits = "";
		for (int i=0; i < 16; i++) {
			digits = hex[(int)(v & 0xf)] + digits;
			v >>= 4;
		}
		result += digits;
		
		return result;
	}
	
	private static String itohex(int v)
	{
		String result = "0x";
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "a", "b", "c", "d", "e", "f"};

		String digits = "";
		for (int i=0; i < 2; i++) {
			digits = hex[(int)(v & 0xf)] + digits;
			v >>= 4;
		}
		result = digits;
		
		return result;
	}
	
	public static void print()
	{
		System.out.println("Environment.get: file.separator   \""+file_separator+"\"");
		System.out.println("Environment.get: java.class.path  \""+java_class_path+"\"");
		System.out.println("Environment.get: java.home        \""+java_home+"\"");
		System.out.println("Environment.get: java.vendor      \""+java_vendor+"\"");
		System.out.println("Environment.get: java.vendor.url  \""+java_vendor_url+"\"");
		System.out.println("Environment.get: java.version     \""+java_version+"\"");
		System.out.println("Environment.get: line_separator   \""+line_separator+"\"");
		System.out.println("Environment.get: os.arch          \""+os_arch+"\"");
		System.out.println("Environment.get: os.name          \""+os_name+"\"");
		System.out.println("Environment.get: os_version       \""+os_version+"\"");
		System.out.println("Environment.get: path.separator   \""+path_separator+"\"");
		System.out.println("Environment.get: user.dir         \""+user_dir+"\"");
		System.out.println("Environment.get: user.home        \""+user_home+"\"");
		System.out.println("Environment.get: user.name        \""+user_name+"\"");

		System.out.println("Environment.get: host name        \""+host_name+"\"");
		System.out.println("Environment.get: host address     \""+host_ip_address+"\"");
		System.out.println("Environment.get: host mac address \""+host_mac_str+"\"");
		
		System.out.println("Enviornment.get: digest name      "+SHA160.name());
		System.out.println("Enviornment.get: digest length    "+SHA160.length());
	}
	
	public static String mac_to_string(byte[] mac)
	{
		String result = null;

		if (mac != null) {
			result = itohex(mac[0]);
			for (int i=1; i < mac.length; i++) {
				result = result + "-" + itohex(mac[i]);
			}
		} else {
			result = "00-00-00-00-00-00";
		}

		return result;
	}
}
