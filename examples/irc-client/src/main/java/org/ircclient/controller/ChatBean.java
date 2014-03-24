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

package org.ircclient.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

@ManagedBean
@SessionScoped
public class ChatBean extends PircBot implements Serializable {
    private static final long serialVersionUID = -4945680041914388092L;
    private static final String SERVER_URL = "irc.freenode.org";
    private static final int SERVER_PORT = 6667;
    private static final String CHANNEL_PREFIX = "#";
    private static final String SUBTOPIC_SEPARATOR = "_";
    private static final String DEFAULT_CHANNEL = "richfaces";
    private static final Logger LOGGER = LogFactory.getLogger(ChatBean.class);
    private String channelName;
    private String message;
    private transient TopicsContext topicsContext;

    public String connect() {
        try {
            this.connect(SERVER_URL, SERVER_PORT);
            this.joinChannel(CHANNEL_PREFIX + DEFAULT_CHANNEL);
            channelName = DEFAULT_CHANNEL;
        } catch (NickAlreadyInUseException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getName() + " nick already in use", this.getName()
                            + " nick already in use"));
            return null;
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, server unresponsible. Try again later.",
                            "Sorry, server unresponsible. Try again later."));
            return null;
        } catch (IrcException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Sorry, we encountered IRC services problems. Try again later.",
                            "Sorry, we encountered IRC services problems. Try again later."));
            return null;
        }
        return "chat";
    }

    public String leave() {
        this.disconnect();
        return "welcome";
    }

    private TopicsContext getTopicsContext() {
        if (topicsContext == null) {
            topicsContext = TopicsContext.lookup();
        }
        return topicsContext;
    }

    public String getMessagesSubtopic() {
        return this.getUserName() + SUBTOPIC_SEPARATOR + channelName;
    }

    public String getListSubtopic() {
        return this.getUserName() + SUBTOPIC_SEPARATOR + channelName + "List";
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        try {
            Message messageObject = new Message(message, sender, DateFormat.getInstance().format(new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        try {
            getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        try {
            getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
            Message messageObject = new Message("joined channel", sender, DateFormat.getInstance().format(new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        try {
            getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
            Message messageObject = new Message("left channel", sender, DateFormat.getInstance().format(new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        try {
            getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
            Message messageObject = new Message(" changed nick to " + newNick, oldNick, DateFormat.getInstance().format(
                    new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        try {
            getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
            Message messageObject = new Message("left channel" + reason, sourceNick, DateFormat.getInstance()
                    .format(new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public User[] getUsers() {
        return this.getUsers(CHANNEL_PREFIX + channelName);
    }

    public void send() {
        this.sendMessage(CHANNEL_PREFIX + channelName, message);
        try {
            Message messageObject = new Message(message, this.getName(), DateFormat.getInstance().format(new Date()));
            getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void changeNick(ValueChangeEvent event) {
        this.changeNick((String) event.getNewValue());
    }

    public String getServerName() {
        return SERVER_URL;
    }

    public void setUserName(String userName) {
        this.setName(userName);
    }

    public String getUserName() {
        if (this.getName().equalsIgnoreCase("PircBot")) {
            return "";
        } else {
            return this.getName();
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
