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
		message.userArgs.put("reciever", username);
		message.userArgs.put("command", "GET_LOGGEDIN" );
		
		messageManager.post(message);
	
	}

	@Override
	public void getRegisteredUsers(String username) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username);
		AgentMessage message = new AgentMessage();
		message.userArgs.put("reciever", username);
		message.userArgs.put("command", "GET_REGISTERED" );
		
		messageManager.post(message);
			
	}

	@Override
	public void getMessages(String username) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, username);
		AgentMessage message = new AgentMessage();
		message.userArgs.put("reciever", username);
		message.userArgs.put("command", "GET_MESSAGES");
		
		messageManager.post(message);
		
	}

	@Override
	public void sendMessage(UserMessage userMessage) {

		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, userMessage.sender);
		
		AgentMessage message = new AgentMessage();
		message.userArgs.put("reciever", userMessage.sender);
		message.userArgs.put("command", "NEW_MESSAGE");
		message.userArgs.put("target", userMessage.recipient);
		message.userArgs.put("sender", userMessage.sender);
		message.userArgs.put("subject", userMessage.subject);
		message.userArgs.put("content", userMessage.content);

		messageManager.post(message);

		
	}

	@Override
	public void logOut(String username) {
		System.out.println("Sent log out request for user: " + username);
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, "MASTER");
		System.out.println("Found Master agent for deletion of other agents: " + agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, "MASTER").getAgentId());
		AgentMessage message = new AgentMessage();
		message.userArgs.put("reciever", "MASTER");
		message.userArgs.put("command", "LOGOUT" );
		message.userArgs.put("username", username);
		
		boolean res = chatManager.logOut(username);
		
		if(res) {
			System.out.println("Odlogovan: " + username);
			messageManager.post(message);		

		}
		
	}

	@Override
	public void sendMessageToAll(UserMessage userMessage) {
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, userMessage.sender);
		AgentMessage message = new AgentMessage();	
		message.userArgs.put("command", "GROUP_MESSAGE");
		message.userArgs.put("reciever", userMessage.sender);
		message.userArgs.put("sender", userMessage.sender);
		message.userArgs.put("subject", userMessage.subject);
		message.userArgs.put("content", userMessage.content);
		
		messageManager.post(message);
	}

}
