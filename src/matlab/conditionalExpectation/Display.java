package matlab.conditionalExpectation;

import java.util.ArrayList;

public class Display {

	ConditionalExpectation conditionalExpectationGenerator;
	
	public Display(ConditionalExpectation condExpectationGenerator){
		
		this.conditionalExpectationGenerator = condExpectationGenerator;
		
	}
	
	public String showODEVariables(){
		String output = "";
		
		ArrayList<ODEVariable> variables = conditionalExpectationGenerator.getOdeVariables();
		
		for (ODEVariable var : variables){
			output += showODEVariable(var);
			output += "\n\n";
		}
		
		return output; 
	}
	
	public String showODEVariable(ODEVariable var){
		String output = "";
		
		output += "ODE Variable : ";
		 
		if (var instanceof ODEVariableProbability){
			
			ODEVariableProbability var2 = (ODEVariableProbability) var;
			
			String name = var2.getName();
			int index = var2.getIndex();
			
			output += "\t" + name + "\t\t\t at index "+ index;
			
		}
		
		if (var instanceof ODEVariableConditionalExpectation){
			ODEVariableConditionalExpectation var2 = (ODEVariableConditionalExpectation) var;
			
			String name = var2.getName();
			int index = var2.getIndex();
			
			output += "\t" + name + "\t\t at index "+ index;
		}
		
		return output ; 
	}
	
}
