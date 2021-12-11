package agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class TerminatorAgent extends Agent {

	public void setup() {
		
		Object[] args = getArguments();
		
		if (args != null && args.length == 1) {
			
			long limit = (long)args[0];
			
			addBehaviour(new OneShotBehaviour(this) {
				@Override
				public void action() {
					
					long counter = 0;
					
					while (true) {
						ACLMessage msg = null;
						msg = blockingReceive();
						counter++;
						if (counter == limit)
						{
							System.out.println(getLocalName() + ": I will be back");
							System.exit(0);
						}
							
					}
				}
			});
		}
	}

}