package matlab.conditionalExpectation;

public class VariableIndexer {
	
	private int index ;
	
	public int getIndex(){
		return index;
	}
	
	public VariableIndexer(int index){
		this.index = index;
	}
	
	public void increaseIndex (){
		index ++ ; 
	}

	public void decreaseIndex(){
		index = index - 1 ; 
	}
}
