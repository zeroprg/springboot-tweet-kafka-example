# Apache Kafka example for Spring Boot with Tweeter API

Example code for connecting to a Apache Kafka and produce messages from 5 different Tweets topics into 5 different file in real time.

Possible to run this example on cluster and authenticate with SASL/SCRAM based on https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-kafka
To quickly test this example you can create a free Apache Kafka instance at https://www.cloudkarafka.com

## Running locally

If you just want to test it out.

### Configure

All of the authentication settings can be found in the Details page for your CloudKarafka instance.

You configure Spring boot in the application.properties file, here you set the brokers to connect to
and the credentials for authentication.

Here is an example of the properties file
```
spring.kafka.bootstrap-servers=${CLOUDKARAFKA_BROKERS:velomobile-01.srvs.cloudkafka.com:9094,velomobile-02.srvs.cloudkafka.com:9094,velomobile-03.srvs.cloudkafka.com:9094}
#Use these options only if you want to secure Kafka server
#spring.kafka.properties.security.protocol=SASL_SSL
#spring.kafka.properties.sasl.mechanism=SCRAM-SHA-256
spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="${CLOUDKARAFKA_USERNAME}" password="${CLOUDKARAFKA_PASSWORD}";
spring.kafka.consumer.group-id=${CLOUDKARAFKA_USERNAME}-consumers

spring.kafka.consumer.auto-offset-reset=latest
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.consumer.properties.spring.json.trusted.packages=sample.kafka

cloudkarafka.topic=${CLOUDKARAFKA_USERNAME}-default
```
CLOUDKARAFKA_BROKERS should be a comma-separated list of all the brokers, the URL must include the port, for example:

`velomobile-01.srvs.cloudkafka.com:9094,velomobile-02.srvs.cloudkafka.com:9094,velomobile-03.srvs.cloudkafka.com:9094`

Please note that prefixing the topic name and consumer group id is only necessary if your broker is hosted on CloudKarafka on Developer Duck plan,
this is because on the Developer Duck plan your instance is running on a shared broker.
If you are running a dedicated plan on CloudKarafka or have your own server, you can use any value for both of the properties

### Run

```
git clone https://github.com/zeroprg/springboot-tweet-kafka-example.git
cd springboot-tweet-kafka-example
mvn spring-boot:run -DCLOUDKARAFKA_USERNAME=vitaly -DCLOUDKARAFKA_BROKERS=localhost:9092
# Only if you specify security on Kafka server add this additional parameter  -DCLOUDKARAFKA_PASSWORD=<password>'
```


It will produce the messages from 5 different Tweeter's topics ('microservices','springcloud','kafka','twitter stream API','springCloud') and populate 5 different files with different tweets.
```
#Twitters embedded properties
debug=true
oauth.consumerKey=r1wFskT3q
oauth.consumerSecret=fBbmp71HKbqalpizIwwwkBpKC
oauth.accessToken=298FPfE16frABXMcRIn7aUSSnNneMEPrUuZ
oauth.accessTokenSecret=1LMNZZIfrAimpD004QilV1pH3PYTvM
twitter.topics=microservices,springcloud,kafka,twitter stream API,springCloud
```

Tweets places in MessageChannel outputChannel in method : TwitterConfig.twitterFlow and send by kafkaProducer to Kafka server

How to setup to configure and run Kafka server use this URL: https://www.sohamkamani.com/blog/2017/11/22/how-to-install-and-run-kafka/
