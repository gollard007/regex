package com.regex;

import com.regex.automat.DFA;
import com.regex.automat.DFABuilder;

public class Regex {

    private DFA dfa;

    public Regex(String regex) {
        dfa = new DFABuilder(regex).build();
    }

    boolean doesMatch(String arg) {
        char[] chars = arg.toCharArray();
        dfa.reset();
        for (char c : chars) {
            if (dfa.canMove(c)) {
                dfa.move(c);
            } else {
                return false;
            }
        }
        return dfa.getCurrentState().isAcceptState();
    }
}
