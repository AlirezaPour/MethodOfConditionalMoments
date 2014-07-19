package data.aggregatedModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.general.Action;
import data.general.Transition;
import data.general.Group;
import data.general.LocalDerivative;
import data.general.NumericalVector;
import data.general.StateDescriptor;
import data.general.StateVariable;
import data.originalModel.OriginalAction;

public class Display {
	
	private int maxStateNumericalRepresentation = 10;
	private int maxActionNameLength = 10;
	private int maxRateValueLength = 10;
	
	private  int spaceAfterGroupDivider = 3;
	private  int spaceBeforeGroupDivider = 3;
	
	private  int spaceAfterLocDerDivider = 1 ; 
	private  int spaceBeforeLocDerDivider = 1;
	
	private  String groupDivider ="||" ;
	private  String localDerDivider = "|";
	
	private  String net = "net";
	private  String plus = "plus";
	private  String minus = "minus";
	private int spaceForImpactType = minus.length() ; // assuming that minus has the largest length compared with plus and net. 
	
	private int spaceBeforeStateIdentifier = 3;
	private int stateIdentifierMaxLength = 10;
	
	private String actionLabel = "Action : ";
	private int spaceAfterActionName = 2 ;
	
	private AggregatedModel model;
	private StateDescriptor descriptor ; 
	private ArrayList<AggregatedAction> actions;
	private ArrayList<Group> groups;
	
	public Display(AggregatedModel model){
		this.model = model;
		this.descriptor = model.getAggStateDescriptor();
		this.actions = model.getAggActions();
		this.groups = model.getGroups();
	}
	
	// this method updated the instance variables of the display
	public void updateDisplayPrimitives(){
		this.descriptor = model.getAggStateDescriptor();
		this.actions = model.getAggActions();
		this.groups = model.getGroups();
	}
	
	public String showStateSpace(AggregatedStateSpace sp){
		String output = "";
		return output; 
	}
	
	
	public String showLocalDerivatives(ArrayList<data.general.LocalDerivative> derivatives){
		String output = "";
		
		for (LocalDerivative derivative : derivatives){
			output += showLocalDerivative(derivative);
			output += "\n";
		}
		
		return output ; 
	}
	
	public String showLocalDerivative (LocalDerivative localDerivative){
		String output = "";

		output += "Local Derivative : " + localDerivative.getName() + " \n";
		
		Iterator<Action> iter = localDerivative.getActionRates().keySet().iterator();

		AggregatedAction action ;
		while(iter.hasNext()){
			
			action = (AggregatedAction) iter.next();

			output += "\t" + action.getName() ;
			output += "\t\t" + localDerivative.getActionRates().get(action) ;
			output += "\t\t" + localDerivative.getParameterNames().get(action);
			
			output += "\n";
			
		}
		
		return output; 
	}
	
	
	
	public String showTransitions(ArrayList<Transition> transitions){
		String output = "";
		
		Iterator<Transition> iter = transitions.iterator();
		
		output += showTransitionHeader() ; 
		
		//output += "\n";
		
		// length of the header
		int length = 5 * localDerDivider.length() + 2 * maxStateNumericalRepresentation  +  maxActionNameLength + maxRateValueLength ;
		
		output += "\n";
		
		Transition transition;
		while(iter.hasNext()){
			transition = iter.next();
			output += underline(length);
			output += "\n";
			output += showTransitionContent(transition);
			output += "\n";
		}
		
		
		
		return output ; 
	
	}
	
	public String showTransition(Transition transition){
		String output = showTransitionHeader();
		
		output += "\n";
		
		// length of the header
		int length = 5 * localDerDivider.length() + 2 * maxStateNumericalRepresentation  +  maxActionNameLength + maxRateValueLength ;
		output += underline(length);
		
		output += "\n";
		
		output += showTransitionContent(transition);

		return output;
	}
	
