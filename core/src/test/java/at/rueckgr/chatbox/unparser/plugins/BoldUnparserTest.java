package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class BoldUnparserTest extends ContainerTest {
    private @Inject BoldUnparser boldUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(boldUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testNoBold() {
        String test = "This is <i>some</i> text";
        String expected = "This is <i>some</i> text";

        assertThat(boldUnparser.unparse(test)).isEqualTo(expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <b>some</b> text";
        String expected = "This is [b]some[/b] text";

        assertThat(boldUnparser.unparse(test)).isEqualTo(expected);
    }
}
