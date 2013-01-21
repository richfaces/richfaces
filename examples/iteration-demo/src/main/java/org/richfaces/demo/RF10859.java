package org.richfaces.demo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf10859")
@SessionScoped
public class RF10859 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DataItem> listDataItems = new LinkedList<DataItem>() {
        private static final long serialVersionUID = 1L;

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
        private static final long serialVersionUID = 1L;

        private List<Inner> list = new LinkedList<Inner>() {
            private static final long serialVersionUID = 1L;

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
        private static final long serialVersionUID = 1L;

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
