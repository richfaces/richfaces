package org.richfaces.taglib;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DataScrollListener;

/**
 * @author Anton Belevich
 *
 */
public class MethodExpressionScrollListener implements DataScrollListener {
    private MethodExpression methodExpression;

    public MethodExpressionScrollListener() {
        super();
    }

    MethodExpressionScrollListener(MethodExpression methodExpression) {
        super();
        this.methodExpression = methodExpression;
    }

    public void processDataScroll(DataScrollEvent dataScrollerEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        methodExpression.invoke(facesContext.getELContext(), new Object[] { dataScrollerEvent });
    }
}
