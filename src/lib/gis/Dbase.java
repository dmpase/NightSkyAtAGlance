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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

import lib.util.Queue;

@SuppressWarnings("unused")
public class Dbase {
	public String cn = this.getClass().getName();

	public Header   header = null;
	public Schema   schema = null;
	public Record[] record = null;

	// open and parse the shape file
	// dir   - directory name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716"
	// layer - layer name, e.g., "parcels"
	public Dbase(String dir, String layer)
	{
		File file = null;
		
		File parent = new File(dir);
		FilenameFilter filter = new DbaseFileFilter(layer);
		File[] list = parent.listFiles(filter);
		if (list != null) {
			file = list[0];
		}
		
		parse(file);
	}


	// open and parse the shape file
	// dir   - directory name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716"
	// layer - layer name, e.g., "parcels"
	public Dbase(File parent, String layer)
	{
		File file = null;
		
		FilenameFilter filter = new DbaseFileFilter(layer);
		File[] list = parent.listFiles(filter);
		if (list != null) {
			file = list[0];
		}
		
		parse(file);
	}


	// open and parse the shape file
	// path - directory and layer name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716\parcels"
	public Dbase(String path)
	{
		File file = null;
		
		if (! path.matches(".*[.][Dd][Bb][Ff]$")) {
			File f0 = new File(path);
			File parent = f0.getParentFile();
			String layer = f0.getName();
			FilenameFilter filter = new DbaseFileFilter(layer);
			File[] list = parent.listFiles(filter);
			if (list != null) {
				file = list[0];
			}
		} else {
			file = new File(path);
		}
		
		parse(file);
	}
	
	public Dbase(File file) throws IOException
	{
		String path = file.getCanonicalPath();
		
		if (! path.matches(".*[.][Dd][Bb][Ff]$")) {
			File f0 = new File(path);
			File parent = f0.getParentFile();
			String base = f0.getName();
			FilenameFilter filter = new DbaseFileFilter(base);
			File[] list = parent.listFiles(filter);
			if (list != null) {
				file = list[0];
			}
		}
		
		parse(file);
	}

