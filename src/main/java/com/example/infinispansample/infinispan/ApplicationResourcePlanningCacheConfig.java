package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.ResourcePlanningTask;
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

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class ApplicationResourcePlanningCacheConfig {
    public static final String CACHE_MANAGER_NAME = "application-resource-planning";
    public static final String ACTIVE_CACHE_NAME = CACHE_MANAGER_NAME + "/active";
    public static final String ACTIVE_QUEUE_CACHE_NAME = CACHE_MANAGER_NAME + "/activeQueue";
    public static final String ACTIVE_QUEUE_SIZE_CACHE_NAME = CACHE_MANAGER_NAME + "/activeQueueSize";

    @Bean(name = CACHE_MANAGER_NAME)
    public EmbeddedCacheManager getCacheManager(GlobalConfigurationBuilder builder) {
        return new DefaultCacheManager(builder.transport().addProperty("lock-timeout", "120000").build());
    }

    @Bean(name = ACTIVE_CACHE_NAME)
    public Cache<String, Long> getActiveCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ACTIVE_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .locking().isolationLevel(IsolationLevel.REPEATABLE_READ)
                .useLockStriping(false)
                .lockAcquisitionTimeout(60, TimeUnit.SECONDS)
                .transaction()
                .lockingMode(LockingMode.PESSIMISTIC)
                //transaction mode="NON_DURABLE_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(true)
                .recovery().enabled(false)
                .expiration().lifespan(10, TimeUnit.MINUTES)
                .build());
        return cacheManager.getCache(ACTIVE_CACHE_NAME);
    }

    @Bean(name = ACTIVE_QUEUE_CACHE_NAME)
    public Cache<String, ConcurrentLinkedDeque<ResourcePlanningTask>> getActiveQueueCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ACTIVE_QUEUE_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .locking().isolationLevel(IsolationLevel.REPEATABLE_READ)
                .useLockStriping(false)
                .lockAcquisitionTimeout(60, TimeUnit.SECONDS)
                .transaction()
                .lockingMode(LockingMode.OPTIMISTIC)
                //transaction mode="NON_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(false)
                .recovery().enabled(false)
                .expiration().lifespan(10, TimeUnit.MINUTES)
                .build());
        return cacheManager.getCache(ACTIVE_QUEUE_CACHE_NAME);
    }

    @Bean(name = ACTIVE_QUEUE_SIZE_CACHE_NAME)
    public Cache<String , AtomicInteger> getActiveQueueSizeCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(ACTIVE_QUEUE_SIZE_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .locking().isolationLevel(IsolationLevel.REPEATABLE_READ)
                .useLockStriping(false)
                .lockAcquisitionTimeout(60, TimeUnit.SECONDS)
                .transaction()
                .lockingMode(LockingMode.OPTIMISTIC)
                //transaction mode="NON_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(false)
                .recovery().enabled(false)
                .expiration().lifespan(10, TimeUnit.MINUTES)
                .build());
        return cacheManager.getCache(ACTIVE_QUEUE_SIZE_CACHE_NAME);
    }

}
