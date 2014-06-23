package data.aggregatedModel;

import java.util.ArrayList;

import data.model.StateDescriptor;
import data.model.Group; 
import data.model.StateVariable;

public class AggregatedModel{

	private StateDescriptor stateDescriptor; 
	private AggregatedState initialState;
	
	private ArrayList<AggregatedAction> actions; 
	private ArrayList<Group> groups; 	
	
	public Display display;
	
	public AggregatedModel(StateDescriptor stateDescriptor, 
							AggregatedState initialState,
							ArrayList<AggregatedAction> actions,
							ArrayList<Group> groups)	 {
		this.stateDescriptor = stateDescriptor;
		this.initialState = initialState;
		this.actions = actions;
		this.groups = groups;
		
		// initialising the model displayer.
		display = new Display(stateDescriptor,actions,groups);
	}
	
	public AggregatedModel(){
		
	}

	public AggregatedStateSpace deriveStateSpace(){
		
		AggregatedStateSpace stateSpace = new AggregatedStateSpace();
		
		ArrayList<AggregatedState> agenda = new ArrayList<AggregatedState>();
		agenda.add(initialState);
		
		ArrayList<AggregatedState> explored = new ArrayList<AggregatedState>();
		
		AggregatedState state;
		while (  ! (agenda.isEmpty())   ){
			state = agenda.get(0);
			
			// adding the state to the list of state in the state space.
			stateSpace.getStates().add(state);
			
			// adding the transitions enabled by the state to the list of transitions in the state space.
			ArrayList<Transition> transitions = getEnabledTransitionsAt(state) ;
			stateSpace.getTransitions().addAll(transitions);
			
			// remembering that the class has been explored.
			agenda.remove(0);
			explored.add(state);
		}

		return stateSpace;
	}

	
	
	public ArrayList<Transition> getEnabledTransitionsAt(AggregatedState state){
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		
		ArrayList<AggregatedAction> actions = getEnabledActionTypesAt(state);
		
		for (AggregatedAction action : actions){
			Transition transition = getTransition(state,action);
			transitions.add(transition);
		}
		
		return transitions;
	}
	
	
	
	public Transition getTransition(AggregatedState state, AggregatedAction action){
		
		Transition transition = new Transition();
		
		// setting the action.
		transition.setAction(action);
					
		// setting the start
		transition.setStart(state);
					
		// I dont know what I should do here. 
		transition.setRate(0);
					
		// setting the target
		AggregatedState target = nextState(state, action);
		transition.setTarget(target);
		
		return transition;
	}
	
	// given a state, returns the states reachable from this state
 	public ArrayList<AggregatedState> nextStates(AggregatedState state){
		
		ArrayList<AggregatedState> nextStates = new ArrayList<>();
		ArrayList<AggregatedAction> enabledActions = getEnabledActionTypesAt(state);
		
		for(AggregatedAction action : enabledActions){
			AggregatedState nextState = nextState(state, action);
			nextStates.add(nextState);
		}
		
		return nextStates;
		
	}
	
	// given a state and action type, returns the target state.
	public AggregatedState nextState (AggregatedState state, AggregatedAction action){
		AggregatedState nextState = new AggregatedState(stateDescriptor);
				
		Integer newPopulation;
		for(StateVariable variable : stateDescriptor){
			newPopulation = state.get(variable) - action.getJumpVectorMinus().get(variable) + action.getJumpVectorPlus().get(variable);
			nextState.put(variable, newPopulation);
		}
		
		return nextState;		
	}
	
	private ArrayList<AggregatedAction> getEnabledActionTypesAt(AggregatedState state){
		ArrayList<AggregatedAction> enabledActions = new ArrayList<AggregatedAction>();
		for (AggregatedAction action : actions){
				if(action.isEnabledAt(state)){
					enabledActions.add(action);
				}
		}
		return enabledActions;
	}

	
	
	
		
	
	/*
	 * 
	 * 
	 * 
	 * set and get methods
	 * 
	 * 
	 * 
	 * 
	 */
	
	public StateDescriptor getAggStateDescriptor() {
		return stateDescriptor;
	}

	public void setAggStateDescriptor(StateDescriptor aggStateDescriptor) {
		this.stateDescriptor = aggStateDescriptor;
	}

	public ArrayList<AggregatedAction> getAggActions() {
		return actions;
	}

	public void setAggActions(ArrayList<AggregatedAction> aggActions) {
		this.actions = aggActions;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public AggregatedState getAggInitialState() {
		return initialState;
	}

	public void setAggInitialState(AggregatedState aggInitialState) {

		initialState = aggInitialState;
	}
	
	
	
}
