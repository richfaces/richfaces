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

    /**
     * <p>
     * Wraps extended visit context from VisitContext created from parent factory,
     * </p>
     *
     * <p>
     * only if current view processing is inside of {@link ExtendedPartialViewContext#processPartial(javax.faces.event.PhaseId)}
     * processing
     * </p>
     *
     * <p>
     * either in {@link ExtendedVisitContextMode#EXECUTE} or {@link ExtendedVisitContextMode#RENDER}.
     * </p>
     *
     * <p>
     * Otherwise if {@link VisitContextFactory} is called outside of
     * {@link ExtendedPartialViewContext#processPartial(javax.faces.event.PhaseId)} processing, a {@link VisitContext} created
     * by parent factory is used instead.
     */
    @Override
    public VisitContext getVisitContext(FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints) {
        final VisitContext visitContextToWrap = parentFactory.getVisitContext(facesContext, clientIds, hints);
        final ExtendedPartialViewContext epvc = ExtendedPartialViewContext.getInstance(facesContext);
        if (epvc != null && clientIds != null) {
            final ExtendedVisitContextMode visitMode = epvc.getVisitMode();
            if (visitMode != null) {
                switch (visitMode) {
                    case EXECUTE:
                        return new ExtendedExecuteVisitContext(visitContextToWrap, facesContext, clientIds, hints);
                    case RENDER:
                        // RF-14252: hack for MyFaces, with @resetValues MyFaces doesn't get render ids from epvc
                        if (clientIds.contains("@component")) {
                            clientIds.addAll(epvc.getRenderIds());
                        }
                        return new ExtendedRenderVisitContext(visitContextToWrap, facesContext, clientIds, hints,
                                epvc.isLimitRender());
                }
            }
        }
        return visitContextToWrap;
    }
}
