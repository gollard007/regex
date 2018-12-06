package com.regex.automat;

public class DLink {

    private char value;
    private DState source;
    private DState target;

    public DLink(char value, DState source, DState target) {
        this.value = value;
        this.source = source;
        this.target = target;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public DState getSource() {
        return source;
    }

    public void setSource(DState source) {
        this.source = source;
    }

    public DState getTarget() {
        return target;
    }

    public void setTarget(DState target) {
        this.target = target;
    }
}
