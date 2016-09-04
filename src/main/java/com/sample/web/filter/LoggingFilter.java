package com.sample.web.filter;

import com.sample.config.Constants;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

public class LoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String NOTIFICATION_PREFIX = "* ";
    private static final String REQUEST_PREFIX = "> ";
    private static final String RESPONSE_PREFIX = "< ";
    private static final int maxOutputLength = 8192;
    private AtomicLong id = new AtomicLong(1);

    public LoggingFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (!httpServletRequest.getRequestURI().equals(Constants.HEALTH_CHECK_URI) &&
                !httpServletRequest.getRequestURI().contains(Constants.SWAGGER_UI_URI) &&
                !httpServletRequest.getRequestURI().contains(Constants.SWAGGER_WEBJARS_URI)) {
            PrintWriter out = response.getWriter();
            long requestId = this.id.getAndIncrement();
            MDC.put("request-id", requestId);
            RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request, requestId);
            logRequest(requestWrapper);

            ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response, requestId);

            long startTime = System.currentTimeMillis();
            chain.doFilter(requestWrapper, responseWrapper);
            logAPITime(requestWrapper, System.currentTimeMillis() - startTime);
            logStatusCode(requestWrapper, responseWrapper);
            logResponse(responseWrapper);

            out.write(responseWrapper.toString());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public void logAPITime(RequestWrapper request, long time) {
        StringBuilder b = new StringBuilder();
        b.append("API Response Time for ")
                .append(request.getMethod()).append(" ")
                .append(LoggingFilter.getUriWithQueryParam(request))
                .append(" : ")
                .append(time).append(" ms");
        log.info("{}", b.toString());
    }

    public void logStatusCode(RequestWrapper request, ResponseWrapper response) {
        StringBuilder b = new StringBuilder();
        b.append("API Response Code for ")
                .append(request.getMethod()).append(" ")
                .append(LoggingFilter.getUriWithQueryParam(request))
                .append(" : ")
                .append(response.getStatus());
        log.info("{}", b.toString());
    }

    public void logRequest(RequestWrapper wrapper) {
        StringBuilder b = new StringBuilder();
        printRequestLine(b, wrapper);
        printRequestHeaders(b, wrapper);
        String charEncoding = wrapper.getCharacterEncoding();
        if (charEncoding == null)
            charEncoding = "UTF-8";
        if (!isMultipart(wrapper) && !isBinaryContent(wrapper)) {
            try {
                prefixId(b, wrapper.getId()).append(REQUEST_PREFIX)
                        .append("Payload: ")
                        .append(new String(wrapper.getBytes(), charEncoding)).append('\n');
            } catch (UnsupportedEncodingException e) {
                log.error("Error while logging request");
                return;
            }
        }
        log.info("{}", b.toString());
    }

    public void logResponse(ResponseWrapper wrapper) {
        StringBuilder b = new StringBuilder();
        printResponseLine(b, wrapper);
        printResponseHeaders(b, wrapper);
        String response = wrapper.toString();
        prefixId(b, wrapper.getId()).append(RESPONSE_PREFIX).append("Response-data-length : ")
                .append(response.length()).append('\n');

        if (response.length() > maxOutputLength) {
            prefixId(b, wrapper.getId()).append(RESPONSE_PREFIX)
                    .append("Too much response data, truncating ....... ").append('\n');
            prefixId(b, wrapper.getId()).append(RESPONSE_PREFIX).append("Response-data : ");
            b.append(response.substring(0, maxOutputLength)).append('\n');
        } else {
            prefixId(b, wrapper.getId()).append(RESPONSE_PREFIX).append("Response-data : ");
            b.append(response).append('\n');
        }
        log.info("{}", b.toString());
    }

    private void printRequestLine(StringBuilder b, RequestWrapper request) {
        prefixId(b, request.getId()).append(NOTIFICATION_PREFIX).append("Server in-bound request").append('\n');
        prefixId(b, request.getId()).append(REQUEST_PREFIX)
                .append(request.getMethod()).append(" ")
                .append(LoggingFilter.getUriWithQueryParam(request)).append('\n');
    }

    private void printRequestHeaders(StringBuilder b, RequestWrapper request) {
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = (String) headers.nextElement();
            prefixId(b, request.getId())
                    .append(REQUEST_PREFIX)
                    .append(header).append(": ")
                    .append(request.getHeader(header)).append('\n');
        }
    }

    private void printResponseLine(StringBuilder b, ResponseWrapper response) {
        prefixId(b, response.getId()).append(NOTIFICATION_PREFIX).append("Server out-bound response").append('\n');
        prefixId(b, response.getId()).append(RESPONSE_PREFIX).append(response.getStatus()).append('\n');
    }

    private void printResponseHeaders(StringBuilder b, ResponseWrapper response) {
        Collection<String> headers = response.getHeaderNames();
        for (String header : headers) {
            prefixId(b, response.getId()).append(RESPONSE_PREFIX)
                    .append(header).append(": ")
                    .append(response.getHeader(header)).append('\n');
        }
    }

    private StringBuilder prefixId(StringBuilder b, long id) {
        b.append("request-id=").append(id).append(" ");
        return b;
    }

    private boolean isBinaryContent(final HttpServletRequest request) {
        if (request.getContentType() == null) {
            return true;
        }
        return request.getContentType().contains("image") ||
                request.getContentType().contains("video") ||
                request.getContentType().contains("audio");
    }

    private boolean isMultipart(final HttpServletRequest request) {
        if (request.getContentType() == null)
            return true;
        return request.getContentType().contains("multipart/form-data");
    }

    public static String getUriWithQueryParam(HttpServletRequest request) {
        StringBuilder b = new StringBuilder();
        b.append(request.getRequestURI());

        String query = request.getQueryString();
        if (query != null) {
            b.append('?').append(query);
        }

        return b.toString();
    }
}
