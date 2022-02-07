package com.spark.examples.dataformats

import org.apache.spark.sql.SparkSession

object ORCReadWrite extends App{

  val spark: SparkSession = SparkSession.builder()
    .master("local[*]")
    .appName("ORCDataformats")
    .getOrCreate()

  val data =Seq(("James ","","Smith","36636","M",3000),
    ("Michael ","Rose","","40288","M",4000),
    ("Robert ","","Williams","42114","M",4000),
    ("Maria ","Anne","Jones","39192","F",4000),
    ("Jen","Mary","Brown","","F",-1))

  val columns=Seq("firstname", "middlename", "lastname", "dob", "gender", "salary")
  val df=spark.createDataFrame(data).toDF(columns:_*)

  df.write.mode("overwrite")
    .orc("/tmp/orc/data.orc")

  df.write.mode("overwrite")
    .option("compression","none12")
    .orc("/tmp/orc/data-nocomp.orc")

  df.write.mode("overwrite")
    .option("compression","zlib")
    .orc("/tmp/orc/data-zlib.orc")

  val df2=spark.read.orc("/tmp/orc/data.orc")
  df2.show(false)

  df2.createOrReplaceTempView("ORCTable")
  val orcSQL = spark.sql("select firstname,dob from ORCTable where salary >= 4000 ")
  orcSQL.show(false)

  spark.sql("CREATE TEMPORARY VIEW PERSON USING orc OPTIONS (path \"/tmp/orc/data.orc\")")
  spark.sql("SELECT * FROM PERSON").show()

}
