package DabEngine.Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public enum InMemoryCache implements Cache {
	
	INSTANCE;

	public long CACHE_CLEANUP_SLEEP_TIME = 5;
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String, SoftReference<CachedObject>> cache = new ConcurrentHashMap<>();
	
	InMemoryCache() {
		Thread cleanUpThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(CACHE_CLEANUP_SLEEP_TIME * 1000L);
					cleanUp();
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		});
		
		cleanUpThread.setDaemon(true);
		cleanUpThread.start();
	}

	@Override
	public <T> void add(String filename, T objectToCache, long lifeTime) {
		if(filename.isEmpty() || filename == null) {
			return;
		}
		if(objectToCache == null) {
			cache.remove(filename);
		} else {
			lifeTime += System.currentTimeMillis();
			cache.put(filename, new SoftReference<>(new CachedObject<T>(objectToCache, lifeTime)));
		}
	}

	@Override
	public void remove(String filename) {
		cache.remove(filename);
	}

	@Override
	public long size() {
		return cache.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String filename) {
		CachedObject<T> t;
		try {
			t = cache.get(filename).get();
		} catch(NullPointerException ex) {
			return null;
		}
		return t.getValue();
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanUp() {
		Iterator<Entry<String, SoftReference<CachedObject>>> it = cache.entrySet().iterator();
		while(it.hasNext()) {
			if(it.next().getValue().get().isExpired()) {
				it.remove();
			}
		}
	}
}
