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


    private Map<String, AtomicInteger> numberofipaddress = new ConcurrentHashMap<>();
    private int MAX_NUMBEROF_REQUESTS = 5;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ipAddress = request.getRemoteAddr();

        numberofipaddress.putIfAbsent(ipAddress, new AtomicInteger(0));
        AtomicInteger atomicInteger = numberofipaddress.get(ipAddress);

        int requests = atomicInteger.incrementAndGet();

        if(requests > MAX_NUMBEROF_REQUESTS){
            response.setStatus(response.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("Too many Requests");

            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
