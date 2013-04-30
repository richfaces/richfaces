package org.richfaces.ui.iteration.dataTable;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

import org.richfaces.ui.iteration.dataScroller.DataScrollEvent;
import org.richfaces.ui.iteration.dataScroller.DataScrollListener;

/**
 * @author Anton Belevich
 *
 */
public class MethodExpressionScrollListener implements DataScrollListener {
    private MethodExpression methodExpression;

    public MethodExpressionScrollListener() {
        super();
    }

    public MethodExpressionScrollListener(MethodExpression methodExpression) {
        super();
        this.methodExpression = methodExpression;
    }

    public void processDataScroll(DataScrollEvent dataScrollerEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        methodExpression.invoke(facesContext.getELContext(), new Object[] { dataScrollerEvent });
    }
}
