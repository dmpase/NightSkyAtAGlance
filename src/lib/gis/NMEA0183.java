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

import java.util.*;

import lib.time.TimeOfDay;
import lib.util.Queue;

public class NMEA0183 {
	String name = null;

	public static NMEA0183 parse(byte[] rec) throws BadRecordException
	{
		if (rec == null) {
			throw new BadRecordException("Null record.");
		}

		String name = null;
		for (int i=1; i < rec.length; i++) {
			if (rec[i] == ',') {
				name = new String(rec, 0, i);
				break;
			}
		}

		if (name == null) {
			throw new BadRecordException("Ill formed record.");
		}

		NMEA0183 result = null;
		if (name.equalsIgnoreCase("GPGGA")) {
			result = new GPGGA(rec);
		} else if (name.equalsIgnoreCase("GPGLL")) {
			result = new GPGLL(rec);
		} else if (name.equalsIgnoreCase("GPGSA")) {
			result = new GPGSA(rec);
		} else if (name.equalsIgnoreCase("GPGSV")) {
			result = new GPGSV(rec);
		} else if (name.equalsIgnoreCase("GPRMC")) {
			result = new GPRMC(rec);
		} else if (name.equalsIgnoreCase("GPVTG")) {
			result = new GPVTG(rec);
		} else if (name.equalsIgnoreCase("GPZDA")) {
			result = new GPZDA(rec);
		} else if (name.equalsIgnoreCase("PSRFTXT")) {
			result = new PSRFTXT(rec);
		} else {
			throw new BadRecordException("Unknown record.");
		}
		
		if (result != null) {
			result.name = name;
		}
		
		return result;
	}

	// GGA Global Positioning System Fix Data. Time, Location and fix related data for a GPS receiver
	public static class GPGGA extends NMEA0183 {
		public static final int NAME           =  0;
		public static final int TIME_UTC       =  1;
		public static final int LATITUDE       =  2;
		public static final int LATITUDE_NS    =  3;
		public static final int LONGITUDE      =  4;
		public static final int LONGITUDE_EW   =  5;
		public static final int GPS_QUALITY    =  6;
		public static final int NUM_SATELLITES =  7;
		public static final int HORIZ_DIL_PREC =  8;
		public static final int ALTITUDE       =  9;
		public static final int ALTITUDE_UNITS = 10;
		public static final int GEOIDAL_SEP    = 11;
		public static final int GEO_SEP_UNITS  = 12;
		public static final int DGPS_DATA_AGE  = 13;
		public static final int DIF_REF_STA_ID = 14;
		public static final int CHECK_SUM      = 15;

		public long   time_utc       = 0L;		// time (UTC)
		public double latitude       = 0;
		public double longitude      = 0;
		public int    gps_quality    = 0;		// GPS Quality Indicator, 0 - fix not available, 1 - GPS fix, 2 - Differential GPS fix
		public int    num_satellites = 0;		// Number of satellites in view, 00 - 12
		public double horiz_dilution = 0;		// Horizontal Dilution of precision
		public double altitude       = 0;		// Antenna Altitude above/below mean-sea-level (geoid)
		public int    altitude_units = 'm';
		public double geoidal_sep    = 0;		// Geoidal separation, the difference between the WGS-84 earth ellipsoid and mean-sea-level 
		public int    geo_sep_units  = 0;		// Units of geoidal separation, meters
		public double dgps_data_age  = 0;		// Age of differential GPS data, time in seconds since last SC104
		public int    dif_ref_sta_id = 0;		// differential reference station id

		public GPGGA(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);
			
			if (baa.length < CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}

			time_utc       = get_time(baa[TIME_UTC]);
			latitude       = get_lat(baa[LATITUDE],  baa[LATITUDE_NS]);
			longitude      = get_lon(baa[LONGITUDE], baa[LONGITUDE_EW]);
			gps_quality    = (baa[GPS_QUALITY] != null) ? baa[GPS_QUALITY][0] - '0' : 0 ;
			num_satellites = get_int(baa[NUM_SATELLITES]);
			horiz_dilution = get_num(baa[HORIZ_DIL_PREC]);
			altitude       = get_num(baa[ALTITUDE]);
			altitude_units = (baa[ALTITUDE_UNITS] != null) ? baa[ALTITUDE_UNITS][0] : 0;
			geoidal_sep    = get_num(baa[GEOIDAL_SEP]);
			geo_sep_units  = (baa[GEO_SEP_UNITS] != null) ? baa[GEO_SEP_UNITS][0] : 0;
			dgps_data_age  = get_num(baa[DGPS_DATA_AGE]);
			dif_ref_sta_id = get_int(baa[DIF_REF_STA_ID]);

