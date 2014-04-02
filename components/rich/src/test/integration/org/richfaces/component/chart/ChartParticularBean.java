package org.richfaces.component.chart;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.ui.output.chart.ChartDataModel.ChartType;
import org.richfaces.ui.output.chart.NumberChartDataModel;
import org.richfaces.ui.output.chart.PlotClickEvent;

@ManagedBean
@ViewScoped
public class ChartParticularBean {

    NumberChartDataModel amodel;
    NumberChartDataModel bmodel;
    String serverMsg;

    @PostConstruct
    public void init(){
        serverMsg = "-";
        amodel = new NumberChartDataModel(ChartType.line);
        amodel.put(1, 1);
        amodel.put(2, 2);
        amodel.put(3, 3);
        amodel.put(3, 4);

        bmodel = new NumberChartDataModel(ChartType.line);
        bmodel.put(1, 3);
        bmodel.put(2, 4);
        bmodel.put(3, 5);
    }

    public void aSeriesHandler(PlotClickEvent event){
        serverMsg = "a-series";
    }

    public NumberChartDataModel getAmodel() {
        return amodel;
    }
    public NumberChartDataModel getBmodel() {
        return bmodel;
    }

    public String getServerMsg() {
        return serverMsg;
    }
    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }
}
