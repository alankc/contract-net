package agent;

import java.util.Date;
import behaviour.InitiatorCFP;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.SearchConstraints;

@SuppressWarnings("serial")
public class InitiatorAgent extends Agent {

	private String[] services;
	private String[] participants;

	protected void setup() {

		Object[] args = getArguments();

		// arg 0 - array of services
		// arg 1 - number of participants
		if (args != null && args.length == 2) {
			
			registerDF("cfp", "initiator");

			services = (String[]) args[0];
			// participants = (String[]) args[1];
			participants = getParticipants("participant");

			if (participants != null && participants.length < (int) args[1]) {
				System.out.print(getLocalName() + "\tError in YELLOW PAGES!");
				System.exit(0);
			}

			// Adding CFPs
			for (int i = 0; i < services.length; i++) {
				ACLMessage msg = generateMsg(services[i]);
				long start = System.nanoTime();
				addBehaviour(new InitiatorCFP(this, msg, participants.length, start));
				// System.out.println(getLocalName() + " - start: " + start / 1000000.0);
			}
		}

	}

	private ACLMessage generateMsg(String service) {
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);

		// Setting receivers
		for (int i = 0; i < participants.length; ++i) {
			msg.addReceiver(new AID((String) participants[i], AID.ISLOCALNAME));
		}

		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

		// deadline to reply is 4 seconds
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 4000));
		msg.setContent(service);

		return msg;
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

	private String[] getParticipants(String serviceType) {
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType(serviceType);
			template.addServices(templateSd);

			// if i want to receive 10 results at most
			// SearchConstraints sc = new SearchConstraints();
			// sc.setMaxResults(new Long(10));
			// DFAgentDescription[] results = DFService.search(this, template, sc);

			DFAgentDescription[] results = DFService.search(this, template);

			if (results.length > 0) {

				String[] res = new String[results.length];

				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					res[i] = provider.getLocalName();
				}

				return res;

			} else {
				System.out.println("Agent " + getLocalName() + " did not find any " + serviceType);
				return null;
			}

		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
	}

}