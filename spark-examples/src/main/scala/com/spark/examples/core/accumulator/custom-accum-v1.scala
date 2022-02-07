package com.spark.examples.core.accumulator
/*
class MyComplexV1(var x: Int, var y: Int) extends Serializable{
  def reset(): Unit = {
    x = 0
    y = 0
  }
  def add(p:MyComplexV1): MyComplexV1 = {
    x = x + p.x
    y = y + p.y
    return this
  }
}

import org.apache.spark.{AccumulatorParam, SparkConf, SparkContext}

class ComplexAccumulatorV1 extends AccumulatorParam[MyComplexV1] {

  def zero(initialVal: MyComplexV1): MyComplexV1 = {
    return initialVal
  }

  def addInPlace(v1: MyComplexV1, v2: MyComplexV1): MyComplexV1 = {
    v1.add(v2)
    return v1;
  }
}

object SparkCustomAccum extends App {

  val conf = new SparkConf().setAppName("spark-custom-partitioner").setMaster("local[4]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  val vecAccum = sc.accumulator(new MyComplexV1(0,0))(new ComplexAccumulatorV1)

  var myrdd = sc.parallelize(Array(1,2,3))
  def myfunc(x:Int):Int = {
    vecAccum += new MyComplexV1(x, x)
    return x * 3
  }
  var myrdd1 = myrdd.map(myfunc)
  myrdd1.collect()
  vecAccum.value.x
  vecAccum.value.y

}

*/