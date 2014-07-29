package matlab.conditionalExpectation;

import data.aggregatedModel.AggregatedState;
import data.general.StateVariable;

public class ODEVariableConditionalExpectation extends ODEVariable {
	
	private String name;
	private int index ; 
	private String expression ; 
	private double initialValue;
	private AggregatedState state;
	private StateVariable variable;
	
	public double getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(double initialValue) {
		this.initialValue = initialValue;
	}

	
	
	
	
	public AggregatedState getState() {
		return state;
	}
	public void setState(AggregatedState state) {
		this.state = state;
	}
	public StateVariable getVariable() {
		return variable;
	}
	public void setVariable(StateVariable variable) {
		this.variable = variable;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	 
	public String toString(){
		return "CondExp_" + state.getStateId() + variable.toString() ;
	}
}
