package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.Display;
import data.model.Group;
import data.model.JumpVector;
import data.model.LocalDerivative;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class ClientServerAggregatedModel {
	
	/* 
	 * 
	 * The stages for defining a model and initialising the data types
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
	
	public static AggregatedModel getAggregatedClientServerModel(){
		
		double r_s = 2; 
		double r_l = 5; 
		double r_b = 7; 
		double r_f = 11;
		
		// constructing the group Servers
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		// local derivatives for group Servers.
		
		LocalDerivative serverIdle = new LocalDerivative("Server_idle");
		LocalDerivative serverLog = new LocalDerivative("Server_log");
		LocalDerivative serverBrk = new LocalDerivative("Server_brk");
		
		ArrayList<LocalDerivative> serverDerivatives = new ArrayList<LocalDerivative>();
		serverDerivatives.add(serverIdle);
		serverDerivatives.add(serverLog);
		serverDerivatives.add(serverBrk);
		
		Group servers = new Group("Servers",serverDerivatives);
		
		// constructing group Clients
		//LocalDerivative clientThinking = new LocalDerivative("Client_think");
		//LocalDerivative clientRequesting = new LocalDerivative("Client_req");
		//ArrayList<LocalDerivative> clientDerivatives = new ArrayList<LocalDerivative>();
		//clientDerivatives.add(clientThinking);
		//clientDerivatives.add(clientRequesting);
		//Group clients = new Group("Clients",clientDerivatives );
		
		groups.add(servers);
		//groups.add(clients);
		
		// creating the state variables related to the servers.
		StateVariable Si = new StateVariable(servers, serverIdle);
		StateVariable Sl = new StateVariable(servers, serverLog);
		StateVariable Sb = new StateVariable(servers, serverBrk);
		
		// creating the state variables related to the clients.
		//StateVariable Cr = new StateVariable(clients, clientReq);
		//StateVariable Ct = new StateVariable(clients, clientThinking);
		
		// put these state variables in a stateDescriptor
		StateDescriptor descriptor = new StateDescriptor();
		descriptor.add(Si);
		descriptor.add(Sl);
		descriptor.add(Sb);
		//descriptor.add(Cr);
		//descriptor.add(Ct);

		// setting the populations in initial state 
		int popServerIdle = 2 ; 
		int popServerLog = 0; 
		int popServerBrok = 0;
		
		//int popClientThink = 2600 ; 
		//int popClientReq = 260;
		
		
		// creating the initial state		
		AggregatedState initialState = new AggregatedState();
		
		initialState.put(Si, popServerIdle);
		initialState.put(Sl, popServerLog);
		initialState.put(Sb, popServerBrok);
		//initialState.put(Ct, popClientThink);
		//initialState.put(Cr, popClientReq);
		initialState.setStateIdentifier("initial state");
		
		// constructing the actions.
		ArrayList<AggregatedAction> actions = new ArrayList<AggregatedAction>();
		
		// creating the action request 
		AggregatedAction request = new AggregatedAction();
		request.setName("request");
		
		JumpVector reqJumpVector = new JumpVector();
		reqJumpVector.put(Si, -1);
		reqJumpVector.put(Sl, +1);
		reqJumpVector.put(Sb, 0);
		//reqJumpVector.put(Ct, +1);
		//reqJumpVector.put(Cr, -1);
		
		request.setJumpVector(reqJumpVector);
		
		JumpVector reqJumpVectorMinus = new JumpVector();
		reqJumpVectorMinus.put(Si, 1);
		reqJumpVectorMinus.put(Sl, 0);
		reqJumpVectorMinus.put(Sb, 0);
		//reqJumpVectorMinus.put(Ct, 0);
		//reqJumpVectorMinus.put(Cr, 1);
		
		request.setJumpVectorMinus(reqJumpVectorMinus);
		
		JumpVector reqJumpVectorPlus = new JumpVector();
		reqJumpVectorPlus.put(Si, 0);
		reqJumpVectorPlus.put(Sl, +1);
		reqJumpVectorPlus.put(Sb, 0);
		//reqJumpVectorPlus.put(Ct, +1);
		//reqJumpVectorPlus.put(Cr, 0);
		
		request.setJumpVectorPlus(reqJumpVectorPlus);
		
		actions.add(request);
		
		
		// creating the action log 
		AggregatedAction log = new AggregatedAction();
		log.setName("log");
				
		JumpVector logJumpVector = new JumpVector();
		logJumpVector.put(Si, +1);
		logJumpVector.put(Sl, -1);
		logJumpVector.put(Sb, 0);
		//reqJumpVector.put(Ct, +1);
		//reqJumpVector.put(Cr, -1);
				
		log.setJumpVector(logJumpVector);
			
		JumpVector logJumpVectorMinus = new JumpVector();
		logJumpVectorMinus.put(Si, 0);
		logJumpVectorMinus.put(Sl, 1);
		logJumpVectorMinus.put(Sb, 0);
		//reqJumpVectorMinus.put(Ct, 0);
		//reqJumpVectorMinus.put(Cr, 1);
			
		log.setJumpVectorMinus(logJumpVectorMinus);
		
		JumpVector logJumpVectorPlus = new JumpVector();
		logJumpVectorPlus.put(Si, 1);
		logJumpVectorPlus.put(Sl, 0);
		logJumpVectorPlus.put(Sb, 0);
		//reqJumpVectorPlus.put(Ct, +1);
		//reqJumpVectorPlus.put(Cr, 0);
				
		log.setJumpVectorPlus(logJumpVectorPlus);
			
		actions.add(log);
		
		// creating the action brk 
		AggregatedAction brk = new AggregatedAction();
		brk.setName("break");
				
		JumpVector brkJumpVector = new JumpVector();
		brkJumpVector.put(Si, -1);
		brkJumpVector.put(Sl, 0);
		brkJumpVector.put(Sb, +1);
		//reqJumpVector.put(Ct, +1);
		//reqJumpVector.put(Cr, -1);
				
		brk.setJumpVector(brkJumpVector);
			
		JumpVector brkJumpVectorMinus = new JumpVector();
		brkJumpVectorMinus.put(Si, 1);
		brkJumpVectorMinus.put(Sl, 0);
		brkJumpVectorMinus.put(Sb, 0);
		//reqJumpVectorMinus.put(Ct, 0);
		//reqJumpVectorMinus.put(Cr, 1);
			
		brk.setJumpVectorMinus(brkJumpVectorMinus);
		
		JumpVector brkJumpVectorPlus = new JumpVector();
		brkJumpVectorPlus.put(Si, 0);
		brkJumpVectorPlus.put(Sl, 0);
		brkJumpVectorPlus.put(Sb, 1);
		//reqJumpVectorPlus.put(Ct, +1);
		//reqJumpVectorPlus.put(Cr, 0);
				
		brk.setJumpVectorPlus(brkJumpVectorPlus);
			
		actions.add(brk);
		
		// creating the action fix 
		AggregatedAction fix = new AggregatedAction();
		fix.setName("fix");
				
		JumpVector fixJumpVector = new JumpVector();
		fixJumpVector.put(Si, +1);
		fixJumpVector.put(Sl, 0);
		fixJumpVector.put(Sb, -1);
		//reqJumpVector.put(Ct, +1);
		//reqJumpVector.put(Cr, -1);
				
		fix.setJumpVector(fixJumpVector);
			
		JumpVector fixJumpVectorMinus = new JumpVector();
		fixJumpVectorMinus.put(Si, 0);
		fixJumpVectorMinus.put(Sl, 0);
		fixJumpVectorMinus.put(Sb, 1);
		//reqJumpVectorMinus.put(Ct, 0);
		//reqJumpVectorMinus.put(Cr, 1);
			
		fix.setJumpVectorMinus(fixJumpVectorMinus);
		
		JumpVector fixJumpVectorPlus = new JumpVector();
		fixJumpVectorPlus.put(Si, 1);
		fixJumpVectorPlus.put(Sl, 0);
		fixJumpVectorPlus.put(Sb, 0);
		//reqJumpVectorPlus.put(Ct, +1);
		//reqJumpVectorPlus.put(Cr, 0);
				
		fix.setJumpVectorPlus(fixJumpVectorPlus);
			
		actions.add(fix);
		
		// setting the apparent rate data. For each local derivative and action type the derivative enables, specify the rate at which the action is enabled.
		
		// local derivative Server_idle and the action request
		serverIdle.getActionRates().put(request, r_s);
		
		// local derivative Server_idle and the action break
		serverIdle.getActionRates().put(brk, r_b);
		
		// local derivative Server_logging
		serverLog.getActionRates().put(log, r_l);
		
		// local derivative Server_broken
		serverBrk.getActionRates().put(fix, r_f);
			
		// constructing the model
		AggregatedModel model = new AggregatedModel();
		model.setGroups(groups);
		model.setAggStateDescriptor(descriptor);
		model.setAggActions(actions);
		model.setAggInitialState(initialState);
		Display display = new Display(model);
		model.setDisplay(display);
	
		return model;
	}

	public static void main(String args[]){
		AggregatedModel model = getAggregatedClientServerModel();
		Display display = model.getDisplay();
		
		//System.out.printf(display.showStateDescriptor());
		
		//System.out.printf("\n\n\n");
		
		//System.out.printf(display.showState(model.getAggInitialState()));
		
		//System.out.printf("\n\n\n");
		
		//System.out.printf(display.showActions(model.getAggActions()));
		
		System.out.printf(display.showModel());
		
	}
}
