package org.richfaces.demo.common.navigation;

import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DemoDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = 6822187362271025752L;
    private static final String BASE_SAMPLES_DIR = "/richfaces/";
    private List<SampleDescriptor> samples;
    private List<SampleDescriptor> filteredSamples;

    private boolean containsNewSamples() {
        for (SampleDescriptor sample : samples) {
            if (sample.isNewItem()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledSamples(FacesContext facesContext) {
        for (SampleDescriptor sample : samples) {
            if (sample.isEnabled(facesContext)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsEnabledMobileSamples(FacesContext facesContext) {
        for (SampleDescriptor sample : samples) {
            if (sample.isEnabled(facesContext) && !sample.isMobileExclude()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewItems() {
        return (isNewItem() || containsNewSamples());
    }

    public boolean hasEnabledItems(FacesContext facesContext) {
        return containsEnabledSamples(facesContext);
    }

    public boolean hasEnabledMobileItems(FacesContext facesContext) {
        return this.isEnabled(facesContext) && containsEnabledMobileSamples(facesContext);
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
                    return sample.isEnabled(FacesContext.getCurrentInstance());
                }
            }));
        }
        return filteredSamples;
    }

    public void setSamples(List<SampleDescriptor> samples) {
        this.samples = samples;
    }
}
