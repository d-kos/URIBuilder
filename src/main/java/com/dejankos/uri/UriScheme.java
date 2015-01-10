package com.dejankos.uri;

public enum UriScheme {
    HTTP("http"),
    HTTPS("https");

    private final String scheme;

    UriScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

}
