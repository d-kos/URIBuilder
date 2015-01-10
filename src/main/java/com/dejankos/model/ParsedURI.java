package com.dejankos.model;

import com.dejankos.uri.UriScheme;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ParsedURI implements Serializable{

    private UriScheme scheme;
    private String host;
    private String path;
    private Integer port;
    private List<Parameter> parameterList;

    public ParsedURI(UriScheme scheme, String host, String path, Integer port, List<Parameter> parameterList) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.port = port;
        this.parameterList = parameterList;
    }

    public UriScheme getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public Integer getPort() {
        return port;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }
    
    public Map<String, String[]> getParameterMap() {
        if(parameterList.isEmpty()) {
            return Collections.EMPTY_MAP;
        }

        Map<String, String[]> parameterMap = new LinkedHashMap<>();
        for(Parameter parameter : parameterList) {
            parameterMap.put(parameter.getName(), parameter.getValue());
        }
        
        return Collections.unmodifiableMap(parameterMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedURI parsedURI = (ParsedURI) o;

        if (host != null ? !host.equals(parsedURI.host) : parsedURI.host != null) return false;
        if (parameterList != null ? !parameterList.equals(parsedURI.parameterList) : parsedURI.parameterList != null)
            return false;
        if (path != null ? !path.equals(parsedURI.path) : parsedURI.path != null) return false;
        if (port != null ? !port.equals(parsedURI.port) : parsedURI.port != null) return false;
        if (scheme != parsedURI.scheme) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (parameterList != null ? parameterList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "URIPart{" +
                "scheme=" + scheme +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", port=" + port +
                ", parameterList=" + parameterList +
                '}';
    }
}
