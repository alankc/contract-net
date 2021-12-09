// gets the price for the product,
// a random value between 100 and 110.
price(Service,X) :- i_do(Service) & not working & .random(R) & X = (10*R)+100.

!register.

+!register <- .df_register("participant");
              .df_subscribe("initiator").

// answer to Call For Proposal
@c1 +cfp(CNPId,Task)[source(A)]
   :  provider(A,"initiator") &
      price(Task,Offer)
   <- +proposal(CNPId,Task,Offer); // remember my proposal
      .send(A,tell,propose(CNPId,Offer)).

@c2 +cfp(CNPId,Task)[source(A)]
   :  provider(A,"initiator") &
      not price(Task,Offer)
   <- +refuse(CNPId,Task); // remember my refuse
      .send(A,tell,refuse(CNPId,Offer)).

@r1 +accept_proposal(CNPId)[source(A)]
   :  proposal(CNPId,Task,Offer)
   <- //.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!");
   		+working;
      	.send(A, tell, infom_result(CNPId, 28.10));
		-working.

@r2 +reject_proposal(CNPId)
   <- //.print("I lost CNP ",CNPId, ".");
      -proposal(CNPId,_,_). // clear memory
