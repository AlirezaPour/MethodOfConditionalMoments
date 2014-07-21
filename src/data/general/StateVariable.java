package data.general;



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
		return group.getName() + "." + localDerivative.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		StateVariable fvariable = (StateVariable) obj;
		
		if (	(this.group).equals(fvariable.getGroup()) 	&& 	(this.localDerivative).equals(fvariable.getLocalDerivative()) 	){
			return true;
		}else{
			return false;
		}
		
	}
	
	public String toStringForMatlab(){
		return group.getName() + "_" + localDerivative.getName();
	}
	
	
}