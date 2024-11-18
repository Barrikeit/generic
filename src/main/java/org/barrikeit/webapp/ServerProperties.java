package org.barrikeit.webapp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ServerProperties {
    @Value("${server.port}")
    private int port;

    @Value("${server.name}")
    private String name;

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${server.apiPath}")
    private String apiPath;

    @Value("${server.module}")
    private String module;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${server.servlet.encoding.force-response}")
    private boolean forceResponse;
}