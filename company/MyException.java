package com.company;

public class MyException extends Exception {

    public String exception;

    public MyException(String string) {
        exception = string;
    }

    public static void throwException(String e) {
        System.out.println(e);
    }
}