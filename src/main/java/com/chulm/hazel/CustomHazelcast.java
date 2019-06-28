package com.chulm.hazel;

import com.hazelcast.config.*;
import com.hazelcast.core.*;

public class CustomHazelcast {

    private HazelcastInstance instance = null;

    private int port;
    private String[] member;

    Config config = new Config();

    private Cluster cluster;

    /// hazelCast MemberLister
    // Clusteing되는 서버 의 추가/삭제/업데이트에 대한 이벤트는 memberShipEvent이다.
    // userMultiMap에 추가되는 이벤트를 수신하기 위해 멤버리스너에 C
    private HazelcastMemberLister memberLister;
    private MultiMap<Object, Object> userMultiMap;

    public void setConfig() {

        config.setProperty("hazelcast.memcache.enabled", "false");
        config.setProperty("hazelcast.rest.enabled", "false");
        config.setProperty("hazelcast.logging.type", "none");
        config.setProperty("hazelcast.system.log.enabled", "false");
        config.setProperty("hazelcast.version.check.enabled", "true");

        NetworkConfig networkConf = config.getNetworkConfig();
        InterfacesConfig interfacesConf = networkConf.getInterfaces();
        JoinConfig joinConf = networkConf.getJoin();
        MulticastConfig multicastConf = joinConf.getMulticastConfig();
        TcpIpConfig tcpIpConf = joinConf.getTcpIpConfig();
        networkConf.setPort(port);
        tcpIpConf.setEnabled(false);
    }

    public HazelcastInstance getInstance() {
        if (instance == null) {
            instance = Hazelcast.newHazelcastInstance(config);
            cluster = instance.getCluster();

            cluster.addMembershipListener(new MembershipListener() {
                @Override
                public void memberAdded(MembershipEvent membershipEvent) {
                    System.out.println("--->memberAdded :" + membershipEvent);
                }

                @Override
                public void memberRemoved(MembershipEvent membershipEvent) {
                    System.out.println("--->memberRemoved :" + membershipEvent);
                }

                @Override
                public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
                    System.out.println("--->memberAttributeEvent :" + memberAttributeEvent);
                }
            });

            cluster = instance.getCluster();
            memberLister = new HazelcastMemberLister(instance);
            cluster.addMembershipListener(memberLister);
            userMultiMap = instance.getMultiMap("user");
            userMultiMap.addEntryListener(memberLister, true);
        }
        return instance;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getMember() {
        return member;
    }

    public void setMember(String[] member) {
        this.member = member;
    }

    public HazelcastMemberLister getMemberLister() {
        return memberLister;
    }

    public void setMemberLister(HazelcastMemberLister memberLister) {
        this.memberLister = memberLister;
    }
}
