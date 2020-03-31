package com.example.infinispansample;

import com.example.infinispansample.infinispan.ApplicationAuthCacheConfig;
import com.example.infinispansample.maxoptra.UserSessionBean;
import com.example.infinispansample.maxoptra.UserSessionBeanStored;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationAuthCacheTest {

    @Autowired
    @Qualifier("application-auth")
    private EmbeddedCacheManager cacheManager;

    @Autowired
    @Qualifier(ApplicationAuthCacheConfig.USER_SESSION_BEAN_CACHE_NAME)
    private  Cache<String, UserSessionBean> userSessionBeanCache;

    @Autowired
    @Qualifier(ApplicationAuthCacheConfig.USER_SESSION_BEAN_STORED_CACHE_NAME)
    private Cache<String, UserSessionBeanStored> userSessionBeanStoredCache;

    @Test
    public void test_cache_initialization() {
        assertNotNull(userSessionBeanCache);
        assertNotNull(userSessionBeanStoredCache);
        userSessionBeanCache.put("user1", new UserSessionBean("user1"));
        assertNotNull(userSessionBeanCache.get("user1"));
    }

}
