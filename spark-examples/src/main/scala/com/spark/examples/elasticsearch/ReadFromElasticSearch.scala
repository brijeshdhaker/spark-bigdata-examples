package com.spark.examples.elasticsearch

import org.apache.spark.sql.SparkSession

object ReadFromElasticSearch {

  def main(args: Array[String]): Unit = {
    ReadFromElasticSearch.readFromIndex()
  }

  def readFromIndex(): Unit = {

    val spark = SparkSession
      .builder()
      .appName("WriteToES")
      .master("local[*]")
      .config("cluster.name", "docker-cluster")
      .config("es.nodes","localhost")
      .config("es.port","9200")
      .config("es.net.http.auth.user","elastic")
      .config("es.net.http.auth.pass","admin")
      .config("es.nodes.wan.only", "true") // Needed for ES hosted on AWS
      .getOrCreate()

    //indexDocuments.saveToEs("demoindex/albumindex")
    //val albums = spark.esDF("demoindex/albumindex")

    val reader = spark.read.format("org.elasticsearch.spark.sql")
      .option("es.read.metadata", "true")
      .option("es.nodes.wan.only","true")
      .option("es.port","9200")
      .option("es.net.ssl","false")
      .option("es.nodes", "http://localhost")

    // check the associated schema
    val esDF = reader.load("demoindex/albumindex")
    println(esDF.schema.treeString)
    esDF.show()

  }
}

case class AlbumIndex(artist:String, yearOfRelease:Int, albumName: String)
