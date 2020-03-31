package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.SchedulerQueueManagerImpl;
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

import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationSchedulingCacheConfig {
    public static final String CACHE_MANAGER_NAME = "application-scheduling";
    public static final String ACTIVE_CACHE_NAME = CACHE_MANAGER_NAME + "/active";
    public static final String RESULT_CACHE_NAME = CACHE_MANAGER_NAME + "/result";


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
                .lockingMode(LockingMode.PESSIMISTIC)
                //transaction mode="NON_DURABLE_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(true)
                .recovery().enabled(false)
                .expiration().lifespan(30, TimeUnit.MINUTES)
                .build());
        return cacheManager.getCache(ACTIVE_CACHE_NAME);
    }

    @Bean(name = RESULT_CACHE_NAME)
    public Cache<Long, SchedulerQueueManagerImpl.RunScheduler> getResultCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(RESULT_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .transaction()
                .lockingMode(LockingMode.OPTIMISTIC)
                .transactionMode(TransactionMode.NON_TRANSACTIONAL)
                .expiration().lifespan(1, TimeUnit.DAYS)
                .build());
        return cacheManager.getCache(RESULT_CACHE_NAME);
    }
}
