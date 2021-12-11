package agent;

import java.util.Date;

import behaviour.InitiatorCFP;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.*;

@SuppressWarnings("serial")
public class InitiatorAgent extends Agent {

	private String[] services;
	private String[] participants;

	protected void setup() {

		Object[] args = getArguments();

		// arg 0 - array of services
		// arg 1 - array of participants
		if (args != null && args.length == 2) {

			services = (String[]) args[0];
			participants = (String[]) args[1];

			// Adding CFPs
			for (int i = 0; i < services.length; i++) {
				ACLMessage msg = generateMsg(services[i]);
				long start = System.nanoTime();
				addBehaviour(new InitiatorCFP(this, msg, participants.length, start));
				System.out.println(getLocalName()  + " - start: " + start / 1000000.0);
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

}