package com.berkethetechnerd.devicetrackerapi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sensor")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 128)
    private String sensor_name;

    private Date start_time;

    private Date end_time;

    private Date create_time;

    public SensorData() {
        // Hibernate requires a no-arg constructor
    }

    public SensorData(Integer id, String sensor_name, Date start_time, Date end_time) {
        this.id = id;
        this.sensor_name = sensor_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.create_time = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
