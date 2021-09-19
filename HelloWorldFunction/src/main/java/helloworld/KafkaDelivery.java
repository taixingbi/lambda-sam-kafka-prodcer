package helloworld;

import java.util.Properties;

import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistryKafkaSerializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.avro.Schema;
import software.amazon.awssdk.services.glue.model.Compatibility;
//import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistryKafkaSerializer;
//import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
//import com.amazonaws.services.schemaregistry.utils.AvroRecordType;


public class KafkaDelivery {
    final String bootstrapServers = "b-1.test-cluster-1.qvtxwq.c7.kafka.us-east-2.amazonaws.com:9094";
    final String topic= "mp.inventory.v1";
    final String group= "java-group";

    public void send() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty("security.protocol","SSL");

        //glue
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Can replace StringSerializer.class.getName()) with any other key serializer that you may use
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GlueSchemaRegistryKafkaSerializer.class.getName());
        properties.put(AWSSchemaRegistryConstants.AWS_REGION, "us-east-2");
        properties.put(AWSSchemaRegistryConstants.DATA_FORMAT, "JSON"); // OR "AVRO"

        properties.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, "true"); // If not passed, uses "false"
        properties.put(AWSSchemaRegistryConstants.SCHEMA_NAME, "schema_test"); // If not passed, uses transport name (topic name in case of Kafka, or stream name in case of Kinesis Data Streams)
        properties.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "registry_test"); // If not passed, uses "default-registry"
        properties.put(AWSSchemaRegistryConstants.COMPATIBILITY_SETTING, Compatibility.FULL); // Pass a compatibility mode. If not passed, uses Compatibility.BACKWARD
        properties.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.SPECIFIC_RECORD.getName());

        //https://docs.aws.amazon.com/glue/latest/dg/vpc-endpoint.html
        //properties.put(AWSSchemaRegistryConstants.AWS_ENDPOINT,"https://glue.us-east-1.amazonaws.com");

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