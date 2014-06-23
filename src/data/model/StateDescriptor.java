package data.model;

import java.util.ArrayList;
import java.util.Iterator;


// every model has a state descriptor. 
public class StateDescriptor extends ArrayList<StateVariable>{

	
	
	
	// for a given group and local derivative, what is the associated state variable constructed? 
	public StateVariable getCorrespondingStateVariable (Group fgroup, LocalDerivative fderivative){
		
		StateVariable variable ;
		Group group;
		LocalDerivative derivative;
		
		Iterator<StateVariable> iter = this.iterator();
		
		while(iter.hasNext()){
			
			variable = iter.next();
			group = variable.getGroup();
			derivative = variable.getLocalDerivative();
			
			if (group.equals(fgroup) && derivative.equals(fderivative)){
				return variable;
			}
			
		}
		
		return null;
		
	}
	
}
