package com.gepardec.hogarama.domain.watering;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

@Entity("watering")
public class WateringData {

    @Id
    private String id;
    private Date time;
    private String name;
    private String location;
    private Integer duration;

    public WateringData() {

    }

    public WateringData(Date time, String name, String location, Integer duration){
        this.time = time;
        this.name = name;
        this.location = location;
        this.duration = duration;
    }

    public WateringData(String id, Date time, String name, String location, Integer duration) {
        this(time, name, location,duration);
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        WateringData other = (WateringData)obj;

        return new EqualsBuilder()
                .append(id, other.id)
                .append(time, other.time)
                .append(name, other.name)
                .append(location, other.location)
                .append(duration, other.duration)
                .isEquals();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        return result;
    }
}
