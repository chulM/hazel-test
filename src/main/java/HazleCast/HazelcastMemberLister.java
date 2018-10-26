package HazleCast;

import com.hazelcast.core.*;

import java.util.Map;

public class HazelcastMemberLister implements EntryListener<Object,Object>, MembershipListener {

//    MultiMap<String, String> userInfo;

    public HazelcastMemberLister(HazelcastInstance instance){
//        userInfo = instance.getMultiMap("user");
    }

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        System.out.println("memberAdded --->" + membershipEvent.toString());
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        System.out.println("memberRemoved --->" + membershipEvent.toString());
    }

    @Override
    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
        System.out.println("MemberAttributeEvent --->" + memberAttributeEvent.toString());
    }


    @Override
    public void entryAdded(EntryEvent<Object, Object> event) {
        System.out.println("EntryEventAdded --->" + event.toString());
    }

    @Override
    public void entryEvicted(EntryEvent<Object, Object> event) {
        System.out.println("entryEvicted --->" + event.toString());
    }

    @Override
    public void entryRemoved(EntryEvent<Object, Object> event) {
        System.out.println("entryRemoved --->" + event.toString());
    }

    @Override
    public void entryUpdated(EntryEvent<Object, Object> event) {
        System.out.println("entryUpdated --->" + event.toString());
    }

    @Override
    public void mapCleared(MapEvent event) {
        System.out.println("mapCleared --->" + event.toString());
    }

    @Override
    public void mapEvicted(MapEvent event) {
        System.out.println("mapEvicted --->" + event.toString());
    }
//
//    public void put(String key, String value){
//        userInfo.put(key,value);
//    }
//
//    public MultiMap<String, String> getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(MultiMap<String, String> userInfo) {
//        this.userInfo = userInfo;
//    }
}
