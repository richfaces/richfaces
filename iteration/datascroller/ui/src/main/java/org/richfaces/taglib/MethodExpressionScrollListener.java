package org.richfaces.taglib;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

import org.richfaces.event.DataScrollerEvent;
import org.richfaces.event.DataScrollerListener;

/**
 * @author Anton Belevich
 *
 */
public class MethodExpressionScrollListener implements DataScrollerListener {

    private MethodExpression methodExpression;

    public MethodExpressionScrollListener() {
        super();
    }

    MethodExpressionScrollListener(MethodExpression methodExpression) {
        super();
        this.methodExpression = methodExpression;
    }

    public void processScroller(DataScrollerEvent dataScrollerEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        methodExpression.invoke(facesContext.getELContext(), new Object[] { dataScrollerEvent });
    }
}
