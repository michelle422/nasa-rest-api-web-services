package com.michelle.nasa.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Asteroid {
	
	private String id;
	private String name;
	private BigDecimal estimatedDiameter;
	private LocalDate closeApproachDate;
	private double missDistance;
	
	public Asteroid() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getEstimatedDiameter() {
		return estimatedDiameter;
	}

	public void setEstimatedDiameter(BigDecimal estimatedDiameter) {
		this.estimatedDiameter = estimatedDiameter;
	}

	public LocalDate getCloseApproachDate() {
		return closeApproachDate;
	}

	public void setCloseApproachDate(LocalDate closeApproachDate) {
		this.closeApproachDate = closeApproachDate;
	}

	public double getMissDistance() {
		return missDistance;
	}

	public void setMissDistance(double missDistance) {
		this.missDistance = missDistance;
	}
	

}
