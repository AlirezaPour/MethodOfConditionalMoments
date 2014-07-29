package matlab.conditionalExpectation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import util.ClientServerModel;
import aggregation.Aggregation;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Explorer;
import data.general.Group;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;
import data.general.Transition;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;

public class ConditionalExpectation {

	private OriginalModel origModel;
	private AggregatedModel aggModel;
	private AggregatedStateSpace aggStateSpace;
	
	private ArrayList<ODEVariable> odeVariables ;
	
	public Display display;
	
	// time options
	double initialTime = 0 ; 
	double endTime = 9000;
	int numberOfPoints = 1000;
	
	// error options
	double relError = 1e-10;
	double absError = 1e-6;
	
	public ConditionalExpectation(OriginalModel origModel, StateDescriptor stDesLargeGroups, AggregatedModel aggModel, AggregatedStateSpace ssp){
		this.origModel = origModel;
		this.aggModel = aggModel;
		odeVariables = constructODEVariables(ssp,stDesLargeGroups);
		this.aggStateSpace = ssp;
		display = new Display(this);
	}
	
	public String constructOverallFunction(){
		
		String output = "function marginal_distribution_conditional_expectation\n\n";
		
		// constants 
		output += constructConstants();
		
		// definition of aggregated states
		output += "\n\n";
		output +=  constructStatesMaps(); 
	
		// the key for the odes capturing the conditional moments
		output += constructKeyMomentsForOneSubChain();
		output += "\n\n";
		
		// time information
		output += constructTimeParameters(); 
		output += "\n\n";
		
		// coming from initial probability distribution
		// here we only put the name of the variables which need to be specified. the modeller
		// is responsible for putting the right values here. 
		output += constructInitialValuesODEVariables() ;
		output += "\n\n";
	
		// options for running the solver.
		output += constructSolverOptions();
		output += "\n\n";
		
		// running solver
		output += runSolverStatement();
		output += "\n\n";
		
		// apparent rate functions
		output += constructKeyMomentsForOneSubChain();
		output += "\n\n";
		
		output += constructApparentRateFunctions();
		output += "\n\n";
		
		// derivative functions
		output += constructDerivativeFunction();
		output += "\n\n";
		
		output += "\n\n";
		output += "end";
		
		return output;
	}
	
	public String constructDerivativeFunction(){
		String output = "function dydt = derivatives(t,y) \n\n ";

		output += String.format("\tdydt = zeros(%d,1);", odeVariables.size());
		output += "\n\n";

		// for each derivative variable, this construct one expression. 
		constructDerivativeExpressions();
		
		Iterator<ODEVariable> iter = odeVariables.iterator();
		
		ODEVariable var ;
		String expression;
		
		// next states
		while (iter.hasNext()) {
			
			var = iter.next();
			// currently only the probability ode variables are supported.
			if (var instanceof ODEVariableProbability){
				output += "\t";
				expression = ((ODEVariableProbability)var).getExpression();
				output += expression + " ; " ;
				output += "\n";
			}
									
		}

		output += "\n";

		output += "end";

		return output;
		
	}
	
	public void constructDerivativeExpressions(){
		
		String expression;
		
		for (ODEVariable variable : odeVariables){
			
			if (variable instanceof ODEVariableProbability){
				expression = constructDerivativeExpression((ODEVariableProbability)variable);
				((ODEVariableProbability)variable).setExpression(expression);
			}
			
			if(variable instanceof ODEVariableConditionalExpectation){
				expression = constructDerivativeExpression((ODEVariableConditionalExpectation)variable);
				((ODEVariableConditionalExpectation)variable).setExpression(expression);
			}
			
		}
		
	}
	
	public String constructDerivativeExpression(ODEVariableProbability variable){
		
		String output = "";
		
		//AggregatedState state = variable.getState();
		
		output += String.format("dydt(%d) = ",variable.getIndex());
		
		String outflux = constructDerivativeFunctionProbabilityVariableOutflux(variable);
		
		String influx = constructDerivativeFunctionProbabilityVariableInflux(variable);
		
		output = outflux + influx ;
		
		return output;
		
	}
	
