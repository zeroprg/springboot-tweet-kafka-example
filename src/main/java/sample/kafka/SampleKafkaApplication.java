/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import sample.tweeter.TwitterMessageProducer;
import twitter4j.Status;
import twitter4j.TwitterStream;

@SpringBootApplication
public class SampleKafkaApplication {
    @Autowired
    private TwitterMessageProducer twitterMessageProducer;
    @Autowired
    private PollableChannel outputChannel;
    @Autowired
    private TwitterStream twitterStream;

    public static void main(String[] args) {
        SpringApplication.run(SampleKafkaApplication.class, args);
    }


    @Bean
    public ApplicationRunner runner(Producer producer) {
        return (args) -> {
            for(long i = 1; i < 20; i++) {
                    producer.send(new SampleMessage(i, "A simple test message"));
            }
        };
    }

    /*
@Bean
public ApplicationRunner runner(Producer producer) {
    return (args) -> {

        TwitterMessageProducer twitterProducer = new TwitterMessageProducer(twitterStream, outputChannel);
        twitterProducer.doStart();

        TwitterMessageProducer.StatusListener statusListener = twitterMessageProducer.getStatusListener();
      //  Status status = mock(Status.class);
        //statusListener.onStatus(status);
        Message<?> statusMessage = outputChannel.receive();
    };
*/

}
