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

import lib.util.Queue;

public class Shape {
	public String cn = this.getClass().getName();
	
	public Header header = null;
	public Record[] records = null;
	
	// open and parse the shape file
	// dir   - directory name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716"
	// layer - layer name, e.g., "parcels"
	public Shape(String dir, String layer)
	{
		File file = null;
		
		File parent = new File(dir);
		FilenameFilter filter = new ShapeFileFilter(layer);
		File[] list = parent.listFiles(filter);
		if (list != null) {
			file = list[0];
		}
		
		parse(file);
	}


	// open and parse the shape file
	// dir   - directory name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716"
	// layer - layer name, e.g., "parcels"
	public Shape(File parent, String layer)
	{
		File file = null;
		
		FilenameFilter filter = new ShapeFileFilter(layer);
		File[] list = parent.listFiles(filter);
		if (list != null) {
			file = list[0];
		}
		
		parse(file);
	}


	// open and parse the shape file
	// path - directory and layer name, e.g., "C:\Users\Doug\Desktop\home\projects\Math\parcels_092716\parcels"
	public Shape(String path)
	{
		File file = null;
		
		if (! path.matches(".*[.][Ss][Hh][Pp]$")) {
			File f0 = new File(path);
			File parent = f0.getParentFile();
			String layer = f0.getName();
			FilenameFilter filter = new ShapeFileFilter(layer);
			File[] list = parent.listFiles(filter);
			if (list != null) {
				file = list[0];
			}
		} else {
			file = new File(path);
		}
		
		parse(file);
	}
	
