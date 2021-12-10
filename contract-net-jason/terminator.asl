+end(X,Y) <- !verify.

+!verify : .count(end(A,B)[source(C)], N) & limit(X) & X == N <- .stopMAS.
+!verify.
