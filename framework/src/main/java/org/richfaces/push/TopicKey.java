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
package org.richfaces.push;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.richfaces.util.FastJoiner;

import com.google.common.base.Function;

/**
 * <p>
 * Immutable TopicKey class encapsulates address of the particular topic.
 * </p>
 *
 * <p>
 * In order to support reuse of the same topic (e.g. JMS topic) for different application message topics, TopicKey contains
 * topic name and sub-topic name separated with ‘@’.
 * </p>
 *
 * <p>
 * Application specifies TopicKey when publishing messages as topic address.
 * </p>
 *
 * @author Nick Belaevski
 */
public class TopicKey implements Serializable {

    public static final char SUBCHANNEL_SEPARATOR = '@';

    private static final long serialVersionUID = -6967010810728932698L;
    private static final Pattern NAME_PATTERN = Pattern.compile("([a-zA-Z0-9_]+|#\\{.+\\})");
    private static final FastJoiner AT_JOINER = FastJoiner.on(SUBCHANNEL_SEPARATOR);

    private final String topicName;
    private final String subtopicName;

    /**
     * Constructs new topic by providing a topic address in format 'subtopic@topic'
     */
    public TopicKey(String topicAddress) {
        this(getTopicName(topicAddress), getSubtopicName(topicAddress));
    }

    /**
     * Contrstructs new topic by providing a name of the topic and a name of a subtopic.
     */
    public TopicKey(String topicName, String subtopicName) {
        super();

        if (topicName == null) {
            throw new NullPointerException();
        }

        this.topicName = topicName;
        this.subtopicName = subtopicName;

        if (!NAME_PATTERN.matcher(topicName).matches()) {
            throw new IllegalArgumentException("Topic name '" + topicName + "' does not match pattern "
                    + NAME_PATTERN.pattern());
        }

        if (subtopicName != null && !NAME_PATTERN.matcher(subtopicName).matches()) {
            throw new IllegalArgumentException("Subtopic name '" + subtopicName + "' does not match pattern "
                    + NAME_PATTERN.pattern());
        }
    }

    /**
     * Returns name of the topic
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * Returns name of the subtopic
     */
    public String getSubtopicName() {
        return subtopicName;
    }

    /**
     * Returns whole address of topic in format 'subtopic@topic'
     */
    public String getTopicAddress() {
        return AT_JOINER.join(subtopicName, topicName);
    }

    /**
     * <p>
     * Returns the {@link TopicKey} that identifies topic without subtopic.
     * </p>
     *
     * <p>
     * This method returns this instance if it isn't associated with subtopic. It returns new {@link TopicKey} with the same
     * topic name as this instance otherwise.
     * </p>
     */
    public TopicKey getRootTopicKey() {
        if (getSubtopicName() == null) {
            return this;
        } else {
            return new TopicKey(getTopicName(), null);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subtopicName == null) ? 0 : subtopicName.hashCode());
        result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TopicKey[" + TO_ADDRESS.apply(this) + "]";
    }

    /**
     * Static method that returns topic name for given topic address, i.e. it removes subtopic name and separated of subtopic.
     * name.
     */
    private static String getTopicName(String topicAddress) {
        int idx = topicAddress.indexOf(SUBCHANNEL_SEPARATOR);

        if (idx < 0) {
            return topicAddress;
        }

        return topicAddress.substring(idx + 1);
    }

    /**
     * Static method that returns subtopic name for given topic address, i.e. it removes topic name and separator of topic name.
     */
    private static String getSubtopicName(String topicAddress) {
        int idx = topicAddress.indexOf(SUBCHANNEL_SEPARATOR);

        if (idx < 0) {
            return null;
        }

        return topicAddress.substring(0, idx);
    }

    /**
     * Returns the function for creating {@link TopicKey}s from strings.
     */
    public static Function<String, TopicKey> factory() {
        return FACTORY;
    }

    /**
     * Returns a function for converting {@link TopicKey} to address strings in format 'subtopic@topic'.
     */
    public static Function<TopicKey, String> toAddress() {
        return TO_ADDRESS;
    }

    private static final Function<String, TopicKey> FACTORY = new Function<String, TopicKey>() {
        public TopicKey apply(String from) {
            return new TopicKey(from);
        }
    };

    private static final Function<TopicKey, String> TO_ADDRESS = new Function<TopicKey, String>() {
        public String apply(TopicKey from) {
            return from.getTopicAddress();
        }
    };
}
