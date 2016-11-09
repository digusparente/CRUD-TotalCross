package com.totalcross.Model;

import totalcross.util.Date;

public class Car {
	
	private String name;
	private String model;
	private Date year;
	
	/* getters and setters */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
		this.year = year;
	}
	
	/* toString */

	@Override
	public String toString() {
		return "Car [name=" + name + ", model=" + model + ", year=" + year + "]";
	}
}
