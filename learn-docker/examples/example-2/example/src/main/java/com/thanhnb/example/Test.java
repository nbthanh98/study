package com.thanhnb.example;

import java.util.Arrays;
import java.util.List;

public class Test {
    private static final List<String> names = Arrays.asList("a", "b", "C");

    public static void main(String[] args) {
        System.out.println(checkName("A"));
    }

    public static boolean checkName(String nameToCheck) {
        names.forEach(name -> {
            if (name.equals(nameToCheck)) {
                return true;
            }
        });
        return false;
    }
}
