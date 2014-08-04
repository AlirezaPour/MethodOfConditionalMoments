function marginal_distribution_conditional_expectation

r_f = 8.0E-4 ;
r_l = 250.0 ;
r_t = 3.0 ;
r_b = 3.0E-4 ;
r_s = 100.0 ;


k={'Servers_Server_idle' , 'Servers_Server_log' , 'Servers_Server_brk'};

v={2,0,0};
st1 = containers.Map(k,v);

v={1,0,1};
st2 = containers.Map(k,v);

v={1,1,0};
st3 = containers.Map(k,v);

v={0,0,2};
st4 = containers.Map(k,v);

v={0,1,1};
st5 = containers.Map(k,v);

v={0,2,0};
st6 = containers.Map(k,v);

conditionals_key = { 'Clients_Client_think' , 'Clients_Client_req' } ;

tspan = linspace(0.000000,9000.000000,1000);

% Initial conditions associate with time point, t = ? 

init_prob_st_1 = 1 ;
init_prob_st_2 = 0 ;
init_prob_st_3 = 0 ;
init_prob_st_4 = 0 ;
init_prob_st_5 = 0 ;
init_prob_st_6 = 0 ;




u_con_E_Clients_Client_think_st_1 = ? ;
init_con_E_Clients_Client_think_st_1 =  ( u_con_E_Clients_Client_think_st_1 )  /  ( init_prob_st_1 )  ; 

u_con_E_Clients_Client_req_st_1 = ? ;
init_con_E_Clients_Client_req_st_1 =  ( u_con_E_Clients_Client_req_st_1 )  /  ( init_prob_st_1 )  ; 



u_con_E_Clients_Client_think_st_2 = ? ;
init_con_E_Clients_Client_think_st_2 =  ( u_con_E_Clients_Client_think_st_2 )  /  ( init_prob_st_2 )  ; 

u_con_E_Clients_Client_req_st_2 = ? ;
init_con_E_Clients_Client_req_st_2 =  ( u_con_E_Clients_Client_req_st_2 )  /  ( init_prob_st_2 )  ; 



u_con_E_Clients_Client_think_st_3 = ? ;
init_con_E_Clients_Client_think_st_3 =  ( u_con_E_Clients_Client_think_st_3 )  /  ( init_prob_st_3 )  ; 

u_con_E_Clients_Client_req_st_3 = ? ;
init_con_E_Clients_Client_req_st_3 =  ( u_con_E_Clients_Client_req_st_3 )  /  ( init_prob_st_3 )  ; 



u_con_E_Clients_Client_think_st_4 = ? ;
init_con_E_Clients_Client_think_st_4 =  ( u_con_E_Clients_Client_think_st_4 )  /  ( init_prob_st_4 )  ; 

u_con_E_Clients_Client_req_st_4 = ? ;
init_con_E_Clients_Client_req_st_4 =  ( u_con_E_Clients_Client_req_st_4 )  /  ( init_prob_st_4 )  ; 



u_con_E_Clients_Client_think_st_5 = ? ;
init_con_E_Clients_Client_think_st_5 =  ( u_con_E_Clients_Client_think_st_5 )  /  ( init_prob_st_5 )  ; 

u_con_E_Clients_Client_req_st_5 = ? ;
init_con_E_Clients_Client_req_st_5 =  ( u_con_E_Clients_Client_req_st_5 )  /  ( init_prob_st_5 )  ; 



u_con_E_Clients_Client_think_st_6 = ? ;
init_con_E_Clients_Client_think_st_6 =  ( u_con_E_Clients_Client_think_st_6 )  /  ( init_prob_st_6 )  ; 

u_con_E_Clients_Client_req_st_6 = ? ;
init_con_E_Clients_Client_req_st_6 =  ( u_con_E_Clients_Client_req_st_6 )  /  ( init_prob_st_6 )  ; 


