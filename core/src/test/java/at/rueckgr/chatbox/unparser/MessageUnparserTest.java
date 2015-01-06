package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class MessageUnparserTest extends ContainerTest {
    private @Inject MessageUnparser messageUnparser;

    @Test
    public void testSpoiler() {
        // text taken from chatbox message with id 983839
        String input = "<div style=\"margin:5px; margin-top:5px;width:auto\">" +
                "<div class=\"smallfont\">Spoiler: (<i>Highlight this box to see the hidden message.</i>)</div>" +
                "<pre class=\"alt2\" style=\"margin:0px; padding:2px; border:1px inset; width:100%;overflow:auto\">" +
                "<div dir=\"ltr\" style=\"text-align:left;\"><table bgcolor=\"#E1E4F2\"><tbody><tr><td>" +
                "<font color=\"#E1E4F2\">kommt auf einmal die 2. armee die viel staerker sein soll ist der krieg auf " +
                "einmal in 2 sec vorbei " +
                "<img src=\"pics/nb/smilies/eek2.gif\" border=\"0\" alt=\"\" title=\"Eek2\" class=\"inlineimg\">" +
                "</font></td></tr></tbody></table></div></pre></div>";
        messageUnparser.unparse(input);

        // TODO check result
    }
}
