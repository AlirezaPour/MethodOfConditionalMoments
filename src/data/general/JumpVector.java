package data.general;

import java.util.HashMap;
import java.util.Iterator;

public class JumpVector extends NumericalVector{

	
	
	public JumpVector(StateDescriptor descriptor){
				
		// return a jump vectors where every where is zero. 
		
		Iterator<StateVariable> iter = descriptor.iterator();

		StateVariable var ;
		
		while(iter.hasNext()){
			
			var = iter.next();
		
			this.put(var, 0);
			
			
		}
				
	}
	
}
