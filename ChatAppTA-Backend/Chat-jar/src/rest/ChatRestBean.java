package rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import util.JNDILookup;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@LocalBean
@Path("/chat")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;
	@EJB
	private ChatManagerRemote chatManager;
	
	private AgentManagerRemote agentManager = JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	
	@Override
	public Response register(User user) {
		
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, "MASTER");
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "MASTER");
		message.userArgs.put("command", "REGISTER");
		message.userArgs.put("username", user.getUsername());

		boolean res = chatManager.register(new User(user.getUsername(), user.getPassword()));
		
		if(res) {
			System.out.println("Sucessfully registered user: " + user.getUsername());
			messageManager.post(message);
		}
		
		return Response.status(Status.OK).entity(res).build();
	}

	@Override
	public Response login(User user) {
		
		agentManager.getByIdOrStartNew(JNDILookup.ChatAgentLookup, "MASTER");

		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "MASTER");
		message.userArgs.put("command", "LOGIN");
		message.userArgs.put("username", user.getUsername());
		
		String res = chatManager.login(user.getUsername(), user.getPassword());
		
	    if(res.equals("bravo")) {
	    	System.out.println("Ulogovan si bato: " + user.getUsername());
	    	messageManager.post(message);
	    	return Response.status(Status.OK).entity(res).build();
	    }else if(res.equals("fail")) {
	    	
	    	System.out.println("Pa ulogovan si vec dbl.");
	    	return Response.status(Status.NOT_FOUND).entity(res).build();

	    }else {
	    	
	    	System.out.println("Ti ne licis ni na jednu.");
	    	return Response.status(Status.CONFLICT).entity(res).build();

	    }
				
				
	}

	

}
