package org.richfaces.demo.push;

import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Sends message to topic using TopicsContext.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class TopicsContextMessageProducer implements MessageProducer {

    public static final String PUSH_TOPICS_CONTEXT_TOPIC = "pushTopicsContext";

    private Logger log = RichfacesLogger.WEBAPP.getLogger();

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#sendMessage()
     */
    public void sendMessage() throws Exception {
        try {
            TopicKey topicKey = new TopicKey(PUSH_TOPICS_CONTEXT_TOPIC);
            TopicsContext topicsContext = TopicsContext.lookup();
            topicsContext.publish(topicKey, "message");
        } catch (Exception e) {
            log.info("Sending push message using TopicContext failed (" + e.getMessage()
                    + ") - operation will be repeated in few seconds");
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#getInterval()
     */
    public int getInterval() {
        return 5000;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#finalizeProducer()
     */
    public void finalizeProducer() {
    }
}
