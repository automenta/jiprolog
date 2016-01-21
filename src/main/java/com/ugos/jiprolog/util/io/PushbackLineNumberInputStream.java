package com.ugos.jiprolog.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PushbackLineNumberInputStream extends FilterInputStream {

	private byte[] buf;

    private int pos;

    private int lineNumber;
    private int colNumber;
    private int read;
    private boolean skipLF;

    private PushbackLineNumberInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("size <= 0");
        }

        this.buf = new byte[size];
        this.pos = size;

        int colNumber1 = -1;
        this.colNumber = colNumber1;
        this.lineNumber = 0;
        this.read = 0;
    }

    public PushbackLineNumberInputStream(InputStream in) {
        this(in, 1);
    }

    public int read() throws IOException
    {
        if (pos < buf.length)
        {
            return buf[pos++] & 0xff;
        }

        int c = super.read();

        if (skipLF)
        {
            if (c == '\n')
            {
                colNumber++;
                read++;
                c = super.read();
            }

            skipLF = false;
        }

        switch (c)
        {
            case '\r':
                skipLF = true;
                c = '\n';
            case '\n':      /* Fall through */
                lineNumber++;
                colNumber = -1;
        }

	    if(c > -1)
	    {
	        colNumber++;
	        read++;
	    }

        return c;
    }

    public void unread(int b) throws IOException
    {
        if (pos == 0) {
            throw new IOException("Push back buffer is full");
        }

        if (b != -1)
            buf[--pos] = (byte)b;
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * Once the stream has been closed, further read(), unread(),
     * available(), reset(), or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void close() throws IOException {
        if (in == null)
            return;
        in.close();
        in = null;
        buf = null;
    }

	public int getLineNumber() {
		return lineNumber;
	}

	private void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	private void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	public int getRead() {
		return read;
	}

	private void setRead(int read) {
		this.read = read;
	}
}
