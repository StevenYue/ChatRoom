package chatroom.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import static chatroom.constants.ChatRoomConstant.*;


public class ChatRoomHandler {
	private ChatRoomGUI gui;
	
	public ChatRoomHandler(ChatRoomGUI gui){
		this.gui = gui;
	}

	public void respondConnectButtonPressed() {
		if(this.gui.isConnected) return;
		try {
			String userID = gui.textFielduserName.getText();
			if(userID.length() == 0){
				PopUpWindow.infoBox( USER_NAME_REQUIRED_MSG, INVALID_INPUT_TITLE);
				return;
			}
			this.gui.client = new ChatRoomClient(this.gui);
			this.gui.client.sendMSG(userID);
			if(this.gui.client.in.readLine().equals(USER_NAME_INVALID)){
				PopUpWindow.infoBox( USER_NAME_EXSITED_MSG, INVALID_INPUT_TITLE);
				this.gui.client.closeConnection();
				return;
			}
			this.gui.ID = userID;
			this.gui.setTitle(userID + "  Online......");
			
			this.gui.listeningThread = new Thread(this.gui.client);
			this.gui.listeningThread.start();
			this.gui.client.sendMSG(READY_FOR_BROADCAST);
			this.gui.isConnected = true;
			
			
//			this.gui.startUndatingOnlineUserList();
		} catch (IOException e) {
			PopUpWindow.infoBox(CONNECTION_ERROR_MSG, CONNECTION_ERROR_TITLE);
		}
		
	}

	public void respondDisconnectButtonPressed() {
		// TODO Auto-generated method stub
		this.gui.client.closeConnection();
		this.gui.isConnected = false;
		this.gui.textFielduserName.setText("");
		this.gui.listModel.clear();
		this.gui.onlineUserSet.clear();
		this.gui.setTitle("Chat Room");
		
	}

	public void respondSendButtonPressed() {
		// TODO Auto-generated method stub
		if(this.gui.targetUsers.size() == 0){
			PopUpWindow.infoBox(CHOOSE_USER_MSG, CHOOSE_USER_TITLE);
			return;
		}
		String input = gui.epTypeIn.getText();
		if(input == null || input.length() == 0) return;
		
		String response = "";
		Date date = new Date();
		response += this.gui.ID + " ["+date.getMonth()+"/"+date.getDate()+" "+date.getHours()
				+":"+date.getMinutes()+":"+date.getSeconds()+"]: ";
		response += input + '\n';
		for(String targetUser:this.gui.targetUsers){
			gui.client.sendMSG(CHAT_MSG);
			gui.client.sendMSG(targetUser);
			gui.client.sendContent(response);
		}
		
		gui.contentSB.append(response);
		if(response != null){
			gui.epChatContent.setText(gui.contentSB.toString());
		}
		gui.epTypeIn.setText("");
	}
	
	
	public void respondToKeyTyped(KeyEvent e){
	}

	public void respondToKeyReleased(KeyEvent e) {
	}

	public void respondToKeyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
			respondSendButtonPressed();
		}
	}

	public void respondToListSelected(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()){
			int A[] = this.gui.onlineUserList.getSelectedIndices();
			this.gui.targetUsers.clear();
			for(Integer i:A){
				String name = (String)this.gui.listModel.getElementAt(i);
				if(!this.gui.targetUsers.contains(name)){
					this.gui.targetUsers.add(name);
				}
			}
		}
	}
}
