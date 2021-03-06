package matlab.marginalDistribution;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import util.ClientServerAggregatedModel;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Display;
import data.aggregatedModel.Explorer;
import data.general.Transition;
import data.general.Group;
import data.general.LocalDerivative;
import data.general.StateVariable;

public class MarginalDistribution {

	AggregatedModel model;
	AggregatedStateSpace stateSpace;

	double initialTime = 0;
	double endTime = 9000;
	int numberOfPoints = 1000;

	double relError = 1e-10;
	double absError = 1e-6;

	int numberOfVariables;

	public MarginalDistribution(AggregatedModel model, AggregatedStateSpace sp) {
		this.model = model;
		this.stateSpace = sp;
		this.numberOfVariables = stateSpace.getExplored().size();
	}

	public void storeMatlabFile() {
		String content = constructOverAllFunction();

		PrintWriter out = null;
		try {
			out = new PrintWriter("marginal_distribution_generated.m");
		} catch (FileNotFoundException e) {
			System.out.printf("\n\nCould not save marginal_distribution.m");
			e.printStackTrace();
		}

		out.println(content);
		out.close();

	}

	public String constructOverAllFunction() {

		String output = "function marginal_distribution\n\n";

		// constants
		output += constructConstants();
		output += "\n\n";

		// definition of aggregated states
		output += constructStatesMaps();

		// time information
		output += constructTimeParameters();
		output += "\n\n";

		// initial probability distribution
		output += constructInitialConditions();
		output += "\n\n";

		// solver options
		output += constructSolverOptions();
		output += "\n\n";

		// running solver
		output += runSolverStatement();
		output += "\n\n";

		// apparent rate functions
		output += constructApparentRateFunctions();

		// derivative functions
		output += constructDerivativeFunction(stateSpace.getExplored());
		output += "\n\n";

		output += constructMassFunction();
		output += "\n\n";

		output += constructPlots();
		output += "\n\n";

		output += constructSaveToCSV();
		output += "\n\n";

		output += "\n\n";
		output += "end";

		return output;

	}

	public String constructMassFunction() {

		String output = "function M = mass(t,y)";
		output += "\n\n";

		output += String.format("\tM = zeros(%d,%d);", numberOfVariables,
				numberOfVariables);
		output += "\n";

		for (int i = 1; i <= numberOfVariables; i++) {
			output += String.format("\tM(%d,%d)=1;\n", i, i);
		}

		output += "\n";
		output += "end";
		return output;

	}

	public String constructDerivativeFunction(ArrayList<AggregatedState> states) {

		String output = "function dydt = derivatives(t,y) \n\n ";

		output += String.format("\tdydt = zeros(%d,1);", numberOfVariables);
		output += "\n\n";

		Iterator<AggregatedState> iter = states.iterator();
		AggregatedState state;

		// next states
		while (iter.hasNext()) {
			output += "\t";
			state = iter.next();
			output += constructDerivativeFunction(states, state) + ";";
			output += "\n";
		}

		output += "\n";

		output += "end";

		return output;
	}

	public String constructDerivativeFunction(	ArrayList<AggregatedState> states, AggregatedState state) {
		String output = String.format("dydt(%d)=", states.indexOf(state) + 1);

		String outflux = constructDerivativeFunctionOutfluxTerms(states, state);

		String influx = constructDerivativeFunctionInfluxTerms(states, state);

		output += outflux + influx;

		return output;
	}

	public String constructDerivativeFunctionOutfluxTerms(
			ArrayList<AggregatedState> states, AggregatedState state) {

		String output = "";

		ArrayList<Transition> transitions = stateSpace
				.getOutgoingTransitionBank().get(state);

		for (Transition transition : transitions) {
			output += " -"
					+ constructDerivativeFunctionOutfluxTerm(states, state,
							transition);
		}

		return output;
	}

	public String constructDerivativeFunctionOutfluxTerm(
			ArrayList<AggregatedState> states, AggregatedState start,
			Transition transition) {

		String output = "";

		int index_start = (states.indexOf(start));
		String stateName = "st" + start.getStateId();
		String actionName = ((AggregatedAction) transition.getAction())
				.getName();

		output += String.format(" rate_%s(%s) * y(%s)", actionName, stateName,
				index_start + 1);

		return output;

	}

	public String constructDerivativeFunctionInfluxTerms(
			ArrayList<AggregatedState> states, AggregatedState state) {

		String output = "";

		ArrayList<Transition> transitions = state.getInwardTransitions();

		for (Transition transition : transitions) {
			output += " +"
					+ constructDerivativeFunctionInfluxTerm(states, state,
							transition);
		}

		return output;

	}

