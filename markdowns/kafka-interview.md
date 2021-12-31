### Differences between RabbitMQ and Kafka

```commandline
+———------———-------+——---——-----——-----——-----——-------——----——-----——-----——-----——------——-----——-----——------------+——-----——-------——----——-----——-----——-----——------——-----——-----——------------——-----——----+
|  #                |  e_id                                                                                            |                                                                                            |
+———------———-------+——---——-----——-----——-----——-------——----——-----——-----——-----——------——-----——-----——------------+——-----——-------——----——-----——-----——-----——------——-----——-----——------------——-----——----+
|  Origion          |  Apache Open Source                                                                              | Linked In                                                                                  |
|  Design           |  Smart broker / dumb consumer model,                                                             | Kafka employs a dumb broker and uses smart consumers                                       |
|  Message handling |  removes messages as soon as the messages are consumed and acknowledged                          | Kafka retains messages based on the retention time set on the topic                        |
|  Volume           |  Fast with low volume in the queues become slowly as the volume goes up.                         | Kakfa is designed to store and stream huge volume of data with very little overhead        |
|  Performance      |  around 20,000 messages per second                                                               | 100,000 messages per second.                                                               |
|  Scaling          |  Vertically scaled by adding more power to machines                                              | Kafka can be horizontally scaled – by adding more machines                                 |
|  Monitoring       |  inbuilt monitoring tools for queues, connections, exchanges and user permissions.               | Vendors : Confluent, Datadog                                                               |
|  Routing rules    |  supports complex routing rules and keeps tracks of message states – consumed, acknowledged etc. | Kafka takes a simple routing approach                                                      |
+———------+——-------+——---——-----——-----——-----——-------——----——-----——-----——-----——------——-----——-----——------------+——-----——-------——----——-----——-----——-----——------——-----——-----——------------——-----——----+
```


#### 1. What is the Retention Period in Kafka Topic ?
   Answer :  Kafka events or Messages older than retention period will not be available for Consumer to Consume. Because it gets deleted. Kafka Retain the data only for retention period. Let;s say if Retention period is 7 days, Then You cannot get the data older than 7 days.

#### 2. One Job which reads data from kafka topic and then process it,is failing because it has offset to read which are older than retention period?

Answer : it should have property failOnDataLoss to be false , in that case it will not fail the job with error, But you would lose the data.

below is the description of property from Kafka Documentation

failOnDataLoss: Whether to fail the query when it’s possible that data is lost (e.g., topics are deleted, or offsets are out of range). This may be a false alarm. You can disable it when it doesn’t work as you expected. Batch queries will always fail if it fails to read any data from the provided offsets due to lost data.

#### 3. Which mechanism [Push Or Pull] is used by Kafka Consumer to read data from Brokers ?

Answer :It is Pulled from the broker by Consumer.Because It if it is Pushed by Brokers to the Consumers , Rate is Controlled by Broker and not by the Consumer.  If Pulled , then each consumer can read from broker at the maximum rate of Consumer, Independent of Other consumers which might be Slower or Faster. That’s why Pull Mechanism is used.

#### 4. Is it Possible to Read data from kafka topic from fixed offset using command line ?

Answer : Yes it is possible , Depending on partitions of topic ,we will have different offset. To read date from fixed offset use below command.
Just change the m and n as per your requirement  –partition m –offset n
```commandline
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sampleTopic1 --property print.key=true --partition 0 --offset 12
```

#### Kafka Architecture is different to ActiveMQ.

Key differences:

1. ActiveMQ Broker had to maintain the delivery state of every message resulting into lower throughput. Kafka producer doesn’t wait for acknowledgements from the broker unlike in ActiveMQ and sends messages as faster as the broker can handle. Overall throughput will be high if broker can handle the messages as fast as producer.

2. Kafka has a more efficient storage format. On average, each message had an overhead of 9 bytes in Kafka, versus 144 bytes in ActiveMQ.

3. ActiveMQ is push based messaging system and Kafka is pull based messaging system . In AcitveMQ, Producer send message to Broker and Broker push messages to all consumers. Producer has responsibility to ensure that message has been delivered. In Kafka, Consumer will pull messages from broker at its own time. It's the responsibility of consumer to consume the messages it has supposed to consume.

4 .Slow Consumers in AMQ can cause problems on non-durable topics since they can force the broker to keep old messages in RAM which once it fills up, forces the broker to slow down producers, causing the fast consumers to be slowed down. A slow consumer in Kakfa does not impact other consumers.

5. In Kafka - A consumer can rewind back to an old offset and re-consume data. It is useful when you fix some issue and decide to re-play the old messages post issue resolution.

6. Performance of Queue and Topics degrades with addition of more consumers in ActiveMQ. But Kafka does not have that dis-advantage with addition of more consumers.

7. Kafka is highly scalable due to replication of partitions. It can ensure that messages are delivered in a sequence with in a partition.

8. ActiveMQ is traditional messaging system where as Kakfa is meant for distributed processing system with huge amount of data and effective for stream processing

Due to above efficiencies, Kafka throughput is more than normal messaging systems like ActiveMQ and RabbitMQ.

####
kafka-configs --zookeeper <zkhost>:2181 --describe --entity-type topics --entity-name <topic name>
kafka-configs.sh --zookeeper <zkhost>:2181 --entity-type topics --alter --entity-name <topic name> --add-config retention.ms=10000