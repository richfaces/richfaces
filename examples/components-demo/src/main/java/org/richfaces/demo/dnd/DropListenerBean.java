package org.richfaces.demo.dnd;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;

@RequestScoped
@ManagedBean
public class DropListenerBean implements DropListener {
    @ManagedProperty(value = "#{dndBean}")
    private DndBean dndBean;

    public void setDndBean(DndBean dndBean) {
        this.dndBean = dndBean;
    }

    public void processDrop(DropEvent event) {
        String value = (String) event.getDragValue();
        dndBean.addDropValues(value);
        dndBean.setPhaseId(event.getPhaseId().toString());
    }
}
