
program(program(FL)) --> functionList(FL).

%  function LIST
functionList(F) --> function(F). 
functionList((F,FL)) --> function(F), functionList(FL).

%  function STRUCTURE
function(func(S, BS)) --> signature(S), blockScope(BS). 
signature(sign(T, I, AL)) --> type(T), id(I), ['('], argList(AL), [')']. 
argList((A)) --> argv(A). 
argList(argList(A, AL)) --> argv(A), [','], argList(AL).
argv(argv(T, I)) --> type(T), id(I). 

%  SCOPE
blockScope(block(B)) --> ['{'], block(B), ['}'].
block(S) --> statementBlockList(S).

%  statementS block LIST
statementBlockList(SB) --> statementBlock(SB).
statementBlockList((SB, SBL)) --> statementBlock(SB), statementBlockList(SBL).

%  statement BLOCK
statementBlock(SL) --> statementsList(SL), [;].
statementBlock(CL) --> conditionalList(CL).

%  conditional LIST
conditionalList(C) --> conditional(C). 
conditionalList((C, CL)) --> conditional(C), conditionalList(CL).

%  statementS LIST
statementsList(S) --> statement(S). 
statementsList((S, SL)) --> statement(S), [;], statementsList(SL).

%  statement
statement(AS) --> assignment(AS).
statement(DL) --> declaration(DL).
statement(PS) --> printStatement(PS).
statement(RT) --> return(RT).

% PRINT STATEMENT
printStatement(print(V)) --> [print], number(V).
printStatement(print_id(I)) --> [print], id(I).

%  conditional
conditional(cond(IF)) --> ifte(IF).
conditional(cond(WL)) --> whileLoop(WL).
conditional(cond(FL)) --> forLoop(FL). 

%  conditional PARAN
conditionalParan(condParan(B)) --> ['('], boolean(B), [')']. 

% ifte
ifte(ifte(C, B)) --> [if], conditionalParan(C), blockScope(B). 
ifte(ifte(C, B, E)) --> [if], conditionalParan(C), blockScope(B), else(E).
else(else(B)) --> [else], blockScope(B). 
else(elseif(I)) --> [else], ifte(I).
else(elseif(I, B)) --> [else], ifte(I), else(B).

% WHILE
whileLoop(wloop(C, B)) --> [while], conditionalParan(C), blockScope(B). 

% FOR LOOP
forLoop(floop(C1, B, C2, BL)) --> [for], ['('], csv(C1), [;], boolean(B), [;], csv(C2), [')'], blockScope(BL). 
csv(csv(S, C)) --> statement(S), [','], csv(C).
csv(S) --> statement(S). 
 
declaration(decl(T, I)) --> type(T), id(I).
declaration(decl(T, I, A)) --> type(T), id(I), [=], anyValue(A).
declaration(decl(T, I, E)) --> type(T), id(I), [=], expression(E). 

% How to scan for partial strings like ++ and -- 
assignment(assign(I, A)) --> id(I), [=], anyValue(A).
assignment(assign(I, B)) --> id(I), [=], boolean(B).
assignment(assign(I, E)) --> id(I), [=], expression(E).
assignment(assign(I, AOP, E)) --> id(I), assignmentOperator(AOP), expression(E).
assignment(assign(I, VOP)) --> id(I), valueOperator(VOP).
assignment(assign(VOP, I)) --> valueOperator(VOP), id(I). 

assignmentOperator(assignOp(+=)) --> [+=].
assignmentOperator(assignOp(-=)) --> [-=].
assignmentOperator(assignOp(*=)) --> [*=].
assignmentOperator(assignOp(\/=)) --> [/=]. 
valueOperator(valueOp(++)) --> [++].
valueOperator(valueOp(--)) --> [--]. 

type(type(int)) --> [int].
type(type(boolean)) --> [boolean]. 
type(type(string)) --> [string].

any(A) --> anyValue(A).
any(E) --> expression(E). 

anyValue(V) --> number(V).
anyValue(S) --> string(S).
anyValue(T) --> ternary(T). 

id(name(S)) --> char(S).
string(string(S)) --> ['"'], chars(S), ['"'].

% TODO Define list of characters 
chars((C)) --> char(C).
chars((C1, C2)) --> char(C1), chars(C2).
char(C) -->
    [Token],
    { atom(Token), C = Token }.

number(number(N)) --> [Token], { number(Token), N = Token }.

expression(T) --> term(T). 
expression(add(T1, T2)) --> term(T1), ['+'], expression(T2).
expression(subt(T1, T2)) --> term(T1), ['-'], expression(T2).

term(P) --> paran(P). 
term(mult(P1, P2)) --> paran(P1), [*], paran(P2).
term(div(P1, P2)) --> paran(P1), [/], paran(P2).

paran(paran(E)) --> ['('], expression(E), [')'].
paran(V) --> number(V). 

ternary(tern(B, A1, A2)) --> boolean(B), [?],  any(A1), [:], any(A2). 
ternary(tern(B, A1, A2)) --> boolean(B), [?],  boolean(A1), [:], boolean(A2). 

boolean(bool(true)) --> [true].
boolean(bool(false)) --> [false].
boolean(bool(not, B)) --> [not], boolean(B).
boolean(bool(V1, NOP, V2)) --> booleanNumOp(V1), numOperator(NOP), booleanNumOp(V2).
boolean(bool(V, BOP, B1)) --> id(V), boolOperator(BOP), id(B1).

booleanNumOp(V) --> id(V).
booleanNumOp(V) --> number(V).

numOperator(nop(>)) --> [>].
numOperator(nop(>=)) --> [>=].
numOperator(nop(<)) --> [<].
numOperator(nop(<=)) --> [<=].
numOperator(nop(==)) --> [==].

boolOperator(bop(and)) --> [and].
boolOperator(bop(or)) --> [or].
boolOperator(bop(==)) --> [==]. 

booleanTernary(bTern(BR, B1, B2)) --> boolean(BR), [?], boolean(B1), [:], boolean(B2). 

functionCall(funcCall(I, PL)) -->  id(I), ['('], paramList(PL), [')'].
paramList((P, PL)) --> any(P), [','], paramList(PL).
paramList(P) --> any(P).
return(P) --> [return], anyValue(P).