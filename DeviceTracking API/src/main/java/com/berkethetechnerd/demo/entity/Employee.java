package com.berkethetechnerd.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 128)
    private String name;

    private Date birthDate;

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToOne
    @JoinColumn(name="status_id")
    private Status status; // Connected to TABLE_STATUS

    @OneToOne
    @JoinColumn(name="position_id")
    private Position position; // Connected to TABLE_POSITION

    private Date created;

    private Date updated;

    public Employee() {
        // Hibernate requires a no-arg constructor
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getBirthDate() { return birthDate; }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

    public Date getCreated() { return created; }

    public void setCreated(Date created) { this.created = created; }

    public Date getUpdated() { return updated; }

    public void setUpdated(Date updated) { this.updated = updated; }

}
