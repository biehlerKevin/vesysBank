package jmr;

import bank.Bank;
import bank.BankDriver2;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mfrey on 11/07/2016.
 */
public class Driver implements BankDriver2 {
    private Bank bank;
    private JMSContext requestContext;
    private JMSContext listenerContext;
    private final List<UpdateHandler> listeners = new CopyOnWriteArrayList<>();


    @Override
    public void registerUpdateHandler(UpdateHandler handler) throws IOException {
        listeners.add(handler);
    }

    @Override
    public void connect(String[] args) throws IOException {
        try{
            Context jndiContext = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

            requestContext = factory.createContext();
            Queue queue = (Queue) jndiContext.lookup("/queue/BANK");
            bank = new ClientBank(new JmsHandler(requestContext, queue));

            listenerContext = factory.createContext();
            Topic topic = (Topic) jndiContext.lookup("/topic/BANK");
            JMSConsumer consumer = listenerContext.createConsumer(topic);
            consumer.setMessageListener(msg -> {
                try{
                    String id = msg.getBody(String.class);
                    for(UpdateHandler h: listeners){
                        try{
                            h.accountChanged(id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disconnect() throws IOException {
        requestContext.close();
        listenerContext.close();
        bank = null;
        System.out.println("disconnected...");
    }

    @Override
    public Bank getBank() {
        return bank;
    }
}
