package com.swift.restapi.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
    @Bean
    public PhysicalNamingStrategy namingStrategy() {
        return new PhysicalNamingStrategyStandardImpl() {
            @Override
            public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
                String snakeCaseName = toSnakeCase(name.getText() + "s");
                return Identifier.toIdentifier(snakeCaseName);
            }

            @Override
            public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
                String snakeCaseName = toSnakeCase(name.getText());
                return Identifier.toIdentifier(snakeCaseName);
            }
        };
    }

    private String toSnakeCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
