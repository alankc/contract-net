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
 *  Starts JADE main container with several agents
 * 
 *  @author jomi
 */
public class StartJade {

    ContainerController cc;
    
    public static void main(String[] args) throws Exception {
        StartJade s = new StartJade();
        s.startContainer();
        s.createAgents();        
    }

    void startContainer() {
        //Runtime rt= Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "true");
        
        cc = Runtime.instance().createMainContainer(p);
    }
    
    void createAgents() throws Exception {
    	int max = 100;
    	Object o[] = new Object[max];
    	
        /*for (int i=0; i<max; i++) {
        	AgentController ar = cc.createNewAgent("Responder" + i, "agents.ParallelContractNetResponderAgent", new Object[] { 1 });
            ar.start();
            String s = "Responder" + i;
            o[i] = s;
        }
        
        
    	AgentController ai = cc.createNewAgent("Initiator0", "agents.ContractNetInitiatorAgent", o);
        ai.start();*/
        //ai = cc.createNewAgent("Initiator1", "agents.ContractNetInitiatorAgent", o);
        //ai.start();
    }
}
