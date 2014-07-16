package util;

import java.util.ArrayList;

import javax.management.modelmbean.DescriptorSupport;

import data.general.Group;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 
		int spaceBeforeDevider = 1 ;
		int spaceAfterDevider = 1 ; 

		ArrayList<Group> groups = new ArrayList<Group>();
		
		// group 1 
		LocalDerivative g1d1 = new LocalDerivative("g1-d1");
		LocalDerivative g1d2 = new LocalDerivative("g1d2");
		ArrayList<LocalDerivative> g1derivatives = new ArrayList<LocalDerivative>();
		g1derivatives.add(g1d1);
		g1derivatives.add(g1d2);
		Group g1 = new Group("group1",g1derivatives );
		
		// group 2
		LocalDerivative g2d1 = new LocalDerivative("group2d1");
		LocalDerivative g2d2 = new LocalDerivative("g2d2");
		ArrayList<LocalDerivative> g2derivatives = new ArrayList<LocalDerivative>();
		g2derivatives.add(g2d1);
		g2derivatives.add(g2d2);
		Group g2 = new Group("group2",g2derivatives );
		
		// group 3
		LocalDerivative g3d1 = new LocalDerivative("g3d1");
		LocalDerivative g3d2 = new LocalDerivative("g3d2");
		LocalDerivative g3d3 = new LocalDerivative("g3d3");
		ArrayList<LocalDerivative> g3derivatives = new ArrayList<LocalDerivative>();
		g3derivatives.add(g3d1);
		g3derivatives.add(g3d2);
		g3derivatives.add(g3d3);
		
		Group g3 = new Group("group3",g3derivatives );
		
		groups.add(g1);
		groups.add(g2);
		groups.add(g3);
				
		/*String format;
		for (Group group : groups){
			int length = (group.maxLocalDerivativeLength() + spaceBetLocDer) * group.numLocalDerivative() ; 
			format = "%-" + length + "s" ;
			System.out.printf(format,group);
			System.out.printf("|");
		}
		
		System.out.printf("\n");
		*/
		
		ArrayList<LocalDerivative> derivatives;
		int localDevSpace;
		int length;
		String format;
		for(Group group : groups){
			
			localDevSpace = group.maxLocalDerivativeLength();
			
			derivatives = group.getGroupLocalDerivatives();

			for(LocalDerivative derivative : derivatives){
				
				System.out.printf("|");
				System.out.printf(spaces(spaceAfterDevider));
				
				format = "%-"+localDevSpace+"s"; 
				
				System.out.printf(format, derivative);
				System.out.printf(spaces(spaceBeforeDevider));
				
			}
			

		}
		
		/*StateDescriptor descriptor = new StateDescriptor();
				
		String formatTitleFirstBit = "%-";
		String formatTitleDigitBit = String.format("%d",titleLength);
		String formatTitleLastBit = "s";
		String finalTitleFormat = "%-" +  titleLength + "s" ; 
			
		System.out.printf(finalTitleFormat,"variable");
		
		System.out.printf(" | ");
		
		*/
		// iterate over the state variables and put their name one by one. 
		// String formatVariableName = "%-" + stVarLength + "s" ; 
	
	}
	
	

}
