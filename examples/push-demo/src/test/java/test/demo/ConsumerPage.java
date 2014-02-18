package test.demo;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

@Location("examples/consumer.jsf")
public class ConsumerPage {

    @FindByJQuery("span[id$='outputDate']")
    private WebElement outputOfConsumedMsg;

    @FindByJQuery("input[type='submit']:eq(0)")
    private WebElement pushServerEventBtn;

    @FindByJQuery("a[id$='incorrectRequestForPushFilter']")
    private WebElement incorrectRequestForPushFilter;

    private void waitForServerMsg(String previousOutput) {
        Graphene.waitModel().until().element(outputOfConsumedMsg).text().not().equalTo(previousOutput);
    }

    public void makeCorrectPush() {
        String previousPushOutput = outputOfConsumedMsg.getText();
        pushServerEventBtn.click();
//        waitForServerMsg(previousPushOutput);
    }

    public void makeMalformedPushRequest() {
        Graphene.guardHttp(incorrectRequestForPushFilter).click();
    }
}
