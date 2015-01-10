import com.dejankos.builder.URIBuilder;
import com.dejankos.model.Parameter;
import org.junit.Assert;
import org.junit.Test;
import com.dejankos.uri.UriScheme;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UriBuilderTest {


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

    @Test
    public void build_uri_with_querystring_from_list() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("first", "1"));
        parameters.add(new Parameter("second", "2"));
        parameters.add(new Parameter("third", "3"));

        URI uri = URIBuilder.fromHost("www.test.com")
                .setScheme("http://")
                .setPath("/test/path")
                .setParameters(parameters)
                .toURI();

        Assert.assertEquals("http://www.test.com/test/path?first=1&second=2&third=3", uri.toString());
    }

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

        Assert.assertEquals("http://www.test.com/test/path?first=1&first=2&second=3&second=4&third=5&third=6", uri.toString());
    }

    @Test
    public void build_uri_with_querystring_remove_parameters() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "1")
                .setParameter("second", "2")
                .setParameter("third", "3")
                .removeParameter("first")
                .removeParameter(new Parameter("second", "2"))
                .toURI();

        Assert.assertEquals("http://www.test.com?third=3", uri.toString());
    }

    @Test
    public void build_uri_with_querystring_remove_multiple_equal_name_parameter() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "1")
                .setParameter("first", "2")
                .setParameter("first", "3")
                .setParameter("second", "2")
                .removeParameter("first")
                .toURI();

        Assert.assertEquals("http://www.test.com?second=2", uri.toString());
    }

    @Test
    public void build_uri_with_querystring_replace_parameter_value() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "1")
                .setParameter("second", "2")
                .replaceParameterValue("first","3")
                .toURI();

        Assert.assertEquals("http://www.test.com?first=3&second=2", uri.toString());
    }


    @Test
    public void build_uri_with_querystring_default_charset() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setParameter("first", "%&$")
                .setParameter("second", "= =")
                .setParameter("third", "šđčćž")
                .setDefaultCharset()
                .toURI();

        Assert.assertEquals("http://www.test.com?first=%25%26%24&second=%3D+%3D&third=%C5%A1%C4%91%C4%8D%C4%87%C5%BE", uri.toString());
    }

    @Test
    public void build_uri_with_port() {
        URI uri = URIBuilder.fromHost("http://www.test.com")
                .setPort(8080)
                .setPath("/test/path")
                .toURI();

        Assert.assertEquals("http://www.test.com:8080/test/path", uri.toString());
    }
}
