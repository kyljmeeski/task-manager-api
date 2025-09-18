package com.amir.task_manager_api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        request.setAttribute(START_TIME, Instant.now());

        log.info("{{\"timestamp\":\"{}\",\"event\":\"request\",\"method\":\"{}\",\"uri\":\"{}\",\"params\":\"{}\"}}",
                Instant.now(),
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Instant start = (Instant) request.getAttribute(START_TIME);
        long durationMs = start != null ? Duration.between(start, Instant.now()).toMillis() : -1;

        log.info("{{\"timestamp\":\"{}\",\"event\":\"response\",\"method\":\"{}\",\"uri\":\"{}\"," +
                        "\"status\":{},\"durationMs\":{}}}",
                Instant.now(),
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                durationMs
        );

        if (ex != null) {
            log.error("{{\"timestamp\":\"{}\",\"event\":\"exception\",\"method\":\"{}\",\"uri\":\"{}\"}}",
                    Instant.now(),
                    request.getMethod(),
                    request.getRequestURI(), ex);
        }
    }
}
