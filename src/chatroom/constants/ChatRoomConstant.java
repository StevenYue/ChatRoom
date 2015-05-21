package chatroom.constants;

import java.awt.Font;
import java.net.URL;

public class ChatRoomConstant {
	public static final Font FONT_DISPLAY = new Font("ARIAL BLACK", Font.PLAIN, 16);
    public static final Font FONT_INPUT = new Font("TIMES NEW ROMAN", Font.BOLD, 22);
    
    public static final int SERVER_PORT_NUM = 27272;
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_IP = "127.0.0.1";
    
    public static final int MAX_INPUT_SIZE = 1000;
    public static final int USER_NAME_MAX_LENGTH = 20;
    
    public static final String CONNECTION_ERROR_MSG = "Can't Connect!";
    public static final String CONNECTION_ERROR_TITLE = "Connection Error!";
    
    public static final String USER_NAME_REQUIRED_MSG = "User Name Required!";
    public static final String USER_NAME_EXSITED_MSG = "User Name Exsited!";
    public static final String INVALID_INPUT_TITLE = "Invalid Input!";
    
    public static final String CHOOSE_USER_MSG = "Please Choose a User!";
    public static final String CHOOSE_USER_TITLE = "No User Selected!";
    
    public static final String BROADCAST_MSG = "bcm$(^";
    public static final String BROADCAST_START = "bcS~@`";
    public static final String BROADCAST_END = "bcE$(*";
    public static final String READY_FOR_BROADCAST = "rfb%#*";
    public static final String CHAT_MSG = "cm)#&";
    
    public static final String USER_NAME_OK = "un_OK@*!";
    public static final String USER_NAME_INVALID = "un_iv(^$";
}
