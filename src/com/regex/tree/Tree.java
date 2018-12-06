package com.regex.tree;

import javafx.util.Pair;

import java.util.Arrays;

public class Tree {

    private int position = 1;
    private Node root;

    public Tree(String regex) {
        root = buildTree(regex);
        prepareTree(root);
        checkRoot();
    }

    //можно опустить внешние скобки
    private String prepareRegex(String regex) {
        if (regex.startsWith("(") && regex.endsWith(")")) {
            return regex.substring(1, regex.length() - 1);
        }
        return regex;
    }

    private Node buildTree(String regex) {
        char[] chars = regex.toCharArray();
        Node currentNode = new Node(Node.Type.UNDEFINED);
        if (chars.length == 1) {
            currentNode.setType(Node.Type.LITERAL);
            currentNode.setValue(chars[0]);
            return currentNode;
        }
        int i = 0;
        while (i < chars.length) {
            switch (chars[i]) {
                case '(':
                    currentNode = addGroup(currentNode);
                    break;
                case ')':
                    currentNode = upFromGroup(currentNode);
                    break;
                case '|':
                    Pair<Node, Integer> pair1 = addOr(currentNode, i);
                    currentNode = pair1.getKey();
                    i = pair1.getValue();
                    break;
                case '*':
                    currentNode = addRepeat(currentNode);
                    break;
                default:
                    Pair<Node, Integer> pair = addLiterals(currentNode, chars, i);
                    currentNode = pair.getKey();
                    i = pair.getValue();
                    break;
            }
            i++;
        }
        while (currentNode.getParent() != null) {
            currentNode = currentNode.getParent();
        }
        return currentNode;
    }

    private Node addGroup(Node currentNode) {
        Node group = new Node(Node.Type.GROUP);
        switch (currentNode.getType()) {
            case UNDEFINED:
                if (currentNode.getLeft() == null) {
                    group.setParent(currentNode);
                    currentNode.setLeft(group);
                    Node node = new Node(Node.Type.UNDEFINED);
                    group.setLeft(node);
                    node.setParent(group);
                    currentNode = node;
                } else if (currentNode.getRight() == null) {
                    // скорее всего если типа нету то в текущем узле будет конкатенация
                    // это не точно нужно проверить
                    currentNode.setType(Node.Type.CONCAT);
                    group.setParent(currentNode);
                    currentNode.setRight(group);
                    Node node = new Node(Node.Type.UNDEFINED);
                    group.setLeft(node);
                    node.setParent(group);
                    currentNode = node;
                }
                break;
            case CONCAT:
            case REPEAT:
                //нужно добавить новый узел и соеденить след группу справа с предыдущей
                Node nodeConcat = new Node(Node.Type.CONCAT);
                //если есть родитель то надо создать новый узел на месте текущего и текущий присоеденить слева
                if (currentNode.getParent() != null) {
                    Node parent = currentNode.getParent();
                    nodeConcat.setParent(parent);
                    if (parent.getLeft() == currentNode) {
                        parent.setLeft(nodeConcat);
                    } else {
                        parent.setRight(nodeConcat);
                    }
                }
                //меняем потомка родительского узла на наш новый узел
                nodeConcat.setLeft(currentNode);
                currentNode.setParent(nodeConcat);
                group.setParent(nodeConcat);
                nodeConcat.setRight(group);
                Node node = new Node(Node.Type.UNDEFINED);
                group.setLeft(node);
                node.setParent(group);
                currentNode = node;
                break;
            case OR:
                if (currentNode.getRight() == null) {
                    group.setParent(currentNode);
                    currentNode.setRight(group);
                    Node node1 = new Node(Node.Type.UNDEFINED);
                    group.setLeft(node1);
                    node1.setParent(group);
                    currentNode = node1;
                } else {
                    //нужно добавить новый узел конкатенации справа
                    Node nodeConcat1 = new Node(Node.Type.CONCAT);
                    nodeConcat1.setLeft(currentNode.getRight());
                    currentNode.getRight().setParent(nodeConcat1);
                    nodeConcat1.setParent(currentNode);
                    currentNode.setRight(nodeConcat1);
                    nodeConcat1.setRight(group);
                    group.setParent(nodeConcat1);
                    Node node1 = new Node(Node.Type.UNDEFINED);
                    group.setLeft(node1);
                    node1.setParent(group);
                    currentNode = node1;
                }
                break;
            default:
                break;
        }
        return currentNode;
    }

