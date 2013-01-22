
/**
 *
 */
package org.ajax4jsf.tests;

import java.util.ArrayList;
import java.util.List;

import org.ajax4jsf.renderkit.RendererUtils.HTML;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * @author Maksim Kaszynski
 *
 */
public final class HtmlTestUtils {
    private HtmlTestUtils() {}

    /**
     * Extract list of all &lt;script&gt; elements' src attributes
     * @param page
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> extractScriptSources(HtmlPage page) {
        List<HtmlScript> list = page.getDocumentHtmlElement().getHtmlElementsByTagName(HTML.SCRIPT_ELEM);
        List<String> sources = new ArrayList<String>();

        for (HtmlScript htmlScript : list) {
            String srcAttribute = htmlScript.getSrcAttribute();

            if (StringUtils.isNotBlank(srcAttribute)) {
                sources.add(srcAttribute);
            }
        }

        return sources;
    }
}
