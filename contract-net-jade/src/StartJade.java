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

	public static void main(String[] args) throws Exception {
		StartJade s = new StartJade();
		s.startContainer();
		s.createAgents();
	}

	void startContainer() {
		// Runtime rt= Runtime.instance();
		ProfileImpl p = new ProfileImpl();
		p.setParameter(Profile.MAIN_HOST, "localhost");
		p.setParameter(Profile.GUI, "true");

		cc = Runtime.instance().createMainContainer(p);
	}

	void createAgents() throws Exception {

		int services = 1;
		int participants = 100;
		int initiators = 10;

		String oServices[] = new String[services];
		String oParticipants[] = new String[participants];

		for (int i = 0; i < services; i++) {
			String s = "a(" + i + ")";
			oServices[i] = s;
		}

		for (int i = 0; i < participants; i++) {
			String name = "P" + i;
			oParticipants[i] = name;

			AgentController aP = cc.createNewAgent(name, "agent.ParticipantAgent", new Object[] { oServices[0] });
			aP.start();
		}

		for (int i = 0; i < initiators; i++) {
			AgentController aI = cc.createNewAgent("I" + i, "agent.InitiatorAgent", new Object[] { oServices, oParticipants });
			aI.start();
		}
	}
}
