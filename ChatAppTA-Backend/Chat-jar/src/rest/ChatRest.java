package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.User;

@Remote
public interface ChatRest {
	@POST
	@Path("users/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user);
	
	@POST
	@Path("users/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user);
	
}
