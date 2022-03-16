package com.yourcompany.phonebooking;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;

public class Postgres {
    public static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:13.4");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            Startables.deepStart(CONTAINER).join();

            context.getEnvironment()
                    .getPropertySources()
                    .addFirst(new MapPropertySource("testcontainers",
                            Map.of("spring.datasource.url",
                                    CONTAINER.getJdbcUrl(),
                                    "spring.datasource.username",
                                    CONTAINER.getUsername(),
                                    "spring.datasource.password",
                                    CONTAINER.getPassword())));
        }
    }
}
