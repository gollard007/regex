package com.regex;

public class Test {

    public void runTest() {

        Regex regex = new Regex("(aa|bb)*");
        System.out.println(regex.doesMatch("ab"));
        System.out.println(regex.doesMatch("a"));
        System.out.println(regex.doesMatch("aa"));
        System.out.println(regex.doesMatch("bb"));
        System.out.println(regex.doesMatch("aabbaa"));
        System.out.println(regex.doesMatch("aabaa"));
        System.out.println(regex.doesMatch("aabbbbaaaaaaaa"));

    }
}
