package org.richfaces.demo.common.navigation;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DemoDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = 6822187362271025752L;
    private List<SampleDescriptor> samples;
    private List<SampleDescriptor> filteredSamples;

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
        return samples.get(0);
    }

    @XmlElementWrapper(name = "samples")
    @XmlElement(name = "sample")
    public List<SampleDescriptor> getSamples() {
        if (samples == null) {
            return null;
        }
        if (filteredSamples == null) {
            filteredSamples = new LinkedList<SampleDescriptor>(Collections2.filter(samples, new Predicate<SampleDescriptor>() {
                public boolean apply(SampleDescriptor sample) {
                    return sample.isCurrentlyEnabled();
                }
            }));
        }
        return filteredSamples;
    }

    public void setSamples(List<SampleDescriptor> samples) {
        this.samples = samples;
    }
}
