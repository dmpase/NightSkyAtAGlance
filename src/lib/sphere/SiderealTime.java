package lib.sphere;

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

public class SiderealTime {
	
	// http://www.tecepe.com.br/nav/inav_c12.htm
	// https://www.omnicalculator.com/everyday-life/sidereal-time#how-to-calculate-sidereal-time-greenwich-sidereal-time-calculator-mean-and-apparent
	// https://phpsciencelabs.com/sidereal-time-calculator/
	// https://ascl.net/1202.003
	// https://aa.usno.navy.mil/software/novasc_intro
	// https://aa.usno.navy.mil/software/novas_info
	// https://aa.usno.navy.mil/software/index
	// https://aa.usno.navy.mil/data/siderealtime
	// https://squarewidget.com/astronomical-calculations-sidereal-time/
	
	public static final int    j2000_year  = 2000;
	public static final int    j2000_month = 0;
	public static final int    j2000_date  = 1;
	public static final int    j2000_hour  = 12;
	public static final int    j2000_min   = 0;
	public static final int    j2000_sec   = 0;
	
	// time, in ms since the current epoch, of the J2000 referenct time (Jan 1, 2000, 12:00)
	public static final long   j2000_ms    = 946728000000L;

	public static final int    ref_sidereal_hour = 18;
	public static final int    ref_sidereal_min  = 41;
	public static final int    ref_sidereal_sec  = 51;
	
	public static final long   ms_per_sec         = 1000;
	public static final long   ms_per_min         = 60 * ms_per_sec;
	public static final long   ms_per_hour        = 60 * ms_per_min;
	public static final long   ms_per_day         = 24 * ms_per_hour;

	public static final long sidereal_offset_ms = ref_sidereal_hour * ms_per_hour + ref_sidereal_min * ms_per_min + ref_sidereal_sec * ms_per_sec;

	// convert the specified time (milliseconds since the start of the epoch) to
	// Greenwich Mean Standard Time (in milliseconds)
	public static long get_GMST_ms(long gmt_ms)
	{
		long sidereal_ms = ((long)(Math.rint((gmt_ms - j2000_ms)*sidereal_per_solar)) + sidereal_offset_ms)%ms_per_day;
		sidereal_ms += (sidereal_ms < 0) ? ms_per_day : 0;
		
		return sidereal_ms;
	}

	// both sidereal_ms and result are 0 <= gmt_ms < ms_per_day (i.e., 00:00:00 to 23:59:59, and not relative to start of an epoch)
	public static long find_local_ms(long sidereal_ms, Angle longitude)
	{
		long gmt_ms  = 0;
		long low_gmt = 0;
		long hi_gmt  = ms_per_hour * 24;
		while (1 < (hi_gmt - low_gmt)) {
			long mid_gmt = gmt_ms = (low_gmt + hi_gmt) / 2;
			long mid_sid = solar_to_sidereal(mid_gmt, longitude);
			if (mid_sid < sidereal_ms) {
				low_gmt = mid_gmt;
			} else if (mid_sid == sidereal_ms) {
				hi_gmt = low_gmt = mid_gmt;
			} else {
				hi_gmt = mid_gmt;
			}
		}
		
		gmt_ms = (gmt_ms     <      0) ? gmt_ms + ms_per_day : gmt_ms;
		gmt_ms = (ms_per_day < gmt_ms) ? gmt_ms - ms_per_day : gmt_ms;

		return gmt_ms;
	}
	
	private static long solar_to_sidereal(long gmt_ms, Angle longitude)
	{
		long sidereal_ms = ((long)(Math.rint((gmt_ms - j2000_ms)*sidereal_per_solar)) + sidereal_offset_ms)%ms_per_day;
		sidereal_ms += longitude.degrees*(24.0/360.0)*ms_per_hour;

		return sidereal_ms;
	}

	// convert the specified Zulu time (milliseconds since the start of the epoch) to
	// Local Sidereal Time (in milliseconds)
	public static long get_local_sidereal_ms(long gmt_ms, Angle longitude)
	{
		long sidereal_ms = ((long)(Math.rint((gmt_ms - j2000_ms)*sidereal_per_solar)) + sidereal_offset_ms)%ms_per_day;
		sidereal_ms += longitude.degrees*(12.0/180.0)*ms_per_hour;
		sidereal_ms += (sidereal_ms < 0) ? ms_per_day : 0;
		
		return sidereal_ms;
	}