	public String constructDerivativeFunctionProbabilityVariableInflux(ODEVariableProbability variable){
		
		String output = "";
		
		AggregatedState state = variable.getState();
		
		ArrayList<Transition> transitions = state.getInwardTransitions();
		
		for(Transition transition : transitions){
			output += " +" + constructDerivativeFunctionProbabilityVariableInflux(variable,transition);
		}
		
		return output;
		
	}
	
	public String constructDerivativeFunctionProbabilityVariableInflux(ODEVariableProbability variable, Transition transition){
		
		String output =" ";
		
		
		// get the start. get the index of the variable which capture the probability of the starting state.
		AggregatedState state = (AggregatedState) transition.getStart();
		int startIndex = state.getOdeVariableProbability().getIndex();		
		
		String stateName = "st" + startIndex;
		String actionName = ((AggregatedAction) transition.getAction()).getName();
		
		
		output += String.format(" rate_%s(%s) * y(%s)", actionName, stateName,
				startIndex);
		
		return output;
		
	}
	
	
	public String constructDerivativeFunctionProbabilityVariableOutflux(ODEVariableProbability variable){
		String output = "";
		
		AggregatedState state = variable.getState();
		
		ArrayList<Transition> transitions = aggStateSpace.getOutgoingTransitionBank().get(state);
		
		for (Transition transition : transitions){
			output += " -" + constructDerivativeFunctionProbabilityVariableOutFlux(variable,transition); 
		}
		
		return output;
	}
	
	public String constructDerivativeFunctionProbabilityVariableOutFlux(ODEVariableProbability variable,Transition transition){
		
		String output = "";
		
		int index = variable.getIndex();
		String stateName = "st" + index;
		String actionName = ((AggregatedAction) transition.getAction()).getName();
		
		output += String.format(" rate_%s(%s) * y(%s)", actionName,stateName,index);
		
		return output;
		
	}
	
	public String constructDerivativeFunctionProbabilityVariableInFlux(ODEVariableProbability variable){
		String output = "";
		return output;
	}
	
	public String constructDerivativeExpression(ODEVariableConditionalExpectation variable){
		
		String output = "";
		
		return output;
		
	}
	
	public String runSolverStatement(){
		String output = "[t,y] = ode15s(@derivatives,tspan,y0,options);";
		return output;
	}
	
	public String constructKeyMomentsForOneSubChain(){
		
		String output = "conditionals_key = { " ;
		
		AggregatedState aggState = aggStateSpace.getExplored().get(0);
		ArrayList<ODEVariableConditionalExpectation> conditionals = construOdeVariableConditionalExpectations(aggState, origModel.getStateDescriptorLargeGroups(), null);
		Iterator<ODEVariableConditionalExpectation> iter = conditionals.iterator();
		
		// first variable.
		ODEVariableConditionalExpectation conditional =  iter.next();
		StateVariable stateVariable = conditional.getVariable();
		String name = stateVariable.toStringForMatlab();
		
		output += "'" ;
		output+= name;
		output += "'";
		
		while(iter.hasNext()){
			
			output += " , ";
			
			conditional = iter.next();
			
			stateVariable = conditional.getVariable();
			name = stateVariable.toStringForMatlab();
			
			output += "'" ;
			output+= name;
			output += "'";
			
		}
		
		
		output += " } ;";
		
		return output;
		
	}
	
	public String constructApparentRateFunctions() {

		// actionsSmall and actionsSmallAndLarge.
		String output = "";

		for (OriginalAction action : origModel.getActionsSmall()) {
			output += constructApparentRateFunctionSmall(action);
			output += "\n\n";
		}
		
		for (OriginalAction action : origModel.getActionsSmallAndLarge()) {
			output += constructApparentRateFunctionSmallAndLarge(action);
			output += "\n\n";
		}
		
		ArrayList<OriginalAction> actionsLarge = origModel.getActionsLarge();
		for (OriginalAction action : actionsLarge){
			output += constructApparentRateFunctionlLarge(action);
			output += "\n\n";
		}
		
		return output;
	}
	
	public String constructApparentRateFunctionlLarge(OriginalAction action){
				
		String output = "function  rate = rate_";

		output += action.getName() + "(moments)";
		output += "\n\n";

		output += "\tst_conditionals = containers.Map(conditionals_key,moments) ; " ; 
		output += "\n\n";
		
		output += String.format(	"\trate = ( %s ) ;\n"	,	action.getSymbolicRateOfActionForMatlabConditionalMoments(	origModel.getStateDescriptor()	,	 origModel.getLargeGroups()	)	);

		output += "\n";
		output += "end";

		return output;
		
		
		
	}
	
