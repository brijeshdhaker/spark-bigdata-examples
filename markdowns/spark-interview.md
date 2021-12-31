#### 1. Suppose you have a spark dataframe which contains millions of records. You need to perform multiple actions on it. How will you minimize the execution time?
Answer : You can use cache or persist. For eg say you have dataframe df and if you use df1=df.cache() ,then df1 will be stored in its storage. once it is stored in its storage, multiple actions can be performed. Only first action will take longer time than others because on the first action, it actually caches the data. You can check the storage size of df1 from spark application tracker.

You can pass different storage level in persist.
Different storage levels are :
MEMORY_ONLY
DISK_ONLY
MEMORY_AND_DISK
MEMORY_ONLY_SER
MEMORY_AND_DISK_SER

#### 2. If your cluster have limited resources, and there are many applications which need to be run, how would you ensure that your spark application will take the fixed number of resource and hence does not impact execution of other applications?
Answer : While submitting the spark application pass these two parameters .

–num-executors 10
–conf spark.dynamicAllocation.enabled = false

Note: you can change the number of executors if you need.

#### 3.How would you check if rdd is empty, without using collect?
Answer : You can use rdd.isEmpty ,it will return true if rdd is empty.

#### 4. There is a json file with following content :-
{“dept_id”:101,”e_id”:[10101,10102,10103]}
{“dept_id”:102,”e_id”:[10201,10202]}

And data is loaded into spark dataframe say mydf, having below dtypes
dept_id: bigint, e_id: array<bigint>
What will be the best way to get the e_id individually with dept_id ?
Answer :
we can use the explode function , which will explode as per the number of items in e_id .
The code would be like
mydf.withColum(“e_id”,explode($”e_id”)).
Here we have taken the new column same as old column, the dtypes of opdf will be
dept_id: bigint, e_id:bigint
So output would look like
```commandline
+———------+——-----+
|  dept_id|  e_id |
+———------+----——-+
|  101    |  10101|
|  101    |  10102|
|  101    |  10103|
|  102    |  10201|
|  102    |  10202|
+———------+——-----+
```

#### Q2 . How many number of column will be present in the df2, if df1 have three columns a1,a2,a3
Var df2=df.withColumn(“b1”,lit(“a1”)).withColumn(“a1”,lit(“a2”)).withColumn(“a2”,$“a2”).withColumn(“b2”,$”a3”)).withColumn(“a3”,lit(“b1”))

Answer :
Total 5 As below

Df // a1,a2,a3

df.withColumn(“b1”,lit(“a1”)) //a1,a2,a3,b1
.withColumn(“a1”,lit(“a2”)) //a1,a2,a3,b1
.withColumn(“a2”,$“a2”) //a1,a2,a3,b1
.withColumn(“b2”,$”a3”))//a1,a2,a3,b1,b2
.withColumn(“a3”,lit(“b1”))//a1,a2,a3,b1,b2



####  Q3 . How to get RDD with its element indices.
Say myrdd = (a1,b1,c1,s2,s5)
Output should be ((a1,0),(b1,1),(c1,2),(s2,3),(s5,4))
Answer : we can use zipWithIndex function
```commandline
var myrdd_windx = myrdd.zipWithIndex()
```

#### Q4. What is Spark Streaming?
Apache Spark Streaming is a scalable fault-tolerant streaming processing system that natively supports both batch and streaming workloads. Spark Streaming is an extension of the core Spark API that allows data engineers and data scientists to process real-time data from various sources including (but not limited to) Kafka, Flume, and Amazon Kinesis.

Four Major Aspects of Spark Streaming
Fast recovery from failures and stragglers
Better load balancing and resource usage
Combining of streaming data with static datasets and interactive queries
Native integration with advanced processing libraries (SQL, machine learning, graph processing)

![](images/Apache-Spark-Streaming-ecosystem-diagram.png)


#### 
#### 

#### 