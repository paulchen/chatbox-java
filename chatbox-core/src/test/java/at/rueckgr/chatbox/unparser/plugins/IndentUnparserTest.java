package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class IndentUnparserTest extends ContainerTest {
    private @Inject IndentUnparser indentUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(indentUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testNoBold() {
        String test = "This is <i>some</i> text";
        String expected = "This is <i>some</i> text";

        assertThat(indentUnparser.unparse(test)).isEqualTo(expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <blockquote><div>some</div></blockquote> text";
        String expected = "This is [indent]some[/indent] text";

        assertThat(indentUnparser.unparse(test)).isEqualTo(expected);
    }

}