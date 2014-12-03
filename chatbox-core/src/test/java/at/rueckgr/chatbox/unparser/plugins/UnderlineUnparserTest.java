package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class UnderlineUnparserTest extends ContainerTest {
    private @Inject UnderlineUnparser underlineUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(underlineUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testNoItalics() {
        String test = "This is <b>some</b> text";
        String expected = "This is <b>some</b> text";

        assertThat(underlineUnparser.unparse(test)).isEqualTo(expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <u>some</u> text";
        String expected = "This is [u]some[/u] text";

        assertThat(underlineUnparser.unparse(test)).isEqualTo(expected);
    }
}
