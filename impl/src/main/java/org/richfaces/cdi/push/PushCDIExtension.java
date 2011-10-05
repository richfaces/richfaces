/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.cdi.push;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

/**
 * <p>
 * CDI Extension for observing CDI events and delegating events with their payload to Push message bus.
 * </p>
 *
 * <p>
 * This extension was introduced as workaround for feature missing in CDI 1.0 (<a
 * href="https://issues.jboss.org/browse/CDI-36">CDI-36</a>).
 * </p>
 *
 * <p>
 * Thus this extension listens on injection target scanning process for all {@link Push} annotations defined on injection points
 * and then registers observer methods designed for one specific topic. As consequence is that this implementation can't be used
 * for observing dynamically created topic names.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class PushCDIExtension implements Extension {

    private Set<Push> pushAnnotations = new LinkedHashSet<Push>();

    /**
     * Scans all the injection points on found injection targets for {@link Push} annotations
     */
    public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit) {
        scanForPushAnnotations(pit.getInjectionTarget().getInjectionPoints());
    }

    /**
     * Stores all {@link Push} annotations on injection points annotated by {@link Push}
     *
     * @param injectionPoints injection points to be scanned
     */
    private void scanForPushAnnotations(Collection<InjectionPoint> injectionPoints) {
        for (InjectionPoint injectionPoint : injectionPoints) {
            Push pushAnnotation = injectionPoint.getAnnotated().getAnnotation(Push.class);
            if (pushAnnotation != null) {
                pushAnnotations.add(pushAnnotation);
            }
        }
    }

    /**
     * Register observer method {@link PushObserverMethod} for each {@link Push} annotation found in annotation scanning.
     *
     * @param event
     * @param beanManager
     */
    private void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        for (Push pushAnnotation : pushAnnotations) {
            event.addObserverMethod(new PushObserverMethod(beanManager, pushAnnotation));
        }
    }

    /**
     * <p>
     * This class remembers {@link BeanManager} and {@link Push} annotation.
     * </p>
     *
     * <p>
     * {@link BeanManager} is needed to get reference to {@link TopicKeyResolver} and {@link TopicsContext}.
     * </p>
     *
     * <p>
     * {@link TopicKeyResolver} is then used to resolve {@link TopicKey} from {@link Push} annotation.
     * </p>
     *
     * <p>
     * {@link TopicsContext} is used for publishing into RichFaces Push message bus.
     * </p>
     *
     * @author lfryc
     *
     */
    private static class PushObserverMethod implements ObserverMethod<Object> {

        private BeanManager beanManager;
        private Push pushAnnotation;

        public PushObserverMethod(BeanManager beanManager, Push pushAnnotation) {
            this.beanManager = beanManager;
            this.pushAnnotation = pushAnnotation;
        }

        /**
         * Publishes message to topic determined by {@link TopicKey} using {@link TopicsContext}.
         *
         * References to {@link TopicsContext} and {@link TopicKeyResolver} (needed for {@link TopicKey} resolution) are done
         * through {@link BeanManager}.
         */
        public void notify(Object payload) {
            TopicsContext topicsContext = getBeanReference(TopicsContext.class);
            TopicKeyResolver topicKeyResolver = getBeanReference(TopicKeyResolver.class);
            TopicKey topicKey = topicKeyResolver.resolveTopicKey(pushAnnotation);
            try {
                topicsContext.publish(topicKey, payload);
            } catch (MessageException e) {
                new PushCDIMessageException("Message wasn't successfully deliver", e);
            }
        }

        public Class<?> getBeanClass() {
            return PushObserverMethod.class;
        }

        public Type getObservedType() {
            return Object.class;
        }

        public Set<Annotation> getObservedQualifiers() {
            return new HashSet<Annotation>(Arrays.asList(pushAnnotation));
        }

        public Reception getReception() {
            return null;
        }

        public TransactionPhase getTransactionPhase() {
            return TransactionPhase.IN_PROGRESS;
        }

        private <T> T getBeanReference(Class<T> beanType) {
            Set<Bean<?>> beans = beanManager.getBeans(beanType);
            Bean<?> bean = (Bean<?>) beans.iterator().next();
            CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
            T reference = (T) beanManager.getReference(bean, beanType, ctx);
            return reference;
        }
    }
}
