package org.richfaces.demo.inplaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.richfaces.demo.tables.model.capitals.Capital;

@ManagedBean
@RequestScoped
public class InplaceSelectBean {
    @ManagedProperty(value = "#{capitalsParser.capitalsList}")
    private List<Capital> capitals;
    private List<SelectItem> capitalsOptions = null;
    private String value;

    @PostConstruct
    public void init() {
        capitalsOptions = new ArrayList<SelectItem>();
        for (Capital capital : capitals) {
            capitalsOptions.add(new SelectItem(capital.getName(), capital.getState()));
        }
    }

    public Collection<Capital> autocomplete(FacesContext facesContext, UIComponent component, final String prefix) {
        Collection<Capital> persons = Collections2.filter(capitals, new Predicate<Capital>() {
            @Override
            public boolean apply(Capital capital) {
                if (prefix == null) {
                    return true;
                }
                return capital.getState().toLowerCase().startsWith(prefix.toLowerCase());
            }
        });
        return persons;
    }

    public List<SelectItem> getCapitalsOptions() {
        return capitalsOptions;
    }

    public void setCapitalsOptions(List<SelectItem> capitalsOptions) {
        this.capitalsOptions = capitalsOptions;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
