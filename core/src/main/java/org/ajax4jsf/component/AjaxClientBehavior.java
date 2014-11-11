/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.ajax4jsf.component;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.event.AjaxBehaviorListener;

/**
 * @author Anton Belevich interface for our ajax behaviors
 *
 */
public interface AjaxClientBehavior extends ClientBehavior {
    boolean isLimitRender();

    void setLimitRender(boolean limitRender);

    void setExecute(Object execute);

    Object getExecute();

    void setRender(Object render);

    Object getRender();

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

    boolean isResetValues();

    void setResetValues(boolean resetValues);

    void addAjaxBehaviorListener(AjaxBehaviorListener listener);

    void removeAjaxBehaviorListener(AjaxBehaviorListener listener);
}