	public String showTransitionContent(Transition transition){
		String output = "";
		
		output += localDerDivider ; 

		AggregatedState start = ( AggregatedState) transition.getStart(); 
		String startNumericalVector = showNumericalRepresentation(start);
		String format = "%-" + maxStateNumericalRepresentation + "s";
		output += String.format(format, startNumericalVector);
		
		output += localDerDivider ;
		
		AggregatedState target = ( AggregatedState) transition.getTarget(); 
		String targetNumericalVector = showNumericalRepresentation(target);
		output += String.format(format, targetNumericalVector);
		
		output += localDerDivider ;
		
		format = "%-" + maxActionNameLength + "s";
		AggregatedAction action = (AggregatedAction) transition.getAction(); 
		output += String.format(format, action.getName());
		
		output += localDerDivider ;
		
		format = "%-" + maxRateValueLength + "s" ;
		double rate = transition.getRate();
		output += String.format(format, rate);
		
		output += localDerDivider ;
		
		return output;
	}
	
	public String showTransitionHeader(){
		String output = "";
		output += "Transition";
		
		output += "\n";
		
		int length = 5 * localDerDivider.length() + 2 * maxStateNumericalRepresentation  +  maxActionNameLength + maxRateValueLength ;
		output += underline(length);
		
		output += "\n";
		
		output += localDerDivider ; 
		String format = "%-" + maxStateNumericalRepresentation + "s";
		output += String.format(format, "Start");
		
		output += localDerDivider ;
		output += String.format(format, "Target");

		output += localDerDivider ;
		
		format = "%-" + maxActionNameLength + "s";
		output += String.format(format, "Action");
		
		output += localDerDivider ;
		
		format = "%-" + maxRateValueLength + "s";
		output += String.format(format, "Rate");
		
		output += localDerDivider ;
		
		return output;
	}
	
	public String showNumericalRepresentation(AggregatedState state){
		String output = "";
		
		
		Iterator<Group> iter = groups.iterator();
		
		// first group
		Group group = iter.next();
		output += "<";
		output += showNumericalRepresentation(state, group);
		
		while (iter.hasNext()){
			
			group = iter.next();
		//	output += groupDivider;
			output +=  showNumericalRepresentation(state, group);
			
		}
		
		output += ">";
		
		return output;
	}
	
	public String showNumericalRepresentation(AggregatedState state, Group group){
		String output = "";
		
		ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
		Iterator<LocalDerivative> iter = derivatives.iterator();
		
		// the first derivative
		LocalDerivative derivative = iter.next();		
		
		output += showNumericalRepresentation(state,group,derivative);
		
		while(iter.hasNext()){
			derivative = iter.next();
			output += ",";
			output += showNumericalRepresentation(state, group, derivative);
		}
		
		
		return output;
	}
	
	public String showNumericalRepresentation(AggregatedState state, Group group, LocalDerivative derivative){
		String output = "";
		
		StateVariable variable = descriptor.getCorrespondingStateVariable(group, derivative);
		int value = state.get(variable).intValue();
		
		output += value;
		
		return output;
		
	}
	
	// displaying the groups and the line under the group titles.
	public String showGroupLabel(Group group){
		String output = "";
		
		int numLocDer = group.getGroupLocalDerivatives().size();
		int length = group.maxLocalDerivativeLength()*numLocDer + (numLocDer - 1 ) * ( spaceAfterLocDerDivider + localDerDivider.length() + spaceBeforeLocDerDivider );
		
		
		
		String format = "%-"+ length  + "s" ;
		output += String.format(format, group);
		
		
		
		return output;
	}
	public String showManyGroupsLables(ArrayList<Group> groups){
		String output = "";
		
		for (Group group : groups){
			
			output += groupDivider;
			output += spaces(spaceAfterGroupDivider);
			
			output += showGroupLabel(group);
			
			output += spaces(spaceBeforeGroupDivider);
		}
		
		output += groupDivider ;
				
		return output;
	}
	public String showUnderlineOneGroup(Group group){
		
		int numLocDer = group.getGroupLocalDerivatives().size();
		int length = group.maxLocalDerivativeLength()*numLocDer + (numLocDer - 1 ) * ( spaceAfterLocDerDivider + localDerDivider.length() + spaceBeforeLocDerDivider ) ;
		String line = underline(length);
		
		//System.out.printf(groupDivider);
		return line;
		
	}
	public String showUnderlineManyGroups(ArrayList<Group> groups){
		
		String line = "";
		for(Group group : groups){
			line += groupDivider;
			line += underline(spaceAfterGroupDivider);
			line += showUnderlineOneGroup(group);
			line += underline(spaceBeforeGroupDivider);
		}
		line += groupDivider;
		return line;
	}
	
