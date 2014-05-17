package com.dubaidrums.jems.util.dashboard;

import org.joda.time.LocalDate;

public class QIData {
	private LocalDate date;
	private Double total;
	
	public QIData(LocalDate current, Double d) {
		this.date = current;
		this.total = d;
	}
	
	public void add(Double d){
		total += d;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
