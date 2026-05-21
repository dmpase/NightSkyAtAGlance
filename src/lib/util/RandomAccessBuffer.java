package lib.util;

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

public class RandomAccessBuffer {

	public RandomAccessBuffer()
	{
		contents = new byte[ PAGE_SIZE ][][];
		for (int i=0; i < contents.length; i++) {
			contents[i] = null;
		}
	}

	public RandomAccessBuffer(byte[] b)
	{
		contents = new byte[ PAGE_SIZE ][][];
		for (int i=0; i < contents.length; i++) {
			contents[i] = null;
		}

		if (b != null) {
			init(b, 0, b.length);
		}
	}

	public RandomAccessBuffer(byte[] b, int off, int len)
	{
		contents = new byte[ PAGE_SIZE ][][];
		for (int i=0; i < contents.length; i++) {
			contents[i] = null;
		}
		
		if (b != null) {
			init(b, off, len);
		}
	}

	private void init(byte[] b, int off, int len)
	{
		check_range(0, len);
		
		// copy the data from b to contents
		for (int i=0; b != null && i < len; i+=PAGE_SIZE) {
			// copy the next page
			int L0 = L0(i);
			int L1 = L1(i);
			for (int L2=0; L2 < PAGE_SIZE && (off+i+L2) < b.length && (i+L2) < len; L2++) {
				contents[L0][L1][L2] = b[off+i+L2];
			}
		}
	}

	public void close()
	{
	}

	public long getFilePointer()
	{
		return file_pointer;
	}

	public long length()
	{
		return length;
	}

	public int read()
	{
		// make sure the buffer is allocated for this range
		check_range(file_pointer, 1);

		// compute the array indexes
		int L0 = L0(file_pointer);
		int L1 = L1(file_pointer);
		int L2 = L2(file_pointer);

		// fetch the contents
		int result = contents[L0][L1][L2];
		
		// advance the file pointer
		file_pointer += 1;

		// return the data
		return result;
	}

	public int read(byte[] b)
	{
		int bytes_read = 0;
		
		if (b != null) {
			bytes_read = read(b, 0, b.length);
		}
		
		return bytes_read;
	}

	public int read(byte[] b, int off, int len)
	{
		// make sure the buffer is allocated for this range
		check_range(file_pointer, len);

		int bytes_read = 0;
		for (int i=0; b != null && (off+i) < b.length && i < len && file_pointer < length; i++) {
			int L0 = L0(file_pointer);
			int L1 = L1(file_pointer);
			int L2 = L2(file_pointer);

			b[off+i] = contents[L0][L1][L2];

			file_pointer += 1;
			bytes_read   += 1;
		}

		return bytes_read;
	}
	
	public boolean readBoolean()
	{
		byte[] b = new byte[SIZEOF_BOOLEAN];
		read(b, 0, b.length);
		return ByteArray.to_boolean(b);
	}
	
	public byte readByte()
	{
		byte[] b = new byte[SIZEOF_BYTE];
		read(b, 0, b.length);
		return ByteArray.to_byte(b);
	}

	public char readChar()
	{
		byte[] b = new byte[SIZEOF_CHAR];
		read(b, 0, b.length);
		return ByteArray.to_char(b);
	}
	
	public double readDouble()
	{
		byte[] b = new byte[SIZEOF_DOUBLE];
		read(b, 0, b.length);
		return ByteArray.to_double(b);
	}

	public float readFloat()
	{
		byte[] b = new byte[SIZEOF_FLOAT];
		read(b, 0, b.length);
		return ByteArray.to_float(b);
	}
	
	public int readInt()
	{
		byte[] b = new byte[SIZEOF_INT];
		read(b, 0, b.length);
		return ByteArray.to_int(b);
	}
	
	public long readLong()
	{
		byte[] b = new byte[SIZEOF_LONG];
		read(b, 0, b.length);
		return ByteArray.to_long(b);
	}
	
	public short readShort()
	{
		byte[] b = new byte[SIZEOF_SHORT];
		read(b, 0, b.length);
		return ByteArray.to_short(b);
	}

	public int readUnsignedByte()
	{
		byte[] b = new byte[SIZEOF_BYTE];
		read(b, 0, b.length);
		int result = ByteArray.to_byte(b);
		result = result & 0x000000FF;

		return result;
	}

	public String readUTF()
	{
		String result = null;
		short len = readShort();
		if (len == NULL) {
			;
		} else if (len == 0) {
			result = "";
		} else {
			byte[] b = new byte[len];
			int r = read(b, 0, b.length);
			result = new String(b, 0, r);
		}

		return result;
	}

	@SuppressWarnings("unused")
	public byte[] readByteArray()
	{
		byte[] result = null;
		int len = readInt();
		if (len == NULL) {
			;
		} else if (len == 0) {
			result = new byte[0];
		} else {
			byte[] b = new byte[len];
			int r = read(b, 0, b.length);
			result = b;
		}

		return result;
	}

	public long seek(long pos)
	{
		long old = file_pointer;
		file_pointer = pos;

		return old;
	}

	public void setLength(long len)
	{
		if (len < 0) {
			file_pointer = length = 0;
		} else if (len < file_pointer) {
			file_pointer = length = len;
		} else {
			length = len;
		}
	}

	public int skipBytes(int n)
	{
		long remaining = length - file_pointer;
		long skipped = (remaining < n) ? remaining : n;
		skipped = (skipped < 0) ? 0 : skipped;
		file_pointer += skipped;

		return (int) skipped;
	}
	
	public void write(byte v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}
	
	public void write(byte[] b)
	{
		if (b != null) {
			write(b, 0, b.length);
		}
	}

