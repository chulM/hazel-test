package com.chulm.hazel.test.monitor;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStoreAdapter;
import com.hazelcast.monitor.LocalMapStats;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


class SimpleMapStore<K, V> extends MapStoreAdapter<K, V> {
    final Map<K, V> store ;
    private boolean loadAllKeys = true;

    public SimpleMapStore() {
        store = new ConcurrentHashMap<K, V>();
    }

    public SimpleMapStore(final Map<K, V> store) {
        this.store = store;
    }

    @Override
    public void delete(final K key) {
        store.remove(key);
    }

    @Override
    public V load(final K key) {
        return store.get(key);
    }

    @Override
    public void store(final K key, final V value) {
        store.put(key, value);
    }

    public Set<K> loadAllKeys() {
        if (loadAllKeys) {
            return store.keySet();
        }
        return null;
    }

    public void setLoadAllKeys(boolean loadAllKeys) {
        this.loadAllKeys = loadAllKeys;
    }

    @Override
    public void storeAll(final Map<K, V> kvMap) {
        store.putAll(kvMap);
    }
}


public class MapStoreTest {

    public static void main(String[] args){
        final ConcurrentMap<Long, String> STORE = new ConcurrentHashMap<>();
        STORE.put(1l, "Event1");
        STORE.put(2l, "Event2");
        STORE.put(3l, "Event3");
        STORE.put(4l, "Event4");
        STORE.put(5l, "Event5");
        STORE.put(6l, "Event6");

        Config config = new Config();
        config.getMapConfig("map")
                .setMapStoreConfig(new MapStoreConfig()
                        .setWriteDelaySeconds(1)
                        .setImplementation(new SimpleMapStore<>(STORE)));

        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        IMap map = h.getMap("map");
        Collection collection = map.values();
        System.out.println(collection.size());

        /*
        * LocalMapStats are the statistics for the local portion of this
        * distributed map and contains information such as ownedEntryCount
        * backupEntryCount, lastUpdateTime, lockedEntryCount.
        */
        LocalMapStats localMapStats = map.getLocalMapStats();

        System.out.println(localMapStats.total());
        //session hits
        System.out.println(localMapStats.getHits());

    }
}
