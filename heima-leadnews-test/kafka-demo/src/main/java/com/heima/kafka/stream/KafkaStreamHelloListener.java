package com.heima.kafka.stream;

import com.heima.kafka.config.KafkaStreamConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@Slf4j
public class KafkaStreamHelloListener {

    @Bean
    public KStream<String, String> kafkaStream(StreamsBuilder streamsBuilder) {
       //创建kstream对象，同时指定从那个topic中接收消息
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        stream.flatMapValues(new ValueMapper<String, Iterable<?>>() {
            @Override
            public Iterable<?> apply(String s) {
                return Arrays.asList(s.split(" "));
            }
        })

                //根据value进行聚合分组
                .groupBy((key,value)->value)
                //聚合计算时间间隔
                .windowedBy(TimeWindows.of(Duration.ofSeconds(1000)))
                //求单词的个数
                .count()
                .toStream()
                //处理后的结果转换为String字符串
                .map((key,value)->{
                    return new KeyValue<>(key.key()+"-"+key.window().startTime()+"-"+key.window().endTime(),value);
                })
                .to("itcast-topic-output");
        return stream;
    }
}
