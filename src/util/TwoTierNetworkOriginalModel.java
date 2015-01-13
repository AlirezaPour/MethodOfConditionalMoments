package util;

import java.util.ArrayList;
import java.util.HashMap;

import data.general.Group;
import data.general.JumpVector;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;
import data.originalModel.Display;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;
import data.originalModel.OriginalState;

public class TwoTierNetworkOriginalModel {

	
	public OriginalModel getTwoTierNetworkModel(){
		

		// the constants
		HashMap<String, Double> constants = new HashMap<String,Double>();
		
		// near users
		constants.put("r_near_process", 5.0);
		constants.put("r_send_macro_nr", 15.0);
		constants.put("r_send_macro_po", 22.0);
		
		
		// macro-cell
		constants.put("r_stream_wcu_femto_1_macro_nr", 0.2);
		constants.put("r_stream_wcu_femto_2_macro_po", 0.2);

		// users near femto-1
		constants.put("r_process_scu_femto_1", 5.0);
		
		constants.put("r_send_femto_1_nr", 20.0);
		constants.put("r_send_femto_1_po", 10.0);
		 
		constants.put("r_wcu_femto_1_process", 0.1);
		
		
		// users near femto-2
		constants.put("r_process_scu_femto_2", 5.0);
		
		constants.put("r_send_femto_2_nr", 20.0);
		constants.put("r_send_femto_2_po", 10.0);
		 
		constants.put("r_wcu_femto_2_process", 0.1);
		
		
		// femto-1
		constants.put("r_stream_femto_1_nr", 2.5);
		constants.put("r_stream_femto_1_po", 1.5);
		
		// femto-2
		constants.put("r_stream_femto_2_nr", 2.5);
		constants.put("r_stream_femto_2_po", 1.5);
		
		// control
		constants.put("r_control", 100000000.0);
		
		constants.put("passive",1000.0);
		
		
		
		
		
		// near users
		double r_near_process=  5.0 ;
		double r_send_macro_nr = 15.0;
		double r_send_macro_po= 22.0;

		// macro-cell
		double r_stream_wcu_femto_1_macro_nr=  0.2;
		double r_stream_wcu_femto_2_macro_po=  0.2;

		// users near femto-1
		double r_process_scu_femto_1 = 5.0;

		double r_send_femto_1_nr = 20.0 ;
		double r_send_femto_1_po = 10.0 ;

		double r_wcu_femto_1_process= 0.1;

		// users near femto-2
		double r_process_scu_femto_2 =  5.0;

		double r_send_femto_2_nr=  20.0;
		double r_send_femto_2_po =  10.0;

		double r_wcu_femto_2_process = 0.1;

		// femto-1
		double r_stream_femto_1_nr =  2.5;
		double r_stream_femto_1_po =  1.5;

		// femto-2
		double r_stream_femto_2_nr = 2.5;
		double r_stream_femto_2_po = 1.5;

		// control
		double r_control =  100000000.0;
		
		// passive rates
		double passive = 10000.0;

		///////////////////////////////////////////////
		// population parameters
		////////////////////////////////////////////////
		
		// near users
		int number_user_near = 200 ;
		
		// users at femto-1
		int number_scu_femto_1 = 30; 
		int number_wcu_femto_1 = 2;

		// users at femto-2
		int number_scu_femto_2 = 30 ; 
		int number_wcu_femto_2 = 2;
		
		// macro-cell
		int number_channel_macro=35 ; 
		
		// femto-cell 1 
		int number_channel_femto_1 = 5; 
		
		// femto-cell 2
		int number_channel_femto_2 = 5;
		
		
		// initialising local derivates
		ArrayList<LocalDerivative> localDerivatives = new ArrayList<LocalDerivative>();



		ArrayList<Group> groups = new ArrayList<Group>();
		ArrayList<Group> smallGroups = new ArrayList<Group>();
		ArrayList<Group> largeGroups = new ArrayList<Group>();		

		// for each group  
		// specify local derivatives
		// add to the models set of local derivatives
		// define the group with its local derivatives 
		// specify whether the group is related to the small or large ones. 
		// define the state variables
		
		///////////////////////////////////////////////
		//////// Group : Near users
		//////////////////////////////////////////////

		LocalDerivative user_near_processing = new LocalDerivative("user_near_processing");
		localDerivatives.add(user_near_processing);

		LocalDerivative user_near_sending = new LocalDerivative("user_near_sending");
		localDerivatives.add(user_near_sending);
		
		ArrayList<LocalDerivative> near_user_derivatives = new ArrayList<LocalDerivative>();
		near_user_derivatives.add(user_near_processing);
		near_user_derivatives.add(user_near_sending);

		Group near_users = new Group("near_users",near_user_derivatives);
		groups.add(near_users);
		largeGroups.add(near_users);

		// creating the state variables related to the servers.
		StateVariable var_user_near_sending = new StateVariable(near_users, user_near_sending);
		StateVariable var_user_near_processing = new StateVariable(near_users, user_near_processing);
		
		
		///////////////////////////////////////////////
		//////// Group : ldcq
		//////////////////////////////////////////////

		LocalDerivative ldcq_0 = new LocalDerivative("ldcq_0");
		localDerivatives.add(ldcq_0);

		LocalDerivative ldcq_1 = new LocalDerivative("ldcq_1");
		localDerivatives.add(ldcq_1);
		
		ArrayList<LocalDerivative> ldcq_derivatives = new ArrayList<LocalDerivative>();
		ldcq_derivatives.add(ldcq_0);
		ldcq_derivatives.add(ldcq_1);

		Group ldcq = new Group("ldcq",ldcq_derivatives);
		groups.add(ldcq);
		smallGroups.add(ldcq);

		// creating the state variables related to the servers.
		StateVariable var_ldcq_0 = new StateVariable(ldcq, ldcq_0);
		StateVariable var_ldcq_1 = new StateVariable(ldcq, ldcq_1);
		
		
		
		///////////////////////////////////////////////
		//////// Group : Ch_macro
		//////////////////////////////////////////////

		LocalDerivative ch_macro_idle = new LocalDerivative("ch_macro_idle");
		localDerivatives.add(ch_macro_idle);
	
		ArrayList<LocalDerivative> ch_macro_derivatives = new ArrayList<LocalDerivative>();
		ch_macro_derivatives.add(ch_macro_idle);

		Group ch_macro = new Group("ch_macro",ch_macro_derivatives);
		groups.add(ch_macro);
		smallGroups.add(ch_macro);

		// creating the state variables related to the servers.
		StateVariable var_ch_macro_idle = new StateVariable(ch_macro, ch_macro_idle);
		

		///////////////////////////////////////////////
		//////// Group : scu_femto_1
		//////////////////////////////////////////////

		LocalDerivative scu_femto_1_processing = new LocalDerivative("scu_femto_1_processing");
		localDerivatives.add(scu_femto_1_processing);
		
		LocalDerivative scu_femto_1_sending = new LocalDerivative("scu_femto_1_sending");
		localDerivatives.add(scu_femto_1_sending);

		ArrayList<LocalDerivative> scu_femto_1_derivatives = new ArrayList<LocalDerivative>();
		scu_femto_1_derivatives.add(scu_femto_1_processing);
		scu_femto_1_derivatives.add(scu_femto_1_sending);

		Group scu_femto_1 = new Group("scu_femto_1",scu_femto_1_derivatives);
		groups.add(scu_femto_1);
		largeGroups.add(scu_femto_1);

		// creating the state variables related to the servers.
		StateVariable var_scu_femto_1_processing = new StateVariable(scu_femto_1, scu_femto_1_processing);
		StateVariable var_scu_femto_1_sending = new StateVariable(scu_femto_1, scu_femto_1_sending);
		
		
		///////////////////////////////////////////////
		//////// Group : wcu_femto_1
		//////////////////////////////////////////////

		LocalDerivative wcu_femto_1_processing = new LocalDerivative("wcu_femto_1_processing");
		localDerivatives.add(wcu_femto_1_processing);

		LocalDerivative wcu_femto_1_waiting_local = new LocalDerivative("wcu_femto_1_waiting_local");
		localDerivatives.add(wcu_femto_1_waiting_local);
		
		LocalDerivative wcu_femto_1_streaming_local = new LocalDerivative("wcu_femto_1_streaming_local");
		localDerivatives.add(wcu_femto_1_streaming_local);
		
		LocalDerivative wcu_femto_1_waiting_macro = new LocalDerivative("wcu_femto_1_waiting_macro");
		localDerivatives.add(wcu_femto_1_waiting_macro);
		
		LocalDerivative wcu_femto_1_streaming_macro = new LocalDerivative("wcu_femto_1_streaming_macro");
		localDerivatives.add(wcu_femto_1_streaming_macro);

		ArrayList<LocalDerivative> wcu_femto_1_derivatives = new ArrayList<LocalDerivative>();
		wcu_femto_1_derivatives.add(wcu_femto_1_processing);
		wcu_femto_1_derivatives.add(wcu_femto_1_waiting_local);
		wcu_femto_1_derivatives.add(wcu_femto_1_streaming_local);
		wcu_femto_1_derivatives.add(wcu_femto_1_waiting_macro);
		wcu_femto_1_derivatives.add(wcu_femto_1_streaming_macro);


		Group wcu_femto_1 = new Group("wcu_femto_1",wcu_femto_1_derivatives);
		groups.add(wcu_femto_1);
		smallGroups.add(wcu_femto_1);

		StateVariable var_wcu_femto_1_processing = new StateVariable(wcu_femto_1, wcu_femto_1_processing);
		StateVariable var_wcu_femto_1_waiting_local = new StateVariable(wcu_femto_1, wcu_femto_1_waiting_local);
		StateVariable var_wcu_femto_1_streaming_local = new StateVariable(wcu_femto_1, wcu_femto_1_streaming_local);
		StateVariable var_wcu_femto_1_waiting_macro = new StateVariable(wcu_femto_1, wcu_femto_1_waiting_macro);
		StateVariable var_wcu_femto_1_streaming_macro = new StateVariable(wcu_femto_1, wcu_femto_1_streaming_macro);
		

		/////////////////////////////////////////////////
		///// Group : ch_femto_1
		////////////////////////////////////////////////
		
		LocalDerivative ch_femto_1_idle = new LocalDerivative("ch_femto_1_idle");
		localDerivatives.add(ch_femto_1_idle);
	
		ArrayList<LocalDerivative> ch_femto_1_derivatives = new ArrayList<LocalDerivative>();
		ch_femto_1_derivatives.add(ch_femto_1_idle);

		Group ch_femto_1 = new Group("ch_femto_1",ch_femto_1_derivatives);
		groups.add(ch_femto_1);
		smallGroups.add(ch_femto_1);


		StateVariable var_ch_femto_1_idle = new StateVariable(ch_femto_1, ch_femto_1_idle);
		
		
		/////////////////////////////////////////////////
		///// Group : wcuq_femto_1
		////////////////////////////////////////////////
		
		LocalDerivative wcuq_femto_1_0 = new LocalDerivative("wcuq_femto_1_0");
		localDerivatives.add(wcuq_femto_1_0);

		LocalDerivative wcuq_femto_1_1 = new LocalDerivative("wcuq_femto_1_1");
		localDerivatives.add(wcuq_femto_1_1);
		
		ArrayList<LocalDerivative> wcuq_femto_1_derivatives = new ArrayList<LocalDerivative>();
		wcuq_femto_1_derivatives.add(wcuq_femto_1_0);
		wcuq_femto_1_derivatives.add(wcuq_femto_1_1);

		Group wcuq_femto_1 = new Group("wcuq_femto_1",wcuq_femto_1_derivatives);
		groups.add(wcuq_femto_1);
		smallGroups.add(wcuq_femto_1);


		StateVariable var_wcuq_femto_1_0 = new StateVariable(wcuq_femto_1, wcuq_femto_1_0);
		StateVariable var_wcuq_femto_1_1 = new StateVariable(wcuq_femto_1, wcuq_femto_1_1);
		

		///////////////////////////////////////////////
		//////// Group : scu_femto_2
		//////////////////////////////////////////////

		LocalDerivative scu_femto_2_processing = new LocalDerivative("scu_femto_2_processing");
		localDerivatives.add(scu_femto_2_processing);

		LocalDerivative scu_femto_2_sending = new LocalDerivative("scu_femto_2_sending");
		localDerivatives.add(scu_femto_2_sending);

		ArrayList<LocalDerivative> scu_femto_2_derivatives = new ArrayList<LocalDerivative>();
		scu_femto_2_derivatives.add(scu_femto_2_processing);
		scu_femto_2_derivatives.add(scu_femto_2_sending);

		Group scu_femto_2 = new Group("scu_femto_2",scu_femto_2_derivatives);
		groups.add(scu_femto_2);
		largeGroups.add(scu_femto_2);

		// creating the state variables related to the servers.
		StateVariable var_scu_femto_2_processing = new StateVariable(scu_femto_2, scu_femto_2_processing);
		StateVariable var_scu_femto_2_sending = new StateVariable(scu_femto_2, scu_femto_2_sending);


		///////////////////////////////////////////////
		//////// Group : wcu_femto_2
		//////////////////////////////////////////////

		LocalDerivative wcu_femto_2_processing = new LocalDerivative("wcu_femto_2_processing");
		localDerivatives.add(wcu_femto_2_processing);

		LocalDerivative wcu_femto_2_waiting_local = new LocalDerivative("wcu_femto_2_waiting_local");
		localDerivatives.add(wcu_femto_2_waiting_local);

		LocalDerivative wcu_femto_2_streaming_local = new LocalDerivative("wcu_femto_2_streaming_local");
		localDerivatives.add(wcu_femto_2_streaming_local);

		LocalDerivative wcu_femto_2_waiting_macro = new LocalDerivative("wcu_femto_2_waiting_macro");
		localDerivatives.add(wcu_femto_2_waiting_macro);

		LocalDerivative wcu_femto_2_streaming_macro = new LocalDerivative("wcu_femto_2_streaming_macro");
		localDerivatives.add(wcu_femto_2_streaming_macro);

		ArrayList<LocalDerivative> wcu_femto_2_derivatives = new ArrayList<LocalDerivative>();
		wcu_femto_2_derivatives.add(wcu_femto_2_processing);
		wcu_femto_2_derivatives.add(wcu_femto_2_waiting_local);
		wcu_femto_2_derivatives.add(wcu_femto_2_streaming_local);
		wcu_femto_2_derivatives.add(wcu_femto_2_waiting_macro);
		wcu_femto_2_derivatives.add(wcu_femto_2_streaming_macro);


		Group wcu_femto_2 = new Group("wcu_femto_2",wcu_femto_2_derivatives);
		groups.add(wcu_femto_2);
		smallGroups.add(wcu_femto_2);

		StateVariable var_wcu_femto_2_processing = new StateVariable(wcu_femto_2, wcu_femto_2_processing);
		StateVariable var_wcu_femto_2_waiting_local = new StateVariable(wcu_femto_2, wcu_femto_2_waiting_local);
		StateVariable var_wcu_femto_2_streaming_local = new StateVariable(wcu_femto_2, wcu_femto_2_streaming_local);
		StateVariable var_wcu_femto_2_waiting_macro = new StateVariable(wcu_femto_2, wcu_femto_2_waiting_macro);
		StateVariable var_wcu_femto_2_streaming_macro = new StateVariable(wcu_femto_2, wcu_femto_2_streaming_macro);


		/////////////////////////////////////////////////
		///// Group : ch_femto_2
		////////////////////////////////////////////////

		LocalDerivative ch_femto_2_idle = new LocalDerivative("ch_femto_2_idle");
		localDerivatives.add(ch_femto_2_idle);

		ArrayList<LocalDerivative> ch_femto_2_derivatives = new ArrayList<LocalDerivative>();
		ch_femto_2_derivatives.add(ch_femto_2_idle);

		Group ch_femto_2 = new Group("ch_femto_2",ch_femto_2_derivatives);
		groups.add(ch_femto_2);
		smallGroups.add(ch_femto_2);


		StateVariable var_ch_femto_2_idle = new StateVariable(ch_femto_2, ch_femto_2_idle);


		/////////////////////////////////////////////////
		///// Group : wcuq_femto_2
		////////////////////////////////////////////////

		LocalDerivative wcuq_femto_2_0 = new LocalDerivative("wcuq_femto_2_0");
		localDerivatives.add(wcuq_femto_2_0);

		LocalDerivative wcuq_femto_2_1 = new LocalDerivative("wcuq_femto_2_1");
		localDerivatives.add(wcuq_femto_2_1);

		ArrayList<LocalDerivative> wcuq_femto_2_derivatives = new ArrayList<LocalDerivative>();
		wcuq_femto_2_derivatives.add(wcuq_femto_2_0);
		wcuq_femto_2_derivatives.add(wcuq_femto_2_1);

		Group wcuq_femto_2 = new Group("wcuq_femto_2",wcuq_femto_2_derivatives);
		groups.add(wcuq_femto_2);
		smallGroups.add(wcuq_femto_2);


		StateVariable var_wcuq_femto_2_0 = new StateVariable(wcuq_femto_2, wcuq_femto_2_0);
		StateVariable var_wcuq_femto_2_1 = new StateVariable(wcuq_femto_2, wcuq_femto_2_1);

		// put these state variables in a stateDescriptor
		// state descriptor defines an order 
		// the first (logical) half, is for the state variables related to the small groups,
		// and the second (logical) half is for the state variables related to the large groups. 
		
		StateDescriptor descriptor = new StateDescriptor();

		// small groups
		descriptor.add(var_ldcq_0);
		descriptor.add(var_ldcq_1);
		
		descriptor.add(var_ch_macro_idle);
		
		descriptor.add(var_wcu_femto_1_processing);
		descriptor.add(var_wcu_femto_1_waiting_local);
		descriptor.add(var_wcu_femto_1_streaming_local);
		descriptor.add(var_wcu_femto_1_waiting_macro);
		descriptor.add(var_wcu_femto_1_streaming_macro);
		
		descriptor.add(var_ch_femto_1_idle);
		
		descriptor.add(var_wcuq_femto_1_0);
		descriptor.add(var_wcuq_femto_1_1);
		
		descriptor.add(var_wcu_femto_2_processing);
		descriptor.add(var_wcu_femto_2_waiting_local);
		descriptor.add(var_wcu_femto_2_streaming_local);
		descriptor.add(var_wcu_femto_2_waiting_macro );
		descriptor.add(var_wcu_femto_2_streaming_macro);
		
		descriptor.add(var_ch_femto_2_idle);
		
		descriptor.add(var_wcuq_femto_2_0);
		descriptor.add(var_wcuq_femto_2_1 );		
		

		// large groups
		descriptor.add(var_user_near_sending);
		descriptor.add(var_user_near_processing);
		
		descriptor.add(var_scu_femto_1_processing);
		descriptor.add(var_scu_femto_1_sending);
		
		descriptor.add(var_scu_femto_2_processing);
		descriptor.add(var_scu_femto_2_sending);
		

		/////////////////////////////////////////////////
		// creating the initial state		
		/////////////////////////////////////////////////
		
		OriginalState initialState = new OriginalState();

		// for each of the state variables, specify its initial value
		
		initialState.put(var_user_near_sending,0); 
		initialState.put(var_user_near_processing,number_user_near);
		initialState.put(var_ldcq_0,1);
		initialState.put(var_ldcq_1,0);
		initialState.put(var_ch_macro_idle,number_channel_macro);
		initialState.put(var_scu_femto_1_processing,number_scu_femto_1);
		initialState.put(var_scu_femto_1_sending,0);
		initialState.put(var_wcu_femto_1_processing,number_wcu_femto_1);
		initialState.put(var_wcu_femto_1_waiting_local,0);
		initialState.put(var_wcu_femto_1_streaming_local,0);
		initialState.put(var_wcu_femto_1_waiting_macro,0);
		initialState.put(var_wcu_femto_1_streaming_macro,0);
		initialState.put(var_ch_femto_1_idle,number_channel_femto_1);
		initialState.put(var_wcuq_femto_1_0,1);
		initialState.put(var_wcuq_femto_1_1,0);
		initialState.put(var_scu_femto_2_processing,number_scu_femto_2);
		initialState.put(var_scu_femto_2_sending,0);
		initialState.put(var_wcu_femto_2_processing,0);
		initialState.put(var_wcu_femto_2_waiting_local,0);
		initialState.put(var_wcu_femto_2_streaming_local,0);
		initialState.put(var_wcu_femto_2_waiting_macro,0);
		initialState.put(var_wcu_femto_2_streaming_macro,0);
		initialState.put(var_ch_femto_2_idle,number_channel_femto_2);
		initialState.put(var_wcuq_femto_2_0,1);
		initialState.put(var_wcuq_femto_2_1,0);
		
		initialState.setStateIdentifier("initial state");

		//////////////////////////////////////////
		// constructing the actions.
		/////////////////////////////////////////
		ArrayList<OriginalAction> actions = new ArrayList<OriginalAction>();

		// creating the action request 
		OriginalAction request = new OriginalAction();
		request.setName("request");

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

		actions.add(request);


		// creating the action log 
		OriginalAction log = new OriginalAction();
		log.setName("log");

		JumpVector logJumpVector = new JumpVector();
		logJumpVector.put(Si, +1);
		logJumpVector.put(Sl, -1);
		logJumpVector.put(Sb, 0);
		logJumpVector.put(Ct, 0);
		logJumpVector.put(Cr, 0);

		log.setJumpVector(logJumpVector);

		JumpVector logJumpVectorMinus = new JumpVector();
		logJumpVectorMinus.put(Si, 0);
		logJumpVectorMinus.put(Sl, 1);
		logJumpVectorMinus.put(Sb, 0);
		logJumpVectorMinus.put(Ct, 0);
		logJumpVectorMinus.put(Cr, 0);

		log.setJumpVectorMinus(logJumpVectorMinus);

		JumpVector logJumpVectorPlus = new JumpVector();
		logJumpVectorPlus.put(Si, 1);
		logJumpVectorPlus.put(Sl, 0);
		logJumpVectorPlus.put(Sb, 0);
		logJumpVectorPlus.put(Ct, 0);
		logJumpVectorPlus.put(Cr, 0);

		log.setJumpVectorPlus(logJumpVectorPlus);

		actions.add(log);

		// creating the action brk 
		OriginalAction brk = new OriginalAction();
		brk.setName("break");

		JumpVector brkJumpVector = new JumpVector();
		brkJumpVector.put(Si, -1);
		brkJumpVector.put(Sl, 0);
		brkJumpVector.put(Sb, +1);
		brkJumpVector.put(Ct, 0);
		brkJumpVector.put(Cr, 0);

		brk.setJumpVector(brkJumpVector);

		JumpVector brkJumpVectorMinus = new JumpVector();
		brkJumpVectorMinus.put(Si, 1);
		brkJumpVectorMinus.put(Sl, 0);
		brkJumpVectorMinus.put(Sb, 0);
		brkJumpVectorMinus.put(Ct, 0);
		brkJumpVectorMinus.put(Cr, 0);

		brk.setJumpVectorMinus(brkJumpVectorMinus);

		JumpVector brkJumpVectorPlus = new JumpVector();
		brkJumpVectorPlus.put(Si, 0);
		brkJumpVectorPlus.put(Sl, 0);
		brkJumpVectorPlus.put(Sb, 1);
		brkJumpVectorPlus.put(Ct, 0);
		brkJumpVectorPlus.put(Cr, 0);

		brk.setJumpVectorPlus(brkJumpVectorPlus);

		actions.add(brk);

		// creating the action fix 
		OriginalAction fix = new OriginalAction();
		fix.setName("fix");

		JumpVector fixJumpVector = new JumpVector();
		fixJumpVector.put(Si, +1);
		fixJumpVector.put(Sl, 0);
		fixJumpVector.put(Sb, -1);
		fixJumpVector.put(Ct, 0);
		fixJumpVector.put(Cr, 0);

		fix.setJumpVector(fixJumpVector);

		JumpVector fixJumpVectorMinus = new JumpVector();
		fixJumpVectorMinus.put(Si, 0);
		fixJumpVectorMinus.put(Sl, 0);
		fixJumpVectorMinus.put(Sb, 1);
		fixJumpVectorMinus.put(Ct, 0);
		fixJumpVectorMinus.put(Cr, 0);

		fix.setJumpVectorMinus(fixJumpVectorMinus);

		JumpVector fixJumpVectorPlus = new JumpVector();
		fixJumpVectorPlus.put(Si, 1);
		fixJumpVectorPlus.put(Sl, 0);
		fixJumpVectorPlus.put(Sb, 0);
		fixJumpVectorPlus.put(Ct, 0);
		fixJumpVectorPlus.put(Cr, 0);

		fix.setJumpVectorPlus(fixJumpVectorPlus);

		actions.add(fix);

		// creating action think
		OriginalAction think = new OriginalAction();
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
		thinkJumpVectorPlus.put(Cr, +1);

		think.setJumpVectorPlus(thinkJumpVectorPlus);

		actions.add(think);


		// setting the apparent rate data. For each local derivative and action type the derivative enables, specify the rate at which the action is enabled.

		// local derivative Server_idle and the action request
		serverIdle.getActionRates().put(request, r_s);
		serverIdle.getParameterNames().put(request, "r_s");

		// local derivative Server_idle and the action break
		serverIdle.getActionRates().put(brk, r_b);
		serverIdle.getParameterNames().put(brk, "r_b");

		// local derivative Server_logging
		serverLog.getActionRates().put(log, r_l);
		serverLog.getParameterNames().put(log, "r_l");

		// local derivative Server_broken
		serverBrk.getActionRates().put(fix, r_f);
		serverBrk.getParameterNames().put(fix, "r_f");

		// local derivative Client_think
		clientThink.getActionRates().put(think, r_t);
		clientThink.getParameterNames().put(think, "r_t");

		clientReq.getActionRates().put(request, passive);
		clientReq.getParameterNames().put(request, "passive");

		// constructing the model
		OriginalModel model = new OriginalModel (localDerivatives,descriptor,initialState,actions,largeGroups,smallGroups,constants);

		Display display = new Display(model);
		model.setDisplay(display);

		return model;
		
	}
	
	
}
