package com.dejankos.builder;

import com.dejankos.model.Parameter;
import com.dejankos.model.ParsedURI;
import com.dejankos.uri.UriScheme;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.dejankos.builder.Constant.*;


public final class URIBuilder {

    private final ParameterList parameterList = new ParameterList();

    private String charset;
    private UriScheme scheme;
    private String host;
    private String path;
    private Integer port;

    private URIBuilder(String host) {
        if (StringUtils.isNullorEmpty(host)) {
            throw new IllegalArgumentException("Host can't be empty!");
        }

        this.host = host;
    }
    
    private URIBuilder(ParsedURI parsedURI) {
        scheme = parsedURI.getScheme();
        host = parsedURI.getHost();
        port = parsedURI.getPort();
        path = parsedURI.getPath();
        parameterList.addParameters(parsedURI.getParameterList());
    }

    /**
     * Create an instance of URIBuilder from host.
     *
     * @param host URI host
     * @return URIBuilder new instance
     */
    public static URIBuilder fromHost(String host) {
        return new URIBuilder(host);
    }

    /**
     * Create an instance of URIBuilder from an existing URI.
     *
     * @param uri URI uri
     * @return URIBuilder new instance
     */
    public static URIBuilder fromURI(URI uri) {
        return new URIBuilder(URIParser.parseFromURI(uri));
    }

    /**
     * Set URI scheme
     * @see UriScheme
     *
     * @param scheme UriScheme
     * @return URIBuilder instance
     */
    public URIBuilder setScheme(UriScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Set URI scheme
     * <P>
     *
     * Valid scheme values:
     * - http, https, http://, https://
     *
     * @param scheme String uri scheme
     * @return URIBuilder instance
     */
    public URIBuilder setScheme(String scheme) {
        this.scheme = UriScheme.valueOf(getClearedScheme(scheme));
        return this;
    }

    private String getClearedScheme(String scheme) {
        return scheme.trim().replace("://", "").toUpperCase();
    }

    /**
     * Set URI port
     *
     * @param port Port
     * @return URIBuilder instance
     */
    public URIBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * Set URI path
     *
     * @param path Path
     * @return URIBuilder instance
     */
    public URIBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Set parameters from map
     *
     * @param parameters Parameter map
     * @return URIBuilder instance
     */
    public URIBuilder setParameters(Map<String, String[]> parameters) {
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Set Parameters from list
     *
     * @param parameters Parameter list
     * @return URIBuilder instance
     */
    public URIBuilder setParameters(List<Parameter> parameters) {
        parameterList.addParameters(parameters);
        return this;
    }

    /**
     * Set parameter
     * @see com.dejankos.model.Parameter
     *
     * @param parameter Name Value Parameter
     * @return URIBuilder instance
     */
    public URIBuilder setParameter(Parameter parameter) {
        parameterList.addParameter(parameter);
        return this;
    }

    /**
     * Set parameter
     *
     * @param name Parameter name
     * @param value Parameter value
     * @return URIBuilder instance
     */
    public URIBuilder setParameter(String name, String... value) {
        setParameter(new Parameter(name, value));
        return this;
    }

    /**
     * Replace parameter value.
     * If more then one parameter with same name exists, all are replaced.
     *
     * @param name Parameter name
     * @param newValue Parameter new value
     * @return URIBuilder instance
     */
    public URIBuilder replaceParameterValue(String name, String... newValue) {
        parameterList.replaceParameterValue(name, newValue);
        return this;
    }

    /**
     * Remove parameter.
     * If more then one parameter with same name exists, all are removed.
     *
     * @param name Parameter name
     * @return URIBuilder instance
     */
    public URIBuilder removeParameter(String name) {
        parameterList.removeParameter(name);
        return this;
    }

    /**
     * Remove parameter.
     * If more then one parameter with same name and value exists, all are removed.
     *
     * @param parameter Parameter
     * @return URIBuilder instance
     */
    public URIBuilder removeParameter(Parameter parameter) {
        parameterList.removeParameter(parameter);
        return this;
    }

    /**
     * Set charset.
     *
     * @param charset Charset
     * @return URIBuilder instance
     */
    public URIBuilder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Set default UTF-8 charset.
     *
     * @return URIBuilder instance
     */
    public URIBuilder setDefaultCharset() {
        charset = DEFAULT_ENCODING;
        return this;
    }


    /**
     * Build URI as string
     *
     * @return String URI value
     */
    @Override
    public String toString() {
        return build();
    }

    /**
     * Build URI
     *
     * @return constructed URI
     */
    public URI toURI() {
        try {
            return new URI(build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String build() {
        return buildFromParts();
    }

    private String buildFromParts() {
        StringBuilder uri = new StringBuilder()
                .append(getScheme())
                .append(getHost())
                .append(getPort())
                .append(getPath())
                .append(getQueryString());

        return uri.toString();
    }

    private String getScheme() {
        if (scheme == null) {
            return EMPTY_STRING;
        }

        return scheme.getScheme() + SCHEME_HOST_SEPARATOR;
    }

    private String getHost() {
        if (host == null) {
            throw new IllegalStateException("How did you get here in the first place?");
        }

        return host;
    }

    private String getPort() {
        if (port == null) {
            return EMPTY_STRING;
        }

        return ":" + port;
    }

    private String getPath() {
        if (StringUtils.isNullorEmpty(path)) {
            return EMPTY_STRING;
        }

        return path;
    }

    private String getQueryString() {
        if (parameterList.isEmpty()) {
            return EMPTY_STRING;
        }

        return parametersToQueryString();
    }

    private String parametersToQueryString() {
        if (parameterList.isEmpty()) {
            return "";
        }

        StringBuilder queryString = new StringBuilder(URI_QUERIABLE_OBJECT_SEPARATOR);
        for (Parameter parameter : parameterList.getParameterList()) {
            if (queryString.length() > 1) {
                queryString.append(PARAMETER_SEPARATOR);
            }

            queryString.append(getNameValuePair(parameter));
        }

        return queryString.toString();
    }

    private String getNameValuePair(Parameter parameter) {
        if (StringUtils.isNullorEmpty(parameter.getName())) {
            throw new IllegalArgumentException("Query parameter name cannot be empty!");
        } else if (parameter.getValue().length == 0) {
            return String.format("%s%s%s", encode(parameter.getName()), NAME_VALUE_SEPARATOR, "");
        } else {
            StringBuilder nameValuePairs = new StringBuilder();
            for (String value : parameter.getValue()) {
                if (nameValuePairs.length() > 1) {
                    nameValuePairs.append(PARAMETER_SEPARATOR);
                }
                nameValuePairs.append(String.format("%s%s%s", encode(parameter.getName()), NAME_VALUE_SEPARATOR, encode(value)));
            }

            return nameValuePairs.toString();
        }
    }

    private String encode(String value) {
        if (value == null) {
            return EMPTY_STRING;
        }
        if (charset == null) {
            return value;
        }

        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
