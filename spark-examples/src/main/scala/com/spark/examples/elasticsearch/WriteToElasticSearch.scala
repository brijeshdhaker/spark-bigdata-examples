package com.spark.examples.elasticsearch

import org.apache.spark.sql.SparkSession

object WriteToElasticSearch {

  def main(args: Array[String]): Unit = {
    WriteToElasticSearch.writeToIndex()
  }

  def writeToIndex(): Unit = {

    val spark = SparkSession
      .builder()
      .appName("WriteToES")
      .master("local[*]")
      .config("es.nodes","localhost")
      .config("es.port","9200")
      .config("es.net.http.auth.user","elastic")
      .config("es.net.http.auth.pass","admin")
      .config("es.nodes.wan.only","true") // Needed for ES hosted on AWS
      .getOrCreate()

    import spark.implicits._

    val indexDocuments = Seq(
      AlbumIndex("Led Zeppelin", 1969, "Led Zeppelin"),
      AlbumIndex("Boston", 1976, "Boston"),
      AlbumIndex("Fleetwood Mac", 1979, "Tusk")
    ).toDF

    //indexDocuments.saveToEs("demoindex/albumindex")

    indexDocuments.toDF().write.format("org.elasticsearch.spark.sql")
      .option("es.mapping.id", "_id")
      .option("es.update.script.inline", "ctx._source.location = params.location")
      .option("es.update.script.params", "location:")
      .option("es.write.operation", "upsert")
      .option("es.nodes.wan.only","true")
      .option("es.port","9200")
      .option("es.net.ssl","false")
      .option("es.nodes", "http://localhost")
      .mode("append")
      .save("demoindex/albumindex")

  }
}

