package http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {

	public static void main(String args[]) throws IOException {

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/commands", new MyHandler());
		server.start();

	}

	static class MyHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String response = "Command received";
			exchange.sendResponseHeaders(200, response.length());
			Object ob = exchange.getResponseBody();
			OutputStream oos = exchange.getResponseBody();
			oos.write(response.getBytes());
			oos.close();
		}
	}
}
