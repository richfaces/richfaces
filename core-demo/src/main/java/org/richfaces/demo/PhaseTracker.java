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
package org.richfaces.demo;

import java.text.MessageFormat;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 * 
 */
public class PhaseTracker implements PhaseListener {

    private static final long serialVersionUID = 6358081870120864332L;

    private Logger logger = LogFactory.getLogger(PhaseTracker.class);

    private ThreadLocal<Long> phaseTimer = new ThreadLocal<Long>();
    
    public void afterPhase(PhaseEvent event) {
        Long phaseStartTime = phaseTimer.get();
        
        long measuredTime = 0;
        if (phaseStartTime != null) {
            measuredTime = System.currentTimeMillis() - phaseStartTime.longValue();
        }
        
        logger.debug(MessageFormat.format("Phase {0} completed by {1}ms", event.getPhaseId(), measuredTime));
        phaseTimer.set(null);
    }

    public void beforePhase(PhaseEvent event) {
        logger.debug(MessageFormat.format("Phase {0} started", event.getPhaseId()));
        phaseTimer.set(System.currentTimeMillis());
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