y0 = [ 	init_prob_st_1  ;       % index 1
	init_prob_st_2  ;     %   index 2
	init_prob_st_3  ;     %   index 3
	init_prob_st_4  ;     %   index 4
	init_prob_st_5  ;     %   index 5
	init_prob_st_6  ;     %   index 6
	init_con_E_Clients_Client_think_st_1  ;     %   index 7
	init_con_E_Clients_Client_req_st_1  ;     %   index 8
	init_con_E_Clients_Client_think_st_2  ;     %   index 9
	init_con_E_Clients_Client_req_st_2  ;     %   index 10
	init_con_E_Clients_Client_think_st_3  ;     %   index 11
	init_con_E_Clients_Client_req_st_3  ;     %   index 12
	init_con_E_Clients_Client_think_st_4  ;     %   index 13
	init_con_E_Clients_Client_req_st_4  ;     %   index 14
	init_con_E_Clients_Client_think_st_5  ;     %   index 15
	init_con_E_Clients_Client_req_st_5  ;     %   index 16
	init_con_E_Clients_Client_think_st_6  ;     %   index 17
	init_con_E_Clients_Client_req_st_6   %   index 18  
	 ]  ;

options=odeset('Mass',@mass,'RelTol',1.0E-10,'AbsTol',[1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 ]);

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

 	dydt = zeros(18,1);

	dydt(1) =  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) ; 

	dydt(2) =  - ( rate_break(st2) * y(2) ) - ( rate_fix(st2) * y(2) ) - ( rate_request(st2) * y(2) ) + ( rate_break(st1) * y(1) ) + ( rate_fix(st4) * y(4) ) + ( rate_log(st5) * y(5) ) ; 

	dydt(3) =  - ( rate_log(st3) * y(3) ) - ( rate_break(st3) * y(3) ) - ( rate_request(st3) * y(3) ) + ( rate_request(st1) * y(1) ) + ( rate_fix(st5) * y(5) ) + ( rate_log(st6) * y(6) ) ; 

	dydt(4) =  - ( rate_fix(st4) * y(4) ) + ( rate_break(st2) * y(2) ) ; 

	dydt(5) =  - ( rate_log(st5) * y(5) ) - ( rate_fix(st5) * y(5) ) + ( rate_request(st2) * y(2) ) + ( rate_break(st3) * y(3) ) ; 

	dydt(6) =  - ( rate_log(st6) * y(6) ) + ( rate_request(st3) * y(3) ) ; 

	dydt(7) = ( - (  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) )  * y(7) )  ...
		 + ( - y(1) * y(7) * (  rate_break(st1) +  rate_request(st1) ) ) ...
		 + ( y(2) * rate_fix(st2) * y(9) ) + ( y(3) * rate_log(st3) * y(11) ) ...
		 + 0 ...
		 + y(1) * (-1) * rate_think({y(7) , y(8)}) ; 

	dydt(8) = ( - (  - ( rate_break(st1) * y(1) ) - ( rate_request(st1) * y(1) ) + ( rate_fix(st2) * y(2) ) + ( rate_log(st3) * y(3) ) )  * y(8) )  ...
		 + ( - y(1) * y(8) * (  rate_break(st1) +  rate_request(st1) ) ) ...
		 + ( y(2) * rate_fix(st2) * y(10) ) + ( y(3) * rate_log(st3) * y(12) ) ...
		 + 0 ...
		 + y(1) * (1) * rate_think({y(7) , y(8)}) ; 

	dydt(9) = ( - (  - ( rate_break(st2) * y(2) ) - ( rate_fix(st2) * y(2) ) - ( rate_request(st2) * y(2) ) + ( rate_break(st1) * y(1) ) + ( rate_fix(st4) * y(4) ) + ( rate_log(st5) * y(5) ) )  * y(9) )  ...
		 + ( - y(2) * y(9) * (  rate_break(st2) +  rate_fix(st2) +  rate_request(st2) ) ) ...
		 + ( y(1) * rate_break(st1) * y(7) ) + ( y(4) * rate_fix(st4) * y(13) ) + ( y(5) * rate_log(st5) * y(15) ) ...
		 + 0 ...
		 + y(2) * (-1) * rate_think({y(9) , y(10)}) ; 

	dydt(10) = ( - (  - ( rate_break(st2) * y(2) ) - ( rate_fix(st2) * y(2) ) - ( rate_request(st2) * y(2) ) + ( rate_break(st1) * y(1) ) + ( rate_fix(st4) * y(4) ) + ( rate_log(st5) * y(5) ) )  * y(10) )  ...
		 + ( - y(2) * y(10) * (  rate_break(st2) +  rate_fix(st2) +  rate_request(st2) ) ) ...
		 + ( y(1) * rate_break(st1) * y(8) ) + ( y(4) * rate_fix(st4) * y(14) ) + ( y(5) * rate_log(st5) * y(16) ) ...
		 + 0 ...
		 + y(2) * (1) * rate_think({y(9) , y(10)}) ; 

	dydt(11) = ( - (  - ( rate_log(st3) * y(3) ) - ( rate_break(st3) * y(3) ) - ( rate_request(st3) * y(3) ) + ( rate_request(st1) * y(1) ) + ( rate_fix(st5) * y(5) ) + ( rate_log(st6) * y(6) ) )  * y(11) )  ...
		 + ( - y(3) * y(11) * (  rate_log(st3) +  rate_break(st3) +  rate_request(st3) ) ) ...
		 + ( y(1) * rate_request(st1) * y(7) ) + ( y(5) * rate_fix(st5) * y(15) ) + ( y(6) * rate_log(st6) * y(17) ) ...
		 + y(1) * rate_request(st1) * ( 1 )  ...
		 + y(3) * (-1) * rate_think({y(11) , y(12)}) ; 

	dydt(12) = ( - (  - ( rate_log(st3) * y(3) ) - ( rate_break(st3) * y(3) ) - ( rate_request(st3) * y(3) ) + ( rate_request(st1) * y(1) ) + ( rate_fix(st5) * y(5) ) + ( rate_log(st6) * y(6) ) )  * y(12) )  ...
		 + ( - y(3) * y(12) * (  rate_log(st3) +  rate_break(st3) +  rate_request(st3) ) ) ...
		 + ( y(1) * rate_request(st1) * y(8) ) + ( y(5) * rate_fix(st5) * y(16) ) + ( y(6) * rate_log(st6) * y(18) ) ...
		 + y(1) * rate_request(st1) * ( -1 )  ...
		 + y(3) * (1) * rate_think({y(11) , y(12)}) ; 

	dydt(13) = ( - (  - ( rate_fix(st4) * y(4) ) + ( rate_break(st2) * y(2) ) )  * y(13) )  ...
		 + ( - y(4) * y(13) * (  rate_fix(st4) ) ) ...
		 + ( y(2) * rate_break(st2) * y(9) ) ...
		 + 0 ...
		 + y(4) * (-1) * rate_think({y(13) , y(14)}) ; 

	dydt(14) = ( - (  - ( rate_fix(st4) * y(4) ) + ( rate_break(st2) * y(2) ) )  * y(14) )  ...
		 + ( - y(4) * y(14) * (  rate_fix(st4) ) ) ...
		 + ( y(2) * rate_break(st2) * y(10) ) ...
		 + 0 ...
		 + y(4) * (1) * rate_think({y(13) , y(14)}) ; 

	dydt(15) = ( - (  - ( rate_log(st5) * y(5) ) - ( rate_fix(st5) * y(5) ) + ( rate_request(st2) * y(2) ) + ( rate_break(st3) * y(3) ) )  * y(15) )  ...
		 + ( - y(5) * y(15) * (  rate_log(st5) +  rate_fix(st5) ) ) ...
		 + ( y(2) * rate_request(st2) * y(9) ) + ( y(3) * rate_break(st3) * y(11) ) ...
		 + y(2) * rate_request(st2) * ( 1 )  ...
		 + y(5) * (-1) * rate_think({y(15) , y(16)}) ; 

	dydt(16) = ( - (  - ( rate_log(st5) * y(5) ) - ( rate_fix(st5) * y(5) ) + ( rate_request(st2) * y(2) ) + ( rate_break(st3) * y(3) ) )  * y(16) )  ...
		 + ( - y(5) * y(16) * (  rate_log(st5) +  rate_fix(st5) ) ) ...
		 + ( y(2) * rate_request(st2) * y(10) ) + ( y(3) * rate_break(st3) * y(12) ) ...
		 + y(2) * rate_request(st2) * ( -1 )  ...
		 + y(5) * (1) * rate_think({y(15) , y(16)}) ; 

	dydt(17) = ( - (  - ( rate_log(st6) * y(6) ) + ( rate_request(st3) * y(3) ) )  * y(17) )  ...
		 + ( - y(6) * y(17) * (  rate_log(st6) ) ) ...
		 + ( y(3) * rate_request(st3) * y(11) ) ...
		 + y(3) * rate_request(st3) * ( 1 )  ...
		 + y(6) * (-1) * rate_think({y(17) , y(18)}) ; 

	dydt(18) = ( - (  - ( rate_log(st6) * y(6) ) + ( rate_request(st3) * y(3) ) )  * y(18) )  ...
		 + ( - y(6) * y(18) * (  rate_log(st6) ) ) ...
		 + ( y(3) * rate_request(st3) * y(12) ) ...
		 + y(3) * rate_request(st3) * ( -1 )  ...
		 + y(6) * (1) * rate_think({y(17) , y(18)}) ; 