 			System.out.println("time (utc):         "+TimeOfDay.show_time(time_utc, TimeOfDay.utc));
			System.out.println("latitude:   "+latitude);
			System.out.println("longitude:  "+longitude);
			System.out.println("altitude:   "+altitude+ " " + (char)altitude_units);
			System.out.println("satellites: "+num_satellites);
		}
	}

	// GLL Geographic Location - Latitude/Longitude
	public static class GPGLL extends NMEA0183 {
		public static final int NAME           =  0;
		public static final int LATITUDE       =  1;
		public static final int LATITUDE_NS    =  2;
		public static final int LONGITUDE      =  3;
		public static final int LONGITUDE_EW   =  4;
		public static final int TIME_UTC       =  5;
		public static final int CHECK_SUM      =  6;

		public long   time_utc  = 0L;		// time (UTC)
		public double latitude  = 0;
		public double longitude = 0;

		public GPGLL(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}

			time_utc       = get_time(baa[TIME_UTC]);
			latitude       = get_lat(baa[LATITUDE],  baa[LATITUDE_NS]);
			longitude      = get_lon(baa[LONGITUDE], baa[LONGITUDE_EW]);

//			System.out.println("time (utc): "+TimeOfDay.show_time(time_utc, TimeOfDay.utc));
//			System.out.println("latitude:   "+latitude);
//			System.out.println("longitude:  "+longitude);
		}
	}
	
	public static class GPGSA extends NMEA0183 {
		public static final int NAME           =  0;
		public static final int SELECTION_MODE =  1;
		public static final int MODE           =  2;
		public static final int SAT_1_ID       =  3;
		public static final int SAT_2_ID       =  4;
		public static final int SAT_3_ID       =  5;
		public static final int SAT_4_ID       =  6;
		public static final int SAT_5_ID       =  7;
		public static final int SAT_6_ID       =  8;
		public static final int SAT_7_ID       =  9;
		public static final int SAT_8_ID       = 10;
		public static final int SAT_9_ID       = 11;
		public static final int SAT_10_ID      = 12;
		public static final int SAT_11_ID      = 13;
		public static final int SAT_12_ID      = 14;
		public static final int PDOP           = 15;
		public static final int HDOP           = 16;
		public static final int VDOP           = 17;
		public static final int CHECK_SUM      = 18;
		
		public int    selection_mode = 0;
		public int    mode           = 0;
		public int[]  satellite      = new int[12];
		public double pdop           = 0;
		public double hdop           = 0;
		public double vdop           = 0;

		public long   time           = 0L;		// time (UTC)

		public GPGSA(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}

			selection_mode = get_int(baa[SELECTION_MODE]);
			mode           = get_int(baa[MODE]);
			for (int i=0; i < satellite.length; i++) {
				satellite[i] = get_int(baa[SAT_1_ID+i]);
			}

			pdop = get_num(baa[baa.length-4]);
			hdop = get_num(baa[baa.length-3]);
			vdop = get_num(baa[baa.length-2]);
		}
	}
	
	public static class GPGSV extends NMEA0183 {
		public static final int NAME              =  0;
		public static final int NUM_MESSAGES      =  1;
		public static final int MESSAGE_NUMBER    =  2;
		public static final int SAT_IN_VIEW       =  3;
		public static final int SATELLITE_NUMBER  =  4;
		public static final int ELEVATION_DEGREES =  5;
		public static final int AZIMUTH_DEGREES   =  6;
		public static final int SNR_IN_DB         =  7;
		public static final int CHECK_SUM         =  8;

		public GPGSV(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}
			
			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}
		}
	}

	public static class GPRMC extends NMEA0183 {
		public static final int NAME               =  0;
		public static final int TIME_UTC           =  1;
		public static final int STATUS             =  2;
		public static final int LATITUDE           =  3;
		public static final int LATITUDE_NS        =  4;
		public static final int LONGITUDE          =  5;
		public static final int LONGITUDE_EW       =  6;
		public static final int SPEED_OVER_GROUND  =  7;
		public static final int TRACK_MADE_GOOD    =  8;
		public static final int DATE_UTC           =  9;
		public static final int MAGNETIC_VARIATION = 10;
		public static final int MAG_VAR_EW         = 11;
		public static final int CHECK_SUM          = 12;

		public long   time_utc           = 0L;		// time (UTC)
		public int    status             = 0;
		public double latitude           = 0;
		public double longitude          = 0;
		public double speed_over_ground  = 0;
		public double track_made_good    = 0;
		public long   date_utc           = 0L;		// date (UTC)
		public double magnetic_variation = 0;

		public GPRMC(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}
			
			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}

			time_utc           = get_time(baa[TIME_UTC]);
			status             = get_int(baa[STATUS]);
			latitude           = get_lat(baa[LATITUDE],  baa[LATITUDE_NS]);
			longitude          = get_lon(baa[LONGITUDE], baa[LONGITUDE_EW]);
			speed_over_ground  = get_num(baa[SPEED_OVER_GROUND]);
			track_made_good    = get_num(baa[TRACK_MADE_GOOD]);
			date_utc           = get_time(baa[TIME_UTC]);
			magnetic_variation = get_num(baa[MAGNETIC_VARIATION]);
			if (baa[MAG_VAR_EW] != null && 0 < baa[MAG_VAR_EW].length && baa[MAG_VAR_EW][0] == 'W') {
				magnetic_variation = -magnetic_variation;
			}

