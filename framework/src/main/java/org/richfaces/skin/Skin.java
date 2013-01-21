/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.skin;

import javax.faces.context.FacesContext;

/**
 * Main interface for configurable parameters.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:43 $
 *
 */
public interface Skin {
    /**
     * Style of the text displaying on the active (selected) tabs.Default value normal.
     */
    String ACTIVETAB_STYLE_FONT = "activetabStyleFont";
    /**
     * Depth of the text displaying on the active (selected) tabs.Default value bold.
     */
    String ACTIVETAB_WEIGHT_FONT = "activetabWeightFont";
    String ADDITIONAL_BACKGROUND_COLOR = "additionalBackgroundColor";
    String BASE_SKIN = "baseSkin";
    /**
     * Font name for displaying buttons titles. Default value Arial, Verdana.
     */
    String BUTTON_FAMILY_FONT = "buttonFamilyFont";
    /**
     * Rounding-off radius of the buttons corners. Default value 13px.
     *
     */
    String BUTTON_RADIUS_CORNER = "buttonRadiusCorner";
    /**
     * Font size for displaying buttons titles. Default value 11px.
     */
    String BUTTON_SIZE_FONT = "buttonSizeFont";
    /**
     * Style of the text displaying on the button. Default value normal.
     *
     */
    String BUTTON_STYLE_FONT = "buttonStyleFont";
    /**
     * Depth of the text displaying on the button. Default value bold.
     */
    String BUTTON_WEIGHT_FONT = "buttonWeightFont";
    String CONTROL_BACKGROUND_COLOR = "controlBackgroundColor";
    /**
     * Responsible for the borders color of the radiobutton and checkbox and color of light part of the fields and text area
     * border. Default value #B0B0B0.
     */
    String CONTROL_BORDER_COLOR = "controlBorderColor";
    /**
     * Style of the text displaying on the disabled tabs.Default value normal.
     */
    String DISABLED_TAB_STYLE_FONT = "disabledTabStyleFont";
    /**
     * Depth of the text displaying on the disabled tabs.Default value normal.
     */
    String DISABLED_TAB_WEIGHT_FONT = "disabledTabWeightFont";
    /**
     * used for define url with extended CSS file for current skin.
     */
    String EXTENDED_STYLE_SHEET = "extendedStyleSheet";
    /**
     * used for defining the background color of the basic area of the panel and tabpanels, background color of the dropdown
     * list boxes of ddmenu. Default value #BFD7E4.
     */
    String GENERAL_BACKGROUND_COLOR = "generalBackgroundColor";
    /**
     *
     */
    String GENERAL_LINK_COLOR = "generalLinkColor";
    String GENERAL_SIZE_FONT = "generalSizeFont";

