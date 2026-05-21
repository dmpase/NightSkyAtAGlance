package lib.stars.catalog;

/*******************************************************************************
 * Copyright (c) 2026 Douglas M. Pase                                          *
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
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import lib.astro.PracticalAstronomy;
import lib.util.Queue;
import nightskyataglance.NightSkyAtAGlance;


public class DsoAlias {
	private Hashtable<String,Element> table    = new Hashtable<String,Element>();		// mapping of whole names to elements
	private Hashtable<String,Element> partials = new Hashtable<String,Element>();		// mapping of whole and partial names to elements
	private Element[]                 dso_list = null;
	
	public Element get   (String name) { return table.get(key(name)); }
	public Element search(String name) { return partials.get(key(name)); }

	private Hashtable<IAUEntry,              Element> iau_tbl = new Hashtable<IAUEntry,              Element>();
	private Hashtable<YaleBrightStarAscEntry,Element> ybs_tbl = new Hashtable<YaleBrightStarAscEntry,Element>();
	private Hashtable<NgcIcEntry,            Element> ngc_tbl = new Hashtable<NgcIcEntry,            Element>();
	private Hashtable<CaldwellEntry,         Element> cal_tbl = new Hashtable<CaldwellEntry,         Element>();
	private Hashtable<Herschel400.Element,   Element> h04_tbl = new Hashtable<Herschel400.Element,   Element>();
	private Hashtable<Herschel2500.Element,  Element> h25_tbl = new Hashtable<Herschel2500.Element,  Element>();
	private Hashtable<MessierEntry,          Element> mes_tbl = new Hashtable<MessierEntry,          Element>();
	private Hashtable<SharplessEntry,        Element> sha_tbl = new Hashtable<SharplessEntry,        Element>();
	private Hashtable<UgcEntry,              Element> ugc_tbl = new Hashtable<UgcEntry,              Element>();
	private Hashtable<ArpEntry,              Element> arp_tbl = new Hashtable<ArpEntry,              Element>();
	private Hashtable<VsxEntry,              Element> vsx_tbl = new Hashtable<VsxEntry,              Element>();
	private Hashtable<WdsEntry,              Element> wds_tbl = new Hashtable<WdsEntry,              Element>();

	public Element get(IAUEntry               ent) { return iau_tbl.get(ent); }
	public Element get(YaleBrightStarAscEntry ent) { return ybs_tbl.get(ent); }
	public Element get(NgcIcEntry             ent) { return ngc_tbl.get(ent); }
	public Element get(CaldwellEntry          ent) { return cal_tbl.get(ent); }
	public Element get(Herschel400.Element    ent) { return h04_tbl.get(ent); }
	public Element get(Herschel2500.Element   ent) { return h25_tbl.get(ent); }
	public Element get(MessierEntry           ent) { return mes_tbl.get(ent); }
	public Element get(SharplessEntry         ent) { return sha_tbl.get(ent); }
	public Element get(UgcEntry               ent) { return ugc_tbl.get(ent); }
	public Element get(ArpEntry               ent) { return arp_tbl.get(ent); }
	public Element get(VsxEntry               ent) { return vsx_tbl.get(ent); }
	public Element get(WdsEntry               ent) { return wds_tbl.get(ent); }

	private Hashtable<Element,String> common    = new Hashtable<Element,String>();
	private Hashtable<Element,String> canon     = new Hashtable<Element,String>();
	private Hashtable<Element,String> bayer     = new Hashtable<Element,String>();
	private Hashtable<Element,String> flamsteed = new Hashtable<Element,String>();
	private Hashtable<Element,String> hr_list   = new Hashtable<Element,String>();
	private Hashtable<Element,String> hd_list   = new Hashtable<Element,String>();
	private Hashtable<Element,String> hip_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> ngc_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> cal_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> h04_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> h25_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> mes_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> sha_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> ugc_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> arp_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> vsx_list  = new Hashtable<Element,String>();
	private Hashtable<Element,String> wds_list  = new Hashtable<Element,String>();

	public String common   (Element elt) { return common == null || elt == null ? null : common.get(elt); }
	public String canon    (Element elt) { return canon.get(elt); }
	public String bayer    (Element elt) { return bayer.get(elt); }
	public String flamsteed(Element elt) { return flamsteed.get(elt); }
	public String hr       (Element elt) { return hr_list.get(elt); }
	public String hd       (Element elt) { return hd_list.get(elt); }
	public String hip      (Element elt) { return hip_list.get(elt); }
	public String ngc      (Element elt) { return ngc_list.get(elt); }
	public String ic       (Element elt) { return ngc_list.get(elt); }
	public String cal      (Element elt) { return cal_list.get(elt); }
	public String h04      (Element elt) { return h04_list.get(elt); }
	public String h25      (Element elt) { return h25_list.get(elt); }
	public String mes      (Element elt) { return mes_list.get(elt); }
	public String sha      (Element elt) { return sha_list.get(elt); }
	public String ugc      (Element elt) { return ugc_list.get(elt); }
	public String arp      (Element elt) { return arp_list.get(elt); }
	public String vsx      (Element elt) { return vsx_list.get(elt); }
	public String wds      (Element elt) { return wds_list.get(elt); }

	private Queue<Element> tmp_que = new Queue<Element>();

	private Queue<String> com_que = new Queue<String>();
	private Queue<String> can_que = new Queue<String>();
	private Queue<String> bay_que = new Queue<String>();
	private Queue<String> fla_que = new Queue<String>();
	private Queue<String> hr_que  = new Queue<String>();
	private Queue<String> hd_que  = new Queue<String>();
	private Queue<String> hip_que = new Queue<String>();
	private Queue<String> ngc_que = new Queue<String>();
	private Queue<String> cal_que = new Queue<String>();
	private Queue<String> h04_que = new Queue<String>();
	private Queue<String> h25_que = new Queue<String>();
	private Queue<String> mes_que = new Queue<String>();
	private Queue<String> sha_que = new Queue<String>();
	private Queue<String> ugc_que = new Queue<String>();
	private Queue<String> arp_que = new Queue<String>();
	private Queue<String> vsx_que = new Queue<String>();
	private Queue<String> wds_que = new Queue<String>();
	
	private Element[] dso_addendum = {
		// https://www.nbcnews.com/id/wbna26566342
		// https://gardenastronomer.com/2025/11/24/navi-the-star-behind-the-ghost-of-cassiopeia/
		new Element(new String[] {"Navi", "Tiansi", "gam Cas", "27 Cas"},  
			PracticalAstronomy.hms_to_decimal_hours(0, 56, 42.50108),  
			PracticalAstronomy.dms_to_decimal_degrees(60, 43, 0.2984),
			2.39, 2.39, Element.NO_SIZE, DsoType.star[0]),

		new Element(new String[] {"Regor", "gam Vel"},  
			PracticalAstronomy.hms_to_decimal_hours(8, 9, 31.95013),  
			PracticalAstronomy.dms_to_decimal_degrees(-47, 20, 11.7108),
			1.83, 1.83, Element.NO_SIZE, DsoType.star[0]),

		new Element(new String[] {"Dnoces", "Talitha", "iot UMa"},  
			PracticalAstronomy.hms_to_decimal_hours(8, 59, 12.45362),  
			PracticalAstronomy.dms_to_decimal_degrees(48, 2, 30.5741),
			1.83, 1.83, Element.NO_SIZE, DsoType.star[0]),

		new Element(new String[] {"Sgr A*", "Sagittarius A*"},  
			PracticalAstronomy.hms_to_decimal_hours(17, 45, 40.0409),  
			PracticalAstronomy.dms_to_decimal_degrees(-29, 0, 28.118),
			Element.NO_MAG, Element.NO_MAG, Element.NO_SIZE, DsoType.star[0]),

		new Element(new String[] {"NGC 891", "Outer Limits Galaxy"},  
			PracticalAstronomy.hms_to_decimal_hours(2, 22, 33.4),  
			PracticalAstronomy.dms_to_decimal_degrees(+42, 20, 57),
			10.8, 10.8, 13.5, DsoType.galaxy[0]),

		new Element(new String[] {"LMC", "Large Magellanic Cloud"},  
			PracticalAstronomy.hms_to_decimal_hours(05, 23, 34),  
			PracticalAstronomy.dms_to_decimal_degrees(-69, 45.4),
			0.13, 0.13, 10*60, DsoType.galaxy[0]),

		new Element(new String[] {"SMC", "Small Magellanic Cloud"},  
			PracticalAstronomy.hms_to_decimal_hours(00, 52, 44.8),  
			PracticalAstronomy.dms_to_decimal_degrees(-72, 49, 43),
			2.7, 2.7, 4.2*60, DsoType.galaxy[0]),

		new Element(new String[] {"Horsehead Nebula", "Barnard 33", "B33"},  
			PracticalAstronomy.hms_to_decimal_hours(05, 40, 59.0),  
			PracticalAstronomy.dms_to_decimal_degrees(-02, 27, 30.0),
			6.8, 6.8, 8, DsoType.nebula[0]),

		new Element(new String[] {"Sun"    },  0   , -0.50, -26.74, -26.74, 60*0.536, DsoType.star[0]),
		new Element(new String[] {"Moon"   },  0   , +2.50,  -2.50, -12.90, 60*0.536, DsoType.moon[0]),
		new Element(new String[] {"Mercury"}, 23.25, -5.00,  -2.48,  +7.25,  13.0/60, DsoType.rocky_planet[0]),
		new Element(new String[] {"Venus"  },  1.00, +5.50,  -4.92,  -2.98,  66.0/60, DsoType.rocky_planet[0]),
		new Element(new String[] {"Mars"   }, 23.00, -7.25,  -2.94,   1.86,  15.0/60, DsoType.rocky_planet[0]),
		new Element(new String[] {"Jupiter"},  6.50, 23.00,  -2.94,  -1.66,  40.0/60, DsoType.gas_giant[0]),
		new Element(new String[] {"Saturn" },  0.50, +3.50,  -0.55,  +1.17,  17.0/60, DsoType.gas_giant[0]),
		new Element(new String[] {"Uranus" },  4,    21.00,   5.38,   6.03,   3.7/60, DsoType.gas_giant[0]),
		new Element(new String[] {"Neptune"},  0.00,  0.50,   7.67,   8.00,   2.3/60, DsoType.gas_giant[0]),

		new Element(new String[] {"Ceres"  },  0.00,  0.00,   7.60,   9.27,   0.6/60, DsoType.minor_planet[0]),

		new Element(new String[] {"Custom"}),
	};
	
	private String[][] common_names_addendum = {
		{ "Tiansi",                "Tiansi"},
		{ "Regor",                 "Regor"},
		{ "Talitha",               "Talitha"},
		{ "Sagittarius A*",        "Sagittarius A*"},
		{ "NGC 891",               "Silver Sliver Galaxy"},
		{ "Sh2-184",               "PacMan Nebula"},

		{ "Sun",                   "Sun"},
		{ "Moon",                  "Moon"},
		{ "Mercury",               "Mercury"},
		{ "Venus",                 "Venus"},
		{ "Mars",                  "Mars"},
		{ "Jupiter",               "Jupiter"},
		{ "Saturn",                "Saturn"},
		{ "Uranus",                "Uranus"},
		{ "Neptune",               "Neptune"},

		{ "Ceres",                 "Ceres"},

		{ "Large Magellanic Cloud", "Large Magellanic Cloud"},
		{ "Small Magellanic Cloud", "Small Magellanic Cloud"},
		{ "Horsehead Nebula",       "Horsehead Nebula"},
	};

	public DsoAlias(	// TODO
			IAUCatalog               iau_csn, 
			YaleBrightStarAscCatalog ybsc, 
			NgcIcCatalog             ngc_ic, 
			CaldwellCatalog          caldwell,
	    	Herschel400              herschel_400,
	    	Herschel2500             herschel_2500,
	    	MessierCatalog           messier,
	    	NamedDsos                named_dsos,
	    	SharplessCatalog         sharpless,
	    	UgcCatalog               ugc,
	    	ArpCatalog               arp,
	    	VsxCatalog               vsx,
			WdsCatalog               wds)
	{
		add(iau_csn);
    	add(ybsc);
    	add(ngc_ic);
    	add(ugc);
    	add(sharpless);
    	add(arp);
    	add(vsx);
    	add(wds);

    	add(caldwell);
    	add(herschel_400);
    	add(herschel_2500);
    	add(messier);
    	
    	// everything is now in the tmp_que, but some objects may be duplicates

    	Hashtable<Element,Element> dso_table = new Hashtable<Element,Element>();
    	Enumeration<Element> elts = table.elements();
    	while (elts.hasMoreElements()) {
    		Element elt = elts.nextElement();
    		// sort_dsos(elt.names);
    		dso_table.put(elt, elt);
    	}

		table.remove("-");

		// add DSOs in the addendum
		for (Element elt: dso_addendum) {
			tmp_que.append(elt);
		}
		
		// merge duplicates
		// build up the <String,Element> table
		while (0 < tmp_que.size()) {
			Element merged = tmp_que.remove();
			// find duplicates and merge them
			Element base = null;
			do {
				base = merged;
				for (String name: base.names) {
					merged = Element.merge(merged, table.get(key(name)));
				}
			} while(base.names.length < merged.names.length);

			sort_dso_names(merged);

			// replace all names in the table with the merged element
			for (String name: merged.names) {
				table.put(key(name), merged);
			}
		}

		// add in the common names in the addendum
		for (String[] str: common_names_addendum) {
			common.put(table.get(key(str[0])), str[1]);
		}

		// TODO adding extra aliases
		if (named_dsos != null && named_dsos.elts != null) {
			for (NamedDsos.Element elt: named_dsos.elts) {
				common.put(table.get(key(elt.ngc)), elt.name);
			}
		}

		regenerate_dso_list();

    	// map elements to common names
    	while (0 < com_que.size()) {
    		String name = com_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
	        	// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		common.put(elt, name);
    		}
    	}

    	// map elements to canonical names
    	while (0 < can_que.size()) {
    		String name = can_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		canon.put(elt, name);
    		}
    	}

    	// map elements to bayer names
    	while (0 < bay_que.size()) {
    		String name = bay_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		bayer.put(elt, name);
    		}
    	}

    	// map elements to flamsteed names
    	while (0 < fla_que.size()) {
    		String name = fla_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		flamsteed.put(elt, name);
    		}
    	}

    	// map elements to HR names
    	while (0 < hr_que.size()) {
    		String name = hr_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		hr_list.put(elt, name);
    		}
    	}

    	// map elements to HD names
    	while (0 < hd_que.size()) {
    		String name = hd_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		hd_list.put(elt, name);
    		}
    	}

    	// map elements to NGC/IC names
    	while (0 < ngc_que.size()) {
    		String name = ngc_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		ngc_list.put(elt, name);
    		}
    	}

    	// map elements to Caldwell names
    	while (0 < cal_que.size()) {
    		String name = cal_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		cal_list.put(elt, name);
    		}
    	}

    	// map elements to Herschel names
    	while (0 < h04_que.size()) {
    		String name = h04_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		h04_list.put(elt, name);
    		}
    	}

    	// map elements to Herschel names
    	while (0 < h25_que.size()) {
    		String name = h25_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		h25_list.put(elt, name);
    		}
    	}

    	// map elements to Messier names
    	while (0 < mes_que.size()) {
    		String name = mes_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		mes_list.put(elt, name);
    		}
    	}

    	// map elements to UGC names
    	while (0 < ugc_que.size()) {
    		String name = ugc_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		ugc_list.put(elt, name);
    		}
    	}

    	// map elements to Sharpless names
    	while (0 < sha_que.size()) {
    		String name = sha_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		sha_list.put(elt, name);
    		}
    	}

    	// map elements to Arp names
    	while (0 < arp_que.size()) {
    		String name = arp_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
	        	// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		arp_list.put(elt, name);
    		}
    	}

    	// map elements to VSX names
    	while (0 < vsx_que.size()) {
    		String name = vsx_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		vsx_list.put(elt, name);
    		}
    	}

    	// map elements to WDS names
    	while (0 < wds_que.size()) {
    		String name = wds_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		wds_list.put(elt, name);
    		}
    	}

    	// map elements to hipparcos names
    	while (0 < hip_que.size()) {
    		String name = hip_que.remove();
    		String key  = key(name);
    		Element elt = table.get(key);
    		if (name != null && ! name.equals("-")) {
    			// System.out.printf("%s: %d: name='%s' key='%s' elt='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), name, key, elt);
	    		hip_list.put(elt, name);
    		}
    	}

    	// sort the aliases for each DSO in the list
    	for (Element elt: dso_list) {
    		sort_dso_names(elt);
    		
    		if (elt.iau != null) iau_tbl.put(elt.iau, elt);
    		if (elt.ybs != null) ybs_tbl.put(elt.ybs, elt);
    		if (elt.ngc != null) ngc_tbl.put(elt.ngc, elt);
    		if (elt.cal != null) cal_tbl.put(elt.cal, elt);
    		if (elt.h04 != null) h04_tbl.put(elt.h04, elt);
    		if (elt.h25 != null) h25_tbl.put(elt.h25, elt);
    		if (elt.mes != null) mes_tbl.put(elt.mes, elt);
    		if (elt.sha != null) sha_tbl.put(elt.sha, elt);
    		if (elt.arp != null) arp_tbl.put(elt.arp, elt);
    		if (elt.ugc != null) ugc_tbl.put(elt.ugc, elt);
    		if (elt.vsx != null) vsx_tbl.put(elt.vsx, elt);
    		if (elt.wds != null) wds_tbl.put(elt.wds, elt);

        	// generate partials
    		for (String name: elt.names) {
    			String key = key(name);
    			for (int i=0; i < name.length(); i++) {
    				for (int j=i+1; j <= name.length(); j++) {
    					String k = key(name.substring(i,j));
    					if (partials.get(k) == null) {
    						partials.put(k, elt);
    					}
    				}
    			}
    			partials.put(key, elt);
    		}
    	}
	}
	
	public void regenerate_dso_list()
	{
    	Enumeration<Element> elts = table.elements();
    	dso_list = new Element[table.size()];
    	for (int i=0; i < dso_list.length; i++) {
    		dso_list[i] = elts.nextElement();
    	}
    	// Arrays.sort(dso_list);
	}

	public static class Element implements Comparable<Element> {
		public static final double NO_RA   = Double.POSITIVE_INFINITY;
		public static final double NO_DE   = Double.POSITIVE_INFINITY;
		public static final double NO_MAG  = Double.POSITIVE_INFINITY;
		public static final double NO_SIZE = 0;

		public final IAUEntry               iau;
		public final YaleBrightStarAscEntry ybs;
		public final NgcIcEntry             ngc;
		public final CaldwellEntry          cal;
		public final Herschel400.Element    h04;
		public final Herschel2500.Element   h25;
		public final MessierEntry           mes;
		public final SharplessEntry         sha;
		public final UgcEntry               ugc;
		public final ArpEntry               arp;
		public final VsxEntry               vsx;
		public final WdsEntry               wds;

		private double ra_hrs;
		private double de_deg;
		private double min_mag;
		private double max_mag;
		private double size_min;
		private String type;

		private String[] names;

		public double   ra_hrs()    { return ra_hrs; }
		public double   de_deg()    { return de_deg; }
		public double   min_mag()   { return min_mag; }
		public double   max_mag()   { return max_mag; }
		public double   size_min()  { return size_min; }
		public String   type()      { return type; }
		public String[] names()     { return names; }
		public boolean  is_custom() { return names[0].equalsIgnoreCase("Custom"); }

		public void set_ra_hrs(double hrs) { ra_hrs = hrs; }
		public void set_de_deg(double deg) { de_deg = deg; }

		public Element(
				String[] names,
				double ra_hrs,
				double de_deg,
				double min_mag,
				double max_mag,
				double size_min,
				String type, 
				IAUEntry iau,
				YaleBrightStarAscEntry ybs,
				NgcIcEntry ngc,
				CaldwellEntry cal,
				Herschel400.Element  h04,
				Herschel2500.Element h25,
				MessierEntry mes,
				SharplessEntry sha,
				UgcEntry ugc,
				ArpEntry arp,
				VsxEntry vsx,
				WdsEntry wds)
		{
			this.names    = names;
			this.ra_hrs   = ra_hrs;
			this.de_deg   = de_deg;
			this.min_mag  = min_mag;
			this.max_mag  = max_mag;
			this.size_min = size_min;
			this.type     = type;
			this.iau      = iau;
			this.ybs      = ybs;
			this.ngc      = ngc;
			this.cal      = cal;
			this.h04      = h04;
			this.h25      = h25;
			this.mes      = mes;
			this.sha      = sha;
			this.ugc      = ugc;
			this.arp      = arp;
			this.vsx      = vsx;
			this.wds      = wds;
		}
		
		public Element(Element elt)
		{
			if (elt != null) {
				names    = elt.names;
				ra_hrs   = elt.ra_hrs;
				de_deg   = elt.de_deg;
				min_mag  = elt.min_mag;
				max_mag  = elt.max_mag;
				size_min = elt.size_min;
				type     = elt.type;
				iau      = elt.iau;
				ybs      = elt.ybs;
				ngc      = elt.ngc;
				cal      = elt.cal;
				h04      = elt.h04;
				h25      = elt.h25;
				mes      = elt.mes;
				sha      = elt.sha;
				ugc      = elt.ugc;
				arp      = elt.arp;
				vsx      = elt.vsx;
				wds      = elt.wds;
			} else {
				iau = null;
				ybs = null;
				ngc = null;
				cal = null;
				h04 = null;
				h25 = null;
				mes = null;
				sha = null;
				ugc = null;
				arp = null;
				vsx = null;
				wds = null;

				ra_hrs   = NO_RA;
				de_deg   = NO_DE;
				max_mag  = NO_MAG;
				min_mag  = NO_MAG;
				size_min = NO_SIZE;
				type     = null;

				names = null;
			}
		}

		public Element(
				String[] names,
				double ra_hrs,
				double de_deg,
				double min_mag,
				double max_mag,
				double size_min,
				String type)
		{
			this.names    = names;
			this.ra_hrs   = ra_hrs;
			this.de_deg   = de_deg;
			this.min_mag  = min_mag;
			this.max_mag  = max_mag;
			this.size_min = size_min;
			this.type     = type;
			this.iau      = null;
			this.ybs      = null;
			this.ngc      = null;
			this.cal      = null;
			this.h04      = null;
			this.h25      = null;
			this.mes      = null;
			this.sha      = null;
			this.ugc      = null;
			this.arp      = null;
			this.vsx      = null;
			this.wds      = null;
		}

		public Element(String[] list)
		{
			iau = null;
			ybs = null;
			ngc = null;
			cal = null;
			h04 = null;
			h25 = null;
			mes = null;
			sha = null;
			ugc = null;
			arp = null;
			vsx = null;
			wds = null;

			ra_hrs   = NO_RA;
			de_deg   = NO_DE;
			max_mag  = NO_MAG;
			min_mag  = NO_MAG;
			size_min = NO_SIZE;
			type     = null;

			names = list;
		}

		public Element(IAUEntry iau, String[] names)
		{
			this.iau = iau;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = iau.ra_hrs;
			this.de_deg   = iau.dec_deg;
			this.min_mag  = iau.mag;
			this.max_mag  = iau.mag;
			this.size_min = 0;
			this.type     = DsoType.star[0];

			this.names = names;
		}

		public Element(YaleBrightStarAscEntry ybs, String[] names)
		{
			this.iau = null;
			this.ybs = ybs;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = ybs.RA;
			this.de_deg   = ybs.DE;
			this.min_mag  = ybs.Vmag;
			this.max_mag  = ybs.Vmag;
			this.size_min = 0;
			this.type     = DsoType.star[0];

			this.names = names;
		}

		public Element(NgcIcEntry ngc, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = ngc;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = ngc.ra_dhrs;
			this.de_deg   = ngc.dec_ddeg;
			this.min_mag  = ngc.app_mag;
			this.max_mag  = ngc.app_mag;
			this.size_min = ngc.ang_diam;
			this.type     = ngc.dso_class;

			this.names = names;
		}

		public Element(CaldwellEntry cal, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = cal;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = cal.ra_dhrs;
			this.de_deg   = cal.dec_ddeg;
			this.min_mag  = cal.amag;
			this.max_mag  = cal.amag;
			this.size_min = cal.diameter;
			this.type     = cal.dso_type;

			this.names = names;
		}

		public Element(Herschel400.Element h04, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = h04;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = h04.ra_hrs;
			this.de_deg   = h04.de_deg;
			this.min_mag  = h04.amag;
			this.max_mag  = h04.amag;
			this.size_min = h04.diameter;
			this.type     = h04.type;

			this.names = names;
		}

		public Element(Herschel2500.Element h25, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = h25;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = h25.ra_hrs;
			this.de_deg   = h25.de_deg;
			this.min_mag  = h25.amag;
			this.max_mag  = h25.amag;
			this.size_min = h25.diameter;
			this.type     = h25.type;

			this.names = names;
		}

		public Element(MessierEntry mes, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = mes;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = mes.ra_dhrs;
			this.de_deg   = mes.dec_ddeg;
			this.min_mag  = mes.amag;
			this.max_mag  = mes.amag;
			this.size_min = mes.diameter;
			this.type     = mes.dso_type;

			this.names = names;
		}

		public Element(SharplessEntry sha, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = sha;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = sha.ra_dhrs;
			this.de_deg   = sha.de_ddeg;
			this.min_mag  = sha.brightness;
			this.max_mag  = sha.brightness;
			this.size_min = sha.diameter;
			this.type     = DsoType.nebula[0];

			this.names = names;
		}

		public Element(UgcEntry ugc, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = ugc;
			this.arp = null;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = ugc.ra_dhrs;
			this.de_deg   = ugc.de_ddeg;
			this.min_mag  = ugc.photo_mag;
			this.max_mag  = ugc.photo_mag;
			this.size_min = ugc.maj_axis_red;
			this.type     = DsoType.galaxy[0];

			this.names = names;
		}

		public Element(ArpEntry arp, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = arp;
			this.vsx = null;
			this.wds = null;

			this.ra_hrs   = arp.ra_dhrs;
			this.de_deg   = arp.de_ddeg;
			this.min_mag  = arp.amag;
			this.max_mag  = arp.amag;
			this.size_min = arp.size;
			this.type     = DsoType.irregular_galaxy[0];

			this.names = names;
		}

		public Element(VsxEntry vsx, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = vsx;
			this.wds = null;

			this.ra_hrs   = vsx.ra_dhrs;
			this.de_deg   = vsx.de_ddeg;
			this.min_mag  = vsx.min_mag;
			this.max_mag  = vsx.max_mag;
			this.size_min = 0;
			this.type     = DsoType.variable_star[0];

			this.names = names;
		}

		public Element(WdsEntry wds, String[] names)
		{
			this.iau = null;
			this.ybs = null;
			this.ngc = null;
			this.cal = null;
			this.h04 = null;
			this.h25 = null;
			this.mes = null;
			this.sha = null;
			this.ugc = null;
			this.arp = null;
			this.vsx = null;
			this.wds = wds;

			this.ra_hrs   = wds.ra_dhrs;
			this.de_deg   = wds.de_ddeg;
			this.min_mag  = wds.mag_b;
			this.max_mag  = wds.mag_a;
			this.size_min = 0;
			this.type     = DsoType.double_star[0];

			this.names = names;
		}

		public boolean equals(Element elt)
		{
			return equals(this, elt);
		}

		public static boolean equals(Element e0, Element e1)
		{
			boolean result = false;
			
			if (e0 == e1) {
				result = true;
			} else if (e0 != null && e1 != null && e0.names.length == e1.names.length) {
				Hashtable<String,String> key_name_pair = new Hashtable<String,String>();
				for (String name: e0.names) {
					key_name_pair.put(key(name), name);
				}

				for (String name: e1.names) {
					key_name_pair.put(key(name), name);
				}
				
				result = key_name_pair.size() == e0.names.length &&
						e0.ra_hrs   == e1.ra_hrs &&
						e0.de_deg   == e1.de_deg &&
						e0.min_mag  == e1.min_mag &&
						e0.max_mag  == e1.max_mag &&
						e0.size_min == e1.size_min &&
						e0.type     == e1.type &&
						e0.iau      == e1.iau &&
						e0.ybs      == e1.ybs &&
						e0.ngc      == e1.ngc &&
						e0.cal      == e1.cal &&
						e0.h04      == e1.h04 &&
						e0.h25      == e1.h25 &&
						e0.mes      == e1.mes &&
						e0.sha      == e1.sha &&
						e0.ugc      == e1.ugc &&
						e0.arp      == e1.arp &&
						e0.vsx      == e1.vsx &&
						e0.wds      == e1.wds;
			}

			return result;
		}

		public static Element merge(Element e0, Element e1)
		{
			Element result = null;

			if (e0 == e1 || equals(e0, e1)) {
				result = e0;
			} else if (e0 != null && e1 != null) {
				// merge the unique names together
				Hashtable<String,String> key_name_pair = new Hashtable<String,String>();
				for (String name: e0.names) {
					key_name_pair.put(key(name), name);
				}

				for (String name: e1.names) {
					key_name_pair.put(key(name), name);
				}

				String[] names = new String[key_name_pair.size()];
				Enumeration<String> name_list = key_name_pair.elements();
				for (int i=0; i < names.length; i++) {
					names[i] = name_list.nextElement();
				}

				// merge the scalar values
				double ra_hrs   = (e0.ra_hrs   != Element.NO_RA)   ? e0.ra_hrs   : e1.ra_hrs;
				double de_deg   = (e0.de_deg   != Element.NO_DE)   ? e0.de_deg   : e1.de_deg;
				double min_mag  = (e0.min_mag  != Element.NO_MAG)  ? e0.min_mag  : e1.min_mag;
				double max_mag  = (e0.max_mag  != Element.NO_MAG)  ? e0.max_mag  : e1.max_mag;
				double size_min = (e0.size_min != Element.NO_SIZE) ? e0.size_min : e1.size_min;
				String type     = (e0.type     != null)            ? e0.type     : e1.type;

				// merge the catalog sources
				IAUEntry               iau = (e0.iau != null) ? e0.iau : e1.iau;
				YaleBrightStarAscEntry ybs = (e0.ybs != null) ? e0.ybs : e1.ybs;
				NgcIcEntry             ngc = (e0.ngc != null) ? e0.ngc : e1.ngc;
				CaldwellEntry          cal = (e0.cal != null) ? e0.cal : e1.cal;
				Herschel400.Element    h04 = (e0.h04 != null) ? e0.h04 : e1.h04;
				Herschel2500.Element   h25 = (e0.h25 != null) ? e0.h25 : e1.h25;
				MessierEntry           mes = (e0.mes != null) ? e0.mes : e1.mes;
				SharplessEntry         sha = (e0.sha != null) ? e0.sha : e1.sha;
				UgcEntry               ugc = (e0.ugc != null) ? e0.ugc : e1.ugc;
				ArpEntry               arp = (e0.arp != null) ? e0.arp : e1.arp;
				VsxEntry               vsx = (e0.vsx != null) ? e0.vsx : e1.vsx;
				WdsEntry               wds = (e0.wds != null) ? e0.wds : e1.wds;

				result = new Element(names, ra_hrs, de_deg, min_mag, max_mag, size_min, type, iau, ybs, ngc, cal, h04, h25, mes, sha, ugc, arp, vsx, wds);
			} else if (e0 == null && e1 != null) {
				result = e1;
			} else if (e0 != null && e1 == null) {
				result = e0;
			}

			return result;
		}

		public void add(String name)
		{
			Hashtable<String,String> list = new Hashtable<String,String>();
			list.put(key(name), name);
			for (String n: names) {
				list.put(key(n), n);
			}

			if (names.length < list.size()) {
				String[] array = new String[names.length+1];
				for (int i=0; i < names.length; i++) {
					array[i] = names[i];
				}
				array[names.length] = name;
				names = array;
			}
		}

		private String name()
		{
			String result = null;

			if (names != null && 0 < names.length) {
				result = names[0];
				if (1 < names.length) {
					result = result + " (" + names[1];
					for (int i=2; i < names.length; i++) {
						result = result + ", " + names[i];
					}
					result = result + ")";
				}
			}
			
			return result;
		}

		public String[] info_text()
		{
			String[] result = new String[(type == null) ? 2 : 3 ];

			if (names != null && 0 < names.length) {
				result[0] = names[0];
				if (1 < names.length) {
					result[0] = result[0] + " (" + names[1];
					for (int i=2; i < names.length; i++) {
						result[0] = result[0] + ", " + names[i];
					}
					result[0] = result[0] + ")";
				}
				result[1] = String.format("RA %s, DE %s", 
						PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs), 
						PracticalAstronomy.decimal_degrees_to_str_dms(de_deg));

				if (min_mag == max_mag && min_mag != NO_MAG) {
					result[1] = String.format("%s, Mag. %.1f", result[1], max_mag);
				} else if (min_mag != max_mag && min_mag != NO_MAG && max_mag != NO_MAG) {
					if (min_mag < max_mag) {
						result[1] = String.format("%s, Mag. (%.1f, %.1f)", result[1], min_mag, max_mag);
					} else {
						result[1] = String.format("%s, Mag. (%.1f, %.1f)", result[1], max_mag, min_mag);
					}
				}
				
				if (0 < size_min) {
					if (size_min < 1) {
						result[1] = String.format("%s, Dia. %.1f\"", result[1], size_min*60);
					} else {
						result[1] = String.format("%s, Dia. %.1f'", result[1], size_min);
					}
				}

				if (type != null) {
					result[2] = type;
				}
			}

			return result;
		}

		private String[] info_text(int max_len)
		{
			String[] result = new String[(type == null) ? 2 : 3 ];

			if (names != null && 0 < names.length) {
				result[0] = names[0];
				if (1 < names.length) {
					result[0] = result[0] + " (" + names[1];
					for (int i=2; i < names.length; i++) {
						String tmp = result[0] + ", " + names[i];
						if ((max_len-1) <= tmp.length()) break;
						result[0] = tmp;
					}
					result[0] = result[0] + ")";
				}
				result[1] = String.format("RA %s, DE %s", 
						PracticalAstronomy.decimal_hours_to_str_hms(ra_hrs), 
						PracticalAstronomy.decimal_degrees_to_str_dms(de_deg));

				if (min_mag == max_mag && min_mag != NO_MAG) {
					result[1] = String.format("%s, Mag. %.1f", result[1], max_mag);
				} else if (min_mag != max_mag && min_mag != NO_MAG && max_mag != NO_MAG) {
					if (min_mag < max_mag) {
						result[1] = String.format("%s, Mag. (%.1f, %.1f)", result[1], min_mag, max_mag);
					} else {
						result[1] = String.format("%s, Mag. (%.1f, %.1f)", result[1], max_mag, min_mag);
					}
				}
				
				if (0 < size_min) {
					if (size_min < 1) {
						result[1] = String.format("%s, Dia. %.1f\"", result[1], size_min*60);
					} else {
						result[1] = String.format("%s, Dia. %.1f'", result[1], size_min);
					}
				}

				if (type != null) {
					result[2] = type;
				}
			}

			return result;
		}

		public void update(double ra, double de) 
		{
			ra_hrs = ra;
			de_deg = de;
		}

		@Override public String toString()
		{
			String result = null;

			if (names != null && 0 < names.length) {
				result = names[0];
				if (1 < names.length) {
					result = result + " (" + names[1];
					for (int i=2; i < names.length; i++) {
						result = result + ", " + names[i];
					}
					result = result + ")";
				}
				result = String.format("%s ra=%f de=%f",  result, ra_hrs, de_deg);
			}
			
			return result;
		}

		@Override public int compareTo(Element obj)
		{
			if (this.ra_hrs < obj.ra_hrs) {
				return -1;
			} else if (this.ra_hrs == obj.ra_hrs && this.de_deg < obj.de_deg) {
				return -1;
			} else if (this.ra_hrs == obj.ra_hrs && this.de_deg == obj.de_deg) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public String name(Element elt)
	{
		String result = null;

		if (elt != null && elt.names != null && 0 < elt.names.length) {
			sort_dso_names(elt);
			result = elt.name();
		}
		
		return result;
	}

	public String[] info_text(Element elt)
	{
		String[] result = null;

		if (elt != null && elt.names != null && 0 < elt.names.length) {
			sort_dso_names(elt);
			result = elt.info_text();
		}

		return result;
	}

	public String[] info_text(Element elt, int max_len)
	{
		String[] result = null;

		if (elt != null && elt.names != null && 0 < elt.names.length) {
			sort_dso_names(elt);
			result = elt.info_text(max_len);
		}

		return result;
	}

	
	public Element update(String name, double ra, double de) 
	{
		Element elt = find(name);

		if (elt != null) {
			elt.update(ra, de);
		}

		return elt;
	}

	private void closure(Element elt, Hashtable<String,String> key_name_pair)
	{
		if (elt == null || key_name_pair == null) return;

		for (String str: elt.names) {
			String key = key(str);
			if (key_name_pair.get(key) == null) {
				key_name_pair.put(key, str);
				closure(str, key_name_pair);
			}
		}
	}

	private void closure(String name, Hashtable<String,String> key_name_pair)
	{
		if (name == null || key_name_pair == null) return;

		String key = key(name);
		key_name_pair.put(key, name);

		Element elt = table.get(key);
		closure(elt, key_name_pair);
	}
	
	private void add(IAUCatalog iau_cat)
	{
		if (iau_cat == null) return;

		Queue<String> que = new Queue<String>();
		for (IAUEntry iau: iau_cat.elts) {
    		if (iau.name != null && ! iau.name.equals("-")) {
				que.append(iau.name);
				com_que.append(iau.name);
    		}

			que.append(iau.designation);
			can_que.append(iau.designation);

	    	if (iau.id_4 != null && ! iau.id_4.equals("") && ! iau.id_4.equals("-")) {
				String name = String.format("%s %s", iau.id_4, iau.constellation);
				que.append(name);
				if (iau.id_4.matches("[0-9][0-9]*")) {
					fla_que.append(iau.name);
				} else {
					bay_que.append(iau.name);
				}
			}

			if (iau.hipparcos != null && ! iau.hipparcos.equals("") && ! iau.hipparcos.equals("-")) {
				String name = String.format("%s %s", "HIP", iau.hipparcos);
				que.append(name);
				hip_que.append(name);
			}

			if (iau.hd != null && ! iau.hd.equals("") && ! iau.hd.equals("-")) {
				String name = String.format("%s %s", "HD", iau.hd);
				que.append(name);
				hd_que.append(iau.name);
			}

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(iau, names);

			tmp_que.append(elt);
		}
	}

	private void add(YaleBrightStarAscCatalog ybs_cat)
	{
		if (ybs_cat == null) return;

		Queue<String> que = new Queue<String>();
		for (YaleBrightStarAscEntry ybs: ybs_cat.elts) {
			String hr = String.format("HR %d", ybs.HR);
			que.append(hr);
			can_que.append(hr);
			hr_que.append(hr);
			if (ybs.constellation != null && ! ybs.constellation.equals("")) {
				if (ybs.flamsteed != null && ! ybs.flamsteed.equals("")) {
					String name = String.format("%s %s", ybs.flamsteed, ybs.constellation);
					que.append(name);
					fla_que.append(name);
				}

				if (ybs.bayer != null && ! ybs.bayer.equals("")) {
					if (ybs.subname != null && ! ybs.subname.equals("")) {
						String name = String.format("%s%s %s", ybs.bayer, ybs.subname, ybs.constellation);
						que.append(name);
						bay_que.append(name);
					} else {
						String name = String.format("%s %s", ybs.bayer, ybs.constellation);
						que.append(name);
						bay_que.append(name);
					}
				}
			}

			if (0 < ybs.HD) {
				String name = String.format("%s %d", "HD", ybs.HD);
				que.append(name);
				hd_que.append(name);
			}

			/*
			if (0 < ybs.SAO) {
				String name = String.format("%s %d", "SAO", ybs.SAO);
				que.append(name);
			}

			if (0 < ybs.FKS) {
				String name = String.format("%s %d", "FKS", ybs.FKS);
				que.append(name);
			}

			if (0 < ybs.ADS) {
				String name = String.format("%s %d", "ADS", ybs.ADS);
				que.append(name);
			}
			*/

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(ybs, names);

			tmp_que.append(elt);
		}
	}

	private void add(NgcIcCatalog ngc_cat)
	{
		if (ngc_cat == null) return;

		for (NgcIcEntry ngc: ngc_cat.elts) {
			String name = ngc.name;
			ngc_que.append(name);
			can_que.append(name);

			String[] names = new String[] {name};

			Element elt = new Element(ngc, names);

			tmp_que.append(elt);
		}
	}

	private void add(CaldwellCatalog cal_cat)
	{
		if (cal_cat == null) return;

		Queue<String> que = new Queue<String>();
		for (CaldwellEntry cal: cal_cat.elts) {
			que.append(String.format("C%d", cal.number));
			cal_que.append(String.format("C%d", cal.number));
			que.append(String.format("Caldwell %d", cal.number));
			if (cal.ngc_ic != null && ! cal.ngc_ic.equals("")) {
				String name = cal.ngc_ic.replaceAll("[ ][ ]*", " ");
				que.append(name);
			}

			if (cal.common_name != null && ! cal.common_name.equals("")) {
				que.append(cal.common_name);
	    		if (cal.common_name != null && ! cal.common_name.equals("-")) {
	    			com_que.append(cal.common_name);
	    		}
			}

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(cal, names);

			tmp_que.append(elt);
		}
	}

	// TODO
	private void add(Herschel400 h04_cat)
	{
		if (h04_cat == null) return;
		for (Herschel400.Element h04: h04_cat.elts) {
			Queue<String> que = new Queue<String>();
			que.append(h04.name);
			que.append(h04.ngc.replaceAll("[ ][ ]*", " "));
			h04_que.append(h04.name);
			if (h04.common_name != null && ! h04.common_name.equals("") && ! h04.common_name.equals("-")) {
				que.append(h04.common_name);
				com_que.append(h04.common_name);
			}

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(h04, names);

			tmp_que.append(elt);
		}
	}

	// TODO
	private void add(Herschel2500 h25_cat)
	{
		if (h25_cat == null) return;
		Queue<String> que = new Queue<String>();
		for (Herschel2500.Element h25: h25_cat.elts) {
			que.append(h25.name);
//			que.append(h25.alt);
//			que.append(h25.h400);
			h25_que.append(h25.name);

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(h25, names);

			tmp_que.append(elt);
		}
	}


	private void add(MessierCatalog mes_cat)
	{
		if (mes_cat == null) return;

		Queue<String> que = new Queue<String>();
		for (MessierEntry mes: mes_cat.elts) {
			que.append(String.format("M%d", mes.number));
			mes_que.append(String.format("M%d", mes.number));
			que.append(String.format("Messier %d", mes.number));
			if (mes.ngc_ic != null && ! mes.ngc_ic.equals("")) {
				String name = mes.ngc_ic.replaceAll("[ ][ ]*", " ");
				que.append(name);
			}

			if (mes.common_name != null && ! mes.common_name.equals("") && ! mes.common_name.equals("-")) {
				que.append(mes.common_name);
				com_que.append(mes.common_name);
			}

			String[] names = new String[que.length()];
			for (int i=0; i < names.length; i++) {
				names[i] = que.remove();
			}
			Element elt = new Element(mes, names);

			tmp_que.append(elt);
		}
	}

	private void add(SharplessCatalog sha_cat)
	{
		if (sha_cat == null) return;

		for (SharplessEntry sha: sha_cat.elts) {
			String n1 = String.format("Sh2-%d", sha.number);
			String n2 = String.format("Sharpless %d", sha.number);
			String[] names = new String[] { n1, n2 };
			sha_que.append(n1);
			can_que.append(n1);

			Element elt = new Element(sha, names);

			tmp_que.append(elt);
		}
	}

	private void add(UgcCatalog ugc_cat)
	{
		if (ugc_cat == null) return;

		for (UgcEntry ugc: ugc_cat.elts) {
			String name = String.format("UGC %d", ugc.number);
			ugc_que.append(name);
			can_que.append(name);

			String[] names = new String[] {name};

			Element elt = new Element(ugc, names);

			tmp_que.append(elt);
		}
	}

	private void add(ArpCatalog arp_cat)
	{
		if (arp_cat == null) return;

		for (ArpEntry arp: arp_cat.elts) {
			String name = String.format("Arp %d", arp.number);
			String[] names = new String[] { name, arp.common_name };
			arp_que.append(name);
			com_que.append(arp.common_name);

			Element elt = new Element(arp, names);

			tmp_que.append(elt);
		}
	}

	private void add(VsxCatalog vsx_cat)
	{
		if (vsx_cat == null) return;

		for (VsxEntry vsx: vsx_cat.elts) {
			String[] names = new String[] { "VSX " + vsx.name };
			vsx_que.append(names[0]);

			Element elt = new Element(vsx, names);

			tmp_que.append(elt);
		}
	}

	private void add(WdsCatalog wds_cat)
	{
		if (wds_cat == null) return;

		for (WdsEntry wds: wds_cat.elts) {
			String[] names = null;
			if (wds.bayer == null || wds.bayer.equals("")) {
				names = new String[] {"WDS " + wds.name};
			} else {
				names = new String[] {"WDS " + wds.name, wds.bayer};
			}
			wds_que.append(names[0]);

			Element elt = new Element(wds, names);

			tmp_que.append(elt);
		}
	}

	private void sort_dso_names(Element elt)
	{
		int j=0;

		String[] list = elt.names;

		String name = common.get(elt);
		j = swap_dso_names(name, list, j);

		name = bayer.get(elt);
		j = swap_dso_names(name, list, j);

		name = flamsteed.get(elt);
		j = swap_dso_names(name, list, j);

		name = hr_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = hd_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = hip_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = ngc_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = ugc_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = sha_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = arp_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = mes_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = cal_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = h04_list.get(elt);
		j = swap_dso_names(name, list, j);

		name = h25_list.get(elt);
		j = swap_dso_names(name, list, j);
	}

	private static int swap_dso_names(String name, String[] list, int j)
	{
		if (name != null && ! name.equals("") && list != null) {
			for (int i=j; i < list.length; i++) {
				if (name.equalsIgnoreCase(list[i])) {
					String tmp = list[j];
					list[j] = list[i];
					list[i] = tmp;
					j += 1;
					break;
				}
			}
		}
		
		return j;
	}
	
	private static String key(String name)
	{
		return name.toUpperCase().replaceAll("[ ]", "");
	}

	public Element find(String name)
	{
		if (name == null || name.equals("") || partials == null || partials.size() == 0) return null;

		return partials.get(key(name));
	}

	public boolean guard = false;
	public Element find_nearest(double ra_dhrs, double de_ddeg)
	{
		double  angle   = Double.MAX_VALUE;
		Element nearest = null;
		if (guard) {
			for (Element elt: dso_list) {
				if (! elt.names[0].equalsIgnoreCase("Custom")) {
					double a = PracticalAstronomy.angle_between_celestial_objects(ra_dhrs, de_ddeg, elt.ra_hrs, elt.de_deg);
					if (a < angle) {
						angle   = a;
						nearest = elt;
					}
				}
			}
		} else {
	    	Enumeration<Element> elts = table.elements();
	    	dso_list = new Element[table.size()];
	    	for (int i=0; i < dso_list.length; i++) {
	    		Element elt = elts.nextElement();
				if (! elt.names[0].equalsIgnoreCase("Custom")) {
					double a = PracticalAstronomy.angle_between_celestial_objects(ra_dhrs, de_ddeg, elt.ra_hrs, elt.de_deg);
					if (a < angle) {
						angle   = a;
						nearest = elt;
					}
				}
	    	}
		}
		
		return nearest;
	}

	public static void barf(Element elt)
	{
		if (elt == null) return;

		System.out.printf("%s: %3d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
	}

	public void barf(String name)
	{
		if (name == null) return;

		String key = key(name);
		Element elt = table.get(key);
		barf(elt);
	}
	
	public static void barf(String[] text)
	{
		for (String str: text) {
			System.out.printf("%s: %3d: %s%n", NightSkyAtAGlance.CLASS(3), NightSkyAtAGlance.LINE(3), str);
		}
	}

	public static void main(String[] args) throws IOException
	{
		String iau_csn_name            = "IAU-CSN.txt";							// IAU Catalog of Star Names
		String ybsc_name               = "bsc5.dat";							// Yale Bright Star Catalog
		String caldwell_name           = "Caldwell Catalog.csv";				// Caldwell Catalog
		String herschel400_name        = "Herschel 400 Catalog go.csv";			// Herschel 400 Catalog
		String herschel2500_name       = "Herschel 2500 Catalog.csv";			// Herschel 2500 Catalog
		String named_dsos_name         = "Named-DSOs.txt";						// Named DSOs
		String messier_name            = "Messier Catalog.txt";					// Messier Catalog
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
    		"C:/Users/Doug/Desktop/home/projects/org.hypercomputing/data/nightsky/catalogs/",
    		"E:/home/projects/org.hypercomputing/data/nightsky/catalogs/",
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

    	IAUCatalog               iau_csn    = null;
    	YaleBrightStarAscCatalog ybsc       = null;
    	NgcIcCatalog             ngc_ic     = null;
    	CaldwellCatalog          caldwell   = null;
    	Herschel400              her_400    = null;
    	Herschel2500             her_2500   = null;
    	MessierCatalog           messier    = null;
    	NamedDsos                named_dsos = null;
    	SharplessCatalog         sharpless  = null;
    	UgcCatalog               ugc        = null;
    	ArpCatalog               arp        = null;
    	VsxCatalog               vsx        = null;
    	WdsCatalog               wds        = null;

    	iau_csn    = new IAUCatalog              (path + iau_csn_name);
    	ybsc       = new YaleBrightStarAscCatalog(path + ybsc_name);
    	ngc_ic     = new NgcIcCatalog            (path + ngc_ic_path);
    	caldwell   = new CaldwellCatalog         (path + caldwell_name, ngc_ic);
    	her_400    = new Herschel400             (path + herschel400_name, ngc_ic);
    	messier    = new MessierCatalog          (path + messier_name,  ngc_ic);
    	named_dsos = new NamedDsos               (path + named_dsos_name);
    	sharpless  = new SharplessCatalog        (path + sharpless_name);
    	ugc        = new UgcCatalog              (path + ugc_path);
    	her_2500   = new Herschel2500            (path + herschel2500_name, ngc_ic, ugc, her_400);
    	arp        = new ArpCatalog              (path + arp_path,      ngc_ic, ugc, messier);
    	vsx        = new VsxCatalog              (path + vsx_path);
    	wds        = new WdsCatalog              (path + wds_path);

    	System.out.printf("%s: %d: loading DsoAlias%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
    	DsoAlias dso = new DsoAlias(iau_csn, ybsc, ngc_ic, caldwell, her_400, her_2500, messier, named_dsos, sharpless, ugc, arp, vsx, wds);
    	System.out.printf("%s: %d: size=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.table.size());

    	System.out.printf("%s: %d: list=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.dso_list.length);
    	System.out.printf("%s: %d: part=%d%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.partials.size());
    	Element elt = dso.partials.get(key("NGC 891"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	System.out.printf("%s: %d: ngc='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.ngc_list.get(elt));
    	System.out.printf("%s: %d: cal='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.cal_list.get(elt));
    	elt = dso.partials.get(key("Silver"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("Sliver"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("Arp 319"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	System.out.printf("%s: %d: ngc='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.ngc_list.get(elt));
    	System.out.printf("%s: %d: arp='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.arp_list.get(elt));
    	elt = dso.partials.get(key("Ngc 3992"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	System.out.printf("%s: %d: ngc='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.ngc_list.get(elt));
    	System.out.printf("%s: %d: mes='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.mes_list.get(elt));
    	elt = dso.partials.get(key("Algol"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	System.out.printf("%s: %d: bay='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.bayer.get(elt));
    	System.out.printf("%s: %d: fla='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.flamsteed.get(elt));
    	System.out.printf("%s: %d: hr ='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.hr_list.get(elt));
    	System.out.printf("%s: %d: hd ='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.hd_list.get(elt));
    	System.out.printf("%s: %d: hip='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.hip_list.get(elt));
    	System.out.printf("%s: %d: wds='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.wds_list.get(elt));
    	System.out.printf("%s: %d: vsx='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.vsx_list.get(elt));
    	elt = dso.partials.get(key("26 Per"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("bet Per"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("HR 936"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("HD 19356"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("HIP 14576"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("ADS 2362"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("FKS 111"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("WDS 03082+4057"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("SAO 38592"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	elt = dso.partials.get(key("Arp 331"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	barf(elt.info_text());
    	elt = dso.partials.get(key("Pisces Cloud"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	barf(elt.info_text());
    	elt = dso.partials.get(key("NGC 383"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	barf(elt.info_text());
    	elt = dso.partials.get(key("Stephan's Quintet"));
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), elt);
    	System.out.printf("%s: %d: com='%s'%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.common.get(elt));
    	barf(elt.info_text());

    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.partials.get(key("Sun")));
    	dso.update("Sun", 0.5, 10);
    	System.out.printf("%s: %d: elt=%s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.partials.get(key("Sun")));
    	barf(dso.partials.get(key("Sun")    ).info_text());
    	barf(dso.partials.get(key("Moon")   ).info_text());
    	barf(dso.partials.get(key("Mercury")).info_text());
    	barf(dso.partials.get(key("Venus")  ).info_text());
    	barf(dso.partials.get(key("Mars")   ).info_text());
    	barf(dso.partials.get(key("Jupiter")).info_text());
    	barf(dso.partials.get(key("Saturn") ).info_text());
    	barf(dso.partials.get(key("Uranus") ).info_text());
    	barf(dso.partials.get(key("Neptune")).info_text());

    	barf(dso.partials.get(key("Moo")).info_text());
    	barf(dso.partials.get(key("Ercu")).info_text());
    	barf(dso.partials.get(key("Enus")).info_text());
    	barf(dso.partials.get(key("Ars")).info_text());
    	barf(dso.partials.get(key("Upit")).info_text());
    	barf(dso.partials.get(key("Atur")).info_text());
    	barf(dso.partials.get(key("Anus")).info_text());
    	barf(dso.partials.get(key("Eptu")).info_text());

    	barf(dso.partials.get(key("Tiansi")).info_text());
    	barf(dso.partials.get(key("Navi")).info_text());
    	barf(dso.partials.get(key("Regor")).info_text());
    	barf(dso.partials.get(key("Dnoces")).info_text());
    	barf(dso.partials.get(key("Sualocin")).info_text());
    	barf(dso.partials.get(key("Rotanev")).info_text());
    	barf(dso.partials.get(key("Suhail")).info_text());

    	System.out.printf("%s: %d: %n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE());
    	barf(dso.partials.get(key("Pleiades")).info_text());
    	System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.name(dso.partials.get(key("Pleiades"))));
    	barf(dso.partials.get(key("Seven Sisters")).info_text());
    	System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.name(dso.partials.get(key("Seven Sisters"))));
    	barf(dso.partials.get(key("Subaru")).info_text());
    	System.out.printf("%s: %d: %s%n", NightSkyAtAGlance.CLASS(), NightSkyAtAGlance.LINE(), dso.name(dso.partials.get(key("Subaru"))));
	}
}
