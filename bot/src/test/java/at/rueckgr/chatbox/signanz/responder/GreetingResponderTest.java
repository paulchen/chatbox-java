package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class GreetingResponderTest extends ContainerTest {
    private @Inject GreetingResponder greetingResponder;
    private @Inject ResponderTestHelper responderTestHelper;

    @Test(dataProvider = "loveReplies")
    public void testLoveReplies(String input, String output) {
        simpleTest(input, output);
    }

    @Test(dataProvider = "brohoofReplies")
    public void testBrohoof(String input, String output) {
        simpleTest(input, output);
    }

    @Test
    public void testTroest() {
        simpleTest("signanz :traurig:", "test :troest:");
    }

    private void simpleTest(String input, String output) {
        responderTestHelper.simpleTest(greetingResponder, input, output);
    }

    @DataProvider(name = "brohoofReplies")
    public String[][] dataProviderBrohoofReplies() {
        return new String[][] {
            {"signanz /)", "test (\\" },
            {"signanz (\\", "test /)" },
            {"signanz /]", "test [\\" },
            {"signanz [\\", "test /]" },
            {"signanz (\\ ^ . ^ /)", "test (\\ ^ . ^ /)" },
        };
    }

    @DataProvider(name = "loveReplies")
    public String[][] dataProviderLoveReplies() {
        return new String[][] {
                {"signanz :inlove:", "test :inlove:" },
                {"signanz :druegg:", "test :druegg:" },
                {"signanz :knutsch:", "test :knutsch:" },
                {"signanz :^101:", "test :^101:" },
                {"signanz <3", "test <3" },
        };
    }

}
