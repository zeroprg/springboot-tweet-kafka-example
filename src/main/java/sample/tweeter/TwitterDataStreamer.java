package sample.tweeter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sample.kafka.Producer;
import sample.kafka.SampleMessage;
import twitter4j.*;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterDataStreamer {

    /** The actual Twitter stream. It's set up to collect raw JSON data */

    private TwitterStream twitterStream;

    @Value("${tweeter.consumerKeyStr}")
    String consumerKeyStr; // = "r1wFskT3q";

    @Value("${tweeter.consumerSecretStr}")
    String consumerSecretStr;// = "fBbmp71HKbqalpizIwwwkBpKC";

    @Value("${tweeter.accessTokenStr}")
    String accessTokenStr;// =           "298FPfE16frABXMcRIn7aUSSnNneMEPrUuZ";

    @Value("${tweeter.accessTokenSecretStr}")
    String accessTokenSecretStr;// = "1LMNZZIfrAimpD004QilV1pH3PYTvM";

    @Autowired
    Producer producer;

    public void start() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumerKeyStr);
        cb.setOAuthConsumerSecret(consumerSecretStr);
        cb.setOAuthAccessToken(accessTokenStr);
        cb.setOAuthAccessTokenSecret(accessTokenSecretStr);
        cb.setJSONStoreEnabled(true);
        cb.setIncludeEntitiesEnabled(true);

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                // send the data to kafka
                producer.send(new SampleMessage(status.getId(), status.getText()));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        }).sample();
    }
}