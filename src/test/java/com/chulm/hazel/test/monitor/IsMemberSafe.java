package com.chulm.hazel.test.monitor;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

public class IsMemberSafe {
    public static void main(String[] args){
        HazelcastInstance instance1 = Hazelcast.newHazelcastInstance();
        HazelcastInstance instance2 = Hazelcast.newHazelcastInstance();

        Member member2 = instance2.getCluster().getLocalMember();
        boolean member2Safe = instance1.getPartitionService().isMemberSafe(member2);

        System.out.printf("# Is member2 safe for shutdown\t: %s\n", member2Safe);

    }
}
