package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class SchlaegereiResponderTest extends ContainerTest {
    private @Inject SchlaegereiResponder schlaegereiResponder;
    private @Inject ResponderTestHelper responderTestHelper;

    @Test(dataProvider = "positiveTestData")
    public void testSchlaegereiPositive(String input, String output) {
        responderTestHelper.simpleTest(schlaegereiResponder, input, output);
    }

    @Test(dataProvider = "negativeTestData")
    public void testSchlaegereiNegative(String input) {
        responderTestHelper.simpleTest(schlaegereiResponder, input);

    }

    @DataProvider(name = "positiveTestData")
    public String[][] dataProviderPositiveTestData() {
        return new String[][] {
                {"schlaegerei", "/me schlaegt test"},
                {"schlaegerei!", "/me schlaegt test"},
                {" schlaegerei ", "/me schlaegt test"},
                {" SCHLAEGERei ", "/me schlaegt test"},
                {" SCHLAEGERei! ", "/me schlaegt test"},
                {"schl\u00e4gerei", "/me schl\u00e4gt test"},
                {"schl\u00e4gerei!", "/me schl\u00e4gt test"},
                {" schl\u00e4gerei ", "/me schl\u00e4gt test"},
                {" SCHL\u00c4GERei ", "/me schl\u00e4gt test"},
                {" SCHL\u00c4GERei! ", "/me schl\u00e4gt test"},
        };
    }

    @DataProvider(name = "negativeTestData")
    public String[][] dataProviderNegativeTestData() {
        return new String[][] {
                {"abc schlaegerei abc"},
                {"abc schlaegerei! abc"},
                {"abc SCHLAEGEREI abc"},
                {"abc SCHLAEGEREI! abc"},
                {"abc schl\u00e4gerei abc"},
                {"abc schl\u00e4gerei! abc"},
                {"abc SCHL\u00c4GEREI abc"},
                {"abc SCHL\u00c4GEREI! abc"},
        };
    }
}
