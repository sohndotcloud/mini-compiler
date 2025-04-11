:- use_rendering(svgtree).
% --- Simplified DCG to Avoid Looping ---
% Entry point: a single function only
program(program(F)) --> function(F).

% Function with one signature and one block
function(func(Signature, BlockScope)) --> signature(Signature), block_scope(BlockScope).

signature(sign(Type, Id, Args)) --> type(Type), id(Id), ['('], arg_list(Args), [')'].

arg_list([A|As]) --> func_arg(A), [','], arg_list(As).
arg_list([A]) --> func_arg(A).
arg_list([]) --> [].

func_arg(arg(Type, Id)) --> type(Type), id(Id).

block_scope(block(Block)) --> ['{'], block(Block), ['}'].

block(statements([S1, S2])) --> statement(S1), [';'], statement(S2), [';'].

% Two types of supported statements
statement(state(decl(Type, Id, Val))) --> type(Type), id(Id), ['='], value(Val).
statement(state(print_stmt(Id))) --> [print], id(Id).

% --- Terminals and Values ---

id(id(X)) --> [id(X)].
value(val(X)) --> [val(X)].

type(type(int)) --> [int].
type(type(boolean)) --> [boolean].
type(type(string)) --> [string].

% --- Sample Input ---
sample_tokens([
  int, id(main), '(', ')', '{',
    int, id(x), '=', val(5), ';',
    print, id(x), ';',
  '}'
]).

% --- Tree Printer ---
print_tree(Tree) :- print_node(Tree, 0).

print_node(Term, Indent) :-
    functor(Term, Name, Arity),
    tab(Indent), write(Name), nl,
    print_args(1, Arity, Term, Indent + 2).

print_args(I, Arity, _, _) :- I > Arity, !.
print_args(I, Arity, Term, Indent) :-
    arg(I, Term, Arg),
    ( compound(Arg) -> print_node(Arg, Indent)
    ; tab(Indent), write(Arg), nl ),
    I1 is I + 1,
    print_args(I1, Arity, Term, Indent).
