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

package org.richfaces.demo.common.navigation;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DemoDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = 6822187362271025752L;
    private Collection<SampleDescriptor> samples;

    private boolean containsNewSamples() {
        for (SampleDescriptor sample : samples) {
            if (sample.isNewEnabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledSamples() {
        for (SampleDescriptor sample : samples) {
            if (sample.isCurrentlyEnabled()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewItems() {
        return isNewEnabled() || containsNewSamples();
    }

    public boolean hasEnabledItems() {
        return isCurrentlyEnabled() && containsEnabledSamples();
    }

    public SampleDescriptor getSampleById(String id) {
        for (SampleDescriptor sample : getSamples()) {
            if (sample.getId().equals(id)) {
                return sample;
            }
        }
        for (SampleDescriptor sample : getSamples()) {
            if (sample.isCurrentlyEnabled()) {
                return sample;
            }
        }
        // TODO: We should never reach here, perhaps throw an ISE if we do?
        return samples.iterator().next();
    }

    @XmlElementWrapper(name = "samples")
    @XmlElement(name = "sample")
    public Collection<SampleDescriptor> getSamples() {
        if (samples == null) {
            return null;
        }
        return Collections2.filter(samples, new Predicate<SampleDescriptor>() {
            public boolean apply(SampleDescriptor sample) {
                return sample.isCurrentlyEnabled();
            }
        });
    }

    public void setSamples(Collection<SampleDescriptor> samples) {
        this.samples = samples;
    }
}
