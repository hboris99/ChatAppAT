package rest;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.UserMessage;
import util.JNDILookup;
@Stateless
@LocalBean

@Path("/chat")
public class RemoteChatRestBean implements RemoteChatRest {
	
	@EJB
	public MessageManagerRemote messageManager;
	
	@EJB
	public ChatManagerRemote chatManager;
	
	private AgentManagerRemote agentManager = JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);

	@Override
	public void getLoggedUsers(String username) {

		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username);
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", username);
		message.userArgs.put("command", "GET_LOGGEDIN" );
		
		messageManager.post(message);
	
	}

	@Override
	public void getRegisteredUsers(String username) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username);
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", username);
		message.userArgs.put("command", "GET_REGISTERED" );
		
		messageManager.post(message);
			
	}

	@Override
	public void getMessages(String username) {
		System.out.println("Looking for: " + username + " messages.");
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username);
		System.out.println("Found " + username + " agent with the id: " + 	
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username).getAgentId());
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", username);
		message.userArgs.put("command", "GET_MESSAGES");
		
		messageManager.post(message);
		
	}

	@Override
	public void sendMessage(UserMessage userMessage) {

		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, userMessage.sender);
		
		AgentMessage message = new AgentMessage();
		System.out.println(userMessage.recipient);
		

		message.userArgs.put("receiver", userMessage.sender);
		message.userArgs.put("command", "NEW_MESSAGE");
		message.userArgs.put("target", userMessage.recipient);
		message.userArgs.put("sender", userMessage.sender);
		message.userArgs.put("subject", userMessage.subject);
		message.userArgs.put("content", userMessage.content);

		messageManager.post(message);

		
	}

	@Override
	public void logOut(String username) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, "MASTER");
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "MASTER");
		message.userArgs.put("command", "LOGOUT" );
		message.userArgs.put("username", username);
		boolean res = chatManager.logOut(username);
		if(res) {
			System.out.println("Odlogovan: " + username);
			messageManager.post(message);		

		}
		System.out.println("Logged out");
	}

	@Override
	public void sendMessageToAll(UserMessage userMessage) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, userMessage.sender);
		AgentMessage message = new AgentMessage();	
		message.userArgs.put("command", "GROUP_MESSAGE");
		message.userArgs.put("receiver", userMessage.sender);
		message.userArgs.put("sender", userMessage.sender);
		message.userArgs.put("subject", userMessage.subject);
		message.userArgs.put("content", userMessage.content);
		
		messageManager.post(message);
	}

}
