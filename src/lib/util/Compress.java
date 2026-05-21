package lib.util;

/*******************************************************************************
 * Copyright (c) 2020-2025 Douglas M. Pase                                     *
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
import java.util.zip.*;

public class Compress {

	// deflate the input byte array and return the
	// deflated data as the resulting byte array
	public static byte[] deflate(byte[] data)
	{   
		// compress the data
		Deflater deflater = new Deflater();
		deflater.setInput(data); 
		deflater.finish(); 
 
		byte[] output = null;
		try {
			// copy the compressed data from the deflater to a ByteArrayOutputStream
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
			byte[] buffer = get_buf();
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
            free_buf(buffer);
			// outputStream.close(); 
 
			// copy the compressed data from the output stream to a byte array
			output = outputStream.toByteArray(); 
		} catch (Exception e) {
			e.printStackTrace();
		}   
 
		return output; 
	}   


	// deflate the input byte array and return the
	// deflated data as the resulting byte array
	public static byte[] compress(byte[] data)
	{
		return deflate(data);
	}
 
	
	// inflate the input byte array and return the
	// inflated data as the resulting byte array
	public static byte[] inflate(byte[] data)
    {   
		Inflater inflater = new Inflater(); 
		inflater.setInput(data); 
 
		byte[] output = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
            byte[] buffer = get_buf();
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            free_buf(buffer);
 
            output = outputStream.toByteArray(); 
        } catch (DataFormatException e) {
            e.printStackTrace();
        }   
        inflater.end(); 
 
        return output; 
    }
 

	// inflate the input byte array and return the
	// inflated data as the resulting byte array
	public static byte[] decompress(byte[] data)
	{
		return inflate(data);
	}

	
	// compress the file src_clr_file and store the resulting
	// compressed data in the file tgt_zip_file
	public static void copy_clr_to_zip(File src_clr_file, File tgt_zip_file) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read clear text from file, write it as compressed text
		InputStream  cis = new FileInputStream(src_clr_file);
		OutputStream zos = new GZIPOutputStream(new FileOutputStream(tgt_zip_file));
		for (int len=cis.read(buf); 0 < len; len=cis.read(buf)) {
			// write compressed text to the file
			zos.write(buf, 0, len);
		}
		cis.close();
		zos.close();
		
		free_buf(buf);
	}


	// compress the file src_clr_file and store the resulting
	// compressed data in the file tgt_zip_file
	public static void copy_clr_to_zip(String src_clr_file_name, String tgt_zip_file_name) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read clear text from file, write it as compressed text
		InputStream  cis = new FileInputStream(src_clr_file_name);
		OutputStream zos = new GZIPOutputStream(new FileOutputStream(tgt_zip_file_name));
		for (int len=cis.read(buf); 0 < len; len=cis.read(buf)) {
			// write compressed text to the file
			zos.write(buf, 0, len);
		}
		cis.close();
		zos.close();
		
		free_buf(buf);
	}

	
	public static void copy_clr_to_zip(InputStream is, String tgt_zip_file_name) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read clear text from file, write it as compressed text
		OutputStream zos = new GZIPOutputStream(new FileOutputStream(tgt_zip_file_name));
		for (int len=is.read(buf); 0 < len; len=is.read(buf)) {
			// write compressed text to the file
			zos.write(buf, 0, len);
		}
		zos.close();
		
		free_buf(buf);
	}

	
	public static void copy_clr_to_zip(String src_clr_file_name, OutputStream os) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read clear text from file, write it as compressed text
		InputStream  cis = new FileInputStream(src_clr_file_name);
		OutputStream zos = new GZIPOutputStream(os);
		for (int len=cis.read(buf); 0 < len; len=cis.read(buf)) {
			// write compressed text to the file
			zos.write(buf, 0, len);
		}
		cis.close();
		zos.close();
		
		free_buf(buf);
	}

	
	public static void copy_clr_to_zip(InputStream is, OutputStream os) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read clear text from file, write it as compressed text
		OutputStream zos = new GZIPOutputStream(os);
		for (int len=is.read(buf); 0 < len; len=is.read(buf)) {
			// write compressed text to the file
			zos.write(buf, 0, len);
		}
		zos.close();
		
		free_buf(buf);
	}
	

	public static void copy_zip_to_clr(File src_zip_file, File tgt_clr_file) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read compressed text from file, write it as clear text
		GZIPInputStream  zis = new GZIPInputStream(new FileInputStream(src_zip_file));
		FileOutputStream cos = new FileOutputStream(tgt_clr_file);
		for (int len=zis.read(buf); 0 < len; len=zis.read(buf)) {
			// write clear text to the file
			cos.write(buf, 0, len);
		}
		zis.close();
		cos.close();
		
		free_buf(buf);
	}

	
	public static void copy_zip_to_clr(String src_zip_file_name, String tgt_clr_file_name) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read compressed text from file, write it as clear text
		GZIPInputStream  zis = new GZIPInputStream(new FileInputStream(src_zip_file_name));
		FileOutputStream cos = new FileOutputStream(tgt_clr_file_name);
		for (int len=zis.read(buf); 0 < len; len=zis.read(buf)) {
			// write clear text to the file
			cos.write(buf, 0, len);
		}
		zis.close();
		cos.close();
		
		free_buf(buf);
	}
	
	
	public static void copy_zip_to_clr(InputStream is, String tgt_clr_file_name) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read compressed text from file, write it as clear text
		GZIPInputStream  zis = new GZIPInputStream(is);
		FileOutputStream cos = new FileOutputStream(tgt_clr_file_name);
		for (int len=zis.read(buf); 0 < len; len=zis.read(buf)) {
			// write clear text to the file
			cos.write(buf, 0, len);
		}
		zis.close();
		cos.close();
		
		free_buf(buf);
	}
	
	
	public static void copy_zip_to_clr(String src_zip_file_name, OutputStream os) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read compressed text from file, write it as clear text
		GZIPInputStream  zis = new GZIPInputStream(new FileInputStream(src_zip_file_name));
		for (int len=zis.read(buf); 0 < len; len=zis.read(buf)) {
			// write clear text to the file
			os.write(buf, 0, len);
		}
		zis.close();
		
		free_buf(buf);
	}
	
	
	public static void copy_zip_to_clr(InputStream is, OutputStream os) throws FileNotFoundException, IOException
	{
		byte[] buf = get_buf();
		
		// read compressed text from file, write it as clear text
		GZIPInputStream  zis = new GZIPInputStream(is);
		for (int len=zis.read(buf); 0 < len; len=zis.read(buf)) {
			// write clear text to the file
			os.write(buf, 0, len);
		}
		zis.close();
		
		free_buf(buf);
	}

	private static final int buf_size = 64 * 1024 * 1024;
	private static byte[] recv_buf    = null;
	private static Object gl          = new Object();

	private static byte[] get_buf()
	{
    	byte[] buf = null;
    	synchronized(gl) {
    		if (recv_buf == null) {
    			buf = new byte[buf_size];
    		} else {
    			buf = recv_buf;
    			recv_buf = null;
    		}
    	}
    	
    	return buf;
	}
	
	private static void free_buf(byte[] buf)
	{
		if (buf != null) {
	    	synchronized(gl) {
	    		recv_buf = buf;
	    	}
		}
	}


	/*
    public static void main(String[] args) 
    {   
        byte[] data = "fsdfesfsfdddddddsfdsfssdfdsfdsfdsfdsfdsdfgggggggggggggggggggggggggggggggggg".getBytes();
        byte[] comp = compress(data);
        byte[] rcon = decompress(comp);

        System.out.println("Original     : " + data.length); 
        System.out.println("Compressed   : " + comp.length); 
        System.out.println("Reconstructed: " + rcon.length); 
        System.out.println("Equals       : " + (new String(rcon)).equals(new String(data))); 
    }
    */
}
