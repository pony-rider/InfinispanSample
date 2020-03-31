package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.*;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.persistence.sifs.configuration.SoftIndexFileStoreConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.util.concurrent.IsolationLevel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class GisServiceCacheConfig {
    public static final String CACHE_MANAGER_NAME = "gis-service";
    public static final String PROVIDER_CLASS_CACHE_NAME = CACHE_MANAGER_NAME + "/provider-class";
    public static final String ROUTE_DISTANCE_CACHE_NAME = CACHE_MANAGER_NAME + "/routeDistance";
    public static final String ROUTE_PATH_CACHE_NAME = CACHE_MANAGER_NAME + "/routePath";
    public static final String ADDRESS_CACHE_NAME = CACHE_MANAGER_NAME + "/address";
    public static final String ADDRESS_REVERSE_CACHE_NAME = CACHE_MANAGER_NAME + "/addressReverse";
    private final InfinispanProperties properties;

    public GisServiceCacheConfig(InfinispanProperties infinispanProperties) {
        properties = infinispanProperties;
    }

    private org.infinispan.configuration.cache.Configuration configurationTemplate(String cacheName) {
        return new ConfigurationBuilder()
                .persistence()
                .passivation(false)
                .addStore(SoftIndexFileStoreConfigurationBuilder.class)
                .dataLocation(Paths.get(properties.getDataLocation()).resolveSibling(cacheName + "Data").toString())
                .indexLocation(Paths.get(properties.getIndexLocation()).resolveSibling(cacheName + "Index").toString())
                .async()
                .threadPoolSize(1)
                .modificationQueueSize(100)
                .transactional(false)
                .preload(false)
                .purgeOnStartup(false)
                .locking()
                .isolationLevel(IsolationLevel.READ_COMMITTED)
                .useLockStriping(false)
                .concurrencyLevel(30)
                .transaction()
                .lockingMode(LockingMode.OPTIMISTIC)
                .memory()
                .size(properties.getDefaultCacheSize())
                .evictionType(EvictionType.COUNT)
                .expiration()
                .lifespan(30, TimeUnit.DAYS)
                .build();
    }

    @Bean(name = CACHE_MANAGER_NAME)
    public EmbeddedCacheManager getCacheManager(GlobalConfigurationBuilder builder) {
        return new DefaultCacheManager(builder.build());
    }

    @Bean(name = PROVIDER_CLASS_CACHE_NAME)
    public Cache<?, ?> getProviderClassCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(PROVIDER_CLASS_CACHE_NAME, new ConfigurationBuilder().build());
        return cacheManager.getCache(PROVIDER_CLASS_CACHE_NAME);
    }


    @Bean(name = ROUTE_DISTANCE_CACHE_NAME)
    public Cache<DistanceCacheKey, Distance> getRouteDistanceCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ROUTE_DISTANCE_CACHE_NAME, new ConfigurationBuilder()
                .read(configurationTemplate(GisServiceCacheConfig.ROUTE_DISTANCE_CACHE_NAME))
                .persistence().stores().get(0)
                .async()
                .threadPoolSize(3)
                .modificationQueueSize(1000)
                .memory().size(properties.getRouteDistanceCacheSize()).evictionType(EvictionType.COUNT)
                .build());
        return cacheManager.getCache(ROUTE_DISTANCE_CACHE_NAME);
    }


    @Bean(name = ROUTE_PATH_CACHE_NAME)
    public Cache<RouteRequestRecordPK, List<GeoLocationRecord>> getRoutePathCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ROUTE_PATH_CACHE_NAME, new ConfigurationBuilder()
                .read(configurationTemplate(GisServiceCacheConfig.ROUTE_PATH_CACHE_NAME))
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .hash().numOwners(1)
                .memory().size(properties.getRoutePathCacheSize())
                .build());
        return cacheManager.getCache(ROUTE_PATH_CACHE_NAME);
    }

    @Bean
    public Cache<ForwardGeocodingPK, GeoAddress> getAddressCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ADDRESS_CACHE_NAME, new ConfigurationBuilder()
                .read(configurationTemplate(GisServiceCacheConfig.ADDRESS_CACHE_NAME))
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .hash().numOwners(1)
                .memory().size(properties.getAddressCacheSize())
                .build());
        return cacheManager.getCache(ADDRESS_CACHE_NAME);
    }

    @Bean
    public Cache<ReverseGeocodingPK, GeoAddress> getAddressReverseCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ADDRESS_REVERSE_CACHE_NAME, new ConfigurationBuilder()
                .read(configurationTemplate(GisServiceCacheConfig.ADDRESS_REVERSE_CACHE_NAME))
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .hash().numOwners(1)
                .memory().size(properties.getAddressReverseCacheSize())
                .build());
        return cacheManager.getCache(ADDRESS_REVERSE_CACHE_NAME);
    }
}
