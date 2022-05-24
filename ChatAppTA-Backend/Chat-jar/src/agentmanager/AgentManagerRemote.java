package agentmanager;

import javax.ejb.Remote;

import agents.Agent;

@Remote
public interface AgentManagerRemote {
	public String startAgent(String name, String id);
	public Agent getAgentById(String agentId);
	public Agent getByIdOrStartNew(String name, String id);
	public void stop(String name);
}
