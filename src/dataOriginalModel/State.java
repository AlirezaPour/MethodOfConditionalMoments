package dataOriginalModel;

import java.util.ArrayList;
import java.util.HashMap;

public class State {

	
	private StateVector stateVector; 
	
	public StateVector getStateVector(){
		return this.stateVector; 
	}
	
	public boolean IsEnabled(Action a){
		return true;
	}
	

	
	
}

