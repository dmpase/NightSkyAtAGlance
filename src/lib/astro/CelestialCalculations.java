package lib.astro;

/*******************************************************************************
 * Copyright (c) 2025-2025 Douglas M. Pase                                     *
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


import java.util.Calendar;
import java.util.TimeZone;

import lib.sphere.Angle;

// this code is created from calculations described in the book:
// Celestial Calculations: A Gentle Introduction to Computational Astronomy, 
// by Jackie L. Lawrence, The MIT Press, Cambridge, London, England, 
// Copyright 2018
public class CelestialCalculations {
	public static final double kelvin_centegrade_offset    = 273.15;
	public static final double centegrade_farenheit_ratio  = 5.0 / 9.0;
	public static final double farenheit_centegrade_offset = 32.0;
	
	public static final double centegrade_to_farenheight(double centegrade)
	{
		return centegrade / centegrade_farenheit_ratio + farenheit_centegrade_offset;
	}

	public static final double centegrade_to_kelvin(double centegrade)
	{
		return centegrade + kelvin_centegrade_offset;
	}

	public static final double farenheight_to_centegrade(double farenheit)
	{
		return (farenheit - farenheit_centegrade_offset) * centegrade_farenheit_ratio;
	}

	public static final double farenheight_to_kelvin(double farenheit)
	{
		return (farenheit - farenheit_centegrade_offset) * centegrade_farenheit_ratio + kelvin_centegrade_offset;
	}

	public static final double kelvin_to_centegrade(double kelvin)
	{
		return kelvin - kelvin_centegrade_offset;
	}

	public static final double kelvin_to_farenheit(double kelvin)
	{
		return (kelvin - kelvin_centegrade_offset) / centegrade_farenheit_ratio + farenheit_centegrade_offset;
	}

	
	public static final double inches_per_foot          =   12.0;
	public static final double inches_per_meter         =   39.37009424;
	public static final double feet_per_yard            =    3.0;
	public static final double feet_per_mile            = 5280.0;
	public static final double feet_per_meter           =    3.28084;
	public static final double yards_per_meter          = feet_per_meter / feet_per_yard;
	public static final double meters_per_mile          = 1609.3445;
	public static final double meters_per_au            =    1.495978707e11;
	public static final double miles_per_au             =    9.29e+7;
	public static final double miles_per_lightyear      =    5.87e12;
	public static final double lightyears_per_kly       =    1.0e+3;
	public static final double lightyears_per_mly       =    1.0e+6;
	public static final double lightyears_per_parsec    =    3.26156;
	public static final double parsecs_per_lightyear    =    0.3068;
	public static final double parsecs_per_kpc          =    1.0e+3;
	public static final double parsecs_per_mpc          =    1.0e+6;
	public static final double degrees_per_hour         =  360.0 / 24.0;

	public static final double angstroms_per_nm         =    1.0e+1;
	public static final double angstroms_per_um         =    1.0e+4;
	public static final double angstroms_per_mm         =    1.0e+7;
	public static final double angstroms_per_cm         =    1.0e+8;
	public static final double angstroms_per_meter      =    1.0e10;
	public static final double angstroms_per_km         =    1.0e13;
	public static final double angstroms_per_inch       = angstroms_per_meter     / inches_per_meter;
	public static final double angstroms_per_foot       = angstroms_per_inch      * inches_per_foot;
	public static final double angstroms_per_yard       = angstroms_per_foot      * feet_per_yard;
	public static final double angstroms_per_mile       = angstroms_per_foot      * feet_per_mile;
	public static final double angstroms_per_au         = angstroms_per_mile      * miles_per_au;
	public static final double angstroms_per_lightyear  = angstroms_per_mile      * miles_per_lightyear;
	public static final double angstroms_per_parsec     = angstroms_per_lightyear * lightyears_per_parsec;
	public static final double angstroms_per_kly        = angstroms_per_lightyear * lightyears_per_kly;
	public static final double angstroms_per_kpc        = angstroms_per_parsec    * parsecs_per_kpc;
	public static final double angstroms_per_mly        = angstroms_per_lightyear * lightyears_per_mly;
	public static final double angstroms_per_mpc        = angstroms_per_parsec    * parsecs_per_mpc;

	public static final double nm_per_angstrom          =    1.0e-1;
	public static final double nm_per_um                =    1.0e+3;
	public static final double nm_per_mm                =    1.0e+6;
	public static final double nm_per_cm                =    1.0e+7;
	public static final double nm_per_meter             =    1.0e+9;
	public static final double nm_per_km                =    1.0e12;
	public static final double nm_per_inch              = nm_per_meter     / inches_per_meter;
	public static final double nm_per_foot              = nm_per_inch      * inches_per_foot;
	public static final double nm_per_yard              = nm_per_foot      * feet_per_yard;
	public static final double nm_per_mile              = nm_per_foot      * feet_per_mile;
	public static final double nm_per_au                = nm_per_mile      * miles_per_au;
	public static final double nm_per_lightyear         = nm_per_mile      * miles_per_lightyear;
	public static final double nm_per_parsec            = nm_per_lightyear * lightyears_per_parsec;
	public static final double nm_per_kly               = nm_per_lightyear * lightyears_per_kly;
	public static final double nm_per_kpc               = nm_per_parsec    * parsecs_per_kpc;
	public static final double nm_per_mly               = nm_per_lightyear * lightyears_per_mly;
	public static final double nm_per_mpc               = nm_per_parsec    * parsecs_per_mpc;

	public static final double um_per_angstrom          =    1.0e-4;
	public static final double um_per_nm                =    1.0e-3;
	public static final double um_per_mm                =    1.0e+3;
	public static final double um_per_cm                =    1.0e+4;
	public static final double um_per_meter             =    1.0e+6;
	public static final double um_per_km                =    1.0e+9;
	public static final double um_per_inch              = um_per_meter     / inches_per_meter;
	public static final double um_per_foot              = um_per_inch      * inches_per_foot;
	public static final double um_per_yard              = um_per_foot      * feet_per_yard;
	public static final double um_per_mile              = um_per_foot      * feet_per_mile;
	public static final double um_per_au                = um_per_mile      * miles_per_au;
	public static final double um_per_lightyear         = um_per_mile      * miles_per_lightyear;
	public static final double um_per_parsec            = um_per_lightyear * lightyears_per_parsec;
	public static final double um_per_kly               = um_per_lightyear * lightyears_per_kly;
	public static final double um_per_kpc               = um_per_parsec    * parsecs_per_kpc;
	public static final double um_per_mly               = um_per_lightyear * lightyears_per_mly;
	public static final double um_per_mpc               = um_per_parsec    * parsecs_per_mpc;

	public static final double mm_per_angstrom          =    1.0e-7;
	public static final double mm_per_nm                =    1.0e-6;
	public static final double mm_per_um                =    1.0e-3;
	public static final double mm_per_cm                =    1.0e+1;
	public static final double mm_per_meter             =    1.0e+3;
	public static final double mm_per_km                =    1.0e+6;
	public static final double mm_per_inch              = mm_per_meter     / inches_per_meter;
	public static final double mm_per_foot              = mm_per_inch      * inches_per_foot;
	public static final double mm_per_yard              = mm_per_foot      * feet_per_yard;
	public static final double mm_per_mile              = mm_per_foot      * feet_per_mile;
	public static final double mm_per_au                = mm_per_mile      * miles_per_au;
	public static final double mm_per_lightyear         = mm_per_mile      * miles_per_lightyear;
	public static final double mm_per_parsec            = mm_per_lightyear * lightyears_per_parsec;
	public static final double mm_per_kly               = mm_per_lightyear * lightyears_per_kly;
	public static final double mm_per_kpc               = mm_per_parsec    * parsecs_per_kpc;
	public static final double mm_per_mly               = mm_per_lightyear * lightyears_per_mly;
	public static final double mm_per_mpc               = mm_per_parsec    * parsecs_per_mpc;
	
	public static final double cm_per_angstrom          =    1.0e-8;
	public static final double cm_per_nm                =    1.0e-7;
	public static final double cm_per_um                =    1.0e-4;
	public static final double cm_per_mm                =    1.0e-1;
	public static final double cm_per_meter             =    1.0e+2;
	public static final double cm_per_km                =    1.0e+5;
	public static final double cm_per_inch              = cm_per_meter     / inches_per_meter;
	public static final double cm_per_foot              = cm_per_inch      * inches_per_foot;
	public static final double cm_per_yard              = cm_per_foot      * feet_per_yard;
	public static final double cm_per_mile              = cm_per_foot      * feet_per_mile;
	public static final double cm_per_au                = cm_per_mile      * miles_per_au;
	public static final double cm_per_lightyear         = cm_per_mile      * miles_per_lightyear;
	public static final double cm_per_parsec            = cm_per_lightyear * lightyears_per_parsec;
	public static final double cm_per_kly               = cm_per_lightyear * lightyears_per_kly;
	public static final double cm_per_kpc               = cm_per_parsec    * parsecs_per_kpc;
	public static final double cm_per_mly               = cm_per_lightyear * lightyears_per_mly;
	public static final double cm_per_mpc               = cm_per_parsec    * parsecs_per_mpc;

	public static final double meters_per_angstrom      =    1.0e-10;
	public static final double meters_per_nm            =    1.0e-9;
	public static final double meters_per_um            =    1.0e-6;
	public static final double meters_per_mm            =    1.0e-3;
	public static final double meters_per_cm            =    1.0e-2;
	public static final double meters_per_km            =    1.0e+3;
	public static final double meters_per_inch          =    1.0               / inches_per_meter;
	public static final double meters_per_foot          = meters_per_inch      * inches_per_foot;
	public static final double meters_per_yard          = meters_per_foot      * feet_per_yard;
	public static final double meters_per_lightyear     = meters_per_mile      * miles_per_lightyear;
	public static final double meters_per_parsec        = meters_per_lightyear * lightyears_per_parsec;
	public static final double meters_per_kly           = meters_per_lightyear * lightyears_per_kly;
	public static final double meters_per_kpc           = meters_per_parsec    * parsecs_per_kpc;
	public static final double meters_per_mly           = meters_per_lightyear * lightyears_per_mly;
	public static final double meters_per_mpc           = meters_per_parsec    * parsecs_per_mpc;

	public static final double km_per_angstrom          =    1.0e-13;
	public static final double km_per_nm                =    1.0e-12;
	public static final double km_per_um                =    1.0e-9;
	public static final double km_per_mm                =    1.0e-6;
	public static final double km_per_cm                =    1.0e-5;
	public static final double km_per_meter             =    1.0e-3;
	public static final double km_per_inch              = km_per_meter     / inches_per_meter;
	public static final double km_per_foot              = km_per_inch      * inches_per_foot;
	public static final double km_per_yard              = km_per_foot      * feet_per_yard;
	public static final double km_per_mile              = km_per_foot      * feet_per_mile;
	public static final double km_per_au                = km_per_mile      * miles_per_au;
	public static final double km_per_lightyear         = km_per_mile      * miles_per_lightyear;
	public static final double km_per_parsec            = km_per_lightyear * lightyears_per_parsec;
	public static final double km_per_kly               = km_per_lightyear * lightyears_per_kly;
	public static final double km_per_kpc               = km_per_parsec    * parsecs_per_kpc;
	public static final double km_per_mly               = km_per_lightyear * lightyears_per_mly;
	public static final double km_per_mpc               = km_per_parsec    * parsecs_per_mpc;
	
	public static final double inches_per_angstrom      = inches_per_meter     * meters_per_angstrom;
	public static final double inches_per_nm            = inches_per_meter     * meters_per_nm;
	public static final double inches_per_um            = inches_per_meter     * meters_per_um;
	public static final double inches_per_mm            = inches_per_meter     * meters_per_mm;
	public static final double inches_per_cm            = inches_per_meter     * meters_per_cm;
	public static final double inches_per_km            = inches_per_meter     * meters_per_km;
	public static final double inches_per_yard          = inches_per_foot      * feet_per_yard;
	public static final double inches_per_mile          = inches_per_foot      * feet_per_mile;
	public static final double inches_per_au            = inches_per_mile      * miles_per_au;
	public static final double inches_per_lightyear     = inches_per_mile      * miles_per_lightyear;
	public static final double inches_per_parsec        = inches_per_lightyear * lightyears_per_parsec;
	public static final double inches_per_kly           = inches_per_lightyear * lightyears_per_kly;
	public static final double inches_per_kpc           = inches_per_parsec    * parsecs_per_kpc;
	public static final double inches_per_mly           = inches_per_lightyear * lightyears_per_mly;
	public static final double inches_per_mpc           = inches_per_parsec    * parsecs_per_mpc;
	
	public static final double feet_per_angstrom        = feet_per_meter     * meters_per_angstrom;
	public static final double feet_per_nm              = feet_per_meter     * meters_per_nm;
	public static final double feet_per_um              = feet_per_meter     * meters_per_um;
	public static final double feet_per_mm              = feet_per_meter     * meters_per_mm;
	public static final double feet_per_cm              = feet_per_meter     * meters_per_cm;
	public static final double feet_per_km              = feet_per_meter     * meters_per_km;
	public static final double feet_per_inch            =     1.0            / inches_per_foot;
	public static final double feet_per_au              = feet_per_mile      * miles_per_au;
	public static final double feet_per_lightyear       = feet_per_mile      * miles_per_lightyear;
	public static final double feet_per_parsec          = feet_per_lightyear * lightyears_per_parsec;
	public static final double feet_per_kly             = feet_per_lightyear * lightyears_per_kly;
	public static final double feet_per_kpc             = feet_per_parsec    * parsecs_per_kpc;
	public static final double feet_per_mly             = feet_per_lightyear * lightyears_per_mly;
	public static final double feet_per_mpc             = feet_per_parsec    * parsecs_per_mpc;

	public static final double yards_per_angstrom       = yards_per_meter     * meters_per_angstrom;
	public static final double yards_per_nm             = yards_per_meter     * meters_per_nm;
	public static final double yards_per_um             = yards_per_meter     * meters_per_um;
	public static final double yards_per_mm             = yards_per_meter     * meters_per_mm;
	public static final double yards_per_cm             = yards_per_meter     * meters_per_cm;
	public static final double yards_per_km             = yards_per_meter     * meters_per_km;
	public static final double yards_per_inch           =     1.0             / inches_per_yard;
	public static final double yards_per_foot           =     1.0             / feet_per_yard;
	public static final double yards_per_mile           = feet_per_mile       / feet_per_yard;
	public static final double yards_per_au             = yards_per_mile      * miles_per_au;
	public static final double yards_per_lightyear      = yards_per_mile      * miles_per_lightyear;
	public static final double yards_per_parsec         = yards_per_lightyear * lightyears_per_parsec;
	public static final double yards_per_kly            = yards_per_lightyear * lightyears_per_kly;
	public static final double yards_per_kpc            = yards_per_parsec    * parsecs_per_kpc;
	public static final double yards_per_mly            = yards_per_lightyear * lightyears_per_mly;
	public static final double yards_per_mpc            = yards_per_parsec    * parsecs_per_mpc;

	public static final double miles_per_angstrom       = meters_per_angstrom / meters_per_mile;
	public static final double miles_per_nm             = meters_per_nm       / meters_per_mile;
	public static final double miles_per_um             = meters_per_um       / meters_per_mile;
	public static final double miles_per_mm             = meters_per_mm       / meters_per_mile;
	public static final double miles_per_cm             = meters_per_cm       / meters_per_mile;
	public static final double miles_per_meter          =     1.0             / meters_per_mile;
	public static final double miles_per_km             = meters_per_km       / meters_per_mile;
	public static final double miles_per_inch           =     1.0             / inches_per_mile;
	public static final double miles_per_foot           =     1.0             / feet_per_mile;
	public static final double miles_per_yard           =     1.0             / yards_per_mile;
	public static final double miles_per_parsec         = miles_per_lightyear * lightyears_per_parsec;
	public static final double miles_per_kly            = miles_per_lightyear * lightyears_per_kly;
	public static final double miles_per_kpc            = miles_per_parsec    * parsecs_per_kpc;
	public static final double miles_per_mly            = miles_per_lightyear * lightyears_per_mly;
	public static final double miles_per_mpc            = miles_per_parsec    * parsecs_per_mpc;

	public static final double aus_per_angstrom         = meters_per_angstrom   / meters_per_au;
	public static final double aus_per_nm               = meters_per_nm         / meters_per_au;
	public static final double aus_per_um               = meters_per_um         / meters_per_au;
	public static final double aus_per_mm               = meters_per_mm         / meters_per_au;
	public static final double aus_per_cm               = meters_per_cm         / meters_per_au;
	public static final double aus_per_meter            =      1.0              / meters_per_au;
	public static final double aus_per_km               = meters_per_km         / meters_per_au;
	public static final double aus_per_inch             =      1.0              / inches_per_au;
	public static final double aus_per_foot             =      1.0              / feet_per_au;
	public static final double aus_per_yard             =      1.0              / yards_per_au;
	public static final double aus_per_mile             =      1.0              / miles_per_au;
	public static final double aus_per_lightyear        = aus_per_meter         * meters_per_lightyear;
	public static final double aus_per_parsec           = aus_per_lightyear     * lightyears_per_parsec;
	public static final double aus_per_kly              = aus_per_lightyear     * lightyears_per_kly;
	public static final double aus_per_kpc              = aus_per_parsec        * parsecs_per_kpc;
	public static final double aus_per_mly              = aus_per_lightyear     * lightyears_per_mly;
	public static final double aus_per_mpc              = aus_per_parsec        * parsecs_per_mpc;

	public static final double lightyears_per_angstrom  = meters_per_angstrom   / meters_per_lightyear;
	public static final double lightyears_per_nm        = meters_per_nm         / meters_per_lightyear;
	public static final double lightyears_per_um        = meters_per_um         / meters_per_lightyear;
	public static final double lightyears_per_mm        = meters_per_mm         / meters_per_lightyear;
	public static final double lightyears_per_cm        = meters_per_cm         / meters_per_lightyear;
	public static final double lightyears_per_meter     =      1.0              / meters_per_lightyear;
	public static final double lightyears_per_km        = meters_per_km         / meters_per_lightyear;
	public static final double lightyears_per_inch      =     1.0               / inches_per_lightyear;
	public static final double lightyears_per_foot      =     1.0               / feet_per_lightyear;
	public static final double lightyears_per_yard      =     1.0               / yards_per_lightyear;
	public static final double lightyears_per_mile      =     1.0               / miles_per_lightyear;
	public static final double lightyears_per_au        =     1.0               / aus_per_lightyear;
	public static final double lightyears_per_kpc       = lightyears_per_parsec * parsecs_per_kpc;
	public static final double lightyears_per_mpc       = lightyears_per_parsec * parsecs_per_mpc;

	public static final double parsecs_per_angstrom     = meters_per_angstrom   / meters_per_parsec;
	public static final double parsecs_per_nm           = meters_per_nm         / meters_per_parsec;
	public static final double parsecs_per_um           = meters_per_um         / meters_per_parsec;
	public static final double parsecs_per_mm           = meters_per_mm         / meters_per_parsec;
	public static final double parsecs_per_cm           = meters_per_cm         / meters_per_parsec;
	public static final double parsecs_per_meter        =      1.0              / meters_per_parsec;
	public static final double parsecs_per_km           = meters_per_km         / meters_per_parsec;
	public static final double parsecs_per_inch         =     1.0               / inches_per_parsec;
	public static final double parsecs_per_foot         =     1.0               / feet_per_parsec;
	public static final double parsecs_per_yard         =     1.0               / yards_per_parsec;
	public static final double parsecs_per_mile         =     1.0               / miles_per_parsec;
	public static final double parsecs_per_au           =     1.0               / aus_per_parsec;
	public static final double parsecs_per_kly          = parsecs_per_lightyear * parsecs_per_kpc;
	public static final double parsecs_per_mly          = parsecs_per_lightyear * parsecs_per_mpc;

	public static final double kly_per_angstrom        = meters_per_angstrom    / meters_per_kly;
	public static final double kly_per_nm              = meters_per_nm          / meters_per_kly;
	public static final double kly_per_um              = meters_per_um          / meters_per_kly;
	public static final double kly_per_mm              = meters_per_mm          / meters_per_kly;
	public static final double kly_per_cm              = meters_per_cm          / meters_per_kly;
	public static final double kly_per_meter           =     1.0                / meters_per_kly;
	public static final double kly_per_km              = meters_per_km          / meters_per_kly;
	public static final double kly_per_inch            =     1.0                / inches_per_kly;
	public static final double kly_per_foot            =     1.0                / feet_per_kly;
	public static final double kly_per_yard            =     1.0                / yards_per_kly;
	public static final double kly_per_mile            =     1.0                / miles_per_kly;
	public static final double kly_per_au              =     1.0                / aus_per_kly;
	public static final double kly_per_lightyear       =     1.0                / lightyears_per_kly;
	public static final double kly_per_parsec          = lightyears_per_parsec  / lightyears_per_kly;
	public static final double kly_per_kpc             = lightyears_per_parsec;
	public static final double kly_per_mly             =     1.0e3;
	public static final double kly_per_mpc             = lightyears_per_mpc     / lightyears_per_kly;

	public static final double kpc_per_angstrom        = meters_per_angstrom    / meters_per_kpc;
	public static final double kpc_per_nm              = meters_per_nm          / meters_per_kpc;
	public static final double kpc_per_um              = meters_per_um          / meters_per_kpc;
	public static final double kpc_per_mm              = meters_per_mm          / meters_per_kpc;
	public static final double kpc_per_meter           =     1.0                / meters_per_kpc;
	public static final double kpc_per_cm              = meters_per_cm          / meters_per_kpc;
	public static final double kpc_per_km              = meters_per_km          / meters_per_kpc;
	public static final double kpc_per_inch            =     1.0                / inches_per_kpc;
	public static final double kpc_per_foot            =     1.0                / feet_per_kpc;
	public static final double kpc_per_yard            =     1.0                / yards_per_kpc;
	public static final double kpc_per_mile            =     1.0                / miles_per_kpc;
	public static final double kpc_per_au              =     1.0                / aus_per_kpc;
	public static final double kpc_per_lightyear       =     1.0                / lightyears_per_kpc;
	public static final double kpc_per_parsec          =     1.0                / parsecs_per_kpc;
	public static final double kpc_per_kly             = parsecs_per_lightyear;
	public static final double kpc_per_mly             = parsecs_per_kly;
	public static final double kpc_per_mpc             =     1.0e3;

	public static final double mly_per_angstrom        = meters_per_angstrom   / meters_per_mly;
	public static final double mly_per_nm              = meters_per_nm         / meters_per_mly;
	public static final double mly_per_um              = meters_per_um         / meters_per_kpc;
	public static final double mly_per_mm              = meters_per_mm         / meters_per_kpc;
	public static final double mly_per_cm              = meters_per_cm         / meters_per_kpc;
	public static final double mly_per_meter           =     1.0               / meters_per_kpc;
	public static final double mly_per_km              = meters_per_km         / meters_per_kpc;
	public static final double mly_per_inch            =     1.0               / inches_per_mly;
	public static final double mly_per_foot            =     1.0               / feet_per_mly;
	public static final double mly_per_yard            =     1.0               / yards_per_mly;
	public static final double mly_per_mile            =     1.0               / miles_per_mly;
	public static final double mly_per_au              =     1.0               / aus_per_mly;
	public static final double mly_per_lightyear       =     1.0e-6;
	public static final double mly_per_parsec          = mly_per_lightyear     * lightyears_per_parsec;
	public static final double mly_per_kly             =     1.0e-3;
	public static final double mly_per_kpc             = lightyears_per_kpc    / lightyears_per_mly;
	public static final double mly_per_mpc             = lightyears_per_mpc    / lightyears_per_mly;

	public static final double mpc_per_angstrom        = meters_per_angstrom   / meters_per_mpc;
	public static final double mpc_per_nm              = meters_per_nm         / meters_per_mpc;
	public static final double mpc_per_um              = meters_per_um         / meters_per_mpc;
	public static final double mpc_per_mm              = meters_per_mm         / meters_per_mpc;
	public static final double mpc_per_cm              = meters_per_cm         / meters_per_mpc;
	public static final double mpc_per_meter           =     1.0               / meters_per_mpc;
	public static final double mpc_per_km              = meters_per_km         / meters_per_mpc;
	public static final double mpc_per_inch            =     1.0               / inches_per_mpc;
	public static final double mpc_per_foot            =     1.0               / feet_per_mpc;
	public static final double mpc_per_yard            =     1.0               / yards_per_mpc;
	public static final double mpc_per_mile            =     1.0               / miles_per_mpc;
	public static final double mpc_per_au              =     1.0               / aus_per_mpc;
	public static final double mpc_per_lightyear       =     1.0               / lightyears_per_mpc;
	public static final double mpc_per_parsec          =     1.0e-6;
	public static final double mpc_per_kly             =     1.0               / kly_per_mpc;
	public static final double mpc_per_kpc             =     1.0e-3;
	public static final double mpc_per_mly             =     1.0               / mly_per_mpc;


	public static final double angstroms_to_nm        (double angstroms)  { return angstroms * nm_per_angstrom;         }
	public static final double angstroms_to_um        (double angstroms)  { return angstroms * um_per_angstrom;         }
	public static final double angstroms_to_mm        (double angstroms)  { return angstroms * mm_per_angstrom;         }
	public static final double angstroms_to_cm        (double angstroms)  { return angstroms * cm_per_angstrom;         }
	public static final double angstroms_to_meters    (double angstroms)  { return angstroms * meters_per_angstrom;     }
	public static final double angstroms_to_km        (double angstroms)  { return angstroms * km_per_angstrom;         }
	public static final double angstroms_to_inches    (double angstroms)  { return angstroms * inches_per_angstrom;     }
	public static final double angstroms_to_feet      (double angstroms)  { return angstroms * feet_per_angstrom;       }
	public static final double angstroms_to_yards     (double angstroms)  { return angstroms * yards_per_angstrom;      }
	public static final double angstroms_to_miles     (double angstroms)  { return angstroms * miles_per_angstrom;      }
	public static final double angstroms_to_aus       (double angstroms)  { return angstroms * aus_per_angstrom;        }
	public static final double angstroms_to_lightyears(double angstroms)  { return angstroms * lightyears_per_angstrom; }
	public static final double angstroms_to_parsec    (double angstroms)  { return angstroms * parsecs_per_angstrom;    }
	public static final double angstroms_to_kly       (double angstroms)  { return angstroms * kly_per_angstrom;        }
	public static final double angstroms_to_kpc       (double angstroms)  { return angstroms * kpc_per_angstrom;        }
	public static final double angstroms_to_mly       (double angstroms)  { return angstroms * mly_per_angstrom;        }
	public static final double angstroms_to_mpc       (double angstroms)  { return angstroms * mpc_per_angstrom;        }

	public static final double nm_to_angstroms        (double nm)         { return nm        * angstroms_per_nm;        }
	public static final double nm_to_um               (double nm)         { return nm        * um_per_nm;               }
	public static final double nm_to_mm               (double nm)         { return nm        * mm_per_nm;               }
	public static final double nm_to_cm               (double nm)         { return nm        * cm_per_nm;               }
	public static final double nm_to_meters           (double nm)         { return nm        * meters_per_nm;           }
	public static final double nm_to_km               (double nm)         { return nm        * km_per_nm;               }
	public static final double nm_to_inches           (double nm)         { return nm        * inches_per_nm;           }
	public static final double nm_to_feet             (double nm)         { return nm        * feet_per_nm;             }
	public static final double nm_to_yards            (double nm)         { return nm        * yards_per_nm;            }
	public static final double nm_to_miles            (double nm)         { return nm        * miles_per_nm;            }
	public static final double nm_to_aus              (double nm)         { return nm        * aus_per_nm;              }
	public static final double nm_to_lightyears       (double nm)         { return nm        * lightyears_per_nm;       }
	public static final double nm_to_parsec           (double nm)         { return nm        * parsecs_per_nm;          }
	public static final double nm_to_kly              (double nm)         { return nm        * kly_per_nm;              }
	public static final double nm_to_kpc              (double nm)         { return nm        * kpc_per_nm;              }
	public static final double nm_to_mly              (double nm)         { return nm        * mly_per_nm;              }
	public static final double nm_to_mpc              (double nm)         { return nm        * mpc_per_nm;              }

	public static final double um_to_angstroms        (double um)         { return um        * angstroms_per_um;  }
	public static final double um_to_nm               (double um)         { return um        * nm_per_um;         }
	public static final double um_to_mm               (double um)         { return um        * mm_per_um;         }
	public static final double um_to_cm               (double um)         { return um        * cm_per_um;         }
	public static final double um_to_meters           (double um)         { return um        * meters_per_um;     }
	public static final double um_to_km               (double um)         { return um        * km_per_um;         }
	public static final double um_to_inches           (double um)         { return um        * inches_per_um;     }
	public static final double um_to_feet             (double um)         { return um        * feet_per_um;       }
	public static final double um_to_yards            (double um)         { return um        * yards_per_um;      }
	public static final double um_to_miles            (double um)         { return um        * miles_per_um;      }
	public static final double um_to_aus              (double um)         { return um        * aus_per_um;        }
	public static final double um_to_lightyears       (double um)         { return um        * lightyears_per_um; }
	public static final double um_to_parsec           (double um)         { return um        * parsecs_per_um;    }
	public static final double um_to_kly              (double um)         { return um        * kly_per_um;        }
	public static final double um_to_kpc              (double um)         { return um        * kpc_per_um;        }
	public static final double um_to_mly              (double um)         { return um        * mly_per_um;        }
	public static final double um_to_mpc              (double um)         { return um        * mpc_per_um;        }

	public static final double mm_to_angstroms        (double mm)         { return mm        * angstroms_per_mm;  }
	public static final double mm_to_nm               (double mm)         { return mm        * nm_per_mm;         }
	public static final double mm_to_um               (double mm)         { return mm        * um_per_mm;         }
	public static final double mm_to_cm               (double mm)         { return mm        * cm_per_mm;         }
	public static final double mm_to_meters           (double mm)         { return mm        * meters_per_mm;     }
	public static final double mm_to_km               (double mm)         { return mm        * km_per_mm;         }
	public static final double mm_to_inches           (double mm)         { return mm        * inches_per_mm;     }
	public static final double mm_to_feet             (double mm)         { return mm        * feet_per_mm;       }
	public static final double mm_to_yards            (double mm)         { return mm        * yards_per_mm;      }
	public static final double mm_to_miles            (double mm)         { return mm        * miles_per_mm;      }
	public static final double mm_to_aus              (double mm)         { return mm        * aus_per_mm;        }
	public static final double mm_to_lightyears       (double mm)         { return mm        * lightyears_per_mm; }
	public static final double mm_to_parsec           (double mm)         { return mm        * parsecs_per_mm;    }
	public static final double mm_to_kly              (double mm)         { return mm        * kly_per_mm;        }
	public static final double mm_to_kpc              (double mm)         { return mm        * kpc_per_mm;        }
	public static final double mm_to_mly              (double mm)         { return mm        * mly_per_mm;        }
	public static final double mm_to_mpc              (double mm)         { return mm        * mpc_per_mm;        }

	public static final double cm_to_angstroms        (double cm)         { return cm        * angstroms_per_cm;  }
	public static final double cm_to_nm               (double cm)         { return cm        * nm_per_cm;         }
	public static final double cm_to_um               (double cm)         { return cm        * um_per_cm;         }
	public static final double cm_to_mm               (double cm)         { return cm        * mm_per_cm;         }
	public static final double cm_to_meters           (double cm)         { return cm        * meters_per_cm;     }
	public static final double cm_to_km               (double cm)         { return cm        * km_per_cm;         }
	public static final double cm_to_inches           (double cm)         { return cm        * inches_per_cm;     }
	public static final double cm_to_feet             (double cm)         { return cm        * feet_per_cm;       }
	public static final double cm_to_yards            (double cm)         { return cm        * yards_per_cm;      }
	public static final double cm_to_miles            (double cm)         { return cm        * miles_per_cm;      }
	public static final double cm_to_aus              (double cm)         { return cm        * aus_per_cm;        }
	public static final double cm_to_lightyears       (double cm)         { return cm        * lightyears_per_cm; }
	public static final double cm_to_parsec           (double cm)         { return cm        * parsecs_per_cm;    }
	public static final double cm_to_kly              (double cm)         { return cm        * kly_per_cm;        }
	public static final double cm_to_kpc              (double cm)         { return cm        * kpc_per_cm;        }
	public static final double cm_to_mly              (double cm)         { return cm        * mly_per_cm;        }
	public static final double cm_to_mpc              (double cm)         { return cm        * mpc_per_cm;        }

	public static final double meters_to_angstroms    (double meters)     { return meters * angstroms_per_meter;  }
	public static final double meters_to_nm           (double meters)     { return meters * nm_per_meter;         }
	public static final double meters_to_um           (double meters)     { return meters * um_per_meter;         }
	public static final double meters_to_mm           (double meters)     { return meters * mm_per_meter;         }
	public static final double meters_to_cm           (double meters)     { return meters * cm_per_meter;         }
	public static final double meters_to_km           (double meters)     { return meters * km_per_meter;         }
	public static final double meters_to_inches       (double meters)     { return meters * inches_per_meter;     }
	public static final double meters_to_feet         (double meters)     { return meters * feet_per_meter;       }
	public static final double meters_to_yards        (double meters)     { return meters * yards_per_meter;      }
	public static final double meters_to_miles        (double meters)     { return meters * miles_per_meter;      }
	public static final double meters_to_aus          (double meters)     { return meters * aus_per_meter;        }
	public static final double meters_to_lightyears   (double meters)     { return meters * lightyears_per_meter; }
	public static final double meters_to_parsec       (double meters)     { return meters * parsecs_per_meter;    }
	public static final double meters_to_kly          (double meters)     { return meters * kly_per_meter;        }
	public static final double meters_to_kpc          (double meters)     { return meters * kpc_per_meter;        }
	public static final double meters_to_mly          (double meters)     { return meters * mly_per_meter;        }
	public static final double meters_to_mpc          (double meters)     { return meters * mpc_per_meter;        }

	public static final double km_to_angstroms        (double km)         { return km        * angstroms_per_km;  }
	public static final double km_to_nm               (double km)         { return km        * nm_per_km;         }
	public static final double km_to_um               (double km)         { return km        * um_per_km;         }
	public static final double km_to_mm               (double km)         { return km        * mm_per_km;         }
	public static final double km_to_cm               (double km)         { return km        * cm_per_km;         }
	public static final double km_to_meters           (double km)         { return km        * meters_per_km;     }
	public static final double km_to_inches           (double km)         { return km        * inches_per_km;     }
	public static final double km_to_feet             (double km)         { return km        * feet_per_km;       }
	public static final double km_to_yards            (double km)         { return km        * yards_per_km;      }
	public static final double km_to_miles            (double km)         { return km        * miles_per_km;      }
	public static final double km_to_aus              (double km)         { return km        * aus_per_km;        }
	public static final double km_to_lightyears       (double km)         { return km        * lightyears_per_km; }
	public static final double km_to_parsec           (double km)         { return km        * parsecs_per_km;    }
	public static final double km_to_kly              (double km)         { return km        * kly_per_km;        }
	public static final double km_to_kpc              (double km)         { return km        * kpc_per_km;        }
	public static final double km_to_mly              (double km)         { return km        * mly_per_km;        }
	public static final double km_to_mpc              (double km)         { return km        * mpc_per_km;        }

	public static final double inches_to_angstroms    (double inches)     { return inches * angstroms_per_inch;  }
	public static final double inches_to_nm           (double inches)     { return inches * nm_per_inch;         }
	public static final double inches_to_um           (double inches)     { return inches * um_per_inch;         }
	public static final double inches_to_mm           (double inches)     { return inches * mm_per_inch;         }
	public static final double inches_to_cm           (double inches)     { return inches * cm_per_inch;         }
	public static final double inches_to_meters       (double inches)     { return inches * meters_per_inch;     }
	public static final double inches_to_km           (double inches)     { return inches * km_per_inch;         }
	public static final double inches_to_feet         (double inches)     { return inches * feet_per_inch;       }
	public static final double inches_to_yards        (double inches)     { return inches * yards_per_inch;      }
	public static final double inches_to_miles        (double inches)     { return inches * miles_per_inch;      }
	public static final double inches_to_aus          (double inches)     { return inches * aus_per_inch;        }
	public static final double inches_to_lightyears   (double inches)     { return inches * lightyears_per_inch; }
	public static final double inches_to_parsec       (double inches)     { return inches * parsecs_per_inch;    }
	public static final double inches_to_kly          (double inches)     { return inches * kly_per_inch;        }
	public static final double inches_to_mpc          (double inches)     { return inches * mpc_per_inch;        }

	public static final double feet_to_angstroms      (double feet)       { return feet * angstroms_per_foot;    }
	public static final double feet_to_nm             (double feet)       { return feet * nm_per_foot;           }
	public static final double feet_to_um             (double feet)       { return feet * um_per_foot;           }
	public static final double feet_to_mm             (double feet)       { return feet * mm_per_foot;           }
	public static final double feet_to_cm             (double feet)       { return feet * cm_per_foot;           }
	public static final double feet_to_meters         (double feet)       { return feet * meters_per_foot;       }
	public static final double feet_to_km             (double feet)       { return feet * km_per_foot;           }
	public static final double feet_to_inches         (double feet)       { return feet * inches_per_foot;       }
	public static final double feet_to_yards          (double feet)       { return feet * yards_per_foot;        }
	public static final double feet_to_miles          (double feet)       { return feet * miles_per_foot;        }
	public static final double feet_to_aus            (double feet)       { return feet * aus_per_foot;          }
	public static final double feet_to_lightyears     (double feet)       { return feet * lightyears_per_foot;   }
	public static final double feet_to_parsec         (double feet)       { return feet * parsecs_per_foot;      }
	public static final double feet_to_kly            (double feet)       { return feet * kly_per_foot;          }
	public static final double feet_to_kpc            (double feet)       { return feet * kpc_per_foot;          }
	public static final double feet_to_mly            (double feet)       { return feet * mly_per_foot;          }
	public static final double feet_to_mpc            (double feet)       { return feet * mpc_per_foot;          }

	public static final double yards_to_angstroms     (double yards)      { return yards * angstroms_per_yard;   }
	public static final double yards_to_nm            (double yards)      { return yards * nm_per_yard;          }
	public static final double yards_to_um            (double yards)      { return yards * um_per_yard;          }
	public static final double yards_to_mm            (double yards)      { return yards * mm_per_yard;          }
	public static final double yards_to_cm            (double yards)      { return yards * cm_per_yard;          }
	public static final double yards_to_meters        (double yards)      { return yards * meters_per_yard;      }
	public static final double yards_to_km            (double yards)      { return yards * km_per_yard;          }
	public static final double yards_to_inches        (double yards)      { return yards * inches_per_yard;      }
	public static final double yards_to_feet          (double yards)      { return yards * feet_per_yard;        }
	public static final double yards_to_miles         (double yards)      { return yards * miles_per_yard;       }
	public static final double yards_to_aus           (double yards)      { return yards * aus_per_yard;         }
	public static final double yards_to_lightyears    (double yards)      { return yards * lightyears_per_yard;  }
	public static final double yards_to_parsec        (double yards)      { return yards * parsecs_per_yard;     }
	public static final double yards_to_kly           (double yards)      { return yards * kly_per_yard;         }
	public static final double yards_to_kpc           (double yards)      { return yards * kpc_per_yard;         }
	public static final double yards_to_mly           (double yards)      { return yards * mly_per_yard;         }
	public static final double yards_to_mpc           (double yards)      { return yards * mpc_per_yard;         }

	public static final double miles_to_angstroms     (double miles)      { return miles * angstroms_per_mile;   }
	public static final double miles_to_nm            (double miles)      { return miles * nm_per_mile;          }
	public static final double miles_to_um            (double miles)      { return miles * um_per_mile;          }
	public static final double miles_to_mm            (double miles)      { return miles * mm_per_mile;          }
	public static final double miles_to_cm            (double miles)      { return miles * cm_per_mile;          }
	public static final double miles_to_meters        (double miles)      { return miles * meters_per_mile;      }
	public static final double miles_to_km            (double miles)      { return miles * km_per_mile;          }
	public static final double miles_to_inches        (double miles)      { return miles * inches_per_mile;      }
	public static final double miles_to_feet          (double miles)      { return miles * feet_per_mile;        }
	public static final double miles_to_yards         (double miles)      { return miles * yards_per_mile;       }
	public static final double miles_to_aus           (double miles)      { return miles * aus_per_mile;         }
	public static final double miles_to_lightyears    (double miles)      { return miles * lightyears_per_mile;  }
	public static final double miles_to_parsec        (double miles)      { return miles * parsecs_per_mile;     }
	public static final double miles_to_kly           (double miles)      { return miles * kly_per_mile;         }
	public static final double miles_to_kpc           (double miles)      { return miles * kpc_per_mile;         }
	public static final double miles_to_mly           (double miles)      { return miles * mly_per_mile;         }
	public static final double miles_to_mpc           (double miles)      { return miles * mpc_per_mile;         }

	public static final double aus_to_angstroms       (double aus)        { return aus * angstroms_per_au;       }
	public static final double aus_to_nm              (double aus)        { return aus * nm_per_au;              }
	public static final double aus_to_um              (double aus)        { return aus * um_per_au;              }
	public static final double aus_to_mm              (double aus)        { return aus * mm_per_au;              }
	public static final double aus_to_cm              (double aus)        { return aus * cm_per_au;              }
	public static final double aus_to_meters          (double aus)        { return aus * meters_per_au;          }
	public static final double aus_to_km              (double aus)        { return aus * km_per_au;              }
	public static final double aus_to_inches          (double aus)        { return aus * inches_per_au;          }
	public static final double aus_to_feet            (double aus)        { return aus * feet_per_au;            }
	public static final double aus_to_yards           (double aus)        { return aus * yards_per_au;           }
	public static final double aus_to_miles           (double aus)        { return aus * miles_per_au;           }
	public static final double aus_to_lightyears      (double aus)        { return aus * lightyears_per_au;      }
	public static final double aus_to_parsec          (double aus)        { return aus * parsecs_per_au;         }
	public static final double aus_to_kly             (double aus)        { return aus * kly_per_au;             }
	public static final double aus_to_kpc             (double aus)        { return aus * kpc_per_au;             }
	public static final double aus_to_mly             (double aus)        { return aus * mly_per_au;             }
	public static final double aus_to_mpc             (double aus)        { return aus * mpc_per_au;             }

	public static final double lightyears_to_angstroms(double lightyears) { return lightyears * angstroms_per_lightyear;  }
	public static final double lightyears_to_nm       (double lightyears) { return lightyears * nm_per_lightyear;         }
	public static final double lightyears_to_um       (double lightyears) { return lightyears * um_per_lightyear;         }
	public static final double lightyears_to_mm       (double lightyears) { return lightyears * mm_per_lightyear;         }
	public static final double lightyears_to_cm       (double lightyears) { return lightyears * cm_per_lightyear;         }
	public static final double lightyears_to_meters   (double lightyears) { return lightyears * meters_per_lightyear;     }
	public static final double lightyears_to_km       (double lightyears) { return lightyears * km_per_lightyear;         }
	public static final double lightyears_to_inches   (double lightyears) { return lightyears * inches_per_lightyear;     }
	public static final double lightyears_to_feet     (double lightyears) { return lightyears * feet_per_lightyear;       }
	public static final double lightyears_to_yards    (double lightyears) { return lightyears * yards_per_lightyear;      }
	public static final double lightyears_to_miles    (double lightyears) { return lightyears * miles_per_lightyear;      }
	public static final double lightyears_to_aus      (double lightyears) { return lightyears * aus_per_lightyear;        }
	public static final double lightyears_to_parsec   (double lightyears) { return lightyears * parsecs_per_lightyear;    }
	public static final double lightyears_to_kly      (double lightyears) { return lightyears * kly_per_lightyear;        }
	public static final double lightyears_to_kpc      (double lightyears) { return lightyears * kpc_per_lightyear;        }
	public static final double lightyears_to_mly      (double lightyears) { return lightyears * mly_per_lightyear;        }
	public static final double lightyears_to_mpc      (double lightyears) { return lightyears * mpc_per_lightyear;        }

	public static final double parsecs_to_angstroms   (double parsecs)    { return parsecs * angstroms_per_parsec;  }
	public static final double parsecs_to_nm          (double parsecs)    { return parsecs * nm_per_parsec;         }
	public static final double parsecs_to_um          (double parsecs)    { return parsecs * um_per_parsec;         }
	public static final double parsecs_to_mm          (double parsecs)    { return parsecs * mm_per_parsec;         }
	public static final double parsecs_to_cm          (double parsecs)    { return parsecs * cm_per_parsec;         }
	public static final double parsecs_to_meters      (double parsecs)    { return parsecs * meters_per_parsec;     }
	public static final double parsecs_to_km          (double parsecs)    { return parsecs * km_per_parsec;         }
	public static final double parsecs_to_inches      (double parsecs)    { return parsecs * inches_per_parsec;     }
	public static final double parsecs_to_feet        (double parsecs)    { return parsecs * feet_per_parsec;       }
	public static final double parsecs_to_yards       (double parsecs)    { return parsecs * yards_per_parsec;      }
	public static final double parsecs_to_miles       (double parsecs)    { return parsecs * miles_per_parsec;      }
	public static final double parsecs_to_aus         (double parsecs)    { return parsecs * aus_per_parsec;        }
	public static final double parsecs_to_lightyears  (double parsecs)    { return parsecs * lightyears_per_parsec; }
	public static final double parsecs_to_kly         (double parsecs)    { return parsecs * kly_per_parsec;        }
	public static final double parsecs_to_kpc         (double parsecs)    { return parsecs * kpc_per_parsec;        }
	public static final double parsecs_to_mly         (double parsecs)    { return parsecs * mly_per_parsec;        }
	public static final double parsecs_to_mpc         (double parsecs)    { return parsecs * mpc_per_parsec;        }

	public static final double kly_to_angstroms       (double kly)        { return kly * angstroms_per_kly;  }
	public static final double kly_to_nm              (double kly)        { return kly * nm_per_kly;         }
	public static final double kly_to_um              (double kly)        { return kly * um_per_kly;         }
	public static final double kly_to_mm              (double kly)        { return kly * mm_per_kly;         }
	public static final double kly_to_cm              (double kly)        { return kly * cm_per_kly;         }
	public static final double kly_to_meters          (double kly)        { return kly * meters_per_kly;     }
	public static final double kly_to_km              (double kly)        { return kly * km_per_kly;         }
	public static final double kly_to_inches          (double kly)        { return kly * inches_per_kly;     }
	public static final double kly_to_feet            (double kly)        { return kly * feet_per_kly;       }
	public static final double kly_to_yards           (double kly)        { return kly * yards_per_kly;      }
	public static final double kly_to_miles           (double kly)        { return kly * miles_per_kly;      }
	public static final double kly_to_aus             (double kly)        { return kly * aus_per_kly;        }
	public static final double kly_to_lightyears      (double kly)        { return kly * lightyears_per_kly; }
	public static final double kly_to_parsec          (double kly)        { return kly * parsecs_per_kly;    }
	public static final double kly_to_kpc             (double kly)        { return kly * kpc_per_kly;        }
	public static final double kly_to_mly             (double kly)        { return kly * mly_per_kly;        }
	public static final double kly_to_mpc             (double kly)        { return kly * mpc_per_kly;        }

	public static final double kpc_to_angstroms       (double kpc)        { return kpc * angstroms_per_kpc;  }
	public static final double kpc_to_nm              (double kpc)        { return kpc * nm_per_kpc;         }
	public static final double kpc_to_um              (double kpc)        { return kpc * um_per_kpc;         }
	public static final double kpc_to_mm              (double kpc)        { return kpc * mm_per_kpc;         }
	public static final double kpc_to_cm              (double kpc)        { return kpc * cm_per_kpc;         }
	public static final double kpc_to_meters          (double kpc)        { return kpc * meters_per_kpc;     }
	public static final double kpc_to_km              (double kpc)        { return kpc * km_per_kpc;         }
	public static final double kpc_to_inches          (double kpc)        { return kpc * inches_per_kpc;     }
	public static final double kpc_to_feet            (double kpc)        { return kpc * feet_per_kpc;       }
	public static final double kpc_to_yards           (double kpc)        { return kpc * yards_per_kpc;      }
	public static final double kpc_to_miles           (double kpc)        { return kpc * miles_per_kpc;      }
	public static final double kpc_to_aus             (double kpc)        { return kpc * aus_per_kpc;        }
	public static final double kpc_to_lightyears      (double kpc)        { return kpc * lightyears_per_kpc; }
	public static final double kpc_to_parsec          (double kpc)        { return kpc * parsecs_per_kpc;    }
	public static final double kpc_to_kly             (double kpc)        { return kpc * kly_per_kpc;        }
	public static final double kpc_to_mly             (double kpc)        { return kpc * mly_per_kpc;        }
	public static final double kpc_to_mpc             (double kpc)        { return kpc * mpc_per_kpc;        }

	public static final double mly_to_angstroms       (double mly)        { return mly * angstroms_per_mly;  }
	public static final double mly_to_nm              (double mly)        { return mly * nm_per_mly;         }
	public static final double mly_to_um              (double mly)        { return mly * um_per_mly;         }
	public static final double mly_to_mm              (double mly)        { return mly * mm_per_mly;         }
	public static final double mly_to_cm              (double mly)        { return mly * cm_per_mly;         }
	public static final double mly_to_meters          (double mly)        { return mly * meters_per_mly;     }
	public static final double mly_to_km              (double mly)        { return mly * km_per_mly;         }
	public static final double mly_to_inches          (double mly)        { return mly * inches_per_mly;     }
	public static final double mly_to_feet            (double mly)        { return mly * feet_per_mly;       }
	public static final double mly_to_yards           (double mly)        { return mly * yards_per_mly;      }
	public static final double mly_to_miles           (double mly)        { return mly * miles_per_mly;      }
	public static final double mly_to_aus             (double mly)        { return mly * aus_per_mly;        }
	public static final double mly_to_lightyears      (double mly)        { return mly * lightyears_per_mly; }
	public static final double mly_to_parsec          (double mly)        { return mly * parsecs_per_mly;    }
	public static final double mly_to_kly             (double mly)        { return mly * kly_per_mly;        }
	public static final double mly_to_kpc             (double mly)        { return mly * kpc_per_mly;        }
	public static final double mly_to_mpc             (double mly)        { return mly * mpc_per_mly;        }

	public static final double mpc_to_angstroms       (double mpc)        { return mpc * angstroms_per_mpc;  }
	public static final double mpc_to_nm              (double mpc)        { return mpc * nm_per_mpc;         }
	public static final double mpc_to_um              (double mpc)        { return mpc * um_per_mpc;         }
	public static final double mpc_to_mm              (double mpc)        { return mpc * mm_per_mpc;         }
	public static final double mpc_to_cm              (double mpc)        { return mpc * cm_per_mpc;         }
	public static final double mpc_to_meters          (double mpc)        { return mpc * meters_per_mpc;     }
	public static final double mpc_to_km              (double mpc)        { return mpc * km_per_mpc;         }
	public static final double mpc_to_inches          (double mpc)        { return mpc * inches_per_mpc;     }
	public static final double mpc_to_feet            (double mpc)        { return mpc * feet_per_mpc;       }
	public static final double mpc_to_yards           (double mpc)        { return mpc * yards_per_mpc;      }
	public static final double mpc_to_miles           (double mpc)        { return mpc * miles_per_mpc;      }
	public static final double mpc_to_aus             (double mpc)        { return mpc * aus_per_mpc;        }
	public static final double mpc_to_lightyears      (double mpc)        { return mpc * lightyears_per_mpc; }
	public static final double mpc_to_parsec          (double mpc)        { return mpc * parsecs_per_mpc;    }
	public static final double mpc_to_kly             (double mpc)        { return mpc * kly_per_mpc;        }
	public static final double mpc_to_kpc             (double mpc)        { return mpc * kpc_per_mpc;        }
	public static final double mpc_to_mly             (double mpc)        { return mpc * mly_per_mpc;        }

	// LCT = Local Civil Time
	// LMT = Local Mean Time
	// UT  = Universal Time
	// GMT = Greenwich Mean Time
	// UTC = Universal Time Coordinated
	// TT  = Terrestrial Time
	// GST = Greenwich Sidereal Time
	// LST = Local Sidereal Time
	// AST = Apparent Sidereal Time
	// MST = Mean Sidereal Time
	
	public static final double mean_solar_days_per_tropical_year     = 365.242191;		// equinox to equinox
	public static final double mean_solar_days_per_sidereal_year     = 365.2564;		// reference star to reference star
	public static final double mean_solar_days_per_anomalistic_year  = 365.2596;		// earth is closest to the sun
	public static final double mean_solar_days_per_draconic_year     = 346.6201;		// useful in predicting eclipses
	public static final double mean_solar_days_per_julian_year       = 365.2500;		// julian years
	public static final double mean_solar_days_per_gregorian_year    = 365.2425;		// gregorian years
	public static final double mean_solar_days_per_gregorian_century = 100 * mean_solar_days_per_gregorian_year;
	public static final double mean_solar_days_per_julian_century    = 100 * mean_solar_days_per_julian_year;
	
	public static final double hours_per_day       = 24.0;
	public static final double minutes_per_hour    = 60.0;
	public static final double seconds_per_minute  = 60.0;
	public static final double millisec_per_second = 1000.0;
	public static final double millisec_per_minute = millisec_per_second * seconds_per_minute;
	public static final double millisec_per_hour   = millisec_per_minute * minutes_per_hour;
	public static final double millisec_per_day    = millisec_per_hour   * hours_per_day;
	public static final double seconds_per_hour    = seconds_per_minute  * minutes_per_hour;
	public static final double seconds_per_day     = seconds_per_hour    * hours_per_day;
	public static final double minutes_per_day     = minutes_per_hour    * hours_per_day;

	public static final double minutes_per_degree  = 60.0;
	public static final double seconds_per_degree  = seconds_per_minute  * minutes_per_degree;
	public static final double millisec_per_degree = millisec_per_second * seconds_per_degree;
	
	public static final class DMS {
		public final boolean positive;
		public final int     degree;
		public final int     minute;
		public final double  second;
		public final double  decimal_degrees;
		public final double  radians;
		
		public DMS(int d, int m, double s)
		{
			decimal_degrees = dms_to_decimal_degrees(d, m, s);

			positive = (0 < decimal_degrees);
			degree  = degree_of_decimal_degrees(decimal_degrees);
			minute  = minute_of_decimal_degrees(decimal_degrees);
			second  = second_of_decimal_degrees(decimal_degrees);
			radians = Math.PI * decimal_degrees / 180;
		}

		public static DMS ddeg_to_dms(double ddeg)
		{
			int    d = degree_of_decimal_degrees(ddeg);
			int    m = minute_of_decimal_degrees(ddeg);
			double s = second_of_decimal_degrees(ddeg);
			
			return new DMS(d, m, s);			
		}

		public static DMS dhrs_to_dms(double dhrs)
		{
			double ddeg = dhrs * degrees_per_hour;
			
			return ddeg_to_dms(ddeg);
		}

		public final double to_decimal_degrees()
		{
			return decimal_degrees;
		}

		public final String toString()
		{
			return decimal_degrees_to_string(decimal_degrees);
		}
	}

	public static final double dms_to_decimal_degrees(double degrees, double minutes, double seconds)
	{
		boolean sign = (degrees < 0) || (degrees == 0 && minutes < 0) || (degrees == 0 && minutes == 0 && seconds < 0);
		double  deg  = (sign ? -1 : 1) * (Math.abs(degrees) + (Math.abs(minutes) / minutes_per_degree) + (Math.abs(seconds) / seconds_per_degree));

		return deg;
	}

	public static final double dms_to_decimal_degrees(boolean sign, double degrees, double minutes, double seconds)
	{
		double  deg  = (sign ? -1 : 1) * (Math.abs(degrees) + (Math.abs(minutes) / minutes_per_degree) + (Math.abs(seconds) / seconds_per_degree));

		return deg;
	}

	public static final int degree_of_decimal_degrees(double decimal_degrees)
	{
		return (int) decimal_degrees;
	}

	public static final int minute_of_decimal_degrees(double decimal_degrees)
	{
		return (int) ((int)(minutes_per_degree * decimal_degrees) % minutes_per_degree);
	}

	public static final double second_of_decimal_degrees(double decimal_degrees)
	{
		return seconds_per_minute * Round.FRAC(minutes_per_hour * decimal_degrees);
	}

	public static final double decimal_degrees_to_radians(double decimal_degrees)
	{
		return decimal_degrees * Math.PI / 180;
	}

	public static final double radians_to_decimal_degrees(double radians)
	{
		return 180 * radians / Math.PI;
	}

	public static final double decimal_degrees_to_hours(double decimal_degrees)
	{
		return decimal_degrees / degrees_per_hour;
	}

	
	// TODO
	public static final String degree_symbol = new String(Character.toChars(0x00B0));
	public static final String decimal_degrees_to_string(double decimal_degrees)
	{
		if (0 <= decimal_degrees) {
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millisec_per_degree) / millisec_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			int s = (int) second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%03d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%02d%s", s, "\"");
			return String.format("+%s %s %s", ds, ms, ss);
		} else {
			decimal_degrees = - decimal_degrees;
			decimal_degrees = Round.round_to_nearest(decimal_degrees * millisec_per_degree) / millisec_per_degree;
			int d = degree_of_decimal_degrees(decimal_degrees);
			int m = minute_of_decimal_degrees(decimal_degrees);
			int s = (int) second_of_decimal_degrees(decimal_degrees);
			String ds = String.format("%03d%s", d, degree_symbol);
			String ms = String.format("%02d%s", m, "'");
			String ss = String.format("%02d%s", s, "\"");
			return String.format("-%s %s %s", ds, ms, ss);
		}
	}
	
	public static final class HMS {
		public final boolean positive;
		public final int     hour;
		public final int     minute;
		public final double  second;
		public final double  decimal_hours;
		public final double  decimal_degrees;
		public final double  radians;

		public HMS(double h, double m, double s)
		{
			decimal_hours = hms_to_decimal_hours(h, m, s);

			positive = (0 < decimal_hours);
			hour     = hour_of_decimal_hours(decimal_hours);
			minute   = minute_of_decimal_hours(decimal_hours);
			second   = second_of_decimal_hours(decimal_hours);
			decimal_degrees = (positive ? +1 : -1) * (hour * degrees_per_hour + minute / minutes_per_degree + second / seconds_per_degree);
			radians  = Math.PI * decimal_degrees / 180;
		}

		public static HMS decimal_degrees_to_hms(double ddeg)
		{
			double dhrs = ddeg / degrees_per_hour;

			return decimal_hours_to_hms(dhrs);			
		}

		public static HMS decimal_hours_to_hms(double dhrs)
		{
			int    h = hour_of_decimal_hours(dhrs);
			int    m = minute_of_decimal_hours(dhrs);
			double s = second_of_decimal_hours(dhrs);
			
			return new HMS(h, m, s);			
		}

		public final double to_decimal_hours()
		{
			return hms_to_decimal_hours(hour, minute, second);
		}

		public final String toString()
		{
			return hms_to_string(hour, minute, second);
		}
	}

	public static final double hms_to_decimal_hours(double hours, double minutes, double seconds)
	{
		boolean sign = (hours < 0) || (hours == 0 && minutes < 0) || (hours == 0 && minutes == 0 && seconds < 0);
		double hrs = (sign ? -1 : 1) * (Math.abs(hours) + (Math.abs(minutes) / minutes_per_hour) + (Math.abs(seconds) / seconds_per_hour));

		// System.out.printf("(%f,%05.2f,%05.2f)=%.2f%n", hours, Math.abs(minutes) / minutes_per_hour, Math.abs(seconds) / seconds_per_hour, hrs);

		return hrs;
	}

	// TODO
	public static final String hms_to_string(int hour, int minute, double second)
	{
		String str = null;
		
		if (hour < 0 || (hour == 0 && minute < 0) || (hour == 0 && minute == 0 && second < 0)) {
			str = String.format("-%2dh %02dm %02.0fs", Math.abs(hour), Math.abs(minute), Math.abs(second));
		} else if (hour == 0 && minute == 0 && second == 0) {
			str = String.format(" %02dh %02dm %02.0fs", hour, minute, second);
		} else {
			str = String.format("+%02dh %02dm %02.0fs", Math.abs(hour), Math.abs(minute), Math.abs(second));
		}

		return str;
	}

	public static final double decimal_hours_to_fraction_of_day(double hours)
	{
		return hours / hours_per_day;
	}

	public static final double fraction_of_day_to_decimal_hours(double fraction_of_day)
	{
		return fraction_of_day * hours_per_day;
	}

	public static final double decimal_hours_to_degrees(double hours)
	{
		return hours * degrees_per_hour;
	}

	public static final double decimal_hours_to_radians(double hours)
	{
		return decimal_degrees_to_radians(decimal_hours_to_degrees(hours));
	}

	public static final double radians_to_decimal_hours(double radians)
	{
		return decimal_degrees_to_hours(radians_to_decimal_degrees(radians));
	}

	public static final int hour_of_decimal_hours(double decimal_hours)
	{
		return (int) decimal_hours;
	}

	public static final int minute_of_decimal_hours(double decimal_hours)
	{
		return (int) (minutes_per_hour * Round.FRAC(decimal_hours));
	}

	public static final double second_of_decimal_hours(double decimal_hours)
	{
		return seconds_per_minute * Round.FRAC(minutes_per_hour * decimal_hours);
	}
	
	// TODO
	public static final String decimal_hours_to_string(double decimal_hours)
	{
		if (0 <= decimal_hours) {
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millisec_per_hour) / millisec_per_hour;
			int h = hour_of_decimal_hours(decimal_hours);
			int m = minute_of_decimal_degrees(decimal_hours);
			int s = (int) second_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%02d%s", s, "s");

			return hs + " " + ms + " " + ss;
		} else {
			decimal_hours += 24;
			decimal_hours = Round.round_to_neg_inf(decimal_hours * millisec_per_hour) / millisec_per_hour;
			int h = hour_of_decimal_hours(decimal_hours);
			int m = minute_of_decimal_degrees(decimal_hours);
			int s = (int) second_of_decimal_degrees(decimal_hours);
			String hs = String.format("%02d%s", h, "h");
			String ms = String.format("%02d%s", m, "m");
			String ss = String.format("%02d%s", s, "s");

			return hs + " " + ms + " " + ss;
		}
	}

	public static boolean is_leap_year(int year)
	{
		return (year%4 == 0 && year%100 != 0) || (year%400 == 0);
	}

	public static boolean is_gregorian(int year, int month, int day_of_month)
	{
		return (1582 < year) || (year == 1582 && 10 < month) || (year == 1582 && month == 10 && day_of_month <= 15);
	}


	public static class Date {
		public final int    year;
		public final int    month;
		public final int    day_of_month; 
		public final double decimal_hour; 
		public final int    hour; 
		public final int    minute; 
		public final double second;

		public Date(int y, int M, int d, int h, int m, double s)
		{
			year         = y;
			month        = M;
			day_of_month = d;
			hour         = h;
			minute       = m;
			second       = s;
			
			decimal_hour = hms_to_decimal_hours(hour, minute, second);
		}

		public Date(int y, int M, int d, double decimal_hour)
		{
			year         = y;
			month        = M;
			day_of_month = d;
			hour         = hour_of_decimal_hours  (decimal_hour);
			minute       = minute_of_decimal_hours(decimal_hour);
			second       = second_of_decimal_hours(decimal_hour);
			
			this.decimal_hour = decimal_hour;
		}

		public String toString()
		{
			return String.format("%04d-%02d-%02d %02d:%02d:%06.3f", year, month, day_of_month, hour, minute, second);
		}
	}

	// month        is the month of the year, where 1 <= month <= 12, where 1 is January and 12 is December
	// day_of_month is the day of the month, where 1 <= day <= {28, 29, 30, 31} according to the month
	// year         is the calendar year, with AD dates being positive, BCE dates being negative
	// hour, etc.   are the local time expressed as a 24 hour clock, with only the hour carrying the sign of the time
	public static final double calendar_date_to_julian_day(int year, int month, int day_of_month, double hour, double minute, double second)
	{
		double decimal_hours   = hms_to_decimal_hours(hour, minute, second);
		double fraction_of_day = decimal_hours_to_fraction_of_day(decimal_hours);
		double day             = day_of_month + fraction_of_day;

		double m = (2 < month) ? month : month + 12;
		double y = (2 < month) ? year  : year  -  1;
		double T = (year  < 0) ? 0.75  : 0;
		
		boolean is_gregorian = is_gregorian(year, month, day_of_month);

		double A = (is_gregorian) ? Round.FIX(y/100)       : 0;
		double B = (is_gregorian) ? 2 - A + Round.fix(A/4) : 0;
		
		double JD = B + Round.FIX(365.25 * y - T) + Round.FIX(30.6001 * (m + 1)) + day + 1720994.5;

		return JD;
	}


	public static final double calendar_date_to_julian_day(Date date)
	{
		return calendar_date_to_julian_day((int) date.year, (int) date.month, (int) date.day_of_month, (int) date.hour, (int) date.minute, date.second);
	}

	public static final Date julian_day_to_calendar_date(double julian_day)
	{
		double JD1 = julian_day + 0.5;
		double I   = Round.fix(JD1);
		double F   = Round.FRAC(JD1);
		double A   = (2299160 < I) ? Round.fix((I - 1867216.25) / mean_solar_days_per_gregorian_century) : 0;
		double B   = (2299160 < I) ? I + 1 + A - Round.FIX(A / 4.0)                                      : I;
		double C   = B + 1524;
		double D   = Round.FIX((C - 122.1) / mean_solar_days_per_julian_year);
		double E   = Round.FIX(mean_solar_days_per_julian_year * D);
		double G   = Round.FIX((C - E) / 30.6001);
		
		double day   = C - E + F - Round.FIX(30.6001 * G);
		double month = (G   <  13.5) ? G -    1 : G -   13 ;
		double year  = (2.5 < month) ? D - 4716 : D - 4715 ;

		double decimal = fraction_of_day_to_decimal_hours(F);
		double hours   = Round.INT(decimal);
		double minutes = Round.FRAC(decimal) * minutes_per_hour;
		double seconds = Round.FRAC(minutes) * seconds_per_minute;

		return new Date((int) year, (int) month, (int) day, (int) hours, (int) minutes, seconds);
	}

	public static final int calendar_date_to_days_into_the_year(int year, int month, int day_of_month)
	{
		int T = (is_leap_year(year)) ? 1 : 2;
		
		return (int) (Round.FIX((275.0 * month) / 9.0) - T * Round.FIX((month + 9.0) / 12.0) + day_of_month - 30);
	}

	public static final int calendar_date_to_days_into_the_year(Date date)
	{
		return calendar_date_to_days_into_the_year(date.year, date.month, date.day_of_month);
	}

	public static final Date days_into_the_year_to_calendar_date(int days_into_the_year, int year, int hour, int minute, double second)
	{
		int N = days_into_the_year;
		double A = (is_leap_year(year)) ? 1523 : 1889;
		double B = Round.FIX((N + A - 122.1) / mean_solar_days_per_julian_year);
		double C = N + A - Round.FIX(B * mean_solar_days_per_julian_year);
		double E = Round.FIX(C / 30.6001);
		
		double month = (E < 13.5) ? E - 1 : E - 13;
		double day   = C - Round.FIX(30.6001 * E);

		return new Date(year, (int) month, (int) day, hour, minute, second);
	}

	public static final Date days_into_the_year_to_calendar_date(int days_into_the_year, int year)
	{
		return days_into_the_year_to_calendar_date(days_into_the_year, year, 0, 0, 0);
	}

	public static final int day_of_the_week(int year, int month, int day_of_month)
	{
		double JD =	calendar_date_to_julian_day(year, month, day_of_month, 0, 0, 0);
		double A  = (JD + 1.5) / 7;
		double B  = 7 * Round.FRAC(A);
		double N  = Round.ROUND(B);

		return (int) N;
	}

	public static final String[] weekdays = { "N/A", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
	public static final String day_of_the_week_to_string(int day_of_the_week)
	{
		return weekdays[1 + ((day_of_the_week - 1) % 7)];
	}

	// UTC offset is calculated for the local time zone and current date
	// result is in decimal hours
	public static final double local_civil_time_to_universal_time(double decimal_hour)
	{
		TimeZone timezone = TimeZone.getDefault();
		Calendar c = Calendar.getInstance(timezone);
		int year         = c.get(Calendar.YEAR);
		int month        = c.get(Calendar.MONTH);
		int day_of_month = c.get(Calendar.DAY_OF_MONTH);
		int hour         = hour_of_decimal_hours(decimal_hour);
		int minute       = minute_of_decimal_hours(decimal_hour);
		int second       = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month, day_of_month, hour, minute, second);
		double offset = timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour;
		double universal_time = decimal_hour - offset;

		return universal_time;
	}

	public static final double LCT_to_UT(double decimal_hour)
	{
		return local_civil_time_to_universal_time(decimal_hour);
	}

	public static final double local_civil_time_to_universal_time(long time_in_ms, TimeZone timezone)
	{
		Calendar c = Calendar.getInstance(timezone);
		c.setTimeInMillis(time_in_ms);
		double decimal_hour = (double) c.get(Calendar.HOUR_OF_DAY) + 
				(double) c.get(Calendar.MINUTE) / minutes_per_hour + 
				(double) c.get(Calendar.SECOND) / seconds_per_hour + 
				(double) c.get(Calendar.MILLISECOND) / millisec_per_hour;

		double offset = timezone.getOffset(time_in_ms) / millisec_per_hour;
		double universal_time = decimal_hour - offset;

		return universal_time;
	}

	public static final double LCT_to_UT(long time_in_ms, TimeZone timezone)
	{
		return local_civil_time_to_universal_time(time_in_ms, timezone);
	}

	// UTC offset is calculated for the local time zone and specified date (January == 1, December == 12)
	// result is in decimal hours
	public static final double local_civil_time_to_universal_time(double decimal_hour, int year, int month, int day_of_month)
	{
		TimeZone timezone = TimeZone.getDefault();
		Calendar c = Calendar.getInstance(timezone);
		int hour   = hour_of_decimal_hours(decimal_hour);
		int minute = minute_of_decimal_hours(decimal_hour);
		int second = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month - 1, day_of_month, hour, minute, second);		// calendar assumes January == 0

		double offset = timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour;
		double universal_time = decimal_hour - offset;

		return universal_time;
	}

	public static final double LCT_to_UT(double decimal_hour, int year, int month, int day_of_month)
	{
		return local_civil_time_to_universal_time(decimal_hour, year, month, day_of_month);
	}

	// UTC offset is calculated for the specified time zone and current date
	// result is in decimal hours
	public static final double local_civil_time_to_universal_time(double decimal_hour, TimeZone timezone)
	{
		Calendar c       = Calendar.getInstance(timezone);
		int year         = c.get(Calendar.YEAR);
		int month        = c.get(Calendar.MONTH);
		int day_of_month = c.get(Calendar.DAY_OF_MONTH);
		int hour         = hour_of_decimal_hours(decimal_hour);
		int minute       = minute_of_decimal_hours(decimal_hour);
		int second       = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month, day_of_month, hour, minute, second);

		return decimal_hour + (timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour);
	}

	public static final double LCT_to_UT(double decimal_hour, TimeZone timezone)
	{
		return local_civil_time_to_universal_time(decimal_hour, timezone);
	}
	
	// UTC offset is calculated for the specified time zone and specified date (January == 1, December == 12)
	// result is in decimal hours
	public static final double local_civil_time_to_universal_time(double decimal_hour, int year, int month, int day_of_month, TimeZone timezone)
	{
		Calendar c = Calendar.getInstance(timezone);
		int hour   = hour_of_decimal_hours(decimal_hour);
		int minute = minute_of_decimal_hours(decimal_hour);
		int second = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month - 1, day_of_month, hour, minute, second);		// calendar assumes January == 0

		return decimal_hour + (timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour);
	}

	public static final double LCT_to_UT(double decimal_hour, int year, int month, int day_of_month, TimeZone timezone)
	{
		return local_civil_time_to_universal_time(decimal_hour, year, month, day_of_month, timezone);
	}

	public static final double universal_time_to_local_civil_time(double decimal_hour)
	{
		TimeZone timezone = TimeZone.getDefault();
		Calendar c = Calendar.getInstance(timezone);
		int year         = c.get(Calendar.YEAR);
		int month        = c.get(Calendar.MONTH);
		int day_of_month = c.get(Calendar.DAY_OF_MONTH);
		int hour         = hour_of_decimal_hours(decimal_hour);
		int minute       = minute_of_decimal_hours(decimal_hour);
		int second       = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month, day_of_month, hour, minute, second);

		return decimal_hour - (timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour);
	}

	public static final double UT_to_LCT(double decimal_hour)
	{
		return universal_time_to_local_civil_time(decimal_hour);
	}

	// UTC offset is calculated for the local time zone and specified date (January == 1, December == 12)
	// result is in decimal hours
	public static final double universal_time_to_local_civil_time(double decimal_hour, int year, int month, int day_of_month)
	{
		TimeZone timezone = TimeZone.getDefault();
		Calendar c = Calendar.getInstance(timezone);
		int hour   = hour_of_decimal_hours(decimal_hour);
		int minute = minute_of_decimal_hours(decimal_hour);
		int second = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month - 1, day_of_month, hour, minute, second);		// calendar assumes January == 0

		return decimal_hour - (timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour);
	}

	public static final double UT_to_LCT(double decimal_hour, int year, int month, int day_of_month)
	{
		return universal_time_to_local_civil_time(decimal_hour, year, month, day_of_month);
	}

	// UTC offset is calculated for the specified time zone and current date
	// result is in decimal hours
	public static final double universal_time_to_local_civil_time(double decimal_hour, TimeZone timezone)
	{
		Calendar c = Calendar.getInstance(timezone);
		int year         = c.get(Calendar.YEAR);
		int month        = c.get(Calendar.MONTH);
		int day_of_month = c.get(Calendar.DAY_OF_MONTH);
		int hour         = hour_of_decimal_hours(decimal_hour);
		int minute       = minute_of_decimal_hours(decimal_hour);
		int second       = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, month, day_of_month, hour, minute, second);

		return decimal_hour - (timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour);
	}

	public static final double UT_to_LCT(double decimal_hour, TimeZone timezone)
	{
		return universal_time_to_local_civil_time(decimal_hour, timezone);
	}
	
	// UTC offset is calculated for the specified time zone and specified date (January == 1, December == 12)
	// result is in decimal hours
	public static final double universal_time_to_local_civil_time(double decimal_hour, int year, int month, int day_of_month, TimeZone timezone)
	{
		Calendar c = Calendar.getInstance(timezone);
		int hour   = hour_of_decimal_hours(decimal_hour);
		int minute = minute_of_decimal_hours(decimal_hour);
		int second = (int) second_of_decimal_hours(decimal_hour);
		c.set(year, Calendar.JANUARY + month - 1, day_of_month, hour, minute, second);		// calendar assumes January == 0
		double offset = timezone.getOffset(c.getTimeInMillis()) / millisec_per_hour;
		double LCT = decimal_hour + offset;

		return LCT;
	}

	public static final double UT_to_LCT(double decimal_hour, int year, int month, int day_of_month, TimeZone timezone)
	{
		return universal_time_to_local_civil_time(decimal_hour, year, month, day_of_month, timezone);
	}


	public static final double universal_time_to_greenwich_sidereal_time(double decimal_hour, int year, int month, int day_of_month)
	{
		double JD  = calendar_date_to_julian_day(year, month, day_of_month, 0, 0, 0);
		double JD0 = calendar_date_to_julian_day(year-1, 12, 31, 0, 0, 0);
		double days = JD - JD0;
		double T    = (JD0 - 2415020) / mean_solar_days_per_julian_century;
		double R    = 6.6460656 + 2400.051262 * T + 0.00002581 * T * T;
		double B    = 24 - R + 24 * (year - 1900);
		double T0   = 0.0657098 * days - B;
		double UT   = decimal_hour;
		double GST  = T0 + 1.002738 * UT;
		GST += (GST <    0) ? 24 : 0;
		GST -= (24  <= GST) ? 24 : 0;

		return GST;
	}

	public static final double UT_to_GST(double decimal_hour, int year, int month, int day_of_month)
	{
		return universal_time_to_greenwich_sidereal_time(decimal_hour, year, month, day_of_month);
	}


	public static final double greenwich_sidereal_time_to_universal_time(double decimal_hour, int year, int month, int day_of_month)
	{
		double JD  = calendar_date_to_julian_day(year, month, day_of_month, 0, 0, 0);
		double JD0 = calendar_date_to_julian_day(year-1, 12, 31, 0, 0, 0);
		double days = JD - JD0;
		double T    = (JD0 - 2415020) / mean_solar_days_per_julian_century;
		double R    = 6.6460656 + 2400.051262 * T + 0.00002581 * T * T;
		double B    = 24 - R + 24 * (year - 1900);
		double T0   = 0.0657098 * days - B;
		T0  += (T0 <   0) ? 24 : 0;
		T0  -= (24 <= T0) ? 24 : 0;
		double GST  = decimal_hour;
		double A    = GST - T0;
		A += (A < 0) ? 24 : 0;
		double UT   = 0.997270 * A;

		return UT;
	}

	public static final double GST_to_UT(double decimal_hour, int year, int month, int day_of_month)
	{
		return greenwich_sidereal_time_to_universal_time(decimal_hour, year, month, day_of_month);
	}


	// longitude is in decimal degrees
	public static final double greenwich_sidereal_time_to_local_sidereal_time(double decimal_hour, double longitude)
	{
		double GST    = decimal_hour;
		double adjust = longitude / degrees_per_hour;
		double LST    = GST + adjust;
		LST += (LST <    0) ? 24 : 0;
		LST -= (24  <= LST) ? 24 : 0;
		
		return LST;
	}

	public static final double GST_to_LST(double decimal_hour, double longitude)
	{
		return greenwich_sidereal_time_to_local_sidereal_time(decimal_hour, longitude);
	}

	public static final double greenwich_sidereal_time_to_local_sidereal_time(double decimal_hour, Angle longitude)
	{
		double GST = decimal_hour;
		double adjust = longitude.degrees / 15.0;
		double LST = GST + adjust;
		LST += (LST <    0) ? 24 : 0;
		LST -= (24  <= LST) ? 24 : 0;
		
		return LST;
	}

	public static final double GST_to_LST(double decimal_hour, Angle longitude)
	{
		return greenwich_sidereal_time_to_local_sidereal_time(decimal_hour, longitude);
	}


	public static final double local_sidereal_time_to_greenwich_sidereal_time(double decimal_hour, Angle longitude)
	{
		double LST = decimal_hour;
		double adjust = longitude.degrees / 15.0;
		double GST = LST - adjust;
		LST += (LST <    0) ? 24 : 0;
		LST -= (24  <= LST) ? 24 : 0;
		
		return GST;
	}

	public static final double LST_to_GST(double decimal_hour, Angle longitude)
	{
		return local_sidereal_time_to_greenwich_sidereal_time(decimal_hour, longitude);
	}


	public static final double local_sidereal_time_to_greenwich_sidereal_time(double local_sidereal_time_hrs, double longitude)
	{
		double LST = local_sidereal_time_hrs;
		double adjust = decimal_degrees_to_hours( longitude );
		double GST = LST - adjust;
		LST += (LST <    0) ? 24 : 0;
		LST -= (24  <= LST) ? 24 : 0;
		
		return GST;
	}

	public static final double LST_to_GST(double local_sidereal_time_hrs, double longitude)
	{
		return local_sidereal_time_to_greenwich_sidereal_time(local_sidereal_time_hrs, longitude);
	}

	public static final double mean_anomaly_degrees(double time_since_perihelion, double orbital_period)
	{
		return (time_since_perihelion / orbital_period) * 360;
	}

	// angles are in radians
	public static final double mean_anomaly_radians(double time_since_perihelion, double orbital_period)
	{
		return (time_since_perihelion / orbital_period) * 2 * Math.PI;
	}

	public static final double equation_of_the_center_degrees(double mean_anomaly, double eccentricity)
	{
		double M = mean_anomaly * Math.PI / 180;
		double e = eccentricity;
		
		return 180 * equation_of_the_center_radians(M, e) / Math.PI;
	}

	// angles are in radians
	public static final double equation_of_the_center_radians(double mean_anomaly, double eccentricity)
	{
		double M = mean_anomaly;
		double e = eccentricity;
		
		return 2 * e * Math.sin(M) + (5 * e * e / 4) * Math.sin(2 * M) + (e * e * e / 12) * (13 * Math.sin(3 * M) - 3 * Math.sin(M));
	}

	public static final double true_anomaly_degrees(double time_since_perihelion, double orbital_period, double eccentricity)
	{
		return 180 * true_anomaly_radians(time_since_perihelion, orbital_period, eccentricity) / Math.PI;
	}

	// angles are in radians
	public static final double true_anomaly_radians(double time_since_perihelion, double orbital_period, double eccentricity)
	{
		double M = mean_anomaly_radians(time_since_perihelion, orbital_period);
		return M + equation_of_the_center_radians(M, eccentricity);
	}

	public static final double true_anomaly_tan_degrees(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = Math.PI * eccentric_anomaly / 180;
		double e = orbital_eccentricity;
		
		return true_anomaly_tan_radians(E, e) * 180 / Math.PI;
	}

	// angles are in radians
	public static final double true_anomaly_tan_radians(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = eccentric_anomaly;
		double e = orbital_eccentricity;
		double tan_nu_over_2 = Math.sqrt((1+e)/(1-e)) * Math.tan(E/2);
		
		return 2 * Math.atan(tan_nu_over_2);
	}

	public static final double true_anomaly_cos_degrees(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = Math.PI * eccentric_anomaly / 180;
		double e = orbital_eccentricity;
		
		return true_anomaly_cos_radians(E, e) * 180 / Math.PI;
	}

	// angles are in radians
	public static final double true_anomaly_cos_radians(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = eccentric_anomaly;
		double e = orbital_eccentricity;
		double cos_nu = (Math.cos(E) - e) / (1 - e * Math.cos(E));
		
		return Math.acos(cos_nu);
	}

	public static final double eccentric_anomaly_degrees(double true_anomaly, double orbital_eccentricity)
	{
		double nu = true_anomaly * Math.PI / 180;
		double e  = orbital_eccentricity;
		
		return eccentric_anomaly_radians(nu, e) * 180 / Math.PI;
	}

	// angles are in radians
	public static final double eccentric_anomaly_radians(double true_anomaly, double orbital_eccentricity)
	{
		double nu = true_anomaly;
		double e  = orbital_eccentricity;
		double tan_E_over_2 = Math.sqrt((1-e)/(1+e)) * Math.tan(nu/2);
		
		return 2 * Math.atan(tan_E_over_2);
	}

	public static final double keplers_equation_degrees(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = eccentric_anomaly * Math.PI / 180;
		double e = orbital_eccentricity;

		return keplers_equation_radians(E, e) * 180 / Math.PI;
	}

	// angles are in radians
	public static final double keplers_equation_radians(double eccentric_anomaly, double orbital_eccentricity)
	{
		double E = eccentric_anomaly;
		double e = orbital_eccentricity;
		double M = E - e * Math.sin(E);		// mean anomaly

		return M;
	}

	// angles are in radians
	public static final double estimate_eccentric_anomaly_radians(double mean_anomaly_radians, double orbital_eccentricity)
	{
		double Mrad = mean_anomaly_radians;
		double e = orbital_eccentricity;

		double Eprev = Mrad;
		double Enext = Mrad + e * Math.sin(Eprev);

		double err_prev = Double.MAX_VALUE;
		double err_next = Math.abs(Eprev - Enext);
		while (err_next < err_prev) {
			Eprev = Enext;
			Enext = Mrad + e * Math.sin(Eprev);
			err_prev = err_next;
			err_next = Math.abs(Eprev - Enext);
		}

		return Enext;
	}

	// angles are in decimal degrees
	public static final double estimate_eccentric_anomaly_degrees(double mean_anomaly_degrees, double orbital_eccentricity)
	{
		double Mrad = mean_anomaly_degrees * Math.PI / 180;
		double e = orbital_eccentricity;

		return estimate_eccentric_anomaly_radians(Mrad, e) * 180 / Math.PI;
	}


	// estimate the eccentric anomaly using the newton raphson method
	// angles are in radians
	public static final double estimate_eccentric_anomaly_newton_radians(double mean_anomaly_radians, double orbital_eccentricity)
	{
		double Mrad = mean_anomaly_radians;
		double e = orbital_eccentricity;

		double Eprev = (e <= 0.75) ? Mrad : Math.PI;
		double Enext = Eprev - (Eprev - e * Math.sin(Eprev) - Mrad) / (1 - e * Math.cos(Eprev));

		double err_prev = Double.MAX_VALUE;
		double err_next = Math.abs(Eprev - Enext);
		while (err_next < err_prev) {
			Eprev = Enext;
			Enext = Eprev - (Eprev - e * Math.sin(Eprev) - Mrad) / (1 - e * Math.cos(Eprev));
			err_prev = err_next;
			err_next = Math.abs(Eprev - Enext);
		}

		return Enext;
	}

	// estimate the eccentric anomaly using the newton raphson method
	// angles are in decimal degrees
	public static final double estimate_eccentric_anomaly_newton_degrees(double mean_anomaly_degrees, double orbital_eccentricity)
	{
		double Mrad = mean_anomaly_degrees * Math.PI / 180;
		double e = orbital_eccentricity;

		return estimate_eccentric_anomaly_newton_radians(Mrad, e) * 180 / Math.PI;
	}

	public static final double local_sidereal_time_to_hour_angle_raw(double local_sidereal_time, double right_ascension)
	{
		double LST   = local_sidereal_time;
		double alpha = right_ascension;
		double H     = LST - alpha;

		return H;
	}

	public static final double local_sidereal_time_to_hour_angle(double local_sidereal_time_decimal_hours, double right_ascension_decimal_hours)
	{
		double LST   = local_sidereal_time_decimal_hours;
		double alpha = right_ascension_decimal_hours;
		double H     = LST - alpha;
		
		H += (H < 0) ? 24 : ((24 <= H) ? -24 : 0);

		return H;
	}

	public static final double hour_angle_to_local_sidereal_time(double hour_angle_decimal_hours, double right_ascension_decimal_hours)
	{
		double H     = hour_angle_decimal_hours;
		double alpha = right_ascension_decimal_hours;
		double LST   = H + alpha;
		
		LST += (LST < 0) ? 24 : ((24 <= LST) ? -24 : 0);

		return LST;
	}

	public static final double hour_angle_and_local_sidereal_time_to_right_ascension_decimal_hours(double hour_angle_decimal_hours, double local_sidereal_time_decimal_hours)
	{
		double H     = hour_angle_decimal_hours;
		double LST   = local_sidereal_time_decimal_hours;
		double alpha = LST - H;

		alpha += (alpha < 0) ? 24 : ((24 <= alpha) ? -24 : 0);

		return alpha;
	}
	
	// phi   = observer's latitude
	// delta = de_B1950
	// alpha = right ascension
	// H     = hour angle
	// A     = azimuth
	// h     = altitude
	// beta   - ecliptic latitude
	// lambda - ecliptic longitude
	// b      - galactic latituce
	// l      - galactic longitude

	// sin(delta) = sin(h) sin(phi) + cos(h) cos(phi) cos(A)
	// cos(H) = (sin(h) - sin(phi) sin(delta)) / (cos(phi) cos(delta))

	// sin(h) = sin(delta) sin(phi) + cos(delta) cos(phi) cos(H)
	// cos(A) = (sin(delta) - sin(phi) sin(h)) / (cos(phi) cos(h))

	// altitude(h), azimuth(A), latitude(phi), to hour angle(H)
	// altitude(h), azimuth(A), latitude(phi), to de_B1950(delta)
	// hour angle(H), local sidereal time(LST), to right ascension(alpha)
	// 
	// altitude(h), azimuth(A), latitude(phi), local sidereal time(LST) to hour angle(H), right ascension(alpha), and de_B1950(delta)

	public static final Equatorial horizontal_to_equatorial_coordinates(double altitude, double azimuth, double latitude, double local_sidereal_time)
	{
		double h   = altitude * Math.PI / 180;	// altitude is in decimal degrees from the horizon; 0 = horizon, 90 = vertical
		double A   = azimuth  * Math.PI / 180;	// azimuth is in decimal degrees from north, clockwise; 0 = north, 90 = east, 180 = south, 270 = west
		double phi = latitude * Math.PI / 180;	// geographic latitude is in decimal degrees

		double T0 = Math.sin(h) * Math.sin(phi) + Math.cos(h) * Math.cos(phi) * Math.cos(A);
		
		double delta = Math.asin(T0);

		double T1 = Math.sin(h) - Math.sin(phi) * Math.sin(delta);

		double H = Math.acos(T1 / (Math.cos(phi) * Math.cos(delta)));

		double sinA = Math.sin(A);
		
		H = (0 < sinA) ? 2 * Math.PI - H : H ;

		H     = radians_to_decimal_hours(H);
		delta = radians_to_decimal_degrees(delta);
		
		double hour_angle  = H;	// in decimal hours
		double declination = delta;
		
		double right_ascension = hour_angle_and_local_sidereal_time_to_right_ascension_decimal_hours(hour_angle, local_sidereal_time);

		return new Equatorial(right_ascension, declination);
	}

	public static final Horizontal equatorial_to_horizontal_coordinates(double hour_angle, double declination, double latitude, double local_sidereal_time)
	{
		double H = hour_angle;
		double Hdeg = decimal_hours_to_degrees(H);
		double Hrad = decimal_degrees_to_radians(Hdeg);
		double delta_deg = declination;
		double delta_rad = decimal_degrees_to_radians(delta_deg);
		double phi = latitude;
		double phi_rad = decimal_degrees_to_radians(phi);
		double T0 = Math.sin(delta_rad) * Math.sin(phi_rad) + Math.cos(delta_rad) * Math.cos(phi_rad) * Math.cos(Hrad);
		double hrad = Math.asin(T0);						// altitude
		double hdeg = radians_to_decimal_degrees(hrad);
		double T1 = Math.sin(delta_rad) - Math.sin(phi_rad) * Math.sin(hrad);
		double T2 = T1 / (Math.cos(phi_rad) * Math.cos(hrad));
		double Arad = Math.acos(T2);
		double Adeg = radians_to_decimal_degrees(Arad);		// azimuth
		double sinHrad = Math.sin(Hrad);
		Adeg = (0 < sinHrad) ? 360 - Adeg : Adeg;

		return new Horizontal(hdeg, Adeg, latitude, local_sidereal_time);
	}

	public static class Equatorial {
		public final double right_ascension;	// decimal hours
		public final double declination;		// decimal degrees
		
		public Equatorial(double ra_dhrs, double dec_ddeg)
		{
			right_ascension = ra_dhrs;
			declination     = dec_ddeg;
		}
		
		// longitude is in decimal degrees (east is positive, west is negative)
		public final Horizontal to_horizontal(long time_in_ms, TimeZone timezone, double latitude, double longitude)
		{
			double universal_time = local_civil_time_to_universal_time(time_in_ms, timezone);

			Calendar c = Calendar.getInstance(timezone);
			int year         = c.get(Calendar.YEAR);
			int month        = c.get(Calendar.MONTH);
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);
			double greewich_sidereal_time = universal_time_to_greenwich_sidereal_time     (universal_time, year, month, day_of_month);
			double local_sidereal_time    = greenwich_sidereal_time_to_local_sidereal_time(greewich_sidereal_time, longitude);
			double hour_angle             = local_sidereal_time_to_hour_angle             (local_sidereal_time, right_ascension);

			Horizontal rv = equatorial_to_horizontal_coordinates(hour_angle, declination, latitude, local_sidereal_time);

			return rv;
		}
		
		public final Ecliptic to_ecliptic(int epoch_month, int epoch_day_of_month, int epoch_year)
		{
			double epsilon_deg = Ecliptic.obliquity(epoch_month, epoch_day_of_month, epoch_year);
			double alpha_hrs = right_ascension;
			double alpha_deg = alpha_hrs * degrees_per_hour;
			double delta_deg = declination;

			double epsilon_rad = Math.PI * epsilon_deg / 180.0;
			double alpha_rad   = Math.PI * alpha_deg   / 180.0;
			double delta_rad   = Math.PI * delta_deg   / 180.0;
			double T = Math.sin(delta_rad) * Math.cos(epsilon_rad) - Math.cos(delta_rad) * Math.sin(epsilon_rad) * Math.sin(alpha_rad);
			double beta_rad = Math.asin(T);
			double beta_deg = 180 * beta_rad / Math.PI;
			double y = Math.sin(alpha_rad) * Math.cos(epsilon_rad) + Math.tan(delta_rad) * Math.sin(epsilon_rad);
			double x = Math.cos(alpha_rad);
			double R_rad = Math.atan2(y, x);
			double R_deg = 180 * R_rad / Math.PI;
			R_deg = (R_deg < 0) ? R_deg + 360 : ((360 <= R_deg) ? R_deg - 360 : R_deg);

			return new Ecliptic(beta_deg, R_deg, epsilon_deg);
		}

		public final Galactic1950 to_galactic_1950()
		{
			double alpha_1950_hrs = right_ascension;
			double alpha_1950_deg = decimal_hours_to_degrees(alpha_1950_hrs);
			double alpha_1950_rad = decimal_degrees_to_radians(alpha_1950_deg);
			double delta_1950_deg = declination;
			double delta_1950_rad = decimal_degrees_to_radians(delta_1950_deg);
			double delta0_1950_rad = Galactic1950.GNP_DEC_1950_rad;
			double alpha0_1950_rad = Galactic1950.GNP_RA_1950_rad;
			double T0 = Math.cos(delta_1950_rad) * Math.cos(delta0_1950_rad) * Math.cos(alpha_1950_rad - alpha0_1950_rad) + Math.sin(delta_1950_rad) * Math.sin(delta0_1950_rad);
			double b_rad = Math.asin(T0);
			double b_deg = radians_to_decimal_degrees(b_rad);
			double y = Math.sin(delta_1950_rad) - Math.sin(b_rad) * Math.sin(delta0_1950_rad);
			double x = Math.cos(delta_1950_rad) * Math.sin(alpha_1950_rad - alpha0_1950_rad) * Math.cos(delta0_1950_rad);
			double T1_rad = Math.atan2(y, x);
			double T1_deg = radians_to_decimal_degrees(T1_rad);
			double N0_1950_deg = Galactic1950.longitude_of_ascending_node_1950;
			double l_deg = T1_deg + N0_1950_deg;

			return new Galactic1950(b_deg, l_deg);
		}

		public final GalacticJ2000 to_galactic_J2000()
		{
			double alpha_J2000_hrs = right_ascension;
			double alpha_J2000_deg = decimal_hours_to_degrees(alpha_J2000_hrs);
			double alpha_J2000_rad = decimal_degrees_to_radians(alpha_J2000_deg);
			double delta_J2000_deg = declination;
			double delta_J2000_rad = decimal_degrees_to_radians(delta_J2000_deg);
			double delta0_J2000_rad = GalacticJ2000.GNP_DEC_J2000_rad;
			double alpha0_J2000_rad = GalacticJ2000.GNP_RA_J2000_rad;
			double T0 = Math.cos(delta_J2000_rad) * Math.cos(delta0_J2000_rad) * Math.cos(alpha_J2000_rad - alpha0_J2000_rad) + Math.sin(delta_J2000_rad) * Math.sin(delta0_J2000_rad);
			double b_rad = Math.asin(T0);
			double b_deg = radians_to_decimal_degrees(b_rad);
			double y = Math.sin(delta_J2000_rad) - Math.sin(b_rad) * Math.sin(delta0_J2000_rad);
			double x = Math.cos(delta_J2000_rad) * Math.sin(alpha_J2000_rad - alpha0_J2000_rad) * Math.cos(delta0_J2000_rad);
			double T1_rad = Math.atan2(y, x);
			double T1_deg = radians_to_decimal_degrees(T1_rad);
			double N0_J2000_deg = GalacticJ2000.longitude_of_ascending_node_J2000;
			double l_deg = T1_deg + N0_J2000_deg;

			return new GalacticJ2000(b_deg, l_deg);
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}

	public static class Horizontal {
		public final double altitude;		// altitud above the horizon in decimal degrees (-90 <= alt <= +90)
		public final double azimuth;		// decimal degrees (N = 0 degrees, E = 0, S = 180, W = 270)
		public final double latitude;		// geographic latitude in decimal degrees
		public final double sidereal;		// local sidereal time 

		public Horizontal(double alt_ddeg, double az_ddeg, double lat_ddeg, double LST)
		{
			altitude = alt_ddeg;
			azimuth  = az_ddeg;
			latitude = lat_ddeg;
			sidereal = LST;
		}
		
		public final Equatorial to_equatorial()
		{
			return horizontal_to_equatorial_coordinates(altitude, azimuth, latitude, sidereal);
		}
		
		public String toString()
		{
			return String.format("%s, %s", DMS.ddeg_to_dms(altitude), DMS.ddeg_to_dms(azimuth));
		}
	}

	public static class Ecliptic {
		public final double latitude;		// decimal degrees
		public final double longitude;		// decimal degrees
		public final double epsilon;		// obliquity in decimal degrees

		public Ecliptic(double lat_ddeg, double lon_ddeg, double eps)
		{
			latitude  = lat_ddeg;
			longitude = lon_ddeg;
			epsilon   = eps;
		}

		public Ecliptic(double lat_ddeg, double lon_ddeg, int epoch_month, int epoch_day_of_month, int epoch_year)
		{
			latitude  = lat_ddeg;
			longitude = lon_ddeg;
			epsilon   = obliquity(epoch_month, epoch_day_of_month, epoch_year);
		}
		
		public final Equatorial to_equatorial()
		{
			double beta        = latitude;
			double beta_rad    = decimal_degrees_to_radians(beta);
			double lambda      = longitude;
			double lambda_rad  = decimal_degrees_to_radians(lambda);
			double epsilon_rad = decimal_degrees_to_radians(epsilon);
			double T           = Math.sin(beta_rad) * Math.cos(epsilon_rad) + Math.cos(beta_rad) * Math.sin(epsilon_rad) * Math.sin(lambda_rad);
			double delta_rad   = Math.asin(T);
			double delta       = radians_to_decimal_degrees(delta_rad);
			double y           = Math.sin(lambda_rad) * Math.cos(epsilon_rad) - Math.tan(beta_rad)*Math.sin(epsilon_rad);
			double x           = Math.cos(lambda_rad);
			double R_rad       = Math.atan2(y, x);
			R_rad              = (R_rad < 0) ? R_rad + 2 * Math.PI : R_rad;
			double alpha_deg   = radians_to_decimal_degrees(R_rad);
			double alpha_hrs   = decimal_degrees_to_hours(alpha_deg);

			return new Equatorial(alpha_hrs, delta);
		}

		public String toString()
		{
			return String.format("%s, %s, %s", DMS.ddeg_to_dms(latitude), DMS.ddeg_to_dms(longitude), DMS.ddeg_to_dms(epsilon));
		}

		public static final double obliquity(int epoch_month, int epoch_day_of_month, int epoch_year)
		{
			double JD = calendar_date_to_julian_day(epoch_year, epoch_month, epoch_day_of_month, 0, 0, 0);
			double T = (JD - 2451545) / 36525;
			double De = T * (46.815 + T * (0.0006 - T * 0.00181));
			// double epsilon0 = 23.439292;
			double epsilon0 = dms_to_decimal_degrees(23, 26, 21.45);
			double epsilon = epsilon0 - (De/3600);

			return epsilon;
		}
	}

	public static class Galactic {
		// galactic center
		public final double center_RA_hrs;
		public final double center_RA_deg;
		public final double center_RA_rad;
		public final double center_DEC_deg;
		public final double center_DEC_rad;

		// galactic north pole, RA = alpha0, DEC = delta0
		public final double GNP_RA_hrs;
		public final double GNP_RA_deg;
		public final double GNP_RA_rad;
		public final double GNP_DEC_deg;
		public final double GNP_DEC_rad;
		
		// longitude of the ascending node of the galactic plane
		public final double longitude_of_ascending_node;	// decimal degrees
		public final double latitude_of_ascending_node;		// decimal degrees

		public final double latitude;		// (b) decimal degrees
		public final double longitude;		// (l) decimal degrees

		public static final double epoch_1950           = 1950;
		public static final double center_RA_1950_hrs   = hms_to_decimal_hours(17,42,0);
		public static final double center_RA_1950_deg   = decimal_hours_to_degrees(center_RA_1950_hrs);
		public static final double center_RA_1950_rad   = decimal_degrees_to_radians(center_RA_1950_deg);
		public static final double center_DEC_1950_deg  = dms_to_decimal_degrees(-28,45,0);
		public static final double center_DEC_1950_rad  = decimal_degrees_to_radians(center_DEC_1950_deg);

		public static final double GNP_RA_1950_hrs      = hms_to_decimal_hours(12,49,0);
		public static final double GNP_RA_1950_deg      = decimal_hours_to_degrees(GNP_RA_1950_hrs);
		public static final double GNP_RA_1950_rad      = decimal_degrees_to_radians(GNP_RA_1950_deg);
		public static final double GNP_DEC_1950_deg     = dms_to_decimal_degrees(27,24,0);
		public static final double GNP_DEC_1950_rad     = decimal_degrees_to_radians(GNP_DEC_1950_deg);

		public static final double longitude_of_ascending_node_1950  = 33.0;	// decimal degrees
		public static final double latitude_of_ascending_node_1950   =  0.0;	// decimal degrees

		public static final double epoch_J2000          = 2000;
		public static final double center_RA_J2000_hrs  = hms_to_decimal_hours(17,45,37.22);
		public static final double center_RA_J2000_deg  = decimal_hours_to_degrees(center_RA_J2000_hrs);
		public static final double center_RA_J2000_rad  = decimal_degrees_to_radians(center_RA_J2000_deg);
		public static final double center_DEC_J2000_deg = dms_to_decimal_degrees(-28,56,10.23);
		public static final double center_DEC_J2000_rad = decimal_degrees_to_radians(center_DEC_J2000_deg);

		public static final double GNP_RA_J2000_hrs     = hms_to_decimal_hours(12,51,26.36);
		public static final double GNP_RA_J2000_deg     = decimal_hours_to_degrees(GNP_RA_J2000_hrs);
		public static final double GNP_RA_J2000_rad     = decimal_degrees_to_radians(GNP_RA_J2000_deg);
		public static final double GNP_DEC_J2000_deg    = dms_to_decimal_degrees(27,7,40.90);
		public static final double GNP_DEC_J2000_rad    = decimal_degrees_to_radians(GNP_DEC_J2000_deg);

		public static final double longitude_of_ascending_node_J2000 = 32.9319;	// decimal degrees
		public static final double latitude_of_ascending_node_J2000  =  0.0;	// decimal degrees

		public Galactic(double lat_ddeg, double lon_ddeg)
		{
			center_RA_hrs  = center_RA_J2000_hrs;
			center_RA_deg  = decimal_hours_to_degrees(center_RA_hrs);
			center_RA_rad  = decimal_degrees_to_radians(center_RA_deg);
			center_DEC_deg = center_DEC_J2000_deg;
			center_DEC_rad = decimal_degrees_to_radians(center_DEC_deg);

			GNP_RA_hrs  = GNP_RA_J2000_hrs;
			GNP_RA_deg  = decimal_hours_to_degrees(GNP_RA_hrs);
			GNP_RA_rad  = decimal_degrees_to_radians(GNP_RA_deg);
			GNP_DEC_deg = GNP_DEC_J2000_deg;
			GNP_DEC_rad = decimal_degrees_to_radians(GNP_DEC_deg);

			latitude_of_ascending_node  = latitude_of_ascending_node_J2000;
			longitude_of_ascending_node = longitude_of_ascending_node_J2000;

			latitude  = lat_ddeg;
			longitude = lon_ddeg;
		}

		public Galactic(double lat_ddeg, double lon_ddeg, double center_ra_hrs, double center_dec_deg, 
				double gnp_ra_hrs, double gnp_dec_deg, double lat_asc, double lon_asc)
		{
			center_RA_hrs  = center_ra_hrs;
			center_RA_deg  = decimal_hours_to_degrees(center_RA_hrs);
			center_RA_rad  = decimal_degrees_to_radians(center_RA_deg);
			center_DEC_deg = center_dec_deg;
			center_DEC_rad = decimal_degrees_to_radians(center_DEC_deg);

			GNP_RA_hrs  = gnp_ra_hrs;
			GNP_RA_deg  = decimal_hours_to_degrees(GNP_RA_hrs);
			GNP_RA_rad  = decimal_degrees_to_radians(GNP_RA_deg);
			GNP_DEC_deg = gnp_dec_deg;
			GNP_DEC_rad = decimal_degrees_to_radians(GNP_DEC_deg);

			latitude_of_ascending_node  = lat_asc;
			longitude_of_ascending_node = lon_asc;

			latitude  = lat_ddeg;
			longitude = lon_ddeg;
		}

		public final Equatorial to_equatorial()
		{
			return to_equatorial(GNP_RA_hrs, GNP_DEC_deg, longitude_of_ascending_node);
		}

		public final Equatorial to_equatorial(double gnp_ra_hrs, double gnp_dec_deg, double lon_asc)
		{
			double b_deg = latitude;
			double l_deg = longitude;
			double b_rad = decimal_degrees_to_radians(b_deg);
			double l_rad = decimal_degrees_to_radians(l_deg);
			double N0_deg = lon_asc;
			double N0_rad = decimal_degrees_to_radians(N0_deg);
			double delta0_rad = decimal_degrees_to_radians(gnp_dec_deg);
			double T = Math.cos(b_rad) * Math.cos(delta0_rad) * Math.sin(l_rad - N0_rad) + Math.sin(b_rad) * Math.sin(delta0_rad);
			double delta_rad = Math.asin(T);
			double delta_deg = radians_to_decimal_degrees(delta_rad);
			double y = Math.cos(b_rad) * Math.cos(l_rad - N0_rad);
			double x = Math.sin(b_rad) * Math.cos(delta0_rad) - Math.cos(b_rad) * Math.sin(delta0_rad) * Math.sin(l_rad - N0_rad);
			double R_rad = Math.atan2(y, x);
			double alpha_deg = radians_to_decimal_degrees(R_rad) + decimal_hours_to_degrees(gnp_ra_hrs);
			alpha_deg += (alpha_deg < 0) ? 360 : ((360 <= alpha_deg) ? -360 : 0);
			double alpha_hrs = decimal_degrees_to_hours(alpha_deg);

			return new Equatorial(alpha_hrs, delta_deg);
		}

		public final Equatorial to_equatorial_1950()
		{
			return to_equatorial(GNP_RA_1950_hrs, GNP_DEC_1950_deg, longitude_of_ascending_node_1950);
		}

		public final Equatorial to_equatorial_J2000()
		{
			return to_equatorial(GNP_RA_J2000_hrs, GNP_DEC_J2000_deg, longitude_of_ascending_node_J2000);
		}

		public static final Equatorial equatorial_precession(double epoch)
		{
			double alpha_1950_hrs = GNP_RA_1950_hrs;
			double alpha_1950_deg = GNP_RA_1950_deg;
			double alpha_1950_rad = GNP_RA_1950_rad;
			System.out.printf("alpha_h=%.12f%n", alpha_1950_hrs);
			System.out.printf("alpha_d=%.12f%n", alpha_1950_deg);
			double delta_1950_deg = GNP_DEC_1950_deg;
			double delta_1950_rad = GNP_DEC_1950_rad;
			System.out.printf("decl=%.12f%n", delta_1950_deg);
			double Et = epoch;
			double T = (Et - 1900) / 100.0;
			System.out.printf("T=%.12f%n", T);
			double M = 3.07234 + 0.00186 * T;
			System.out.printf("M=%.12f%n", M);
			double Nd = 20.0468 - 0.0085 * T;
			System.out.printf("Nd=%.12f%n", Nd);
			double Nt = Nd / 15;
			System.out.printf("Nt=%.12f%n", Nt);
			double Ef = epoch_1950;
			double D = Et - Ef;
			System.out.printf("D=%.12f%n", D);
			double DELTA_alpha = (M + Nt * Math.sin(alpha_1950_rad) * Math.tan(delta_1950_rad)) * D;
			System.out.printf("DELTA_alpha=%.12f%n", DELTA_alpha);
			double DELTA_delta = Nd * Math.cos(alpha_1950_rad) * D;
			System.out.printf("DELTA_delta=%.12f%n", DELTA_delta);
			double DELTA_alpha_hrs = DELTA_alpha / 3600;
			double DELTA_delta_deg = DELTA_delta / 3600;
			System.out.printf("DELTA_alpha_hrs=%.12f%n", DELTA_alpha_hrs);
			System.out.printf("DELTA_delta_deg=%.12f%n", DELTA_delta_deg);
			double alpha_2000_hrs = alpha_1950_hrs + DELTA_alpha_hrs;
			double delta_2000_deg = delta_1950_deg + DELTA_delta_deg;
			System.out.printf("alpha_2000_hrs=%.12f%n", alpha_2000_hrs);
			System.out.printf("delta_2000_deg=%.12f%n", delta_2000_deg);

			return new Equatorial(0, 0);
		}

		public String toString()
		{
			return String.format("%s, %s", DMS.ddeg_to_dms(latitude), DMS.ddeg_to_dms(longitude));
		}
	}

	public static class Galactic1950 extends Galactic {
		public Galactic1950(double lat_ddeg, double lon_ddeg)
		{
			super(lat_ddeg, lon_ddeg, center_RA_1950_hrs, center_DEC_1950_deg, GNP_RA_1950_hrs, GNP_DEC_1950_deg, 
					latitude_of_ascending_node_1950, longitude_of_ascending_node_1950);
		}
	}

	public static class GalacticJ2000 extends Galactic {
		public GalacticJ2000(double lat_ddeg, double lon_ddeg)
		{
			super(lat_ddeg, lon_ddeg, center_RA_J2000_hrs, center_DEC_J2000_deg, GNP_RA_J2000_hrs, GNP_DEC_J2000_deg, 
					latitude_of_ascending_node_J2000, longitude_of_ascending_node_J2000);
		}
	}

	public static class RiseAndSetTime {
		public final double   right_ascension;		// equatorial right ascension of a star or other object
		public final double   declination;			// equatorial de_B1950 of a star or other object
		public final double   latitude;				// geographic latitude of observer
		public final double   longitude;			// geographic longitude of observer
		public final double   rise_azimuth;			// azimuth of where the object will cross the horizon
		public final double   set_azimuth;			// azimuth of where the object will cross the horizon
		public final long     time_in_millis;		// approximate time/date of observation
		public final boolean  always_visible;		// is the object always visible (always above the horizon)
		public final boolean  rises_and_sets;		// does the object rise and set (sometimes visible, sometimes not)
		public final boolean  never_visible;		// is the object never visible (always below the horizon)
		public final double   local_rise_time;		// local civil time the object rises above the horizon (decimal hours)
		public final double   local_set_time;		// local civil time the object crosses below the horizon (decimal hours)
		public final long     rise_time_in_millis;	// local civil rise time in milliseconds from the start of the epoch 
		public final long     set_time_in_millis;	// local civil set time in milliseconds from the start of the epoch
		public final TimeZone timezone;				// local civil time zone

		// calculate the local civil time of a stellar object rising above and setting below the horizon 
		public RiseAndSetTime(double ra, double dec, double lat, double lon, long millis, TimeZone tz)
		{
			right_ascension = ra;
			declination     = dec;
			latitude        = lat;
			longitude       = lon;
			time_in_millis  = millis;
			timezone = tz;
			if (0 <= latitude) {
				if ((90 - latitude) <= declination) {
					always_visible      = true;
					rises_and_sets      = false;
					never_visible       = false;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				} else if (declination < (latitude - 90)) {
					always_visible      = false;
					rises_and_sets      = false;
					never_visible       = true;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				}
			} else if (latitude < 0) {
				if (declination <= (-90 - latitude)) {
					always_visible      = true;
					rises_and_sets      = false;
					never_visible       = false;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				} else if ((latitude + 90) < declination) {
					always_visible      = false;
					rises_and_sets      = false;
					never_visible       = true;
					local_rise_time     = Double.NaN;
					local_set_time      = Double.NaN;
					rise_azimuth        = Double.NaN;
					set_azimuth         = Double.NaN;
					rise_time_in_millis = Long.MAX_VALUE;
					set_time_in_millis  = Long.MAX_VALUE;
					return;
				}
			}
			always_visible = false;
			rises_and_sets = true;
			never_visible  = false;

			double alpha_hrs = right_ascension;
			double delta_deg = declination;
			double delta_rad = decimal_degrees_to_radians(delta_deg);
			double phi_deg = latitude;
			double phi_rad = decimal_degrees_to_radians(phi_deg);
			double Ar = Math.sin(delta_rad) / Math.cos(phi_rad);
			double R_rad = Math.acos(Ar);
			double R_deg = radians_to_decimal_degrees(R_rad);
			double S_deg = 360 - R_deg;
			rise_azimuth = R_deg;
			set_azimuth  = S_deg;

			double H1 = Math.tan(phi_rad) * Math.tan(delta_rad);
			double H2_rad = Math.acos(-H1);
			double H2_hrs = radians_to_decimal_hours(H2_rad);
			double LSTr = 24 + alpha_hrs - H2_hrs;
			LSTr += (LSTr < 0) ? 24 : ((24 <= LSTr) ? -24 : 0 );

			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(time_in_millis);
			int year  = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			double GSTr = local_sidereal_time_to_greenwich_sidereal_time(LSTr, longitude);
			GSTr += (GSTr < 0) ? 24 : ((24 <= GSTr) ? -24 : 0 );
			double UTr  = greenwich_sidereal_time_to_universal_time(GSTr, year, month, day_of_month);
			UTr += (UTr < 0) ? 24 : ((24 <= UTr) ? -24 : 0 );
			double LCTr = universal_time_to_local_civil_time(UTr, year, month, day_of_month, timezone);
			LCTr += (LCTr < 0) ? 24 : ((24 <= LCTr) ? -24 : 0 );
			local_rise_time = LCTr;

			c.set(Calendar.HOUR,   hour_of_decimal_hours(LCTr));
			c.set(Calendar.MINUTE, minute_of_decimal_hours(LCTr));
			c.set(Calendar.SECOND, (int) second_of_decimal_hours(LCTr));
			rise_time_in_millis = c.getTimeInMillis();

			double LSTs = alpha_hrs + H2_hrs;
			LSTs += (LSTs < 0) ? 24 : ((24 <= LSTs) ? -24 : 0 );
			double GSTs = local_sidereal_time_to_greenwich_sidereal_time(LSTs, longitude);
			GSTs += (GSTs < 0) ? 24 : ((24 <= GSTs) ? -24 : 0 );
			double UTs  = greenwich_sidereal_time_to_universal_time(GSTs, year, month, day_of_month);
			UTs += (UTs < 0) ? 24 : ((24 <= UTs) ? -24 : 0 );
			double LCTs = universal_time_to_local_civil_time(UTs, year, month, day_of_month, timezone);
			LCTs += (LCTs < 0) ? 24 : ((24 <= LCTs) ? -24 : 0 );
			local_set_time = LCTs;

			c.set(Calendar.HOUR,   hour_of_decimal_hours(LCTr));
			c.set(Calendar.MINUTE, minute_of_decimal_hours(LCTr));
			c.set(Calendar.SECOND, (int) second_of_decimal_hours(LCTr));
			set_time_in_millis = c.getTimeInMillis();
		}

		public String toString()
		{
			if (always_visible) {
				// always visible
				return "always visible";
			} else if (never_visible) {
				// never visible
				return "never visible";
			}
			
			// rises and sets
			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(rise_time_in_millis);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			return String.format("rise %s, set %s, %02d/%02d/%04d, %s%s", 
					decimal_hours_to_string(local_rise_time), 
					decimal_hours_to_string(local_set_time),
					month, day_of_month, year,
					timezone.getDisplayName(),
					(c.get(Calendar.DST_OFFSET) != 0) ? " (DST)" : "");
		}
	}


	public static final class SolarLocation {
		public final double   right_ascension;		// apparent solar right ascension
		public final double   declination;			// apparent solar de_B1950
		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone
		
		public SolarLocation(long millis, TimeZone tz)
		{
			time_in_millis = millis;
			timezone       = tz;

			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(time_in_millis);
			int year  = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			double universal_time = local_civil_time_to_universal_time(time_in_millis, timezone);
			if (universal_time < 0) {
				universal_time += 24;
				day_of_month   -= 1;
			} else if (24 <= universal_time) {
				universal_time -= 24;
				day_of_month   += 1;
			}

			// double greenwich_sidereal_time = universal_time_to_greenwich_sidereal_time(universal_time, year, month, day_of_month);
			// double local_sidereal_time = greenwich_sidereal_time_to_local_sidereal_time(greenwich_sidereal_time, longitude);
			// double JDe = calendar_date_to_julian_day(2000, 1, 1, 12, 0, 0);								// julian day for epoch J2000
			double JD = calendar_date_to_julian_day(year, month, day_of_month, universal_time, 0, 0);		// julian day for UT
			// double De = JD - JDe;

			// TODO
			double T  = (JD - 2451545.0) / 36525.0;
			double L0_deg = 280.46646     + 36000.76983 * T + 0.0003032    * T * T;	// geometric mean of the longitude of the sun
			L0_deg = (L0_deg < 360) ? L0_deg : L0_deg - (((int) L0_deg / 360) * 360);
			L0_deg = (0  <= L0_deg) ? L0_deg : L0_deg - (((int) L0_deg / 360) * 360) + 360;
			double M_deg  = 357.52911     + 35999.05029 * T + 0.0001537    * T * T;	// mean anomaly of the sun
			M_deg = (M_deg < 360) ? M_deg : M_deg - (((int) M_deg / 360) * 360);
			M_deg = (0  <= M_deg) ? M_deg : M_deg - (((int) M_deg / 360) * 360) + 360;
			double M_rad  = decimal_degrees_to_radians(M_deg);
			// double e = 0.016708634 - 0.000042037 * T - 0.0000001267 * T * T;		// eccentricity of earth's orbit (e)
			double C_deg = (1.914602 - 0.004817 * T - 0.000014 * T * T) * Math.sin(M_rad) +
					(0.019993 - 0.000101 * T) * Math.sin(2 * M_rad) + 0.000289 * Math.sin(3 * M_rad);
			double L_sun_deg = L0_deg + C_deg;										// sun's true longitude
			double L_sun_rad = decimal_degrees_to_radians(L_sun_deg);
			// double nu_deg = M_deg + C_deg;											// sun's true anomaly
			// double nu_rad = decimal_degrees_to_radians(nu_deg);
			// double R = 1.000001018 * (1 - e * e) / (1 + e * Math.cos(nu_rad));
			double omega_deg = 125.04 - 1934.136 * T;
			double omega_rad = decimal_degrees_to_radians(omega_deg);
			// double lambda_deg = L_sun_deg - 0.00569 - 0.00478 * Math.sin(omega_rad);
			double epsilon_deg = 23.43999 + 0 * 0.00256 * Math.cos(omega_rad);
			double epsilon_rad = decimal_degrees_to_radians(epsilon_deg);
			double alpha_rad = Math.atan2(Math.cos(epsilon_rad) * Math.sin(L_sun_rad), Math.cos(L_sun_rad));
			// double alpha_deg = radians_to_decimal_degrees(alpha_rad);
			double alpha_hrs = radians_to_decimal_hours(alpha_rad);
			alpha_hrs += (alpha_hrs <   0) ? 24 : 0;
			alpha_hrs -= (24 <= alpha_hrs) ? 24 : 0;
			double delta_rad = Math.asin(Math.sin(epsilon_rad) * Math.sin(L_sun_rad));
			double delta_deg = radians_to_decimal_degrees(delta_rad);
			
			right_ascension = alpha_hrs;
			declination     = delta_deg;

			/*/
			System.out.printf("UT=%.12f%n", universal_time);
			System.out.printf("GST=%.12f%n", greenwich_sidereal_time);
			System.out.printf("LST=%.12f%n", local_sidereal_time);
			System.out.printf("Date=%02d/%02d/%04d%n", month, day_of_month, year);
			System.out.printf("JDe=%.12f%n", JDe);
			System.out.printf("JD=%.12f%n", JD);
			System.out.printf("De=%.12f%n", De);
			System.out.printf("T=%.12f%n", T);
			System.out.printf("L0_deg=%.12f%n", L0_deg);
			System.out.printf("M_deg=%.12f%n",  M_deg);
			System.out.printf("e=%.12f%n", e);
			System.out.printf("C_deg=%.12f%n",  C_deg);
			System.out.printf("L_sun=%.12f%n",  L_sun_deg);
			System.out.printf("nu_deg=%.12f%n", nu_deg);
			System.out.printf("R=%.12f%n", R);
			System.out.printf("omega_deg=%.12f%n", omega_deg);
			System.out.printf("lambda_deg=%.12f%n", lambda_deg);
			System.out.printf("epsilon_deg=%.12f%n", epsilon_deg);
			System.out.printf("alpha_deg=%.12f%n", alpha_deg);
			System.out.printf("alpha_hrs=%.12f%n", alpha_hrs);
			System.out.printf("delta_deg=%.12f%n", delta_deg);
			/*/
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}


	public static final class LunarLocation {
		public final double   right_ascension;		// apparent lunar right ascension
		public final double   declination;			// apparent lunar de_B1950
		public final long     time_in_millis;		// approximate time/date of observation
		public final TimeZone timezone;				// local civil time zone
		
		public LunarLocation(long millis, double latitude, double longitude, TimeZone tz)
		{
			time_in_millis = millis;
			timezone       = tz;
			right_ascension = Double.NaN;
			declination     = Double.NaN;

			Calendar c = Calendar.getInstance(timezone);
			c.setTimeInMillis(time_in_millis);
			int year  = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day_of_month = c.get(Calendar.DAY_OF_MONTH);

			double universal_time = local_civil_time_to_universal_time(time_in_millis, timezone);
			if (universal_time < 0) {
				universal_time += 24;
				day_of_month   -= 1;
			} else if (24 <= universal_time) {
				universal_time -= 24;
				day_of_month   += 1;
			}
			System.out.printf("UT=%.12f%n", universal_time);
			System.out.printf("Date=%02d/%02d/%04d%n", month, day_of_month, year);
			double JD = calendar_date_to_julian_day(year, month, day_of_month, universal_time, 0, 0);		// julian day for UT
			System.out.printf("JD=%.12f%n", JD);

			/*/
			double greenwich_sidereal_time = universal_time_to_greenwich_sidereal_time(universal_time, year, month, day_of_month);
			System.out.printf("GST=%.12f%n", greenwich_sidereal_time);
			double local_sidereal_time = greenwich_sidereal_time_to_local_sidereal_time(greenwich_sidereal_time, longitude);
			System.out.printf("LST=%.12f%n", local_sidereal_time);
			double TT = universal_time + hms_to_decimal_hours(0,0,63.8);
			System.out.printf("TT=%.12f%n", TT);
			double JDe = calendar_date_to_julian_day(2000, 1, 1, 12, 0, 0);						// julian day for epoch J2000
			System.out.printf("JDe=%.12f%n", JDe);
			double JD = calendar_date_to_julian_day(year, month, day_of_month, TT, 0, 0);		// julian day for TT
			System.out.printf("JD=%.12f%n", JD);
			double De = JD - JDe;
			System.out.printf("De=%.12f%n", De);

			double T  = (JD - 2451545.0) / 36525.0;
			System.out.printf("T=%.12f%n", T);
			double e_rad  = 0.01675104 - 0.0000418 * T - 0.000000126 * T * T;
			System.out.printf("e_rad=%.12f%n", e_rad);
			double epsilon_g = 279.6966778 + 36000.76892 * T + 0.0003025 * T * T;
			System.out.printf("epsilon_g=%.12f%n", epsilon_g);
			double omega_g = 281.2208444 + 1.719175 * T + 0.000452778 * T * T;
			System.out.printf("omega_g=%.12f%n", omega_g);
			double M_deg = (De * 360) / 365.242191;
			System.out.printf("M=%.12f%n", M_deg);
			double M_sun_deg = M_deg + epsilon_g + omega_g;
			M_sun_deg -= (int)(M_sun_deg/360) * 360;
			System.out.printf("M_sun_deg=%.12f%n", M_sun_deg);
			double M_sun_rad = decimal_degrees_to_radians(M_sun_deg);
			double Ec_deg = 360 * e_rad * Math.sin(M_sun_rad) / Math.PI;
			System.out.printf("Ec=%.12f%n", Ec_deg);
			double nu_sun_deg = M_sun_deg + Ec_deg;
			System.out.printf("nu_sun_deg=%.12f%n", nu_sun_deg);
			double lambda_sun_deg = nu_sun_deg + omega_g;
			System.out.printf("lambda_sun_deg=%.12f%n", lambda_sun_deg);
			lambda_sun_deg -= (int)(lambda_sun_deg/360) * 360;
			System.out.printf("lambda_sun_deg=%.12f%n", lambda_sun_deg);
			/*/

			/*/
			// TODO
			double T  = (JD - 2451545.0) / 36525.0;
			double L0_deg = 280.46646     + 36000.76983 * T + 0.0003032    * T * T;	// geometric mean of the longitude of the sun
			L0_deg = (L0_deg < 360) ? L0_deg : L0_deg - (((int) L0_deg / 360) * 360);
			L0_deg = (0  <= L0_deg) ? L0_deg : L0_deg - (((int) L0_deg / 360) * 360) + 360;
			double M_deg  = 357.52911     + 35999.05029 * T + 0.0001537    * T * T;	// mean anomaly of the sun
			M_deg = (M_deg < 360) ? M_deg : M_deg - (((int) M_deg / 360) * 360);
			M_deg = (0  <= M_deg) ? M_deg : M_deg - (((int) M_deg / 360) * 360) + 360;
			double M_rad  = decimal_degrees_to_radians(M_deg);
			// double e = 0.016708634 - 0.000042037 * T - 0.0000001267 * T * T;		// eccentricity of earth's orbit (e)
			double C_deg = (1.914602 - 0.004817 * T - 0.000014 * T * T) * Math.sin(M_rad) +
					(0.019993 - 0.000101 * T) * Math.sin(2 * M_rad) + 0.000289 * Math.sin(3 * M_rad);
			double L_sun_deg = L0_deg + C_deg;										// sun's true longitude
			double L_sun_rad = decimal_degrees_to_radians(L_sun_deg);
			// double nu_deg = M_deg + C_deg;											// sun's true anomaly
			// double nu_rad = decimal_degrees_to_radians(nu_deg);
			// double R = 1.000001018 * (1 - e * e) / (1 + e * Math.cos(nu_rad));
			double omega_deg = 125.04 - 1934.136 * T;
			double omega_rad = decimal_degrees_to_radians(omega_deg);
			// double lambda_deg = L_sun_deg - 0.00569 - 0.00478 * Math.sin(omega_rad);
			double epsilon_deg = 23.43999 + 0 * 0.00256 * Math.cos(omega_rad);
			double epsilon_rad = decimal_degrees_to_radians(epsilon_deg);
			double alpha_rad = Math.atan2(Math.cos(epsilon_rad) * Math.sin(L_sun_rad), Math.cos(L_sun_rad));
			// double alpha_deg = radians_to_decimal_degrees(alpha_rad);
			double alpha_hrs = radians_to_decimal_hours(alpha_rad);
			alpha_hrs += (alpha_hrs <   0) ? 24 : 0;
			alpha_hrs -= (24 <= alpha_hrs) ? 24 : 0;
			double delta_rad = Math.asin(Math.sin(epsilon_rad) * Math.sin(L_sun_rad));
			double delta_deg = radians_to_decimal_degrees(delta_rad);
			
			ra_B1950 = alpha_hrs;
			de_B1950     = delta_deg;

			System.out.printf("UT=%.12f%n", universal_time);
			System.out.printf("GST=%.12f%n", greenwich_sidereal_time);
			System.out.printf("LST=%.12f%n", local_sidereal_time);
			System.out.printf("Date=%02d/%02d/%04d%n", month, day_of_month, year);
			System.out.printf("JDe=%.12f%n", JDe);
			System.out.printf("JD=%.12f%n", JD);
			System.out.printf("De=%.12f%n", De);
			System.out.printf("T=%.12f%n", T);
			System.out.printf("L0_deg=%.12f%n", L0_deg);
			System.out.printf("M_deg=%.12f%n",  M_deg);
			System.out.printf("e=%.12f%n", e);
			System.out.printf("C_deg=%.12f%n",  C_deg);
			System.out.printf("L_sun=%.12f%n",  L_sun_deg);
			System.out.printf("nu_deg=%.12f%n", nu_deg);
			System.out.printf("R=%.12f%n", R);
			System.out.printf("omega_deg=%.12f%n", omega_deg);
			System.out.printf("lambda_deg=%.12f%n", lambda_deg);
			System.out.printf("epsilon_deg=%.12f%n", epsilon_deg);
			System.out.printf("alpha_deg=%.12f%n", alpha_deg);
			System.out.printf("alpha_hrs=%.12f%n", alpha_hrs);
			System.out.printf("delta_deg=%.12f%n", delta_deg);
			/*/
		}

		public String toString()
		{
			return String.format("%s, %s", HMS.decimal_hours_to_hms(right_ascension), DMS.ddeg_to_dms(declination));
		}
	}


	public static void main(String[] argv)
	{
		/*/
		// System.out.println(calendar_date_to_julian_day(2010, 1, 1, 0, 0, 0));
		// System.out.println(calendar_date_to_julian_day(2015, 3,21,12, 0, 0));
		// System.out.println(julian_day_to_calendar_date(2400000.5));
		// System.out.println(julian_day_to_calendar_date(calendar_date_to_julian_day(2015, 3, 21, 12, 23, 9.717)));
		// System.out.println(calendar_date_to_days_into_the_year(2005, 3, 9));
		// System.out.println(calendar_date_to_days_into_the_year(2000, 3, 9));
		// System.out.println(days_into_the_year_to_calendar_date(68, 2005, 13, 21, 4.333));
		// System.out.println(day_of_the_week(1985, 2, 7));
		// System.out.println(day_of_the_week_to_string(day_of_the_week(1985, 2, 7)));
		// System.out.println(local_civil_time_to_universal_time(hms_to_decimal_hours(11, 27, 34)));
		// System.out.println(local_civil_time_to_universal_time(hms_to_decimal_hours(11, 27, 34), TimeZone.getTimeZone("America/Denver")));
		// System.out.println(local_civil_time_to_universal_time(hms_to_decimal_hours(11, 27, 34), TimeZone.getTimeZone("America/Phoenix")));
		// System.out.println(local_civil_time_to_universal_time(hms_to_decimal_hours(11, 27, 34), 2025, 1, 1, TimeZone.getTimeZone("America/Los_Angeles")));
		// System.out.println(universal_time_to_local_civil_time(local_civil_time_to_universal_time(hms_to_decimal_hours(11, 30, 0), 2025, 1, 1, TimeZone.getTimeZone("America/Los_Angeles")), 2025, 1, 1, TimeZone.getTimeZone("America/Los_Angeles")));
		// System.out.println(new Date(2010, 2, 7, universal_time_to_greenwich_sidereal_time(hms_to_decimal_hours(23, 30, 0), 2010, 2, 7)));
		// System.out.println(new Date(2010, 2, 7, greenwich_sidereal_time_to_universal_time(hms_to_decimal_hours(8, 41, 53), 2010, 2, 7)));
		// Date gst = new Date(2010, 2, 7, universal_time_to_greenwich_sidereal_time(hms_to_decimal_hours(23, 30, 41), 2010, 2, 7));
		// System.out.println(new Date(2010, 2, 7, greenwich_sidereal_time_to_universal_time(gst.decimal_hour, 2010, 2, 7)));
		// System.out.println(decimal_hours_to_string(greenwich_sidereal_time_to_local_sidereal_time(hms_to_decimal_hours( 2,  3, 41), new Angle(40, 0, 0, Angle.W))));
		// System.out.println(decimal_hours_to_string(local_sidereal_time_to_greenwich_sidereal_time(hms_to_decimal_hours(23, 23, 41), new Angle(50, 0, 0, Angle.E))));
		// System.out.println(mean_anomaly_degrees(100.25, 365.2564));
		// System.out.println(equation_of_the_center_degrees(mean_anomaly_degrees(100.25, 365.2564), 0.0167));
		// System.out.println(true_anomaly_degrees(100.25, 365.2465, 0.0167));
		// System.out.println(true_anomaly_degrees(365.2465/2, 365.2465, 0.0167));
		// System.out.println(true_anomaly_tan_degrees(45, 0.5));
		// System.out.println(true_anomaly_cos_degrees(45, 0.5));
		// System.out.println(eccentric_anomaly_degrees(true_anomaly_cos_degrees(45, 0.5), 0.5));
		// System.out.println(keplers_equation_degrees(45, 0.5));
		// System.out.println(estimate_eccentric_anomaly_degrees       (keplers_equation_degrees(45, 0.5), 0.5));
		// System.out.println(estimate_eccentric_anomaly_newton_degrees(keplers_equation_degrees(45, 0.5), 0.5));
		// System.out.println(HMS.decimal_hours_to_hms(local_sidereal_time_to_hour_angle(hms_to_decimal_hours(18,0,0),hms_to_decimal_hours(3,24,6))));
		// System.out.println(HMS.decimal_hours_to_hms(hour_angle_to_local_sidereal_time(local_sidereal_time_to_hour_angle(hms_to_decimal_hours(18,0,0),hms_to_decimal_hours(3,24,6)),hms_to_decimal_hours(3,24,6))));
		// System.out.println(HMS.decimal_hours_to_hms(hour_angle_and_local_sidereal_time_to_right_ascension_decimal_hours(hms_to_decimal_hours(1,15,0),hms_to_decimal_hours(21,0,0))));

		System.out.println(horizontal_to_equatorial_coordinates(
				dms_to_decimal_degrees(40.0,0.0,0.0),	// altitude
				dms_to_decimal_degrees(115.0,0.0,0.0),	// azimuth
				dms_to_decimal_degrees(38.0,0.0,0.0),	// latitude
				hms_to_decimal_hours(0.0,0.0,0.0)));	// local sidereal time 

		System.out.println(horizontal_to_equatorial_coordinates(
				dms_to_decimal_degrees(0.0,0.0,0.0),	// altitude
				dms_to_decimal_degrees(0.0,0.0,0.0),	// azimuth
				dms_to_decimal_degrees(35.0,0.0,0.0),	// latitude
				hms_to_decimal_hours  (13.0,0.0,0.0)));	// local sidereal time 

		System.out.println(horizontal_to_equatorial_coordinates(
				dms_to_decimal_degrees(0.0,0.0,0.0),	// altitude
				dms_to_decimal_degrees(6.0,0.0,0.0),	// azimuth
				dms_to_decimal_degrees(35.0,0.0,0.0),	// latitude
				hms_to_decimal_hours  (13.0,0.0,0.0)));	// local sidereal time 

		System.out.println(horizontal_to_equatorial_coordinates(
				dms_to_decimal_degrees(0.0,0.0,0.0),	// altitude
				dms_to_decimal_degrees(12.0,0.0,0.0),	// azimuth
				dms_to_decimal_degrees(35.0,0.0,0.0),	// latitude
				hms_to_decimal_hours  (13.0,0.0,0.0)));	// local sidereal time 

		System.out.println(horizontal_to_equatorial_coordinates(
				dms_to_decimal_degrees(0.0,0.0,0.0),	// altitude
				dms_to_decimal_degrees(18.0,0.0,0.0),	// azimuth
				dms_to_decimal_degrees(35.0,0.0,0.0),	// latitude
				hms_to_decimal_hours  (13.0,0.0,0.0)));	// local sidereal time 

		System.out.println(equatorial_to_horizontal_coordinates(
				hms_to_decimal_hours  (16.0,29.0,45.0),		// hour angle
				dms_to_decimal_degrees(true,0.0,30.0,30.0),	// de_B1950
				dms_to_decimal_degrees(25.0,0.0,0.0),		// latitude
				hms_to_decimal_hours  (0.0,0.0,0.0)));		// local sidereal time 
		System.out.println(Ecliptic.obliquity(1, 0, 2010));
		System.out.println(Ecliptic.obliquity(1, 0, 2000));

		Ecliptic   e0 = new Ecliptic(dms_to_decimal_degrees(1,12,0), dms_to_decimal_degrees(184,36,0), 1, 0, 2000);
		Equatorial e1 = e0.to_equatorial();
		Ecliptic   e2 = e1.to_ecliptic(1, 0, 2000);
		System.out.println(e0);
		System.out.println(e1);
		System.out.println(e2);
		Galactic   g0 = new GalacticJ2000(dms_to_decimal_degrees(55,20,0), dms_to_decimal_degrees(180,0,0));
		Equatorial e0 = g0.to_equatorial();
		Galactic   g1 = e0.to_galactic_J2000();

		System.out.println(g0);
		System.out.println(e0);
		System.out.println(g1);

		Equatorial e1 = Galactic.equatorial_precession(2000);
		System.out.println(e1);
		
		// System.out.println((new Ecliptic(dms_to_decimal_degrees(1,12,0), dms_to_decimal_degrees(184,36,0), 1, 0, 2000)).to_equatorial());
		// System.out.println((new Equatorial(dms_to_decimal_degrees(12,18,47.5), dms_to_decimal_degrees(true,0,43,35.5))).to_ecliptic(1, 0, 2000));

		double ra_B1950 = hms_to_decimal_hours  (  5, 55, 0);
		double de_B1950     = dms_to_decimal_degrees(  7, 30, 0);
		double latitude        = dms_to_decimal_degrees( 38,  0, 0);
		double longitude       = dms_to_decimal_degrees(-78,  0, 0);
		TimeZone timezone      = TimeZone.getTimeZone("US/Eastern");
		Calendar c = Calendar.getInstance(timezone);
		c.set(2016, Calendar.JANUARY, 21, 0, 0, 0);
		long time_in_millis = c.getTimeInMillis();
		System.out.println(new RiseAndSetTime(ra_B1950, de_B1950, latitude, longitude, time_in_millis, timezone));

		TimeZone timezone = TimeZone.getTimeZone("US/Eastern");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1992, Calendar.OCTOBER, 12, 20, 0, 0);
		long time_in_millis = c.getTimeInMillis();
		System.out.println(new SolarLocation(time_in_millis, timezone));

		TimeZone timezone = TimeZone.getTimeZone("US/Eastern");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(2015, Calendar.JANUARY, 1, 22, 0, 0);
		long time_in_millis = c.getTimeInMillis();
		double latitude = 38;
		double longitude = -78;
		System.out.println(new LunarLocation(time_in_millis, latitude, longitude, timezone));

		TimeZone timezone = TimeZone.getTimeZone("US/Eastern");
		Calendar c = Calendar.getInstance(timezone);
		c.clear();
		c.set(1992, Calendar.APRIL, 11, 21, 0, 0);
		long time_in_millis = c.getTimeInMillis();
		double latitude = 38;
		double longitude = -78;
		System.out.println(new LunarLocation(time_in_millis, latitude, longitude, timezone));
		/*/
		
		Galactic   ga = new Galactic(0,0);
		Equatorial eq = ga.to_equatorial();
		System.out.println(eq);
	}
}
