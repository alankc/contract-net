import FIPA.stringsHelper;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.RequestManagementBehaviour;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * Starts JADE main container with several agents
 * 
 * @author jomi
 */
public class StartJade {

	ContainerController cc;

	private int initiators;
	private int participants;
	private int services;

	public static void main(String[] args) throws Exception {

		int ini = Integer.parseInt(args[0]);
		int par = Integer.parseInt(args[1]);
		int ser = Integer.parseInt(args[2]);
		
		System.out.println("Initiator: " + ini);
		System.out.println("Participant: " + par);
		System.out.println("Services: " + ser);

		StartJade s = new StartJade(ini, par, ser);
		s.startContainer();
		s.createAgents();
	}

	public StartJade(int initiators, int participants, int services) {
		this.initiators = initiators;
		this.participants = participants;
		this.services = services;
	}

	void startContainer() {
		// Runtime rt= Runtime.instance();
		ProfileImpl p = new ProfileImpl();
		p.setParameter(Profile.MAIN_HOST, "localhost");
		p.setParameter(Profile.GUI, "true");

		cc = Runtime.instance().createMainContainer(p);
	}

	void createAgents() throws Exception {

		String oServices[] = new String[services];
		
		//used in previous solution without yellow pages
		//String oParticipants[] = new String[participants];

		for (int i = 0; i < services; i++) {
			String s = "a(" + i + ")";
			oServices[i] = s;
		}

		AgentController aT = cc.createNewAgent("Terminator", "agent.TerminatorAgent",
				new Object[] { (long) (services * initiators) });
		aT.start();

		for (int i = 0; i < participants; i++) {
			String name = "P" + i;
			//oParticipants[i] = name;

			AgentController aP = cc.createNewAgent(name, "agent.ParticipantAgent",
					new Object[] { oServices[i % services] });
			aP.start();
		}
		
		Thread.sleep(2000);

		for (int i = 0; i < initiators; i++) {
			//AgentController aI = cc.createNewAgent("I" + i, "agent.InitiatorAgent",
			//		new Object[] { oServices, oParticipants });
			AgentController aI = cc.createNewAgent("I" + i, "agent.InitiatorAgent",
					new Object[] { oServices, participants });
			aI.start();
		}
	}
}
