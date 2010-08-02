/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.demo;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Nick Belaevski
 * 
 */
@ManagedBean
@SessionScoped
public class RepeatBean implements Serializable {

    private static final long serialVersionUID = -4468004449310935853L;

    public static final class MatrixCell implements Serializable {
        
        private static final long serialVersionUID = -5911659561854593681L;
        
        private int value = 0;
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
        
        public void clearValueAction() {
            setValue(0);
        }
        
        public void increaseValueAction() {
            value++;
        }

        public void decreaseValueAction() {
            value--;
        }
    }

    public static final class MatrixRow implements Serializable {
        
        private static final long serialVersionUID = -5051037819565283283L;
        
        private List<MatrixCell> cells = new ArrayList<MatrixCell>();
        
        public List<MatrixCell> getCells() {
            return cells;
        }
        
        public void addCell(MatrixCell cell) {
            cells.add(cell);
        }
    }
    
    public static final class Data implements Serializable {
        
        private static final long serialVersionUID = -1461777632529492912L;
        
        private String text;
        
        /**
         * @return the text
         */
        public String getText() {
            return text;
        }
        
        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }
        
    }
    
    private static final int MATRIX_DIMENSION = 4;
    
    private List<Data> dataList;
    
    private Data selectedDataItem = null;
    
    private List<MatrixRow> matrixRows;
    
    public RepeatBean() {
        dataList = new ArrayList<Data>();
        
        for (int i = 0; i < 10; i++) {
            Data data = new Data();
            data.setText(MessageFormat.format("Item {0}", i));
            dataList.add(data);
        }
        
        matrixRows = new ArrayList<MatrixRow>();
        
        for (int i = 0; i < MATRIX_DIMENSION; i++) {
            MatrixRow matrixRow = new MatrixRow();

            for (int j = 0; j < MATRIX_DIMENSION; j++) {
                MatrixCell matrixCell = new MatrixCell();
                matrixRow.addCell(matrixCell);
            }
            
            matrixRows.add(matrixRow);
        }
    }
    
    /**
     * @return the data
     */
    public List<Data> getDataList() {
        return dataList;
    }

    /**
     * @return the selectedDataItem
     */
    public Data getSelectedDataItem() {
        return selectedDataItem;
    }
    
    /**
     * @param selectedDataItem the selectedDataItem to set
     */
    public void setSelectedDataItem(Data selectedDataItem) {
        this.selectedDataItem = selectedDataItem;
    }
    
    public List<MatrixRow> getMatrixRows() {
        return matrixRows;
    }
}
