package com.example.infinispansample.infinispan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Validated
@Component
@ConfigurationProperties(prefix = "infinispan")
@Data
public class InfinispanProperties {

    @NotEmpty
    private String dataLocation;

    @NotEmpty
    private String indexLocation;

    @Positive
    private long defaultCacheSize;

    @Positive
    private long routeDistanceCacheSize;

    @Positive
    private long routePathCacheSize;

    @Positive
    private long addressCacheSize;

    @Positive
    private long addressReverseCacheSize;
}
