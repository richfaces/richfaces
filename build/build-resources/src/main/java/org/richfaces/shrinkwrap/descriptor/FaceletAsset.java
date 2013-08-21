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
package org.richfaces.shrinkwrap.descriptor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;

/**
 * Assert simplifying process of creating facelets XHTML definitions out of template.
 *
 * @author Lukas Fryc
 */
public class FaceletAsset implements Asset {

    private static final String TEMPLATE;

    private StringBuilder head = new StringBuilder();
    private StringBuilder body = new StringBuilder();
    private StringBuilder xmlns = new StringBuilder();
    private StringBuilder form = new StringBuilder();

    static {
        try {
            TEMPLATE = IOUtils.toString(FaceletAsset.class.getResourceAsStream("facelet-template.xhtml"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public FaceletAsset() {
        this.xmlns("ui", "http://java.sun.com/jsf/facelets");
        this.xmlns("h", "http://java.sun.com/jsf/html");
        this.xmlns("f", "http://java.sun.com/jsf/core");
        this.xmlns("c", "http://java.sun.com/jsp/jstl/core");
        this.xmlns("r", "http://richfaces.org");
        this.xmlns("s", "http://richfaces.org/sandbox/prototyping");

        this.head("<style>body {background: white;}</style>");
    }

    @Override
    public InputStream openStream() {
        return getAsStringAsset().openStream();
    }

    private StringAsset getAsStringAsset() {
        StringBuilder formFull = new StringBuilder(form);
        if (form.length() > 0) {
            formFull.insert(0, "        <h:form id='form' prependId='false'>");
            formFull.append("\n        </h:form>");
        }

        return new StringAsset(SimplifiedFormat.format(TEMPLATE, xmlns, head, body, formFull));
    }

    /**
     * Places content into &lt;h:head&gt;
     */
    public FaceletAsset head(Object... heads) {
        for (Object head : heads) {
            this.head.append(head);
            this.head.append('\n');
        }
        return this;
    }

    /**
     * Places content into &lt;h:body&gt;
     */
    public FaceletAsset body(Object... bodies) {
        for (Object body : bodies) {
            this.body.append(body);
            this.body.append('\n');
        }
        return this;
    }

    /**
     * <p>
     * Adds content enclosed within form: &lt;h:form id='form' prependId='false' /&gt;
     * </p>
     *
     * <p>
     * No form is inserted when this method is not invoked.
     * </p>
     */
    public FaceletAsset form(Object... forms) {
        for (Object form : forms) {
            this.form.append(form);
            this.form.append('\n');
        }
        return this;
    }

    /**
     * <p>
     * Adds XMLNS with given prefix and URI
     * </p>
     */
    public FaceletAsset xmlns(String prefix, String uri) {
        xmlns.append("xmlns:" + prefix + "=\"" + uri + "\" \n");
        return this;
    }
}
