# URIBuilder

A simple URI builder with no dependencies.  
Inspired by Apache HTTP Component and Spring UriComponentsBuilder.
Contains a simple URIParser too.

## URIBuilder Examples

Easy to create a new URI.  
Add parameter as String or new Parameter object.

```java
    @Test
    public void build_uri_with_querystring() {

        URI uri = URIBuilder.fromHost("www.test.com")
                .setScheme(UriScheme.HTTP)
                .setPath("/test/path")
                .setParameter("first", "1")
                .setParameter(new Parameter("second", "2"))
                .toURI();

        Assert.assertEquals("http://www.test.com/test/path?first=1&second=2", uri.toString());
    }
```
Create a new URI with parameters from map (e.g. when creating a new URI from ServletRequest.getParameterMap())
```java
    @Test
    public void build_uri_with_querystring_from_map() {
        Map<String, String[]> parameterMap = new LinkedHashMap<>();
        parameterMap.put("first", new String[]{"1"});
        parameterMap.put("second", new String[]{"2"});
        parameterMap.put("third", new String[]{"3"});

        URI uri = URIBuilder.fromHost("www.test.com")
                .setScheme("http://")
                .setPath("/test/path")
                .setParameters(parameterMap)
                .toURI();

        Assert.assertEquals("http://www.test.com/test/path?first=1&second=2&third=3", uri.toString());
    }
```

Manipulate existing URI
```java
    @Test
    public void build_and_manipulate_from_existing_uri() throws URISyntaxException {
        URI someUri = new URI("http://www.test.com/test/path?first=1&second=2&third=3");

        URI uri = URIBuilder.fromURI(someUri)
                .setParameter("new", "newValue")
                .removeParameter("first")
                .setPort(8080)
                .setScheme("https")
                .toURI();

        Assert.assertEquals("https://www.test.com:8080/test/path?second=2&third=3&new=newValue", uri.toString());
    }
```

Multiple values parameter
```java
    @Test
    public void build_uri_with_querystring_multiple_parameter_values() {
        Map<String, String[]> parameterMap = new LinkedHashMap<>();
        parameterMap.put("first", new String[]{"1", "2"});

        URI uri = URIBuilder.fromHost("www.test.com")
                .setScheme("http://")
                .setPath("/test/path")
                .setParameters(parameterMap)
                .setParameter("second", "3", "4")
                .setParameter(new Parameter("third", "5", "6"))
                .toURI();

        Assert.assertEquals("http://www.test.com/test/path?first=1&first=2&second=3&second=4&third=5&third=6",
        uri.toString());
    }
```

Replace parameter value
```java
    @Test
    public void build_uri_with_querystring_replace_parameter_value() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "1")
                .setParameter("second", "2")
                .replaceParameterValue("first","3")
                .toURI();

        Assert.assertEquals("http://www.test.com?first=3&second=2", uri.toString());
    }
```

UTF-8 used as default character set  
```java
    @Test
    public void build_uri_with_querystring_default_charset() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "%&$")
                .setParameter("second", "= =")
                .setParameter("third", "šđčćž")
                .setDefaultCharset()
                .toURI();

        Assert.assertEquals("http://www.test.com?first=%25%26%24&second=%3D+%3D&third=%C5%A1%C4%91%C4%8D%C4%87%C5%BE",       uri.toString());
    }
```

## URIParser Examples  
  
```java
    @Test
    public void parse_uri_with_querystring() {
        List<Parameter> expectedParameterList = new ArrayList<>();
        expectedParameterList.add(new Parameter("first", "1"));
        expectedParameterList.add(new Parameter("second", "2"));

        ParsedURI parsedURI = URIParser.parseFromString("http://www.test.com/test/path?first=1&second=2");

        assertEquals(UriScheme.HTTP, parsedURI.getScheme());
        assertEquals("www.test.com", parsedURI.getHost());
        assertEquals("/test/path", parsedURI.getPath());
        assertTrue(parsedURI.getParameterList().size() == 2);
        assertEquals(expectedParameterList, parsedURI.getParameterList());
        assertTrue(parsedURI.getParameterMap().size() == 2);
        assertArrayEquals(new String[]{"1"}, parsedURI.getParameterMap().get("first"));
        assertArrayEquals(new String[]{"2"}, parsedURI.getParameterMap().get("second"));
    }
```