	// displaying the local derivatives.
	public String showLocalDerivativesOneGroup (Group group){
		
		String output = ""; 
		
		String format = "";
		int length = group.maxLocalDerivativeLength();
		LocalDerivative localDerivative;
		Iterator<LocalDerivative> iter = group.getGroupLocalDerivatives().iterator();
		
		// printing the first local derivative;
		localDerivative = iter.next();
		format = "%-" + length + "s";
		output += String.format(format, localDerivative);
				
		// printing the later ones.
		while(iter.hasNext()){
			
			output += spaces(spaceBeforeLocDerDivider);
			
			localDerivative = iter.next();
			
			output += localDerDivider;
			
			output += spaces(spaceAfterLocDerDivider);
			
			output += String.format(format, localDerivative);
			
		}
		
		return output;
		
	}
	public String showLocalDerivativesManyGroups (ArrayList<Group> groups){
		
		String output = "";
		
		for(Group group : groups){
			output += groupDivider;
			output += spaces(spaceAfterGroupDivider);
			output += showLocalDerivativesOneGroup(group);
			output += spaces(spaceBeforeGroupDivider);			
		}
		
		output += groupDivider;
		return output;
		
	}	
		
	// displaying a numerical vector
	public String showNumericalVectorOneGroup(AggregatedModel model, NumericalVector vector, Group group){
	
		int length = group.maxLocalDerivativeLength();
		
		String output = "";
					
		// the local derivatives
		ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
		LocalDerivative derivative;
		Iterator<LocalDerivative> iter = derivatives.iterator();
			
		// the first state variable
		derivative = iter.next();
		StateDescriptor descriptor  = model.getAggStateDescriptor();
		StateVariable variable  = descriptor.getCorrespondingStateVariable(group, derivative);
		int value = vector.get(variable);
		
		String format = "%-" +  length + "s";
		
		output += String.format(format, value);
			
		// the rest of the derivatives in this group
		while(iter.hasNext()){
			derivative = iter.next();
			
			output += spaces(spaceBeforeLocDerDivider);
			output += localDerDivider;
			output += spaces(spaceAfterLocDerDivider);
				
			variable  = descriptor.getCorrespondingStateVariable(group, derivative);
			value = vector.get(variable);
			output += String.format(format, value); 
			
		}
			
			// end of this group. Get ready for the next one.
		//System.out.printf();		
			
		return output;
		
	}
	public String showNumericalVectorManyGroups (AggregatedModel model, NumericalVector vector , ArrayList<Group> groups){
		String output = "";
		
		for (Group group : groups){
			output += groupDivider;
			output += spaces(spaceAfterGroupDivider);
			output += showNumericalVectorOneGroup(model,vector, group);
			output += spaces(spaceBeforeGroupDivider);
		} 
		output += groupDivider;
		return output;
	}
	
