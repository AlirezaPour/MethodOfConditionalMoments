package data.originalModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import data.originalModel.Display;
import data.aggregatedModel.AggregatedAction;
import data.general.Group;
import data.general.JumpVector;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;

// this class captures the notion of a 
// LSRB model where the groups are 
// partitions based on their size into
// large and small groups.

public class OriginalModel{
	
	private ArrayList<LocalDerivative> localDerivatives;
	
	private StateDescriptor stateDescriptor;
	private StateDescriptor stateDescriptorSmallGroups;
	private StateDescriptor stateDescriptorLargeGroups; 
	
	private OriginalState initialState;
	
	private ArrayList<OriginalAction> actions;
	private ArrayList<OriginalAction> actionsSmall;
	private ArrayList<OriginalAction> actionsSmallAndLarge;
	private ArrayList<OriginalAction> actionsLarge;
	
	private ArrayList<Group> largeGroups; 
	private ArrayList<Group> smallGroups;
	
	private HashMap<String, Integer> constants;
	
	private Display display;
	
	
	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public OriginalModel (	ArrayList<LocalDerivative> localDerivatives, 
							StateDescriptor stateDescriptor, 
						  	OriginalState initialState,
						  	ArrayList<OriginalAction> actions, 
						  	ArrayList<Group> largeGroups,
						  	ArrayList<Group> smallGroups,
						  	HashMap<String, Integer> constants)   {
		this.localDerivatives = localDerivatives;
		this.initialState = initialState;
		this.actions = actions;
		this.largeGroups = largeGroups;
		this.smallGroups = smallGroups;
		this.constants = constants;
		
		this.stateDescriptorSmallGroups = generateStateDescriptorSmallGroups(stateDescriptor);
		this.stateDescriptorLargeGroups = generateStateDescriptorLargeGroups(stateDescriptor);
		
		// creating a state descriptor with a defined order.
		this.stateDescriptor =  new StateDescriptor();
		this.stateDescriptor.addAll(stateDescriptorSmallGroups);
		this.stateDescriptor.addAll(stateDescriptorLargeGroups);
		
		setActionsInCategories();
		
	}

	// returns the list of the state variables which 
	// enable the action type given.
	public ArrayList<StateVariable> getEnablingStateVariables(OriginalAction action){
		
		ArrayList<StateVariable> enablingVariables = new ArrayList<StateVariable>();
	
		for (StateVariable var : stateDescriptor){
			
			if ( action.getJumpVectorMinus().get(var) == 1 ){
				enablingVariables.add(var);
			}
			
		}
		
		return enablingVariables;
		
	}
	
	
	// returns the list of local derivatives which are involved in enabling the action.
	public ArrayList<LocalDerivative> getEnablingLocalDerivative(OriginalAction action){
		
		ArrayList<LocalDerivative> enablingDerivatives = new ArrayList<LocalDerivative>();

		ArrayList<StateVariable> enablingVariables = getEnablingStateVariables(action);

		
		LocalDerivative derivative;
		
		for (StateVariable var : enablingVariables){
			
			derivative = var.getLocalDerivative();
			
			if (!( enablingDerivatives.contains(derivative))){
				enablingDerivatives.add(derivative);
			}
			
		}
		
		return enablingDerivatives;
		
	}
	
	

		
	public StateDescriptor getStateDescriptor() {
		return stateDescriptor;
	}

	public void setStateDescriptor(StateDescriptor stateDescriptor) {
		this.stateDescriptor = stateDescriptor;
	}

	public OriginalState getInitialState() {
		return initialState;
	}

	public void setInitialState(OriginalState initialState) {
		this.initialState = initialState;
	}

	public ArrayList<OriginalAction> getActions() {
		return actions;
	}

	public void setActions(ArrayList<OriginalAction> actions) {
		this.actions = actions;
	}

	public ArrayList<Group> getLargeGroups() {
		return largeGroups;
	}

	public void setLargeGroups(ArrayList<Group> largeGroups) {
		this.largeGroups = largeGroups;
	}

	public ArrayList<Group> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(ArrayList<Group> smallGroups) {
		this.smallGroups = smallGroups;
	}
	
	private StateDescriptor generateStateDescriptorSmallGroups(StateDescriptor stateDescriptor){
		StateDescriptor sDSG = new StateDescriptor();
		
		Group group; 
		StateDescriptor temp;
		
		Iterator<Group> iter = smallGroups.iterator();
		while(iter.hasNext()){
			group = iter.next();
			temp = generateStateDescriptorSmallGroups(stateDescriptor,group);
			sDSG.addAll(temp);
		}
		
		return sDSG; 
	}
	
	private StateDescriptor generateStateDescriptorSmallGroups(StateDescriptor stateDescriptor,Group group){
		StateDescriptor sd = new StateDescriptor();
		
		ArrayList<LocalDerivative> lds = group.getGroupLocalDerivatives(); 
		
		StateVariable var; 
		for(LocalDerivative ld : lds){
			var = stateDescriptor.getCorrespondingStateVariable(group, ld);
			sd.add(var);
		}
		
		return sd;
	}
	