	public String constructDerivativeFunctionInfluxTerm(
			ArrayList<AggregatedState> states, AggregatedState state,
			Transition tr) {

		String output = "";

		AggregatedState start = (AggregatedState) tr.getStart();
		int index_start = states.indexOf(start);

		String stateName = "st" + start.getStateId();
		String actionName = ((AggregatedAction) tr.getAction()).getName();

		output += String.format(" rate_%s(%s) * y(%s)", actionName, stateName,
				index_start + 1);

		return output;

	}

	public String constructApparentRateFunctions() {

		String output = "";

		for (AggregatedAction action : model.getAggActions()) {
			output += constructApparentRateFunction(action);
			output += "\n\n";
		}

		return output;
	}

	public String constructApparentRateFunction(AggregatedAction action) {

		String output = "function  rate = rate_";

		output += action.getName() + "(state)";
		output += "\n\n";

		output += String.format(
				"\trate = ( %s ) ;\n",
				action.getSymbolicRateOfActionForMatlab(
						model.getAggStateDescriptor(), model.getGroups()));

		output += "\n";
		output += "end";

		return output;

	}

	public String runSolverStatement() {
		String output = "[t,y] = ode15s(@derivatives,tspan,y0,options);";
		return output;
	}

	public String constructInitialConditions() {
		String output = "";

		output += constructInitialValuesODEVariables();
		output += "\n";

		output += "y0 = [ ";

		ArrayList<AggregatedState> states = stateSpace.getExplored();
		Iterator<AggregatedState> iter = states.iterator();

		AggregatedState state;
		// first state
		state = iter.next();
		output += "init_prob_st_" + state.getStateId();

		while (iter.hasNext()) {
			output += " ; ";
			state = iter.next();
			output += "init_prob_st_" + state.getStateId();
		}

		output += " ] ;";

		return output;
	}

	public String constructInitialValueVector(Group group) {

		String output = "";

		ArrayList<LocalDerivative> derivatives = group
				.getGroupLocalDerivatives();
		Iterator<LocalDerivative> iter = derivatives.iterator();
		LocalDerivative derivative;

		// first variable.
		derivative = iter.next();
		output += "init_" + group.getName() + "_" + derivative.getName();

		while (iter.hasNext()) {
			output += " ; ";
			derivative = iter.next();
			output += "init_" + group.getName() + "_" + derivative.getName();
		}

		return output;

	}

	public String constructInitialValuesODEVariables() {

		String output = "";

		ArrayList<AggregatedState> states = stateSpace.getExplored();

		for (AggregatedState state : states) {

			output += "init_prob_st_" + state.getStateId() + " = ";

			if (state.equals(model.getAggInitialState())) {
				output += "1 ;";
			} else {
				output += "0 ;";
			}

			output += "\n";

		}

		return output;
	}

	public String constructTimeParameters() {
		String output = "";
		output += String.format("tspan = linspace(%f,%f,%d);", initialTime,
				endTime, numberOfPoints);
		return output;
	}

	public String constructSolverOptions() {
		String output = "";
		output += String.format(
				"options=odeset('Mass',@mass,'RelTol',%s,'AbsTol',%s);",
				Double.toString(relError), absErrorVector());
		return output;
	}

	public String absErrorVector() {

		String output = "[";

		for (AggregatedState state : stateSpace.getExplored()) {
			output += Double.toString(absError);
			output += " ";
		}

		output += "]";
		return output;
	}

	public String constructStatesMaps() {

		String output = "";

		ArrayList<AggregatedState> states = stateSpace.getExplored();

		output += constructKeyManyGroups(model.getGroups());
		output += "\n\n";

		for (AggregatedState state : states) {

			output += constructMapForOneState(state);
			output += "\n\n";
		}

		return output;
	}

	public String constructMapForOneState(AggregatedState state) {

		String firstLine = constructValueOneState(state);

		firstLine += "\n";

		int id = state.getStateId();
		String secondLine = "st" + Integer.toString(id)
				+ " = containers.Map(k,v);";

		String output = firstLine + secondLine;
		return output;
	}

	public String constructValueOneState(AggregatedState state) {

		String output = "v={";

		Iterator<Group> iter = model.getGroups().iterator();

		Group group;

		// first group
		group = iter.next();
		output += constructValuesOneStateOneGroup(state, group);

		while (iter.hasNext()) {
			output += ",";
			group = iter.next();
			output += constructValuesOneStateOneGroup(state, group);
		}

		output += "};";

		return output;
	}

