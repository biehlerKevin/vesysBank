package jmr;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class JmsHandler implements RequestHandler {
    private final TemporaryQueue responseQueue;
    private final JMSProducer producer;
    private final JMSConsumer consumer;
    private final ObjectMessage request;
    private final Queue queue;

    public JmsHandler(JMSContext context, Queue queue) throws  JMSException{
        this.responseQueue = context.createTemporaryQueue();
        this.producer = context.createProducer().setDeliveryMode(Server.deliveryMode);
        this.consumer = context.createConsumer(responseQueue);
        this.request = context.createObjectMessage();
        this.queue = queue;
    }

    @Override
    public Request handle(Request req) throws IOException {
        try{
            request.setObject(req);
            request.setJMSReplyTo(responseQueue);
            producer.send(queue, request);
            return consumer.receiveBody(Request.class);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }
}
