package data.aggregatedModel;
import java.util.ArrayList;
import java.util.Iterator;

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
		
		ArrayList<AggregatedState> nextStates = new ArrayList<AggregatedState>();
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
		double rate = state.getRateOf(action, descriptor, groups);
		transition.setRate(rate);
					
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
 	
 	
 	// generates the state space. 
 	
 	public ArrayList<AggregatedState> generateStateSpace(){
 			
 		ArrayList<AggregatedState> explored = new ArrayList<AggregatedState>();
 		ArrayList<AggregatedState> agenda = new ArrayList<AggregatedState>();
 		
 		ArrayList<AggregatedState> toBeAddedToAgenda ;
 		ArrayList<AggregatedState> toBeAddedToAgendaUnified;
 		
 		// adding the initial state
 		agenda.add(model.getAggInitialState());
 		
 		AggregatedState state ; 
 		
 		while (   !(	agenda.isEmpty()	)	)	{
 			state = agenda.get(0);
 			agenda.remove(state);
 			
 			// this state has been explored.
 			explored.add(state);	
 			
 			toBeAddedToAgenda = nextStates(state);	// the instances of states return are not exactly the instances previously stored in explored. 
 			
 			toBeAddedToAgendaUnified = unifyNextStatesWithExplored(toBeAddedToAgenda,explored);
 			
 			// each of these states are added to the current states reachable state
 			state.getReachableStates().addAll(toBeAddedToAgendaUnified);
 			
 			for (AggregatedState reachableState : toBeAddedToAgendaUnified){
 				reachableState.getIncomingStates().add(state);
 			}
 			
 			// remove states that have been explored.
 			toBeAddedToAgendaUnified.remove(state);
 			toBeAddedToAgendaUnified.removeAll(explored);
 			toBeAddedToAgendaUnified.removeAll(agenda);
 			
 			// add the necessary states to the agenda for future exploration. 
 			agenda.addAll(toBeAddedToAgendaUnified);
 			
 		}
 		
 		return explored;
 		
  	}
 	
 	public ArrayList<AggregatedState> unifyNextStatesWithExplored(ArrayList<AggregatedState> newStates , ArrayList<AggregatedState> explored){
 		// replace the states in the newState with the one that is present in the explored. 
 		// here we modify the new states
 		// therefore avoiding the construction of a new ArrayList<state>
 		
 		ArrayList<AggregatedState> unifiedNewStates = new ArrayList<AggregatedState>();
 		
 		AggregatedState unifiedState ;
 		for (AggregatedState newState : newStates){
 			
 			int index = explored.indexOf(newState);
 			
 			
 			if (index >= 0){	// the explored array list contains the state
 				 	unifiedState = explored.get(index);
 				 	unifiedNewStates.add(unifiedState);
 			}else if (index == -1 ){
 					unifiedNewStates.add(newState);
 			}
 		
 		}
 		
 		
 		return unifiedNewStates;
 		
 		
 	}
 	
 	public ArrayList<Transition> getStartTargetTransitions (AggregatedState start, AggregatedState target){
 		ArrayList<Transition> relevantTransitions = new ArrayList<Transition>();
 		
 		ArrayList<Transition> allTransitions = getTransitions(start);
 		
 		for(Transition transition : allTransitions){
 			
 			if (transition.getStart().equals(start)   && transition.getTarget().equals(target)){
 				relevantTransitions.add(transition);
 			}
 			
 		}
 		
 		return relevantTransitions;
 	}
 	
 	
 	public double totalTransitionRate(AggregatedState start, AggregatedState target){
		double rate = 0 ;
		
		ArrayList<Transition> relevantTransitions = getStartTargetTransitions(start, target);
		
		for(Transition transition : relevantTransitions){
			rate = rate + transition.getRate();
		}
		
		return rate; 
	}
 	
}
