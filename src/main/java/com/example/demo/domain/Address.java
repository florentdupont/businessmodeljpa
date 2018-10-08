package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Address {

    @Id
    String code;

    @ManyToMany(mappedBy = "adresses")
    List<User> users;
}
