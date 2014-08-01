function marginal_distribution_conditional_expectation

r_f = 11 ;
r_l = 5 ;
r_t = 13 ;
r_b = 7 ;
r_s = 2 ;


k={'Servers_Server_idle' , 'Servers_Server_log' , 'Servers_Server_brk'};

v={1,0,0};
st1 = containers.Map(k,v);

v={0,0,1};
st2 = containers.Map(k,v);

v={0,1,0};
st3 = containers.Map(k,v);

conditionals_key = { 'Clients_Client_think' , 'Clients_Client_req' } ;

tspan = linspace(0.000000,9000.000000,1000);

Initial conditions associate with time point, t = ? 

init_prob_st_1 = 1 ;
init_prob_st_2 = 0 ;
init_prob_st_3 = 0 ;



y0 = [ init_prob_st_1  ;       % index 1
init_prob_st_2  ;     %   index 2
init_prob_st_3  ;     %   index 3
init_con_E_Clients_Client_think_st_1  ;     %   index 4
init_con_E_Clients_Client_req_st_1  ;     %   index 5
init_con_E_Clients_Client_think_st_2  ;     %   index 6
init_con_E_Clients_Client_req_st_2  ;     %   index 7
init_con_E_Clients_Client_think_st_3  ;     %   index 8
init_con_E_Clients_Client_req_st_3   %   index 9  
	 ]  ;

options=odeset('Mass',@mass,'RelTol',1.0E-10,'AbsTol',[1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 ]);

[t,y] = ode15s(@derivatives,tspan,y0,options);

%---------------------------------------------------------

function  rate = rate_log(state)

	rate = ( r_l * state('Servers_Server_log') ) ;

end

function  rate = rate_break(state)

	rate = ( r_b * state('Servers_Server_idle') ) ;

end

function  rate = rate_fix(state)

	rate = ( r_f * state('Servers_Server_brk') ) ;

end

function  rate = rate_request(state)

	rate = ( r_s * state('Servers_Server_idle') ) ;

end

function  rate = rate_think(moments)

	state_conditionals = containers.Map(conditionals_key,moments) ; 

	rate = ( r_t * state_conditionals('Clients_Client_think') ) ;

end



function dydt = derivatives(t,y) 

 	dydt = zeros(9,1);

	dydt(1) =  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) ; 

	dydt(2) =  - ( rate_fix(st2) * y(2) ) + ( rate_break(st1) * y(1) ) ; 

	dydt(3) =  - ( rate_log(st3) * y(3) ) + ( rate_request(st1) * y(1) ) ; 

	dydt(4) = ( - (  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) )  * y(4) )  ...
		 + ( - y(1) * y(4) * (  rate_break(st1) +  rate_request(st1) ) ) ...
		 + ( y(2) * rate_fix(st2) * y(6) ) + ( y(3) * rate_log(st3) * y(8) ) ...
		 + 0 ...
		 + y(1) * (-1) * rate_think({y(4) , y(5)}) ; 

	dydt(5) = ( - (  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) )  * y(5) )  ...
		 + ( - y(1) * y(5) * (  rate_break(st1) +  rate_request(st1) ) ) ...
		 + ( y(2) * rate_fix(st2) * y(7) ) + ( y(3) * rate_log(st3) * y(9) ) ...
		 + 0 ...
		 + y(1) * (1) * rate_think({y(4) , y(5)}) ; 

	dydt(6) = ( - (  - ( rate_fix(st2) * y(2) ) + ( rate_break(st1) * y(1) ) )  * y(6) )  ...
		 + ( - y(2) * y(6) * (  rate_fix(st2) ) ) ...
		 + ( y(1) * rate_break(st1) * y(4) ) ...
		 + 0 ...
		 + y(2) * (-1) * rate_think({y(6) , y(7)}) ; 

	dydt(7) = ( - (  - ( rate_fix(st2) * y(2) ) + ( rate_break(st1) * y(1) ) )  * y(7) )  ...
		 + ( - y(2) * y(7) * (  rate_fix(st2) ) ) ...
		 + ( y(1) * rate_break(st1) * y(5) ) ...
		 + 0 ...
		 + y(2) * (1) * rate_think({y(6) , y(7)}) ; 

	dydt(8) = ( - (  - ( rate_log(st3) * y(3) ) + ( rate_request(st1) * y(1) ) )  * y(8) )  ...
		 + ( - y(3) * y(8) * (  rate_log(st3) ) ) ...
		 + ( y(1) * rate_request(st1) * y(4) ) ...
		 + y(1) * rate_request(st1) * ( 1 )  ...
		 + y(3) * (-1) * rate_think({y(8) , y(9)}) ; 

	dydt(9) = ( - (  - ( rate_log(st3) * y(3) ) + ( rate_request(st1) * y(1) ) )  * y(9) )  ...
		 + ( - y(3) * y(9) * (  rate_log(st3) ) ) ...
		 + ( y(1) * rate_request(st1) * y(5) ) ...
		 + y(1) * rate_request(st1) * ( -1 )  ...
		 + y(3) * (1) * rate_think({y(8) , y(9)}) ; 


end

function M = mass(t,y)

	M = zeros(9,9);
	M(1,1)=1;
	M(2,2)=1;
	M(3,3)=1;
	M(4,4)=y(1);
	M(5,5)=y(1);
	M(6,6)=y(2);
	M(7,7)=y(2);
	M(8,8)=y(3);
	M(9,9)=y(3);

end

figure;
plot(t,y(:,1),'-b','DisplayName','prob_st_1')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,2),'-b','DisplayName','prob_st_2')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,3),'-b','DisplayName','prob_st_3')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,4),'-b','DisplayName','con_E_Clients_Client_think_st_1')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,5),'-b','DisplayName','con_E_Clients_Client_req_st_1')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,6),'-b','DisplayName','con_E_Clients_Client_think_st_2')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,7),'-b','DisplayName','con_E_Clients_Client_req_st_2')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,8),'-b','DisplayName','con_E_Clients_Client_think_st_3')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,9),'-b','DisplayName','con_E_Clients_Client_req_st_3')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;



for i=1:length(t)
	output(i,1) = t(i);
end

for i=1:length(t)
	output(i,2) = y(i,1);
end
csvwrite('prob_st_1.dat',output);

for i=1:length(t)
	output(i,2) = y(i,2);
end
csvwrite('prob_st_2.dat',output);

for i=1:length(t)
	output(i,2) = y(i,3);
end
csvwrite('prob_st_3.dat',output);

for i=1:length(t)
	output(i,2) = y(i,4);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_1.dat',output);

for i=1:length(t)
	output(i,2) = y(i,5);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_1.dat',output);

for i=1:length(t)
	output(i,2) = y(i,6);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_2.dat',output);

for i=1:length(t)
	output(i,2) = y(i,7);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_2.dat',output);

for i=1:length(t)
	output(i,2) = y(i,8);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_3.dat',output);

for i=1:length(t)
	output(i,2) = y(i,9);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_3.dat',output);





end
