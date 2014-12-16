package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class ItalicsUnparserTest extends ContainerTest {
    private @Inject ItalicsUnparser italicsUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(italicsUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testNoItalics() {
        String test = "This is <b>some</b> text";
        String expected = "This is <b>some</b> text";

        assertThat(italicsUnparser.unparse(test)).isEqualTo(expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <i>some</i> text";
        String expected = "This is [i]some[/i] text";

        assertThat(italicsUnparser.unparse(test)).isEqualTo(expected);
    }
}
