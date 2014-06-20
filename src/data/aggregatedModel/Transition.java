package data.aggregatedModel;

public class Transition {
	
	AggregatedAction action;
	AggregatedState start;
	AggregatedState target;
	double rate ; 
	
	
	
	public Transition(){
		
	}


	public AggregatedAction getAction() {
		return action;
	}


	public void setAction(AggregatedAction action) {
		this.action = action;
	}


	public AggregatedState getStart() {
		return start;
	}


	public void setStart(AggregatedState start) {
		this.start = start;
	}


	public AggregatedState getTarget() {
		return target;
	}


	public void setTarget(AggregatedState target) {
		this.target = target;
	}


	public double getRate() {
		return rate;
	}


	public void setRate(double rate) {
		this.rate = rate;
	}
	
	

}
