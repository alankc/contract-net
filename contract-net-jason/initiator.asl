//Code adapted from Jason contract-net example

//Initial beliefs and goals
!register.
//!launch_cfp(1, buy(car)).
//!launch_cfp(2, buy(banana)).
//!launch_cfp(3, buy(bola)).

//Test, all proposals has arrived
all_proposals(Id, NP)
	:- 	.count(propose(Id,_)[source(_)], Npr) &
		.count(refuse(Id,_)[source(_)], Nr) &
		NP == Npr + Nr.

concluded(Id, WinAg, Res)
	:- 	failure(Id)[source(WinAg)] |
		infom_done(Id)[source(WinAg)] |
		infom_result(Id, Res)[source(WinAg)].

//Registering
+!register
	<- 	.df_register("initiator").

//Launch CFP and measure time
+!l_cfp (Id, Task)
	<- 	.wait(1000);
		.time(HH_ini,MM_ini,SS_ini,MS_ini);
		!cfp(Id, Task);
		.time(HH_fim,MM_fim,SS_fim,MS_fim);
		TimeF = HH_fim * 3600 + MM_fim * 60 + SS_fim + MS_fim / 1000.0;
		TimeI = HH_ini * 3600 + MM_ini * 60 + SS_ini + MS_ini / 1000.0;
		.print("	", (TimeF - TimeI) * 1000).

+!cfp(Id, Task)
	<- 	!call(Id, Task, LP);
		!bid(Id, LP);
		!select_bid(Id, ListOff, WinAg);
		!send_result(Id, ListOff, WinAg);
    .send(terminator, tell, end(Id,Task)).
		//!wait_conclusion(Id, WinAg).//;
		//!conclusion(Id, WinAg).

+!call(Id, Task, LP)
	<- 	//.print("cfp(",Id, ", ", Task, ")");
		.df_search("participant", LP);
		//.print("Sending CFP to ", LP);
		.send(LP, tell, cfp(Id, Task)).

+!bid(Id, LP)
	<- .wait(all_proposals(Id, .length(LP)), 5000, ElipsedTime).

//If, there is proposals, define a winner
+!select_bid(Id, ListOff, WinAg)
	: .findall(offer(O,A), propose(Id,O)[source(A)], ListOff) & ListOff \== []
	<- 	.min(ListOff, offer(WinOff, WinAg)).
+!select_bid(_, _, noWinner).

+!send_result(_,[],_). //If there is no offers, do nothing
+!send_result(Id, [offer(_, WinAg) | LosAg], WinAg)
	<- 	.send(WinAg, tell, accept_proposal(Id));
		!send_result(Id, LosAg, WinAg).
+!send_result(Id, [offer(_, Ag) | LosAg], WinAg)
	<- 	.send(Ag, tell, reject_proposal(Id));
		!send_result(Id, LosAg, WinAg).

+!wait_conclusion(_, noWinner).
+!wait_conclusion(Id, WinAg)
	<- 	.wait(concluded(Id, WinAg, Res), 5000, ElipsedTime).

+!conclusion(_, noWinner).
+!conclusion(Id, WinAg)
	: failure(Id)[source(A)] & A == WinAg
	<- .print("o ", A, " falhou ao executar ", Id).
+!conclusion(Id, WinAg)
	: infom_done(Id)[source(A)] & A == WinAg
	<- .print("O ", A, " terminou de execucar ", Id).
+!conclusion(Id, WinAg)
	: infom_result(Id, Res)[source(A)] & A == WinAg
	<- .print("O ", A, " terminou de executar (", Id, ") com resultado ", Res).
+!conclusion(Id, WinAg)
	<- .print("TIME OUT (", Id, ") realizado por ", WinAg).
