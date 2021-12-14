//Agent used to finish the mas when all CInitiators have sent selection results to Participants
+end(X,Y) <- !verify.

+!verify : .count(end(A,B)[source(C)], N) & limit(X) & X == N <- .stopMAS.
+!verify.
