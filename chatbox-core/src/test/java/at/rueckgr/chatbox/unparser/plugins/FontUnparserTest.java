package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.*;

public class FontUnparserTest extends ContainerTest {
    private @Inject FontUnparser fontUnparser;

    @Test
    public void testUnparseNoTag() {
        String test = "This is some text";

        assertEquals(fontUnparser.unparse(test), test);
    }

    @Test
    public void testUnparseEmpty() {
        String test = "";

        assertEquals(fontUnparser.unparse(test), test);
    }

    @Test
    public void testUnparseColor() {
        String test = "This <font color=\"blue\">is</font> some text";
        String expected = "This [color=blue]is[/color] some text";

        assertEquals(fontUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparseSize() {
        String test = "This <font size=\"+2\">is</font> some text";
        String expected = "This [size=+2]is[/size] some text";

        assertEquals(fontUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparseFont() {
        String test = "This <font face=\"courier\">is</font> some text";
        String expected = "This [font=courier]is[/font] some text";

        assertEquals(fontUnparser.unparse(test), expected);
    }

    @Test
    public void testUnparseNested() {
        String test = "This is <font face=\"courier\">some <font size=\"+2\">longer <font color=\"blue\">text</font></font> <font color=\"red\">which</font></font> " +
                "is used for testing <font color=\"blue\">the <font size=\"+2\">FontUnparser</font> with</font> nested font tags";
        String expected = "This is [font=courier]some [size=+2]longer [color=blue]text[/color][/size] [color=red]which[/color][/font] " +
                "is used for testing [color=blue]the [size=+2]FontUnparser[/size] with[/color] nested font tags";

        assertEquals(fontUnparser.unparse(test), expected);
    }
}