package ConnectionManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import chatmanager.ChatManagerRemote;
import models.Host;
import models.UserMessage;
import ws.WSChat;

public class ConnectionManagerBean implements ConnectionManager{
	
	private Host localNode;
	
	private List<String> cluster = new ArrayList<String>();
	
	private static final String HTTP = "http://";
	private static final String PORT = ":8080";
	
	
	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private WSChat ws;
	
	
	@PostConstruct
	public void initialize() {
		String address = getNodeAddress();
	}
	

	private String getNodeAddress() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<String> registerNewnode(String nodeAlias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNode(String nodeAlias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void syncLoggedIn(String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNode(String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addRemoteLogin(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRemoteLogin(String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRemoteMessage(UserMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAllNewMessage(UserMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAllNewLogin(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAllLogout(String user) {
		// TODO Auto-generated method stub
		
	}

}
