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
package org.richfaces.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractMediaOutput;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@DynamicResource
public class MediaOutputResource extends AbstractUserResource implements StateHolder, CacheableResource {
    private String contentType;
    private boolean cacheable;
    private MethodExpression contentProducer;
    private ValueExpression expiresExpression;

    private static final String PARENTHESES = "[^\\(]*";
    /*
     * TODO: add handling for expressions:
     *
     * 1. State saving 2. Evaluation
     */
    private ValueExpression lastModifiedExpression;
    private ValueExpression timeToLiveExpression;
    private Object userData;
    private String fileName;

    public void encode(FacesContext facesContext) throws IOException {
        OutputStream outStream = facesContext.getExternalContext().getResponseOutputStream();
        String expr = contentProducer.getExpressionString();

        if (!Pattern.matches(PARENTHESES, expr)) { // method expression must not be executed
            throw new IllegalArgumentException("Expression \"" + expr + "\" contains parentheses.");
        }

        contentProducer.invoke(facesContext.getELContext(), new Object[] { outStream, userData });
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public Object saveState(FacesContext context) {
        Object[] state = new Object[5];

        // parent fields state saving
        state[0] = isCacheable(context) ? Boolean.TRUE : Boolean.FALSE;
        state[1] = getContentType();
        state[2] = UIComponentBase.saveAttachedState(context, userData);
        state[3] = UIComponentBase.saveAttachedState(context, contentProducer);
        state[4] = fileName;

        return state;
    }

    public void restoreState(FacesContext context, Object stateObject) {
        Object[] state = (Object[]) stateObject;

        setCacheable((Boolean) state[0]);
        setContentType((String) state[1]);
        userData = UIComponentBase.restoreAttachedState(context, state[2]);
        contentProducer = (MethodExpression) UIComponentBase.restoreAttachedState(context, state[3]);
        fileName = (String) state[4];
    }

    // TODO use ResourceComponent or exchange object as argument?
    @PostConstructResource
    public void initialize() {
        AbstractMediaOutput uiMediaOutput = (AbstractMediaOutput) UIComponent.getCurrentComponent(FacesContext
            .getCurrentInstance());
        this.setCacheable(uiMediaOutput.isCacheable());
        this.setContentType(uiMediaOutput.getMimeType());
        this.userData = uiMediaOutput.getValue();
        this.contentProducer = uiMediaOutput.getCreateContent();
        this.lastModifiedExpression = uiMediaOutput.getValueExpression("lastModfied");
        this.expiresExpression = uiMediaOutput.getValueExpression("expires");
        this.timeToLiveExpression = uiMediaOutput.getValueExpression("timeToLive");
        this.fileName = uiMediaOutput.getFileName();
    }

    public boolean isCacheable(FacesContext context) {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public Date getExpires(FacesContext context) {
        return null;
    }

    public int getTimeToLive(FacesContext context) {
        return -1;
    }

    public String getEntityTag(FacesContext context) {
        return null;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<String, String>(2);

        if (!Strings.isNullOrEmpty(fileName)) {
            headers.put("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        }

        return headers;
    }
}
