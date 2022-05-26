package rest;

import javax.ejb.Remote;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import models.UserMessage;

@Remote
public interface RemoteChatRest {
	
	@GET
	@Path("/users/loggedIn/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getLoggedUsers(@PathParam("userId") String username);	
	
	
	@GET
	@Path("/users/registered/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getRegisteredUsers(@PathParam("userId") String username);	
	
	
	@GET
	@Path("/messages/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getMessages(@PathParam("userId") String username);	
	
	
	@POST
	@Path("/messages/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessage(UserMessage userMessage);	
	
	
	@DELETE
	@Path("/users/loggedIn/{userId}")
	public void logOut(@PathParam("userId") String username);	
	
	
	@POST
	@Path("/messages/all")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessageToAll(UserMessage userMessage);	

}