	// displaying an action
	public String showAction (AggregatedAction action){
		String output = "";
		
		output += spaces(spaceForImpactType + groupDivider.length());
		output += underline(17);
		
		output += "\n";
		
		// first line
		output += spaces(spaceForImpactType);
		output += groupDivider;
		
		String actionLabel = String.format(" Action: %s", action.getName());
		output += actionLabel; 
		output += " " + groupDivider ;

		// aiming to draw the line under the first line.
		output += "\n";
		// the line under the first line
		output += spaces(spaceForImpactType);
		output += showUnderlineManyGroups(groups);
		
		output += "\n";
		 
		// group titles
		output += spaces(spaceForImpactType);	
		output += showManyGroupsLables(groups);
		
		output += "\n";
		
		// the line under the group titles
		output += spaces(spaceForImpactType);
		output += showUnderlineManyGroups(groups);
		
		output += "\n";
		output += spaces(spaceForImpactType);
		output += showLocalDerivativesManyGroups(groups);
		
		output += "\n";
		
		// the line under the local derivatives
		output += underline(spaceForImpactType);
		output += showUnderlineManyGroups(groups);
		
		output += "\n";

		// the net impact vector
		String format = "%" + minus.length() + "s";
		output += String.format(format, net);
		output += showNumericalVectorManyGroups(model, action.getJumpVector(), groups);
		
		output += "\n";
		
		// the line under the net jump vector
		output += underline(spaceForImpactType);
		output += showUnderlineManyGroups(groups);
		
		output += "\n";
		
		// the minus impact vector
		
		format = "%" + minus.length() + "s";
		output += String.format(format, minus);
		output += showNumericalVectorManyGroups(model, action.getJumpVectorMinus(), groups);
		
		output += "\n";
		
		// the line under the minus jump vector
		output += underline(spaceForImpactType);
		output += showUnderlineManyGroups(groups);

		
		output += "\n";
		
		// the plus impact vector
		format = "%" + minus.length() + "s";
		output += String.format(format, plus);
		output += showNumericalVectorManyGroups(model, action.getJumpVectorPlus(), groups);
		
		output += "\n";

		// the line under the plus jump vector
		output += underline(spaceForImpactType);
		output += showUnderlineManyGroups(groups);
		
		// the symbolic apparent rate of the action 
		output += "\n\n";
		output += "The symbolic apparent rate : ";
		output += action.getSymbolicRateOf(descriptor, groups);
		
		return output;
	}
	public String showActions (ArrayList<AggregatedAction> actions){
		String output = "";
		
		for (AggregatedAction action : actions){
			output += showAction(action);
			output += "\n\n\n";
		}
		
		return output;
	}
	
	// displaying a state and array list of states 
	public String showState (AggregatedState state){
		String output = "";
		output += showManyGroupsLables(groups);
		output += "\n";
		output += showUnderlineManyGroups(groups);
		output += "\n";
		output += showLocalDerivativesManyGroups(groups);
		output += "\n";
		output += showUnderlineManyGroups(groups);
		output += underline(spaceBeforeStateIdentifier + stateIdentifierMaxLength ); 
		output += "\n";
		output += showStatePopulationsOnly(state);
		
		return output;
	}
	public String showStatePopulationsOnly(AggregatedState state){
		String output = "";
		output += showNumericalVectorManyGroups(model, state, groups);
		output += spaces(spaceBeforeStateIdentifier);
		//output += state.getStateIdentifier();
		output += showNumericalRepresentation(state);
		return output ;
	}
	public String showStates (ArrayList<AggregatedState> states){
		String output = "";	
		 
		Iterator<AggregatedState> iter = states.iterator();
		
		AggregatedState state = iter.next();
		
		output += showState(state);
		
		while(iter.hasNext()){
			
			state = iter.next();
			
			output += "\n";
			output += showUnderlineManyGroups(groups);
			output += underline(spaceBeforeStateIdentifier + stateIdentifierMaxLength ); 
			output += "\n";
			output += showStatePopulationsOnly(state);
		
		}
		
		return output; 
	}

	// displaying the state descriptor
	// it inherently uses the class variable groups. therefore, no input is required to this method.
	public String showStateDescriptor (){
		String output = "The state descriptor is: \n\n";
		output += showUnderlineManyGroups(groups);
		output += "\n";
		output += showManyGroupsLables(groups);
		output += "\n";
		output += showUnderlineManyGroups(groups);
		output += "\n";
		output += showLocalDerivativesManyGroups(groups);
		output += "\n";
		output += showUnderlineManyGroups(groups);
		return output;
	}

	public String showModel(){
		String output = "Aggregated Model: \n\n";
		
		output += "Local Derivatives\n";
		output += showLocalDerivatives(model.getLocalDerivatives());
		output += "\n\n";
		
		output += showStateDescriptor();
		output += "\n\n";
		
		output += "The initial state is: \n\n";
		output += showState(model.getAggInitialState());
		output += "\n\n";
		
		
		output += "The actions are: \n\n";
		output += showActions(model.getAggActions());
		output += "\n\n";		
		
		return output;
	}
	
