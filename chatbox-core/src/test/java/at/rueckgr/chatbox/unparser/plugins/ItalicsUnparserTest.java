package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

public class ItalicsUnparserTest extends ContainerTest {
    private @Inject ItalicsUnparser italicsUnparser;

    @Test
    public void testUnparseEmpty() {
        assertEquals(italicsUnparser.unparse(""), "");
    }

    @Test
    public void testNoItalics() {
        String test = "This is <b>some</b> text";
        String expected = "This is <b>some</b> text";

        assertEquals(italicsUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <i>some</i> text";
        String expected = "This is [i]some[/i] text";

        assertEquals(italicsUnparser.unparse(test), expected);
    }

}