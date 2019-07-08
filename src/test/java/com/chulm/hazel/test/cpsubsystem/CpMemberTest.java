package com.chulm.hazel.test.cpsubsystem;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICountDownLatch;

import java.util.concurrent.TimeUnit;

public class CpMemberTest {
    private static final int CP_MEMBER_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.getCPSubsystemConfig().setCPMemberCount(CP_MEMBER_COUNT);
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        ICountDownLatch latch = hz.getCPSubsystem().getCountDownLatch("latch");

        latch.trySetCount(CP_MEMBER_COUNT);

        System.out.println("Starting to do some work");

        // do some sleeping to simulate doing something
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));


        System.out.println("Finished my work");

        // now we do a countdown which notifies all followers
        latch.countDown();

        System.out.println("Latch is decremented");

        boolean success = latch.await(60, TimeUnit.SECONDS);

        if (success) {
            System.out.println("EVERYONE FINISHED THEIR WORK!");
        } else {
            System.out.println("SEEMS SOMEONE IS BEING LAZY :(");
        }

        Thread.sleep(TimeUnit.SECONDS.toMillis(5));

        hz.getLifecycleService().terminate();

    }
}
