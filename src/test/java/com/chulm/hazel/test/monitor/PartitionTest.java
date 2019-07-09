package com.chulm.hazel.test.monitor;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class PartitionTest {
    public static void main(String[] args){

        HazelcastInstance node = Hazelcast.newHazelcastInstance();
        boolean safe = node.getPartitionService().isClusterSafe();

        System.out.println("cluster is safe ? = "  + safe);
        System.out.println("partition size ? = " + node.getPartitionService().getPartitions().size());
    }
}