	// get the current Local Sidereal Time (in milliseconds)
	public static long get_local_sidereal_ms(Angle longitude)
	{
		long gmt_ms = get_GMT_ms();
		long sidereal_ms = get_local_sidereal_ms(gmt_ms, longitude);
		
		return sidereal_ms;
	}
	
	public static long get_local_sidereal_ms()
	{
		TimeZone ltz = TimeZone.getDefault();
		double offset_sec = ltz.getRawOffset() / 1000;
		double hour = 0.5 + ((offset_sec < 0) ? 24 + offset_sec / 3600 : offset_sec / 3600);
		Angle longitude = new Angle(hour, Angle.Scale.HOURS);
		return get_local_sidereal_ms(longitude);
	}

	// get the current Local Sidereal Time (in degrees)
	public static double get_local_sidereal_degrees(Angle longitude)
	{
		long gmt_ms = get_GMT_ms();
		long sidereal_ms = get_local_sidereal_ms(gmt_ms, longitude);
		double degrees = millisec_to_degrees(sidereal_ms);
		
		return degrees;
	}

	// get the current Local Sidereal Time (in radians)
	public static double get_local_sidereal_radians(Angle longitude)
	{
		long gmt_ms = get_GMT_ms();
		long sidereal_ms = get_local_sidereal_ms(gmt_ms, longitude);
		double radians = millisec_to_radians(sidereal_ms);
		
		return radians;
	}

	// convert milliseconds to degrees (24 hour day == 360 degrees)
	public static double millisec_to_degrees(long ms)
	{
		double degrees = 360 * (double)(ms % ms_per_day)/(double) ms_per_day;
		
		return degrees;
	}

	// convert milliseconds to radians (24 hour day == 2 PI radians)
	public static double millisec_to_radians(long ms)
	{
		double radians = 2 * Math.PI * (double)(ms % ms_per_day)/(double) ms_per_day;
		
		return radians;
	}

	// convert the specified UTC time to 
	// Greenwich Mean Time (in milliseconds)
	public static long get_GMT_ms(int year, int month, int date, int hour, int min, int sec)
	{
		// get the requested solar time in milliseconds since the current epoch
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar rzt  = Calendar.getInstance(utc);
		rzt.set(year, month, date, hour, min, sec);
		long gmt_ms = (rzt.getTimeInMillis()/1000)*1000;

		return gmt_ms;
	}

	// return the current UTC time as milliseconds since the start of the epoch,
	// Greenwich Mean Time (in milliseconds)
	public static long get_GMT_ms()
	{
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar zulu = Calendar.getInstance(utc);
		long millisec = zulu.getTimeInMillis();
		
		return millisec;
	}

	// convert the specified local time to milliseconds since the start of epoch
	// Local Solar Time (in milliseconds)
	public static long get_local_ms(int year, int month, int date, int hour, int min, int sec)
	{
		// get the requested solar time in milliseconds since the current epoch
		TimeZone ltz = TimeZone.getDefault();
		Calendar rzt  = Calendar.getInstance(ltz);
		rzt.set(year, month, date, hour, min, sec);
		long gmt_ms = rzt.getTimeInMillis();

		return gmt_ms;
	}
	
	public static long get_local_ms()
	{
		TimeZone ltz = TimeZone.getDefault();
		Calendar zulu = Calendar.getInstance(ltz);
		long millisec = zulu.getTimeInMillis();
		millisec += ltz.getOffset(millisec);
		
		return millisec;
	}
	
	public static int get_year(long ms)
	{
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar rzt  = Calendar.getInstance(utc);
		rzt.setTimeInMillis(ms);
		return rzt.get(Calendar.YEAR);
	}

	public static int get_month(long ms)
	{
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar rzt  = Calendar.getInstance(utc);
		rzt.setTimeInMillis(ms);
		return rzt.get(Calendar.MONTH);
	}

	public static int get_day_of_month(long ms)
	{
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar rzt  = Calendar.getInstance(utc);
		rzt.setTimeInMillis(ms);
		return rzt.get(Calendar.DAY_OF_MONTH);
	}

	public static int get_hour(long ms)
	{
		return (int)((ms % ms_per_day) / ms_per_hour);
	}
	
	public static int get_min(long ms)
	{
		return (int)((ms % ms_per_hour) / ms_per_min);
	}
	
	public static int get_sec(long ms)
	{
		return (int)((ms % ms_per_min) / ms_per_sec);
	}
	
	public static int get_ms(long ms)
	{
		return (int)(ms % ms_per_sec);
	}

