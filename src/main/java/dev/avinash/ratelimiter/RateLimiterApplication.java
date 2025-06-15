package dev.avinash.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RateLimiterApplication {

    public static void main(String[] args) {

        SpringApplication.run(RateLimiterApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<RateLimittingFilet> filterRegistrationBean() {
        FilterRegistrationBean<RateLimittingFilet> filter = new FilterRegistrationBean<>();

        filter.setFilter(new RateLimittingFilet());
        filter.addUrlPatterns("/api/*");

        return filter;
    }

}
