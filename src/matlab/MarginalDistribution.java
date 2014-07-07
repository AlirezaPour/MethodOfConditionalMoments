package matlab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import util.ClientServerAggregatedModel;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Display;
import data.aggregatedModel.Explorer;
import data.model.Group;
import data.model.LocalDerivative;
import data.model.StateVariable;

public class MarginalDistribution {
	
	
	
	AggregatedModel model;
	AggregatedStateSpace stateSpace;
	
	double initialTime = 0 ; 
	double endTime = 9000;
	int numberOfPoints = 1000;
	
	double relError = 1e-10;
	double absError = 1e-6;
	
	public MarginalDistribution(AggregatedModel model, AggregatedStateSpace sp){
		this.model = model;
		this.stateSpace = sp;
	}
	
	
	
	public String runSolverStatement(){
		String output = "[t,y] = ode15s(@derivatives,tspan,y0,options)" ; 
		return output ; 
	}
	
	public String constructInitialConditions(){
		String output = "";
		
		return output;
	}
	
	public String constructTimeParameters(){
		String output = "";
		output += String.format("tspan = linspace(%f,%f,%d);", initialTime,endTime,numberOfPoints); 
		return output; 
	}
	
	public String constructSolverOptions(){
		String output = "";
		output += String.format("options=odeset('Mass',@mass,'RelTol',%s,'AbsTol',%s);", Double.toString(relError),relErrorVector()); 	
		return output; 
	}
	
	public String relErrorVector(){
		String output = "[";
		
		for (Group group : model.getGroups()){
			for(LocalDerivative derivative : group.getGroupLocalDerivatives()){
				output += Double.toString(absError);
				output += " "; 
			}
		}
		
		output += "]";
		return output;
	}
	
	public String constructStatesMaps(){
		
		String output = "";
		
		ArrayList<AggregatedState> states = stateSpace.getExplored();
			
		// give each state an identifier.
		int identifier = 1; 
		for (AggregatedState state : states ){
			state.setStateId(identifier);
			identifier++; 
		}
		
		output += constructKeyManyGroups(model.getGroups());
		output += "\n\n";
		
		for (AggregatedState state : states){
			
			output += constructMapForOneState(state);
			output += "\n\n";
		}
		
		return output; 
	}
	
	public String constructMapForOneState(AggregatedState state){
		
		String firstLine = constructValueOneState(state);
		
		firstLine += "\n";
			
		int id = state.getStateId();
		String secondLine = "st" + Integer.toString(id) + " = containers.Map(k,v)" ;
		
		String output = firstLine + secondLine;
		return output;
	}
	
	public String constructValueOneState(AggregatedState state){
		
		String output = "v={";
		
		Iterator<Group> iter = model.getGroups().iterator();
		
		Group group;
		
		// first group
		group = iter.next();
		output += constructValuesOneStateOneGroup(state, group);
		
		while(iter.hasNext()){
			output += ",";
			group = iter.next();
			output += constructValuesOneStateOneGroup(state, group);
		}
		
		output += "};";
		
		return output; 
	}
	
	public String constructValuesOneStateOneGroup (AggregatedState state , Group group){
		String output = "";
		
		ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
		
		Iterator<LocalDerivative> iter = derivatives.iterator();
		LocalDerivative dr;
		StateVariable var;
		int value;
		
		// first local derivative;
		dr = iter.next();
		var = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
		value = state.get(var).intValue();
		output += Integer.toString(value);
		
		while(iter.hasNext()){
			output += ",";
			dr = iter.next();
			var = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
			value = state.get(var).intValue();
			output += Integer.toString(value);
		}
		
		
		return output; 
	}
	
	public String constructKeyManyGroups (ArrayList<Group> groups){
		
		String output = "k={";
		
		Iterator<Group> iter = groups.iterator();
		
		// first group
		Group group = iter.next();
		
		output += constructKeyOneGroup(group);
	
		while(iter.hasNext()){
			group = iter.next();
			output += ",";
			output += constructKeyOneGroup(group);
		}
		
		output += "};" ;
		
		return output;
		
	}
	
	public String constructKeyOneGroup(Group group){
		String output= "";
		
		ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
		
		Iterator<LocalDerivative> iter = derivatives.iterator();
		
		// the first derivaitive
		LocalDerivative dr = iter.next();
		
		StateVariable variable = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
		output = "'"+variable.toString()+"'";
		
		while (iter.hasNext()){
			dr = iter.next();
			variable = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
			output += ","	+	"'"	+	variable.toString()	+	"'"	;
		}
		
		return output;
	}
	
	public String constructConstants (){
		
		String output = "";
		
		HashMap<String, Integer> constants = model.getConstants();
		
		Iterator<String> names = constants.keySet().iterator();
		
		String name; 
		int value;
		
		while(names.hasNext()){
			name = names.next();
			value = constants.get(name).intValue();
			output += name + " = " +  Integer.toString(value) + " ;\n";
		}
		
		return output;
		
	}

	
	
	public static void main(String args[]){
		
		// testing set parameter;
		
		AggregatedModel model  = ClientServerAggregatedModel.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();
		
		/*Display display = model.getDisplay();
		String output1 = display.storeStateSpace(sp);
		System.out.printf(output1);*/
				
		MarginalDistribution md = new MarginalDistribution(model, sp);
		
		String output = md.constructStatesMaps();
		System.out.printf(output); 
		
		String output2 = md.constructTimeParameters();
		System.out.printf(output2);
		
		System.out.printf("\n");
		
		String output3 = md.constructSolverOptions();
		System.out.printf(output3);
		
		if (sp.getExplored().contains(model.getAggInitialState())){
			System.out.println("\nyes");
			System.out.print(sp.getExplored().indexOf(model.getAggInitialState()));
		}
		
	}
	
	
}
