package com.spark.examples.hbase.dataframe

import org.apache.spark.sql.SparkSession

object HBaseSparkInsert {

  case class Employee(key: String, fName: String, lName: String,
                      mName:String, addressLine:String, city:String,
                      state:String, zipCode:String)

  def main(args: Array[String]): Unit = {



    def catalog =
      s"""{
         |"table":{"namespace":"default", "name":"employee"},
         |"rowkey":"key",
         |"columns":{
         |"key":{"cf":"rowkey", "col":"key", "type":"string"},
         |"fName":{"cf":"person", "col":"firstName", "type":"string"},
         |"lName":{"cf":"person", "col":"lastName", "type":"string"},
         |"mName":{"cf":"person", "col":"middleName", "type":"string"},
         |"addressLine":{"cf":"address", "col":"addressLine", "type":"string"},
         |"city":{"cf":"address", "col":"city", "type":"string"},
         |"state":{"cf":"address", "col":"state", "type":"string"},
         |"zipCode":{"cf":"address", "col":"zipCode", "type":"string"}
         |}
         |}""".stripMargin


    val data = Seq(Employee("1", "Abby", "Smith", "K", "3456 main", "Orlando", "FL", "45235"),
      Employee("2", "Amaya", "Williams", "L", "123 Orange", "Newark", "NJ", "27656"),
      Employee("3", "Alchemy", "Davis", "P", "Warners", "Sanjose", "CA", "34789"))

    val spark: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExamples.com")
      .getOrCreate()

    import org.apache.hadoop.hbase.spark.datasources._
    import spark.implicits._

    val df = spark.sparkContext.parallelize(data).toDF

    df.write
      .option("hbase.use.hbase.context", false)
      .option("hbase.config.resources", "/opt/sandbox/hbase-2.4.9/conf/hbase-site.xml")
      .option("hbase.spark.config.location", "/opt/sandbox/spark-2.3.4/conf")
      .option("hbase-push.down.column.filter", false)
      .options(Map(HBaseTableCatalog.tableCatalog -> catalog, HBaseTableCatalog.newTable -> "4"))
      .format("org.apache.hadoop.hbase.spark")
      .save()



  }
}