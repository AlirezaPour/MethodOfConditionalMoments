package matlab.conditionalExpectation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	
	public void storeMatlabFile() {
		String content = constructOverallFunction();

		PrintWriter out = null;
		try {
			out = new PrintWriter("conditional_expectation_generated.m");
		} catch (FileNotFoundException e) {
			System.out.printf("\n\nCould not save marginal_distribution.m");
			e.printStackTrace();
		}

		out.println(content);
		out.close();

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
		
		output += "%---------------------------------------------------------\n\n";
		
		// apparent rate functions	
		output += constructApparentRateFunctions();
		output += "\n\n";
		
		// derivative functions
		output += constructDerivativeFunction();
		output += "\n\n";
		
		// mass function
		output += constructMassFunction();
		output += "\n\n";
		
		// plots
		output += constructPlots();
		output += "\n\n";
		
		// save to csv
		output += constructSaveToCSV();
		output += "\n\n";
		
		
		output += "\n\n";
		output += "end";
		
		return output;
	}
	
	public String constructSaveToCSV(){
		
		String output = "for i=1:length(t)";
		output += "\n";

		output += String.format("\toutput(i,1) = t(i);");
		output += "\n";

		output += "end";
		output += "\n\n";

		for (	ODEVariable odeVariable : odeVariables	) {
			
			if (odeVariable instanceof ODEVariableProbability){
				output += constructSaveToCSV((ODEVariableProbability) odeVariable);
			}
			if(odeVariable instanceof ODEVariableConditionalExpectation){
				output += constructSaveToCSV((ODEVariableConditionalExpectation) odeVariable);
			}
			
			output += "\n\n";
		}

		return output;
	}
	
	
	public String constructSaveToCSV(ODEVariableProbability odeVariable) {
		
		String output = "for i=1:length(t)";
		output += "\n";

		int index = odeVariable.getIndex();
		
		output += String.format("\toutput(i,2) = y(i,%d);", index);
		output += "\n";

		output += "end";
		output += "\n";

		output += String.format("csvwrite('%s.dat',output);", odeVariable.getName(), index);

		return output;
		
	}
	
	
	public String constructSaveToCSV(ODEVariableConditionalExpectation odeVariable) {
		
		String output = "for i=1:length(t)";
		output += "\n";

		int index = odeVariable.getIndex();
		
		output += String.format("\toutput(i,2) = y(i,%d);", index);
		output += "\n";

		output += "end";
		output += "\n";

		output += String.format("csvwrite('MCM_Analysis_Output/%s.dat',output);", odeVariable.getName(), index);

		return output;
		
	}

	
	public String constructPlots() {
		
		String output = "";

		for (ODEVariable odeVariable : odeVariables) {
			
			if (odeVariable instanceof ODEVariableProbability){
				output += constructPlot((ODEVariableProbability)odeVariable);
			}
			
			if (odeVariable instanceof ODEVariableConditionalExpectation){
				output += constructPlot((ODEVariableConditionalExpectation)odeVariable);
			}
		
			output += "\n\n";
		}

		return output;
	}
	
	public String constructPlot(ODEVariableProbability odeVariable) {
		
		String output = "figure;";
		output += "\n";

		int index = odeVariable.getIndex();
		
		output += String.format(
				"plot(t,y(:,%d),'-b','DisplayName','%s')",
				index, odeVariable.getName());
		output += "\n";

		output += "legend('-DynamicLegend');";
		output += "\n";

		output += "legend('Location', 'BestOutside');";
		output += "\n";

		output += "grid on;";

		return output;
		
	}
	
	public String constructPlot(ODEVariableConditionalExpectation odeVariable){
		
		String output = "figure;";
		output += "\n";

		int index = odeVariable.getIndex();
		
		output += String.format(
				"plot(t,y(:,%d),'-b','DisplayName','%s')",
				index, odeVariable.getName());
		output += "\n";

		output += "legend('-DynamicLegend');";
		output += "\n";

		output += "legend('Location', 'BestOutside');";
		output += "\n";

		output += "grid on;";

		return output;
		
	}

	
	public String constructMassFunction() {

		String output = "function M = mass(t,y)";
		output += "\n\n";

		int numberOfVariables = odeVariables.size();
		
		output += String.format("\tM = zeros(%d,%d);", numberOfVariables , numberOfVariables);
		output += "\n";

		for (ODEVariable variable : odeVariables) {

			if (variable instanceof ODEVariableProbability){
				
				ODEVariableProbability varProbability = (ODEVariableProbability) variable;
				
				int index = varProbability.getIndex();
				
				output += String.format("\tM(%d,%d)=1;\n", index, index);
				
			}
			
			if(variable instanceof ODEVariableConditionalExpectation){
				ODEVariableConditionalExpectation varConditional = (ODEVariableConditionalExpectation) variable;
				
				int index = varConditional.getIndex();
				int indexSubchain = varConditional.getState().getOdeVariableProbability().getIndex();
				output += String.format("\tM(%d,%d)=y(%d);\n", index, index, indexSubchain);
				
			}		
			
		}

		output += "\n";
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
		String expression = "";
		
		// next states
		while (iter.hasNext()) {
			
			var = iter.next();
			// currently only the probability ode variables are supported.
			
			output += "\t";
			
			
			if (var instanceof ODEVariableProbability){
				output += String.format("dydt(%d) = ",((ODEVariableProbability)var).getIndex());
				expression = ((ODEVariableProbability)var).getExpression();
			}
			
			if (var instanceof ODEVariableConditionalExpectation){
				output += String.format("dydt(%d) = ",((ODEVariableConditionalExpectation)var).getIndex());
				expression = ((ODEVariableConditionalExpectation)var).getExpression();
			}
			
			output += expression + " ; " ;
			output += "\n\n";
									
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
		
		//output += String.format("dydt(%d) = ",variable.getIndex());
		
		String outflux = constructDerivativeFunctionProbabilityVariableOutflux(variable);
		
		String influx = constructDerivativeFunctionProbabilityVariableInflux(variable);
		
		output += outflux + influx ;
		
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
		
		
		output += String.format("( rate_%s(%s) * y(%s) )", actionName, stateName,
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
		
		output += String.format(" ( rate_%s(%s) * y(%s) )", actionName,stateName,index);
		
		return output;
		
	}
	
	
	
	public String constructDerivativeExpression(ODEVariableConditionalExpectation variable){
		
		String output = "";
		
		// the flow out of the moment due to the probability outflux from the related aggregated state.
		output += constructConditionalMomentOutfluxDueToProbabilityOutflux(variable);
		output += " ...\n\t\t";
		
		
		// the flow out of the moment due to the outward Small and Small&Large transitions leaving the associated aggregated state.
		output += constructConditionalMomentOutfluxDueToOutwardTransitions(variable);
		output += " ...\n\t\t";
		
		// the flow into the conditional moment due to the inward transitions enabled by ActionSmall and ActionSmallAndLarge
		output += constructConditionalMomentInfluxDueToInwardTransitions(variable);
		output += " ...\n\t\t";
		
		
		// the flow into the conditional moment due to inward transition enabled by ActionsSmallLarge
		String partialInflux =constructConditionalMomentPartialInfluxDueToInwardActSLTransitions(variable);
		if (partialInflux.equals("")){
			
			output += " + 0";
		}
		else{
			output += partialInflux;
		}
		
		output += " ...\n\t\t";
		
		
		// the flow into the conditional moment due to inward transitions enabled by ActionsLarge
		output += constructConditionalMomentInfluxDueToInwardLargeOnlyTransitions(variable);
		
		
		
		return output;
		
	}
	
	
	public String constructConditionalMomentInfluxDueToInwardLargeOnlyTransitions(ODEVariableConditionalExpectation variable){
		
		String output = "";
		
		ArrayList<OriginalAction> allLargeActions = origModel.getActionsLarge();
		
		for(OriginalAction action : allLargeActions){
			output += " + " ;
			output += constructConditionalMomentInfluxDueToInwardLargeOnlyTransitions(variable,action);
		}
		
		return output;
		
	}
	
	public String constructConditionalMomentInfluxDueToInwardLargeOnlyTransitions(ODEVariableConditionalExpectation variable, OriginalAction action){
		
		String output = "";
		
		AggregatedState state = variable.getState();
		int probabilityIndex = state.getOdeVariableProbability().getIndex();
		output += String.format("y(%d)", probabilityIndex);
		
		output += " * ";
		
		StateVariable stateVariable = variable.getVariable();
		int impact = action.getImpactOn(stateVariable);
		output += String.format("(%d)", impact);
		
		output += " * " ; 
		
		String conditionalMoments = getConditionalMomentsOf(state);
	
		output += String.format("rate_%s(%s)",action.getName(),conditionalMoments);
		
		return output;
		
	}
	
	public String getConditionalMomentsOf(AggregatedState state){
		
		String output = "{";
		
		ArrayList<ODEVariableConditionalExpectation> conditionalExpectationVariables = state.getOdeVariablesConditionalExpectation();
		
		Iterator<ODEVariableConditionalExpectation> iter = conditionalExpectationVariables.iterator();
		
		ODEVariableConditionalExpectation variable;
		
		while(iter.hasNext()){

			variable = iter.next();
			
			int index = variable.getIndex();
			
			output += String.format("y(%d)",index );
			
			if (iter.hasNext()){
				output += " , ";
			}
		}
		
		output += "}";
		
		return output;
	}
	
	public String constructConditionalMomentPartialInfluxDueToInwardActSLTransitions(ODEVariableConditionalExpectation variable){
	
		String output = "";
		
		// filter the transitions into the associated aggregated state which are enabled by A_{sl} transitions
		AggregatedState aggState = variable.getState();
		
		ArrayList<Transition> relevantTransitions = filterInwardTransitionsSmallLarge(aggState);
		
		for(Transition transition : relevantTransitions){
			
			output += " + " ;
			
			output += constructConditionalMomentPartialInfluxDueToInwardActSLTransitions(variable, transition);
			
		}
		
		
		return output;
		
	}
	
	public String constructConditionalMomentPartialInfluxDueToInwardActSLTransitions(ODEVariableConditionalExpectation odeVariable, Transition transition){
		
		String output = "";
		
		StateVariable stateVariable = odeVariable.getVariable();
		
		AggregatedAction aggAction = (AggregatedAction) transition.getAction();
		OriginalAction origAction = aggAction.getOriginalAction();
		String actionName = aggAction.getName();
	
		// impact
		int impactOnStateVariable = origAction.getImpactOn(stateVariable);
		
		// probability index of the transition's start state
		AggregatedState aggState = (AggregatedState) transition.getStart();
		int probability_index = aggState.getOdeVariableProbability().getIndex();
		String stateName =String.format("st%d" , probability_index); 

		output += String.format("y(%d) * rate_%s(%s) * ( %d ) ", probability_index,actionName,stateName, impactOnStateVariable);
		
		return output;
		
	}

	public String constructConditionalMomentInfluxDueToInwardTransitions(ODEVariableConditionalExpectation variable){
		
		String output = "";
		
		AggregatedState aggState = variable.getState();
		
		ArrayList<Transition> inwardTransitions = aggState.getInwardTransitions();
		
		Iterator<Transition> iter = inwardTransitions.iterator();
		
		Transition transition;
		
		while(iter.hasNext()){
			
			transition = iter.next();
			
			output += " + ";
			
			output += constructConditionalMomentInfluxDueToInwardTransitions(variable,transition);
			
		}
		
		
		return output;
		
	}
	
	public String constructConditionalMomentInfluxDueToInwardTransitions(ODEVariableConditionalExpectation variable, Transition transition){
		
		String output = "";
		
		AggregatedState state = (AggregatedState) transition.getStart();
		int start_index = state.getOdeVariableProbability().getIndex();
		String stateName = String.format("st%d",start_index);
		
		String actionName = ((AggregatedAction) transition.getAction()).getName();
		
		StateVariable stateVariable = variable.getVariable();
		ODEVariableConditionalExpectation relatedVariable = getODEVariableInSubChain(state, stateVariable);
		int index_conditional_start = relatedVariable.getIndex();
		
		output += String.format("( y(%s) * rate_%s(%s) * y(%d) )", start_index,actionName, stateName, index_conditional_start);	
		
		return output;
		
	}
	
	public ODEVariableConditionalExpectation getODEVariableInSubChain(AggregatedState aggState, StateVariable fStateVariable){
		
		ArrayList<ODEVariableConditionalExpectation> allODEVariables = aggState.getOdeVariablesConditionalExpectation();
		
		StateVariable stateVariable;
		for(ODEVariableConditionalExpectation odeVariable : allODEVariables){
			stateVariable = odeVariable.getVariable();
			if (stateVariable.equals(fStateVariable)){
				return odeVariable;
			}
		}
		
		return null;
	}
	
	public String constructConditionalMomentOutfluxDueToOutwardTransitions(ODEVariableConditionalExpectation variable){
		
		String output = " + (";
		
		AggregatedState aggState = variable.getState();
		
		
		output += " - " ;
		
		int index_subchain = aggState.getOdeVariableProbability().getIndex();
		output += String.format("y(%d)",index_subchain);
		
		output += " * " ;
		
		int index_odeVariable = variable.getIndex();
		output += String.format("y(%d)",index_odeVariable);
		
		output += " * " ;
		
		// outward transitions.
			
		ArrayList<Transition> outwardTransitions = aggStateSpace.getOutgoingTransitionBank().get(aggState);
		Iterator<Transition> iter = outwardTransitions.iterator();
		
		Transition transition;
		
		output += "( ";
		
		while(iter.hasNext()){
			
			transition = iter.next();
									
			output += constructConditionalMomentOutfluxDueToOutwardTransitions(variable,transition) ;
			
			if (iter.hasNext()){
				output += " + " ;
			}
			
		}
		
		// for the last transitoin.
		output += " )";
		
		// for the end of the expression. 
		output += " )";
		
		
		return output;
		
	}
	
	public String constructConditionalMomentOutfluxDueToOutwardTransitions(ODEVariableConditionalExpectation variable , Transition transition){
		
		String output = "";
		
		AggregatedState state = variable.getState();
		int indexSubchain = state.getOdeVariableProbability().getIndex();
		
		String stateName = "st" + indexSubchain;
		String actionName =  ((AggregatedAction) transition.getAction()).getName();
		
		output += String.format(" rate_%s(%s)", actionName, stateName);
		
		
		
		return output;
	}
	
	public String constructConditionalMomentOutfluxDueToProbabilityOutflux(ODEVariableConditionalExpectation variable){
		
		String output = "";
		
		output += "( -" ;
		
		// the first part: change in the probability of being in the related sub-chain
		output += " ( " ;
		String probabilityChangeExpression = variable.getState().getOdeVariableProbability().getExpression();
		output += probabilityChangeExpression;
		output += " ) " ; 
		
		output += " * " ;
		
		output += String.format("y(%d)", variable.getIndex());
		
		output += " ) ";
				
		return output;
	}
	
	public String runSolverStatement(){
		String output = "[t,y] = ode15s(@derivatives,tspan,y0,options);";
		return output;
	}
	
	public String constructKeyMomentsForOneSubChain(){
		
		String output = "conditionals_key = { " ;
		
		AggregatedState aggState = aggStateSpace.getExplored().get(0);
		//ArrayList<ODEVariableConditionalExpectation> conditionals = construOdeVariableConditionalExpectations(aggState, origModel.getStateDescriptorLargeGroups(), null);
		ArrayList<ODEVariableConditionalExpectation> conditionals = aggState.getOdeVariablesConditionalExpectation();
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

		output += "\tstate_conditionals = containers.Map(conditionals_key,moments) ; " ; 
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
		// logic 
		// for each aggregated state construct one initial condition condition for its ODEVariableProbability. 
		// for each aggregated state, and each ODEVariableConditionalExpectation, construct one initial value
		String output = "Initial conditions associate with time point, t = ? \n" ;

		// introducing the initial values in the matlab file.
		output += constructInitialValueODEVariables();
		output += "\n";
	
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
		
		output += "  \n\t ]  ;";
		
		return output;
		
	}
	
	public ArrayList<ODEVariable> getTail(ArrayList<ODEVariable> list){
	
		ArrayList<ODEVariable> tail = new ArrayList<ODEVariable>(); ;
		ODEVariable item;
		
		int size = list.size();
		
		if (size == 1){
			// return an empty list.
			return tail;
		}
		
		if (size > 1){
			
			for (int i = 1; i <= size-1 ; i++){
				
				item = list.get(i);
				
				tail.add(item);
			}
			
		}
		
		return tail;
	
	}
	
	public String constructInitialValueODEVariables(){
		
		String output = "";
		
		output += constructInitialValueODEVariablesProbabilities();
		
		output += "\n";
			
		output += constructInitialValueODEVariablesConditionalExpectation();
			
		
		
		
		return output;
		
	}
	
	public String constructInitialValueODEVariablesConditionalExpectation(){
		
		String output = "";
		
		ArrayList<AggregatedState> aggStates = aggStateSpace.getExplored();
		
		for (AggregatedState aggState : aggStates){
			output += "\n\n";
			output += constructInitialValueODEVariablesConditionalExpectation(aggState);
			
		}
		
		return output;
	}
	
	public String constructInitialValueODEVariablesConditionalExpectation(AggregatedState state){
		String output = "";
		
		ArrayList<ODEVariableConditionalExpectation> conditionalExpectations = state.getOdeVariablesConditionalExpectation();
		
		for (ODEVariableConditionalExpectation varCon : conditionalExpectations){
			output += "\n\n";
			output += constructInitialValueODEVariablesConditionalExpectation(state,varCon);
		}
		
		return output;
	}
	
	public String constructInitialValueODEVariablesConditionalExpectation(AggregatedState state , ODEVariableConditionalExpectation varCon){
		
		String output = "";
		
		String unconditional_name = "u_" + varCon.getName();
		
		output += unconditional_name ;
		output += " = ? ;";
		output += "\n";
		

		String name = "init_"+ varCon.getName();
		output += name ;
		
		output += " = " ;
		
		output += " ( " + unconditional_name + " ) " + " / " + " ( " + "init_" + state.getOdeVariableProbability().getName() + " ) " + " ; ";
		
		return output;
		
	}
	
	public String constructInitialValueODEVariablesProbabilities(){
		String output = "";
		
		for (AggregatedState state : aggStateSpace.getExplored()){
			output += "\n";
			output += constructInitialValueODEVariablesProbabilities(state);
		}
		return output;
	}
	
	public String constructInitialValueODEVariablesProbabilities(AggregatedState state){
		
		String output = "";
		
		ODEVariableProbability odeVar = state.getOdeVariableProbability();
		
		output += "init_" + odeVar.getName();
		AggregatedState aggState = odeVar.getState();
		
		if (aggState.equals(aggModel.getAggInitialState())){
			output += " = 1 ;";	
		}else{
			output += " = 0 ;";
		}
		
		return output ;
		
	}
	
	public String constructInitialValueConditionalExpectationODEVariables(ArrayList<ODEVariable> odeVars){
		
		String output = "";
		
		// base case- check if this is empty.
		if (odeVars.isEmpty()){
			return "";
		}
		
		output += "\n";
		
		// process the head.
		ODEVariableConditionalExpectation head = (ODEVariableConditionalExpectation) odeVars.get(0);
		output += constructInitialValueConditionalExpectationODEVariable (head);
		
		// process the tail
		ArrayList<ODEVariable> tail = getTail(odeVars);
		return output + constructInitialValueConditionalExpectationODEVariables(tail);
		
	}
	
	public String constructInitialValueConditionalExpectationODEVariable(ODEVariableConditionalExpectation odeVarCon){
		String output = "";
		
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
		
		return output;
	}
	
	public String constructInitialValueODEVariable(ODEVariableProbability odeVar){
		
		return "";
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
			ArrayList<ODEVariableConditionalExpectation> exisitingConVariables = state.getOdeVariablesConditionalExpectation();
			exisitingConVariables.add(odeVar);
			
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
		
		condExptGenerator.storeMatlabFile();
		
		
		//-------------------------
		// Test 1- showing the ODE variables.
		//-------------------------
		//String output1 = condExptGenerator.display.showODEVariables();
		//System.out.printf(output1);
		
		
		
		
		
	}

}
