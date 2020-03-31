package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.BufferBucket;
import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.util.concurrent.IsolationLevel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingCacheConfig {
    public static final String CACHE_MANAGER_NAME = "tracking";
    public static final String BUFFER_CACHE_NAME = CACHE_MANAGER_NAME + "/buffer";
    public static final String DIST_CACHE_NAME = CACHE_MANAGER_NAME + "/dist";

    @Bean(name = CACHE_MANAGER_NAME)
    public EmbeddedCacheManager getCacheManager(GlobalConfigurationBuilder builder) throws Exception {
        GlobalConfiguration globalConfig = builder.defaultCacheName(DIST_CACHE_NAME).build();
        return new DefaultCacheManager(globalConfig, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .l1().lifespan(0)
                .hash().numOwners(4)
                .locking()
                .isolationLevel(IsolationLevel.REPEATABLE_READ)
                .invocationBatching().enable().build());
    }


    @Bean(name = BUFFER_CACHE_NAME)
    public AdvancedCache<Long, BufferBucket> getBufferCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(BUFFER_CACHE_NAME, new ConfigurationBuilder()
                .locking()
                .isolationLevel(IsolationLevel.READ_COMMITTED)
                .useLockStriping(false)
                .concurrencyLevel(30)
                .transaction()
                .lockingMode(LockingMode.PESSIMISTIC)
                //transaction mode="NON_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(false)
                .recovery().enabled(false)
                .build()
        );
        Cache<Long, BufferBucket> cache = cacheManager.getCache(BUFFER_CACHE_NAME);
        return cache.getAdvancedCache();
    }

    @Bean(name = DIST_CACHE_NAME)
    public Cache<Long, BufferBucket> getDistCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        return cacheManager.getCache(DIST_CACHE_NAME);
    }
}
