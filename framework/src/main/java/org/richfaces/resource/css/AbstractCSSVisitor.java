/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;

/**
 * @author Nick Belaevski
 *
 */
public abstract class AbstractCSSVisitor {
    public void visitStyleSheet(CSSStyleSheet styleSheet) {
        startStyleSheet(styleSheet);

        CSSRuleList rules = styleSheet.getCssRules();
        int length = rules.getLength();
        for (int i = 0; i < length; i++) {
            CSSRule rule = rules.item(i);
            visitRule(rule);
        }

        endStyleSheet(styleSheet);
    }

    public void visitRule(CSSRule rule) {
        switch (rule.getType()) {
            case CSSRule.UNKNOWN_RULE:
                visitUnknownRule((CSSUnknownRule) rule);
                break;

            case CSSRule.STYLE_RULE:
                visitStyleRule((CSSStyleRule) rule);
                break;

            case CSSRule.CHARSET_RULE:
                visitCharsetRule((CSSCharsetRule) rule);
                break;

            case CSSRule.IMPORT_RULE:
                visitImportRule((CSSImportRule) rule);
                break;

            case CSSRule.MEDIA_RULE:
                visitMediaRule((CSSMediaRule) rule);
                break;

            case CSSRule.FONT_FACE_RULE:
                visitFontFaceRule((CSSFontFaceRule) rule);
                break;

            case CSSRule.PAGE_RULE:
                visitPageRule((CSSPageRule) rule);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    protected abstract void startStyleSheet(CSSStyleSheet styleSheet);

    protected abstract void endStyleSheet(CSSStyleSheet styleSheet);

    public abstract void visitUnknownRule(CSSUnknownRule rule);

    public void visitStyleRule(CSSStyleRule rule) {
        startStyleRule(rule);
        CSSStyleDeclaration style = rule.getStyle();
        visitStyleDeclaration(style);
        endStyleRule(rule);
    }

    protected abstract void startStyleRule(CSSStyleRule rule);

    protected abstract void endStyleRule(CSSStyleRule rule);

    public abstract void visitCharsetRule(CSSCharsetRule rule);

    public abstract void visitImportRule(CSSImportRule rule);

    public void visitMediaRule(CSSMediaRule rule) {
        startMediaRule(rule);

        CSSRuleList rules = rule.getCssRules();
        int length = rules.getLength();
        for (int i = 0; i < length; i++) {
            CSSRule childRule = rules.item(i);
            visitRule(childRule);
        }

        endMediaRule(rule);
    }

    protected abstract void startMediaRule(CSSMediaRule rule);

    protected abstract void endMediaRule(CSSMediaRule rule);

    public void visitFontFaceRule(CSSFontFaceRule rule) {
        startFontRule(rule);
        CSSStyleDeclaration style = rule.getStyle();
        visitStyleDeclaration(style);
        endFontRule(rule);
    }

    protected abstract void startFontRule(CSSFontFaceRule rule);

    protected abstract void endFontRule(CSSFontFaceRule rule);

    public void visitPageRule(CSSPageRule rule) {
        startPageRule(rule);
        CSSStyleDeclaration style = rule.getStyle();
        visitStyleDeclaration(style);
        endPageRule(rule);
    }

    protected abstract void startPageRule(CSSPageRule rule);

    protected abstract void endPageRule(CSSPageRule rule);

    public abstract void visitStyleDeclaration(CSSStyleDeclaration styleDeclaration);
}
