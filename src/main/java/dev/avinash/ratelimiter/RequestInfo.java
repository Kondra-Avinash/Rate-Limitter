package dev.avinash.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestInfo {
    AtomicInteger count;
    long timestamp;

    public RequestInfo() {
        this.count = new AtomicInteger(0);
        this.timestamp = System.currentTimeMillis();
    }
}
