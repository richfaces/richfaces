/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.fragment.tree.RichFacesTreeNode;

/**
 * Custom tree
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CustomTree extends RichFacesTree {

    @FindByJQuery("> .rf-tr-nd")
    private List<CustomNode> childNodes;

    private final AdvancedOwnTreeInteractionsImpl interactions = new AdvancedOwnTreeInteractionsImpl();

    @Override
    public AdvancedTreeInteractionsImpl advanced() {
        return interactions;
    }

    private class AdvancedOwnTreeInteractionsImpl extends AdvancedTreeInteractionsImpl {

        @Override
        protected List<? extends TreeNode> getChildNodes() {
            return Collections.unmodifiableList(childNodes);
        }
    }

    public static class CustomNode extends RichFacesTreeNode {

        @FindByJQuery(".rf-trn > .rf-trn-cnt > .rf-trn-lbl >*[onclick]")
        private WebElement elementForInteraction;
        @FindByJQuery("> .rf-tr-nd")
        private List<CustomNode> childNodes;

        private final AdvancedOwnNodeInteractions interactions = new AdvancedOwnNodeInteractions();

        @Override
        public AdvancedNodeInteractionsImpl advanced() {
            return interactions;
        }

        private class AdvancedOwnNodeInteractions extends AdvancedNodeInteractionsImpl {

            @Override
            protected List<? extends TreeNode> getChildNodes() {
                return Collections.unmodifiableList(childNodes);
            }

            @Override
            protected WebElement getCorrectElementForInteraction() {
                return elementForInteraction;
            }
        }
    }
}
