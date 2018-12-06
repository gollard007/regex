package com.regex.automat;

import java.util.ArrayList;
import java.util.List;

public class State {

    private boolean acceptState = false;
    private List<Link> inputLinks = new ArrayList<>();
    private List<Link> outputLinks = new ArrayList<>();

    //метка для построения ДКА
    private boolean label = false;

    void setAcceptState(boolean state) {
        this.acceptState = state;
    }

    public boolean isAccept() {
        return acceptState;
    }

    public List<Link> getInputLinks() {
        return inputLinks;
    }

    public List<Link> getOutputLinks() {
        return outputLinks;
    }

    public void addInputLink(Link link) {
        inputLinks.add(link);
    }

    public void addOutputLink(Link link) {
        outputLinks.add(link);
    }

    public void setLabel(boolean label){
        this.label = label;
    }

    public boolean getLabel() {
        return this.label;
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) {
//            return true;
//        }
//
//        if (obj == null || obj.getClass() != this.getClass()) {
//            return false;
//        }
//
//        State state = (State) obj;
//        return inputLinks.equals(state.inputLinks) && outputLinks.equals(state.outputLinks)
//                && acceptState == state.acceptState;
//    }
}
