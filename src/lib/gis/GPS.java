package lib.gis;

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

import java.io.*;

import lib.time.*;

@SuppressWarnings("unused")
public class GPS {

	public static void main(String[] args) throws Exception
	{
		System.out.println("Opening COM4");

		OutputStream os = new FileOutputStream("\\\\.\\COM4");

		System.out.println("Initializing GPS");

//		os.write((cmd_list[25][1]+"\r\n").getBytes());			// Factory Reset
//		os.write((cmd_list[26][1]+"\r\n").getBytes());			// Cold Start
		os.write((cmd_list[22][1]+"\r\n").getBytes());			// Enable Development Data
//		os.write((cmd_list[17][1]+"\r\n").getBytes());			// 9600/8/N/1

		int en = 0;												// enable == 0, disable == 1
		os.write((cmd_list[ 0+en][1]+"\r\n").getBytes());		// enable/disable WAAS/EGNOS
		os.write((cmd_list[ 2+en][1]+"\r\n").getBytes());		// enable/disable GGA Output  1 sec
		os.write((cmd_list[ 4+en][1]+"\r\n").getBytes());		// enable/disable GLL Output  1 sec
		os.write((cmd_list[ 6+en][1]+"\r\n").getBytes());		// enable/disable GSA Output  5 sec
		os.write((cmd_list[ 8+en][1]+"\r\n").getBytes());		// enable/disable GSV Output 10 sec
		os.write((cmd_list[10+en][1]+"\r\n").getBytes());		// enable/disable RMC Output  1 sec
		os.write((cmd_list[12+en][1]+"\r\n").getBytes());		// enable/disable VTG Output  1 sec
		os.write((cmd_list[14+en][1]+"\r\n").getBytes());		// enable/disable ZDA Output  1 sec

//		os.write((cmd_list[21][1]+"\r\n").getBytes());
//		os.write((cmd_list[22][1]+"\r\n").getBytes());

		os.close();
		
		System.out.println("Reading GPS data");

		InputStream  is = new FileInputStream(new File("\\\\.\\COM4"));

		while (true) {
			byte[] ba = read_bytes(is);
			try {
				NMEA0183 rec = NMEA0183.parse(ba);
				if (rec.getClass().getName().equals("gis.NMEA0183$GPGGA")) {
					System.out.println(new String(ba));
				}
//				if (ba != null) System.out.println(TimeOfDay.show_time(TimeOfDay.current_zulu(), TimeOfDay.ltz)+" "+ba.length+" "+new String(ba));
			} catch (NMEA0183.BadRecordException e) {
//				System.out.println("Bad record. "+e);
			}
		}
		
//		is.close();
	}
	
	private static byte[] buf = new byte[4096];
	
	public static byte[] read_bytes(InputStream is) throws IOException
	{
		int len = 0;

		while (len < 10) {
			// find the start of a new record
			while (0 <= is.read(buf, 0, 1)) {
				if (buf[0] == '$') {
					break;
				}
			}
	
			// find the end of the record
			for (int i=0; i < buf.length; i++) {
				is.read(buf, i, 1);
				if (buf[i] == '$') {
					i--;
				} else if (buf[i] == '\r' || buf[i] == '\n') {
					len = i;
					break;
				}
			}
		}

		// create a new byte array that contains the record
		byte[] ba = byte_copy(buf, len);

		return ba;
	}

	public static byte[][] get_records(byte[] ba)
	{
		byte[][] list = new byte[10000][];
		
		return list;
	}
	
	public static byte[] byte_copy(byte[] ba, int len)
	{
		byte[] cpy = new byte[len];
		
		for (int i=0; i < cpy.length; i++) {
			cpy[i] = ba[i];
		}

		return cpy;
	}
	
	public static int min(int x, int y)
	{
		return (x < y) ? x : y;
	}

