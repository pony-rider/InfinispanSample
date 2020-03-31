package com.example.infinispansample;

import com.example.infinispansample.infinispan.ApplicationSchedulingCacheConfig;
import com.example.infinispansample.maxoptra.SchedulerQueueManagerImpl;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationSchedulingCacheTest {

    @Autowired
    @Qualifier(ApplicationSchedulingCacheConfig.CACHE_MANAGER_NAME)
    private EmbeddedCacheManager cacheManager;

    @Autowired
    @Qualifier(ApplicationSchedulingCacheConfig.ACTIVE_CACHE_NAME)
    private  Cache<Long, Long> activeCache;

    @Autowired
    @Qualifier(ApplicationSchedulingCacheConfig.RESULT_CACHE_NAME)
    private Cache<Long, SchedulerQueueManagerImpl.RunScheduler> resultCache;

    @Test
    public void test_cache_initialization() {
        assertNotNull(activeCache);
        assertNotNull(resultCache);
        activeCache.put(1L, 1L);
        resultCache.put(1L, new SchedulerQueueManagerImpl.RunScheduler());
        assertNotNull(activeCache.get(1L));
        assertNotNull(resultCache.get(1L));
    }

}
