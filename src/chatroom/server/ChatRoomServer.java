package chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static chatroom.constants.ChatRoomConstant.*;

public class ChatRoomServer {
	private HashMap<String, Socket> socketMap;
	private ServerSocket serverSock;
	private ReentrantLock lock;
	
	public ChatRoomServer(int portNum){
		System.out.println("Chat Room Server Started, listening now.........");
		socketMap = new HashMap<String, Socket>();
		lock = new ReentrantLock();
		try {
			serverSock = new ServerSocket(portNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer(){
		while(true){
			Socket clientSock = null;
			try {
				clientSock = serverSock.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
				PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
				String id = in.readLine();
				if(socketMap.containsKey(id)){
					out.println(USER_NAME_INVALID);
					clientSock.close();
					in.close();
					out.close();
					continue;
				}
				else out.println(USER_NAME_OK);
				System.out.println("Server is Connecting with Client(" + id +"):" + clientSock);
				socketMap.put(id, clientSock);
				if(in.readLine().equals(READY_FOR_BROADCAST)){
//					System.out.println("Start BroadCasting!");
					boardCastOnlineUserList();
				}
					
				new Thread(new Channel(id, clientSock)).start();
			} catch (IOException e) {e.printStackTrace();}
			
		}
	}
	
	private void boardCastOnlineUserList() throws IOException{
		lock.lock();
		for(String id:socketMap.keySet()){
			PrintWriter out = new PrintWriter(socketMap.get(id).getOutputStream(), true);
			out.println(BROADCAST_START);
			for(String target:socketMap.keySet()){
				if(!id.equals(target)){
//					out.println(BROADCAST_MSG);
					out.println(target);
				}
			}
			out.println(BROADCAST_END);
		}
		lock.unlock();
	}
	
	private class Channel implements Runnable{
		private Socket clientSock;
		private String ID;
		public Channel(String id,Socket clientSock){
			this.ID = id;
			this.clientSock = clientSock;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			BufferedReader in = null;
			PrintWriter out = null;
			try {
				in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
				out = new PrintWriter(clientSock.getOutputStream(), true);
				CharSeq msg = new CharSeq(MAX_INPUT_SIZE);
				String msgType = null;
				while ((msgType = in.readLine())!=null) {// read a null, if client sock is closed
					if(msgType.equals(CHAT_MSG)){
						String targetUser = in.readLine();
						Socket targetSock = socketMap.get(targetUser);
						PrintWriter targetOut = new PrintWriter(targetSock.getOutputStream(), true);
						in.read(msg.charSeq);
						targetOut.println(CHAT_MSG);
						targetOut.println(msg.charSeq);
						msg.nullify();
					}
				}
				if(msgType == null){
					System.out.println("Client(" +this.ID+"):" + clientSock + " disconnected!");
					socketMap.remove(this.ID);
					try {
						boardCastOnlineUserList();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}catch(IOException e){
				System.out.println("Client(" +this.ID+"):" + clientSock + " crashed!");
				socketMap.remove(this.ID);
				try {
					boardCastOnlineUserList();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		ChatRoomServer crs = new ChatRoomServer(SERVER_PORT_NUM);
		crs.startServer();
	}
}
