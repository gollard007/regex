package com.regex.automat;

import java.util.ArrayList;
import java.util.List;

public class DState {

    private boolean acceptState = false;

    private int number;
    List<State> states = new ArrayList<>();
    List<DLink> inLinks = new ArrayList<>();
    List<DLink> outLinks = new ArrayList<>();

    boolean label = false;

    public List<State> getStates() {
        return states;
    }

    void addState(State state) {
        if (!states.contains(state))
            states.add(state);
    }

    void addInLink(DLink link) {
        inLinks.add(link);
    }

    void addOutLink(DLink link) {
        outLinks.add(link);
    }

    public List<DLink> getOutLinks() {
        return outLinks;
    }

    void checkAccept() {
        states.forEach(state -> {
            if (state.isAccept()) {
                acceptState = true;
            }
        });
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public boolean getLabel() {
        return label;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public void setAcceptState(boolean acceptState) {
        this.acceptState = acceptState;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        DState dState = (DState) obj;

        if (states.size() != dState.states.size()) return false;

        return states.containsAll(dState.states);
    }
}
