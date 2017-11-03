package com.chen.kafka;

import com.chen.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Date;
import java.util.Properties;

/**
 * Created by ChenQiang on 2017/10/12.
 */
public class KafkaUtils {

    private static Properties props = null;
    private static KafkaProducer<String, String> kp = null;
    private String topic = "movie";

    private static KafkaUtils kafKaUtils = new KafkaUtils();

    public static KafkaUtils getInstance() {
        if (kafKaUtils == null) {
            kafKaUtils = new KafkaUtils();
        }
        return kafKaUtils;
    }

    private KafkaUtils() {
        System.out.println("static-start" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("buffer.memory", "3554432");
        props.put("max.block.ms", "120000");
        props.put("timeout.ms", "6000");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kp = new KafkaProducer<String, String>(props);
        System.out.println("static-end" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
    }

    public void send(String message) {
        producer(message, topic);
    }

    private void producer(String message, String topic) {

        System.out.println("************************开始发送kafka消息************************");
        try {
            if (message == null && message.equals("")) {
                return;
            }
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
            kp.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null)
                        e.printStackTrace();
                    else
                        System.out.println("kafka:message send to partition " + metadata.partition() + ", offset: " + metadata.offset());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("************************发送kafka消息结束************************");
    }
}