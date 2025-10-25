package com.shop.util;

import java.util.logging.Logger;

public class SecurityLogger {
    private static final Logger logger = Logger.getLogger(SecurityLogger.class.getName());

    public static void logSecurityEvent(String event, String details) {
        logger.warning("SECURITY_EVENT: " + event + " - " + details);
    }

    public static void logSqlInjectionAttempt(String ip, String query) {
        logSecurityEvent("SQL_INJECTION_ATTEMPT", "IP: " + ip + ", Query: " + query);
    }

    public static void logXssAttempt(String ip, String input) {
        logSecurityEvent("XSS_ATTEMPT", "IP: " + ip + ", Input: " + input);
    }
}