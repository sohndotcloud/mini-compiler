:- [grammel_paser].
:- use_rendering(svgtree).

run_test :-
    phrase(program(P), [
        int, n, '(', int, x, ',', int, y, ')', '{',
            int, z, '=', 234, ';',
            z, '=', 22, ';',
            if, '(', x, '==', y, ')', '{',
                y, '=', 5, ';',
            '}',
            else, if, '(', true, ')', '{',
                x, '=', 5, ';',
            '}',
            else, '{',
                x, '=', 10, ';',
            '}',
            while, '(', x, '>', y, ')', '{',
                z, '=', x, '==', y, '?', true, ':', false, ';',
            '}',
        '}'
    ], []),
    portray_clause(P).
