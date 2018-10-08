package com.example.demo;

import com.example.demo.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe de simulation de temps d'accès un peu plus long....
 */
public interface CountryRepository extends JpaRepository<Country, String>{



}
