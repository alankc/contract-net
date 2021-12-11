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
		// TODO Auto-generated method stub
		return new SSContractNetResponder(myAgent, cfp) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				// System.out.println("Agent "+getLocalName()+": CFP received from
				// "+cfp.getSender().getName()+". Action is "+cfp.getContent());
				
				//If agent is no working and know the service
				if (!((ParticipantAgent)myAgent).getWorking() && cfp.getContent().equals(myService)) {
					// We provide a proposal
					// System.out.println("Agent "+getLocalName()+": Proposing "+proposal);
									
					int proposal = evaluateAction();
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
				if (performAction()) {
					// System.out.println("Agent "+getLocalName()+": Action successfully
					// performed");
					
					
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
				// System.out.println("Agent "+getLocalName()+": Proposal rejected");
			}
		};
	}
	
	private int evaluateAction() {
		// Simulate an evaluation by generating a random number
		return (int) (Math.random() * 10);
	}

	private boolean performAction() {
		// Simulate action execution by generating a random number
		return (Math.random() > 0.2);
	}

}
