package com.gepardec.hogarama.domain.sensor;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("habarama")
public class SensorData {
	
	@Id
	private String id;
	private Date time;
	private String sensorName;
	private String type;
	private double value;
	private String location;
	private String version;

	public SensorData() {
		
	}
	
	public SensorData(String id, Date time, String sensorName, String type, double value, String location,
			String version) {
		super();
		this.id = id;
		this.time = time;
		this.sensorName = sensorName;
		this.type = type;
		this.value = value;
		this.location = location;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public SensorData setValue(double value) {
		this.value = value;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((sensorName == null) ? 0 : sensorName.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public  boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorData other = (SensorData) obj;

		return new EqualsBuilder()
				.append(id, other.id)
				.append(location, other.location)
				.append(sensorName, other.sensorName)
				.append(time, other.time)
				.append(type, other.type)
				.append(Double.doubleToLongBits(value), Double.doubleToLongBits(other.value))
				.append(version, other.version)
				.build();
	}

    @Override
    public String toString() {
        return "SensorData [id=" + id + ", time=" + time + ", sensorName=" + sensorName + ", type=" + type + ", value="
                + value + ", location=" + location + ", version=" + version + "]";
    }	
}
