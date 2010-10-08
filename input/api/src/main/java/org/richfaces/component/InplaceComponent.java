package org.richfaces.component;


public interface InplaceComponent {
    
    public String getDefaultLabel();
    
    public String getEditEvent();
    
    public boolean isShowControls();
    
    public boolean isSaveOnBlur();
    
    public InplaceState getState();

}
