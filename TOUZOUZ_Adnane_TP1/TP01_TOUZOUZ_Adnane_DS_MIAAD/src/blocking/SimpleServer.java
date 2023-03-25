package blocking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.InputStream;
import java.io.OutputStream;

public class SimpleServer {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(1234);
		System.out.println("I m waiting new connection ...");
		Socket socket = ss.accept();
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		System.out.println("I m waiting data");
		int nb = is.read();
		int res = nb*23;
		System.out.println("I m sending response");
		os.write(res);
		socket.close();
	}

}