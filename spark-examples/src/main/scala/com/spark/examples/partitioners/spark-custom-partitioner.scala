package com.spark.examples.partitioners

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
class TwoPartsPartitioner(override val numPartitions: Int) extends Partitioner {
  def getPartition(key: Any): Int = key match {
    case s: String => {
      if (s(0).toUpper > 'J') 1 else 0
    }
  }
}

object SparkCustomPartitioner extends App {

  val conf = new SparkConf().setAppName("spark-custom-partitioner").setMaster("local[4]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")
  var x = sc.parallelize(Array(("sandeep",1),("giri",1),("abhishek",1),("sravani",1),("jude",1)), 3)
  x.glom().collect()

  //Array(Array((sandeep,1)), Array((giri,1), (abhishek,1)), Array((sravani,1), (jude,1)))
  //[ [(sandeep,1)], [(giri,1), (abhishek,1)], [(sravani,1), (jude,1)] ]


  var y = x.partitionBy(new TwoPartsPartitioner(2))
  y.glom().collect()
  //Array(Array((giri,1), (abhishek,1), (jude,1)), Array((sandeep,1), (sravani,1)))
  //[ [(giri,1), (abhishek,1), (jude,1)], [(sandeep,1), (sravani,1)] ]

}