	public String spaces(int number){
		
		String output ="" ; 
		for(int i=0 ; i < number;i++ ){
			output = output + " ";
		}
		return output;
		
	}
	public String underline(int length){
		String output = "";
		for (int i=0 ; i<length ; i++){
			output= output + "-";
		}
		return output;
	}
	
	
	
	
	public String storeStateSpace(AggregatedStateSpace sp){
	
		//Explorer explorer = new Explorer(model);

		ArrayList<AggregatedState> states = sp.getExplored();
		
		String output = "States:\n\n\n";
				
		// store the list of states.
		
		output += storeStates(states);
		
		output += "\n\n";
		
		output += "Outward Transitions:\n\n";
		
		output += storeOutwardTransitionBank(sp);
		
		output += "\n\n";
		
		output += "Inward Transitions:\n\n";
		
		output += storeInwardTransitions(sp);
		
		return output ; 
	 
	}

	public String storeOutwardTransitionBank (AggregatedStateSpace sp){
		String output = "" ;
		
		ArrayList<AggregatedState> states = sp.getExplored();
		ArrayList<Transition> transitions;
		
		for (AggregatedState state : states){
			
			transitions = sp.getOutgoingTransitionBank().get(state);
			output += storeTransitionsForState(transitions);
			
			
		}
		
		return output; 
	}
	
	
	public String storeInwardTransitions (AggregatedStateSpace sp){
		String output = "" ;
		
		ArrayList<AggregatedState> states = sp.getExplored();
		
		for (AggregatedState state : states){
			
			output += storeInwardTransitions(sp,state);
		
		}
		
		return output; 
	}
	
	
	public String storeInwardTransitions (AggregatedStateSpace sp, AggregatedState state){
		String output = "";
		
		ArrayList<Transition> transitions = state.getInwardTransitions();
		
		for (Transition tr: transitions){
			output += storeInwardTransitions (sp,state,tr);
			output += "\n";
		}
		
		return output;
	}
	
	public String storeInwardTransitions (AggregatedStateSpace sp, AggregatedState state, Transition tr){
		
		String output = "";
		
		AggregatedState start, target;
		double rate;
		AggregatedAction action;
		
		
		start = (AggregatedState) tr.getStart();
		target = (AggregatedState) tr.getTarget();
		rate = tr.getRate();
		action= (AggregatedAction) tr.getAction();
		
		// target should be the same as state
		if (!(state.equals(target))){
			return "Error";
		}
			
		output += start.getStateId() ;
		output += "\t" ;
		
		output += target.getStateId() ;
		output += "\t" ;
			
		output += Double.toString(rate);
		output += "\t" ;
			
		output += action.getName();
	
			
		
		
		return output;
	}
	
	public String storeTransitionsForState(ArrayList<Transition> transitions){
		String output = "";
		
		AggregatedState start, target;
		double rate;
		AggregatedAction action;
		
		for (Transition tr : transitions){
			
			start = (AggregatedState) tr.getStart();
			target = (AggregatedState) tr.getTarget();
			rate = tr.getRate();
			action= (AggregatedAction) tr.getAction();
			
			output += start.getStateId() ;
			output += "\t" ;
			
			output += target.getStateId() ;
			output += "\t" ;
			
			output += Double.toString(rate);
			output += "\t" ;
			
			output += action.getName();
			output += "\n" ;	
			
		}
		
		return output;
	}
	// 
	public String storeStates(ArrayList<AggregatedState> states){
		String output = "";
		
		for (AggregatedState state : states){
			output += storeOneState(state);
			output += "\n";
		}
		
		return output ; 
	}
	
	public String storeOneState(AggregatedState state){
 		String output = "";
 		
 		output += state.getStateId(); 
 		output += "\t\t";
 		output += showNumericalRepresentation(state);
 	
 		return output; 
 	}
	
	public String storeTransitions(ArrayList<Transition> transitions){
		String output = "";
		for (Transition tr : transitions){
			output += storeOneTransition(tr);
			output += "\n";
		}
		return output;
	}
	
	public String storeOneTransition (Transition transition){
		String output = "";
		
		AggregatedState state = (AggregatedState) transition.getStart(); 
		int id = state.getStateId();
		
		output += Integer.toString(id);
		output += "\t\t";
		
		state = (AggregatedState) transition.getTarget();
		id = state.getStateId();
		output +=Integer.toBinaryString(id);
		output += "\t\t";
		
		double rate = transition.getRate();
		output += Double.toString(rate);
		
		return output; 
	}
 	
	
}
