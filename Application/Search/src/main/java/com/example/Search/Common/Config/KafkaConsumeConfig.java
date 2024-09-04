package com.example.Search.Common.Config;


import com.example.Search.Common.Util.KafkaUtil;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConsumeConfig {

    @Value("${bootstrap.server}")
    private String bootstrapServer;

    @Bean
    public ConsumerFactory<String,Object> consumerFactory(){
        Map<String,Object> config= new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"group_1");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        config.put(JsonSerializer.TYPE_MAPPINGS, KafkaUtil.getJsonTypeMapping();

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,Object> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,Object> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}

