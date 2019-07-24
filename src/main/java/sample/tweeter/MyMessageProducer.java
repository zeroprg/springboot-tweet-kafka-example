package sample.tweeter;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

public class MyMessageProducer extends MessageProducerSupport {
    public MyMessageProducer(MessageChannel outputChannel) {
        // Defining an output channel is required
        setOutputChannel(outputChannel);
    }
    @Override
    protected void onInit() {
        super.onInit();
        // Custom initialization - if applicable - comes here
    }
    @Override
    public void doStart() {
        // Lifecycle method for starting receiving messages
    }
    @Override
    public void doStop() {
        // Lifecycle method for stopping receiving messages
    }
    private void receiveMessage() {
        // Receive data from upstream service
        //SomeData data = ...;
        Object data = null;
        // Convert it to a message as appropriate and send it out
        this.sendMessage(MessageBuilder.withPayload(data).build());
    }
}