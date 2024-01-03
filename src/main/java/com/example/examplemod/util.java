package com.example.examplemod;

public class util {
    public static int indexOf(String[] list, String str) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(str)) {
                return i;
            }
        }

        return -1;
    }
}
