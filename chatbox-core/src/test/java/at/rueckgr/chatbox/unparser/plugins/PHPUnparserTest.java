package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class PHPUnparserTest extends ContainerTest {
    private @Inject PHPUnparser phpUnparser;

    @Test
    public void testUnparseEmpty() {
        assertThat(phpUnparser.unparse("")).isEmpty();
    }

    @Test
    public void testUnparseNoTag() {
        String test = "this is some text";

        assertThat(phpUnparser.unparse(test)).isEqualTo(test);
    }

    @Test
    public void testUnparse() {
        // taken from http://www.informatik-forum.at/misc.php?do=bbcode
        String test = "this is some text before the tag " +
                "<!-- BEGIN TEMPLATE: bbcode_php -->\n" +
                "<div class=\"bbcode_container\">\n" +
                "\t<div class=\"bbcode_description\">PHP Code:</div>\n" +
                "\t<div class=\"bbcode_code\" style=\"height:84px;\"><code><code><span style=\"color: #000000\">\n" +
                "<span style=\"color: #0000BB\">$myvar&nbsp;</span><span style=\"color: #007700\">=&nbsp;</span><span style=\"color: #DD0000\">'Hello&nbsp;World!'</span>" +
                "<span style=\"color: #007700\">;<br />for&nbsp;(</span><span style=\"color: #0000BB\">$i&nbsp;</span><span style=\"color: #007700\">=&nbsp;</span>" +
                "<span style=\"color: #0000BB\">0</span><span style=\"color: #007700\">;&nbsp;</span><span style=\"color: #0000BB\">$i&nbsp;</span>" +
                "<span style=\"color: #007700\">&lt;&nbsp;</span><span style=\"color: #0000BB\">10</span><span style=\"color: #007700\">;&nbsp;</span>" +
                "<span style=\"color: #0000BB\">$i</span><span style=\"color: #007700\">++)<br />{<br />&nbsp;&nbsp;&nbsp;&nbsp;echo&nbsp;</span>" +
                "<span style=\"color: #0000BB\">$myvar&nbsp;</span><span style=\"color: #007700\">.&nbsp;</span><span style=\"color: #DD0000\">\"\\n\"</span>" +
                "<span style=\"color: #007700\">;<br />}&nbsp;\n" +
                "<br /></span><span style=\"color: #0000BB\"></span>\n" +
                "</span>\n" +
                "</code></code></div>\n" +
                "</div>\n" +
                "<!-- END TEMPLATE: bbcode_php -->" +
                "and this is some text after the tag";

        String expected = "this is some text before the tag " +
                "[php]$myvar = 'Hello World!';\n" +
                "for ($i = 0; $i < 10; $i++)\n" +
                "{\n" +
                "    echo $myvar . \"\\n\";\n" +
                "}[/php]" +
                "and this is some text after the tag";

        assertThat(phpUnparser.unparse(test)).isEqualTo(expected);
    }

}