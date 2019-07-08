package com.chulm.hazel.test.basic;

import com.chulm.hazel.CustomHazelcast;
import com.hazelcast.core.*;

import java.util.Map;


public class main_server1 implements MessageListener<Object> {

    public static void main(String[] args) throws InterruptedException {

        CustomHazelcast custom = new CustomHazelcast();

        // Map, MultiMap, Queue, List, Set, Ring Buffer, AtomicLong, AtomicReference, CountdownLatch

        HazelcastInstance hazelInstance = custom.getInstance();
        Map<String, String> map = hazelInstance.getMap("test");
        map.put("1", "1");
        map.put("2", "2");

        System.out.println(map.size());

        // this main is only receive
        //ITOIC Message listener & Pub Sub
        ITopic sTopic = hazelInstance.getTopic("push");
        sTopic.addMessageListener(new main_server2());



        IExecutorService executor = hazelInstance.getExecutorService( "exec" );
        for ( int k = 1; k <= 1000; k++ ) {
            Thread.sleep( 1000 );
            System.out.println( "Producing echo task: " + k );
//            executor.executeOnAllMembers(new Test(sTopic)) ;
        }
        System.out.println( "EchoTaskMain finished!" );

        hazelInstance.getMultiMap("user").put("choi", "test");
        hazelInstance.getMultiMap("user").put("kim", "test2");


    }

    @Override
    public void onMessage(Message<Object> message) {
        Object obj = message.getMessageObject();
        System.out.println("Message Receive = " + obj.toString());
    }
}

