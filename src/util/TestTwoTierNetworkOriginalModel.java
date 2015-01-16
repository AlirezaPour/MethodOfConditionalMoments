package util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import data.originalModel.Display;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;

public class TestTwoTierNetworkOriginalModel {

	public static void main(String[] args) {
		
		OriginalModel model = TwoTierNetworkOriginalModel.getTwoTierNetworkModel();
		
		Display display = model.getDisplay();
		
		ArrayList<OriginalAction> actions = model.getActions();
		
		String output = display.showModel();
		
		try {
			PrintWriter out = new PrintWriter("mode_description.txt");
			out.println(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not present the model. "); 
			e.printStackTrace();
		}
		
		
//		System.out.println(output);


	}

}
