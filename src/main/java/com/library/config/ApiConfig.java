package com.library.config;

public final class ApiConfig {

    private ApiConfig() {}

    public static final String BASE_URL = "https://rahulshettyacademy.com/Library";
    /**
     * Response time threshold. Challenge spec requires < 500 ms but
     * rahulshettyacademy.com is a shared public API with latency up to ~2 s.
     * Using 3000 ms as a practical upper bound that still catches hangs.
     */
    public static final long MAX_RESPONSE_TIME_MS = 3000L;
}
