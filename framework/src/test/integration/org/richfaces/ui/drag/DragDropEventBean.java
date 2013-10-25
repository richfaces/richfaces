package org.richfaces.ui.drag;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.richfaces.ui.drag.dropTarget.DropEvent;
import org.richfaces.ui.drag.dropTarget.DropListener;

@ManagedBean
@RequestScoped
public class DragDropEventBean implements DropListener {

    @ManagedProperty(value = "#{dragDropBean}")
    private DragDropBean dragDropBean;

    public void setDragDropBean(DragDropBean dragDropBean) {
        this.dragDropBean = dragDropBean;
    }

    @Override
    public void processDrop(DropEvent event) {
        dragDropBean.moveFramework((Framework) event.getDragValue());
    }
}