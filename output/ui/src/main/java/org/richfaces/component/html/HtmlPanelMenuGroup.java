/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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


package org.richfaces.component.html;

import org.richfaces.component.UIPanelMenuGroup;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class HtmlPanelMenuGroup extends UIPanelMenuGroup implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuGroup";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuGroup";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "collapse",
        "expand",
        "switch",
        "beforecollapse",
        "beforeexpand",
        "beforeswitch"
    ));


    public enum PropertyKeys {
        oncollapse,
        onexpand,
        onswitch,
        onbeforecollapse,
        onbeforeexpand,
        onbeforeswitch
    }

    public HtmlPanelMenuGroup() {
        setRendererType("org.richfaces.PanelMenuGroup");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getOncollapse() {
        return (String) getStateHelper().eval(PropertyKeys.oncollapse);
    }

    public void setOncollapse(String oncollapse) {
        getStateHelper().put(PropertyKeys.oncollapse, oncollapse);
    }

    public String getOnexpand() {
        return (String) getStateHelper().eval(PropertyKeys.onexpand);
    }

    public void setOnexpand(String onexpand) {
        getStateHelper().put(PropertyKeys.onexpand, onexpand);
    }

    public String getOnswitch() {
        return (String) getStateHelper().eval(PropertyKeys.onswitch);
    }

    public void setOnswitch(String onswitch) {
        getStateHelper().put(PropertyKeys.onswitch, onswitch);
    }

    public String getOnbeforecollapse() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforecollapse);
    }

    public void setOnbeforecollapse(String onbeforecollapse) {
        getStateHelper().put(PropertyKeys.onbeforecollapse, onbeforecollapse);
    }

    public String getOnbeforeexpand() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeexpand);
    }

    public void setOnbeforeexpand(String onbeforeexpand) {
        getStateHelper().put(PropertyKeys.onbeforeexpand, onbeforeexpand);
    }

    public String getOnbeforeswitch() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeswitch);
    }

    public void setOnbeforeswitch(String onbeforeswitch) {
        getStateHelper().put(PropertyKeys.onbeforeswitch, onbeforeswitch);
    }



    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
}