    private Node upFromGroup(Node currentNode) {
        return currentNode.getParent().getParent();
    }

    private Pair<Node, Integer> addOr(Node currentNode, int i) {
        Node nodeOr = new Node(Node.Type.OR);
        switch (currentNode.getType()) {
            case UNDEFINED:
                currentNode.setType(Node.Type.OR);
                break;
            case REPEAT:
                if (currentNode.getParent() != null) {
                    Node parent = currentNode.getParent();
                    nodeOr.setParent(parent);
                    if (parent.getLeft() == currentNode) {
                        parent.setLeft(nodeOr);
                    } else {
                        parent.setRight(nodeOr);
                    }
                }
                nodeOr.setLeft(currentNode);
                currentNode.setParent(nodeOr);
                currentNode = nodeOr;
                break;
            case CONCAT:
                // OR имеет меньший приоритет, занчит нам нужно установить currentNode в верхний узел с OR
                if (currentNode.getParent() != null && currentNode.getParent().getType() == Node.Type.OR) {
                    currentNode = currentNode.getParent();
                    i--;
                } else {
                    if (currentNode.getParent() != null) {
                        Node parent = currentNode.getParent();
                        nodeOr.setParent(parent);
                        if (parent.getLeft() == currentNode) {
                            parent.setLeft(nodeOr);
                        } else {
                            parent.setRight(nodeOr);
                        }
                    }
                    nodeOr.setLeft(currentNode);
                    currentNode.setParent(nodeOr);
                    currentNode = nodeOr;
                }
                break;
            case OR:
                Node nodeOr1 = new Node(Node.Type.OR);
                if (currentNode.getParent() != null) {
                    Node parent = currentNode.getParent();
                    nodeOr1.setParent(parent);
                    if (parent.getLeft() == currentNode) {
                        parent.setLeft(nodeOr1);
                    } else {
                        parent.setRight(nodeOr1);
                    }
                }
                //меняем потомка родительского узла на наш новый узел
                nodeOr1.setLeft(currentNode);
                currentNode.setParent(nodeOr1);
                currentNode = nodeOr1;
            default:
                break;
        }
        return new Pair<>(currentNode, i);
    }

    private Node addRepeat(Node currentNode) {
        switch (currentNode.getType()) {
            case UNDEFINED:
                currentNode.setType(Node.Type.REPEAT);
                break;
            case CONCAT:
                Node right = currentNode.getRight();
                if (right != null) {
                    Node repeatNode = new Node(Node.Type.REPEAT);
                    repeatNode.setParent(currentNode);
                    currentNode.setRight(repeatNode);
                    repeatNode.setLeft(right);
                    right.setParent(repeatNode);
                } else {
                    currentNode.setType(Node.Type.REPEAT);
                }
                break;
            default:
                break;
        }
        return currentNode;
    }

