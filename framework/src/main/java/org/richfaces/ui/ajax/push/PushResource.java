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
package org.richfaces.ui.ajax.push;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.javascript.ScriptUtils;
import org.richfaces.push.PushContext;
import org.richfaces.push.PushContextFactory;
import org.richfaces.push.Session;
import org.richfaces.push.TopicKey;
import org.richfaces.resource.AbstractUserResource;
import org.richfaces.resource.DynamicResource;
import org.richfaces.services.ServiceTracker;

/**
 * PushResource is RichFaces dynamic resource responsible for push sessions management.
 *
 * @author Nick Belaevski
 */
@DynamicResource
public class PushResource extends AbstractUserResource {
    private static final String PUSH_TOPIC_PARAM = "pushTopic";
    private static final String FORGET_PUSH_SESSION_ID_PARAM = "forgetPushSessionId";

    private Map<String, String> getFailuresMap(Map<TopicKey, String> failedSubscriptions) {
        Map<String, String> result = new HashMap<String, String>();

        for (Entry<TopicKey, String> entry : failedSubscriptions.entrySet()) {
            result.put(entry.getKey().getTopicAddress(), entry.getValue());
        }

        return result;
    }

    public void encode(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();

        PushContextFactory pushContextFactory = ServiceTracker.getService(PushContextFactory.class);

        // resource plugin stub
        if (pushContextFactory == null) {
            return;
        }

        PushContext pushContext = pushContextFactory.getPushContext();

        String forgetPushSessionId = externalContext.getRequestParameterMap().get(FORGET_PUSH_SESSION_ID_PARAM);
        if (forgetPushSessionId != null) {
            Session oldSession = pushContext.getSessionManager().getPushSession(forgetPushSessionId);
            if (oldSession != null) {
                oldSession.invalidate();
            }
        }

        Session session = pushContext.getSessionFactory().createSession(UUID.randomUUID().toString());

        String[] topicNames = externalContext.getRequestParameterValuesMap().get(PUSH_TOPIC_PARAM);

        if (topicNames == null) {
            throw new IllegalArgumentException(PUSH_TOPIC_PARAM + " request parameter must be present");
        }

        session.subscribe(topicNames);

        Map<String, Object> subscriptionData = new HashMap<String, Object>(4);
        subscriptionData.put("sessionId", session.getId());

        Map<TopicKey, String> failedSubscriptions = session.getFailedSubscriptions();
        subscriptionData.put("failures", getFailuresMap(failedSubscriptions));

        Writer outWriter = facesContext.getExternalContext().getResponseOutputWriter();
        ScriptUtils.appendScript(outWriter, subscriptionData);
    }

    public String getContentType() {
        return "text/javascript; charset=utf-8";
    }
}
