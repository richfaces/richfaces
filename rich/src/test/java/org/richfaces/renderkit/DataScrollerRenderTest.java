package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.richfaces.ui.common.HtmlConstants;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Anton Belevich
 *
 */
public class DataScrollerRenderTest {
    private HtmlUnitEnvironment environment;

    @Before
    public void setUp() {
        environment = new HtmlUnitEnvironment();
        // environment.withWebRoot(new File("src/test/resources"));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/renderkit/faces-config.xml");
        environment.withResource("/test.xhtml", "org/richfaces/renderkit/dataTableTest.xhtml");
        environment.start();
    }

    @Test
    public void testEncoding() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");

        String scrollerId = "scroller1";
        // first scroll
        List<?> nodes = page.getByXPath("//*[@id = 'form:scroller1']");

        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        assertEquals("span", span.getNodeName());
        assertEquals("rf-ds", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        // first/fastRewind/previous buttons with arrows
        HtmlElement first = getFirstButton(page, scrollerId);
        assertEquals("span", first.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-first rf-ds-dis", first.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        HtmlElement fastRewind = getFastRewindButton(page, scrollerId);
        assertEquals("span", fastRewind.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastrwd rf-ds-dis", fastRewind.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        HtmlElement previous = getPreviousButton(page, scrollerId);
        assertEquals("span", previous.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-prev rf-ds-dis", previous.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        // currently selected digital button
        HtmlElement dc1 = getDigitalButton(page, scrollerId, 1);
        assertEquals("span", dc1.getNodeName());
        assertEquals("rf-ds-nmb-btn rf-ds-act", dc1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        // digital buttons
        HtmlElement d2 = getDigitalButton(page, scrollerId, 2);
        assertEquals("a", d2.getNodeName());
        assertEquals("rf-ds-nmb-btn", d2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", d2.getAttribute(HtmlConstants.HREF_ATTR));

        HtmlElement d3 = getDigitalButton(page, scrollerId, 3);
        assertEquals("a", d3.getNodeName());
        assertEquals("rf-ds-nmb-btn", d3.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", d3.getAttribute(HtmlConstants.HREF_ATTR));

        HtmlElement d4 = getDigitalButton(page, scrollerId, 4);
        assertEquals("a", d4.getNodeName());
        assertEquals("rf-ds-nmb-btn", d4.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", d4.getAttribute(HtmlConstants.HREF_ATTR));

        HtmlElement d5 = getDigitalButton(page, scrollerId, 5);
        assertEquals("a", d5.getNodeName());
        assertEquals("rf-ds-nmb-btn", d5.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", d5.getAttribute(HtmlConstants.HREF_ATTR));

        // next/fastForward/last buttons with arrows
        HtmlElement next = getNextButton(page, scrollerId);
        assertEquals("a", next.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-next", next.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", next.getAttribute(HtmlConstants.HREF_ATTR));

        HtmlElement fastForward = getFastForwardButton(page, scrollerId);
        assertEquals("a", fastForward.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd", fastForward.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", fastForward.getAttribute(HtmlConstants.HREF_ATTR));

        HtmlElement last = getLastButton(page, scrollerId);
        assertEquals("a", last.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-last", last.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
        assertEquals("javascript:void(0);", last.getAttribute(HtmlConstants.HREF_ATTR));
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testOutDataScrollerFirstLastButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        HtmlElement last = getLastButton(page, firstScrollerId);
        last.click();
        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        HtmlElement first = getFirstButton(page, firstScrollerId);
        first.click();
        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testInnerDataScrollerFirstLastButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        HtmlElement last = getLastButton(page, secondScrollerId);
        last.click();
        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        HtmlElement first = getFirstButton(page, secondScrollerId);
        first.click();
        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testOutDataScrollerNextPreviousButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 2; i <= 5; i++) {
            HtmlElement next = getNextButton(page, firstScrollerId);

            next.click();

            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 4; i >= 1; i--) {
            HtmlElement previous = getPreviousButton(page, firstScrollerId);
            previous.click();
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);

            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testInnerDataScrollerNextPreviousButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 2; i <= 5; i++) {
            HtmlElement next = getNextButton(page, secondScrollerId);
            next.click();
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller outside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 4; i >= 1; i--) {
            HtmlElement previous = getPreviousButton(page, secondScrollerId);
            previous.click();

            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testOutDataScrollerFastButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 3; i <= 5; i = i + 2) {
            HtmlElement ff = getFastForwardButton(page, firstScrollerId);

            ff.click();

            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 3; i >= 1; i = i - 2) {
            HtmlElement fr = getFastRewindButton(page, firstScrollerId);
            fr.click();
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);

            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testInnerDataScrollerFastButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 3; i <= 5; i = i + 2) {
            HtmlElement ff = getFastForwardButton(page, secondScrollerId);
            ff.click();
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller outside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 3; i >= 1; i = i - 2) {
            HtmlElement fr = getFastRewindButton(page, secondScrollerId);
            fr.click();

            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testOutDataScrollerDigitalButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 2; i <= 5; i++) {
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            currentDigital1.click();

            currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 4; i >= 1; i--) {
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            currentDigital1.click();

            currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @Test
    @Ignore // broke with the jQuery 1.6.2 upgrade
    public void testInnerDataScrollerDigitalButtons() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        String firstScrollerId = "scroller1";

        // dataScroller inside dataTable 'footer' facet
        String secondScrollerId = "richTable:scroller2";

        for (int i = 2; i <= 5; i++) {
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            currentDigital2.click();

            currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller outside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkLastPageButtons(page, firstScrollerId, secondScrollerId);

        // scroll back
        for (int i = 4; i >= 1; i--) {
            HtmlElement currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            currentDigital2.click();

            currentDigital2 = getDigitalButton(page, secondScrollerId, i);
            assertEquals("span", currentDigital2.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital2.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // if scroller inside dataTable has switched
            HtmlElement currentDigital1 = getDigitalButton(page, firstScrollerId, i);
            assertEquals("span", currentDigital1.getNodeName());
            assertEquals("rf-ds-nmb-btn rf-ds-act", currentDigital1.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

            // check if page has switched
            assertEquals(i + " page content", getCurrentPageContent(page, i));
        }

        checkFirstPageButtons(page, firstScrollerId, secondScrollerId);
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;
    }

    private void checkFirstPageButtons(HtmlPage page, String firstScrollerId, String secondScrollerId) throws Exception {
        HtmlElement fastForward = getFastForwardButton(page, firstScrollerId);
        HtmlElement last = getLastButton(page, firstScrollerId);
        HtmlElement next = getNextButton(page, firstScrollerId);

        // check right buttons
        assertEquals("a", fastForward.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd", fastForward.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", last.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-last", last.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", next.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-next", next.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        fastForward = getFastForwardButton(page, secondScrollerId);
        last = getLastButton(page, secondScrollerId);
        next = getNextButton(page, secondScrollerId);

        assertEquals("a", fastForward.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd", fastForward.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", last.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-last", last.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", next.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-next", next.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        // check if left buttons is disabled
        HtmlElement fastRewind = getFastRewindButton(page, firstScrollerId);
        HtmlElement first = getFirstButton(page, firstScrollerId);
        HtmlElement previous = getPreviousButton(page, firstScrollerId);

        assertEquals("span", fastRewind.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastrwd rf-ds-dis", fastRewind.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", first.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-first rf-ds-dis", first.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", previous.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-prev rf-ds-dis", previous.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        fastRewind = getFastRewindButton(page, secondScrollerId);
        first = getFirstButton(page, secondScrollerId);
        next = getNextButton(page, secondScrollerId);

        assertEquals("span", fastRewind.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastrwd rf-ds-dis", fastRewind.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", first.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-first rf-ds-dis", first.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", previous.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-prev rf-ds-dis", previous.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
    }

    private void checkLastPageButtons(HtmlPage page, String firstScrollerId, String secondScrollerId) throws Exception {
        // check if right buttons is disabled
        HtmlElement fastForward = getFastForwardButton(page, firstScrollerId);
        HtmlElement last = getLastButton(page, firstScrollerId);
        HtmlElement next = getNextButton(page, firstScrollerId);

        assertEquals("span", fastForward.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd rf-ds-dis", fastForward.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", last.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-last rf-ds-dis", last.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", next.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-next rf-ds-dis", next.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        fastForward = getFastForwardButton(page, secondScrollerId);
        last = getFastForwardButton(page, secondScrollerId);
        next = getNextButton(page, secondScrollerId);

        assertEquals("span", fastForward.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd rf-ds-dis", fastForward.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", last.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastfwd rf-ds-dis", last.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("span", next.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-next rf-ds-dis", next.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        // check if left buttons is enabled
        HtmlElement fastRewind = getFastRewindButton(page, firstScrollerId);
        HtmlElement first = getFirstButton(page, firstScrollerId);
        HtmlElement previous = getPreviousButton(page, firstScrollerId);

        assertEquals("a", fastRewind.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastrwd", fastRewind.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", first.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-first", first.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", previous.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-prev", previous.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        fastRewind = getFastRewindButton(page, secondScrollerId);
        first = getFirstButton(page, secondScrollerId);
        previous = getPreviousButton(page, secondScrollerId);

        assertEquals("a", fastRewind.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-fastrwd", fastRewind.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", first.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-first", first.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());

        assertEquals("a", previous.getNodeName());
        assertEquals("rf-ds-btn rf-ds-btn-prev", previous.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).trim());
    }

    private String getCurrentPageContent(HtmlPage page, int i) throws Exception {
        HtmlElement content = page.getFirstByXPath("//*[@id = 'form:richTable:" + (--i) + ":pageContent']");
        DomNode text = content.getFirstChild();
        assertEquals(DomNode.TEXT_NODE, text.getNodeType());
        return text.getNodeValue();
    }

    private HtmlElement getPreviousButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_prev']");
    }

    private HtmlElement getNextButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_next']");
    }

    private HtmlElement getFastRewindButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_fr']");
    }

    private HtmlElement getFirstButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_f']");
    }

    private HtmlElement getFastForwardButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_ff']");
    }

    private HtmlElement getLastButton(HtmlPage page, String scrollerId) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_l']");
    }

    private HtmlElement getDigitalButton(HtmlPage page, String scrollerId, int i) {
        return page.getFirstByXPath("//*[@id = 'form:" + scrollerId + "_ds_" + i + "']");
    }
}
