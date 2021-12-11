package agent;

import behaviour.ParticipantCFP;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ParticipantAgent extends Agent {

	protected void setup() {

		Object[] args = getArguments();

		// arg 0 - my service
		if (args != null && args.length == 1) {

			String service = (String) args[0];
			MessageTemplate protocolTemplate = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
					MessageTemplate.MatchPerformative(ACLMessage.CFP));
			MessageTemplate contentTemplate = MessageTemplate.MatchContent(service);
			MessageTemplate generalTemplate = MessageTemplate.and(protocolTemplate, contentTemplate);
			
			addBehaviour(new ParticipantCFP(this, generalTemplate));
		}

	}

}
