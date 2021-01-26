package com.berkethetechnerd.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 128)
    private String description;

    public Status() {
        // Hibernate requires a no-arg constructor
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

}