    // parameters names constants
    // parameters names constants
    /**
     * used for define url with general CSS file for current skin.
     */
    String GENERAL_STYLE_SHEET = "generalStyleSheet";
    /**
     * used for defining color of the basic text, color of the text in the dropdown list boxes of ddmenu, color of the right and
     * top borders for the controls like text, textArea, secret. Default value #000000.
     */
    String GENERAL_TEXT_COLOR = "generalTextColor";
    String GRADIENT_TYPE = "gradientType";
    /**
     * used for defining a header background color in the panel, a tabpanel active tab background color, a bar background color
     * for ddmenu, tables background color, buttons background color. Default value #1D7DB3.
     */
    String HEADER_BACKGROUND_COLOR = "headerBackgroundColor";
    /**
     * Font name for the displaying panels headers and top level of the ddmenu.Default value Arial, Verdana.
     */
    String HEADER_FAMILY_FONT = "headerFamilyFont";
    String HEADER_GRADIENT_COLOR = "headerGradientColor";
    /**
     * Font size for the displaying panels headers and top level of the ddmenu. Default value 12px.
     */
    String HEADER_SIZE_FONT = "headerSizeFont";
    /**
     * used for defining color of the titles of the top level of the menu, the panel header color, the table header color, color
     * of the title of the active tab in the tabpanel, color of the title on the buttons. Default value #FFFFFF.
     */
    String HEADER_TEXT_COLOR = "headerTextColor";
    /**
     * Parameter responsible for 3D Look of panels and buttons.
     */
    String INTERFACE_LEVEL_3D = "interfaceLevel3D";
    String LOAD_STYLE_SHEETS = "loadStyleSheets";
    /**
     *
     */
    String OVER_ALL_BACKGROUND = "overAllBackground";
    /**
     * Rounding-off radius of the panels corners. Default value 5px.
     */
    String PANEL_RADIUS_CORNER = "panelRadiusCorner";
    String PANEL_TEXT_COLOR = "panelTextColor";
    /**
     * defines the layout of the tabs in the panel. Possible values Top. Left. Bottom, Right. Default value ???.
     */
    String PREFERABLE_TAB_POSITION = "preferableTabPosition";
    /**
     * text aligning in the tabs with the fixed tab length (hight). Values Left, Center,Right for the horizontal oriented tabs,
     * values Top, center, Bottom - for vertical oriented tabs. Default value center.
     */
    String PREFERABLE_TAB_TEXT_DIRECTION = "preferableTabTextDirection";
    /**
     * defines the variant of text writing. Values - Hor (horisontal position), Vert (vertical position - letters are arranged
     * into column), VertCW (vertical position with text rotation anticlockwise). Default value Hor.
     */
    String PREFERABLE_TAB_TEXT_ORIENTATION = "preferableTabTextOrientation";
    /**
     * used for defining the background color of the bar under the header in the panel and the tabpanel, color of the border for
     * the top level of the ddmenu, background highlight color of the selected item in the dropdown list boxes of the ddmenu,
     * background color of the footer in the tables, color of the text for the inactive and disabled tab in the tabpanel, color
     * of the border of the buttons. Default value #BFD7E4.
     */
    String SELECT_BACKGROUND_COLOR = "selectBackgroundColor";
    /**
     * Color for selected checkbox or selectOneRadio.
     */
    String SELECT_CONTROL_COLOR = "selectControlColor";
    /**
     * used for defining shadow background color of the panels, color of the bottom and right borders for the dropdown list box
     * in ddmenu. Default value #AFB1B2.
     */
    String SHADOW_BACKGROUND_COLOR = "shadowBackgroundColor";
    /**
     * used for defining the seamlessness of the tip shadow. Default value 2.
     */
    String SHADOW_OPACITY = "shadowOpacity";
    /**
     * used for defining tables and controls background color. Default value #FFFFFF.
     */
    String TABLE_BACKGROUND_COLOR = "tableBackgroundColor";
    /**
     * used for defining tables border color, color of the bottom and left controls like text, textArea, secret, color of the
     * top and left borders for the dropdown list box in the ddmenu. Default value #CCCCCC.
     */
    String TABLE_BORDER_COLOR = "tableBorderColor";
    /**
     * Font name for displaying tab titles. Default value Arial, Verdana.
     *
     */
    String TAB_FAMILY_FONT = "tabFamilyFont";
    /**
     * Rounding-off radius of the tabs corners. Default value 5px.
     */
    String TAB_RADIUS_CORNER = "tabRadiusCorner";
    /**
     * Font size for displaying tab titles. Default value 11px.
     */
    String TAB_SIZE_FONT = "tabSizeFont";
    /**
     * Style of of the text displaying on the inactive (unselected) tabs. Default value normal.
     */
    String TAB_STYLE_FONT = "tabStyleFont";
    /**
     * Depth of the text displaying on the inactive (unselected) tabs.Default value normal.
     */
    String TAB_WEIGHT_FONT = "tabWeightFont";
    String TAB_BACKGROUND_COLOR = "tabBackgroundColor";
    String TRIM_COLOR = "trimColor";

    /**
     * Get value for configuration parameter. If parameter set as EL-expression, calculate it value.
     *
     * @param context - {@link FacesContext } for current request.
     * @param name name of parameter.
     * @return value of parameter in config, or null
     */
    Object getParameter(FacesContext context, String name);

    /**
     * Get value for configuration parameter. If parameter set as EL-expression, calculate it value.
     *
     * @param context - {@link FacesContext } for current request.
     * @param name name of paremeter.
     * @param defaultValue - default value if parameter not present in Skin
     * @return value of parameter in config, or null
     */
    Object getParameter(FacesContext context, String name, Object defaultValue);

    /**
     * Get value for configuration parameter & interpret it as color string. If parameter set as EL-expression, calculate it
     * value.
     *
     * @param context - {@link FacesContext } for current request.
     * @param name name of parameter.
     * @return value of parameter in config, or null
     * @since 4.0.M1
     */
    Integer getColorParameter(FacesContext context, String name);

    /**
     * Get value for configuration parameter & interpret it as color string. If parameter set as EL-expression, calculate it
     * value.
     *
     * @param context - {@link FacesContext } for current request.
     * @param name name of parameter.
     * @param defaultValue - default value if parameter not present in Skin
     * @return
     * @since 4.0.M1
     */
    Integer getColorParameter(FacesContext context, String name, Object defaultValue);

    Integer getIntegerParameter(FacesContext context, String name);

    Integer getIntegerParameter(FacesContext context, String name, Object defaultValue);

    /**
     * @param name
     * @return
     */
    boolean containsProperty(String name);

    /**
     * Calculate unique ( as possible ) code to identity this skin instance. Used for generate hash key in skin-depended
     * resources
     *
     * @param context
     * @return
     */
    int hashCode(FacesContext context);

    String getName();

    // Preferable parameters

    /**
     * Preferable parameters names for skin ( in common, for Preferable.Name parameter will PreferableName )
     *
     * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
     * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:43 $
     *
     */
    public interface Preferable {
        /**
         *
         */
        String DATA_FAMILY_FONT = "preferableDataFamilyFont";
        /**
         *
         */
        String DATA_SIZE_FONT = "preferableDataSizeFont";
        /**
         *
         */
        String HEADER_WEIGHT_FONT = "preferableHeaderWeightFont";
        /**
         *
         */
        String PANEL_BODY_PADDING = "preferablePanelBodyPadding";
    }
}
