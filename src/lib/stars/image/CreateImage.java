package lib.stars.image;

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


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lib.astro.PracticalAstronomy;
import lib.math.matrix.Matrix;

public class CreateImage {
	
	/**
	 * Converts a given Image into a BufferedImage.
	 *
	 * @param img The Image to be converted.
	 * @return The converted BufferedImage.
	 */
	public static BufferedImage create_buffered_image(Image img)
	{
	    if (img instanceof BufferedImage) {
	        return (BufferedImage) img;
	    }

	    // create a buffered image with transparency, BufferedImage.TYPE_INT_ARGB or 
	    BufferedImage buf_img = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // draw the image on to the buffered image
	    Graphics2D g = buf_img.createGraphics();
	    g.drawImage(img, 0, 0, null);
	    g.dispose();

	    // return the buffered image
	    return buf_img;
	}


	public static BufferedImage create_buffered_image(double[][] red, double[][] green, double[][] blue)
	{
		int[] dims = Matrix.dimensions(red);
		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) red  [i][j];
				int g = 0xFF & (int) green[i][j];
				int b = 0xFF & (int) blue [i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(float[][] red, float[][] green, float[][] blue)
	{
		int[] dims = Matrix.dimensions(red);
		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) red  [i][j];
				int g = 0xFF & (int) green[i][j];
				int b = 0xFF & (int) blue [i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(int[][] red, int[][] green, int[][] blue)
	{
		int[] dims = Matrix.dimensions(red);
		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) red  [i][j];
				int g = 0xFF & (int) green[i][j];
				int b = 0xFF & (int) blue [i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(short[][] red, short[][] green, short[][] blue)
	{
		int[] dims = Matrix.dimensions(red);
		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) red  [i][j];
				int g = 0xFF & (int) green[i][j];
				int b = 0xFF & (int) blue [i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(byte[][] red, byte[][] green, byte[][] blue)
	{
		int[] dims = Matrix.dimensions(red);
		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) red  [i][j];
				int g = 0xFF & (int) green[i][j];
				int b = 0xFF & (int) blue [i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(double[][][] pixels)
	{
		int[] dims = Matrix.dimensions(pixels);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) pixels[i][j][0];
				int g = 0xFF & (int) pixels[i][j][1];
				int b = 0xFF & (int) pixels[i][j][2];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(double[][] grey)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(float[][][] pixels)
	{
		int[] dims = Matrix.dimensions(pixels);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) pixels[i][j][0];
				int g = 0xFF & (int) pixels[i][j][1];
				int b = 0xFF & (int) pixels[i][j][2];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(float[][] grey)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(int[][][] pixels)
	{
		int[] dims = Matrix.dimensions(pixels);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) pixels[i][j][0];
				int g = 0xFF & (int) pixels[i][j][1];
				int b = 0xFF & (int) pixels[i][j][2];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(int[][] grey)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(short[][][] pixels)
	{
		int[] dims = Matrix.dimensions(pixels);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) pixels[i][j][0];
				int g = 0xFF & (int) pixels[i][j][1];
				int b = 0xFF & (int) pixels[i][j][2];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(short[][] grey)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(byte[][][] pixels)
	{
		int[] dims = Matrix.dimensions(pixels);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) pixels[i][j][0];
				int g = 0xFF & (int) pixels[i][j][1];
				int b = 0xFF & (int) pixels[i][j][2];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(byte[][] grey)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				buffered_image.setRGB(i, j, 0xFF000000 | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(byte[][] grey, int alpha)
	{
		int[] dims = Matrix.dimensions(grey);

		int width  = dims[0];
		int height = dims[1];

		BufferedImage buffered_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i=0; i < width; i++) {				// width
			for (int j=0; j < height; j++) {		// height
				int r = 0xFF & (int) grey[i][j];
				int g = 0xFF & (int) grey[i][j];
				int b = 0xFF & (int) grey[i][j];
				int a = 0xFF & (int) alpha;
				buffered_image.setRGB(i, j, (a << 24) | (r << 16) | (g << 8) | (b << 0));
			}
		}

		return buffered_image;
	}


	public static BufferedImage create_buffered_image(byte[] buf)
	{
		BufferedImage buffered_image = null;

		if (buf != null) {
	        ImageIcon icon = new ImageIcon(buf);
	        if (icon != null) {
		        Image image = icon.getImage();
		        if (image != null) {
		        	buffered_image = create_buffered_image(image);
		        }
	        }
		}

		return buffered_image;
	}


	public static double[][][] get_image_data(BufferedImage buffered_image)
	{
		// ColorModel cm = buffered_image.getColorModel();
		// int type = cm.getTransferType();

		Raster r = buffered_image.getData();
		double[] px = r.getPixel(0, 0, (double[]) null);
		double[][][] data = new double[buffered_image.getWidth()][buffered_image.getHeight()][px.length];
		for (int i=0; i < data.length; i++) {
			for (int j=0; j < data[i].length; j++) {
				px = r.getPixel(i, j, (double[]) null);
				for (int k=0; k < px.length; k++) {
					data[i][j][k] = px[k];
				}
			}
		}

		return data;
	}
	
	public static byte[][] rgb_to_grey_byte(double[][][] rgb)
	{
		byte[][] grey = new byte[rgb.length][];

		for (int i=0; i < rgb.length; i++) {
			grey[i] = new byte[rgb[i].length];
			for (int j=0; j < rgb[i].length; j++) {
				double sum = 0;
				for (int k=0; k < rgb[i][j].length-1; k++) {			// rgb[i][j].length-1 to skip the alpha value 
					sum += rgb[i][j][k];								// rgb[i][j][0] == red, rgb[i][j][1] == green, rgb[i][j][2] == blue, rgb[i][j][3] == alpha
				}
				grey[i][j] = (byte) (sum / (rgb[i][j].length - 1));		// rgb[i][j].length-1 to skip the ALPHA value
			}
		}

		return grey;
	}

	public static short[][] rgb_to_grey_short(double[][][] rgb)
	{
		short[][] grey = new short[rgb.length][];

		for (int i=0; i < rgb.length; i++) {
			grey[i] = new short[rgb[i].length];
			for (int j=0; j < rgb[i].length; j++) {
				double sum = 0;
				for (int k=0; k < rgb[i][j].length-1; k++) {			// rgb[i][j].length-1 to skip the ALPHA value
					sum += rgb[i][j][k];								// rgb[i][j][0] == red, rgb[i][j][1] == green, rgb[i][j][2] == blue, rgb[i][j][3] == alpha
				}
				grey[i][j] = (short) (sum / (rgb[i][j].length - 1));	// rgb[i][j].length-1 to skip the ALPHA value
			}
		}

		return grey;
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{
		ImageCache cache = new ImageCache("D:\\data\\nightsky\\cache\\");
		double ra_deg = PracticalAstronomy.hours_to_degrees(2);
		double de_deg = 24.5;
		ImageList elt = cache.lookup_cache(ra_deg, de_deg);
		System.out.printf("%s%n", PracticalAstronomy.decimal_degrees_to_str_dms3(-1.0/(4000.0)));

		double ra = ra_deg-7.5/60.0;
		double de = de_deg-7.5/60.0;
		double[] px = elt.sphere_to_plane(ra, de);
		System.out.printf("(%f,%f)eq => (%f,%f)im %n", ra, de, px[0], px[1]);
		double rx = px[0] + 0 * (int) (elt.ctr_ra_px - elt.width_px  / 2);
		double dx = px[1] + 0 * (int) (elt.ctr_de_px - elt.height_px / 2);
		double[] eq = elt.plane_to_sphere(rx, dx);
		System.out.printf("(%f,%f)im => (%f,%f)eq%n", rx, dx, eq[0], eq[1]);

		BufferedImage buffered_image0 = create_buffered_image(elt.image);
		elt.image = buffered_image0.getScaledInstance(1000, 1000, BufferedImage.SCALE_DEFAULT);
		buffered_image0 = create_buffered_image(elt.image);

		ImageIcon icon0 = new ImageIcon(buffered_image0);
		JLabel label0 = new JLabel(icon0);
		JFrame f = new JFrame("From Disk");
	    f.getContentPane().add(label0);
	    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    f.pack();
	    f.setLocationRelativeTo(null);
	    f.setVisible(true);

		double[][][] data = get_image_data(buffered_image0);
		BufferedImage buffered_image1 = create_buffered_image(data);

		ImageIcon icon1 = new ImageIcon(buffered_image1);
		JLabel label1 = new JLabel(icon1);
		JFrame g = new JFrame("From Image");
	    g.getContentPane().add(label1);
	    g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    g.pack();
	    g.setLocationRelativeTo(null);
	    g.setVisible(true);
	}
}