	public static final double sidereal_day = 23.0 + 56.0/60.0 + 4.0916/3600.0;	// solar hours per sidereal day
	public static final double solar_day    = 24.0;								// solar hours per solar day
	public static final double sidereal_per_solar = solar_day / sidereal_day;	// solar hours per solar day / solar hours per sidereal day
	
	/* http://www.timeanddate.com/calendar/seasons.html?n=769
	 * http://en.wikipedia.org/wiki/Equinox
	 * 
	 * Spring Equinox (GMT)         Summer Solstice (GMT)   Fall Equinox (GMT)      Winter Solstice (GMT)
	 * 2010	Mar 20	05:32 PM GMT	Jun 21	12:29 PM BST	Sep 23	04:09 AM BST	Dec 21	11:38 PM GMT
	 * 2011	Mar 20	11:21 PM GMT	Jun 21	06:16 PM BST	Sep 23	10:04 AM BST	Dec 22	05:30 AM GMT
	 * 2012	Mar 20	05:14 AM GMT	Jun 21	12:08 AM BST	Sep 22	03:49 PM BST	Dec 21	11:12 AM GMT
	 * 2013	Mar 20	11:02 AM GMT	Jun 21	06:04 AM BST	Sep 22	09:44 PM BST	Dec 21	05:11 PM GMT
	 * 2014	Mar 20	04:57 PM GMT	Jun 21	11:51 AM BST	Sep 23	03:29 AM BST	Dec 21	11:03 PM GMT
	 * 2015	Mar 20	10:45 PM GMT	Jun 21	05:38 PM BST	Sep 23	09:20 AM BST	Dec 22	04:48 AM GMT
	 * 2016	Mar 20	04:30 AM GMT	Jun 20	11:34 PM BST	Sep 22	03:21 PM BST	Dec 21	10:44 AM GMT
	 * 2017	Mar 20	10:29 AM GMT	Jun 21	05:24 AM BST	Sep 22	09:02 PM BST	Dec 21	04:28 PM GMT
	 * 2018	Mar 20	04:15 PM GMT	Jun 21	11:07 AM BST	Sep 23	02:54 AM BST	Dec 21	10:22 PM GMT
	 * 2019	Mar 20	09:58 PM GMT	Jun 21	04:54 PM BST	Sep 23	08:50 AM BST	Dec 22	04:19 AM GMT
	 * 2020	Mar 20	03:50 AM GMT	Jun 20	10:43 PM BST	Sep 22	02:31 PM BST	Dec 21	10:02 AM GMT
	 */
	
