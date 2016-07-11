package websockets;

import bank.Bank;
import jmr.BankImpl;
import jmr.Request;
import org.glassfish.tyrus.server.Server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.rmi.Remote;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/bank", decoders = RequestDecoder.class, encoders = RequestEncoder.class)
public class BankServer {

    private static Bank bank;
    private static List<Session> sessions = new CopyOnWriteArrayList<>();

    private static void notifyListeners(String id){
        for(Session s : sessions){
            try{
                s.getBasicRemote().sendText(id);
            } catch (IOException e) {
                sessions.remove(s);
            }
        }
    }

    public static void main(String[] args) throws DeploymentException, IOException {
        Server server = new Server("localhost", 2222, "/ws", null, BankServer.class);
        server.start();
        bank = new BankImpl(new bank.local.Driver.Bank(), BankServer::notifyListeners);
        System.out.println("server running, press key to stop");
        System.in.read();
    }

    @OnOpen
    public void onOpen(Session session){
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    @OnMessage
    public Request onMessage(Request request){
        request.handleRequest(bank);
        return request;
    }
}