	static String[][] cmd_list = {
		{ "Enable  WAAS/EGNOS",      	"$PSRF151,01*0F"       },			// 00 Enable  WAAS/EGNOS       - $PSRF151,01*0F
		{ "Disable WAAS/EGNOS",     	"$PSRF151,00*0E"       },			// 01 Disable WAAS/EGNOS       - $PSRF151,00*0E
		{ "Enable  GGA Output 1sec", 	"$PSRF103,0,0,1,1*25"  },			// 02 Enable  GGA Output 1sec  - $PSRF103,0,0,1,1*25
		{ "Disable GGA Output",			"$PSRF103,0,0,0,1*24"  },			// 03 Disable GGA Output       - $PSRF103,0,0,0,1*24
		{ "Enable  GLL Output 1sec",	"$PSRF103,1,0,1,1*24"  },			// 04 Enable  GLL Output 1sec  - $PSRF103,1,0,1,1*24
		{ "Disable GLL Output",			"$PSRF103,1,0,0,1*25"  },			// 05 Disable GLL Output       - $PSRF103,1,0,0,1*25
		{ "Enable  GSA Output 5sec",	"$PSRF103,2,0,5,1*23"  },			// 06 Enable  GSA Output 5sec  - $PSRF103,2,0,5,1*23
		{ "Disable GSA Output",			"$PSRF103,2,0,0,1*26"  },			// 07 Disable GSA Output       - $PSRF103,2,0,0,1*26
		{ "Enable  GSV Output 10sec",	"$PSRF103,3,0,10,1*16" },			// 08 Enable  GSV Output 10sec - $PSRF103,3,0,10,1*16
		{ "Disable GSV Output",			"$PSRF103,3,0,0,1*27"  },			// 09 Disable GSV Output       - $PSRF103,3,0,0,1*27
		{ "Enable  RMC Output 1sec",	"$PSRF103,4,0,1,1*21"  },			// 10 Enable  RMC Output 1sec  - $PSRF103,4,0,1,1*21
		{ "Disable RMC Output",			"$PSRF103,4,0,0,1*20"  },			// 11 Disable RMC Output       - $PSRF103,4,0,0,1*20
		{ "Enable  VTG Output 1sec",	"$PSRF103,5,0,1,1*20"  },			// 12 Enable  VTG Output 1sec  - $PSRF103,5,0,1,1*20
		{ "Disable VTG Output",			"$PSRF103,5,0,0,1*21"  },			// 13 Disable VTG Output       - $PSRF103,5,0,0,1*21
		{ "Enable  ZDA Output 1sec",	"$PSRF103,8,0,1,1*2D"  },			// 14 Enable  ZDA Output 1sec  - $PSRF103,8,0,1,1*2D
		{ "Disable ZDA Output",			"$PSRF103,8,0,0,1*2C"  },			// 15 Disable ZDA Output       - $PSRF103,8,0,0,1*2C

			// Change Baud Rates (NMEA Protocol):
		{ "4800/8/N/1",					"$PSRF100,1,4800,8,1,0*0E"  },		// 16 4800/8/N/1               - $PSRF100,1,4800,8,1,0*0E
		{ "9600/8/N/1",					"$PSRF100,1,9600,8,1,0*0D"  },		// 17 9600/8/N/1               - $PSRF100,1,9600,8,1,0*0D
		{ "19200/8/N/1",				"$PSRF100,1,19200,8,1,0*38" },		// 18 19200/8/N/1              - $PSRF100,1,19200,8,1,0*38
		{ "38400/8/N/1",				"$PSRF100,1,38400,8,1,0*3D" },		// 19 38400/8/N/1              - $PSRF100,1,38400,8,1,0*3D

			// Other Commands:
		{ "Enable Power Save Mode",		"$PSRF150,0,300,1000,1*10"  },		// 20 Enable Power Save Mode   - $PSRF150,0,300,1000,1*10
		{ "Disable Power Save Mode",	"$PSRF150,0,1000,1000,0*23" },		// 21 Disable Power Save Mode  - $PSRF150,0,1000,1000,0*23
		{ "Enable Development Data",	"$PSRF105,1*3E" },					// 22 Enable Development Data  - $PSRF105,1*3E
		{ "Disable Development Data",	"$PSRF105,0*3F" },					// 23 Disable Development Data - $PSRF105,0*3F
		{ "Warm Start",					"$PSRF101,0,0,0,0,0,0,12,2*16" },	// 24 Warm Start               - $PSRF101,0,0,0,0,0,0,12,2*16
		{ "Factory Reset",				"$PSRF101,0,0,0,0,0,0,12,8*1C" },	// 25 Factory Reset            - $PSRF101,0,0,0,0,0,0,12,8*1C
		{ "Cold Start",					"$PSRF101,0,0,0,0,0,0,12,4*10" },	// 26 Cold Start               - $PSRF101,0,0,0,0,0,0,12,4*10
	};
}
