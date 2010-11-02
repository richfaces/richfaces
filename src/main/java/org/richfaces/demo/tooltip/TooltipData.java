package org.richfaces.demo.tooltip;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean
@ViewScoped
public class TooltipData {
	private int tooltipCounter = 0;

	public int getTooltipCounter() {
		return tooltipCounter++;
	}
	public Date getTooltipDate() {
		return new Date();
	}

} 
