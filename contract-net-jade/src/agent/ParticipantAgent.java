package agent;

import behaviour.ParticipantCFP;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ParticipantAgent extends Agent {
	
	private boolean working;
	
	public boolean getWorking()
	{
		return	working;
	}
	
	public void setWorking(boolean working)
	{
		this.working = working;
	}

	protected void setup() {
		
		this.working = false;
		
		Object[] args = getArguments();

		// arg 0 - my service
		if (args != null && args.length == 1) {
			
			registerDF("cfp", "participant");

			String service = (String) args[0];
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
					MessageTemplate.MatchPerformative(ACLMessage.CFP));
			
			addBehaviour(new ParticipantCFP(this, template, service));
		}

	}
	
	private void registerDF(String serviceName, String serviceType) {

		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName(serviceName);
			sd.setType(serviceType);
			// Agents that want to use this service need to "know" the
			// serviceType-ontology
			sd.addOntologies(serviceType + "-ontology");
			// Agents that want to use this service need to "speak" the FIPA-SL language
			sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
			// sd.addProperties(new Property("country", "Italy"));
			dfd.addServices(sd);

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