	public String constructApparentRateFunctionSmallAndLarge(OriginalAction action) {
		// the rate expression should be produced by the aggregated model and not the original one.

		String output = "function  rate = rate_";

		output += action.getName() + "(state)";
		output += "\n\n";

		AggregatedAction aggAction = action.getAggregatedVersion();
		
		
		
		output += String.format(	"\trate = ( %s ) ;\n"	,	aggAction.getSymbolicRateOfActionForMatlab(	aggModel.getAggStateDescriptor()	,	 aggModel.getGroups()	)	);

		output += "\n";
		output += "end";

		return output;

	}
	
	public String constructApparentRateFunctionSmall(OriginalAction action) {

		String output = "function  rate = rate_";

		output += action.getName() + "(state)";
		output += "\n\n";

		output += String.format(	"\trate = ( %s ) ;\n"	,	action.getSymbolicRateOfActionForMatlab(	origModel.getStateDescriptor()	,	 origModel.getAllGroups()	)	);

		output += "\n";
		output += "end";

		return output;

	}

	
	public String constructSolverOptions(){
		
		String output = "";
		output += String.format(
				"options=odeset('Mass',@mass,'RelTol',%s,'AbsTol',%s);",
				Double.toString(relError), absErrorVector());
		return output;
		
	}
	
	public String absErrorVector() {

		String output = "[";

		for (ODEVariable var : odeVariables) {
			output += Double.toString(absError);
			output += " ";
		}

		output += "]";
		return output;
	}
	
	public String constructInitialValuesODEVariables(){
		
		String output = "" ;

		// introducing the initial values in the matlab file.
		for(ODEVariable odeVar : odeVariables){
			
			output += constructInitialValueODEVariable(odeVar);
			output += "\n";
	
		}
		
		// constructing the vector of initial values in matlab.
		output += "\n\n";
		output += "y0 = [ " ; 
		
		
		Iterator<ODEVariable> iter = odeVariables.iterator();
		ODEVariable var;
		
		// first variable.
		var = iter.next();
		if(var instanceof ODEVariableProbability){
			output += "init_" + ((ODEVariableProbability)var).getName();
		}
		if(var instanceof ODEVariableConditionalExpectation){
			output += "init_" + ((ODEVariableConditionalExpectation)var).getName();
		}

		if (iter.hasNext()){
			output += "  ;  ";
		}
		
		if(var instanceof ODEVariableProbability){
			output += "     % index " + ((ODEVariableProbability)var).getIndex();
		}
		if(var instanceof ODEVariableConditionalExpectation){
			output += "     % index " + ((ODEVariableConditionalExpectation)var).getIndex();
		}
		
		 
		// next variables.
		while(iter.hasNext()){
			
			var = iter.next();
			
			output += "\n";
			
			if(var instanceof ODEVariableProbability){
				output += "init_" + ((ODEVariableProbability)var).getName();
			}
			if(var instanceof ODEVariableConditionalExpectation){
				output += "init_" + ((ODEVariableConditionalExpectation)var).getName();
			}

			if (iter.hasNext()){
				output += "  ;  ";
			}
			
			if(var instanceof ODEVariableProbability){
				output += "   %   index " + ((ODEVariableProbability)var).getIndex();
			}
			if(var instanceof ODEVariableConditionalExpectation){
				output += "   %   index " + ((ODEVariableConditionalExpectation)var).getIndex();
			}
		}
		
		output += "   ]  ;";
		
		return output;
		
	}
	
