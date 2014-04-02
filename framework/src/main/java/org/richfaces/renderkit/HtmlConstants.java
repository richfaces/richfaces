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
package org.richfaces.renderkit;

/**
 * Common HTML elements and attributes names.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.6 $ $Date: 2007/02/08 19:07:16 $
 *
 */
public interface HtmlConstants {
    // elements
    String A_ELEMENT = "a";
    String BR_ELEMENT = "br";
    String BODY_ELEMENT = "body";
    String IMG_ELEMENT = "img";
    String INPUT_ELEM = "input";
    String OPTION_ELEM = "option";
    String INPUT_TYPE_HIDDEN = "hidden";
    String BUTTON = "button";
    String CAPTION_ELEMENT = "caption";
    String CHARSET_ATTR = "charset";
    String COORDS_ATTR = "coords";
    String COLGROUP_ELEMENT = "colgroup";
    String COL_ELEMENT = "col";
    String DISABLED_ATTR = "disabled";
    String DIV_ELEM = "div";
    String DD_ELEMENT = "dd";
    String DL_ELEMENT = "dl";
    String DT_ELEMENT = "dt";
    String FORM_ELEMENT = "form";
    String HEAD_ELEMENT = "head";
    String HEIGHT_ATTRIBUTE = "height";
    String HREFLANG_ATTR = "hreflang";
    String HREF_ATTR = "href";
    String HTML_ELEMENT = "html";
    String LINK_ELEMENT = "link";
    String SCRIPT_ELEM = "script";
    String SPAN_ELEM = "span";
    String TFOOT_ELEMENT = "tfoot";
    String THEAD_ELEMENT = "thead";
    String TABLE_ELEMENT = "table";
    String TBODY_ELEMENT = "tbody";
    String TD_ELEM = "td";
    String TR_ELEMENT = "tr";
    String TH_ELEM = "th";
    String TITLE_ELEM = "title";
    String UL_ELEMENT = "ul";
    String OL_ELEMENT = "ol";
    String LI_ELEMENT = "li";
    // attributes
    String FRAME_ATTRIBUTE = "frame";
    String BORDER_ATTRIBUTE = "border";
    String BGCOLOR_ATTRIBUTE = "bgcolor";
    String ACCEPT_ATTRIBUTE = "accept";
    String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
    String ACCESSKEY_ATTRIBUTE = "accesskey";
    String ACTION_ATTRIBUTE = "action";
    String ALIGN_ATTRIBUTE = "align";
    String ALT_ATTRIBUTE = "alt";
    String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
    String CLASS_ATTRIBUTE = "class";
    String COLS_ATTRIBUTE = "cols";
    String COLSPAN_ATTRIBUTE = "colspan";
    String CELLPADDING_ATTRIBUTE = "cellpadding";
    String CELLSPACING_ATTRIBUTE = "cellspacing";
    String DIR_ATTRIBUTE = "dir";
    String ENCTYPE_ATTRIBUTE = "enctype";
    String ID_ATTRIBUTE = "id";
    String LANG_ATTRIBUTE = "lang";
    String LONGDESC_ATTRIBUTE = "longdesc";
    String MAXLENGTH_ATTRIBUTE = "maxlength";
    String MEDIA_ATTRIBUTE = "media";
    String METHOD_ATTRIBUTE = "method";
    String NAME_ATTRIBUTE = "name";
    String NOWRAP_ATTRIBUTE = "nowrap";
    String ROWS_ATTRIBUTE = "rows";
    String RULES_ATTRIBUTE = "rules";
    String ROWSPAN_ATTRIBUTE = "rowspan";
    String READONLY_ATTRIBUTE = "readonly";
    String SIZE_ATTRIBUTE = "size";
    String SRC_ATTRIBUTE = "src";
    String STYLE_ATTRIBUTE = "style";
    String SUMMARY_ATTRIBUTE = "summary";
    String SCOPE_ATTRIBUTE = "scope";
    String TABINDEX_ATTRIBUTE = "tabindex";
    String TITLE_ATTRIBUTE = "title";
    String TARGET_ATTRIBUTE = "target";
    String TYPE_ATTR = "type";
    String USEMAP_ATTRIBUTE = "usemap";
    String VALIGN_ATTRIBUTE = "valign";
    String VALUE_ATTRIBUTE = "value";
    String WIDTH_ATTRIBUTE = "width";
    String ONBLUR_ATTRIBUTE = "onblur";
    String ONCHANGE_ATTRIBUTE = "onchange";
    String ONCLICK_ATTRIBUTE = "onclick";
    String ONDBLCLICK_ATTRIBUTE = "ondblclick";
    String ONFOCUS_ATTRIBUTE = "onfocus";
    String ONKEYDOWN_ATTRIBUTE = "onkeydown";
    String ONKEYPRESS_ATTRIBUTE = "onkeypress";
    String ONKEYUP_ATTRIBUTE = "onkeyup";
    String ONLOAD_ATTRIBUTE = "onload";
    String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
    String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
    String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
    String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
    String ONMOUSEUP_ATTRIBUTE = "onmouseup";
    String ONRESET_ATTRIBUTE = "onreset";
    String ONSELECT_ATTRIBUTE = "onselect";
    String ONSUBMIT_ATTRIBUTE = "onsubmit";
    String ONUNLOAD_ATTRIBUTE = "onunload";
    String REL_ATTR = "rel";
    String REV_ATTR = "rev";
    String SHAPE_ATTR = "shape";
    String STYLE_CLASS_ATTR = "styleClass";
    // String ONRESET_ATTRIBUTE = "onreset";
    // attributes sets.
    String[] PASS_THRU = {

            // DIR_ATTRIBUTE,
            // LANG_ATTRIBUTE,
            // STYLE_ATTRIBUTE,
            // TITLE_ATTRIBUTE
            "accesskey", "alt", "cols", "height", "lang", "longdesc", "maxlength", "rows", "size", "tabindex", "title",
            "width", "dir", "rules", "frame", "border", "cellspacing", "cellpadding", "summary", "bgcolor", "usemap",
            "enctype", "accept-charset", "accept", "target", "charset", "coords", "hreflang", "rel", "rev", "shape",
            "disabled", "readonly", "ismap", "align", "type" };
    /**
     * HTML attributes allowed boolean-values only
     */
    String[] PASS_THRU_BOOLEAN = { "disabled", "declare", "readonly", "compact", "ismap", "selected", "checked", "nowrap",
            "noresize", "nohref", "noshade", "multiple" };
    String[] PASS_THRU_EVENTS = { "onblur", "onchange", "onclick", "ondblclick", "onfocus", "onkeydown", "onkeypress",
            "onkeyup", "onload", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onselect",
            "onsubmit", "onunload" };
    String[] PASS_THRU_STYLES = { "style", "class", };
    /**
     * all HTML attributes with URI value.
     */
    String[] PASS_THRU_URI = { "usemap", "background", "codebase", "cite", "data", "classid", "href", "longdesc", "profile",
            "src" };
    String TEXT_JAVASCRIPT_TYPE = "text/javascript";
    String REL_STYLESHEET = "stylesheet";
    String CSS_TYPE = "text/css";
    String JAVASCRIPT_TYPE = "text/javascript";
}