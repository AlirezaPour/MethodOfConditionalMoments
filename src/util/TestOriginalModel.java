package util;

import java.util.ArrayList;

import data.originalModel.Display;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;

public class TestOriginalModel {

	
	public static void main(String[] args) {
	
		
		OriginalModel model = ClientServerModel.getClientServerModel();
		
		Display display = model.getDisplay();
		
		ArrayList<OriginalAction> actions = model.getActions();
		
		String output = display.showActions(actions);
		
		System.out.println(output);

	}

}
