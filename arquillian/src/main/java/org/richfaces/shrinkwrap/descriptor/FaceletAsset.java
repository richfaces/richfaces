/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

    @Override
    public InputStream openStream() {
        return getAsStringAssert().openStream();
    }

    private StringAsset getAsStringAssert() {
        return new StringAsset(SimplifiedFormat.format(TEMPLATE, xmlns, head, body, form));
    }

    public FaceletAsset head(Object... heads) {
        for (Object head : heads) {
            this.head.append(head);
            this.head.append('\n');
        }
        return this;
    }

    public FaceletAsset body(Object... bodies) {
        for (Object body : bodies) {
            this.body.append(body);
            this.body.append('\n');
        }
        return this;
    }

    public FaceletAsset form(Object... forms) {
        for (Object form : forms) {
            this.form.append(form);
            this.form.append('\n');
        }
        return this;
    }

    public FaceletAsset xmlns(String prefix, String uri) {
        xmlns.append("xmlns:" + prefix + "=\"" + uri + "\" \n");
        return this;
    }
}
