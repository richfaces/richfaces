package org.richfaces.demo.tables.model.cars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InventoryVendorList implements Serializable {
    private static final long serialVersionUID = -6547391197128734913L;
    private String vendor;
    private List<InventoryItem> vendorItems;

    public InventoryVendorList() {
        vendorItems = new ArrayList<InventoryItem>();
    }

    public long getCount() {
        if (vendorItems != null) {
            return vendorItems.size();
        } else {
            return 0;
        }
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<InventoryItem> getVendorItems() {
        return vendorItems;
    }

    public void setVendorItems(List<InventoryItem> vendorItems) {
        this.vendorItems = vendorItems;
    }
}
