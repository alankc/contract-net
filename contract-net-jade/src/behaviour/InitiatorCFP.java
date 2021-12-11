package behaviour;

import java.util.Enumeration;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

@SuppressWarnings("serial")
public class InitiatorCFP extends ContractNetInitiator {

	private long start;
	private long end;
	private long nResponders;
	
	public InitiatorCFP(Agent a, ACLMessage cfp, long nResponders, long start) {
		super(a, cfp);
		this.nResponders = nResponders;
		this.start = start;
	}

	protected void handlePropose(ACLMessage propose, Vector v) {
		//System.out.println("Agent " + propose.getSender().getName() + " proposed " + propose.getContent());
	}

	protected void handleRefuse(ACLMessage refuse) {
		//System.out.println("Agent " + refuse.getSender().getName() + " refused");
	}

	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			//System.out.println("Responder does not exist");
		} else {
			//System.out.println("Agent " + failure.getSender().getName() + " failed");
			//System.exit(0);

		}
		// Immediate failure --> we will not receive a response from this agent
		nResponders--;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		if (responses.size() < nResponders) {
			// Some responder didn't reply within the specified timeout
			//System.out.println("Timeout expired: missing " + (nResponders - responses.size()) + " responses");
		}
		// Evaluate proposals.
		int bestProposal = -1;
		AID bestProposer = null;
		ACLMessage accept = null;
		Enumeration e = responses.elements();
		while (e.hasMoreElements()) {
			ACLMessage msg = (ACLMessage) e.nextElement();
			if (msg.getPerformative() == ACLMessage.PROPOSE) {
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.addElement(reply);
				int proposal = Integer.parseInt(msg.getContent());
				if (proposal > bestProposal) {
					bestProposal = proposal;
					bestProposer = msg.getSender();
					accept = reply;
				}
			}
		}
		// Accept the proposal of the best proposer
		if (accept != null) {
			//System.out.println(myAgent.getLocalName() + ": Accepting proposal " + bestProposal + " from responder "
			//		+ bestProposer.getName());
			accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		}
		end = System.nanoTime();
		//System.out.println(myAgent.getLocalName()  + " - End: " + end / 1000000.0);
		System.out.println(myAgent.getLocalName() + ": TIME: " + (end - start) / 1000000.0 + " ms");
	}

	protected void handleInform(ACLMessage inform) {
		//System.out.println("Agent " + inform.getSender().getName() + " successfully performed the requested action");
		//System.exit(0);
	}

}
