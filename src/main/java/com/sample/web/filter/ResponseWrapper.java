package com.sample.web.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream bos;
    private final long id;
    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseWrapper(HttpServletResponse response, long id) {
        super(response);
        bos = new ByteArrayOutputStream();
        this.id = id;
    }

    public long getId(){
        return id;
    }

    @Override
    public ServletResponse getResponse() {
        return this;
    }

    @Override
    public PrintWriter getWriter(){
        return new PrintWriter(bos);
    }

    @Override
    public String toString(){
        return bos.toString();
    }

    public byte[] toByteArray(){
        return bos.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream(){
        return new ServletOutputStream(){

            @Override
            public void write(int b) throws IOException {
                bos.write(b);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }
        };
    }
}
