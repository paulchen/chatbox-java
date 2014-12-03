package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class StrikeUnparserTest extends ContainerTest {
    private @Inject StrikeUnparser strikeUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(strikeUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testNoBold() {
        String test = "This is <i>some</i> text";
        String expected = "This is <i>some</i> text";

        assertThat(strikeUnparser.unparse(test)).isEqualTo(expected);
    }

    @Test
    public void testUnparse() {
        String test = "This is <strike>some</strike> text";
        String expected = "This is [strike]some[/strike] text";

        assertThat(strikeUnparser.unparse(test)).isEqualTo(expected);
    }
}
