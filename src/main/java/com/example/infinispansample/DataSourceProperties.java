package com.example.infinispansample;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


//@Component
@ConfigurationProperties(prefix = "spring.datasource")
@ConstructorBinding
public class DataSourceProperties {
        private String url;
        private String username;
        private String password;
        private Integer hikariMaximumPoolSize;
        private Integer hikariConnectionTimeout;

    public DataSourceProperties(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DataSourceProperties{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
