package com.example.infinispansample.repository;

import com.example.infinispansample.entity.Country;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

public interface CountryRepository extends CrudRepository<Country, Long> {

    @Override
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Iterable<Country> findAll();
}


