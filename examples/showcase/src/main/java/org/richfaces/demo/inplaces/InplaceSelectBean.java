package org.richfaces.demo.inplaces;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

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
