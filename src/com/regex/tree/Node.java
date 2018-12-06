package com.regex.tree;

import com.regex.automat.NFA;

public class Node {

    public enum Type {
        UNDEFINED,
        LITERAL,
        REPEAT,
        OR,
        CONCAT,
        GROUP
    }

    private int position;
    private char value;
    private Type type;
    private Node parent;
    private Node left;
    private Node right;

    //НКА для текущего узла
    private NFA auto;

    public Node(Type type) {
        this.type = type;
        left = null;
        right = null;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public NFA getAuto() {
        return auto;
    }

    public void setAuto(NFA auto) {
        this.auto = auto;
    }
}
