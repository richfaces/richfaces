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

package org.richfaces.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.context.FacesContext;

import org.jboss.el.ExpressionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.richfaces.el.model.Bean;
import org.richfaces.el.model.Person;
import org.richfaces.validator.GraphValidatorState;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

public class ELTestBase {
    class DummyELResolver extends ELResolver {
        private final ELResolver beanResolver = new BeanELResolver();
        private final ELResolver mapResolver = new MapELResolver();
        private final ELResolver listResolver = new ListELResolver();

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            return String.class;
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return Iterators.emptyIterator();
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) {
            if (null == base) {
                if ("bean".equals(property)) {
                    return Bean.class;
                } else if ("person".equals(property)) {
                    return Person.class;
                }
            } else if (base instanceof List) {
                return listResolver.getType(context, base, property);
            } else if (base instanceof Map) {
                return mapResolver.getType(context, base, property);
            }
            return beanResolver.getType(context, base, property);
        }

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            if (null == base) {
                if ("bean".equals(property)) {
                    return bean;
                } else if ("person".equals(property)) {
                    return person;
                }
            } else if (base instanceof List) {
                return listResolver.getValue(context, base, property);
            } else if (base instanceof Map) {
                return mapResolver.getValue(context, base, property);
            }
            return beanResolver.getValue(context, base, property);
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            return true;
        }

        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            // do nothing

        }
    }

    class DummyELContext extends ELContext {
        public DummyELContext() {
            putContext(FacesContext.class, FacesContext.getCurrentInstance());
        }

        @Override
        public ELResolver getELResolver() {
            return elResolver;
        }

        @Override
        public FunctionMapper getFunctionMapper() {
            return null;
        }

        @Override
        public VariableMapper getVariableMapper() {
            return null;
        }
    }

    protected ExpressionFactoryImpl expressionFactory;
    protected Bean bean;
    protected ELResolver elResolver;
    protected ELContext elContext;
    protected CapturingELContext capturingELContext;
    protected Person person;

    @Before
    public void setUp() throws Exception {
        expressionFactory = new ExpressionFactoryImpl();
        bean = new Bean();
        person = new Person();
        bean.setString("foo");
        ArrayList<String> list = new ArrayList<String>(1);
        list.add("bar");
        bean.setList(list);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("boo", "baz");
        bean.setMap(map);
        elResolver = new DummyELResolver();
        elContext = new DummyELContext();
        capturingELContext = new CapturingELContext(elContext,Collections.<Object,GraphValidatorState>emptyMap());
    }

    @After
    public void tearDown() throws Exception {
        expressionFactory = null;
    }

    protected ValueExpression parse(String expressionString) {
        ValueExpression expression = expressionFactory.createValueExpression(elContext, expressionString, String.class);
        return expression;
    }
}
