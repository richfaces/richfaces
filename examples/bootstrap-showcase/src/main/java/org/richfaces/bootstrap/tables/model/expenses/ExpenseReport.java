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

package org.richfaces.bootstrap.tables.model.expenses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpenseReport {
    private List<ExpenseReportRecord> records = null;

    public List<ExpenseReportRecord> getRecords() {
        if (records == null) {
            initRecords();
        }
        return records;
    }

    public void setRecords(List<ExpenseReportRecord> records) {
        this.records = records;
    }

    public double getTotalMeals() {
        double ret = 0.0;
        Iterator<ExpenseReportRecord> it = getRecords().iterator();
        while (it.hasNext()) {
            ExpenseReportRecord record = it.next();
            ret += record.getTotalMeals();
        }
        return ret;
    }

    public double getTotalHotels() {
        double ret = 0.0;
        Iterator<ExpenseReportRecord> it = getRecords().iterator();
        while (it.hasNext()) {
            ExpenseReportRecord record = it.next();
            ret += record.getTotalHotels();
        }
        return ret;
    }

    public double getTotalTransport() {
        double ret = 0.0;
        Iterator<ExpenseReportRecord> it = getRecords().iterator();
        while (it.hasNext()) {
            ExpenseReportRecord record = it.next();
            ret += record.getTotalTransport();
        }
        return ret;
    }

    public double getGrandTotal() {
        return getTotalMeals() + getTotalHotels() + getTotalTransport();
    }

    public int getRecordsCount() {
        return getRecords().size();
    }

    private void initRecords() {
        records = new ArrayList<ExpenseReportRecord>();
        ExpenseReportRecord rec;
        rec = new ExpenseReportRecord();
        rec.setCity("San Jose");
        rec.getItems().add(new ExpenseReportRecordItem("25-Aug-97", 37.74, 112.0, 45.0, "San Jose"));
        rec.getItems().add(new ExpenseReportRecordItem("26-Aug-97", 27.28, 112.0, 45.0, "San Jose"));
        records.add(rec);
        rec = new ExpenseReportRecord();
        rec.setCity("Seattle");
        rec.getItems().add(new ExpenseReportRecordItem("27-Aug-97", 96.25, 109.0, 36.00, "Seattle"));
        rec.getItems().add(new ExpenseReportRecordItem("28-Aug-97", 35.0, 109.0, 36.0, "Seattle"));
        records.add(rec);
    }
}
