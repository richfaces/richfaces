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

/**
 *
 */
package org.richfaces.demo.validator;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * @author asmirnov
 *
 */
@ManagedBean
@SessionScoped
public class ValidatorBean {
    private final List<Validable<?>> beans;

    /**
     * @return the beans
     */
    @Valid
    public List<Validable<?>> getBeans() {
        return beans;
    }

    public ValidatorBean() {
        beans = new ArrayList<Validable<?>>(7);
        beans.add(new NotNullBean());
        beans.add(new NotEmptyBean());
        beans.add(new SizeBean());
        beans.add(new MinBean());
        beans.add(new MaxBean());
        beans.add(new MinMaxBean());
        beans.add(new PatternBean());
    }

    @Max(value = 20, message = "Total value should be less then 20")
    public int getTotal() {
        int total = 0;
        for (Validable<?> bean : beans) {
            Object value = bean.getValue();
            if (value instanceof Integer) {
                Integer intValue = (Integer) value;
                total += intValue;
            }
        }
        return total;
    }
}
