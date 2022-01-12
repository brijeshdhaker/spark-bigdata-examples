package com.spark.streaming.dstreams

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.File
import java.nio.charset.Charset
import com.google.common.io.Files
import com.spark.streaming.dstreams.HiveDstreamTransformer.{conf, kafkaParams, sc, ssc, stream, topics}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext, TaskContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext, Time}
import org.apache.spark.util.{IntParam, LongAccumulator}

import java.util.UUID

/**
 * Use this singleton to get or register a Broadcast variable.
 */
object WordExcludeList {

  @volatile private var instance: Broadcast[Seq[Int]] = null

  def getInstance(sc: SparkContext): Broadcast[Seq[Int]] = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          val wordExcludeList = Seq(3, 5)
          instance = sc.broadcast(wordExcludeList)
        }
      }
    }
    instance
  }
}

/**
 * Use this singleton to get or register an Accumulator.
 */
object DroppedWordsCounter {

  @volatile private var instance: LongAccumulator = null

  def getInstance(sc: SparkContext): LongAccumulator = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          instance = sc.longAccumulator("DroppedWordsCounter")
        }
      }
    }
    instance
  }
}

/**
 * Counts words in text encoded with UTF8 received from the network every second. This example also
 * shows how to use lazily instantiated singleton instances for Accumulator and Broadcast so that
 * they can be registered on driver failures.
 *
 * Usage: RecoverableNetworkWordCount <hostname> <port> <checkpoint-directory> <output-file>
 * <hostname> and <port> describe the TCP server that Spark Streaming would connect to receive
 * data. <checkpoint-directory> directory to HDFS-compatible file system which checkpoint data
 * <output-file> file to which the word counts will be appended
 *
 * <checkpoint-directory> and <output-file> must be absolute paths
 *
 * To run this on your local machine, you need to first run a Netcat server
 *
 * `$ nc -lk 9999`
 *
 * and run the example as
 *
 * `$ ./bin/run-example org.apache.spark.examples.streaming.RecoverableNetworkWordCount \
 * localhost 9999 ~/checkpoint/ ~/out`
 *
 * If the directory ~/checkpoint/ does not exist (e.g. running for the first time), it will create
 * a new StreamingContext (will print "Creating new context" to the console). Otherwise, if
 * checkpoint data exists in ~/checkpoint/, then it will create StreamingContext from
 * the checkpoint data.
 *
 * Refer to the online documentation for more details.
 */
object RecoverableKafkaWordCount {

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "kafka-broker:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "hive-dstream-cg",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
  val topics = Array("tweeter-tweets")

  def createContext(outputPath: String, checkpointDirectory: String): StreamingContext = {

    // If you do not see this printed, that means the StreamingContext has been loaded
    // from the new checkpoint
    println("Creating new context")
    val outputFile = new File(outputPath)
    if (outputFile.exists()) outputFile.delete()
    val sparkConf = new SparkConf().setAppName("RecoverableKafkaWordCount").setMaster("local[4]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    // Create the context with a 1 second batch size
    val ssc = new StreamingContext(sc, Durations.seconds(10))
    ssc.checkpoint(checkpointDirectory)

    // Create a kafka Stream on target ip:port and count the
    val stream = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

    // Kafka Offset Commit
    stream.foreachRDD{(rdd, time) => {
        //Obtaining Offsets
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

        // some time later, after outputs have completed
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }

        // some time later, after outputs have completed
        stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      }
    }

    val lines = stream.map(record => (record.key, record.value))

    //val words = lines.flatMap(_.split(" "))
    //val wordCounts = words.map((_, 1)).reduceByKey(_ + _)
    lines.foreachRDD { (rdd: RDD[(String, String)], time: Time) =>

      // Get or register the excludeList Broadcast
      val excludeList = WordExcludeList.getInstance(rdd.sparkContext)

      // Get or register the droppedWordsCounter Accumulator
      val droppedWordsCounter = DroppedWordsCounter.getInstance(rdd.sparkContext)

      // Use excludeList to drop words and use droppedWordsCounter to count them
      val recordsRDD = rdd.map(x => ((UUID.randomUUID().toString, x._2, x._2.split(" ").size, x._2.size)))

      // Count if sentence have 3or 5 words count them
      val counts = recordsRDD.filter {
          case (x) =>
        if (excludeList.value.contains(x._3)) {
          droppedWordsCounter.add(1)
          false
        } else {
          true
        }
      }.collect() //.mkString("[", ", ", "]")

      val output = s"Total $counts._3 sentence have either 3 or 5 words at time $time "
      println(output)

      println(s"Dropped ${droppedWordsCounter.value} word(s) totally")

      println(s"Appending to ${outputFile.getAbsolutePath}")

      Files.append(output + "\n", outputFile, Charset.defaultCharset())

    }
    ssc
  }

  def main(args: Array[String]): Unit = {
    /*
    if (args.length != 4) {
      System.err.println(s"Your arguments were ${args.mkString("[", ", ", "]")}")
      System.err.println(
        """
          |Usage: RecoverableNetworkWordCount <hostname> <port> <checkpoint-directory>
          |     <output-file>. <hostname> and <port> describe the TCP server that Spark
          |     Streaming would connect to receive data. <checkpoint-directory> directory to
          |     HDFS-compatible file system which checkpoint data <output-file> file to which the
          |     word counts will be appended
          |
          |In local mode, <master> should be 'local[n]' with n > 1
          |Both <checkpoint-directory> and <output-file> must be absolute paths
        """.stripMargin
      )
      System.exit(1)
    }
    */

    val Array(checkpointDirectory, outputPath) = Array("/home/brijeshdhaker/dstreams/data/","/home/brijeshdhaker/IdeaProjects/spark-bigdata-examples/dstream.log")
    val ssc = StreamingContext.getOrCreate(checkpointDirectory, () => createContext("/home/brijeshdhaker/dstreams/log/dstream.log", checkpointDirectory))

    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println