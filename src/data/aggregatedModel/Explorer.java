package data.aggregatedModel;
import java.util.ArrayList;
import data.model.Group;
import data.model.StateDescriptor;
import data.model.StateVariable;


public class Explorer {

	private AggregatedModel model;
	private ArrayList<AggregatedAction> actions;
	private ArrayList<Group> groups;
	private StateDescriptor descriptor ;
	
	public Explorer(AggregatedModel model){
		this.model = model;
		this.actions = model.getAggActions();
		this.groups = model.getGroups();
		this.descriptor = model.getAggStateDescriptor();
	}
	
	// for a given state, returns the action types that are enabled in that state. 
	public ArrayList<AggregatedAction> actionsEnabledAt(AggregatedState state){
		
		ArrayList<AggregatedAction> actionsEnabled = new ArrayList<AggregatedAction>();
		
		for (AggregatedAction action : actions){
			if ( state.doesEnable(action) ) actionsEnabled.add(action);
		}
		
		return actionsEnabled;
		
	}

	// given a state and action type, returns the target state.
	public AggregatedState nextState (AggregatedState state, AggregatedAction action){
		
		AggregatedState nextState = new AggregatedState(descriptor);
				
		Integer newPopulation;
		for(StateVariable variable : descriptor){
			newPopulation = state.get(variable) - action.getJumpVectorMinus().get(variable) + action.getJumpVectorPlus().get(variable);
			nextState.put(variable, newPopulation);
		}
		
		return nextState;		
	}

	// given a state, returns the states reachable from this state
 	public ArrayList<AggregatedState> nextStates(AggregatedState state){
		
		ArrayList<AggregatedState> nextStates = new ArrayList<>();
		ArrayList<AggregatedAction> enabledActions = actionsEnabledAt(state);
		
		for(AggregatedAction action : enabledActions){
			AggregatedState nextState = nextState(state, action);
			nextStates.add(nextState);
		}
		
		return nextStates;
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

 	public ArrayList<Transition> getTransitions (AggregatedState state){
 		
 		ArrayList<Transition> transitions = new ArrayList<Transition>();
 		
 		ArrayList<AggregatedAction> actions = actionsEnabledAt(state);
 		
 		Transition transition;
 		for (AggregatedAction action : actions){
 			transition = getTransition(state, action);
 			transitions.add(transition);
 		}
 		
 		return transitions;
 	}

 	public ArrayList<AggregatedState> generateStates(){
 		
 		AggregatedState state;
 		
 		ArrayList<AggregatedState> explored = new ArrayList<AggregatedState>();
 		ArrayList<AggregatedState> agenda = new ArrayList<AggregatedState>();
 		
 		ArrayList<AggregatedState> toBeAddedToAgenda ;
 		
 		// adding the initial state
 		agenda.add(model.getAggInitialState());
 		
 		while (   !(	agenda.isEmpty()	)	)	{
 			state = agenda.get(0);
 			agenda.remove(state);
 			
 			// this state has been explored.
 			explored.add(state);
 			
 			toBeAddedToAgenda = nextStates(state);
 			
 			// remove states that have been explored.
 			toBeAddedToAgenda.remove(state);
 			toBeAddedToAgenda.removeAll(explored);
 			toBeAddedToAgenda.removeAll(agenda);
 			
 			// add the necessary states to the agenda for future exploration. 
 			agenda.addAll(toBeAddedToAgenda);
 			
 			
 		}
 		
 		return explored;
 	}
 	
}
