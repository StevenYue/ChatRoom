package chatroom.client;

import static chatroom.constants.ChatRoomConstant.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import chatroom.server.CharSeq;

public class ChatRoomClient implements Runnable{
	private ReentrantLock lock;
	private Socket socket;
	private PrintWriter out;
	public BufferedReader in;
	private ChatRoomGUI gui;
	
	public ChatRoomClient(ChatRoomGUI gui) throws IOException{
			socket = new Socket(SERVER_IP, SERVER_PORT_NUM);
//			ps = new PrintStream(socket.getOutputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.gui = gui;
			lock = new ReentrantLock();
	}
	
	public String monitorIncoming(){
		CharSeq msg = new CharSeq(MAX_INPUT_SIZE);
		try {
			in.read(msg.charSeq);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return msg.toString();
	}

	public void sendMSG(String input) {
		out.println(input);
	}
	public void sendContent(String input){
		CharSeq msg = new CharSeq(MAX_INPUT_SIZE,input);
		out.println(msg.charSeq);
	}
	
	public void closeConnection(){
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public void run() {
		while(true){
			try{
//				this.lock.lock();
				String res = in.readLine();
				if(res == null) return;
				if (res.equals(BROADCAST_START)) {
					HashSet<String> set = new HashSet<String>();
					while(!(res = in.readLine()).equals(BROADCAST_END)){
						if(!this.gui.onlineUserSet.contains(res)){
							this.gui.onlineUserSet.add(res);
						}
						set.add(res);
					}
					for(String s:this.gui.onlineUserSet){
						if(!set.contains(s)){
							gui.onlineUserSet.remove(s);
						}
					}
					//When data update is ready, use this SwingUtilities.invokeLate
					// to render UI, this is guaranteed to be executed on EDT
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							gui.listModel.clear();
							for(String s:gui.onlineUserSet)
								gui.listModel.addElement(s);
						}
					});
					
				}
				else if(res.equals(CHAT_MSG)){
					CharSeq msg = new CharSeq(MAX_INPUT_SIZE);
					in.read(msg.charSeq);
					this.gui.contentSB.append(msg.toString());
					this.gui.contentSB.append('\n');
					
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							gui.epChatContent.setText(gui.contentSB.toString());
						}
					});
					
				}
//				this.lock.unlock();
			}catch(IOException | ConcurrentModificationException e2){
				break;
			}
			
		}
	}
}


