package com.dubaidrums.jems.util.dashboard;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class QIData {
	private LocalDate date;
	private BigDecimal total;

	public QIData(LocalDate current, BigDecimal d) {
		this.date = current;
		this.total = d;
	}

	public void add(BigDecimal d) {
		total = total.add(d);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
