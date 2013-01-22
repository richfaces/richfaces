package org.ajax4jsf.component;

import java.util.Collection;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.event.AjaxBehaviorListener;

/**
 * @author Anton Belevich interface for our ajax behaviors
 *
 */
public interface AjaxClientBehavior extends ClientBehavior {
    boolean isLimitRender();

    void setLimitRender(boolean limitRender);

    void setExecute(Collection<String> execute);

    Collection<String> getExecute();

    void setRender(Collection<String> render);

    Collection<String> getRender();

    boolean isDisabled();

    void setDisabled(boolean disabled);

    void setQueueId(String queueId);

    String getQueueId();

    void setStatus(String statusId);

    String getStatus();

    String getOnerror();

    void setOnerror(String onerror);

    String getOncomplete();

    void setOncomplete(String oncomplete);

    String getOnbegin();

    void setOnbegin(String onbegin);

    String getOnbeforedomupdate();

    void setOnbeforedomupdate(String onbeforedomupdate);

    String getOnbeforesubmit();

    void setOnbeforesubmit(String onbeforesubmit);

    Object getData();

    void setData(Object data);

    void addAjaxBehaviorListener(AjaxBehaviorListener listener);

    void removeAjaxBehaviorListener(AjaxBehaviorListener listener);
}
