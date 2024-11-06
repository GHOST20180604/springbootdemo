package com.hanyc.demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author ：hanyc
 * @date ：2024/5/13 10:52
 * @description：
 */
public class KafkaSendDemo {
//    public static void main(String[] args) {
//        // 配置Kafka生产者
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "kafka1:9092"); // Kafka 集群地址
//        props.put("acks", "all");
//        props.put("retries", 0);
////        props.put("batch.size", 16384);
////        props.put("linger.ms", 1);
////        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//        Producer<String, String> producer = new KafkaProducer<>(props);
//
//        // 发送消息
//        producer.send(new ProducerRecord<String, String>("kg_baidu_expose_task_sit", Integer.toString(0), "{\"keguanId\": \"1789130457962573826\", \"oneEndStatus\": \"1\"}"));
//
//        // 关闭生产者
//        producer.close();
//    }

//    public static void main(String[] args) {
//        // 设置 Kafka 服务器地址
//        String bootstrapServers = "kafka1:9092";
//
//        // 配置 Producer 属性
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "kafka1:9092");
//        props.put("acks", "1");
//        props.put("retries", 0);
//        props.put("batch.size", 0);//16384
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.setProperty("security.protocol", "SASL_PLAINTEXT");
//        props.setProperty("sasl.mechanism", "PLAIN");
//        String jassc = "org.apache.kafka.common.security.plain.PlainLoginModule required\n"
//                + "username=" + "admin" + "\n"
//                + "password=" + "Admin@123." + ";";
//        props.setProperty("sasl.jaas.config", jassc);
//        KafkaProducer<String, String> kp = new KafkaProducer<String, String>(props);
//    }
    public static void main(String[] args) {
        // 设置 Kafka 服务器地址
        String bootstrapServers = "kafka1:9092";

        // 设置认证信息
        String username = "admin";
        String password = "Admin@123.";

        // 配置 Producer 属性
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 添加账号密码认证
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='" + username + "' password='" + password + "';");

        // 创建 KafkaProducer 实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 发送消息
//        ProducerRecord<String, String> record = new ProducerRecord<>("kg_baidu_expose_task_sit", "{\"keguanId\": \"1790293268344782850\", \"oneEndStatus\": \"1\"}");
//        ProducerRecord<String, String> record = new ProducerRecord<>("kg_baidu_expose_task_sit", "END");
        ProducerRecord<String, String> record = new ProducerRecord<>("kg_baidu_expose_task_prod", "END");
//        ProducerRecord<String, String> record = new ProducerRecord<>("kg_baidu_expose_task_sit", "{\"keguanId\": \"1789130457962573826\", \"oneEndStatus\": \"1\"}");
//        ProducerRecord<String, String> record = new ProducerRecord<>("kg_baidu_expose_task_sit", "{\"keguanId\": \"1789130457962573826\", \"oneEndStatus\": \"1\"}");

        producer.send(record, new org.apache.kafka.clients.producer.Callback() {
            @Override
            public void onCompletion(org.apache.kafka.clients.producer.RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println("消息发送成功：topic = " + metadata.topic() + ", partition = " + metadata.partition() + ", offset = " + metadata.offset());
                } else {
                    System.err.println("消息发送失败: " + exception.getMessage());
                }
            }
        });

        // 关闭 KafkaProducer
        producer.close();
    }
}
