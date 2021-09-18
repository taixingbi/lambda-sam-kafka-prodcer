package helloworld;

import java.util.Properties;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDelivery {
    Properties properties = new Properties();
    final String bootstrapServers = "b-1.test-cluster-1.qvtxwq.c7.kafka.us-east-2.amazonaws.com:9094";
    final String topic= "mp.inventory.v1";

    public void send() {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty("security.protocol","SSL");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,
                "key1", "value1" );

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    System.out.println("successfully posted to Kafka");
                } else {
                    e.printStackTrace();
                }
            }
        });



        producer.flush();
        producer.close();
    }
}


//avro  https://www.youtube.com/watch?v=_6HTHH1NCK0