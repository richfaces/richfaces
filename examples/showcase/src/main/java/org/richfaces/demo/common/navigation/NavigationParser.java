package org.richfaces.demo.common.navigation;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@ManagedBean
@ApplicationScoped
public class NavigationParser {
    private List<GroupDescriptor> groupsList;

    @XmlRootElement(name = "root")
    private static final class GroupsHolder {
        private List<GroupDescriptor> groups;

        @XmlElement(name = "group")
        public List<GroupDescriptor> getGroups() {
            return groups;
        }

        @SuppressWarnings("unused")
        public void setGroups(List<GroupDescriptor> groups) {
            this.groups = groups;
        }
    }

    public synchronized List<GroupDescriptor> getGroupsList() {
        if (groupsList == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            URL resource = ccl.getResource("org/richfaces/demo/data/common/navigation.xml");
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(GroupsHolder.class);
                GroupsHolder groupsHolder = (GroupsHolder) context.createUnmarshaller().unmarshal(resource);
                groupsList = groupsHolder.getGroups();
            } catch (JAXBException e) {
                throw new FacesException(e.getMessage(), e);
            }
        }

        return groupsList;
    }
}
