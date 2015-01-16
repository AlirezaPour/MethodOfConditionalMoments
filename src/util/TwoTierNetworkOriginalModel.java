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

	
	public static OriginalModel getTwoTierNetworkModel(){
		
		// the constants
		HashMap<String, Double> constants = new HashMap<String,Double>();
		
		// near users
		constants.put("r_near_process", 5.0);
		constants.put("r_send_macro_nr", 15.0);
		constants.put("r_send_macro_po", 22.0);
		
		
		// macro-cell
		constants.put("r_stream_wcu_femto_1_macro_po", 0.2);
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
		double r_stream_wcu_femto_1_macro_po=  0.2;
		double r_stream_wcu_femto_2_macro_po=  0.2;

		// users near femto-1
		double r_process_scu_femto_1 = 5.0;

		double r_send_femto_1_nr = 20.0 ;
		double r_send_femto_1_po = 10.0 ;

		double r_process_wcu_femto_1= 0.1;

		// users near femto-2
		double r_process_scu_femto_2 =  5.0;

		double r_send_femto_2_nr=  20.0;
		double r_send_femto_2_po =  10.0;

		double r_process_wcu_femto_2 = 0.1;

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

		
		
		//////////////////////////////////////////////
		// Action: near_proc 
		//////////////////////////////////////////////
		
		OriginalAction near_proc = new OriginalAction();
		near_proc.setName("near_proc");

		JumpVector near_proc_JumpVector = new JumpVector(descriptor);

		near_proc_JumpVector.put(var_user_near_processing , -1 ) ;
		near_proc_JumpVector.put(var_user_near_sending , +1 ) ;		
		near_proc.setJumpVector(near_proc_JumpVector);

		JumpVector near_proc_JumpVectorMinus = new JumpVector(descriptor);
		
		near_proc_JumpVectorMinus.put(var_user_near_processing , 1 ) ;
		near_proc.setJumpVectorMinus(near_proc_JumpVectorMinus);

		JumpVector near_proc_JumpVectorPlus = new JumpVector(descriptor);
		
		near_proc_JumpVectorMinus.put(var_user_near_sending , +1 ) ;

		near_proc.setJumpVectorPlus(near_proc_JumpVectorPlus);

		actions.add(near_proc);

		//////////////////////////////////////////////
		// Action: send_macro_nr
		//////////////////////////////////////////////

		OriginalAction send_macro_nr = new OriginalAction();
		send_macro_nr.setName("send_macro_nr");

		JumpVector send_macro_nr_JumpVector = new JumpVector(descriptor);

		send_macro_nr_JumpVector.put(var_user_near_processing , +1 ) ;
		send_macro_nr_JumpVector.put(var_user_near_sending , -1 ) ;

		send_macro_nr.setJumpVector(send_macro_nr_JumpVector);

		JumpVector send_macro_nr_JumpVectorMinus = new JumpVector(descriptor);

		send_macro_nr_JumpVectorMinus.put(var_user_near_sending , 1 ) ;
		send_macro_nr_JumpVectorMinus.put(var_ldcq_0 ,1 );
		send_macro_nr_JumpVectorMinus.put(var_ch_macro_idle ,1 );

		send_macro_nr.setJumpVectorMinus(send_macro_nr_JumpVectorMinus);

		JumpVector send_macro_nr_JumpVectorPlus = new JumpVector(descriptor);

		send_macro_nr_JumpVectorPlus.put(var_user_near_processing , +1 ) ;
		send_macro_nr_JumpVectorPlus.put(var_ldcq_0 ,1 );
		send_macro_nr_JumpVectorPlus.put(var_ch_macro_idle ,1 );

		send_macro_nr.setJumpVectorPlus(send_macro_nr_JumpVectorPlus);

		actions.add(send_macro_nr);

		//////////////////////////////////////////////
		// Action: send_macro_po
		//////////////////////////////////////////////

		OriginalAction send_macro_po = new OriginalAction();
		send_macro_po.setName("send_macro_po");

		JumpVector send_macro_po_JumpVector = new JumpVector(descriptor);

		send_macro_po_JumpVector.put(var_user_near_processing , +1 ) ;
		send_macro_po_JumpVector.put(var_user_near_sending , -1 ) ;

		send_macro_po.setJumpVector(send_macro_po_JumpVector);

		JumpVector send_macro_po_JumpVectorMinus = new JumpVector(descriptor);

		send_macro_po_JumpVectorMinus.put(var_user_near_sending , 1 ) ;
		send_macro_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		send_macro_po_JumpVectorMinus.put(var_ch_macro_idle ,1 );
		
		send_macro_po.setJumpVectorMinus(send_macro_po_JumpVectorMinus);

		JumpVector send_macro_po_JumpVectorPlus = new JumpVector(descriptor);

		send_macro_po_JumpVectorPlus.put(var_user_near_processing , +1 ) ;
		send_macro_po_JumpVectorPlus.put(var_ldcq_1 ,1 );
		send_macro_po_JumpVectorPlus.put(var_ch_macro_idle ,1 );

		send_macro_po.setJumpVectorPlus(send_macro_po_JumpVectorPlus);

		actions.add(send_macro_po);
		

		//////////////////////////////////////////////
		// Action: accept_wcu_femto_1_macro
		//////////////////////////////////////////////

		OriginalAction accept_wcu_femto_1_macro = new OriginalAction();
		 accept_wcu_femto_1_macro.setName("accept_wcu_femto_1_macro");

		JumpVector accept_wcu_femto_1_macro_JumpVector = new JumpVector(descriptor);

		accept_wcu_femto_1_macro_JumpVector.put(var_ldcq_0 ,-1 );
		accept_wcu_femto_1_macro_JumpVector.put(var_ldcq_1 ,+1 );
		accept_wcu_femto_1_macro_JumpVector.put(var_wcu_femto_1_waiting_macro ,-1 );
		accept_wcu_femto_1_macro_JumpVector.put(var_wcu_femto_1_streaming_macro , +1);
		
		accept_wcu_femto_1_macro.setJumpVector(accept_wcu_femto_1_macro_JumpVector);

		JumpVector accept_wcu_femto_1_JumpVectorMinus = new JumpVector(descriptor);

		accept_wcu_femto_1_JumpVectorMinus.put(var_ldcq_0 ,1 );
		accept_wcu_femto_1_JumpVectorMinus.put(var_wcu_femto_1_waiting_macro ,1 );
		
		accept_wcu_femto_1_macro.setJumpVectorMinus(accept_wcu_femto_1_JumpVectorMinus);

		JumpVector accept_wcu_femto_1_macro_JumpVectorPlus = new JumpVector(descriptor);

		accept_wcu_femto_1_macro_JumpVectorPlus.put(var_ldcq_1 ,1 );
		accept_wcu_femto_1_macro_JumpVectorPlus.put(var_wcu_femto_1_streaming_macro , 1);

		accept_wcu_femto_1_macro.setJumpVectorPlus(accept_wcu_femto_1_macro_JumpVectorPlus);

		actions.add(accept_wcu_femto_1_macro);
		

		//////////////////////////////////////////////
		// Action: reject_wcu_femto_1_macro
		//////////////////////////////////////////////

		OriginalAction reject_wcu_femto_1_macro = new OriginalAction();
		reject_wcu_femto_1_macro.setName("reject_wcu_femto_1_macro");

		JumpVector reject_wcu_femto_1_macro_JumpVector = new JumpVector(descriptor);

		reject_wcu_femto_1_macro_JumpVector.put(var_wcu_femto_1_processing , 1);
		reject_wcu_femto_1_macro_JumpVector.put(var_wcu_femto_1_waiting_macro ,-1 );
		
		reject_wcu_femto_1_macro.setJumpVector(reject_wcu_femto_1_macro_JumpVector);

		JumpVector reject_wcu_femto_1_JumpVectorMinus = new JumpVector(descriptor);

		reject_wcu_femto_1_JumpVectorMinus.put(var_ldcq_1 ,1 );
		reject_wcu_femto_1_JumpVectorMinus.put(var_wcu_femto_1_waiting_macro ,1 );
		
		reject_wcu_femto_1_macro.setJumpVectorMinus(reject_wcu_femto_1_JumpVectorMinus);

		JumpVector reject_wcu_femto_1_macro_JumpVectorPlus = new JumpVector(descriptor);

		reject_wcu_femto_1_macro_JumpVectorPlus.put(var_ldcq_1 ,1 );
		reject_wcu_femto_1_macro_JumpVectorPlus.put(var_wcu_femto_1_processing , 1);

		reject_wcu_femto_1_macro.setJumpVectorPlus(reject_wcu_femto_1_macro_JumpVectorPlus);

		actions.add(reject_wcu_femto_1_macro);
		

		//////////////////////////////////////////////
		// Action: stream_wcu_femto_1_macro_po
		//////////////////////////////////////////////

		OriginalAction stream_wcu_femto_1_macro_po = new OriginalAction();
		stream_wcu_femto_1_macro_po.setName("stream_wcu_femto_1_macro_po");

		JumpVector stream_wcu_femto_1_macro_po_JumpVector = new JumpVector(descriptor);


		stream_wcu_femto_1_macro_po_JumpVector.put(var_ldcq_0 ,1 );
		stream_wcu_femto_1_macro_po_JumpVector.put(var_ldcq_1 ,-1 );
		stream_wcu_femto_1_macro_po_JumpVector.put(var_wcu_femto_1_processing , 1);
		stream_wcu_femto_1_macro_po_JumpVector.put(var_wcu_femto_1_streaming_macro , -1);
		
		stream_wcu_femto_1_macro_po.setJumpVector(stream_wcu_femto_1_macro_po_JumpVector);

		JumpVector stream_wcu_femto_1_macro_po_JumpVectorMinus = new JumpVector(descriptor);

		stream_wcu_femto_1_macro_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		stream_wcu_femto_1_macro_po_JumpVectorMinus.put(var_wcu_femto_1_streaming_macro ,1);
		
		stream_wcu_femto_1_macro_po.setJumpVectorMinus(stream_wcu_femto_1_macro_po_JumpVectorMinus);

		JumpVector stream_wcu_femto_1_macro_po_JumpVectorPlus = new JumpVector(descriptor);

		stream_wcu_femto_1_macro_po_JumpVectorPlus.put(var_ldcq_0 ,1 );
		stream_wcu_femto_1_macro_po_JumpVectorPlus.put(var_wcu_femto_1_processing , 1);
		
		stream_wcu_femto_1_macro_po.setJumpVectorPlus(stream_wcu_femto_1_macro_po_JumpVectorPlus);

		actions.add(stream_wcu_femto_1_macro_po);
		

		//////////////////////////////////////////////
		// Action: send_femto_1_nr
		//////////////////////////////////////////////

		OriginalAction send_femto_1_nr = new OriginalAction();
		send_femto_1_nr.setName("send_femto_1_nr");

		JumpVector send_femto_1_nr_JumpVector = new JumpVector(descriptor);

		send_femto_1_nr_JumpVector.put(var_scu_femto_1_processing , 1);
		send_femto_1_nr_JumpVector.put(var_scu_femto_1_sending ,-1 );
		
		send_femto_1_nr.setJumpVector(send_femto_1_nr_JumpVector);

		JumpVector send_femto_1_nr_JumpVectorMinus = new JumpVector(descriptor);

		send_femto_1_nr_JumpVectorMinus.put(var_ldcq_0 ,1 );
		send_femto_1_nr_JumpVectorMinus.put(var_scu_femto_1_sending ,1 );
		send_femto_1_nr_JumpVectorMinus.put(var_ch_femto_1_idle,1);
		
		send_femto_1_nr.setJumpVectorMinus(send_femto_1_nr_JumpVectorMinus);

		JumpVector send_femto_1_nr_JumpVectorPlus = new JumpVector(descriptor);

		send_femto_1_nr_JumpVectorPlus.put(var_ldcq_0 ,1 );
		send_femto_1_nr_JumpVectorPlus.put(var_scu_femto_1_processing , 1);
		send_femto_1_nr_JumpVectorPlus.put(var_ch_femto_1_idle ,1);

		send_femto_1_nr.setJumpVectorPlus(send_femto_1_nr_JumpVectorPlus);

		actions.add(send_femto_1_nr);
		
	
		//////////////////////////////////////////////
		// Action: send_femto_1_po
		//////////////////////////////////////////////

		OriginalAction send_femto_1_po = new OriginalAction();
		send_femto_1_po.setName("send_femto_1_po");

		JumpVector send_femto_1_po_JumpVector = new JumpVector(descriptor);

		send_femto_1_po_JumpVector.put(var_scu_femto_1_processing , +1);
		send_femto_1_po_JumpVector.put(var_scu_femto_1_sending ,-1 );

		send_femto_1_po.setJumpVector(send_femto_1_po_JumpVector);

		JumpVector send_femto_1_po_JumpVectorMinus = new JumpVector(descriptor);

		send_femto_1_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		send_femto_1_po_JumpVectorMinus.put(var_scu_femto_1_sending ,1 );
		send_femto_1_po_JumpVectorMinus.put(var_ch_femto_1_idle , 1);
		
		send_femto_1_po.setJumpVectorMinus(send_femto_1_po_JumpVectorMinus);

		JumpVector send_femto_1_po_JumpVectorPlus = new JumpVector(descriptor);

		
		send_femto_1_po_JumpVectorPlus.put(var_ldcq_1 ,1 );
		send_femto_1_po_JumpVectorPlus.put(var_scu_femto_1_processing , 1);
		send_femto_1_po_JumpVectorPlus.put(var_ch_femto_1_idle , 1);
		
		send_femto_1_po.setJumpVectorPlus(send_femto_1_po_JumpVectorPlus);

		actions.add(send_femto_1_po);
		

		

		//////////////////////////////////////////////
		// Action: stream_femto_1_nr
		//////////////////////////////////////////////

		OriginalAction stream_femto_1_nr = new OriginalAction();
		stream_femto_1_nr.setName("stream_femto_1_nr");

		JumpVector stream_femto_1_nr_JumpVector = new JumpVector(descriptor);

		stream_femto_1_nr_JumpVector.put(var_wcu_femto_1_processing , +1);
		stream_femto_1_nr_JumpVector.put(var_wcu_femto_1_streaming_local, -1);
		stream_femto_1_nr_JumpVector.put(var_wcuq_femto_1_0 ,+1 );
		stream_femto_1_nr_JumpVector.put(var_wcuq_femto_1_1 ,-1);
		
		stream_femto_1_nr.setJumpVector(stream_femto_1_nr_JumpVector);

		JumpVector stream_femto_1_nr_JumpVectorMinus = new JumpVector(descriptor);

		stream_femto_1_nr_JumpVectorMinus.put(var_ldcq_0 ,1 );
		stream_femto_1_nr_JumpVectorMinus.put(var_wcu_femto_1_streaming_local, 1);
		stream_femto_1_nr_JumpVectorMinus.put(var_wcuq_femto_1_1 , 1);
		
		stream_femto_1_nr.setJumpVectorMinus(stream_femto_1_nr_JumpVectorMinus);

		JumpVector stream_femto_1_nr_JumpVectorPlus = new JumpVector(descriptor);

		stream_femto_1_nr_JumpVectorPlus.put(var_ldcq_0 ,1 );
		stream_femto_1_nr_JumpVectorPlus.put(var_wcu_femto_1_processing , +1);
		stream_femto_1_nr_JumpVectorPlus.put(var_wcuq_femto_1_0 ,1 );

		stream_femto_1_nr.setJumpVectorPlus(stream_femto_1_nr_JumpVectorPlus);

		actions.add(stream_femto_1_nr);
		


		//////////////////////////////////////////////
		// Action: stream_femto_1_po
		//////////////////////////////////////////////

		OriginalAction stream_femto_1_po = new OriginalAction();
		stream_femto_1_po.setName("stream_femto_1_po");

		JumpVector stream_femto_1_po_JumpVector = new JumpVector(descriptor);

		stream_femto_1_po_JumpVector.put(var_wcu_femto_1_processing , 1);
		stream_femto_1_po_JumpVector.put(var_wcu_femto_1_streaming_local, -1);
		stream_femto_1_po_JumpVector.put(var_wcuq_femto_1_0 ,+1 );
		stream_femto_1_po_JumpVector.put(var_wcuq_femto_1_1 ,-1);
		
		stream_femto_1_po.setJumpVector(stream_femto_1_po_JumpVector);

		JumpVector stream_femto_1_po_JumpVectorMinus = new JumpVector(descriptor);

		stream_femto_1_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		stream_femto_1_po_JumpVectorMinus.put(var_wcu_femto_1_streaming_local, 1);
		stream_femto_1_po_JumpVectorMinus.put(var_wcuq_femto_1_1 , 1);

		stream_femto_1_po.setJumpVectorMinus(stream_femto_1_po_JumpVectorMinus);

		JumpVector stream_femto_1_po_JumpVectorPlus = new JumpVector(descriptor);

		stream_femto_1_po_JumpVectorPlus.put(var_ldcq_1 ,1 );
		stream_femto_1_po_JumpVectorPlus.put(var_wcu_femto_1_processing , 1);
		stream_femto_1_po_JumpVectorPlus.put(var_wcuq_femto_1_0 ,1 );

		stream_femto_1_po.setJumpVectorPlus(stream_femto_1_po_JumpVectorPlus);

		actions.add(stream_femto_1_po);
		

		

		//////////////////////////////////////////////
		// Action: accept_wcu_femto_2_macro
		//////////////////////////////////////////////

		OriginalAction accept_wcu_femto_2_macro = new OriginalAction();
		accept_wcu_femto_2_macro.setName("accept_wcu_femto_2_macro");

		JumpVector accept_wcu_femto_2_macro_JumpVector = new JumpVector(descriptor);

		accept_wcu_femto_2_macro_JumpVector.put(var_ldcq_0 ,-1 );
		accept_wcu_femto_2_macro_JumpVector.put(var_ldcq_1 ,+1 );
		accept_wcu_femto_2_macro_JumpVector.put(var_wcu_femto_2_waiting_macro ,-1 );
		accept_wcu_femto_2_macro_JumpVector.put(var_wcu_femto_2_streaming_macro , +1);

		accept_wcu_femto_2_macro.setJumpVector(accept_wcu_femto_2_macro_JumpVector);

		JumpVector accept_wcu_femto_2_JumpVectorMinus = new JumpVector(descriptor);

		accept_wcu_femto_2_JumpVectorMinus.put(var_ldcq_0 ,1 );
		accept_wcu_femto_2_JumpVectorMinus.put(var_wcu_femto_2_waiting_macro ,1 );

		accept_wcu_femto_2_macro.setJumpVectorMinus(accept_wcu_femto_2_JumpVectorMinus);

		JumpVector accept_wcu_femto_2_macro_JumpVectorPlus = new JumpVector(descriptor);

		accept_wcu_femto_2_macro_JumpVectorPlus.put(var_ldcq_1 ,1 );
		accept_wcu_femto_2_macro_JumpVectorPlus.put(var_wcu_femto_2_streaming_macro , 1);

		accept_wcu_femto_2_macro.setJumpVectorPlus(accept_wcu_femto_2_macro_JumpVectorPlus);

		actions.add(accept_wcu_femto_2_macro);


		//////////////////////////////////////////////
		// Action: reject_wcu_femto_2_macro
		//////////////////////////////////////////////

		OriginalAction reject_wcu_femto_2_macro = new OriginalAction();
		reject_wcu_femto_2_macro.setName("reject_wcu_femto_2_macro");

		JumpVector reject_wcu_femto_2_macro_JumpVector = new JumpVector(descriptor);

		reject_wcu_femto_2_macro_JumpVector.put(var_wcu_femto_2_processing , +1);
		reject_wcu_femto_2_macro_JumpVector.put(var_wcu_femto_2_waiting_macro ,-1 );

		reject_wcu_femto_2_macro.setJumpVector(reject_wcu_femto_2_macro_JumpVector);

		JumpVector reject_wcu_femto_2_JumpVectorMinus = new JumpVector(descriptor);

		reject_wcu_femto_2_JumpVectorMinus.put(var_ldcq_1 ,1 );
		reject_wcu_femto_2_JumpVectorMinus.put(var_wcu_femto_2_waiting_macro ,1 );

		reject_wcu_femto_2_macro.setJumpVectorMinus(reject_wcu_femto_2_JumpVectorMinus);

		JumpVector reject_wcu_femto_2_macro_JumpVectorPlus = new JumpVector(descriptor);

		reject_wcu_femto_2_macro_JumpVectorPlus.put(var_ldcq_1 ,1 );
		reject_wcu_femto_2_macro_JumpVectorPlus.put(var_wcu_femto_2_processing , 1);

		reject_wcu_femto_2_macro.setJumpVectorPlus(reject_wcu_femto_2_macro_JumpVectorPlus);

		actions.add(reject_wcu_femto_2_macro);


		//////////////////////////////////////////////
		// Action: stream_wcu_femto_2_macro_po
		//////////////////////////////////////////////

		OriginalAction stream_wcu_femto_2_macro_po = new OriginalAction();
		stream_wcu_femto_2_macro_po.setName("stream_wcu_femto_2_macro_po");

		JumpVector stream_wcu_femto_2_macro_po_JumpVector = new JumpVector(descriptor);

		stream_wcu_femto_2_macro_po_JumpVector.put(var_ldcq_0 ,1 );
		stream_wcu_femto_2_macro_po_JumpVector.put(var_ldcq_1 ,-1 );
		stream_wcu_femto_2_macro_po_JumpVector.put(var_wcu_femto_2_processing , +1);
		stream_wcu_femto_2_macro_po_JumpVector.put(var_wcu_femto_2_streaming_macro , -1);

		stream_wcu_femto_2_macro_po.setJumpVector(stream_wcu_femto_2_macro_po_JumpVector);

		JumpVector stream_wcu_femto_2_macro_po_JumpVectorMinus = new JumpVector(descriptor);

		stream_wcu_femto_2_macro_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		stream_wcu_femto_2_macro_po_JumpVectorMinus.put(var_wcu_femto_2_streaming_macro , 1);

		stream_wcu_femto_2_macro_po.setJumpVectorMinus(stream_wcu_femto_2_macro_po_JumpVectorMinus);

		JumpVector stream_wcu_femto_2_macro_po_JumpVectorPlus = new JumpVector(descriptor);

		stream_wcu_femto_2_macro_po_JumpVectorPlus.put(var_ldcq_0 ,1 );
		stream_wcu_femto_2_macro_po_JumpVectorPlus.put(var_wcu_femto_2_processing , 1);
		
		stream_wcu_femto_2_macro_po.setJumpVectorPlus(stream_wcu_femto_2_macro_po_JumpVectorPlus);

		actions.add(stream_wcu_femto_2_macro_po);
		
		
		

		//////////////////////////////////////////////
		// Action: send_femto_2_nr
		//////////////////////////////////////////////

		OriginalAction send_femto_2_nr = new OriginalAction();
		send_femto_2_nr.setName("send_femto_2_nr");

		JumpVector send_femto_2_nr_JumpVector = new JumpVector(descriptor);

		send_femto_2_nr_JumpVector.put(var_scu_femto_2_processing ,1 );
		send_femto_2_nr_JumpVector.put(var_scu_femto_2_sending ,-1);

		send_femto_2_nr.setJumpVector(send_femto_2_nr_JumpVector);

		JumpVector send_femto_2_nr_JumpVectorMinus = new JumpVector(descriptor);

		send_femto_2_nr_JumpVectorMinus.put(var_ldcq_0 ,1 );
		send_femto_2_nr_JumpVectorMinus.put(var_scu_femto_2_sending ,1 );
		send_femto_2_nr_JumpVectorMinus.put(var_ch_femto_2_idle , 1);

		send_femto_2_nr.setJumpVectorMinus(send_femto_2_nr_JumpVectorMinus);

		JumpVector send_femto_2_nr_JumpVectorPlus = new JumpVector(descriptor);

		send_femto_2_nr_JumpVectorPlus.put(var_ldcq_0 ,1 );
		send_femto_2_nr_JumpVectorPlus.put(var_scu_femto_2_processing ,1 );
		send_femto_2_nr_JumpVectorPlus.put(var_ch_femto_2_idle , 1);

		send_femto_2_nr.setJumpVectorPlus(send_femto_2_nr_JumpVectorPlus);

		actions.add(send_femto_2_nr);
		
		
		//////////////////////////////////////////////
		// Action: send_femto_2_po
		//////////////////////////////////////////////

		OriginalAction send_femto_2_po = new OriginalAction();
		send_femto_2_po.setName("send_femto_2_po");

		JumpVector send_femto_2_po_JumpVector = new JumpVector(descriptor);

		send_femto_2_po_JumpVector.put(var_scu_femto_2_processing ,+1 );
		send_femto_2_po_JumpVector.put(var_scu_femto_2_sending ,-1 );

		send_femto_2_po.setJumpVector(send_femto_2_po_JumpVector);

		JumpVector send_femto_2_po_JumpVectorMinus = new JumpVector(descriptor);

		send_femto_2_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		send_femto_2_po_JumpVectorMinus.put(var_scu_femto_2_sending ,1 );
		send_femto_2_po_JumpVectorMinus.put(var_ch_femto_2_idle , 1);
		
		send_femto_2_po.setJumpVectorMinus(send_femto_2_po_JumpVectorMinus);

		JumpVector send_femto_2_po_JumpVectorPlus = new JumpVector(descriptor);

		send_femto_2_po_JumpVectorPlus.put(var_ldcq_1 ,1 );
		send_femto_2_po_JumpVectorPlus.put(var_scu_femto_2_processing ,1 );
		send_femto_2_po_JumpVectorPlus.put(var_ch_femto_2_idle , 1);

		send_femto_2_po.setJumpVectorPlus(send_femto_2_po_JumpVectorPlus);

		actions.add(send_femto_2_po);
		

		//////////////////////////////////////////////
		// Action: stream_femto_2_nr
		//////////////////////////////////////////////

		OriginalAction stream_femto_2_nr = new OriginalAction();
		stream_femto_2_nr.setName("stream_femto_2_nr");

		JumpVector stream_femto_2_nr_JumpVector = new JumpVector(descriptor);

		stream_femto_2_nr_JumpVector.put(var_wcu_femto_2_processing , 1);
		stream_femto_2_nr_JumpVector.put(var_wcu_femto_2_streaming_local , -1);
		stream_femto_2_nr_JumpVector.put(var_wcuq_femto_2_0 , +1);
		stream_femto_2_nr_JumpVector.put(var_wcuq_femto_2_1 , -1);

		stream_femto_2_nr.setJumpVector(stream_femto_2_nr_JumpVector);

		JumpVector stream_femto_2_nr_JumpVectorMinus = new JumpVector(descriptor);

		stream_femto_2_nr_JumpVectorMinus.put(var_ldcq_0 ,1 );
		stream_femto_2_nr_JumpVectorMinus.put(var_wcu_femto_2_streaming_local , 1);
		stream_femto_2_nr_JumpVectorMinus.put(var_wcuq_femto_2_1 , 1);

		stream_femto_2_nr.setJumpVectorMinus(stream_femto_2_nr_JumpVectorMinus);

		JumpVector stream_femto_2_nr_JumpVectorPlus = new JumpVector(descriptor);

		stream_femto_2_nr_JumpVectorPlus.put(var_ldcq_0 ,1 );
		stream_femto_2_nr_JumpVectorPlus.put(var_wcu_femto_2_processing , 1);
		stream_femto_2_nr_JumpVectorPlus.put(var_wcuq_femto_2_0 , 1);
		
		stream_femto_2_nr.setJumpVectorPlus(stream_femto_2_nr_JumpVectorPlus);

		actions.add(stream_femto_2_nr);
		
		

		//////////////////////////////////////////////
		// Action: stream_femto_2_po
		//////////////////////////////////////////////

		OriginalAction stream_femto_2_po = new OriginalAction();
		stream_femto_2_po.setName("stream_femto_2_po");

		JumpVector stream_femto_2_po_JumpVector = new JumpVector(descriptor);

		stream_femto_2_po_JumpVector.put(var_wcu_femto_2_processing , +1);
		stream_femto_2_po_JumpVector.put(var_wcu_femto_2_streaming_local , -1);
		stream_femto_2_po_JumpVector.put(var_wcuq_femto_2_0 , +1);
		stream_femto_2_po_JumpVector.put(var_wcuq_femto_2_1 , -1);

		stream_femto_2_po.setJumpVector(stream_femto_2_po_JumpVector);

		JumpVector stream_femto_2_po_JumpVectorMinus = new JumpVector(descriptor);

		stream_femto_2_po_JumpVectorMinus.put(var_ldcq_1 ,1 );
		stream_femto_2_po_JumpVectorMinus.put(var_wcu_femto_2_streaming_local , 1);
		stream_femto_2_po_JumpVectorMinus.put(var_wcuq_femto_2_1 , 1);

		stream_femto_2_po.setJumpVectorMinus(stream_femto_2_po_JumpVectorMinus);

		JumpVector stream_femto_2_po_JumpVectorPlus = new JumpVector(descriptor);

		stream_femto_2_po_JumpVectorPlus.put(var_ldcq_1 ,1 );
		stream_femto_2_po_JumpVectorPlus.put(var_wcu_femto_2_processing , 1);
		stream_femto_2_po_JumpVectorPlus.put(var_wcuq_femto_2_0 , 1);

		stream_femto_2_po.setJumpVectorPlus(stream_femto_2_po_JumpVectorPlus);

		actions.add(stream_femto_2_po);
		

		//////////////////////////////////////////////
		// Action: process_scu_femto_1
		//////////////////////////////////////////////

		OriginalAction process_scu_femto_1 = new OriginalAction();
		process_scu_femto_1.setName("process_scu_femto_1");

		JumpVector process_scu_femto_1_JumpVector = new JumpVector(descriptor);

		process_scu_femto_1_JumpVector.put(var_scu_femto_1_processing , -1);
		process_scu_femto_1_JumpVector.put(var_scu_femto_1_sending ,1 );

		process_scu_femto_1.setJumpVector(process_scu_femto_1_JumpVector);

		JumpVector process_scu_femto_1_JumpVectorMinus = new JumpVector(descriptor);

		process_scu_femto_1_JumpVectorMinus.put(var_scu_femto_1_processing , 1);

		process_scu_femto_1.setJumpVectorMinus(process_scu_femto_1_JumpVectorMinus);

		JumpVector process_scu_femto_1_JumpVectorPlus = new JumpVector(descriptor);

		process_scu_femto_1_JumpVectorPlus.put(var_ldcq_1 ,1 );
		process_scu_femto_1_JumpVectorPlus.put(var_scu_femto_1_sending ,1 );

		process_scu_femto_1.setJumpVectorPlus(process_scu_femto_1_JumpVectorPlus);

		actions.add(process_scu_femto_1);
		

		//////////////////////////////////////////////
		// Action: process_wcu_femto_1
		//////////////////////////////////////////////

		OriginalAction process_wcu_femto_1 = new OriginalAction();
		process_wcu_femto_1.setName("process_wcu_femto_1");

		JumpVector process_wcu_femto_1_JumpVector = new JumpVector(descriptor);


		process_wcu_femto_1_JumpVector.put(var_wcu_femto_1_processing , -1);
		process_wcu_femto_1_JumpVector.put(var_wcu_femto_1_waiting_local , +1);
		
		process_wcu_femto_1.setJumpVector(process_wcu_femto_1_JumpVector);

		JumpVector process_wcu_femto_1_JumpVectorMinus = new JumpVector(descriptor);

		process_wcu_femto_1_JumpVectorMinus.put(var_wcu_femto_1_processing , 1);

		process_wcu_femto_1.setJumpVectorMinus(process_wcu_femto_1_JumpVectorMinus);

		JumpVector process_wcu_femto_1_JumpVectorPlus = new JumpVector(descriptor);

		process_wcu_femto_1_JumpVectorPlus.put(var_wcu_femto_1_waiting_local , 1);

		process_wcu_femto_1.setJumpVectorPlus(process_wcu_femto_1_JumpVectorPlus);

		actions.add(process_wcu_femto_1);
		
		
		//////////////////////////////////////////////
		// Action: accept_wcu_femto_1_local
		//////////////////////////////////////////////

		OriginalAction accept_wcu_femto_1_local = new OriginalAction();
		accept_wcu_femto_1_local.setName("accept_wcu_femto_1_local");

		JumpVector accept_wcu_femto_1_local_JumpVector = new JumpVector(descriptor);

	
		accept_wcu_femto_1_local_JumpVector.put(var_wcu_femto_1_waiting_local , -1);
		accept_wcu_femto_1_local_JumpVector.put(var_wcu_femto_1_streaming_local, +1);
		accept_wcu_femto_1_local_JumpVector.put(var_wcuq_femto_1_0 ,-1);
		accept_wcu_femto_1_local_JumpVector.put(var_wcuq_femto_1_1 ,+1);

		accept_wcu_femto_1_local.setJumpVector(accept_wcu_femto_1_local_JumpVector);

		JumpVector accept_wcu_femto_1_local_JumpVectorMinus = new JumpVector(descriptor);

		accept_wcu_femto_1_local_JumpVectorMinus.put(var_wcu_femto_1_waiting_local , 1);
		accept_wcu_femto_1_local_JumpVectorMinus.put(var_wcuq_femto_1_0 ,1 );

		accept_wcu_femto_1_local.setJumpVectorMinus(accept_wcu_femto_1_local_JumpVectorMinus);

		JumpVector accept_wcu_femto_1_local_JumpVectorPlus = new JumpVector(descriptor);

		accept_wcu_femto_1_local_JumpVectorPlus.put(var_wcu_femto_1_streaming_local, 1);
		accept_wcu_femto_1_local_JumpVectorPlus.put(var_wcuq_femto_1_1 ,1);

		accept_wcu_femto_1_local.setJumpVectorPlus(accept_wcu_femto_1_local_JumpVectorPlus);

		actions.add(accept_wcu_femto_1_local);
		
		
		//////////////////////////////////////////////
		// Action: reject_wcu_femto_1_local
		//////////////////////////////////////////////

		OriginalAction reject_wcu_femto_1_local = new OriginalAction();
		reject_wcu_femto_1_local.setName("reject_wcu_femto_1_local");

		JumpVector reject_wcu_femto_1_local_JumpVector = new JumpVector(descriptor);

		reject_wcu_femto_1_local_JumpVector.put(var_wcu_femto_1_waiting_local , -1);
		reject_wcu_femto_1_local_JumpVector.put(var_wcu_femto_1_waiting_macro ,+1 );

		reject_wcu_femto_1_local.setJumpVector(reject_wcu_femto_1_local_JumpVector);

		JumpVector reject_wcu_femto_1_local_JumpVectorMinus = new JumpVector(descriptor);

		reject_wcu_femto_1_local_JumpVectorMinus.put(var_wcu_femto_1_waiting_local , 1);
		reject_wcu_femto_1_local_JumpVectorMinus.put(var_wcuq_femto_1_1 , 1);

		reject_wcu_femto_1_local.setJumpVectorMinus(reject_wcu_femto_1_local_JumpVectorMinus);

		JumpVector reject_wcu_femto_1_local_JumpVectorPlus = new JumpVector(descriptor);

		reject_wcu_femto_1_local_JumpVectorPlus.put(var_wcu_femto_1_waiting_macro ,1);
		reject_wcu_femto_1_local_JumpVectorPlus.put(var_wcuq_femto_1_1 , 1);

		reject_wcu_femto_1_local.setJumpVectorPlus(reject_wcu_femto_1_local_JumpVectorPlus);

		actions.add(reject_wcu_femto_1_local);

		
		
		

		//////////////////////////////////////////////
		// Action: process_scu_femto_2
		//////////////////////////////////////////////

		OriginalAction process_scu_femto_2 = new OriginalAction();
		process_scu_femto_2.setName("process_scu_femto_2");

		JumpVector process_scu_femto_2_JumpVector = new JumpVector(descriptor);
		
		process_scu_femto_2_JumpVector.put(var_scu_femto_2_processing ,-1 );
		process_scu_femto_2_JumpVector.put(var_scu_femto_2_sending ,+1 );
		

		process_scu_femto_2.setJumpVector(process_scu_femto_2_JumpVector);

		JumpVector process_scu_femto_2_JumpVectorMinus = new JumpVector(descriptor);

		process_scu_femto_2_JumpVectorMinus.put(var_scu_femto_2_processing ,1 );

		process_scu_femto_2.setJumpVectorMinus(process_scu_femto_2_JumpVectorMinus);

		JumpVector process_scu_femto_2_JumpVectorPlus = new JumpVector(descriptor);

		process_scu_femto_2_JumpVectorPlus.put(var_scu_femto_2_sending ,1);
		
		process_scu_femto_2.setJumpVectorPlus(process_scu_femto_2_JumpVectorPlus);

		actions.add(process_scu_femto_2);
		

		//////////////////////////////////////////////
		// Action: process_wcu_femto_2
		//////////////////////////////////////////////

		OriginalAction process_wcu_femto_2 = new OriginalAction();
		process_wcu_femto_2.setName("process_wcu_femto_2");

		JumpVector process_wcu_femto_2_JumpVector = new JumpVector(descriptor);

		process_wcu_femto_2_JumpVector.put(var_wcu_femto_2_processing , -1);
		process_wcu_femto_2_JumpVector.put(var_wcu_femto_2_waiting_local , +1);

		process_wcu_femto_2.setJumpVector(process_wcu_femto_2_JumpVector);

		JumpVector process_wcu_femto_2_JumpVectorMinus = new JumpVector(descriptor);

		process_wcu_femto_2_JumpVectorMinus.put(var_wcu_femto_2_processing , 1);

		process_wcu_femto_2.setJumpVectorMinus(process_wcu_femto_2_JumpVectorMinus);

		JumpVector process_wcu_femto_2_JumpVectorPlus = new JumpVector(descriptor);

		process_wcu_femto_2_JumpVectorPlus.put(var_wcu_femto_2_waiting_local , 1);

		process_wcu_femto_2.setJumpVectorPlus(process_wcu_femto_2_JumpVectorPlus);

		actions.add(process_wcu_femto_2);

		//////////////////////////////////////////////
		// Action: accept_wcu_femto_2_local
		//////////////////////////////////////////////

		OriginalAction accept_wcu_femto_2_local = new OriginalAction();
		accept_wcu_femto_2_local.setName("accept_wcu_femto_2_local");

		JumpVector accept_wcu_femto_2_local_JumpVector = new JumpVector(descriptor);

		accept_wcu_femto_2_local_JumpVector.put(var_wcu_femto_2_waiting_local , -1);
		accept_wcu_femto_2_local_JumpVector.put(var_wcu_femto_2_streaming_local , +1);
		accept_wcu_femto_2_local_JumpVector.put(var_wcuq_femto_2_0 , -1);
		accept_wcu_femto_2_local_JumpVector.put(var_wcuq_femto_2_1 , +1);

		accept_wcu_femto_2_local.setJumpVector(accept_wcu_femto_2_local_JumpVector);

		JumpVector accept_wcu_femto_2_local_JumpVectorMinus = new JumpVector(descriptor);

		accept_wcu_femto_2_local_JumpVectorMinus.put(var_wcu_femto_2_waiting_local , 1);
		accept_wcu_femto_2_local_JumpVectorMinus.put(var_wcuq_femto_2_0 , 1);

		accept_wcu_femto_2_local.setJumpVectorMinus(accept_wcu_femto_2_local_JumpVectorMinus);

		JumpVector accept_wcu_femto_2_local_JumpVectorPlus = new JumpVector(descriptor);

		accept_wcu_femto_2_local_JumpVectorPlus.put(var_wcu_femto_2_streaming_local , 1);
		accept_wcu_femto_2_local_JumpVectorPlus.put(var_wcuq_femto_2_1 , 1);

		accept_wcu_femto_2_local.setJumpVectorPlus(accept_wcu_femto_2_local_JumpVectorPlus);

		actions.add(accept_wcu_femto_2_local);
		
		//////////////////////////////////////////////
		// Action: reject_wcu_femto_2_local
		//////////////////////////////////////////////

		OriginalAction reject_wcu_femto_2_local = new OriginalAction();
		reject_wcu_femto_2_local.setName("reject_wcu_femto_2_local");

		JumpVector reject_wcu_femto_2_local_JumpVector = new JumpVector(descriptor);

		reject_wcu_femto_2_local_JumpVector.put(var_wcu_femto_2_waiting_local , -1);
		reject_wcu_femto_2_local_JumpVector.put(var_wcu_femto_2_waiting_macro ,+1 );
		
		reject_wcu_femto_2_local.setJumpVector(reject_wcu_femto_2_local_JumpVector);

		JumpVector reject_wcu_femto_2_local_JumpVectorMinus = new JumpVector(descriptor);

		reject_wcu_femto_2_local_JumpVectorMinus.put(var_wcu_femto_2_waiting_local , 1);
		reject_wcu_femto_2_local_JumpVectorMinus.put(var_wcuq_femto_2_1 , 1);

		reject_wcu_femto_2_local.setJumpVectorMinus(reject_wcu_femto_2_local_JumpVectorMinus);

		JumpVector reject_wcu_femto_2_local_JumpVectorPlus = new JumpVector(descriptor);

		reject_wcu_femto_2_local_JumpVectorPlus.put(var_wcu_femto_2_waiting_macro ,1 );
		reject_wcu_femto_2_local_JumpVectorPlus.put(var_wcuq_femto_2_1 , 1);

		reject_wcu_femto_2_local.setJumpVectorPlus(reject_wcu_femto_2_local_JumpVectorPlus);

		actions.add(reject_wcu_femto_2_local);
		
		// setting the apparent rate data.
		//For each local derivative and action type that the derivative enables, specify the rate of the action in the local derivative.
		
		///////////////////////////////////////////////////
		// local derivative: user_near_processing
		//////////////////////////////////////////////////
		
		// action: near_proc
		user_near_processing.getActionRates().put(near_proc,r_near_process);
		user_near_processing.getParameterNames().put(near_proc,"r_near_process");
		
		
		//////////////////////////////////////////////////
		// local derivative: user_near_sending
		/////////////////////////////////////////////////
		
		// action: send_macro_nr
		user_near_sending.getActionRates().put(send_macro_nr,passive);
		user_near_sending.getParameterNames().put(send_macro_nr,"r_passive");
		
		// action: send_macro_nr
		user_near_sending.getActionRates().put(send_macro_po,passive);
		user_near_sending.getParameterNames().put(send_macro_po,"r_passive");
		
		
		//////////////////////////////////////////////////
		// local derivative: ldcq_0
		/////////////////////////////////////////////////
		
		// action: send_macro_nr
		ldcq_0.getActionRates().put(send_macro_nr,passive);
		ldcq_0.getParameterNames().put(send_macro_nr,"r_passive");
				
		// action: accept_wcu_femto_1_macro
		ldcq_0.getActionRates().put(accept_wcu_femto_1_macro,r_control);
		ldcq_0.getParameterNames().put(accept_wcu_femto_1_macro,"r_control");
		
		// action: send_femto_1_nr
		ldcq_0.getActionRates().put(send_femto_1_nr,passive);
		ldcq_0.getParameterNames().put(send_femto_1_nr,"r_passive");


		// action: stream_femto_1_nr
		ldcq_0.getActionRates().put(stream_femto_1_nr,passive);
		ldcq_0.getParameterNames().put(stream_femto_1_nr,"r_passive");

		
		// action: accept_wcu_femto_2_macro
		ldcq_0.getActionRates().put(accept_wcu_femto_2_macro,r_control);
		ldcq_0.getParameterNames().put(accept_wcu_femto_2_macro,"r_control");
		
		
		// action: send_femto_2_nr
		ldcq_0.getActionRates().put(send_femto_2_nr,passive);
		ldcq_0.getParameterNames().put(send_femto_2_nr,"r_passive");


		// action: stream_femto_2_nr
		ldcq_0.getActionRates().put(stream_femto_2_nr,passive);
		ldcq_0.getParameterNames().put(stream_femto_2_nr,"r_passive");

		
		
		
		//////////////////////////////////////////////////
		// local derivative: ldcq_1 
		/////////////////////////////////////////////////
		
		// action: send_macro_po 
		ldcq_1.getActionRates().put(send_macro_po,passive);
		ldcq_1.getParameterNames().put(send_macro_po,"passive");
		
		// action: reject_wcu_femto_1_macro
		ldcq_1.getActionRates().put(reject_wcu_femto_1_macro,r_control);
		ldcq_1.getParameterNames().put(reject_wcu_femto_1_macro,"r_control");
		
		// action: stream_wcu_femto_1_macro_po
		ldcq_1.getActionRates().put(stream_wcu_femto_1_macro_po,r_stream_wcu_femto_1_macro_po);
		ldcq_1.getParameterNames().put(stream_wcu_femto_1_macro_po,"r_stream_wcu_femto_1_macro_po");

		// action: send_femto_1_po
		ldcq_1.getActionRates().put(send_femto_1_po,passive);
		ldcq_1.getParameterNames().put(send_femto_1_po,"r_passive");

		
		// action: stream_femto_1_po
		ldcq_1.getActionRates().put(stream_femto_1_po,passive);
		ldcq_1.getParameterNames().put(stream_femto_1_po,"r_passive");

		
		// action: reject_wcu_femto_2_macro
		ldcq_1.getActionRates().put(reject_wcu_femto_2_macro,r_control);
		ldcq_1.getParameterNames().put(reject_wcu_femto_2_macro,"r_control");

		
		// action: stream_wcu_femto_2_macro_po
		ldcq_1.getActionRates().put(stream_wcu_femto_2_macro_po,r_stream_wcu_femto_2_macro_po);
		ldcq_1.getParameterNames().put(stream_wcu_femto_2_macro_po,"r_stream_wcu_femto_2_macro_po");


		// action: send_femto_2_po
		ldcq_1.getActionRates().put(send_femto_2_po,passive);
		ldcq_1.getParameterNames().put(send_femto_2_po,"r_passive");


		// action: stream_femto_2_po
		ldcq_1.getActionRates().put(stream_femto_2_po,passive);
		ldcq_1.getParameterNames().put(stream_femto_2_po,"r_passive");

		
		//////////////////////////////////////////////////
		// local derivative: ch_macro_idle
		/////////////////////////////////////////////////
		
		// action: send_macro_nr 
		ch_macro_idle.getActionRates().put(send_macro_nr,r_send_macro_nr);
		ch_macro_idle.getParameterNames().put(send_macro_nr,"r_send_macro_nr");
		
		// action: send_macro_po		
		ch_macro_idle.getActionRates().put(send_macro_po,r_send_macro_po);
		ch_macro_idle.getParameterNames().put(send_macro_po,"r_send_macro_po");

		
		//////////////////////////////////////////////////
		// local derivative: scu_femto_1_processing
		/////////////////////////////////////////////////
		
		// action: process_scu_femto_1
		scu_femto_1_processing.getActionRates().put(process_scu_femto_1,r_process_scu_femto_1);
		scu_femto_1_processing.getParameterNames().put(process_scu_femto_1,"r_process_scu_femto_1"); 
		

		//////////////////////////////////////////////////
		// local derivative: scu_femto_1_sending
		/////////////////////////////////////////////////
		
		// action: send_femto_1_nr
		scu_femto_1_sending.getActionRates().put(send_femto_1_nr,passive);
		scu_femto_1_sending.getParameterNames().put(send_femto_1_nr,"r_passive");
		
		// action: send_femto_1_po
		scu_femto_1_sending.getActionRates().put(send_femto_1_po,passive);
		scu_femto_1_sending.getParameterNames().put(send_femto_1_po,"r_passive");		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_1_processing
		/////////////////////////////////////////////////
		
		// action: process_wcu_femto_1_
		wcu_femto_1_processing.getActionRates().put(process_wcu_femto_1,r_process_wcu_femto_1);
		wcu_femto_1_processing.getParameterNames().put(process_wcu_femto_1,"r_process_wcu_femto_1"); 
			
		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_1_waiting_local
		/////////////////////////////////////////////////
		
		// action: accept_wcu_femto_1_local 
		wcu_femto_1_waiting_local.getActionRates().put(accept_wcu_femto_1_local,r_control);
		wcu_femto_1_waiting_local.getParameterNames().put(accept_wcu_femto_1_local,"r_control");
		
		// action: reject_wcu_femto_1_local
		wcu_femto_1_waiting_local.getActionRates().put(reject_wcu_femto_1_local,r_control);
		wcu_femto_1_waiting_local.getParameterNames().put(reject_wcu_femto_1_local,"r_control");

		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_1_streaming_local
		/////////////////////////////////////////////////
		
		// action: stream_femto_1_nr
		wcu_femto_1_streaming_local.getActionRates().put(stream_femto_1_nr,passive);
		wcu_femto_1_streaming_local.getParameterNames().put(stream_femto_1_nr,"r_passive");
		
		
		// action: stream_femto_1_po
		wcu_femto_1_streaming_local.getActionRates().put(stream_femto_1_po,passive);
		wcu_femto_1_streaming_local.getParameterNames().put(stream_femto_1_nr,"r_passive");
		
		
		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_1_waiting_macro
		/////////////////////////////////////////////////
		
		// action: accept_wcu_femto_1_macro
		wcu_femto_1_waiting_macro.getActionRates().put(accept_wcu_femto_1_macro,r_control);
		wcu_femto_1_waiting_macro.getParameterNames().put(accept_wcu_femto_1_macro,"r_control"); 
		
		// action: reject_wcu_femto_1_macro
		wcu_femto_1_waiting_macro.getActionRates().put(reject_wcu_femto_1_macro,r_control);
		wcu_femto_1_waiting_macro.getParameterNames().put(reject_wcu_femto_1_macro,"r_control"); 
		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_1_streaming_macro
		/////////////////////////////////////////////////
		
		// action: stream_wcu_femto_1_macro_po 
		wcu_femto_1_streaming_macro.getActionRates().put(stream_wcu_femto_1_macro_po,passive);
		wcu_femto_1_streaming_macro.getParameterNames().put(stream_wcu_femto_1_macro_po,"r_passive");
		
		
		
		//////////////////////////////////////////////////
		// local derivative: ch_femto_1_idle
		/////////////////////////////////////////////////
		
		// action: send_femto_1_nr
		ch_femto_1_idle.getActionRates().put(send_femto_1_nr,r_send_femto_1_nr);
		ch_femto_1_idle.getParameterNames().put(send_femto_1_nr,"r_send_femto_1_nr");
		
		// action: send_femto_1_po
		ch_femto_1_idle.getActionRates().put(send_femto_1_po,r_send_femto_1_po);
		ch_femto_1_idle.getParameterNames().put(send_femto_1_po,"r_send_femto_1_po");
				
		
		
		//////////////////////////////////////////////////
		// local derivative: wcuq_femto_1_0
		/////////////////////////////////////////////////
		
		// action: accept_wcu_femto_1_local 
		wcuq_femto_1_0.getActionRates().put(accept_wcu_femto_1_local,r_control);
		wcuq_femto_1_0.getParameterNames().put(accept_wcu_femto_1_local,"r_control");
		
		
		//////////////////////////////////////////////////
		// local derivative: wcuq_femto_1_1
		/////////////////////////////////////////////////
		
		// action: reject_wcu_femto_1_local 
		wcuq_femto_1_1.getActionRates().put(reject_wcu_femto_1_local,r_control);
		wcuq_femto_1_1.getParameterNames().put(reject_wcu_femto_1_local,"r_control");
	
		
		// action: stream_femto_1_nr 
		wcuq_femto_1_1.getActionRates().put(stream_femto_1_nr,r_stream_femto_1_nr);
		wcuq_femto_1_1.getParameterNames().put(stream_femto_1_nr,"r_stream_femto_1_nr");
			
		// action: stream_femto_1_po 
		wcuq_femto_1_1.getActionRates().put(stream_femto_1_po,r_stream_femto_1_po);
		wcuq_femto_1_1.getParameterNames().put(stream_femto_1_po,"r_stream_femto_1_po");
		
		
		//////////////////////////////////////////////////
		// local derivative: scu_femto_2_processing
		/////////////////////////////////////////////////
		
		// action: process_scu_femto_2
		scu_femto_2_processing.getActionRates().put(process_scu_femto_2,r_process_scu_femto_2);
		scu_femto_2_processing.getParameterNames().put(process_scu_femto_2,"r_process_scu_femto_2");

		
		//////////////////////////////////////////////////
		// local derivative: scu_femto_2_sending 
		/////////////////////////////////////////////////
		
		// action: send_femto_2_nr
		scu_femto_2_sending.getActionRates().put(send_femto_2_nr,r_send_femto_2_nr);
		scu_femto_2_sending.getParameterNames().put(send_femto_2_nr,"r_send_femto_2_nr");
		
		
		// action: send_femto_2_po
		scu_femto_2_sending.getActionRates().put(send_femto_2_po,r_send_femto_2_po);
		scu_femto_2_sending.getParameterNames().put(send_femto_2_po,"r_send_femto_2_po");

		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_2_processing
		/////////////////////////////////////////////////
		
		// action: process_wcu_femto_2
		wcu_femto_2_processing.getActionRates().put(process_wcu_femto_2,r_process_wcu_femto_2);
		wcu_femto_2_processing.getParameterNames().put(process_wcu_femto_2,"r_process_wcu_femto_2"); 
		
		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_2_waiting_local
		/////////////////////////////////////////////////
		
		// action: accept_wcu_femto_2_local
		wcu_femto_2_waiting_local.getActionRates().put(accept_wcu_femto_2_local,r_control);
		wcu_femto_2_waiting_local.getParameterNames().put(accept_wcu_femto_2_local,"r_control");
		
		
		// action: reject_wcu_femto_2_local
		wcu_femto_2_waiting_local.getActionRates().put(reject_wcu_femto_2_local,r_control);
		wcu_femto_2_waiting_local.getParameterNames().put(reject_wcu_femto_2_local,"r_control");
				
			
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_2_streaming_local
		/////////////////////////////////////////////////
		
		// action: stream_femto_2_nr
		wcu_femto_2_streaming_local.getActionRates().put(stream_femto_2_nr,passive);
		wcu_femto_2_streaming_local.getParameterNames().put(stream_femto_2_nr,"r_passive");


		// action: stream_femto_2_po
		wcu_femto_2_streaming_local.getActionRates().put(stream_femto_2_po,passive);
		wcu_femto_2_streaming_local.getParameterNames().put(stream_femto_2_po,"r_passive");
		
		
		
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_2_waiting_macro
		/////////////////////////////////////////////////
		
		// action: accept_wcu_femto_2_macro 
		wcu_femto_2_waiting_macro.getActionRates().put(accept_wcu_femto_2_macro,r_control);
		wcu_femto_2_waiting_macro.getParameterNames().put(accept_wcu_femto_2_macro,"r_control");
		
		// action: reject_wcu_femto_2_macro 
		wcu_femto_2_waiting_macro.getActionRates().put(reject_wcu_femto_2_macro,r_control);
		wcu_femto_2_waiting_macro.getParameterNames().put(reject_wcu_femto_2_macro,"r_control");	
	
	
		//////////////////////////////////////////////////
		// local derivative: wcu_femto_2_streaming_macro
		/////////////////////////////////////////////////
		
		// action: stream_wcu_femto_2_macro_po 
		wcu_femto_2_streaming_macro.getActionRates().put(stream_wcu_femto_2_macro_po,passive);
		wcu_femto_2_streaming_macro.getParameterNames().put(stream_wcu_femto_2_macro_po,"r_passive");
		
		
		//////////////////////////////////////////////////
		// local derivative: ch_femto_2_idle
		/////////////////////////////////////////////////

		// action: send_femto_2_nr 
		ch_femto_2_idle.getActionRates().put(send_femto_2_nr,r_send_femto_2_nr);
		ch_femto_2_idle.getParameterNames().put(send_femto_2_nr,"r_send_femto_2_nr");
		
		
		// action: send_femto_2_po 
		ch_femto_2_idle.getActionRates().put(send_femto_2_po,r_send_femto_2_po);
		ch_femto_2_idle.getParameterNames().put(send_femto_2_po,"r_send_femto_2_po");
					
		
		//////////////////////////////////////////////////
		// local derivative: wcuq_femto_2_0
		/////////////////////////////////////////////////
		
		// action: aceept_wcu_femto_2_local 
		wcuq_femto_2_0.getActionRates().put(accept_wcu_femto_2_local,r_control);
		wcuq_femto_2_0.getParameterNames().put(accept_wcu_femto_2_local,"r_control");
		
				
		
		//////////////////////////////////////////////////
		// local derivative: wcuq_femto_2_1
		/////////////////////////////////////////////////
		
		// action: reject_wcu_femto_2_local 
		wcuq_femto_2_1.getActionRates().put(reject_wcu_femto_2_local,r_control);
		wcuq_femto_2_1.getParameterNames().put(reject_wcu_femto_2_local,"r_control"); 
		
		//action: stream_femto_2_nr
		wcuq_femto_2_1.getActionRates().put(stream_femto_2_nr,r_stream_femto_2_nr);
		wcuq_femto_2_1.getParameterNames().put(stream_femto_2_nr,"r_stream_femto_2_nr");
		
		//action: stream_femto_2_po
		wcuq_femto_2_1.getActionRates().put(stream_femto_2_po,r_stream_femto_2_po);
		wcuq_femto_2_1.getParameterNames().put(stream_femto_2_po,"r_stream_femto_2_po");
		
		
		// local derivative Server_idle and the action request
		
		// constructing the model
		OriginalModel model = new OriginalModel (localDerivatives,descriptor,initialState,actions,largeGroups,smallGroups,constants);

		Display display = new Display(model);
		model.setDisplay(display);

		return model;
		
	}
	
	
}
