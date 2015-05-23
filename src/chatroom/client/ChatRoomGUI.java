package chatroom.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import static chatroom.constants.ChatRoomConstant.*;

@SuppressWarnings("serial")
public class ChatRoomGUI extends JFrame{
	//Application widgets
	private JButton buttonConnect;
	private JButton buttonDisconnect;
	private JButton buttonSend;
	private JLabel lableUserName;
	private JLabel lableOnlineUsers;
	
	private JScrollPane spOnlineUsersList;
	public JEditorPane epOnlineUsersList;
	public JList<String> onlineUserList;
	public DefaultListModel<String> listModel;
	
	private JScrollPane spTypeIn;
	public JTextPane epTypeIn;
	
	private JScrollPane spChatContent;
	public JEditorPane epChatContent;
	public JTextField textFielduserName;
	//end of declration of app widgets
	
	//event Handler
	public ChatRoomHandler handler;
	//every GUI window has its corresponding client
	public ChatRoomClient client;
	
	//Toolkit
	private Toolkit toolkit;
	
	//Jpanel for the purpose of layout
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel eastPanel;
	private JPanel southPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	
	public StringBuffer contentSB;
	public String ID = null;
	public boolean isConnected = false;
	public Thread listeningThread;
	public HashSet<String> targetUsers = null;
	public HashSet<String> onlineUserSet;
	
	public ChatRoomGUI(){
		super("Chat Room");
		setLocation(200, 200);
//		setPreferredSize(new Dimension(850, 490));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		toolkit = Toolkit.getDefaultToolkit();
		Image frameIcon = toolkit.getImage("res\\voice-chat-icon.png");
		setIconImage(frameIcon);
		handler = new ChatRoomHandler(this);
		contentSB = new StringBuffer();
		targetUsers = new HashSet<String>();
		onlineUserSet = new HashSet<String>();
		initComponents();
		myLayout();
	}

	//define every widgets
	private void initComponents() {
		this.buttonConnect = new JButton("CONNECT");
		buttonConnect.setFont(FONT_DISPLAY);
		this.buttonDisconnect = new JButton("DISCONNECT");
		buttonDisconnect.setFont(FONT_DISPLAY);
		this.buttonSend = new JButton("SEND");
		buttonSend.setFont(FONT_DISPLAY);
		buttonSend.setPreferredSize(new Dimension(160,160));
		
		this.buttonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handler.respondConnectButtonPressed();
			}
		});
		this.buttonDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handler.respondDisconnectButtonPressed();
			}
		});
		this.buttonSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handler.respondSendButtonPressed();
			}
		});
		
		
		lableUserName = new JLabel("User Name");
		lableUserName.setFont(FONT_DISPLAY);
		lableOnlineUsers = new JLabel("Online Users");
		lableOnlineUsers.setFont(FONT_DISPLAY);
		
//		list = new JList(data); //data has type Object[]
//		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//		list.setVisibleRowCount(-1);

		listModel = new DefaultListModel<String>();
		onlineUserList = new JList<String>(listModel);
		onlineUserList.setFont(FONT_DISPLAY);
		onlineUserList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		onlineUserList.setLayoutOrientation(JList.VERTICAL);
		onlineUserList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				handler.respondToListSelected(e);
			}
		});
		
//		epOnlineUsersList = new JEditorPane();
//		epOnlineUsersList.setEditable(false);
//		epOnlineUsersList.setFont(FONT_INPUT);
		spOnlineUsersList = new JScrollPane(onlineUserList,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spOnlineUsersList.setPreferredSize(new Dimension(150,450));
		
		epTypeIn = new JTextPane(new DefaultStyledDocument(){
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		        if ((getLength() + str.length()) <= MAX_INPUT_SIZE) {
		            super.insertString(offs, str, a);
		        }
		        else {
		        	toolkit.beep();
		        	PopUpWindow.infoBox("You can't type in more than "+
		        				MAX_INPUT_SIZE +" characters!", "Warning");
		        }
		    }
		});
		
		epTypeIn.setFont(FONT_INPUT);
//		Image contentBackgroundImage = toolkit.getImage("res\\content-background.jpg");
		spTypeIn = new JScrollPane(epTypeIn,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spTypeIn.setPreferredSize(new Dimension(450,180));
		
		epChatContent = new JEditorPane();
		epChatContent.setEditable(false);
		epChatContent.setFont(FONT_INPUT);
		spChatContent = new JScrollPane(epChatContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spChatContent.setPreferredSize(new Dimension(630,240));
		
		textFielduserName = new JTextField(new DefaultStyledDocument(){
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		        if ((getLength() + str.length()) <= USER_NAME_MAX_LENGTH) {
		            super.insertString(offs, str, a);
		        }
		        else {
		        	toolkit.beep();
		        	PopUpWindow.infoBox("User Name has to be within "+
		        					USER_NAME_MAX_LENGTH + " Characters", "Warning");
		        }
		    }
		}, null,12);
		
		textFielduserName.setPreferredSize(new Dimension(150,30));
		textFielduserName.setFont(FONT_INPUT);
		
		epTypeIn.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				
			}
			
			public void keyReleased(KeyEvent e) {
				handler.respondToKeyReleased(e);
			}
			
			public void keyPressed(KeyEvent e) {
				handler.respondToKeyPressed(e);
			}
		});
	}
	
	//Setup of the layout of all components
	private void myLayout() {
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		rightPanel = new JPanel();
		
		northPanel.add(this.lableUserName);
		northPanel.add(this.textFielduserName);
		northPanel.add(this.buttonConnect);
		northPanel.add(this.buttonDisconnect);
		
		centerPanel.add(spChatContent);
		
		southPanel.add(spTypeIn);
		southPanel.add(buttonSend);
		
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		eastPanel.add(lableOnlineUsers);
		eastPanel.add(spOnlineUsersList);
		
		leftPanel.add(northPanel);
		leftPanel.add(centerPanel);
		leftPanel.add(southPanel);
		
		rightPanel.add(eastPanel);
		
		this.setLayout(new FlowLayout());
		this.add(leftPanel);
		this.add(rightPanel);
		this.pack();
	}
	
	
	/**
	 * this method still won't solve the Jlist update issue,
	 * Have to make sure UI update happens only in event dispatch thread
	 */
	public void startUndatingOnlineUserList(){
		SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>() {
			protected Void doInBackground() throws Exception {
				while(true){
					if(onlineUserSet.size() != listModel.getSize()){
						listModel.clear();
						for(String s:onlineUserSet){
							listModel.addElement(s);
						}
							
					}
				}
				
			}
		};
		worker.execute();
	}
	
}