end

function M = mass(t,y)

	M = zeros(18,18);
	M(1,1)=1;
	M(2,2)=1;
	M(3,3)=1;
	M(4,4)=1;
	M(5,5)=1;
	M(6,6)=1;
	M(7,7)=y(1);
	M(8,8)=y(1);
	M(9,9)=y(2);
	M(10,10)=y(2);
	M(11,11)=y(3);
	M(12,12)=y(3);
	M(13,13)=y(4);
	M(14,14)=y(4);
	M(15,15)=y(5);
	M(16,16)=y(5);
	M(17,17)=y(6);
	M(18,18)=y(6);

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
plot(t,y(:,4),'-b','DisplayName','prob_st_4')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,5),'-b','DisplayName','prob_st_5')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,6),'-b','DisplayName','prob_st_6')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,7),'-b','DisplayName','con_E_Clients_Client_think_st_1')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,8),'-b','DisplayName','con_E_Clients_Client_req_st_1')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,9),'-b','DisplayName','con_E_Clients_Client_think_st_2')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,10),'-b','DisplayName','con_E_Clients_Client_req_st_2')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,11),'-b','DisplayName','con_E_Clients_Client_think_st_3')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,12),'-b','DisplayName','con_E_Clients_Client_req_st_3')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,13),'-b','DisplayName','con_E_Clients_Client_think_st_4')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,14),'-b','DisplayName','con_E_Clients_Client_req_st_4')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,15),'-b','DisplayName','con_E_Clients_Client_think_st_5')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,16),'-b','DisplayName','con_E_Clients_Client_req_st_5')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,17),'-b','DisplayName','con_E_Clients_Client_think_st_6')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,18),'-b','DisplayName','con_E_Clients_Client_req_st_6')
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
csvwrite('prob_st_4.dat',output);

