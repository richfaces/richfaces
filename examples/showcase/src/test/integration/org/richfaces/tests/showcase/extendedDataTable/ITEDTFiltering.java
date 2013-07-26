/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.showcase.extendedDataTable;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.showcase.dataTable.ITTableFiltering;
import org.richfaces.tests.showcase.dataTable.page.TableFilteringPage;
import org.richfaces.tests.showcase.extendedDataTable.page.EDTFilteringPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ITEDTFiltering extends ITTableFiltering {

    @Page
    private EDTFilteringPage page;

    @Override
    protected String getAdditionToContextRoot() {
        String sampleName = "edt-filtering";

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        String addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}",
                demoName, sampleName);

        return addition;
    }

    @Override
    protected TableFilteringPage getPage() {
        return page;
    }

}
