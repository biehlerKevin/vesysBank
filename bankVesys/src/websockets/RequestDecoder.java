package websockets;

import jmr.Request;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * Created by mfrey on 11/07/2016.
 */
public class RequestDecoder implements Decoder.Binary<Request> {
    @Override
    public Request decode(ByteBuffer byteBuffer) throws DecodeException {
        byte[] buf;
        if(byteBuffer.hasArray()) buf = byteBuffer.array();
        else { buf = new byte[byteBuffer.capacity()]; byteBuffer.get(buf);}
        try{
            return (Request) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean willDecode(ByteBuffer byteBuffer) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
