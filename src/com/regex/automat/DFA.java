package com.regex.automat;

//детерминированный конечный автомат(ДКА) для регулярного выражения
public class DFA {

    private DState currentState;
    private DState initialState;

    public DFA(DState inputState) {
        this.initialState = inputState;
        this.currentState = inputState;
    }

    public void move(char c) {
        for (DLink link : currentState.getOutLinks()) {
            if (link.getValue() == c) {
                currentState = link.getTarget();
                break;
            }
        }
    }

    public boolean canMove(char c) {
        for (DLink link : currentState.getOutLinks()) {
            if (link.getValue() == c) return true;
        }
        return false;
    }

    public DState getCurrentState() {
        return currentState;
    }

    public void reset() {
        this.currentState = initialState;
    }
}