    private Pair<Node, Integer> addLiterals(Node currentNode, char[] chars, int i) {
        int finish = i;
        while (finish < chars.length && isLiteral(chars[finish])) {
            finish++;
        }
        char[] arr = Arrays.copyOfRange(chars, i, finish);
        Node chain = buildChain(arr);
        switch (currentNode.getType()) {
            case UNDEFINED:
                currentNode.setType(Node.Type.CONCAT);
                currentNode.setLeft(chain);
                chain.setParent(currentNode);
                break;
            case REPEAT:
            case CONCAT:
                //если есть родитель то надо создать новый узел на месте текущего и текущий присоеденить слева
                Node nodeConcat = new Node(Node.Type.CONCAT);
                if (currentNode.getParent() != null) {
                    Node parent = currentNode.getParent();
                    nodeConcat.setParent(parent);
                    if (parent.getLeft() == currentNode) {
                        parent.setLeft(nodeConcat);
                    } else {
                        parent.setRight(nodeConcat);
                    }
                }
                nodeConcat.setLeft(currentNode);
                currentNode.setParent(nodeConcat);
                nodeConcat.setRight(chain);
                chain.setParent(nodeConcat);
                currentNode = nodeConcat;
                break;
            case OR:
                currentNode.setRight(chain);
                chain.setParent(currentNode);
                break;
            default:
                break;

        }
        i = finish - 1;
        return new Pair<>(currentNode, i);
    }

    private Node buildChain(char[] chars) {
        if (chars.length == 1) {
            // если длина равна единице создаем конкатенацию с пустым символом
            // слева будет хранится литерал, справа null
            // невозможно вернуть узел с литералом из-за того что currentNode у нас всегда оператор
            Node node = new Node(Node.Type.CONCAT);
            Node literal = new Node(Node.Type.LITERAL);
            literal.setValue(chars[0]);
            literal.setParent(node);
            node.setLeft(literal);
            return node;
        }
        Node node = new Node(Node.Type.CONCAT);
        Node left = new Node(Node.Type.LITERAL);
        left.setValue(chars[0]);
        Node right = new Node(Node.Type.LITERAL);
        right.setValue(chars[1]);
        node.setLeft(left);
        node.setRight(right);
        left.setParent(node);
        right.setParent(node);
        if (chars.length == 2) {
            return node;
        } else {
            Node current;
            Node prev = node;
            for (int i = 2; i < chars.length; i++) {
                current = new Node(Node.Type.CONCAT);
                current.setLeft(prev);
                Node tmp = new Node(Node.Type.LITERAL);
                tmp.setParent(current);
                tmp.setValue(chars[i]);
                current.setRight(tmp);
                prev = current;
            }
            return prev;
        }
    }

    private boolean isLiteral(char c) {
        return c != '(' && c != ')' && c != '*' && c != '|';
    }

    public void goTree() {
        printTree(root);
    }

    private void printTree(Node node) {
        if (node.getLeft() != null) {
            printTree(node.getLeft());
        }
        if (node.getRight() != null) {
            printTree(node.getRight());
        }
        if (node.getType() == Node.Type.LITERAL) {
            System.out.println(node.getValue() + " " + position++);
        } else if (node.getType() != Node.Type.UNDEFINED) {
            System.out.println(node.getType().toString() + " " + position++);
        }
    }

    private void prepareTree(Node node) {
        if(node.getLeft() != null) {
            prepareTree(node.getLeft());
        }
        if (node.getRight() != null) {
            prepareTree(node.getRight());
        }
        //избавляемся от конкатенаций одного символа и заменяем на узел с типом литерал
        if (node.getParent() != null && node.getType() == Node.Type.CONCAT && node.getRight() == null) {
            if (node.getParent().getLeft() == node) {
                node.getParent().setLeft(node.getLeft());
            } else {
                node.getParent().setRight(node.getLeft());
            }
        }

        // избавляемся от узлов с типом группа
        if (node.getType() == Node.Type.GROUP) {
            if (node.getParent().getLeft() == node) {
                node.getParent().setLeft(node.getLeft());
            } else {
                node.getParent().setRight(node.getLeft());
            }
        }
    }

    private void checkRoot() {
        if (root.getType() == Node.Type.CONCAT
                && root.getRight() == null
                && root.getLeft().getType() == Node.Type.CONCAT) {
            root = root.getLeft();
        }
    }
    public Node getRoot() {
        return root;
    }
}
