package chatroom.client;

import javax.swing.JOptionPane;

public class PopUpWindow {

	 public static void infoBox(String infoMsg, String titleBar){
        JOptionPane.showMessageDialog(null, infoMsg, titleBar, 
        		JOptionPane.INFORMATION_MESSAGE);
    }
}
