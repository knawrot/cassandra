## To run Cassandra:
home/hadoop/apache-cassandra-2.1.4/bin/cassandra

## To load input database files already uploaded on the cluster:
home/hadoop/apache-cassandra-2.1.4/bin/cqlsh -f /home/hadoop/cassandra-development/scripts/create_table.cql

## To run an application already uploaded on the cluster
java -jar /home/hadoop/cassandra-development/cassandra-0.0.1-SNAPSHOT-jar-with-dependencies.jar