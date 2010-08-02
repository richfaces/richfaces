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

package org.ajax4jsf.application;

import org.ajax4jsf.Messages;

import javax.el.Expression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:11 $
 */
public class DebugOutputMaker {
    // Attributes that should not be printed
    public static final Set<String> IGNORE_ATTRIBUTES;

    private static final String GT = "&gt;";
    private static final String LOGFILE_PARAM = "org.ajax4jsf.LOGFILE";

    private static final String LT = "&lt;";

    static {
        IGNORE_ATTRIBUTES = new HashSet<String>();
        IGNORE_ATTRIBUTES.add("attributes");
        IGNORE_ATTRIBUTES.add("children");
        IGNORE_ATTRIBUTES.add("childCount");
        IGNORE_ATTRIBUTES.add("class");
        IGNORE_ATTRIBUTES.add("facets");
        IGNORE_ATTRIBUTES.add("facetsAndChildren");
        IGNORE_ATTRIBUTES.add("parent");
        IGNORE_ATTRIBUTES.add("actionListeners");
        IGNORE_ATTRIBUTES.add("valueChangeListeners");
        IGNORE_ATTRIBUTES.add("validators");
        IGNORE_ATTRIBUTES.add("rowData");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.COMPONENT_IDS");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.FACET_NAMES");
        IGNORE_ATTRIBUTES.add("javax.faces.webapp.CURRENT_VIEW_ROOT");
    }

    /**
     * Output error state for lifecycle.
     *
     * @param context
     * @param phase   TODO
     * @param error
     * @throws IOException
     */
    public void writeErrorMessage(FacesContext context, Throwable e, String phase) throws FacesException {
        if (null == context) {
            throw new FacesException(Messages.getMessage(Messages.FACES_CONTEXT_NOT_CREATED), e);
        }

        ExternalContext externalContext = context.getExternalContext();

        if (null == externalContext) {
            throw new FacesException(Messages.getMessage(Messages.FACES_CONTEXT_HAS_NOT_EXTERNAL), e);
        }

        // debug for http servlet environment, portlets needs other debugger
        HttpServletResponse response;
        HttpServletRequest request;

        try {
            response = (HttpServletResponse) externalContext.getResponse();
            request = (HttpServletRequest) externalContext.getRequest();
            response.setContentType("text/html;charset=UTF-8");

            // set internal server error status
            response.setStatus(500);
        } catch (Exception er) {
            throw new FacesException(Messages.getMessage(Messages.FACES_CONTEXT_HAS_NOT_RESPONSE), e);
        }

        PrintWriter out;

        try {
            out = response.getWriter();
        } catch (IOException e1) {
            throw new FacesException(e1.getMessage(), e);
        }

        UIViewRoot viewRoot = context.getViewRoot();
        String viewId;

        if (null != viewRoot) {
            viewId = viewRoot.getViewId();
        } else {
            viewId = request.getRequestURL().toString();
        }

        // output html prolog
        out.println("<html><head><title>" + Messages.getMessage(Messages.ERROR_ON_PAGE, viewId)
            + "</title></head><body>");

        // write script
        writeScriptAndStyle(out);

        // Header
        PhaseId facesPhase = (PhaseId) context.getExternalContext().getRequestMap().get(DebugLifecycle.PHASE_ID_PARAM);
        String errorMessage = (facesPhase == null) ? Messages.getMessage(Messages.LIFECYCLE_ERROR, viewId, phase)
            : Messages.getMessage(Messages.LIFECYCLE_ERROR_AT_PHASE,
                new Object[]{viewId, phase, facesPhase.toString()});

        out.println("<h1 >");
        out.println(errorMessage);
        out.println("</h1>");
        response.setHeader("Ajax-Error-Message",
            errorMessage + ",\n caused by " + e.getClass().getName() + ", with message: "
                + e.getMessage());

        // Output exceptions
        writeExceptionStack(e, out);

        // Output view tree :
        if (null != viewRoot) {
            writeComponentsTree(context, out);
        } else {
            out.print("<h2 class=' a4j_debug'> " + Messages.getMessage(Messages.COMPONENT_TREE_NOT_CREATED) + " </h2>");
        }

        // Out scope variables
        writeContextVariables(context, out);

        // Write log output iframe :
        writeLog(context, out);

        // out html tail
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    /**
     * @param e
     * @param out
     */
    public void writeExceptionStack(Throwable e, PrintWriter out) {
        out.println("<h2 class=\"a4j_debug\">Exceptions: </h2>");

        Throwable error = e;
        int errorId = 0;
        String caused = "exception ";

        while (null != error) {
            out.print("<h3 onclick=\"toggle('exception" + errorId + "')\"  class='exception a4j_debug'>");
            writeToggleMark(out, "exception" + errorId);
            out.print(caused + error.getClass().getName() + " : " + error.getMessage() + "</h3>");
            out.println("<div id='exception" + errorId
                + "' style='display: none;' class='exception'><p>Exception stack :</p><pre>");

            StackTraceElement[] stackTrace = error.getStackTrace();

            for (int i = 0; i < stackTrace.length; i++) {
                out.print("  at " + stackTrace[i].getClassName());
                out.print("." + stackTrace[i].getMethodName());
                out.println(" in " + stackTrace[i].getFileName() + " line " + stackTrace[i].getLineNumber());
            }

            // error.printStackTrace(out);
            out.println("</pre></div>");
            error = error.getCause();
            caused = "caused by ";
            errorId++;
        }
    }

    /**
     * @param context
     * @param out
     */
    public void writeContextVariables(FacesContext context, PrintWriter out) {
        out.print("<h2  class=\"a4j_debug\" onclick=\"toggle('variables')\">");
        writeToggleMark(out, "variables");
        out.println("Faces variables: </h2><div id='variables' style='display: none;'  class='variables a4j_debug'>");
        writeVariables(out, context);
        out.println("</div>");
    }

    /**
     * @param context
     * @param out
     */
    public void writeComponentsTree(FacesContext context, PrintWriter out) {
        out.print("<h2 class=\"a4j_debug\" onclick=\"toggle('tree')\">");
        writeToggleMark(out, "tree");
        out.println("Component tree: </h2><div id='tree' style='display: none;'  class='tree a4j_debug'><dl>");
        writeComponent(context, out, context.getViewRoot(), null);
        out.println("</dl></div>");
    }

    public void writeScriptAndStyle(PrintWriter out) {
        writeScript(out);
        writeStyleSheet(out);
    }

    /**
     * @param context
     * @param out
     * @throws FacesException
     */
    public void writeLog(FacesContext context, PrintWriter out) throws FacesException {
        String logname = context.getExternalContext().getInitParameter(LOGFILE_PARAM);

        if (null != logname) {
            Resource logResource = context.getApplication().getResourceHandler().createResource(logname);

            out.print("<h2 onclick=\"toggle('log')\" class=\"a4j_debug\">");
            writeToggleMark(out, "log");
            out.println("Faces log: </h2><div id='log' style='display: none;'  class='log a4j_debug'>");

            String logResourceRequestPath = logResource.getRequestPath();

            out.print("<iframe name='log' class='log' src='" + logResourceRequestPath + "'><a href='src='"
                + logResourceRequestPath + "'>Faces log file </a> </iframe>");
            out.println("</div>");
        }
    }

    public void writeToggleMark(PrintWriter out, String id) {
        out.print("<span style=\"display:none;\" id=\"" + id + "_expanded\" >-&nbsp;</span>");
        out.print("<span style=\"display:inline;\" id=\"" + id + "_collapsed\" >+</span>&nbsp;");
    }

    /**
     * Out component with properties and it's facets and childrens
     *
     * @param context  TODO
     * @param out
     * @param viewRoot
     */
    private void writeComponent(FacesContext context, PrintWriter out, UIComponent component, String facetName) {
        String clientId = "_tree:" + component.getClientId(context);

        out.println("<dt onclick=\"toggle('" + clientId + "')\" class='tree'>");
        writeToggleMark(out, clientId);

        // out component name
        if (null != facetName) {
            out.print("Facet:'" + facetName + "' ");
        }

        out.println("<code>" + component.getClass().getName() + "</code> Id:[" + component.getId() + "]");
        out.println("</dt>");
        out.println("<dd id='" + clientId + "' style='display:none;'   class='tree' ><ul   class='tree'>");

        // out bean properties
        try {
            Map<String, Object> attributes = component.getAttributes();
            List<String> attributeNamesList = new ArrayList<String>(attributes.keySet());

            Collections.sort(attributeNamesList);

            for (String attributeName : attributeNamesList) {
                ValueExpression ve = component.getValueExpression(attributeName);

                if (ve != null) {
                    writeAttribute(out, attributeName, ve.getExpressionString());
                } else {
                    if (!IGNORE_ATTRIBUTES.contains(attributeName)) {
                        try {
                            Object value = attributes.get(attributeName);

                            writeAttribute(out, attributeName, value);
                        } catch (Exception e) {
                            writeAttribute(out, attributeName, null);
                        }
                    }
                }
            }
        } catch (Exception e) {

            // Do nothing - we in error page.
        }

        // out bindings
        // out attributes map
        for (Iterator<Entry<String, Object>> it = component.getAttributes().entrySet().iterator(); it.hasNext();) {
            Entry<String, Object> entry = it.next();

            writeAttribute(out, entry.getKey(), entry.getValue());
        }

        // out listeners
        out.println("</ul></dd>");

        if (component.getFacetCount() > 0 || component.getChildCount() > 0) {
            out.println("<dd class='tree_childs'><dl class='tree_childs'>");

            // out childs of this component
            // facets
            Map<String, UIComponent> facetsMap = component.getFacets();
            List<String> facetNamesList = new ArrayList<String>(facetsMap.keySet());

            Collections.sort(facetNamesList);

            for (String nextFacetName : facetNamesList) {
                writeComponent(context, out, facetsMap.get(nextFacetName), nextFacetName);
            }

            // childs components
            for (Iterator<UIComponent> childIter = component.getChildren().iterator(); childIter.hasNext();) {
                UIComponent child = childIter.next();

                writeComponent(context, out, child, null);
            }

            out.println("</dl></dd>");
        }
    }

    private void writeAttribute(PrintWriter out, String name, Object value) {
        if (IGNORE_ATTRIBUTES.contains(name)) {
            return;
        }

        if (name.startsWith("javax.faces.webapp.UIComponentTag.")) {
            name = name.substring("javax.faces.webapp.UIComponentTag.".length());
        }

        out.print("<li>");
        out.print(name);
        out.print("=\"");

        if (value != null) {
            if (value instanceof UIComponent) {
                out.print("[id:");
                out.print(((UIComponent) value).getId());
                out.print(']');
            } else if (value instanceof Expression) {
                out.print(((Expression) value).getExpressionString());
            } else {
                out.print(value.toString());
            }
        } else {
            out.print("NULL");
        }

        out.println("\"</li>");
    }

    private void writeVariables(PrintWriter out, FacesContext faces) {
        ExternalContext ctx = faces.getExternalContext();

        writeVariables(out, ctx.getRequestParameterMap(), "Request Parameters");
        writeVariables(out, ctx.getRequestMap(), "Request Attributes");

        if (ctx.getSession(false) != null) {
            writeVariables(out, ctx.getSessionMap(), "Session Attributes");
        }

        writeVariables(out, ctx.getApplicationMap(), "Application Attributes");
    }

    private <K, V> void writeVariables(PrintWriter out, Map<K, V> vars, String caption) {
        out.print("<table><caption>");
        out.print(caption);
        out.println("</caption><thead><tr><th style=\"width: 10%; \">Name</th><th style=\"width: 90%; \">"
            + "Value</th></tr></thead><tbody>");

        boolean written = false;

        if (!vars.isEmpty()) {
            SortedMap<K, V> map = new TreeMap<K, V>(vars);
            Map.Entry<K, V> entry = null;
            String key = null;

            for (Iterator<Entry<K, V>> itr = map.entrySet().iterator(); itr.hasNext();) {
                entry = itr.next();
                key = entry.getKey().toString();

                if (key.indexOf('.') == -1) {
                    out.println("<tr><td>");
                    out.println(key.replaceAll("<", LT).replaceAll(">", GT));
                    out.println("</td><td><span class='value'>");

                    Object value = entry.getValue();

                    out.println(value.toString().replaceAll("<", LT).replaceAll(">", GT));
                    out.println("</span>");

                    // TODO - commented after beanutils dependency removed - review
//                  try {
//                        PropertyDescriptor propertyDescriptors[] = PropertyUtils.getPropertyDescriptors(value);
//                        if (propertyDescriptors.length>0) {
//                            out.print("<div class='properties'><ul class=\'properties\'>");
//                            for (int i = 0; i < propertyDescriptors.length; i++) {
//                                String beanPropertyName = propertyDescriptors[i].getName(); 
//                                if (PropertyUtils.isReadable(value,beanPropertyName
//                                        )) {
//                                    out.print("<li class=\'properties\'>");
//                                    out.print(beanPropertyName+" = "+BeanUtils.getProperty(value,beanPropertyName));
//                                    out.print("</li>");
//
//                                }
//                            }
//                            out.print("</ul></div>");
//                        }
//                    } catch (Exception e) {
//                        // TODO: log exception
//                    }
                    out.println("</td></tr>");
                    written = true;
                }
            }
        }

        if (!written) {
            out.println("<tr><td colspan=\"2\"><em>None</em></td></tr>");
        }

        out.println("</tbody></table>");
    }

    /**
     * @param out
     */
    private void writeScript(PrintWriter out) {
        out.println("<script type='text/javascript' language='javascript'>\n" + "function toggle(id) {\n"
            + "var style = document.getElementById(id).style;\n" + "if ('block' == style.display) {\n"
            + "style.display = 'none';\n"
            + "document.getElementById(id+'_collapsed').style.display = 'inline';\n"
            + "document.getElementById(id+'_expanded').style.display = 'none';\n" + "} else {\n"
            + "style.display = 'block';\n"
            + "document.getElementById(id+'_collapsed').style.display = 'none';\n"
            + "document.getElementById(id+'_expanded').style.display = 'inline';\n" + "}\n" + "}\n\n"
            + "</script>");
    }

    /**
     * @param out
     */
    private void writeStyleSheet(PrintWriter out) {
        out.println(
            "<style type=\'text/css\' >\n"
                + "div.a4j_debug, .a4j_debug span, .a4j_debug td, .a4j_debug th, .a4j_debug caption { font-family: "
                + "Verdana, Arial, Sans-Serif; }\n" + ".a4j_debug li{\n list-style-position : inside;\n}\n\n"
                + ".a4j_debug li, .a4j_debug pre { padding: 0; margin: 0;  font-size : 12px;}\n"
                + ".a4j_debug ul { padding: 0 0 10 0; margin: 0;  font-size : 12px;}\n"
                + ".a4j_debug h1 { color: #fff; background-color :#a00; font-size :17px; padding :7px 10px 10px 10px}\n"
                + "h2.a4j_debug , h2.a4j_debug  span { color: #a00;  font-size : 17px; padding : 0px 0px 0px 10px;}\n"
                + "h2.a4j_debug  a { text-decoration: none; color: #a00; }\n"
                + ".exception { color: #000;  font-size : 14px; padding : 0px 0px 0px 10px;}\n"
                + ".grayBox { padding: 8px; margin: 10px 0; border: 1px solid #CCC; background-color:#f9f9f9; "
                + "font-size:12px;}\n" + "#error { color: #900; font-weight: bold; font-size: medium; }\n"
                + "#trace, #tree, #vars { display: none; }\n"
                + ".a4j_debug code { font-size: medium;   font-size : 14px;}\n" + "#tree dl { color: #666; }\n"
                + "#tree dd {   font-size : 12px;}\n"
                + "#tree dt { border: 1px solid #DDD; padding: 2px 4px 4px 4px; border-left: 2px solid #a00; "
                + "font-family: \"Courier New\", Courier, mono; font-size: small;   font-size : 12px; margin-top: 2px; "
                + "margin-bottom: 2px;}\n" + ".uicText { color: #999;  }\n"
                + ".a4j_debug table { border: 1px solid #CCC; border-collapse: collapse; border-spacing: 0px; width: "
                + "100%; text-align: left; }\n"
                + ".a4j_debug td { border: 1px solid #CCC;    font-size : 12px; vertical-align : top}\n"
                + ".a4j_debug thead tr th { padding: 2px; color: #030; background-color: #F9F9F9; font-size : 12px;}\n"
                + ".a4j_debug tbody tr td { padding: 10px 6px; }\n"
                + ".a4j_debug table caption { text-align:left; padding: 20 0 5 0; font-size:12px; font-weight:bold;}\n"
                + ".value {font-size : 12px; font-weight : bold;}\n" + "div.log { width: 100%; height: 400px;}\n"
                + "iframe.log { width: 99%; height: 99%; border: 1px solid #CCC;}\n" + "</style>");
    }
}