	public static void main(String[] args)
	{
		// verify against: http://www.jgiesen.de/astro/astroJS/siderealClock/
		for (int i=0; i < 1500; i++) {
			long zulu_time  = get_GMT_ms();
			int  gmt_year   = get_year(zulu_time);
			int  gmt_month  = get_month(zulu_time);
			int  gmt_day    = get_day_of_month(zulu_time);
			int  gmt_hour   = get_hour(zulu_time);
			int  gmt_minute = get_min(zulu_time);
			int  gmt_second = get_sec(zulu_time);
			System.out.printf("Zulu: %d %4d-%s-%02d %02d:%02d:%02d", zulu_time, 
					gmt_year, month_name[gmt_month], gmt_day, 
					gmt_hour, gmt_minute, gmt_second);
			
			System.out.print("  ");

			long local_time = get_local_ms();
			int  loc_year   = get_year(local_time);
			int  loc_month  = get_month(local_time);
			int  loc_day    = get_day_of_month(local_time);
			int  loc_hour   = get_hour(local_time);
			int  loc_minute = get_min(local_time);
			int  loc_second = get_sec(local_time);
			System.out.printf("Local: %d %4d-%s-%02d %02d:%02d:%02d", local_time, 
					loc_year, month_name[loc_month], loc_day, 
					loc_hour, loc_minute, loc_second);
			
			System.out.print("  ");

			long sidereal_time = get_local_sidereal_ms(zulu_time, new Angle(-106.629181, Angle.Scale.DEGREES));
			System.out.printf("Sidereal: %8d %02d:%02d:%02d", sidereal_time, get_hour(sidereal_time), get_min(sidereal_time), get_sec(sidereal_time));

			System.out.println();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static final String[] month_name   = {
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
		"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	@SuppressWarnings("unused")
	private static final String[] month_number = { 
		"01", "02", "03", "04", "05", "06", 
		"07", "08", "09", "10", "11", "12" };
	
	@SuppressWarnings("unused")
	private static final String[] two_digit   = { 
		"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", 
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
		"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", 
		"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", 
		"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", 
		"50", "51", "52", "53", "54", "55", "56", "57", "58", "59", 
		"60", "61", "62", "63", "64", "65", "66", "67", "68", "69", 
		"70", "71", "72", "73", "74", "75", "76", "77", "78", "79", 
		"80", "81", "82", "83", "84", "85", "86", "87", "88", "89", 
		"90", "91", "92", "93", "94", "95", "96", "97", "98", "99", 
		};

	@SuppressWarnings("unused")
	private static final String[] three_digit = { 
		"000", "001", "002", "003", "004", "005", "006", "007", "008", "009", 
		"010", "011", "012", "013", "014", "015", "016", "017", "018", "019", 
		"020", "021", "022", "023", "024", "025", "026", "027", "028", "029", 
		"030", "031", "032", "033", "034", "035", "036", "037", "038", "039", 
		"040", "041", "042", "043", "044", "045", "046", "047", "048", "049", 
		"050", "051", "052", "053", "054", "055", "056", "057", "058", "059", 
		"060", "061", "062", "063", "064", "065", "066", "067", "068", "069", 
		"070", "071", "072", "073", "074", "075", "076", "077", "078", "079", 
		"080", "081", "082", "083", "084", "085", "086", "087", "088", "089", 
		"090", "091", "092", "093", "094", "095", "096", "097", "098", "099", 

		"100", "101", "102", "103", "104", "105", "106", "107", "108", "109", 
		"110", "111", "112", "113", "114", "115", "116", "117", "118", "119", 
		"120", "121", "122", "123", "124", "125", "126", "127", "128", "129", 
		"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", 
		"140", "141", "142", "143", "144", "145", "146", "147", "148", "149", 
		"150", "151", "152", "153", "154", "155", "156", "157", "158", "159", 
		"160", "161", "162", "163", "164", "165", "166", "167", "168", "169", 
		"170", "171", "172", "173", "174", "175", "176", "177", "178", "179", 
		"180", "181", "182", "183", "184", "185", "186", "187", "188", "189", 
		"190", "191", "192", "193", "194", "195", "196", "197", "198", "199", 

		"200", "201", "202", "203", "204", "205", "206", "207", "208", "209", 
		"210", "211", "212", "213", "214", "215", "216", "217", "218", "219", 
		"220", "221", "222", "223", "224", "225", "226", "227", "228", "229", 
		"230", "231", "232", "233", "234", "235", "236", "237", "238", "239", 
		"240", "241", "242", "243", "244", "245", "246", "247", "248", "249", 
		"250", "251", "252", "253", "254", "255", "256", "257", "258", "259", 
		"260", "261", "262", "263", "264", "265", "266", "267", "268", "269", 
		"270", "271", "272", "273", "274", "275", "276", "277", "278", "279", 
		"280", "281", "282", "283", "284", "285", "286", "287", "288", "289", 
		"290", "291", "292", "293", "294", "295", "296", "297", "298", "299", 

		"300", "301", "302", "303", "304", "305", "306", "307", "308", "309", 
		"310", "311", "312", "313", "314", "315", "316", "317", "318", "319", 
		"320", "321", "322", "323", "324", "325", "326", "327", "328", "329", 
		"330", "331", "332", "333", "334", "335", "336", "337", "338", "339", 
		"340", "341", "342", "343", "344", "345", "346", "347", "348", "349", 
		"350", "351", "352", "353", "354", "355", "356", "357", "358", "359", 
		"360", "361", "362", "363", "364", "365", "366", "367", "368", "369", 
		"370", "371", "372", "373", "374", "375", "376", "377", "378", "379", 
		"380", "381", "382", "383", "384", "385", "386", "387", "388", "389", 
		"390", "391", "392", "393", "394", "395", "396", "397", "398", "399", 

		"400", "401", "402", "403", "404", "405", "406", "407", "408", "409", 
		"410", "411", "412", "413", "414", "415", "416", "417", "418", "419", 
		"420", "421", "422", "423", "424", "425", "426", "427", "428", "429", 
		"430", "431", "432", "433", "434", "435", "436", "437", "438", "439", 
		"440", "441", "442", "443", "444", "445", "446", "447", "448", "449", 
		"450", "451", "452", "453", "454", "455", "456", "457", "458", "459", 
		"460", "461", "462", "463", "464", "465", "466", "467", "468", "469", 
		"470", "471", "472", "473", "474", "475", "476", "477", "478", "479", 
		"480", "481", "482", "483", "484", "485", "486", "487", "488", "489", 
		"490", "491", "492", "493", "494", "495", "496", "497", "498", "499", 

		"500", "501", "502", "503", "504", "505", "506", "507", "508", "509", 
		"510", "511", "512", "513", "514", "515", "516", "517", "518", "519", 
		"520", "521", "522", "523", "524", "525", "526", "527", "528", "529", 
		"530", "531", "532", "533", "534", "535", "536", "537", "538", "539", 
		"540", "541", "542", "543", "544", "545", "546", "547", "548", "549", 
		"550", "551", "552", "553", "554", "555", "556", "557", "558", "559", 
		"560", "561", "562", "563", "564", "565", "566", "567", "568", "569", 
		"570", "571", "572", "573", "574", "575", "576", "577", "578", "579", 
		"580", "581", "582", "583", "584", "585", "586", "587", "588", "589", 
		"590", "591", "592", "593", "594", "595", "596", "597", "598", "599", 

		"600", "601", "602", "603", "604", "605", "606", "607", "608", "609", 
		"610", "611", "612", "613", "614", "615", "616", "617", "618", "619", 
		"620", "621", "622", "623", "624", "625", "626", "627", "628", "629", 
		"630", "631", "632", "633", "634", "635", "636", "637", "638", "639", 
		"640", "641", "642", "643", "644", "645", "646", "647", "648", "649", 
		"650", "651", "652", "653", "654", "655", "656", "657", "658", "659", 
		"660", "661", "662", "663", "664", "665", "666", "667", "668", "669", 
		"670", "671", "672", "673", "674", "675", "676", "677", "678", "679", 
		"680", "681", "682", "683", "684", "685", "686", "687", "688", "689", 
		"690", "691", "692", "693", "694", "695", "696", "697", "698", "699", 

		"700", "701", "702", "703", "704", "705", "706", "707", "708", "709", 
		"710", "711", "712", "713", "714", "715", "716", "717", "718", "719", 
		"720", "721", "722", "723", "724", "725", "726", "727", "728", "729", 
		"730", "731", "732", "733", "734", "735", "736", "737", "738", "739", 
		"740", "741", "742", "743", "744", "745", "746", "747", "748", "749", 
		"750", "751", "752", "753", "754", "755", "756", "757", "758", "759", 
		"760", "761", "762", "763", "764", "765", "766", "767", "768", "769", 
		"770", "771", "772", "773", "774", "775", "776", "777", "778", "779", 
		"780", "781", "782", "783", "784", "785", "786", "787", "788", "789", 
		"790", "791", "792", "793", "794", "795", "796", "797", "798", "799", 

		"800", "801", "802", "803", "804", "805", "806", "807", "808", "809", 
		"810", "811", "812", "813", "814", "815", "816", "817", "818", "819", 
		"820", "821", "822", "823", "824", "825", "826", "827", "828", "829", 
		"830", "831", "832", "833", "834", "835", "836", "837", "838", "839", 
		"840", "841", "842", "843", "844", "845", "846", "847", "848", "849", 
		"850", "851", "852", "853", "854", "855", "856", "857", "858", "859", 
		"860", "861", "862", "863", "864", "865", "866", "867", "868", "869", 
		"870", "871", "872", "873", "874", "875", "876", "877", "878", "879", 
		"880", "881", "882", "883", "884", "885", "886", "887", "888", "889", 
		"890", "891", "892", "893", "894", "895", "896", "897", "898", "899", 

		"900", "901", "902", "903", "904", "905", "906", "907", "908", "909", 
		"910", "911", "912", "913", "914", "915", "916", "917", "918", "919", 
		"920", "921", "922", "923", "924", "925", "926", "927", "928", "929", 
		"930", "931", "932", "933", "934", "935", "936", "937", "938", "939", 
		"940", "941", "942", "943", "944", "945", "946", "947", "948", "949", 
		"950", "951", "952", "953", "954", "955", "956", "957", "958", "959", 
		"960", "961", "962", "963", "964", "965", "966", "967", "968", "969", 
		"970", "971", "972", "973", "974", "975", "976", "977", "978", "979", 
		"980", "981", "982", "983", "984", "985", "986", "987", "988", "989", 
		"990", "991", "992", "993", "994", "995", "996", "997", "998", "999", 
	};
}
