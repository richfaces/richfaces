package org.richfaces.context;

import java.util.Collection;
import java.util.Set;

import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

// TODO visitContext instanceof ExtendedVisitContext
public class ExtendedVisitContextFactory extends VisitContextFactory {

    private VisitContextFactory parentFactory;

    public ExtendedVisitContextFactory(VisitContextFactory parentFactory) {
        super();
        this.parentFactory = parentFactory;
    }

    @Override
    public VisitContextFactory getWrapped() {
        return parentFactory;
    }

    @Override
    public VisitContext getVisitContext(FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints) {

        final VisitContext visitContextToWrap = parentFactory.getVisitContext(facesContext, clientIds, hints);

        final ExtendedPartialViewContext epvc = ExtendedPartialViewContext.getInstance(facesContext);

        if (epvc != null && clientIds != null) {
            final ExtendedVisitContextMode visitMode = epvc.getVisitMode();

            if (visitMode != null) {
                switch (visitMode) {
                    case EXECUTE :
                        return new ExecuteExtendedVisitContext(visitContextToWrap, facesContext, clientIds, hints);
                    case RENDER :
                        return new RenderExtendedVisitContext(visitContextToWrap, facesContext, clientIds, hints, epvc.isLimitRender());
                }
            }
        }

        return visitContextToWrap;
    }

}
