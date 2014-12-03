package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeUnparserTest extends ContainerTest {
    private @Inject CodeUnparser codeUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(codeUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testUnparseNoTag() {
        String test = "this is some text";

        assertThat(codeUnparser.unparse(test)).isEqualTo(test);
    }

    @Test
    public void testUnparse() {
        // taken from http://www.informatik-forum.at/misc.php?do=bbcode
        String test = "this is some text before the tag " +
                "<!-- BEGIN TEMPLATE: bbcode_code -->\n" +
                "<div class=\"bbcode_container\">\n" +
                "\t<div class=\"bbcode_description\">Code:</div>\n" +
                "\t<pre class=\"bbcode_code\" style=\"height:84px;\">&lt;script type=&quot;text/javascript&quot;&gt;\n" +
                "&lt;!--\n" +
                "\talert(&quot;Hello world!&quot;);\n" +
                "//--&gt;\n" +
                "&lt;/script&gt;</pre>\n" +
                "</div>\n" +
                "<!-- END TEMPLATE: bbcode_code -->" +
                "and this is some text after the tag";

        String expected = "this is some text before the tag " +
                "[code]<script type=\"text/javascript\">\n" +
                "<!--\n" +
                "\talert(\"Hello world!\");\n" +
                "//-->\n" +
                "</script>[/code]" +
                "and this is some text after the tag";

        assertThat(codeUnparser.unparse(test)).isEqualTo(expected);
    }
}
