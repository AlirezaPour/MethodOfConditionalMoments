function marginal_distribution

r_f = 11 ;
r_l = 5 ;
r_b = 7 ;
r_s = 2 ;


k={'Servers_Server_idle' , 'Servers_Server_log' , 'Servers_Server_brk'};

v={2,0,0};
st1 = containers.Map(k,v);

v={1,1,0};
st2 = containers.Map(k,v);

v={1,0,1};
st3 = containers.Map(k,v);

v={0,2,0};
st4 = containers.Map(k,v);

v={0,1,1};
st5 = containers.Map(k,v);

v={0,0,2};
st6 = containers.Map(k,v);

tspan = linspace(0.000000,9000.000000,1000);

init_prob_st_1 = 1 ;
init_prob_st_2 = 0 ;
init_prob_st_3 = 0 ;
init_prob_st_4 = 0 ;
init_prob_st_5 = 0 ;
init_prob_st_6 = 0 ;

y0 = [ init_prob_st_1 ; init_prob_st_2 ; init_prob_st_3 ; init_prob_st_4 ; init_prob_st_5 ; init_prob_st_6 ] ;

options=odeset('Mass',@mass,'RelTol',1.0E-10,'AbsTol',[1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 1.0E-6 ]);

[t,y] = ode15s(@derivatives,tspan,y0,options);

function  rate = rate_request(state)

	rate = ( r_s * state('Servers_Server_idle') ) ;

end

function  rate = rate_log(state)

	rate = ( r_l * state('Servers_Server_log') ) ;

end

function  rate = rate_break(state)

	rate = ( r_b * state('Servers_Server_idle') ) ;

end

function  rate = rate_fix(state)

	rate = ( r_f * state('Servers_Server_brk') ) ;

end

function dydt = derivatives(t,y) 

 	dydt = zeros(6,1);

	dydt(1)= - rate_request(st1) * y(1) - rate_break(st1) * y(1) + rate_log(st2) * y(2) + rate_fix(st3) * y(3);
	dydt(2)= - rate_request(st2) * y(2) - rate_log(st2) * y(2) - rate_break(st2) * y(2) + rate_request(st1) * y(1) + rate_log(st4) * y(4) + rate_fix(st5) * y(5);
	dydt(3)= - rate_request(st3) * y(3) - rate_break(st3) * y(3) - rate_fix(st3) * y(3) + rate_break(st1) * y(1) + rate_log(st5) * y(5) + rate_fix(st6) * y(6);
	dydt(4)= - rate_log(st4) * y(4) + rate_request(st2) * y(2);
	dydt(5)= - rate_log(st5) * y(5) - rate_fix(st5) * y(5) + rate_break(st2) * y(2) + rate_request(st3) * y(3);
	dydt(6)= - rate_fix(st6) * y(6) + rate_break(st3) * y(3);

end

function M = mass(t,y)

	M = zeros(6,6);
	M(1,1)=1;
	M(2,2)=1;
	M(3,3)=1;
	M(4,4)=1;
	M(5,5)=1;
	M(6,6)=1;

end

figure;
plot(t,y(:,1),'-b','DisplayName','P_{t}(St1)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,2),'-b','DisplayName','P_{t}(St2)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,3),'-b','DisplayName','P_{t}(St3)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,4),'-b','DisplayName','P_{t}(St4)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,5),'-b','DisplayName','P_{t}(St5)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;

figure;
plot(t,y(:,6),'-b','DisplayName','P_{t}(St6)')
legend('-DynamicLegend');
legend('Location', 'BestOutside');
grid on;



for i=1:length(t)
	output(i,1) = t(i);
end

for i=1:length(t)
	output(i,2) = y(i,1);
end
csvwrite('P(st1).dat',output);

for i=1:length(t)
	output(i,2) = y(i,2);
end
csvwrite('P(st2).dat',output);

for i=1:length(t)
	output(i,2) = y(i,3);
end
csvwrite('P(st3).dat',output);

for i=1:length(t)
	output(i,2) = y(i,4);
end
csvwrite('P(st4).dat',output);

for i=1:length(t)
	output(i,2) = y(i,5);
end
csvwrite('P(st5).dat',output);

for i=1:length(t)
	output(i,2) = y(i,6);
end
csvwrite('P(st6).dat',output);





end
