/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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



package org.ajax4jsf.tests;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.TemplateClient;

/**
 * @author Nick Belaevski
 * @since 3.2.2
 */
public class MockFaceletContext extends FaceletContext {
    private Map<String, Integer> ids = new HashMap<String, Integer>();
    private FacesContext context;
    private ELResolver elResolver;
    private FunctionMapper functionMapper;
    private List<TemplateClient> templateClients;
    private VariableMapper variableMapper;

    public MockFaceletContext(FacesContext context) {
        super();
        this.context = context;
        this.templateClients = new ArrayList<TemplateClient>();

        ELContext elContext = context.getELContext();

        this.elResolver = elContext.getELResolver();
        this.functionMapper = elContext.getFunctionMapper();
        this.variableMapper = elContext.getVariableMapper();
    }

    public List<TemplateClient> getTemplateClients() {
        return templateClients;
    }

    public void setTemplateClients(List<TemplateClient> templateClients) {
        this.templateClients = templateClients;
    }

    @Override
    public String generateUniqueId(String base) {
        Integer cnt = (Integer) this.ids.get(base);

        if (cnt == null) {
            this.ids.put(base, new Integer(0));

            return base;
        } else {
            int i = cnt.intValue() + 1;

            this.ids.put(base, new Integer(i));

            return base + "_" + i;
        }
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return context.getApplication().getExpressionFactory();
    }

    @Override
    public FacesContext getFacesContext() {
        return context;
    }

    @Override
    public boolean includeDefinition(UIComponent parent, String name)
            throws IOException, FaceletException, FacesException, ELException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public void includeFacelet(UIComponent parent, String relativePath)
            throws IOException, FaceletException, FacesException, ELException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public void includeFacelet(UIComponent parent, URL absolutePath)
            throws IOException, FaceletException, FacesException, ELException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public void popClient(TemplateClient client) {
        if (this.templateClients.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.templateClients.remove(client);
    }

    @Override
    public void pushClient(TemplateClient client) {
        this.templateClients.add(0, client);
    }

    @Override
    public void extendClient(TemplateClient client) {
        this.templateClients.add(client);
    }

    @Override
    public Object getAttribute(String name) {
        if (this.variableMapper != null) {
            ValueExpression ve = this.variableMapper.resolveVariable(name);

            if (ve != null) {
                return ve.getValue(this);
            }
        }

        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (this.variableMapper != null) {
            if (value == null) {
                this.variableMapper.setVariable(name, null);
            } else {
                this.variableMapper.setVariable(name,
                                                this.getExpressionFactory().createValueExpression(value, Object.class));
            }
        }
    }

    @Override
    public void setFunctionMapper(FunctionMapper fnMapper) {
        this.functionMapper = fnMapper;
    }

    @Override
    public void setVariableMapper(VariableMapper varMapper) {
        this.variableMapper = varMapper;
    }

    @Override
    public ELResolver getELResolver() {
        return this.elResolver;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return this.functionMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return this.variableMapper;
    }
}
