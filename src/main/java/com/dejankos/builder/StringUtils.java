package com.dejankos.builder;

final class StringUtils {

    static boolean isNullorEmpty(String value) {
        if (value == null || value.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
