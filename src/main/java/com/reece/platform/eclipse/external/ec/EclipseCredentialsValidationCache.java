package com.reece.platform.eclipse.external.ec;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Optional;
import java.util.concurrent.*;

@Component
public class EclipseCredentialsValidationCache {
    private final ConcurrentMap<String, SoftReference<Boolean>> cache = new ConcurrentHashMap<>();
    private final DelayQueue<CacheObject> cleaningUpQueue = new DelayQueue<>();

    public EclipseCredentialsValidationCache() {
        val cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    val cacheObject = cleaningUpQueue.take();
                    cache.remove(cacheObject.getKey(), cacheObject.getReference());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(String key, Boolean value, long periodInMillis) {
        if (key == null) {
            return;
        }

        if (value == null) {
            cache.remove(key);
        } else {
            val expiryTime = System.currentTimeMillis() + periodInMillis;
            val reference = new SoftReference<>(value);
            cache.put(key, reference);
            cleaningUpQueue.put(new CacheObject(key, reference, expiryTime));
        }
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public Boolean get(String key) {
        return Optional.ofNullable(cache.get(key))
                .map(SoftReference::get)
                .orElse(null);
    }

    public void clear() {
        cache.clear();
    }

    public long size() {
        return cache.size();
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class CacheObject implements Delayed {
        @Getter
        private final String key;
        @Getter
        private final SoftReference<Boolean> reference;
        private final long expiryTime;

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(expiryTime, ((CacheObject) o).expiryTime);
        }
    }
}
