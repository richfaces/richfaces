package org.richfaces.context;

import java.util.Collection;
import java.util.Set;

import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

public class ExtendedVisitContextFactory extends VisitContextFactory {

    private static final String VISIT_MODE_ATTRIBUTE = ExtendedVisitContextFactory.class.getName() + ".VISIT_MODE";

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
        final ExtendedVisitContextMode visitMode = getVisitMode(facesContext);

        switch (visitMode) {
            case EXECUTE : return new ExecuteExtendedVisitContext(visitContextToWrap, facesContext, clientIds, hints);
            case RENDER : return new RenderExtendedVisitContext(visitContextToWrap, facesContext, clientIds, hints, limitRender);
        }
    }

    private ExtendedVisitContextMode getVisitMode(FacesContext facesContext) {
        ExtendedVisitContextMode mode = (ExtendedVisitContextMode) facesContext.getAttributes().get(VISIT_MODE_ATTRIBUTE);
        if (mode == null) {
            throw new IllegalStateException("The VisitMode must be initialized when creating ExtendedVisitModeContext");
        }
        return mode;
    }

}
