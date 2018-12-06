package com.regex.automat;

import java.util.*;

public class DFABuilder {

    private String regex;
    private NFA nAuto;

    // множество всех состояний НКА из которого будем удалять состояния по мере посроения ДКА
    private Set<State> allNfaStates = new HashSet<>();

    private List<Character> symbols = new ArrayList<>();

    public DFABuilder(String regex) {
        this.regex = regex;
        nAuto = new NFABuilder(regex).build();
        initAllStates();
        initSymbols();
    }

    public DFA build() {
        DState initialState = eClosure(nAuto.getInitialState());
        List<DState> dStates = new ArrayList<>();
        checkAcceptedState(initialState);
        dStates.add(initialState);
        while (hasUnlabeled(dStates)) {
            DState state = getUnlabeled(dStates);
            state.setLabel(true);
            for (char symbol: symbols) {
                DState nextState = eClosure(move(state, symbol));
                checkAcceptedState(nextState);
                if (!dStates.contains(nextState)) {
                    dStates.add(nextState);
                    DLink link = new DLink(symbol, state, nextState);
                    state.addOutLink(link);
                    nextState.addInLink(link);
                } else {
                    // если имеется следующее состояние
                    DState eqState = getEqual(dStates, nextState);
                    DLink link = new DLink(symbol, state, eqState);
                    state.addOutLink(link);
                    eqState.addInLink(link);
                }

            }
        }
        return new DFA(initialState);
    }

    //используется только если contains == true
    private DState getEqual(List<DState> states, DState state) {
        for (DState s : states) {
            if (s.equals(state))
                return s;
        }
        return null;
    }

    //ищем конечные состояния в узлах НКА
    private void checkAcceptedState(DState state) {
        for(State s: state.getStates()) {
            if (s.isAccept()) {
                state.setAcceptState(true);
            }
        }
    }

    private boolean hasUnlabeled(List<DState> states) {
        for (DState state: states) {
            if (!state.getLabel()) return true;
        }
        return false;
    }

    //используется только если hasUnlabeled == true
    private DState getUnlabeled(List<DState> states) {
        for (DState state: states) {
            if (!state.getLabel()) return state;
        }
        return null;
    }

    private void initSymbols() {
        char [] chars = regex.toCharArray();
        for (char c : chars) {
            if(isLiteral(c)) symbols.add(c);
        }
    }

    private void initAllStates() {
        allNfaStates.add(nAuto.getInitialState());
        List<State> allStates = getStates(nAuto.getInitialState());
        //нужно удалить метки, потому что будем еще помечать в след проходах
        allStates.forEach(state -> state.setLabel(false));
        allNfaStates.addAll(allStates);
    }

    // переходы по eps для начального состояния
    private DState eClosure(State state) {
        DState dState = new DState();
        dState.addState(state);
        getEpsStates(state).forEach(s -> dState.addState(s));
        getEpsStates(state).forEach(s -> s.setLabel(false));
        return dState;
    }

    private DState eClosure(List<State> states) {
        DState eClosure = new DState();
        Stack<State> stack = new Stack<>();
        states.forEach(s -> {
            stack.push(s);
            eClosure.addState(s);
        });
        while (!stack.empty()) {
           State s = stack.pop();
           for (Link link: s.getOutputLinks()) {
               if (!eClosure.getStates().contains(link.getTarget()) && link.isEpsilon()) {
                   eClosure.addState(link.getTarget());
                   stack.add(link.getTarget());
               }
           }
        }
        return eClosure;
    }

    private List<State> move(DState state, char c) {
        List<State> states = new ArrayList<>();
        for (State s : state.getStates()) {
            for (Link link: s.getOutputLinks()) {
                if (link.getValue() == c) {
                    states.add(link.getTarget());
                }
            }
        }
        return states;
    }

    //список всех состояний доступных по eps переходам
    private List<State> getEpsStates(State state) {
        List<State> list = new ArrayList<>();
        state.getOutputLinks().forEach(link -> {
            if (link.isEpsilon()) {
                list.add(link.getTarget());
                list.addAll(getEpsStates(link.getTarget()));
            }
        });
        state.setLabel(true);
        return list;
    }

    //обход всех состояний НКА
    private List<State> getStates(State state) {
        final List<State> list = new ArrayList<>();
        state.setLabel(true);
        state.getOutputLinks().forEach( link -> {
            if (!link.getTarget().getLabel()) {
                list.add(link.getTarget());
                list.addAll(getStates(link.getTarget()));
            }
        });
        return list;
    }

    private boolean isLiteral(char c) {
        return c != '(' && c != ')' && c != '*' && c != '|';
    }
}
