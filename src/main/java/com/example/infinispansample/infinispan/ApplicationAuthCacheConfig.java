package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.UserSessionBean;
import com.example.infinispansample.maxoptra.UserSessionBeanStored;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationAuthCacheConfig {
    public static final String CACHE_MANAGER_NAME = "application-auth";
    public static final String USER_SESSION_BEAN_CACHE_NAME = CACHE_MANAGER_NAME + "/user-session-bean";
    public static final String USER_SESSION_BEAN_STORED_CACHE_NAME = CACHE_MANAGER_NAME + "/user-session-bean-stored";

    @Value("${infinispan.user-session-store-data-location}")
    private String userSessionStoreCacheDataLocation;

    @Bean(name = CACHE_MANAGER_NAME)
    public EmbeddedCacheManager getCacheManager(GlobalConfigurationBuilder builder) {
        return new DefaultCacheManager(builder.build());
    }

    @Bean(name = USER_SESSION_BEAN_CACHE_NAME)
    public Cache<String, UserSessionBean> getUserSessionBeanCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(USER_SESSION_BEAN_CACHE_NAME, new ConfigurationBuilder()
                //transaction mode="NONE"
                .transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
                .memory().size(1000)
                .expiration().lifespan(1, TimeUnit.DAYS)
                .build());
        return cacheManager.getCache(USER_SESSION_BEAN_CACHE_NAME);
    }

    @Bean(name = USER_SESSION_BEAN_STORED_CACHE_NAME)
    public Cache<String, UserSessionBeanStored> getUserSessionBeanStoredCache(@Qualifier(CACHE_MANAGER_NAME) EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration(USER_SESSION_BEAN_STORED_CACHE_NAME, new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .transaction()
                .lockingMode(LockingMode.OPTIMISTIC)
                //transaction mode="NON_XA"
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(false)
                .recovery().enabled(false)
                .memory().size(1000)
                .expiration().lifespan(1, TimeUnit.DAYS)
                .persistence()
                .passivation(false)
                .addSingleFileStore()
                .location(userSessionStoreCacheDataLocation)
                .preload(false)
                .purgeOnStartup(false)
                .build());
        return cacheManager.getCache(USER_SESSION_BEAN_STORED_CACHE_NAME);
    }
}
