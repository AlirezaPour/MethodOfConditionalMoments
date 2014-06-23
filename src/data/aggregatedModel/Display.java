package data.aggregatedModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.model.Group;
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

	private String actionLabel = "Action : ";
	private int spaceAfterActionName = 2 ;
	
	private StateDescriptor descriptor ; 
	private ArrayList<AggregatedAction> actions;
	private ArrayList<Group> groups;
	
	public Display(StateDescriptor descriptor, ArrayList<AggregatedAction> actions, ArrayList<Group> groups){
		this.descriptor = descriptor;
		this.actions = actions;
		this.groups = groups;
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
	
	
	/*
	
	
	
	public static void displayAggregatedAction(AggregatedModel model, AggregatedAction action){
		lineAboveActionTitle(action);
		
		System.out.printf("\n");
		
		displayTitleAction(action);
		
		System.out.printf("\n");
		
		lineBelowActionTitle(action);
		
		System.out.printf("\n");
		
		// group labels
		System.out.printf(spaces(minus.length()));
		displayTitlesManyGroups(model.getGroups());
		System.out.printf(groupDivider);
		
		System.out.printf("\n");
		
		// local derivative labels
		System.out.printf(spaces(minus.length()));
		displayLocalDerivativesOfManyGroups(model.getGroups());
		System.out.printf(groupDivider);
		
		System.out.printf("\n");
		
		// line above Net
		
		// net vector
		displayJumpVectorOneActionManyGroups( model,  action, net);
		
		System.out.printf("\n");
		
		// minus vector 
		displayJumpVectorOneActionManyGroups( model,  action, minus);
		
		System.out.printf("\n");
		
		displayJumpVectorOneActionManyGroups( model,  action, plus);

		System.out.printf("\n");
		
	}
	
	
	
	public static void displayJumpVectorOneActionOneGroup(AggregatedModel model, AggregatedAction action, Group group, String type){
		// the group divider
				System.out.printf(groupDivider);
				System.out.printf(spaces((spaceAfterGroupDivider)));
					
				// the local derivatives
				ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
				LocalDerivative derivative;
				Iterator<LocalDerivative> iter = derivatives.iterator();
				
				// the first state variable
				derivative = iter.next();
				StateDescriptor descriptor  = model.getAggStateDescriptor();
				StateVariable variable  = descriptor.getCorrespondingStateVariable(group, derivative);
				int impact = -2 ; 
				switch (type) {
				case "net":
					impact = action.getJumpVector().get(variable);
					break;
				case "minus" : 
					impact = action.getJumpVectorMinus().get(variable);
					break;
				case "plus" : 
					impact = action.getJumpVectorPlus().get(variable);
					break;			
				}
				
				int length = group.maxLocalDerivativeLength();
				String format = "%-" +  length + "s";
				
				System.out.printf(format, impact);
				
				while(iter.hasNext()){
					// the rest of the derivatives in this group
					derivative = iter.next();
					
					System.out.printf(spaces(spaceBeforeLocDerDivider));
					System.out.printf(localDerDivider);
					System.out.printf(spaces(spaceAfterLocDerDivider));
					
					variable  = descriptor.getCorrespondingStateVariable(group, derivative);
					impact = action.getJumpVector().get(variable);
					System.out.printf(format, impact);
				}
				
				// end of this group. Get ready for the next one.
				System.out.printf(spaces(spaceBeforeGroupDivider));		
	}
	
	
	
	public static void displayJumpVectorOneActionManyGroups(AggregatedModel model, AggregatedAction action){
		ArrayList<Group> groups = model.getGroups();
		
		for (Group group : groups){
			displayJumpVectorOneActionOneGroup(model, action, group);
		} 
		System.out.printf(groupDivider);
	}

	
	
	
	public static void displayTitleAction (AggregatedAction action){
		String output = "";
		output +=  spaces(minus.length());
		output +=  groupDivider;
		output +=  spaceAfterGroupDivider;
		output +=  actionLabel;
		output += action.getName();
		output += spaces(spaceAfterActionName);
		output += "|";
		
		System.out.printf(output);
	}
	
	public static void lineAboveActionTitle(AggregatedAction action){
		
		String output =  spaces(minus.length());
		int length = groupDivider.length() + 
					spaceAfterGroupDivider +
					actionLabel.length() + 
					action.getName().length()+
					spaceAfterActionName + 
					"1".length();
		output += underline(length);
		System.out.printf(output);
		
	}
	
	public static void lineBelowActionTitle(AggregatedAction action){
		
		String output = spaces(minus.length());
		
		// i dont know this length yet
		int length = 10;
		output += underline(length);
		
		System.out.printf(output);
		
	}

	
	

		
	
	
	
	// display numerical vector with group titles and local derivative titles
	public static void displayNumericalVector(AggregatedModel model , NumericalVector vector){

		ArrayList<Group> groups = model.getGroups();
		
		lineUnderManyGroups(groups);
		
		System.out.printf("\n");
		
		displayTitlesManyGroups(groups);
		
		System.out.printf("\n");
		
		lineUnderManyGroups(groups);
		
		System.out.printf("\n");
		
		displayLocalDerivativesOfManyGroups(groups);
		
		System.out.printf("\n");
		
		lineUnderManyGroups(groups);
		
		System.out.printf("\n");
		
		displayNumericalVectorManyGroups(model,vector,groups);
		
		System.out.printf("\n");
		
		lineUnderManyGroups(groups);
		
	}
	
	public static void displayNumericalVectorValuesOnly (AggregatedModel model, AggregatedState state){
		ArrayList<Group> groups = model.getGroups();
		displayNumericalVectorManyGroups(model,state,groups);
	}
		
	
	*/
	
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
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
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

		// setting the populations
		int popServerIdle = 10 ; 
		int popServerLog = 20; 
		int popServerBrok = 30;
		
		int popClientThink = 20000 ; 
		int popClientReq = 200;
		
		
		// creating the state		
		AggregatedState state = new AggregatedState();
		
		state.put(Si, popServerIdle);
		state.put(Sl, popServerLog);
		state.put(Sb, popServerBrok);
		state.put(Ct, popClientThink);
		state.put(Cr, popClientReq);
		
		// constructing the model
		AggregatedModel model = new AggregatedModel();
		model.setGroups(groups);
		model.setAggStateDescriptor(descriptor);
		
		Display display = new Display(descriptor, null, groups);
		
		String output = display.showManyGroupsLables(groups);
		System.out.println(output);
		System.out.println(display.showUnderlineManyGroups(groups));
		output = display.showLocalDerivativesManyGroups(groups);
		System.out.println(output);		
		System.out.println(display.showUnderlineManyGroups(groups));
		output = display.showNumericalVectorManyGroups(model, state, groups);
		System.out.println(output);
	}
	
}
