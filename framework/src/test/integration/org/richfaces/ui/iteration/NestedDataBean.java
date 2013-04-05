package org.richfaces.ui.iteration;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class NestedDataBean implements Serializable {

    private List<DataItem> listDataItems = new LinkedList<DataItem>() {
        {
            add(new DataItem());
            add(new DataItem());
            add(new DataItem());
        }
    };

    public List<DataItem> getListDataItems() {
        return listDataItems;
    }

    public void setListDataItems(List<DataItem> listDataItems) {
        this.listDataItems = listDataItems;
    }

    public static class DataItem implements Serializable {
        private List<Inner> list = new LinkedList<Inner>() {
            {
                add(new Inner());
            }
        };

        public List<Inner> getList() {
            return list;
        }

        public void setList(List<Inner> list) {
            this.list = list;
        }
    }

    public static class Inner implements Serializable {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}