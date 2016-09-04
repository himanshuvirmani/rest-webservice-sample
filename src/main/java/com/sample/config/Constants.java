package com.sample.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String HEALTH_CHECK_URI="/health";
    public static final String SWAGGER_UI_URI="/swagger-ui";
    public static final String SWAGGER_WEBJARS_URI="/webjars";

    private Constants() {
    }
}
