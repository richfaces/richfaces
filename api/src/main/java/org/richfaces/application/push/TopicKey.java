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
package org.richfaces.application.push;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.richfaces.util.FastJoiner;

import com.google.common.base.Function;

/**
 * @author Nick Belaevski
 *
 */
public class TopicKey implements Serializable {
    public static final char SUBCHANNEL_SEPARATOR = '@';
    private static final Function<String, TopicKey> FACTORY = new Function<String, TopicKey>() {
        public TopicKey apply(String from) {
            return new TopicKey(from);
        }

        ;
    };
    private static final Function<TopicKey, String> TO_ADDRESS = new Function<TopicKey, String>() {
        public String apply(TopicKey from) {
            return from.getTopicAddress();
        }

        ;
    };
    private static final long serialVersionUID = -6967010810728932698L;
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");
    private static final FastJoiner AT_JOINER = FastJoiner.on(SUBCHANNEL_SEPARATOR);
    private final String topicName;
    private final String subtopicName;

    public TopicKey(String topicAddress) {
        this(getTopicName(topicAddress), getSubtopicName(topicAddress));
    }

    public TopicKey(String topicName, String subtopicName) {
        super();

        if (topicName == null) {
            throw new NullPointerException();
        }

        this.topicName = topicName;
        this.subtopicName = subtopicName;

        if (!NAME_PATTERN.matcher(topicName).matches()) {
            throw new IllegalArgumentException(topicName);
        }

        if (subtopicName != null && !NAME_PATTERN.matcher(subtopicName).matches()) {
            throw new IllegalArgumentException(subtopicName);
        }
    }

    public static Function<String, TopicKey> factory() {
        return FACTORY;
    }

    public static Function<TopicKey, String> toAddress() {
        return TO_ADDRESS;
    }

    private static String getTopicName(String topicAddress) {
        int idx = topicAddress.indexOf(SUBCHANNEL_SEPARATOR);

        if (idx < 0) {
            return topicAddress;
        }

        return topicAddress.substring(idx + 1);
    }

    private static String getSubtopicName(String topicAddress) {
        int idx = topicAddress.indexOf(SUBCHANNEL_SEPARATOR);

        if (idx < 0) {
            return null;
        }

        return topicAddress.substring(0, idx);
    }

    public String getTopicName() {
        return topicName;
    }

    public String getSubtopicName() {
        return subtopicName;
    }

    public String getTopicAddress() {
        return AT_JOINER.join(subtopicName, topicName);
    }

    public TopicKey getRootTopicKey() {
        if (getSubtopicName() == null) {
            return this;
        } else {
            return new TopicKey(getTopicName(), null);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subtopicName == null) ? 0 : subtopicName.hashCode());
        result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TopicKey other = (TopicKey) obj;
        if (subtopicName == null) {
            if (other.subtopicName != null) {
                return false;
            }
        } else if (!subtopicName.equals(other.subtopicName)) {
            return false;
        }
        if (topicName == null) {
            if (other.topicName != null) {
                return false;
            }
        } else if (!topicName.equals(other.topicName)) {
            return false;
        }
        return true;
    }
}
