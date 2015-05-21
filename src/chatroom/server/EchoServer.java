package chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static chatroom.constants.ChatRoomConstant.*;

public class EchoServer implements Runnable{
	private Socket clientSocket;
	public static void main(String[] args) {
		System.out.println("Chat Room Server Started, listening now.........");
		ServerSocket serverSock = null;
		Socket clientSock = null;
		Runnable chatRoomServer = null;
		try {
			serverSock = new ServerSocket(SERVER_PORT_NUM);
		} catch (IOException e) {e.printStackTrace();}
		
		while(true){
			try {
				clientSock = serverSock.accept();
				chatRoomServer = new EchoServer(clientSock);
			} catch (IOException e) {e.printStackTrace();}			
			Thread thread = new Thread(chatRoomServer);
			thread.start();
		}
	}
	
	public EchoServer(Socket clientSock){
		this.clientSocket = clientSock;
		System.out.println("Server is Connecting with Client:" + clientSocket);
	}
	
	
	public void run(){
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
//			String msg = null;
			CharSeq msg = new CharSeq(MAX_INPUT_SIZE);
			while (in.read(msg.charSeq) > 0) {
//				System.out.print("Server: "+msg.toString()+"&&&");
				out.println(msg.toString());
				msg.nullify();
			}  
			clientSocket.close();
			in.close();
			out.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client:" + clientSocket + " crashed!");
//			e.printStackTrace();
		}
	}

}
