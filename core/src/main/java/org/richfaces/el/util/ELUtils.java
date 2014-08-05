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
package org.richfaces.el.util;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.text.MessageFormat;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import com.google.common.primitives.Primitives;

/**
 * @author asmirnov
 */
public final class ELUtils {
    private ELUtils() {

        // Utility class with static methods only - do not instantiate.
    }

    /**
     * Get EL-enabled value. Return same string, if not el-expression. Otherthise, return parsed and evaluated expression.
     *
     * @param value string to parse
     * @return interpreted el or unmodified value
     */
    public static boolean isValueReference(String value) {
        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");

        if (start >= 0) {
            int end = value.lastIndexOf('}');

            if ((end >= 0) && (start < end)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create a <code>ValueExpression</code> with the expected type of <code>Object.class</code>
     *
     * @param expression an EL expression
     * @return a new <code>ValueExpression</code> instance based off the provided <code>valueRef</code>
     */
    public static ValueExpression createValueExpression(String expression) {

        return createValueExpression(expression, Object.class);
    }

    /**
     * Creates value expression from string and stores expression's expected type
     *
     * @param expression string with EL expressions
     * @param expectedType the type expected from expression after evaluation
     * @return value expression from string and stores expression's expected type
     */
    public static ValueExpression createValueExpression(String expression, Class<?> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(), expression, expectedType);
    }

    public static Object evaluateValueExpression(ValueExpression expression, ELContext elContext) {
        if (expression.isLiteralText()) {
            return expression.getExpressionString();
        } else {
            return expression.getValue(elContext);
        }
    }

    /**
     * <p>
     * Creates value expression from string and stores expression's expected type.
     * </p>
     *
     * <p>
     * If the literal is provided, constant value expression is used instead.
     * </p>
     *
     * @param context current {@link FacesContext}
     * @param expression string with EL expressions
     * @param literal determined if the literal value is required
     * @param expectedType the type expected from expression after evaluation
     * @return value expression from string and stores expression's expected type
     */
    public static ValueExpression createValueExpression(FacesContext context, String expression, boolean literal,
            Class<?> expectedType) {

        ValueExpression result = null;

        if (!literal && ELUtils.isValueReference(expression)) {
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();

            if (expressionFactory == null) {
                throw new IllegalStateException("ExpressionFactory is null");
            }

            result = expressionFactory.createValueExpression(context.getELContext(), expression, expectedType);
        } else {
            Object coercedValue = coerce(expression, expectedType);
            if (coercedValue != null) {
                result = new ConstantValueExpression(coercedValue);
            }
        }

        return result;
    }

    /**
     * Coerce the given object to targetType.
     *
     * @param value object to be coerced
     * @param targetType which should be object coerced into
     * @return the given value coerced to targetType
     */
    public static <T> T coerce(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return targetType.cast(value);
        }

        if (value instanceof String) {
            PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
            if (editor == null && Primitives.isWrapperType(targetType)) {
                editor = PropertyEditorManager.findEditor(Primitives.unwrap(targetType));
            }

            if (editor != null) {

                editor.setAsText((String) value);
                return targetType.cast(editor.getValue());
            } else if (targetType.isEnum()) {
                return targetType.cast(Enum.valueOf((Class<Enum>) targetType, (String) value));
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Cannot convert {0} to object of {1} type", value,
                targetType.getName()));
    }
}