	public String constructInitialValueODEVariable(ODEVariable odeVar){
		
		String output = "";
		AggregatedState aggState;
		
		if (odeVar instanceof ODEVariableProbability){
			
			output += "init_" + ((ODEVariableProbability)odeVar).getName();
			aggState = ((ODEVariableProbability) odeVar).getState();
			
			if (aggState.equals(aggModel.getAggInitialState())){
				output += " = 1 ;";	
			}else{
				output += " = 0 ;";
			}
			
		}
		
		else if ( odeVar instanceof ODEVariableConditionalExpectation	){
			
			ODEVariableConditionalExpectation odeVarCon = ((ODEVariableConditionalExpectation)odeVar);
			
			output += "init_" + odeVarCon.getName() + " = ";
			
			AggregatedState subchain = odeVarCon.getState();
			
			if (subchain.equals(aggModel.getAggInitialState())){
				StateVariable variable = odeVarCon.getVariable();
				Integer value = origModel.getInitialState().get(variable);
				output += value.toString() + ";" ;
			}
			else{
				output += " 0 ; ";
			}
			
		}
		
		return output;
		
	}
	
	public String constructTimeParameters(){
		String output = "";
		output += String.format("tspan = linspace(%f,%f,%d);", initialTime,endTime,numberOfPoints); 
		return output; 
	}
	
	public String constructConstants (){
		
		String output = "";
		
		HashMap<String, Integer> constants = origModel.getConstants();
		
		Iterator<String> names = constants.keySet().iterator();
		
		String name; 
		int value;
		
		while(names.hasNext()){
			
			name = names.next();
			
			value = constants.get(name).intValue();
			
			if (!(name.equals("passive"))){
				output += name + " = " +  Integer.toString(value) + " ;\n";
			}
			
		}
		
		return output;
		
	}
	
	
	public int getIndexOf(ODEVariable odeVar){
		
		if (odeVar instanceof ODEVariableProbability){
			int index = (	(ODEVariableProbability) odeVar).getIndex();
			return index;
		}
		
		if(odeVar instanceof ODEVariableConditionalExpectation){
			int index = (	(ODEVariableConditionalExpectation) odeVar).getIndex();
			return index;
		}
		
		return -1;
	}
	
	public ArrayList<ODEVariable> constructODEVariables(AggregatedStateSpace ssp, StateDescriptor stDesLargeGroups){
		
		ArrayList<ODEVariable> odeVariables = new ArrayList<ODEVariable>();
		
		ArrayList<? extends ODEVariable> currentVariables;
		
		// ode variables related to the marginal distribution over the small groups.
		currentVariables = constructODEVariablesProbabilities(ssp);
		odeVariables.addAll(currentVariables);
		
		// the zero point for the indexer is exactly the size of the aggregated State space.
		int zeroPlace = odeVariables.size();
		VariableIndexer indexer = new VariableIndexer(zeroPlace);
		
		// ode variables related to the large groups
		currentVariables = constructODEVariablesForLargeGroups(ssp, stDesLargeGroups, indexer);
		odeVariables.addAll(currentVariables);
		
		return odeVariables;
	}
	
	
	public ArrayList<ODEVariableConditionalExpectation> constructODEVariablesForLargeGroups(AggregatedStateSpace ssp, StateDescriptor stDesLargeGroups, VariableIndexer indexer){
		
		ArrayList<ODEVariableConditionalExpectation> odeVariables = new ArrayList<ODEVariableConditionalExpectation>();
		
		ArrayList<AggregatedState> allAggregatedStates = ssp.getExplored();
		
		ArrayList<ODEVariableConditionalExpectation> currentOdeVariables;
		
		for (AggregatedState state : allAggregatedStates){
			
			currentOdeVariables = construOdeVariableConditionalExpectations(state, stDesLargeGroups, indexer);
			
			odeVariables.addAll(currentOdeVariables);
			
		}
			
		return odeVariables;
		
	}
	
	// given an aggregated state, constructs the conditional expectation variables related to the variables of the large groups.
	public ArrayList<ODEVariableConditionalExpectation> construOdeVariableConditionalExpectations(AggregatedState state,StateDescriptor stDesLargeGroups, VariableIndexer indexer){
		
		ArrayList<ODEVariableConditionalExpectation> odeVariables = new ArrayList<ODEVariableConditionalExpectation>();
		
		ODEVariableConditionalExpectation odeVar ;
		
		String varName;
		int index ;
		
		for (StateVariable variable : stDesLargeGroups){
			
			odeVar = new ODEVariableConditionalExpectation();
			
			if (indexer != null){
				indexer.increaseIndex();
				index = indexer.getIndex();
				odeVar.setIndex(index);	
			}
			
			
			varName = "con_E_"+ variable.toStringForMatlab() + "_st_" + state.getStateId() ;
			odeVar.setName(varName);
			
			odeVar.setState(state);
			
			odeVar.setVariable(variable);
			
			odeVariables.add(odeVar);
		}
		
		return odeVariables;
		
	}	
	
