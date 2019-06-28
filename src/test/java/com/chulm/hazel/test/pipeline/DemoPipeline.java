package com.chulm.hazel.test.pipeline;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Pipelining;

import java.util.List;
import java.util.Random;

public class DemoPipeline {

    private HazelcastInstance member;
    private HazelcastInstance client;
    private IMap<Integer, String> map;
    private int keyDomain = 100000;
    private int iterations = 500;
    private int getsPerIteration = 1000;

    public static void main(String[] args) throws Exception {
        DemoPipeline main = new DemoPipeline();
        main.init();
        main.pipelined(5);
        main.pipelined(10);
        main.pipelined(100);
        main.nonPipelined();
        System.exit(0);
    }

    private void nonPipelined() {
        System.out.println("Starting non pipelined");
        long startMs = System.currentTimeMillis();
        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            for (long k = 0; k < getsPerIteration; k++) {
                int key = random.nextInt(keyDomain);
                map.get(key);
            }
        }
        long duration = System.currentTimeMillis();
        System.out.println("Non pipelined duration:" + (duration - startMs) + " ms");
    }

    private void pipelined(int depth) throws Exception {
        System.out.println("Starting pipelined with depth:" + depth);
        long startMs = System.currentTimeMillis();
        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            Pipelining<String> pipelining = new Pipelining<String>(depth);
            for (long k = 0; k < getsPerIteration; k++) {
                int key = random.nextInt(keyDomain);
                pipelining.add(map.getAsync(key));
            }

            // wait for completion
            List<String> results = pipelining.results();
            // and verification we got the appropriate number of results.
            if (results.size() != getsPerIteration) {
                throw new RuntimeException();
            }
        }
        long duration = System.currentTimeMillis();
        System.out.println("Pipelined with depth:" + depth + ", duration:" + (duration - startMs) + " ms");
    }

    private void init() {
        member = Hazelcast.newHazelcastInstance();
        client = HazelcastClient.newHazelcastClient();
        map = client.getMap("map");

        for (long l = 0; l < keyDomain; l++) {
            // directly insert on member to speed up insert
            member.getMap(map.getName()).put(l, "" + l);
        }
    }
}
