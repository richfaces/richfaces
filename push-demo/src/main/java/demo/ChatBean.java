/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package demo;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean
@SessionScoped
public class ChatBean implements Serializable {
    private static final long serialVersionUID = -6377543444437645751L;
    private static final Logger LOGGER = LogFactory.getLogger(ChatBean.class);
    private String userName;
    private String message;
    private boolean chatJoined;
    private String subchannel;
    @ManagedProperty("#{channelsBean}")
    private ChannelsBean channelsBean;
    @ManagedProperty("#{jmsBean}")
    private JMSBean jmsBean;

    private TopicsContext lookupTopicsContext() {
        return TopicsContext.lookup();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private void sendJMSMessage(TopicKey key, String text) {
        jmsBean.publish(key, text);
    }

    private void sendMessage(TopicKey key, String text) {
        sendJMSMessage(key, text);
        // sendSimpleMessage(key, text);
    }

    private void sendSimpleMessage(TopicKey key, String text) {
        try {
            lookupTopicsContext().publish(key, text);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void publishStateChangeMessage(String name, String action) {
        sendMessage(new TopicKey("chat", name),
                MessageFormat.format("*** {0} {1} chat in {2,time,medium}", userName, action, new Date()));
    }

    public void joinChat() {
        if (!chatJoined) {
            if (userName == null) {
                throw new NullPointerException("username");
            }

            chatJoined = true;

            for (Channel subchannel : channelsBean.getChannels()) {
                publishStateChangeMessage(subchannel.getName(), "joined");
            }
        }
    }

    public void handleStateChange(Channel channel) {
        String action;
        if (channel.isRendered()) {
            action = "joined";
        } else {
            action = "left";
        }

        publishStateChangeMessage(channel.getName(), action);
    }

    public void say() {
        sendMessage(new TopicKey("chat", subchannel),
                MessageFormat.format("{0,time,medium} {1}: {2}", new Date(), userName, message));
    }

    /**
     * @return the subchannel
     */
    public String getSubchannel() {
        return subchannel;
    }

    /**
     * @param subchannel the subchannel to set
     */
    public void setSubchannel(String subchannel) {
        this.subchannel = subchannel;
    }

    /**
     * @param channelsBean the channelsBean to set
     */
    public void setChannelsBean(ChannelsBean channelsBean) {
        this.channelsBean = channelsBean;
    }

    /**
     * @param jmsBean the jmsBean to set
     */
    public void setJmsBean(JMSBean jmsBean) {
        this.jmsBean = jmsBean;
    }
}
