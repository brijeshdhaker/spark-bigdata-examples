#
# What exactly is big data?
The definition of big data is data that contains greater variety, arriving in increasing volumes and with more velocity. This is also known as the three Vs.
![big_data](../images/bigdata_vs.png)

#
### What is the Architectural Pattern of Big Data?
![](../images/xenonstack-big-data-framework-ingestion.webp)

### Big data benefits:
* Big data makes it possible for you to gain more complete answers because you have more information.
* More complete answers mean more confidence in the data—which means a completely different approach to tackling problems.

### Big Data challenges ?
* **Complexity** Organizations still struggle to keep pace with their data and find ways to effectively store it.
* **Skillset** Clean data, or data that’s relevant to the client and organized in a way that enables meaningful analysis, requires a lot of work. Data scientists spend 50 to 80 percent of their time curating and preparing data before it can actually be used.
* **Technology maturity** Finally, big data technology is changing at a rapid pace. A few years ago, Apache Hadoop was the popular technology used to handle big data. Then Apache Spark was introduced in 2014. Today, a combination of the two frameworks appears to be the best approach. Keeping up with big data technology is an ongoing challenge.
* **Security** Big data solutions usually rely on storing all static data in a centralized data lake. Securing access to this data can be challenging, especially when the data must be ingested and consumed by multiple applications and platforms.

### Big data architecture style ..?
![big_data](../images/big-data-logical.svg)

Big data solutions typically involve one or more of the following types of workload:
* Batch processing of big data sources at rest.
* Real-time processing of big data in motion.
* Interactive exploration of big data.
* Predictive analytics and machine learning.

#
### Why we need Data Pipeline?
* Convert incoming data to a common format.
* Prepare data for Analysis and Visualization.
* Migrate between Databases.
* Share Data Processing logic across Web Apps, Batch Jobs, and APIs.
* Power your Data Ingestion and Integration tools.

#
### What is the Lambda Architecture : 
# 
Lambda architecture is a data-processing architecture designed to handle massive quantities of data by taking advantage of both batch and stream-processing methods. 
This approach to architecture attempts to balance latency, throughput, and fault-tolerance by using batch processing to provide comprehensive and accurate views 
of batch data, while simultaneously using real-time stream processing to provide views of online data.

![Lambda Architecture](../images/lambda.png)

#
### What is the Kappa Architecture :
# 
A drawback to the lambda architecture is its complexity. Processing logic appears in two different places — the cold and hot paths — using different frameworks. This leads to duplicate computation logic and the complexity of managing the architecture for both paths.

The kappa architecture was proposed by Jay Kreps as an alternative to the lambda architecture. It has the same basic goals as the lambda architecture, but with an important distinction: All data flows through a single path, using a stream processing system.

![kappa](../images/kappa.png)

There are some similarities to the lambda architecture's batch layer, in that the event data is immutable and all of it is collected, instead of a subset. The data is ingested as a stream of events into a distributed and fault tolerant unified log. These events are ordered, and the current state of an event is changed only by a new event being appended. Similar to a lambda architecture's speed layer, all event processing is performed on the input stream and persisted as a real-time view.

If you need to recompute the entire data set (equivalent to what the batch layer does in lambda), you simply replay the stream, typically using parallelism to complete the computation in a timely fashion.

#
#### How to generate Live Report with last 20 yr of Data.
![process-large-data](../images/process-large-data.png)

#
#### When We should go for No Sql Database
![sql-nosql](../images/SQL-NOSQL.png)

The structure of many different forms of data is more easily handled and evolved with a NoSQL database. 
NoSQL databases are often better suited to storing and modeling structured, semi-structured, 
and unstructured data in one database.