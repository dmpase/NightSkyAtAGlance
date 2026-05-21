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
 * infringement of that parties intellectual property rights.                  *
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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import lib.astro.PracticalAstronomy;

class ImageMouseListener implements MouseMotionListener, MouseListener {

    public final JFrame frame;
    public final double ra_dhrs;
    public final double de_ddeg;
    public final double width_arcmin;
    public final double height_arcmin;
	public final int width_px;
	public final int height_px;
	public final MainFrame mainframe;

	public final double left =  7;
	public final double top  = 30;
	JPopupMenu popupMenu = new JPopupMenu();

	public ImageMouseListener(JFrame f, double ra, double de, double wamin, double hamin, int w, int h, MainFrame mf) 
	{
		frame         = f;
		ra_dhrs       = ra;
		de_ddeg       = de;
		width_arcmin  = wamin;
		height_arcmin = hamin;
		width_px      = w;
		height_px     = h;
		mainframe     = mf;
	}

	private String xy_to_eq(int x, int y)
	{
		double r = px_to_ra(x);
		double d = py_to_de(y);
		return String.format("%s,%s", PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
	}

	private double px_to_ra(int px)
	{
		double width_deg  = width_arcmin / 60.0 / 15.0;
		double r = ((double)(width_px - px + left) / (double) width_px) * width_deg  + ra_dhrs - width_deg / 2;
		
		return r;
	}

	private double py_to_de(int py)
	{
		double height_deg = height_arcmin / 60.0;
		double d = ((double) (height_px - py + top) / (double)height_px) * height_deg + de_ddeg - height_deg / 2;
		
		return d;
	}

	private final void drawTarget(Graphics g, int x, int y, int len_px, Color color)
	{
		g.setColor(color);
		g.drawLine(x - 2*len_px, y,            x - len_px, y);
		g.drawLine(x + 2*len_px, y,            x + len_px, y);
		g.drawLine(x,            y - 2*len_px, x,          y - len_px);
		g.drawLine(x,            y + 2*len_px, x,          y + len_px);
	}


	@Override public void mouseClicked(MouseEvent e)
	{
		Point p = e.getPoint();
		int x = p.x;
		int y = p.y;
		double r = px_to_ra(x);
		double d = py_to_de(y);

		// System.out.printf("%s: %d: cc=%d ra=%s de=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), e.getClickCount(), PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
 		if (1 == e.getClickCount()) {
 			// Single click -- draws the RA and DE on the image
			String s = String.format("%s, %s", PracticalAstronomy.decimal_hours_to_str_hms(r), PracticalAstronomy.decimal_degrees_to_str_dms(d));
    		Graphics g = frame.getGraphics();
    		int w = g.getFontMetrics().getStringBounds(s, g).getBounds().width;
    		int h = g.getFontMetrics().getHeight();
    		g.setColor(Color.WHITE);
    		g.fillRect(x + 10, y + 10, w + 10, h + 4);
    		g.setColor(Color.BLACK);
    		g.drawString(s, x + 10 + 5, y + 10 + h - 2);
		} else if (2 == e.getClickCount()) {
			// Double click -- selects the location in the main program
			if (mainframe != null && mainframe.dso_alias_table != null) {
				mainframe.selected_dso = mainframe.dso_alias_table.find("Custom");
				mainframe.selected_dso.update(r, d);
	    		Graphics g = frame.getGraphics();
				drawTarget(g, x, y, 5, Color.RED);
			}					
		}
	}

	// CTRL-click and drag measures the distance between points
	int oval_width = 5;
	private Point start  = null;
	private Point finish = null;
	@Override public void mousePressed(MouseEvent e)  
	{
		// mouse button is pressed, start the measurement
		if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK || e.getButton() == MouseEvent.BUTTON3) {
			start = e.getPoint();
    		Graphics g = frame.getGraphics();
    		g.setColor(Color.RED);
    		g.drawOval(start.x-(oval_width/2), start.y-(oval_width/2), oval_width, oval_width);
		}
	}

	@Override public void mouseReleased(MouseEvent e)
	{
		// mouse button is released, finish the measurement
		if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK || e.getButton() == MouseEvent.BUTTON3) {
			finish = e.getPoint();
    		Graphics g = frame.getGraphics();
    		g.setColor(Color.RED);
    		g.drawOval(finish.x-(oval_width/2), finish.y-(oval_width/2), oval_width, oval_width);

    		g.drawLine(start.x, start.y, finish.x, finish.y);

    		double ras = px_to_ra(start.x);
    		double des = py_to_de(start.y);
    		double raf = px_to_ra(finish.x);
    		double def = py_to_de(finish.y);

    		double d = PracticalAstronomy.angle_between_celestial_objects(ras, des, raf, def);
    		String s = PracticalAstronomy.decimal_degrees_to_str_dms(d);
    		int w = g.getFontMetrics().getStringBounds(s, g).getBounds().width;
    		int h = g.getFontMetrics().getHeight();
    		g.setColor(Color.WHITE);
    		g.fillRect((start.x + finish.x) / 2 + 5, (start.y + finish.y) / 2, w + 10, h+4);
    		g.setColor(Color.BLACK);
    		g.drawString(s, (start.x + finish.x) / 2 + 10, (start.y + finish.y) / 2 + h - 2);
		}
	}

	@Override public void mouseEntered(MouseEvent e)
	{
		frame.repaint();
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override public void mouseExited(MouseEvent e) 
	{
		frame.repaint();
		popupMenu.removeAll();
	    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override public void mouseDragged(MouseEvent e)  {}

	@Override public void mouseMoved(MouseEvent e)
	{
		frame.repaint();
		Point p = e.getPoint();
        JMenuItem item = new JMenuItem(xy_to_eq(p.x, p.y));
		popupMenu.removeAll();
        popupMenu.add(item);
        popupMenu.show(frame.getComponentAt(p), p.x, p.y);
	}
}
