package dev.avinash.ratelimiter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimittingFilet implements Filter {


    private Map<String, RequestInfo> numberofipaddress = new ConcurrentHashMap<>();
    private int MAX_NUMBEROF_REQUESTS = 5;
    private long Time_Window = 60_000;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ipAddress = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        numberofipaddress.putIfAbsent(ipAddress, new RequestInfo());

        RequestInfo requestInfo = numberofipaddress.get(ipAddress);

        if(now - requestInfo.timestamp > Time_Window){
            requestInfo.count.set(0);
            requestInfo.timestamp = now;
        }
        int current = requestInfo.count.incrementAndGet();

        if (current > MAX_NUMBEROF_REQUESTS) {
            response.setStatus(response.SC_BAD_REQUEST);
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
