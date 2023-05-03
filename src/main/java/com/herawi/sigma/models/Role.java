package com.herawi.sigma.models;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @SequenceGenerator(sequenceName = "role_sequence", name = "role_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    private int id;
    @Column(nullable = false)
    private String name;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
