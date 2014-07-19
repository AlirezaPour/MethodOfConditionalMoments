package data.general;



public class Transition {
	
	Action action;
	State start;
	public State target;
	double rate ; 
	
	public Transition(){
		
	}


	public Action getAction() {
		return action;
	}


	public void setAction(Action action) {
		this.action = action;
	}


	public State getStart() {
		return start;
	}


	public void setStart(State start) {
		this.start = start;
	}


	public State getTarget() {
		return target;
	}


	public void setTarget(State target) {
		this.target = target;
	}


	public double getRate() {
		return rate;
	}


	public void setRate(double rate) {
		this.rate = rate;
	}
	
	

}
