package com.dejankos.builder;

import com.dejankos.model.Parameter;

import java.util.*;

class ParameterList {

    private final List<Parameter> parameterList = new ArrayList<>();

    void addParameter(Parameter parameter) {
        parameterList.add(parameter);
    }

    void addParameters(List<Parameter> parameters) {
        parameterList.addAll(parameters);
    }

    void removeParameter(String name) {
        Iterator<Parameter> it = parameterList.iterator();
        while (it.hasNext()){
            Parameter parameter = it.next();
            if(parameter.getName().equals(name)){
                it.remove();
            }
        }
    }

    void removeParameter(Parameter parameter) {
        if(parameterList.contains(parameter)){
            Iterator<Parameter> it = parameterList.iterator();
            while (it.hasNext()){
                if(it.next().equals(parameter)){
                    it.remove();
                }
            }
        }
    }

    void replaceParameterValue(String name, String... newValue) {
        for(Parameter parameter : parameterList) {
            if (parameter.getName().equals(name)) {
                int indexOf = parameterList.indexOf(parameter);
                parameterList.set(indexOf, new Parameter(name, newValue));
            }
        }
    }

    boolean isEmpty() {
        return parameterList.isEmpty();
    }

    List<Parameter> getParameterList() {
        return Collections.unmodifiableList(parameterList);
    }

}
