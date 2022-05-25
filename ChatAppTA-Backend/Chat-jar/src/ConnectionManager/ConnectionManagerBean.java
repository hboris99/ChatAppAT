package ConnectionManager;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import chatmanager.ChatManagerRemote;
import models.Host;
import models.UserMessage;
import ws.WSChat;
@Singleton
@Startup
@Remote(ConnectionManager.class)
@Path("/connection")
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
		String alias = getNodeAlias() + PORT;
		String address = getNodeAddress();

		localNode = new Host(alias, address);
		
		System.out.println("StARTED NODE: " + localNode.alias + " AT " + localNode.address);
		
		String masterAlias = getMasterAlias();
		if(masterAlias != null && masterAlias != PORT) {
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget resteasyWebTarget = resteasyClient.target(HTTP + masterAlias + "/Chat-war/api/connection");
			ConnectionManager manager = resteasyWebTarget.proxy(ConnectionManager.class);
			
			cluster = manager.registerNewnode(localNode.alias);
			cluster.add(masterAlias);
			cluster.removeIf(n -> n.equals(localNode.alias));
			resteasyClient.close();
			
			
		}
	}
	

	private String getMasterAlias() {
		try {
			InputStream inputStream = ConnectionManagerBean.class.getClassLoader().getResourceAsStream("../properties/connection.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
			return properties.getProperty("master") + PORT;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
			}
	}


	private String getNodeAlias() {
		return System.getProperty("jboss.node.name");
	}


	private String getNodeAddress() {
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			return (String) mBeanServer.getAttribute(http, "boundAddress");
		} catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | ReflectionException | MBeanException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<String> registerNewnode(String nodeAlias) {
		
		addNode(nodeAlias);
		for(String node: cluster) {
			if(!node.equals(nodeAlias)) {
				ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
				ResteasyWebTarget target = resteasyClient.target(HTTP + node + "/Chat-war/api/connection");
				ConnectionManager manager = target.proxy(ConnectionManager.class);
				manager.addNode(nodeAlias);
				resteasyClient.close();
			}
		}
		
		return getNodes();
		
	}

	@Override
	public void addNode(String nodeAlias) {
		if (!nodeAlias.equals(localNode.getAlias())) {
			cluster.add(nodeAlias);
		}		
	}

	@Override
	public List<String> getNodes() {
		return cluster;
	}


	@Override
	public void deleteNode(String node) {

		cluster.removeIf(c -> c.equals(node));
		chatManager.logOutFromNode(node);
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