	public synchronized void write(byte[] b, int off, int len)
	{
		// make sure the buffer is allocated for this range
		check_range(file_pointer, len);

		// write the bytes
		for (int i=0; b != null && (off+i) < b.length && i < len; i++) {
			int L0 = L0(file_pointer);
			int L1 = L1(file_pointer);
			int L2 = L2(file_pointer);

			contents[L0][L1][L2] = b[off+i];

			file_pointer += 1;
		}

		// update the file length, if needed
		length = (file_pointer < length) ? length : file_pointer;
	}
	
	public void writeBoolean(boolean v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}
	
	public void writeByte(byte v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}
	
	public void writeByte(char v)
	{
		byte[] b = ByteArray.to_bytes((byte)v);
		write(b, 0, b.length);
	}
	
	public void writeByte(short v)
	{
		byte[] b = ByteArray.to_bytes((byte)v);
		write(b, 0, b.length);
	}
	
	public void writeByte(int v)
	{
		byte[] b = ByteArray.to_bytes((byte)v);
		write(b, 0, b.length);
	}
	
	public void writeByte(long v)
	{
		byte[] b = ByteArray.to_bytes((byte)v);
		write(b, 0, b.length);
	}

	public void writeChar(char v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}

	public void writeDouble(double v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}

	public void writeFloat(float v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}

	public void writeFloat(double v)
	{
		byte[] b = ByteArray.to_bytes((float)v);
		write(b, 0, b.length);
	}

	public void writeInt(int v)
	{
		byte[] b = ByteArray.to_bytes((int)v);
		write(b, 0, b.length);
	}

	public void writeInt(long v)
	{
		byte[] b = ByteArray.to_bytes((int)v);
		write(b, 0, b.length);
	}

	public void writeLong(long v)
	{
		byte[] b = ByteArray.to_bytes(v);
		write(b, 0, b.length);
	}

	public void writeShort(short v)
	{
		byte[] b = ByteArray.to_bytes((short)v);
		write(b, 0, b.length);
	}

	public void writeShort(int v)
	{
		byte[] b = ByteArray.to_bytes((short)v);
		write(b, 0, b.length);
	}

	public void writeShort(long v)
	{
		byte[] b = ByteArray.to_bytes((short)v);
		write(b, 0, b.length);
	}

	public void writeUTF(String str)
	{
		if (str == null) {
			byte[] strlen = ByteArray.to_bytes((short)NULL);
			write(strlen, 0, strlen.length);
		} else if (str.equals("")) {
			byte[] strlen = ByteArray.to_bytes((short)0);
			write(strlen, 0, strlen.length);
		} else {
			byte[] strlen = ByteArray.to_bytes((short)str.length());
			write(strlen, 0, strlen.length);

			byte[] b = str.getBytes();
			write(b, 0, b.length);
		}
	}

	public void writeByteArray(byte[] b)
	{
		if (b == null) {
			byte[] len = ByteArray.to_bytes((int)NULL);
			write(len, 0, len.length);
		} else if (b.length == 0) {
			byte[] len = ByteArray.to_bytes((int)0);
			write(len, 0, len.length);
		} else {
			byte[] len = ByteArray.to_bytes((int)b.length);
			write(len, 0, len.length);
			write(b, 0, b.length);
		}
	}

    private              long       length         =  0;
    private              long       file_pointer   =  0;
	private              byte[][][] contents       = null;
    private static final int        BITS_PER_LEVEL = 10;
    private static final int        PAGE_SIZE      = (1 << BITS_PER_LEVEL);
    private static final int        PAGE_MASK      = PAGE_SIZE - 1;

    private static final int        SIZEOF_BOOLEAN = 1;
    private static final int        SIZEOF_BYTE    = 1;
    private static final int        SIZEOF_CHAR    = 2;
    private static final int        SIZEOF_SHORT   = 2;
    private static final int        SIZEOF_INT     = 4;
    private static final int        SIZEOF_LONG    = 8;
    private static final int        SIZEOF_FLOAT   = 4;
    private static final int        SIZEOF_DOUBLE  = 8;

    private static final int        NULL           = ~0;

    private synchronized void check_range(long pos, int len)
    {
		if (0 <= pos) {	
			for (int idx=(int) pos; idx < pos+len; idx+=PAGE_SIZE) {
				check_page(idx);
			}
			check_page(pos+len-1);
		}
    }

    private synchronized void check_page(long off)
    {
		if (0 <= off) {	
			int L0 = L0(off);
			int L1 = L1(off);

			if (contents[L0] == null) {
				contents[L0] = new byte[ PAGE_SIZE ][];
				for (int i=0; i < contents[L0].length; i++) {
					contents[L0][i] = null;
				}
			}
		
			if (contents[L0][L1] == null) {
			    contents[L0][L1] = new byte[ PAGE_SIZE ];
				for (int i=0; i < contents[L0][L1].length; i++) {
					contents[L0][L1][i] = 0;
				}
			}
		}
    }
    
    private static final int L0(long pos)
    {
    	return (int) (pos >> (2 * BITS_PER_LEVEL)) & PAGE_MASK;
    }
    
    private static final int L1(long pos)
    {
    	return (int) (pos >> (1 * BITS_PER_LEVEL)) & PAGE_MASK;
    }
    
    private static final int L2(long pos)
    {
    	return (int) (pos >> (0 * BITS_PER_LEVEL)) & PAGE_MASK;
    }
    
    /*
    public static void main(String[] args)
    {
    	RandomAccessBuffer rab = new RandomAccessBuffer();

    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    	rab.writeInt(42);
    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    	rab.writeUTF("Hello, world!");
    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    	rab.seek(0);
    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    	System.out.println(rab.readInt());
    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    	System.out.println(rab.readUTF());
    	System.out.println("length="+rab.length+" pos="+rab.file_pointer);
    }
    */
}
