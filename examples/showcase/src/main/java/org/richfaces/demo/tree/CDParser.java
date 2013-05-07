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

package org.richfaces.demo.tree;

import java.net.URL;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ManagedBean(name = "cdsParser")
@ApplicationScoped
public class CDParser {
    private List<CDXmlDescriptor> cdsList;

    @XmlRootElement(name = "CATALOG")
    private static final class CDsHolder {
        private List<CDXmlDescriptor> cds;

        @XmlElement(name = "CD")
        public List<CDXmlDescriptor> getCds() {
            return cds;
        }

        @SuppressWarnings("unused")
        public void setCds(List<CDXmlDescriptor> cds) {
            this.cds = cds;
        }
    }

    public synchronized List<CDXmlDescriptor> getCdsList() {
        if (cdsList == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            URL resource = ccl.getResource("org/richfaces/demo/tree/CDCatalog.xml");
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(CDsHolder.class);
                CDsHolder cdsHolder = (CDsHolder) context.createUnmarshaller().unmarshal(resource);
                cdsList = cdsHolder.getCds();
            } catch (JAXBException e) {
                throw new FacesException(e.getMessage(), e);
            }
        }

        return cdsList;
    }
}
