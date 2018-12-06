package com.regex.automat;

import com.regex.tree.Node;
import com.regex.tree.Tree;

public class NFABuilder {

    private Tree tree;

    public NFABuilder(String regex) {
        this.tree = new Tree(regex);
    }

    public NFA build() {
        buildForNode(tree.getRoot());
        return tree.getRoot().getAuto();
    }

    private void buildForNode(Node node) {
        if (node.getLeft() != null) {
            buildForNode(node.getLeft());
        }

        if (node.getRight() != null) {
            buildForNode(node.getRight());
        }

        switch (node.getType()) {
            case LITERAL:
                node.setAuto(buildLiteral(node.getValue()));
                break;
            case OR:
                node.setAuto(buildInvariant(node.getLeft().getAuto(), node.getRight().getAuto()));
                break;
            case CONCAT:
                node.setAuto(buildConcatenation(node.getLeft().getAuto(), node.getRight().getAuto()));
                break;
            case REPEAT:
                node.setAuto(buildRepeat(node.getLeft().getAuto()));
                break;
            default:
                break;
        }
    }

    private NFA buildLiteral(char c) {
        State initialState = new State();
        State outputState = new State();
        outputState.setAcceptState(true);
        Link link = new Link(initialState, outputState);
        link.setValue(c);
        initialState.addOutputLink(link);
        outputState.addInputLink(link);
        return new NFA(initialState, outputState);
    }

    private NFA buildConcatenation(NFA left, NFA right) {
        State concatState = new State();
        left.getOutputState().getInputLinks().forEach(link -> {
            link.setTarget(concatState);
            concatState.addInputLink(link);
        });
        right.getInitialState().getOutputLinks().forEach(link -> {
            link.setSource(concatState);
            concatState.addOutputLink(link);
        });
        return new NFA(left.getInitialState(), right.getOutputState());
    }

    private NFA buildInvariant(NFA left, NFA right) {
        State initial = new State();
        State outputState = new State();
        outputState.setAcceptState(true);

        Link inToLeft = new Link(initial, left.getInitialState());
        inToLeft.setEpsilon();
        initial.addOutputLink(inToLeft);
        left.getInitialState().addInputLink(inToLeft);

        Link inToRight = new Link(initial, right.getInitialState());
        inToRight.setEpsilon();
        initial.addOutputLink(inToRight);
        right.getInitialState().addInputLink(inToRight);

        Link leftToOut = new Link(left.getOutputState(), outputState);
        leftToOut.setEpsilon();
        outputState.addInputLink(leftToOut);
        left.getOutputState().addOutputLink(leftToOut);
        left.getOutputState().setAcceptState(false);

        Link rightToOut = new Link(right.getOutputState(), outputState);
        rightToOut.setEpsilon();
        outputState.addInputLink(rightToOut);
        right.getOutputState().addOutputLink(rightToOut);
        right.getOutputState().setAcceptState(false);

        return new NFA(initial, outputState);
    }

    private NFA buildRepeat(NFA auto) {
        State initialState = new State();
        State outputState = new State();
        outputState.setAcceptState(true);
        auto.getOutputState().setAcceptState(false);

        Link inToOut = new Link(initialState, outputState);
        inToOut.setEpsilon();
        initialState.addOutputLink(inToOut);
        outputState.addInputLink(inToOut);

        Link inToAutomatIn = new Link(initialState, auto.getInitialState());
        inToAutomatIn.setEpsilon();
        initialState.addOutputLink(inToAutomatIn);
        auto.getInitialState().addInputLink(inToAutomatIn);

        Link autoOutToOut = new Link(auto.getOutputState(), outputState);
        autoOutToOut.setEpsilon();
        auto.getOutputState().addOutputLink(autoOutToOut);
        outputState.addInputLink(autoOutToOut);

        Link autoOutToAutoIn = new Link(auto.getOutputState(), auto.getInitialState());
        autoOutToAutoIn.setEpsilon();
        auto.getOutputState().addOutputLink(autoOutToAutoIn);
        auto.getInitialState().addInputLink(autoOutToAutoIn);

        return new NFA(initialState, outputState);
    }
}
