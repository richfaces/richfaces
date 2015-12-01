/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.component.panelMenu;

import static java.text.MessageFormat.format;

import java.util.List;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.collect.Lists;

/**
 * Generator for all IT_RF10169 pages.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class IT_RF10169_PagesGenerator {

    public static final String[] ALL_ATTRIBUTES = new String[] {
        "groupClass",
        "groupCollapsedLeftIcon",
        "groupCollapsedRightIcon",
        "groupDisabledClass",
        "groupDisabledLeftIcon",
        "groupDisabledRightIcon",
        "groupExpandedLeftIcon",
        "groupExpandedRightIcon",
        "itemClass",
        "itemDisabledClass",
        "itemDisabledLeftIcon",
        "itemDisabledRightIcon",
        "itemLeftIcon",
        "itemRightIcon"
    };

    public static final int COUNT_INNER_DISABLED_GROUPS = 1;
    public static final int COUNT_INNER_DISABLED_ITEMS = 1;
    public static final int COUNT_INNER_NOT_DISABLED_GROUPS = 1;
    public static final int COUNT_INNER_NOT_DISABLED_ITEMS = 18;// 6 are in disabled groups
    public static final int COUNT_TOP_DISABLED_GROUPS = 1;
    public static final int COUNT_TOP_DISABLED_ITEMS = 2;
    public static final int COUNT_TOP_NOT_DISABLED_GROUPS = 3;
    public static final int COUNT_TOP_NOT_DISABLED_ITEMS = 3;
    public static final int COUNT_ALL_DISABLED_GROUPS = COUNT_INNER_DISABLED_GROUPS + COUNT_TOP_DISABLED_GROUPS;
    public static final int COUNT_ALL_DISABLED_ITEMS = COUNT_INNER_DISABLED_ITEMS + COUNT_TOP_DISABLED_ITEMS;
    public static final int COUNT_ALL_NOT_DISABLED_GROUPS = COUNT_INNER_NOT_DISABLED_GROUPS + COUNT_TOP_NOT_DISABLED_GROUPS;
    public static final int COUNT_ALL_NOT_DISABLED_ITEMS = COUNT_INNER_NOT_DISABLED_ITEMS + COUNT_TOP_NOT_DISABLED_ITEMS;

    public static final String DEFAULT_CLASS = "klass";
    public static final String DEFAULT_ICON = "triangle";
    public static final String DEFAULT_TOP_CLASS = "topKlass";
    public static final String DEFAULT_TOP_ICON = "chevron";

    private static final String EMPTY_STRING = "";
    private static final String ICON = "icon";
    private static final String TEMPLATE_ATTRIBUTE_VALUE = "{0}=\"{1}\" ";
    private static final String TOP = "top";

    private static final List<String> links = Lists.newArrayList();

    private static void addAllPages(RichDeployment deployment) {
        final String[] names = new String[] { EMPTY_STRING, "WithTopClass", "WithEmptyTopClass" };
        int i;
        String pageName;
        for (String att : ALL_ATTRIBUTES) {
            i = 0;
            for (String attsCombination : getAttributesCombinationForClassName(att)) {
                pageName = format("{0}{1}.xhtml", att, names[i++]);
                links.add(pageName);
                addPage(deployment, pageName, attsCombination);
            }
        }
    }

    /**
     * Creates an index page for easier manual browsing of all the samples
     */
    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<style type=\"text/css\">");
        p.body("    a { font-size: 16px;}");
        p.body("</style>");
        p.body("<ul>");
        for (String link : links) {
            p.body(format("<li><a href=\"{0}.jsf\">{0}</a></li>", link.replace(".xhtml", EMPTY_STRING)));
        }
        p.body("</ul>");
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    /**
     * Creates page with panelMenu with specified attributes and saves it under specified page name.
     */
    private static void addPage(RichDeployment deployment, String pageName, String atts) {
        FaceletAsset p = new FaceletAsset();

        p.body("<style type=\"text/css\">");
        p.body("    ." + DEFAULT_CLASS + " { border: 2px dashed orange }");
        p.body("    ." + DEFAULT_TOP_CLASS + " { border: 2px solid blue }");
        p.body("    a { font-size: 16px;}");
        p.body("</style>");

        p.form("<p>");
        p.form("    <a href=\"index.jsf\">To index page</a>");
        p.form("</p>");

        p.form("<p>");
        p.form("    Following panelMenu has these attributes: <b>", atts, "</b>");
        p.form("</p>");
        p.form("<br/>");

        p.form("<rich:panelMenu id=\"panelMenu\" ", atts, " >");
        p.form("    <rich:panelMenuGroup id=\"group1\" name=\"group1\" label=\"Group 1\">");
        p.form("        <rich:panelMenuItem id=\"item11\" name=\"item11\" label=\"Item 1.1\"/>");
        p.form("        <rich:panelMenuItem id=\"item12\" name=\"item12\" label=\"Item 1.2\"/>");
        p.form("        <rich:panelMenuItem id=\"item13\" name=\"item13\" label=\"Item 1.3\"/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuItem id=\"item1\" name=\"item1\" label=\"Item 1\"/>");
        p.form("    <rich:panelMenuGroup id=\"group2\"");
        p.form("                         name=\"group2\"");
        p.form("                         label=\"Group 2\">");
        p.form("        <rich:panelMenuItem id=\"item21\" name=\"item21\" label=\"Item 2.1\"/>");
        p.form("        <rich:panelMenuItem id=\"item22\" name=\"item22\" label=\"Item 2.2\"/>");
        p.form("        <rich:panelMenuItem id=\"item23\" name=\"item23\" label=\"Item 2.3\"/>");
        p.form("        <rich:panelMenuGroup id=\"group24\"");
        p.form("                             name=\"group24\"");
        p.form("                             label=\"Group 2.4\">");
        p.form("            <rich:panelMenuItem id=\"item241\" name=\"item241\" label=\"Item 2.4.1\"/>");
        p.form("            <rich:panelMenuItem id=\"item242\" name=\"item242\" label=\"Item 2.4.2\"/>");
        p.form("            <rich:panelMenuItem id=\"item243\" name=\"item243\" label=\"Item 2.4.3\"/>");
        p.form("        </rich:panelMenuGroup>");
        p.form("        <rich:panelMenuItem id=\"item25\" name=\"item25\" disabled=\"true\" label=\"Item 2.5\"/>");
        p.form("        <rich:panelMenuGroup id=\"group26\"");
        p.form("                             name=\"group26\"");
        p.form("                             label=\"Group 2.6\"");
        p.form("                             disabled=\"true\">");
        p.form("            <rich:panelMenuItem id=\"item261\" name=\"item261\" label=\"Item 2.6.1\"/>");
        p.form("            <rich:panelMenuItem id=\"item262\" name=\"item262\" label=\"Item 2.6.2\"/>");
        p.form("            <rich:panelMenuItem id=\"item263\" name=\"item263\" label=\"Item 2.6.3\"/>");
        p.form("        </rich:panelMenuGroup>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuItem id=\"item2\" name=\"item2\" label=\"Item 2\" disabled=\"true\" />");
        p.form("    <rich:panelMenuGroup id=\"group3\"");
        p.form("                         name=\"group3\"");
        p.form("                         label=\"Group 3\">");
        p.form("        <rich:panelMenuItem id=\"item31\" name=\"item31\" label=\"Item 3.1\"/>");
        p.form("        <rich:panelMenuItem id=\"item32\" name=\"item32\" label=\"Item 3.2\"/>");
        p.form("        <rich:panelMenuItem id=\"item33\" name=\"item33\" label=\"Item 3.3\"/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuGroup id=\"group4\"");
        p.form("                         name=\"group4\"");
        p.form("                         label=\"Group 4\"");
        p.form("                         disabled=\"true\">");
        p.form("        <rich:panelMenuItem id=\"item41\" name=\"item41\" label=\"Item 4.1\"/>");
        p.form("        <rich:panelMenuItem id=\"item42\" name=\"item42\" label=\"Item 4.2\"/>");
        p.form("        <rich:panelMenuItem id=\"item43\" name=\"item43\" label=\"Item 4.3\"/>");
        p.form("    </rich:panelMenuGroup>");
        p.form("    <rich:panelMenuItem id=\"item3\" name=\"item3\" label=\"Item 3\" />");
        p.form("    <rich:panelMenuItem id=\"item4\" name=\"item4\" label=\"Item 4\" disabled=\"true\" />");
        p.form("    <rich:panelMenuItem id=\"item5\" name=\"item5\" label=\"Item 5\" />");
        p.form("</rich:panelMenu>");

        deployment.archive().addAsWebResource(p, pageName);
    }

    private static String createAttributeWithValue(String att, String value) {
        return format(TEMPLATE_ATTRIBUTE_VALUE, att, value);
    }

    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(IT_RF10169.class);
        addAllPages(deployment);
        addIndexPage(deployment);
        return deployment.getFinalArchive();
    }

    private static String[] getAttributesCombinationForClassName(String className) {
        String cTopClass = getCorrespondingTopClass(className);
        boolean containsIcon = className.toLowerCase().contains(ICON);
        String value = containsIcon ? DEFAULT_ICON : DEFAULT_CLASS;
        String emptyValue = EMPTY_STRING;
        String topValue = containsIcon ? DEFAULT_TOP_ICON : DEFAULT_TOP_CLASS;
        return new String[] {
            // only first attribute (e.g. groupClass='a')
            createAttributeWithValue(className, value),
            // both attributes (e.g. groupClass='a' + topGroupClass='b')
            createAttributeWithValue(className, value) + createAttributeWithValue(cTopClass, topValue),
            // both attributes, second is empty (e.g. groupClass='a' + topGroupClass='')
            createAttributeWithValue(className, value) + createAttributeWithValue(cTopClass, emptyValue)
        };
    }

    private static String getCorrespondingTopClass(String className) {
        return TOP + upperCaseFirstChar(className);
    }

    private static String upperCaseFirstChar(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
