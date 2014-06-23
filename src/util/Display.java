package util;

import java.util.ArrayList;
import java.util.Iterator;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.model.Group;
import data.model.LocalDerivative;
import data.model.StateDescriptor;

public class Display {
	
	private int spaceAfterGroupDivider = 2;
	private int spaceBeforeGroupDivider = 2;
	
	private int spaceAfterLocDerDivider = 1 ; 
	private int spaceBeforeLocDerDivider = 1;
	
	
	
	private String groupDivider ="||" ;
	private String localDerDivider = "|";

	public void displayTitleOneGroup(Group group){
		
		int numLocDer = group.getGroupLocalDerivatives().size();
		int length = group.maxLocalDerivativeLength()*numLocDer + (numLocDer - 1 ) * ( spaceAfterLocDerDivider + localDerDivider.length() + spaceBeforeLocDerDivider );
		
		System.out.printf(groupDivider);
		System.out.printf(spaces(spaceAfterGroupDivider));
		
		String format = "%-"+ length  + "s" ;
		System.out.printf(format, group);
		
		System.out.printf(spaces(spaceBeforeGroupDivider));
	}
	
	public void displayTitlesManyGroups(ArrayList<Group> groups){
		
		for (Group group : groups){
			
			displayTitleOneGroup(group);
			
		}
		System.out.printf(groupDivider);
	}
	
	public void displayLocalDerivativesOfOneGroup (Group group){

		String format = "";
		int length = group.maxLocalDerivativeLength();

		LocalDerivative localDerivative;
		Iterator<LocalDerivative> iter = group.getGroupLocalDerivatives().iterator();
		
		// printing the first local derivative;
		localDerivative = iter.next();
		format = "%-" + length + "s";
		System.out.printf(format, localDerivative);
		
		// printing the later ones.
		while(iter.hasNext()){
			
			System.out.printf(spaces(spaceBeforeLocDerDivider));
			
			localDerivative = iter.next();
			
			System.out.printf(localDerDivider);
			
			System.out.printf(spaces(spaceAfterLocDerDivider));
			
			System.out.printf(format, localDerivative);
			
		}
		
	}
	
	public void displayLocalDerivativesOfManyGroups (ArrayList<Group> groups){
		for(Group group : groups){
			
			System.out.printf(groupDivider);
			System.out.printf(spaces(spaceAfterGroupDivider));
			displayLocalDerivativesOfOneGroup(group);
			System.out.printf(spaces(spaceBeforeGroupDivider));
			
		}
		System.out.printf(groupDivider);
	}
	
	public void lineUnderOneGroup(Group group){
		
		int numLocDer = group.getGroupLocalDerivatives().size();
		int length = spaceAfterGroupDivider + group.maxLocalDerivativeLength()*numLocDer + (numLocDer - 1 ) * ( spaceAfterLocDerDivider + localDerDivider.length() + spaceBeforeLocDerDivider ) + spaceAfterGroupDivider;
		String line = underline(length);
		
		System.out.printf(groupDivider);
		System.out.printf(line);
		
	}
	
	public void lineUnderManyGroups(ArrayList<Group> groups){
		for(Group group : groups){
			lineUnderOneGroup(group);
		}
		System.out.printf(groupDivider);
	}
	
	// displays the state including its headings.
	public void displayStateFull(AggregatedModel model , AggregatedState state){
		
	}
	
	public void displayStatePoulationsOnly (AggregatedModel model, AggregatedState state){
		
	}
	
	
	
	
	public String spaces(int number){
		
		String output ="" ; 
		for(int i=0 ; i < number;i++ ){
			output = output + " ";
		}
		return output;
		
	}
	
	public String underline(int length){
		String output = "";
		for (int i=0 ; i<length ; i++){
			output= output + "-";
		}
		return output;
	}
	
	public void runTest(){
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		// group 1 
		LocalDerivative g1d1 = new LocalDerivative("idle");
		LocalDerivative g1d2 = new LocalDerivative("logging");
		LocalDerivative g1d3 = new LocalDerivative("broken");
		ArrayList<LocalDerivative> g1derivatives = new ArrayList<LocalDerivative>();
		g1derivatives.add(g1d1);
		g1derivatives.add(g1d2);
		g1derivatives.add(g1d3);
		Group g1 = new Group("servers",g1derivatives );
		
		// group 2
		LocalDerivative g2d1 = new LocalDerivative("think");
		LocalDerivative g2d2 = new LocalDerivative("req");
		ArrayList<LocalDerivative> g2derivatives = new ArrayList<LocalDerivative>();
		g2derivatives.add(g2d1);
		g2derivatives.add(g2d2);
		Group g2 = new Group("clients",g2derivatives );
		
		// group 3
		/*LocalDerivative g3d1 = new LocalDerivative("564jfghn");
		LocalDerivative g3d2 = new LocalDerivative("fh");
		LocalDerivative g3d3 = new LocalDerivative("7906789067");
		ArrayList<LocalDerivative> g3derivatives = new ArrayList<LocalDerivative>();
		g3derivatives.add(g3d1);
		g3derivatives.add(g3d2);
		g3derivatives.add(g3d3);
		
		Group g3 = new Group("group3",g3derivatives );*/
		
		groups.add(g1);
		groups.add(g2);
		//groups.add(g3);
		
		lineUnderManyGroups(groups);
		System.out.printf("\n");
		displayTitlesManyGroups(groups);
		System.out.printf("\n");
		lineUnderManyGroups(groups);
		System.out.printf("\n");
		displayLocalDerivativesOfManyGroups(groups);
		System.out.printf("\n");
		lineUnderManyGroups(groups);
	}
	
	public static void main(String args[]){
		Display display = new Display(); 
		display.runTest();
	}
	
}
