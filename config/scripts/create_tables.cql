DROP KEYSPACE demo
CREATE KEYSPACE demo
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE demo;

DROP TABLE IF EXISTS results;
CREATE TABLE results
(
	query_id int,
	group_by varchar,
	result varchar,
	PRIMARY KEY (query_id, group_by)
);

DROP TABLE IF EXISTS queries;
CREATE TABLE queries
(
	id int,
	query varchar,
	PRIMARY KEY (id)
);