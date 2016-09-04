package com.sample.web.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Arrays;

/**
 * Created by ankit.c on 22/06/16.
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private final ByteArrayInputStream bos;
    private final byte buf[];
    private final long id;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request, long id) throws IOException {
        super(request);
        this.id = id;
        buf = IOUtils.toByteArray(request.getReader());
        bos = new ByteArrayInputStream(buf);
    }

    public long getId(){
        return id;
    }

    public byte[] getBytes(){
        return Arrays.copyOf(buf, buf.length);
    }

    public ServletInputStream getInputStream(){
        return new ByteArrayServletInputStream(bos);
    }

    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(bos));
    }

    private class ByteArrayServletInputStream extends ServletInputStream {
        private InputStream inputStream;

        public ByteArrayServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }
}
