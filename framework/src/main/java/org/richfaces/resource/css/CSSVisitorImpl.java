/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource.css;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.el.util.ELUtils;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;
import org.w3c.dom.stylesheets.MediaList;

/**
 * @author Nick Belaevski
 *
 */
public final class CSSVisitorImpl extends AbstractCSSVisitor {
    private static final String RESOURCE_START_PREFIX = "resource[";
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final String NEW_LINE = "\r\n";
    private FacesContext facesContext;
    private String encoding;
    private StringBuilder buffer = new StringBuilder();
    private List<String> prefixes = new ArrayList<String>(2);

    public CSSVisitorImpl(FacesContext facesContext) {
        super();
        this.facesContext = facesContext;
    }

    private void appendCSSText(CSSRule rule) {
        String cssText = rule.getCssText().trim();

        // TODO nick - escape values
        if (cssText.length() != 0) {
            buffer.append(cssText);
            buffer.append(NEW_LINE);
        }
    }

    /*
     * private String escape(String cssText) { cssText = cssText.replaceAll("\\\\", "\\\\\\\\"); cssText =
     * cssText.replaceAll("\"", "\\\\\""); cssText = cssText.replaceAll("'", "\\\\'"); return cssText; }
     */

    private void flushPrefixes() {
        if (!prefixes.isEmpty()) {
            for (String prefix : prefixes) {
                buffer.append(prefix);
                buffer.append(" {");
                buffer.append(NEW_LINE);
            }

            prefixes.clear();
        }
    }

    private void flushSuffix() {
        if (prefixes.isEmpty()) {
            buffer.append('}');
            buffer.append(NEW_LINE);
        } else {
            prefixes.remove(prefixes.size() - 1);
        }
    }

    @Override
    public void visitUnknownRule(CSSUnknownRule rule) {
        appendCSSText(rule);
    }

    @Override
    public void visitCharsetRule(CSSCharsetRule rule) {
        encoding = rule.getEncoding();

        appendCSSText(rule);
    }

    @Override
    public void visitImportRule(CSSImportRule rule) {
        // TODO nick - process imported stylesheet?
        String resourceName = rule.getHref();
        if (ELUtils.isValueReference(resourceName)) {
            if (resourceName.indexOf(RESOURCE_START_PREFIX) == -1) {
                resourceName = facesContext.getApplication().evaluateExpressionGet(facesContext, resourceName, String.class);
            } else {
                int start = resourceName.indexOf(RESOURCE_START_PREFIX) + RESOURCE_START_PREFIX.length();
                int end = resourceName.lastIndexOf("]");
                resourceName = resourceName.substring(start, end);
                resourceName = resourceName.replaceAll("\"", "").replaceAll("'", "").trim();
            }
        }
        Resource imported = facesContext.getApplication().getResourceHandler().createResource(resourceName);
        if (imported == null) {
            LOGGER.error("Resource with name " + resourceName + "can't be found.");
            return;
        }
        String toAdd = null;
        try {
            toAdd = convertStreamToString(imported.getInputStream(), this.getEncoding());
        } catch (IOException e) {
            LOGGER.error("Error while importing nested resource with name " + resourceName);
        }
        if (toAdd != null && toAdd.length() > 0) {
            buffer.append(toAdd);
            buffer.append(NEW_LINE);
        } else {
            appendCSSText(rule);
        }
    }

    @Override
    protected void startFontRule(CSSFontFaceRule rule) {
        prefixes.add("@font-face");
    }

    @Override
    protected void endFontRule(CSSFontFaceRule rule) {
        flushSuffix();
    }

    @Override
    protected void startMediaRule(CSSMediaRule rule) {
        MediaList mediaList = rule.getMedia();
        // String mediaText = escape(mediaList.getMediaText());
        String mediaText = mediaList.getMediaText();
        prefixes.add("@media " + mediaText);
    }

    @Override
    protected void endMediaRule(CSSMediaRule rule) {
        flushSuffix();
    }

    @Override
    protected void startPageRule(CSSPageRule rule) {
        // String selectorText = escape(rule.getSelectorText());
        String selectorText = rule.getSelectorText();
        // TODO nick - multiple selectors?
        prefixes.add("@page " + selectorText);
    }

    @Override
    protected void endPageRule(CSSPageRule rule) {
        flushSuffix();
    }

    @Override
    protected void startStyleRule(CSSStyleRule rule) {
        // String selectorText = escape(rule.getSelectorText());
        String selectorText = rule.getSelectorText();
        prefixes.add(selectorText);
    }

    @Override
    protected void endStyleRule(CSSStyleRule rule) {
        flushSuffix();
    }

    @Override
    protected void startStyleSheet(CSSStyleSheet styleSheet) {
    }

    @Override
    protected void endStyleSheet(CSSStyleSheet styleSheet) {
    }

    @Override
    public void visitStyleDeclaration(CSSStyleDeclaration styleDeclaration) {
        for (int j = 0; j < styleDeclaration.getLength(); j++) {
            String propertyName = styleDeclaration.item(j);

            String value = styleDeclaration.getPropertyValue(propertyName).trim();
            String priority = styleDeclaration.getPropertyPriority(propertyName);
            if (ELUtils.isValueReference(value)) {
                value = facesContext.getApplication().evaluateExpressionGet(facesContext, value, String.class);
            }
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            if (value.startsWith("'") && value.endsWith("'")) {
                value = value.substring(1, value.length() - 1);
            }
            if (value.length() != 0 && !value.equals("\"\"") && !value.equals("''")) {
                flushPrefixes();

                // One of properties of selector is not empty
                buffer.append('\t');
                // buffer.append(escape(propertyName));
                buffer.append(propertyName);
                buffer.append(": ");

                // buffer.append(escape(value));
                buffer.append(value);

                if (priority != null && priority.length() != 0) {
                    buffer.append(" !");
                    buffer.append(priority);
                }

                buffer.append(";");
                buffer.append(NEW_LINE);
            }
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public String getCSSText() {
        return buffer.toString();
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String convertStreamToString(InputStream is, String encoding) throws IOException {

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(NEW_LINE);
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}