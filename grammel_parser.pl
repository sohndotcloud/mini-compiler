%  START
Program(program(FL)) --> FunctionList(FL). 

%  FUNCTION LIST
FunctionList((F, FL)) --> Function(F) FunctionList(FL).
FunctionList(F) --> Function(F). 

%  FUNCTION STRUCTURE
Function(func(S, BS)) --> Signature(S), BlockScope(BS). 
Signature(sign(T, I, AL)) --> Type(T), Id(I), [(], ArgList(AL), [)]. 
ArgList((A, AL)) --> Arg(A), [,], ArgList(AL).
ArgList((A)) --> Arg(A). 
Arg(arg(T, I)) --> Type(T), Id(I). 

%  SCOPE
BlockScope((B)) --> [{], Block(B), [}]. 
Block(block(S)) --> StatementBlockList(S). 

%  STATEMENTS BLOCK LIST
StatementBlockList((SB, SBL)) --> StatementBlock(SB), StatementBlockList(SBL).
StatementBlockList((SB)) --> StatementBlock(SB). 

%  STATEMENT BLOCK
StatementBlock(SL) --> StatementsList(SL), [;].
StatementBlock(CL) --> ConditionalList(CL).

%  CONDITIONAL LIST
ConditionalList((C, CL)) --> Conditional(C), ConditionalList(CL).
ConditionalList(C) --> Conditional(C). 

%  STATEMENTS LIST
StatementsList((S, SL)) --> Statement(S), [;], StatementsList(SL).
StatementsList(S) --> Statement(S). 

%  STATEMENT
Statement(state(DL)) --> Declaration(DL).
Statement(state(AS)) --> Assignment(AS). 

%  CONDITIONAL
Conditional(cond(IF)) --> IFTE(IF).
Conditional(cond(WL)) --> WhileLoop(WL).
Conditional(cond(FL)) --> ForLoop(FL). 

%  CONDITIONAL PARAN
ConditionalParan(condParan(B)) -> [(], Boolean(B), [)]. 

% IFTE
Ifte(ifte(C, B)) --> [if], ConditionalParan(C), BlockScope(B). 
Ifte(ifte(C, B, E)) --> [if], ConditionalParan(C), BlockScope(B), Else(E).
Else(elseif(I)) --> [else], Ifte(I)
Else(ifelse(B)) --> [else], BlockScope(B). 

% WHILE
WhileLoop(wloop(C, B)) -> [while], ConditionalParan(C), BlockScope(B). 

% FOR LOOP
ForLoop(floop(C1, B, C2, BL)) -> [for], [(], Csv(C1), [;], Boolean(B), [;], Csv(C2), [)], BlockScope(BL). 
Csv(csv(S, C)) --> Statement(S), [,], Csv(C).
Csv(S) --> Statement(S). 
 
Declaration(decl(T, I)) --> Type(T), Id(I).
Declaration(decl(T, I, A)) --> Type(T), Id(I), [=], AnyValue(A).
Declaration(decl(T, I, E)) --> Type(T), Id(I), [=], Expression(E). 

% How to scan for partial strings like ++ and -- 
Assignment(assign(I, A)) --> Id(I), [=], AnyValue(A).
Assignment(assign(I, E)) --> Id(I), [=], Expression(E).
Assignment(assign(I, AOP, E)) --> Id(I), AssignmentOperator(AOP), Expression(E).
Assignment(assign(I, VOP)) --> Id(I), ValueOperator(VOP).
Assignment(assign(VOP, I)) --> ValueOperator(VOP), Id(I). 

AssignmentOperator(assignOp(+=)) --> [+=].
AssignmentOperator(assignOp(-=)) --> [-=].
AssignmentOperator(assignOp(*=)) --> [*=].
AssignmentOperator(assignOp(\/=)) --> [/=]. 
ValueOperator(valueOp(++)) --> [++].
ValueOperator(valueOp(--)) --> [--]. 

Type(type(int)) --> [int].
Type(type(boolean)) --> [boolean]. 
Type(type(String)) --> [String].

Any(A) --> AnyValue(A).
Any(E) --> Expression(E). 

AnyValue(B) --> Boolean(B).
AnyValue(V) --> Value(V).
AnyValue(S) --> String(S).
AnyValue(T) --> Ternary(T). 

String(string(C)) -> [“], Chars(C), [“]. 

% TODO Define list of characters 
Chars(chars()) -> [a-zA-Z0-9...]+ 
Integer(int()) -> [0-9]+ 

Expression(add(T1, T2)) --> Term(T1), +, Term(T2).
Expression(subt(T1, T2)) --> Term(T1), –, Term(T2).
Expression(T) --> Term(T). 

Term(mult(P1, P2)) --> Paran(P1), *, Paran(P2).
Term(div(P1, P2)) --> Paran(P1) / Paran(P2).
Term(P) --> Paran(P). 

Paran(paran(E)) --> [(], Expression(E), [)].
Paran(V) --> Value(V). 

Ternary(tern(B, A1, A2)) -> Boolean(B), [?],  Any(A1), [:], Any(A2). 

Boolean(bool(true)) --> [true].
Boolean(bool(false)) --> [false].
Boolean(bool(not, B)) --> [not], Boolean(B).
Boolean(bool(B, BOP, B)) --> Boolean(B), BoolOperator(BOP), Boolean(B).
Boolean(bool(E1, NOP, E2, BT)) --> Expression(E1), NumOperator(NOP), Expression(E2), BooleanTernary(BT).  

NumOperator(nop(>)) --> [>].
NumOperator(nop(>=)) --> [>=].
NumOperator(nop(<)) --> [<].
NumOperator(nop(<=)) --> [<=].
NumOperator(nop(==)) --> [==].

BoolOperator(bop(and)) --> [and].
BoolOperator(bop(or)) --> [or].
BoolOperator(bop(==)) --> [==]. 

BooleanTernary(bTern(BR, B1, B2)) -> BooleanReturn(BR), [?], Boolean(B1), [:], Boolean(B2). 

BooleanReturn(B) --> Boolean(B).
BooleanReturn(BT) --> BooleanTernary(BT). 
BooleanReturn(F) --> FunctionCall(F).
FunctionCall(funcCall(I, PL)) -->  Id, [(], ParamList(PL), [)].
