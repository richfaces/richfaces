package org.demo;


import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name = "dropListenerBean")
public class DropListenerBean implements DropListener {

    private DropEvent dropEvent;

    @ManagedProperty (value="#{dataBean}")
    private DataBean dataBean;

    public void setDataBean (DataBean dataBean){
        this.dataBean = dataBean;
    }

    public void processDrop(DropEvent event) {
        String value = (String)event.getDragValue();
        dataBean.addDropValues(value);
        dataBean.setPhaseId(event.getPhaseId().toString());
    }


}
