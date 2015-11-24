/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and individual contributors
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
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import org.richfaces.application.push.MessageException;

@Named("richBean")
@SessionScoped
public class RichBean implements Serializable {

    // jms-destinations > jms-topic name
    private static final String JMS_ADDRESS = "jmsSampleAddress";
    // jms-destinations > jms-topic > entry name
    private static final String JMS_ADDRESS_JNDI = "topic/jmsSampleAddress";

    private static final long serialVersionUID = 1L;

    @Inject
    private transient JMSContext jmsContext;

    @Resource(mappedName = JMS_ADDRESS_JNDI)
    private Topic jmsTopic1;

    private boolean pushEnabled;

    public String getAddress() {
        return JMS_ADDRESS;
    }

    public Date getDate() {
        return new Date();
    }

    public JMSContext getJmsContext() {
        return jmsContext;
    }

    public Topic getJmsTopic1() {
        return jmsTopic1;
    }

    @PostConstruct
    public void init() {
        pushEnabled = true;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void push() throws MessageException {
        ObjectMessage msg = jmsContext.createObjectMessage(new Date().toString());
        jmsContext.createProducer().send(jmsTopic1, msg);
    }

    public void setJmsContext(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    public void setJmsTopic1(Topic jmsTopic1) {
        this.jmsTopic1 = jmsTopic1;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }
}