	private StateDescriptor generateStateDescriptorLargeGroups(StateDescriptor stateDescriptor){
		
		StateDescriptor sd = new StateDescriptor();
		
		StateDescriptor temp;
		
		for (Group group : largeGroups){
			temp = generateStateDescriptorLargeGroups(stateDescriptor,group);
			sd.addAll(temp);
		}
		
		return sd;
		
	}
	
	private StateDescriptor generateStateDescriptorLargeGroups(StateDescriptor stateDescriptor , Group group){
		
		StateDescriptor sd = new StateDescriptor();

		ArrayList<LocalDerivative> ldrs = group.getGroupLocalDerivatives();
		
		StateVariable var ;
		
		for(LocalDerivative dr : ldrs){
			var = stateDescriptor.getCorrespondingStateVariable(group, dr);
			sd.add(var);
		}
		
		return sd;
		
	}
	
	
	
	// use the actions state variable and populate the state variables actionSmall, actionsSmallLarge and actionsLarge
	private void setActionsInCategories(){
		
		actionsLarge = new ArrayList<OriginalAction>();
		actionsSmall = new ArrayList<OriginalAction>();
		actionsSmallAndLarge = new ArrayList<OriginalAction>();
		
		for (OriginalAction action : actions){
			actionCategory category = getActionCategory(action);
			switch (category) {
			case LARGE:
				actionsLarge.add(action);
				break;

			case SMALL:
				actionsSmall.add(action);
				break;
			case SMALLLARGE:
				actionsSmallAndLarge.add(action);
				break;
			default:
				break;
			}
		}
	}
	
	// for one action, this method returns the category where this action belongs.
	private actionCategory getActionCategory(OriginalAction action){
		
		JumpVector jumpVector = action.getJumpVectorMinus();
		
		boolean involvedSmallGroups = false ;
		boolean involvedLargeGroups = false ;
		
		StateVariable variable;
		Integer impactMinus ;
		Group group;
		
		Iterator<StateVariable> iter = jumpVector.keySet().iterator();
		
		while( iter.hasNext() & (involvedSmallGroups == false || involvedLargeGroups==false)  ){
			variable = iter.next();
			group = variable.getGroup();
			impactMinus = jumpVector.get(variable);
			if ( impactMinus ==1 & smallGroups.contains(group) ) involvedSmallGroups = true ;
			if ( impactMinus ==1 & largeGroups.contains(group) ) involvedLargeGroups = true ;		
		}
		
		if (involvedSmallGroups==true & involvedLargeGroups==true) return actionCategory.SMALLLARGE;
		if (involvedSmallGroups==false & involvedLargeGroups ==true) return actionCategory.LARGE;
		if (involvedSmallGroups == true & involvedLargeGroups== false) return actionCategory.SMALL;
		
		return null;
	}

	public ArrayList<OriginalAction> getActionsSmall() {
		return actionsSmall;
	}

	public void setActionsSmall(ArrayList<OriginalAction> actionsSmall) {
		this.actionsSmall = actionsSmall;
	}

	public ArrayList<OriginalAction> getActionsSmallAndLarge() {
		return actionsSmallAndLarge;
	}

	public void setActionsSmallAndLarge(
			ArrayList<OriginalAction> actionsSmallAndLarge) {
		this.actionsSmallAndLarge = actionsSmallAndLarge;
	}

	public ArrayList<OriginalAction> getActionsLarge() {
		return actionsLarge;
	}

	public void setActionsLarge(ArrayList<OriginalAction> actionsLarge) {
		this.actionsLarge = actionsLarge;
	}

	public StateDescriptor getStateDescriptorSmallGroups() {
		return stateDescriptorSmallGroups;
	}

	public void setStateDescriptorSmallGroups(
			StateDescriptor stateDescriptorSmallGroups) {
		this.stateDescriptorSmallGroups = stateDescriptorSmallGroups;
	}

	public StateDescriptor getStateDescriptorLargeGroups() {
		return stateDescriptorLargeGroups;
	}

	public void setStateDescriptorLargeGroups(
			StateDescriptor stateDescriptorLargeGroups) {
		this.stateDescriptorLargeGroups = stateDescriptorLargeGroups;
	}

	public HashMap<String, Integer> getConstants() {
		return constants;
	}

	public void setConstants(HashMap<String, Integer> constants) {
		this.constants = constants;
	}

	public ArrayList<LocalDerivative> getLocalDerivatives() {
		return localDerivatives;
	}

	public void setLocalDerivatives(ArrayList<LocalDerivative> localDerivatives) {
		this.localDerivatives = localDerivatives;
	}
		
	public LocalDerivative findLocalDerivativeByName(String fname){
		Iterator<LocalDerivative> iter = localDerivatives.iterator();
		
		LocalDerivative derivative;
		
		while(iter.hasNext()){
			derivative = iter.next();
			
			if (derivative.getName().equals(fname)){
				return derivative;
			}
		}
		
		return null;
	}
	
	public ArrayList<Group> getAllGroups(){
		ArrayList<Group> allGroups = new  ArrayList<Group>();
		allGroups.addAll(smallGroups);
		allGroups.addAll(largeGroups);
		return allGroups;
	}
	
}
