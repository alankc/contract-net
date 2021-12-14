/*
 * Base on JADE examples of protocols
 * https://jade.tilab.com/documentation/examples/protocols/
 * 
 * */

package behaviour;

import agent.ParticipantAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SSContractNetResponder;
import jade.proto.SSResponderDispatcher;

@SuppressWarnings("serial")
public class ParticipantCFP extends SSResponderDispatcher {

	private String myService;

	public ParticipantCFP(Agent a, MessageTemplate tpl, String myService) {
		super(a, tpl);
		this.myService = myService;
	}

	@Override
	protected Behaviour createResponder(ACLMessage cfp) {
		return new SSContractNetResponder(myAgent, cfp) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				// System.out.println("Agent "+getLocalName()+": CFP received from
				// "+cfp.getSender().getName()+". Action is "+cfp.getContent());

				//If agent is no working and know the service
				if (!((ParticipantAgent)myAgent).getWorking() && cfp.getContent().equals(myService)) {
					// We provide a proposal
					// System.out.println("Agent "+getLocalName()+": Proposing "+proposal);

					double proposal = evaluateAction();
					//System.out.println("Agent "+ myAgent.getLocalName()+": Proposing "+proposal);
					ACLMessage propose = cfp.createReply();
					propose.setPerformative(ACLMessage.PROPOSE);
					propose.setContent(String.valueOf(proposal));
					return propose;
				} else {
					// We refuse to provide a proposal
					// System.out.println("Agent "+getLocalName()+": Refuse");
					throw new RefuseException("evaluation-failed");
				}
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
					throws FailureException {
				// System.out.println("Agent "+getLocalName()+": Proposal accepted");

				//change if(performAction()) to if(true) to test with measurements in conclusion
				//Obs.: in initiator
				//The CFP's end must be measured in the receivement of inform when if(true)
				//The CFP's end must be measured in before handleAllResponses when if(performAction())
				if (true) {
					// System.out.println("Agent "+getLocalName()+": Action successfully
					// performed");

					//Setting "belief" working as in the Jason version
					((ParticipantAgent)myAgent).setWorking(true);

					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);

					((ParticipantAgent)myAgent).setWorking(false);

					return inform;
				} else {
					// System.out.println("Agent "+getLocalName()+": Action execution failed");
					throw new FailureException("unexpected-error");
				}
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				 //System.out.println("Agent "+ myAgent.getLocalName()+": Proposal rejected");
			}
		};
	}

	private double evaluateAction() {
		// Simulate an evaluation by generating a random number
		return (Math.random() * 100.0);
	}

	private boolean performAction() {
		// Simulate action execution by generating a random number
		return (Math.random() > 0.2);
	}

}
