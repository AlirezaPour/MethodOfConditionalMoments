package data.model;


public class StateVariable {

	private Group group; 
	private LocalDerivative localDerivative;
	
	public StateVariable (Group group, LocalDerivative derivative){
		this.group = group;
		this.localDerivative = derivative;
	}
	
	public Group getGroup(){
		return this.group;
	}
	
	public void setGroup(Group group){
		this.group = group; 
	}
	
	public LocalDerivative getLocalDerivative(){
		return this.localDerivative;
	}
	
	public void setLocalDerivative(LocalDerivative localDerivative){
		this.localDerivative = localDerivative ;
	}
	
	public String toString(){
		return localDerivative.toString();
	}
}