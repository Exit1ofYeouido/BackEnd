package com.example.Search.Common.Config;


import com.example.Search.Common.Kafka.GiveStockDTO;
import com.example.Search.Common.Util.KafkaUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;


import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConsumeConfig {

    @Value("${bootstrap.server}")
    private String bootstrapServer;

    @Bean
    public ConsumerFactory<String,GiveStockDTO> consumerFactory(){
        Map<String,Object> config= new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"search");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonSerializer.TYPE_MAPPINGS, KafkaUtil.getJsonTypeMapping(GiveStockDTO.class));

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(GiveStockDTO.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,GiveStockDTO> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,GiveStockDTO> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}

