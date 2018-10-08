package com.example.demo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    Long id;

    @OneToMany
    List<Country> countries;

    @ManyToMany
    @JoinTable(
            name = "address_user",
            joinColumns = { @JoinColumn(name = "address_id") },
            inverseJoinColumns = { @JoinColumn(name = "users_id") }
    )
    List<Address> adresses;
}
