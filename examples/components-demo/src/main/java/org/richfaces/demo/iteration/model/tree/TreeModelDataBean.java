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
package org.richfaces.demo.iteration.model.tree;

import org.richfaces.demo.iteration.model.tree.adaptors.Entry;
import org.richfaces.demo.iteration.model.tree.adaptors.Root;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;

/**
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 25.07.2007
 *
 */
@ManagedBean
@ApplicationScoped
public class TreeModelDataBean {
    private Root root;

    private void initializeRoot() {

        try {
            JAXBContext context = JAXBContext.newInstance(Root.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setListener(new Listener() {
                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    super.afterUnmarshal(target, parent);

                    if (parent instanceof Entry) {
                        ((Entry) target).setParent((Entry) parent);
                    }
                }
            });

            root = (Root) unmarshaller.unmarshal(TreeModelDataBean.class.getResource("tree-model-data.xml"));
        } catch (JAXBException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public Root getRoot() {
        if (root == null) {
            initializeRoot();
        }

        return root;
    }
}
