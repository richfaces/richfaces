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
package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;

/**
 * Interface defining the methods for AjaxOutput-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface AjaxOutputProps {
    /**
     * Defines, whether the content of this component must be (or not) included in AJAX response created by parent AJAX
     * Container, even if it is not forced by reRender list of ajax action. Ignored if component marked to output by
     * some Ajax action component. Default value - "true"
     */
    @Attribute(description = @Description("Defines, whether the content of this component must be (or not) included in AJAX response created by parent AJAX Container, even if it is not forced by reRender list of ajax action. Ignored if component marked to output by some Ajax action component. Default value - \"true\""), defaultValue = "true")
    boolean isAjaxRendered();

    /**
     * Flag to mark all child components to non-transient. If true, all children components will be set to non-transient
     * state and keep in saved components tree. For output in self-renderer region all content ( By default, all content
     * in &lt;f:verbatim&gt; tags and non-jsf elements in facelets, marked as transient - since, self-rendered ajax
     * regions don't plain output for ajax processing ).
     */
    @Attribute(description = @Description("Flag to mark all child components to non-transient. If true, all children components will be set to non-transient state and keep in saved components tree. For output in self-renderer region all content ( By default, all content in &lt;f:verbatim&gt; tags and non-jsf elements in facelets, marked as transient - since, self-rendered ajax regions don't plain output for ajax processing )."))
    boolean isKeepTransient();
}