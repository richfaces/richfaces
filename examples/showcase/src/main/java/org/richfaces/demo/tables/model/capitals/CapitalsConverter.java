package org.richfaces.demo.tables.model.capitals;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@FacesConverter("CapitalsConverter")
public class CapitalsConverter implements Converter {
    private CapitalsParser capitalsParser;

    public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
        for (Capital capital : getCapitalsParser(facesContext).getCapitalsList()) {
            if (capital.getName().equals(s)) {
                return capital;
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
        if (o == null) return null;
        return ((Capital) o).getName();
    }

    private CapitalsParser getCapitalsParser(FacesContext facesContext) {
        if (capitalsParser == null) {
            ELContext elContext = facesContext.getELContext();
            capitalsParser = (CapitalsParser) elContext.getELResolver().getValue(elContext, null, "capitalsParser");
        }
        return capitalsParser;
    }
}
