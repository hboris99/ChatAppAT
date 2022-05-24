package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import models.UserMessage;
import util.JNDILookup;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class ChatAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;

	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Chat Agent!");
	}

	//private List<String> chatClients = new ArrayList<String>();

	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
	}

	@Override
	public void handleMessage(Message message) {
		TextMessage tmsg = (TextMessage) message;

		String receiver;
		try {
			receiver = (String) tmsg.getObjectProperty("receiver");
			if (agentId.equals(receiver)) {
				String option = "";
				String username = "";
				UserMessage userMessage = new UserMessage();
				try {
					option = (String) tmsg.getObjectProperty("command");
					switch (option) {
					case "REGISTER":
						
						username = (String) tmsg.getObjectProperty("username");
						ws.notifyNewRegistration(username);
						break;
					case "LOGIN":
						username = (String) tmsg.getObjectProperty("username");
						
						break;
					case "GET_LOGGEDIN":
						List<String> activeUsernames = chatManager.getActiveUsernames();
						List<User> activeRemoteUsers = chatManager.loggedInRemote();
						for(String user: activeUsernames) {
							ws.sendMessage(receiver, "LOGIN%" + user);
						}for(User u : activeRemoteUsers) {
							ws.sendMessage(receiver, "LOGIN%" + u.getUsername());
						}
						
						break;
					case "GET_REGISTERED":
						List<String> registeredUsers = chatManager.getRegisteredUsernames();
						for(String registered : registeredUsers) {
							ws.sendMessage(receiver,"REGISTRATION%" + registered);	
						}
						break;
					case "x":
						break;
					default:
						response = "ERROR!Option: " + option + " does not exist.";
						break;
					}
					System.out.println(response);
					ws.onMessage("chat", response);
					
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String init(String id) {
		agentId = id;
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}

	@Override
	public String getAgentId() {
		return agentId;
	}
}
