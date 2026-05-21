package lib.util;

/*******************************************************************************
 * Copyright (c) 1988-2021 Douglas M. Pase                                     *
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

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public class MachineID {

	public static String getID() throws IOException
	{
		String machine_id = null;

		/*
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			System.out.println("Windows");
			Process process = Runtime.getRuntime().exec("wmic csproduct get UUID");
			InputStream pis = process.getInputStream();
			byte[] buf = new byte[4096];
			pis.read(buf);
			String output = new String(buf);
			machine_id=output.toString().substring(output.indexOf("\n"), output.length()).trim();
			System.out.println(machine_id);
		} else if (OS.indexOf("mac") >= 0) {
			System.out.println("MacOS");
			String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";
		    Process process = Runtime.getRuntime().exec(command);
			InputStream pis = process.getInputStream();
			byte[] buf = new byte[4096];
			pis.read(buf);
			String output = new String(buf);
		    machine_id = output.toString().substring(output.indexOf("UUID: "), output.length()).replace("UUID: ", "");
		} else if (OS.indexOf("unix") >= 0) {
			System.out.println("Unix");
		} else if (OS.indexOf("linux") >= 0) {
			System.out.println("Linux");
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime().exec("cat /sys/class/dmi/id/product_uuid");
		} else if (OS.indexOf("aix") >= 0) {
			System.out.println("AIX");
		}
		*/

		return machine_id;
	}

	public static void main(String[] args) 
	{
	}

}
