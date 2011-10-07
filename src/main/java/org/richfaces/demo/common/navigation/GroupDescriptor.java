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
    private List<DemoDescriptor> desktopDemos;
    private List<DemoDescriptor> mobileDemos;

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

    /**
     * "This method must be present for JAXB - you should be calling {link #getFilteredDemos} instead"
     */
    @Deprecated()
    @XmlElementWrapper(name = "demos")
    @XmlElement(name = "demo")
    public List<DemoDescriptor> getDemos() {
        return demos;
    }

    public void setDemos(List<DemoDescriptor> demos) {
        this.demos = demos;
    }

    public List<DemoDescriptor> getFilteredDemos(boolean mobile) {
        if (mobile) {
            return getMobileDemos();
        } else {
            return getDesktopDemos();
        }
    }

    public List<DemoDescriptor> getDesktopDemos() {
        if (demos == null) {
            return null;
        }
        if (desktopDemos == null) {
            desktopDemos = new LinkedList<DemoDescriptor>(Collections2.filter(demos, new Predicate<DemoDescriptor>() {
                public boolean apply(DemoDescriptor demo) {
                    return demo.hasEnabledItems(FacesContext.getCurrentInstance());
                };
            }));
        }
        return desktopDemos;
    }

    public List<DemoDescriptor> getMobileDemos() {
        if (demos == null) {
            return null;
        }
        if (mobileDemos == null) {
            mobileDemos = new LinkedList<DemoDescriptor>(Collections2.filter(demos, new Predicate<DemoDescriptor>() {
                public boolean apply(DemoDescriptor demo) {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    return !demo.isMobileExclude() && demo.hasEnabledMobileItems(facesContext);
                };
            }));
        }
        return mobileDemos;
    }
}
