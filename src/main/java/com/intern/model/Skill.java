package com.intern.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name= "skill")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    public Skill(String name) {
        this.name = name;
    }
}
