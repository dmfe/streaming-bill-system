CREATE KEYSPACE sbs_keyspace WITH REPLICATION = {
  'class' : 'SimpleStrategy',
  'replication_factor' : 1
};

CREATE TABLE sbs_keyspace.collected_events (
  epoch_hour int,
  transaction_id text,
  count int,

  PRIMARY KEY (
    epoch_hour,
    transaction_id
  )
);