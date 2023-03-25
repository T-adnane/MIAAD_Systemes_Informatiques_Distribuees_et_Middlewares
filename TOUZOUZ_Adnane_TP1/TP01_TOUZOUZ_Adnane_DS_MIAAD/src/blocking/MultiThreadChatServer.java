package blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadChatServer extends Thread {
	private List<Conversation>conversations=new ArrayList<>();
	int clientsCount;
	
	public static void main(String[] args) {
		new MultiThreadChatServer().start();
	}
	
	public void run() {
		System.out.println("The server is started using port = 1234");
		try{
			ServerSocket serverSocket = new ServerSocket(1234);
			while(true) {
				Socket socket = serverSocket.accept();
				++clientsCount;
				Conversation conversation = new Conversation(socket,clientsCount);
				conversations.add(conversation);
				conversation.start();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	class Conversation extends Thread {
		private Socket socket;
		private int clientId;
		public Conversation(Socket socket, int clientId) {
			this.socket = socket;
			this.clientId=clientId;
		}
		public void run() {
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				String ip = socket.getRemoteSocketAddress().toString();
				System.out.println("New Client connection => "+clientId+" IP= "+ip);
				pw.println("Welcome, your ID is => "+clientId);
				String request;
				while((request=br.readLine())!=null) {
					System.out.println("New Request => IP = "+ip+" Request = "+request);
					List<Integer> clientsTo=new ArrayList<>();
					String message;
					if(request.contains("=>")) {
						String[] items = request.split("=>");
						String clients = items[0];
						message = items[1];
						if(clients.contains(",")) {
							String[] clientIds = clients.split(",");
							for(String id: clientIds) {
								clientsTo.add(Integer.parseInt(id));
							}
						}else {
							clientsTo.add(Integer.parseInt(clients));
						}
					}else {
						clientsTo=conversations.stream().map(c->c.clientId).collect(Collectors.toList());
						message=request;
					}
					broadcastMessage(message,this,clientsTo);
				}
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public void broadcastMessage(String message, Conversation from,List<Integer> clients) {
		try {
			for(Conversation conversation:conversations) {
				if(conversation!=from && clients.contains(conversation.clientId)) {
					Socket socket = conversation.socket;
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter pw = new PrintWriter(outputStream,true);
					pw.println(message);
				}
			}
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
