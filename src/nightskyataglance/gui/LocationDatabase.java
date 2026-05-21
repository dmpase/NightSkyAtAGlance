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


public class LocationDatabase {
	
	public String city;
	public String state;
	public String country;
	public double lat_deg;
	public double lat_min;
	public double lat_sec;
	public double lon_deg;
	public double lon_min;
	public double lon_sec;
	public String tz_category;
	public String tz_zone;
	
	public LocationDatabase(
		String city,
		String state,
		String country,
		double lat_deg,
		double lat_min,
		double lat_sec,
		double lon_deg,
		double lon_min,
		double lon_sec,
		String tz_category,
		String tz_zone) 
	{
	}

	public static final LocationDatabase[] location = {
		new LocationDatabase( "Eagar",				"AZ", "USA", +34,  6, 39.6786, -105, 17, 29.4678, "US", "Arizona" ),
		new LocationDatabase( "Flagstaff",			"AZ", "USA", +35, 10, 59.5992, -111, 40, 40.9536, "US", "Arizona" ),
		new LocationDatabase( "Greens Peak",		"AZ", "USA", +34,  6, 42.1122, -109, 34, 27.9042, "US", "Arizona" ),
		new LocationDatabase( "Lowell Observatory",	"AZ", "USA", +35, 12,  9.3852, -111, 39, 59.9322, "US", "Arizona" ),
		new LocationDatabase( "Mesa",				"AZ", "USA", +35, 0, 0, -105, 0, 0, "US", "Arizona" ),
		new LocationDatabase( "Phoenix",			"AZ", "USA", +35, 0, 0, -105, 0, 0, "US", "Arizona" ),
		new LocationDatabase( "Springerville",		"AZ", "USA", +34,  7, 45.4545, -105, 16, 56.6508, "US", "Arizona" ),
		new LocationDatabase( "Tucson",				"AZ", "USA", +35, 0, 0, -105, 0, 0, "US", "Arizona" ),

		new LocationDatabase( "Albuquerque",		"NM", "USA", +35,  6, 24.3576, -106, 37, 45.0516, "US", "Mountain" ),
		new LocationDatabase( "Belen",				"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "City of Rocks",		"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "Cosmic Campground",	"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "El Malpais",			"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "El Morro",			"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "GNTO",				"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "Las Cruces",			"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "Los Lunas",			"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "Roswell",			"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
		new LocationDatabase( "Valle Calderas",		"NM", "USA", +35, 0, 0, -105, 0, 0, "US", "Mountain" ),
	};

	public static final String[][] state_alias = {
		{"Alabama",			"AL", "USA" },	// 01
		{"Alaska",			"AK", "USA" },	// 02
		{"Arizona",			"AZ", "USA" },	// 03
		{"Arkansas",		"AR", "USA" },	// 04

		{"California",		"CA", "USA" },	// 05
		{"Colorado",		"CO", "USA" },	// 06
		{"Conneticut",		"CT", "USA" },	// 07

		{"Delaware",		"DE", "USA" },	// 08

		{"Florida",			"FL", "USA" },	// 09

		{"Georgia",			"GA", "USA" },	// 10

		{"Hawaii",			"HI", "USA" },	// 11

		{"Idaho",			"ID", "USA" },	// 12
		{"Illinois",		"IL", "USA" },	// 13
		{"Indianna",		"IN", "USA" },	// 14
		{"Iowa",			"IA", "USA" },	// 15

		{"Kansas",			"KS", "USA" },	// 16
		{"Kentucky",		"KY", "USA" },	// 17

		{"Louisiana",		"LA", "USA" },	// 18
		
		{"Maine",			"ME", "USA" },	// 18
		{"Maryland",		"MD", "USA" },	// 20
		{"Massachussets",	"MA", "USA" },	// 21
		{"Michigan",		"MI", "USA" },	// 22
		{"Minnesota",		"MN", "USA" },	// 23
		{"Mississippi",		"MS", "USA" },	// 24
		{"Missouri",		"MO", "USA" },	// 25
		{"Montana",			"MT", "USA" },	// 26

		{"Nebraska",		"NB", "USA" },	// 27
		{"Nevada",			"NV", "USA" },	// 28
		{"New Hampshire",	"NH", "USA" },	// 29
		{"New Jersey",		"NJ", "USA" },	// 30
		{"New Mexico",		"NM", "USA" },	// 31
		{"New York",		"NY", "USA" },	// 32
		{"North Carolina",	"NC", "USA" },	// 33
		{"North Dakota",	"ND", "USA" },	// 34

		{"Ohio",			"OH", "USA" },	// 35
		{"Oklahoma",		"OK", "USA" },	// 36
		{"Oregon",			"OR", "USA" },	// 37

		{"Pennsylvania",	"PA", "USA" },	// 38

		{"Rhode Island",	"RI", "USA" },	// 39

		{"South Carolina",	"SC", "USA" },	// 40
		{"South Dakota",	"SD", "USA" },	// 41

		{"Tennessey",		"TN", "USA" },	// 42
		{"Texas",			"TX", "USA" },	// 43
		
		{"Utah",			"UT", "USA" },	// 44
		
		{"Vermont",			"VT", "USA" },	// 45
		{"Virginia",		"VI", "USA" },	// 46
		{"Washington",		"WA", "USA" },	// 47
		{"West Virginia",	"WV", "USA" },	// 48
		{"Wisconsin",		"WI", "USA" },	// 49
		{"Wyoming",			"WY", "USA" },	// 50
	};
}
