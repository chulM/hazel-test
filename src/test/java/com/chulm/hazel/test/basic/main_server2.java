package com.chulm.hazel.test.basic;//HazelCast version is 3.10.5

import com.chulm.hazel.CustomHazelcast;
import com.hazelcast.core.*;

import java.util.Map;
import java.util.Set;

public class main_server2 implements MessageListener<Object> {

    public static void main(String[] args) throws InterruptedException {

        CustomHazelcast custom = new CustomHazelcast();
//        custom.setConfig();
        custom.getInstance();


        HazelcastInstance hazelInstance =custom.getInstance();
        Map<String,String> map = hazelInstance.getMap("test");
        System.out.println(map.get("1"));
        System.out.println(map.size());

        // this main is only Topic send
        ITopic sTopic = hazelInstance.getTopic("push");
        sTopic.addMessageListener(new main_server2());
//        IExecutorService executor = hazelInstance.getExecutorService( "exec" );
//        for ( int k = 1; k <= 1000; k++ ) {
//            Thread.sleep( 1000 );
//            System.out.println( "Producing echo task: " + k );
//            executor.executeOnAllMembers(() -> sTopic.publish("test")) ;
//        }
        System.out.println( "EchoTaskMain finished!" );


           Set<Map.Entry<Object, Object>> entrySet = hazelInstance.getMultiMap("user").entrySet();

           for (Map.Entry<Object,Object> entry : entrySet){
               String key = (String)entry.getKey();
               String value = (String)entry.getValue();

               System.out.println("key -->" + key + "," + "value --->" + value);

        }

    }

    @Override
    public void onMessage(Message<Object> message) {
        Object obj = message.getMessageObject();
        System.out.println("Message Receive = " + obj.toString());
    }
}
