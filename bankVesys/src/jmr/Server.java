package jmr;

import bank.Bank;
import bank.local.Driver;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.DeliveryMode;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Server {
    static final int deliveryMode = DeliveryMode.NON_PERSISTENT;

    public static void main(String[] args) throws Exception {
        final Context jndiContext = new InitialContext();
        final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        final Queue queue = (Queue) jndiContext.lookup("/queue/BANK");
        final Topic topic = (Topic) jndiContext.lookup("/topic/BANK");

        try(JMSContext context = factory.createContext()){
            JMSConsumer queueConsumer = context.createConsumer(queue);
            JMSProducer queueProducer = context.createProducer().setDeliveryMode(deliveryMode);
            final JMSProducer topicProducer = context.createProducer().setDeliveryMode(deliveryMode).setTimeToLive(10_000);

            Bank bank = new BankImpl(new Driver.Bank(), id -> topicProducer.send(topic, id));

            System.out.println("JMS Server is running...");
            while(true){
                Message msg = queueConsumer.receive();
                Request r = msg.getBody(Request.class);
                r.handleRequest(bank);
                queueProducer.send(msg.getJMSReplyTo(), r);
            }
        }
    }
}
