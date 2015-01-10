package com.dejankos.model;

import java.io.Serializable;
import java.util.Arrays;

public final class Parameter implements Serializable{

    private final String name;
    private final String[] value;

    public Parameter(String name, String... value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;

        if (!name.equals(parameter.name)) return false;
        if (!Arrays.equals(value, parameter.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? Arrays.hashCode(value) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", value=" + Arrays.toString(value) +
                '}';
    }
}
