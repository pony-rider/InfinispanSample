package com.example.infinispansample.infinispan;

import org.infinispan.commons.configuration.ClassWhiteList;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.JChannel;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.protocols.pbcast.STATE_TRANSFER;
import org.jgroups.stack.Protocol;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GlobalCacheConfig {

    private JChannel getUDPChannel() {
        List<Protocol> protocolStack = Arrays.asList(
                new UDP().setMcastPort(45588),
                new PING(),
                new MERGE3().setMaxInterval(30000L).setMinInterval(1000),
                new FD_SOCK(),
                new FD_ALL(),
                new VERIFY_SUSPECT().setTimeout(1500),
                new BARRIER(),
                new NAKACK2(),
                new UNICAST3(),
                new STABLE(),
                new GMS(),
                new MFC(),
                new FRAG2(),
                new RSVP(),
                new STATE_TRANSFER()
        );
        try {
            return new JChannel(protocolStack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GlobalConfigurationBuilder getGlobalConfigurationBuilder() {
        JavaSerializationMarshaller marshaller = new JavaSerializationMarshaller();
        marshaller.initialize(new ClassWhiteList(Arrays.asList(".*")));
        GlobalConfigurationBuilder builder = new GlobalConfigurationBuilder();
        //TODO: replace with tcp/kube_ping configured channel
        builder.transport().transport(new JGroupsTransport(getUDPChannel()))
                .addProperty("lock-timeout", "60000")
                .serialization().marshaller(marshaller);
        return builder;
    }
}
