package org.richfaces.demo.common.navigation;

import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class GroupDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = -3481702232804120885L;
    private List<DemoDescriptor> demos;
    private List<DemoDescriptor> filteredDemos;

    private boolean containsNewDemos() {
        for (DemoDescriptor demo : demos) {
            if (demo.isNewItems()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledDemos(FacesContext facesContext) {
        for (DemoDescriptor demo : demos) {
            if (demo.hasEnabledItems(facesContext)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewItems() {
        return isNewItem() || containsNewDemos();
    }

    public boolean hasEnabledItems(FacesContext facesContext) {
        return this.isEnabled(facesContext) && containsEnabledDemos(facesContext);
    }

    @XmlElementWrapper(name = "demos")
    @XmlElement(name = "demo")
    public List<DemoDescriptor> getDemos() {
        if (demos == null) {
            return null;
        }
        if (filteredDemos == null) {
            filteredDemos = new LinkedList<DemoDescriptor>(Collections2.filter(demos, new Predicate<DemoDescriptor>() {
                public boolean apply(DemoDescriptor demo) {
                    return demo.hasEnabledItems(FacesContext.getCurrentInstance());
                };
            }));
        }
        return filteredDemos;
    }

    public void setDemos(List<DemoDescriptor> demos) {
        this.demos = demos;
    }
}
