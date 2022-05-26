package chatmanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;

import ConnectionManager.ConnectionManager;
import models.User;
import models.UserMessage;
import ws.WSChat;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Singleton
@LocalBean
public class ChatManagerBean implements ChatManagerRemote {

	private List<User> registered = new ArrayList<>();
	private List<User> loggedIn = new ArrayList<>();
	
	private List<User> loggedInRemote = new ArrayList<>();
	private List<UserMessage> messages = new ArrayList<>();
	
	@EJB
	private WSChat ws;
	
	@EJB
	private ConnectionManager connectionManager;
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
	}

	@Override
	public boolean register(User user) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(user.getUsername()));
		System.out.println(exists);
		if(exists) {
			return false;
		}
		registered.add(user);
		return true;
	}

	@Override
	public String login(String username, String password) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(username) && u.getPassword().equals(password));
		if(exists) {
			if(!activeUser(username)) {
			loggedIn.add(getRegisteredByUsername(username));
			ws.notifyNewLogin(username);
			sendLogInToNetwork(username);
			return "bravo";
			}
			return "fail";
		}
		return "invalid";
	}
	
	

	private boolean activeUser(String username) {
		for(String activeUser: getActiveUsernames()) {
			if(activeUser.equals(username))
				return true;
		}
		
		for(User activeUser : loggedInRemote()) {
			if(activeUser.getUsername().equals(username))
				return true;
		}
		return false;
	}

	@Override
	public boolean logOut(String username) {
		if (activeUser(username)) {
			loggedIn.removeIf(u -> u.username.equals(username));
			sendLogOutToNetwork(username);
			return true;
		} else {
			return false;
		}			
	}



	

	

	private User getRegisteredByUsername(String username) {
	
		for(User u : registered) {
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	@Override
	public void addRemoteLoggedIn(User user) {

		loggedInRemote.add(new User(user.getUsername(), user.getPassword()));
		ws.notifyNewLogin(user.getUsername());
	}

	@Override
	public void removeRemoteActive(String username) {

		loggedInRemote.removeIf(u -> u.getUsername().equals(username));
		ws.notifyLogOut(username);
	}

	@Override
	public List<String> getActiveUsernames() {
		
		List<String> activeUsernames = new ArrayList<>();
		for(User user : loggedIn) {
			activeUsernames.add(user.getUsername());
		}
		return activeUsernames;
	}

	@Override
	public List<String> getRegisteredUsernames() {
		List<String> usernames = new ArrayList<>();
		for(User user : registered) {
			usernames.add(user.getUsername());
		}
		return usernames;
	}

	@Override
	public List<UserMessage> getUserMessages(String username) {
		List<UserMessage> userMessages = new ArrayList<>();
		for(UserMessage message : messages) {
			if(message.recipient.equals(username))
				userMessages.add(message);
		}
		return userMessages;
	}

	@Override
	public void saveRemoteMessage(UserMessage userMessage) {

		messages.add(userMessage);
		ws.sendMessage(userMessage.recipient, userMessage);
	}

	@Override
	public void saveMessage(UserMessage msg) {
		
		messages.add(msg);
		sendMessageToNetwork(msg);
		
	}

	@Override
	public List<User> getActiveUsers() {
		
		return loggedIn;
	}

	@Override
	public void logOutFromNode(String nodeAlias) {
		for(User u: loggedInRemote) {
			if(u.password.equals(nodeAlias)) {
				ws.notifyLogOut(u.username);
			}
		}
		loggedInRemote.removeIf(u -> u.password.equals(nodeAlias));
	}

	@Override
	public List<User> loggedInRemote() {
		return loggedInRemote;
	}

	@Override
	public void sendLogInToNetwork(String username) {

		connectionManager.notifyAllNewLogin(username);
		
	}

	@Override
	public void sendLogOutToNetwork(String username) {
		connectionManager.notifyAllLogout(username);
		
	}

	@Override
	public void sendMessageToNetwork(UserMessage msg) {

		connectionManager.notifyAllNewMessage(msg);
	}

}
