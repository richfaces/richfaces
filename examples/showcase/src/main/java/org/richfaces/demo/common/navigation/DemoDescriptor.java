package org.richfaces.demo.common.navigation;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DemoDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = 6822187362271025752L;
    private Collection<SampleDescriptor> samples;
    private Boolean excludeReference = false;

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

    @XmlAttribute(name = "excludeReference")
    public boolean isExcludeReference() {
        return excludeReference;
    }

    public void setExcludeReference(boolean excludeReference) {
        this.excludeReference = excludeReference;
    }

}
