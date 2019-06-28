package com.chulm.hazel.test.monitor;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.partition.PartitionLostEvent;
import com.hazelcast.partition.PartitionLostListener;

public class ListenPartitionLostEvents {

    public static void main(String[] args) {
        HazelcastInstance instance1 = Hazelcast.newHazelcastInstance(null);
        HazelcastInstance instance2 = Hazelcast.newHazelcastInstance(null);

        // initialize partitions
        instance1.getMap("map1").put(0, 0);

        instance1.getPartitionService().addPartitionLostListener(new PartitionLostListener() {
            @Override
            public void partitionLost(PartitionLostEvent event) {
                System.out.println("Instance2 has lost a partition for data with 0 backup! " + event);
            }
        });

        instance2.getLifecycleService().terminate();
    }
}
