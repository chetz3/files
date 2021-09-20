package com.sprinklr.ops.nocservice.config;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chetz on 2021-06-28
 */
@Profile("ssl")
@Configuration
public class EsConfig {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    String clusterNodes;
    @Value("${spring.data.elasticsearch.cluster-name}")
    String clusterName;
    @Value("${elasticsearch.red.user:NA}")
    String user;
    @Value("${elasticsearch.red.password:NA}")
    String password;
    @Value("${elasticsearch.red.ssl:false}")
    Boolean sslEnabled;

    @Primary
    @Bean
    public TransportClient esClient() throws UnknownHostException {
        String[] nodes = clusterNodes.split(",");

        Settings.Builder builder = Settings.builder()
                .put("cluster.name", clusterName);

        if (sslEnabled)
            builder
                    .put("xpack.security.transport.ssl.enabled", true);

        if (!(user.equals("NA") && password.equals("NA")))
            builder
                    .put("xpack.security.user", user + ":" + password);


        Settings build = builder
                .build();

        PreBuiltXPackTransportClient preBuiltXPackTransportClient = new PreBuiltXPackTransportClient(build);

        for (String node : nodes) {
            String[] nodeWithPort = node.contains(":") ? node.split(":") : new String[]{node, "9300"};
            preBuiltXPackTransportClient.addTransportAddress(
                    new TransportAddress(InetAddress.getByName(nodeWithPort[0]), Integer.valueOf(nodeWithPort[1]))
            );
        }

        return preBuiltXPackTransportClient;
    }

}
