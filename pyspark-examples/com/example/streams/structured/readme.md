
# Topic - Creation
kafka-topics --create --zookeeper quickstart-bigdata:2181 --partitions 1 --replication-factor 1 --topic structured-stream-topic

# Topic - List
kafka-topics --list --zookeeper quickstart-bigdata:2181

# Topic - Describe
kafka-topics --describe --topic structured-stream-topic --zookeeper quickstart-bigdata:2181

# Topic - Alter
kafka-topics --alter --topic structured-stream-topic --partitions 3 --bootstrap-server quickstart-bigdata:2181

# Topic - Delete
kafka-topics --delete --topic structured-stream-topic --zookeeper quickstart-bigdata:2181
kafka-topics --delete --topic structured-stream-topic --bootstrap-server quickstart-bigdata:9092

# Command Line Producer :
kafka-console-producer --topic structured-stream-topic --broker-list quickstart-bigdata:9092

kafka-console-producer --topic structured-stream-topic --broker-list quickstart-bigdata:9092 --property parse.key=true --property key.separator=":"

# Command Line Consumer :
kafka-console-consumer --topic structured-stream-topic --group pyspark-structured-stream-cg --bootstrap-server quickstart-bigdata:9092

listeners=PLAINTEXT://quickstart-bigdata:9092
advertised.listeners=PLAINTEXT://localhost:19092



#
#
#
$SPARK_HOME/bin/spark-submit \
--name "Sample Spark Application" \
--master local[*] \
--packages org.apache.spark:spark-streaming-kafka-0-8:2.4.0-cdh6.3.2 \
--conf "spark.executorEnv.PYSPARK_DRIVER_PYTHON=./venv/bin/python" \
--conf "spark.executorEnv.PYSPARK_PYTHON=./venv/bin/python" \
--py-files "file:///apps/hostpath/spark/artifacts/application.zip" /apps/hostpath/spark/artifacts/hello.py