package org.barrikeit.util.constants;

public class ConfigurationConstants {
    private ConfigurationConstants() {}

    public static final String COMPONENT_PACKAGE_TO_SCAN = "org.barrikeit";
    public static final String CONFIG_PACKAGE_TO_SCAN = "org.barrikeit.config";
    public static final String WEBAPP_PACKAGE_TO_SCAN = "org.barrikeit.webapp";
    public static final String REST_PACKAGE_TO_SCAN = "org.barrikeit.controller";
    public static final String REPOSITORIES_PACKAGE_TO_SCAN = "org.barrikeit.model.repository";
    public static final String ENTITIES_PACKAGE_TO_SCAN = "org.barrikeit.model.domain";

    public static final String[] CONFIG_LOCATIONS = {"/", "/config/", "/configuration/"};
    public static final String[] CONFIG_EXTENSIONS = {"properties", "yml", "yaml"};


}
