package blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyTelnetClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost",1234);
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			new Thread(()->{
				String request;
				try {
					while ((request=br.readLine())!=null) {
						String response = br.readLine();
						System.out.println(response);}
				} catch(IOException e) {
					throw new RuntimeException(e);
				}
			}).start();
			Scanner scanner = new Scanner(System.in);
			String request;
			while(true) {
				request = scanner.nextLine();
				pw.println(request);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
