package chatroom.server;


public class CharSeq {
	public char charSeq[];
	
	public CharSeq(int size){
		charSeq = new char[size];
 	}
	public CharSeq(int size, String s){
		charSeq = new char[size];
		for(int i=0;i<s.length();i++)
			charSeq[i] = s.charAt(i);
 	}
	
	
	public void nullify(){
		for(int i=0;i<charSeq.length;i++)
			charSeq[i] = 0;
	}
	
	public String toString(){
		int i = 0;
		for(i=charSeq.length-1;i>=0;i--){
			if(charSeq[i] != 0 && charSeq[i] != '\n'){
				break;
			}
		}
		if(i == 0) return null;
		char cc[] = new char[i+1];
		for(int j=0;j<i+1;j++)
			cc[j] = charSeq[j];
		return new String(cc);
	}
}