	private void parse(File file)
	{
		String fn = cn+".parse: ";
		
		try {
			System.out.println(fn+"Opening file: "+file.getCanonicalPath());
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			
			header = new Header(raf);
			schema = new Schema(raf);

			Queue<Record> rq = new Queue<Record>();
			long record_start = schema.schema_end;
			Record rec = null;
			while ((rec=read_record(raf, schema, record_start)) != null) {
				rq.append(rec);
				record_start += schema.record_len;
//				if (50 < rq.length()) break;
			}

			record = new Record[rq.length()];
			for (int i=0; i < record.length; i++) {
				record[i] = rq.remove();
				System.out.println(i+" "+record[i].toString());
			}

			raf.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static class DbaseFileFilter implements FilenameFilter {
		String base = null;
		public DbaseFileFilter(String b)
		{
			base = b;
		}
		
		public boolean accept(File dir, String name) {
			return name.matches(base+"[.][Dd][Bb][Ff]$");
		}
	}
	
	public static class Header {
		String cn = this.getClass().getName();
		
		public int version = 0;
		public int level   = 0;
		public int last_update_yy       = 0;
		public int last_update_mm       = 0;
		public int last_update_dd       = 0;
		public int num_table_rec        = 0;
		public int num_header_bytes     = 0;
		public int num_rec_bytes        = 0;
		public int incomp_db4_xact      = 0;
		public int production_mdx_file  = 0;


		public String language_driver_name = null;

		public static final long version_loc              =  0L;
		public static final long last_update_yy_loc       =  1L;
		public static final long last_update_mm_loc       =  2L;
		public static final long last_update_dd_loc       =  3L;
		public static final long num_table_rec_loc        =  4L;
		public static final long num_header_bytes_loc     =  8L;
		public static final long num_rec_bytes_loc        = 10L;
		public static final long incomp_db4_xact_loc      = 14L;
		public static final long production_mdx_file_loc  = 28L;
		public static final long schema_loc = 32L;

		public Header(RandomAccessFile raf) throws IOException
		{
			String fn = cn+".Header: ";			

			version = read_byte(raf, version_loc);
			level = ((version&0x7) == 3) ? 5 : (((version & 0x7) == 4) ? 7 : -1);
			System.out.println(fn+"dBASE level="+level);

			last_update_yy = read_byte(raf, last_update_yy_loc);
			last_update_mm = read_byte(raf, last_update_mm_loc);
			last_update_dd = read_byte(raf, last_update_dd_loc);
			System.out.println(fn+"dBASE last update="+last_update_mm+"/"+last_update_dd+"/"+(1900+last_update_yy));

			num_table_rec = read_little_int(raf, num_table_rec_loc);
			System.out.println(fn+"number of records in the table="+num_table_rec);

			num_header_bytes = read_little_short(raf, num_header_bytes_loc);
			System.out.println(fn+"number of bytes in the header="+num_header_bytes);

			num_rec_bytes = read_little_short(raf, num_rec_bytes_loc);
			System.out.println(fn+"number of bytes in the record="+num_rec_bytes);

			incomp_db4_xact = read_little_short(raf, incomp_db4_xact_loc);
			System.out.println(fn+"incomplete dBASE IV transaction="+incomp_db4_xact);

			production_mdx_file = read_byte(raf, production_mdx_file_loc);
			System.out.println(fn+"production MDX file exists="+production_mdx_file);
		}
	}

	public static class Schema {
		String cn = this.getClass().getName();

		public FieldType[] field = null;
		public long schema_end = 0L;
		public int  record_len = 1;
		
		public static final long schema_loc = 32L;

		public Schema(RandomAccessFile raf) throws IOException
		{
			String fn = cn+".Schema: ";

			Queue<FieldType> ftq = new Queue<FieldType>();

			long record_loc = schema_loc;
			long field_loc  = record_loc;
			while (true) {
				byte[] ba = read_byte_array(raf, field_loc, FieldType.field_type_width);
				if (ba == null || ba[0] == 0x0d) {
					schema_end = field_loc + 1;
					print_byte_array(ba,0,1);
					break;
				}

				print_byte_array(ba);
				ftq.append(new FieldType(ba));

				field_loc += FieldType.field_type_width;
			}

			field = new FieldType[ftq.length()];

			for (int i=0; field != null & i < field.length; i++) {
				field[i] = ftq.remove();
				field[i].field_start_idx = record_len;
				record_len += field[i].width;
				System.out.println(fn+"field["+i+"].field_start="+field[i].field_start_idx);
			}
			
			System.out.println(fn+"width="+record_len);
		}
	}
	
	public static class FieldType {
		String cn = this.getClass().getName();

		public static final int field_type_width = 32;

		public String name  = null;
		public static final int name_start =  0;
		public static final int name_len   = 11;
		
		public char type = 0;
		public static final int type_start =  name_start + name_len;
		public static final int type_len   = 1;

		public static final int reserved_start = type_start + type_len;
		public static final int reserved_len = 4;
		
		public int width = 0;
		public static final int width_start = reserved_start + reserved_len;
		public static final int width_len = 1;
		
		public int field_start_idx = 1;

		public FieldType(byte[] rec) throws IOException
		{
			String fn = cn+".FieldType: ";

			if (rec == null) {
				return;
			}
	
			for (int i=0; i < name_len && i < rec.length; i++) {
				if (rec[name_start+i] == 0) {
					name = new String( rec, name_start, i );
					break;
				}
			}
			if (name == null) {
				name = new String( rec, name_start, name_len );
			}

			type = (char) rec[ type_start ];

			width = rec[ width_start ];
		}
	}

	public static class Record {
		Schema   schema = null;
		Object[] field = null;

		public Record(Schema s, byte[] ba)
		{
			schema = s;
			if (ba != null && schema != null && schema.field != null && ba.length == schema.record_len) {
				field = new Object[schema.field.length];
				for (int i=0; i < field.length; i++) {
					field[i] = parse_field(schema.field[i], ba);
				}
			}
		}
		
		public String toString()
		{
			String rv = "";
			
			for (int i=0; field != null && i < field.length; i++) {
				if (0 < i) {
					rv += ",";
				}
				if (field[i] == null) {
					rv += "null";
					continue;
				}
				switch (schema.field[i].type) {
				case 'F' :
					rv += ((Double)field[i]).doubleValue();
					break;
				case 'N' :
					rv += ((Long)field[i]).longValue();
					break;
				default :
					rv += "unknown";
					break;
				}
			}

			return rv;
		}
	}

	public static Record read_record(RandomAccessFile raf, Schema schema, long record_start) throws IOException
	{
		String fn = "Dbase.read_record: ";

		Record rv = null;
		
		byte[] buf = new byte[schema.record_len];
		raf.seek(record_start);
		int len = raf.read(buf);
		if (len == schema.record_len) {
			rv = new Record(schema, buf);
		}

		return rv;
	}
			
	public static Object parse_field(FieldType type, byte[] buf)
	{
		String fn = "Dbase.parse_field: ";

		Object rv = null;
		
		if (buf != null && type != null) {
			switch (type.type) {
			case 'F' :
				Double flt = Double.parseDouble(new String(buf, type.field_start_idx, type.width));
				rv = flt;
				break;
			case 'N' :
				Long num = Long.parseLong(new String(buf, type.field_start_idx, type.width));
				rv = num;
				break;
			default :
				System.out.println(fn+"found field ("+type.name+") unknown type ("+type.type+")");
				System.out.println(fn+new String(buf, type.field_start_idx, type.width));
				break;
			}
		}
		
		return rv;
	}
	
	public static double parse_double(byte[] buf, int start, int width)
	{
		double rv = Double.NaN;
		
		if (buf != null) {
			int end = (start+width < buf.length) ? start+width : buf.length;

			// eliminate leading white space and garbage
			while (start < end) {
				if (buf[start] == '+' || buf[start] == '-' || ('0' <= buf[start] && buf[start] <= '9')) {
					break;
				}
				
				start += 1;
			}
			
			// process the leading sign ('+' or '-')
			double sign = 1;
			if (start < end && buf[start] == '-') {
				sign = -1;
				start += 1;
			} else if (start < end && buf[start] == '+') {
				start += 1;
			}
		
			// process digits on the left side of the '.' or 'e' or 'E' or 'd' or 'D'
			rv = 0;
			while (start < end && '0' <= buf[start] && buf[start] <= '9') {
				rv = rv * 10 + (buf[start] - '0');
				start += 1;
			}
			
			// process digits on the right side of the '.'
			if (start < end && buf[start] == '.') {
				start += 1;
				double factor = 1;
				while (start < end && '0' <= buf[start] && buf[start] <= '9') {
					factor *= 0.1;
					rv += factor * (buf[start] - '0');
					start += 1;
				}
			}
			
			// process digits on the right side of the 'E' or 'e' or 'D' or 'd
			if (start < end && (buf[start] == 'E' || buf[start] == 'e' || buf[start] == 'D' || buf[start] == 'd')) {
				start += 1;
				double mult = buf[start] == '-' ? 0.1 : 10.0;
				if (buf[start] == '-' || buf[start] == '+') {
					start += 1;
				}

				int exponent = 0;
				while (start < end && '0' <= buf[start] && buf[start] <= '9') {
					exponent = exponent*10 + (buf[start] - '0');
					start += 1;
				}
				
				for (int i=0; i < exponent; i++) {
					rv *= mult;
				}
			}
		}

		return rv;
	}

	public static void print_byte_array(byte[] ba)
	{
		if (ba != null) {
			print_byte_array(ba, 0, ba.length);
		}
	}

	public static void print_byte_array(byte[] ba, int start, int len)
	{
		if (ba != null) {
			for (int i=start; i < start+len && i < ba.length; i++) {
				System.out.print(hex[(ba[i]>>4)&0xf]);
				System.out.print(hex[(ba[i]>>0)&0xf]);
			}
			System.out.println();
			for (int i=start; i < start+len && i < ba.length; i++) {
				if (ba[i] < ' ') {
					System.out.print(". ");
				} else {
					System.out.print((char)ba[i]+" ");
				}
			}
			System.out.println();
		}
	}

	public static int read_byte(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		return raf.read();
	}

	public static byte[] read_byte_array(RandomAccessFile raf, long loc, int len) throws IOException
	{
		byte[] buf = new byte[len];
		raf.seek(loc);
		raf.read(buf);
		
		return buf;
	}

	public static int read_big_short(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		return raf.readShort();
	}
	
	public static int read_little_short(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		return change_endian(raf.readShort());
	}

	public static int read_big_int(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		return raf.readInt();
	}
	
	public static int read_little_int(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		return change_endian(raf.readInt());
	}
	
	public static double read_double(RandomAccessFile raf, long loc) throws IOException
	{
		raf.seek(loc);
		long bits = change_endian(raf.readLong());
		double result = Double.longBitsToDouble(bits);

		return result;
	}

	private static short change_endian(short ia)
	{
		return (short)(((ia <<  8) & 0xff00) | ((ia >>  8) & 0x00ff));
	}

	private static int change_endian(int ia)
	{
		return 
			((ia << 24) & 0xff000000) | 
			((ia <<  8) & 0x00ff0000) | 
			((ia >>  8) & 0x0000ff00) | 
			((ia >> 24) & 0x000000ff)
			;
	}

	private static long change_endian(long ia)
	{
		return 
			((ia << 56) & 0xff00000000000000L) | 
			((ia << 40) & 0x00ff000000000000L) | 
			((ia << 24) & 0x0000ff0000000000L) | 
			((ia <<  8) & 0x000000ff00000000L) |
			((ia >>  8) & 0x00000000ff000000L) | 
			((ia >> 24) & 0x0000000000ff0000L) | 
			((ia >> 40) & 0x000000000000ff00L) | 
			((ia >> 56) & 0x00000000000000ffL)
			;
	}

	private static String[] hex = {
			"0",	"1",	"2",	"3",	"4",	"5",	"6",	"7", 
			"8",	"9",	"a",	"b",	"c",	"d",	"e",	"f", 
	};
	
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("user.dir"));
		Dbase d = new Dbase("C:\\Users\\Doug\\Desktop\\home\\projects\\Math\\parcels_092716\\parcels");
	}
}
