import com.dejankos.builder.URIParser;
import com.dejankos.model.Parameter;
import com.dejankos.model.ParsedURI;
import org.junit.Test;
import com.dejankos.uri.UriScheme;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class URIParserTest {
    
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

    @Test
    public void parse_uri() {
        ParsedURI parsedURI = URIParser.parseFromString("http://www.test.com");

        assertEquals(UriScheme.HTTP, parsedURI.getScheme());
        assertEquals("www.test.com", parsedURI.getHost());
        assertNull(parsedURI.getPort());
        assertEquals("", parsedURI.getPath());
        assertTrue(parsedURI.getParameterList().size() == 0);
    }
}


