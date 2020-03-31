package com.example.infinispansample;

import com.example.infinispansample.maxoptra.BufferBucket;
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
//@Import(TrackingCacheConfig.class)
//@TestConfiguration("com.example.demo.infinispan.TrackingCacheConfig")
//@Configuration()
public class TrackingCacheTest {

   /* @Import(TrackingCacheConfig.class)
    @Configuration
    public static class TestConfiguration {

    }*/


    @Autowired
    @Qualifier("tracking")
    private EmbeddedCacheManager cacheManager;

    @Autowired
    @Qualifier("buffer")
    private Cache<Long, BufferBucket> buffer;

    @Autowired
    @Qualifier("dist")
    private Cache<Long, BufferBucket> dist;

    @Test
    public void test_cache_initialization() {
        assertNotNull(buffer);
        assertNotNull(dist);
        buffer.put(1L, new BufferBucket("1"));
        assertNotNull(buffer.get(1L));
    }

}
