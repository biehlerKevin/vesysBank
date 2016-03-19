package http;

import bank.Request.GetAccountNumbers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Created by mfrey on 19/03/2016.
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/bank");

        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestMethod("POST");
        urlCon.setDoOutput(true); // to be able to write.
        urlCon.setDoInput(true); // to be able to read.
        urlCon.connect();

        ObjectOutputStream oos = new ObjectOutputStream(urlCon.getOutputStream());
        oos.writeObject(new GetAccountNumbers());
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(urlCon.getInputStream());
        Set<String> resp = (Set<String>)ois.readObject();
        System.out.println(resp.toString());
        ois.close();
    }
}
