package sample.tweeter;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.CollectionUtils;
import twitter4j.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


public class TwitterMessageProducer extends MessageProducerSupport {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TwitterStream twitterStream;
    private List<Long> follows;
    private List<String> terms;
    private StatusListener statusListener;
    private FilterQuery filterQuery;
    public TwitterMessageProducer(TwitterStream twitterStream, MessageChannel outputChannel) {
        this.twitterStream = twitterStream;
        setOutputChannel(outputChannel);
    }
    @Override
    protected void onInit() {
        super.onInit();
        statusListener = new StatusListener();
        long[] followsArray = null;
        if (!CollectionUtils.isEmpty(follows)) {
            followsArray = new long[follows.size()];
            for (int i = 0; i < follows.size(); i++) {
                followsArray[i] = follows.get(i);
            }
        }
        String[] termsArray = null;
        if (!CollectionUtils.isEmpty(terms)) {
            termsArray = terms.toArray(new String[0]);
        }
        filterQuery = new FilterQuery(0, followsArray, termsArray);
    }
    @Override
    public void doStart() {
        twitterStream.addListener(statusListener);
        twitterStream.filter(filterQuery);
    }
    @Override
    public void doStop() {
        twitterStream.cleanUp();
        twitterStream.clearListeners();
    }
    public void setFollows(List<Long> follows) {
        this.follows = follows;
    }
    public void setTerms(List<String> terms) {
        this.terms = terms;
    }
    public StatusListener getStatusListener() {
        return statusListener;
    }
    FilterQuery getFilterQuery() {
        return filterQuery;
    }
    public class StatusListener extends StatusAdapter {
        @Override
        public void onStatus(Status status) {
            sendMessage(MessageBuilder.withPayload(status).build());
        }
        @Override
        public void onException(Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        @Override
        public void onStallWarning(StallWarning warning) {
            log.warn(warning.toString());
        }
    }
}