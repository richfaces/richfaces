package org.ajax4jsf.component;

import java.util.Collection;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.event.AjaxBehaviorListener;

/**
 * @author Anton Belevich
 * interface for our ajax behaviors
 *
 */
public interface AjaxClientBehavior extends ClientBehavior {
    public boolean isLimitRender();

    public void setLimitRender(boolean limitRender);

    public void setExecute(Collection<String> execute);

    public Collection<String> getExecute();

    public void setRender(Collection<String> render);

    public Collection<String> getRender();

    public boolean isDisabled();

    public void setDisabled(boolean disabled);

    public void setQueueId(String queueId);

    public String getQueueId();

    public void setStatus(String statusId);

    public String getStatus();

    public String getOnerror();

    public void setOnerror(String onerror);

    public String getOncomplete();

    public void setOncomplete(String oncomplete);

    public String getOnbegin();

    public void setOnbegin(String onbegin);

    public String getOnbeforedomupdate();

    public void setOnbeforedomupdate(String onbeforedomupdate);

    public String getOnbeforesubmit();
    
    public void setOnbeforesubmit(String onbeforesubmit);
    
    public Object getData();
    
    public void setData(Object data);
    
    public void addAjaxBehaviorListener(AjaxBehaviorListener listener);
    
    public void removeAjaxBehaviorListener(AjaxBehaviorListener listener);

}
