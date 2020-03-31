package com.example.infinispansample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.sql.DataSource;
import javax.validation.constraints.Positive;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {
    @Positive
    @Value("${application-cache.infinispan.cache-size:100000}")
    private long defaultCacheSize;

    @Autowired
    DataSource dataSource;



	/*@Autowired
	@Qualifier("tracking")
	private EmbeddedCacheManager cacheManager;*/

	/*@Autowired
	@Qualifier("buffer")
	private AdvancedCache<Long, BufferBucket> buffer;

	@Autowired
	@Qualifier("dist")
	private Cache<Long, BufferBucket> dist;
*/
	/*@Autowired
	@Qualifier("application-auth")
	private EmbeddedCacheManager cacheManager;

	@Autowired
	@Qualifier(ApplicationAuthCacheConfig.USER_SESSION_BEAN_CACHE_NAME)
	Cache<String, UserSessionBean> userSessionBeanCache;

	@Autowired
	@Qualifier(ApplicationAuthCacheConfig.USER_SESSION_STORE_CACHE_NAME)
	Cache<String, UserSessionBeanStored> userSessionBeanStoredCache;*/


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

   /* public void checkTrackingCache() {
        cacheManager.addListener(new CacheListener());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        AtomicLong atomicLong = new AtomicLong(0L);
        executorService.scheduleWithFixedDelay(() -> {
            Long key = atomicLong.incrementAndGet();
            dist.put(key, new BufferBucket(key + ""));
            System.out.println("put to cache: " + key);
        }, 1, 1, TimeUnit.SECONDS);

        executorService.scheduleWithFixedDelay(() -> {
            System.out.println("retrieve from cache " + dist.get(1L));
        }, 1, 1, TimeUnit.SECONDS);
    }*/

	/*public void checkUserSessionStoreCache() {
		cacheManager.addListener(new CacheListener());
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		AtomicLong atomicLong = new AtomicLong(0L);
		executorService.scheduleWithFixedDelay(() -> {
			Long key = atomicLong.incrementAndGet();
			userSessionBeanStoredCache.put(key.toString(), new UserSessionBeanStored(key.toString()));
			System.out.println("put to cache: " + key);
		}, 1, 1, TimeUnit.SECONDS);

		executorService.scheduleWithFixedDelay(() -> {
			System.out.println("retrieve from cache " + userSessionBeanStoredCache.get("1"));
		}, 1, 1, TimeUnit.SECONDS);
	}*/
}
