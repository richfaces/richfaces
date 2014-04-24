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
package org.richfaces.model;
/**
 * @author Lukas Macko
 */
public class NumberChartDataModel extends ChartDataModel<Number, Number> {

    public NumberChartDataModel(ChartType type) {
        super(type);
        switch (type) {
        case line:
            strategy = new LineStrategy();
            break;
        case bar:
            strategy = new BarStrategy();
            break;
        default:
            throw new IllegalArgumentException(type
                    + "not supported by NumberChartDataModel");
        }
    }

    @Override
    public Class getKeyType() {
        return Number.class;
    }

    @Override
    public Class getValueType() {
        return Number.class;
    }

}
