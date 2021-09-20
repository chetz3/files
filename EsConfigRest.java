package com.xxx.config;

        import org.apache.http.HttpHost;
        import org.elasticsearch.client.RestClient;
        import org.elasticsearch.client.RestHighLevelClient;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * Created by chetz on 2021-07-20
 */
@Configuration
public class ESConfigRest extends AbstractElasticsearchConfiguration {
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        System.out.println("HERER!");
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200)));
    }
}
