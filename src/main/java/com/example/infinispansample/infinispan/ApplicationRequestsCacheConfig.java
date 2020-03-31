package com.example.infinispansample.infinispan;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
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
public class ApplicationRequestsCacheConfig {
    public static final String CACHE_MANAGER_NAME = "application-requests";
    public static final String ACTIVE_CACHE_NAME = CACHE_MANAGER_NAME + "/active";

    @Bean(name = CACHE_MANAGER_NAME)
    public EmbeddedCacheManager getCacheManager(GlobalConfigurationBuilder builder) {
        return new DefaultCacheManager(builder.build());
    }


    @Bean(name = ACTIVE_CACHE_NAME)
    public Cache<Long, Long> getActiveCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ACTIVE_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .locking().isolationLevel(IsolationLevel.READ_COMMITTED)
                .useLockStriping(false)
                .transaction()
                //transaction mode="NONE"
                .lockingMode(LockingMode.OPTIMISTIC)
                .transactionMode(TransactionMode.NON_TRANSACTIONAL)
                .expiration().lifespan(18000)
                .build());
        return cacheManager.getCache(ACTIVE_CACHE_NAME);
    }
}
