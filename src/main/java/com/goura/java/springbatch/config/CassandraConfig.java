package com.goura.java.springbatch.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {
    @Value("${spring.data.cassandra.local-datacenter:datacenter1}")
    private String localDatacenter;

    @Bean
    public CqlSession cqlSession() {
        return CqlSession.builder()
                .withLocalDatacenter(localDatacenter)
                .build();
    }
}
