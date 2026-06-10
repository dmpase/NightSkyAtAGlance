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

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import nightskyataglance.NightSkyAtAGlance;
import nightskyataglance.util.PersistentState;

public class FileMenu extends Menu {
	private static final long serialVersionUID = 3897223680174643759L;

	public final MainFrame main_frame;

    public static final String FIELD_OF_VIEW       = "Field of View Calculator";
    public static final String FILTER              = "Filter DSOs";
    public static final String ANGLE_OF_SEPARATION = "Angle of Separation";
    public static final String PLANETARY_ALIGNMENT = "Planetary Alignment";
    public static final String IMAGE_CACHE         = "Image Cache";
    public static final String EXIT                = "Exit";

    public static final MenuItem fv = new MenuItem(FIELD_OF_VIEW,       new MenuShortcut(KeyEvent.VK_F));
    public static final MenuItem ff = new MenuItem(FILTER,              new MenuShortcut(KeyEvent.VK_R));
    public static final MenuItem fs = new MenuItem(ANGLE_OF_SEPARATION, new MenuShortcut(KeyEvent.VK_S));
    public static final MenuItem fa = new MenuItem(PLANETARY_ALIGNMENT, new MenuShortcut(KeyEvent.VK_A));
    public static final MenuItem fc = new MenuItem(IMAGE_CACHE,         new MenuShortcut(KeyEvent.VK_C));
    public static final MenuItem fx = new MenuItem(EXIT,                new MenuShortcut(KeyEvent.VK_Q));

	public FileMenu(MainFrame f, String name)
	{
		super(name);
		main_frame = f;

		setFont(PersistentState.get_font());

		fv.setEnabled(main_frame.dso_alias_table != null);
		fs.setEnabled(main_frame.dso_alias_table != null);
		fa.setEnabled(main_frame.dso_alias_table != null);
		fc.setEnabled(true);

    	add(fv);
    	add(ff);
    	add(fs);
    	add(fa);
    	add(fc);
    	addSeparator();
    	add(fx);

    	addActionListener(new Listener());
    }

	public class Listener implements ActionListener {
		public Listener()
		{
		}
		
		public void actionPerformed(ActionEvent evt)
		{
			String what = evt.getActionCommand();
			Cursor old = main_frame.getCursor();
			main_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (FIELD_OF_VIEW.equals(what)) {
				new FieldOfViewDialog(main_frame, FIELD_OF_VIEW);
			} else if (FILTER.equals(what)) {
		    	new FilterDialog(main_frame, FILTER);
			} else if (ANGLE_OF_SEPARATION.equals(what)) {
		    	new SeparationDialog(main_frame, ANGLE_OF_SEPARATION);
			} else if (PLANETARY_ALIGNMENT.equals(what)) {
		    	new AlignmentDialog(main_frame, PLANETARY_ALIGNMENT);
			} else if (IMAGE_CACHE.equals(what)) {
		    	new ImageCacheDialog(main_frame, IMAGE_CACHE);
			} else if (EXIT.equals(what)) {
				main_frame.exit();
			} else {
		    	System.out.printf("%s: %3d: else...\n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
			}
			main_frame.setCursor(old);
		}
	}
}
