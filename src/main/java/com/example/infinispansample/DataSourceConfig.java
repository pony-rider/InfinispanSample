package com.example.infinispansample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration
//@ConfigurationProperties(prefix = "spring.datasource") does not work, why?
public class DataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;


    @Bean
    //@ConfigurationProperties("spring.datasource")
    public DataSource getDatasource() {
        System.out.println(dataSourceProperties);
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dataSourceProperties.getUrl());
        dataSourceBuilder.username(dataSourceProperties.getUsername());
        dataSourceBuilder.password(dataSourceProperties.getPassword());
        //return new HikariDataSource(dataSourceBuilder.build());
        /*dataSourceBuilder.type(HikariDataSource.class);
        HikariDataSource hikariDataSource = (HikariDataSource) dataSourceBuilder.build();
        hikariDataSource.setMaximumPoolSize();*/
        return dataSourceBuilder.build();
    }
}
