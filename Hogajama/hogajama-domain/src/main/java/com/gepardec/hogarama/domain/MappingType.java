package com.gepardec.hogarama.domain;

/**
 * Defines how sensor Values are mapped to a [0..1] interval.
 * See {@link SensorType} for configuration of sensor types
 */
public enum MappingType {
	
	 /** The values are not mapped, just returned as received */
	IDENTITY(),          
	/** The interval [0..1024] is mapped linear to [0..1] */
	LINEAR1024(1024.0),  
	/** The interval [0..1024] is mapped linear to [1..0] */
	INVERSE_LINEAR1024(1024.0, true);
	
	private boolean identiy = false;
	private double max;
	private boolean inverse;
	
	MappingType(){
		this.identiy  = true;
	}
	MappingType(Double max){
		this.max = max;
	}
	MappingType(Double max, boolean inverse){
		this(max);
		this.inverse = inverse;
	}
	public double map(double value) {
		if ( identiy ) {
			return value;
		}
		double mapped = value / max;
		if (inverse) {
			mapped = 1 - mapped;
		}
		return mapped;
	}
}
