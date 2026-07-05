package nightskyataglance.gui;

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


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import lib.astro.PracticalAstronomy;
import lib.stars.catalog.ArpCatalog;
import lib.stars.catalog.CaldwellCatalog;
import lib.stars.catalog.DsoAlias;
import lib.stars.catalog.HerschelCatalog;
import lib.stars.catalog.IAUCatalog;
import lib.stars.catalog.MessierCatalog;
import lib.stars.catalog.NamedDsos;
import lib.stars.catalog.NgcIcCatalog;
import lib.stars.catalog.SharplessCatalog;
import lib.stars.catalog.UgcCatalog;
import lib.stars.catalog.VsxCatalog;
import lib.stars.catalog.WdsCatalog;
import lib.stars.catalog.YaleBrightStarAscCatalog;
import lib.stars.image.CreateImage;
import lib.stars.image.MapEquatorialToPlane;
import lib.stars.catalog.DsoAlias.Element;
import lib.stars.catalog.DsoType;
import lib.stars.catalog.Herschel2500;
import lib.stars.catalog.Herschel400;
import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class ImageFrame extends JFrame implements WindowListener, ComponentListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 6393127392158527863L;

	private final MainFrame main_frame;
	private final Element   elt;
	private final double    width_arcmin;
	private final double    height_arcmin;
	private final double    width_deg;
	private final double    height_deg;
	private final double    width_over_height;
	
	public static final double max_width_amin  = 120;
	public static final double max_height_amin = 120;

	private final BufferedImage original_image;

	private final double    ctr_ra_hrs;
	private final double    ctr_ra_deg;
	private final double    ctr_de_deg;
	
	private int             width_px;
	private int             height_px;
	private BufferedImage   image;

	public int width_px()  { return width_px;  }
	public int height_px() { return height_px; }

	private enum  ProjectionType {LINEAR, GNOMONIC}
	private final ProjectionType projection_type = ProjectionType.GNOMONIC;
	private final MapEquatorialToPlane map;

	private Queue<Segment>         segments = new LinkedList<Segment>();
	private Queue<EquatorialPoint> points   = new LinkedList<EquatorialPoint>();

	private static final double left =  7;
	private static final double top  = 30;
	JPopupMenu popupMenu = new JPopupMenu();
	
	public static class Segment {
		public final EquatorialPoint e0;
		public final EquatorialPoint e1;
		public final double edist_deg;
		public final String edist_deg_str;

		public Segment(double ra0_hrs, double de0_deg, int x0, int y0, double ra1_hrs, double de1_deg, int x1, int y1)
		{
			e0 = new EquatorialPoint(ra0_hrs, de0_deg, x0, y0);
			e1 = new EquatorialPoint(ra1_hrs, de1_deg, x1, y1);

			edist_deg     = PracticalAstronomy.angle_between_celestial_objects(e0.ra_hrs, e0.de_deg, e1.ra_hrs, e1.de_deg);
			edist_deg_str = PracticalAstronomy.decimal_degrees_to_str_dms3(edist_deg);
		}

		public Segment(EquatorialPoint e0, EquatorialPoint e1)
		{
			this.e0 = e0;
			this.e1 = e1;
			edist_deg     = PracticalAstronomy.angle_between_celestial_objects(e0.ra_hrs, e0.de_deg, e1.ra_hrs, e1.de_deg);
			edist_deg_str = PracticalAstronomy.decimal_degrees_to_str_dms3(edist_deg);
		}

		@Override public String toString()
		{
			return String.format("e0=(%s) e1=(%s) dist='%s'", e0.toString(), e1.toString(), edist_deg_str);
		}
	}
	
	public static class EquatorialPoint {
		public final double ra_hrs;
		public final double de_deg;
		public final int    x_px;
		public final int    y_px;
		public final String str;

		public EquatorialPoint(double ra, double de, int x, int y)
		{
			ra_hrs = ra;
			de_deg = de;
			x_px   = x;
			y_px   = y;
			str = PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs) + ", " + PracticalAstronomy.decimal_degrees_to_str_dms(de_deg);
		}

		@Override public String toString()
		{
			return String.format("ra=%f de=%f x=%d y=%d str='%s'", ra_hrs, de_deg, x_px, y_px, str);
		}
	}

	public ImageFrame(MainFrame mf, Element e, double w_arcmin, double h_arcmin, int w_px, int h_px)
	{
		main_frame    = mf;
		elt           = e;

		ctr_ra_hrs = elt.ra_hrs();
		ctr_de_deg = elt.de_deg();
		ctr_ra_deg = PracticalAstronomy.hours_to_degrees(ctr_ra_hrs);
		
		width_over_height = w_arcmin / h_arcmin;

		String ra_hrs_str = PracticalAstronomy.decimal_hours_to_str_hms(ctr_ra_hrs);
		String de_deg_str = PracticalAstronomy.decimal_degrees_to_str_dms(ctr_de_deg);

		String title;
		if (elt.is_custom()) {
			title = String.format("RA %s, DE %s, %.0f' x %.0f'", ra_hrs_str, de_deg_str, w_arcmin, h_arcmin);
		} else {
			title = String.format("%s RA %s, DE %s, %.0f' x %.0f'", elt.names()[0], ra_hrs_str, de_deg_str, w_arcmin, h_arcmin);
		}
		setTitle(title);

		double w,h;
		if (max_width_amin < w_arcmin || max_height_amin < h_arcmin) {
			String msg = String.format("Your requested image size is %.3f' x %.3f'.%n", w_arcmin, h_arcmin) + 
					"Images must be smaller than 2"+PracticalAstronomy.degree_symbol+" x 2"+PracticalAstronomy.degree_symbol+" (120' x 120').";
			JOptionPane.showMessageDialog(this, msg, "Image Size Error", JOptionPane.PLAIN_MESSAGE);

			// rescale the image to fit within 120' x 120'
			if (h_arcmin < w_arcmin) {
				w = max_width_amin;
				h = w * (h_arcmin / w_arcmin);
			} else {
				h = max_height_amin;
				w = h * (w_arcmin / h_arcmin);
			}
		} else {
			w = w_arcmin;
			h = h_arcmin;
		}
		width_arcmin  = w;
		height_arcmin = h;
		
		width_deg  = width_arcmin  / PracticalAstronomy.minutes_per_degree;
		height_deg = height_arcmin / PracticalAstronomy.minutes_per_degree;

		resize_frame(new Dimension(w_px, h_px));
		setPreferredSize(new Dimension(width_px, height_px));

		BufferedImage img = null;
		try {
			img = get_sky_patch(ctr_ra_hrs, ctr_de_deg, width_arcmin, height_arcmin, width_px, height_px);
		} catch (URISyntaxException ex) {
			JOptionPane.showMessageDialog(main_frame, ex.getMessage(), "URI Syntax Problem", JOptionPane.PLAIN_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(main_frame, ex.getMessage(), "Network Problem", JOptionPane.PLAIN_MESSAGE);
		}
		original_image = image = img;

		/*
		System.out.printf("%s: %7d: RA %s (%f), DE %s, w %s/%d, h %s/%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), 
				PracticalAstronomy.decimal_hours_to_str_hms(ctr_ra_hrs), ctr_ra_deg, 
				PracticalAstronomy.decimal_degrees_to_str_dms(ctr_de_deg),
				PracticalAstronomy.decimal_degrees_to_str_dms(width_deg),  width_px,
				PracticalAstronomy.decimal_degrees_to_str_dms(height_deg), height_px);
		 */

		map = new MapEquatorialToPlane(ctr_ra_deg, ctr_de_deg, width_deg, height_deg, width_px, height_px);
	}

	@Override public void paint(Graphics g)
	{
		if (original_image != null) {
	        Image scaled_image = original_image.getScaledInstance(width_px, height_px, BufferedImage.SCALE_DEFAULT);
	        image = CreateImage.create_buffered_image(scaled_image);
		    g.drawImage(image, 0, 0, null);
		} else {
			Dimension d = getSize();
			String str = "Image Not Available";
			int w = g.getFontMetrics().getStringBounds(str, g).getBounds().width;
			int h = g.getFontMetrics().getHeight();
			int box_width  = w;
		    int box_height = h;
		    int box_x = (d.width  - w) / 2;
		    int box_y = (d.height - h) / 2;
		    g.setColor(Color.WHITE);
		    g.fillRect(box_x, box_y, box_width, box_height);
		    g.setColor(Color.BLACK);
		    g.drawString(str, box_x + 10, box_y + 3*h/4);
		}

	    // add the target and location label
	    Iterator<EquatorialPoint> point_iter = points.iterator();
	    while (point_iter.hasNext()) {
	    	EquatorialPoint point = point_iter.next();
	    	drawLocation(g, point, 5, Color.CYAN);
 	    	// System.out.printf("%s: %d: path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), point.toString());
	    }

	    // add the end point circles, line segment, location labels, and distance label
	    Iterator<Segment> seg_iter = segments.iterator();
	    while (seg_iter.hasNext()) {
	    	Segment segment = seg_iter.next();
	    	drawSegment(g, segment, 5, Color.MAGENTA);
 	    	// System.out.printf("%s: %d: path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), segment.toString());
	    }

	    // is there a selected DSO
	    if (main_frame != null && main_frame.selected_dso != null) {
	    	// is the selected DSO within the current frame boundaries
	    	double dso_ra_hrs = main_frame.selected_dso.ra_hrs();
	    	double dso_ra_deg = PracticalAstronomy.hours_to_degrees(dso_ra_hrs);
	    	double dso_de_deg = main_frame.selected_dso.de_deg();
	    	boolean is_in_the_box = 
	    			(ctr_ra_deg - width_arcmin /PracticalAstronomy.minutes_per_degree) <= dso_ra_deg && dso_ra_deg <= (ctr_ra_deg + width_arcmin /PracticalAstronomy.minutes_per_degree) &&
		    		(ctr_de_deg - height_arcmin/PracticalAstronomy.minutes_per_degree) <= dso_de_deg && dso_de_deg <= (ctr_de_deg + height_arcmin/PracticalAstronomy.minutes_per_degree);
	    	if (is_in_the_box) {
	    		int[] xy = eq_to_px(dso_ra_hrs, dso_de_deg);
	    		if (xy != null) {
		    		int x = xy[0];
		    		int y = xy[1];
		    		EquatorialPoint point = new EquatorialPoint(dso_ra_hrs, dso_de_deg, x, y);
		    		drawLocation(g, point, 5, Color.RED);
	    		}
	    	}
	    }

	    // setSize(width_px, height_px);
		drawLocationBox(g, mouse_location.x, mouse_location.y);
	}

	public Dimension resize_frame(Dimension d)
	{
		return resize_frame(d.width, d.height);
	}

	// TODO resize_frame needs work...
	public Dimension resize_frame(int wp, int hp)
	{
		Dimension old = new Dimension(width_px, height_px);
		
		if (wp == 0 && hp == 0) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screen_size = toolkit.getScreenSize();
			int frame_width_px  = 2 * screen_size.width  / 3;
			int frame_height_px = 2 * screen_size.height / 3;

			int sv_w = (int) (frame_height_px * width_over_height);
			int sv_h = frame_height_px;
			int sh_w = frame_width_px;
			int sh_h = (int) (frame_width_px  / width_over_height);

			if (sv_w < sh_w) {
				width_px  = sv_w;
				height_px = sv_h;
			} else {
				width_px  = sh_w;
				height_px = sh_h;
			}
		} else if (wp != 0 && hp == 0) {
			width_px  = wp;
			height_px = (int) (wp / width_over_height);
		} else if (wp == 0 && hp != 0) {
			width_px  = (int) (hp * width_over_height);
			height_px = (int) (wp / width_over_height);
		} else {
			int sv_w = (int) (hp * width_over_height);
			int sv_h = hp;
			int sh_w = wp;
			int sh_h = (int) (wp / width_over_height);

			if (sv_w < hp) {
				width_px  = sv_w;
				height_px = sv_h;
			} else {
				width_px  = sh_w;
				height_px = sh_h;
			}
		}

		return old;
	}
	
	private BufferedImage get_sky_patch(double ra_hrs, double de_deg, double width_arcmin, double height_arcmin, int width_px, int height_px) throws URISyntaxException, IOException
	{
		// System.out.printf("%s: %d: cache='%s' path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), cache_images, cache_path);
		BufferedImage scaled_buffered_image   = null;
		if (0 < width_arcmin && width_arcmin <= max_width_amin && 0 < height_arcmin && height_arcmin <= max_height_amin) {	// TODO
	        BufferedImage unscaled_buffered_image = read_image_from_disk(ra_hrs, de_deg, width_arcmin, height_arcmin, width_px, height_px);

			if (unscaled_buffered_image == null) {
				String ra_hrs_str = PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs);
				String de_deg_str = PracticalAstronomy.decimal_degrees_to_str_dms(de_deg);

				ra_hrs_str = ra_hrs_str.replaceAll("[hms\"']", "").replaceAll("[ ]", "+");
				de_deg_str = de_deg_str.replaceAll("[dms"+PracticalAstronomy.degree_symbol+"\"']", "").replaceAll("[ ]", "+");
				// width_arcmin   = width_arcmin  + 1 - 1e-12;
				// height_arcmin  = height_arcmin + 1 - 1e-12;
				String uri_str = String.format("https://archive.stsci.edu/cgi-bin/dss_search?v=poss2ukstu_red&r=%s&d=%s&e=J2000&w=%.0f&h=%.0f&f=gif", ra_hrs_str, de_deg_str, width_arcmin, height_arcmin);
	
				URI uri = new URI(uri_str);
				URL url = uri.toURL();
		        InputStream is = url.openStream(); 
				byte[] ch = is.readAllBytes();
		        is.close();
	
		        unscaled_buffered_image = CreateImage.create_buffered_image(ch);
		        write_image_to_disk(unscaled_buffered_image, ra_hrs, de_deg, width_arcmin, height_arcmin, width_px, height_px);
			}

	        Image scaled_image    = (unscaled_buffered_image != null) ? unscaled_buffered_image.getScaledInstance(width_px, height_px, BufferedImage.SCALE_DEFAULT) : null;
			scaled_buffered_image = (scaled_image != null) ? CreateImage.create_buffered_image(scaled_image) : null;
		} else {
			String msg = 
				String.format("Something is wrong with our requested image size of %.3f' x %.3f'.%n") + 
				"Images must be greater than zero and smaller than 2"+PracticalAstronomy.degree_symbol+" x 2"+PracticalAstronomy.degree_symbol+" (120' x 120').";
			JOptionPane.showMessageDialog(main_frame, msg, "Image Size Error", JOptionPane.PLAIN_MESSAGE);
		}

		return scaled_buffered_image;
	}

	private String get_image_file_name(double ra_hrs, double de_deg, double width_arcmin, double height_arcmin) throws IOException 
	{
		File img_file = new File(PersistentState.cache_path, String.format("RA%s DE%s %.1fx%.1f.gif",
				PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs), 
				PracticalAstronomy.decimal_degrees_to_sstr_dms(de_deg),
				width_arcmin, height_arcmin));
		// System.out.printf("%s: %d: img='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), img_file.getCanonicalPath());
		return img_file.getCanonicalPath();
	}
	
	private BufferedImage read_image_from_disk(double ra_hrs, double de_deg, double width_arcmin, double height_arcmin, int width_px, int height_px) throws IOException 
	{
		// System.out.printf("%s: %d: cache='%s' path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), cache_images, cache_path);
		BufferedImage img = null;
		if (PersistentState.cache_images) {
			String file_name = get_image_file_name(ra_hrs, de_deg, width_arcmin, height_arcmin);
			File img_file = new File(file_name);
			// System.out.printf("%s: %d: img='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), img_file.getCanonicalPath());
			if (img_file.canRead()) {
				byte[] ch = new byte[(int) img_file.length()];
				RandomAccessFile raf = new RandomAccessFile(img_file, "r");
				raf.readFully(ch);
				raf.close();
	
		        BufferedImage unscaled_buffered_image = CreateImage.create_buffered_image(ch);
		        Image scaled_image = unscaled_buffered_image.getScaledInstance(width_px, height_px, BufferedImage.SCALE_DEFAULT);
		        img = CreateImage.create_buffered_image(scaled_image);
			}
		}
		
		return img;
	}

	private void write_image_to_disk(BufferedImage img, double ra_hrs, double de_deg, double width_arcmin, double height_arcmin, int width_px, int height_px) throws IOException 
	{
		if (PersistentState.cache_images) {
			// System.out.printf("%s: %d: cache='%s' path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), cache_images, cache_path);
			String file_name = get_image_file_name(ra_hrs, de_deg, width_arcmin, height_arcmin);
			File img_file = new File(file_name);
			if (! img_file.exists()) {
				// System.out.printf("%s: %d: img='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), img_file.getCanonicalPath());
				ImageIO.write(img, "gif", img_file);
			}
		}
	}

	int[] eq_to_px(double dso_ra_hrs, double dso_de_deg)
	{
		if (projection_type == ProjectionType.LINEAR) {
			return new int[] {lin_ra_to_px(dso_ra_hrs), lin_de_to_py(dso_de_deg)};
		} else {
			return gno_eq_to_px(dso_ra_hrs, dso_de_deg);
		}
	}

	double[] px_to_eq(int px, int py)
	{
		if (projection_type == ProjectionType.LINEAR) {
			return new double[] {lin_px_to_ra(px), lin_py_to_de(py)};
		} else {
			return gno_px_to_eq(px, py);
		}
	}

	private int[] gno_eq_to_px(double ra_hrs, double de_deg)
	{
		double ra_deg = PracticalAstronomy.hours_to_degrees(ra_hrs);

		return map.eq_deg_to_pl_pu(ra_deg, de_deg);
	}

	private double[] gno_px_to_eq(int x, int y)
	{
		double[] eq = map.pl_pu_to_eq_deg(x, y);

		double ra_hrs = PracticalAstronomy.degrees_to_hours(eq[0]);
		double de_deg = eq[1];

		return new double[] {ra_hrs, de_deg};
	}

	private int lin_ra_to_px(double ra_hrs)
	{
		double width_deg  = width_arcmin / 60.0 / 15.0;
		int px = (int) (width_px + left - (((ra_hrs + width_deg / 2 - ctr_ra_hrs) / width_deg) * width_px));

		return px;
	}

	private int lin_de_to_py(double de_deg)
	{
		double height_deg = height_arcmin / 60.0;
		int py = (int) (height_px + top - ((de_deg + height_deg / 2 - ctr_de_deg) / height_deg) * height_px);
		
		return py;
	}

	private double lin_px_to_ra(int px)
	{
		double width_deg  = width_arcmin / 60.0 / 15.0;
		double ra_hrs = ((double)(width_px - px + left) / (double) width_px) * width_deg  + ctr_ra_hrs - width_deg / 2;
		
		return ra_hrs;
	}

	private double lin_py_to_de(int py)
	{
		double height_deg = height_arcmin / 60.0;
		double de_deg = ((double) (height_px - py + top) / (double)height_px) * height_deg + ctr_de_deg - height_deg / 2;
		
		return de_deg;
	}

	private String xy_to_eq(int x, int y)
	{
		double[] eq = px_to_eq(x, y);
		double r = eq[0];
		double d = eq[1];
		return String.format("%s,%s", PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	private final void drawTarget(Graphics g, int x, int y, int len_px, Color color)
	{
		Color old_color = g.getColor();
		g.setColor(color);
		g.drawLine(x - 2*len_px, y,            x - len_px, y);
		g.drawLine(x + 2*len_px, y,            x + len_px, y);
		g.drawLine(x,            y - 2*len_px, x,          y - len_px);
		g.drawLine(x,            y + 2*len_px, x,          y + len_px);
		g.setColor(old_color);
	}

	private final void drawLocation(Graphics g, EquatorialPoint p, int len_px, Color color)
	{
		drawLocation(g, p, len_px, color, 10, 10);
	}

	private final void drawLocation(Graphics g, EquatorialPoint p, int len_px, Color color, int off_x, int off_y)
	{
		Color old_color = g.getColor();
		g.drawOval(p.x_px-len_px/2, p.y_px-len_px/2-1, len_px-1, len_px);
		drawTarget(g, p.x_px, p.y_px, len_px, color);
		drawLabel(g, p, color, off_x, off_y);
		g.setColor(old_color);
	}
	
	private final void drawLocationBox(Graphics g, int x, int y)
	{
		Dimension d = getSize();
	    String str = xy_to_eq(x, y);
		int w = g.getFontMetrics().getStringBounds(str, g).getBounds().width;
		int h = g.getFontMetrics().getHeight();
		int box_width  = Math.max(w, 175);
	    int box_height = h;
	    int box_x = 0;
	    int box_y = d.height - box_height - 10;
	    g.setColor(Color.WHITE);
	    g.fillRect(box_x, box_y, box_width, box_height);
	    g.setColor(Color.BLACK);
	    g.drawString(str, box_x + 10, box_y + 3*h/4);
    }

	private final void drawLabel(Graphics g, EquatorialPoint p, Color color, int off_x, int off_y)
	{
		Color old_color = g.getColor();
		int w = g.getFontMetrics().getStringBounds(p.str, g).getBounds().width;
		int h = g.getFontMetrics().getHeight();
		g.setColor(Color.WHITE);
		g.fillRect(p.x_px + off_x, p.y_px + off_y, w + 10, h + 4);
		g.setColor(Color.BLACK);
		g.drawString(p.str, p.x_px + off_x + 5, p.y_px + off_y + h - 2);
		g.setColor(old_color);
	}

	private final void drawSegment(Graphics g, Segment s, int len_px, Color color)
	{
		Color old_color = g.getColor();
		g.setColor(color);
		g.drawOval(s.e0.x_px-len_px/2, s.e0.y_px-len_px/2-1, len_px-1, len_px);
		g.drawOval(s.e1.x_px-len_px/2, s.e1.y_px-len_px/2-1, len_px-1, len_px);
		g.drawLine(s.e0.x_px, s.e0.y_px, s.e1.x_px, s.e1.y_px);

		drawLocation(g, s.e0, len_px, color, 10, -30);
		drawLocation(g, s.e1, len_px, color,  5,  10);

		Point p = new Point((s.e0.x_px + s.e1.x_px)/2, (s.e0.y_px + s.e1.y_px)/2);
		g.setColor(Color.WHITE);
		String str = s.edist_deg_str;
		int w = g.getFontMetrics().getStringBounds(str, g).getBounds().width;
		int h = g.getFontMetrics().getHeight();
		g.fillRect(p.x + 10, p.y - 10, w + 10, h + 4);
		g.setColor(Color.BLACK);
		g.drawString(str, p.x + 10 + 5, p.y - 10 + h - 2);
		g.setColor(old_color);
	}

	@Override public void windowOpened(WindowEvent e)      {}
	@Override public void windowClosing(WindowEvent e)     { dispose(); }
	@Override public void windowClosed(WindowEvent e)      {}
	@Override public void windowIconified(WindowEvent e)   {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowActivated(WindowEvent e)   {}
	@Override public void windowDeactivated(WindowEvent e) {}

	@Override public void componentResized(ComponentEvent e) 
	{
		Dimension d = getSize();
    	// System.out.printf("%s: %d: size=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), d);
		resize_frame(d);
		repaint();
	}

	@Override public void componentMoved(ComponentEvent e)   {}
	@Override public void componentShown(ComponentEvent e)   {}
	@Override public void componentHidden(ComponentEvent e)  {}

	// TODO mouse actions
	@Override public void mouseClicked(MouseEvent e)
	{
		Point p = e.getPoint();
		int x = p.x;
		int y = p.y;
		double[] eq = px_to_eq(x, y);
		double r = eq[0];
		double d = eq[1];

		if (e.isAltDown()) {
			if (e.isShiftDown() && ! e.isControlDown()) {
				// ALT click -- selects end points for angle of separation calculations, SHFT click for the first end point, CTRL click for the second end point
				main_frame.alt_shft_click(r, d, e.getClickCount());
			} else if (! e.isShiftDown() && e.isControlDown()) {
				main_frame.alt_ctrl_click(r, d, e.getClickCount());
			} else if (e.isShiftDown() && e.isControlDown()) {
				main_frame.alt_shft_ctrl_click(r, d, e.getClickCount());
			} else {
				main_frame.alt_click(r, d, e.getClickCount());
			}
		} else if (e.isControlDown() && e.isShiftDown()) {
 			// CTRL SHIFT click -- erases the selected points 
 	    	// System.out.printf("%s: %d: ctrl shift click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			points.clear();
 			segments.clear();
 		} else if (0 < e.getClickCount() && e.isShiftDown()) {
 			// double SHIFT click -- pushes the selection to the main window 
 	    	// System.out.printf("%s: %d: double shift click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			EquatorialPoint point = new EquatorialPoint(r, d, x, y);
 			points.add(point);
 			if (main_frame != null) {
 	 	    	// System.out.printf("%s: %d: set selected dso RA %s, DE %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
 				main_frame.selected_dso = new DsoAlias.Element(new String[] {"Custom"}, r, d, DsoAlias.Element.NO_MAG, DsoAlias.Element.NO_MAG, DsoAlias.Element.NO_SIZE, DsoType.custom[0]);
 			}
 		} else if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
 	    	System.out.printf("%s: %d: ctrl click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			;
 		} else if (e.isShiftDown()) {
 	    	System.out.printf("%s: %d: shift click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			;
 		} else if (e.isAltDown()) {
 	    	System.out.printf("%s: %d: alt click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			;
 		} else if (e.getClickCount() == 2) {
 			// double click -- draws the RA and DE on the image
 	    	// System.out.printf("%s: %d: double click%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
 			EquatorialPoint point = new EquatorialPoint(r, d, x, y);
 			points.add(point);
 	    	// System.out.printf("%s: %d: point='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), point.toString());
 		}
 		repaint();
	}

	EquatorialPoint start = null;
	@Override public void mousePressed(MouseEvent e)
	{
		Point p = e.getPoint();
		int x = p.x;
		int y = p.y;
		double[] eq = px_to_eq(x, y);
		double r = eq[0];
		double d = eq[1];
		if (((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK && (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != MouseEvent.SHIFT_DOWN_MASK && (e.getModifiersEx() & MouseEvent.ALT_DOWN_MASK) != MouseEvent.ALT_DOWN_MASK) || e.getButton() == MouseEvent.BUTTON3) {
			start = new EquatorialPoint(r, d, x, y);
 	    	// System.out.printf("%s: %d: start='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), start.toString());
		}
	}

	@Override public void mouseReleased(MouseEvent e)
	{
		Point p = e.getPoint();
		int x = p.x;
		int y = p.y;
		double[] eq = px_to_eq(x, y);
		double r = eq[0];
		double d = eq[1];
		if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK || e.getButton() == MouseEvent.BUTTON3) {
			if (start != null) {
				EquatorialPoint finish = new EquatorialPoint(r, d, x, y);
				segments.add(new Segment(start, finish));
	 	    	// System.out.printf("%s: %d: finish='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), finish.toString());
			}
		}
		start = null;
		repaint();
	}

	Cursor old_cursor = null;
	@Override public void mouseEntered(MouseEvent e)
	{
		old_cursor = getCursor();
		if (main_frame != null) {
			main_frame.repaint();
			main_frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override public void mouseExited(MouseEvent e)
	{
		if (main_frame != null) {
			main_frame.repaint();
			popupMenu.removeAll();
		    main_frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		setCursor(old_cursor != null ? old_cursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private Point mouse_location = new Point(0,0);

	@Override public void mouseMoved(MouseEvent e)
	{
		mouse_location = e.getPoint();
		Graphics g = getGraphics();
		drawLocationBox(g, mouse_location.x, mouse_location.y);
		/*
		popupMenu.removeAll();
        popupMenu.add(item);
        repaint();
        popupMenu.show(getComponentAt(p), 0, d.height - 50);
        */
	}

	@Override public void mouseDragged(MouseEvent e)  {}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException
	{
		String iau_csn_name            = "IAU-CSN.txt";							// IAU Catalog of Star Names
		String ybsc_name               = "bsc5.dat";							// Yale Bright Star Catalog
		String caldwell_name           = "Caldwell Catalog.csv";				// Caldwell Catalog
		String herschel_name           = "Herschel 400 Catalog.csv";			// Herschel Catalog
		String herschel400_name        = "Herschel 400 Catalog.csv";			// Herschel 400 Catalog
		String herschel2500_name       = "Herschel 2500 Catalog.csv";			// Herschel 2500 Catalog
		String messier_name            = "Messier Catalog.txt";					// Messier Catalog
		String named_dsos_name         = "Named-DSOs.csv";						// Named DSOs
		String sharpless_name          = "sharpless.asu.csv";					// Sharpless Catalog
		String arp_path                = "arpord.dat";							// Arp Catalog
		String ngc_ic_path             = "NGC+IC-J2000.txt";					// NGC+IC-J2000/NGC+IC-J2000.txt
		String ugc_path                = "ugc.dat.txt";							// Uppsala General Catalog
		String vsx_path                = "aavso.vsx.10.tsv";					// Variable Star Index (VSX)
		String wds_path                = "wds.summ_con.txt";					// Washington Double Star Catalog

    	String path = null;
    	String[] alternate_paths = { 
    		"/data/nightsky/catalogs/",
    		"D:/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"E:/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"//magrathea/dsk/dmpase/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    	};
    	for (String p: alternate_paths) {
    		File f = new File(p);
    		if (f.isDirectory()) {
    			path = p;
    			break;
    		}
    	}
    	if (path == null) {
    		path = "/data/nightsky/catalogs/";
    	}
    	System.out.printf("%s: %d: path='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), path);

    	IAUCatalog               iau_csn       = null;
    	YaleBrightStarAscCatalog ybsc          = null;
    	NgcIcCatalog             ngc_ic        = null;
    	CaldwellCatalog          caldwell      = null;
    	HerschelCatalog          herschel      = null;
    	Herschel400              herschel_400  = null;		// Herschel catalog of 400 objects
    	Herschel2500             herschel_2500 = null;		// Herschel catalog of 2500 objects
    	MessierCatalog           messier       = null;
    	NamedDsos                named_dsos    = null;
    	SharplessCatalog         sharpless     = null;
    	UgcCatalog               ugc           = null;
    	ArpCatalog               arp           = null;
    	VsxCatalog               vsx           = null;
    	WdsCatalog               wds           = null;

    	iau_csn       = new IAUCatalog              (path + iau_csn_name);
    	ybsc          = new YaleBrightStarAscCatalog(path + ybsc_name);
    	ngc_ic        = new NgcIcCatalog            (path + ngc_ic_path);
    	caldwell      = new CaldwellCatalog         (path + caldwell_name, ngc_ic);
    	herschel      = new HerschelCatalog         (path + herschel_name, ngc_ic);
    	messier       = new MessierCatalog          (path + messier_name,  ngc_ic);
    	named_dsos    = new NamedDsos               (path + named_dsos_name);
    	sharpless     = new SharplessCatalog        (path + sharpless_name);
    	ugc           = new UgcCatalog              (path + ugc_path);
    	herschel_400  = new Herschel400             (path + herschel400_name, ngc_ic);
    	herschel_2500 = new Herschel2500            (path + herschel2500_name, ngc_ic, ugc, herschel_400);
    	arp           = new ArpCatalog              (path + arp_path,      ngc_ic, ugc, messier);
    	vsx           = new VsxCatalog              (path + vsx_path);
    	wds           = new WdsCatalog              (path + wds_path);

    	System.out.printf("%s: %d: loading DsoAlias%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
    	DsoAlias dso = new DsoAlias(iau_csn, ybsc, ngc_ic, caldwell, herschel_400, herschel_2500, messier, named_dsos, sharpless, ugc, arp, vsx, wds);
    	Element elt = dso.search("Alcor");

    	for (String line: elt.info_text()) {
        	System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), line);
    	}

    	double width_arcmin  = 60;
    	double height_arcmin = 60;

    	ImageFrame imf = new ImageFrame(null, elt, width_arcmin, height_arcmin, 0, 0);
	    imf.addMouseListener(imf);
	    imf.addMouseMotionListener(imf);
	    imf.addWindowListener(imf);
	    imf.addComponentListener(imf);
	    imf.setResizable(false);

	    imf.pack();
	    imf.setSize(imf.width_px(), imf.height_px());
	    imf.setLocationRelativeTo(null);
	    imf.setVisible(true);
    }
}
