package matlab.conditionalExpectation;

import data.aggregatedModel.AggregatedState;

public class ODEVariableProbability extends ODEVariable{
	
	private String name;
	private int index ; 
	private String expression ; 
	private AggregatedState state;
	private double initialValue;
	
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
	
	public String toString (){
		return "Prob_" + state.getStateId()   ;
	}
	
	
}
