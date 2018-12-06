package com.regex.automat;

public class NFA {

    private State initialState;
    private State outputState;

    public NFA(State initialState, State outputState) {
        this.initialState = initialState;
        this.outputState = outputState;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public State getOutputState() {
        return outputState;
    }

    public void setOutputState(State outputState) {
        this.outputState = outputState;
    }
}
