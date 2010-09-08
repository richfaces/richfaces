package org.ajax4jsf.config;

import org.richfaces.el.util.ELUtils;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Anton Belevich
 * @since 4.0
 *        framework configuration class
 */
public final class FrameworkConfiguration {
    private static final Logger LOGGER = RichfacesLogger.CONFIG.getLogger();
    private static final Pattern ALLOWABLE_NUMBER = Pattern.compile("^\\d+$");
    private static final Pattern ALLOWABLE_BOOLEANS = Pattern.compile("^true|false$", Pattern.CASE_INSENSITIVE);
    private Map<InitParam, String> initParams = new EnumMap<InitParam, String>(InitParam.class);

    private FrameworkConfiguration(ExternalContext externalContext) {
        processInitParams(externalContext);
    }

    public enum InitParam {
        SKIN("org.richfaces.SKIN", "DEFAULT"), LoadScriptStrategy("org.richfaces.LoadScriptStrategy", "DEFAULT"),
        LoadStyleStrategy("org.richfaces.LoadStyleStrategy", "DEFAULT"), LOGFILE("org.ajax4jsf.LOGFILE", "none"),
        VIEW_HANDLERS("org.ajax4jsf.VIEW_HANDLERS", "none"),
        CONTROL_COMPONENTS("org.ajax4jsf.CONTROL_COMPONENTS", "none"),
        ENCRYPT_PASSWORD("org.ajax4jsf.ENCRYPT_PASSWORD", "random"),
        GLOBAL_RESOURCE_URI_PREFIX("org.ajax4jsf.GLOBAL_RESOURCE_URI_PREFIX", "a4j/g"),
        SESSION_RESOURCE_URI_PREFIX("org.ajax4jsf.SESSION_RESOURCE_URI_PREFIX", "a4j/s"),
        ENCRYPT_RESOURCE_DATA("org.ajax4jsf.ENCRYPT_RESOURCE_DATA", "false"),
        COMPRESS_SCRIPT("org.ajax4jsf.COMPRESS_SCRIPT", "true"),
        SERIALIZE_SERVER_STATE("org.ajax4jsf.SERIALIZE_SERVER_STATE", "false"),
        DEFAULT_EXPIRE("org.ajax4jsf.DEFAULT_EXPIRE", "86400");

        private String defaultValue;
        private String qualifiedName;

        InitParam(String qualifiedName, String defaultValue) {
            this.qualifiedName = qualifiedName;
            this.defaultValue = defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getQualifiedName() {
            return qualifiedName;
        }
    }

    public static FrameworkConfiguration getInstance() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        return getInstance(facesContext.getExternalContext());
    }

    public static FrameworkConfiguration getInstance(ExternalContext externalContext) {
        return new FrameworkConfiguration(externalContext);
    }

    private boolean isBooleanValue(String value) {
        return ALLOWABLE_BOOLEANS.matcher(value).matches();
    }

    private boolean isNumberValue(String value) {
        return ALLOWABLE_NUMBER.matcher(value).matches();
    }

    public int getOptionNumber(InitParam param) {
        String value = getOption(param);

        if (ELUtils.isValueReference(value)) {
            return ((Integer) resolveELParam(FacesContext.getCurrentInstance(), value,
                                             java.lang.Integer.class)).intValue();
        }

        if (isNumberValue(value)) {
            return Integer.parseInt(value);
        }

        LOGGER.error("option value: " + param.getQualifiedName() + " is not an integer number set default value: "
                     + param.getDefaultValue());

        return Integer.parseInt(param.getDefaultValue());
    }

    public boolean isOptionEnabled(InitParam param) {
        String value = getOption(param);

        if (ELUtils.isValueReference(value)) {
            return ((Boolean) resolveELParam(FacesContext.getCurrentInstance(), value,
                                             java.lang.Boolean.class)).booleanValue();
        }

        if (isBooleanValue(value)) {
            return Boolean.parseBoolean(value);
        }

        LOGGER.error("option value: " + param.getQualifiedName() + " is not a boolean value set default value: "
                     + param.getDefaultValue());

        return Boolean.parseBoolean(param.getDefaultValue());
    }

    public String getOptionValue(InitParam param) {
        String value = getOption(param);

        if (ELUtils.isValueReference(value)) {
            return (String) resolveELParam(FacesContext.getCurrentInstance(), value, java.lang.String.class);
        }

        return value;
    }

    public String getOption(InitParam param) {
        return initParams.get(param);
    }

    private void processInitParams(ExternalContext externalContext) {
        for (InitParam param : InitParam.values()) {
            String paramName = param.qualifiedName;
            String paramValue = externalContext.getInitParameter(paramName);
            String value = param.getDefaultValue();

            if ((paramValue != null) && !"".equals(paramValue.trim())) {
                value = paramValue;
            }

            initParams.put(param, value);
        }
    }

    public Object resolveELParam(FacesContext context, String value, Class<?> expectedClass) {
        ExpressionFactory factory = context.getApplication().getExpressionFactory();
        ValueExpression valueExpression = factory.createValueExpression(context.getELContext(), value, expectedClass);
        Object returnValue = valueExpression.getValue(context.getELContext());

        return returnValue;
    }
}
