package com.example.demo;


import com.example.demo.domain.Country;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Transactional()
@EnableJpaRepositories()
@Service
public class CountryService {

    @Autowired
    private CountryRepository repository;

    @PostConstruct
    void init() throws IOException {
        // après l'injection
        InputStream is = getClass().getResourceAsStream("/country.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new InputStreamReader(is));
        for (CSVRecord record : records) {
            String name = record.get(0);
            String code = record.get(1);
            repository.save(Country.builder().code(code).label(name).build());
        }
    }

    public List<Country> findAllCountries() {

        return repository.findAll();
    }

    public void creerCountry(Country c) {
        repository.save(c);
    }

    public Country getCountry(String id) {
        return repository.getOne(id);
    }

    public void deleteCountry(Country c) {
        repository.delete(c);
    }






}