	// constructs the ode variables for the variables 
	// related to the marginal distribution over the small groups.
	public ArrayList<ODEVariableProbability> constructODEVariablesProbabilities(AggregatedStateSpace ssp){
		
		ArrayList<ODEVariableProbability> odeVariables = new ArrayList<ODEVariableProbability>();
		
		ODEVariableProbability  odeVar ;
		int index ;
		
		String name ;
		
		for (AggregatedState state : ssp.getExplored()){
			
			odeVar = new ODEVariableProbability();
			
			index = ssp.getExplored().indexOf(state);
			index += 1;	// the indexes in the matlab file start from one and not zero. 
			odeVar.setIndex(index);
			
			name = "prob_st_" + Integer.toString(state.getStateId());
			odeVar.setName(name);
			
			odeVar.setState(state);
			state.setOdeVariableProbability(odeVar);
			
			odeVariables.add(odeVar);
			
		}
		
		return odeVariables;
		
	}
	
	public ArrayList<ODEVariable> getOdeVariables() {
		return odeVariables;
	}

	public void setOdeVariables(ArrayList<ODEVariable> odeVariables) {
		this.odeVariables = odeVariables;
	}
	
	
	// from all transitions into this state, filter the
	// transitions enabled by an small and large action type.
	public ArrayList<Transition> filterInwardTransitionsSmallLarge(AggregatedState state){
		
			ArrayList<Transition> allTransitions = state.getInwardTransitions();
			ArrayList<Transition> inwTrans = new ArrayList<Transition>();
			
			for (Transition trans : allTransitions){
				
				AggregatedAction aggAction = (AggregatedAction) trans.getAction();
				
				OriginalAction origAction = aggAction.getOriginalAction();
				
				if (origModel.getActionsSmallAndLarge().contains(origAction)){
					inwTrans.add(trans);
				}
				
			}
			
			return inwTrans;
			
	} 


	public String constructStatesMaps(){
		
		String output = "";
		
		ArrayList<AggregatedState> states = aggStateSpace.getExplored();
			
		output += constructKeyManyGroups(aggModel.getGroups());
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
		String secondLine = "st" + Integer.toString(id) + " = containers.Map(k,v);" ;
		
		String output = firstLine + secondLine;
		return output;
	}
	
	
	public String constructValueOneState(AggregatedState state){
		
		String output = "v={";
		
		Iterator<Group> iter = aggModel.getGroups().iterator();
		
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
		var = aggModel.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
		value = state.get(var).intValue();
		output += Integer.toString(value);
		
		while(iter.hasNext()){
			output += ",";
			dr = iter.next();
			var = aggModel.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
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
		
		//StateVariable variable = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
		
		output = "'" + group.getName() + "_" + dr.getName()+"'";
		
		while (iter.hasNext()){
			dr = iter.next();
			output += " , ";
			//variable = model.getAggStateDescriptor().getCorrespondingStateVariable(group, dr);
			output += "'" + group.getName() + "_" + dr.getName()+"'"	;
		}
		
		return output;
	}
	
	public static void main (String args[]){
		
		
		OriginalModel origModel = ClientServerModel.getClientServerModel();

		// aggregation
		Aggregation aggregationAlgorithm = new Aggregation(origModel);
		AggregatedModel aggModel = aggregationAlgorithm.runAggregationAlgorithm();
		
		// aggregated state space
		Explorer explorer = new Explorer(aggModel);
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();

		// initialising the conditional expectation generator
		StateDescriptor stDesLargeGroups = origModel.getStateDescriptorLargeGroups();
		ConditionalExpectation condExptGenerator = new ConditionalExpectation(origModel,stDesLargeGroups,aggModel,sp);
		
		// producing the matlab file
		String output = condExptGenerator.constructOverallFunction();
		System.out.println(output);
		
		
		//-------------------------
		// Test 1- showing the ODE variables.
		//-------------------------
		//String output1 = condExptGenerator.display.showODEVariables();
		//System.out.printf(output1);
		
		
		
		
		
	}

}
