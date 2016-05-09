package org.richfaces.demo.common.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@ManagedBean
@SessionScoped
public class DemoNavigator implements Serializable {
    private static final long serialVersionUID = 3970933260901989658L;

    public static final String MOBILE_SUPPORT_PARAM = "org.richfaces.request.mobile";
    private static final String DEMO_VIEW_PARAMETER = "demo";
    private static final String SAMPLE_VIEW_PARAMETER = "sample";
    private static final String SEPARATOR = "/";
    private static final String SAMPLE_PREFIX = "-sample";
    private static final String SAMPLES_FOLDER = "samples/";
    @ManagedProperty(value = "#{navigationParser.groupsList}")
    private List<GroupDescriptor> groups;
    private DemoDescriptor currentDemo;
    private SampleDescriptor currentSample;
    private String sample;
    private String demo;

    @PostConstruct
    public void init() {
        currentDemo = null;
        currentSample = null;
        groups = new ArrayList<GroupDescriptor>(Collections2.filter(groups, new Predicate<GroupDescriptor>() {
            public boolean apply(GroupDescriptor input) {
                return input.hasEnabledItems();
            }
        }));
    }

    public DemoDescriptor getCurrentDemo() {
        String id = getViewParameter(DEMO_VIEW_PARAMETER);
        if (currentDemo == null || !currentDemo.getId().equals(id)) {
            if (id != null) {
                currentDemo = findDemoById(id);
                currentSample = null;
            }
            if (currentDemo == null) {
                currentDemo = groups.iterator().next().getDemos().iterator().next();
                currentSample = null;
            }
        }
        return currentDemo;
    }

    public SampleDescriptor getCurrentSample() {
        String id = getViewParameter(SAMPLE_VIEW_PARAMETER);
        if (currentSample == null || !currentSample.getId().equals(id)) {
            if (id != null) {
                currentSample = getCurrentDemo().getSampleById(id);
            }
            if (currentSample == null) {
                currentSample = getCurrentDemo().getSamples().iterator().next();
            }
        }
        return currentSample;
    }

    private String getViewParameter(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String param = (String) fc.getExternalContext().getRequestParameterMap().get(name);
        if (param != null && param.trim().length() > 0) {
            return param;
        } else {
            return null;
        }
    }

    public DemoDescriptor findDemoById(String id) {
        Iterator<GroupDescriptor> it = groups.iterator();
        while (it.hasNext()) {
            GroupDescriptor group = it.next();
            Iterator<DemoDescriptor> dit = group.getDemos().iterator();
            while (dit.hasNext()) {
                DemoDescriptor locDemo = (DemoDescriptor) dit.next();
                if (locDemo.getId().equals(id)) {
                    return locDemo;
                }
            }
        }
        return null;
    }

    public String getSampleURI() {
        FacesContext context = FacesContext.getCurrentInstance();

        NavigationHandler handler = context.getApplication().getNavigationHandler();

        if (handler instanceof ConfigurableNavigationHandler) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) handler;

            demo = getCurrentDemo().getId();
            sample = getCurrentSample().getId();

            NavigationCase navCase = navigationHandler.getNavigationCase(context, null, "/richfaces" + SEPARATOR
                    + getCurrentDemo().getId() + SEPARATOR + getCurrentSample().getId());

            if (navCase == null) {
                navCase = new NavigationCase("/index.xhtml", null, null, null, "/richfaces/ajax/ajax.xhtml", null, false,
                        true);
            }

            return navCase.getToViewId(context);

        }

        return null;
    }

    /**
     * @return actual sample inclusion src Consider that: 1) all the samples should be placed in "samples" subfolder of the
     *         actual sample 2) all the samples pages should use the same name as main sample page with "-sample" prefix
     */
    public String getSampleIncludeURI() {
        String sampleURI = getSampleURI();
        StringBuffer sampleURIBuffer = new StringBuffer(sampleURI);
        int folderOffset = sampleURIBuffer.lastIndexOf(currentSample.getId());
        int fileNameOffset = sampleURIBuffer.lastIndexOf(currentSample.getId()) + currentSample.getId().length()
                + SAMPLE_PREFIX.length() + 1;
        String result = new StringBuffer(sampleURI).insert(folderOffset, SAMPLES_FOLDER).insert(fileNameOffset, SAMPLE_PREFIX)
                .toString();
        return result;
    }

    public List<GroupDescriptor> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDescriptor> groups) {
        this.groups = groups;
    }

    public void getFilteredGroups() {

    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
