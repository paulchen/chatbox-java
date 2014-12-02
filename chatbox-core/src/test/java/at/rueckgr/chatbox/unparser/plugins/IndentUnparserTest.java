package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

public class IndentUnparserTest extends ContainerTest {
    private @Inject IndentUnparser indentUnparser;

    @Test
    public void testUnparseEmpty() {
        assertEquals(indentUnparser.unparse(""), "");
    }

    @Test
    public void testNoBold() {
        String test = "This is <i>some</i> text";
        String expected = "This is <i>some</i> text";

        assertEquals(indentUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <blockquote><div>some</div></blockquote> text";
        String expected = "This is [indent]some[/indent] text";

        assertEquals(indentUnparser.unparse(test), expected);
    }

}