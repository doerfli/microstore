# Microstore 

[![Build Status](https://travis-ci.com/doerfli/microstore.svg?branch=master)](https://travis-ci.com/doerfli/microstore)

Experiment project for microservices 

# TODOs and ideas

- Propper failure handling
- Kafka Streams
- Send Mails using Mailinator
- UI with React/Vue/Angular/...

# Container startup for development

Only start infrastructure containers, no services

```
docker-compose up -d customer-redis payment-redis inventory-redis zookeeper kafka elasticsearch kibana
```

Local logging with logstash into elasticsearch container (kibana viewer available at http://localhost:5601)

```
export CLOUDSTORE_ROOT=<absolute path to git root>
logstash -f logstash/logstash.conf
```

# Kafka configuration and setup

## Create a Topic and Produce Data

Source: https://docs.confluent.io/current/installation/docker/docs/installation/single-node-client.html

### Start zookeeper and kafka

```
docker-compose up -d zookeeper
docker-compose up -d kafka
```

### Create and verify topic 

```
docker run \
--net=microstore_default \
--rm confluentinc/cp-kafka:5.1.0 \
kafka-topics --create --topic foo --partitions 1 --replication-factor 1 \
--if-not-exists --zookeeper zookeeper:2181
```

```
docker run \
--net=microstore_default \
--rm \
confluentinc/cp-kafka:5.1.0 \
kafka-topics --describe --topic foo --zookeeper zookeeper:2181
```

### Create data and read back

```
docker run \
--net=microstore_default \
--rm \
confluentinc/cp-kafka:5.1.0 \
bash -c "seq 42 | kafka-console-producer --request-required-acks 1 \
--broker-list kafka:9092 --topic foo && echo 'Produced 42 messages.'"
```

```
docker run \
--net=microstore_default \
--rm \
confluentinc/cp-kafka:5.1.0 \
kafka-console-consumer --bootstrap-server kafka:9092 --topic foo --from-beginning --max-messages 42
```


## Command center

```
docker run -d \
--name=control-center \
--net=cloudstore_default \
--ulimit nofile=16384:16384 \
-p 9021:9021 \
-v /tmp/control-center/data:/var/lib/confluent-control-center \
-e CONTROL_CENTER_ZOOKEEPER_CONNECT=zookeeper:2181 \
-e CONTROL_CENTER_BOOTSTRAP_SERVERS=kafka:9092 \
-e CONTROL_CENTER_REPLICATION_FACTOR=1 \
-e CONTROL_CENTER_MONITORING_INTERCEPTOR_TOPIC_PARTITIONS=1 \
-e CONTROL_CENTER_INTERNAL_TOPICS_PARTITIONS=1 \
-e CONTROL_CENTER_STREAMS_NUM_STREAM_THREADS=2 \
-e CONTROL_CENTER_CONNECT_CLUSTER=http://kafka-connect:8082 \
confluentinc/cp-enterprise-control-center:5.1.0
```

# Kong 

## Startup

```
docker-compose up -d kong-db
docker-compose run kong kong migrations bootstrap
docker-compose up kong
```

