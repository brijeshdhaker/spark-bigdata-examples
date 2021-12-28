#
from pyspark.sql import SparkSession
import pyspark.sql.functions as fn
from pyspark.sql.types import StringType

spark = SparkSession \
    .builder \
    .master("local[*]") \
    .appName("structured-kafka-stream") \
    .getOrCreate()

#
binary_to_string = fn.udf(lambda x: str(int.from_bytes(x, byteorder='big')), StringType())

# Subscribe to 1 topic

structureStreamDf = (
    spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", "quickstart-bigdata:9092")
        .option("subscribe", "structured-stream-topic")
        .option("startingOffsets", "earliest")
        .option("failOnDataLoss", "false")
        .load()
        .withColumn('key', fn.col("key").cast(StringType()))
        .withColumn('stringValue', fn.col("value").cast(StringType()))
        .withColumn('valueSchemaId', fn.col("value").cast(StringType()))
        .select('topic', 'partition', 'offset', 'timestamp', 'timestampType', 'key', 'value', 'valueSchemaId', 'stringValue')
)

print("Kafka_df.isStreaming : " + str(structureStreamDf.isStreaming))
print("Schema for structureStreamDf : ")
structureStreamDf.printSchema()


# Select Record from Kafka DF
data_df = structureStreamDf.select('topic', 'timestamp', 'key', 'stringValue').alias("records").selectExpr("records.*")

#data_df = structureStreamDf.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")

#df.isStreaming()    # Returns True for DataFrames that have streaming sources
print("Schema for data_df : ")
data_df.printSchema()
data_df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start() \
    .awaitTermination()

spark.stop()