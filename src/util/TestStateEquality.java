package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.Display;
import data.general.Group;
import data.general.JumpVector;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;

public class TestStateEquality {

	public static void main(String[] args) {
		
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		// group 1 
		LocalDerivative serverIdle = new LocalDerivative("Server_idle");
		LocalDerivative serverLogging = new LocalDerivative("Server_logging");
		LocalDerivative serverBroken = new LocalDerivative("Server_broken");
		ArrayList<LocalDerivative> serverDerivatives = new ArrayList<LocalDerivative>();
		serverDerivatives.add(serverIdle);
		serverDerivatives.add(serverLogging);
		serverDerivatives.add(serverBroken);
		Group servers = new Group("Servers",serverDerivatives );
		
		// group 2
		LocalDerivative clientThinking = new LocalDerivative("Client_think");
		LocalDerivative clientRequesting = new LocalDerivative("Client_req");
		ArrayList<LocalDerivative> clientDerivatives = new ArrayList<LocalDerivative>();
		clientDerivatives.add(clientThinking);
		clientDerivatives.add(clientRequesting);
		Group clients = new Group("Clients",clientDerivatives );
		
		groups.add(servers);
		groups.add(clients);
		
		// creating the state variables
		StateVariable Si = new StateVariable(servers, serverIdle);
		StateVariable Sl = new StateVariable(servers, serverLogging);
		StateVariable Sb = new StateVariable(servers, serverBroken);
		
		StateVariable Cr = new StateVariable(clients, clientRequesting);
		StateVariable Ct = new StateVariable(clients, clientThinking);
		
		// put these state variables in a stateDescriptor
		StateDescriptor descriptor = new StateDescriptor();
		descriptor.add(Si);
		descriptor.add(Sl);
		descriptor.add(Sb);
		descriptor.add(Cr);
		descriptor.add(Ct);

		
		// setting the populations in state1 
		int popServerIdle = 12 ; 
		int popServerLog = 24; 
		int popServerBrok = 3;
		
		int popClientThink = 2600 ; 
		int popClientReq = 260;
		
		
		// creating the state1		
		AggregatedState state1 = new AggregatedState();
		
		state1.put(Si, popServerIdle);
		state1.put(Sl, popServerLog);
		state1.put(Sb, popServerBrok);
		state1.put(Ct, popClientThink);
		state1.put(Cr, popClientReq);
		state1.setStateIdentifier("state1");
		
		// setting the populations in state2 
		popServerIdle = 12 ; 
		popServerLog = 24; 
		popServerBrok = 36;
				
		popClientThink = 2600 ; 
		popClientReq = 260;
		
		// creating the state2		
		AggregatedState state2 = new AggregatedState();
				
		state2.put(Si, popServerIdle);
		state2.put(Sl, popServerLog);
		state2.put(Sb, popServerBrok);
		state2.put(Ct, popClientThink);
		state2.put(Cr, popClientReq);
		state2.setStateIdentifier("state1");

		
		// creating a list of states. this would represent a flatten state space
		ArrayList<AggregatedState> states = new ArrayList<AggregatedState>();
		states.add(state1);
		states.add(state2);
		
		// creating the action request 
		AggregatedAction request = new AggregatedAction();
		request.setName("Request");
		
		JumpVector reqJumpVector = new JumpVector();
		reqJumpVector.put(Si, -1);
		reqJumpVector.put(Sl, +1);
		reqJumpVector.put(Sb, 0);
		reqJumpVector.put(Ct, +1);
		reqJumpVector.put(Cr, -1);
		
		request.setJumpVector(reqJumpVector);
		
		JumpVector reqJumpVectorMinus = new JumpVector();
		reqJumpVectorMinus.put(Si, 1);
		reqJumpVectorMinus.put(Sl, 0);
		reqJumpVectorMinus.put(Sb, 0);
		reqJumpVectorMinus.put(Ct, 0);
		reqJumpVectorMinus.put(Cr, 1);
		
		request.setJumpVectorMinus(reqJumpVectorMinus);
		
		JumpVector reqJumpVectorPlus = new JumpVector();
		reqJumpVectorPlus.put(Si, 0);
		reqJumpVectorPlus.put(Sl, +1);
		reqJumpVectorPlus.put(Sb, 0);
		reqJumpVectorPlus.put(Ct, +1);
		reqJumpVectorPlus.put(Cr, 0);
		
		request.setJumpVectorPlus(reqJumpVectorPlus);
		
		ArrayList<AggregatedAction> actions = new ArrayList<AggregatedAction>();
		actions.add(request);
		
		// creating the action think
		
		// creating the action request 
		AggregatedAction think = new AggregatedAction();
		think.setName("think");
				
		JumpVector thinkJumpVector = new JumpVector();
		thinkJumpVector.put(Si, 0);
		thinkJumpVector.put(Sl, 0);
		thinkJumpVector.put(Sb, 0);
		thinkJumpVector.put(Ct, -1);
		thinkJumpVector.put(Cr, +1);
				
		think.setJumpVector(thinkJumpVector);
				
		JumpVector thinkJumpVectorMinus = new JumpVector();
		thinkJumpVectorMinus.put(Si, 0);
		thinkJumpVectorMinus.put(Sl, 0);
		thinkJumpVectorMinus.put(Sb, 0);
		thinkJumpVectorMinus.put(Ct, 1);
		thinkJumpVectorMinus.put(Cr, 0);
				
		think.setJumpVectorMinus(thinkJumpVectorMinus);
				
		JumpVector thinkJumpVectorPlus = new JumpVector();
		thinkJumpVectorPlus.put(Si, 0);
		thinkJumpVectorPlus.put(Sl, 0);
		thinkJumpVectorPlus.put(Sb, 0);
		thinkJumpVectorPlus.put(Ct, 0);
		thinkJumpVectorPlus.put(Cr, 1);
				
		think.setJumpVectorPlus(thinkJumpVectorPlus);
		
		actions.add(think);	
		
		// constructing the model
		AggregatedModel model = new AggregatedModel();
		model.setGroups(groups);
		model.setAggStateDescriptor(descriptor);
		model.setAggActions(actions);
		model.setAggInitialState(state1);
		Display display = new Display(model);
		model.setDisplay(display);
		
		
		
		System.out.printf(model.getDisplay().showState(state1));
		System.out.printf("\n\n\n");
		System.out.printf(model.getDisplay().showState(state2));
		System.out.printf("\n\n\n");

		// comparing state 1 and state 2
		System.out.println("comparing states");
		if (state1.equals(state2)){
			System.out.println("state1 is equal to state 2");
		}else{
			System.out.println("state1 is not equal to state 2");
		}
		System.out.printf("\n\n\n");
		Integer a = 2 ;
		Integer b = 2; 
		
		

	}

}
