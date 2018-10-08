package com.example.demo;

import com.example.demo.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("countries")
public class CountryController {

    @Autowired
   CountryService service;

    @PostMapping
    void put(@RequestBody Country country) {
        service.creerCountry(country);
    }

    @GetMapping("/{id}")
    void put(@PathVariable String id) {
        service.getCountry(id);
    }

    @GetMapping
    Collection<Country> get() {
        return service.findAllCountries();
    }

}
