package org.richfaces.demo.common.navigation;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class GroupDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = -3481702232804120885L;
    private Collection<DemoDescriptor> demos;

    private boolean containsNewDemos() {
        for (DemoDescriptor demo : demos) {
            if (demo.isNewItems()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledDemos() {
        for (DemoDescriptor demo : demos) {
            if (demo.hasEnabledItems()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewItems() {
        return isNewEnabled() || containsNewDemos();
    }

    public boolean hasEnabledItems() {
        return isCurrentlyEnabled() && containsEnabledDemos();
    }

    /**
     * "This method must be present for JAXB - you should be calling {link #getFilteredDemos} instead"
     */
    @XmlElementWrapper(name = "demos")
    @XmlElement(name = "demo")
    public Collection<DemoDescriptor> getDemos() {
        if (demos == null) {
            return null;
        }
        return Collections2.filter(demos, new Predicate<DemoDescriptor>() {
            public boolean apply(DemoDescriptor demo) {
                return demo.hasEnabledItems();
            };
        });
    }

    public void setDemos(Collection<DemoDescriptor> demos) {
        this.demos = demos;
    }
}
