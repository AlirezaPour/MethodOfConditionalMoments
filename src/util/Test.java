package util;

import java.util.ArrayList;

import javax.management.modelmbean.DescriptorSupport;

import data.model.Group;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int stVarLength = 10 ;
		int titleLength = 8  ;
		

		ArrayList<Group> groups = null;
		int groupNumLocDeriv ;
		
		String format;
		for (Group group : groups){
			int length = group.maxLocalDerivativeLength() * group.numLocalDerivative() ; 
			format = "%-" + length + "s" ;
			System.out.printf(,);
		}
		
		StateDescriptor descriptor = new StateDescriptor();
	
		
				
		String formatTitleFirstBit = "%-";
		String formatTitleDigitBit = String.format("%d",titleLength);
		String formatTitleLastBit = "s";
		String finalTitleFormat = "%-" +  titleLength + "s" ; 
			
		System.out.printf(finalTitleFormat,"variable");
		
		System.out.printf(" | ");
		
		// iterate over the state variables and put their name one by one. 
		String formatVariableName = "%-" + stVarLength + "s" ; 
	
		
		
		
		
		
		
	}

}
