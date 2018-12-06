package com.regex.automat;

public class Link {

    private boolean isEpsilon = false;
    private char value;
    private State source;
    private State target;

    public Link(State source, State target) {
        this.source = source;
        this.target = target;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public State getSource() {
        return source;
    }

    public void setSource(State source) {
        this.source = source;
    }

    public State getTarget() {
        return target;
    }

    public void setTarget(State target) {
        this.target = target;
    }

    void setEpsilon() {
        this.isEpsilon = true;
    }

    boolean isEpsilon() {
        return this.isEpsilon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Link link  = (Link) obj;
        return source == link.source && target == link.target && value == link.value;
    }
}