	public Shape(File file) throws IOException
	{
		String path = file.getCanonicalPath();
		
		if (! path.matches(".*[.][Ss][Hh][Pp]$")) {
			File f0 = new File(path);
			File parent = f0.getParentFile();
			String base = f0.getName();
			FilenameFilter filter = new ShapeFileFilter(base);
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
			if (header.file_end_loc != raf.length()) {
				System.out.println(fn+"internal file length "+header.file_end_loc+" does not equal actual file length "+raf.length());
			}
			
			Queue<Record> qr = new Queue<Record>();
			long end_loc = Header.header_end_loc;
			while (end_loc < header.file_end_loc) {
				Record rec = new Record(raf, end_loc);
				qr.append(rec);
				end_loc = rec.end_loc;
			}
			
			records = new Record[qr.length()];
			for (int i=0; i < records.length; i++) {
				records[i] = qr.remove();
			}
			
			System.out.println(fn+"total bytes processed "+header.file_end_loc);
			System.out.println(fn+"total records is "+records.length+" or "+Record.count);
			System.out.println(fn+"total points is "+Point.count);

			raf.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static class ShapeFileFilter implements FilenameFilter {
		String base = null;
		public ShapeFileFilter(String b)
		{
			base = b;
		}
		
		public boolean accept(File dir, String name) {
			return name.matches(base+"[.][Ss][Hh][Pp]$");
		}
	}

	private static final long big_int_size         = 4L;
	private static final long little_int_size      = 4L;
	private static final long double_size          = 8L;
	
	public static class Header {
		String cn = this.getClass().getName();

		public int    file_code        = 0;
		public int    file_length_x16b = 0;
		public int    file_end_loc     = 0;
		public int    file_version     = 0;
		public int    shape_type       = 0;
		public double bbox_xmin        = 0;
		public double bbox_ymin        = 0;
		public double bbox_xmax        = 0;
		public double bbox_ymax        = 0;
		public double bbox_zmin        = 0;
		public double bbox_zmax        = 0;
		public double bbox_mmin        = 0;
		public double bbox_mmax        = 0;

		public static final long file_code_loc        = 0L;
		public static final long file_length_x16b_loc = file_code_loc        + 6 * big_int_size;
		public static final long file_version_loc     = file_length_x16b_loc + big_int_size;
		public static final long shape_type_loc       = file_version_loc     + little_int_size;
		public static final long bbox_xmin_loc        = shape_type_loc       + little_int_size;
		public static final long bbox_ymin_loc        = bbox_xmin_loc        + double_size;
		public static final long bbox_xmax_loc        = bbox_ymin_loc        + double_size;
		public static final long bbox_ymax_loc        = bbox_xmax_loc        + double_size;
		public static final long bbox_zmin_loc        = bbox_ymax_loc        + double_size;
		public static final long bbox_zmax_loc        = bbox_zmin_loc        + double_size;
		public static final long bbox_mmin_loc        = bbox_zmax_loc        + double_size;
		public static final long bbox_mmax_loc        = bbox_mmin_loc        + double_size;
		public static final long header_end_loc       = bbox_mmax_loc        + double_size;

		public Header(RandomAccessFile raf) throws IOException
		{
			String fn = cn+".Header: ";
			
			file_code = read_big_int(raf, file_code_loc);
			System.out.println(fn+"file code: "+file_code);

			file_length_x16b = read_big_int(raf, file_length_x16b_loc);
			System.out.println(fn+"file length (16b): "+file_length_x16b);
			file_end_loc = 2 * file_length_x16b;

			file_version = read_little_int(raf, file_version_loc);
			System.out.println(fn+"file version: "+file_version);

			shape_type = read_little_int(raf, shape_type_loc);
			System.out.println(fn+"shape type: "+shape_type);

			bbox_xmin = read_double(raf, bbox_xmin_loc);
			System.out.println(fn+"bounding box xmin: "+bbox_xmin);

			bbox_ymin = read_double(raf, bbox_ymin_loc);
			System.out.println(fn+"bounding box ymin: "+bbox_ymin);

			bbox_xmax = read_double(raf, bbox_xmax_loc);
			System.out.println(fn+"bounding box xmax: "+bbox_xmax);

			bbox_ymax = read_double(raf, bbox_ymax_loc);
			System.out.println(fn+"bounding box ymax: "+bbox_ymax);

			bbox_zmin = read_double(raf, bbox_zmin_loc);
			System.out.println(fn+"bounding box zmin: "+bbox_zmin);

			bbox_zmax = read_double(raf, bbox_zmax_loc);
			System.out.println(fn+"bounding box zmax: "+bbox_zmax);

			bbox_mmin = read_double(raf, bbox_mmin_loc);
			System.out.println(fn+"bounding box mmin: "+bbox_mmin);

			bbox_mmax = read_double(raf, bbox_mmax_loc);
			System.out.println(fn+"bounding box mmax: "+bbox_mmax);			
		}
	}

	
	public static class Point {
		public static int count = 0;
		public double x = 0;
		public double y = 0;
		public Point()
		{
			count += 1;
		}
	}

	public static class Record {
		String cn = this.getClass().getName();

		public static int count = 0;

		public long start_loc = 0L;
		public long end_loc = 0L;

		// record header
		public long rec_num_loc;
		public long content_length_loc;

		// record contents
		public long shape_type_loc;
		public long bbox_xmin_loc;
		public long bbox_ymin_loc;
		public long bbox_xmax_loc;
		public long bbox_ymax_loc;
		public long num_parts_loc;
		public long num_points_loc;
		
		public int[] parts;
		public Point points[];

		// record header
		public int rec_num;
		public int content_length;

		// record contents
		public int shape_type;
		public double bbox_xmin;
		public double bbox_ymin;
		public double bbox_xmax;
		public double bbox_ymax;
		public int num_parts;
		public int num_points;

		public Record(RandomAccessFile raf, long rec_start_loc) throws IOException
		{
			String fn = cn+".Record: ";

			start_loc = rec_start_loc;

			// record header
			rec_num_loc = rec_start_loc;
			rec_num = read_big_int(raf, rec_num_loc);
			System.out.println(fn+"record number: "+rec_num);

			content_length_loc = rec_num_loc + big_int_size;
			content_length = read_big_int(raf, content_length_loc);
			System.out.println(fn+"content length: "+content_length);

			// record contents
			shape_type_loc = content_length_loc + big_int_size;
			shape_type = read_little_int(raf, shape_type_loc);
			System.out.println(fn+"record shape type: "+shape_type);

			bbox_xmin_loc = shape_type_loc + little_int_size;
			double bbox_xmin = read_double(raf, bbox_xmin_loc);
			System.out.println(fn+"record bbox xmin: "+bbox_xmin);

			bbox_ymin_loc = bbox_xmin_loc + double_size;
			bbox_ymin = read_double(raf, bbox_ymin_loc);
			System.out.println(fn+"record bbox ymin: "+bbox_ymin);

			bbox_xmax_loc = bbox_ymin_loc + double_size;
			bbox_xmax = read_double(raf, bbox_xmax_loc);
			System.out.println(fn+"record bbox xmax: "+bbox_xmax);

			bbox_ymax_loc = bbox_xmax_loc + double_size;
			bbox_ymax = read_double(raf, bbox_ymax_loc);
			System.out.println(fn+"record bbox ymax: "+bbox_ymax);

			num_parts_loc = bbox_ymax_loc + double_size;
			num_parts = read_little_int(raf, num_parts_loc);
			System.out.println(fn+"record num parts: "+num_parts);

			num_points_loc = num_parts_loc + little_int_size;
			num_points = read_little_int(raf, num_points_loc);
			System.out.println(fn+"record num points: "+num_points);
			
			parts = new int[num_parts];
			long part_loc = num_points_loc + little_int_size;
			for (int i=0; i < num_parts; i++) {
				parts[i] = read_little_int(raf, part_loc);
				part_loc += little_int_size;
				System.out.println(fn+"record part["+i+"]: "+parts[i]);
			}

			points = new Point[num_points];
			long point_loc = part_loc;
			for (int i=0; i < num_points; i++) {
				points[i] = new Point();
				points[i].x = read_double(raf, point_loc);
				point_loc += double_size;
				points[i].y = read_double(raf, point_loc);
				point_loc += double_size;
				System.out.println(fn+"record point["+i+"]: ("+points[i].x+","+points[i].y+")");
			}

			end_loc = point_loc;
			
			count += 1;
		}
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

	
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("user.dir"));
		Shape s = new Shape("C:\\Users\\Doug\\Desktop\\home\\projects\\Math\\parcels_092716\\parcels");
	}
}
