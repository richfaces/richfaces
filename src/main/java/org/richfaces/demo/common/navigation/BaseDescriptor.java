package org.richfaces.demo.common.navigation;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.sun.faces.el.ELUtils;
import org.richfaces.demo.ui.UserAgentProcessor;

public class BaseDescriptor implements Serializable {
    private static final long serialVersionUID = 5614594358147757458L;
    private String id;
    private String name;
    private boolean newItem;
    private boolean currentItem;
    private String enabled;
    private boolean mobileExclude;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "new")
    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    @XmlAttribute(name = "enabled")
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @XmlAttribute(name = "mobileExclude")
    public boolean isMobileExclude() {
        return mobileExclude;
    }

    public void setMobileExclude(boolean mobileExclude) {
        this.mobileExclude = mobileExclude;
    }

    public boolean isEnabled(FacesContext facesContext) {
        if (enabled == null) {
            return true;
        }
        ELContext elContext = facesContext.getELContext();
        ValueExpression enabledVE = ELUtils.createValueExpression(enabled);
        try {
            return (Boolean) ELUtils.evaluateValueExpression(enabledVE, elContext);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(boolean currentItem) {
        this.currentItem = currentItem;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
