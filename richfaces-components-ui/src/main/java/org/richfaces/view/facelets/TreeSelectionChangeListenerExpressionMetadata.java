package org.richfaces.view.facelets;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.event.MethodExpressionTreeSelectionChangeListener;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.event.TreeSelectionChangeSource;

/**
 * @author Nick Belaevski
 *
 */
final class TreeSelectionChangeListenerExpressionMetadata extends Metadata {
    private static final Class<?>[] SIGNATURE = new Class[] { TreeSelectionChangeEvent.class };
    private final TagAttribute attr;

    TreeSelectionChangeListenerExpressionMetadata(TagAttribute attr) {
        this.attr = attr;
    }

    @Override
    public void applyMetadata(FaceletContext ctx, Object instance) {
        ((TreeSelectionChangeSource) instance).addTreeSelectionChangeListener(new MethodExpressionTreeSelectionChangeListener(
            this.attr.getMethodExpression(ctx, null, SIGNATURE)));
    }
}