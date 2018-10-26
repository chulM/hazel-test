import HazleCast.customHazelcast;
import com.hazelcast.core.*;
import com.hazelcast.executor.impl.RunnableAdapter;

import java.io.Serializable;
import java.util.Map;


//HazelCast version is 3.10.5

public class main_server1 implements MessageListener<Object> {

    public static void main(String[] args) throws InterruptedException {

        customHazelcast custom = new customHazelcast();

        // 자료구조는 다음과 같으며, Map만 간단히 테스트 해보자.
        // Map, MultiMap, Queue, List, Set, Ring Buffer, AtomicLong, AtomicReference, CountdownLatch

        HazelcastInstance hazelInstance =custom.getInstance();
        Map<String,String> map = hazelInstance.getMap("test");
        map.put("1","1");
        map.put("2","2");

        System.out.println(map.size());

        // this main is only receive
        //ITOIC Message listener & Pub Sub
        ITopic sTopic = hazelInstance.getTopic("push");
        sTopic.addMessageListener(new main_server2());



//        ExecutorService의 분산 구현입니다.
//        IExecutorService는 특정 멤버에 대한 작업 실행, 특정 키의 소유자 인 멤버에 대한 작업 실행, 여러 멤버에 대한 작업 실행 및 콜백을 사용하여 실행 결과 청취와 같은 추가 메서드를 제공합니다

//        IExecutorService executor = hazelInstance.getExecutorService( "exec" );
//        for ( int k = 1; k <= 1000; k++ ) {
//            Thread.sleep( 1000 );
//            System.out.println( "Producing echo task: " + k );
////            executor.executeOnAllMembers(new Test(sTopic)) ;
//        }
//        System.out.println( "EchoTaskMain finished!" );



            hazelInstance.getMultiMap("user").put("choi","test");
            hazelInstance.getMultiMap("user").put("kim","test2");



    }

    @Override
    public void onMessage(Message<Object> message) {
            Object obj = message.getMessageObject();
            System.out.println("Message Receive = " + obj.toString());
    }
}

