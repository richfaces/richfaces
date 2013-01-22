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
package org.ajax4jsf.component;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.IntegerConverter;
import javax.faces.event.ActionEvent;

import org.richfaces.test.AbstractFacesTest;

/**
 * @author shura
 *
 */
public class ActionListenerTest extends AbstractFacesTest {
    /*
    *  (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIActionParameter#getValue()}.
     */
    public void testGetValue() {
        Bean bean = new Bean();

        bean.setFirst(1);
        bean.setSecond(2.0);

        UIActionParameter param = new UIActionParameter();

        param.setConverter(new TestConverter());
        param.setValue(bean);
        assertEquals("1;2.0", param.getValue());
    }

    public void testGetIntValue() throws Exception {
        UIActionParameter param = new UIActionParameter();

        application.addConverter(Integer.class, IntegerConverter.class.getName());
        param.setValue(new Integer(1));
        assertEquals("1", param.getValue());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIActionParameter#processAction(javax.faces.event.ActionEvent)}.
     */
    public void testProcessAction() {
        UICommand command = new UICommand();
        UIActionParameter param = new UIActionParameter();

        param.setConverter(new IntegerConverter());
        param.setName("param");

        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ValueExpression expression = expressionFactory.createValueExpression(elContext, "#{bean.first}", Integer.TYPE);

        param.setAssignToBinding(expression);

        Bean bean = new Bean();

        facesContext.getExternalContext().getRequestMap().put("bean", bean);
        this.connection.addRequestParameter("param", "123");
        command.addActionListener(param);
        command.broadcast(new ActionEvent(command));
        assertEquals(123, bean.getFirst());
    }

    public static class Bean implements Serializable {
        int _first;
        double _second;

        /**
         * @return the first
         */
        public int getFirst() {
            return this._first;
        }

        /**
         * @param first the first to set
         */
        public void setFirst(int first) {
            this._first = first;
        }

        /**
         * @return the second
         */
        public double getSecond() {
            return this._second;
        }

        /**
         * @param second the second to set
         */
        public void setSecond(double second) {
            this._second = second;
        }
    }

    static class TestConverter implements Converter {
        /*
        *  (non-Javadoc)
        * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
        */
        public Object getAsObject(FacesContext context, UIComponent comp, String str) {
            Bean bean = new Bean();
            String[] values = str.split(";");

            bean.setFirst(Integer.parseInt(values[0]));
            bean.setSecond(Double.parseDouble(values[1]));

            return bean;
        }

        /*
         *  (non-Javadoc)
         * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
         */
        public String getAsString(FacesContext context, UIComponent comp, Object value) {
            Bean bean = (Bean) value;

            return String.valueOf(bean.getFirst()) + ";" + String.valueOf(bean.getSecond());
        }
    }
}
