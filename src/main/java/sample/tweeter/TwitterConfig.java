package sample.tweeter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import sample.kafka.Producer;
import sample.kafka.SampleMessage;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


import java.util.List;


@Configuration
public class TwitterConfig {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("#{'${twitter.topics}'.split(',')}")
    private List<String> twitterTopics;

/*
    @Bean
    Producer producer() { return new Producer()r()}  ;
*/

    @Bean
    TwitterStreamFactory twitterStreamFactory() {
        return new TwitterStreamFactory();
    }
    @Bean
    TwitterStream twitterStream(TwitterStreamFactory twitterStreamFactory) {
        return twitterStreamFactory.getInstance();
    }
    @Bean
    MessageChannel outputChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    TwitterMessageProducer twitterMessageProducer(
            TwitterStream twitterStream, MessageChannel outputChannel) {
        TwitterMessageProducer twitterMessageProducer =
                new TwitterMessageProducer(twitterStream, outputChannel);
        twitterMessageProducer.setTerms(twitterTopics);
        return twitterMessageProducer;
    }


    /**
     *  Finally flow itself
     * **/
    @Bean
    IntegrationFlow twitterFlow(MessageChannel outputChannel) {

        return IntegrationFlows.from(outputChannel)
                .transform(Status::getText)
                .handle(m -> { String mesg = m.getPayload().toString(); log.info(mesg);})
                .get();
    }
}