for i=1:length(t)
	output(i,2) = y(i,5);
end
csvwrite('prob_st_5.dat',output);

for i=1:length(t)
	output(i,2) = y(i,6);
end
csvwrite('prob_st_6.dat',output);

for i=1:length(t)
	output(i,2) = y(i,7);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_1.dat',output);

for i=1:length(t)
	output(i,2) = y(i,8);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_1.dat',output);

for i=1:length(t)
	output(i,2) = y(i,9);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_2.dat',output);

for i=1:length(t)
	output(i,2) = y(i,10);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_2.dat',output);

for i=1:length(t)
	output(i,2) = y(i,11);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_3.dat',output);

for i=1:length(t)
	output(i,2) = y(i,12);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_3.dat',output);

for i=1:length(t)
	output(i,2) = y(i,13);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_4.dat',output);

for i=1:length(t)
	output(i,2) = y(i,14);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_4.dat',output);

for i=1:length(t)
	output(i,2) = y(i,15);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_5.dat',output);

for i=1:length(t)
	output(i,2) = y(i,16);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_5.dat',output);

for i=1:length(t)
	output(i,2) = y(i,17);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_think_st_6.dat',output);

for i=1:length(t)
	output(i,2) = y(i,18);
end
csvwrite('MCM_Analysis_Output/con_E_Clients_Client_req_st_6.dat',output);





end