//			System.out.println("time (utc):         "+TimeOfDay.show_time(time_utc, TimeOfDay.utc));
//			System.out.println("latitude:           "+latitude);
//			System.out.println("longitude:          "+longitude);
//			System.out.println("speed over ground:  "+speed_over_ground);
//			System.out.println("track made good:    "+track_made_good);
//			System.out.println("date (utc):         "+TimeOfDay.show_time(date_utc, TimeOfDay.utc));
//			System.out.println("magnetic variation: "+magnetic_variation);
		}
	}

	// VTG Track Made Good and Ground Speed
	public static class GPVTG extends NMEA0183 {
		public static final int NAME               =  0;
		public static final int TRACK_DEG_TRUE     =  1;
		public static final int TRACK_DEG_MAG      =  3;
		public static final int SPEED_KNOTS        =  5;
		public static final int SPEED_KMPH         =  7;
		public static final int CHECK_SUM          =  9;
		
		public double track_deg_true = 0;
		public double track_deg_mag  = 0;
		public double speed_knots    = 0;
		public double speed_kmph     = 0;

		public GPVTG(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}

			track_deg_true = get_num(baa[TRACK_DEG_TRUE]);
			track_deg_mag  = get_num(baa[TRACK_DEG_MAG]);
			speed_knots    = get_num(baa[SPEED_KNOTS]);
			speed_kmph     = get_num(baa[SPEED_KMPH]);

			System.out.println("track degrees true:     "+track_deg_true);
			System.out.println("track degrees magnetic: "+track_deg_mag);
			System.out.println("speed (knots):          "+speed_knots);
			System.out.println("speed (kph):            "+speed_kmph);
		}
	}

	// ZDA Time & Date - UTC, Day, Month, Year and Local Time Zone
	public static class GPZDA extends NMEA0183 {
		public static final int NAME               =  0;
		public static final int CHECK_SUM          =  1;
		public GPZDA(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}
		}
	}
	
	public static class PSRFTXT extends NMEA0183 {
		public static final int NAME               =  0;
		public static final int CHECK_SUM          =  1;
		public PSRFTXT(byte[] rec) throws BadRecordException
		{
			if (rec == null) {
				throw new BadRecordException("Null record.");
			}

			byte[][] baa = get_csv(rec);

			if (baa.length <= CHECK_SUM) {
				throw new BadRecordException("Ill formed record.");
			}
		}
	}
	
	public static class BadRecordException extends Exception
	{
		private static final long serialVersionUID = 5765945973207569125L;
		public BadRecordException(String msg)
		{
			super(msg);
		}
	}
	
	public static byte[][] get_csv(byte[] rec)
	{
		byte[][] result = null;
		
		Queue<byte[]> fq = new Queue<byte[]>();

		// look for the fields and store them in a queue
		int start = 1;
		for (int i=1; rec != null && i < rec.length; i++) {
			if (rec[i] == ',' || rec[i] == '\r' || rec[i] == '*') {
				if (start == i) {
					fq.append((byte[]) null);
				} else {
					byte[] field = new byte[i-start];
					for (int j=start; j < i; j++) {
						field[j-start] = rec[j];
					}
					fq.append(field);
				}
				
				start = i + 1;
			}
		}

		// copy the fields from the queue to an array
		if (0 < fq.length()) {
			result = new byte[fq.length()][];
			
			for (int i=0; i < result.length; i++) {
				result[i] = fq.remove();
			}
		}
		
		return result;
	}

	private static TimeZone utc = TimeZone.getTimeZone("UTC");
	
	public static long get_time(byte[] rec)
	{
		if (rec == null || rec.length < 6) {
			return 0;
		}

		int gps_dy = 10 * (rec[0]-'0') + (rec[1]-'0');
		int gps_mo = 10 * (rec[2]-'0') + (rec[3]-'0');
		int gps_yr = 10 * (rec[4]-'0') + (rec[5]-'0');

		Calendar zulu = Calendar.getInstance(utc);
		zulu.set(gps_yr, gps_mo, gps_dy);
		long zulu_date = zulu.getTimeInMillis();

		return zulu_date;
	}
	
	public static long get_date(byte[] rec)
	{
		int gps_hour = 10 * (rec[0]-'0') + (rec[1]-'0');
		int gps_min  = 10 * (rec[2]-'0') + (rec[3]-'0');
		int gps_sec  = 10 * (rec[4]-'0') + (rec[5]-'0');
		int gps_mil  = 100 * (rec[7]-'0') + 10 * (rec[8]-'0') + (rec[9]-'0');

		int gps_time = 1000 * (60 * (60 * gps_hour + gps_min) + gps_sec) + gps_mil;
		
		Calendar zulu = Calendar.getInstance(utc);
		long zulu_date_time = zulu.getTimeInMillis();
		zulu.setTimeInMillis(zulu_date_time);

		int zulu_hour = zulu.get(Calendar.HOUR_OF_DAY);
		int zulu_min  = zulu.get(Calendar.MINUTE);
		int zulu_sec  = zulu.get(Calendar.SECOND);
		int zulu_mil  = zulu.get(Calendar.MILLISECOND);

		int zulu_time = 1000 * (60 * (60 * zulu_hour + zulu_min) + zulu_sec) + zulu_mil;
		
		long result = zulu_date_time - zulu_time + gps_time;
		if ((12*60*60*1000) < (gps_time - zulu_time)) {
			// gps time was taken before midnight, zulu time is after midnight
			result -= 24*60*60*1000;
		}

		return result;
	}
	
	public static double get_lat(byte[] lat, byte[] ns)
	{
		if (lat == null || ns == null || lat.length < 4) {
			return 0;
		}

		double degrees = 10*(lat[0]-'0') + (lat[1]-'0');
		double minutes = 10*(lat[2]-'0') + (lat[3]-'0');
		minutes += (5 < lat.length) ? 0.1000*(lat[5]-'0') : 0;
		minutes += (6 < lat.length) ? 0.0100*(lat[6]-'0') : 0;
		minutes += (7 < lat.length) ? 0.0010*(lat[7]-'0') : 0;
		minutes += (8 < lat.length) ? 0.0001*(lat[8]-'0') : 0;

		if (ns != null && (ns[0] == 'S' || ns[0] == 's')) {
			degrees = -(degrees - minutes / 60.0);
		} else {
			degrees =  (degrees - minutes / 60.0);
		}

		return degrees;
	}
	
	public static double get_lon(byte[] lon, byte[] ew)
	{
		if (lon == null || lon.length < 10) {
			return 0;
		}

		double degrees = 100*(lon[0]-'0') + 10*(lon[1]-'0') + (lon[2]-'0');
		double minutes = 10*(lon[3]-'0') + (lon[4]-'0');
		minutes += (6 < lon.length) ? 0.1000*(lon[6]-'0') : 0;
		minutes += (7 < lon.length) ? 0.0100*(lon[7]-'0') : 0;
		minutes += (8 < lon.length) ? 0.0010*(lon[8]-'0') : 0;
		minutes += (9 < lon.length) ? 0.0001*(lon[9]-'0') : 0;

		if (ew != null && (ew[0] == 'W' || ew[0] == 'w')) {
			degrees = -(degrees + minutes / 60.0);
		} else {
			degrees =  (degrees + minutes / 60.0);
		}

		return degrees;
	}

	public static int get_int(byte[] num)
	{
		int result = 0;
		
		for (int i=0; num != null && i < num.length; i++) {
			result = 10*result + (num[i] - '0');
		}
		
		return result;
	}

	public static double get_num(byte[] num)
	{
		double result = 0;

		int i = 0;
		for ( ; num != null && i < num.length && num[i] != '.'; i++) {
			result = 10*result + (num[i] - '0');
		}

		i += 1;
		double factor = 1;
		for ( ; num != null && i < num.length; i++) {
			factor *= 0.1;
			result += factor * (num[i] - '0');
		}

		return result;
	}
}
