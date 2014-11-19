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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.renderkit.ChartRendererBase;


/**
 * @author Lukas Macko
 */
class CategoryBarStrategy implements ChartStrategy {

    public CategoryBarStrategy() {

    }

    @Override
    public Object export(ChartDataModel model) throws IOException {
        JSONObject obj = model.defaultExport();

        JSONObject bars = new JSONObject();
        ChartRendererBase.addAttribute(bars, "show", true);
        ChartRendererBase.addAttribute(bars, "align", "center");
        ChartRendererBase.addAttribute(obj, "bars", bars);

        // label
        ChartRendererBase.addAttribute(obj, "label",
                model.getAttributes().get("label"));
        // color
        ChartRendererBase.addAttribute(obj, "color",
                model.getAttributes().get("color"));

        return obj;
    }

}
