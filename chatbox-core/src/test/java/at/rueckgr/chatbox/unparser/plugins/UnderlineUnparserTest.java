package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

public class UnderlineUnparserTest extends ContainerTest {
    private @Inject UnderlineUnparser underlineUnparser;

    @Test
    public void testUnparseEmpty() {
        assertEquals(underlineUnparser.unparse(""), "");
    }

    @Test
    public void testNoItalics() {
        String test = "This is <b>some</b> text";
        String expected = "This is <b>some</b> text";

        assertEquals(underlineUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <u>some</u> text";
        String expected = "This is [u]some[/u] text";

        assertEquals(underlineUnparser.unparse(test), expected);
    }
}