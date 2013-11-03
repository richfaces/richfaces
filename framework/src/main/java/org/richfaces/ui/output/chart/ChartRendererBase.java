package org.richfaces.ui.output.chart;

import static java.util.Arrays.asList;
import static org.richfaces.renderkit.RenderKitUtils.addToScriptHash;
import static org.richfaces.renderkit.RenderKitUtils.attributes;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.output.chart.ChartDataModel.ChartType;


/**
 * @author Lukas Macko
 */
public abstract class ChartRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.ui.output.ChartRenderer";

    private static final String X_VALUE = "x";
    private static final String Y_VALUE = "y";
    private static final String POINT_INDEX = "dataIndex";
    private static final String SERIES_INDEX = "seriesIndex";
    private static final String EVENT_TYPE = "name";
    private static final String PLOT_CLICK_TYPE = "plotclick";

    /**
     * Method adds key-value pair to object.
     * @param obj
     * @param key
     * @param value
     * @return
     * @throws IOException
     *             if put to JSONObject fails
     */
    public static JSONObject addAttribute(JSONObject obj, String key,
            Object value) throws IOException {
        try {
            if (value != null && !value.equals("")) {
                obj.put(key, value);
            }
        } catch (JSONException ex) {
            throw new IOException("JSONObject put failed.");
        }
        return obj;
    }

    /**
     * Method creates JSON containing chart options
     * @param context
     * @param component
     * @return
     * @throws IOException
     */
    public JSONObject getOpts(FacesContext context, UIComponent component)
            throws IOException {
        JSONObject obj = new JSONObject();
        addAttribute(obj, "zoom", component.getAttributes().get("zoom"));
        addAttribute(obj, "charttype",
                component.getAttributes().get("charttype"));
        addAttribute(obj, "xtype", component.getAttributes().get("xtype"));
        addAttribute(obj, "ytype", component.getAttributes().get("ytype"));

        JSONObject xaxis = new JSONObject();
        addAttribute(xaxis, "min", component.getAttributes().get("xmin"));
        addAttribute(xaxis, "max", component.getAttributes().get("xmax"));
        addAttribute(xaxis, "autoscaleMargin",
                component.getAttributes().get("xpad"));
        addAttribute(xaxis, "axisLabel", component.getAttributes()
                .get("xlabel"));
        addAttribute(xaxis, "format", component.getAttributes().get("xformat"));

        JSONObject yaxis = new JSONObject();
        addAttribute(yaxis, "min", component.getAttributes().get("ymin"));
        addAttribute(yaxis, "max", component.getAttributes().get("ymax"));
        addAttribute(yaxis, "autoscaleMargin",
                component.getAttributes().get("ypad"));
        addAttribute(yaxis, "axisLabel", component.getAttributes()
                .get("ylabel"));
        addAttribute(yaxis, "format", component.getAttributes().get("yformat"));

        JSONObject legend = new JSONObject();
        addAttribute(legend, "position",
                component.getAttributes().get("position"));
        addAttribute(legend, "sorted", component.getAttributes().get("sorting"));

        addAttribute(obj, "xaxis", xaxis);
        addAttribute(obj, "yaxis", yaxis);
        addAttribute(obj, "legend", legend);
        return obj;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

        if (!component.isRendered()) {
            return;
        }
        Map<String, String> requestParameterMap = context.getExternalContext()
                .getRequestParameterMap();

        if (requestParameterMap.get(component.getClientId(context)) != null) {
            String xParam = requestParameterMap.get(getFieldId(component,
                    X_VALUE));
            String yParam = requestParameterMap.get(getFieldId(component,
                    Y_VALUE));
            String pointIndexParam = requestParameterMap.get(getFieldId(
                    component, POINT_INDEX));
            String eventTypeParam = requestParameterMap.get(getFieldId(
                    component, EVENT_TYPE));
            String seriesIndexParam = requestParameterMap.get(getFieldId(
                    component, SERIES_INDEX));
            try {

                if (PLOT_CLICK_TYPE.equals(eventTypeParam)) {
                    double y = Double.parseDouble(yParam);
                    int seriesIndex = Integer.parseInt(seriesIndexParam);
                    int pointIndex = Integer.parseInt(pointIndexParam);
                    String x = xParam;
                    new PlotClickEvent(component, seriesIndex, pointIndex, x, y)
                            .queue();
                }
            } catch (NumberFormatException ex) {
                throw new FacesException("Cannot convert request parmeters", ex);
            }

        }
    }

    /**
     * Returns chart chart data
     *
     * @param ctx
     * @param component
     * @return
     */
    public JSONArray getData(FacesContext ctx, UIComponent component) {
        return (JSONArray) component.getAttributes().get("data");
    }

    /**
     * Method process chart tags, it collects chart options and data.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        super.encodeBegin(context, component);

        AbstractChart chart = (AbstractChart) component;

        VisitChart visitCallback = new VisitChart(chart);
        // copy attributes to parent tag and process data
        chart.visitTree(VisitContext.createVisitContext(FacesContext
                .getCurrentInstance()), visitCallback);

        // store data to parent tag
        component.getAttributes().put("data", visitCallback.getData());

        if (!visitCallback.isDataEmpty()) {
            component.getAttributes().put("charttype",
                    visitCallback.getChartType());
            component.getAttributes().put("xtype",
                    axisDataTypeToString(visitCallback.getKeyType()));
            component.getAttributes().put("ytype",
                    axisDataTypeToString(visitCallback.getValType()));
        }

        component.getAttributes().put("handlers",
                visitCallback.getSeriesSpecificHandlers());

    }

    /**
     * Converts class name of data type used in axes to shorter string
     * representation ie. class java.lang.String -> string
     *
     * @param c
     * @return
     */
    public String axisDataTypeToString(Class c) {
        if (c == String.class) {
            return "string";
        } else if (c == Number.class) {
            return "number";
        } else if (c == Date.class) {
            return "date";
        } else {
            return c.getName();
        }
    }

    public JSONObject getParticularSeriesHandler(FacesContext context,
            UIComponent component) {
        return (JSONObject) component.getAttributes().get("handlers");
    };

    /**
     * Method creates unique identifier for request parameter.
     * @param component
     * @param attribute
     * @return
     */
    public String getFieldId(UIComponent component, String attribute) {
        return component.getClientId() + attribute;
    }

    /**
     * Callback loop through children tags: axis, series, legend
     */
    class VisitChart implements VisitCallback {

        private AbstractChart chart;
        private JSONArray data;
        private JSONObject seriesSpecificHandlers;
        private JSONArray plotClickHandlers;
        private JSONArray plothoverHandlers;
        private ChartDataModel.ChartType chartType;
        private Class keyType;
        private Class valType;
        private RenderKitUtils.ScriptHashVariableWrapper eventWrapper = RenderKitUtils.ScriptHashVariableWrapper.eventHandler;
        private boolean nodata;

        public VisitChart(AbstractChart ch) {
            this.nodata = true;
            this.chart = ch;
            this.chartType = null;
            this.data = new JSONArray();
            this.seriesSpecificHandlers = new JSONObject();
            this.plotClickHandlers = new JSONArray();
            this.plothoverHandlers = new JSONArray();

            try {
                addAttribute(seriesSpecificHandlers, "onplotclick",
                        plotClickHandlers);
                addAttribute(seriesSpecificHandlers, "onplothover",
                        plothoverHandlers);
            } catch (IOException ex) {
                throw new FacesException(ex);
            }
        }

        private void copyAttr(UIComponent src, UIComponent target,
                String prefix, String attr) {
            Object val = src.getAttributes().get(attr);
            if (val != null) {
                target.getAttributes().put(prefix + attr, val);
            }
        }

        /**
         * Copy attributes from source UIComponent to target.
         * @param src
         * @param target
         * @param prefix
         * @param attrs
         */
        private void copyAttrs(UIComponent src, UIComponent target,
                String prefix, List<String> attrs) {
            for (Iterator<String> it = attrs.iterator(); it.hasNext();) {
                String attr = it.next();
                copyAttr(src, target, prefix, attr);
            }
        }

        @Override
        public VisitResult visit(VisitContext context, UIComponent target) {

            if (target instanceof AbstractLegend) {
                copyAttrs(target, chart, "", asList("position", "sorting"));
            } else if (target instanceof AbstractSeries) {
                AbstractSeries s = (AbstractSeries) target;
                ChartDataModel model = s.getData();

                // Collect Series specific handlers
                Map<String, Object> optMap = new HashMap<String, Object>();
                RenderKitUtils.Attributes seriesEvents = attributes().generic(
                        "onplothover", "onplothover", "plothover").generic(
                        "onplotclick", "onplotclick", "plotclick");

                addToScriptHash(optMap, context.getFacesContext(), target,
                        seriesEvents,
                        RenderKitUtils.ScriptHashVariableWrapper.eventHandler);

                if (optMap.get("onplotclick") != null) {
                    plotClickHandlers.put(new RawJSONString(optMap.get(
                            "onplotclick").toString()));
                } else {
                    plotClickHandlers.put(s.getOnplotclick());
                }

                if (optMap.get("onplothover") != null) {
                    plothoverHandlers.put(new RawJSONString(optMap.get(
                            "onplothover").toString()));
                } else {
                    plothoverHandlers.put(s.getOnplothover());
                }
                // end collect series specific handler

                if (model == null) {
                    /**
                     * data model priority: if there is data model passed
                     * through data attribute use it. Otherwise nested point
                     * tags are expected.
                     */
                    VisitSeries seriesCallback = new VisitSeries(s.getType());
                    s.visitTree(VisitContext.createVisitContext(FacesContext
                            .getCurrentInstance()), seriesCallback);
                    model = seriesCallback.getModel();

                    // if series has no data create empty model
                    if (model == null) {
                        switch (s.getType()) {
                        case line:
                            model = new NumberChartDataModel(ChartType.line);
                            break;
                        case bar:
                            model = new NumberChartDataModel(ChartType.bar);
                            break;
                        case pie:
                            model = new StringChartDataModel(ChartType.pie);
                            break;
                        default:
                            break;
                        }
                    } else {
                        nodata = false;
                    }
                } else {
                    nodata = false;
                }
                model.setAttributes(s.getAttributes());

                try {
                    // Check model/series compatibility

                    if (chartType == null && (!nodata)) {
                        // if series is empty do not set types
                        chartType = model.getType();
                        keyType = model.getKeyType();
                        valType = model.getValueType();
                    } else {
                        if (chartType == ChartDataModel.ChartType.pie) {
                            throw new IllegalArgumentException(
                                    "Pie chart supports only one series.");
                        }
                    }
                    if (keyType != model.getKeyType()
                            || valType != model.getValueType()) {
                        throw new IllegalArgumentException(
                                "Data model is not valid for this chart type.");
                    }

                    data.put(model.export());
                } catch (IOException ex) {
                    throw new FacesException(ex);
                }

            } else if (target instanceof AbstractXaxis) {
                copyAttrs(target, chart, "x",
                        asList("min", "max", "pad", "label", "format"));
            } else if (target instanceof AbstractYaxis) {
                copyAttrs(target, chart, "y",
                        asList("min", "max", "pad", "label", "format"));
            }
            return VisitResult.ACCEPT;
        }

        public boolean isDataEmpty() {
            return nodata;
        }

        public JSONArray getData() {
            return data;
        }

        public Class getKeyType() {
            return keyType;
        }

        public Class getValType() {
            return valType;
        }

        public ChartDataModel.ChartType getChartType() {
            return chartType;
        }

        public JSONObject getSeriesSpecificHandlers() {
            return seriesSpecificHandlers;
        }

    }

    /**
     * Callback loops through series children tags - points
     */
    class VisitSeries implements VisitCallback {

        private ChartDataModel model = null;
        private ChartDataModel.ChartType type;

        public VisitSeries(ChartDataModel.ChartType type) {
            this.type = type;

        }

        @Override
        public VisitResult visit(VisitContext context, UIComponent target) {

            if (target instanceof AbstractPoint) {

                AbstractPoint p = (AbstractPoint) target;

                Object x = p.getX();
                Object y = p.getY();

                // the first point determine type of data model
                if (model == null) {
                    if (x instanceof Number && y instanceof Number) {
                        model = new NumberChartDataModel(type);
                    } else if (x instanceof String && y instanceof Number) {
                        model = new StringChartDataModel(type);
                    } else {
                        throw new IllegalArgumentException("Not supported type");
                    }
                }

                if (model.getKeyType().isAssignableFrom(x.getClass())
                        && model.getValueType().isAssignableFrom(y.getClass())) {

                    if (x instanceof Number && y instanceof Number) {
                        model.put((Number) x, (Number) y);
                    } else if (x instanceof String && y instanceof Number) {
                        model.put((String) x, (Number) y);
                    } else {
                        throw new IllegalArgumentException(
                                "Not supported types " + x.getClass() + " "
                                        + y.getClass() + " for "
                                        + model.getClass());
                    }

                } else {
                    throw new IllegalArgumentException("Not supported types "
                            + x.getClass() + " " + y.getClass() + " for "
                            + model.getClass());
                }
            }
            return VisitResult.ACCEPT;
        }

        public ChartDataModel getModel() {
            return model;
        }
    }
}
