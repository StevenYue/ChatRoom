package chatroom.client;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class ChatRoomListModel extends AbstractListModel{
	ArrayList<String> list;
	
	public ChatRoomListModel(){
		list = new ArrayList<String>();
	}
	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Object getElementAt(int index) {
		return list.get(index);
	}
	
	public void add(String s) {
	    if (list.add(s)) {
	      fireContentsChanged(this, 0, getSize());
	    }
	    fireIntervalAdded(s,  0,  getSize());    
	}
	
	public void removeElement(int i) {
	    String removed = list.remove(i);
	    if (removed != null) {
	      fireContentsChanged(this, 0, getSize());
	    }
	}
	
	public void clear() {
	    list.clear();
	    fireContentsChanged(this, 0, getSize());
	}
	
	public boolean contains(Object element) {
	    return list.contains(element);
	}
	
}
