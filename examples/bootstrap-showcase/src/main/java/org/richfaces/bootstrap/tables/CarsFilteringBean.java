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

package org.richfaces.bootstrap.tables;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.bootstrap.tables.model.cars.InventoryItem;
import org.richfaces.model.Filter;

@ManagedBean
@ViewScoped
public class CarsFilteringBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5680001353441022183L;
    private String vinFilter;
    private String vendorFilter;
    private String modelFilter;
    private Long mileageFilter;
    private Long priceFilter;

    public Filter<?> getMileageFilterImpl() {
        return new Filter<InventoryItem>() {
            @Override
            public boolean accept(InventoryItem item) {
                Long mileage = getMileageFilter();
                if (mileage == null || mileage == 0 || mileage.compareTo(item.getMileage().longValue()) >= 0) {
                    return true;
                }
                return false;
            }
        };
    }

    public Filter<?> getFilterVendor() {
        return new Filter<InventoryItem>() {
            @Override
            public boolean accept(InventoryItem t) {
                String vendor = getVendorFilter();
                if (vendor == null || vendor.length() == 0 || vendor.equals(t.getVendor())) {
                    return true;
                }
                return false;
            }
        };
    }

    public Long getMileageFilter() {
        return mileageFilter;
    }

    public void setMileageFilter(Long mileageFilter) {
        this.mileageFilter = mileageFilter;
    }

    public String getVendorFilter() {
        return vendorFilter;
    }

    public void setVendorFilter(String vendorFilter) {
        this.vendorFilter = vendorFilter;
    }

    public String getVinFilter() {
        return vinFilter;
    }

    public void setVinFilter(String vinFilter) {
        this.vinFilter = vinFilter;
    }

    public String getModelFilter() {
        return modelFilter;
    }

    public void setModelFilter(String modelFilter) {
        this.modelFilter = modelFilter;
    }

    public Long getPriceFilter() {
        return priceFilter;
    }

    public void setPriceFilter(Long priceFilter) {
        this.priceFilter = priceFilter;
    }
}
