package com.example.infinispansample.infinispan;

import com.example.infinispansample.maxoptra.BufferBucket;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.*;
import org.infinispan.notifications.cachelistener.event.*;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;

@Listener
public class CacheListener {
    @CacheStarted
    public void cacheStarted(CacheStartedEvent event) {
        System.out.println(event);
    }

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<Long, BufferBucket> event) {
        this.printLog("Adding key '" + event.getKey()
                + "' to cache", event);
    }

    @CacheEntryExpired
    public void entryExpired(CacheEntryExpiredEvent<Long, BufferBucket> event) {
        this.printLog("Expiring key '" + event.getKey()
                + "' from cache", event);
    }

    @CacheEntryVisited
    public void entryVisited(CacheEntryVisitedEvent<Long, BufferBucket> event) {
        this.printLog("Key '" + event.getKey() + "' was visited", event);
    }

    @CacheEntryActivated
    public void entryActivated(CacheEntryActivatedEvent<Long, BufferBucket> event) {
        this.printLog("Activating key '" + event.getKey()
                + "' on cache", event);
    }

    @CacheEntryPassivated
    public void entryPassivated(CacheEntryPassivatedEvent<Long, BufferBucket> event) {
        this.printLog("Passivating key '" + event.getKey()
                + "' from cache", event);
    }

    @CacheEntryLoaded
    public void entryLoaded(CacheEntryLoadedEvent<Long, BufferBucket> event) {
        this.printLog("Loading key '" + event.getKey()
                + "' to cache", event);
    }

    @CacheEntriesEvicted
    public void entriesEvicted(CacheEntriesEvictedEvent<Long, BufferBucket> event) {
        StringBuilder builder = new StringBuilder();
        event.getEntries().forEach(
                (key, value) -> builder.append(key).append(", "));
        System.out.println("Evicting following entries from cache: "
                + builder.toString());
    }

    private void printLog(String log, CacheEntryEvent event) {
        if (!event.isPre()) {
            System.out.println(log);
        }
    }
}
