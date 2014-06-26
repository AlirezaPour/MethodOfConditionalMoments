package data.aggregatedModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.model.Group;
import data.model.JumpVector;
import data.model.LocalDerivative;
import data.model.NumericalVector;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class Display {
	
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
		output += state.getStateIdentifier();
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
		String output = "Model: \n\n";
		
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
	
	public static void main(String args[]){
		
		/*ArrayList<Group> groups = new ArrayList<Group>();
		
		// group 1 
		LocalDerivative serverIdle = new LocalDerivative("Server_idle");
		LocalDerivative serverLogging = new LocalDerivative("Server_logging");
		LocalDerivative serverBroken = new LocalDerivative("Server_broken");
		ArrayList<LocalDerivative> serverDerivatives = new ArrayList<LocalDerivative>();
		serverDerivatives.add(serverIdle);
		serverDerivatives.add(serverLogging);
		serverDerivatives.add(serverBroken);
		Group servers = new Group("Servers",serverDerivatives );
		
		// group 2
		LocalDerivative clientThinking = new LocalDerivative("Client_think");
		LocalDerivative clientRequesting = new LocalDerivative("Client_req");
		ArrayList<LocalDerivative> clientDerivatives = new ArrayList<LocalDerivative>();
		clientDerivatives.add(clientThinking);
		clientDerivatives.add(clientRequesting);
		Group clients = new Group("Clients",clientDerivatives );
		
		groups.add(servers);
		groups.add(clients);
		
		// creating the state variables
		StateVariable Si = new StateVariable(servers, serverIdle);
		StateVariable Sl = new StateVariable(servers, serverLogging);
		StateVariable Sb = new StateVariable(servers, serverBroken);
		
		StateVariable Cr = new StateVariable(clients, clientRequesting);
		StateVariable Ct = new StateVariable(clients, clientThinking);
		
		// put these state variables in a stateDescriptor
		StateDescriptor descriptor = new StateDescriptor();
		descriptor.add(Si);
		descriptor.add(Sl);
		descriptor.add(Sb);
		descriptor.add(Cr);
		descriptor.add(Ct);

		
		// setting the populations in state1 
		int popServerIdle = 12 ; 
		int popServerLog = 24; 
		int popServerBrok = 36;
		
		int popClientThink = 2600 ; 
		int popClientReq = 260;
		
		
		// creating the state1		
		AggregatedState state1 = new AggregatedState();
		
		state1.put(Si, popServerIdle);
		state1.put(Sl, popServerLog);
		state1.put(Sb, popServerBrok);
		state1.put(Ct, popClientThink);
		state1.put(Cr, popClientReq);
		state1.setStateIdentifier("state1");
		
		// setting the populations in state1 
		popServerIdle = 10 ; 
		popServerLog = 20; 
		popServerBrok = 30;
				
		popClientThink = 20000 ; 
		popClientReq = 200;
		
		// creating the state2		
		AggregatedState state2 = new AggregatedState();
				
		state2.put(Si, popServerIdle);
		state2.put(Sl, popServerLog);
		state2.put(Sb, popServerBrok);
		state2.put(Ct, popClientThink);
		state2.put(Cr, popClientReq);
		state2.setStateIdentifier("state2");

		
		// creating a list of states. this would represent a flatten state space
		ArrayList<AggregatedState> states = new ArrayList<AggregatedState>();
		states.add(state1);
		states.add(state2);
		
		// creating the action request 
		AggregatedAction request = new AggregatedAction();
		request.setName("Request");
		
		JumpVector reqJumpVector = new JumpVector();
		reqJumpVector.put(Si, -1);
		reqJumpVector.put(Sl, +1);
		reqJumpVector.put(Sb, 0);
		reqJumpVector.put(Ct, +1);
		reqJumpVector.put(Cr, -1);
		
		request.setJumpVector(reqJumpVector);
		
		JumpVector reqJumpVectorMinus = new JumpVector();
		reqJumpVectorMinus.put(Si, 1);
		reqJumpVectorMinus.put(Sl, 0);
		reqJumpVectorMinus.put(Sb, 0);
		reqJumpVectorMinus.put(Ct, 0);
		reqJumpVectorMinus.put(Cr, 1);
		
		request.setJumpVectorMinus(reqJumpVectorMinus);
		
		JumpVector reqJumpVectorPlus = new JumpVector();
		reqJumpVectorPlus.put(Si, 0);
		reqJumpVectorPlus.put(Sl, +1);
		reqJumpVectorPlus.put(Sb, 0);
		reqJumpVectorPlus.put(Ct, +1);
		reqJumpVectorPlus.put(Cr, 0);
		
		request.setJumpVectorPlus(reqJumpVectorPlus);
		
		ArrayList<AggregatedAction> actions = new ArrayList<AggregatedAction>();
		actions.add(request);
		
		// creating the action think
		
		// creating the action request 
		AggregatedAction think = new AggregatedAction();
		think.setName("think");
				
		JumpVector thinkJumpVector = new JumpVector();
		thinkJumpVector.put(Si, 0);
		thinkJumpVector.put(Sl, 0);
		thinkJumpVector.put(Sb, 0);
		thinkJumpVector.put(Ct, -1);
		thinkJumpVector.put(Cr, +1);
				
		think.setJumpVector(thinkJumpVector);
				
		JumpVector thinkJumpVectorMinus = new JumpVector();
		thinkJumpVectorMinus.put(Si, 0);
		thinkJumpVectorMinus.put(Sl, 0);
		thinkJumpVectorMinus.put(Sb, 0);
		thinkJumpVectorMinus.put(Ct, 1);
		thinkJumpVectorMinus.put(Cr, 0);
				
		think.setJumpVectorMinus(thinkJumpVectorMinus);
				
		JumpVector thinkJumpVectorPlus = new JumpVector();
		thinkJumpVectorPlus.put(Si, 0);
		thinkJumpVectorPlus.put(Sl, 0);
		thinkJumpVectorPlus.put(Sb, 0);
		thinkJumpVectorPlus.put(Ct, 0);
		thinkJumpVectorPlus.put(Cr, 1);
				
		think.setJumpVectorPlus(thinkJumpVectorPlus);
		
		actions.add(think);	
		
		// constructing the model
		AggregatedModel model = new AggregatedModel();
		model.setGroups(groups);
		model.setAggStateDescriptor(descriptor);
		model.setAggActions(actions);
		model.setAggInitialState(state1);
		
		Display display = new Display(model);
		
		// show the different things in the model
		System.out.printf(display.showModel());
		*/
		
	}
	
}
