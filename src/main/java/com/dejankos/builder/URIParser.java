package com.dejankos.builder;

import com.dejankos.model.Parameter;
import com.dejankos.model.ParsedURI;
import com.dejankos.uri.UriScheme;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.dejankos.builder.Constant.*;

public final class URIParser {

    /**
     * Parse from string.
     * Default "UTF-8" encoding will be used.
     * @see ParsedURI 
     *  
     * @param uri String uri
     * @return ParsedURI
     */
    public static ParsedURI parseFromString(String uri) {
        return parseFromString(uri, DEFAULT_ENCODING);
    }

    /**
     * Parse from string.
     * @see ParsedURI
     *  
     * @param uri String uri
     * @param enc String encoding
     * @return ParsedURI
     */
    public static ParsedURI parseFromString(String uri, String enc) {
        try {
            return parseFromURI(new URI(uri), enc);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse from URI.
     * Default "UTF-8" encoding will be used.
     *  
     * @param uri URI uri
     * @return ParsedURI
     */
    public static ParsedURI parseFromURI(URI uri) {
        return parseFromURI(uri, DEFAULT_ENCODING);
    }

    /**
     * Parse from URI.
     *
     * @param uri URI uri
     * @param enc String encoding
     * @return ParsedURI
     */
    public static ParsedURI parseFromURI(URI uri, String enc) {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();


        return new ParsedURI(UriScheme.valueOf(scheme.toUpperCase()),
                host,
                path,
                port > -1 ? port : null,
                getParsedQueryString(query, enc));
    }

    private static List<Parameter> getParsedQueryString(String queryString, String enc) {
        if (StringUtils.isNullorEmpty(queryString)) {
            return Collections.EMPTY_LIST;
        }
        if (StringUtils.isNullorEmpty(enc)) {
            enc = DEFAULT_ENCODING;
        }

        List<Parameter> parameterList = new LinkedList<>();
        String[] pairs = queryString.split(PARAMETER_SEPARATOR);
        for (String pair : pairs) {
            String[] nameValuePair = pair.split(NAME_VALUE_SEPARATOR);
            parameterList.add(new Parameter(decode(nameValuePair[0], enc), decode(nameValuePair[1], enc)));
        }

        return parameterList;
    }

    private static String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
