package org.richfaces.component;

import java.util.Iterator;

import javax.faces.component.UIComponent;

import org.ajax4jsf.component.IterationStateHolder;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
final class StatefulDataTableChildrenIterator extends AbstractIterator<UIComponent> {

    private Iterator<UIComponent> dataChildren;

    private Iterator<UIComponent> subIterator = Iterators.emptyIterator();

    /**
     * @param uiDataTableBase
     */
    StatefulDataTableChildrenIterator(Iterator<UIComponent> dataChildren) {
        this.dataChildren = dataChildren;
    }

    private UIComponent traverseToNextElement() {
        if (dataChildren.hasNext()) {
            UIComponent c = dataChildren.next();
            
            if (c instanceof IterationStateHolder) {
                return c;
            } else {
                subIterator = c.getFacetsAndChildren();
            }
        }
        
        return null;
    }

    private UIComponent traverseToNextSubElement() {
        if (subIterator.hasNext()) {
            return subIterator.next();
        }

        return null;
    }

    @Override
    protected UIComponent computeNext() {
        while (dataChildren.hasNext() || subIterator.hasNext()) {
            UIComponent c = traverseToNextSubElement();
            
            if (c == null) {
                c = traverseToNextElement();
            }
            
            if (c != null) {
                return c;
            }
        }
        
        return endOfData();
    }
}