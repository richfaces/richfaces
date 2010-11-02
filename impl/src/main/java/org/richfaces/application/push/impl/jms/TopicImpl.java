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
package org.richfaces.application.push.impl.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.impl.AbstractTopic;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * 
 */
public class TopicImpl extends AbstractTopic {

    static final String SERIALIZED_DATA_INDICATOR = "org_richfaces_push_SerializedData";
    
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    
    private MessagingContext messagingContext;
    
    public TopicImpl(TopicKey key, MessagingContext messagingContext) {
        super(key);
        
        this.messagingContext = messagingContext;
    }

    @Override
    public void publish(String subtopic, Object messageData) throws MessageException {
        String serializedData = getMessageDataSerializer().serialize(messageData);
        
        Session session = null;
        try {
            session = messagingContext.createSession();
            MessageProducer producer = session.createProducer(messagingContext.lookup(getKey()));
            
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(serializedData);
            textMessage.setBooleanProperty(SERIALIZED_DATA_INDICATOR, true);
            
            if (!Strings.isNullOrEmpty(subtopic)) {
                textMessage.setStringProperty(MessagingContext.SUBTOPIC_ATTRIBUTE_NAME, subtopic);
            }
            
            producer.send(textMessage);
        } catch (JMSException e) {
            throw new MessageException(e.getMessage(), e);
        } catch (NamingException e) {
            throw new MessageException(e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }
        }
    }
    
}
