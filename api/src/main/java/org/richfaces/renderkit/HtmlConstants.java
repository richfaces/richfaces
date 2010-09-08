/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
    public static final String A_ELEMENT = "a";
    public static final String BODY_ELEMENT = "body";
    public static final String IMG_ELEMENT = "img";
    public static final String INPUT_ELEM = "input";
    public static final String INPUT_TYPE_HIDDEN = "hidden";
    public static final String BUTTON = "button";
    public static final String CAPTION_ELEMENT = "caption";
    public static final String CHARSET_ATTR = "charset";
    public static final String COORDS_ATTR = "coords";
    public static final String COLGROUP_ELEMENT = "colgroup";
    public static final String COL_ELEMENT = "col";
    public static final String DISABLED_ATTR = "disabled";
    public static final String DIV_ELEM = "div";
    public static final String DD_ELEMENT = "dd";
    public static final String DL_ELEMENT = "dl";
    public static final String DT_ELEMENT = "dt";
    public static final String FORM_ELEMENT = "form";
    public static final String HEAD_ELEMENT = "head";
    public static final String HEIGHT_ATTRIBUTE = "height";
    public static final String HREFLANG_ATTR = "hreflang";
    public static final String HREF_ATTR = "href";
    public static final String HTML_ELEMENT = "html";
    public static final String LINK_ELEMENT = "link";
    public static final String SCRIPT_ELEM = "script";
    public static final String SPAN_ELEM = "span";
    public static final String TFOOT_ELEMENT = "tfoot";
    public static final String THEAD_ELEMENT = "thead";
    public static final String TABLE_ELEMENT = "table";
    public static final String TBODY_ELEMENT = "tbody";
    public static final String TD_ELEM = "td";
    public static final String TR_ELEMENT = "tr";
    public static final String TH_ELEM = "th";
    public static final String TITLE_ELEM = "title";
    public static final String UL_ELEMENT = "ul";
    public static final String OL_ELEMENT = "ol";
    public static final String LI_ELEMENT = "li";
    
    // attributes
    public static final String FRAME_ATTRIBUTE = "frame";
    public static final String BORDER_ATTRIBUTE = "border";
    public static final String BGCOLOR_ATTRIBUTE = "bgcolor";
    public static final String ACCEPT_ATTRIBUTE = "accept";
    public static final String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
    public static final String ACCESSKEY_ATTRIBUTE = "accesskey";
    public static final String ACTION_ATTRIBUTE = "action";
    public static final String ALIGN_ATTRIBUTE = "align";
    public static final String ALT_ATTRIBUTE = "alt";
    public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String COLS_ATTRIBUTE = "cols";
    public static final String COLSPAN_ATTRIBUTE = "colspan";
    public static final String CELLPADDING_ATTRIBUTE = "cellpadding";
    public static final String CELLSPACING_ATTRIBUTE = "cellspacing";
    public static final String DIR_ATTRIBUTE = "dir";
    public static final String ENCTYPE_ATTRIBUTE = "enctype";
    
    public static final String ID_ATTRIBUTE = "id";
    public static final String LANG_ATTRIBUTE = "lang";
    public static final String LONGDESC_ATTRIBUTE = "longdesc";
    public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
    public static final String MEDIA_ATTRIBUTE = "media";
    public static final String METHOD_ATTRIBUTE = "method";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String NOWRAP_ATTRIBUTE = "nowrap";
    public static final String ROWS_ATTRIBUTE = "rows";
    public static final String RULES_ATTRIBUTE = "rules";
    public static final String ROWSPAN_ATTRIBUTE = "rowspan";
    public static final String READONLY_ATTRIBUTE = "readonly";
    public static final String SIZE_ATTRIBUTE = "size";
    public static final String SRC_ATTRIBUTE = "src";
    public static final String STYLE_ATTRIBUTE = "style";
    public static final String SUMMARY_ATTRIBUTE = "summary";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String TABINDEX_ATTRIBUTE = "tabindex";
    public static final String TITLE_ATTRIBUTE = "title";
    public static final String TARGET_ATTRIBUTE = "target";
    public static final String TYPE_ATTR = "type";
    
    public static final String USEMAP_ATTRIBUTE = "usemap";

    public static final String VALIGN_ATTRIBUTE = "valign";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String WIDTH_ATTRIBUTE = "width";


    public static final String ONBLUR_ATTRIBUTE = "onblur";
    public static final String ONCHANGE_ATTRIBUTE = "onchange";
    public static final String ONCLICK_ATTRIBUTE = "onclick";
    public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
    public static final String ONFOCUS_ATTRIBUTE = "onfocus";
    public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";
    public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
    public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
    public static final String ONLOAD_ATTRIBUTE = "onload";
    public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
    public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
    public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
    public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
    public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
    public static final String ONRESET_ATTRIBUTE = "onreset";
    public static final String ONSELECT_ATTRIBUTE = "onselect";
    public static final String ONSUBMIT_ATTRIBUTE = "onsubmit";
    public static final String ONUNLOAD_ATTRIBUTE = "onunload";
    
    public static final String REL_ATTR = "rel";
    public static final String REV_ATTR = "rev";
    public static final String SHAPE_ATTR = "shape";
    public static final String STYLE_CLASS_ATTR = "styleClass";



    // public static final String ONRESET_ATTRIBUTE = "onreset";
    // attributes sets.
    public static final String[] PASS_THRU = {

        // DIR_ATTRIBUTE,
        // LANG_ATTRIBUTE,
        // STYLE_ATTRIBUTE,
        // TITLE_ATTRIBUTE
        "accesskey", "alt", "cols", "height", "lang", "longdesc", "maxlength", "rows", "size", "tabindex", "title",
        "width", "dir", "rules", "frame", "border", "cellspacing", "cellpadding", "summary", "bgcolor", "usemap",
        "enctype", "accept-charset", "accept", "target", "charset", "coords", "hreflang", "rel", "rev", "shape",
        "disabled", "readonly", "ismap", "align"
    };

    /**
     * HTML attributes allowed boolean-values only
     */
    public static final String[] PASS_THRU_BOOLEAN = {
        "disabled", "declare", "readonly", "compact", "ismap", "selected", "checked", "nowrap", "noresize",
        "nohref", "noshade", "multiple"
    };
    public static final String[] PASS_THRU_EVENTS = {
        "onblur", "onchange", "onclick", "ondblclick", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onload",
        "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onselect", "onsubmit",
        "onunload"
    };
    public static final String[] PASS_THRU_STYLES = {"style", "class", };

    /**
     * all HTML attributes with URI value.
     */
    public static final String[] PASS_THRU_URI = {
        "usemap", "background", "codebase", "cite", "data", "classid", "href", "longdesc", "profile", "src"
    };


    public static final String TEXT_JAVASCRIPT_TYPE = "text/javascript";
    public static final String REL_STYLESHEET = "stylesheet";
    public static final String CSS_TYPE = "text/css";
    public static final String JAVASCRIPT_TYPE = "text/javascript";
}