	public String constructValuesOneStateOneGroup(AggregatedState state,
			Group group) {
		String output = "";

		ArrayList<LocalDerivative> derivatives = group
				.getGroupLocalDerivatives();

		Iterator<LocalDerivative> iter = derivatives.iterator();
		LocalDerivative dr;
		StateVariable var;
		int value;

		// first local derivative;
		dr = iter.next();
		var = model.getAggStateDescriptor().getCorrespondingStateVariable(
				group, dr);
		value = state.get(var).intValue();
		output += Integer.toString(value);

		while (iter.hasNext()) {
			output += ",";
			dr = iter.next();
			var = model.getAggStateDescriptor().getCorrespondingStateVariable(
					group, dr);
			value = state.get(var).intValue();
			output += Integer.toString(value);
		}

		return output;
	}

	public String constructKeyManyGroups(ArrayList<Group> groups) {

		String output = "k={";

		Iterator<Group> iter = groups.iterator();

		// first group
		Group group = iter.next();

		output += constructKeyOneGroup(group);

		while (iter.hasNext()) {
			group = iter.next();
			output += ",";
			output += constructKeyOneGroup(group);
		}

		output += "};";

		return output;

	}

	public String constructKeyOneGroup(Group group) {
		String output = "";

		ArrayList<LocalDerivative> derivatives = group
				.getGroupLocalDerivatives();

		Iterator<LocalDerivative> iter = derivatives.iterator();

		// the first derivaitive
		LocalDerivative dr = iter.next();

		// StateVariable variable =
		// model.getAggStateDescriptor().getCorrespondingStateVariable(group,
		// dr);

		output = "'" + group.getName() + "_" + dr.getName() + "'";

		while (iter.hasNext()) {
			dr = iter.next();
			output += " , ";
			// variable =
			// model.getAggStateDescriptor().getCorrespondingStateVariable(group,
			// dr);
			output += "'" + group.getName() + "_" + dr.getName() + "'";
		}

		return output;
	}

	public String constructConstants() {

		String output = "";

		HashMap<String, Integer> constants = model.getConstants();

		Iterator<String> names = constants.keySet().iterator();

		String name;
		int value;

		while (names.hasNext()) {
			name = names.next();
			value = constants.get(name).intValue();
			output += name + " = " + Integer.toString(value) + " ;\n";
		}

		return output;

	}

	// //////////////////////////////////////////////////////////////
	// /// plotting capabilities - saving into CSV files capability
	// //////////////////////////////////////////////////////////////

	public String constructPlots() {
		String output = "";

		for (AggregatedState state : stateSpace.getExplored()) {
			output += constructPlots(state);
			output += "\n\n";
		}

		return output;
	}

	public String constructSaveToCSV() {

		String output = "for i=1:length(t)";
		output += "\n";

		output += String.format("\toutput(i,1) = t(i);");
		output += "\n";

		output += "end";
		output += "\n\n";

		for (AggregatedState state : stateSpace.getExplored()) {
			output += constructSaveToCSV(state);
			output += "\n\n";
		}

		return output;

	}

	public String constructSaveToCSV(AggregatedState state) {
		String output = "for i=1:length(t)";
		output += "\n";

		output += String.format("\toutput(i,2) = y(i,%d);", state.getStateId());
		output += "\n";

		output += "end";
		output += "\n";

		output += String.format("csvwrite('P(st%d).dat',output);",
				state.getStateId());

		return output;
	}

	public String constructPlots(AggregatedState state) {
		String output = "figure;";
		output += "\n";

		output += String.format(
				"plot(t,y(:,%d),'-b','DisplayName','P_{t}(St%d)')",
				state.getStateId(), state.getStateId());
		output += "\n";

		output += "legend('-DynamicLegend');";
		output += "\n";

		output += "legend('Location', 'BestOutside');";
		output += "\n";

		output += "grid on;";

		return output;
	}

	public static void main(String args[]) {

		// testing set parameter;

		AggregatedModel model = ClientServerAggregatedModel
				.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();

		/*
		 * Display display = model.getDisplay(); String output1 =
		 * display.storeStateSpace(sp); System.out.printf(output1);
		 */

		MarginalDistribution md = new MarginalDistribution(model, sp);

		String output = md.constructOverAllFunction();

		System.out.printf(output);

		md.storeMatlabFile();

	}

}
